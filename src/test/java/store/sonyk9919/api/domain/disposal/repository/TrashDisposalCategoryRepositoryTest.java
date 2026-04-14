package store.sonyk9919.api.domain.disposal.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import store.sonyk9919.api.domain.taxonomy.repository.TrashCategoryRepository;
import store.sonyk9919.api.global.config.QueryDslConfig;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(QueryDslConfig.class)
class TrashDisposalCategoryRepositoryTest {

    @Autowired private TrashDisposalCategoryRepository trashDisposalCategoryRepository;
    @Autowired private TrashCategoryRepository trashCategoryRepository;

    @Test
    @DisplayName("카테고리에 대한 처리방법은 항상 존재")
    public void isCountCorrect() {
        assertThat(trashDisposalCategoryRepository.count()).isEqualTo(trashCategoryRepository.count());
    }
}