package com.infostudio.ba.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the PmQuestionnaires entity.
 */
public class PmQuestionnairesDTO implements Serializable {

    private Long id;

    @NotNull
    private Long idHeader;

    private Long idQuestionnaireTypeId;

    private String idQuestionnaireTypeName;

    private String createdBy;

    private Instant createdAt;

    private String updatedBy;

    private Instant updatedAt;

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

    public Long getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(Long idHeader) {
        this.idHeader = idHeader;
    }

    public Long getIdQuestionnaireTypeId() {
        return idQuestionnaireTypeId;
    }

    public void setIdQuestionnaireTypeId(Long pmQuestTypesId) {
        this.idQuestionnaireTypeId = pmQuestTypesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PmQuestionnairesDTO pmQuestionnairesDTO = (PmQuestionnairesDTO) o;
        if(pmQuestionnairesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmQuestionnairesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmQuestionnairesDTO{" +
            "id=" + getId() +
            ", idHeader=" + getIdHeader() +
            "}";
    }
}
