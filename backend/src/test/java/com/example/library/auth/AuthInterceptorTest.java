package com.example.library.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AuthInterceptor 鉴权拦截器单元测试
 */
class AuthInterceptorTest {

    private TokenStore tokenStore;
    private AuthInterceptor interceptor;

    @BeforeEach
    void setUp() {
        tokenStore = new TokenStore();
        interceptor = new AuthInterceptor(tokenStore);
    }

    @Test
    void getRequest_withoutToken_pass() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/books");
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertTrue(interceptor.preHandle(request, response, null));
    }

    @Test
    void postRequest_withoutToken_blockedWith401() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/books");
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(request, response, null));
        assertEquals(401, response.getStatus());
    }

    @Test
    void postRequest_withValidToken_pass() throws Exception {
        String token = tokenStore.create("admin");
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/books");
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertTrue(interceptor.preHandle(request, response, null));
    }

    @Test
    void postRequest_withInvalidToken_blocked() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/books");
        request.addHeader("Authorization", "Bearer bad-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(request, response, null));
        assertEquals(401, response.getStatus());
    }

    @Test
    void postRequest_withRemovedToken_blocked() throws Exception {
        String token = tokenStore.create("admin");
        tokenStore.remove(token);
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/books");
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(request, response, null));
        assertEquals(401, response.getStatus());
    }
}
