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
// for static metamodels
import io.github.rimesc.planner.domain.Goal_;
import io.github.rimesc.planner.domain.Tag_;
import io.github.rimesc.planner.domain.Theme;
import io.github.rimesc.planner.domain.Theme_;
import io.github.rimesc.planner.repository.ThemeRepository;
import io.github.rimesc.planner.service.dto.ThemeCriteria;
import io.github.rimesc.planner.service.dto.ThemeDTO;
import io.github.rimesc.planner.service.mapper.ThemeMapper;

/**
 * Service for executing complex queries for {@link Theme} entities in the database.
 * The main input is a {@link ThemeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ThemeDTO} or a {@link Page} of {@link ThemeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ThemeQueryService extends QueryService<Theme> {

    private final Logger log = LoggerFactory.getLogger(ThemeQueryService.class);

    private final ThemeRepository themeRepository;

    private final ThemeMapper themeMapper;

    public ThemeQueryService(ThemeRepository themeRepository, ThemeMapper themeMapper) {
        this.themeRepository = themeRepository;
        this.themeMapper = themeMapper;
    }

    /**
     * Return a {@link List} of {@link ThemeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ThemeDTO> findByCriteria(ThemeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Theme> specification = createSpecification(criteria);
        return themeMapper.toDto(themeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ThemeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ThemeDTO> findByCriteria(ThemeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Theme> specification = createSpecification(criteria);
        return themeRepository.findAll(specification, page)
            .map(themeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ThemeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Theme> specification = createSpecification(criteria);
        return themeRepository.count(specification);
    }

    /**
     * Function to convert {@link ThemeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Theme> createSpecification(ThemeCriteria criteria) {
        Specification<Theme> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Theme_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Theme_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Theme_.description));
            }
            if (criteria.getTagId() != null) {
                specification = specification.and(buildSpecification(criteria.getTagId(),
                    root -> root.join(Theme_.tags, JoinType.LEFT).get(Tag_.id)));
            }
            if (criteria.getGoalId() != null) {
                specification = specification.and(buildSpecification(criteria.getGoalId(),
                    root -> root.join(Theme_.goals, JoinType.LEFT).get(Goal_.id)));
            }
        }
        return specification;
    }
}
