package io.github.rimesc.planner.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.github.jhipster.web.util.ResponseUtil;
import io.github.rimesc.planner.service.NoteQueryService;
import io.github.rimesc.planner.service.NoteService;
import io.github.rimesc.planner.service.dto.NoteCriteria;
import io.github.rimesc.planner.service.dto.NoteDTO;
import io.github.rimesc.planner.web.rest.errors.BadRequestAlertException;
import io.github.rimesc.planner.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Note.
 */
@RestController
@RequestMapping("/api")
public class NoteResource {

    private final Logger log = LoggerFactory.getLogger(NoteResource.class);

    private static final String ENTITY_NAME = "note";

    private final NoteService noteService;

    private final NoteQueryService noteQueryService;

    public NoteResource(NoteService noteService, NoteQueryService noteQueryService) {
        this.noteService = noteService;
        this.noteQueryService = noteQueryService;
    }

    /**
     * POST  /notes : Create a new note.
     *
     * @param noteDTO the noteDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new noteDTO, or with status 400 (Bad Request) if the note has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notes")
    public ResponseEntity<NoteDTO> createNote(@Valid @RequestBody NoteDTO noteDTO) throws URISyntaxException {
        log.debug("REST request to save Note : {}", noteDTO);
        if (noteDTO.getId() != null) {
            throw new BadRequestAlertException("A new note cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NoteDTO result = noteService.save(noteDTO);
        return ResponseEntity.created(new URI("/api/notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notes : Updates an existing note.
     *
     * @param noteDTO the noteDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated noteDTO,
     * or with status 400 (Bad Request) if the noteDTO is not valid,
     * or with status 500 (Internal Server Error) if the noteDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notes")
    public ResponseEntity<NoteDTO> updateNote(@Valid @RequestBody NoteDTO noteDTO) throws URISyntaxException {
        log.debug("REST request to update Note : {}", noteDTO);
        if (noteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NoteDTO result = noteService.save(noteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, noteDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notes : get all the notes.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of notes in body
     */
    @GetMapping("/notes")
    public ResponseEntity<List<NoteDTO>> getAllNotes(NoteCriteria criteria) {
        log.debug("REST request to get Notes by criteria: {}", criteria);
        List<NoteDTO> entityList = noteQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /notes/count : count all the notes.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/notes/count")
    public ResponseEntity<Long> countNotes(NoteCriteria criteria) {
        log.debug("REST request to count Notes by criteria: {}", criteria);
        return ResponseEntity.ok().body(noteQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /notes/:id : get the "id" note.
     *
     * @param id the id of the noteDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the noteDTO, or with status 404 (Not Found)
     */
    @GetMapping("/notes/{id}")
    public ResponseEntity<NoteDTO> getNote(@PathVariable Long id) {
        log.debug("REST request to get Note : {}", id);
        Optional<NoteDTO> noteDTO = noteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(noteDTO);
    }

    /**
     * DELETE  /notes/:id : delete the "id" note.
     *
     * @param id the id of the noteDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notes/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        log.debug("REST request to delete Note : {}", id);
        noteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
