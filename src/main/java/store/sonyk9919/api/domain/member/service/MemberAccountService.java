package store.sonyk9919.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.auth.dto.OAuthUserInfoDto;
import store.sonyk9919.api.domain.auth.entity.OAuthProviderType;
import store.sonyk9919.api.domain.member.entitiy.AccountRole;
import store.sonyk9919.api.domain.member.entitiy.MemberAccount;
import store.sonyk9919.api.domain.member.exception.MemberStatus;
import store.sonyk9919.api.domain.member.repository.MemberAccountRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberAccountService {

    private final MemberAccountRepository memberAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberAccount getMemberAccount(Long memberAccountId) {
        return memberAccountRepository.findById(memberAccountId)
                .orElseThrow(() -> new CustomException(MemberStatus.MEMBER_ACCOUNT_BAD_REQUEST));
    }

    @Transactional
    public MemberAccount createMemberAccount(String account, String password) {
        MemberAccount memberAccount = MemberAccount.from(account, passwordEncoder.encode(password), AccountRole.USER);
        memberAccountRepository.save(memberAccount);
        return memberAccount;
    }

    public MemberAccount getMemberAccount(String account, String password) {
        MemberAccount memberAccount = memberAccountRepository.findByAccount(account)
                .orElseThrow(() -> new CustomException(MemberStatus.MEMBER_ACCOUNT_UNAUTHORIZED));

        if (!passwordEncoder.matches(password, memberAccount.getPassword())) {
            throw new CustomException(MemberStatus.MEMBER_ACCOUNT_UNAUTHORIZED);
        }

        return memberAccount;
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
