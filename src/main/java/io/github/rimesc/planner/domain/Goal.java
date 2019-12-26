package io.github.rimesc.planner.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    @Column(name = "jhi_order", nullable = false)
    private Long order;

    @Column(name = "completed_at")
    private Instant completedAt;

    @OneToMany(mappedBy = "goal")
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(mappedBy = "goal")
    private Set<Note> notes = new HashSet<>();

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

    public Instant getCompletedAt() {
        return completedAt;
    }

    public Goal completedAt(Instant completedAt) {
        this.completedAt = completedAt;
        return this;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
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
        if (!(o instanceof Goal)) {
            return false;
        }
        return id != null && id.equals(((Goal) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Goal{" +
            "id=" + getId() +
            ", summary='" + getSummary() + "'" +
            ", order=" + getOrder() +
            ", completedAt='" + getCompletedAt() + "'" +
            "}";
    }
}
