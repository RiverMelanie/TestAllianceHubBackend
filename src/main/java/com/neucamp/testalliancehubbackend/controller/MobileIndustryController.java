package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.mapper.MobileIndustryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class MobileIndustryController {

    @Autowired
    private MobileIndustryMapper mobileIndustryMapper;

    @GetMapping("/mobiletopClicked")
    public ResponseEntity<List<Map<String, Object>>> getTopClickedDynamics(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // 验证日期范围有效性
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("结束日期不能早于开始日期");
        }
        // 将日期转换为当天的开始和结束时刻
        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.atTime(23, 59, 59);

        // 转换时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> result = mobileIndustryMapper.selectTopDynamicsByTimeRange(
                startTime.format(formatter),
                endTime.format(formatter)
        );

        System.out.println("DEBUG: 返回 " + result.size() + " 条热门动态数据");
        return ResponseEntity.ok(result);
    }


}