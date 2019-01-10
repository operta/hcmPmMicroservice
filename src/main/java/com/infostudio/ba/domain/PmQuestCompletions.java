package com.infostudio.ba.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A PmQuestCompletions.
 */
@Entity
@Table(name = "pm_quest_completions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PmQuestCompletions extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "id_employee_completed_by", nullable = false)
    private Long idEmployeeCompletedBy;

    @NotNull
    @Column(name = "id_employee_ordered_by", nullable = false)
    private Long idEmployeeOrderedBy;

    @NotNull
    @Column(name = "state_date", nullable = false)
    private LocalDate stateDate;

    @ManyToOne
    @JoinColumn(name = "id_quest_completion_state")
    private PmQuestComplStates idQuestCompletionState;

    @ManyToOne
    @JoinColumn(name = "id_questionnaire")
    private PmQuestionnaires idQuestionnaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PmQuestCompletions withId(Long id){
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PmQuestCompletions description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIdEmployeeCompletedBy() {
        return idEmployeeCompletedBy;
    }

    public PmQuestCompletions idEmployeeCompletedBy(Long idEmployeeCompletedBy) {
        this.idEmployeeCompletedBy = idEmployeeCompletedBy;
        return this;
    }

    public void setIdEmployeeCompletedBy(Long idEmployeeCompletedBy) {
        this.idEmployeeCompletedBy = idEmployeeCompletedBy;
    }

    public Long getIdEmployeeOrderedBy() {
        return idEmployeeOrderedBy;
    }

    public PmQuestCompletions idEmployeeOrderedBy(Long idEmployeeOrderedBy) {
        this.idEmployeeOrderedBy = idEmployeeOrderedBy;
        return this;
    }

    public void setIdEmployeeOrderedBy(Long idEmployeeOrderedBy) {
        this.idEmployeeOrderedBy = idEmployeeOrderedBy;
    }

    public LocalDate getStateDate() {
        return stateDate;
    }

    public PmQuestCompletions stateDate(LocalDate stateDate) {
        this.stateDate = stateDate;
        return this;
    }

    public void setStateDate(LocalDate stateDate) {
        this.stateDate = stateDate;
    }

    public PmQuestComplStates getIdQuestCompletionState() {
        return idQuestCompletionState;
    }

    public PmQuestCompletions idQuestCompletionState(PmQuestComplStates pmQuestComplStates) {
        this.idQuestCompletionState = pmQuestComplStates;
        return this;
    }

    public void setIdQuestCompletionState(PmQuestComplStates pmQuestComplStates) {
        this.idQuestCompletionState = pmQuestComplStates;
    }

    public PmQuestionnaires getIdQuestionnaire() {
        return idQuestionnaire;
    }

    public PmQuestCompletions idQuestionnaire(PmQuestionnaires pmQuestionnaires) {
        this.idQuestionnaire = pmQuestionnaires;
        return this;
    }

    public void setIdQuestionnaire(PmQuestionnaires pmQuestionnaires) {
        this.idQuestionnaire = pmQuestionnaires;
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
        PmQuestCompletions pmQuestCompletions = (PmQuestCompletions) o;
        if (pmQuestCompletions.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmQuestCompletions.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmQuestCompletions{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", idEmployeeCompletedBy=" + getIdEmployeeCompletedBy() +
            ", idEmployeeOrderedBy=" + getIdEmployeeOrderedBy() +
            ", stateDate='" + getStateDate() + "'" +
            "}";
    }
}
