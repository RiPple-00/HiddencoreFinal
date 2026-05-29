-- 보호자 페이지에서 linked-patients가 비는 문제 해결용 더미 스크립트
-- 목적:
-- 1) GUARDIAN 사용자 1명 확보

SET @now := NOW();

SET @guardian_user_id := (
  SELECT u.user_id
  FROM USERS u
  WHERE u.users_role = 'GUARDIAN'
    AND u.users_status = 'ACTIVE'
  ORDER BY u.user_id
  LIMIT 1
);

SET @facility_id := (
  SELECT f.facility_id
  FROM FACILITY f
  ORDER BY f.facility_id
  LIMIT 1
);

SET @patient_id := (
  SELECT p.patient_id
  FROM PATIENT p
  WHERE p.name = '김태우'
  ORDER BY p.patient_id
  LIMIT 1
);

SET @patient_id := COALESCE(
  @patient_id,
  (SELECT p.patient_id FROM PATIENT p ORDER BY p.patient_id LIMIT 1)
);

-- GUARDIAN 없으면 의도적으로 중단
SET @guard_guardian_exists := IF(@guardian_user_id IS NULL, (SELECT 1 / 0), 1);

-- 환자가 없으면 신규 생성
INSERT INTO PATIENT (
  facility_id,
  location_id,
  name,
  gender,
  birth_date,
  address,
  admission_date,
  discharge_date,
  blood_type,
  height,
  weight,
  admission_status,
  diet_type,
  memo,
  status,
  created_at,
  updated_at
)
SELECT
  @facility_id,
  NULL,
  '김태우',
  'FEMALE',
  '1942-05-12',
  '서울',
  CURDATE(),
  NULL,
  'A_POSITIVE',
  NULL,
  NULL,
  NULL,
  NULL,
  'guardian-link-seed',
  'STABLE',
  @now,
  @now
WHERE @patient_id IS NULL;

SET @patient_id := COALESCE(@patient_id, LAST_INSERT_ID());

-- 기존 연결이 있으면 그대로, 없으면 1건 생성
INSERT INTO GUARDIAN_PATIENT (
  guardian_user_id,
  patient_id,
  relationship,
  is_primary,
  created_at
)
SELECT
  @guardian_user_id,
  @patient_id,
  '가족',
  1,
  @now
WHERE NOT EXISTS (
  SELECT 1
  FROM GUARDIAN_PATIENT gp
  WHERE gp.guardian_user_id = @guardian_user_id
    AND gp.patient_id = @patient_id
);

-- 동일 보호자에 다수 primary가 있으면 최신 연결만 primary 유지
UPDATE GUARDIAN_PATIENT gp
JOIN (
  SELECT guardian_user_id, MAX(guardian_patient_id) AS keep_id
  FROM GUARDIAN_PATIENT
  WHERE guardian_user_id = @guardian_user_id
  GROUP BY guardian_user_id
) x ON x.guardian_user_id = gp.guardian_user_id
SET gp.is_primary = CASE WHEN gp.guardian_patient_id = x.keep_id THEN 1 ELSE 0 END
WHERE gp.guardian_user_id = @guardian_user_id;

SELECT
  @guardian_user_id AS guardian_user_id,
  @patient_id AS patient_id,
  (SELECT u.login_id FROM USERS u WHERE u.user_id = @guardian_user_id) AS guardian_login_id,
  (SELECT p.name FROM PATIENT p WHERE p.patient_id = @patient_id) AS patient_name,
  (SELECT COUNT(*) FROM GUARDIAN_PATIENT gp WHERE gp.guardian_user_id = @guardian_user_id) AS linked_count;
