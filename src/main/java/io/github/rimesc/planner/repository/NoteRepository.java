package io.github.rimesc.planner.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import io.github.rimesc.planner.domain.Note;

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
