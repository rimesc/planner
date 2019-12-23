package io.github.rimesc.planner.repository;

import io.github.rimesc.planner.domain.Task;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Task entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select task from Task task where task.owner.login = ?#{principal.username}")
    List<Task> findByOwnerIsCurrentUser();

}
