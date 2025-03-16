package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Mentor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Mentor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {}
