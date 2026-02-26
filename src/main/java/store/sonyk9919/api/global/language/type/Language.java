package store.sonyk9919.api.global.language.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Language {
    KO("ko"),
    EN("en");

    private final String key;
}
