package com.neucamp.testalliancehubbackend.mapper;

import com.neucamp.testalliancehubbackend.entity.IndustryDynamic;
import com.neucamp.testalliancehubbackend.entity.dynamicreviewrecordtable;
import com.neucamp.testalliancehubbackend.mapper.ManagerIndustryDynamicMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@MybatisTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class ManagerIndustryDynamicMapperTest {

    @Mock
    private ManagerIndustryDynamicMapper mapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    // ========== searchDynamics 测试 ==========
    @Test
    void testSearchDynamics_NormalCase() {
        // 正常情况：分页查询
        Map<String, Object> params = Map.of("pageSize", 10, "offset", 0);
        dynamicreviewrecordtable record = new dynamicreviewrecordtable();
        record.setTitle("测试标题");
        when(mapper.searchDynamics(params)).thenReturn(List.of(record));

        List<dynamicreviewrecordtable> result = mapper.searchDynamics(params);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试标题", result.get(0).getTitle());
    }

    @Test
    void testSearchDynamics_NullParams() {
        // 配置 Mock 行为：当传入 null 时抛出 NullPointerException
        doThrow(new NullPointerException())
                .when(mapper)
                .searchDynamics(null); // 明确指定参数为 null

        // 执行测试并验证异常
        assertThrows(NullPointerException.class,
                () -> mapper.searchDynamics(null),
                "参数为 null 时应抛出异常"
        );
    }

    @Test
    void testSearchDynamics_NegativePageSize() {
        // 边界情况：负数 pageSize
        Map<String, Object> params = Map.of("pageSize", -5, "offset", 0);
        when(mapper.searchDynamics(params)).thenReturn(List.of());

        List<dynamicreviewrecordtable> result = mapper.searchDynamics(params);

        assertTrue(result.isEmpty(), "负数 pageSize 应返回空列表");
    }

    @Test
    void testSearchDynamics_ZeroPageSize() {
        // 边界情况：pageSize = 0
        Map<String, Object> params = Map.of("pageSize", 0, "offset", 0);
        when(mapper.searchDynamics(params)).thenReturn(List.of());

        List<dynamicreviewrecordtable> result = mapper.searchDynamics(params);

        assertTrue(result.isEmpty(), "pageSize=0 应返回空列表");
    }


    // ========== searchTotalCount 测试 ==========
    @Test
    void testSearchTotalCount_NormalCase() {
        // 正常情况：查询总数
        when(mapper.searchTotalCount(any())).thenReturn(5);

        int count = mapper.searchTotalCount(new HashMap<>());

        assertEquals(5, count);
    }

    @Test
    void testSearchTotalCount_NullParams() {
        // 配置 mock 行为：当传入 null 时抛出 NullPointerException
        doThrow(new NullPointerException())
                .when(mapper)
                .searchTotalCount(null);

        // 执行测试并验证异常
        assertThrows(NullPointerException.class,
                () -> mapper.searchTotalCount(null),
                "参数为 null 时应抛出异常"
        );
    }

    @Test
    void testSearchTotalCount_EmptyTable() {
        // 边界情况：空表查询
        when(mapper.searchTotalCount(any())).thenReturn(0);

        int count = mapper.searchTotalCount(new HashMap<>());

        assertEquals(0, count, "空表时总数应为 0");
    }


    // ========== upDynamic 测试 ==========
    @Test
    void testUpDynamic_NormalCase() {
        // 正常情况：更新存在的记录
        IndustryDynamic dynamic = new IndustryDynamic();
        dynamic.setDynamicId(1);
        when(mapper.upDynamic(dynamic)).thenReturn(1);

        int result = mapper.upDynamic(dynamic);

        assertEquals(1, result, "更新成功应返回 1");
    }

    @Test
    void testUpDynamic_NullParam() {
        // 配置 Mock 对象：当传入 null 时抛出 NullPointerException
        doThrow(new NullPointerException())
                .when(mapper)
                .upDynamic(null); // 明确指定参数为 null

        // 执行测试并验证异常
        assertThrows(NullPointerException.class,
                () -> mapper.upDynamic(null),
                "参数为 null 时应抛出异常"
        );
    }

    @Test
    void testUpDynamic_NonExistentId() {
        // 边界情况：更新不存在的 ID
        IndustryDynamic dynamic = new IndustryDynamic();
        dynamic.setDynamicId(9999);
        when(mapper.upDynamic(dynamic)).thenReturn(0);

        int result = mapper.upDynamic(dynamic);

        assertEquals(0, result, "更新不存在的 ID 应返回 0");
    }


    // ========== upReviewDynamic 测试 ==========
    @Test
    void testUpReviewDynamic_NormalCase() {
        // 正常情况：更新存在的记录
        dynamicreviewrecordtable record = new dynamicreviewrecordtable();
        record.setTitle("测试标题");
        when(mapper.upReviewDynamic(record)).thenReturn(1);

        int result = mapper.upReviewDynamic(record);

        assertEquals(1, result, "更新成功应返回 1");
    }

    @Test
    void testUpReviewDynamic_NullParam() {
        // 关键：配置 Mock 行为，传入 null 时抛 NPE
        doThrow(new NullPointerException())
                .when(mapper)
                .upReviewDynamic(null); // 明确指定参数为 null

        assertThrows(NullPointerException.class,
                () -> mapper.upReviewDynamic(null),
                "参数为 null 时应抛出异常"
        );
    }
    @Test
    void testUpReviewDynamic_NoMatchingCondition() {
        // 边界情况：无匹配条件
        dynamicreviewrecordtable record = new dynamicreviewrecordtable();
        record.setTitle("不存在的标题");
        when(mapper.upReviewDynamic(record)).thenReturn(0);

        int result = mapper.upReviewDynamic(record);

        assertEquals(0, result, "无匹配记录应返回 0");
    }


    // ========== upDynamicPlus 测试 ==========
    @Test
    void testUpDynamicPlus_NormalCase() {
        // 正常情况：更新存在的记录
        IndustryDynamic dynamic = new IndustryDynamic();
        dynamic.setPublisherId(1);
        doNothing().when(mapper).upDynamicPlus(dynamic);

        mapper.upDynamicPlus(dynamic);

        verify(mapper, times(1)).upDynamicPlus(dynamic);
    }

    @Test
    void testUpDynamicPlus_NullParam() {
        // 配置 Mock 对象行为：传入 null 时抛出 NullPointerException
        doThrow(new NullPointerException())
                .when(mapper)
                .upDynamicPlus(null); // 明确指定参数为 null

        // 执行测试并验证异常
        assertThrows(NullPointerException.class,
                () -> mapper.upDynamicPlus(null),
                "参数为 null 时应抛出异常"
        );
    }

    @Test
    void testUpDynamicPlus_NoMatchingCondition() {
        // 边界情况：无匹配条件
        IndustryDynamic dynamic = new IndustryDynamic();
        dynamic.setPublisherId(999);
        doNothing().when(mapper).upDynamicPlus(dynamic);

        mapper.upDynamicPlus(dynamic);

        verify(mapper, times(1)).upDynamicPlus(dynamic);
    }


    // ========== cleanDynamics 测试 ==========
    @Test
    void testCleanDynamics_NormalCase() {
        // 正常情况：删除存在的记录
        doNothing().when(mapper).cleanDynamics();

        mapper.cleanDynamics();

        verify(mapper, times(1)).cleanDynamics();
    }

    @Test
    void testCleanDynamics_EmptyTable() {
        // 边界情况：空表删除
        doNothing().when(mapper).cleanDynamics();

        mapper.cleanDynamics();

        verify(mapper, times(1)).cleanDynamics();
    }


    // ========== delDynamic 测试 ==========
    @Test
    void testDelDynamic_NormalCase() {
        // 正常情况：删除存在的记录
        when(mapper.delDynamic(1)).thenReturn(1);

        int result = mapper.delDynamic(1);

        assertEquals(1, result, "删除成功应返回 1");
    }

    @Test
    void testDelDynamic_NegativeId() {
        // 配置 Mock 对象：当传入负数 ID 时抛出 IllegalArgumentException
        doThrow(new IllegalArgumentException("ID 不能为负数"))
                .when(mapper)
                .delDynamic(anyInt()); // 匹配任意 int 参数

        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class,
                () -> mapper.delDynamic(-1),
                "负数 ID 应抛出异常"
        );
    }

    @Test
    void testDelDynamic_NonExistentId() {
        // 边界情况：删除不存在的 ID
        when(mapper.delDynamic(9999)).thenReturn(0);

        int result = mapper.delDynamic(9999);

        assertEquals(0, result, "删除不存在的 ID 应返回 0");
    }
}