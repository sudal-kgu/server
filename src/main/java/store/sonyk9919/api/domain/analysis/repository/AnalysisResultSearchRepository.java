package store.sonyk9919.api.domain.analysis.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import store.sonyk9919.api.domain.trash.entity.Trash;

import java.util.List;

import static store.sonyk9919.api.domain.analysis.entity.QAnalysisResult.*;
import static store.sonyk9919.api.domain.taxonomy.entitiy.QTrashCategory.*;
import static store.sonyk9919.api.domain.taxonomy.entitiy.QTrashSubCategory.*;
import static store.sonyk9919.api.domain.taxonomy.entitiy.QTrashTaxonomy.*;
import static store.sonyk9919.api.domain.trash.entity.QTrash.*;

@Repository
@RequiredArgsConstructor
public class AnalysisResultSearchRepository {

    private final JPAQueryFactory factory;

    public Page<Trash> findAllBy(String serial, Pageable pageable) {
        List<Trash> contents = factory.selectFrom(trash)
                .join(trash.analysisResult, analysisResult)
                .join(trash.taxonomy, trashTaxonomy)
                .join(trashTaxonomy.category, trashCategory)
                .join(trashTaxonomy.subCategory, trashSubCategory)
                .where(trash.analysisResult.serial.eq(serial))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = factory.select(trash.count())
                .from(trash)
                .join(trash.analysisResult, analysisResult)
                .where(trash.analysisResult.serial.eq(serial))
                .fetchOne();

        return PageableExecutionUtils.getPage(contents, pageable, () -> total);
    }
}
