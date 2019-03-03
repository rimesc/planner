package uk.co.zoneofavoidance.planner.service.mapper;

import uk.co.zoneofavoidance.planner.domain.*;
import uk.co.zoneofavoidance.planner.service.dto.TagDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Tag and its DTO TagDTO.
 */
@Mapper(componentModel = "spring", uses = {ThemeMapper.class})
public interface TagMapper extends EntityMapper<TagDTO, Tag> {

    @Mapping(source = "theme.id", target = "themeId")
    TagDTO toDto(Tag tag);

    @Mapping(source = "themeId", target = "theme")
    @Mapping(target = "goals", ignore = true)
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
