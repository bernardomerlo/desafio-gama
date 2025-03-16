package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Aluno;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Aluno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    List<Aluno> findAllByMentorId(Long mentorId);
}
