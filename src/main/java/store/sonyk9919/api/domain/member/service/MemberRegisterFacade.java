package store.sonyk9919.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.member.dto.MemberProfileResponseDto;
import store.sonyk9919.api.domain.member.entity.MemberAccount;
import store.sonyk9919.api.domain.member.entity.MemberProfile;
import store.sonyk9919.api.domain.member.exception.MemberStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberRegisterFacade {
    private final MemberAccountService memberAccountService;
    private final MemberProfileService memberProfileService;

    @Transactional
    public void registerProfile(String nickname, Long memberAccountId) {
        MemberAccount account = memberAccountService.getMemberAccount(memberAccountId);
        MemberProfile profile = memberProfileService.createMemberProfile(nickname);
        account.registerMemberProfile(profile);
    }

    public MemberProfileResponseDto getProfile(Long memberAccountId) {
        MemberAccount account = memberAccountService.getMemberAccountWithProfile(memberAccountId);
        MemberProfile profile = account.getProfile();

        if (profile == null) {
            throw new CustomException(MemberStatus.MEMBER_PROFILE_NOT_FOUND);
        }

        return MemberProfileResponseDto.from(profile);
    }
}
