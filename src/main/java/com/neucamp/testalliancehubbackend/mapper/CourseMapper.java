package com.neucamp.testalliancehubbackend.mapper;

import com.neucamp.testalliancehubbackend.entity.Course;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Mapper
@CrossOrigin
public interface CourseMapper {
    //查询
    @Select("select * from course")
    List<Course> getAllCrs();

    //添加课程
    @Insert("INSERT INTO course(course_name, cover_url, description, sort_order, video_url, author, status, created_at, created_by) " +
            "VALUES (#{course_name}, #{cover_url}, #{description}, #{sort_order}, #{video_url}, #{author}, #{status}, #{created_at}, #{created_by})")
    @Options(useGeneratedKeys = true, keyProperty = "course_id", keyColumn = "course_id")
    int addCourse(Course course);

    //删除
    @Delete("delete from course where course_id=#{course_id}")
    int delCourse(int course_id);

    // 修改
    @Update("update course set course_name=#{course_name},cover_url=#{cover_url},description=#{description}," +
            "sort_order=#{sort_order},video_url=#{video_url},author=#{author},status=#{status}," +
            "updated_at=#{updated_at,jdbcType=TIMESTAMP} " +
            "where course_id=#{course_id}")
    int updateCourse(Course course);

    // 分页查询（新增）
    @Select("SELECT * FROM course LIMIT #{offset}, #{pageSize}")
    List<Course> getCoursesWithPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 获取总数（新增）
    @Select("SELECT COUNT(*) FROM course")
    int getCourseCount();

    // 带条件的分页查询（修改后）
    @Select("<script>" +
            "SELECT * FROM course " +
            "<where>" +
            "   <if test='courseName != null and courseName != \"\"'>" +
            "       AND course_name LIKE CONCAT('%', #{courseName}, '%')" +
            "   </if>" +
            "   <if test='sortOrder != null'>" +
            "       AND sort_order = #{sortOrder}" +
            "   </if>" +
            "   <if test='status != null'>" +
            "       AND status = #{status}" +
            "   </if>" +
            "</where>" +
            "LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<Course> getCoursesWithPageByCondition(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("courseName") String courseName,
            @Param("sortOrder") Integer sortOrder,
            @Param("status") Integer status);

    // 带条件的总数查询（修改后）
    @Select("<script>" +
            "SELECT COUNT(*) FROM course " +
            "<where>" +
            "   <if test='courseName != null and courseName != \"\"'>" +
            "       AND course_name LIKE CONCAT('%', #{courseName}, '%')" +
            "   </if>" +
            "   <if test='sortOrder != null'>" +
            "       AND sort_order = #{sortOrder}" +
            "   </if>" +
            "   <if test='status != null'>" +
            "       AND status = #{status}" +
            "   </if>" +
            "</where>" +
            "</script>")
    int getCourseCountByCondition(
            @Param("courseName") String courseName,
            @Param("sortOrder") Integer sortOrder,
            @Param("status") Integer status);

    @Select("SELECT c.*, u.nickname as created_by_name FROM course c LEFT JOIN user u ON c.created_by = u.user_id WHERE c.course_id = #{course_id}")
    Course getCourseWithUser(@Param("course_id") int course_id);

    // 更新课程状态方法
    @Update("UPDATE course SET status=#{status}, updated_at=#{updated_at,jdbcType=TIMESTAMP} WHERE course_id=#{course_id}")
    int updateCourseStatus(Course course);
}