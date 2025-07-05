package com.neucamp.testalliancehubbackend.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MobileIndustryMapperTest {

    @Mock
    private MobileIndustryMapper mobileIndustryMapper;

    @Test
    void selectTopDynamicsByTimeRange_shouldReturnTopDynamics() {
        // 准备测试数据
        List<Map<String, Object>> expected = Arrays.asList(
                Map.of("dynamicId", 101L, "title", "行业趋势报告", "clickCount", 2450L),
                Map.of("dynamicId", 205L, "title", "技术创新分析", "clickCount", 1980L)
        );

        // 模拟方法调用
        when(mobileIndustryMapper.selectTopDynamicsByTimeRange("2023-01-01 00:00:00", "2023-01-31 23:59:59"))
                .thenReturn(expected);

        // 执行测试
        List<Map<String, Object>> result = mobileIndustryMapper.selectTopDynamicsByTimeRange(
                "2023-01-01 00:00:00", "2023-01-31 23:59:59");

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("行业趋势报告", result.get(0).get("title"));
        assertEquals(2450L, result.get(0).get("clickCount"));
    }

    @Test
    void selectTopDynamicsByTimeRange_shouldReturnEmptyListWhenNoData() {
        // 模拟方法调用
        when(mobileIndustryMapper.selectTopDynamicsByTimeRange("2023-01-01 00:00:00", "2023-01-31 23:59:59"))
                .thenReturn(Collections.emptyList());

        // 执行测试
        List<Map<String, Object>> result = mobileIndustryMapper.selectTopDynamicsByTimeRange(
                "2023-01-01 00:00:00", "2023-01-31 23:59:59");

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}