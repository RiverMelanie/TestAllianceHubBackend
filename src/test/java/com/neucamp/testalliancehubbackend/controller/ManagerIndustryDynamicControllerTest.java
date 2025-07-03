package com.neucamp.testalliancehubbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neucamp.testalliancehubbackend.entity.IndustryDynamic;
import com.neucamp.testalliancehubbackend.entity.dynamicreviewrecordtable;
import com.neucamp.testalliancehubbackend.mapper.IndustryDynamicMapper;
import com.neucamp.testalliancehubbackend.mapper.ManagerIndustryDynamicMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ManagerIndustryDynamicControllerTest {

    // Mock Mapper（实际不会执行数据库操作，仅模拟行为）
    @Mock
    private ManagerIndustryDynamicMapper managerIndustryDynamicMapper;

    @Mock
    private IndustryDynamicMapper industryDynamicMapper;

    // 将 Controller 注入到测试环境（结合 Mock 的 Mapper）
    @InjectMocks
    private ManagerIndustryDynamicController controller;

    private MockMvc mockMvc; // 模拟 HTTP 请求
    private ObjectMapper objectMapper; // 序列化/反序列化 JSON

    // 初始化测试环境
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    // 测试 1：分页查询动态（正常场景）
    @Test
    void testSearchDynamics() throws Exception {
        int pageNum = 1;
        int pageSize = 8;
        int offset = (pageNum - 1) * pageSize;

        Map<String, Object> params = new HashMap<>();
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        // 模拟 Mapper 返回数据
        List<dynamicreviewrecordtable> dynamics = new ArrayList<>();
        dynamicreviewrecordtable dynamic1 = new dynamicreviewrecordtable();
        dynamic1.setReviewerID(1);
        dynamic1.setTitle("测试标题1");
        dynamics.add(dynamic1);

        when(managerIndustryDynamicMapper.searchDynamics(params)).thenReturn(dynamics);
        when(managerIndustryDynamicMapper.searchTotalCount(params)).thenReturn(1);

        // 执行 HTTP 请求并断言结果
        mockMvc.perform(get("/reviewsearch")
                        .param("pageNum", String.valueOf(pageNum))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].ReviewerID").value(1))
                .andExpect(jsonPath("$.totalCount").value(1));

        // 验证 Mapper 方法被调用次数
        verify(managerIndustryDynamicMapper, times(1)).searchDynamics(params);
        verify(managerIndustryDynamicMapper, times(1)).searchTotalCount(params);
    }

    // 测试 2：更新动态（正常场景）
    @Test
    void testUpDynamic() throws Exception {
        IndustryDynamic industryDynamic = new IndustryDynamic();
        industryDynamic.setDynamicId(1);
        industryDynamic.setPublisherId(1);
        industryDynamic.setTitle("更新后的标题");

        // 模拟 Mapper 返回更新成功（影响行数 1）
        when(managerIndustryDynamicMapper.upDynamic(industryDynamic)).thenReturn(1);

        // 执行 HTTP 请求并断言结果
        mockMvc.perform(post("/upDynamic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(industryDynamic)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        // 验证 Mapper 方法被调用
        verify(managerIndustryDynamicMapper, times(1)).upDynamic(industryDynamic);
    }

    // 测试 3：审核动态（成功场景：审核通过 + 更新状态）
    @Test
    void testUpReviewDynamic_Success() throws Exception {
        dynamicreviewrecordtable reviewRecord = new dynamicreviewrecordtable();
        reviewRecord.setReviewerID(1);
        reviewRecord.setTitle("测试标题");
        reviewRecord.setNewsImage("image.jpg");
        reviewRecord.setReviewResult("通过");

        // 模拟查询动态结果
        IndustryDynamic industryDynamic = new IndustryDynamic();
        industryDynamic.setDynamicId(1);
        industryDynamic.setAuditStatus(0);

        // 模拟 Mapper 方法返回
        when(managerIndustryDynamicMapper.upReviewDynamic(reviewRecord)).thenReturn(1);
        when(industryDynamicMapper.searchreviewdDynamics(reviewRecord)).thenReturn(industryDynamic);

        // 执行 HTTP 请求并断言结果
        mockMvc.perform(post("/upReviewDynamic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRecord)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        // 验证 Mapper 调用
        verify(managerIndustryDynamicMapper, times(1)).upReviewDynamic(reviewRecord);
        verify(industryDynamicMapper, times(1)).searchreviewdDynamics(reviewRecord);
        // 验证 upDynamicPlus 调用（通过条件匹配器校验参数）
        verify(managerIndustryDynamicMapper, times(1)).upDynamicPlus(argThat(updated ->
                updated.getAuditStatus() == 1
        ));
    }

    // 测试 4：审核动态（失败场景：审核记录更新失败，不执行状态更新）
    @Test
    void testUpReviewDynamic_Failure() throws Exception {
        dynamicreviewrecordtable reviewRecord = new dynamicreviewrecordtable();
        reviewRecord.setReviewerID(1);
        reviewRecord.setTitle("测试标题");
        reviewRecord.setNewsImage("image.jpg");
        reviewRecord.setReviewResult("通过");

        // 模拟 Mapper 返回更新失败（影响行数 0）
        when(managerIndustryDynamicMapper.upReviewDynamic(reviewRecord)).thenReturn(0);

        // 执行 HTTP 请求并断言结果
        mockMvc.perform(post("/upReviewDynamic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRecord)))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));

        // 验证 Mapper 调用（失败时不执行后续方法）
        verify(managerIndustryDynamicMapper, times(1)).upReviewDynamic(reviewRecord);
        verify(industryDynamicMapper, never()).searchreviewdDynamics(any());
        verify(managerIndustryDynamicMapper, never()).upDynamicPlus(any());
    }

    // 测试 5：清空动态
    @Test
    void testClean() throws Exception {
        mockMvc.perform(post("/clean")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 验证 Mapper 方法被调用
        verify(managerIndustryDynamicMapper, times(1)).cleanDynamics();
    }

    // 测试 6：删除动态（正常场景）
    @Test
    void testDelDynamic() throws Exception {
        int id = 1;
        // 模拟 Mapper 返回删除成功（影响行数 1）
        when(managerIndustryDynamicMapper.delDynamic(id)).thenReturn(1);

        // 执行 HTTP 请求并断言结果
        mockMvc.perform(get("/delDynamic?id=" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        // 验证 Mapper 方法被调用
        verify(managerIndustryDynamicMapper, times(1)).delDynamic(id);
    }
}