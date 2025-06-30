package com.neucamp.testalliancehubbackend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MeetingFeedback {
    private Integer feedbackId;

    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("meeting_id")
    private Integer meetingId;

    @JsonProperty("user_name")
    private String userName;

    private String gender;
    private String company;
    private String phone;
    private String email;

    @JsonProperty("arrive_way")
    private String arriveWay;

    @JsonProperty("arrive_train")
    private String arriveTrain;

    @JsonProperty("arrive_time")
    private String arriveTime;

    @JsonProperty("create_time")
    private String createTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Integer meetingId) {
        this.meetingId = meetingId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getArriveWay() {
        return arriveWay;
    }

    public void setArriveWay(String arriveWay) {
        this.arriveWay = arriveWay;
    }

    public String getArriveTrain() {
        return arriveTrain;
    }

    public void setArriveTrain(String arriveTrain) {
        this.arriveTrain = arriveTrain;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    @Override
    public String toString() {
        return "MeetingFeedback{" +
                "userId=" + userId +
                ", meetingId=" + meetingId +
                ", userName='" + userName + '\'' +
                ", gender='" + gender + '\'' +
                ", company='" + company + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", arriveWay='" + arriveWay + '\'' +
                ", arriveTrain='" + arriveTrain + '\'' +
                ", arriveTime='" + arriveTime + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }

}
