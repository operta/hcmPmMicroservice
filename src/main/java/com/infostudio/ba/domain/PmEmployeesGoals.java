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
 * A PmEmployeesGoals.
 */
@Entity
@Table(name = "pm_employees_goals")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PmEmployeesGoals extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "id_employee_responsible", nullable = false)
    private Long idEmployeeResponsible;

    @NotNull
    @Column(name = "id_employee_set_by", nullable = false)
    private Long idEmployeeSetBy;

    @NotNull
    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @NotNull
    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;

    @NotNull
    @Column(name = "current_value", nullable = false)
    private Long currentValue;

    @NotNull
    @Column(name = "target_value", nullable = false)
    private Long targetValue;

    @NotNull
    @Column(name = "initial_value", nullable = false)
    private Long initialValue;

    @NotNull
    @Column(name = "state_date", nullable = false)
    private LocalDate stateDate;

    @NotNull
    @Column(name = "goal_set_date", nullable = false)
    private LocalDate goalSetDate;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_unit")
    private PmUnitOfMeasures idUnit;

    @ManyToOne
    @JoinColumn(name = "id_goal_state")
    private PmGoalStates idGoalState;

    @ManyToOne
    @JoinColumn(name = "id_goal")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PmGoals idGoal;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEmployeeResponsible() {
        return idEmployeeResponsible;
    }

    public PmEmployeesGoals idEmployeeResponsible(Long idEmployeeResponsible) {
        this.idEmployeeResponsible = idEmployeeResponsible;
        return this;
    }

    public void setIdEmployeeResponsible(Long idEmployeeResponsible) {
        this.idEmployeeResponsible = idEmployeeResponsible;
    }

    public Long getIdEmployeeSetBy() {
        return idEmployeeSetBy;
    }

    public PmEmployeesGoals idEmployeeSetBy(Long idEmployeeSetBy) {
        this.idEmployeeSetBy = idEmployeeSetBy;
        return this;
    }

    public void setIdEmployeeSetBy(Long idEmployeeSetBy) {
        this.idEmployeeSetBy = idEmployeeSetBy;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public PmEmployeesGoals fromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public PmEmployeesGoals toDate(LocalDate toDate) {
        this.toDate = toDate;
        return this;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Long getCurrentValue() {
        return currentValue;
    }

    public PmEmployeesGoals currentValue(Long currentValue) {
        this.currentValue = currentValue;
        return this;
    }

    public void setCurrentValue(Long currentValue) {
        this.currentValue = currentValue;
    }

    public Long getTargetValue() {
        return targetValue;
    }

    public PmEmployeesGoals targetValue(Long targetValue) {
        this.targetValue = targetValue;
        return this;
    }

    public void setTargetValue(Long targetValue) {
        this.targetValue = targetValue;
    }

    public Long getInitialValue() {
        return initialValue;
    }

    public PmEmployeesGoals initialValue(Long initialValue) {
        this.initialValue = initialValue;
        return this;
    }

    public void setInitialValue(Long initialValue) {
        this.initialValue = initialValue;
    }

    public LocalDate getStateDate() {
        return stateDate;
    }

    public PmEmployeesGoals stateDate(LocalDate stateDate) {
        this.stateDate = stateDate;
        return this;
    }

    public void setStateDate(LocalDate stateDate) {
        this.stateDate = stateDate;
    }

    public LocalDate getGoalSetDate() {
        return goalSetDate;
    }

    public PmEmployeesGoals goalSetDate(LocalDate goalSetDate) {
        this.goalSetDate = goalSetDate;
        return this;
    }

    public void setGoalSetDate(LocalDate goalSetDate) {
        this.goalSetDate = goalSetDate;
    }

    public String getDescription() {
        return description;
    }

    public PmEmployeesGoals description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PmUnitOfMeasures getIdUnit() {
        return idUnit;
    }

    public PmEmployeesGoals idUnit(PmUnitOfMeasures pmUnitOfMeasures) {
        this.idUnit = pmUnitOfMeasures;
        return this;
    }

    public void setIdUnit(PmUnitOfMeasures pmUnitOfMeasures) {
        this.idUnit = pmUnitOfMeasures;
    }

    public PmGoalStates getIdGoalState() {
        return idGoalState;
    }

    public PmEmployeesGoals idGoalState(PmGoalStates pmGoalStates) {
        this.idGoalState = pmGoalStates;
        return this;
    }

    public void setIdGoalState(PmGoalStates pmGoalStates) {
        this.idGoalState = pmGoalStates;
    }

    public PmGoals getIdGoal() {
        return idGoal;
    }

    public PmEmployeesGoals idGoal(PmGoals pmGoals) {
        this.idGoal = pmGoals;
        return this;
    }

    public void setIdGoal(PmGoals pmGoals) {
        this.idGoal = pmGoals;
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
        PmEmployeesGoals pmEmployeesGoals = (PmEmployeesGoals) o;
        if (pmEmployeesGoals.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmEmployeesGoals.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmEmployeesGoals{" +
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
