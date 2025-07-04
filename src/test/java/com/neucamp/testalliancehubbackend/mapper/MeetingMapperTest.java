package com.neucamp.testalliancehubbackend.mapper;

import com.neucamp.testalliancehubbackend.entity.Meeting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingMapperTest {

    @Mock
    private MeetingMapper meetingMapper;

    private Meeting validMeeting;
    private Meeting minimalMeeting;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        // 完整字段的会议对象
        validMeeting = new Meeting(
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

        // 最小字段的会议对象 (只包含非空字段)
        minimalMeeting = new Meeting(
                null,
                "http://example.com/minimal.jpg",
                "Minimal Meeting",
                now.plusHours(1),
                now.plusHours(2),
                null,
                null,
                "jane_doe",
                null
        );
    }

    // =============== getAllMeetings 测试 ===============
    @Test
    void getAllMeetings_ShouldReturnAllMeetings() {
        // 正常情况 - 返回多个会议
        List<Meeting> expectedMeetings = Arrays.asList(validMeeting, minimalMeeting);
        when(meetingMapper.getAllMeetings()).thenReturn(expectedMeetings);

        List<Meeting> actualMeetings = meetingMapper.getAllMeetings();

        assertEquals(2, actualMeetings.size());
        assertEquals(expectedMeetings, actualMeetings);
        verify(meetingMapper, times(1)).getAllMeetings();
    }

    @Test
    void getAllMeetings_ShouldReturnEmptyListWhenNoMeetings() {
        // 边界情况 - 无会议时返回空列表
        when(meetingMapper.getAllMeetings()).thenReturn(Collections.emptyList());

        List<Meeting> emptyMeetings = meetingMapper.getAllMeetings();

        assertTrue(emptyMeetings.isEmpty());
        verify(meetingMapper, times(1)).getAllMeetings();
    }

    // =============== getMeetingsBy 测试 ===============
    @Test
    void getMeetingsBy_ShouldReturnFilteredMeetings() {
        // 正常情况 - 根据创建者名称查询
        when(meetingMapper.getMeetingsBy("john", null, null))
                .thenReturn(Collections.singletonList(validMeeting));

        List<Meeting> meetings = meetingMapper.getMeetingsBy("john", null, null);

        assertEquals(1, meetings.size());
        assertEquals(validMeeting, meetings.get(0));
        verify(meetingMapper, times(1)).getMeetingsBy("john", null, null);
    }

    @Test
    void getMeetingsBy_ShouldReturnMeetingsByMeetingName() {
        // 正常情况 - 根据会议名称查询
        when(meetingMapper.getMeetingsBy(null, "Workshop", null))
                .thenReturn(Collections.singletonList(validMeeting));

        List<Meeting> meetings = meetingMapper.getMeetingsBy(null, "Workshop", null);

        assertEquals(1, meetings.size());
        verify(meetingMapper, times(1)).getMeetingsBy(null, "Workshop", null);
    }

    @Test
    void getMeetingsBy_ShouldReturnMeetingsAfterStartTime() {
        // 正常情况 - 根据开始时间查询
        LocalDateTime startTime = now.minusDays(1);
        when(meetingMapper.getMeetingsBy(null, null, startTime))
                .thenReturn(Arrays.asList(validMeeting, minimalMeeting));

        List<Meeting> meetings = meetingMapper.getMeetingsBy(null, null, startTime);

        assertEquals(2, meetings.size());
        verify(meetingMapper, times(1)).getMeetingsBy(null, null, startTime);
    }

    @Test
    void getMeetingsBy_ShouldReturnEmptyListWhenNoMatches() {
        // 边界情况 - 无匹配结果
        when(meetingMapper.getMeetingsBy("nonexistent", "nonexistent", now.plusYears(10)))
                .thenReturn(Collections.emptyList());

        List<Meeting> noMeetings = meetingMapper.getMeetingsBy("nonexistent", "nonexistent", now.plusYears(10));

        assertTrue(noMeetings.isEmpty());
        verify(meetingMapper, times(1)).getMeetingsBy("nonexistent", "nonexistent", now.plusYears(10));
    }

    // =============== getMeetingById 测试 ===============
    @Test
    void getMeetingById_ShouldReturnMeetingWhenExists() {
        // 正常情况 - 存在的ID
        when(meetingMapper.getMeetingById(1)).thenReturn(validMeeting);

        Meeting meeting = meetingMapper.getMeetingById(1);

        assertEquals(validMeeting, meeting);
        verify(meetingMapper, times(1)).getMeetingById(1);
    }

    @Test
    void getMeetingById_ShouldReturnNullWhenNotExists() {
        // 边界情况 - 不存在的ID
        when(meetingMapper.getMeetingById(999)).thenReturn(null);

        Meeting meeting = meetingMapper.getMeetingById(999);

        assertNull(meeting);
        verify(meetingMapper, times(1)).getMeetingById(999);
    }

    // =============== deleteMeeting 测试 ===============
    @Test
    void deleteMeeting_ShouldReturn1WhenSuccess() {
        // 正常情况 - 删除成功
        when(meetingMapper.deleteMeeting(1)).thenReturn(1);

        int result = meetingMapper.deleteMeeting(1);

        assertEquals(1, result);
        verify(meetingMapper, times(1)).deleteMeeting(1);
    }

    @Test
    void deleteMeeting_ShouldReturn0WhenNotExists() {
        // 边界情况 - 会议不存在
        when(meetingMapper.deleteMeeting(999)).thenReturn(0);

        int result = meetingMapper.deleteMeeting(999);

        assertEquals(0, result);
        verify(meetingMapper, times(1)).deleteMeeting(999);
    }

    // =============== addMeeting 测试 ===============
    @Test
    void addMeeting_ShouldReturn1WhenSuccessWithFullFields() {
        // 正常情况 - 完整字段添加
        when(meetingMapper.addMeeting(validMeeting)).thenReturn(1);

        int result = meetingMapper.addMeeting(validMeeting);

        assertEquals(1, result);
        verify(meetingMapper, times(1)).addMeeting(validMeeting);
    }

    @Test
    void addMeeting_ShouldReturn1WhenSuccessWithMinimalFields() {
        // 边界情况 - 最小字段添加 (只包含非空字段)
        when(meetingMapper.addMeeting(minimalMeeting)).thenReturn(1);

        int result = meetingMapper.addMeeting(minimalMeeting);

        assertEquals(1, result);
        verify(meetingMapper, times(1)).addMeeting(minimalMeeting);
    }

    @Test
    void addMeeting_ShouldThrowExceptionWhenRequiredFieldsMissing() {
        // 异常情况 - 缺少必填字段
        Meeting invalidMeeting = new Meeting();
        invalidMeeting.setMeeting_name("Invalid Meeting"); // 缺少 cover_url, start_time, end_time, creator_name

        // 模拟 MyBatis 抛出异常
        when(meetingMapper.addMeeting(invalidMeeting)).thenThrow(new RuntimeException("Required fields missing"));

        assertThrows(RuntimeException.class, () -> meetingMapper.addMeeting(invalidMeeting));
        verify(meetingMapper, times(1)).addMeeting(invalidMeeting);
    }

    // =============== updateMeeting 测试 ===============
    @Test
    void updateMeeting_ShouldReturn1WhenSuccess() {
        // 正常情况 - 更新成功
        when(meetingMapper.updateMeeting(validMeeting)).thenReturn(1);

        int result = meetingMapper.updateMeeting(validMeeting);

        assertEquals(1, result);
        verify(meetingMapper, times(1)).updateMeeting(validMeeting);
    }

    @Test
    void updateMeeting_ShouldReturn0WhenNotExists() {
        // 边界情况 - 会议不存在
        Meeting nonExistentMeeting = new Meeting(999, "cover.jpg", "Nonexistent",
                now, now.plusHours(1), "Content", now, "user", 0);

        when(meetingMapper.updateMeeting(nonExistentMeeting)).thenReturn(0);

        int result = meetingMapper.updateMeeting(nonExistentMeeting);

        assertEquals(0, result);
        verify(meetingMapper, times(1)).updateMeeting(nonExistentMeeting);
    }

    @Test
    void updateMeeting_ShouldThrowExceptionWhenRequiredFieldsMissing() {
        // 异常情况 - 缺少必填字段
        Meeting invalidMeeting = new Meeting(1, null, null, null, null, null, null, null, null);

        // 模拟 MyBatis 抛出异常
        when(meetingMapper.updateMeeting(invalidMeeting)).thenThrow(new RuntimeException("Required fields missing"));

        assertThrows(RuntimeException.class, () -> meetingMapper.updateMeeting(invalidMeeting));
        verify(meetingMapper, times(1)).updateMeeting(invalidMeeting);
    }

    // =============== updateMeetingAuditStatus 测试 ===============
    @Test
    void updateMeetingAuditStatus_ShouldReturn1WhenSuccess() {
        // 正常情况 - 更新成功
        when(meetingMapper.updateMeetingAuditStatus(1, 2)).thenReturn(1);

        int result = meetingMapper.updateMeetingAuditStatus(1, 2);

        assertEquals(1, result);
        verify(meetingMapper, times(1)).updateMeetingAuditStatus(1, 2);
    }

    @Test
    void updateMeetingAuditStatus_ShouldReturn0WhenNotExists() {
        // 边界情况 - 会议不存在
        when(meetingMapper.updateMeetingAuditStatus(999, 1)).thenReturn(0);

        int result = meetingMapper.updateMeetingAuditStatus(999, 1);

        assertEquals(0, result);
        verify(meetingMapper, times(1)).updateMeetingAuditStatus(999, 1);
    }

    @Test
    void updateMeetingAuditStatus_ShouldReturn0WhenInvalidStatus() {
        // 边界情况 - 无效状态
        when(meetingMapper.updateMeetingAuditStatus(1, 999)).thenReturn(0);

        int result = meetingMapper.updateMeetingAuditStatus(1, 999);

        assertEquals(0, result);
        verify(meetingMapper, times(1)).updateMeetingAuditStatus(1, 999);
    }
}