package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Mentor;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Mentor}.
 */
public interface MentorService {
    /**
     * Save a mentor.
     *
     * @param mentor the entity to save.
     * @return the persisted entity.
     */
    Mentor save(Mentor mentor);

    /**
     * Updates a mentor.
     *
     * @param mentor the entity to update.
     * @return the persisted entity.
     */
    Mentor update(Mentor mentor);

    /**
     * Partially updates a mentor.
     *
     * @param mentor the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Mentor> partialUpdate(Mentor mentor);

    /**
     * Get all the mentors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Mentor> findAll(Pageable pageable);

    /**
     * Get the "id" mentor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Mentor> findOne(Long id);

    /**
     * Delete the "id" mentor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
