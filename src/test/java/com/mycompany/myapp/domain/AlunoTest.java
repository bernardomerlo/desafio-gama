package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AlunoTestSamples.*;
import static com.mycompany.myapp.domain.MentorTestSamples.*;
import static com.mycompany.myapp.domain.MetaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlunoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Aluno.class);
        Aluno aluno1 = getAlunoSample1();
        Aluno aluno2 = new Aluno();
        assertThat(aluno1).isNotEqualTo(aluno2);

        aluno2.setId(aluno1.getId());
        assertThat(aluno1).isEqualTo(aluno2);

        aluno2 = getAlunoSample2();
        assertThat(aluno1).isNotEqualTo(aluno2);
    }

    @Test
    void metaTest() {
        Aluno aluno = getAlunoRandomSampleGenerator();
        Meta metaBack = getMetaRandomSampleGenerator();

        aluno.addMeta(metaBack);
        assertThat(aluno.getMetas()).containsOnly(metaBack);
        assertThat(metaBack.getAluno()).isEqualTo(aluno);

        aluno.removeMeta(metaBack);
        assertThat(aluno.getMetas()).doesNotContain(metaBack);
        assertThat(metaBack.getAluno()).isNull();

        aluno.metas(new HashSet<>(Set.of(metaBack)));
        assertThat(aluno.getMetas()).containsOnly(metaBack);
        assertThat(metaBack.getAluno()).isEqualTo(aluno);

        aluno.setMetas(new HashSet<>());
        assertThat(aluno.getMetas()).doesNotContain(metaBack);
        assertThat(metaBack.getAluno()).isNull();
    }

    @Test
    void mentorTest() {
        Aluno aluno = getAlunoRandomSampleGenerator();
        Mentor mentorBack = getMentorRandomSampleGenerator();

        aluno.setMentor(mentorBack);
        assertThat(aluno.getMentor()).isEqualTo(mentorBack);

        aluno.mentor(null);
        assertThat(aluno.getMentor()).isNull();
    }
}
