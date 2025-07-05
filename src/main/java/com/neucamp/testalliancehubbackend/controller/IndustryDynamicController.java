package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.entity.IndustryDynamic;
import com.neucamp.testalliancehubbackend.entity.dynamicreviewrecordtable;
import com.neucamp.testalliancehubbackend.entity.User;
import com.neucamp.testalliancehubbackend.mapper.IndustryDynamicMapper;
import com.neucamp.testalliancehubbackend.mapper.ManagerIndustryDynamicMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.stream.Collectors;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class IndustryDynamicController {

    @Autowired
    IndustryDynamicMapper industryDynamicMapper;
    //添加
    @Autowired
    ManagerIndustryDynamicMapper managerIndustryDynamicMapper;
    @RequestMapping("/addDynamic")
    public int addDynamic(@Valid @RequestBody IndustryDynamic industryDynamic){
        return industryDynamicMapper.addDynamic(industryDynamic);
    }
    @RequestMapping("/search")
    public Map<String, Object> searchDynamics(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String author,
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "分页参数非法") int pageNum,
            @RequestParam(defaultValue = "8") int pageSize
    ) {

        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;

        // 构建查询参数
        Map<String, Object> params = new HashMap<>();
        params.put("title", "%" + title + "%");    // 模糊查询：%关键词%
        params.put("author", "%" + author + "%");
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        List<IndustryDynamic> dynamics = industryDynamicMapper.searchDynamics(params);

        List<IndustryDynamic> filteredDynamics = dynamics.stream()
                .filter(dynamic -> dynamic.getAuditStatus() != null && dynamic.getAuditStatus() == 1)
                .collect(Collectors.toList());

        int totalCount = industryDynamicMapper.searchTotalCount(params);


        Map<String, Object> result = new HashMap<>();
        result.put("data", filteredDynamics);

        result.put("totalCount", totalCount);
        System.out.println(result);
        return result;
    }


    @RequestMapping("/addreviewrecord")
    public int addReviewRecord(@Valid @RequestBody dynamicreviewrecordtable dynamicreviewrecordtable){
        return industryDynamicMapper.addReviewRecord(
                dynamicreviewrecordtable.getReviewerID(),
                dynamicreviewrecordtable.getTitle(),
                dynamicreviewrecordtable.getNewsImage(),
                dynamicreviewrecordtable.getContent(),
                dynamicreviewrecordtable.getNewsSummary(),
                dynamicreviewrecordtable.getAuthor(),
                dynamicreviewrecordtable.getReviewResult());
    }
    /**
     * 图片上传接口：保存文件到本地，并返回访问 URL
     */
    @PostMapping("/upload")
    public Map<String, String> uploadFile(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request // 注入请求对象，动态生成URL
    ) {
        try {
            // 1. 动态获取项目根路径（解决环境差异问题）
            String projectRoot = System.getProperty("user.dir");
            String uploadDir = projectRoot + File.separator + "uploads" + File.separator;

            // 确保上传目录存在（递归创建）
            File directory = new File(uploadDir);
            if (!directory.exists() && !directory.mkdirs()) {
                throw new RuntimeException("创建上传目录失败");
            }

            // 2. 生成唯一文件名（避免重复 + 清理非法字符）
            String fileName = UUID.randomUUID().toString()
                    + getSafeFileExtension(file.getOriginalFilename());

            // 3. 保存文件到本地（带异常重试建议）
            File dest = new File(uploadDir + fileName);
            file.transferTo(dest);

            // 4. 动态构建可访问URL（适配本地/服务器/域名环境）
            String url = buildAccessibleUrl(request, "uploads/" + fileName);

            // 5. 返回结果
            Map<String, String> result = new HashMap<>();
            result.put("url", url);

            return result;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败：" + e.getMessage());
        }
    }

    @PostMapping("/Managerupload")
    public Map<String, String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("dynamicId") Integer dynamicId,
            @RequestParam("publisherId") Integer publisherId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("summary") String summary,
            @RequestParam("author") String author,
            @RequestParam("imageUrl") String imageUrl,
            @RequestParam("auditStatus") Integer auditStatus,
            HttpServletRequest request // 注入请求对象，动态生成URL
    ) {
        try {
            // 1. 动态获取项目根路径（解决环境差异问题）
            String projectRoot = System.getProperty("user.dir");
            String uploadDir = projectRoot + File.separator + "uploads" + File.separator;

            // 确保上传目录存在（递归创建）
            File directory = new File(uploadDir);
            if (!directory.exists() && !directory.mkdirs()) {
                throw new RuntimeException("创建上传目录失败");
            }

            // 2. 生成唯一文件名（避免重复 + 清理非法字符）
            String fileName = UUID.randomUUID().toString()
                    + getSafeFileExtension(file.getOriginalFilename());

            // 3. 保存文件到本地（带异常重试建议）
            File dest = new File(uploadDir + fileName);
            file.transferTo(dest);

            // 4. 动态构建可访问URL（适配本地/服务器/域名环境）
            String url = buildAccessibleUrl(request, "uploads/" + fileName);

            // 5. 返回结果
            Map<String, String> result = new HashMap<>();
            result.put("url", url);
            IndustryDynamic industryDynamic=new IndustryDynamic(dynamicId,publisherId,title,content,summary,author,imageUrl,null,auditStatus);
            managerIndustryDynamicMapper.upDynamic(industryDynamic);
            return result;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败：" + e.getMessage());
        }
    }

    // 辅助方法1：提取安全的文件扩展名（小写+过滤非法字符）
    private String getSafeFileExtension(String originalFilename) {
        if (originalFilename == null) return "";
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex <= 0 || dotIndex >= originalFilename.length() - 1) {
            return ""; // 无扩展名或格式异常时返回空
        }
        // 转小写并过滤特殊字符（避免跨平台问题）
        return originalFilename.substring(dotIndex).toLowerCase()
                .replaceAll("[^a-z0-9.]", "");
    }

    // 辅助方法2：动态构建访问URL（自动适配端口、上下文路径）
    private String buildAccessibleUrl(HttpServletRequest request, String filePath) {
        String scheme = request.getScheme(); // http/https
        String serverName = request.getServerName(); // 域名/IP
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath(); // 应用上下文（如/springboot）

        // 拼接基础路径（自动处理默认端口80/443）
        StringBuilder url = new StringBuilder(scheme + "://" + serverName);
        if (!((scheme.equals("http") && serverPort == 80)
                || (scheme.equals("https") && serverPort == 443))) {
            url.append(":").append(serverPort);
        }
        // 拼接上下文路径和文件路径
        url.append(contextPath).append("/").append(filePath);
        return url.toString();
    }
}
