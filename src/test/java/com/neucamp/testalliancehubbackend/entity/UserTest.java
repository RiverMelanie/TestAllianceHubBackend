package com.neucamp.testalliancehubbackend.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserEntity() {
        // 创建测试数据
        User user = new User();
        Date now = new Date();

        // 测试setter和getter
        user.setUser_id(1);
        user.setUsername("testuser");
        user.setNickname("Test User");
        user.setPhone("1234567890");
        user.setEmail("test@example.com");
        user.setGender(1);
        user.setPassword("password123");
        user.setCreate_time(now);
        user.setStatus((byte) 1);
        user.setIs_super((byte) 0);

        // 验证getter返回值
        assertEquals(1, user.getUser_id());
        assertEquals("testuser", user.getUsername());
        assertEquals("Test User", user.getNickname());
        assertEquals("1234567890", user.getPhone());
        assertEquals("test@example.com", user.getEmail());
        assertEquals(1, user.getGender());
        assertEquals("password123", user.getPassword());
        assertEquals(now, user.getCreate_time());
        assertEquals(1, (int ) user.getStatus());
        assertEquals(0, (int )user.getIs_super());

        // 测试格式化时间
        user.setFormatted_create_time("2023-01-01T00:00:00");
        assertEquals("2023-01-01T00:00:00", user.getFormatted_create_time());
    }
    @Test
    void testUserEntity_DefaultValues() {
        // 测试用户实体默认值
        User user = new User();

        assertNull(user.getUser_id());
        assertNull(user.getUsername());
        assertNull(user.getNickname());
        assertNull(user.getPhone());
        assertNull(user.getEmail());
        assertNull(user.getGender());
        assertNull(user.getPassword());
        assertNull(user.getCreate_time());
        assertNull(user.getStatus());
        assertEquals(0, (int) user.getIs_super()); // 测试默认值
    }

    @Test
    void testUserEntity_EdgeCases() {
        // 测试边界情况
        User user = new User();
        user.setGender(0); // 边界值
        user.setStatus((byte)2); // 超出正常状态值

        assertEquals(0, user.getGender());
        assertEquals(2, (int) user.getStatus());
    }
}
