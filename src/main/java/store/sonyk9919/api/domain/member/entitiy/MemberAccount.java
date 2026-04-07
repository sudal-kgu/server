package store.sonyk9919.api.domain.member.entitiy;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import store.sonyk9919.api.domain.auth.dto.OAuthUserInfoDto;
import store.sonyk9919.api.domain.auth.entity.OAuthProviderType;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAccount {

    @Id
    @Column(name = "member_account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String account;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "account_role")
    private AccountRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type")
    private OAuthProviderType type;

    @Column(name = "provider_id")
    private String providerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberProfile profile;

    private MemberAccount(String account, String password, AccountRole role) {
        this.account = account;
        this.password = password;
        this.role = role;
    }

    private MemberAccount(String providerId, OAuthProviderType type, AccountRole role) {
        this.providerId = providerId;
        this.type = type;
        this.role = role;
    }

    public static MemberAccount from(String name, String password, AccountRole role) {
        return new MemberAccount(name, password, role);
    }

    public static MemberAccount from(OAuthUserInfoDto userInfo, OAuthProviderType type, AccountRole role) {
        return new MemberAccount(userInfo.getId(), type, role);
    }

    public void registerMemberProfile(MemberProfile profile) {
        if (this.profile != null) throw new IllegalStateException();
        this.profile = profile;
    }
}
