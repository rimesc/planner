package uk.co.zoneofavoidance.planner.repository;

import uk.co.zoneofavoidance.planner.domain.Tag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Tag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

}
