package com.example.library.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.library.common.BusinessException;
import com.example.library.entity.Admin;
import com.example.library.mapper.AdminMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员登录认证服务
 */
@Service
public class AuthService {

    private final AdminMapper adminMapper;
    private final TokenStore tokenStore;

    public AuthService(AdminMapper adminMapper, TokenStore tokenStore) {
        this.adminMapper = adminMapper;
        this.tokenStore = tokenStore;
    }

    /**
     * 登录：校验用户名密码，成功后签发 token
     *
     * @param username 用户名
     * @param password 密码
     * @return {token, username}
     */
    public Map<String, String> login(String username, String password) {
        Admin admin = adminMapper.selectOne(
                new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username));
        if (admin == null || !admin.getPassword().equals(password)) {
            throw new BusinessException("用户名或密码错误");
        }
        String token = tokenStore.create(username);
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        result.put("username", username);
        return result;
    }
}
