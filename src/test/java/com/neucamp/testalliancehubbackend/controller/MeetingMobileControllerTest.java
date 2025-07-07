package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.Service.ConferenceService;
import com.neucamp.testalliancehubbackend.entity.ConferenceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeetingMobileControllerTest {

    private MeetingMobileController controller;
    private FakeConferenceService fakeService;

    static class FakeConferenceService extends ConferenceService {
        @Override
        public List<ConferenceDTO> getAllConferences() {
            List<ConferenceDTO> list = new ArrayList<>();
            list.add(new ConferenceDTO(1, "会议A", "创建者A", "赞助A", "议程", "嘉宾", "地点A", "内容A", "类别A", "2025-07-01 09:00", "2025-07-01 17:00", "coverA.jpg", "2025-06-01"));
            list.add(new ConferenceDTO(2, "会议B", "创建者B", "赞助B", "议程", "嘉宾", "地点B", "内容B", "类别B", "2025-08-01 09:00", "2025-08-01 17:00", "coverB.jpg", "2025-06-15"));
            return list;
        }
    }

    @BeforeEach
    void setup() throws Exception {
        controller = new MeetingMobileController();
        fakeService = new FakeConferenceService();

        Field field = MeetingMobileController.class.getDeclaredField("conferenceService");
        field.setAccessible(true);
        field.set(controller, fakeService);
    }

    @Test
    void testGetAllConferences() {
        ResponseEntity<List<ConferenceDTO>> response = controller.getAllConferences();
        assertNotNull(response);
        List<ConferenceDTO> list = response.getBody();
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("会议A", list.get(0).getName());
        assertEquals("创建者B", list.get(1).getCreatorName());
    }
}
