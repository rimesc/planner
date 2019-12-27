package io.github.rimesc.planner.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github.rimesc.planner.domain.Theme;
import io.github.rimesc.planner.service.dto.ThemeDTO;

/**
 * Mapper for the entity {@link Theme} and its DTO {@link ThemeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ThemeMapper extends EntityMapper<ThemeDTO, Theme> {

    @Override
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "removeTag", ignore = true)
    @Mapping(target = "goals", ignore = true)
    @Mapping(target = "removeGoal", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
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
