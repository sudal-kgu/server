package store.sonyk9919.api.domain.disposal.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import store.sonyk9919.api.global.config.QueryDslConfig;

@DataJpaTest
@Import(QueryDslConfig.class)
class TrashDisposalSubCategoryRepositoryTest {

    @Autowired private TrashDisposalSubCategoryRepository trashDisposalSubCategoryRepository;

    @Test
    @DisplayName("더 자세한 처리방법이 있는 소분류는 39개")
    public void isCountCorrect() {
        Assertions.assertThat(trashDisposalSubCategoryRepository.count()).isEqualTo(39);
    }
}