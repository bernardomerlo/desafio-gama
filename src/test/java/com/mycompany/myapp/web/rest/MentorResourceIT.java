package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MentorAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Mentor;
import com.mycompany.myapp.repository.MentorRepository;
import com.mycompany.myapp.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MentorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MentorResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mentors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMentorMockMvc;

    private Mentor mentor;

    private Mentor insertedMentor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mentor createEntity() {
        return new Mentor().nome(DEFAULT_NOME).email(DEFAULT_EMAIL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mentor createUpdatedEntity() {
        return new Mentor().nome(UPDATED_NOME).email(UPDATED_EMAIL);
    }

    @BeforeEach
    public void initTest() {
        mentor = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMentor != null) {
            mentorRepository.delete(insertedMentor);
            insertedMentor = null;
        }
    }

    @Test
    @Transactional
    void createMentor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Mentor
        var returnedMentor = om.readValue(
            restMentorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mentor)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Mentor.class
        );

        // Validate the Mentor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMentorUpdatableFieldsEquals(returnedMentor, getPersistedMentor(returnedMentor));

        insertedMentor = returnedMentor;
    }

    @Test
    @Transactional
    void createMentorWithExistingId() throws Exception {
        // Create the Mentor with an existing ID
        mentor.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMentorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mentor)))
            .andExpect(status().isBadRequest());

        // Validate the Mentor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mentor.setNome(null);

        // Create the Mentor, which fails.

        restMentorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mentor)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mentor.setEmail(null);

        // Create the Mentor, which fails.

        restMentorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mentor)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMentors() throws Exception {
        // Initialize the database
        insertedMentor = mentorRepository.saveAndFlush(mentor);

        // Get all the mentorList
        restMentorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mentor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getMentor() throws Exception {
        // Initialize the database
        insertedMentor = mentorRepository.saveAndFlush(mentor);

        // Get the mentor
        restMentorMockMvc
            .perform(get(ENTITY_API_URL_ID, mentor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mentor.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingMentor() throws Exception {
        // Get the mentor
        restMentorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMentor() throws Exception {
        // Initialize the database
        insertedMentor = mentorRepository.saveAndFlush(mentor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mentor
        Mentor updatedMentor = mentorRepository.findById(mentor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMentor are not directly saved in db
        em.detach(updatedMentor);
        updatedMentor.nome(UPDATED_NOME).email(UPDATED_EMAIL);

        restMentorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMentor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMentor))
            )
            .andExpect(status().isOk());

        // Validate the Mentor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMentorToMatchAllProperties(updatedMentor);
    }

    @Test
    @Transactional
    void putNonExistingMentor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mentor.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMentorMockMvc
            .perform(put(ENTITY_API_URL_ID, mentor.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mentor)))
            .andExpect(status().isBadRequest());

        // Validate the Mentor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMentor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mentor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMentorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mentor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mentor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMentor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mentor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMentorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mentor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mentor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMentorWithPatch() throws Exception {
        // Initialize the database
        insertedMentor = mentorRepository.saveAndFlush(mentor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mentor using partial update
        Mentor partialUpdatedMentor = new Mentor();
        partialUpdatedMentor.setId(mentor.getId());

        partialUpdatedMentor.email(UPDATED_EMAIL);

        restMentorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMentor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMentor))
            )
            .andExpect(status().isOk());

        // Validate the Mentor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMentorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMentor, mentor), getPersistedMentor(mentor));
    }

    @Test
    @Transactional
    void fullUpdateMentorWithPatch() throws Exception {
        // Initialize the database
        insertedMentor = mentorRepository.saveAndFlush(mentor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mentor using partial update
        Mentor partialUpdatedMentor = new Mentor();
        partialUpdatedMentor.setId(mentor.getId());

        partialUpdatedMentor.nome(UPDATED_NOME).email(UPDATED_EMAIL);

        restMentorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMentor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMentor))
            )
            .andExpect(status().isOk());

        // Validate the Mentor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMentorUpdatableFieldsEquals(partialUpdatedMentor, getPersistedMentor(partialUpdatedMentor));
    }

    @Test
    @Transactional
    void patchNonExistingMentor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mentor.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMentorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mentor.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mentor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mentor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMentor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mentor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMentorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mentor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mentor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMentor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mentor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMentorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mentor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mentor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMentor() throws Exception {
        // Initialize the database
        insertedMentor = mentorRepository.saveAndFlush(mentor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the mentor
        restMentorMockMvc
            .perform(delete(ENTITY_API_URL_ID, mentor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mentorRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Mentor getPersistedMentor(Mentor mentor) {
        return mentorRepository.findById(mentor.getId()).orElseThrow();
    }

    protected void assertPersistedMentorToMatchAllProperties(Mentor expectedMentor) {
        assertMentorAllPropertiesEquals(expectedMentor, getPersistedMentor(expectedMentor));
    }

    protected void assertPersistedMentorToMatchUpdatableProperties(Mentor expectedMentor) {
        assertMentorAllUpdatablePropertiesEquals(expectedMentor, getPersistedMentor(expectedMentor));
    }
}
