package store.sonyk9919.api.domain.quiz.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizSessionProblem {

    @Id
    @Column(name = "quiz_session_problem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_session_id", nullable = false)
    private QuizSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column
    private Long choice;

    @Column(nullable = false)
    private Long answer;

    @Column(nullable = false)
    private String displayOrder;

    @Column(name = "quiz_expired_at")
    private LocalDateTime expiredAt;

    private QuizSessionProblem(QuizSession session, Quiz quiz, Long answer, String displayOrder) {
        this.session = session;
        this.quiz = quiz;
        this.answer = answer;
        this.displayOrder = displayOrder;
    }

    public static QuizSessionProblem from(QuizSession session, Quiz quiz, Long answer, String displayOrder) {
        return new QuizSessionProblem(session, quiz, answer, displayOrder);
    }

    public void updateExpiredAt() {
        if (expiredAt != null) return;
        expiredAt = LocalDateTime.now().plusSeconds(20);
    }
}
