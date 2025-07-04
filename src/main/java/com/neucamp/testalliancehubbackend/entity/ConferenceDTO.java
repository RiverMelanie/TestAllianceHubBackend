package com.neucamp.testalliancehubbackend.entity;

import java.time.LocalDateTime;

public class ConferenceDTO {
    private Integer id;
    private String name;
    private String creatorName;
    private String sponsor;
    private String agenda;
    private String guests;
    private String location;
    private String content;
    private String category;
    private String startTime;
    private String endTime;
    private String coverUrl;
    private String createTime;

    public ConferenceDTO() {
    }

    public ConferenceDTO(Integer id, String name, String creatorName, String sponsor, String agenda,
                         String guests, String location, String content, String category,
                         String startTime, String endTime, String coverUrl, String createTime) {
        this.id = id;
        this.name = name;
        this.creatorName = creatorName;
        this.sponsor = sponsor;
        this.agenda = agenda;
        this.guests = guests;
        this.location = location;
        this.content = content;
        this.category = category;
        this.startTime = startTime;
        this.endTime = endTime;
        this.coverUrl = coverUrl;
        this.createTime = createTime;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getGuests() {
        return guests;
    }

    public void setGuests(String guests) {
        this.guests = guests;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String  startTime) {
        this.startTime = startTime;
    }

    public String  getEndTime() {
        return endTime;
    }

    public void setEndTime(String  endTime) {
        this.endTime = endTime;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
