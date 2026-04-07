package store.sonyk9919.api.domain.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileRequestDto {

    @NotEmpty
    @Length(min = 1, max = 20)
    private String nickname;
}
