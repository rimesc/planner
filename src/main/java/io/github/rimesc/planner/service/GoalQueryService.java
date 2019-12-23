package io.github.rimesc.planner.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;
import io.github.rimesc.planner.domain.Goal;
// for static metamodels
import io.github.rimesc.planner.domain.Goal_;
import io.github.rimesc.planner.domain.Note_;
import io.github.rimesc.planner.domain.Tag_;
import io.github.rimesc.planner.domain.Task_;
import io.github.rimesc.planner.domain.Theme_;
import io.github.rimesc.planner.domain.User_;
import io.github.rimesc.planner.repository.GoalRepository;
import io.github.rimesc.planner.service.dto.GoalCriteria;
import io.github.rimesc.planner.service.dto.GoalDTO;
import io.github.rimesc.planner.service.mapper.GoalMapper;

/**
 * Service for executing complex queries for {@link Goal} entities in the database.
 * The main input is a {@link GoalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GoalDTO} or a {@link Page} of {@link GoalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GoalQueryService extends QueryService<Goal> {

    private final Logger log = LoggerFactory.getLogger(GoalQueryService.class);

    private final GoalRepository goalRepository;

    private final GoalMapper goalMapper;

    public GoalQueryService(GoalRepository goalRepository, GoalMapper goalMapper) {
        this.goalRepository = goalRepository;
        this.goalMapper = goalMapper;
    }

    /**
     * Return a {@link List} of {@link GoalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GoalDTO> findByCriteria(GoalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Goal> specification = createSpecification(criteria);
        return goalMapper.toDto(goalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GoalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GoalDTO> findByCriteria(GoalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Goal> specification = createSpecification(criteria);
        return goalRepository.findAll(specification, page)
            .map(goalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GoalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Goal> specification = createSpecification(criteria);
        return goalRepository.count(specification);
    }

    /**
     * Function to convert {@link GoalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Goal> createSpecification(GoalCriteria criteria) {
        Specification<Goal> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Goal_.id));
            }
            if (criteria.getSummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSummary(), Goal_.summary));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), Goal_.created));
            }
            if (criteria.getCompleted() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCompleted(), Goal_.completed));
            }
            if (criteria.getOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrder(), Goal_.order));
            }
            if (criteria.getVisibility() != null) {
                specification = specification.and(buildSpecification(criteria.getVisibility(), Goal_.visibility));
            }
            if (criteria.getTaskId() != null) {
                specification = specification.and(buildSpecification(criteria.getTaskId(),
                    root -> root.join(Goal_.tasks, JoinType.LEFT).get(Task_.id)));
            }
            if (criteria.getNoteId() != null) {
                specification = specification.and(buildSpecification(criteria.getNoteId(),
                    root -> root.join(Goal_.notes, JoinType.LEFT).get(Note_.id)));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(buildSpecification(criteria.getOwnerId(),
                    root -> root.join(Goal_.owner, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getTagId() != null) {
                specification = specification.and(buildSpecification(criteria.getTagId(),
                    root -> root.join(Goal_.tags, JoinType.LEFT).get(Tag_.id)));
            }
            if (criteria.getThemeId() != null) {
                specification = specification.and(buildSpecification(criteria.getThemeId(),
                    root -> root.join(Goal_.theme, JoinType.LEFT).get(Theme_.id)));
            }
        }
        return specification;
    }
}
