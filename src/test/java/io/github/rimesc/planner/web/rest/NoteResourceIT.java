package io.github.rimesc.planner.web.rest;

import static io.github.rimesc.planner.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import io.github.rimesc.planner.PlannerApp;
import io.github.rimesc.planner.domain.Goal;
import io.github.rimesc.planner.domain.Note;
import io.github.rimesc.planner.domain.User;
import io.github.rimesc.planner.domain.enumeration.Visibility;
import io.github.rimesc.planner.repository.NoteRepository;
import io.github.rimesc.planner.service.NoteQueryService;
import io.github.rimesc.planner.service.NoteService;
import io.github.rimesc.planner.service.dto.NoteDTO;
import io.github.rimesc.planner.service.mapper.NoteMapper;
import io.github.rimesc.planner.web.rest.errors.ExceptionTranslator;

/**
 * Integration tests for the {@link NoteResource} REST controller.
 */
@SpringBootTest(classes = PlannerApp.class)
public class NoteResourceIT {

    private static final String DEFAULT_MARKDOWN = "AAAAAAAAAA";
    private static final String UPDATED_MARKDOWN = "BBBBBBBBBB";

    private static final String DEFAULT_HTML = "AAAAAAAAAA";
    private static final String UPDATED_HTML = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EDITED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EDITED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Visibility DEFAULT_VISIBILITY = Visibility.PUBLIC;
    private static final Visibility UPDATED_VISIBILITY = Visibility.PRIVATE;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteQueryService noteQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restNoteMockMvc;

