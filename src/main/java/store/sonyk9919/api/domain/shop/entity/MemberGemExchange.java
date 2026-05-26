package store.sonyk9919.api.domain.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.domain.member.entity.MemberAccount;
import store.sonyk9919.api.global.common.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_gem_exchange")
public class MemberGemExchange extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_gem_exchange_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_account_id", nullable = false)
    private MemberAccount memberAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gem_item_id", nullable = false)
    private GemItem gemItem;

    private MemberGemExchange(MemberAccount memberAccount, GemItem gemItem) {
        this.memberAccount = memberAccount;
        this.gemItem = gemItem;
    }

    public static MemberGemExchange create(MemberAccount memberAccount, GemItem gemItem) {
        return new MemberGemExchange(memberAccount, gemItem);
    }
}
