package io.github.rimesc.planner.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github.rimesc.planner.domain.Goal;
import io.github.rimesc.planner.service.dto.GoalDTO;

/**
 * Mapper for the entity {@link Goal} and its DTO {@link GoalDTO}.
 */
@Mapper(componentModel = "spring", uses = { TagMapper.class, ThemeMapper.class })
public interface GoalMapper extends EntityMapper<GoalDTO, Goal> {

    @Override
    @Mapping(source = "theme", target = "theme", qualifiedBy = Summary.class)
    GoalDTO toDto(Goal goal);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "summary", target = "summary")
    @Summary
    GoalDTO toSummaryDTO(Goal goal);

    @Override
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "removeTask", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "removeNote", ignore = true)
    @Mapping(target = "removeTag", ignore = true)
    @Mapping(source = "theme", target = "theme")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Goal toEntity(GoalDTO goalDTO);

    default Goal fromId(Long id) {
        if (id == null) {
            return null;
        }
        Goal goal = new Goal();
        goal.setId(id);
        return goal;
    }

}
