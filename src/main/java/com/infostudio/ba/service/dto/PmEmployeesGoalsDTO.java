package com.infostudio.ba.service.dto;


import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the PmEmployeesGoals entity.
 */
public class PmEmployeesGoalsDTO implements Serializable {

    private Long id;

    @NotNull
    private Long idEmployeeResponsible;

    @NotNull
    private Long idEmployeeSetBy;

    @NotNull
    private LocalDate fromDate;

    @NotNull
    private LocalDate toDate;

    @NotNull
    private Long currentValue;

    @NotNull
    private Long targetValue;

    @NotNull
    private Long initialValue;

    @NotNull
    private LocalDate stateDate;

    @NotNull
    private LocalDate goalSetDate;

    private String description;

    private Long idUnitId;

    private String idUnitName;

    private Long idGoalStateId;

    private String idGoalStateName;

    private Long idGoalId;

    private String createdBy;

    private Instant createdAt;

    private String updatedBy;

    private Instant updatedAt;

    public String getIdUnitName() {
        return idUnitName;
    }

    public void setIdUnitName(String idUnitName) {
        this.idUnitName = idUnitName;
    }

    public String getIdGoalStateName() {
        return idGoalStateName;
    }

    public void setIdGoalStateName(String idGoalStateName) {
        this.idGoalStateName = idGoalStateName;
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

    public Long getIdEmployeeResponsible() {
        return idEmployeeResponsible;
    }

    public void setIdEmployeeResponsible(Long idEmployeeResponsible) {
        this.idEmployeeResponsible = idEmployeeResponsible;
    }

    public Long getIdEmployeeSetBy() {
        return idEmployeeSetBy;
    }

    public void setIdEmployeeSetBy(Long idEmployeeSetBy) {
        this.idEmployeeSetBy = idEmployeeSetBy;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Long getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Long currentValue) {
        this.currentValue = currentValue;
    }

    public Long getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Long targetValue) {
        this.targetValue = targetValue;
    }

    public Long getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Long initialValue) {
        this.initialValue = initialValue;
    }

    public LocalDate getStateDate() {
        return stateDate;
    }

    public void setStateDate(LocalDate stateDate) {
        this.stateDate = stateDate;
    }

    public LocalDate getGoalSetDate() {
        return goalSetDate;
    }

    public void setGoalSetDate(LocalDate goalSetDate) {
        this.goalSetDate = goalSetDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIdUnitId() {
        return idUnitId;
    }

    public void setIdUnitId(Long pmUnitOfMeasuresId) {
        this.idUnitId = pmUnitOfMeasuresId;
    }

    public Long getIdGoalStateId() {
        return idGoalStateId;
    }

    public void setIdGoalStateId(Long pmGoalStatesId) {
        this.idGoalStateId = pmGoalStatesId;
    }

    public Long getIdGoalId() {
        return idGoalId;
    }

    public void setIdGoalId(Long pmGoalsId) {
        this.idGoalId = pmGoalsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PmEmployeesGoalsDTO pmEmployeesGoalsDTO = (PmEmployeesGoalsDTO) o;
        if(pmEmployeesGoalsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmEmployeesGoalsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmEmployeesGoalsDTO{" +
            "id=" + getId() +
            ", idEmployeeResponsible=" + getIdEmployeeResponsible() +
            ", idEmployeeSetBy=" + getIdEmployeeSetBy() +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", currentValue=" + getCurrentValue() +
            ", targetValue=" + getTargetValue() +
            ", initialValue=" + getInitialValue() +
            ", stateDate='" + getStateDate() + "'" +
            ", goalSetDate='" + getGoalSetDate() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
