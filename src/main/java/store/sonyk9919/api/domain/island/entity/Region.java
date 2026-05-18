package store.sonyk9919.api.domain.island.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Region {

    SEOUL("서울"),
    GYEONGGI("경기"),
    GANGWON("강원"),
    CHUNGBUK("충북"),
    CHUNGNAM("충남"),
    GYEONGBUK("경북"),
    GYEONGNAM("경남"),
    JEONBUK("전북"),
    JEONNAM("전남"),
    JEJU("제주");

    private final String label;
}
