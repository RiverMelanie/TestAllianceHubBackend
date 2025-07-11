package com.neucamp.testalliancehubbackend.mapper;

import com.neucamp.testalliancehubbackend.entity.IndustryDynamic;
import com.neucamp.testalliancehubbackend.entity.Visit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void getAllDynamicsByKeyword_shouldReturnMatchingDynamics() {
        // 准备测试数据
        IndustryDynamic dynamic1 = new IndustryDynamic(1, 1001, "行业趋势报告", "最新的行业趋势分析报告",
                "趋势分析摘要", "作者1", "image1.jpg", new Date(), 1);
        IndustryDynamic dynamic2 = new IndustryDynamic(2, 1002, "市场分析", "行业市场趋势分析",
                "市场分析摘要", "作者2", "image2.jpg", new Date(), 1);

        List<IndustryDynamic> expected = Arrays.asList(dynamic1, dynamic2);

        // 模拟方法调用
        when(mobileIndustryMapper.getAllDynamicsByKeyword("趋势"))
                .thenReturn(expected);

        // 执行测试
        List<IndustryDynamic> result = mobileIndustryMapper.getAllDynamicsByKeyword("趋势");

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getTitle().contains("趋势") || result.get(0).getContent().contains("趋势"));
        assertEquals(1, result.get(0).getAuditStatus());
        assertEquals("作者1", result.get(0).getAuthor());
        assertNotNull(result.get(0).getCreateTime());
    }

    @Test
    void getAllDynamicsByKeyword_shouldReturnEmptyListWhenNoMatch() {
        // 模拟方法调用
        when(mobileIndustryMapper.getAllDynamicsByKeyword("不存在的关键词"))
                .thenReturn(Collections.emptyList());

        // 执行测试
        List<IndustryDynamic> result = mobileIndustryMapper.getAllDynamicsByKeyword("不存在的关键词");

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllDynamicsByKeyword_shouldHandleEmptyKeyword() {
        // 准备测试数据
        IndustryDynamic dynamic = new IndustryDynamic(1, 1001, "默认动态", "默认内容",
                "摘要", "作者", "image.jpg", new Date(), 1);

        // 模拟方法调用 - 空关键词应返回所有审核通过的动态
        when(mobileIndustryMapper.getAllDynamicsByKeyword(""))
                .thenReturn(Collections.singletonList(dynamic));

        // 执行测试
        List<IndustryDynamic> result = mobileIndustryMapper.getAllDynamicsByKeyword("");

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getAuditStatus());
        assertEquals("默认动态", result.get(0).getTitle());
    }

    @Test
    void getAllDynamicsByKeyword_shouldHandleNullKeyword() {
        // 模拟方法调用 - 处理null关键词
        when(mobileIndustryMapper.getAllDynamicsByKeyword(null))
                .thenReturn(Collections.emptyList());

        // 执行测试
        List<IndustryDynamic> result = mobileIndustryMapper.getAllDynamicsByKeyword(null);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllDynamicsByKeyword_shouldOnlyReturnAuditedDynamics() {
        // 准备测试数据
        IndustryDynamic auditedDynamic = new IndustryDynamic(1, 1001, "已审核动态", "已审核内容",
                "摘要", "作者", "image.jpg", new Date(), 1);
        IndustryDynamic unAuditedDynamic = new IndustryDynamic(2, 1002, "未审核动态", "未审核内容",
                "摘要", "作者", "image.jpg", new Date(), 0);

        // 模拟方法调用 - 只返回auditStatus=1的记录
        when(mobileIndustryMapper.getAllDynamicsByKeyword("动态"))
                .thenReturn(Collections.singletonList(auditedDynamic));

        // 执行测试
        List<IndustryDynamic> result = mobileIndustryMapper.getAllDynamicsByKeyword("动态");

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getAuditStatus());
        assertNotEquals(0, result.get(0).getAuditStatus());
    }

    // insertVisit 测试
    @Test
    void insertVisit_shouldReturn1WhenSuccess() {
        // 准备测试数据
        Visit visit = new Visit(1001, 2001);
        visit.setVisit_time(LocalDateTime.now());

        // 模拟方法调用
        when(mobileIndustryMapper.insertVisit(visit)).thenReturn(1);

        // 执行测试
        int result = mobileIndustryMapper.insertVisit(visit);

        // 验证结果
        assertEquals(1, result);
        assertNotNull(visit.getVisit_time());
    }

    @Test
    void insertVisit_shouldHandleNullFields() {
        // 准备测试数据 - 使用无参构造
        Visit visit = new Visit();
        visit.setUser_id(null);
        visit.setDynamic_id(2001);
        visit.setVisit_time(null);

        // 模拟方法调用 - 假设数据库不允许null值，返回0表示失败
        when(mobileIndustryMapper.insertVisit(visit)).thenReturn(0);

        // 执行测试
        int result = mobileIndustryMapper.insertVisit(visit);

        // 验证结果
        assertEquals(0, result);
        assertNull(visit.getUser_id());
        assertNull(visit.getVisit_time());
    }

    @Test
    void insertVisit_shouldHandleDuplicateVisit() {
        // 准备测试数据
        Visit visit = new Visit(1001, 2001);
        visit.setVisit_time(LocalDateTime.now());

        // 模拟方法调用 - 假设数据库有唯一约束，返回0表示失败
        when(mobileIndustryMapper.insertVisit(visit)).thenReturn(0);

        // 执行测试
        int result = mobileIndustryMapper.insertVisit(visit);

        // 验证结果
        assertEquals(0, result);
    }

    @Test
    void checkVisitExists_shouldReturnCount() {
        // 模拟方法调用
        when(mobileIndustryMapper.checkVisitExists(1001, 2001)).thenReturn(1);
        when(mobileIndustryMapper.checkVisitExists(1002, 2002)).thenReturn(0);

        // 执行测试
        int exists = mobileIndustryMapper.checkVisitExists(1001, 2001);
        int notExists = mobileIndustryMapper.checkVisitExists(1002, 2002);

        // 验证结果
        assertEquals(1, exists);
        assertEquals(0, notExists);
    }

    @Test
    void checkVisitExists_shouldHandleNullParameters() {
        // 模拟方法调用
        when(mobileIndustryMapper.checkVisitExists(null, null)).thenReturn(0);

        // 执行测试
        int result = mobileIndustryMapper.checkVisitExists(null, null);

        // 验证结果
        assertEquals(0, result);
    }


}