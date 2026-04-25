-- MEAL_PLAN 테이블에 영양 컬럼이 없을 때 수동 적용 (ddl-auto: none 사용 시)
-- MySQL에서 ddasum DB 선택 후 실행

ALTER TABLE meal_plan
  ADD COLUMN calorie INT NULL COMMENT '열량(kcal)',
  ADD COLUMN protein INT NULL COMMENT '단백질(g)';
