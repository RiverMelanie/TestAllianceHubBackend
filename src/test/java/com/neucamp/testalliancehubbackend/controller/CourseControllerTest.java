package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.entity.Course;
import com.neucamp.testalliancehubbackend.mapper.CourseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class CourseControllerTest {

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseController courseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        courseController.setUploadDir("./uploads/");
        courseController.setImageDir("images/");
        courseController.setVideoDir("videos/");
    }

    @Test
    void testGetAllCrs() {
        // 准备测试数据
        List<Course> mockCourses = new ArrayList<>();
        mockCourses.add(new Course(1, "Java基础", "cover1.jpg", "Java入门课程", 1, "video1.mp4", "张老师", 1, 1, LocalDateTime.now(), null));

        when(courseMapper.getCoursesWithPageByCondition(anyInt(), anyInt(), any(), any(), any())).thenReturn(mockCourses);
        when(courseMapper.getCourseCountByCondition(any(), any(), any())).thenReturn(1);

        // 执行测试
        Map<String, Object> result = courseController.getallcrs(1, 10, null, null, null);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, ((List<?>) result.get("data")).size());
        assertEquals(1, result.get("total"));
    }

    @Test
    void testGetAllCrsWithInvalidPageNum() {
        // 准备测试数据
        List<Course> mockCourses = new ArrayList<>();
        mockCourses.add(new Course(1, "Java基础", "cover1.jpg", "Java入门课程", 1, "video1.mp4", "张老师", 1, 1, LocalDateTime.now(), null));

        when(courseMapper.getCoursesWithPageByCondition(anyInt(), anyInt(), any(), any(), any())).thenReturn(mockCourses);
        when(courseMapper.getCourseCountByCondition(any(), any(), any())).thenReturn(1);

        // 执行测试 - 传入无效的pageNum
        Map<String, Object> result = courseController.getallcrs(-1, 10, null, null, null);

        // 验证结果 - 应该被纠正为第1页
        assertNotNull(result);
        assertEquals(1, ((List<?>) result.get("data")).size());
    }

    @Test
    void testAddCourseSuccess() {
        // 准备测试数据
        Course course = new Course();
        course.setCourse_name("测试课程");
        course.setDescription("测试描述");

        when(courseMapper.addCourse(any(Course.class))).thenReturn(1);

        // 执行测试
        Map<String, Object> result = courseController.addcourse(course);

        // 验证结果
        assertTrue((Boolean) result.get("success"));
        assertEquals(200, result.get("code"));
        assertNotNull(result.get("data"));
    }

    @Test
    void testAddCourseFailure() {
        // 准备测试数据
        Course course = new Course();
        course.setCourse_name("测试课程");
        course.setDescription("测试描述");

        when(courseMapper.addCourse(any(Course.class))).thenReturn(0);

        // 执行测试
        Map<String, Object> result = courseController.addcourse(course);

        // 验证结果
        assertFalse((Boolean) result.get("success"));
        assertEquals(500, result.get("code"));
    }

    @Test
    void testDelCourseSuccess() {
        when(courseMapper.delCourse(anyInt())).thenReturn(1);

        int result = courseController.delcrs(1);

        assertEquals(1, result);
    }

    @Test
    void testUpdateCourseSuccess() {
        Course course = new Course();
        course.setCourse_id(1);
        course.setCourse_name("更新后的课程名");

        when(courseMapper.updateCourse(any(Course.class))).thenReturn(1);

        int result = courseController.updatecrs(course);

        assertEquals(1, result);
    }

    @Test
    void testUploadImageSuccess() throws Exception {
        // 准备测试文件
        MultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        // 执行测试
        Map<String, Object> result = courseController.uploadImage(mockFile);

        // 验证结果
        assertEquals(200, result.get("code"));
        assertTrue(((String) result.get("message")).contains("上传成功"));
    }

    @Test
    void testUploadImageInvalidType() throws Exception {
        // 准备测试文件 - 错误类型
        MultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "test content".getBytes()
        );

        // 执行测试
        Map<String, Object> result = courseController.uploadImage(mockFile);

        // 验证结果
        assertEquals(400, result.get("code"));
        assertTrue(((String) result.get("message")).contains("只支持JPEG/PNG格式图片"));
    }

    @Test
    void testUploadVideoSuccess() throws Exception {
        // 准备测试文件
        MultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.mp4",
                "video/mp4",
                "test video content".getBytes()
        );

        // 执行测试
        Map<String, Object> result = courseController.uploadVideo(mockFile);

        // 验证结果
        assertEquals(200, result.get("code"));
        assertTrue(((String) result.get("message")).contains("上传成功"));
    }

    @Test
    void testGetCourseDetailsSuccess() {
        // 准备测试数据
        Course mockCourse = new Course(1, "Java基础", "/images/cover1.jpg", "Java入门课程", 1, "/videos/video1.mp4", "张老师", 1, 1, LocalDateTime.now(), null);
        mockCourse.setCreated_by_name("张老师");

        when(courseMapper.getCourseWithUser(anyInt())).thenReturn(mockCourse);

        // 执行测试
        Map<String, Object> result = courseController.getCourseDetails(1);

        // 验证结果
        assertTrue((Boolean) result.get("success"));
        assertEquals(200, result.get("code"));

        Course returnedCourse = (Course) result.get("data");
        assertNotNull(returnedCourse);
        assertTrue(returnedCourse.getCover_url().startsWith("http"));
        assertTrue(returnedCourse.getVideo_url().startsWith("http"));
    }

    @Test
    void testGetCourseDetailsNotFound() {
        when(courseMapper.getCourseWithUser(anyInt())).thenReturn(null);

        // 执行测试
        Map<String, Object> result = courseController.getCourseDetails(999);

        // 验证结果
        assertFalse((Boolean) result.get("success"));
        assertEquals(404, result.get("code"));
    }

    @Test
    void testUpdateCourseStatusSuccess() {
        // 准备测试数据
        Map<String, Object> params = new HashMap<>();
        params.put("course_id", 1);
        params.put("status", 2);

        when(courseMapper.updateCourseStatus(any(Course.class))).thenReturn(1);

        // 执行测试
        Map<String, Object> result = courseController.updateCourseStatus(params);

        // 验证结果
        assertTrue((Boolean) result.get("success"));
        assertEquals(200, result.get("code"));
    }

    @Test
    void testUpdateCourseStatusFailure() {
        // 准备测试数据
        Map<String, Object> params = new HashMap<>();
        params.put("course_id", 1);
        params.put("status", 2);

        when(courseMapper.updateCourseStatus(any(Course.class))).thenReturn(0);

        // 执行测试
        Map<String, Object> result = courseController.updateCourseStatus(params);

        // 验证结果
        assertFalse((Boolean) result.get("success"));
        assertEquals(500, result.get("code"));
    }
}