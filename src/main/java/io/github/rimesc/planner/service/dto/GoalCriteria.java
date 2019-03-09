package io.github.rimesc.planner.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.rimesc.planner.domain.enumeration.Visibility;

/**
 * Criteria class for the Goal entity. This class is used in GoalResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /goals?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GoalCriteria implements Serializable {
    /**
     * Class for filtering Visibility
     */
    public static class VisibilityFilter extends Filter<Visibility> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter summary;

    private InstantFilter created;

    private InstantFilter completed;

    private LongFilter order;

    private VisibilityFilter visibility;

    private LongFilter taskId;

    private LongFilter noteId;

    private LongFilter ownerId;

    private LongFilter tagId;

    private LongFilter themeId;

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

    public InstantFilter getCreated() {
        return created;
    }

    public void setCreated(InstantFilter created) {
        this.created = created;
    }

    public InstantFilter getCompleted() {
        return completed;
    }

    public void setCompleted(InstantFilter completed) {
        this.completed = completed;
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
            Objects.equals(created, that.created) &&
            Objects.equals(completed, that.completed) &&
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
            created,
            completed,
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
            (created != null ? "created=" + created + ", " : "") +
            (completed != null ? "completed=" + completed + ", " : "") +
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
