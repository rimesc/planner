package io.github.rimesc.planner.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.github.rimesc.planner.service.GoalQueryService;
import io.github.rimesc.planner.service.GoalService;
import io.github.rimesc.planner.service.dto.GoalCriteria;
import io.github.rimesc.planner.service.dto.GoalDTO;
import io.github.rimesc.planner.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link io.github.rimesc.planner.domain.Goal}.
 */
@RestController
@RequestMapping("/api")
public class GoalResource {

    private final Logger log = LoggerFactory.getLogger(GoalResource.class);

    private static final String ENTITY_NAME = "goal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GoalService goalService;

    private final GoalQueryService goalQueryService;

    public GoalResource(GoalService goalService, GoalQueryService goalQueryService) {
        this.goalService = goalService;
        this.goalQueryService = goalQueryService;
    }

    /**
     * {@code POST  /goals} : Create a new goal.
     *
     * @param goalDTO the goalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new goalDTO, or with status {@code 400 (Bad Request)} if the goal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/goals")
    public ResponseEntity<GoalDTO> createGoal(@Valid @RequestBody GoalDTO goalDTO) throws URISyntaxException {
        log.debug("REST request to save Goal : {}", goalDTO);
        if (goalDTO.getId() != null) {
            throw new BadRequestAlertException("A new goal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GoalDTO result = goalService.save(goalDTO);
        return ResponseEntity.created(new URI("/api/goals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /goals} : Updates an existing goal.
     *
     * @param goalDTO the goalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated goalDTO,
     * or with status {@code 400 (Bad Request)} if the goalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the goalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/goals")
    public ResponseEntity<GoalDTO> updateGoal(@Valid @RequestBody GoalDTO goalDTO) throws URISyntaxException {
        log.debug("REST request to update Goal : {}", goalDTO);
        if (goalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GoalDTO result = goalService.save(goalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, goalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /goals} : get all the goals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of goals in body.
     */
    @GetMapping("/goals")
    public ResponseEntity<List<GoalDTO>> getAllGoals(GoalCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Goals by criteria: {}", criteria);
        Page<GoalDTO> page = goalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /goals/count} : count all the goals.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/goals/count")
    public ResponseEntity<Long> countGoals(GoalCriteria criteria) {
        log.debug("REST request to count Goals by criteria: {}", criteria);
        return ResponseEntity.ok().body(goalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /goals/:id} : get the "id" goal.
     *
     * @param id the id of the goalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the goalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/goals/{id}")
    public ResponseEntity<GoalDTO> getGoal(@PathVariable Long id) {
        log.debug("REST request to get Goal : {}", id);
        Optional<GoalDTO> goalDTO = goalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(goalDTO);
    }

    /**
     * {@code DELETE  /goals/:id} : delete the "id" goal.
     *
     * @param id the id of the goalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/goals/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        log.debug("REST request to delete Goal : {}", id);
        goalService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
