package com.neucamp.testalliancehubbackend.Service;

import com.neucamp.testalliancehubbackend.entity.MeetingFeedback;
import com.neucamp.testalliancehubbackend.mapper.FeedbackMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FeedbackServiceTest {

    @Mock
    private FeedbackMapper feedbackMapper;

    @InjectMocks
    private FeedbackService feedbackService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateFeedback() {
        MeetingFeedback feedback = new MeetingFeedback();
        feedback.setFeedbackId(10);
        feedback.setUserId(123);
        feedback.setMeetingId(456);
        feedback.setUserName("张三");
        feedback.setGender("男");
        feedback.setCompany("公司X");
        feedback.setPhone("13800000000");
        feedback.setEmail("zhangsan@example.com");
        feedback.setArriveWay("飞机");
        feedback.setArriveTrain("G123");
        feedback.setArriveTime("2025-07-01 09:00");
        feedback.setCreateTime("2025-06-30 18:00");

        // 修复：使用thenReturn模拟有返回值的方法
        when(feedbackMapper.insertFeedback(feedback)).thenReturn(1);

        MeetingFeedback result = feedbackService.createFeedback(feedback);

        verify(feedbackMapper, times(1)).insertFeedback(feedback);
        assertEquals(feedback, result);
    }
}