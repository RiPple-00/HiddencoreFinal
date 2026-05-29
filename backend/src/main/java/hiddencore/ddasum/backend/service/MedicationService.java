package hiddencore.ddasum.backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import hiddencore.ddasum.backend.domain.Medication;
import hiddencore.ddasum.backend.domain.Patient;
import hiddencore.ddasum.backend.domain.Users;
import hiddencore.ddasum.backend.repository.MedicationRepository;
import hiddencore.ddasum.backend.repository.PatientRepository;
import hiddencore.ddasum.backend.repository.UsersRepository;
import hiddencore.ddasum.backend.web.dto.guardian.MedicationDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final PatientRepository patientRepository;
    private final UsersRepository usersRepository;

    private final MedicationQrParser medicationQrParser;
    private final MedicationApiClient medicationApiClient;
    private final ObjectMapper objectMapper;

    private final EdbQrDecoder edbQrDecoder;
    private final HiraMedicationApiClient hiraMedicationApiClient;
    private final MfdsEasyDrugApiClient mfdsEasyDrugApiClient;
    private final MfdsDrugPermitApiClient mfdsDrugPermitApiClient;
    private final MfdsDurApiClient mfdsDurApiClient;

    @Transactional
    public MedicationDto.Response saveMedicationFromQr(MedicationDto.ScanRequest request) {

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("환자를 찾을 수 없습니다. id=" + request.getPatientId()));

        Users guardian = null;

        if (request.getGuardianId() != null) {
            guardian = usersRepository.findById(request.getGuardianId())
                    .orElseThrow(() -> new IllegalArgumentException("보호자를 찾을 수 없습니다. id=" + request.getGuardianId()));
        }

        if (isEdbQr(request.getQrRawData())) {
            return saveMedicationFromEdbQr(request, patient, guardian);
        }

        MedicationDto.ParsedPrescription parsedPrescription = medicationQrParser.parse(request.getQrRawData());

        if (parsedPrescription.getMedicines() == null || parsedPrescription.getMedicines().isEmpty()) {
            throw new IllegalArgumentException("QR 데이터에 약 정보가 없습니다.");
        }

        List<MedicationDto.MedicineApiInfo> medicineApiInfos = new ArrayList<>();

        for (MedicationDto.ParsedMedicine parsedMedicine : parsedPrescription.getMedicines()) {

            Optional<MedicationApiClient.DrugInfo> drugInfoOptional = medicationApiClient
                    .searchByMedicineName(parsedMedicine.getMedicineName());

            MedicationDto.MedicineApiInfo.MedicineApiInfoBuilder medicineBuilder = MedicationDto.MedicineApiInfo
                    .builder()
                    .medicineName(parsedMedicine.getMedicineName())
                    .dose(parsedMedicine.getDose())
                    .timesPerDay(parsedMedicine.getTimesPerDay())
                    .days(parsedMedicine.getDays());

            if (drugInfoOptional.isPresent()) {
                MedicationApiClient.DrugInfo drugInfo = drugInfoOptional.get();

                medicineBuilder
                        .itemSeq(drugInfo.getItemSeq())
                        .entpName(drugInfo.getEntpName())
                        .efficacy(drugInfo.getEfficacy())
                        .useMethod(drugInfo.getUseMethod())
                        .cautionWarning(drugInfo.getCautionWarning())
                        .caution(drugInfo.getCaution())
                        .interaction(drugInfo.getInteraction())
                        .sideEffect(drugInfo.getSideEffect())
                        .storageMethod(drugInfo.getStorageMethod());
            }

            medicineApiInfos.add(medicineBuilder.build());
        }

        String medicineData;

        try {
            medicineData = objectMapper.writeValueAsString(medicineApiInfos);
        } catch (Exception e) {
            throw new IllegalArgumentException("약 정보를 JSON으로 변환하는 중 오류가 발생했습니다.");
        }

        String medicineSummary = createMedicineSummary(medicineApiInfos);

        Medication medication = Medication.builder()
                .patientId(patient)
                .guardianId(guardian)
                .prescriptionDate(parsedPrescription.getPrescriptionDate())
                .qrRawData(request.getQrRawData())
                .medicineData(medicineData)
                .medicineSummary(medicineSummary)
                .build();

        Medication savedMedication = medicationRepository.save(medication);

        return MedicationDto.Response.builder()
                .medicationId(savedMedication.getMedicationId())
                .patientId(request.getPatientId())
                .guardianId(request.getGuardianId())
                .prescriptionDate(savedMedication.getPrescriptionDate())
                .medicineSummary(savedMedication.getMedicineSummary())
                .medicineData(savedMedication.getMedicineData())
                .build();
    }

    private MedicationDto.Response saveMedicationFromEdbQr(
            MedicationDto.ScanRequest request,
            Patient patient,
            Users guardian) {
        LocalDate prescriptionDate = edbQrDecoder.extractPrescriptionDateFromQr(request.getQrRawData());

        List<String> itemSeqs = edbQrDecoder.extractMedicationCodesFromQr(request.getQrRawData());

        if (itemSeqs == null || itemSeqs.isEmpty()) {
            throw new IllegalArgumentException("EDB QR에서 약 코드를 추출할 수 없습니다.");
        }

        List<Map<String, Object>> medicineInfos = new ArrayList<>();

        for (String itemSeq : itemSeqs) {
            Map<String, Object> row = new LinkedHashMap<>();

            row.put("source", "EDB_QR");
            row.put("itemSeq", itemSeq);

            var hiraDrugInfoOptional = hiraMedicationApiClient.searchByMdsCd(itemSeq);

            if (hiraDrugInfoOptional.isPresent()) {
                HiraMedicationApiClient.HiraDrugInfo drugInfo = hiraDrugInfoOptional.get();

                row.put("apiFound", true);
                row.put("medicineName", drugInfo.getItemName());
                row.put("itemName", drugInfo.getItemName());
                row.put("manufacturerName", drugInfo.getManufacturerName());
                row.put("unit", drugInfo.getUnit());
                row.put("payType", drugInfo.getPayType());
                row.put("route", drugInfo.getRoute());
                row.put("classNo", drugInfo.getClassNo());
                row.put("mainIngredientCode", drugInfo.getMainIngredientCode());
                row.put("applyStartDate", drugInfo.getApplyStartDate());
                row.put("applyEndDate", drugInfo.getApplyEndDate());
                row.put("maxPrice", drugInfo.getMaxPrice());
                row.put("specialGeneralType", drugInfo.getSpecialGeneralType());
                row.put("substitutionType", drugInfo.getSubstitutionType());
            } else {
                Map<String, Object> permitInfoByEdi = mfdsDrugPermitApiClient.getPermitInfoByEdiCode(itemSeq);

                row.putAll(permitInfoByEdi);

                if (Boolean.TRUE.equals(permitInfoByEdi.get("permitInfoFound"))) {
                    row.put("apiFound", true);
                    row.put("apiSource", "MFDS_PERMIT_BY_EDI");

                    Object permitItemName = permitInfoByEdi.get("permitItemName");
                    Object permitEntpName = permitInfoByEdi.get("permitEntpName");

                    row.put("medicineName", permitItemName == null ? null : permitItemName.toString());
                    row.put("itemName", permitItemName == null ? null : permitItemName.toString());
                    row.put("manufacturerName", permitEntpName == null ? "" : permitEntpName.toString());

                } else {
                    row.put("apiFound", false);
                    row.put("medicineName", null);
                    row.put("itemName", null);
                }
            }

            String medicineNameForSearch = resolveMedicineName(row);
            String manufacturerName = resolveManufacturerName(row);

            Map<String, Object> easyDrugInfo = mfdsEasyDrugApiClient.getEasyDrugInfo(medicineNameForSearch);
            row.putAll(easyDrugInfo);

            if (!Boolean.TRUE.equals(easyDrugInfo.get("drugInfoFound"))
                    && !Boolean.TRUE.equals(row.get("permitInfoFound"))) {
                Map<String, Object> permitInfo = mfdsDrugPermitApiClient.getPermitInfo(
                        medicineNameForSearch,
                        manufacturerName);
                row.putAll(permitInfo);
            }

            String permitItemSeq = "";

            Object permitItemSeqValue = row.get("permitItemSeq");
            if (permitItemSeqValue != null) {
                permitItemSeq = permitItemSeqValue.toString();
            }

            Map<String, Object> durInfo = mfdsDurApiClient.getDurInfo(medicineNameForSearch, permitItemSeq);

            row.putAll(durInfo);

            medicineInfos.add(row);
        }

        String medicineData;

        try {
            medicineData = objectMapper.writeValueAsString(medicineInfos);
        } catch (Exception e) {
            throw new IllegalArgumentException("EDB 약 정보를 JSON으로 변환하는 중 오류가 발생했습니다.");
        }

        String medicineSummary = createEdbMedicineSummary(medicineInfos);

        Medication medication = Medication.builder()
                .patientId(patient)
                .guardianId(guardian)
                .prescriptionDate(prescriptionDate)
                .qrRawData(request.getQrRawData())
                .medicineData(medicineData)
                .medicineSummary(medicineSummary)
                .build();

        Medication savedMedication = medicationRepository.save(medication);

        return MedicationDto.Response.builder()
                .medicationId(savedMedication.getMedicationId())
                .patientId(request.getPatientId())
                .guardianId(request.getGuardianId())
                .prescriptionDate(savedMedication.getPrescriptionDate())
                .medicineSummary(savedMedication.getMedicineSummary())
                .medicineData(savedMedication.getMedicineData())
                .build();
    }

    private boolean isEdbQr(String qrRawData) {
        if (qrRawData == null) {
            return false;
        }

        String normalized = qrRawData
                .replace("%2B", "+")
                .replace("%2b", "+")
                .replace(" ", "+");

        return normalized.contains("edb.co.kr/m.htm")
                && normalized.contains("MLK");
    }

    private String createEdbMedicineSummary(List<Map<String, Object>> medicines) {
        List<String> medicineNames = medicines.stream()
                .map(row -> {
                    Object name = row.get("medicineName");
                    Object itemSeq = row.get("itemSeq");

                    if (name != null && !name.toString().isBlank()) {
                        return name.toString();
                    }

                    return itemSeq == null ? "알 수 없는 약" : itemSeq.toString();
                })
                .toList();

        if (medicineNames.size() <= 3) {
            return String.join(", ", medicineNames);
        }

        return String.join(", ", medicineNames.subList(0, 3))
                + " 외 "
                + (medicineNames.size() - 3)
                + "개";
    }

    private String createMedicineSummary(List<MedicationDto.MedicineApiInfo> medicines) {
        List<String> medicineNames = medicines.stream()
                .map(MedicationDto.MedicineApiInfo::getMedicineName)
                .toList();

        if (medicineNames.size() <= 3) {
            return String.join(", ", medicineNames);
        }

        return String.join(", ", medicineNames.subList(0, 3))
                + " 외 "
                + (medicineNames.size() - 3)
                + "개";
    }

    private String resolveMedicineName(Map<String, Object> row) {
        if (row == null) {
            return "";
        }

        Object medicineName = row.get("medicineName");

        if (medicineName == null || medicineName.toString().isBlank()) {
            medicineName = row.get("itemName");
        }

        if (medicineName == null) {
            return "";
        }

        return medicineName.toString();
    }

    private String resolveManufacturerName(Map<String, Object> row) {
        if (row == null) {
            return "";
        }

        Object manufacturerName = row.get("manufacturerName");

        if (manufacturerName == null) {
            return "";
        }

        return manufacturerName.toString();
    }

}