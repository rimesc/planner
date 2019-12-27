package io.github.rimesc.planner.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    @Lob
    private byte[] avatar;

    private String avatarContentType;

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
            ", avatar='" + getAvatar() + "'" +
            "}";
    }
}
