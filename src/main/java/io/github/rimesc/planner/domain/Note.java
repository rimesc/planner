package io.github.rimesc.planner.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Note.
 */
@Entity
@Table(name = "note")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "markdown", nullable = false)
    private String markdown;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("notes")
    private Goal goal;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarkdown() {
        return markdown;
    }

    public Note markdown(String markdown) {
        this.markdown = markdown;
        return this;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public Goal getGoal() {
        return goal;
    }

    public Note goal(Goal goal) {
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
        if (!(o instanceof Note)) {
            return false;
        }
        return id != null && id.equals(((Note) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Note{" +
            "id=" + getId() +
            ", markdown='" + getMarkdown() + "'" +
            "}";
    }
}
