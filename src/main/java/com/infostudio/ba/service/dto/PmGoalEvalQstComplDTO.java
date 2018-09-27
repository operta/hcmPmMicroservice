package com.infostudio.ba.service.dto;


import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the PmGoalEvalQstCompl entity.
 */
public class PmGoalEvalQstComplDTO implements Serializable {

    private Long id;

    private String description;

    private Long idQuestionnaireCompletionId;

    private Long idGoalEvaluationId;

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

    public Long getIdQuestionaireCompletionId() {
        return idQuestionnaireCompletionId;
    }

    public void setIdQuestionaireCompletionId(Long pmQuestCompletionsId) {
        this.idQuestionnaireCompletionId = pmQuestCompletionsId;
    }

    public Long getIdGoalEvaluationId() {
        return idGoalEvaluationId;
    }

    public void setIdGoalEvaluationId(Long pmGoalsEvaluationsId) {
        this.idGoalEvaluationId = pmGoalsEvaluationsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PmGoalEvalQstComplDTO pmGoalEvalQstComplDTO = (PmGoalEvalQstComplDTO) o;
        if(pmGoalEvalQstComplDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmGoalEvalQstComplDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmGoalEvalQstComplDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
