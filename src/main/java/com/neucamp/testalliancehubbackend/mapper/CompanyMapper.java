package com.neucamp.testalliancehubbackend.mapper;

import com.neucamp.testalliancehubbackend.entity.Company;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CompanyMapper {
    // 获取下一个企业ID
    @Select("SELECT COUNT(*) + 1 AS nextId FROM company")
    int getNextCompanyId();

    // 注册新企业
    @Insert("INSERT INTO company (company_name, contact_info, account, password, create_time) " +
            "VALUES (#{companyName}, #{contactInfo}, #{account}, #{password}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "companyId")
    int registerCompany(Company company);

    // 检查账号是否已存在
    @Select("SELECT COUNT(*) FROM company WHERE account = #{account}")
    int checkAccountExists(String account);

    // 检查企业是否存在
    @Select("SELECT COUNT(*) FROM company WHERE company_id = #{companyId}")
    int checkCompanyExists(Integer companyId);

}