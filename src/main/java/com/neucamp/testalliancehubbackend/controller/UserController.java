package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.Service.UserService;
import com.neucamp.testalliancehubbackend.entity.LoginRequest;
import com.neucamp.testalliancehubbackend.entity.User;
import com.neucamp.testalliancehubbackend.mapper.CompanyMapper;
import com.neucamp.testalliancehubbackend.mapper.UserMapper;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
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
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserMapper userMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private UserService userService;

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
        boolean isAdmin = dbUser.getIs_super() == 1;

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
            String companyId = registerData.get("company_id");
            String username = registerData.get("username");
            String nickname = registerData.get("nickname");
            String phone = registerData.get("phone");
            String email = registerData.get("email");
            String gender = registerData.get("gender");
            String password = registerData.get("password");


            // 检查企业是否存在
            int companyId2 = Integer.parseInt(registerData.get("company_id").toString());
            if (userMapper.checkCompanyExists(companyId2) == 0) {
                response.put("success", false);
                response.put("message", "该企业不存在");
                return ResponseEntity.badRequest().body(response);
            }

            // 创建用户对象
            User user = new User();
            user.setCompany_id(Integer.valueOf(companyId));
            user.setUsername(registerData.get("username").toString());
            user.setNickname(registerData.get("nickname").toString());
            user.setPhone(registerData.get("phone").toString());
            user.setEmail(registerData.get("email").toString());

            //user.setGender(Integer.parseInt(registerData.get("gender").toString()));

//            String res = registerData.get("gender").toString();
//            Byte res1 = (byte) Integer.parseInt(res);
//            user.setGender((byte) res1);

            user.setPassword(registerData.get("password").toString());
//            user.setStatus((byte)1); // 默认状态正常
//            user.setIs_super((byte)0); // 默认不是管理员


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
        } catch (Exception e) {
            //logger.error((Supplier<String>) e);
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    //移动端登录
    @PostMapping("/mobileLogin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(
                loginRequest.getUsername(),
                loginRequest.getPassword(),
                loginRequest.getCompanyName()
        );

        if (user != null) {
            // 可选择只返回必要字段，如 id、username、email 等
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
