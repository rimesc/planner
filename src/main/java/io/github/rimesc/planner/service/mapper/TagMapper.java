package io.github.rimesc.planner.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github.rimesc.planner.domain.Tag;
import io.github.rimesc.planner.service.dto.TagDTO;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring", uses = { ThemeMapper.class })
public interface TagMapper extends EntityMapper<TagDTO, Tag> {

    @Override
    @Mapping(source = "theme.id", target = "themeId")
    TagDTO toDto(Tag tag);

    @Override
    @Mapping(source = "themeId", target = "theme")
    @Mapping(target = "goals", ignore = true)
    @Mapping(target = "removeGoal", ignore = true)
    Tag toEntity(TagDTO tagDTO);

    default Tag fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.setId(id);
        return tag;
    }
}
