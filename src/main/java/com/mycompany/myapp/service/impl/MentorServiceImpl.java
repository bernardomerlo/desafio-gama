package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Mentor;
import com.mycompany.myapp.repository.MentorRepository;
import com.mycompany.myapp.service.MentorService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Mentor}.
 */
@Service
@Transactional
public class MentorServiceImpl implements MentorService {

    private static final Logger LOG = LoggerFactory.getLogger(MentorServiceImpl.class);

    private final MentorRepository mentorRepository;

    public MentorServiceImpl(MentorRepository mentorRepository) {
        this.mentorRepository = mentorRepository;
    }

    @Override
    public Mentor save(Mentor mentor) {
        LOG.debug("Request to save Mentor : {}", mentor);
        return mentorRepository.save(mentor);
    }

    @Override
    public Mentor update(Mentor mentor) {
        LOG.debug("Request to update Mentor : {}", mentor);
        return mentorRepository.save(mentor);
    }

    @Override
    public Optional<Mentor> partialUpdate(Mentor mentor) {
        LOG.debug("Request to partially update Mentor : {}", mentor);

        return mentorRepository
            .findById(mentor.getId())
            .map(existingMentor -> {
                if (mentor.getNome() != null) {
                    existingMentor.setNome(mentor.getNome());
                }

                return existingMentor;
            })
            .map(mentorRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Mentor> findAll(Pageable pageable) {
        LOG.debug("Request to get all Mentors");
        return mentorRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Mentor> findOne(Long id) {
        LOG.debug("Request to get Mentor : {}", id);
        return mentorRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Mentor : {}", id);
        mentorRepository.deleteById(id);
    }
}
