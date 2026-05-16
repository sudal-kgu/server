package store.sonyk9919.api.domain.island.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import store.sonyk9919.api.domain.island.entity.Region;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberIslandCreateRequestDto {

    @Length(min = 3, max = 20)
    private String nickname;

    @NotNull
    private Region region;
}
