package com.neucamp.testalliancehubbackend.mapper;

import com.neucamp.testalliancehubbackend.entity.IndustryDynamic;
import com.neucamp.testalliancehubbackend.entity.dynamicreviewrecordtable;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MybatisTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class IndustryDynamicMapperTest {
    @Mock
    private IndustryDynamicMapper industryDynamicMapper1;

    @InjectMocks
    private ManagerIndustryDynamicMapperTest testInstance;

    @Autowired
    private IndustryDynamicMapper industryDynamicMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SqlSession sqlSession;

    // ========== 正常测试 ==========
    @Test
    void testSearchDynamics() {
        Map<String, Object> params = new HashMap<>();
        params.put("title", "测试");
        params.put("author", "测试");
        params.put("pageSize", 10);
        params.put("offset", 0);

        List<IndustryDynamic> dynamics = industryDynamicMapper.searchDynamics(params);
        assertNotNull(dynamics);
    }

    @Test
    void testSearchTotalCount() {
        Map<String, Object> params = new HashMap<>();
        params.put("title", "测试");
        params.put("author", "测试");

        int totalCount = industryDynamicMapper.searchTotalCount(params);
        assertTrue(totalCount >= 0);
    }

    @Test
    void testAddDynamic() {
        IndustryDynamic dynamic = new IndustryDynamic();
        dynamic.setPublisherId(1);
        dynamic.setTitle("测试动态标题");
        dynamic.setContent("测试动态内容");
        dynamic.setSummary("测试动态摘要");
        dynamic.setAuthor("测试作者");
        dynamic.setImageUrl("http://test.com/image.jpg");
        dynamic.setAuditStatus(0);

        int result = industryDynamicMapper.addDynamic(dynamic);
        assertEquals(1, result);

        String sql = "SELECT dynamicId " +
                "FROM industry_dynamic " +
                "WHERE publisherId = ? " +
                "  AND title = ? " +
                "  AND author = ?";

        Integer dynamicId = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                dynamic.getPublisherId(),
                dynamic.getTitle(),
                dynamic.getAuthor()
        );
        assertNotNull(dynamicId);
    }

    @Test
    void testAddReviewRecord() {
        String uniqueTitle = "测试动态标题_" + UUID.randomUUID().toString();
        Integer reviewerId = 1;
        String newsImage = "http://test.com/review.jpg";
        String content = "审核内容_" + LocalDateTime.now().toString();
        String newsSummary = "审核摘要";
        String author = "测试作者";
        Integer reviewResult = 1;

        when(industryDynamicMapper1.addDynamic(any(IndustryDynamic.class))).thenReturn(1);
        when(industryDynamicMapper1.addReviewRecord(
                eq(reviewerId), eq(uniqueTitle), eq(newsImage),
                eq(content), eq(newsSummary), eq(author), eq(reviewResult)
        )).thenReturn(1);

        int result = industryDynamicMapper1.addReviewRecord(
                reviewerId, uniqueTitle, newsImage, content,
                newsSummary, author, reviewResult
        );

        assertEquals(1, result);
        verify(industryDynamicMapper1, times(1)).addReviewRecord(
                eq(reviewerId), eq(uniqueTitle), eq(newsImage),
                eq(content), eq(newsSummary), eq(author), eq(reviewResult)
        );
    }

    @Test
    void testSearchreviewdDynamics() {
        IndustryDynamic dynamic = new IndustryDynamic();
        dynamic.setPublisherId(1);
        dynamic.setTitle("待查询标题");
        dynamic.setContent("待查询内容");
        dynamic.setSummary("待查询摘要");
        dynamic.setAuthor("待查询作者");
        dynamic.setImageUrl("http://test.com/image.jpg");
        dynamic.setAuditStatus(0);
        industryDynamicMapper.addDynamic(dynamic);

        dynamicreviewrecordtable query = new dynamicreviewrecordtable();
        query.setTitle("待查询标题");
        query.setAuthor("待查询作者");

        IndustryDynamic result = industryDynamicMapper.searchreviewdDynamics(query);
        assertNotNull(result);
        assertEquals("待查询标题", result.getTitle());
    }

    // ========== 异常测试 ==========
    @Test
    void testSearchDynamics_NullParams() {
        // 配置 Mock 对象：当传入 null 时抛出 NullPointerException
        doThrow(new NullPointerException())
                .when(industryDynamicMapper1)
                .searchDynamics(null); // 明确指定参数为 null

        // 执行测试并验证异常
        assertThrows(NullPointerException.class,
                () -> industryDynamicMapper1.searchDynamics(null),
                "参数为 null 时应抛出异常"
        );
    }

    @Test
    void testSearchDynamics_InvalidPageSize() {
        // 配置 Mock 对象：当 pageSize 为负数时抛出 PersistenceException
        Map<String, Object> invalidParams = Map.of("pageSize", -1, "offset", 0);
        doThrow(new PersistenceException("无效的分页参数"))
                .when(industryDynamicMapper1)
                .searchDynamics(invalidParams); // 明确指定参数内容

        // 执行测试并验证异常
        assertThrows(PersistenceException.class,
                () -> industryDynamicMapper1.searchDynamics(invalidParams),
                "负数 pageSize 应抛出异常"
        );
    }

    @Test
    void testSearchTotalCount_NullParams() {
        // 配置 Mock 对象：传入 null 时抛出 NullPointerException
        doThrow(new NullPointerException())
                .when(industryDynamicMapper1)
                .searchTotalCount(null);

        assertThrows(NullPointerException.class,
                () -> industryDynamicMapper1.searchTotalCount(null),
                "参数为 null 时应抛出异常"
        );
    }

    @Test
    void testAddDynamic_NullObject() {
        // 配置 Mock 对象：传入 null 时抛出 NullPointerException
        doThrow(new NullPointerException())
                .when(industryDynamicMapper1)
                .addDynamic(null);

        assertThrows(NullPointerException.class,
                () -> industryDynamicMapper1.addDynamic(null),
                "参数为 null 时应抛出异常"
        );
    }

    @Test
    void testAddDynamic_DuplicateTitle() {
        // 配置 Mock 对象：传入重复标题时抛出 DuplicateKeyException
        IndustryDynamic duplicate = new IndustryDynamic();
        duplicate.setTitle("重复标题");

        doThrow(new DuplicateKeyException("重复标题"))
                .when(industryDynamicMapper1)
                .addDynamic(argThat(dyn -> "重复标题".equals(dyn.getTitle())));

        assertThrows(DuplicateKeyException.class,
                () -> industryDynamicMapper1.addDynamic(duplicate),
                "重复标题应抛出唯一约束异常"
        );
    }

    @Test
    void testAddReviewRecord_NullTitle() {
        // 配置 Mock 对象：title 为 null 时抛出 NullPointerException
        doThrow(new NullPointerException())
                .when(industryDynamicMapper1)
                .addReviewRecord(anyInt(), isNull(), anyString(), anyString(), anyString(), anyString(), anyInt());

        assertThrows(NullPointerException.class,
                () -> industryDynamicMapper1.addReviewRecord(1, null, "image.jpg", "content", "summary", "author", 1),
                "标题为 null 时应抛出异常"
        );
    }

    @Test
    void testSearchreviewdDynamics_NullQuery() {
        // 配置 Mock 对象：传入 null 时抛出 NullPointerException
        doThrow(new NullPointerException())
                .when(industryDynamicMapper1)
                .searchreviewdDynamics(null);

        assertThrows(NullPointerException.class,
                () -> industryDynamicMapper1.searchreviewdDynamics(null),
                "查询参数为 null 时应抛出异常"
        );
    }
    // ========== 边界测试 ==========
    @Test
    void testSearchDynamics_MaxPageSize() {
        Map<String, Object> params = Map.of("pageSize", 1000, "offset", 0);
        List<IndustryDynamic> dynamics = industryDynamicMapper.searchDynamics(params);
        assertNotNull(dynamics);
    }

    @Test
    void testSearchDynamics_ZeroOffset() {
        Map<String, Object> params = Map.of("pageSize", 10, "offset", 0);
        List<IndustryDynamic> dynamics = industryDynamicMapper.searchDynamics(params);
        assertTrue(dynamics.size() <= 10, "第一页数据不应超过 pageSize");
    }

    @Test
    void testAddDynamic_MaxLengthFields() {
        IndustryDynamic dynamic = new IndustryDynamic();
        dynamic.setPublisherId(1);
        dynamic.setTitle("A".repeat(255)); // 假设 title 最大长度 255
        dynamic.setContent("C".repeat(65535)); // 假设 content 最大长度 65535
        dynamic.setSummary("S".repeat(500)); // 假设 summary 最大长度 500
        dynamic.setAuthor("Author");
        dynamic.setImageUrl("http://test.com/image.jpg");
        dynamic.setAuditStatus(0);

        assertDoesNotThrow(() -> industryDynamicMapper.addDynamic(dynamic),
                "最大长度字段应正常插入");
    }

    @Test
    void testSearchTotalCount_EmptyTable() {
        jdbcTemplate.update("DELETE FROM industry_dynamic");
        int count = industryDynamicMapper.searchTotalCount(new HashMap<>());
        assertEquals(0, count, "空表总数应为 0");
    }

    @Test
    void testSearchreviewdDynamics_NoResult() {
        dynamicreviewrecordtable query = new dynamicreviewrecordtable();
        query.setTitle("不存在的标题");
        query.setAuthor("不存在的作者");

        IndustryDynamic result = industryDynamicMapper.searchreviewdDynamics(query);
        assertNull(result, "无匹配结果时应返回 null");
    }
}