package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.entity.IndustryDynamic;
import com.neucamp.testalliancehubbackend.entity.dynamicreviewrecordtable;
import com.neucamp.testalliancehubbackend.entity.user;
import com.neucamp.testalliancehubbackend.mapper.IndustryDynamicMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    @RequestMapping("/addDynamic")
    public int addstu(@RequestBody IndustryDynamic industryDynamic){
        return industryDynamicMapper.addDynamic(industryDynamic);
    }
    @RequestMapping("/search")
    public Map<String, Object> searchDynamics(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String author,
            @RequestParam(defaultValue = "1") int pageNum,
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
        return result;
    }


    @RequestMapping("/addreviewrecord")
    public void addReviewRecord(@RequestBody dynamicreviewrecordtable dynamicreviewrecordtable){
                industryDynamicMapper.addReviewRecord(
                dynamicreviewrecordtable.getReviewerID(),
                dynamicreviewrecordtable.getTitle(),
                dynamicreviewrecordtable.getNewsImage(),
                dynamicreviewrecordtable.getContent(),
                dynamicreviewrecordtable.getNewsSummary(),
                dynamicreviewrecordtable.getAuthor(),
                dynamicreviewrecordtable.getReviewResult());
    }
}
