-- 통합 시드 (모든 엔티티 초기화 + 더미 재생성)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE NOTIFICATION;
TRUNCATE TABLE EMERGENCY_CALL;
TRUNCATE TABLE DOCUMENT;
TRUNCATE TABLE POST_APPLICATION;
TRUNCATE TABLE POST;
TRUNCATE TABLE SCHEDULE;
TRUNCATE TABLE MEDICATION;
TRUNCATE TABLE MEAL_PLAN;
TRUNCATE TABLE PATIENT_NOTE;
TRUNCATE TABLE PATIENT_ASSIGNMENT;
TRUNCATE TABLE GUARDIAN_PATIENT;
TRUNCATE TABLE LOCATION;
TRUNCATE TABLE PATIENT;
TRUNCATE TABLE USERS;
TRUNCATE TABLE FACILITY;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO FACILITY (facility_id, name, address, phone, created_at, updated_at) VALUES
(1, '따숨요양병원', '서울특별시 강남구 테헤란로 123', '02-1234-5678', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- login_id(직원): 2자리 사번코드 + YYMMDD + 2자리 순번 (총 10자리)
INSERT INTO USERS (user_id, facility_id, login_id, password, name, phone, email, users_role, users_status, created_at, updated_at) VALUES
(1, 1, '1126030401', '$2a$10$7EqJtq98hPqEX7fNZaFWoO5bQFQfQfQfQfQfQfQfQfQfQfQfQfQf', '김원무', '010-5000-0001', 'admin@ddasum.com', 'ADMIN', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, '2126031501', '$2a$10$7EqJtq98hPqEX7fNZaFWoO5bQFQfQfQfQfQfQfQfQfQfQfQfQfQf', '박의사', '010-5000-0002', 'doctor1@ddasum.com', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 1, '2126031502', '$2a$10$7EqJtq98hPqEX7fNZaFWoO5bQFQfQfQfQfQfQfQfQfQfQfQfQfQf', '최의사', '010-5000-0003', 'doctor2@ddasum.com', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 1, '3126031501', '$2a$10$7EqJtq98hPqEX7fNZaFWoO5bQFQfQfQfQfQfQfQfQfQfQfQfQfQf', '이요양', '010-5000-0004', 'caregiver1@ddasum.com', 'CAREGIVER', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 1, '3126031502', '$2a$10$7EqJtq98hPqEX7fNZaFWoO5bQFQfQfQfQfQfQfQfQfQfQfQfQfQf', '정요양', '010-5000-0005', 'caregiver2@ddasum.com', 'CAREGIVER', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 1, '9026030101', '$2a$10$7EqJtq98hPqEX7fNZaFWoO5bQFQfQfQfQfQfQfQfQfQfQfQfQfQf', '한보호', '010-5000-0006', 'guardian1@ddasum.com', 'GUARDIAN', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 1, '9026030102', '$2a$10$7EqJtq98hPqEX7fNZaFWoO5bQFQfQfQfQfQfQfQfQfQfQfQfQfQf', '조보호', '010-5000-0007', 'guardian2@ddasum.com', 'GUARDIAN', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO PATIENT
(patient_id, facility_id, location_id, name, gender, birth_date, address, admission_date, discharge_date, blood_type, height, weight, admission_status, diet_type, memo, status, created_at, updated_at)
VALUES
(260401001, 1, NULL, '김영희', 'FEMALE', '1952-03-14', '서울특별시 강남구 역삼동 101-11', '2026-04-01', '2099-12-31', 'A_POSITIVE', 158.20, 52.30, '입원중', '일반식', '고혈압 약 복용 중', 'STABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260403001, 1, NULL, '박순자', 'FEMALE', '1948-11-22', '서울특별시 송파구 문정동 22-8', '2026-04-03', '2099-12-31', 'O_POSITIVE', 154.50, 48.10, '입원중', '당뇨식', '혈당 모니터링 필요', 'MONITORING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260404001, 1, NULL, '장원준', 'MALE', '1951-08-16', '경기도 성남시 분당구 정자동 55-2', '2026-04-04', '2099-12-31', 'B_POSITIVE', 170.30, 66.40, '입원중', '일반식', '재활치료 병행', 'STABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260405001, 1, NULL, '정민호', 'MALE', '1945-09-10', '인천광역시 부평구 부평동 13-4', '2026-04-05', '2099-12-31', 'O_NEGATIVE', 173.80, 68.90, '입원중', '저염식', '최근 낙상 이력으로 관찰 강화', 'CRITICAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260406001, 1, NULL, '강순자', 'FEMALE', '1937-10-05', '서울특별시 강서구 화곡동 91-14', '2026-04-06', '2099-12-31', 'O_POSITIVE', 152.70, 47.10, '입원중', '연식', '보행 보조 필요', 'MONITORING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260407001, 1, NULL, '조현식', 'MALE', '1940-09-19', '경기도 부천시 상동 101-9', '2026-04-07', '2099-12-31', 'A_NEGATIVE', 167.20, 60.80, '입원중', '일반식', '신규 입원 교육 완료', 'STABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260408001, 1, NULL, '문경자', 'FEMALE', '1935-01-09', '서울특별시 중랑구 면목동 18-5', '2026-04-08', '2026-04-13', 'AB_POSITIVE', 148.60, 42.90, '퇴원완료', '연하식', '보호자 동행 퇴원', 'DISCHARGED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260409001, 1, NULL, '차성훈', 'MALE', '1949-03-27', '인천광역시 연수구 동춘동 88-4', '2026-04-09', '2099-12-31', 'B_NEGATIVE', 174.00, 69.70, '입원중', '당뇨식', '아침 어지럼증 호소', 'MONITORING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO LOCATION
(location_id, facility_id, patient_id, building, floor, room, bed, room_type, room_capacity, created_at, updated_at, is_occupied)
VALUES
(101, 1, 260401001, '본관', 3, '303', 'A', 'GENERAL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(102, 1, 260403001, '본관', 3, '303', 'B', 'GENERAL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(103, 1, 260404001, '본관', 3, '303', 'C', 'GENERAL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(104, 1, 260405001, '본관', 3, '303', 'D', 'GENERAL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(201, 1, 260406001, 'A동', 4, '402호', '1번 침상', 'GENERAL', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(202, 1, 260407001, 'A동', 4, '402호', '2번 침상', 'GENERAL', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(301, 1, 260409001, 'B동', 2, '201호', '1번 침상', 'GENERAL', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(302, 1, 260408001, 'B동', 2, '201호', '2번 침상', 'GENERAL', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

UPDATE PATIENT SET location_id = 101 WHERE patient_id = 260401001;
UPDATE PATIENT SET location_id = 102 WHERE patient_id = 260403001;
UPDATE PATIENT SET location_id = 103 WHERE patient_id = 260404001;
UPDATE PATIENT SET location_id = 104 WHERE patient_id = 260405001;
UPDATE PATIENT SET location_id = 201 WHERE patient_id = 260406001;
UPDATE PATIENT SET location_id = 202 WHERE patient_id = 260407001;
UPDATE PATIENT SET location_id = 301 WHERE patient_id = 260409001;
UPDATE PATIENT SET location_id = 302 WHERE patient_id = 260408001;

INSERT INTO GUARDIAN_PATIENT (guardian_patient_id, guardian_user_id, patient_id, relationship, is_primary, created_at) VALUES
(1, 6, 260401001, '딸', TRUE, CURRENT_TIMESTAMP),
(2, 6, 260403001, '조카', FALSE, CURRENT_TIMESTAMP),
(3, 7, 260404001, '아들', TRUE, CURRENT_TIMESTAMP),
(4, 7, 260405001, '배우자', FALSE, CURRENT_TIMESTAMP);

INSERT INTO PATIENT_ASSIGNMENT (assignment_id, patient_id, employee_user_id, start_date, end_date, created_at) VALUES
(1, 260401001, 4, '2026-04-01', '2099-12-31', CURRENT_TIMESTAMP),
(2, 260403001, 4, '2026-04-03', '2099-12-31', CURRENT_TIMESTAMP),
(3, 260404001, 5, '2026-04-04', '2099-12-31', CURRENT_TIMESTAMP),
(4, 260405001, 5, '2026-04-05', '2099-12-31', CURRENT_TIMESTAMP),
(5, 260406001, 4, '2026-04-06', '2099-12-31', CURRENT_TIMESTAMP),
(6, 260407001, 5, '2026-04-07', '2099-12-31', CURRENT_TIMESTAMP),
(7, 260408001, 4, '2026-04-08', '2026-04-13', CURRENT_TIMESTAMP),
(8, 260409001, 5, '2026-04-09', '2099-12-31', CURRENT_TIMESTAMP);

INSERT INTO PATIENT_NOTE
(note_id, patient_id, employee_user_id, note_type, title, content, period_start, period_end, created_at, updated_at)
VALUES
(1, 260401001, 4, 'CARE', '오전 활력징후', '혈압 130/80, 체온 정상, 식사량 양호', '2026-04-14 09:00:00', '2026-04-14 09:30:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 260403001, 2, 'DOCTOR', '혈당 관리 지시', '식전 혈당 측정 유지, 당뇨식 지속', '2026-04-14 10:00:00', '2026-04-14 10:20:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 260405001, 5, 'HANDOVER', '야간 인계', '낙상 위험으로 야간 순회 간격 단축', '2026-04-14 22:00:00', '2026-04-14 22:15:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 260409001, 4, 'INCIDENT', '어지럼증 보고', '기립 시 어지럼증 호소, 침상 안정 권고', '2026-04-14 08:40:00', '2026-04-14 09:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO MEDICATION
(medication_id, patient_id, docter_user_id, medication_name, dosage, frequency, route, start_date, end_date, note, created_at, updated_at)
VALUES
(1, 260401001, 2, '암로디핀정', '5mg', '1일 1회', 'PO', '2026-04-01', '2026-05-01', '아침 식후 복용', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 260403001, 2, '메트포르민', '500mg', '1일 2회', 'PO', '2026-04-03', '2026-05-03', '식후 복용', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 260405001, 3, '아스피린', '100mg', '1일 1회', 'PO', '2026-04-05', '2026-05-05', '출혈 징후 관찰', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 260409001, 3, '베타히스틴', '8mg', '1일 2회', 'PO', '2026-04-09', '2026-04-30', '어지럼증 완화 목적', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO MEAL_PLAN
(meal_plan_id, facility_id, admin_id, meal_date, meal_type, diet_type, menu, created_at)
VALUES
(1, 1, 1, '2026-04-14', 'BREAKFAST', 'GENERAL', '잡곡밥, 북어국, 계란찜, 배추김치', CURRENT_TIMESTAMP),
(2, 1, 1, '2026-04-14', 'LUNCH', 'DIABETIC', '현미밥, 닭가슴살구이, 나물무침, 저당김치', CURRENT_TIMESTAMP),
(3, 1, 1, '2026-04-14', 'DINNER', 'LOW_SODIUM', '보리밥, 된장국(저염), 두부조림, 오이무침', CURRENT_TIMESTAMP),
(4, 1, 1, '2026-04-15', 'BREAKFAST', 'SOFT', '야채죽, 스크램블에그, 바나나', CURRENT_TIMESTAMP);

INSERT INTO SCHEDULE
(schedule_id, facility_id, created_user_id, patient_id, title, content, schedule_type, scheduled_at, end_at, created_at, updated_at)
VALUES
(1, 1, 1, 260401001, '시설 전체 소독', '본관 3층 공용구역 정기 소독', 'FACILITY', '2026-04-15 14:00:00', '2026-04-15 16:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, 2, 260403001, '내과 외래 협진', '혈당 추적 검사 및 처방 조정', 'PATIENT', '2026-04-16 10:00:00', '2026-04-16 10:30:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 1, 4, 260406001, '야간 근무', 'A동 야간 순회 및 투약 확인', 'WORK_SHIFT', '2026-04-14 22:00:00', '2026-04-15 06:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 1, 5, 260409001, '개인 교육 일정', '낙상 예방 교육 자료 정리', 'PERSONAL', '2026-04-15 09:00:00', '2026-04-15 10:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO POST
(post_id, facility_id, author_user_id, post_type, title, content, post_status, start_at, end_at, capacity, current_enrolled, attachment_urls, created_at, updated_at)
VALUES
(1, 1, 1, 'NOTICE', '4월 보호자 면회 안내', '면회 가능 시간과 준수사항을 확인해주세요.', 'ACTIVE', '2026-04-14 00:00:00', '2026-04-30 23:59:59', 999, 12, 'https://example.com/notice/visit-guide.pdf', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, 4, 'BOARD', '봄맞이 산책 프로그램 후기', '참여 환자들의 컨디션이 전반적으로 양호했습니다.', 'ACTIVE', '2026-04-10 10:00:00', '2026-04-30 18:00:00', 50, 17, 'https://example.com/board/walk.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 1, 1, 'PROGRAM', '주간 인지활동 프로그램 모집', '미술치료와 음악치료를 포함한 주간 프로그램입니다.', 'ACTIVE', '2026-04-14 09:00:00', '2026-04-20 18:00:00', 10, 4, 'https://example.com/program/cognitive.pdf', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO POST_APPLICATION
(application_id, post_id, guardian_user_id, patient_id, postapplication_status, applied_at, processed_by, processed_at, memo)
VALUES
(1, 3, 6, 260401001, 'WAITING', '2026-04-14 11:00:00', 1, '2026-04-14 11:00:00', '오전반 희망'),
(2, 3, 7, 260404001, 'COMPLETED', '2026-04-14 11:10:00', 1, '2026-04-14 12:00:00', '참여 확정'),
(3, 3, 6, 260403001, 'FULL', '2026-04-14 11:20:00', 1, '2026-04-14 12:05:00', '대기자 등록');

INSERT INTO DOCUMENT
(document_id, patient_id, facility_id, document_type, title, content, requester_user_id, approver_user_id, document_status, file_urls, requested_at, approved_at, issued_at, created_at, updated_at)
VALUES
(1, 260401001, 1, 'CERTIFICATE', '입원확인서 발급 요청', '보호자 제출용 입원확인서 발급 요청 건', 6, 1, 'ISSUED', 'https://example.com/docs/cert-260401001.pdf', '2026-04-12 09:00:00', '2026-04-12 12:00:00', '2026-04-12 13:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 260403001, 1, 'PAYMENT', '4월 수납 고지서', '4월 간병 및 치료비 고지 문서', 6, 1, 'PAID', 'https://example.com/docs/pay-260403001.pdf', '2026-04-13 09:00:00', '2026-04-13 10:00:00', '2026-04-13 10:30:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 260409001, 1, 'VISIT_REQUEST', '특별 면회 신청', '주말 특별 면회 신청서', 7, 1, 'PENDING_APPROVAL', 'https://example.com/docs/visit-260409001.pdf', '2026-04-14 08:00:00', '2026-04-14 08:00:00', '2026-04-14 08:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO EMERGENCY_CALL
(emergency_call_id, patient_id, requested_by, responded_by, reason, called_at, responded_at)
VALUES
(1, 260405001, 5, 2, '낙상 위험 징후 및 어지럼 호소', '2026-04-14 03:10:00', '2026-04-14 03:17:00'),
(2, 260409001, 4, 3, '혈압 급변 및 어지럼증 재발', '2026-04-14 07:45:00', '2026-04-14 07:53:00');

INSERT INTO NOTIFICATION
(notification_id, receiver_user_id, title, content, notification_type, ref_type, ref_id, is_read, created_at, read_at)
VALUES
(1, 1, '새 문서 승인 요청', '특별 면회 신청 문서 승인 대기 중입니다.', 'DOCUMENT', 'DOCUMENT', 3, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 4, '응급 호출 처리 완료', '260405001 환자 응급 호출이 처리되었습니다.', 'EMERGENCY', 'EMERGENCY_CALL', 1, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 6, '프로그램 신청 상태 변경', '주간 인지활동 프로그램 신청이 대기 상태입니다.', 'PROGRAM', 'POST_APPLICATION', 1, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 7, '프로그램 신청 확정', '주간 인지활동 프로그램 신청이 확정되었습니다.', 'PROGRAM', 'POST_APPLICATION', 2, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
