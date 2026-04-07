package store.sonyk9919.api.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.entity.AuthMember;
import store.sonyk9919.api.domain.member.dto.MemberProfileRequestDto;
import store.sonyk9919.api.domain.member.service.MemberRegisterFacade;

@RestController
@RequestMapping("/v1/members/me")
@RequiredArgsConstructor
public class MemberProfileController {

    private final MemberRegisterFacade memberRegisterFacade;

    @PostMapping
    public void registerProfile(
            @AuthMember AuthMemberDto authMember,
            @Validated @RequestBody MemberProfileRequestDto requestDto
    ) {
        memberRegisterFacade.registerProfile(requestDto.getNickname(), authMember.getId());
    }
}
