package store.sonyk9919.api.domain.trash.repository;

import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.analysis.entity.QAnalysisRequest;
import store.sonyk9919.api.domain.disposal.entity.*;
import store.sonyk9919.api.domain.trash.dto.QTrashDetailDto;
import store.sonyk9919.api.domain.trash.dto.TrashDetailDto;
import store.sonyk9919.api.global.language.type.Language;

import java.util.Map;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.*;
import static store.sonyk9919.api.domain.analysis.entity.QAnalysisRequest.*;
import static store.sonyk9919.api.domain.disposal.entity.QTrashDisposalCategory.trashDisposalCategory;
import static store.sonyk9919.api.domain.disposal.entity.QTrashDisposalSubCategory.trashDisposalSubCategory;
import static store.sonyk9919.api.domain.taxonomy.entity.QTrashTaxonomy.trashTaxonomy;
import static store.sonyk9919.api.domain.trash.entity.QTrash.trash;

@Repository
@RequiredArgsConstructor
public class TrashSearchRepository {

    private final static QTrashDisposal categoryDisposal = new QTrashDisposal("categoryDisposal");
    private final static QTrashDisposal subcategoryDisposal = new QTrashDisposal("subcategoryDisposal");

    private final JPAQueryFactory factory;

    public Optional<TrashDetailDto> findBy(String serial, Language language) {
        Map<String, TrashDetailDto> results = factory
                .from(trash)
                .join(trash.taxonomy, trashTaxonomy)
                .join(trash.analysisRequest, analysisRequest)
                .leftJoin(trashDisposalCategory).on(trashDisposalCategory.category.eq(trashTaxonomy.category))
                .leftJoin(trashDisposalCategory.disposal, categoryDisposal)
                .leftJoin(trashDisposalSubCategory).on(trashDisposalSubCategory.subCategory.eq(trashTaxonomy.subCategory))
                .leftJoin(trashDisposalSubCategory.disposal, subcategoryDisposal)
                .where(trash.trashUuid.eq(serial))
                .transform(
                        groupBy(trash.trashUuid).as(new QTrashDetailDto(
                                selectByLang(trashTaxonomy.category.ko, trashTaxonomy.category.en, language),
                                selectByLang(trashTaxonomy.subCategory.ko, trashTaxonomy.subCategory.en, language),
                                trash.analysisRequest.requestId,
                                trash.filename,
                                set(selectByLang(categoryDisposal.ko, categoryDisposal.en, language)),
                                set(selectByLang(subcategoryDisposal.ko, subcategoryDisposal.en, language))
                        ))
                );

        return Optional.ofNullable(results.get(serial));
    }

    private StringExpression selectByLang(StringPath ko, StringPath en, Language language) {
        return new CaseBuilder()
                .when(Expressions.asString(language.getKey()).eq(Language.KO.getKey())).then(ko)
                .when(Expressions.asString(language.getKey()).eq(Language.EN.getKey())).then(en)
                .otherwise(ko);
    }
}
