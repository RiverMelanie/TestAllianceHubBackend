package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.entity.IndustryDynamic;
import com.neucamp.testalliancehubbackend.entity.dynamicreviewrecordtable;
import com.neucamp.testalliancehubbackend.mapper.IndustryDynamicMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

class IndustryDynamicControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IndustryDynamicMapper industryDynamicMapper;

    @InjectMocks
    private IndustryDynamicController controller;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    }
    //正常添加行业动态
    @Test
    void testAddDynamic_Success() throws Exception {
        IndustryDynamic dynamic = new IndustryDynamic();
        dynamic.setPublisherId(1);
        dynamic.setTitle("AI行业新趋势");
        dynamic.setContent("详细内容...");
        dynamic.setSummary("AI趋势总结");
        dynamic.setAuthor("技术专家");
        dynamic.setImageUrl("image.png");
        dynamic.setAuditStatus(1);

        when(industryDynamicMapper.addDynamic(any(IndustryDynamic.class))).thenReturn(1);

        mockMvc.perform(post("/addDynamic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dynamic)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(industryDynamicMapper, times(1)).addDynamic(any(IndustryDynamic.class));
    }
    //正常查询
    @Test
    void testSearchDynamics_FilterAuditStatus() throws Exception {
        IndustryDynamic approved = new IndustryDynamic();
        approved.setDynamicId(1);
        approved.setTitle("已审核动态");
        approved.setAuditStatus(1);

        IndustryDynamic unApproved = new IndustryDynamic();
        unApproved.setDynamicId(2);
        unApproved.setTitle("未审核动态");
        unApproved.setAuditStatus(0);

        List<IndustryDynamic> mockData = new ArrayList<>();
        mockData.add(approved);
        mockData.add(unApproved);

        when(industryDynamicMapper.searchDynamics(anyMap())).thenReturn(mockData);
        when(industryDynamicMapper.searchTotalCount(anyMap())).thenReturn(2);

        mockMvc.perform(get("/search")
                        .param("title", "动态")
                        .param("author", "")
                        .param("pageNum", "1")
                        .param("pageSize", "8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].title", is("已审核动态")))
                .andExpect(jsonPath("$.totalCount", is(2)));

        verify(industryDynamicMapper, times(1)).searchDynamics(anyMap());
        verify(industryDynamicMapper, times(1)).searchTotalCount(anyMap());
    }
    //正常增加审核记录
    @Test
    void testAddReviewRecord_Success() throws Exception {
        dynamicreviewrecordtable record = new dynamicreviewrecordtable();
        record.setReviewerID(100);
        record.setTitle("待审核动态");
        record.setNewsImage("image.png");
        record.setContent("内容...");
        record.setNewsSummary("摘要");
        record.setAuthor("作者");
        record.setReviewResult("通过");

        when(industryDynamicMapper.addReviewRecord(
                anyInt(), any(String.class), any(String.class),
                any(String.class), any(String.class),
                any(String.class), any(String.class))).thenReturn(1);

        mockMvc.perform(post("/addreviewrecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isOk());

        verify(industryDynamicMapper, times(1)).addReviewRecord(
                anyInt(), any(String.class), any(String.class),
                any(String.class), any(String.class),
                any(String.class), any(String.class));
    }

    // 添加动态记录：标题为空
    @Test
    void testAddDynamic_EmptyTitle_Fail() throws Exception {
        // 1. 构造非法参数（标题为空）
        IndustryDynamic dynamic = new IndustryDynamic();
        dynamic.setPublisherId(1);
        dynamic.setTitle(""); // 空标题
        dynamic.setContent("内容...");

        // 2. 执行HTTP请求
        mockMvc.perform(post("/addDynamic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dynamic)))
                .andExpect(status().isBadRequest()); // 期望返回400（需Controller补充@Valid校验）

        // 3. 验证：Mapper未被调用（参数非法时不应执行数据库操作）
        verify(industryDynamicMapper, never()).addDynamic(any(IndustryDynamic.class));
    }
    //输入无效页数
    @Test
    void testSearchDynamics_InvalidPageNum_Fail() throws Exception {
        mockMvc.perform(get("/search")
                        .param("pageNum", "0") // 非法参数
                        .param("pageSize", "8"))
                .andExpect(status().isBadRequest()); // 验证返回400

        // 验证：Mapper未被调用
        verify(industryDynamicMapper, never()).searchDynamics(anyMap());
        verify(industryDynamicMapper, never()).searchTotalCount(anyMap());
    }
    //添加审核记录：审核员ID为空
    @Test
    void testAddReviewRecord_NullReviewerID_Fail() throws Exception {
        dynamicreviewrecordtable record = new dynamicreviewrecordtable();
        record.setReviewerID(null);
        record.setTitle("标题");

        mockMvc.perform(post("/addreviewrecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isBadRequest());

        verify(industryDynamicMapper, never()).addReviewRecord(
                anyInt(), any(String.class), any(String.class),
                any(String.class), any(String.class),
                any(String.class), any(String.class));
    }
    //分页边界值测试
    @Test
    void testSearchDynamics_MaxPageSize() throws Exception {
        List<IndustryDynamic> mockData = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            IndustryDynamic dynamic = new IndustryDynamic();
            dynamic.setDynamicId(i + 1);
            dynamic.setAuditStatus(1);
            mockData.add(dynamic);
        }

        when(industryDynamicMapper.searchDynamics(anyMap())).thenReturn(mockData);
        when(industryDynamicMapper.searchTotalCount(anyMap())).thenReturn(1000);

        mockMvc.perform(get("/search")
                        .param("pageNum", "1")
                        .param("pageSize", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1000)))
                .andExpect(jsonPath("$.totalCount", is(1000)));

        verify(industryDynamicMapper, times(1)).searchDynamics(anyMap());
    }
    //创建时间边界值测试
    @Test
    void testAddDynamic_MaxCreateTime() throws Exception {
        IndustryDynamic dynamic = new IndustryDynamic();
        dynamic.setPublisherId(1);
        dynamic.setTitle("边界测试动态");
        dynamic.setCreateTime(new Date(Long.MAX_VALUE));

        when(industryDynamicMapper.addDynamic(any(IndustryDynamic.class))).thenReturn(1);

        mockMvc.perform(post("/addDynamic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dynamic)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(industryDynamicMapper, times(1)).addDynamic(any(IndustryDynamic.class));
    }
}