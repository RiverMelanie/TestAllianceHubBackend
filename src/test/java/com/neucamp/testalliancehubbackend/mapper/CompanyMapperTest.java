package com.neucamp.testalliancehubbackend.mapper;

import com.neucamp.testalliancehubbackend.entity.Company;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CompanyMapperTest {

    @Autowired
    private CompanyMapper companyMapper;

    @Test
    void testGetNextCompanyId() {
        // 调用方法
        int nextId = companyMapper.getNextCompanyId();

        // 验证结果
        assertTrue(nextId > 0);
    }

    @Test
    void testRegisterCompany() {
        // 准备测试数据
        Company company = new Company();
        company.setCompanyName("Test Company");
        company.setContactInfo("contact@test.com");
        company.setAccount("testaccount");
        company.setPassword("testpass");

        // 调用方法
        int result = companyMapper.registerCompany(company);

        // 验证结果
        assertEquals(1, result);
        assertNotNull(company.getCompanyId());
    }

    @Test
    void testCheckCompanyExists() {
        // 准备测试数据 - 假设ID为1的公司存在
        int existingCompanyId = 1;

        // 调用方法
        int count = companyMapper.checkCompanyExists(existingCompanyId);

        // 验证结果
        assertEquals(1, count);
    }
}