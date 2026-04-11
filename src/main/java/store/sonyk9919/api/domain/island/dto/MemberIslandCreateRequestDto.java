package store.sonyk9919.api.domain.island.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberIslandCreateRequestDto {

    @Length(min = 3, max = 20)
    private String nickname;
}
