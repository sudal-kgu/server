package store.sonyk9919.api.domain.member.entitiy;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RefreshTokenStatus {
    ACTIVATE("ACTIVATE"),
    STALE("STALE");

    private final String key;

    @Override
    public String toString() {
        return key;
    }
}
