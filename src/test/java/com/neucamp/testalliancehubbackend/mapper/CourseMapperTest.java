package com.neucamp.testalliancehubbackend.mapper;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseMapperTest {

    @Autowired
    private CourseMapper courseMapper;

    @Test
    void testGetAllCrs() {
        List<Course> courses = courseMapper.getAllCrs();
        assertNotNull(courses);
    }

    @Test
    void testAddCourse() {
        Course course = new Course();
        course.setCourse_name("测试课程");
        course.setDescription("测试描述");
        course.setSort_order(1);
        course.setAuthor("测试作者");
        course.setStatus(1);
        course.setCreated_at(LocalDateTime.now());
        course.setCreated_by(1);

        int result = courseMapper.addCourse(course);
        assertEquals(1, result);
        assertTrue(course.getCourse_id() > 0);
    }

    @Test
    void testDelCourse() {
        // 先添加一个课程用于测试删除
        Course course = new Course();
        course.setCourse_name("待删除课程");
        course.setDescription("测试删除");
        course.setSort_order(1);
        course.setAuthor("测试作者");
        course.setStatus(1);
        course.setCreated_at(LocalDateTime.now());
        course.setCreated_by(1);
        courseMapper.addCourse(course);

        int result = courseMapper.delCourse(course.getCourse_id());
        assertEquals(1, result);
    }

    @Test
    void testUpdateCourse() {
        // 先添加一个课程用于测试更新
        Course course = new Course();
        course.setCourse_name("原始课程");
        course.setDescription("原始描述");
        course.setSort_order(1);
        course.setAuthor("原始作者");
        course.setStatus(1);
        course.setCreated_at(LocalDateTime.now());
        course.setCreated_by(1);
        courseMapper.addCourse(course);

        // 更新课程信息
        course.setCourse_name("更新后的课程");
        course.setDescription("更新后的描述");
        course.setUpdated_at(LocalDateTime.now());

        int result = courseMapper.updateCourse(course);
        assertEquals(1, result);
    }

    @Test
    void testGetCoursesWithPageByCondition() {
        // 添加一些测试数据
        for (int i = 1; i <= 5; i++) {
            Course course = new Course();
            course.setCourse_name("课程" + i);
            course.setDescription("描述" + i);
            course.setSort_order(i);
            course.setAuthor("作者" + i);
            course.setStatus(1);
            course.setCreated_at(LocalDateTime.now());
            course.setCreated_by(1);
            courseMapper.addCourse(course);
        }

        // 测试分页查询
        List<Course> courses = courseMapper.getCoursesWithPageByCondition(0, 3, null, null, null);
        assertEquals(3, courses.size());

        // 测试带条件查询
        courses = courseMapper.getCoursesWithPageByCondition(0, 10, "课程1", null, null);
        assertEquals(1, courses.size());
        assertEquals("课程1", courses.get(0).getCourse_name());
    }

    @Test
    void testGetCourseCountByCondition() {
        // 添加一些测试数据
        for (int i = 1; i <= 5; i++) {
            Course course = new Course();
            course.setCourse_name("课程" + i);
            course.setDescription("描述" + i);
            course.setSort_order(i);
            course.setAuthor("作者" + i);
            course.setStatus(i % 2 + 1); // 状态1或2
            course.setCreated_at(LocalDateTime.now());
            course.setCreated_by(1);
            courseMapper.addCourse(course);
        }

        // 测试总数查询
        int total = courseMapper.getCourseCountByCondition(null, null, null);
        assertTrue(total >= 5);

        // 测试带条件查询
        int count = courseMapper.getCourseCountByCondition("课程1", null, null);
        assertEquals(1, count);

        count = courseMapper.getCourseCountByCondition(null, 1, null);
        assertTrue(count >= 1);

        count = courseMapper.getCourseCountByCondition(null, null, 1);
        assertTrue(count >= 1);
    }

    @Test
    void testGetCourseWithUser() {
        // 添加一个测试课程
        Course course = new Course();
        course.setCourse_name("用户关联测试课程");
        course.setDescription("测试用户关联");
        course.setSort_order(1);
        course.setAuthor("测试作者");
        course.setStatus(1);
        course.setCreated_at(LocalDateTime.now());
        course.setCreated_by(1); // 假设用户ID为1存在
        courseMapper.addCourse(course);

        // 测试查询
        Course result = courseMapper.getCourseWithUser(course.getCourse_id());
        assertNotNull(result);
        assertEquals("用户关联测试课程", result.getCourse_name());
        assertNotNull(result.getCreated_by_name());
    }

    @Test
    void testUpdateCourseStatus() {
        // 添加一个测试课程
        Course course = new Course();
        course.setCourse_name("状态测试课程");
        course.setDescription("测试状态更新");
        course.setSort_order(1);
        course.setAuthor("测试作者");
        course.setStatus(1);
        course.setCreated_at(LocalDateTime.now());
        course.setCreated_by(1);
        courseMapper.addCourse(course);

        // 准备更新数据
        course.setStatus(2);
        course.setUpdated_at(LocalDateTime.now());

        // 测试更新
        int result = courseMapper.updateCourseStatus(course);
        assertEquals(1, result);

        // 验证更新结果
        Course updatedCourse = courseMapper.getCourseWithUser(course.getCourse_id());
        assertEquals(2, updatedCourse.getStatus());
    }
}