package com.neucamp.testalliancehubbackend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MeetingFeedbackTest {

    @Test
    void testAllFieldsAndToString() {
        MeetingFeedback fb = new MeetingFeedback();
        fb.setFeedbackId(1);
        fb.setUserId(2);
        fb.setMeetingId(3);
        fb.setUserName("李四");
        fb.setGender("男");
        fb.setCompany("公司B");
        fb.setPhone("123456789");
        fb.setEmail("test@example.com");
        fb.setArriveWay("飞机");
        fb.setArriveTrain("G123");
        fb.setArriveTime("2025-07-01 12:00");
        fb.setCreateTime("2025-07-01 11:00");

        assertEquals(1, fb.getFeedbackId());
        assertEquals(2, fb.getUserId());
        assertEquals(3, fb.getMeetingId());
        assertEquals("李四", fb.getUserName());
        assertEquals("男", fb.getGender());
        assertEquals("公司B", fb.getCompany());
        assertEquals("123456789", fb.getPhone());
        assertEquals("test@example.com", fb.getEmail());
        assertEquals("飞机", fb.getArriveWay());
        assertEquals("G123", fb.getArriveTrain());
        assertEquals("2025-07-01 12:00", fb.getArriveTime());
        assertEquals("2025-07-01 11:00", fb.getCreateTime());

        String toStr = fb.toString();
        assertTrue(toStr.contains("userName='李四'"));
    }
}
