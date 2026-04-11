package store.sonyk9919.api.domain.quiz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.entity.AuthMember;
import store.sonyk9919.api.domain.quiz.dto.QuizCreateRequestDto;
import store.sonyk9919.api.domain.quiz.dto.QuizSessionResponseDto;
import store.sonyk9919.api.domain.quiz.service.QuizGenerateFacade;

@RestController
@RequestMapping("/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizGenerateFacade quizGenerateFacade;

    @PostMapping("/sessions")
    @ResponseStatus(HttpStatus.CREATED)
    public QuizSessionResponseDto createQuiz(
            @AuthMember AuthMemberDto authMember,
            @RequestBody QuizCreateRequestDto request
    ) {
        return quizGenerateFacade.generate(authMember.getId(), request.getSerial());
    }
}
