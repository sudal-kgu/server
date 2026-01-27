package store.sonyk9919.api.domain.taxonomy.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashTaxonomy;

import java.util.List;
import java.util.Optional;

public interface TrashTaxonomyRepository extends Repository<TrashTaxonomy, Long> {

    int count();

    @EntityGraph(attributePaths = { "category", "subCategory" })
    List<TrashTaxonomy> findAll();

    @EntityGraph(attributePaths = { "category", "subCategory" })
    Optional<TrashTaxonomy> getTrashTaxonomiesByCategory_NameAndSubCategory_Alias(String name, String alias);
}
