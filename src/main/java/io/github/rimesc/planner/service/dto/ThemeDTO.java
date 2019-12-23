package io.github.rimesc.planner.service.dto;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.github.rimesc.planner.domain.enumeration.Visibility;

/**
 * A DTO for the {@link io.github.rimesc.planner.domain.Theme} entity.
 */
public class ThemeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 128)
    private String name;

    @NotNull
    @Size(max = 512)
    private String description;

    @NotNull
    @Size(max = 32)
    private String shortName;

    
    @Lob
    private byte[] avatar;

    private String avatarContentType;
    @NotNull
    private Instant created;

    @NotNull
    private Visibility visibility;


    private Long ownerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getAvatarContentType() {
        return avatarContentType;
    }

    public void setAvatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long userId) {
        this.ownerId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ThemeDTO themeDTO = (ThemeDTO) o;
        if (themeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), themeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ThemeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", avatar='" + getAvatar() + "'" +
            ", created='" + getCreated() + "'" +
            ", visibility='" + getVisibility() + "'" +
            ", ownerId=" + getOwnerId() +
            "}";
    }
}
