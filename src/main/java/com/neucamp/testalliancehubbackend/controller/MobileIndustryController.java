package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.entity.IndustryDynamic;
import com.neucamp.testalliancehubbackend.entity.Visit;
import com.neucamp.testalliancehubbackend.mapper.MobileIndustryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @RequestMapping("/dynamics")
    public ResponseEntity<List<IndustryDynamic>> getAllDynamicsByKeyword(@RequestParam(required = false) String keyword) {
        String searchKeyword = Optional.ofNullable(keyword).orElse("");
        List<IndustryDynamic> dynamics = mobileIndustryMapper.getAllDynamicsByKeyword(searchKeyword);
        return ResponseEntity.ok(dynamics);
    }
    
    @PostMapping("/recordVisit")
    public ResponseEntity<String> recordVisit(@RequestBody Visit visit) {
        // 增强验证
        if (visit.getDynamic_id() == null) {
            return ResponseEntity.badRequest().body("dynamic_id不能为空");
        }
        if (visit.getUser_id() == null) {
            return ResponseEntity.badRequest().body("user_id不能为空");
        }

        visit.setVisit_time(LocalDateTime.now());
        mobileIndustryMapper.insertVisit(visit);
        return ResponseEntity.ok("记录成功");
    }
}
