package com.neucamp.testalliancehubbackend.controller;

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

@RestController
@CrossOrigin
public class MobileIndustryController {

    @Autowired
    private MobileIndustryMapper mobileIndustryMapper;

    @GetMapping("/api/dynamics/mobiletopClicked")
    public ResponseEntity<List<Map<String, Object>>> getTopClickedDynamics(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateUtc,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateUtc) {

        // 将 UTC 时间转换为数据库时区（如东八区）
        ZoneId dbTimeZone = ZoneId.of("Asia/Shanghai"); // 数据库时区
        LocalDateTime startDateDb = startDateUtc.atZone(ZoneId.of("UTC")).withZoneSameInstant(dbTimeZone).toLocalDateTime();
        LocalDateTime endDateDb = endDateUtc.atZone(ZoneId.of("UTC")).withZoneSameInstant(dbTimeZone).toLocalDateTime();

        List<Map<String, Object>> result = mobileIndustryMapper.selectTopDynamicsByTimeRange(
                startDateDb.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                endDateDb.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

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
