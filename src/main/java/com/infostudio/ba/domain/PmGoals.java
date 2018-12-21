package com.infostudio.ba.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A PmGoals.
 */
@Entity
@Table(name = "pm_goals")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PmGoals extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "id_employee_owner", nullable = false)
    private Long idEmployeeOwner;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Integer isActive;

    @ManyToOne
    @JoinColumn(name = "id_goal_type")
    private PmGoalTypes idGoalType;

    @ManyToOne
    @JoinColumn(name = "id_goal")
    private PmGoals idGoal;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public PmGoals code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public PmGoals name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public PmGoals description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIdEmployeeOwner() {
        return idEmployeeOwner;
    }

    public PmGoals idEmployeeOwner(Long idEmployeeOwner) {
        this.idEmployeeOwner = idEmployeeOwner;
        return this;
    }

    public void setIdEmployeeOwner(Long idEmployeeOwner) {
        this.idEmployeeOwner = idEmployeeOwner;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public PmGoals isActive(Integer isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public PmGoalTypes getIdGoalType() {
        return idGoalType;
    }

    public PmGoals idGoalType(PmGoalTypes pmGoalTypes) {
        this.idGoalType = pmGoalTypes;
        return this;
    }

    public void setIdGoalType(PmGoalTypes pmGoalTypes) {
        this.idGoalType = pmGoalTypes;
    }

    public PmGoals getIdGoal() {
        return idGoal;
    }

    public PmGoals idGoal(PmGoals pmGoals) {
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
        PmGoals pmGoals = (PmGoals) o;
        if (pmGoals.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmGoals.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmGoals{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", idEmployeeOwner=" + getIdEmployeeOwner() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
