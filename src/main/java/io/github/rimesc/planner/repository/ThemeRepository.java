package io.github.rimesc.planner.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import io.github.rimesc.planner.domain.Theme;

import java.util.List;

/**
 * Spring Data  repository for the Theme entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long>, JpaSpecificationExecutor<Theme> {

    @Query("select theme from Theme theme where theme.owner.login = ?#{principal.username}")
    List<Theme> findByOwnerIsCurrentUser();

}
