package store.sonyk9919.api.domain.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.quiz.dto.QuizSessionResponseDto;
import store.sonyk9919.api.domain.quiz.dto.QuizProblemResponseDto;
import store.sonyk9919.api.domain.quiz.entity.Quiz;
import store.sonyk9919.api.domain.quiz.entity.QuizSession;
import store.sonyk9919.api.domain.quiz.entity.QuizSessionProblem;
import store.sonyk9919.api.domain.quiz.entity.QuizStatus;
import store.sonyk9919.api.domain.trash.entity.Trash;
import store.sonyk9919.api.domain.trash.service.TrashSearchService;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizPlayService {

    private final QuizSelectService quizSelectService;
    private final QuizSessionRegistryService quizSessionRegistryService;
    private final QuizSessionProblemRegistryService quizSessionProblemRegistryService;
    private final TrashSearchService trashSearchService;
    private final MemberIslandRegistryService memberIslandRegistryService;

    @Transactional
    public QuizSessionResponseDto startSession(Long memberAccountId, String serial) {
        MemberIsland island = memberIslandRegistryService.getIslandWithWriteLock(memberAccountId);
        return quizSessionRegistryService.getActiveSession(island).map(session -> {
            List<QuizSessionProblem> problems = quizSessionProblemRegistryService.getProblems(session);
            return QuizSessionResponseDto.from(session, problems);
        }).orElseGet(() -> createNewSession(island, serial));
    }

    private QuizSessionResponseDto createNewSession(MemberIsland island, String serial) {
        QuizSession session = quizSessionRegistryService.create(island);
        List<Quiz> quizzes = getQuizzes(serial);
        List<QuizSessionProblem> problems = quizSessionProblemRegistryService.createAll(session, quizzes);
        return QuizSessionResponseDto.from(session, problems);
    }

    private List<Quiz> getQuizzes(String serial) {
        Trash trash = trashSearchService.getTrashWithCategory(serial);
        return quizSelectService.select(trash.getTaxonomy().getCategory(), 3);
    }

    public QuizSessionResponseDto getActiveQuizSession(Long memberAccountId) {
        MemberIsland island = memberIslandRegistryService.getIsland(memberAccountId);
        return quizSessionRegistryService.getActiveSession(island).map(session -> {
            List<QuizSessionProblem> problems = quizSessionProblemRegistryService.getProblems(session);
            return QuizSessionResponseDto.from(session, problems);
        }).orElseThrow(() -> new CustomException(QuizStatus.NOT_FOUND_QUIZ_SESSION));
    }

    @Transactional
    public QuizProblemResponseDto getQuizProblem(Long memberAccountId, Long sessionId, Long problemId) {
        if (!quizSessionRegistryService.existsQuizSession(memberAccountId, sessionId)) {
            throw new CustomException(QuizStatus.FORBIDDEN);
        }
        QuizSessionProblem problem = quizSessionProblemRegistryService.getProblem(memberAccountId, sessionId, problemId);
        problem.updateExpiredAt();
        return QuizProblemResponseDto.from(problem);
    }
}
