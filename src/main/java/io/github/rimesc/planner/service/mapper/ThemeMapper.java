package io.github.rimesc.planner.service.mapper;

import io.github.rimesc.planner.domain.*;
import io.github.rimesc.planner.service.dto.ThemeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Theme} and its DTO {@link ThemeDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ThemeMapper extends EntityMapper<ThemeDTO, Theme> {

    @Mapping(source = "owner.id", target = "ownerId")
    ThemeDTO toDto(Theme theme);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "removeTag", ignore = true)
    @Mapping(target = "goals", ignore = true)
    @Mapping(target = "removeGoal", ignore = true)
    @Mapping(source = "ownerId", target = "owner")
    Theme toEntity(ThemeDTO themeDTO);

    default Theme fromId(Long id) {
        if (id == null) {
            return null;
        }
        Theme theme = new Theme();
        theme.setId(id);
        return theme;
    }
}
