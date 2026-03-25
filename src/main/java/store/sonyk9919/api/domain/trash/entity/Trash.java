package store.sonyk9919.api.domain.trash.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.analysis.entity.AnalysisResult;
import store.sonyk9919.api.domain.taxonomy.entity.TrashTaxonomy;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trash_id")
    private Long id;

    @Column(name = "trash_uuid", updatable = false, nullable = false)
    private String trashUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private AnalysisRequest analysisRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id", nullable = true)
    private AnalysisResult analysisResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxonomy_id", nullable = false)
    private TrashTaxonomy taxonomy;

    @Column(nullable = false)
    private String filename;

    private Trash(AnalysisRequest analysisRequest, TrashTaxonomy taxonomy, String filename) {
        this.trashUuid = UUID.randomUUID().toString();
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

    public boolean isConfirmed(){
        return analysisResult != null;
    }
}