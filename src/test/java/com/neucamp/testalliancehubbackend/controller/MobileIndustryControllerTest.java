package com.neucamp.testalliancehubbackend.controller;
import com.neucamp.testalliancehubbackend.mapper.MobileIndustryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DynamicAnalysisControllerTest {

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
}