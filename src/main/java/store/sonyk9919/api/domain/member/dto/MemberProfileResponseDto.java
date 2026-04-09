package store.sonyk9919.api.domain.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.member.entity.MemberProfile;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileResponseDto {

    private final Long id;
    private final Long exp;
    private final String nickname;

    public static MemberProfileResponseDto from(MemberProfile profile) {
        return new MemberProfileResponseDto(profile.getId(), profile.getExp(), profile.getNickname());
    }
}
