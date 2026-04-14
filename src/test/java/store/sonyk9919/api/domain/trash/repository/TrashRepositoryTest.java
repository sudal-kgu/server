package store.sonyk9919.api.domain.trash.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.analysis.repository.AnalysisRequestRepository;
import store.sonyk9919.api.domain.taxonomy.entity.TrashTaxonomy;
import store.sonyk9919.api.domain.taxonomy.repository.TrashTaxonomyRepository;
import store.sonyk9919.api.domain.trash.entity.Trash;
import store.sonyk9919.api.global.config.QueryDslConfig;

@DataJpaTest
@Transactional
@Import(QueryDslConfig.class)
class TrashRepositoryTest {
    @Autowired
    private TrashRepository trashRepository;

    @Autowired
    private AnalysisRequestRepository analysisRequestRepository;

    @Autowired
    private TrashTaxonomyRepository trashTaxonomyRepository;

    @Test
    @DisplayName("request_id로 분석된 쓰레기 데이터들을 조회")
    void findByAnalysisRequest_RequestId_SimpleTest() {
        AnalysisRequest request = AnalysisRequest.createWithUUID();
        analysisRequestRepository.save(request);

        TrashTaxonomy taxonomy = trashTaxonomyRepository.findAll().get(0);

        Trash trash1 = Trash.create(request, taxonomy, "crop01.png");
        Trash trash2 = Trash.create(request, taxonomy, "crop02.png");
        trashRepository.save(trash1);
        trashRepository.save(trash2);

        String requestId = request.getRequestId();
        List<Trash> result = trashRepository.findByAnalysisRequest_RequestId(requestId);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Trash::getFilename)
                .containsExactlyInAnyOrder("crop01.png", "crop02.png");
    }
}