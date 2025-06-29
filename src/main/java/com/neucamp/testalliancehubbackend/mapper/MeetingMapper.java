package com.neucamp.testalliancehubbackend.mapper;

import com.neucamp.testalliancehubbackend.entity.ConferenceDTO;
import com.neucamp.testalliancehubbackend.entity.Meeting;
import org.apache.ibatis.annotations.*;


import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MeetingMapper {


    @Select("select * from meeting")
    List<Meeting> getAllMeetings();

    @Select("select * from meeting where creator_name like '%' || #{creator_name} || '%' and  meeting_name like '%' || #{meeting_name} || '%' and start_time>=#{start_time}")
    List<Meeting> getMeetingsBy(String creator_name, String meeting_name, LocalDateTime start_time);

    @Select("select * from meeting where meeting_id = #{meeting_id}")
    Meeting getMeetingById(int meeting_id);

    @Delete("delete from meeting where meeting_id=#{meeting_id}")
    int deleteMeeting(int meeting_id);

    @Insert("insert into meeting values (null,#{cover_url},#{meeting_name},#{start_time},#{end_time},#{content},#{create_time},#{creator_name},#{audit_status})")
    int addMeeting(Meeting meeting);

    @Update("update meeting set cover_url=#{cover_url},meeting_name=#{meeting_name},start_time=#{start_time},end_time=#{end_time},content=#{content},create_time=#{create_time},creator_name=#{creator_name},audit_status=#{audit_status} where meeting_id=#{meeting_id}")
    int updateMeeting(Meeting meeting);

    @Select("SELECT m.meeting_id, m.meeting_name, m.creator_name, md.sponsor, md.agenda, md.guests, md.location, " +
            "m.content, mc.category_name, m.start_time, m.end_time, m.cover_url, m.create_time " +
            "FROM meeting m " +
            "LEFT JOIN meeting_detail md ON m.meeting_id = md.meeting_id " +
            "LEFT JOIN meeting_category mc ON md.category_id = mc.category_id")
    List<ConferenceDTO> getAllConferences();
}
