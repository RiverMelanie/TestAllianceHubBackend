package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.Service.ConferenceService;
import com.neucamp.testalliancehubbackend.entity.ConferenceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET},
        maxAge = 3600
)
public class MeetingMobileController {
    @Autowired
    private ConferenceService conferenceService;
    @RequestMapping("/getmeetingMobile")
    public ResponseEntity<List<ConferenceDTO>> getAllConferences() {
        List<ConferenceDTO> conferences = conferenceService.getAllConferences();

        for (int i = 0; i < conferences.size(); i++) {
            ConferenceDTO dto = conferences.get(i);
            System.out.println("第 " + (i + 1) + " 条数据:");
            System.out.println("  id: " + dto.getId());
            System.out.println("  标题: " + dto.getName());
            System.out.println("  创建者: " + dto.getCreatorName());
            System.out.println("  创建时间: " + dto.getCreateTime());
            System.out.println("  地点: " + dto.getLocation());
            System.out.println("  描述: " + dto.getContent());
            System.out.println("------------------------------------");
        }

        return ResponseEntity.ok(conferences);
    }
}
