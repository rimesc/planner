package io.github.rimesc.planner.web.rest;
import io.github.jhipster.web.util.ResponseUtil;
import io.github.rimesc.planner.service.GoalQueryService;
import io.github.rimesc.planner.service.GoalService;
import io.github.rimesc.planner.service.dto.GoalCriteria;
import io.github.rimesc.planner.service.dto.GoalDTO;
import io.github.rimesc.planner.web.rest.errors.BadRequestAlertException;
import io.github.rimesc.planner.web.rest.util.HeaderUtil;
import io.github.rimesc.planner.web.rest.util.PaginationUtil;

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
 * REST controller for managing Goal.
 */
@RestController
@RequestMapping("/api")
public class GoalResource {

    private final Logger log = LoggerFactory.getLogger(GoalResource.class);

    private static final String ENTITY_NAME = "goal";

    private final GoalService goalService;

    private final GoalQueryService goalQueryService;

    public GoalResource(GoalService goalService, GoalQueryService goalQueryService) {
        this.goalService = goalService;
        this.goalQueryService = goalQueryService;
    }

    /**
     * POST  /goals : Create a new goal.
     *
     * @param goalDTO the goalDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new goalDTO, or with status 400 (Bad Request) if the goal has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/goals")
    public ResponseEntity<GoalDTO> createGoal(@Valid @RequestBody GoalDTO goalDTO) throws URISyntaxException {
        log.debug("REST request to save Goal : {}", goalDTO);
        if (goalDTO.getId() != null) {
            throw new BadRequestAlertException("A new goal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GoalDTO result = goalService.save(goalDTO);
        return ResponseEntity.created(new URI("/api/goals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /goals : Updates an existing goal.
     *
     * @param goalDTO the goalDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated goalDTO,
     * or with status 400 (Bad Request) if the goalDTO is not valid,
     * or with status 500 (Internal Server Error) if the goalDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/goals")
    public ResponseEntity<GoalDTO> updateGoal(@Valid @RequestBody GoalDTO goalDTO) throws URISyntaxException {
        log.debug("REST request to update Goal : {}", goalDTO);
        if (goalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GoalDTO result = goalService.save(goalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, goalDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /goals : get all the goals.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of goals in body
     */
    @GetMapping("/goals")
    public ResponseEntity<List<GoalDTO>> getAllGoals(GoalCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Goals by criteria: {}", criteria);
        Page<GoalDTO> page = goalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/goals");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /goals/count : count all the goals.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/goals/count")
    public ResponseEntity<Long> countGoals(GoalCriteria criteria) {
        log.debug("REST request to count Goals by criteria: {}", criteria);
        return ResponseEntity.ok().body(goalQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /goals/:id : get the "id" goal.
     *
     * @param id the id of the goalDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the goalDTO, or with status 404 (Not Found)
     */
    @GetMapping("/goals/{id}")
    public ResponseEntity<GoalDTO> getGoal(@PathVariable Long id) {
        log.debug("REST request to get Goal : {}", id);
        Optional<GoalDTO> goalDTO = goalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(goalDTO);
    }

    /**
     * DELETE  /goals/:id : delete the "id" goal.
     *
     * @param id the id of the goalDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/goals/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        log.debug("REST request to delete Goal : {}", id);
        goalService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
