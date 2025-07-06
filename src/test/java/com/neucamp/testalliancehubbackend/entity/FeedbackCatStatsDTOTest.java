package com.neucamp.testalliancehubbackend.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackCatStatsDTOTest {

    @Test
    void testGettersAndSetters() {
        FeedbackCatStatsDTO dto = new FeedbackCatStatsDTO();

        dto.setCatName("学术会议");
        dto.setCount(42L);
        dto.setRatio(0.35);

        assertEquals("学术会议", dto.getCatName());
        assertEquals(42L, dto.getCount());
        assertEquals(0.35, dto.getRatio());
    }
}
