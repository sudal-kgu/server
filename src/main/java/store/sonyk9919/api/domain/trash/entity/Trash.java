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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private TrashCategory trashCategory;

    private String label;
    private String material;
    private String filename;

    private Trash(AnalysisRequest analysisRequest, TrashCategory trashCategory,
                 String label, String material, String filename) {
        this.analysisRequest = analysisRequest;
        this.trashCategory = trashCategory;
        this.label = label;
        this.material = material;
        this.filename = filename;
    }

    public static Trash create(AnalysisRequest analysisRequest, TrashCategory trashCategory,
                               String label, String material, String filename) {
        return new Trash(analysisRequest,trashCategory,label,material,filename);
    }

    public void confirmResult(AnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }
}