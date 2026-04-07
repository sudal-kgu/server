package store.sonyk9919.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.member.entitiy.MemberAccount;
import store.sonyk9919.api.domain.member.entitiy.MemberProfile;

@Service
@RequiredArgsConstructor
public class MemberRegisterFacade {
    private final MemberAccountService memberAccountService;
    private final MemberProfileService memberProfileService;

    @Transactional
    public void registerProfile(String nickname, Long memberAccountId) {
        MemberAccount account = memberAccountService.getMemberAccount(memberAccountId);
        MemberProfile profile = memberProfileService.createMemberProfile(nickname);
        account.registerMemberProfile(profile);
    }
}
