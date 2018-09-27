package com.infostudio.ba.service.dto;


import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the PmQuestCompletions entity.
 */
public class PmQuestCompletionsDTO implements Serializable {

    private Long id;

    private String description;

    @NotNull
    private Long idEmployeeCompletedBy;

    @NotNull
    private Long idEmployeeOrderedBy;

    @NotNull
    private LocalDate stateDate;

    private Long idQuestCompletionStateId;

    private Long idQuestionnaireId;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIdEmployeeCompletedBy() {
        return idEmployeeCompletedBy;
    }

    public void setIdEmployeeCompletedBy(Long idEmployeeCompletedBy) {
        this.idEmployeeCompletedBy = idEmployeeCompletedBy;
    }

    public Long getIdEmployeeOrderedBy() {
        return idEmployeeOrderedBy;
    }

    public void setIdEmployeeOrderedBy(Long idEmployeeOrderedBy) {
        this.idEmployeeOrderedBy = idEmployeeOrderedBy;
    }

    public LocalDate getStateDate() {
        return stateDate;
    }

    public void setStateDate(LocalDate stateDate) {
        this.stateDate = stateDate;
    }

    public Long getIdQuestCompletionStateId() {
        return idQuestCompletionStateId;
    }

    public void setIdQuestCompletionStateId(Long pmQuestComplStatesId) {
        this.idQuestCompletionStateId = pmQuestComplStatesId;
    }

    public Long getIdQuestionnaireId() {
        return idQuestionnaireId;
    }

    public void setIdQuestionnaireId(Long pmQuestionnairesId) {
        this.idQuestionnaireId = pmQuestionnairesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PmQuestCompletionsDTO pmQuestCompletionsDTO = (PmQuestCompletionsDTO) o;
        if(pmQuestCompletionsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmQuestCompletionsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmQuestCompletionsDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", idEmployeeCompletedBy=" + getIdEmployeeCompletedBy() +
            ", idEmployeeOrderedBy=" + getIdEmployeeOrderedBy() +
            ", stateDate='" + getStateDate() + "'" +
            "}";
    }
}
