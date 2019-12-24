package io.github.rimesc.planner.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.github.rimesc.planner.domain.enumeration.Visibility;

/**
 * A DTO for the {@link io.github.rimesc.planner.domain.Goal} entity.
 */
public class GoalDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 128)
    private String summary;

    @NotNull
    private Instant createdAt;

    private Instant completedAt;

    @NotNull
    private Long order;

    @NotNull
    private Visibility visibility;

    private Long ownerId;

    private Set<TagDTO> tags = new HashSet<>();

    private Long themeId;

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long userId) {
        this.ownerId = userId;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
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
            ", createdAt='" + getCreatedAt() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", order=" + getOrder() +
            ", visibility='" + getVisibility() + "'" +
            ", ownerId=" + getOwnerId() +
            ", themeId=" + getThemeId() +
            "}";
    }
}
