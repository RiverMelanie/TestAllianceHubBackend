package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.Service.FeedbackService;
import com.neucamp.testalliancehubbackend.entity.FeedbackCatStatsDTO;
import com.neucamp.testalliancehubbackend.entity.MeetingFeedback;
import com.neucamp.testalliancehubbackend.mapper.FeedbackMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET},
        maxAge = 3600
)
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;
    @Autowired
    FeedbackMapper feedbackMapper;

    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    @RequestMapping("/feedbackMobile")
    public ResponseEntity<?> createFeedback(@RequestBody MeetingFeedback feedback) {
        try {
            logger.info("收到反馈数据: {}", feedback.toString());
            MeetingFeedback created = feedbackService.createFeedback(feedback);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            logger.error("创建反馈失败", e);
            return ResponseEntity.internalServerError().body("创建反馈失败");
        }
    }

    //用于会议用户数据分析
    @RequestMapping("/feedbackStats")
    public List<FeedbackCatStatsDTO> getFeedbackCategoryStats() {
        System.out.println("会议数据分析");
        List<FeedbackCatStatsDTO> status = feedbackMapper.getCategoryFeedbackStats();
        return status;
    }
}
