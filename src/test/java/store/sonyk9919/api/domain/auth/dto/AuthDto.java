package store.sonyk9919.api.domain.auth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthDto {

    private String name;
    private String password;

    private AuthDto(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public static AuthDto from(String name, String password) {
        return new AuthDto(name, password);
    }

}
