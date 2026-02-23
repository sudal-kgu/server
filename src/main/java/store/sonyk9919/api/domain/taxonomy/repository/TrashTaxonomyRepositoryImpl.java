package store.sonyk9919.api.domain.taxonomy.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import store.sonyk9919.api.domain.analysis.dto.DetectedItemDto;
import store.sonyk9919.api.domain.taxonomy.entitiy.QTrashCategory;
import store.sonyk9919.api.domain.taxonomy.entitiy.QTrashSubCategory;
import store.sonyk9919.api.domain.taxonomy.entitiy.QTrashTaxonomy;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashTaxonomy;
import store.sonyk9919.api.domain.taxonomy.repository.custom.TrashTaxonomyRepositoryCustom;

@Repository
@RequiredArgsConstructor
public class TrashTaxonomyRepositoryImpl implements TrashTaxonomyRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private static final QTrashTaxonomy taxonomy = QTrashTaxonomy.trashTaxonomy;
    private static final QTrashCategory category = QTrashCategory.trashCategory;
    private static final QTrashSubCategory subCategory = QTrashSubCategory.trashSubCategory;

    @Override
    public List<TrashTaxonomy> findAllByExactCategoryAndSubcategoryPairs(List<DetectedItemDto> detectedItems) {
        BooleanExpression condition = buildOrCondition(detectedItems);

        if (condition == null) {
            return Collections.emptyList();
        }

        return queryFactory
                .selectFrom(taxonomy)
                .join(taxonomy.category, category).fetchJoin()
                .join(taxonomy.subCategory, subCategory).fetchJoin()
                .where(condition)
                .fetch();
    }

    private BooleanExpression buildOrCondition(List<DetectedItemDto> detectedItems) {
        if (detectedItems == null || detectedItems.isEmpty()) return null;

        return detectedItems.stream()
                .distinct()
                .map(item -> category.name.eq(item.getCategory())
                        .and(subCategory.alias.eq(item.getSubcategory())))
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}