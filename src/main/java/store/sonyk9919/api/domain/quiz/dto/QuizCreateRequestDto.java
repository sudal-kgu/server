package store.sonyk9919.api.domain.quiz.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizCreateRequestDto {

    private String serial;
}
