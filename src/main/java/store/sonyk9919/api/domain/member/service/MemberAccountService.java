package store.sonyk9919.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.auth.dto.OAuthUserInfoDto;
import store.sonyk9919.api.domain.auth.entity.OAuthProviderType;
import store.sonyk9919.api.domain.member.entity.AccountRole;
import store.sonyk9919.api.domain.member.entity.MemberAccount;
import store.sonyk9919.api.domain.member.exception.MemberStatus;
import store.sonyk9919.api.domain.member.repository.MemberAccountRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberAccountService {

    private final MemberAccountRepository memberAccountRepository;

    public MemberAccount getMemberAccount(Long memberAccountId) {
        return memberAccountRepository.findById(memberAccountId)
                .orElseThrow(() -> new CustomException(MemberStatus.MEMBER_ACCOUNT_BAD_REQUEST));
    }

    public MemberAccount getMemberAccountWithProfile(Long memberAccountId) {
        return memberAccountRepository.findWithProfileById(memberAccountId)
                .orElseThrow(() -> new CustomException(MemberStatus.MEMBER_ACCOUNT_BAD_REQUEST));
    }

    @Transactional
    public MemberAccount createMemberAccount(OAuthUserInfoDto userInfo, OAuthProviderType type) {
        MemberAccount memberAccount = MemberAccount.from(userInfo, type, AccountRole.USER);
        memberAccountRepository.save(memberAccount);
        return memberAccount;
    }

    public MemberAccount getMemberAccount(OAuthUserInfoDto userInfo, OAuthProviderType type) {
        return memberAccountRepository.findByProviderIdAndType(userInfo.getId(), type)
                .orElseThrow(() -> new CustomException(MemberStatus.MEMBER_ACCOUNT_BAD_REQUEST));
    }
}
