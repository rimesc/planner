package uk.co.zoneofavoidance.planner.web.rest;

import uk.co.zoneofavoidance.planner.PlannerApp;

import uk.co.zoneofavoidance.planner.domain.Theme;
import uk.co.zoneofavoidance.planner.domain.Tag;
import uk.co.zoneofavoidance.planner.domain.Goal;
import uk.co.zoneofavoidance.planner.domain.User;
import uk.co.zoneofavoidance.planner.repository.ThemeRepository;
import uk.co.zoneofavoidance.planner.service.ThemeService;
import uk.co.zoneofavoidance.planner.service.dto.ThemeDTO;
import uk.co.zoneofavoidance.planner.service.mapper.ThemeMapper;
import uk.co.zoneofavoidance.planner.web.rest.errors.ExceptionTranslator;
import uk.co.zoneofavoidance.planner.service.dto.ThemeCriteria;
import uk.co.zoneofavoidance.planner.service.ThemeQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static uk.co.zoneofavoidance.planner.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import uk.co.zoneofavoidance.planner.domain.enumeration.Visibility;
/**
 * Test class for the ThemeResource REST controller.
 *
 * @see ThemeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlannerApp.class)
public class ThemeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_AVATAR = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_AVATAR = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_AVATAR_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_AVATAR_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Visibility DEFAULT_VISIBILITY = Visibility.PUBLIC;
    private static final Visibility UPDATED_VISIBILITY = Visibility.PRIVATE;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ThemeMapper themeMapper;

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ThemeQueryService themeQueryService;

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

    private MockMvc restThemeMockMvc;

    private Theme theme;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ThemeResource themeResource = new ThemeResource(themeService, themeQueryService);
        this.restThemeMockMvc = MockMvcBuilders.standaloneSetup(themeResource)
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
    public static Theme createEntity(EntityManager em) {
        Theme theme = new Theme()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .shortName(DEFAULT_SHORT_NAME)
            .avatar(DEFAULT_AVATAR)
            .avatarContentType(DEFAULT_AVATAR_CONTENT_TYPE)
            .created(DEFAULT_CREATED)
            .visibility(DEFAULT_VISIBILITY);
        return theme;
    }

    @Before
    public void initTest() {
        theme = createEntity(em);
    }

    @Test
    @Transactional
    public void createTheme() throws Exception {
        int databaseSizeBeforeCreate = themeRepository.findAll().size();

        // Create the Theme
        ThemeDTO themeDTO = themeMapper.toDto(theme);
        restThemeMockMvc.perform(post("/api/themes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(themeDTO)))
            .andExpect(status().isCreated());

        // Validate the Theme in the database
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeCreate + 1);
        Theme testTheme = themeList.get(themeList.size() - 1);
        assertThat(testTheme.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTheme.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTheme.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testTheme.getAvatar()).isEqualTo(DEFAULT_AVATAR);
        assertThat(testTheme.getAvatarContentType()).isEqualTo(DEFAULT_AVATAR_CONTENT_TYPE);
        assertThat(testTheme.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testTheme.getVisibility()).isEqualTo(DEFAULT_VISIBILITY);
    }

    @Test
    @Transactional
    public void createThemeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = themeRepository.findAll().size();

        // Create the Theme with an existing ID
        theme.setId(1L);
        ThemeDTO themeDTO = themeMapper.toDto(theme);

        // An entity with an existing ID cannot be created, so this API call must fail
        restThemeMockMvc.perform(post("/api/themes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(themeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Theme in the database
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = themeRepository.findAll().size();
        // set the field null
        theme.setName(null);

        // Create the Theme, which fails.
        ThemeDTO themeDTO = themeMapper.toDto(theme);

        restThemeMockMvc.perform(post("/api/themes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(themeDTO)))
            .andExpect(status().isBadRequest());

        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = themeRepository.findAll().size();
        // set the field null
        theme.setDescription(null);

        // Create the Theme, which fails.
        ThemeDTO themeDTO = themeMapper.toDto(theme);

        restThemeMockMvc.perform(post("/api/themes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(themeDTO)))
            .andExpect(status().isBadRequest());

        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkShortNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = themeRepository.findAll().size();
        // set the field null
        theme.setShortName(null);

        // Create the Theme, which fails.
        ThemeDTO themeDTO = themeMapper.toDto(theme);

        restThemeMockMvc.perform(post("/api/themes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(themeDTO)))
            .andExpect(status().isBadRequest());

        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = themeRepository.findAll().size();
        // set the field null
        theme.setCreated(null);

        // Create the Theme, which fails.
        ThemeDTO themeDTO = themeMapper.toDto(theme);

        restThemeMockMvc.perform(post("/api/themes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(themeDTO)))
            .andExpect(status().isBadRequest());

        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVisibilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = themeRepository.findAll().size();
        // set the field null
        theme.setVisibility(null);

        // Create the Theme, which fails.
        ThemeDTO themeDTO = themeMapper.toDto(theme);

        restThemeMockMvc.perform(post("/api/themes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(themeDTO)))
            .andExpect(status().isBadRequest());

        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllThemes() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList
        restThemeMockMvc.perform(get("/api/themes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(theme.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME.toString())))
            .andExpect(jsonPath("$.[*].avatarContentType").value(hasItem(DEFAULT_AVATAR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(Base64Utils.encodeToString(DEFAULT_AVATAR))))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())));
    }
    
    @Test
    @Transactional
    public void getTheme() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get the theme
        restThemeMockMvc.perform(get("/api/themes/{id}", theme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(theme.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME.toString()))
            .andExpect(jsonPath("$.avatarContentType").value(DEFAULT_AVATAR_CONTENT_TYPE))
            .andExpect(jsonPath("$.avatar").value(Base64Utils.encodeToString(DEFAULT_AVATAR)))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.visibility").value(DEFAULT_VISIBILITY.toString()));
    }

    @Test
    @Transactional
    public void getAllThemesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where name equals to DEFAULT_NAME
        defaultThemeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the themeList where name equals to UPDATED_NAME
        defaultThemeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllThemesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultThemeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the themeList where name equals to UPDATED_NAME
        defaultThemeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllThemesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where name is not null
        defaultThemeShouldBeFound("name.specified=true");

        // Get all the themeList where name is null
        defaultThemeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllThemesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where description equals to DEFAULT_DESCRIPTION
        defaultThemeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the themeList where description equals to UPDATED_DESCRIPTION
        defaultThemeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllThemesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultThemeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the themeList where description equals to UPDATED_DESCRIPTION
        defaultThemeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllThemesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where description is not null
        defaultThemeShouldBeFound("description.specified=true");

        // Get all the themeList where description is null
        defaultThemeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllThemesByShortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where shortName equals to DEFAULT_SHORT_NAME
        defaultThemeShouldBeFound("shortName.equals=" + DEFAULT_SHORT_NAME);

        // Get all the themeList where shortName equals to UPDATED_SHORT_NAME
        defaultThemeShouldNotBeFound("shortName.equals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    public void getAllThemesByShortNameIsInShouldWork() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where shortName in DEFAULT_SHORT_NAME or UPDATED_SHORT_NAME
        defaultThemeShouldBeFound("shortName.in=" + DEFAULT_SHORT_NAME + "," + UPDATED_SHORT_NAME);

        // Get all the themeList where shortName equals to UPDATED_SHORT_NAME
        defaultThemeShouldNotBeFound("shortName.in=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    public void getAllThemesByShortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where shortName is not null
        defaultThemeShouldBeFound("shortName.specified=true");

        // Get all the themeList where shortName is null
        defaultThemeShouldNotBeFound("shortName.specified=false");
    }

    @Test
    @Transactional
    public void getAllThemesByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where created equals to DEFAULT_CREATED
        defaultThemeShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the themeList where created equals to UPDATED_CREATED
        defaultThemeShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllThemesByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultThemeShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the themeList where created equals to UPDATED_CREATED
        defaultThemeShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllThemesByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where created is not null
        defaultThemeShouldBeFound("created.specified=true");

        // Get all the themeList where created is null
        defaultThemeShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    public void getAllThemesByVisibilityIsEqualToSomething() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where visibility equals to DEFAULT_VISIBILITY
        defaultThemeShouldBeFound("visibility.equals=" + DEFAULT_VISIBILITY);

        // Get all the themeList where visibility equals to UPDATED_VISIBILITY
        defaultThemeShouldNotBeFound("visibility.equals=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllThemesByVisibilityIsInShouldWork() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where visibility in DEFAULT_VISIBILITY or UPDATED_VISIBILITY
        defaultThemeShouldBeFound("visibility.in=" + DEFAULT_VISIBILITY + "," + UPDATED_VISIBILITY);

        // Get all the themeList where visibility equals to UPDATED_VISIBILITY
        defaultThemeShouldNotBeFound("visibility.in=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllThemesByVisibilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where visibility is not null
        defaultThemeShouldBeFound("visibility.specified=true");

        // Get all the themeList where visibility is null
        defaultThemeShouldNotBeFound("visibility.specified=false");
    }

    @Test
    @Transactional
    public void getAllThemesByTagIsEqualToSomething() throws Exception {
        // Initialize the database
        Tag tag = TagResourceIntTest.createEntity(em);
        em.persist(tag);
        em.flush();
        theme.addTag(tag);
        themeRepository.saveAndFlush(theme);
        Long tagId = tag.getId();

        // Get all the themeList where tag equals to tagId
        defaultThemeShouldBeFound("tagId.equals=" + tagId);

        // Get all the themeList where tag equals to tagId + 1
        defaultThemeShouldNotBeFound("tagId.equals=" + (tagId + 1));
    }


    @Test
    @Transactional
    public void getAllThemesByGoalIsEqualToSomething() throws Exception {
        // Initialize the database
        Goal goal = GoalResourceIntTest.createEntity(em);
        em.persist(goal);
        em.flush();
        theme.addGoal(goal);
        themeRepository.saveAndFlush(theme);
        Long goalId = goal.getId();

        // Get all the themeList where goal equals to goalId
        defaultThemeShouldBeFound("goalId.equals=" + goalId);

        // Get all the themeList where goal equals to goalId + 1
        defaultThemeShouldNotBeFound("goalId.equals=" + (goalId + 1));
    }


    @Test
    @Transactional
    public void getAllThemesByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        User owner = UserResourceIntTest.createEntity(em);
        em.persist(owner);
        em.flush();
        theme.setOwner(owner);
        themeRepository.saveAndFlush(theme);
        Long ownerId = owner.getId();

        // Get all the themeList where owner equals to ownerId
        defaultThemeShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the themeList where owner equals to ownerId + 1
        defaultThemeShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultThemeShouldBeFound(String filter) throws Exception {
        restThemeMockMvc.perform(get("/api/themes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(theme.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].avatarContentType").value(hasItem(DEFAULT_AVATAR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(Base64Utils.encodeToString(DEFAULT_AVATAR))))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())));

        // Check, that the count call also returns 1
        restThemeMockMvc.perform(get("/api/themes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultThemeShouldNotBeFound(String filter) throws Exception {
        restThemeMockMvc.perform(get("/api/themes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restThemeMockMvc.perform(get("/api/themes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTheme() throws Exception {
        // Get the theme
        restThemeMockMvc.perform(get("/api/themes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTheme() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        int databaseSizeBeforeUpdate = themeRepository.findAll().size();

        // Update the theme
        Theme updatedTheme = themeRepository.findById(theme.getId()).get();
        // Disconnect from session so that the updates on updatedTheme are not directly saved in db
        em.detach(updatedTheme);
        updatedTheme
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .shortName(UPDATED_SHORT_NAME)
            .avatar(UPDATED_AVATAR)
            .avatarContentType(UPDATED_AVATAR_CONTENT_TYPE)
            .created(UPDATED_CREATED)
            .visibility(UPDATED_VISIBILITY);
        ThemeDTO themeDTO = themeMapper.toDto(updatedTheme);

        restThemeMockMvc.perform(put("/api/themes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(themeDTO)))
            .andExpect(status().isOk());

        // Validate the Theme in the database
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeUpdate);
        Theme testTheme = themeList.get(themeList.size() - 1);
        assertThat(testTheme.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTheme.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTheme.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testTheme.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testTheme.getAvatarContentType()).isEqualTo(UPDATED_AVATAR_CONTENT_TYPE);
        assertThat(testTheme.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testTheme.getVisibility()).isEqualTo(UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void updateNonExistingTheme() throws Exception {
        int databaseSizeBeforeUpdate = themeRepository.findAll().size();

        // Create the Theme
        ThemeDTO themeDTO = themeMapper.toDto(theme);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThemeMockMvc.perform(put("/api/themes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(themeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Theme in the database
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTheme() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        int databaseSizeBeforeDelete = themeRepository.findAll().size();

        // Delete the theme
        restThemeMockMvc.perform(delete("/api/themes/{id}", theme.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Theme.class);
        Theme theme1 = new Theme();
        theme1.setId(1L);
        Theme theme2 = new Theme();
        theme2.setId(theme1.getId());
        assertThat(theme1).isEqualTo(theme2);
        theme2.setId(2L);
        assertThat(theme1).isNotEqualTo(theme2);
        theme1.setId(null);
        assertThat(theme1).isNotEqualTo(theme2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThemeDTO.class);
        ThemeDTO themeDTO1 = new ThemeDTO();
        themeDTO1.setId(1L);
        ThemeDTO themeDTO2 = new ThemeDTO();
        assertThat(themeDTO1).isNotEqualTo(themeDTO2);
        themeDTO2.setId(themeDTO1.getId());
        assertThat(themeDTO1).isEqualTo(themeDTO2);
        themeDTO2.setId(2L);
        assertThat(themeDTO1).isNotEqualTo(themeDTO2);
        themeDTO1.setId(null);
        assertThat(themeDTO1).isNotEqualTo(themeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(themeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(themeMapper.fromId(null)).isNull();
    }
}
