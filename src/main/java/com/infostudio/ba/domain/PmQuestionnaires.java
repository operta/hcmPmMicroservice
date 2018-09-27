package com.infostudio.ba.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A PmQuestionnaires.
 */
@Entity
@Table(name = "pm_questionnaires")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PmQuestionnaires extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "id_header", nullable = false)
    private Long idHeader;

    @ManyToOne
    @JoinColumn(name = "id_questionnaire_type")
    private PmQuestTypes idQuestionnaireType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdHeader() {
        return idHeader;
    }

    public PmQuestionnaires idHeader(Long idHeader) {
        this.idHeader = idHeader;
        return this;
    }

    public void setIdHeader(Long idHeader) {
        this.idHeader = idHeader;
    }

    public PmQuestTypes getIdQuestionnaireType() {
        return idQuestionnaireType;
    }

    public PmQuestionnaires idQuestionnaireType(PmQuestTypes pmQuestTypes) {
        this.idQuestionnaireType = pmQuestTypes;
        return this;
    }

    public void setIdQuestionnaireType(PmQuestTypes pmQuestTypes) {
        this.idQuestionnaireType = pmQuestTypes;
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
        PmQuestionnaires pmQuestionnaires = (PmQuestionnaires) o;
        if (pmQuestionnaires.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmQuestionnaires.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmQuestionnaires{" +
            "id=" + getId() +
            ", idHeader=" + getIdHeader() +
            "}";
    }
}
