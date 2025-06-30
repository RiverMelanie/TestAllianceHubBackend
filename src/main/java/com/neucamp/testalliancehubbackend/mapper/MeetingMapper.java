package com.neucamp.testalliancehubbackend.mapper;

import com.neucamp.testalliancehubbackend.entity.Meeting;
import org.apache.ibatis.annotations.*;


import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MeetingMapper {


    @Select("select * from meeting")
    List<Meeting> getAllMeetings();

    @Select("select * from meeting where creator_name like concat('%', #{creator_name},'%')  or  meeting_name like concat('%', #{meeting_name} ,'%') or start_time>=#{start_time}")
    List<Meeting> getMeetingsBy(String creator_name, String meeting_name, LocalDateTime start_time);

    @Select("select * from meeting where meeting_id = #{meeting_id}")
    Meeting getMeetingById(Integer meeting_id);

    @Delete("delete from meeting where meeting_id=#{meeting_id}")
    int deleteMeeting(Integer meeting_id);

    @Insert("insert into meeting values (null,#{cover_url},#{meeting_name},#{start_time},#{end_time},#{content},#{create_time},#{creator_name},#{audit_status})")
    int addMeeting(Meeting meeting);

    @Update("update meeting set cover_url=#{cover_url},meeting_name=#{meeting_name},start_time=#{start_time},end_time=#{end_time},content=#{content},create_time=#{create_time},creator_name=#{creator_name},audit_status=#{audit_status} where meeting_id=#{meeting_id}")
    int updateMeeting(Meeting meeting);

    @Update("update meeting set audit_status=#{audit_status} where meeting_id=#{meeting_id}")
    int updateMeetingAuditStatus(int meeting_id, int audit_status);
}
