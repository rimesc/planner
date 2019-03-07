package io.github.rimesc.planner.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.rimesc.planner.domain.enumeration.Visibility;

/**
 * Criteria class for the Theme entity. This class is used in ThemeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /themes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ThemeCriteria implements Serializable {
    /**
     * Class for filtering Visibility
     */
    public static class VisibilityFilter extends Filter<Visibility> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter shortName;

    private InstantFilter created;

    private VisibilityFilter visibility;

    private LongFilter tagId;

    private LongFilter goalId;

    private LongFilter ownerId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getShortName() {
        return shortName;
    }

    public void setShortName(StringFilter shortName) {
        this.shortName = shortName;
    }

    public InstantFilter getCreated() {
        return created;
    }

    public void setCreated(InstantFilter created) {
        this.created = created;
    }

    public VisibilityFilter getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityFilter visibility) {
        this.visibility = visibility;
    }

    public LongFilter getTagId() {
        return tagId;
    }

    public void setTagId(LongFilter tagId) {
        this.tagId = tagId;
    }

    public LongFilter getGoalId() {
        return goalId;
    }

    public void setGoalId(LongFilter goalId) {
        this.goalId = goalId;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ThemeCriteria that = (ThemeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(shortName, that.shortName) &&
            Objects.equals(created, that.created) &&
            Objects.equals(visibility, that.visibility) &&
            Objects.equals(tagId, that.tagId) &&
            Objects.equals(goalId, that.goalId) &&
            Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        description,
        shortName,
        created,
        visibility,
        tagId,
        goalId,
        ownerId
        );
    }

    @Override
    public String toString() {
        return "ThemeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (shortName != null ? "shortName=" + shortName + ", " : "") +
                (created != null ? "created=" + created + ", " : "") +
                (visibility != null ? "visibility=" + visibility + ", " : "") +
                (tagId != null ? "tagId=" + tagId + ", " : "") +
                (goalId != null ? "goalId=" + goalId + ", " : "") +
                (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
            "}";
    }

}
