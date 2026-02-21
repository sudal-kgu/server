package store.sonyk9919.api.domain.taxonomy.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashTaxonomy;

public interface TrashTaxonomyRepository extends Repository<TrashTaxonomy, Long> {

    int count();

    @EntityGraph(attributePaths = { "category", "subCategory" })
    List<TrashTaxonomy> findAll();

    @EntityGraph(attributePaths = { "category", "subCategory" })
    Optional<TrashTaxonomy> getTrashTaxonomiesByCategory_NameAndSubCategory_Alias(String name, String alias);

    @Query("""
            SELECT t FROM TrashTaxonomy t
            JOIN FETCH t.category c
            JOIN FETCH t.subCategory s
            WHERE CONCAT(c.name, ':', s.alias) IN :keys
            """)
    List<TrashTaxonomy> findAllByExactCategoryAndSubcategoryPairs(@Param("keys") List<String> keys);
}
