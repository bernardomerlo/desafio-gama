package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AlunoTestSamples.*;
import static com.mycompany.myapp.domain.MentorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MentorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mentor.class);
        Mentor mentor1 = getMentorSample1();
        Mentor mentor2 = new Mentor();
        assertThat(mentor1).isNotEqualTo(mentor2);

        mentor2.setId(mentor1.getId());
        assertThat(mentor1).isEqualTo(mentor2);

        mentor2 = getMentorSample2();
        assertThat(mentor1).isNotEqualTo(mentor2);
    }

    @Test
    void alunoTest() {
        Mentor mentor = getMentorRandomSampleGenerator();
        Aluno alunoBack = getAlunoRandomSampleGenerator();

        mentor.addAluno(alunoBack);
        assertThat(mentor.getAlunos()).containsOnly(alunoBack);
        assertThat(alunoBack.getMentor()).isEqualTo(mentor);

        mentor.removeAluno(alunoBack);
        assertThat(mentor.getAlunos()).doesNotContain(alunoBack);
        assertThat(alunoBack.getMentor()).isNull();

        mentor.alunos(new HashSet<>(Set.of(alunoBack)));
        assertThat(mentor.getAlunos()).containsOnly(alunoBack);
        assertThat(alunoBack.getMentor()).isEqualTo(mentor);

        mentor.setAlunos(new HashSet<>());
        assertThat(mentor.getAlunos()).doesNotContain(alunoBack);
        assertThat(alunoBack.getMentor()).isNull();
    }
}
