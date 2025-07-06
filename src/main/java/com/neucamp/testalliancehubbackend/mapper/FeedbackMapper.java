package com.neucamp.testalliancehubbackend.mapper;

import com.neucamp.testalliancehubbackend.entity.FeedbackCatStatsDTO;
import com.neucamp.testalliancehubbackend.entity.MeetingFeedback;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FeedbackMapper {
    @Insert("INSERT INTO meeting_feedback (user_id, meeting_id, name, gender, company, " +
            "phone, email, arrive_way, arrive_train, arrive_time, create_time) " +
            "VALUES (#{userId}, #{meetingId}, #{userName}, #{gender}, #{company}, " +
            "#{phone}, #{email}, #{arriveWay}, #{arriveTrain}, #{arriveTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "feedbackId")
    int insertFeedback(MeetingFeedback feedback);

    //接受所有反馈的类别
    @Select("""
        SELECT 
            mc.category_name AS catName,
            COUNT(*) AS count,
            ROUND(COUNT(*) * 1.0 / total.total_count, 4) AS ratio
        FROM meeting_feedback mf
        JOIN meeting_detail md ON mf.meeting_id = md.meeting_id
        JOIN meeting_category mc ON md.category_id = mc.category_id
        CROSS JOIN (
            SELECT COUNT(*) AS total_count
            FROM meeting_feedback
        ) total
        GROUP BY mc.category_name, total.total_count
    """)
    List<FeedbackCatStatsDTO> getCategoryFeedbackStats();
}
