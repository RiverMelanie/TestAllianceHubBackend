package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.entity.Company;
import com.neucamp.testalliancehubbackend.mapper.CompanyMapper;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/company")
@CrossOrigin(origins = "http://localhost:5173")
public class CompanyController {
    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private CompanyMapper companyMapper;

    @GetMapping("/next-id")
    public ResponseEntity<Map<String, Object>> getNextCompanyId() {
        Map<String, Object> response = new HashMap<>();
        try {
            int nextId = companyMapper.getNextCompanyId(); // 确保 Mapper 方法存在
            response.put("success", true);
            response.put("nextId", nextId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取企业ID失败");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerCompany(@RequestBody Map<String, String> registerData) {
        Map<String, Object> response = new HashMap<>();

        try {
            String companyName = registerData.get("company_name");
            String contactInfo = registerData.get("contact_info");
            String account = registerData.get("account");
            String password = registerData.get("password");

            // 验证参数
            if (companyName == null || contactInfo == null || account == null || password == null) {
                response.put("success", false);
                response.put("message", "参数不完整");
                return ResponseEntity.badRequest().body(response);
            }

            // 创建企业对象
            Company company = new Company();
            company.setCompanyName(companyName);
            company.setContactInfo(contactInfo);
            company.setAccount(account);
            company.setPassword(password);
            // create_time由数据库的NOW()自动生成
            // company_id由数据库自动生成并回填

            // 注册企业
            int result = companyMapper.registerCompany(company);

            if (result > 0) {
                // 注册成功后，company对象会被回填自动生成的companyId
                response.put("success", true);
                response.put("message", "注册成功");
                response.put("companyId", company.getCompanyId()); // 这里可以获取到自动生成的ID
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "注册失败");
                return ResponseEntity.internalServerError().body(response);
            }
        } catch (Exception e) {
            //logger.error("企业注册失败", e);
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}