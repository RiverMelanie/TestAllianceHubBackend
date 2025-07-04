package com.neucamp.testalliancehubbackend.mapper;

import com.neucamp.testalliancehubbackend.entity.User;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testLogin() {
        // 确保测试数据存在
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("testpass");
        userMapper.registerUser(testUser); // 先注册测试用户

        // 测试登录
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpass");
        User result = userMapper.Login(user);

        assertNotNull(result); // 确保返回非null
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testRegisterUser() {
        // 准备测试数据
        User user = new User();
        user.setUsername("newuser");
        user.setNickname("New User");
        user.setPhone("1234567890");
        user.setEmail("new@example.com");
        user.setGender(1);
        user.setPassword("newpass123");
        user.setCompany_id(1);

        // 调用方法
        int result = userMapper.registerUser(user);

        // 验证结果
        assertEquals(1, result);
        assertNotNull(user.getUser_id());
    }

    @Test
    void testGetUserById() {
        // 调用方法
        User user = userMapper.getUserById(1);

        // 验证结果
        assertNotNull(user);
        assertEquals(1, user.getUser_id());
    }

    @Test
    void testUpdateUserPassword() {
        // 准备测试数据
        User user = new User();
        user.setUser_id(1);
        user.setPassword("newpassword");

        // 调用方法
        int result = userMapper.updateUserPassword(user);

        // 验证结果
        assertEquals(1, result);
    }
}