package store.sonyk9919.api.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import store.sonyk9919.api.domain.auth.dto.OAuthUserInfoDto;
import store.sonyk9919.api.domain.auth.entity.OAuthProviderType;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.member.exception.MemberStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAccount {

    @Id
    @Column(name = "member_account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "account_role")
    private AccountRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type")
    private OAuthProviderType type;

    @Column(name = "provider_id")
    private String providerId;

    @OneToOne(mappedBy = "memberAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberIsland island;

    private MemberAccount(String providerId, OAuthProviderType type, AccountRole role) {
        this.providerId = providerId;
        this.type = type;
        this.role = role;
    }

    public static MemberAccount from(OAuthUserInfoDto userInfo, OAuthProviderType type, AccountRole role) {
        return new MemberAccount(userInfo.getId(), type, role);
    }

    public void registerMemberIsland(MemberIsland island) {
        if (this.island != null) throw new CustomException(MemberStatus.MEMBER_ACCOUNT_ISLAND_ALREADY_EXISTS);
        this.island = island;
    }
}
