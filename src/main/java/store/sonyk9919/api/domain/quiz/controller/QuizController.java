package store.sonyk9919.api.domain.quiz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.entity.AuthMember;
import store.sonyk9919.api.domain.quiz.dto.QuizChoiceRequestDto;
import store.sonyk9919.api.domain.quiz.dto.QuizCreateRequestDto;
import store.sonyk9919.api.domain.quiz.dto.QuizProblemResponseDto;
import store.sonyk9919.api.domain.quiz.dto.QuizSessionResponseDto;
import store.sonyk9919.api.domain.quiz.service.QuizPlayService;

import java.util.List;

@Tag(name = "퀴즈 (Quiz)", description = "퀴즈 세션 관리 및 문제 풀이 관련 API")
@RestController
@RequestMapping("/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizPlayService quizPlayService;

    @Operation(summary = "퀴즈 세션 시작", description = "새로운 퀴즈 세션을 생성합니다. 이미 진행 중인 세션이 있다면 해당 세션 정보를 반환합니다.")
    @ApiResponse(responseCode = "201", description = "세션 시작 성공")
    @PostMapping("/sessions")
    @ResponseStatus(HttpStatus.CREATED)
    public QuizSessionResponseDto createQuiz(
            @Parameter(hidden = true) @AuthMember AuthMemberDto authMember,
            @RequestBody QuizCreateRequestDto request
    ) {
        return quizPlayService.startSession(authMember.getId(), request.getSerial());
    }

    @Operation(summary = "진행 중인 퀴즈 세션 조회", description = "사용자가 현재 진행 중인(만료되지 않은) 퀴즈 세션 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "404", description = "진행 중인 세션이 없음")
    @GetMapping("/sessions")
    @ResponseStatus(HttpStatus.OK)
    public QuizSessionResponseDto getActiveQuizSession(
            @Parameter(hidden = true) @AuthMember AuthMemberDto authMember
    ) {
        return quizPlayService.getActiveQuizSession(authMember.getId());
    }

    @GetMapping("/sessions/{sessionId}/problems")
    @ResponseStatus(HttpStatus.OK)
    public List<QuizProblemResponseDto> getQuizProblems(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Long sessionId
    ) {
        return quizPlayService.getQuizProblems(authMember.getId(), sessionId);
    }

    @Operation(summary = "퀴즈 문제 단건 조회", description = "세션 내의 특정 문제 상세 정보를 조회합니다. 조회 시 문제의 만료 시간이 갱신됩니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/sessions/{sessionId}/problems/{problemId}")
    @ResponseStatus(HttpStatus.OK)
    public QuizProblemResponseDto getQuizProblem(
            @Parameter(hidden = true) @AuthMember AuthMemberDto authMember,
            @Parameter(description = "퀴즈 세션 ID", example = "1") @PathVariable Long sessionId,
            @Parameter(description = "퀴즈 문제 ID", example = "10") @PathVariable Long problemId
    ) {
        return quizPlayService.getQuizProblem(authMember.getId(), sessionId, problemId);
    }

    @Operation(summary = "퀴즈 정답 선택 (제출)", description = "특정 문제에 대해 사용자가 선택한 정답을 제출하고 확정합니다. 세션이나 문제가 만료된 경우 제출할 수 없습니다.")
    @ApiResponse(responseCode = "200", description = "정답 제출 성공")
    @PostMapping("/sessions/{sessionId}/problems/{problemId}")
    @ResponseStatus(HttpStatus.OK)
    public QuizProblemResponseDto confirmQuizChoice(
            @Parameter(hidden = true) @AuthMember AuthMemberDto authMember,
            @Parameter(description = "퀴즈 세션 ID", example = "1") @PathVariable Long sessionId,
            @Parameter(description = "퀴즈 문제 ID", example = "10") @PathVariable Long problemId,
            @RequestBody QuizChoiceRequestDto request
    ) {
        return quizPlayService.confirmQuizChoice(authMember.getId(), sessionId, problemId, request.getChoice());
    }

    @Operation(summary = "퀴즈 세션 완료", description = "현재 진행 중인 퀴즈 세션을 강제로 만료(완료) 처리합니다.")
    @ApiResponse(responseCode = "200", description = "세션 만료(완료) 성공")
    @PostMapping("/sessions/{sessionId}/complete")
    @ResponseStatus(HttpStatus.OK)
    public void completeQuizSession(
            @Parameter(hidden = true) @AuthMember AuthMemberDto authMember,
            @Parameter(description = "퀴즈 세션 ID", example = "1") @PathVariable Long sessionId
    ) {
        quizPlayService.completeQuizSession(authMember.getId(), sessionId);
    }
}