package com.infostudio.ba.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the PmQuestQuestions entity.
 */
public class PmQuestQuestionsDTO implements Serializable {

    private Long id;

    private String description;

    @NotNull
    private Long weight;

    @NotNull
    private Long idDetail;

    private Long idQuestionnaire;

    private String createdBy;

    private Instant createdAt;

    private String updatedBy;

    private Instant updatedAt;

    public void setIdQuestionnaire(Long idQuestionnaire) {
        this.idQuestionnaire = idQuestionnaire;
    }

    public Long getIdQuestionnaire() {
        return idQuestionnaire;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Long getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(Long idDetail) {
        this.idDetail = idDetail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PmQuestQuestionsDTO pmQuestQuestionsDTO = (PmQuestQuestionsDTO) o;
        if(pmQuestQuestionsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmQuestQuestionsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmQuestQuestionsDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", weight=" + getWeight() +
            ", idDetail=" + getIdDetail() +
            ", idQuestionnaire=" + getIdQuestionnaire() +
            "}";
    }
}
