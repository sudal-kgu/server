package store.sonyk9919.api.domain.trash.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.analysis.entity.AnalysisResult;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trash_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private AnalysisRequest analysisRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id", nullable = true)
    private AnalysisResult analysisResult;

    private String filename;

    private Trash(AnalysisRequest analysisRequest, String filename) {
        this.analysisRequest = analysisRequest;
        this.filename = filename;
    }

    public static Trash create(AnalysisRequest analysisRequest, String filename) {
        return new Trash(analysisRequest, filename);
    }

    public void confirmResult(AnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }
}