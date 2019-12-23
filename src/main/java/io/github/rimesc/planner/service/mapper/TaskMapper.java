package io.github.rimesc.planner.service.mapper;

import org.mapstruct.*;

import io.github.rimesc.planner.domain.*;
import io.github.rimesc.planner.service.dto.TaskDTO;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, GoalMapper.class})
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "goal.id", target = "goalId")
    TaskDTO toDto(Task task);

    @Mapping(source = "ownerId", target = "owner")
    @Mapping(source = "goalId", target = "goal")
    Task toEntity(TaskDTO taskDTO);

    default Task fromId(Long id) {
        if (id == null) {
            return null;
        }
        Task task = new Task();
        task.setId(id);
        return task;
    }
}
