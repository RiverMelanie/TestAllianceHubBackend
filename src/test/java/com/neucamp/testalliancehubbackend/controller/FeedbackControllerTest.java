package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.Service.FeedbackService;
import com.neucamp.testalliancehubbackend.entity.FeedbackCatStatsDTO;
import com.neucamp.testalliancehubbackend.entity.MeetingFeedback;
import com.neucamp.testalliancehubbackend.mapper.FeedbackMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService;

    @Mock
    private FeedbackMapper feedbackMapper;

    @InjectMocks
    private FeedbackController feedbackController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFeedback_Success() {
        MeetingFeedback feedback = new MeetingFeedback();
        feedback.setUserId(1);
        feedback.setMeetingId(10);
        feedback.setUserName("张三");

        when(feedbackService.createFeedback(any(MeetingFeedback.class))).thenReturn(feedback);

        ResponseEntity<?> response = feedbackController.createFeedback(feedback);

        verify(feedbackService, times(1)).createFeedback(feedback);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(feedback, response.getBody());
    }

    @Test
    void testCreateFeedback_Failure() {
        MeetingFeedback feedback = new MeetingFeedback();
        when(feedbackService.createFeedback(any(MeetingFeedback.class)))
                .thenThrow(new RuntimeException("测试异常"));

        ResponseEntity<?> response = feedbackController.createFeedback(feedback);

        verify(feedbackService, times(1)).createFeedback(any(MeetingFeedback.class));
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("创建反馈失败", response.getBody());
    }

    @Test
    void testGetFeedbackCategoryStats() {
        FeedbackCatStatsDTO dto1 = new FeedbackCatStatsDTO();
        dto1.setCatName("科技");
        dto1.setCount(30L);
        dto1.setRatio(0.3);

        FeedbackCatStatsDTO dto2 = new FeedbackCatStatsDTO();
        dto2.setCatName("教育");
        dto2.setCount(70L);
        dto2.setRatio(0.7);

        List<FeedbackCatStatsDTO> stats = Arrays.asList(dto1, dto2);

        when(feedbackMapper.getCategoryFeedbackStats()).thenReturn(stats);

        List<FeedbackCatStatsDTO> result = feedbackController.getFeedbackCategoryStats();

        verify(feedbackMapper, times(1)).getCategoryFeedbackStats();
        assertEquals(2, result.size());
        assertEquals("教育", result.get(1).getCatName());
    }
}
