package io.github.rimesc.planner.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.rimesc.planner.domain.Goal;

/**
 * Spring Data  repository for the Goal entity.
 */
@Repository
public interface GoalRepository extends JpaRepository<Goal, Long>, JpaSpecificationExecutor<Goal> {

    @Query("select goal from Goal goal where goal.owner.login = ?#{principal.username}")
    List<Goal> findByOwnerIsCurrentUser();

    @Query(value = "select distinct goal from Goal goal left join fetch goal.tags",
        countQuery = "select count(distinct goal) from Goal goal")
    Page<Goal> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct goal from Goal goal left join fetch goal.tags")
    List<Goal> findAllWithEagerRelationships();

    @Query("select goal from Goal goal left join fetch goal.tags where goal.id =:id")
    Optional<Goal> findOneWithEagerRelationships(@Param("id") Long id);

}
