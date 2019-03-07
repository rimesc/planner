package io.github.rimesc.planner.service.mapper;

import org.mapstruct.*;

import io.github.rimesc.planner.domain.*;
import io.github.rimesc.planner.service.dto.GoalDTO;

/**
 * Mapper for the entity Goal and its DTO GoalDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, TagMapper.class, ThemeMapper.class})
public interface GoalMapper extends EntityMapper<GoalDTO, Goal> {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "theme.id", target = "themeId")
    GoalDTO toDto(Goal goal);

    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(source = "ownerId", target = "owner")
    @Mapping(source = "themeId", target = "theme")
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
