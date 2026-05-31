package store.sonyk9919.api.domain.island.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.domain.shop.entity.ShopItem;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"island_id", "shop_item_id"}))
public class IslandItemUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_item_id", nullable = false)
    private ShopItem item;

    @Column(nullable = false)
    private long useCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "island_id", nullable = false)
    private MemberIsland island;

    private IslandItemUsage(ShopItem item, MemberIsland island) {
        this.item = item;
        this.island = island;
        this.useCount = 0;
    }

    public static IslandItemUsage create(ShopItem item, MemberIsland island) {
        return new IslandItemUsage(item, island);
    }

    public void incrementUseCount() {
        useCount++;
    }
}
