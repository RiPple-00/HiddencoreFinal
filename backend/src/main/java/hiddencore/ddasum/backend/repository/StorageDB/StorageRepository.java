package hiddencore.ddasum.backend.repository.StorageDB;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import hiddencore.ddasum.backend.domain.Document;
import hiddencore.ddasum.backend.domain.Patient;
import hiddencore.ddasum.backend.domain.Users;
import hiddencore.ddasum.backend.domain.Users.UsersStatus;
import hiddencore.ddasum.backend.web.dto.StorageDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StorageRepository {

	@PersistenceContext
	private final EntityManager entityManager;

	public List<Patient> findPatients(StorageDto.PatientSearchRequest request) {
		StringBuilder jpql = new StringBuilder(
				"select p from Patient p left join fetch p.locationId l where p.facilityId.facilityId = :facilityId");

		if (StringUtils.hasText(request.getKeyword())) {
			jpql.append(" and lower(p.name) like :keyword");
		}
		if (StringUtils.hasText(request.getAdmissionStatus())) {
			jpql.append(" and p.admissionStatus = :admissionStatus");
		}
		if (request.getAdmissionStartDate() != null) {
			jpql.append(" and p.admissionDate >= :admissionStartDate");
		}
		if (request.getAdmissionEndDate() != null) {
			jpql.append(" and p.admissionDate <= :admissionEndDate");
		}

		jpql.append(" order by p.admissionDate desc, p.patientId desc");

		TypedQuery<Patient> query = entityManager.createQuery(jpql.toString(), Patient.class);
		bindPatientSearchParameters(query, request);
		applyPaging(query, request.getPage(), request.getSize());
		return query.getResultList();
	}

	public long countPatients(StorageDto.PatientSearchRequest request) {
		StringBuilder jpql = new StringBuilder(
				"select count(p) from Patient p where p.facilityId.facilityId = :facilityId");

		if (StringUtils.hasText(request.getKeyword())) {
			jpql.append(" and lower(p.name) like :keyword");
		}
		if (StringUtils.hasText(request.getAdmissionStatus())) {
			jpql.append(" and p.admissionStatus = :admissionStatus");
		}
		if (request.getAdmissionStartDate() != null) {
			jpql.append(" and p.admissionDate >= :admissionStartDate");
		}
		if (request.getAdmissionEndDate() != null) {
			jpql.append(" and p.admissionDate <= :admissionEndDate");
		}

		TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);
		bindPatientSearchParameters(query, request);
		return query.getSingleResult();
	}

	public List<Document> findDocuments(StorageDto.InvoiceSearchRequest request) {
		StringBuilder jpql = new StringBuilder(
				"select d from Document d left join fetch d.patientId p where 1=1");

		appendDocumentConditions(jpql, request);
		jpql.append(" order by d.createdAt desc, d.documentId desc");

		TypedQuery<Document> query = entityManager.createQuery(jpql.toString(), Document.class);
		bindDocumentSearchParameters(query, request);
		applyPaging(query, request.getPage(), request.getSize());
		return query.getResultList();
	}

	public long countDocuments(StorageDto.InvoiceSearchRequest request) {
		StringBuilder jpql = new StringBuilder("select count(d) from Document d where 1=1");

		appendDocumentConditions(jpql, request);

		TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);
		bindDocumentSearchParameters(query, request);
		return query.getSingleResult();
	}

	public Optional<Document> findDocument(Long documentId) {
		return Optional.ofNullable(entityManager.find(Document.class, documentId));
	}

	public Optional<Users> findStaff(Long staffUserId, Long facilityId) {
		TypedQuery<Users> query = entityManager.createQuery(
				"select u from Users u where u.userId = :staffUserId and u.facilityId.facilityId = :facilityId and u.status = :status",
				Users.class);
		query.setParameter("staffUserId", staffUserId);
		query.setParameter("facilityId", facilityId);
		query.setParameter("status", UsersStatus.ACTIVE);

		List<Users> users = query.setMaxResults(1).getResultList();
		return users.stream().findFirst();
	}

	public Document saveDocument(Document document) {
		if (document.getDocumentId() == null) {
			entityManager.persist(document);
			return document;
		}
		return entityManager.merge(document);
	}

	private void appendDocumentConditions(StringBuilder jpql, StorageDto.InvoiceSearchRequest request) {
		if (request.getFacilityId() != null) {
			jpql.append(" and d.facilityId.facilityId = :facilityId");
		}
		if (request.getPatientId() != null) {
			jpql.append(" and d.patientId.patientId = :patientId");
		}
		if (request.getDocumentId() != null) {
			jpql.append(" and d.documentId = :documentId");
		}
		if (request.getDocumentStatus() != null) {
			jpql.append(" and d.status = :documentStatus");
		}
		if (request.getDocumentType() != null) {
			jpql.append(" and d.type = :documentType");
		}
		if (request.getBillingStartDate() != null) {
			jpql.append(" and d.createdAt >= :billingStartDateTime");
		}
		if (request.getBillingEndDate() != null) {
			jpql.append(" and d.createdAt < :billingEndDateTime");
		}
	}

	private void bindPatientSearchParameters(TypedQuery<?> query, StorageDto.PatientSearchRequest request) {
		query.setParameter("facilityId", request.getFacilityId());

		if (StringUtils.hasText(request.getKeyword())) {
			query.setParameter("keyword", likeValue(request.getKeyword()));
		}
		if (StringUtils.hasText(request.getAdmissionStatus())) {
			query.setParameter("admissionStatus", request.getAdmissionStatus());
		}
		if (request.getAdmissionStartDate() != null) {
			query.setParameter("admissionStartDate", request.getAdmissionStartDate());
		}
		if (request.getAdmissionEndDate() != null) {
			query.setParameter("admissionEndDate", request.getAdmissionEndDate());
		}
	}

	private void bindDocumentSearchParameters(TypedQuery<?> query, StorageDto.InvoiceSearchRequest request) {
		if (request.getFacilityId() != null) {
			query.setParameter("facilityId", request.getFacilityId());
		}
		if (request.getPatientId() != null) {
			query.setParameter("patientId", request.getPatientId());
		}
		if (request.getDocumentId() != null) {
			query.setParameter("documentId", request.getDocumentId());
		}
		if (request.getDocumentStatus() != null) {
			query.setParameter("documentStatus", request.getDocumentStatus());
		}
		if (request.getDocumentType() != null) {
			query.setParameter("documentType", request.getDocumentType());
		}
		if (request.getBillingStartDate() != null) {
			query.setParameter("billingStartDateTime", atStartOfDay(request.getBillingStartDate()));
		}
		if (request.getBillingEndDate() != null) {
			query.setParameter("billingEndDateTime", atStartOfNextDay(request.getBillingEndDate()));
		}
	}

	private void applyPaging(TypedQuery<?> query, Integer page, Integer size) {
		int safePage = page != null && page >= 0 ? page : 0;
		int safeSize = size != null && size > 0 ? size : 20;
		query.setFirstResult(safePage * safeSize);
		query.setMaxResults(safeSize);
	}

	private String likeValue(String keyword) {
		return "%" + keyword.trim().toLowerCase() + "%";
	}

	private LocalDateTime atStartOfDay(LocalDate date) {
		return date.atStartOfDay();
	}

	private LocalDateTime atStartOfNextDay(LocalDate date) {
		return date.plusDays(1).atStartOfDay();
	}

}

