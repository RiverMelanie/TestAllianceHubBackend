package com.neucamp.testalliancehubbackend.mapper;

import com.neucamp.testalliancehubbackend.entity.MeetingFeedback;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface FeedbackMapper {
    @Insert("INSERT INTO meeting_feedback (user_id, meeting_id, name, gender, company, " +
            "phone, email, arrive_way, arrive_train, arrive_time, create_time) " +
            "VALUES (#{userId}, #{meetingId}, #{userName}, #{gender}, #{company}, " +
            "#{phone}, #{email}, #{arriveWay}, #{arriveTrain}, #{arriveTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "feedbackId")
    int insertFeedback(MeetingFeedback feedback);
}
