package store.sonyk9919.api.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.dto.TokenResponseDto;
import store.sonyk9919.api.domain.member.entitiy.MemberAccount;
import store.sonyk9919.api.domain.member.service.MemberAccountService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthLoginFacade {

    private final MemberAccountService memberAccountService;
    private final AuthTokenIssuer authTokenIssuer;

    public List<TokenResponseDto> login(String name, String password) {
        MemberAccount memberAccount = memberAccountService.getMemberAccount(name, password);
        return authTokenIssuer.issue(AuthMemberDto.from(memberAccount));
    }
}
