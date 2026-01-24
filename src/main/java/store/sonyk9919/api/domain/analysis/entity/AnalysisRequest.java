package store.sonyk9919.api.domain.analysis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import store.sonyk9919.api.global.common.entity.BaseEntity;
import java.util.UUID;

@Entity
@Getter
@AttributeOverride(
        name = "createdAt",
        column = @Column(name = "request_at", updatable = false)
)
public class AnalysisRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_request_id")
    private Long id;

    @Column(name = "request_id", nullable = false, unique = true)
    private String requestId;

    public AnalysisRequest() {
        this.requestId = UUID.randomUUID().toString();
    }
}