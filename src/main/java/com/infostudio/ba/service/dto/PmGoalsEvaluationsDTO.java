package com.infostudio.ba.service.dto;


import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the PmGoalsEvaluations entity.
 */
public class PmGoalsEvaluationsDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate evaluationDate;

    @NotNull
    private Long idEmployeeEvaluator;

    @NotNull
    private Long idEmployeeApproving;

    @NotNull
    private LocalDate evaluationPeriodFrom;

    @NotNull
    private LocalDate evaluationPeriodTo;

    @NotNull
    private Long achievedValue;

    @NotNull
    private LocalDate stateDate;

    private Long idEmployeeGoalId;

    private Long idEvaluationStateId;

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

    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public Long getIdEmployeeEvaluator() {
        return idEmployeeEvaluator;
    }

    public void setIdEmployeeEvaluator(Long idEmployeeEvaluator) {
        this.idEmployeeEvaluator = idEmployeeEvaluator;
    }

    public Long getIdEmployeeApproving() {
        return idEmployeeApproving;
    }

    public void setIdEmployeeApproving(Long idEmployeeApproving) {
        this.idEmployeeApproving = idEmployeeApproving;
    }

    public LocalDate getEvaluationPeriodFrom() {
        return evaluationPeriodFrom;
    }

    public void setEvaluationPeriodFrom(LocalDate evaluationPeriodFrom) {
        this.evaluationPeriodFrom = evaluationPeriodFrom;
    }

    public LocalDate getEvaluationPeriodTo() {
        return evaluationPeriodTo;
    }

    public void setEvaluationPeriodTo(LocalDate evaluationPeriodTo) {
        this.evaluationPeriodTo = evaluationPeriodTo;
    }

    public Long getAchievedValue() {
        return achievedValue;
    }

    public void setAchievedValue(Long achievedValue) {
        this.achievedValue = achievedValue;
    }

    public LocalDate getStateDate() {
        return stateDate;
    }

    public void setStateDate(LocalDate stateDate) {
        this.stateDate = stateDate;
    }

    public Long getIdEmployeeGoalId() {
        return idEmployeeGoalId;
    }

    public void setIdEmployeeGoalId(Long pmEmployeesGoalsId) {
        this.idEmployeeGoalId = pmEmployeesGoalsId;
    }

    public Long getIdEvaluationStateId() {
        return idEvaluationStateId;
    }

    public void setIdEvaluationStateId(Long pmEvaluationStatesId) {
        this.idEvaluationStateId = pmEvaluationStatesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO = (PmGoalsEvaluationsDTO) o;
        if(pmGoalsEvaluationsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmGoalsEvaluationsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmGoalsEvaluationsDTO{" +
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
