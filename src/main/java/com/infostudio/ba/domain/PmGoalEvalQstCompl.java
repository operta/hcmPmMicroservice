package com.infostudio.ba.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A PmGoalEvalQstCompl.
 */
@Entity
@Table(name = "pm_goal_eval_qst_compl")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PmGoalEvalQstCompl extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_questionnaire_completion")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PmQuestCompletions idQuestionnaireCompletion;

    @ManyToOne
    @JoinColumn(name = "id_goal_evaluation")
    private PmGoalsEvaluations idGoalEvaluation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public PmGoalEvalQstCompl description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PmQuestCompletions getIdQuestionaireCompletion() {
        return idQuestionnaireCompletion;
    }

    public PmGoalEvalQstCompl idQuestionaireCompletion(PmQuestCompletions pmQuestCompletions) {
        this.idQuestionnaireCompletion = pmQuestCompletions;
        return this;
    }

    public void setIdQuestionaireCompletion(PmQuestCompletions pmQuestCompletions) {
        this.idQuestionnaireCompletion = pmQuestCompletions;
    }

    public PmGoalsEvaluations getIdGoalEvaluation() {
        return idGoalEvaluation;
    }

    public PmGoalEvalQstCompl idGoalEvaluation(PmGoalsEvaluations pmGoalsEvaluations) {
        this.idGoalEvaluation = pmGoalsEvaluations;
        return this;
    }

    public void setIdGoalEvaluation(PmGoalsEvaluations pmGoalsEvaluations) {
        this.idGoalEvaluation = pmGoalsEvaluations;
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
        PmGoalEvalQstCompl pmGoalEvalQstCompl = (PmGoalEvalQstCompl) o;
        if (pmGoalEvalQstCompl.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmGoalEvalQstCompl.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmGoalEvalQstCompl{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
