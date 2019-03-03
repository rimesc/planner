package uk.co.zoneofavoidance.planner.repository;

import uk.co.zoneofavoidance.planner.domain.Note;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Note entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("select note from Note note where note.owner.login = ?#{principal.username}")
    List<Note> findByOwnerIsCurrentUser();

}
