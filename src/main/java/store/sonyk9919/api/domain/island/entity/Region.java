package store.sonyk9919.api.domain.island.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Region {

    SEOUL("서울"),
    INCHEON("인천"),
    GYEONGGI("경기"),
    GANGWON("강원"),
    DAEJEON("대전"),
    SEJONG("세종"),
    CHUNGBUK("충북"),
    CHUNGNAM("충남"),
    BUSAN("부산"),
    DAEGU("대구"),
    ULSAN("울산"),
    GWANGJU("광주"),
    GYEONGBUK("경북"),
    GYEONGNAM("경남"),
    JEONBUK("전북"),
    JEONNAM("전남"),
    JEJU("제주");

    private final String label;
}
