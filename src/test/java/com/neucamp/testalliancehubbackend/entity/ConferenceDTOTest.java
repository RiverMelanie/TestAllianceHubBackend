package com.neucamp.testalliancehubbackend.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConferenceDTOTest {

    @Test
    void testAllGettersAndSettersAndConstructors() {
        ConferenceDTO dto = new ConferenceDTO();
        dto.setId(1);
        dto.setName("会议");
        dto.setCreatorName("张三");
        dto.setSponsor("公司A");
        dto.setAgenda("日程安排");
        dto.setGuests("嘉宾名单");
        dto.setLocation("北京");
        dto.setContent("会议内容");
        dto.setCategory("学术");
        dto.setStartTime("2025-01-01 09:00");
        dto.setEndTime("2025-01-01 17:00");
        dto.setCoverUrl("http://example.com/img.jpg");
        dto.setCreateTime("2024-12-31 08:00");

        assertEquals(1, dto.getId());
        assertEquals("会议", dto.getName());
        assertEquals("张三", dto.getCreatorName());
        assertEquals("公司A", dto.getSponsor());
        assertEquals("日程安排", dto.getAgenda());
        assertEquals("嘉宾名单", dto.getGuests());
        assertEquals("北京", dto.getLocation());
        assertEquals("会议内容", dto.getContent());
        assertEquals("学术", dto.getCategory());
        assertEquals("2025-01-01 09:00", dto.getStartTime());
        assertEquals("2025-01-01 17:00", dto.getEndTime());
        assertEquals("http://example.com/img.jpg", dto.getCoverUrl());
        assertEquals("2024-12-31 08:00", dto.getCreateTime());

        ConferenceDTO fullDto = new ConferenceDTO(1, "会议", "张三", "公司A", "日程安排",
                "嘉宾名单", "北京", "会议内容", "学术", "2025-01-01 09:00", "2025-01-01 17:00",
                "http://example.com/img.jpg", "2024-12-31 08:00");
        assertNotNull(fullDto);
    }
}
