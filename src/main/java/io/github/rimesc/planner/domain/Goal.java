package io.github.rimesc.planner.domain;


import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.github.rimesc.planner.domain.enumeration.Visibility;

/**
 * A Goal.
 */
@Entity
@Table(name = "goal")
public class Goal implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 128)
    @Column(name = "summary", length = 128, nullable = false)
    private String summary;

    @NotNull
    @Column(name = "created", nullable = false)
    private Instant created;

    @Column(name = "completed")
    private Instant completed;

    @NotNull
    @Column(name = "jhi_order", nullable = false)
    private Long order;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    private Visibility visibility;

    @OneToMany(mappedBy = "goal")
    private Set<Task> tasks = new HashSet<>();
    @OneToMany(mappedBy = "goal")
    private Set<Note> notes = new HashSet<>();
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("goals")
    private User owner;

    @ManyToMany
    @JoinTable(name = "goal_tag",
               joinColumns = @JoinColumn(name = "goal_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("goals")
    private Theme theme;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public Goal summary(String summary) {
        this.summary = summary;
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Instant getCreated() {
        return created;
    }

    public Goal created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getCompleted() {
        return completed;
    }

    public Goal completed(Instant completed) {
        this.completed = completed;
        return this;
    }

    public void setCompleted(Instant completed) {
        this.completed = completed;
    }

    public Long getOrder() {
        return order;
    }

    public Goal order(Long order) {
        this.order = order;
        return this;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public Goal visibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public Goal tasks(Set<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public Goal addTask(Task task) {
        this.tasks.add(task);
        task.setGoal(this);
        return this;
    }

    public Goal removeTask(Task task) {
        this.tasks.remove(task);
        task.setGoal(null);
        return this;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public Goal notes(Set<Note> notes) {
        this.notes = notes;
        return this;
    }

    public Goal addNote(Note note) {
        this.notes.add(note);
        note.setGoal(this);
        return this;
    }

    public Goal removeNote(Note note) {
        this.notes.remove(note);
        note.setGoal(null);
        return this;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public User getOwner() {
        return owner;
    }

    public Goal owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Goal tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Goal addTag(Tag tag) {
        this.tags.add(tag);
        tag.getGoals().add(this);
        return this;
    }

    public Goal removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getGoals().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Theme getTheme() {
        return theme;
    }

    public Goal theme(Theme theme) {
        this.theme = theme;
        return this;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Goal goal = (Goal) o;
        if (goal.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), goal.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Goal{" +
            "id=" + getId() +
            ", summary='" + getSummary() + "'" +
            ", created='" + getCreated() + "'" +
            ", completed='" + getCompleted() + "'" +
            ", order=" + getOrder() +
            ", visibility='" + getVisibility() + "'" +
            "}";
    }
}
