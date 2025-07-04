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
class UserAdminMapperTest {

    @Autowired
    private UserAdminMapper userAdminMapper;

    @Test
    void testFindByCondition() {
        // 调用方法 - 查询所有用户
        List<User> users = userAdminMapper.findByCondition(null, null, null, 0, 10);

        // 验证结果
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    void testCountByCondition() {
        // 调用方法 - 统计所有用户
        int count = userAdminMapper.countByCondition(null, null, null);

        // 验证结果
        assertTrue(count > 0);
    }

    @Test
    void testInsertUser() {
        // 准备测试数据
        User user = new User();
        user.setUsername("adminuser");
        user.setNickname("Admin User");
        user.setPhone("1234567890");
        user.setEmail("admin@example.com");
        user.setGender(1);
        user.setPassword("adminpass");
        user.setStatus((byte) 1);
        user.setIs_super((byte) 1);
        user.setCreate_time(new Date()); // 添加这行

        // 调用方法
        int result = userAdminMapper.insert(user);

        // 验证结果
        assertEquals(1, result);
        assertNotNull(user.getUser_id());
    }

    @Test
    void testUpdateUserStatus() {
        // 调用方法 - 更新用户状态
        int result = userAdminMapper.updateStatus(1, 0);

        // 验证结果
        assertEquals(1, result);
    }
}