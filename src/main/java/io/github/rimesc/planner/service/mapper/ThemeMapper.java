package io.github.rimesc.planner.service.mapper;

import org.mapstruct.*;

import io.github.rimesc.planner.domain.*;
import io.github.rimesc.planner.service.dto.ThemeDTO;

/**
 * Mapper for the entity Theme and its DTO ThemeDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ThemeMapper extends EntityMapper<ThemeDTO, Theme> {

    @Mapping(source = "owner.id", target = "ownerId")
    ThemeDTO toDto(Theme theme);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "goals", ignore = true)
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