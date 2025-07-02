package com.neucamp.testalliancehubbackend.mapper;

import com.neucamp.testalliancehubbackend.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

import com.neucamp.testalliancehubbackend.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserAdminMapper {

    @Select("<script>" +
            "SELECT user_id, company_id, username, nickname, gender, phone, email, create_time, status, is_super FROM user " +
            "<where>" +
            "   <if test='username != null'>AND username LIKE CONCAT('%', #{username}, '%')</if>" +
            "   <if test='phone != null'>AND phone LIKE CONCAT('%', #{phone}, '%')</if>" +
            "   <if test='status != null'>AND status = #{status}</if>" +
            "</where>" +
            " LIMIT #{offset}, #{size}" +
            "</script>")
    List<User> findByCondition(@Param("username") String username,
                               @Param("phone") String phone,
                               @Param("status") Integer status,
                               @Param("offset") int offset,
                               @Param("size") int size);

    @Select("<script>" +
            "SELECT COUNT(*) FROM user " +
            "<where>" +
            "   <if test='username != null'>AND username LIKE CONCAT('%', #{username}, '%')</if>" +
            "   <if test='phone != null'>AND phone LIKE CONCAT('%', #{phone}, '%')</if>" +
            "   <if test='status != null'>AND status = #{status}</if>" +
            "</where>" +
            "</script>")
    int countByCondition(@Param("username") String username,
                         @Param("phone") String phone,
                         @Param("status") Integer status);

    @Insert("INSERT INTO user (username, nickname, phone, email, company_id, gender, password, create_time, status, is_super) " +
            "VALUES (#{username}, #{nickname}, #{phone}, #{email}, #{company_id}, #{gender}, #{password}, #{create_time}, #{status}, #{is_super})")
    @Options(useGeneratedKeys = true, keyProperty = "user_id")
    int insert(User user);

    @Update("<script>" +
            "UPDATE user " +
            "<set>" +
            "   <if test='username != null'>username = #{username},</if>" +  // 确保包含username
            "   <if test='company_id != null'>company_id = #{company_id},</if>" +
            "   <if test='nickname != null'>nickname = #{nickname},</if>" +
            "   <if test='gender != null'>gender = #{gender},</if>" +
            "   <if test='phone != null'>phone = #{phone},</if>" +
            "   <if test='email != null'>email = #{email},</if>" +
            "   <if test='status != null'>status = #{status},</if>" +
            "   <if test='is_super != null'>is_super = #{is_super}</if>" +
            "</set>" +
            "WHERE user_id = #{user_id}" +
            "</script>")
    int update(User user);

    @Update("UPDATE user SET status = #{status} WHERE user_id = #{userId}")
    int updateStatus(@Param("userId") Integer userId, @Param("status") Integer status);

    // 添加删除方法
    @Delete("DELETE FROM user WHERE user_id = #{userId}")
    int deleteById(@Param("userId") Integer userId);
}