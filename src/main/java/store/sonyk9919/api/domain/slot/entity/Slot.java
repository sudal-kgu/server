package store.sonyk9919.api.domain.slot.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.slot.exception.SlotStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"island_id", "slot_number"})})
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "island_id", nullable = false)
    private MemberIsland island;

    @Column(nullable = false)
    private int slotNumber;

    @Column(nullable = false)
    private boolean activated;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "building_id")
    private Building building;

    private Slot(MemberIsland island, int slotNumber) {
        this.island = island;
        this.slotNumber = slotNumber;
        this.activated = false;
    }

    public static Slot of(MemberIsland member, int slotNumber){
        return new Slot(member, slotNumber);
    }

    public void activate() {
        if (activated) throw new CustomException(SlotStatus.SLOT_ALREADY_ACTIVATED);

        activated = true;
    }

    public void build(Building newBuilding) {
        if (!activated) throw new CustomException(SlotStatus.SLOT_NOT_ACTIVATED);
        if (building != null) throw new CustomException(SlotStatus.SLOT_ALREADY_BUILT);

        building = newBuilding;
    }
}