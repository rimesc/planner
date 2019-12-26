package io.github.rimesc.planner.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter summary;

    private LongFilter order;

    private InstantFilter completedAt;

    private LongFilter taskId;

    private LongFilter noteId;

    private LongFilter tagId;

    private LongFilter themeId;

    public GoalCriteria() {
    }

    public GoalCriteria(GoalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.summary = other.summary == null ? null : other.summary.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.completedAt = other.completedAt == null ? null : other.completedAt.copy();
        this.taskId = other.taskId == null ? null : other.taskId.copy();
        this.noteId = other.noteId == null ? null : other.noteId.copy();
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

    public LongFilter getOrder() {
        return order;
    }

    public void setOrder(LongFilter order) {
        this.order = order;
    }

    public InstantFilter getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(InstantFilter completedAt) {
        this.completedAt = completedAt;
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
            Objects.equals(order, that.order) &&
            Objects.equals(completedAt, that.completedAt) &&
            Objects.equals(taskId, that.taskId) &&
            Objects.equals(noteId, that.noteId) &&
            Objects.equals(tagId, that.tagId) &&
            Objects.equals(themeId, that.themeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            summary,
            order,
            completedAt,
            taskId,
            noteId,
            tagId,
            themeId);
    }

    @Override
    public String toString() {
        return "GoalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (summary != null ? "summary=" + summary + ", " : "") +
            (order != null ? "order=" + order + ", " : "") +
            (completedAt != null ? "completedAt=" + completedAt + ", " : "") +
            (taskId != null ? "taskId=" + taskId + ", " : "") +
            (noteId != null ? "noteId=" + noteId + ", " : "") +
            (tagId != null ? "tagId=" + tagId + ", " : "") +
            (themeId != null ? "themeId=" + themeId + ", " : "") +
            "}";
    }

}
