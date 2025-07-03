package com.neucamp.testalliancehubbackend.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neucamp.testalliancehubbackend.Service.ConferenceService;
import com.neucamp.testalliancehubbackend.entity.ConferenceDTO;
import com.neucamp.testalliancehubbackend.entity.Meeting;
import com.neucamp.testalliancehubbackend.mapper.MeetingMapper;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET},
        maxAge = 3600
)
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


    @RequestMapping("/deleteMeeting/{meeting_id}")
    public int deleteMeeting(@PathVariable Integer meeting_id) {
        if (meeting_id == null) {
            throw new IllegalArgumentException("meeting_id 参数不能为 null");
        }
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


    @Autowired
    private ConferenceService conferenceService;
    @RequestMapping("/getmeetingMobile")
    public ResponseEntity<List<ConferenceDTO>> getAllConferences() {
        List<ConferenceDTO> conferences = conferenceService.getAllConferences();
        System.out.println("第一条数据示例: " + conferences.get(0));
        System.out.println("创建者字段是否存在: " + conferences.get(0).getCreatorName() != null);
        System.out.println("创建时间字段是否存在: " + conferences.get(0).getCreateTime() != null);
        return ResponseEntity.ok(conferences);
    @RequestMapping("/updateStatu")
    public int updateMeetingAuditStatus(@RequestBody Map<String,Object> params){
        int meeting_id = Integer.parseInt(params.get("meeting_id").toString());
        int audit_status = Integer.parseInt(params.get("audit_status").toString());
        return meetingMapper.updateMeetingAuditStatus(meeting_id, audit_status);
    }

}
