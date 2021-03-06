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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import io.github.rimesc.planner.PlannerApp;
import io.github.rimesc.planner.domain.Goal;
import io.github.rimesc.planner.domain.Tag;
import io.github.rimesc.planner.domain.Theme;
import io.github.rimesc.planner.repository.ThemeRepository;
import io.github.rimesc.planner.service.ThemeQueryService;
import io.github.rimesc.planner.service.ThemeService;
import io.github.rimesc.planner.service.dto.ThemeDTO;
import io.github.rimesc.planner.service.mapper.ThemeMapper;
import io.github.rimesc.planner.web.rest.errors.ExceptionTranslator;

/**
 * Integration tests for the {@link ThemeResource} REST controller.
 */
@SpringBootTest(classes = PlannerApp.class)
public class ThemeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_AVATAR = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_AVATAR = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_AVATAR_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_AVATAR_CONTENT_TYPE = "image/png";

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

    @BeforeEach
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
            .avatar(DEFAULT_AVATAR)
            .avatarContentType(DEFAULT_AVATAR_CONTENT_TYPE);
        return theme;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Theme createUpdatedEntity(EntityManager em) {
        Theme theme = new Theme()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .avatar(UPDATED_AVATAR)
            .avatarContentType(UPDATED_AVATAR_CONTENT_TYPE);
        return theme;
    }

    @BeforeEach
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
        assertThat(testTheme.getAvatar()).isEqualTo(DEFAULT_AVATAR);
        assertThat(testTheme.getAvatarContentType()).isEqualTo(DEFAULT_AVATAR_CONTENT_TYPE);
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
    public void getAllThemes() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList
        restThemeMockMvc.perform(get("/api/themes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(theme.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].avatarContentType").value(hasItem(DEFAULT_AVATAR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(Base64Utils.encodeToString(DEFAULT_AVATAR))));
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
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.avatarContentType").value(DEFAULT_AVATAR_CONTENT_TYPE))
            .andExpect(jsonPath("$.avatar").value(Base64Utils.encodeToString(DEFAULT_AVATAR)));
    }

    @Test
    @Transactional
    public void getThemesByIdFiltering() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        Long id = theme.getId();

        defaultThemeShouldBeFound("id.equals=" + id);
        defaultThemeShouldNotBeFound("id.notEquals=" + id);

        defaultThemeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultThemeShouldNotBeFound("id.greaterThan=" + id);

        defaultThemeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultThemeShouldNotBeFound("id.lessThan=" + id);
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
    public void getAllThemesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where name not equals to DEFAULT_NAME
        defaultThemeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the themeList where name not equals to UPDATED_NAME
        defaultThemeShouldBeFound("name.notEquals=" + UPDATED_NAME);
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
    public void getAllThemesByNameContainsSomething() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where name contains DEFAULT_NAME
        defaultThemeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the themeList where name contains UPDATED_NAME
        defaultThemeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllThemesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where name does not contain DEFAULT_NAME
        defaultThemeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the themeList where name does not contain UPDATED_NAME
        defaultThemeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
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
    public void getAllThemesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where description not equals to DEFAULT_DESCRIPTION
        defaultThemeShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the themeList where description not equals to UPDATED_DESCRIPTION
        defaultThemeShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
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
    public void getAllThemesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where description contains DEFAULT_DESCRIPTION
        defaultThemeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the themeList where description contains UPDATED_DESCRIPTION
        defaultThemeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllThemesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themeList where description does not contain DEFAULT_DESCRIPTION
        defaultThemeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the themeList where description does not contain UPDATED_DESCRIPTION
        defaultThemeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllThemesByTagIsEqualToSomething() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);
        Tag tag = TagResourceIT.createEntity(em);
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
        themeRepository.saveAndFlush(theme);
        Goal goal = GoalResourceIT.createEntity(em);
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

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultThemeShouldBeFound(String filter) throws Exception {
        restThemeMockMvc.perform(get("/api/themes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(theme.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].avatarContentType").value(hasItem(DEFAULT_AVATAR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(Base64Utils.encodeToString(DEFAULT_AVATAR))));

        // Check, that the count call also returns 1
        restThemeMockMvc.perform(get("/api/themes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
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
            .avatar(UPDATED_AVATAR)
            .avatarContentType(UPDATED_AVATAR_CONTENT_TYPE);
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
        assertThat(testTheme.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testTheme.getAvatarContentType()).isEqualTo(UPDATED_AVATAR_CONTENT_TYPE);
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
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Theme> themeList = themeRepository.findAll();
        assertThat(themeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
