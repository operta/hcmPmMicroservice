package com.infostudio.ba.service.helper.model;

import com.infostudio.ba.service.dto.PmQuestQuestionsDTO;
import com.infostudio.ba.service.dto.PmQuestionnairesDTO;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public class PmQuestionnairesComposition {
    private Long id;

    @NotNull
    private Long idHeader;

    private Long idQuestionnaireTypeId;

    private String createdBy;

    private Instant createdAt;

    private String updatedBy;

    private Instant updatedAt;

    private List<PmQuestQuestionsDTO> questions;

    public PmQuestionnairesComposition() {}

    public PmQuestionnairesComposition(PmQuestionnairesDTO questionnaires,
                                       List<PmQuestQuestionsDTO> pmQuestQuestions) {
        this.questions = pmQuestQuestions;

        this.id = questionnaires.getId();
        this.idHeader = questionnaires.getIdHeader();
        this.idQuestionnaireTypeId = questionnaires.getIdQuestionnaireTypeId();
        this.createdBy = questionnaires.getCreatedBy();
        this.createdAt = questionnaires.getCreatedAt();
        this.updatedAt = questionnaires.getUpdatedAt();
        this.updatedBy = questionnaires.getUpdatedBy();
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

    public void setIdQuestionnaireTypeId(Long idQuestionnaireTypeId) {
        this.idQuestionnaireTypeId = idQuestionnaireTypeId;
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

    public List<PmQuestQuestionsDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<PmQuestQuestionsDTO> questions) {
        this.questions = questions;
    }


    @Override
    public String toString() {
        return "PmQuestionnairesComposition{" +
            "id=" + id +
            ", idHeader=" + idHeader +
            ", idQuestionnaireTypeId=" + idQuestionnaireTypeId +
            ", createdBy='" + createdBy + '\'' +
            ", createdAt=" + createdAt +
            ", updatedBy='" + updatedBy + '\'' +
            ", updatedAt=" + updatedAt +
            ", questions=" + questions +
            '}';
    }
}
