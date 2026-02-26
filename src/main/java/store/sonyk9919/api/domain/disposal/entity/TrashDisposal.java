package store.sonyk9919.api.domain.disposal.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.global.language.type.Language;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashDisposal {

    @Id
    @Column(name = "disposal_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String ko;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String en;

    public String getGuide() {
        return getGuide(Language.KO);
    }

    public String getGuide(Language language) {
        return switch (language) {
            case KO -> ko;
            case EN -> en;
        };
    }
}
