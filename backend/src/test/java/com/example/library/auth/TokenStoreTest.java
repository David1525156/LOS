package com.example.library.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TokenStore 单元测试
 */
class TokenStoreTest {

    private final TokenStore tokenStore = new TokenStore();

    @Test
    void createToken_thenValid() {
        String token = tokenStore.create("admin");
        assertNotNull(token);
        assertTrue(tokenStore.isValid(token));
    }

    @Test
    void nullToken_invalid() {
        assertFalse(tokenStore.isValid(null));
        assertFalse(tokenStore.isValid(""));
    }

    @Test
    void unknownToken_invalid() {
        assertFalse(tokenStore.isValid("not-exist-token"));
    }

    @Test
    void removedToken_invalid() {
        String token = tokenStore.create("admin");
        tokenStore.remove(token);
        assertFalse(tokenStore.isValid(token));
    }
}
