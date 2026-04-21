-- A동 1층·2층 병실(LOCATION) 더미만 — 환자는 넣지 않음
-- LOCATION.facility_id = 1 을 쓰므로, 아래 시설 행이 있어야 FK가 성공합니다.
-- 이미 facility_id=1 이 있으면 INSERT 는 건너뜁니다(중복 시 변경 없음).
--
INSERT INTO FACILITY (facility_id, name, address, phone, created_at, updated_at)
VALUES (1, '따숨요양병원', '서울특별시 강남구', '02-1234-5678', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE facility_id = facility_id;

--
-- 규칙
-- - building: 'A동' (프론트 WardPage / API 파라미터와 동일)
-- - room: 숫자만 문자열 ('101', '201', … 호 접미사 없음)
-- - 층별 호실 10개씩, 2인실 3 + 4인실 4 + 6인실 3
-- - roomgender_type: MALE / FEMALE / CONCOCTION 모두 등장
-- - room_type: GENERAL / ICU / ISOLATION 혼합
-- - 모든 침상: patient_id NULL, is_occupied = 0
--
-- 기존 동일 구역 데이터가 있으면 PK/중복 충돌 가능 → 필요 시 아래 주석 해제 후 실행
-- SET FOREIGN_KEY_CHECKS = 0;
-- UPDATE PATIENT SET location_id = NULL WHERE location_id IN (
--   SELECT location_id FROM (SELECT location_id FROM LOCATION WHERE building = 'A동' AND floor IN (1, 2)) t
-- );
-- DELETE FROM LOCATION WHERE building = 'A동' AND floor IN (1, 2);
-- SET FOREIGN_KEY_CHECKS = 1;

-- ========== A동 1층 101~110 ==========

-- 101: 2인실 GENERAL 남
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 1, '101', '1', 'GENERAL', 'MALE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '101', '2', 'GENERAL', 'MALE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 102: 2인실 ICU 여
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 1, '102', '1', 'ICU', 'FEMALE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '102', '2', 'ICU', 'FEMALE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 103: 2인실 ISOLATION 혼합
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 1, '103', '1', 'ISOLATION', 'CONCOCTION', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '103', '2', 'ISOLATION', 'CONCOCTION', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 104: 4인실 ICU 남
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 1, '104', '1', 'ICU', 'MALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '104', '2', 'ICU', 'MALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '104', '3', 'ICU', 'MALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '104', '4', 'ICU', 'MALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 105: 4인실 GENERAL 여
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 1, '105', '1', 'GENERAL', 'FEMALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '105', '2', 'GENERAL', 'FEMALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '105', '3', 'GENERAL', 'FEMALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '105', '4', 'GENERAL', 'FEMALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 106: 4인실 ISOLATION 혼합
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 1, '106', '1', 'ISOLATION', 'CONCOCTION', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '106', '2', 'ISOLATION', 'CONCOCTION', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '106', '3', 'ISOLATION', 'CONCOCTION', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '106', '4', 'ISOLATION', 'CONCOCTION', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 107: 4인실 GENERAL 남
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 1, '107', '1', 'GENERAL', 'MALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '107', '2', 'GENERAL', 'MALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '107', '3', 'GENERAL', 'MALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '107', '4', 'GENERAL', 'MALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 108: 6인실 GENERAL 여
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 1, '108', '1', 'GENERAL', 'FEMALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '108', '2', 'GENERAL', 'FEMALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '108', '3', 'GENERAL', 'FEMALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '108', '4', 'GENERAL', 'FEMALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '108', '5', 'GENERAL', 'FEMALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '108', '6', 'GENERAL', 'FEMALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 109: 6인실 ISOLATION 남
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 1, '109', '1', 'ISOLATION', 'MALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '109', '2', 'ISOLATION', 'MALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '109', '3', 'ISOLATION', 'MALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '109', '4', 'ISOLATION', 'MALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '109', '5', 'ISOLATION', 'MALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '109', '6', 'ISOLATION', 'MALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 110: 6인실 ICU 혼합
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 1, '110', '1', 'ICU', 'CONCOCTION', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '110', '2', 'ICU', 'CONCOCTION', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '110', '3', 'ICU', 'CONCOCTION', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '110', '4', 'ICU', 'CONCOCTION', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '110', '5', 'ICU', 'CONCOCTION', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 1, '110', '6', 'ICU', 'CONCOCTION', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- ========== A동 2층 201~210 ==========

-- 201: 2인실 ISOLATION 여
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 2, '201', '1', 'ISOLATION', 'FEMALE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '201', '2', 'ISOLATION', 'FEMALE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 202: 2인실 GENERAL 남
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 2, '202', '1', 'GENERAL', 'MALE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '202', '2', 'GENERAL', 'MALE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 203: 2인실 ICU 혼합
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 2, '203', '1', 'ICU', 'CONCOCTION', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '203', '2', 'ICU', 'CONCOCTION', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 204: 4인실 ICU 여
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 2, '204', '1', 'ICU', 'FEMALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '204', '2', 'ICU', 'FEMALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '204', '3', 'ICU', 'FEMALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '204', '4', 'ICU', 'FEMALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 205: 4인실 ISOLATION 남
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 2, '205', '1', 'ISOLATION', 'MALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '205', '2', 'ISOLATION', 'MALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '205', '3', 'ISOLATION', 'MALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '205', '4', 'ISOLATION', 'MALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 206: 4인실 GENERAL 혼합
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 2, '206', '1', 'GENERAL', 'CONCOCTION', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '206', '2', 'GENERAL', 'CONCOCTION', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '206', '3', 'GENERAL', 'CONCOCTION', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '206', '4', 'GENERAL', 'CONCOCTION', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 207: 4인실 GENERAL 여
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 2, '207', '1', 'GENERAL', 'FEMALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '207', '2', 'GENERAL', 'FEMALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '207', '3', 'GENERAL', 'FEMALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '207', '4', 'GENERAL', 'FEMALE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 208: 6인실 GENERAL 남
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 2, '208', '1', 'GENERAL', 'MALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '208', '2', 'GENERAL', 'MALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '208', '3', 'GENERAL', 'MALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '208', '4', 'GENERAL', 'MALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '208', '5', 'GENERAL', 'MALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '208', '6', 'GENERAL', 'MALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 209: 6인실 ISOLATION 여
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 2, '209', '1', 'ISOLATION', 'FEMALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '209', '2', 'ISOLATION', 'FEMALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '209', '3', 'ISOLATION', 'FEMALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '209', '4', 'ISOLATION', 'FEMALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '209', '5', 'ISOLATION', 'FEMALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '209', '6', 'ISOLATION', 'FEMALE', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 210: 6인실 ICU 혼합
INSERT INTO LOCATION (facility_id, patient_id, building, floor, room, bed, room_type, roomgender_type, room_capacity, created_at, updated_at, is_occupied) VALUES
(1, NULL, 'A동', 2, '210', '1', 'ICU', 'CONCOCTION', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '210', '2', 'ICU', 'CONCOCTION', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '210', '3', 'ICU', 'CONCOCTION', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '210', '4', 'ICU', 'CONCOCTION', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '210', '5', 'ICU', 'CONCOCTION', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(1, NULL, 'A동', 2, '210', '6', 'ICU', 'CONCOCTION', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 검증 예시
-- SELECT floor, room, COUNT(*) AS beds, MIN(room_capacity) AS cap, MIN(roomgender_type) AS g, MIN(room_type) AS rt
-- FROM LOCATION WHERE building = 'A동' AND floor IN (1, 2) GROUP BY floor, room ORDER BY floor, room;
