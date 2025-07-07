package com.neucamp.testalliancehubbackend.Service;

import com.neucamp.testalliancehubbackend.entity.ConferenceDTO;
import com.neucamp.testalliancehubbackend.mapper.MeetingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConferenceServiceTest {

    @Mock
    private MeetingMapper meetingMapper;

    @InjectMocks
    private ConferenceService conferenceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllConferences_ReturnsExpectedList() {
        ConferenceDTO dto1 = new ConferenceDTO(1, "会议A", "Alice", "主办方A", "", "", "", "", "", "", "", "", "");
        ConferenceDTO dto2 = new ConferenceDTO(2, "会议B", "Bob", "主办方B", "", "", "", "", "", "", "", "", "");
        List<ConferenceDTO> expectedList = Arrays.asList(dto1, dto2);

        when(meetingMapper.getAllConferences()).thenReturn(expectedList);

        List<ConferenceDTO> result = conferenceService.getAllConferences();

        assertEquals(2, result.size());
        assertEquals("会议A", result.get(0).getName());
        verify(meetingMapper, times(1)).getAllConferences();
    }
}
