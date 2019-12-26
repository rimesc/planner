package io.github.rimesc.planner.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github.rimesc.planner.domain.Task;
import io.github.rimesc.planner.service.dto.TaskDTO;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring", uses = { GoalMapper.class })
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {

    @Override
    @Mapping(source = "goal.id", target = "goalId")
    TaskDTO toDto(Task task);

    @Override
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
