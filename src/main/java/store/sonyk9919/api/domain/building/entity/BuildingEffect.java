package store.sonyk9919.api.domain.building.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BuildingEffect {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EffectType type;

    @Column(nullable = false)
    private double effectValue;

    public static BuildingEffect of(EffectType type, double value) {
        return new BuildingEffect(type, value);
    }

    public boolean isEquals(EffectType targetType){
        return type == targetType;
    }
}