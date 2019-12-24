package io.github.rimesc.planner.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.github.rimesc.planner.domain.enumeration.Visibility;

/**
 * A Theme.
 */
@Entity
@Table(name = "theme")
public class Theme implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 128)
    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @NotNull
    @Size(max = 512)
    @Column(name = "description", length = 512, nullable = false)
    private String description;

    @Lob
    @Column(name = "avatar")
    private byte[] avatar;

    @Column(name = "avatar_content_type")
    private String avatarContentType;

    @NotNull
    @Column(name = "created", nullable = false)
    private Instant created;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    private Visibility visibility;

    @OneToMany(mappedBy = "theme")
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "theme")
    private Set<Goal> goals = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("themes")
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Theme name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Theme description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public Theme avatar(byte[] avatar) {
        this.avatar = avatar;
        return this;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getAvatarContentType() {
        return avatarContentType;
    }

    public Theme avatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
        return this;
    }

    public void setAvatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
    }

    public Instant getCreated() {
        return created;
    }

    public Theme created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public Theme visibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Theme tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Theme addTag(Tag tag) {
        this.tags.add(tag);
        tag.setTheme(this);
        return this;
    }

    public Theme removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.setTheme(null);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Goal> getGoals() {
        return goals;
    }

    public Theme goals(Set<Goal> goals) {
        this.goals = goals;
        return this;
    }

    public Theme addGoal(Goal goal) {
        this.goals.add(goal);
        goal.setTheme(this);
        return this;
    }

    public Theme removeGoal(Goal goal) {
        this.goals.remove(goal);
        goal.setTheme(null);
        return this;
    }

    public void setGoals(Set<Goal> goals) {
        this.goals = goals;
    }

    public User getOwner() {
        return owner;
    }

    public Theme owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Theme)) {
            return false;
        }
        return id != null && id.equals(((Theme) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Theme{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", avatar='" + getAvatar() + "'" +
            ", avatarContentType='" + getAvatarContentType() + "'" +
            ", created='" + getCreated() + "'" +
            ", visibility='" + getVisibility() + "'" +
            "}";
    }
}
