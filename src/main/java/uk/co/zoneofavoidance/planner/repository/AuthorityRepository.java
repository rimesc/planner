package uk.co.zoneofavoidance.planner.repository;

import uk.co.zoneofavoidance.planner.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
