package com.infostudio.ba.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A PmGoalsEvaluations.
 */
@Entity
@Table(name = "pm_goals_evaluations")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PmGoalsEvaluations extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "evaluation_date", nullable = false)
    private LocalDate evaluationDate;

    @NotNull
    @Column(name = "id_employee_evaluator", nullable = false)
    private Long idEmployeeEvaluator;

    @NotNull
    @Column(name = "id_employee_approving", nullable = false)
    private Long idEmployeeApproving;

    @NotNull
    @Column(name = "evaluation_period_from", nullable = false)
    private LocalDate evaluationPeriodFrom;

    @NotNull
    @Column(name = "evaluation_period_to", nullable = false)
    private LocalDate evaluationPeriodTo;

    @NotNull
    @Column(name = "achieved_value", nullable = false)
    private Long achievedValue;

    @NotNull
    @Column(name = "state_date", nullable = false)
    private LocalDate stateDate;

    @ManyToOne
    @JoinColumn(name = "id_employee_goal")
    private PmEmployeesGoals idEmployeeGoal;

    @ManyToOne
    @JoinColumn(name = "id_evaluation_state")
    private PmEvaluationStates idEvaluationState;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    public PmGoalsEvaluations evaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
        return this;
    }

    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public Long getIdEmployeeEvaluator() {
        return idEmployeeEvaluator;
    }

    public PmGoalsEvaluations idEmployeeEvaluator(Long idEmployeeEvaluator) {
        this.idEmployeeEvaluator = idEmployeeEvaluator;
        return this;
    }

    public void setIdEmployeeEvaluator(Long idEmployeeEvaluator) {
        this.idEmployeeEvaluator = idEmployeeEvaluator;
    }

    public Long getIdEmployeeApproving() {
        return idEmployeeApproving;
    }

    public PmGoalsEvaluations idEmployeeApproving(Long idEmployeeApproving) {
        this.idEmployeeApproving = idEmployeeApproving;
        return this;
    }

    public void setIdEmployeeApproving(Long idEmployeeApproving) {
        this.idEmployeeApproving = idEmployeeApproving;
    }

    public LocalDate getEvaluationPeriodFrom() {
        return evaluationPeriodFrom;
    }

    public PmGoalsEvaluations evaluationPeriodFrom(LocalDate evaluationPeriodFrom) {
        this.evaluationPeriodFrom = evaluationPeriodFrom;
        return this;
    }

    public void setEvaluationPeriodFrom(LocalDate evaluationPeriodFrom) {
        this.evaluationPeriodFrom = evaluationPeriodFrom;
    }

    public LocalDate getEvaluationPeriodTo() {
        return evaluationPeriodTo;
    }

    public PmGoalsEvaluations evaluationPeriodTo(LocalDate evaluationPeriodTo) {
        this.evaluationPeriodTo = evaluationPeriodTo;
        return this;
    }

    public void setEvaluationPeriodTo(LocalDate evaluationPeriodTo) {
        this.evaluationPeriodTo = evaluationPeriodTo;
    }

    public Long getAchievedValue() {
        return achievedValue;
    }

    public PmGoalsEvaluations achievedValue(Long achievedValue) {
        this.achievedValue = achievedValue;
        return this;
    }

    public void setAchievedValue(Long achievedValue) {
        this.achievedValue = achievedValue;
    }

    public LocalDate getStateDate() {
        return stateDate;
    }

    public PmGoalsEvaluations stateDate(LocalDate stateDate) {
        this.stateDate = stateDate;
        return this;
    }

    public void setStateDate(LocalDate stateDate) {
        this.stateDate = stateDate;
    }

    public PmEmployeesGoals getIdEmployeeGoal() {
        return idEmployeeGoal;
    }

    public PmGoalsEvaluations idEmployeeGoal(PmEmployeesGoals pmEmployeesGoals) {
        this.idEmployeeGoal = pmEmployeesGoals;
        return this;
    }

    public void setIdEmployeeGoal(PmEmployeesGoals pmEmployeesGoals) {
        this.idEmployeeGoal = pmEmployeesGoals;
    }

    public PmEvaluationStates getIdEvaluationState() {
        return idEvaluationState;
    }

    public PmGoalsEvaluations idEvaluationState(PmEvaluationStates pmEvaluationStates) {
        this.idEvaluationState = pmEvaluationStates;
        return this;
    }

    public void setIdEvaluationState(PmEvaluationStates pmEvaluationStates) {
        this.idEvaluationState = pmEvaluationStates;
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
        PmGoalsEvaluations pmGoalsEvaluations = (PmGoalsEvaluations) o;
        if (pmGoalsEvaluations.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmGoalsEvaluations.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmGoalsEvaluations{" +
            "id=" + getId() +
            ", evaluationDate='" + getEvaluationDate() + "'" +
            ", idEmployeeEvaluator=" + getIdEmployeeEvaluator() +
            ", idEmployeeApproving=" + getIdEmployeeApproving() +
            ", evaluationPeriodFrom='" + getEvaluationPeriodFrom() + "'" +
            ", evaluationPeriodTo='" + getEvaluationPeriodTo() + "'" +
            ", achievedValue=" + getAchievedValue() +
            ", stateDate='" + getStateDate() + "'" +
            "}";
    }
}
