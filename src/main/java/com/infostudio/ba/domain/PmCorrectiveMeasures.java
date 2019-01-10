package com.infostudio.ba.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A PmCorrectiveMeasures.
 */
@Entity
@Table(name = "pm_corrective_measures")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PmCorrectiveMeasures extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "measure_success_rate", nullable = false)
    private Double measureSuccessRate;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "state_date", nullable = false)
    private LocalDate stateDate;

    @ManyToOne
    @JoinColumn(name = "id_cm_state")
    private PmCorMeasureStates idCmState;

    @ManyToOne
    @JoinColumn(name = "id_corrective_measure_type")
    private PmCorMeasureTypes idCorrectiveMeasureType;

    @ManyToOne
    @JoinColumn(name = "id_goal_evaluation")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PmGoalsEvaluations idGoalEvaluation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public PmCorrectiveMeasures startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public PmCorrectiveMeasures endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getMeasureSuccessRate() {
        return measureSuccessRate;
    }

    public PmCorrectiveMeasures measureSuccessRate(Double measureSuccessRate) {
        this.measureSuccessRate = measureSuccessRate;
        return this;
    }

    public void setMeasureSuccessRate(Double measureSuccessRate) {
        this.measureSuccessRate = measureSuccessRate;
    }

    public String getDescription() {
        return description;
    }

    public PmCorrectiveMeasures description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStateDate() {
        return stateDate;
    }

    public PmCorrectiveMeasures stateDate(LocalDate stateDate) {
        this.stateDate = stateDate;
        return this;
    }

    public void setStateDate(LocalDate stateDate) {
        this.stateDate = stateDate;
    }

    public PmCorMeasureStates getIdCmState() {
        return idCmState;
    }

    public PmCorrectiveMeasures idCmState(PmCorMeasureStates pmCorMeasureStates) {
        this.idCmState = pmCorMeasureStates;
        return this;
    }

    public void setIdCmState(PmCorMeasureStates pmCorMeasureStates) {
        this.idCmState = pmCorMeasureStates;
    }

    public PmCorMeasureTypes getIdCorrectiveMeasureType() {
        return idCorrectiveMeasureType;
    }

    public PmCorrectiveMeasures idCorrectiveMeasureType(PmCorMeasureTypes pmCorMeasureTypes) {
        this.idCorrectiveMeasureType = pmCorMeasureTypes;
        return this;
    }

    public void setIdCorrectiveMeasureType(PmCorMeasureTypes pmCorMeasureTypes) {
        this.idCorrectiveMeasureType = pmCorMeasureTypes;
    }

    public PmGoalsEvaluations getIdGoalEvaluation() {
        return idGoalEvaluation;
    }

    public PmCorrectiveMeasures idGoalEvaluation(PmGoalsEvaluations pmGoalsEvaluations) {
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
        PmCorrectiveMeasures pmCorrectiveMeasures = (PmCorrectiveMeasures) o;
        if (pmCorrectiveMeasures.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmCorrectiveMeasures.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmCorrectiveMeasures{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", measureSuccessRate=" + getMeasureSuccessRate() +
            ", description='" + getDescription() + "'" +
            ", stateDate='" + getStateDate() + "'" +
            "}";
    }
}
