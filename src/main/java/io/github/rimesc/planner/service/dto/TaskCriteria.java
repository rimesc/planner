package io.github.rimesc.planner.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Task entity. This class is used in TaskResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /tasks?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaskCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter summary;

    private InstantFilter createdAt;

    private InstantFilter completedAt;

    private LongFilter ownerId;

    private LongFilter goalId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSummary() {
        return summary;
    }

    public void setSummary(StringFilter summary) {
        this.summary = summary;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(InstantFilter completedAt) {
        this.completedAt = completedAt;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    public LongFilter getGoalId() {
        return goalId;
    }

    public void setGoalId(LongFilter goalId) {
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
        final TaskCriteria that = (TaskCriteria) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(summary, that.summary) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(completedAt, that.completedAt) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(goalId, that.goalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            summary,
            createdAt,
            completedAt,
            ownerId,
            goalId);
    }

    @Override
    public String toString() {
        return "TaskCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (summary != null ? "summary=" + summary + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (completedAt != null ? "completedAt=" + completedAt + ", " : "") +
            (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
            (goalId != null ? "goalId=" + goalId + ", " : "") +
            "}";
    }

}
