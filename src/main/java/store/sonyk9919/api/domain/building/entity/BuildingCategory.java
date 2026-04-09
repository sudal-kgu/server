package store.sonyk9919.api.domain.building.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BuildingCategory {
    PRODUCTION("생산 시설"),
    PURIFICATION("정화 시설");

    private final String description;
}