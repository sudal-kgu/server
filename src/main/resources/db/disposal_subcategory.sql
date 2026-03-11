-- 1. 가구류 소분류

-- 2. 고철류 소분류
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%코팅된 프라이팬이나 스테인리스 냄비도 이물질만 없다면%'
  and s.ko = '프라이팬(고철류)';

-- 비철금속류
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%스테인리스 식기, 양은그릇, 구리선 등 자석에 붙지 않는 금속%'
  and s.ko = '비철금속(고철류)';

-- 자전거
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%철제 선반, 자전거, 휠체어 등 부피가 큰 고철은%'
  and s.ko = '두발자전거(자전거)';

-- 3. 불연성 종량제 소분류

-- 도기류
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '깨진 그릇, 화분, 도자기 인형 등 흙이나 돌로 만들어진%'
  and s.ko in ('그릇류(도기류)', '뚝배기(도기류)', '병(도기류)', '컵(도기류)', '항아리(도기류)', '화분(도기류)');

-- 4. 비닐류 소분류

-- 식품봉지
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%라면 봉지처럼 스프 등으로 오염된 경우 물로 헹구어%'
  and s.ko = '식품봉지(비닐)';

-- 에어캡
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%택배 완충재로 사용된 에어캡은 바람을 뺀 뒤%'
  and s.ko = '에어캡(비닐)';

-- 5. 스티로폼류 소분류

-- 보호재
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%전자제품 구입 시 들어있는 깨끗한 흰색 완충재는%'
  and s.ko = '보호재(스티로폼류)';

-- 포장용기
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%국물 자국이 심하게 배어 빨갛게 변한 용기는 재활용이%'
  and s.ko in ('포장용기(스티로폼류)', '네모트레이(스티로폼류)');

-- 6. 유리병류 소분류

insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%소주병, 맥주병 등 빈용기보증금 대상 병은%'
  and s.ko in ('기타술병(유리병)', '맥주병(유리병)', '박카스병(유리병)', '소주병(유리병)', '음료병(유리병)');

-- 7. 전용함 소분류

-- 형광등
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%형광등 내부의 수은 가스가 유출되지 않도록%'
  and s.ko = '공통(형광등)';

-- 투명 페트병
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%생수 및 음료가 담겼던 투명한 페트병은 일반 플라스틱과%'
  and s.ko in ('일반페트병(페트병류)', '일회용컵(페트병류)');

insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%세탁 후 건조된 상태로 배출하며, 가급적 투명 봉투%'
  and s.ko in ('상의(의류)', '원피스(의류)', '하의(의류)');

-- 8. 종량제 소분류

-- 9. 종이류 소분류
-- 신문지
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%신문 사이에 끼어 있는 비닐 코팅된 광고지%'
  and s.ko = '신문지(종이)';

-- 상자류
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%상자에 붙어 있는 비닐 테이프와 택배 송장%'
  and s.ko in ('상자류(종이)', '포장상자(종이)');

-- 노트
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%비닐 코팅된 겉표지와 플라스틱 또는 철제 스프링%'
  and s.ko = '노트(종이)';

-- 종이팩
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%가위로 종이팩을 잘라 펼친 뒤 햇볕에%'
  and s.ko = '음료수곽(종이)';

-- 10. 캔류 소분류

-- 음료 캔
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%담배꽁초 등 이물질을 넣지 않고 가급적 압착%'
  and s.ko = '음료(캔류)';

-- 통조림 캔
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%참치나 햄 캔 등은 세제를 이용해 내부의 기름기%'
  and s.ko = '통조림(캔류)';

-- 11. 폐가전제품 소분류

-- 대형 가전
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '폐가전 무상방문수거 서비스를 예약하여 전문 수거 기사가%'
  and s.ko in ('TV(전자제품)', '냉장고(전자제품)', '세탁기(전자제품)');

-- 소형 가전
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '소형 가전은 5개 이상 모아서 무상방문수거 서비스를 신청하거나,%'
  and s.ko = '가습기(전자제품)';

-- 컴퓨터
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%마우스, 키보드, 스피커 등은 소형 가전 수거 방식으로%'
  and s.ko = '컴퓨터(전자제품)';

-- 12. 플라스틱류 소분류

-- 밀폐용기
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '%반찬통 등 밀폐 용기의 뚜껑에 달린 고무나 실리콘 패킹%'
  and s.ko = '밀폐용기(플라스틱)';

-- 욕실용품
insert into trash_disposal_sub_category (disposal_id, subcategory_id)
select d.disposal_id, s.subcategory_id
from trash_disposal d, trash_sub_category s
where d.ko like '내용물을 완전히 비우고 내부를 물로 헹구어 거품이나 잔여물이 없도록 합니다.%'
  and s.ko = '욕실용품(플라스틱)';