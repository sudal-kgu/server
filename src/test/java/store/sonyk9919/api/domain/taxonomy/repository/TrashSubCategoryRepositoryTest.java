package store.sonyk9919.api.domain.taxonomy.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import store.sonyk9919.api.global.config.QueryDslConfig;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
@Import(QueryDslConfig.class)
class TrashSubCategoryRepositoryTest {

    @Autowired
    private TrashSubCategoryRepository trashSubCategoryRepository;

    @Test
    @DisplayName("전체 SubCategory 54개")
    void isCountCorrect() { assertThat(trashSubCategoryRepository.count()).isEqualTo(54); }
}