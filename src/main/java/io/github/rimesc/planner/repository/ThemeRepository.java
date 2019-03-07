package io.github.rimesc.planner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.github.rimesc.planner.domain.Theme;

/**
 * Spring Data  repository for the Theme entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long>, JpaSpecificationExecutor<Theme> {

    @Query("select theme from Theme theme where theme.owner.login = ?#{principal.username}")
    List<Theme> findByOwnerIsCurrentUser();

}
