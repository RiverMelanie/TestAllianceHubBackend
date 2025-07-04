package com.neucamp.testalliancehubbackend.entity;

import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {

    @Test
    void testCompanyEntity() {
        // 创建测试数据
        Company company = new Company();
        Date now = new Date(System.currentTimeMillis());

        // 测试setter和getter
        company.setCompanyId(1);
        company.setCompanyName("Test Company");
        company.setContactInfo("contact@test.com");
        company.setAccount("testaccount");
        company.setPassword("testpass");
        company.setCreateTime(now);

        // 验证getter返回值
        assertEquals(1, company.getCompanyId());
        assertEquals("Test Company", company.getCompanyName());
        assertEquals("contact@test.com", company.getContactInfo());
        assertEquals("testaccount", company.getAccount());
        assertEquals("testpass", company.getPassword());
        assertEquals(now, company.getCreateTime());
    }
}
