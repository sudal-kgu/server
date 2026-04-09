package store.sonyk9919.api.domain.island.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.domain.island.exception.IslandStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_resource_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceType resourceType;

    @Column(nullable = false)
    private long amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "island_id", nullable = false)
    private MemberIsland island;

    private MemberResource(ResourceType resourceType, MemberIsland island) {
        this.resourceType = resourceType;
        this.amount = 0;
        this.island = island;
    }

    public static MemberResource create(ResourceType resourceType, MemberIsland island) {
        return new MemberResource(resourceType, island);
    }

    public void addAmount(long amount) {
        if (amount <= 0) throw new CustomException(IslandStatus.INVALID_AMOUNT);
        this.amount += amount;
    }

    public void subtractAmount(long amount) {
        if (amount <= 0) throw new CustomException(IslandStatus.INVALID_AMOUNT);
        if (this.amount - amount < 0) throw new CustomException(IslandStatus.INSUFFICIENT_AMOUNT);
        this.amount -= amount;
    }
}
