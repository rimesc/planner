package io.github.rimesc.planner.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.github.rimesc.planner.domain.enumeration.Visibility;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

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

    
    @Lob
    @Column(name = "html", nullable = false)
    private String html;

    @NotNull
    @Column(name = "created", nullable = false)
    private Instant created;

    @Column(name = "edited")
    private Instant edited;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    private Visibility visibility;

    @ManyToOne
    @JsonIgnoreProperties("notes")
    private User owner;

    @ManyToOne
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

    public String getHtml() {
        return html;
    }

    public Note html(String html) {
        this.html = html;
        return this;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Instant getCreated() {
        return created;
    }

    public Note created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getEdited() {
        return edited;
    }

    public Note edited(Instant edited) {
        this.edited = edited;
        return this;
    }

    public void setEdited(Instant edited) {
        this.edited = edited;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public Note visibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public User getOwner() {
        return owner;
    }

    public Note owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Note note = (Note) o;
        if (note.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), note.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Note{" +
            "id=" + getId() +
            ", markdown='" + getMarkdown() + "'" +
            ", html='" + getHtml() + "'" +
            ", created='" + getCreated() + "'" +
            ", edited='" + getEdited() + "'" +
            ", visibility='" + getVisibility() + "'" +
            "}";
    }
}
