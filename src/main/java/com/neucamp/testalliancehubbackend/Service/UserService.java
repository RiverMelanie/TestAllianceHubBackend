package com.neucamp.testalliancehubbackend.Service;

import com.neucamp.testalliancehubbackend.entity.User;
import com.neucamp.testalliancehubbackend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper1;

    public User login(String username, String password, String companyName) {
        return userMapper1.finduserForLogin(username, password, companyName);
    }
}
