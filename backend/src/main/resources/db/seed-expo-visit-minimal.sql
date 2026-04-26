-- =============================================================================
-- Expo 면회 앱 연동용 최소 더미 (앱 기본 patientId = 260401001)
-- - FACILITY(1), 보호자 USERS(6), 환자 PATIENT(260401001), GUARDIAN_PATIENT
-- - INSERT IGNORE: 이미 데이터가 있으면 PK/unique 충돌 행만 건너뜀
-- 수동 실행 예: mysql -u root -p ddasum < backend/src/main/resources/db/seed-expo-visit-minimal.sql
-- =============================================================================

INSERT IGNORE INTO FACILITY (facility_id, name, address, phone, created_at, updated_at) VALUES
(1, '따숨요양병원', '서울특별시 강남구 테헤란로 123', '02-1234-5678', NOW(6), NOW(6));

INSERT IGNORE INTO USERS (user_id, facility_id, login_id, password, name, phone, email, users_role, users_status, created_at, updated_at) VALUES
(6, 1, '9026030101', '$2a$10$7EqJtq98hPqEX7fNZaFWoO5bQFQfQfQfQfQfQfQfQfQfQfQfQfQf', '한보호', '010-5000-0006', 'guardian1@ddasum.com', 'GUARDIAN', 'ACTIVE', NOW(6), NOW(6));

INSERT IGNORE INTO PATIENT
(patient_id, facility_id, location_id, name, gender, birth_date, address, admission_date, discharge_date, blood_type, height, weight, admission_status, diet_type, memo, status, created_at, updated_at)
VALUES
(260401001, 1, NULL, '김영희', 'FEMALE', '1952-03-14', '서울특별시 강남구 역삼동 101-11', '2026-04-01', '2099-12-31', 'A_POSITIVE', 158.20, 52.30, '입원중', '일반식', 'Expo 면회 테스트 더미', 'STABLE', NOW(6), NOW(6));

INSERT IGNORE INTO GUARDIAN_PATIENT (guardian_patient_id, guardian_user_id, patient_id, relationship, is_primary, created_at) VALUES
(990001, 6, 260401001, '딸', TRUE, NOW(6));
