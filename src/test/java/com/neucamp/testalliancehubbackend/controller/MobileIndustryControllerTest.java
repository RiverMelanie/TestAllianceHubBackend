package com.neucamp.testalliancehubbackend.controller;
import com.neucamp.testalliancehubbackend.mapper.MobileIndustryMapper;
import com.neucamp.testalliancehubbackend.entity.IndustryDynamic;
import com.neucamp.testalliancehubbackend.entity.Visit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MobileIndustryControllerTest {

    @Mock
    private MobileIndustryMapper mobileIndustryMapper;

    @InjectMocks
    private MobileIndustryController controller;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void getTopClickedDynamics_shouldReturnDataForValidDateRange() {
        // 准备测试数据
        List<Map<String, Object>> testData = Arrays.asList(
                Map.of("dynamicId", 101L, "title", "行业趋势报告", "clickCount", 2450L),
                Map.of("dynamicId", 205L, "title", "技术创新分析", "clickCount", 1980L)
        );

        // 模拟Mapper响应
        when(mobileIndustryMapper.selectTopDynamicsByTimeRange(anyString(), anyString()))
                .thenReturn(testData);

        // 定义测试日期范围
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        // 执行测试
        ResponseEntity<List<Map<String, Object>>> response =
                controller.getTopClickedDynamics(startDate, endDate);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Map<String, Object>> result = response.getBody();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("行业趋势报告", result.get(0).get("title"));
        assertEquals(2450L, result.get(0).get("clickCount"));
    }

    @Test
    void getTopClickedDynamics_shouldHandleEmptyResult() {
        // 模拟空响应
        when(mobileIndustryMapper.selectTopDynamicsByTimeRange(anyString(), anyString()))
                .thenReturn(Collections.emptyList());

        // 定义测试日期范围
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();


        // 执行测试
        ResponseEntity<List<Map<String, Object>>> response =
                controller.getTopClickedDynamics(startDate, endDate);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Map<String, Object>> result = response.getBody();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getTopClickedDynamics_shouldHandleInvalidDateRange() {
        // 定义无效日期范围（结束日期早于开始日期）
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().minusDays(7);

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.getTopClickedDynamics(startDate, endDate);
        });

        // 验证异常消息
        assertEquals("结束日期不能早于开始日期", exception.getMessage());
    }


    @Test
    void getTopClickedDynamics_shouldHandleSameStartAndEndDate() {
        // 准备测试数据
        List<Map<String, Object>> testData = Collections.singletonList(
                Map.of("dynamicId", 101L, "title", "行业趋势报告", "clickCount", 2450L)
        );

        // 模拟Mapper响应
        when(mobileIndustryMapper.selectTopDynamicsByTimeRange(anyString(), anyString()))
                .thenReturn(testData);

        // 定义测试日期范围（同一天）
        LocalDate today = LocalDate.now();

        // 执行测试
        ResponseEntity<List<Map<String, Object>>> response =
                controller.getTopClickedDynamics(today, today);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Map<String, Object>> result = response.getBody();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getAllDynamicsByKeyword_shouldReturnDynamicsWithKeyword() {
        // Prepare test data with correct property names
        IndustryDynamic dynamic1 = new IndustryDynamic(1, 100, "Industry Trend", "Content",
                "Summary", "Author", "image.jpg", new Date(), 1);
        IndustryDynamic dynamic2 = new IndustryDynamic(2, 101, "Tech Innovation", "Content",
                "Summary", "Author", "image2.jpg", new Date(), 1);
        List<IndustryDynamic> testData = Arrays.asList(dynamic1, dynamic2);

        // Mock mapper response
        when(mobileIndustryMapper.getAllDynamicsByKeyword("tech")).thenReturn(testData);

        // Execute test
        ResponseEntity<List<IndustryDynamic>> response = controller.getAllDynamicsByKeyword("tech");

        // Verify results
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<IndustryDynamic> result = response.getBody();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Tech Innovation", result.get(1).getTitle());
        verify(mobileIndustryMapper).getAllDynamicsByKeyword("tech");
    }

    @Test
    void getAllDynamicsByKeyword_shouldHandleEmptyKeyword() {
        // Prepare test data
        IndustryDynamic dynamic = new IndustryDynamic(1, 100, "General Report", "Content",
                "Summary", "Author", "image.jpg", new Date(), 1);
        List<IndustryDynamic> testData = Collections.singletonList(dynamic);

        // Mock mapper response
        when(mobileIndustryMapper.getAllDynamicsByKeyword("")).thenReturn(testData);

        // Execute test
        ResponseEntity<List<IndustryDynamic>> response = controller.getAllDynamicsByKeyword(null);

        // Verify results
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<IndustryDynamic> result = response.getBody();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("General Report", result.get(0).getTitle());
        verify(mobileIndustryMapper).getAllDynamicsByKeyword("");
    }

    @Test
    void getAllDynamicsByKeyword_shouldReturnEmptyListWhenNoResults() {
        // Mock empty response
        when(mobileIndustryMapper.getAllDynamicsByKeyword(anyString())).thenReturn(Collections.emptyList());

        // Execute test
        ResponseEntity<List<IndustryDynamic>> response = controller.getAllDynamicsByKeyword("nonexistent");

        // Verify results
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<IndustryDynamic> result = response.getBody();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mobileIndustryMapper).getAllDynamicsByKeyword("nonexistent");
    }

    // Tests for recordVisit
    @Test
    void recordVisit_shouldSuccessfullyRecordValidVisit() {
        // Prepare test data with correct property names
        Visit visit = new Visit();
        visit.setDynamic_id(1);
        visit.setUser_id(100);

        // Mock mapper - 假设 insertVisit 返回影响的行数
        when(mobileIndustryMapper.insertVisit(any(Visit.class))).thenReturn(1);

        // Execute test
        ResponseEntity<String> response = controller.recordVisit(visit);

        // Verify results
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("记录成功", response.getBody());
        assertNotNull(visit.getVisit_time());
        verify(mobileIndustryMapper).insertVisit(visit);
    }

    @Test
    void recordVisit_shouldRejectWhenDynamicIdIsNull() {
        // Prepare test data
        Visit visit = new Visit();
        visit.setUser_id(100);
        visit.setDynamic_id(null);

        // Execute test
        ResponseEntity<String> response = controller.recordVisit(visit);

        // Verify results
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("dynamic_id不能为空", response.getBody());
        verify(mobileIndustryMapper, never()).insertVisit(any(Visit.class));
    }

    @Test
    void recordVisit_shouldRejectWhenUserIdIsNull() {
        // Prepare test data
        Visit visit = new Visit();
        visit.setDynamic_id(1);
        visit.setUser_id(null);

        // Execute test
        ResponseEntity<String> response = controller.recordVisit(visit);

        // Verify results
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("user_id不能为空", response.getBody());
        verify(mobileIndustryMapper, never()).insertVisit(any(Visit.class));
    }

    @Test
    void recordVisit_shouldSetCurrentTimeForVisit() {
        // Prepare test data
        LocalDateTime beforeTest = LocalDateTime.now().minusSeconds(1);
        Visit visit = new Visit();
        visit.setDynamic_id(1);
        visit.setUser_id(100);

        // Mock mapper - 假设返回1表示插入成功
        when(mobileIndustryMapper.insertVisit(any(Visit.class))).thenReturn(1);

        // Execute test
        ResponseEntity<String> response = controller.recordVisit(visit);

        // Verify results
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(visit.getVisit_time());
        assertTrue(visit.getVisit_time().isAfter(beforeTest));
        assertTrue(visit.getVisit_time().isBefore(LocalDateTime.now().plusSeconds(1)));
        verify(mobileIndustryMapper).insertVisit(visit);
    }

    @Test
    void recordVisit_shouldHandleConstructorWithParameters() {
        // Prepare test data using constructor
        LocalDateTime beforeTest = LocalDateTime.now().minusSeconds(1);
        Visit visit = new Visit(100, 1);

        // Mock mapper
        when(mobileIndustryMapper.insertVisit(any(Visit.class))).thenReturn(1);

        // Execute test
        ResponseEntity<String> response = controller.recordVisit(visit);

        // Verify results
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("记录成功", response.getBody());
        assertNotNull(visit.getVisit_time());
        assertTrue(visit.getVisit_time().isAfter(beforeTest));
        verify(mobileIndustryMapper).insertVisit(visit);
    }
}