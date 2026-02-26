-- 1. 가구류 (대형 폐기물 공통)
INSERT INTO trash_disposal_category (disposal_id, category_id)
SELECT d.disposal_id, c.category_id
FROM trash_disposal d, trash_category c
WHERE d.ko LIKE '%지정된 판매소에서 대형 폐기물 스티커를 구매하여%'
  AND c.ko = '가구류';

-- 2. 고철류 (고철류 공통)
INSERT INTO trash_disposal_category (disposal_id, category_id)
SELECT d.disposal_id, c.category_id
FROM trash_disposal d, trash_category c
WHERE d.ko LIKE '%철사, 못, 철판 등 자석에 붙는 고철은%'
  AND c.ko = '고철류';

-- 3. 불연성 종량제 (불연성 종량제 공통)
INSERT INTO trash_disposal_category (disposal_id, category_id)
SELECT d.disposal_id, c.category_id
FROM trash_disposal d, trash_category c
WHERE d.ko LIKE '%일반 종량제 봉투가 아닌 지자체 전용 불연성 마대에%'
  AND c.ko = '불연성 종량제';

-- 4. 비닐류 (비닐류 공통)
INSERT INTO trash_disposal_category (disposal_id, category_id)
SELECT d.disposal_id, c.category_id
FROM trash_disposal d, trash_category c
WHERE d.ko LIKE '%비닐에 붙은 택배 송장, 테이프, 스티커 등 다른 재질은 반드시 제거%'
  AND c.ko = '비닐류';

-- 5. 스티로폼류 (스티로폼 공통)
INSERT INTO trash_disposal_category (disposal_id, category_id)
SELECT d.disposal_id, c.category_id
FROM trash_disposal d, trash_category c
WHERE d.ko LIKE '%바람에 날아가지 않도록 가급적 묶거나 투명 비닐봉투%'
  AND c.ko = '스티로폼류';

-- 6. 유리병류 (유리병류 공통)
INSERT INTO trash_disposal_category (disposal_id, category_id)
SELECT d.disposal_id, c.category_id
FROM trash_disposal d, trash_category c
WHERE d.ko LIKE '%유리병 본체와 재질이 다른 뚜껑이나 라벨%'
  AND c.ko = '유리병류';

-- 7. 전용함 (전용함 배출 품목 공통)
INSERT INTO trash_disposal_category (disposal_id, category_id)
SELECT d.disposal_id, c.category_id
FROM trash_disposal d, trash_category c
WHERE d.ko LIKE '%환경 오염이나 화재 위험이 있는 품목은 반드시 지정된 전용 수거함%'
  AND c.ko = '전용함';

-- 8. 종량제 (종량제 봉투 공통)
INSERT INTO trash_disposal_category (disposal_id, category_id)
SELECT d.disposal_id, c.category_id
FROM trash_disposal d, trash_category c
WHERE d.ko LIKE '%재활용이 불가능한 가연성 쓰레기는 지자체 전용 종량제 봉투%'
  AND c.ko = '종량제';

-- 9. 종이류 (종이류 공통)
INSERT INTO trash_disposal_category (disposal_id, category_id)
SELECT d.disposal_id, c.category_id
FROM trash_disposal d, trash_category c
WHERE d.ko LIKE '%종이가 아닌 부속물인 테이프%비에 젖지 않도록%'
  AND c.ko = '종이류';

-- 10. 캔류 (금속캔류 공통)
INSERT INTO trash_disposal_category (disposal_id, category_id)
SELECT d.disposal_id, c.category_id
FROM trash_disposal d, trash_category c
WHERE d.ko LIKE '%겉면의 플라스틱 뚜껑이나 라벨 등 금속 이외의 재질은 반드시 분리%'
  AND c.ko = '캔류';

-- 11. 폐가전제품 (폐가전제품 공통)
INSERT INTO trash_disposal_category (disposal_id, category_id)
SELECT d.disposal_id, c.category_id
FROM trash_disposal d, trash_category c
WHERE d.ko LIKE '%원형이 훼손되지 않은 가전제품은 무상 방문 수거 서비스%'
  AND c.ko = '폐가전제품';

-- 12. 플라스틱류 (플라스틱 용기류 공통)
INSERT INTO trash_disposal_category (disposal_id, category_id)
SELECT d.disposal_id, c.category_id
FROM trash_disposal d, trash_category c
WHERE d.ko LIKE '%플라스틱 이외의 재질인 금속 스프링이나 고무 패킹 등은 반드시 분리%'
  AND c.ko = '플라스틱류';