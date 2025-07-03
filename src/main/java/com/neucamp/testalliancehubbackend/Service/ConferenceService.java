package com.neucamp.testalliancehubbackend.Service;

import com.neucamp.testalliancehubbackend.entity.ConferenceDTO;
import com.neucamp.testalliancehubbackend.mapper.MeetingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConferenceService {
    @Autowired
    private MeetingMapper meetingMapper;

    public List<ConferenceDTO> getAllConferences() {
        return meetingMapper.getAllConferences();  // 直接调用 Mapper 查询
    }
}
