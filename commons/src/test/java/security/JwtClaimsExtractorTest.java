package security;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtClaimsExtractorTest {

    private static final String TEST_JWT = "eyJhbGciOiJIUzI1NiJ9.eyJhdXRob3JpdGllcyI6WyJST0xFX0NMSUVOVCJdLCJzdWIiOiJ0ZXN0LWVtYWlsQG91dGxvb2suY29tIiwiaWF0IjoxNzA2NDQ1ODUyLCJleHAiOjEzMDU1NTQ2NDY3Mn0.";
    private static final String TEST_SUBJECT = "test-email@outlook.com";
    private static final List<String> TEST_AUTHORITIES = List.of("ROLE_CLIENT");
    @Test
    void getAuthorities() {
        assertIterableEquals(TEST_AUTHORITIES, JwtClaimsExtractor.getAuthorities(TEST_JWT));
    }

    @Test
    void getSubject() {
        assertEquals(TEST_SUBJECT, JwtClaimsExtractor.getSubject(TEST_JWT));
    }
}