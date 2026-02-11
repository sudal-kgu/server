package store.sonyk9919.api.domain.member.entitiy;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountRole role;

    private MemberAccount(String name, String password, AccountRole role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public static MemberAccount from(String name, String password, AccountRole role) {
        return new MemberAccount(name, password, role);
    }
}
