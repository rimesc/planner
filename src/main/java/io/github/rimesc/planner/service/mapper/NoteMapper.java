package io.github.rimesc.planner.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github.rimesc.planner.domain.Note;
import io.github.rimesc.planner.service.dto.NoteDTO;

/**
 * Mapper for the entity Note and its DTO NoteDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, GoalMapper.class})
public interface NoteMapper extends EntityMapper<NoteDTO, Note> {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "goal.id", target = "goalId")
    NoteDTO toDto(Note note);

    @Mapping(source = "ownerId", target = "owner")
    @Mapping(source = "goalId", target = "goal")
    Note toEntity(NoteDTO noteDTO);

    default Note fromId(Long id) {
        if (id == null) {
            return null;
        }
        Note note = new Note();
        note.setId(id);
        return note;
    }
}
