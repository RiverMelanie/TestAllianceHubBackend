package com.neucamp.testalliancehubbackend.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neucamp.testalliancehubbackend.entity.Meeting;
import com.neucamp.testalliancehubbackend.mapper.MeetingMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class MeetingController {
    @Autowired
    MeetingMapper meetingMapper;

    @RequestMapping("/getAllMeetings")
    public PageInfo<Meeting> getAllMeetings(@RequestParam(defaultValue = "1") int currentPage,
                                            @RequestParam(defaultValue = "5") int pageSize) {
        try {
            PageHelper.startPage(currentPage, pageSize);
            List<Meeting> meetings = meetingMapper.getAllMeetings();
            return new PageInfo<>(meetings);
        }finally {
            PageHelper.clearPage();
        }
    }

    @RequestMapping("/getMeetingsBy")
    public PageInfo<Meeting> getMeetingsBy(@RequestParam(required = false) String creator_name, @RequestParam(required = false) String meeting_name, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_time,
                                           @RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "5") int pageSize) {
        try {
            PageHelper.startPage(currentPage, pageSize);
            List<Meeting> meetings = meetingMapper.getMeetingsBy(creator_name,meeting_name,start_time);
            return new PageInfo<>(meetings);
        }finally {
            PageHelper.clearPage();
        }
    }


    @RequestMapping("/deleteMeeting")
    public int deleteMeeting(int meeting_id) {
        Meeting meeting = meetingMapper.getMeetingById(meeting_id);
        if (meeting != null && meeting.getCover_url() != null) {
            String filePath = "./uploads/images/" + meeting.getCover_url();
            new File(filePath).delete();
        }
        return meetingMapper.deleteMeeting(meeting_id);
    }

    @PostMapping(value = "/addMeeting", consumes = MediaType.APPLICATION_JSON_VALUE)
    public int addMeeting(@RequestBody Meeting meeting) {
        return meetingMapper.addMeeting(meeting);
    }

    @PostMapping(value = "/updateMeeting", consumes = MediaType.APPLICATION_JSON_VALUE)
    public int updateMeeting(@RequestBody Meeting meeting) {
        return meetingMapper.updateMeeting(meeting);
    }

    @RequestMapping("/updateStatu")
    public int updateMeetingAuditStatus(@RequestBody Map<String,Object> params){
        int meeting_id = Integer.parseInt(params.get("meeting_id").toString());
        int audit_status = Integer.parseInt(params.get("audit_status").toString());
        return meetingMapper.updateMeetingAuditStatus(meeting_id, audit_status);
    }
}
