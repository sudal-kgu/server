package store.sonyk9919.api.domain.trash.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.analysis.entity.AnalysisResult;

@Entity
@Getter
@NoArgsConstructor
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private TrashCategory trashCategory;

    private String label;
    private String material;
    private String filename;

    @Builder
    public Trash(AnalysisRequest analysisRequest, TrashCategory trashCategory,
                 String label, String material, String filename) {
        this.analysisRequest = analysisRequest;
        this.trashCategory = trashCategory;
        this.label = label;
        this.material = material;
        this.filename = filename;
    }

    public void confirmResult(AnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }
}