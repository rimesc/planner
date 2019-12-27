package io.github.rimesc.planner.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link io.github.rimesc.planner.domain.Task} entity.
 */
public class TaskDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 128)
    private String summary;

    private Instant completedAt;

    private GoalDTO goal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public GoalDTO getGoal() {
        return goal;
    }

    public void setGoal(GoalDTO goal) {
        this.goal = goal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;
        if (taskDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taskDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
            "id=" + getId() +
            ", summary='" + getSummary() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", goal=" + getGoal() +
            "}";
    }
}
