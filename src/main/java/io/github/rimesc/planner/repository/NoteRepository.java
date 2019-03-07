package io.github.rimesc.planner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.github.rimesc.planner.domain.Note;

/**
 * Spring Data  repository for the Note entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("select note from Note note where note.owner.login = ?#{principal.username}")
    List<Note> findByOwnerIsCurrentUser();

}
