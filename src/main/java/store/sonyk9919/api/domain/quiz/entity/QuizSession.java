package store.sonyk9919.api.domain.quiz.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.quiz.exception.QuizStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizSession {

    @Id
    @Column(name = "quiz_session_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "island_id", nullable = false)
    private MemberIsland island;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    private QuizSession(MemberIsland island) {
        this.island = island;
        isActive = true;
        expiredAt = LocalDate.now().plusDays(1).atStartOfDay();
    }

    public static QuizSession from(MemberIsland island) {
        return new QuizSession(island);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt) || !isActive;
    }

    public void expireSession() {
        if (!isActive) throw new CustomException(QuizStatus.EXPIRED_QUIZ_SESSION);
        isActive = false;
    }
}
