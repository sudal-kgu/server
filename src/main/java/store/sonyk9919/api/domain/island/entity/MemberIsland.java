package store.sonyk9919.api.domain.island.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.domain.member.entity.MemberAccount;

import static java.lang.Math.min;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberIsland {

    private static final int MAX_LEVEL = 7;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "island_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private int cumulativeExp;

    @Column(nullable = false)
    private int recyclingContributionExp;

    @Column(nullable = false)
    private int itemContributionExp;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_account_id", nullable = false, unique = true)
    private MemberAccount memberAccount;

    private MemberIsland(String nickname, MemberAccount memberAccount) {
        this.nickname = nickname;
        this.memberAccount = memberAccount;
        level = 1;
        cumulativeExp = 0;
        recyclingContributionExp = 0;
        itemContributionExp = 0;
    }

    public static MemberIsland create(String nickname, MemberAccount memberAccount) {
        MemberIsland island = new MemberIsland(nickname, memberAccount);
        memberAccount.registerMemberIsland(island);
        return island;
    }

    public boolean isMaxLevel() {
        return level >= MAX_LEVEL;
    }

    public void addRecyclingExp(LevelSpec currentSpec) {
        int remaining = currentSpec.getRecyclingContributionExpLimit() - recyclingContributionExp;
        int gain = min(currentSpec.getExpPerRecycling(), remaining);
        if (gain <= 0) return;
        recyclingContributionExp += gain;
        cumulativeExp += gain;
    }

    public void addItemExp(int expAmount) {
        itemContributionExp += expAmount;
        cumulativeExp += expAmount;
    }

    public boolean canLevelUp(LevelSpec currentSpec, LevelSpec nextSpec) {
        if (isMaxLevel()) return false;
        boolean expMet = cumulativeExp >= nextSpec.getRequiredExp();
        boolean itemMet = currentSpec.getItemContributionExpMin() == null
                || itemContributionExp >= currentSpec.getItemContributionExpMin();
        return expMet && itemMet;
    }

    public void levelUp() {
        level++;
        recyclingContributionExp = 0;
        itemContributionExp = 0;
    }
}