    private Note note;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NoteResource noteResource = new NoteResource(noteService, noteQueryService);
        this.restNoteMockMvc = MockMvcBuilders.standaloneSetup(noteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Note createEntity(EntityManager em) {
        Note note = new Note()
            .markdown(DEFAULT_MARKDOWN)
            .html(DEFAULT_HTML)
            .createdAt(DEFAULT_CREATED_AT)
            .editedAt(DEFAULT_EDITED_AT)
            .visibility(DEFAULT_VISIBILITY);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        note.setOwner(user);
        // Add required entity
        Goal goal;
        if (TestUtil.findAll(em, Goal.class).isEmpty()) {
            goal = GoalResourceIT.createEntity(em);
            em.persist(goal);
            em.flush();
        } else {
            goal = TestUtil.findAll(em, Goal.class).get(0);
        }
        note.setGoal(goal);
        return note;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Note createUpdatedEntity(EntityManager em) {
        Note note = new Note()
            .markdown(UPDATED_MARKDOWN)
            .html(UPDATED_HTML)
            .createdAt(UPDATED_CREATED_AT)
            .editedAt(UPDATED_EDITED_AT)
            .visibility(UPDATED_VISIBILITY);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        note.setOwner(user);
        // Add required entity
        Goal goal;
        if (TestUtil.findAll(em, Goal.class).isEmpty()) {
            goal = GoalResourceIT.createUpdatedEntity(em);
            em.persist(goal);
            em.flush();
        } else {
            goal = TestUtil.findAll(em, Goal.class).get(0);
        }
        note.setGoal(goal);
        return note;
    }

    @BeforeEach
    public void initTest() {
        note = createEntity(em);
    }

    @Test
    @Transactional
    public void createNote() throws Exception {
        int databaseSizeBeforeCreate = noteRepository.findAll().size();

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);
        restNoteMockMvc.perform(post("/api/notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(noteDTO)))
            .andExpect(status().isCreated());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeCreate + 1);
        Note testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getMarkdown()).isEqualTo(DEFAULT_MARKDOWN);
        assertThat(testNote.getHtml()).isEqualTo(DEFAULT_HTML);
        assertThat(testNote.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testNote.getEditedAt()).isEqualTo(DEFAULT_EDITED_AT);
        assertThat(testNote.getVisibility()).isEqualTo(DEFAULT_VISIBILITY);
    }

    @Test
    @Transactional
    public void createNoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = noteRepository.findAll().size();

        // Create the Note with an existing ID
        note.setId(1L);
        NoteDTO noteDTO = noteMapper.toDto(note);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNoteMockMvc.perform(post("/api/notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(noteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = noteRepository.findAll().size();
        // set the field null
        note.setCreatedAt(null);

        // Create the Note, which fails.
        NoteDTO noteDTO = noteMapper.toDto(note);

        restNoteMockMvc.perform(post("/api/notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(noteDTO)))
            .andExpect(status().isBadRequest());

        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVisibilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = noteRepository.findAll().size();
        // set the field null
        note.setVisibility(null);

        // Create the Note, which fails.
        NoteDTO noteDTO = noteMapper.toDto(note);

        restNoteMockMvc.perform(post("/api/notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(noteDTO)))
            .andExpect(status().isBadRequest());

        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotes() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList
        restNoteMockMvc.perform(get("/api/notes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(note.getId().intValue())))
            .andExpect(jsonPath("$.[*].markdown").value(hasItem(DEFAULT_MARKDOWN.toString())))
            .andExpect(jsonPath("$.[*].html").value(hasItem(DEFAULT_HTML.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].editedAt").value(hasItem(DEFAULT_EDITED_AT.toString())))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())));
    }

    @Test
    @Transactional
    public void getNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get the note
        restNoteMockMvc.perform(get("/api/notes/{id}", note.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(note.getId().intValue()))
            .andExpect(jsonPath("$.markdown").value(DEFAULT_MARKDOWN.toString()))
            .andExpect(jsonPath("$.html").value(DEFAULT_HTML.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.editedAt").value(DEFAULT_EDITED_AT.toString()))
            .andExpect(jsonPath("$.visibility").value(DEFAULT_VISIBILITY.toString()));
    }

    @Test
    @Transactional
    public void getNotesByIdFiltering() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        Long id = note.getId();

        defaultNoteShouldBeFound("id.equals=" + id);
        defaultNoteShouldNotBeFound("id.notEquals=" + id);

        defaultNoteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNoteShouldNotBeFound("id.greaterThan=" + id);

        defaultNoteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNoteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllNotesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where createdAt equals to DEFAULT_CREATED_AT
        defaultNoteShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the noteList where createdAt equals to UPDATED_CREATED_AT
        defaultNoteShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllNotesByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where createdAt not equals to DEFAULT_CREATED_AT
        defaultNoteShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the noteList where createdAt not equals to UPDATED_CREATED_AT
        defaultNoteShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllNotesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultNoteShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the noteList where createdAt equals to UPDATED_CREATED_AT
        defaultNoteShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllNotesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where createdAt is not null
        defaultNoteShouldBeFound("createdAt.specified=true");

        // Get all the noteList where createdAt is null
        defaultNoteShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotesByEditedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where editedAt equals to DEFAULT_EDITED_AT
        defaultNoteShouldBeFound("editedAt.equals=" + DEFAULT_EDITED_AT);

        // Get all the noteList where editedAt equals to UPDATED_EDITED_AT
        defaultNoteShouldNotBeFound("editedAt.equals=" + UPDATED_EDITED_AT);
    }

    @Test
    @Transactional
    public void getAllNotesByEditedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where editedAt not equals to DEFAULT_EDITED_AT
        defaultNoteShouldNotBeFound("editedAt.notEquals=" + DEFAULT_EDITED_AT);

        // Get all the noteList where editedAt not equals to UPDATED_EDITED_AT
        defaultNoteShouldBeFound("editedAt.notEquals=" + UPDATED_EDITED_AT);
    }

    @Test
    @Transactional
    public void getAllNotesByEditedAtIsInShouldWork() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where editedAt in DEFAULT_EDITED_AT or UPDATED_EDITED_AT
        defaultNoteShouldBeFound("editedAt.in=" + DEFAULT_EDITED_AT + "," + UPDATED_EDITED_AT);

        // Get all the noteList where editedAt equals to UPDATED_EDITED_AT
        defaultNoteShouldNotBeFound("editedAt.in=" + UPDATED_EDITED_AT);
    }

    @Test
    @Transactional
    public void getAllNotesByEditedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where editedAt is not null
        defaultNoteShouldBeFound("editedAt.specified=true");

        // Get all the noteList where editedAt is null
        defaultNoteShouldNotBeFound("editedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotesByVisibilityIsEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where visibility equals to DEFAULT_VISIBILITY
        defaultNoteShouldBeFound("visibility.equals=" + DEFAULT_VISIBILITY);

        // Get all the noteList where visibility equals to UPDATED_VISIBILITY
        defaultNoteShouldNotBeFound("visibility.equals=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllNotesByVisibilityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where visibility not equals to DEFAULT_VISIBILITY
        defaultNoteShouldNotBeFound("visibility.notEquals=" + DEFAULT_VISIBILITY);

        // Get all the noteList where visibility not equals to UPDATED_VISIBILITY
        defaultNoteShouldBeFound("visibility.notEquals=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllNotesByVisibilityIsInShouldWork() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where visibility in DEFAULT_VISIBILITY or UPDATED_VISIBILITY
        defaultNoteShouldBeFound("visibility.in=" + DEFAULT_VISIBILITY + "," + UPDATED_VISIBILITY);

        // Get all the noteList where visibility equals to UPDATED_VISIBILITY
        defaultNoteShouldNotBeFound("visibility.in=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllNotesByVisibilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where visibility is not null
        defaultNoteShouldBeFound("visibility.specified=true");

        // Get all the noteList where visibility is null
        defaultNoteShouldNotBeFound("visibility.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotesByOwnerIsEqualToSomething() throws Exception {
        // Get already existing entity
        User owner = note.getOwner();
        noteRepository.saveAndFlush(note);
        Long ownerId = owner.getId();

        // Get all the noteList where owner equals to ownerId
        defaultNoteShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the noteList where owner equals to ownerId + 1
        defaultNoteShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    @Test
    @Transactional
    public void getAllNotesByGoalIsEqualToSomething() throws Exception {
        // Get already existing entity
        Goal goal = note.getGoal();
        noteRepository.saveAndFlush(note);
        Long goalId = goal.getId();

        // Get all the noteList where goal equals to goalId
        defaultNoteShouldBeFound("goalId.equals=" + goalId);

        // Get all the noteList where goal equals to goalId + 1
        defaultNoteShouldNotBeFound("goalId.equals=" + (goalId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNoteShouldBeFound(String filter) throws Exception {
        restNoteMockMvc.perform(get("/api/notes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(note.getId().intValue())))
            .andExpect(jsonPath("$.[*].markdown").value(hasItem(DEFAULT_MARKDOWN.toString())))
            .andExpect(jsonPath("$.[*].html").value(hasItem(DEFAULT_HTML.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].editedAt").value(hasItem(DEFAULT_EDITED_AT.toString())))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())));

        // Check, that the count call also returns 1
        restNoteMockMvc.perform(get("/api/notes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNoteShouldNotBeFound(String filter) throws Exception {
        restNoteMockMvc.perform(get("/api/notes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNoteMockMvc.perform(get("/api/notes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingNote() throws Exception {
        // Get the note
        restNoteMockMvc.perform(get("/api/notes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        int databaseSizeBeforeUpdate = noteRepository.findAll().size();

        // Update the note
        Note updatedNote = noteRepository.findById(note.getId()).get();
        // Disconnect from session so that the updates on updatedNote are not directly saved in db
        em.detach(updatedNote);
        updatedNote
            .markdown(UPDATED_MARKDOWN)
            .html(UPDATED_HTML)
            .createdAt(UPDATED_CREATED_AT)
            .editedAt(UPDATED_EDITED_AT)
            .visibility(UPDATED_VISIBILITY);
        NoteDTO noteDTO = noteMapper.toDto(updatedNote);

        restNoteMockMvc.perform(put("/api/notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(noteDTO)))
            .andExpect(status().isOk());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
        Note testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getMarkdown()).isEqualTo(UPDATED_MARKDOWN);
        assertThat(testNote.getHtml()).isEqualTo(UPDATED_HTML);
        assertThat(testNote.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testNote.getEditedAt()).isEqualTo(UPDATED_EDITED_AT);
        assertThat(testNote.getVisibility()).isEqualTo(UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void updateNonExistingNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().size();

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNoteMockMvc.perform(put("/api/notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(noteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        int databaseSizeBeforeDelete = noteRepository.findAll().size();

        // Delete the note
        restNoteMockMvc.perform(delete("/api/notes/{id}", note.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
