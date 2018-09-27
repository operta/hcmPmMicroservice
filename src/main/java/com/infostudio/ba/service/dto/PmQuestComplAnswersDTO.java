package com.infostudio.ba.service.dto;


import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the PmQuestComplAnswers entity.
 */
public class PmQuestComplAnswersDTO implements Serializable {

    private Long id;

    private String dtlCode;

    private String dtlName;

    private String dtlDescription;

    private String description;

    @NotNull
    private String answer;

    @NotNull
    private LocalDate dateAnswered;

    @NotNull
    private String dtlId;

    @NotNull
    private String dtlIdHeader;

    private String dtlMandatory;

    private String dtlIdDataType;

    private Long idQuestionnaireQuestionId;

    private Long idQuestionnaireCompletionId;

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

    public String getDtlCode() {
        return dtlCode;
    }

    public void setDtlCode(String dtlCode) {
        this.dtlCode = dtlCode;
    }

    public String getDtlName() {
        return dtlName;
    }

    public void setDtlName(String dtlName) {
        this.dtlName = dtlName;
    }

    public String getDtlDescription() {
        return dtlDescription;
    }

    public void setDtlDescription(String dtlDescription) {
        this.dtlDescription = dtlDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public LocalDate getDateAnswered() {
        return dateAnswered;
    }

    public void setDateAnswered(LocalDate dateAnswered) {
        this.dateAnswered = dateAnswered;
    }

    public String getDtlId() {
        return dtlId;
    }

    public void setDtlId(String dtlId) {
        this.dtlId = dtlId;
    }

    public String getDtlIdHeader() {
        return dtlIdHeader;
    }

    public void setDtlIdHeader(String dtlIdHeader) {
        this.dtlIdHeader = dtlIdHeader;
    }

    public String getDtlMandatory() {
        return dtlMandatory;
    }

    public void setDtlMandatory(String dtlMandatory) {
        this.dtlMandatory = dtlMandatory;
    }

    public String getDtlIdDataType() {
        return dtlIdDataType;
    }

    public void setDtlIdDataType(String dtlIdDataType) {
        this.dtlIdDataType = dtlIdDataType;
    }

    public Long getIdQuestionnaireQuestionId() {
        return idQuestionnaireQuestionId;
    }

    public void setIdQuestionnaireQuestionId(Long pmQuestQuestionsId) {
        this.idQuestionnaireQuestionId = pmQuestQuestionsId;
    }

    public Long getIdQuestionnaireCompletionId() {
        return idQuestionnaireCompletionId;
    }

    public void setIdQuestionnaireCompletionId(Long pmQuestCompletionsId) {
        this.idQuestionnaireCompletionId = pmQuestCompletionsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PmQuestComplAnswersDTO pmQuestComplAnswersDTO = (PmQuestComplAnswersDTO) o;
        if(pmQuestComplAnswersDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmQuestComplAnswersDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmQuestComplAnswersDTO{" +
            "id=" + getId() +
            ", dtlCode='" + getDtlCode() + "'" +
            ", dtlName='" + getDtlName() + "'" +
            ", dtlDescription='" + getDtlDescription() + "'" +
            ", description='" + getDescription() + "'" +
            ", answer='" + getAnswer() + "'" +
            ", dateAnswered='" + getDateAnswered() + "'" +
            ", dtlId='" + getDtlId() + "'" +
            ", dtlIdHeader='" + getDtlIdHeader() + "'" +
            ", dtlMandatory='" + getDtlMandatory() + "'" +
            ", dtlIdDataType='" + getDtlIdDataType() + "'" +
            "}";
    }
}
