package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.Service.UserService;
import com.neucamp.testalliancehubbackend.entity.LoginRequest;
import com.neucamp.testalliancehubbackend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
@RestController
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET},
        maxAge = 3600
)
public class UserMobileController {
    @Autowired
    UserService userService;
    //移动端登录
    @RequestMapping("/mobileLogin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(
                loginRequest.getUsername(),
                loginRequest.getPassword(),
                loginRequest.getCompanyName()
        );

        if (user != null) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("user_id", user.getUser_id());
            userInfo.put("username", user.getUsername());
            userInfo.put("nickname", user.getNickname());
            userInfo.put("email", user.getEmail());
            userInfo.put("phone", user.getPhone());
            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "用户名、密码或公司名错误"));
        }
    }
}
