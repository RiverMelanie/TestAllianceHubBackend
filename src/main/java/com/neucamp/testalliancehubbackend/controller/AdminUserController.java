package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.mapper.UserAdminMapper;
import com.neucamp.testalliancehubbackend.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/user")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminUserController {

    private final UserAdminMapper adminUserMapper;
    private Charset passwordEncoder;

    public AdminUserController(UserAdminMapper adminUserMapper) {
        this.adminUserMapper = adminUserMapper;
    }

    // 获取用户列表（支持模糊查询）
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getUserList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            // 计算偏移量
            int offset = (page - 1) * size;
            List<User> users = adminUserMapper.findByCondition(username, phone, status, offset, size);
            int total = adminUserMapper.countByCondition(username, phone, status);

            Map<String, Object> response = new HashMap<>();
            response.put("data", users);
            response.put("total", total);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody Map<String, Object> userMap) {
        try {
            User user = new User();
            user.setUsername((String) userMap.get("username"));
            user.setNickname((String) userMap.get("nickname"));
            user.setPhone((String) userMap.get("phone"));
            user.setEmail((String) userMap.get("email"));
            user.setCompany_id(userMap.get("company_id") != null ?
                    ((Number) userMap.get("company_id")).intValue() : 0);
            user.setGender(userMap.get("gender") != null ?
                    ((Number) userMap.get("gender")).intValue() : 0);
            user.setPassword((String) userMap.get("password"));

            // 修复状态值问题 - 确保1表示启用，0表示禁用
            user.setStatus(userMap.get("status") != null ?
                    ((Number) userMap.get("status")).byteValue() : (byte)1); // 默认启用

            user.setIs_super(userMap.get("is_super") != null ?
                    ((Number) userMap.get("is_super")).byteValue() : (byte)0);
            user.setCreate_time(new Date());

            int result = adminUserMapper.insert(user);
            return result > 0
                    ? ResponseEntity.ok("用户创建成功")
                    : ResponseEntity.badRequest().body("用户创建失败");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("创建用户时出错：" + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody Map<String, Object> userMap) {
        try {
            User user = new User();
            user.setUser_id(((Number) userMap.get("user_id")).intValue());
            user.setUsername((String) userMap.get("username")); // 恢复用户名更新
            user.setCompany_id(userMap.get("company_id") != null ?
                    ((Number) userMap.get("company_id")).intValue() : 0);
            user.setNickname((String) userMap.get("nickname"));
            user.setGender(userMap.get("gender") != null ?
                    ((Number) userMap.get("gender")).intValue() : 0);
            user.setPhone((String) userMap.get("phone"));
            user.setEmail((String) userMap.get("email"));
            user.setStatus(userMap.get("status") != null ?
                    ((Number) userMap.get("status")).byteValue() : null);
            user.setIs_super(userMap.get("is_super") != null ?
                    ((Number) userMap.get("is_super")).byteValue() : null);

            int result = adminUserMapper.update(user);
            return result > 0
                    ? ResponseEntity.ok("用户更新成功")
                    : ResponseEntity.badRequest().body("用户更新失败");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("更新用户时出错：" + e.getMessage());
        }
    }

    // 修改用户状态
    @PutMapping("/updateStatus")
    public int updateUserStatus(@RequestParam Integer userId, @RequestParam Integer status) {
        return adminUserMapper.updateStatus(userId, status);
    }

    // 添加删除用户接口
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {
        try {
            int result = adminUserMapper.deleteById(userId);
            return result > 0
                    ? ResponseEntity.ok("用户删除成功")
                    : ResponseEntity.badRequest().body("用户删除失败，可能用户不存在");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("删除用户时出错：" + e.getMessage());
        }
    }
}