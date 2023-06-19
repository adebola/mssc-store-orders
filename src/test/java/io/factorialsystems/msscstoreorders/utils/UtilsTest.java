package io.factorialsystems.msscstoreorders.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Optional;

@Slf4j
public class UtilsTest {

    @Test
    void testOptionalMap() {

        Optional<String> o1 = Optional.of("String");
        Optional<String> o3 = Optional.empty();

        final Optional<String> s = o1.map(this::someFn);
        final Optional<String> s1 = o3.map(this::someFn);

        log.info("s is {} and {}", s, s1);
    }

    private String someFn(String s1) {
        return s1;
    }
}
