package com.neucamp.testalliancehubbackend.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neucamp.testalliancehubbackend.entity.Meeting;
import com.neucamp.testalliancehubbackend.mapper.MeetingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
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
    public PageInfo<Meeting> getMeetingsBy(@RequestParam String creator_name, @RequestParam String meeting_name, @RequestParam LocalDate meeting_date,
                                           @RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "5") int pageSize) {
        try {
            PageHelper.startPage(currentPage, pageSize);
            List<Meeting> meetings = meetingMapper.getMeetingsBy(creator_name,meeting_name,meeting_date);
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


}
