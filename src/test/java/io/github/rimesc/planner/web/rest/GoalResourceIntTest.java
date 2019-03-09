package io.github.rimesc.planner.web.rest;

import static io.github.rimesc.planner.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import io.github.rimesc.planner.PlannerApp;
import io.github.rimesc.planner.domain.Goal;
import io.github.rimesc.planner.domain.Note;
import io.github.rimesc.planner.domain.Tag;
import io.github.rimesc.planner.domain.Task;
import io.github.rimesc.planner.domain.Theme;
import io.github.rimesc.planner.domain.User;
import io.github.rimesc.planner.domain.enumeration.Visibility;
import io.github.rimesc.planner.repository.GoalRepository;
import io.github.rimesc.planner.service.GoalQueryService;
import io.github.rimesc.planner.service.GoalService;
import io.github.rimesc.planner.service.dto.GoalDTO;
import io.github.rimesc.planner.service.mapper.GoalMapper;
import io.github.rimesc.planner.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the GoalResource REST controller.
 *
 * @see GoalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlannerApp.class)
public class GoalResourceIntTest {

    private static final String DEFAULT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_COMPLETED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_ORDER = 1L;
    private static final Long UPDATED_ORDER = 2L;

    private static final Visibility DEFAULT_VISIBILITY = Visibility.PUBLIC;
    private static final Visibility UPDATED_VISIBILITY = Visibility.PRIVATE;

    @Autowired
    private GoalRepository goalRepository;

    @Mock
    private GoalRepository goalRepositoryMock;

    @Autowired
    private GoalMapper goalMapper;

    @Mock
    private GoalService goalServiceMock;

    @Autowired
    private GoalService goalService;

    @Autowired
    private GoalQueryService goalQueryService;

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

    private MockMvc restGoalMockMvc;

    private Goal goal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GoalResource goalResource = new GoalResource(goalService, goalQueryService);
        this.restGoalMockMvc = MockMvcBuilders.standaloneSetup(goalResource)
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
    public static Goal createEntity(EntityManager em) {
        Goal goal = new Goal()
            .summary(DEFAULT_SUMMARY)
            .created(DEFAULT_CREATED)
            .completed(DEFAULT_COMPLETED)
            .order(DEFAULT_ORDER)
            .visibility(DEFAULT_VISIBILITY);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        goal.setOwner(user);
        // Add required entity
        Theme theme = ThemeResourceIntTest.createEntity(em);
        em.persist(theme);
        em.flush();
        goal.setTheme(theme);
        return goal;
    }

    @Before
    public void initTest() {
        goal = createEntity(em);
    }

