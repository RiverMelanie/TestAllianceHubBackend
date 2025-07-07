package com.neucamp.testalliancehubbackend.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void testGettersAndSetters() {
        LoginRequest req = new LoginRequest();
        req.setUsername("admin");
        req.setPassword("123456");
        req.setCompanyName("OpenAI");

        assertEquals("admin", req.getUsername());
        assertEquals("123456", req.getPassword());
        assertEquals("OpenAI", req.getCompanyName());
    }
}
