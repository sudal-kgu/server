-- 1. 가구류 매핑
INSERT INTO trash_taxonomy (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id
FROM trash_category c, trash_sub_category s
WHERE c.ko = '가구류' AND s.ko = '공통(가구류)';

-- 2. 고철류 매핑
INSERT INTO trash_taxonomy (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id
FROM trash_category c, trash_sub_category s
WHERE c.ko = '고철류'
  AND s.ko IN ('고철(고철류)', '비철금속(고철류)', '주전자(고철류)', '프라이팬(고철류)', '두발자전거(자전거)');

-- 3. 불연성 종량제 매핑
INSERT INTO trash_taxonomy (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id
FROM trash_category c, trash_sub_category s
WHERE c.ko = '불연성 종량제'
  AND s.ko IN ('그릇류(도기류)', '뚝배기(도기류)', '병(도기류)', '컵(도기류)', '항아리(도기류)', '화분(도기류)');

-- 4. 비닐류 매핑
INSERT INTO trash_taxonomy (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id
FROM trash_category c, trash_sub_category s
WHERE c.ko = '비닐류'
  AND s.ko IN ('식품봉지(비닐)', '리필용기(비닐)', '봉투(비닐)', '에어캡(비닐)', '포장재(비닐)');

-- 5. 스티로폼류 매핑
INSERT INTO trash_taxonomy (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id
FROM trash_category c, trash_sub_category s
WHERE c.ko = '스티로폼류'
  AND s.ko IN ('네모트레이(스티로폼류)', '보호재(스티로폼류)', '일반스티로폼(스티로폼류)', '포장용기(스티로폼류)');

-- 6. 유리병류 매핑
INSERT INTO trash_taxonomy (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id
FROM trash_category c, trash_sub_category s
WHERE c.ko = '유리병류'
  AND s.ko IN ('기타술병(유리병)', '맥주병(유리병)', '박카스병(유리병)', '소주병(유리병)', '음료병(유리병)');

-- 7. 전용함 매핑
INSERT INTO trash_taxonomy (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id
FROM trash_category c, trash_sub_category s
WHERE c.ko = '전용함'
  AND s.ko IN ('공통(형광등)', '일회용컵(페트병류)', '일반페트병(페트병류)', '상의(의류)', '원피스(의류)', '하의(의류)');

-- 8. 종량제봉투 매핑
INSERT INTO trash_taxonomy (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id
FROM trash_category c, trash_sub_category s
WHERE c.ko = '종량제'
  AND s.ko IN ('도마(나무)', '액자(나무)', '장식품(나무)', '주걱(나무)', '주방용품(나무)');

-- 9. 종이류 매핑
INSERT INTO trash_taxonomy (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id
FROM trash_category c, trash_sub_category s
WHERE c.ko = '종이류'
  AND s.ko IN ('노트(종이)', '상자류(종이)', '신문지(종이)', '음료수곽(종이)', '포장상자(종이)');

-- 10. 캔류 매핑
INSERT INTO trash_taxonomy (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id
FROM trash_category c, trash_sub_category s
WHERE c.ko = '캔류'
  AND s.ko IN ('음료(캔류)', '통조림(캔류)');

-- 11. 폐가전제품 매핑
INSERT INTO trash_taxonomy (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id
FROM trash_category c, trash_sub_category s
WHERE c.ko = '폐가전제품'
  AND s.ko IN ('TV(전자제품)', '가습기(전자제품)', '냉장고(전자제품)', '세탁기(전자제품)', '컴퓨터(전자제품)');

-- 12. 플라스틱류 매핑
INSERT INTO trash_taxonomy (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id
FROM trash_category c, trash_sub_category s
WHERE c.ko = '플라스틱류'
  AND s.ko IN ('대용량통(플라스틱)', '밀폐용기(플라스틱)', '바구니(플라스틱)', '욕실용품(플라스틱)', '장난감(플라스틱)');