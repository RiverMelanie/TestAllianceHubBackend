package com.neucamp.testalliancehubbackend.Service;

import com.neucamp.testalliancehubbackend.entity.User;
import com.neucamp.testalliancehubbackend.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() {
        // 准备测试数据
        String username = "testuser";
        String password = "123456";
        String companyName = "TestCompany";

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(password);
        mockUser.setCompany_id(1);
        mockUser.setUser_id(1001);

        // 设置 mock 行为
        when(userMapper.finduserForLogin(username, password, companyName)).thenReturn(mockUser);

        // 调用 Service 方法
        User result = userService.login(username, password, companyName);

        // 验证 mapper 调用
        verify(userMapper, times(1)).finduserForLogin(username, password, companyName);

        // 验证返回结果
        assertNotNull(result);
        assertEquals(1001, result.getUser_id());
        assertEquals("testuser", result.getUsername());
        assertEquals("123456", result.getPassword());
    }

    @Test
    void testLogin_UserNotFound() {
        String username = "notExist";
        String password = "wrong";
        String companyName = "NoCompany";

        when(userMapper.finduserForLogin(username, password, companyName)).thenReturn(null);

        User result = userService.login(username, password, companyName);

        verify(userMapper, times(1)).finduserForLogin(username, password, companyName);
        assertNull(result);
    }
}
