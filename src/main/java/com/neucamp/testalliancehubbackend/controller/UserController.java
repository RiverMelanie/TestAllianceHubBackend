package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.entity.User;
import com.neucamp.testalliancehubbackend.mapper.CompanyMapper;
import com.neucamp.testalliancehubbackend.mapper.UserMapper;
import org.apache.catalina.deploy.NamingResourcesImpl;
import org.apache.tomcat.util.descriptor.web.ContextTransaction;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserMapper userMapper;

    @Autowired
    private CompanyMapper companyMapper;

    private NamingResourcesImpl transactionManager;

    //登录
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, Object> loginData) {
        Map<String, Object> response = new HashMap<>();

        // 获取前端传递的数据
        String username = (String) loginData.get("uname");
        String password = (String) loginData.get("upwd");
        String loginType = (String) loginData.get("loginType"); // "user" or "admin"

        // 验证参数
        if (username == null || password == null || loginType == null) {
            response.put("success", false);
            response.put("message", "参数不完整");
            return response;
        }

        // 创建User对象查询数据库
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        User dbUser = userMapper.Login(user);

        if (dbUser == null) {
            // 用户名或密码错误
            response.put("success", false);
            response.put("message", "用户名或密码错误");
            return response;
        }

        // 检查用户类型是否匹配

        Byte isSuper = dbUser.getIs_super();
        boolean isAdmin = isSuper != null && isSuper == 1;

        if (("admin".equals(loginType) && !isAdmin)){
            response.put("success", false);
            response.put("message", "该账号不是管理员账号");
            return response;
        }

        if (("user".equals(loginType) && isAdmin)) {
            response.put("success", false);
            response.put("message", "管理员账号请使用管理员登录");
            return response;
        }

        if (dbUser != null) {
            response.put("token", String.valueOf(dbUser.getUser_id())); // 关键修改
        }

        // 登录成功
        response.put("success", true);
        response.put("userType", isAdmin ? "admin" : "user");
        response.put("userInfo", dbUser);

        return response;
    }

    @GetMapping("/next-id")
    public ResponseEntity<Map<String, Object>> getNextUserId() {
        Map<String, Object> response = new HashMap<>();
        try {
            int nextId = userMapper.getNextUserId(); // 确保SQL查询正确
            response.put("success", true);
            response.put("nextId", nextId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            //logger.error("获取下一个用户ID失败", e);
            response.put("success", false);
            response.put("message", "获取用户ID失败");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 用户注册接口
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, String> registerData) {
        Map<String, Object> response = new HashMap<>();
         try {

            // 检查企业是否存在
            int companyId = Integer.parseInt(registerData.get("company_id"));
            if (userMapper.checkCompanyExists(companyId) == 0) {
                response.put("success", false);
                response.put("message", "该企业不存在");
                return ResponseEntity.badRequest().body(response);
            }

            // 创建用户对象
            User user = new User();
            user.setCompany_id(companyId);
            user.setUsername(registerData.get("username"));
            user.setNickname(registerData.get("nickname"));
            user.setPhone(registerData.get("phone"));
            user.setEmail(registerData.get("email"));
            //user.setGender(Integer.parseInt(registerData.get("gender").toString()));

            int gender  = Integer.parseInt(registerData.get("gender"));
            if (gender  != 0) {
                user.setGender(gender);
            } else {
                response.put("success", false);
                response.put("message", "性别参数缺失");
                return ResponseEntity.badRequest().body(response);
            }

            user.setPassword(registerData.get("password"));

            // 注册用户
            int result = userMapper.registerUser(user);

            if (result > 0) {
                response.put("success", true);
                response.put("message", "注册成功");
                response.put("userId", user.getUser_id());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "注册失败");
                return ResponseEntity.internalServerError().body(response);
            }
        } catch (NumberFormatException e) {
            response.put("success", false);
            response.put("message", "参数格式错误");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 获取用户信息
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserInfo(@PathVariable Integer userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userMapper.getUserById(userId);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.notFound().build();
            }

            // 不返回密码等敏感信息
            user.setPassword(null);
            response.put("success", true);
            response.put("userInfo", user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取用户信息失败");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 更新用户信息
    @PutMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> updateUserInfo(
            @PathVariable Integer userId,
            @RequestBody Map<String, Object> updateData,
            @RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 验证token和用户权限
            User currentUser = validateTokenAndGetUser(token);
            if (currentUser == null || !currentUser.getUser_id().equals(userId)) {
                response.put("success", false);
                response.put("message", "无权修改该用户信息");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            User user = userMapper.getUserById(userId);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.notFound().build();
            }

            // 只允许修改特定字段
            if (updateData.containsKey("nickname")) {
                user.setNickname((String) updateData.get("nickname"));
            }
            if (updateData.containsKey("phone")) {
                user.setPhone((String) updateData.get("phone"));
            }
            if (updateData.containsKey("email")) {
                user.setEmail((String) updateData.get("email"));
            }
            if (updateData.containsKey("gender")) {
                user.setGender((Integer) updateData.get("gender"));
            }

            int result = userMapper.updateUser(user);
            if (result > 0) {
                response.put("success", true);
                response.put("message", "更新成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "更新失败");
                return ResponseEntity.internalServerError().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.internalServerError().body(response);
        }
    }

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
