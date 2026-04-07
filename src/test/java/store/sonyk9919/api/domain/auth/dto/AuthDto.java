package store.sonyk9919.api.domain.auth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthDto {

    private String account;
    private String password;

    private AuthDto(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public static AuthDto from(String account, String password) {
        return new AuthDto(account, password);
    }

}
