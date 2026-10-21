package com.example.library.auth;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存 Token 存储（Sprint1 演示用，进程重启后失效）
 * token -> 过期时间戳
 */
@Component
public class TokenStore {

    /** Token 有效期：2 小时 */
    private static final long EXPIRE_MILLIS = 2 * 60 * 60 * 1000L;

    private final Map<String, Long> tokens = new ConcurrentHashMap<>();

    /** 为指定用户签发 token */
    public String create(String username) {
        String token = UUID.randomUUID().toString().replace("-", "");
        tokens.put(token, System.currentTimeMillis() + EXPIRE_MILLIS);
        return token;
    }

    /** 校验 token 是否有效（不存在或已过期均视为无效，过期即删除） */
    public boolean isValid(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        Long expireAt = tokens.get(token);
        if (expireAt == null) {
            return false;
        }
        if (System.currentTimeMillis() > expireAt) {
            tokens.remove(token);
            return false;
        }
        return true;
    }

    /** 注销 token */
    public void remove(String token) {
        if (token != null) {
            tokens.remove(token);
        }
    }
}
