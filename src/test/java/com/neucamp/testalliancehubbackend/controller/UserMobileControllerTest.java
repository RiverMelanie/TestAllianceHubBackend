package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.Service.UserService;
import com.neucamp.testalliancehubbackend.entity.LoginRequest;
import com.neucamp.testalliancehubbackend.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserMobileControllerTest {

    private UserMobileController controller;
    private FakeUserService fakeUserService;

    static class FakeUserService extends UserService {
        @Override
        public User login(String username, String password, String companyName) {
            if ("user".equals(username) && "pass".equals(password) && "company".equals(companyName)) {
                User user = new User();
                user.setUser_id(100);
                user.setUsername("user");
                user.setNickname("nickname");
                user.setEmail("email@example.com");
                user.setPhone("1234567890");
                return user;
            }
            return null;
        }
    }

    @BeforeEach
    void setup() {
        controller = new UserMobileController();
        fakeUserService = new FakeUserService();
        controller.userService = fakeUserService;
    }

    @Test
    void testLoginSuccess() {
        LoginRequest req = new LoginRequest();
        req.setUsername("user");
        req.setPassword("pass");
        req.setCompanyName("company");

        ResponseEntity<?> response = controller.login(req);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Object body = response.getBody();
        assertTrue(body instanceof Map);
        Map<?, ?> map = (Map<?, ?>) body;
        assertEquals(100, map.get("user_id"));
        assertEquals("user", map.get("username"));
    }

    @Test
    void testLoginFailure() {
        LoginRequest req = new LoginRequest();
        req.setUsername("wrong");
        req.setPassword("wrong");
        req.setCompanyName("wrong");

        ResponseEntity<?> response = controller.login(req);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Object body = response.getBody();
        assertTrue(body instanceof Map);
        Map<?, ?> map = (Map<?, ?>) body;
        assertEquals("用户名、密码或公司名错误", map.get("message"));
    }
}
