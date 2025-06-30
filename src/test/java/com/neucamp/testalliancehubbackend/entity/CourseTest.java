package com.neucamp.testalliancehubbackend.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void testCourseConstructorAndGettersSetters() {
        // 测试无参构造函数和setter/getter方法
        Course course = new Course();
        course.setCourse_id(1);
        course.setCourse_name("Java基础");
        course.setCover_url("cover.jpg");
        course.setDescription("Java入门课程");
        course.setSort_order(1);
        course.setVideo_url("video.mp4");
        course.setAuthor("张老师");
        course.setStatus(1);
        course.setCreated_by(1);
        LocalDateTime now = LocalDateTime.now();
        course.setCreated_at(now);
        course.setUpdated_at(now);
        course.setCreated_by_name("张老师");

        assertEquals(1, course.getCourse_id());
        assertEquals("Java基础", course.getCourse_name());
        assertEquals("cover.jpg", course.getCover_url());
        assertEquals("Java入门课程", course.getDescription());
        assertEquals(1, course.getSort_order());
        assertEquals("video.mp4", course.getVideo_url());
        assertEquals("张老师", course.getAuthor());
        assertEquals(1, course.getStatus());
        assertEquals(1, course.getCreated_by());
        assertEquals(now, course.getCreated_at());
        assertEquals(now, course.getUpdated_at());
        assertEquals("张老师", course.getCreated_by_name());
    }

    @Test
    void testCourseAllArgsConstructor() {
        // 测试全参构造函数
        LocalDateTime now = LocalDateTime.now();
        Course course = new Course(1, "Java基础", "cover.jpg", "Java入门课程",
                1, "video.mp4", "张老师", 1, 1, now, now);

        assertEquals(1, course.getCourse_id());
        assertEquals("Java基础", course.getCourse_name());
        assertEquals("cover.jpg", course.getCover_url());
        assertEquals("Java入门课程", course.getDescription());
        assertEquals(1, course.getSort_order());
        assertEquals("video.mp4", course.getVideo_url());
        assertEquals("张老师", course.getAuthor());
        assertEquals(1, course.getStatus());
        assertEquals(1, course.getCreated_by());
        assertEquals(now, course.getCreated_at());
        assertEquals(now, course.getUpdated_at());
    }

    @Test
    void testCourseEquality() {
        LocalDateTime now = LocalDateTime.now();
        Course course1 = new Course(1, "Java基础", "cover.jpg", "Java入门课程",
                1, "video.mp4", "张老师", 1, 1, now, now);
        Course course2 = new Course(1, "Java基础", "cover.jpg", "Java入门课程",
                1, "video.mp4", "张老师", 1, 1, now, now);

        // 测试对象相等性
        assertEquals(course1.getCourse_id(), course2.getCourse_id());
        assertEquals(course1.getCourse_name(), course2.getCourse_name());
        assertEquals(course1.getCover_url(), course2.getCover_url());
        assertEquals(course1.getDescription(), course2.getDescription());
        assertEquals(course1.getSort_order(), course2.getSort_order());
        assertEquals(course1.getVideo_url(), course2.getVideo_url());
        assertEquals(course1.getAuthor(), course2.getAuthor());
        assertEquals(course1.getStatus(), course2.getStatus());
        assertEquals(course1.getCreated_by(), course2.getCreated_by());
        assertEquals(course1.getCreated_at(), course2.getCreated_at());
        assertEquals(course1.getUpdated_at(), course2.getUpdated_at());
    }

}