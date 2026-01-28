package store.sonyk9919.api.domain.disposal.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashCategory;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_disposal_category",
                columnNames = { "disposal_id", "category_id" }
        )
})
public class TrashDisposalCategory {

    @Id
    @Column(name = "disposal_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disposal_id", nullable = false)
    private TrashDisposal disposal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private TrashCategory category;

}
