package store.sonyk9919.api.domain.taxonomy.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashTaxonomy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class TrashTaxonomyRepositoryTest {

    @Autowired
    private TrashTaxonomyRepository trashTaxonomyRepository;

    @Test
    @DisplayName("전체 Taxonomy 조합은 274개")
    void isCountCorrect() {
        assertThat(trashTaxonomyRepository.count()).isEqualTo(274);
    }

    @Test
    @DisplayName("Taxonomy에서 Category의 Name과 SubCategory의 alias 조합은 항상 Unique 함")
    void isAllPairUnique() {
        List<TrashTaxonomy> allTaxonomyList = trashTaxonomyRepository.findAll();

        Set<String> allTexonomySet = allTaxonomyList.stream()
                .map(taxonomy -> taxonomy.getCategory().getName() + ":" + taxonomy.getSubCategory().getAlias())
                .collect(Collectors.toSet());

        assertThat(allTaxonomyList.size()).isEqualTo(allTexonomySet.size());
    }

    @ParameterizedTest(name = "name = {0}, alias = {1}")
    @CsvSource({
            "플라스틱 용기류, Bottle", "플라스틱 용기류, Container", "플라스틱 용기류, Cosmetics",
            "유리병류, Bottle", "비닐류, Balloon", "금속캔, Beer"
    })
    @DisplayName("카테고리 이름과 서브 카테고리의 별칭으로 최종 분류를 찾을 수 있음")
    void findTaxonomy(String name, String alias) {
        TrashTaxonomy taxonomy = trashTaxonomyRepository
                .getTrashTaxonomiesByCategory_NameAndSubCategory_Alias(name, alias)
                .orElse(null);

        assertThat(taxonomy).isNotNull();
        assertThat(taxonomy.getCategory().getName()).isEqualTo(name);
        assertThat(taxonomy.getSubCategory().getAlias()).isEqualTo(alias);
    }

    @ParameterizedTest(name = "name = {0}, alias = {1}")
    @CsvSource({
            "플라스틱, Bottle", "용기류, Container", "금속캔, Cosmetics",
            "비닐류, Bottle", "유리병류, Balloon", "비닐류, Beer"
    })
    @DisplayName("없는 조합으로 검색 시 Null 값이 반환")
    void notFoundTaxonomy(String name, String alias) {
        TrashTaxonomy taxonomy = trashTaxonomyRepository
                .getTrashTaxonomiesByCategory_NameAndSubCategory_Alias(name, alias)
                .orElse(null);

        assertThat(taxonomy).isNull();
    }

}