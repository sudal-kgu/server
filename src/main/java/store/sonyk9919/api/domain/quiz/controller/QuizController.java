package store.sonyk9919.api.domain.quiz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.entity.AuthMember;
import store.sonyk9919.api.domain.quiz.dto.QuizCreateRequestDto;
import store.sonyk9919.api.domain.quiz.dto.QuizProblemResponseDto;
import store.sonyk9919.api.domain.quiz.dto.QuizSessionResponseDto;
import store.sonyk9919.api.domain.quiz.service.QuizPlayService;

@RestController
@RequestMapping("/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizPlayService quizPlayService;

    @PostMapping("/sessions")
    @ResponseStatus(HttpStatus.CREATED)
    public QuizSessionResponseDto createQuiz(
            @AuthMember AuthMemberDto authMember,
            @RequestBody QuizCreateRequestDto request
    ) {
        return quizPlayService.startSession(authMember.getId(), request.getSerial());
    }

    @GetMapping("/sessions")
    @ResponseStatus(HttpStatus.OK)
    public QuizSessionResponseDto getActiveQuizSession(@AuthMember AuthMemberDto authMember) {
        return quizPlayService.getActiveQuizSession(authMember.getId());
    }

    @GetMapping("/sessions/{sessionId}/problems/{problemId}")
    @ResponseStatus(HttpStatus.OK)
    public QuizProblemResponseDto getQuizProblem(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Long sessionId,
            @PathVariable Long problemId
    ) {
        return quizPlayService.getQuizProblem(authMember.getId(), sessionId, problemId);
    }
}
