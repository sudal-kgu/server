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
    @DisplayName("전체 Category는 18개")
    void isCountCorrect() {
        assertThat(trashCategoryRepository.count()).isEqualTo(18);
    }

    @ParameterizedTest(name = "카테고리: {0}")
    @ValueSource(strings = {
            "종이류", "종이팩", "금속캔", "고철", "유리병류",
            "플라스틱 용기류", "비닐류", "스티로폼", "의류 및 원단류",
            "폐가전제품", "대형 폐기물", "음식물 쓰레기", "불연성 종량제",
            "종량제봉투", "전용함", "전문시설", "주의", "재질별분리"
    })
    @DisplayName("필수 기초 카테고리 데이터가 모두 존재해야 함")
    void allCategoriesMustExist(String categoryName) {
        assertThat(trashCategoryRepository.existsByName(categoryName)).isTrue();
    }
}