package io.github.rimesc.planner.domain;


import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
public class Task implements Serializable {

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

    @ManyToOne
    @JsonIgnoreProperties("tasks")
    private User owner;

    @ManyToOne
    @JsonIgnoreProperties("tasks")
    private Goal goal;

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

    public Task summary(String summary) {
        this.summary = summary;
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Instant getCreated() {
        return created;
    }

    public Task created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getCompleted() {
        return completed;
    }

    public Task completed(Instant completed) {
        this.completed = completed;
        return this;
    }

    public void setCompleted(Instant completed) {
        this.completed = completed;
    }

    public User getOwner() {
        return owner;
    }

    public Task owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Goal getGoal() {
        return goal;
    }

    public Task goal(Goal goal) {
        this.goal = goal;
        return this;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
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
        Task task = (Task) o;
        if (task.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), task.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", summary='" + getSummary() + "'" +
            ", created='" + getCreated() + "'" +
            ", completed='" + getCompleted() + "'" +
            "}";
    }
}
