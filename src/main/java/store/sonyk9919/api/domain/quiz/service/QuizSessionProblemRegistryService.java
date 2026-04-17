package store.sonyk9919.api.domain.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.quiz.entity.*;
import store.sonyk9919.api.domain.quiz.exception.QuizStatus;
import store.sonyk9919.api.domain.quiz.repository.QuizSessionProblemRepository;
import store.sonyk9919.api.domain.quiz.repository.QuizSessionProblemSearchRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizSessionProblemRegistryService {

    private final QuizSessionProblemRepository quizSessionProblemRepository;
    private final QuizSessionProblemSearchRepository quizSessionProblemSearchRepository;

    @Transactional
    public List<QuizSessionProblem> createAll(QuizSession session, List<Quiz> quizzes) {
        List<QuizSessionProblem> quizSessionProblemList = quizzes.stream()
                .map(quiz -> createProblem(session, quiz))
                .toList();
        quizSessionProblemRepository.saveAll(quizSessionProblemList);
        return quizSessionProblemList;
    }

    private QuizSessionProblem createProblem(QuizSession session, Quiz quiz) {
        List<QuizOption> shuffledOptions = new ArrayList<>(quiz.getOptions());
        List<String> orderList = new ArrayList<>();
        long answer = -1;

        Collections.shuffle(shuffledOptions);
        for (QuizOption option : shuffledOptions) {
            if (option.isAnswer()) {
                answer = option.getId();
            }
            orderList.add(String.valueOf(option.getId()));
        }

        if (answer == -1) {
            throw new CustomException(QuizStatus.NO_ANSWER_IN_OPTIONS);
        }

        String displayOrder = String.join(",", orderList);
        return QuizSessionProblem.from(session, quiz, answer, displayOrder);
    }

    public List<QuizSessionProblem> getProblems(QuizSession session) {
        return quizSessionProblemRepository.findAllBySession(session);
    }

    public List<QuizSessionProblem> getProblems(Long sessionId) {
        return quizSessionProblemRepository.findAllBySessionId(sessionId);
    }

    public QuizSessionProblem getProblem(Long memberAccountId, Long sessionId, Long problemId) {
        QuizSessionProblem problem = quizSessionProblemSearchRepository.findBy(memberAccountId, sessionId, problemId)
                .orElseThrow(() -> new CustomException(QuizStatus.NOT_FOUND_QUIZ_SESSION_PROBLEM));
        if (problem.getSession().isExpired()) {
            throw new CustomException(QuizStatus.EXPIRED_QUIZ_SESSION);
        }
        return problem;
    }
}
