package io.github.rimesc.planner.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A Theme.
 */
@Entity
@Table(name = "theme")
public class Theme extends AbstractAuditingEntity {

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

    @OneToMany(mappedBy = "theme")
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "theme")
    private Set<Goal> goals = new HashSet<>();

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
            "}";
    }
}
