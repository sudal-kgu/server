package store.sonyk9919.api.domain.taxonomy.repository;

import static com.querydsl.core.group.GroupBy.groupBy;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import store.sonyk9919.api.domain.analysis.dto.DetectedItemDto;
import store.sonyk9919.api.domain.taxonomy.entity.QTrashCategory;
import store.sonyk9919.api.domain.taxonomy.entity.QTrashSubCategory;
import store.sonyk9919.api.domain.taxonomy.entity.QTrashTaxonomy;
import store.sonyk9919.api.domain.taxonomy.entity.TrashTaxonomy;
import store.sonyk9919.api.domain.taxonomy.repository.custom.TrashTaxonomyRepositoryCustom;

@Repository
@RequiredArgsConstructor
public class TrashTaxonomyRepositoryImpl implements TrashTaxonomyRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private static final QTrashTaxonomy taxonomy = QTrashTaxonomy.trashTaxonomy;
    private static final QTrashCategory category = QTrashCategory.trashCategory;
    private static final QTrashSubCategory subCategory = QTrashSubCategory.trashSubCategory;

    @Override
    public Map<String, TrashTaxonomy> findAllTaxonomy(List<DetectedItemDto> detectedItems) {
        BooleanExpression condition = buildOrCondition(detectedItems);

        if (condition == null) {
            return Collections.emptyMap();
        }

        return queryFactory
                .selectFrom(taxonomy)
                .join(taxonomy.category, category).fetchJoin()
                .join(taxonomy.subCategory, subCategory).fetchJoin()
                .where(condition)
                .transform(groupBy(subCategory.ko).as(taxonomy));
    }

    private BooleanExpression buildOrCondition(List<DetectedItemDto> detectedItems) {
        return detectedItems.stream()
                .distinct()
                .map(item -> subCategory.ko.eq(item.getCategory()))
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}