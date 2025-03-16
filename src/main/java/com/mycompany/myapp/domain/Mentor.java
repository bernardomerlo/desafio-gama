package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Mentor.
 */
@Entity
@Table(name = "mentor")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Mentor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mentor")
    @JsonIgnoreProperties(value = { "metas", "mentor" }, allowSetters = true)
    private Set<Aluno> alunos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Mentor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Mentor nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Aluno> getAlunos() {
        return this.alunos;
    }

    public void setAlunos(Set<Aluno> alunos) {
        if (this.alunos != null) {
            this.alunos.forEach(i -> i.setMentor(null));
        }
        if (alunos != null) {
            alunos.forEach(i -> i.setMentor(this));
        }
        this.alunos = alunos;
    }

    public Mentor alunos(Set<Aluno> alunos) {
        this.setAlunos(alunos);
        return this;
    }

    public Mentor addAluno(Aluno aluno) {
        this.alunos.add(aluno);
        aluno.setMentor(this);
        return this;
    }

    public Mentor removeAluno(Aluno aluno) {
        this.alunos.remove(aluno);
        aluno.setMentor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mentor)) {
            return false;
        }
        return getId() != null && getId().equals(((Mentor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mentor{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
