package com.infostudio.ba.service.dto;


import com.infostudio.ba.domain.PmEmployeesGoals;

import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the PmCorrectiveMeasures entity.
 */
public class PmCorrectiveMeasuresDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private Double measureSuccessRate;

    private String description;

    @NotNull
    private LocalDate stateDate;

    private Long idCmStateId;

    private String idCmStateName;

    private Long idCorrectiveMeasureTypeId;

    private String idCorrectiveMeasureTypeName;

    private Long idGoalEvaluationId;

    private Long idEmployeeGoalId;

    private Long idEmployeeId;

    private String createdBy;

    private Instant createdAt;

    private String updatedBy;

    private Instant updatedAt;

    public Long getIdEmployeeId() {
        return idEmployeeId;
    }

    public void setIdEmployeeId(Long idEmployeeId) {
        this.idEmployeeId = idEmployeeId;
    }

    public Long getIdEmployeeGoalId() {
        return idEmployeeGoalId;
    }

    public void setIdEmployeeGoalId(Long idEmployeeGoalId) {
        this.idEmployeeGoalId = idEmployeeGoalId;
    }

    public String getIdCmStateName() {
        return idCmStateName;
    }

    public void setIdCmStateName(String idCmStateName) {
        this.idCmStateName = idCmStateName;
    }

    public String getIdCorrectiveMeasureTypeName() {
        return idCorrectiveMeasureTypeName;
    }

    public void setIdCorrectiveMeasureTypeName(String idCorrectiveMeasureTypeName) {
        this.idCorrectiveMeasureTypeName = idCorrectiveMeasureTypeName;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getMeasureSuccessRate() {
        return measureSuccessRate;
    }

    public void setMeasureSuccessRate(Double measureSuccessRate) {
        this.measureSuccessRate = measureSuccessRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStateDate() {
        return stateDate;
    }

    public void setStateDate(LocalDate stateDate) {
        this.stateDate = stateDate;
    }

    public Long getIdCmStateId() {
        return idCmStateId;
    }

    public void setIdCmStateId(Long pmCorMeasureStatesId) {
        this.idCmStateId = pmCorMeasureStatesId;
    }

    public Long getIdCorrectiveMeasureTypeId() {
        return idCorrectiveMeasureTypeId;
    }

    public void setIdCorrectiveMeasureTypeId(Long pmCorMeasureTypesId) {
        this.idCorrectiveMeasureTypeId = pmCorMeasureTypesId;
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

        PmCorrectiveMeasuresDTO pmCorrectiveMeasuresDTO = (PmCorrectiveMeasuresDTO) o;
        if(pmCorrectiveMeasuresDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmCorrectiveMeasuresDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmCorrectiveMeasuresDTO{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", measureSuccessRate=" + getMeasureSuccessRate() +
            ", description='" + getDescription() + "'" +
            ", stateDate='" + getStateDate() + "'" +
            "}";
    }
}
