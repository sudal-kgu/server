package store.sonyk9919.api.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.dto.OAuthUserInfoDto;
import store.sonyk9919.api.domain.auth.dto.TokenResponseDto;
import store.sonyk9919.api.domain.auth.entity.OAuthProvider;
import store.sonyk9919.api.domain.auth.entity.OAuthProviderType;
import store.sonyk9919.api.domain.member.entity.MemberAccount;
import store.sonyk9919.api.domain.member.service.MemberAccountService;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthLoginFacade {

    private final MemberAccountService memberAccountService;
    private final OAuthProviderFactory oAuthProviderFactory;
    private final AuthTokenIssuer authTokenIssuer;

    public List<TokenResponseDto> login(OAuthProviderType type, String code) {
        OAuthProvider provider = oAuthProviderFactory.getProvider(type);
        OAuthUserInfoDto userInfo = provider.getUserInfo(code);
        try {
            MemberAccount memberAccount = memberAccountService.getMemberAccount(userInfo, type);
            return authTokenIssuer.issue(AuthMemberDto.from(memberAccount));
        } catch (CustomException e) {
            MemberAccount memberAccount = memberAccountService.createMemberAccount(userInfo, type);
            return authTokenIssuer.issue(AuthMemberDto.from(memberAccount));
        }
    }
}
