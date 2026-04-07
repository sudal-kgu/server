package store.sonyk9919.api.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfile {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long exp;

    @Column
    private String nickname;

    private MemberProfile(String nickname, Long exp) {
        this.nickname = nickname;
        this.exp = exp;
    }

    public static MemberProfile from(String nickname, Long exp) {
        return new MemberProfile(nickname, exp);
    }
}
