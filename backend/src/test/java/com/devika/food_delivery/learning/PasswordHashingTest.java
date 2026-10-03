package com.devika.food_delivery.learning;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class PasswordHashingTest {

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    void hashLooksNothingLikeThePassword(){
        String hash = encoder.encode("demo1234");
        System.out.println(hash);

        assertThat(hash).startsWith("$2a$10$");
        assertThat(hash).doesNotContain("demo1234");
    }


    @Test
    void samePasswordGivesADifferentHashEachTime() {
        String first = encoder.encode("demo1234");
        String second = encoder.encode("demo1234");

        // Different salt, so different hashes...
        assertThat(first).isNotEqualTo(second);

        // ...and yet both still match the password
        assertThat(encoder.matches("demo1234", first)).isTrue();
        assertThat(encoder.matches("demo1234", second)).isTrue();
    }

    @Test
    void matchesOnlyAcceptsTheExactPassword() {
        String hash = encoder.encode("demo1234");

        assertThat(encoder.matches("Demo1234", hash)).isFalse();
        assertThat(encoder.matches("demo123", hash)).isFalse();
    }

    @Test
    void passwordsOver72BytesAreRejected() {
        String tooLong = "a".repeat(73);

        assertThatThrownBy(() -> encoder.encode(tooLong))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void printHashesForTheSeedUsers() {
        System.out.println("demo:  " + encoder.encode("demo1234"));
        System.out.println("admin: " + encoder.encode("admin1234"));
    }

}
