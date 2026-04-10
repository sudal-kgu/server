package store.sonyk9919.api.domain.building.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"building_base_id", "level"})})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BuildingSpec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_spec_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_base_id", nullable = false)
    private BuildingBase buildingBase;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private int requiredLevel;

    @Column(nullable = false)
    private int costShells;

    @Column(nullable = false)
    private int costGems;

    @Column(nullable = false)
    private int pph;

    @Column(nullable = false)
    private int requiredFuel;

    @Column(nullable = false)
    private int durationSecond;

    @Embedded
    private BuildingEffect effect;
}