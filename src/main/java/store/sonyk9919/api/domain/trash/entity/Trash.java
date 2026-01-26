package store.sonyk9919.api.domain.trash.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.analysis.entity.AnalysisResult;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashTaxonomy;

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
    @JoinColumn(name = "taxonomy_id")
    private TrashTaxonomy taxonomy;

    private String filename;

    private Trash(AnalysisRequest analysisRequest, TrashTaxonomy taxonomy, String filename) {
        this.analysisRequest = analysisRequest;
        this.taxonomy = taxonomy;
        this.filename = filename;
    }

    public static Trash create(AnalysisRequest analysisRequest, TrashTaxonomy taxonomy, String filename) {
        return new Trash(analysisRequest, taxonomy, filename);
    }

    public void confirmResult(AnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }
}