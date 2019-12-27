package io.github.rimesc.planner.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;

/**
 * Criteria class for the {@link io.github.rimesc.planner.domain.Note} entity. This class is used
 * in {@link io.github.rimesc.planner.web.rest.NoteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NoteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter goalId;

    public NoteCriteria() {
    }

    public NoteCriteria(NoteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.goalId = other.goalId == null ? null : other.goalId.copy();
    }

    @Override
    public NoteCriteria copy() {
        return new NoteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
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
            Objects.equals(goalId, that.goalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            goalId);
    }

    @Override
    public String toString() {
        return "NoteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (goalId != null ? "goalId=" + goalId + ", " : "") +
            "}";
    }

}
