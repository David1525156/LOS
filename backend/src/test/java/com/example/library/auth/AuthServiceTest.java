package com.example.library.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.library.common.BusinessException;
import com.example.library.entity.Admin;
import com.example.library.mapper.AdminMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * AuthService（登录认证）单元测试
 */
class AuthServiceTest {

    private AdminMapper adminMapper;
    private TokenStore tokenStore;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        adminMapper = mock(AdminMapper.class);
        tokenStore = new TokenStore();
        authService = new AuthService(adminMapper, tokenStore);
    }

    @Test
    void login_success_returnToken() {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setPassword("123456");
        when(adminMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(admin);

        Map<String, String> result = authService.login("admin", "123456");

        assertNotNull(result.get("token"));
        assertEquals("admin", result.get("username"));
        assertTrue(tokenStore.isValid(result.get("token")));
    }

    @Test
    void login_wrongPassword_throwBusinessException() {
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("123456");
        when(adminMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(admin);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authService.login("admin", "wrong"));
        assertEquals("用户名或密码错误", ex.getMessage());
        assertEquals(400, ex.getCode());
    }

    @Test
    void login_userNotFound_throwBusinessException() {
        when(adminMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authService.login("nobody", "123456"));
        assertEquals("用户名或密码错误", ex.getMessage());
    }
}
