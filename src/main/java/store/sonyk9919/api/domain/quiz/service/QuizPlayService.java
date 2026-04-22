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
import store.sonyk9919.api.domain.quiz.exception.QuizStatus;
import store.sonyk9919.api.domain.trash.entity.Trash;
import store.sonyk9919.api.domain.trash.service.TrashSearchService;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.util.List;
import store.sonyk9919.api.global.common.lock.DistributedLock;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizPlayService {

    private final QuizSelectService quizSelectService;
    private final QuizSessionRegistryService quizSessionRegistryService;
    private final QuizSessionProblemRegistryService quizSessionProblemRegistryService;
    private final TrashSearchService trashSearchService;
    private final MemberIslandRegistryService memberIslandRegistryService;

    @DistributedLock(key = "'quiz:' + #memberAccountId")
    @Transactional
    public QuizSessionResponseDto startSession(Long memberAccountId, String serial) {
        MemberIsland island = memberIslandRegistryService.getIsland(memberAccountId);
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

    public List<QuizProblemResponseDto> getQuizProblems(Long memberAccountId, Long sessionId) {
        validateQuizSession(memberAccountId, sessionId);
        List<QuizSessionProblem> problems = quizSessionProblemRegistryService.getProblems(sessionId);
        validateQuizSessionComplete(problems.getFirst().getSession());
        return problems
                .stream()
                .map(QuizProblemResponseDto::fromWithAnswer)
                .toList();
    }

    public void validateQuizSessionComplete(QuizSession session) {
        if (!session.isExpired()) throw new CustomException(QuizStatus.QUIZ_NOT_COMPLETED);
    }

    public void validateQuizSession(Long memberAccountId, Long sessionId) {
        if (!quizSessionRegistryService.existsQuizSession(memberAccountId, sessionId))
            throw new CustomException(QuizStatus.FORBIDDEN);
    }

    @Transactional
    public QuizProblemResponseDto getQuizProblem(Long memberAccountId, Long sessionId, Long problemId) {
        QuizSessionProblem problem = getValidatedProblem(memberAccountId, sessionId, problemId);
        problem.updateExpiredAt();
        if (problem.getChoice() != null || problem.isExpired() || problem.getSession().isExpired()) {
            return QuizProblemResponseDto.fromWithAnswer(problem);
        }
        return QuizProblemResponseDto.from(problem);
    }

    @Transactional
    public QuizProblemResponseDto confirmQuizChoice(
            Long memberAccountId,
            Long sessionId,
            Long problemId,
            Long choice
    ) {
        QuizSessionProblem problem = getValidatedProblem(memberAccountId, sessionId, problemId);
        if (problem.isExpired() || problem.getSession().isExpired()) throw new CustomException(QuizStatus.EXPIRED_QUIZ_SESSION);
        problem.confirmChoice(choice);
        return QuizProblemResponseDto.fromWithAnswer(problem);
    }

    private QuizSessionProblem getValidatedProblem(Long memberAccountId, Long sessionId, Long problemId) {
        validateQuizSession(memberAccountId, sessionId);
        return quizSessionProblemRegistryService.getProblem(memberAccountId, sessionId, problemId);
    }

    @Transactional
    public void completeQuizSession(Long memberAccountId, Long sessionId) {
        QuizSession session = quizSessionRegistryService.getSession(memberAccountId, sessionId);
        if (session.isExpired()) {
            return;
        }
        session.expireSession();
    }
}
