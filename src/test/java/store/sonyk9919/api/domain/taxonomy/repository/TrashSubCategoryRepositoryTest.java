package store.sonyk9919.api.domain.taxonomy.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class TrashSubCategoryRepositoryTest {

    @Autowired
    private TrashSubCategoryRepository trashSubCategoryRepository;

    @Test
    @DisplayName("전체 SubCategory 54개")
    void isCountCorrect() { assertThat(trashSubCategoryRepository.count()).isEqualTo(54); }
}