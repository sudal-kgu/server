package store.sonyk9919.api.domain.taxonomy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.global.language.type.Language;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashSubCategory {

    @Id
    @Column(name = "subcategory_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ko;

    @Column(nullable = false)
    private String en;

    public String getName() {
        return getName(Language.KO);
    }

    public String getName(Language language) {
        return switch (language) {
            case KO -> ko;
            case EN -> en;
        };
    }
}
