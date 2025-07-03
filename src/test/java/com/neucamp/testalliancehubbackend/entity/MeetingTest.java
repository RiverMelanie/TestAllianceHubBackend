package com.neucamp.testalliancehubbackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MeetingTest {

    private Meeting meeting;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        meeting = new Meeting();
    }

    // =============== 构造函数测试 ===============
    @Test
    void noArgsConstructor_ShouldCreateEmptyMeeting() {
        assertNotNull(meeting);
        assertNull(meeting.getMeeting_id());
        assertNull(meeting.getCover_url());
        assertNull(meeting.getMeeting_name());
        assertNull(meeting.getStart_time());
        assertNull(meeting.getEnd_time());
        assertNull(meeting.getContent());
        assertNull(meeting.getCreate_time());
        assertNull(meeting.getCreator_name());
        assertNull(meeting.getAudit_status());
    }

    @Test
    void allArgsConstructor_ShouldCreateMeetingWithAllFields() {
        Meeting fullMeeting = new Meeting(
                1,
                "http://example.com/cover.jpg",
                "Spring Boot Workshop",
                now.plusDays(1),
                now.plusDays(2),
                "Learn Spring Boot in 2 days",
                now,
                "john_doe",
                1
        );

        assertNotNull(fullMeeting);
        assertEquals(1, fullMeeting.getMeeting_id());
        assertEquals("http://example.com/cover.jpg", fullMeeting.getCover_url());
        assertEquals("Spring Boot Workshop", fullMeeting.getMeeting_name());
        assertEquals(now.plusDays(1), fullMeeting.getStart_time());
        assertEquals(now.plusDays(2), fullMeeting.getEnd_time());
        assertEquals("Learn Spring Boot in 2 days", fullMeeting.getContent());
        assertEquals(now, fullMeeting.getCreate_time());
        assertEquals("john_doe", fullMeeting.getCreator_name());
        assertEquals(1, fullMeeting.getAudit_status());
    }

    // =============== getter/setter 测试 ===============
    @Test
    void meetingId_GetterAndSetter_ShouldWorkCorrectly() {
        meeting.setMeeting_id(100);
        assertEquals(100, meeting.getMeeting_id());

        meeting.setMeeting_id(null);
        assertNull(meeting.getMeeting_id());
    }

    @Test
    void coverUrl_GetterAndSetter_ShouldWorkCorrectly() {
        meeting.setCover_url("http://test.com/cover.png");
        assertEquals("http://test.com/cover.png", meeting.getCover_url());

        meeting.setCover_url(null);
        assertNull(meeting.getCover_url());
    }

    @Test
    void meetingName_GetterAndSetter_ShouldWorkCorrectly() {
        meeting.setMeeting_name("Test Meeting");
        assertEquals("Test Meeting", meeting.getMeeting_name());

        meeting.setMeeting_name(null);
        assertNull(meeting.getMeeting_name());
    }

    @Test
    void startTime_GetterAndSetter_ShouldWorkCorrectly() {
        LocalDateTime startTime = now.plusHours(1);
        meeting.setStart_time(startTime);
        assertEquals(startTime, meeting.getStart_time());

        meeting.setStart_time(null);
        assertNull(meeting.getStart_time());
    }

    @Test
    void endTime_GetterAndSetter_ShouldWorkCorrectly() {
        LocalDateTime endTime = now.plusHours(2);
        meeting.setEnd_time(endTime);
        assertEquals(endTime, meeting.getEnd_time());

        meeting.setEnd_time(null);
        assertNull(meeting.getEnd_time());
    }

    @Test
    void content_GetterAndSetter_ShouldWorkCorrectly() {
        meeting.setContent("Meeting content");
        assertEquals("Meeting content", meeting.getContent());

        meeting.setContent(null);
        assertNull(meeting.getContent());
    }

    @Test
    void createTime_GetterAndSetter_ShouldWorkCorrectly() {
        LocalDateTime createTime = now.minusDays(1);
        meeting.setCreate_time(createTime);
        assertEquals(createTime, meeting.getCreate_time());

        meeting.setCreate_time(null);
        assertNull(meeting.getCreate_time());
    }

    @Test
    void creatorName_GetterAndSetter_ShouldWorkCorrectly() {
        meeting.setCreator_name("test_user");
        assertEquals("test_user", meeting.getCreator_name());

        meeting.setCreator_name(null);
        assertNull(meeting.getCreator_name());
    }

    @Test
    void auditStatus_GetterAndSetter_ShouldWorkCorrectly() {
        meeting.setAudit_status(2);
        assertEquals(2, meeting.getAudit_status());

        meeting.setAudit_status(null);
        assertNull(meeting.getAudit_status());
    }

    // =============== JsonFormat 注解测试 ===============
    @Test
    void dateTimeFields_ShouldHaveJsonFormatAnnotation() throws NoSuchFieldException {
        // 检查 start_time 字段是否有 JsonFormat 注解
        JsonFormat startTimeFormat = Meeting.class
                .getDeclaredField("start_time")
                .getAnnotation(JsonFormat.class);
        assertNotNull(startTimeFormat);
        assertEquals("yyyy-MM-dd HH:mm:ss", startTimeFormat.pattern());

        // 检查 end_time 字段是否有 JsonFormat 注解
        JsonFormat endTimeFormat = Meeting.class
                .getDeclaredField("end_time")
                .getAnnotation(JsonFormat.class);
        assertNotNull(endTimeFormat);
        assertEquals("yyyy-MM-dd HH:mm:ss", endTimeFormat.pattern());

        // 检查 create_time 字段是否有 JsonFormat 注解
        JsonFormat createTimeFormat = Meeting.class
                .getDeclaredField("create_time")
                .getAnnotation(JsonFormat.class);
        assertNotNull(createTimeFormat);
        assertEquals("yyyy-MM-dd HH:mm:ss", createTimeFormat.pattern());
    }

    // =============== 边界条件测试 ===============
    @Test
    void meetingName_ShouldHandleLongStrings() {
        String longName = "A".repeat(255); // 最大长度
        meeting.setMeeting_name(longName);
        assertEquals(longName, meeting.getMeeting_name());

        // 超过255字符会由数据库约束处理，这里不做测试
    }

    @Test
    void creatorName_ShouldHandleLongStrings() {
        String longName = "B".repeat(255); // 最大长度
        meeting.setCreator_name(longName);
        assertEquals(longName, meeting.getCreator_name());
    }

    @Test
    void timeFields_ShouldHandleEdgeCases() {
        // 最小日期时间
        LocalDateTime minDateTime = LocalDateTime.MIN;
        meeting.setStart_time(minDateTime);
        meeting.setEnd_time(minDateTime);
        meeting.setCreate_time(minDateTime);

        assertEquals(minDateTime, meeting.getStart_time());
        assertEquals(minDateTime, meeting.getEnd_time());
        assertEquals(minDateTime, meeting.getCreate_time());

        // 最大日期时间
        LocalDateTime maxDateTime = LocalDateTime.MAX;
        meeting.setStart_time(maxDateTime);
        meeting.setEnd_time(maxDateTime);
        meeting.setCreate_time(maxDateTime);

        assertEquals(maxDateTime, meeting.getStart_time());
        assertEquals(maxDateTime, meeting.getEnd_time());
        assertEquals(maxDateTime, meeting.getCreate_time());
    }




}