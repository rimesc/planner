package io.github.rimesc.planner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.rimesc.planner.domain.Goal;
import io.github.rimesc.planner.repository.GoalRepository;
import io.github.rimesc.planner.service.dto.GoalDTO;
import io.github.rimesc.planner.service.mapper.GoalMapper;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Goal}.
 */
@Service
@Transactional
public class GoalService {

    private final Logger log = LoggerFactory.getLogger(GoalService.class);

    private final GoalRepository goalRepository;

    private final GoalMapper goalMapper;

    public GoalService(GoalRepository goalRepository, GoalMapper goalMapper) {
        this.goalRepository = goalRepository;
        this.goalMapper = goalMapper;
    }

    /**
     * Save a goal.
     *
     * @param goalDTO the entity to save.
     * @return the persisted entity.
     */
    public GoalDTO save(GoalDTO goalDTO) {
        log.debug("Request to save Goal : {}", goalDTO);
        Goal goal = goalMapper.toEntity(goalDTO);
        goal = goalRepository.save(goal);
        return goalMapper.toDto(goal);
    }

    /**
     * Get all the goals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GoalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Goals");
        return goalRepository.findAll(pageable)
            .map(goalMapper::toDto);
    }

    /**
     * Get all the goals with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<GoalDTO> findAllWithEagerRelationships(Pageable pageable) {
        return goalRepository.findAllWithEagerRelationships(pageable).map(goalMapper::toDto);
    }
    

    /**
     * Get one goal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GoalDTO> findOne(Long id) {
        log.debug("Request to get Goal : {}", id);
        return goalRepository.findOneWithEagerRelationships(id)
            .map(goalMapper::toDto);
    }

    /**
     * Delete the goal by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Goal : {}", id);
        goalRepository.deleteById(id);
    }
}
