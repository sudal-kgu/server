package store.sonyk9919.api.domain.taxonomy.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TrashCategoryRepositoryTest {

    @Autowired
    private TrashCategoryRepository trashCategoryRepository;

    @Test
    @DisplayName("전체 Category는 12개")
    void isCountCorrect() {
        assertThat(trashCategoryRepository.count()).isEqualTo(12);
    }

    @ParameterizedTest(name = "카테고리: {0}")
    @ValueSource(strings = {
            "가구류", "고철류", "불연성 종량제", "비닐류", "스티로폼류",
            "유리병류", "전용함", "종량제", "종이류",
            "캔류", "폐가전제품", "플라스틱류"
    })
    @DisplayName("필수 기초 카테고리 데이터가 모두 존재해야 함")
    void allCategoriesMustExist(String categoryName) {
        assertThat(trashCategoryRepository.existsByName(categoryName)).isTrue();
    }
}