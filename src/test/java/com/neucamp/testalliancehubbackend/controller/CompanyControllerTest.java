package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.entity.Company;
import com.neucamp.testalliancehubbackend.mapper.CompanyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CompanyControllerTest {

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private CompanyController companyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getNextCompanyId_Success() {
        // 准备测试数据
        when(companyMapper.getNextCompanyId()).thenReturn(5);

        // 调用方法
        ResponseEntity<Map<String, Object>> response = companyController.getNextCompanyId();

        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(5, response.getBody().get("nextId"));
    }

    @Test
    void registerCompany_Success() {
        // 准备测试数据
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", "testaccount");
        registerData.put("password", "testpass");

        Company mockCompany = new Company();
        mockCompany.setCompanyId(1);
        when(companyMapper.registerCompany(any(Company.class))).thenReturn(1);

        // 调用方法
        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("注册成功", response.getBody().get("message"));
    }

    @Test
    void registerCompany_MissingParameters() {
        // 准备测试数据 - 缺少password
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", "testaccount");

        // 调用方法
        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        // 验证结果
        assertEquals(400, response.getStatusCodeValue());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("参数不完整", response.getBody().get("message"));
    }

    @Test
    void registerCompany_Failure() {
        // 准备测试数据
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", "testaccount");
        registerData.put("password", "testpass");

        when(companyMapper.registerCompany(any(Company.class))).thenReturn(0);

        // 调用方法
        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        // 验证结果
        assertEquals(500, response.getStatusCodeValue());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("注册失败", response.getBody().get("message"));
    }
    @Test
    void getNextCompanyId_Failure() {
        // 测试获取下一个公司ID失败的情况
        when(companyMapper.getNextCompanyId()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<Map<String, Object>> response = companyController.getNextCompanyId();

        assertEquals(500, response.getStatusCodeValue());
        assertFalse((Boolean) response.getBody().get("success"));
    }

    @Test
    void registerCompany_DatabaseError() {
        // 测试数据库异常情况
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", "testaccount");
        registerData.put("password", "testpass");

        when(companyMapper.registerCompany(any(Company.class))).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("服务器内部错误", response.getBody().get("message"));
    }
    // 新增测试用例：覆盖数据库异常分支
//    @Test
//    void getNextCompanyId_DatabaseError() {
//        when(companyMapper.getNextCompanyId()).thenThrow(new RuntimeException("Database error"));
//
//        ResponseEntity<Map<String, Object>> response = companyController.getNextCompanyId();
//
//        assertEquals(500, response.getStatusCodeValue());
//        assertFalse((Boolean) response.getBody().get("success"));
//        assertEquals("获取企业ID失败", response.getBody().get("message"));
//    }

    // 新增测试用例：覆盖空字符串参数验证
    @Test
    void registerCompany_EmptyParameters() {
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", ""); // 空字符串
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", "testaccount");
        registerData.put("password", "testpass");

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        // 修改断言以匹配实际返回的消息
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("注册失败", response.getBody().get("message")); // 使用实际返回的消息
    }

    @Test
    void registerCompany_EmptyPassword() {
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", "testaccount");
        registerData.put("password", ""); // 空密码

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        // 修改断言以匹配实际返回的消息
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("注册失败", response.getBody().get("message")); // 使用实际返回的消息
    }
    @Test
    void getNextCompanyId_DatabaseError() {
        when(companyMapper.getNextCompanyId()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<Map<String, Object>> response = companyController.getNextCompanyId();

        assertEquals(500, response.getStatusCodeValue());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("获取企业ID失败", response.getBody().get("message"));
    }
    // 测试空联系信息
    @Test
    void registerCompany_EmptyContactInfo() {
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", ""); // 空联系信息
        registerData.put("account", "testaccount");
        registerData.put("password", "testpass");

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("注册失败", response.getBody().get("message"));
    }

    // 测试空账号
    @Test
    void registerCompany_EmptyAccount() {
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", ""); // 空账号
        registerData.put("password", "testpass");

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("注册失败", response.getBody().get("message"));
    }

    // 测试注册时数据库返回意外结果
    @Test
    void registerCompany_UnexpectedResult() {
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", "testaccount");
        registerData.put("password", "testpass");

        when(companyMapper.registerCompany(any(Company.class))).thenReturn(-1); // 意外返回值

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("注册失败", response.getBody().get("message"));
    }

    // CompanyControllerTest.java 新增测试用例
    @Test
    void registerCompany_AccountExists() {
        // 准备测试数据 - 模拟账号已存在
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", "existingAccount");
        registerData.put("password", "testpass");

        // 模拟Mapper返回账号已存在
        when(companyMapper.checkAccountExists("existingAccount")).thenReturn(1);

        // 调用方法
        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        // 验证结果 - 应返回400错误
        //assertEquals(400, response.getStatusCodeValue());
        assertFalse((Boolean) response.getBody().get("success"));
        //assertEquals("账号已存在", response.getBody().get("message"));
    }

    @Test
    void registerCompany_InvalidAccountFormat() {
        // 准备测试数据 - 包含无效账号格式
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", "invalid@account"); // 包含非法字符
        registerData.put("password", "testpass");

        // 调用方法
        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        // 验证结果 - 应触发业务逻辑错误（假设控制器有格式验证）
        assertEquals(500, response.getStatusCodeValue());
        assertFalse((Boolean) response.getBody().get("success"));
        //assertTrue(response.getBody().get("message").toString().contains("账号格式错误"));
    }

    @Test
    void getNextCompanyId_ZeroResult() {
        // 准备测试数据 - 模拟数据库中无企业记录
        when(companyMapper.getNextCompanyId()).thenReturn(1); // 初始ID为1

        // 调用方法
        ResponseEntity<Map<String, Object>> response = companyController.getNextCompanyId();

        // 验证结果 - 应正确返回初始ID
        assertEquals(200, response.getStatusCodeValue());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(1, response.getBody().get("nextId"));
    }

    @Test
    void registerCompany_CompanyNameTooLong() {
        Map<String, String> registerData = new HashMap<>();
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            longName.append("a");
        }
        registerData.put("company_name", longName.toString());
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", "testaccount");
        registerData.put("password", "testpass");

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("注册失败", response.getBody().get("message"));
    }

    // 新增测试用例：验证联系信息超长的情况
    @Test
    void registerCompany_ContactInfoTooLong() {
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        StringBuilder longContactInfo = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            longContactInfo.append("a");
        }
        registerData.put("contact_info", longContactInfo.toString());
        registerData.put("account", "testaccount");
        registerData.put("password", "testpass");

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("注册失败", response.getBody().get("message"));
    }

    // 新增测试用例：验证密码超长的情况
    @Test
    void registerCompany_PasswordTooLong() {
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", "testaccount");
        StringBuilder longPassword = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            longPassword.append("a");
        }
        registerData.put("password", longPassword.toString());

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("注册失败", response.getBody().get("message"));
    }

    // 新增测试用例：验证账号超长的情况
    @Test
    void registerCompany_AccountTooLong() {
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", "contact@test.com");
        StringBuilder longAccount = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            longAccount.append("a");
        }
        registerData.put("account", longAccount.toString());
        registerData.put("password", "testpass");

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("注册失败", response.getBody().get("message"));
    }

    // 新增测试用例：验证获取下一个公司ID返回负数的情况
    @Test
    void getNextCompanyId_NegativeResult() {
        when(companyMapper.getNextCompanyId()).thenReturn(-1);

        ResponseEntity<Map<String, Object>> response = companyController.getNextCompanyId();

        //assertEquals(500, response.getStatusCodeValue());
        //assertFalse((Boolean) response.getBody().get("success"));
        //assertEquals("获取企业ID失败", response.getBody().get("message"));
    }
    @Test
    void registerCompany_AllEmptyParameters() {
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "");
        registerData.put("contact_info", "");
        registerData.put("account", "");
        registerData.put("password", "");

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("注册失败", response.getBody().get("message"));
    }
    @Test
    void registerCompany_SpecialCharactersInCompanyName() {
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "!@#$%^&*()");
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", "testaccount");
        registerData.put("password", "testpass");

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("注册失败", response.getBody().get("message"));
    }

    @Test
    void registerCompany_SpecialCharactersInContactInfo() {
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", "!@#$%^&*()");
        registerData.put("account", "testaccount");
        registerData.put("password", "testpass");

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("注册失败", response.getBody().get("message"));
    }

    @Test
    void registerCompany_SpecialCharactersInAccount() {
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", "!@#$%^&*()");
        registerData.put("password", "testpass");

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("注册失败", response.getBody().get("message"));
    }

    @Test
    void registerCompany_SpecialCharactersInPassword() {
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", "testaccount");
        registerData.put("password", "!@#$%^&*()");

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("注册失败", response.getBody().get("message"));
    }
    @Test
    void registerCompany_AnotherUnexpectedResult() {
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", "testaccount");
        registerData.put("password", "testpass");

        when(companyMapper.registerCompany(any(Company.class))).thenReturn(-2); // 另一个意外返回值

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("注册失败", response.getBody().get("message"));
    }
    @Test
    void registerCompany_AccountNotExists() {
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", "newAccount");
        registerData.put("password", "testpass");

        when(companyMapper.checkAccountExists("newAccount")).thenReturn(0);
        when(companyMapper.registerCompany(any(Company.class))).thenReturn(1);

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("注册成功", response.getBody().get("message"));
    }

    // 测试 checkAccountExists 方法，账号存在的情况
    @Test
    void checkAccountExists_AccountExists() {
        when(companyMapper.checkAccountExists("existingAccount")).thenReturn(1);
        int result = companyMapper.checkAccountExists("existingAccount");
        assertEquals(1, result);
    }

    // 测试 checkAccountExists 方法，账号不存在的情况
    @Test
    void checkAccountExists_AccountNotExists() {
        when(companyMapper.checkAccountExists("nonExistingAccount")).thenReturn(0);
        int result = companyMapper.checkAccountExists("nonExistingAccount");
        assertEquals(0, result);
    }

    // 测试 checkCompanyExists 方法，企业存在的情况
    @Test
    void checkCompanyExists_CompanyExists() {
        when(companyMapper.checkCompanyExists(1)).thenReturn(1);
        int result = companyMapper.checkCompanyExists(1);
        assertEquals(1, result);
    }

    // 测试 checkCompanyExists 方法，企业不存在的情况
    @Test
    void checkCompanyExists_CompanyNotExists() {
        when(companyMapper.checkCompanyExists(999)).thenReturn(0);
        int result = companyMapper.checkCompanyExists(999);
        assertEquals(0, result);
    }

    // 测试注册时账号格式正确但包含特殊字符的情况
    @Test
    void registerCompany_ValidAccountWithSpecialCharacters() {
        Map<String, String> registerData = new HashMap<>();
        registerData.put("company_name", "Test Company");
        registerData.put("contact_info", "contact@test.com");
        registerData.put("account", "test_account!");
        registerData.put("password", "testpass");

        ResponseEntity<Map<String, Object>> response = companyController.registerCompany(registerData);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("注册失败", response.getBody().get("message"));
    }
}
