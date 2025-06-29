package com.neucamp.testalliancehubbackend.entity;


import com.fasterxml.jackson.annotation.JsonFormat;


import java.time.*;


public class Meeting {
    private Integer meeting_id;
    private String cover_url;
    private String meeting_name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate meeting_date;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start_time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end_time;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime create_time;
    private String creator_name;
    private Integer audit_status;

    public Meeting() {
    }

    public Meeting(Integer meeting_id, String cover_url, String meeting_name, LocalDate meeting_date, LocalDateTime start_time, LocalDateTime end_time, String content, LocalDateTime create_time, String creator_name, Integer audit_status) {
        this.meeting_id = meeting_id;
        this.cover_url = cover_url;
        this.meeting_name = meeting_name;
        this.meeting_date = meeting_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.content = content;
        this.create_time = create_time;
        this.creator_name = creator_name;
        this.audit_status = audit_status;
    }

    public Integer getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(Integer meeting_id) {
        this.meeting_id = meeting_id;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getMeeting_name() {
        return meeting_name;
    }

    public void setMeeting_name(String meeting_name) {
        this.meeting_name = meeting_name;
    }

    public LocalDate getMeeting_date() {
        return meeting_date;
    }

    public void setMeeting_date(LocalDate meeting_date) {
        this.meeting_date = meeting_date;
    }

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreate_time() {
        return create_time;
    }

    public void setCreate_time(LocalDateTime create_time) {
        this.create_time = create_time;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public Integer getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(Integer audit_status) {
        this.audit_status = audit_status;
    }
}
