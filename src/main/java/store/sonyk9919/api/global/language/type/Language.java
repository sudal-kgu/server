package store.sonyk9919.api.global.language.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Language {
    KO("ko"),
    EN("en");

    private final String key;

    public static Language from(String header) {
        if (header == null || header.isBlank()) {
            return KO;
        }
        String primaryLang = header.split(",")[0]
                .split("-")[0]
                .trim()
                .toLowerCase();
        return Arrays.stream(Language.values())
                .filter(lang -> lang.key.equals(primaryLang))
                .findFirst()
                .orElse(KO);
    }
}
