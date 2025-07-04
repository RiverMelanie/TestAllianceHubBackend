package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.entity.User;
import com.neucamp.testalliancehubbackend.mapper.UserAdminMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AdminUserControllerTest {

    @Mock
    private UserAdminMapper adminUserMapper;

    @InjectMocks
    private AdminUserController adminUserController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserList_Success() {
        // 准备测试数据
        List<User> mockUsers = Arrays.asList(new User(), new User());
        when(adminUserMapper.findByCondition(any(), any(), any(), anyInt(), anyInt())).thenReturn(mockUsers);
        when(adminUserMapper.countByCondition(any(), any(), any())).thenReturn(2);

        // 调用方法
        ResponseEntity<Map<String, Object>> response = adminUserController.getUserList(null, null, null, 1, 10);

        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().containsKey("data"));
        assertEquals(2, ((List<?>) response.getBody().get("data")).size());
    }

    @Test
    void createUser_Success() {
        // 准备测试数据
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", "testUser");
        userMap.put("nickname", "Test User");
        userMap.put("phone", "1234567890");
        userMap.put("email", "test@example.com");
        userMap.put("company_id", 1);
        userMap.put("gender", 1);
        userMap.put("password", "password123");
        userMap.put("status", 1);
        userMap.put("is_super", 0);

        when(adminUserMapper.insert(any(User.class))).thenReturn(1);

        // 调用方法
        ResponseEntity<String> response = adminUserController.createUser(userMap);

        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("用户创建成功", response.getBody());
    }

    @Test
    void updateUser_Success() {
        // 准备测试数据
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user_id", 1);
        userMap.put("username", "updatedUser");
        userMap.put("nickname", "Updated User");
        userMap.put("phone", "0987654321");
        userMap.put("email", "updated@example.com");
        userMap.put("company_id", 1);
        userMap.put("gender", 1);
        userMap.put("status", 1);
        userMap.put("is_super", 0);

        when(adminUserMapper.update(any(User.class))).thenReturn(1);

        // 调用方法
        ResponseEntity<String> response = adminUserController.updateUser(userMap);

        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("用户更新成功", response.getBody());
    }

    @Test
    void deleteUser_Success() {
        // 准备测试数据
        int userId = 1;
        when(adminUserMapper.deleteById(userId)).thenReturn(1);

        // 调用方法
        ResponseEntity<String> response = adminUserController.deleteUser(userId);

        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("用户删除成功", response.getBody());
    }

    @Test
    void deleteUser_NotFound() {
        // 准备测试数据
        int userId = 999;
        when(adminUserMapper.deleteById(userId)).thenReturn(0);

        // 调用方法
        ResponseEntity<String> response = adminUserController.deleteUser(userId);

        // 验证结果
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("用户删除失败"));
    }
    @Test
    void getUserList_WithFilters() {
        // 测试带过滤条件的查询
        List<User> mockUsers = Collections.singletonList(new User());
        when(adminUserMapper.findByCondition("test", "123", 1, 0, 10)).thenReturn(mockUsers);
        when(adminUserMapper.countByCondition("test", "123", 1)).thenReturn(1);

        ResponseEntity<Map<String, Object>> response = adminUserController.getUserList("test", "123", 1, 1, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, ((List<?>) response.getBody().get("data")).size());
        assertEquals(1, response.getBody().get("total"));
    }

    @Test
    void createUser_InvalidData() {
        // 测试创建用户时缺少必要字段
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", "testUser");
        // 缺少其他必要字段

        ResponseEntity<String> response = adminUserController.createUser(userMap);

        // 修改为期望400而不是500
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("用户创建失败"));
    }

    @Test
    void updateUser_InvalidData() {
        // 测试更新用户时缺少必要字段
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user_id", 1);
        // 缺少其他必要字段

        ResponseEntity<String> response = adminUserController.updateUser(userMap);

        // 修改为期望400而不是500
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("用户更新失败"));
    }

    @Test
    void updateUserStatus_Success() {
        // 测试更新用户状态
        when(adminUserMapper.updateStatus(1, 0)).thenReturn(1);

        int result = adminUserController.updateUserStatus(1, 0);

        assertEquals(1, result);
        verify(adminUserMapper).updateStatus(1, 0);
    }
}
