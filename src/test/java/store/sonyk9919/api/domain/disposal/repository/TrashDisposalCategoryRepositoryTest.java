package store.sonyk9919.api.domain.disposal.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import store.sonyk9919.api.domain.taxonomy.repository.TrashCategoryRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TrashDisposalCategoryRepositoryTest {

    @Autowired private TrashDisposalCategoryRepository trashDisposalCategoryRepository;
    @Autowired private TrashCategoryRepository trashCategoryRepository;

    @Test
    @DisplayName("카테고리에 대한 처리방법은 항상 존재")
    public void isCountCorrect() {
        assertThat(trashDisposalCategoryRepository.count()).isEqualTo(trashCategoryRepository.count());
    }
}