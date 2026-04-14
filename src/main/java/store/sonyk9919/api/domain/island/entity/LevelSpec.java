package store.sonyk9919.api.domain.island.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LevelSpec {

    @Id
    private int level;

    @Column(nullable = false)
    private int requiredExp;

    @Column(nullable = false)
    private int recyclingContributionExpLimit;

    @Column(nullable = false)
    private int maxSlotCount;

    @Column(nullable = false)
    private int expPerRecycling;
}
