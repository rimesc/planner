package io.github.rimesc.planner.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link io.github.rimesc.planner.domain.Goal} entity.
 */
public class GoalDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 128)
    private String summary;

    @NotNull
    private Long order;

    private Instant completedAt;

    private Set<TagDTO> tags;

    private ThemeDTO theme;

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

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    public ThemeDTO getTheme() {
        return theme;
    }

    public void setTheme(ThemeDTO theme) {
        this.theme = theme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GoalDTO goalDTO = (GoalDTO) o;
        if (goalDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), goalDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GoalDTO{" +
            "id=" + getId() +
            ", summary='" + getSummary() + "'" +
            ", order=" + getOrder() +
            ", completedAt='" + getCompletedAt() + "'" +
            ", theme=" + getTheme() +
            "}";
    }
}
