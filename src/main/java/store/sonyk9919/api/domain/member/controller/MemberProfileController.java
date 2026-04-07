package store.sonyk9919.api.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "회원 프로필 등록",
            description = "로그인한 사용자의 닉네임을 설정하여 프로필을 등록합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 등록 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력값 (공백 또는 닉네임 길이가 3 미만 20 초과 하는 경우)"),
    })
    @PostMapping
    public void registerProfile(
            @Parameter(hidden = true) @AuthMember AuthMemberDto authMember,
            @RequestBody MemberProfileRequestDto requestDto
    ) {
        memberRegisterFacade.registerProfile(requestDto.getNickname(), authMember.getId());
    }
}
