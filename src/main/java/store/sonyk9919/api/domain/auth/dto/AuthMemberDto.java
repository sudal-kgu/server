package store.sonyk9919.api.domain.auth.dto;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.member.entitiy.AccountRole;
import store.sonyk9919.api.domain.member.entitiy.MemberAccount;
import store.sonyk9919.api.global.jwt.dto.ClaimsConvertible;

import java.util.Map;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthMemberDto implements ClaimsConvertible {
    private final Long id;
    private final AccountRole role;

    public static AuthMemberDto from(MemberAccount account) {
        return new AuthMemberDto(account.getId(), account.getRole());
    }

    public static AuthMemberDto from(Claims claims) {
        return new AuthMemberDto(
                Long.valueOf(claims.getSubject()),
                AccountRole.findByKey(claims.get(Body.ROLE, String.class))
        );
    }

    @Override
    public String getSubject() {
        return String.valueOf(id);
    }

    @Override
    public Map<String, Object> getBody() {
        return Map.of(
                Body.ROLE, role.getKey()
        );
    }

    private static class Body {
        public static final String ROLE = "role";
    }
}
