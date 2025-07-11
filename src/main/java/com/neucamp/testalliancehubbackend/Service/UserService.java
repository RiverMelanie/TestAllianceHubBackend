package com.neucamp.testalliancehubbackend.Service;

import com.neucamp.testalliancehubbackend.entity.User;
import com.neucamp.testalliancehubbackend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User login(String username, String password, String companyName) {
        System.out.println("Received Login Request: " + username + ", " + password + ", " + companyName);
        return userMapper.finduserForLogin(username, password, companyName);
    }
}
