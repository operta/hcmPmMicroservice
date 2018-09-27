package com.infostudio.ba.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A PmQuestQuestions.
 */
@Entity
@Table(name = "pm_quest_questions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PmQuestQuestions extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "weight", nullable = false)
    private Long weight;

    @NotNull
    @Column(name = "id_detail", nullable = false)
    private Long idDetail;

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

    public PmQuestQuestions description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getWeight() {
        return weight;
    }

    public PmQuestQuestions weight(Long weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Long getIdDetail() {
        return idDetail;
    }

    public PmQuestQuestions idDetail(Long idDetail) {
        this.idDetail = idDetail;
        return this;
    }

    public void setIdDetail(Long idDetail) {
        this.idDetail = idDetail;
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
        PmQuestQuestions pmQuestQuestions = (PmQuestQuestions) o;
        if (pmQuestQuestions.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pmQuestQuestions.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PmQuestQuestions{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", weight=" + getWeight() +
            ", idDetail=" + getIdDetail() +
            "}";
    }
}
