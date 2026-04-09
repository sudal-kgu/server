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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberIsland {

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
        this.level = 1;
        this.cumulativeExp = 0;
        this.recyclingContributionExp = 0;
        this.itemContributionExp = 0;
        this.memberAccount = memberAccount;
    }

    public static MemberIsland create(String nickname, MemberAccount memberAccount) {
        return new MemberIsland(nickname, memberAccount);
    }
}
