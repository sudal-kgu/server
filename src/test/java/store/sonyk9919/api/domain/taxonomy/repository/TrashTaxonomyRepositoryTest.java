package store.sonyk9919.api.domain.taxonomy.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import store.sonyk9919.api.domain.analysis.dto.DetectedItemDto;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashTaxonomy;
import store.sonyk9919.api.global.language.type.Language;

@SpringBootTest
class TrashTaxonomyRepositoryTest {

    @Autowired
    private TrashTaxonomyRepository trashTaxonomyRepository;

    @Test
    @DisplayName("전체 Taxonomy 조합은 54")
    void isCountCorrect() {
        assertThat(trashTaxonomyRepository.count()).isEqualTo(54);
    }

    @Test
    @DisplayName("Taxonomy에서 Category의 Name과 SubCategory의 Name의 조합은 항상 Unique 함")
    void isAllPairUnique() {
        List<TrashTaxonomy> allTaxonomyList = trashTaxonomyRepository.findAll();

        Set<String> allTexonomySet = allTaxonomyList.stream()
                .map(taxonomy -> taxonomy.getCategory().getName() + ":" + taxonomy.getSubCategory().getName())
                .collect(Collectors.toSet());

        assertThat(allTaxonomyList.size()).isEqualTo(allTexonomySet.size());
    }

    @ParameterizedTest(name = "categoryName = {0}, subCategoryName = {1}")
    @CsvSource({
            "전용함, 일회용컵(페트병류)", "전용함, 일반페트병(페트병류)", "플라스틱류, 대용량통(플라스틱)",
            "플라스틱류, 밀폐용기(플라스틱)", "비닐류, 식품봉지(비닐)", "비닐류, '리필용기(비닐)'"
    })
    @DisplayName("카테고리 이름과 서브 카테고리의 이름으로 최종 분류를 찾을 수 있음")
    void findTaxonomy(String categoryName, String subCategoryName) {
        Map<String, TrashTaxonomy> allTaxonomy = trashTaxonomyRepository
                .findAllTaxonomy(List.of(DetectedItemDto.of(categoryName, subCategoryName)));

        assertThat(allTaxonomy.size()).isNotZero();
        TrashTaxonomy taxonomy = allTaxonomy.get(TrashTaxonomy.generateKey(categoryName, subCategoryName));

        assertThat(taxonomy).isNotNull();
        assertThat(taxonomy.getCategory().getName()).isEqualTo(categoryName);
        assertThat(taxonomy.getSubCategory().getName()).isEqualTo(subCategoryName);
    }

    @ParameterizedTest(name = "categoryName = {0}, subCategoryName = {1}")
    @CsvSource({
            "플라스틱, Bottle", "용기류, Container", "금속캔, Cosmetics",
            "비닐류, Bottle", "유리병류, Balloon", "비닐류, Beer"
    })
    @DisplayName("없는 조합으로 검색 시 Null 값이 반환")
    void notFoundTaxonomy(String categoryName, String subCategoryName) {
        Map<String, TrashTaxonomy> allTaxonomy = trashTaxonomyRepository
                .findAllTaxonomy(List.of(DetectedItemDto.of(categoryName, subCategoryName)));

        assertThat(allTaxonomy.size()).isZero();
    }

    @Test
    @DisplayName("category의 name과 subcategory의 alias의 조합한 키들(list)로 일치하는 taxonomy 다중 조회")
    void findTaxonomiesByCombinedKeys() {
        List<DetectedItemDto> detectedItems = List.of(
                DetectedItemDto.of("전용함", "일회용컵(페트병류)"),
                DetectedItemDto.of("전용함", "일반페트병(페트병류)")
        );

        Map<String, TrashTaxonomy> result = trashTaxonomyRepository.findAllTaxonomy(detectedItems);

        assertThat(result).hasSize(2);
        assertThat(result.keySet()).containsExactlyInAnyOrder(
                TrashTaxonomy.generateKey("전용함", "일회용컵(페트병류)"),
                TrashTaxonomy.generateKey("전용함", "일반페트병(페트병류)")
        );
        assertThat(result.values())
                .extracting(t -> t.getSubCategory().getName())
                .containsExactlyInAnyOrder("일회용컵(페트병류)", "일반페트병(페트병류)");
    }
}