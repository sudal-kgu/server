package store.sonyk9919.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public MemberAccount createMemberAccount(String name, String password) {
        MemberAccount memberAccount = MemberAccount.from(name, password, AccountRole.USER);
        memberAccountRepository.save(memberAccount);
        return memberAccount;
    }

    public MemberAccount getMemberAccount(String name) {
        return memberAccountRepository.findByName(name)
                .orElseThrow(() -> new CustomException(MemberStatus.MEMBER_ACCOUNT_UNAUTHORIZED));
    }
}
