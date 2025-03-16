package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MentorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Mentor getMentorSample1() {
        return new Mentor().id(1L).nome("nome1").email("email1");
    }

    public static Mentor getMentorSample2() {
        return new Mentor().id(2L).nome("nome2").email("email2");
    }

    public static Mentor getMentorRandomSampleGenerator() {
        return new Mentor().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString()).email(UUID.randomUUID().toString());
    }
}