    @Test
    @Transactional
    public void createGoal() throws Exception {
        int databaseSizeBeforeCreate = goalRepository.findAll().size();

        // Create the Goal
        GoalDTO goalDTO = goalMapper.toDto(goal);
        restGoalMockMvc.perform(post("/api/goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goalDTO)))
            .andExpect(status().isCreated());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeCreate + 1);
        Goal testGoal = goalList.get(goalList.size() - 1);
        assertThat(testGoal.getSummary()).isEqualTo(DEFAULT_SUMMARY);
        assertThat(testGoal.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testGoal.getCompleted()).isEqualTo(DEFAULT_COMPLETED);
        assertThat(testGoal.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testGoal.getVisibility()).isEqualTo(DEFAULT_VISIBILITY);
    }

    @Test
    @Transactional
    public void createGoalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = goalRepository.findAll().size();

        // Create the Goal with an existing ID
        goal.setId(1L);
        GoalDTO goalDTO = goalMapper.toDto(goal);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGoalMockMvc.perform(post("/api/goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSummaryIsRequired() throws Exception {
        int databaseSizeBeforeTest = goalRepository.findAll().size();
        // set the field null
        goal.setSummary(null);

        // Create the Goal, which fails.
        GoalDTO goalDTO = goalMapper.toDto(goal);

        restGoalMockMvc.perform(post("/api/goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goalDTO)))
            .andExpect(status().isBadRequest());

        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = goalRepository.findAll().size();
        // set the field null
        goal.setCreated(null);

        // Create the Goal, which fails.
        GoalDTO goalDTO = goalMapper.toDto(goal);

        restGoalMockMvc.perform(post("/api/goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goalDTO)))
            .andExpect(status().isBadRequest());

        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = goalRepository.findAll().size();
        // set the field null
        goal.setOrder(null);

        // Create the Goal, which fails.
        GoalDTO goalDTO = goalMapper.toDto(goal);

        restGoalMockMvc.perform(post("/api/goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goalDTO)))
            .andExpect(status().isBadRequest());

        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVisibilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = goalRepository.findAll().size();
        // set the field null
        goal.setVisibility(null);

        // Create the Goal, which fails.
        GoalDTO goalDTO = goalMapper.toDto(goal);

        restGoalMockMvc.perform(post("/api/goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goalDTO)))
            .andExpect(status().isBadRequest());

        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGoals() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList
        restGoalMockMvc.perform(get("/api/goals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goal.getId().intValue())))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.toString())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER.intValue())))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    public void getAllGoalsWithEagerRelationshipsIsEnabled() throws Exception {
        GoalResource goalResource = new GoalResource(goalServiceMock, goalQueryService);
        when(goalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restGoalMockMvc = MockMvcBuilders.standaloneSetup(goalResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restGoalMockMvc.perform(get("/api/goals?eagerload=true"))
            .andExpect(status().isOk());

        verify(goalServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    public void getAllGoalsWithEagerRelationshipsIsNotEnabled() throws Exception {
        GoalResource goalResource = new GoalResource(goalServiceMock, goalQueryService);
        when(goalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
        MockMvc restGoalMockMvc = MockMvcBuilders.standaloneSetup(goalResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restGoalMockMvc.perform(get("/api/goals?eagerload=true"))
            .andExpect(status().isOk());

        verify(goalServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getGoal() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get the goal
        restGoalMockMvc.perform(get("/api/goals/{id}", goal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(goal.getId().intValue()))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.toString()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER.intValue()))
            .andExpect(jsonPath("$.visibility").value(DEFAULT_VISIBILITY.toString()));
    }

    @Test
    @Transactional
    public void getAllGoalsBySummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList where summary equals to DEFAULT_SUMMARY
        defaultGoalShouldBeFound("summary.equals=" + DEFAULT_SUMMARY);

        // Get all the goalList where summary equals to UPDATED_SUMMARY
        defaultGoalShouldNotBeFound("summary.equals=" + UPDATED_SUMMARY);
    }

    @Test
    @Transactional
    public void getAllGoalsBySummaryIsInShouldWork() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList where summary in DEFAULT_SUMMARY or UPDATED_SUMMARY
        defaultGoalShouldBeFound("summary.in=" + DEFAULT_SUMMARY + "," + UPDATED_SUMMARY);

        // Get all the goalList where summary equals to UPDATED_SUMMARY
        defaultGoalShouldNotBeFound("summary.in=" + UPDATED_SUMMARY);
    }

    @Test
    @Transactional
    public void getAllGoalsBySummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList where summary is not null
        defaultGoalShouldBeFound("summary.specified=true");

        // Get all the goalList where summary is null
        defaultGoalShouldNotBeFound("summary.specified=false");
    }

    @Test
    @Transactional
    public void getAllGoalsByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList where created equals to DEFAULT_CREATED
        defaultGoalShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the goalList where created equals to UPDATED_CREATED
        defaultGoalShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllGoalsByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultGoalShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the goalList where created equals to UPDATED_CREATED
        defaultGoalShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllGoalsByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList where created is not null
        defaultGoalShouldBeFound("created.specified=true");

        // Get all the goalList where created is null
        defaultGoalShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    public void getAllGoalsByCompletedIsEqualToSomething() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList where completed equals to DEFAULT_COMPLETED
        defaultGoalShouldBeFound("completed.equals=" + DEFAULT_COMPLETED);

        // Get all the goalList where completed equals to UPDATED_COMPLETED
        defaultGoalShouldNotBeFound("completed.equals=" + UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    public void getAllGoalsByCompletedIsInShouldWork() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList where completed in DEFAULT_COMPLETED or UPDATED_COMPLETED
        defaultGoalShouldBeFound("completed.in=" + DEFAULT_COMPLETED + "," + UPDATED_COMPLETED);

        // Get all the goalList where completed equals to UPDATED_COMPLETED
        defaultGoalShouldNotBeFound("completed.in=" + UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    public void getAllGoalsByCompletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList where completed is not null
        defaultGoalShouldBeFound("completed.specified=true");

        // Get all the goalList where completed is null
        defaultGoalShouldNotBeFound("completed.specified=false");
    }

    @Test
    @Transactional
    public void getAllGoalsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList where order equals to DEFAULT_ORDER
        defaultGoalShouldBeFound("order.equals=" + DEFAULT_ORDER);

        // Get all the goalList where order equals to UPDATED_ORDER
        defaultGoalShouldNotBeFound("order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllGoalsByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList where order in DEFAULT_ORDER or UPDATED_ORDER
        defaultGoalShouldBeFound("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER);

        // Get all the goalList where order equals to UPDATED_ORDER
        defaultGoalShouldNotBeFound("order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllGoalsByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList where order is not null
        defaultGoalShouldBeFound("order.specified=true");

        // Get all the goalList where order is null
        defaultGoalShouldNotBeFound("order.specified=false");
    }

    @Test
    @Transactional
    public void getAllGoalsByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList where order greater than or equals to DEFAULT_ORDER
        defaultGoalShouldBeFound("order.greaterOrEqualThan=" + DEFAULT_ORDER);

        // Get all the goalList where order greater than or equals to UPDATED_ORDER
        defaultGoalShouldNotBeFound("order.greaterOrEqualThan=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllGoalsByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList where order less than or equals to DEFAULT_ORDER
        defaultGoalShouldNotBeFound("order.lessThan=" + DEFAULT_ORDER);

        // Get all the goalList where order less than or equals to UPDATED_ORDER
        defaultGoalShouldBeFound("order.lessThan=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllGoalsByVisibilityIsEqualToSomething() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList where visibility equals to DEFAULT_VISIBILITY
        defaultGoalShouldBeFound("visibility.equals=" + DEFAULT_VISIBILITY);

        // Get all the goalList where visibility equals to UPDATED_VISIBILITY
        defaultGoalShouldNotBeFound("visibility.equals=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllGoalsByVisibilityIsInShouldWork() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList where visibility in DEFAULT_VISIBILITY or UPDATED_VISIBILITY
        defaultGoalShouldBeFound("visibility.in=" + DEFAULT_VISIBILITY + "," + UPDATED_VISIBILITY);

        // Get all the goalList where visibility equals to UPDATED_VISIBILITY
        defaultGoalShouldNotBeFound("visibility.in=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllGoalsByVisibilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList where visibility is not null
        defaultGoalShouldBeFound("visibility.specified=true");

        // Get all the goalList where visibility is null
        defaultGoalShouldNotBeFound("visibility.specified=false");
    }

    @Test
    @Transactional
    public void getAllGoalsByTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        Task task = TaskResourceIntTest.createEntity(em);
        em.persist(task);
        em.flush();
        goal.addTask(task);
        goalRepository.saveAndFlush(goal);
        Long taskId = task.getId();

        // Get all the goalList where task equals to taskId
        defaultGoalShouldBeFound("taskId.equals=" + taskId);

        // Get all the goalList where task equals to taskId + 1
        defaultGoalShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }

    @Test
    @Transactional
    public void getAllGoalsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        Note note = NoteResourceIntTest.createEntity(em);
        em.persist(note);
        em.flush();
        goal.addNote(note);
        goalRepository.saveAndFlush(goal);
        Long noteId = note.getId();

        // Get all the goalList where note equals to noteId
        defaultGoalShouldBeFound("noteId.equals=" + noteId);

        // Get all the goalList where note equals to noteId + 1
        defaultGoalShouldNotBeFound("noteId.equals=" + (noteId + 1));
    }

    @Test
    @Transactional
    public void getAllGoalsByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        User owner = UserResourceIntTest.createEntity(em);
        em.persist(owner);
        em.flush();
        goal.setOwner(owner);
        goalRepository.saveAndFlush(goal);
        Long ownerId = owner.getId();

        // Get all the goalList where owner equals to ownerId
        defaultGoalShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the goalList where owner equals to ownerId + 1
        defaultGoalShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    @Test
    @Transactional
    public void getAllGoalsByTagIsEqualToSomething() throws Exception {
        // Initialize the database
        Tag tag = TagResourceIntTest.createEntity(em);
        em.persist(tag);
        em.flush();
        goal.addTag(tag);
        goalRepository.saveAndFlush(goal);
        Long tagId = tag.getId();

        // Get all the goalList where tag equals to tagId
        defaultGoalShouldBeFound("tagId.equals=" + tagId);

        // Get all the goalList where tag equals to tagId + 1
        defaultGoalShouldNotBeFound("tagId.equals=" + (tagId + 1));
    }

    @Test
    @Transactional
    public void getAllGoalsByThemeIsEqualToSomething() throws Exception {
        // Initialize the database
        Theme theme = ThemeResourceIntTest.createEntity(em);
        em.persist(theme);
        em.flush();
        goal.setTheme(theme);
        goalRepository.saveAndFlush(goal);
        Long themeId = theme.getId();

        // Get all the goalList where theme equals to themeId
        defaultGoalShouldBeFound("themeId.equals=" + themeId);

        // Get all the goalList where theme equals to themeId + 1
        defaultGoalShouldNotBeFound("themeId.equals=" + (themeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultGoalShouldBeFound(String filter) throws Exception {
        restGoalMockMvc.perform(get("/api/goals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goal.getId().intValue())))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.toString())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER.intValue())))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())));

        // Check, that the count call also returns 1
        restGoalMockMvc.perform(get("/api/goals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultGoalShouldNotBeFound(String filter) throws Exception {
        restGoalMockMvc.perform(get("/api/goals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGoalMockMvc.perform(get("/api/goals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingGoal() throws Exception {
        // Get the goal
        restGoalMockMvc.perform(get("/api/goals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGoal() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        int databaseSizeBeforeUpdate = goalRepository.findAll().size();

        // Update the goal
        Goal updatedGoal = goalRepository.findById(goal.getId()).get();
        // Disconnect from session so that the updates on updatedGoal are not directly saved in db
        em.detach(updatedGoal);
        updatedGoal
            .summary(UPDATED_SUMMARY)
            .created(UPDATED_CREATED)
            .completed(UPDATED_COMPLETED)
            .order(UPDATED_ORDER)
            .visibility(UPDATED_VISIBILITY);
        GoalDTO goalDTO = goalMapper.toDto(updatedGoal);

        restGoalMockMvc.perform(put("/api/goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goalDTO)))
            .andExpect(status().isOk());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
        Goal testGoal = goalList.get(goalList.size() - 1);
        assertThat(testGoal.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testGoal.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testGoal.getCompleted()).isEqualTo(UPDATED_COMPLETED);
        assertThat(testGoal.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testGoal.getVisibility()).isEqualTo(UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void updateNonExistingGoal() throws Exception {
        int databaseSizeBeforeUpdate = goalRepository.findAll().size();

        // Create the Goal
        GoalDTO goalDTO = goalMapper.toDto(goal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoalMockMvc.perform(put("/api/goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGoal() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        int databaseSizeBeforeDelete = goalRepository.findAll().size();

        // Delete the goal
        restGoalMockMvc.perform(delete("/api/goals/{id}", goal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Goal.class);
        Goal goal1 = new Goal();
        goal1.setId(1L);
        Goal goal2 = new Goal();
        goal2.setId(goal1.getId());
        assertThat(goal1).isEqualTo(goal2);
        goal2.setId(2L);
        assertThat(goal1).isNotEqualTo(goal2);
        goal1.setId(null);
        assertThat(goal1).isNotEqualTo(goal2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GoalDTO.class);
        GoalDTO goalDTO1 = new GoalDTO();
        goalDTO1.setId(1L);
        GoalDTO goalDTO2 = new GoalDTO();
        assertThat(goalDTO1).isNotEqualTo(goalDTO2);
        goalDTO2.setId(goalDTO1.getId());
        assertThat(goalDTO1).isEqualTo(goalDTO2);
        goalDTO2.setId(2L);
        assertThat(goalDTO1).isNotEqualTo(goalDTO2);
        goalDTO1.setId(null);
        assertThat(goalDTO1).isNotEqualTo(goalDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(goalMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(goalMapper.fromId(null)).isNull();
    }
}
