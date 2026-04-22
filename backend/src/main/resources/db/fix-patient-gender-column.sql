-- PATIENT.gender 는 MALE / FEMALE / OTHER 또는 NULL 만 허용 (JPA EnumType.STRING)
-- 한글·잘못된 값이 있으면 GET /api/rooms/{room}/beds 조회 시 엔티티 로드가 실패할 수 있음.
-- 실행 전 백업 권장.

UPDATE patient SET gender = 'FEMALE' WHERE gender IN ('FEMALE', '여성', '여', 'female', 'Female');
UPDATE patient SET gender = 'MALE' WHERE gender IN ('MALE', '남성', '남', 'male', 'Male');
UPDATE patient SET gender = 'OTHER' WHERE gender IN ('OTHER', '기타', 'other', 'Other');

-- 위에 해당하지 않는 비정상 값은 NULL 로 (선택 입력 허용)
UPDATE patient
SET gender = NULL
WHERE gender IS NOT NULL
  AND gender NOT IN ('MALE', 'FEMALE', 'OTHER');
