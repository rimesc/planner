package uk.co.zoneofavoidance.planner.web.rest;
import uk.co.zoneofavoidance.planner.service.ThemeService;
import uk.co.zoneofavoidance.planner.web.rest.errors.BadRequestAlertException;
import uk.co.zoneofavoidance.planner.web.rest.util.HeaderUtil;
import uk.co.zoneofavoidance.planner.web.rest.util.PaginationUtil;
import uk.co.zoneofavoidance.planner.service.dto.ThemeDTO;
import uk.co.zoneofavoidance.planner.service.dto.ThemeCriteria;
import uk.co.zoneofavoidance.planner.service.ThemeQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Theme.
 */
@RestController
@RequestMapping("/api")
public class ThemeResource {

    private final Logger log = LoggerFactory.getLogger(ThemeResource.class);

    private static final String ENTITY_NAME = "theme";

    private final ThemeService themeService;

    private final ThemeQueryService themeQueryService;

    public ThemeResource(ThemeService themeService, ThemeQueryService themeQueryService) {
        this.themeService = themeService;
        this.themeQueryService = themeQueryService;
    }

    /**
     * POST  /themes : Create a new theme.
     *
     * @param themeDTO the themeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new themeDTO, or with status 400 (Bad Request) if the theme has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/themes")
    public ResponseEntity<ThemeDTO> createTheme(@Valid @RequestBody ThemeDTO themeDTO) throws URISyntaxException {
        log.debug("REST request to save Theme : {}", themeDTO);
        if (themeDTO.getId() != null) {
            throw new BadRequestAlertException("A new theme cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ThemeDTO result = themeService.save(themeDTO);
        return ResponseEntity.created(new URI("/api/themes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /themes : Updates an existing theme.
     *
     * @param themeDTO the themeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated themeDTO,
     * or with status 400 (Bad Request) if the themeDTO is not valid,
     * or with status 500 (Internal Server Error) if the themeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/themes")
    public ResponseEntity<ThemeDTO> updateTheme(@Valid @RequestBody ThemeDTO themeDTO) throws URISyntaxException {
        log.debug("REST request to update Theme : {}", themeDTO);
        if (themeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ThemeDTO result = themeService.save(themeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, themeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /themes : get all the themes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of themes in body
     */
    @GetMapping("/themes")
    public ResponseEntity<List<ThemeDTO>> getAllThemes(ThemeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Themes by criteria: {}", criteria);
        Page<ThemeDTO> page = themeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/themes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /themes/count : count all the themes.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/themes/count")
    public ResponseEntity<Long> countThemes(ThemeCriteria criteria) {
        log.debug("REST request to count Themes by criteria: {}", criteria);
        return ResponseEntity.ok().body(themeQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /themes/:id : get the "id" theme.
     *
     * @param id the id of the themeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the themeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/themes/{id}")
    public ResponseEntity<ThemeDTO> getTheme(@PathVariable Long id) {
        log.debug("REST request to get Theme : {}", id);
        Optional<ThemeDTO> themeDTO = themeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(themeDTO);
    }

    /**
     * DELETE  /themes/:id : delete the "id" theme.
     *
     * @param id the id of the themeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        log.debug("REST request to delete Theme : {}", id);
        themeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
