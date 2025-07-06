package com.neucamp.testalliancehubbackend.Service;

import com.neucamp.testalliancehubbackend.entity.MeetingFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    @Autowired
    FeedbackMapper feedbackMapper;
    public MeetingFeedback createFeedback(MeetingFeedback feedback){
        feedbackMapper.insertFeedback(feedback);
        System.out.println("收到反馈信息");
        return feedback;
    }
}
