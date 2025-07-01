package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.entity.Course;
import com.neucamp.testalliancehubbackend.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
public class CourseController {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.image-dir}")
    private String imageDir;

    @Value("${file.video-dir}")
    private String videoDir;

    @Autowired
    CourseMapper courseMapper;

    //分页查询
    @RequestMapping("/getallcrs")
    public Map<String, Object> getallcrs(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String courseName,
            @RequestParam(required = false) Integer sortOrder,
            @RequestParam(required = false) Integer status) {  // 添加status参数

        // 验证分页参数
        if(pageNum < 1) pageNum = 1;
        if(pageSize < 1 || pageSize > 100) pageSize = 10;

        int offset = (pageNum - 1) * pageSize;

        Map<String, Object> result = new HashMap<>();

        // 处理courseName为null或空字符串的情况
        if(courseName != null && courseName.trim().isEmpty()) {
            courseName = null;
        }

        // 使用带条件的查询
        result.put("data", courseMapper.getCoursesWithPageByCondition(
                offset, pageSize, courseName, sortOrder, status));  // 添加status参数
        result.put("total", courseMapper.getCourseCountByCondition(
                courseName, sortOrder, status));  // 添加status参数

        return result;
    }

    // 添加课程
    @RequestMapping("/addcrs")
    public Map<String, Object> addcourse(@RequestBody Course course) {
        Map<String, Object> result = new HashMap<>();

        try {
            course.setCreated_at(LocalDateTime.now());
            int rows = courseMapper.addCourse(course);

            if (rows > 0) {
                result.put("success", true);
                result.put("code", 200);
                result.put("message", "添加成功");
                result.put("data", course); // 返回包含ID的完整对象
            } else {
                result.put("success", false);
                result.put("code", 500);
                result.put("message", "数据库操作失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }

        System.out.println("返回响应: " + result); // 添加日志
        return result;
    }

    //删除
    @RequestMapping("/delcrs")
    public int delcrs(int course_id) {
        return courseMapper.delCourse(course_id);
    }

    // 修改
    @RequestMapping("/upcrs")
    public int updatecrs(@RequestBody Course course){
        // 设置当前时间为更新时间
        course.setUpdated_at(LocalDateTime.now());
        return courseMapper.updateCourse(course);
    }

    // 新增上传接口
    @PostMapping("/upload/image")
    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 验证文件类型
            String contentType = file.getContentType();
            if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
                result.put("code", 400);
                result.put("message", "只支持JPEG/PNG格式图片");
                return result;
            }

            // 2. 验证文件大小
            if (file.getSize() > 5 * 1024 * 1024) { // 5MB
                result.put("code", 400);
                result.put("message", "图片大小不能超过5MB");
                return result;
            }

            // 创建完整上传目录路径
            String fullImageDir = uploadDir + imageDir;
            File dir = new File(fullImageDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 保存文件
            String newFileName=file.getOriginalFilename();
            File dest = new File(dir, newFileName);
            file.transferTo(dest);

            // 返回访问URL (注意这里要去掉开头的"./")
            String fileUrl = "/uploads/" + imageDir + newFileName;

            result.put("code", 200);
            result.put("message", "上传成功");
            result.put("data", Map.of("url", fileUrl));
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "上传失败: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/upload/video")
    public Map<String, Object> uploadVideo(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 验证文件类型
            String contentType = file.getContentType();
            if (!contentType.startsWith("video/")) {
                result.put("code", 400);
                result.put("message", "只支持视频文件");
                return result;
            }

            // 2. 验证文件大小
            if (file.getSize() > 50 * 1024 * 1024) { // 50MB
                result.put("code", 400);
                result.put("message", "视频大小不能超过50MB");
                return result;
            }

            // 创建完整上传目录路径
            String fullVideoDir = uploadDir + videoDir;
            File dir = new File(fullVideoDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 保存文件
            String newFileName = file.getOriginalFilename();
            File dest = new File(dir, newFileName);
            file.transferTo(dest);

            // 返回访问URL (注意这里要去掉开头的"./")
            String fileUrl = "http://localhost:8080/uploads/videos/" + newFileName;
            System.out.println("生成的视频URL: " + fileUrl);

            result.put("code", 200);
            result.put("message", "上传成功");
            result.put("data", Map.of("url", fileUrl));
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "上传失败: " + e.getMessage());
        }

        return result;
    }

    @GetMapping("/getcrsdetails")
    public Map<String, Object> getCourseDetails(@RequestParam int course_id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Course course = courseMapper.getCourseWithUser(course_id);
            if (course != null) {
                // 将相对路径转换为绝对路径
                if (course.getCover_url() != null && !course.getCover_url().startsWith("http")) {
                    course.setCover_url("http://localhost:8080" + course.getCover_url());
                }
                if (course.getVideo_url() != null && !course.getVideo_url().startsWith("http")) {
                    course.setVideo_url("http://localhost:8080" + course.getVideo_url());
                }

                result.put("success", true);
                result.put("code", 200);
                result.put("data", course);
            } else {
                result.put("success", false);
                result.put("code", 404);
                result.put("message", "课程不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    // 更新课程状态接口
    @RequestMapping("/updateCourseStatus")
    public Map<String, Object> updateCourseStatus(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        try {
            int courseId = (int) params.get("course_id");
            int status = (int) params.get("status");

            // 创建Course对象并设置更新时间和状态
            Course course = new Course();
            course.setCourse_id(courseId);
            course.setStatus(status);
            course.setUpdated_at(LocalDateTime.now());

            // 调用Mapper更新
            int rows = courseMapper.updateCourseStatus(course);

            if (rows > 0) {
                result.put("success", true);
                result.put("code", 200);
                result.put("message", "状态更新成功");
            } else {
                result.put("success", false);
                result.put("code", 500);
                result.put("message", "状态更新失败，课程可能不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }

        return result;
    }
}
