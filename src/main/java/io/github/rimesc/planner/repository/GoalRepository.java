package io.github.rimesc.planner.repository;

import io.github.rimesc.planner.domain.Goal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Goal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoalRepository extends JpaRepository<Goal, Long>, JpaSpecificationExecutor<Goal> {

    @Query("select goal from Goal goal where goal.owner.login = ?#{principal.username}")
    List<Goal> findByOwnerIsCurrentUser();

    @Query(value = "select distinct goal from Goal goal left join fetch goal.tags",
        countQuery = "select count(distinct goal) from Goal goal")
    Page<Goal> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct goal from Goal goal left join fetch goal.tags")
    List<Goal> findAllWithEagerRelationships();

    @Query("select goal from Goal goal left join fetch goal.tags where goal.id =:id")
    Optional<Goal> findOneWithEagerRelationships(@Param("id") Long id);

}
