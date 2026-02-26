package store.sonyk9919.api.domain.analysis.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.global.common.entity.BaseEntity;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnalysisProgress state;

    private AnalysisRequest(String requestId) {
        this.requestId = requestId;
        this.state = AnalysisProgress.PENDING;
    }

    public static AnalysisRequest createWithUUID() {
        return new AnalysisRequest(UUID.randomUUID().toString());
    }

    public void updateState(AnalysisProgress progress){
        this.state = progress;
    }
}