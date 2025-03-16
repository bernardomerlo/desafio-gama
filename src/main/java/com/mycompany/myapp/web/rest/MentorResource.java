package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Mentor;
import com.mycompany.myapp.repository.MentorRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Mentor}.
 */
@RestController
@RequestMapping("/api/mentors")
@Transactional
public class MentorResource {

    private static final Logger LOG = LoggerFactory.getLogger(MentorResource.class);

    private static final String ENTITY_NAME = "mentor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MentorRepository mentorRepository;

    public MentorResource(MentorRepository mentorRepository) {
        this.mentorRepository = mentorRepository;
    }

    /**
     * {@code POST  /mentors} : Create a new mentor.
     *
     * @param mentor the mentor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mentor, or with status {@code 400 (Bad Request)} if the mentor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Mentor> createMentor(@Valid @RequestBody Mentor mentor) throws URISyntaxException {
        LOG.debug("REST request to save Mentor : {}", mentor);
        if (mentor.getId() != null) {
            throw new BadRequestAlertException("A new mentor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        mentor = mentorRepository.save(mentor);
        return ResponseEntity.created(new URI("/api/mentors/" + mentor.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, mentor.getId().toString()))
            .body(mentor);
    }

    /**
     * {@code PUT  /mentors/:id} : Updates an existing mentor.
     *
     * @param id the id of the mentor to save.
     * @param mentor the mentor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mentor,
     * or with status {@code 400 (Bad Request)} if the mentor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mentor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Mentor> updateMentor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Mentor mentor
    ) throws URISyntaxException {
        LOG.debug("REST request to update Mentor : {}, {}", id, mentor);
        if (mentor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mentor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mentorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        mentor = mentorRepository.save(mentor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mentor.getId().toString()))
            .body(mentor);
    }

    /**
     * {@code PATCH  /mentors/:id} : Partial updates given fields of an existing mentor, field will ignore if it is null
     *
     * @param id the id of the mentor to save.
     * @param mentor the mentor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mentor,
     * or with status {@code 400 (Bad Request)} if the mentor is not valid,
     * or with status {@code 404 (Not Found)} if the mentor is not found,
     * or with status {@code 500 (Internal Server Error)} if the mentor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Mentor> partialUpdateMentor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Mentor mentor
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Mentor partially : {}, {}", id, mentor);
        if (mentor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mentor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mentorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Mentor> result = mentorRepository
            .findById(mentor.getId())
            .map(existingMentor -> {
                if (mentor.getNome() != null) {
                    existingMentor.setNome(mentor.getNome());
                }

                return existingMentor;
            })
            .map(mentorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mentor.getId().toString())
        );
    }

    /**
     * {@code GET  /mentors} : get all the mentors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mentors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Mentor>> getAllMentors(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Mentors");
        Page<Mentor> page = mentorRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mentors/:id} : get the "id" mentor.
     *
     * @param id the id of the mentor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mentor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Mentor> getMentor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Mentor : {}", id);
        Optional<Mentor> mentor = mentorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mentor);
    }

    /**
     * {@code DELETE  /mentors/:id} : delete the "id" mentor.
     *
     * @param id the id of the mentor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMentor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Mentor : {}", id);
        mentorRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
