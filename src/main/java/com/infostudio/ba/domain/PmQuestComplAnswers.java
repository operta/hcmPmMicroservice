package com.infostudio.ba.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A PmQuestComplAnswers.
 */
@Entity
@Table(name = "pm_quest_compl_answers")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PmQuestComplAnswers extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "dtl_code")
    private String dtlCode;

    @Column(name = "dtl_name")
    private String dtlName;

    @Column(name = "dtl_description")
    private String dtlDescription;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "answer", nullable = false)
    private String answer;

    @NotNull
    @Column(name = "date_answered", nullable = false)
    private LocalDate dateAnswered;

    @NotNull
    @Column(name = "dtl_id", nullable = false)
    private String dtlId;

    @NotNull
    @Column(name = "dtl_id_header", nullable = false)
    private String dtlIdHeader;

    @Column(name = "dtl_mandatory")
    private String dtlMandatory;

    @Column(name = "dtl_id_data_type")
    private String dtlIdDataType;

    @ManyToOne
    @JoinColumn(name = "id_questionnaire_question")
    private PmQuestQuestions idQuestionnaireQuestion;

    @ManyToOne
    @JoinColumn(name = "id_questionnaire_completion")
    private PmQuestCompletions idQuestionnaireCompletion;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDtlCode() {
        return dtlCode;
    }

    public PmQuestComplAnswers dtlCode(String dtlCode) {
        this.dtlCode = dtlCode;
        return this;
    }

    public void setDtlCode(String dtlCode) {
        this.dtlCode = dtlCode;
    }

    public String getDtlName() {
        return dtlName;
    }

    public PmQuestComplAnswers dtlName(String dtlName) {
        this.dtlName = dtlName;
        return this;
    }

    public void setDtlName(String dtlName) {
        this.dtlName = dtlName;
    }

    public String getDtlDescription() {
        return dtlDescription;
    }

    public PmQuestComplAnswers dtlDescription(String dtlDescription) {
        this.dtlDescription = dtlDescription;
        return this;
    }

    public void setDtlDescription(String dtlDescription) {
        this.dtlDescription = dtlDescription;
    }

    public String getDescription() {
        return description;
    }

    public PmQuestComplAnswers description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswer() {
        return answer;
    }

    public PmQuestComplAnswers answer(String answer) {
        this.answer = answer;
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public LocalDate getDateAnswered() {
        return dateAnswered;
    }

    public PmQuestComplAnswers dateAnswered(LocalDate dateAnswered) {
        this.dateAnswered = dateAnswered;
        return this;
    }

    public void setDateAnswered(LocalDate dateAnswered) {
        this.dateAnswered = dateAnswered;
    }

    public String getDtlId() {
        return dtlId;
    }

    public PmQuestComplAnswers dtlId(String dtlId) {
        this.dtlId = dtlId;
        return this;
    }

    public void setDtlId(String dtlId) {
        this.dtlId = dtlId;
    }

    public String getDtlIdHeader() {
        return dtlIdHeader;
    }

    public PmQuestComplAnswers dtlIdHeader(String dtlIdHeader) {
        this.dtlIdHeader = dtlIdHeader;
        return this;
    }

    public void setDtlIdHeader(String dtlIdHeader) {
        this.dtlIdHeader = dtlIdHeader;
    }

    public String getDtlMandatory() {
        return dtlMandatory;
    }

    public PmQuestComplAnswers dtlMandatory(String dtlMandatory) {
        this.dtlMandatory = dtlMandatory;
        return this;
    }

    public void setDtlMandatory(String dtlMandatory) {
        this.dtlMandatory = dtlMandatory;
    }

    public String getDtlIdDataType() {
        return dtlIdDataType;
    }

    public PmQuestComplAnswers dtlIdDataType(String dtlIdDataType) {
        this.dtlIdDataType = dtlIdDataType;
        return this;
    }

    public void setDtlIdDataType(String dtlIdDataType) {
        this.dtlIdDataType = dtlIdDataType;
    }

    public PmQuestQuestions getIdQuestionnaireQuestion() {
        return idQuestionnaireQuestion;
    }

    public PmQuestComplAnswers idQuestionnaireQuestion(PmQuestQuestions pmQuestQuestions) {
        this.idQuestionnaireQuestion = pmQuestQuestions;
        return this;
    }

    public void setIdQuestionnaireQuestion(PmQuestQuestions pmQuestQuestions) {
        this.idQuestionnaireQuestion = pmQuestQuestions;
    }

    public PmQuestCompletions getIdQuestionnaireCompletion() {
        return idQuestionnaireCompletion;
    }

    public PmQuestComplAnswers idQuestionnaireCompletion(PmQuestCompletions pmQuestCompletions) {
        this.idQuestionnaireCompletion = pmQuestCompletions;
        return this;
    }

    public void setIdQuestionnaireCompletion(PmQuestCompletions pmQuestCompletions) {
        this.idQuestionnaireCompletion = pmQuestCompletions;
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
        PmQuestComplAnswers pmQuestComplAnswers = (PmQuestComplAnswers) o;
        if (pmQuestComplAnswers.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmQuestComplAnswers.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmQuestComplAnswers{" +
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
