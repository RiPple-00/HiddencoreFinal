-- 통합 시드: (1) 본관 303호 A~D 침상  (2) 기존 다인실/타 병동 더미
-- MySQL 직접 실행용. JPA 스키마: FACILITY / PATIENT / LOCATION
--
-- 환자 ID 규칙 (9자리 BIGINT): YYMMDD + 당일 순번 001~999
--   예: 260410001 = 2026-04-10 입원 등록 첫 번째
-- 같은 입원일(admission_date)이 여러 명이면 001, 002, 003 … 부여
--
-- gender: MALE / FEMALE / OTHER (선택, NULL 가능 — 한글 저장 불가)
-- 병실 조회 API: GET /api/rooms/{room}/beds  → room 컬럼과 동일해야 함
--   · 303호 병실: room = '303'  → 프론트 /bedroompage/303
--   · 타 병동: room = '301호' 등 기존 유지
--
-- 실행 전 주의: 기존 데이터 삭제(TRUNCATE). 운영 DB에서는 백업 후 사용.

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE LOCATION;
TRUNCATE TABLE PATIENT;
TRUNCATE TABLE FACILITY;
SET FOREIGN_KEY_CHECKS = 1;

-- ---------------------------------------------------------------------------
-- FACILITY
-- ---------------------------------------------------------------------------
INSERT INTO FACILITY
(facility_id, name, address, phone, created_at, updated_at)
VALUES
(1, '따숨요양병원', '서울특별시 강남구 테헤란로 123', '02-1234-5678', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ---------------------------------------------------------------------------
-- PATIENT (먼저 삽입 — location_id 는 아래 UPDATE 로 연결)
-- ID | 이름     | 입원일     | 부여 ID
-- 303 본관
--    김영희     2026-04-01   260401001
--    박순자     2026-04-03   260403001, 260403002(이철수) … 아래 표 참고
-- ---------------------------------------------------------------------------
-- 입원일별 일련번호는 아래 순서(같은 날짜: 이름 가나다 순)로 부여했습니다.

INSERT INTO PATIENT
(patient_id, facility_id, location_id, name, gender, birth_date, address, admission_date, discharge_date, blood_type, height, weight, admission_status, diet_type, memo, status, created_at, updated_at)
VALUES
-- === 본관 303호 (기존 DB 형식 유지: 성별 한글 가능) ===
(260401001, 1, NULL, '김영희', '여성', DATE '1952-03-14', NULL, DATE '2026-04-01', NULL, 'A_POSITIVE', 158.20, 52.30, '입원중', '일반식', '고혈압 약 복용 중', 'STABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260403001, 1, NULL, '박순자', '여성', DATE '1948-11-22', NULL, DATE '2026-04-03', NULL, 'O_POSITIVE', 154.50, 48.10, '입원중', '당뇨식', '혈당 모니터링 필요', 'MONITORING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260404001, 1, NULL, '장원준', '남성', DATE '2001-08-16', NULL, DATE '2026-04-04', NULL, 'A_POSITIVE', 190.50, 60.50, '입원중', '당뇨식', '혈당 모니터링 필요', 'MONITORING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- === 기존 대량 더미 (입원일 기준 ID 재부여) ===
(260328001, 1, NULL, '김영수', 'MALE', DATE '1942-03-15', '서울특별시 강남구 역삼동 101-11', DATE '2026-03-28', NULL, 'A_POSITIVE', 168.50, 63.20, '입원중', '당뇨식', '보행 보조 필요', 'STABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260401002, 1, NULL, '박정희', 'FEMALE', DATE '1939-07-22', '서울특별시 송파구 문정동 22-8', DATE '2026-04-01', NULL, 'O_POSITIVE', 154.20, 49.80, '입원중', '저염식', '야간 화장실 이동 도움 필요', 'MONITORING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260403002, 1, NULL, '이철수', 'MALE', DATE '1947-11-03', '경기도 성남시 분당구 정자동 55-2', DATE '2026-04-03', NULL, 'B_POSITIVE', 171.00, 70.40, '입원중', '일반식', '혈압 변동 관찰', 'STABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260320001, 1, NULL, '최은숙', 'FEMALE', DATE '1936-12-29', '서울특별시 노원구 상계동 88-19', DATE '2026-03-20', NULL, 'AB_POSITIVE', 150.30, 45.70, '입원중', '연하식', '삼킴 주의', 'MONITORING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260405001, 1, NULL, '정민호', 'MALE', DATE '1945-09-10', '인천광역시 부평구 부평동 13-4', DATE '2026-04-05', NULL, 'O_NEGATIVE', 173.80, 68.90, '입원중', '일반식', '최근 낙상 이력 있음', 'CRITICAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260330001, 1, NULL, '한수진', 'FEMALE', DATE '1949-01-17', '서울특별시 은평구 불광동 7-31', DATE '2026-03-30', NULL, 'A_NEGATIVE', 158.00, 52.30, '입원중', '죽식', '식사량 적음', 'MONITORING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260318001, 1, NULL, '오상훈', 'MALE', DATE '1941-06-08', '경기도 고양시 일산동구 마두동 31-9', DATE '2026-03-18', NULL, 'B_NEGATIVE', 166.40, 59.20, '입원중', '저염식', '혈당 체크 필요', 'STABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260312001, 1, NULL, '윤미자', 'FEMALE', DATE '1938-04-14', '서울특별시 도봉구 창동 44-1', DATE '2026-03-12', DATE '2026-04-10', 'AB_NEGATIVE', 149.90, 43.60, '퇴원완료', '일반식', '가정 복귀', 'DISCHARGED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260402001, 1, NULL, '서동혁', 'MALE', DATE '1950-08-26', '경기도 수원시 영통구 망포동 20-6', DATE '2026-04-02', NULL, 'A_POSITIVE', 175.10, 72.50, '입원중', '당뇨식', '인슐린 투약 중', 'MONITORING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260406001, 1, NULL, '강순자', 'FEMALE', DATE '1937-10-05', '서울특별시 강서구 화곡동 91-14', DATE '2026-04-06', NULL, 'O_POSITIVE', 152.70, 47.10, '접수완료', '죽식', '병실 배정 대기', 'STABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260327001, 1, NULL, '배재호', 'MALE', DATE '1943-02-11', '인천광역시 남동구 구월동 61-7', DATE '2026-03-27', NULL, 'B_POSITIVE', 169.30, 64.00, '입원중', '일반식', '재활치료 병행', 'STABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260322001, 1, NULL, '신명숙', 'FEMALE', DATE '1946-05-30', '서울특별시 마포구 성산동 12-3', DATE '2026-03-22', NULL, 'A_POSITIVE', 156.40, 50.20, '퇴원예정', '저염식', '4월 중 보호자 동행 퇴원 예정', 'DISCHARGE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260407001, 1, NULL, '조현식', 'MALE', DATE '1940-09-19', '경기도 부천시 상동 101-9', DATE '2026-04-07', NULL, 'O_POSITIVE', 167.20, 60.80, '접수완료', '일반식', '신규 입원, 병실 미배정', 'STABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260325001, 1, NULL, '문경자', 'FEMALE', DATE '1935-01-09', '서울특별시 중랑구 면목동 18-5', DATE '2026-03-25', NULL, 'AB_POSITIVE', 148.60, 42.90, '입원중', '연하식', '욕창 예방 체위변경 필요', 'CRITICAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260329001, 1, NULL, '임도윤', 'MALE', DATE '1948-12-01', '서울특별시 동작구 상도동 27-2', DATE '2026-03-29', NULL, 'A_NEGATIVE', 172.40, 67.30, '입원중', '일반식', '기력 저하 관찰', 'STABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260404002, 1, NULL, '백현주', 'FEMALE', DATE '1944-06-16', '경기도 안양시 동안구 평촌동 39-12', DATE '2026-04-04', NULL, 'B_NEGATIVE', 157.80, 53.60, '입원중', '당뇨식', '아침 어지럼증 호소', 'MONITORING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260310001, 1, NULL, '유태식', 'MALE', DATE '1934-11-21', '서울특별시 서대문구 홍제동 77-1', DATE '2026-03-10', DATE '2026-04-08', 'O_NEGATIVE', 165.00, 58.00, '퇴원완료', '일반식', '요양 종료 후 전원', 'DISCHARGED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260401003, 1, NULL, '노미영', 'FEMALE', DATE '1941-08-02', '경기도 의정부시 금오동 11-15', DATE '2026-04-01', NULL, 'AB_NEGATIVE', 153.20, 46.80, '입원중', '죽식', '저체중으로 영양관리 필요', 'MONITORING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260409001, 1, NULL, '차성훈', 'MALE', DATE '1949-03-27', '인천광역시 연수구 동춘동 88-4', DATE '2026-04-09', NULL, 'B_POSITIVE', 174.00, 69.70, '접수완료', '일반식', '검사 후 병실 배정 예정', 'STABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(260315001, 1, NULL, '송혜자', 'FEMALE', DATE '1933-07-13', '서울특별시 종로구 창신동 14-8', DATE '2026-03-15', NULL, 'O_POSITIVE', 151.00, 44.50, '입원중', '연하식', '낙상 고위험군', 'CRITICAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ---------------------------------------------------------------------------
-- LOCATION (patient_id FK — 위 PATIENT id 와 일치)
-- location_id: 101~104 = 본관 303호 A~D, 1~15 = 타 병동 침상
-- ---------------------------------------------------------------------------
INSERT INTO LOCATION
(location_id, facility_id, patient_id, building, floor, room, bed, room_type, room_capacity, created_at, updated_at, is_occupied)
VALUES
-- 본관 303 (room 컬럼 '303' — API·프론트에서 동일 문자열 사용)
(101, 1, 260401001, '본관', 3, '303', 'A', 'GENERAL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(102, 1, 260403001, '본관', 3, '303', 'B', 'GENERAL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(103, 1, 260404001, '본관', 3, '303', 'C', 'GENERAL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(104, 1, NULL,      '본관', 3, '303', 'D', 'GENERAL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),

-- A동·B동·C동 더미 병상 (기존 seed 와 동일 연결)
(1, 1, 260328001, 'A동', 3, '301호', '1번 침상', 'GENERAL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(2, 1, 260401002, 'A동', 3, '301호', '2번 침상', 'GENERAL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(3, 1, 260403002, 'A동', 3, '302호', '1번 침상', 'GENERAL', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(4, 1, 260320001, 'A동', 3, '302호', '2번 침상', 'GENERAL', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(5, 1, 260405001, 'A동', 4, '401호', '1번 침상', 'GENERAL', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(6, 1, 260330001, 'A동', 4, '402호', '1번 침상', 'GENERAL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(7, 1, 260318001, 'A동', 4, '402호', '2번 침상', 'GENERAL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(8, 1, 260402001, 'B동', 2, '201호', '1번 침상', 'GENERAL', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(9, 1, 260327001, 'B동', 2, '201호', '2번 침상', 'GENERAL', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(10, 1, 260322001, 'B동', 2, '202호', '1번 침상', 'GENERAL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(11, 1, 260325001, 'B동', 2, '202호', '2번 침상', 'GENERAL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(12, 1, 260329001, 'B동', 3, '301호', '1번 침상', 'GENERAL', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(13, 1, 260404002, 'B동', 3, '301호', '2번 침상', 'GENERAL', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(14, 1, 260310001, 'C동', 1, '101호', '1번 침상', 'GENERAL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(15, 1, 260315001, 'C동', 1, '101호', '2번 침상', 'GENERAL', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

-- ---------------------------------------------------------------------------
-- PATIENT.location_id 양방향 맞춤
-- ---------------------------------------------------------------------------
UPDATE PATIENT SET location_id = 101 WHERE patient_id = 260401001;
UPDATE PATIENT SET location_id = 102 WHERE patient_id = 260403001;
UPDATE PATIENT SET location_id = 103 WHERE patient_id = 260404001;

UPDATE PATIENT SET location_id = 1  WHERE patient_id = 260328001;
UPDATE PATIENT SET location_id = 2  WHERE patient_id = 260401002;
UPDATE PATIENT SET location_id = 3  WHERE patient_id = 260403002;
UPDATE PATIENT SET location_id = 4  WHERE patient_id = 260320001;
UPDATE PATIENT SET location_id = 5  WHERE patient_id = 260405001;
UPDATE PATIENT SET location_id = 6  WHERE patient_id = 260330001;
UPDATE PATIENT SET location_id = 7  WHERE patient_id = 260318001;
UPDATE PATIENT SET location_id = 8  WHERE patient_id = 260402001;
UPDATE PATIENT SET location_id = 9  WHERE patient_id = 260327001;
UPDATE PATIENT SET location_id = 10 WHERE patient_id = 260322001;
UPDATE PATIENT SET location_id = 11 WHERE patient_id = 260325001;
UPDATE PATIENT SET location_id = 12 WHERE patient_id = 260329001;
UPDATE PATIENT SET location_id = 13 WHERE patient_id = 260404002;
UPDATE PATIENT SET location_id = 14 WHERE patient_id = 260310001;
UPDATE PATIENT SET location_id = 15 WHERE patient_id = 260315001;

-- 병실 미배정: 윤미자, 강순자, 조현식, 노미영, 차성훈 (location_id 유지 NULL)
-- 260312001 윤미자, 260406001 강순자, 260407001 조현식, 260401003 노미영, 260409001 차성훈
