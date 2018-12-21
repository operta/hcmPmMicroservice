package com.infostudio.ba.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the PmGoals entity.
 */
public class PmGoalsDTO implements Serializable {

    private Long id;

    private String code;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private Long idEmployeeOwner;

    @NotNull
    private Integer isActive;

    private Long idGoalTypeId;

    private String idGoalTypeName;

    private Long idGoalId;

    private String idGoalName;

    private String createdBy;

    private Instant createdAt;

    private String updatedBy;

    private Instant updatedAt;

    public String getIdGoalName() {
        return idGoalName;
    }

    public void setIdGoalName(String idGoalName) {
        this.idGoalName = idGoalName;
    }

    public String getIdGoalTypeName() {
        return idGoalTypeName;
    }

    public void setIdGoalTypeName(String idGoalTypeName) {
        this.idGoalTypeName = idGoalTypeName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Long getIdEmployeeOwner() {
        return idEmployeeOwner;
    }

    public void setIdEmployeeOwner(Long idEmployeeOwner) {
        this.idEmployeeOwner = idEmployeeOwner;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Long getIdGoalTypeId() {
        return idGoalTypeId;
    }

    public void setIdGoalTypeId(Long pmGoalTypesId) {
        this.idGoalTypeId = pmGoalTypesId;
    }

    public Long getIdGoalId() {
        return idGoalId;
    }

    public void setIdGoalId(Long pmGoalsId) {
        this.idGoalId = pmGoalsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PmGoalsDTO pmGoalsDTO = (PmGoalsDTO) o;
        if(pmGoalsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmGoalsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmGoalsDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", idEmployeeOwner=" + getIdEmployeeOwner() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
