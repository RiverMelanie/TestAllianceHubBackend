package com.neucamp.testalliancehubbackend.mapper;

import com.neucamp.testalliancehubbackend.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    @Select("select * from user where username=#{username} and password=#{password}")
    User Login(User user);

    // 获取下一个用户ID
    @Select("SELECT COUNT(*) + 1 FROM user")
    int getNextUserId();

    // 检查企业是否存在
    @Select("SELECT COUNT(*) FROM company WHERE company_id = #{company_id}")
    int checkCompanyExists(int companyId);

    // 注册用户
    @Insert("INSERT INTO user (company_id, username, nickname, phone, email, gender, password,create_time,status,is_super) " +
            "VALUES (#{company_id}, #{username}, #{nickname}, #{phone}, #{email},#{gender}, #{password}, NOW(),1,0)")
    @Options(useGeneratedKeys = true, keyProperty = "user_id")
    int registerUser(User user);

    @Select("SELECT * FROM user WHERE user_id = #{userId}")
    User getUserById(Integer userId);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User getUserByUsername(String username);

    @Update("UPDATE user SET nickname=#{nickname}, phone=#{phone}, email=#{email}, gender=#{gender} WHERE user_id=#{user_id}")
    int updateUser(User user);

    @Select("SELECT * FROM user")
    List<User> getAllUsers();

    @Update("UPDATE user SET password = #{password} WHERE user_id = #{user_id}")
    int updateUserPassword(User user);

    // 检查用户名是否存在
    @Select("SELECT COUNT(*) FROM user WHERE username = #{username}")
    boolean checkUsernameExists(String username);

    @Select("<script>" +
            "SELECT * FROM user " +
            "<where>" +
            "   <if test='username != null'>AND username LIKE #{username}</if>" +
            "   <if test='phone != null'>AND phone LIKE #{phone}</if>" +
            "   <if test='status != null'>AND status = #{status}</if>" +
            "</where>" +
            "ORDER BY user_id DESC" +
            "</script>")
    List<User> getUsersByCondition(Map<String, Object> params);

    @Select("SELECT * FROM user WHERE token = #{token}")
    User getUserByToken(String token);

    @Select("SELECT u.user_id, u.company_id, u.username, u.nickname, u.phone, u.email FROM user u " +
            "JOIN company c ON u.company_id = c.company_id " +
            "WHERE u.username = #{username} AND u.password = #{password} AND c.company_name = #{companyName}")
    @Results({
            @Result(column = "user_id", property = "user_id"),
            @Result(column = "company_id", property = "company_id"),
            @Result(column = "username", property = "username"),
            @Result(column = "nickname", property = "nickname"),
            @Result(column = "phone", property = "phone"),
            @Result(column = "email", property = "email")
    })
    User finduserForLogin(@Param("username") String username,
                          @Param("password") String password,
                          @Param("companyName") String companyName);


}
