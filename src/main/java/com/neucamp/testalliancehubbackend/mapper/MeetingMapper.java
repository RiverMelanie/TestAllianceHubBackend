package com.neucamp.testalliancehubbackend.mapper;

import com.neucamp.testalliancehubbackend.entity.Meeting;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MeetingMapper {


    @Select("select * from meeting")
    List<Meeting> getAllMeetings();

    @Select("select * from meeting where creator_name like '%' || #{creator_name} || '%' and  meeting_name like '%' || #{meeting_name} || '%' and meeting_date>=#{meeting_date}")
    List<Meeting> getMeetingsBy(String creator_name, String meeting_name, LocalDate meeting_date);

    @Select("select * from meeting where meeting_id = #{meeting_id}")
    Meeting getMeetingById(int meeting_id);

    @Delete("delete from meeting where meeting_id=#{meeting_id}")
    int deleteMeeting(int meeting_id);

    @Insert("insert into meeting values (null,#{cover_url},#{meeting_name},#{meeting_date},#{start_time},#{end_time},#{content},#{create_time},#{creator_name},#{audit_status})")
    int addMeeting(Meeting meeting);

    @Update("update meeting set cover_url=#{cover_url},meeting_name=#{meeting_name},meeting_date=#{meeting_date},start_time=#{start_time},end_time=#{end_time},content=#{content},create_time=#{create_time},creator_name=#{creator_name},audit_status=#{audit_status}")
    int updateMeeting(Meeting meeting);
}
