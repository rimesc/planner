package io.github.rimesc.planner.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Lob;

/**
 * A DTO for the {@link io.github.rimesc.planner.domain.Note} entity.
 */
public class NoteDTO implements Serializable {

    private Long id;

    @Lob
    private String markdown;

    private Long goalId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public Long getGoalId() {
        return goalId;
    }

    public void setGoalId(Long goalId) {
        this.goalId = goalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NoteDTO noteDTO = (NoteDTO) o;
        if (noteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), noteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NoteDTO{" +
            "id=" + getId() +
            ", markdown='" + getMarkdown() + "'" +
            ", goalId=" + getGoalId() +
            "}";
    }
}
