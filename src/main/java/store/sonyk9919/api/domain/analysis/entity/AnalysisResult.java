package store.sonyk9919.api.domain.analysis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.global.common.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@AttributeOverride(
        name = "createdAt",
        column = @Column(name = "result_at", updatable = false)
)
public class AnalysisResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long id;
}