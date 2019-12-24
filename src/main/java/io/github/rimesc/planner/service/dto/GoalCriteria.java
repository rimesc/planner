package io.github.rimesc.planner.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.rimesc.planner.domain.enumeration.Visibility;

/**
 * Criteria class for the {@link io.github.rimesc.planner.domain.Goal} entity. This class is used
 * in {@link io.github.rimesc.planner.web.rest.GoalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /goals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GoalCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Visibility
     */
    public static class VisibilityFilter extends Filter<Visibility> {

        public VisibilityFilter() {
        }

        public VisibilityFilter(VisibilityFilter filter) {
            super(filter);
        }

        @Override
        public VisibilityFilter copy() {
            return new VisibilityFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter summary;

    private InstantFilter createdAt;

    private InstantFilter completedAt;

    private LongFilter order;

    private VisibilityFilter visibility;

    private LongFilter taskId;

    private LongFilter noteId;

    private LongFilter ownerId;

    private LongFilter tagId;

    private LongFilter themeId;

    public GoalCriteria() {
    }

    public GoalCriteria(GoalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.summary = other.summary == null ? null : other.summary.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.completedAt = other.completedAt == null ? null : other.completedAt.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.visibility = other.visibility == null ? null : other.visibility.copy();
        this.taskId = other.taskId == null ? null : other.taskId.copy();
        this.noteId = other.noteId == null ? null : other.noteId.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
        this.tagId = other.tagId == null ? null : other.tagId.copy();
        this.themeId = other.themeId == null ? null : other.themeId.copy();
    }

    @Override
    public GoalCriteria copy() {
        return new GoalCriteria(this);
    }

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

    public LongFilter getOrder() {
        return order;
    }

    public void setOrder(LongFilter order) {
        this.order = order;
    }

    public VisibilityFilter getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityFilter visibility) {
        this.visibility = visibility;
    }

    public LongFilter getTaskId() {
        return taskId;
    }

    public void setTaskId(LongFilter taskId) {
        this.taskId = taskId;
    }

    public LongFilter getNoteId() {
        return noteId;
    }

    public void setNoteId(LongFilter noteId) {
        this.noteId = noteId;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    public LongFilter getTagId() {
        return tagId;
    }

    public void setTagId(LongFilter tagId) {
        this.tagId = tagId;
    }

    public LongFilter getThemeId() {
        return themeId;
    }

    public void setThemeId(LongFilter themeId) {
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
        final GoalCriteria that = (GoalCriteria) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(summary, that.summary) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(completedAt, that.completedAt) &&
            Objects.equals(order, that.order) &&
            Objects.equals(visibility, that.visibility) &&
            Objects.equals(taskId, that.taskId) &&
            Objects.equals(noteId, that.noteId) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(tagId, that.tagId) &&
            Objects.equals(themeId, that.themeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            summary,
            createdAt,
            completedAt,
            order,
            visibility,
            taskId,
            noteId,
            ownerId,
            tagId,
            themeId);
    }

    @Override
    public String toString() {
        return "GoalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (summary != null ? "summary=" + summary + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (completedAt != null ? "completedAt=" + completedAt + ", " : "") +
            (order != null ? "order=" + order + ", " : "") +
            (visibility != null ? "visibility=" + visibility + ", " : "") +
            (taskId != null ? "taskId=" + taskId + ", " : "") +
            (noteId != null ? "noteId=" + noteId + ", " : "") +
            (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
            (tagId != null ? "tagId=" + tagId + ", " : "") +
            (themeId != null ? "themeId=" + themeId + ", " : "") +
            "}";
    }

}
