package store.sonyk9919.api.domain.trash.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "disposal_guide", columnDefinition = "TEXT")
    private String disposalGuide;

    private TrashCategory(String name, String disposalGuide) {
        this.name = name;
        this.disposalGuide = disposalGuide;
    }

    public static TrashCategory create(String name, String disposalGuide){
        return new TrashCategory(name,disposalGuide);
    }
}