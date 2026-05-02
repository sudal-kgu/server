package store.sonyk9919.api.domain.quiz.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RecyclingRewardResponse {

    private final int earnedShell;
    private final int earnedFuel;
    private final int earnedExp;

    public static RecyclingRewardResponse of(int shell, int fuel, int exp) {
        return new RecyclingRewardResponse(shell, fuel, exp);
    }
}
