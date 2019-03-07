package io.github.rimesc.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.rimesc.planner.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
