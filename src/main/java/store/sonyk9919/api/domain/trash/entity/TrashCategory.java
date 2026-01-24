package store.sonyk9919.api.domain.trash.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TrashCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "disposal_guide", columnDefinition = "TEXT")
    private String disposalGuide;

    public TrashCategory(String name, String disposalGuide) {
        this.name = name;
        this.disposalGuide = disposalGuide;
    }
}