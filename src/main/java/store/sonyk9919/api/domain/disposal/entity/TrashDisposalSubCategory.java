package store.sonyk9919.api.domain.disposal.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashSubCategory;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_disposal_subcategory",
                columnNames = { "disposal_id", "subcategory_id" }
        )
})
public class TrashDisposalSubCategory {

    @Id
    @Column(name = "disposal_subcategory_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disposal_id")
    private TrashDisposal disposal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id")
    private TrashSubCategory subCategory;

}
