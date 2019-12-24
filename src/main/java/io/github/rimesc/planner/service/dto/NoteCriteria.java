package io.github.rimesc.planner.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.rimesc.planner.domain.enumeration.Visibility;

/**
 * Criteria class for the Note entity. This class is used in NoteResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /notes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NoteCriteria implements Serializable {
    /**
     * Class for filtering Visibility
     */
    public static class VisibilityFilter extends Filter<Visibility> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdAt;

    private InstantFilter editedAt;

    private VisibilityFilter visibility;

    private LongFilter ownerId;

    private LongFilter goalId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(InstantFilter editedAt) {
        this.editedAt = editedAt;
    }

    public VisibilityFilter getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityFilter visibility) {
        this.visibility = visibility;
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
        final NoteCriteria that = (NoteCriteria) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(editedAt, that.editedAt) &&
            Objects.equals(visibility, that.visibility) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(goalId, that.goalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            createdAt,
            editedAt,
            visibility,
            ownerId,
            goalId);
    }

    @Override
    public String toString() {
        return "NoteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (editedAt != null ? "editedAt=" + editedAt + ", " : "") +
            (visibility != null ? "visibility=" + visibility + ", " : "") +
            (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
            (goalId != null ? "goalId=" + goalId + ", " : "") +
            "}";
    }

}
