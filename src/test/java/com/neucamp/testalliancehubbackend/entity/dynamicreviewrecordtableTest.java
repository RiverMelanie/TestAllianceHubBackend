package com.neucamp.testalliancehubbackend.entity;

import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class dynamicreviewrecordtableTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    // 测试实体类的基本属性设置与获取
    @Test
    void testEntityAttributes() {
        // Arrange
        dynamicreviewrecordtable record = new dynamicreviewrecordtable();

        // Act
        record.setReviewerID(1);
        record.setTitle("测试标题");
        record.setNewsImage("http://example.com/image.jpg");
        record.setContent("测试内容");
        record.setNewsSummary("摘要");
        record.setAuthor("作者");
        record.setReviewResult(1);

        // Assert
        assertThat(record.getReviewerID()).isEqualTo(1);
        assertThat(record.getTitle()).isEqualTo("测试标题");
        assertThat(record.getNewsImage()).isEqualTo("http://example.com/image.jpg");
        assertThat(record.getContent()).isEqualTo("测试内容");
        assertThat(record.getNewsSummary()).isEqualTo("摘要");
        assertThat(record.getAuthor()).isEqualTo("作者");
        assertThat(record.getReviewResult()).isEqualTo(1);
    }

    // 测试 @NotNull 注解对 ReviewerID 字段的约束
    @Test
    void testReviewerIDNotNull() {
        // Arrange
        dynamicreviewrecordtable record = new dynamicreviewrecordtable();
        record.setReviewerID(null); // 违反 @NotNull 约束

        // Act
        Set<ConstraintViolation<dynamicreviewrecordtable>> violations = validator.validate(record);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .containsExactly("审核人 ID 不能为空");
    }

    // 测试有效 ReviewerID 的情况
    @Test
    void testValidReviewerID() {
        // Arrange
        dynamicreviewrecordtable record = new dynamicreviewrecordtable();
        record.setReviewerID(1); // 符合约束

        // Act
        Set<ConstraintViolation<dynamicreviewrecordtable>> violations = validator.validate(record);

        // Assert
        assertThat(violations).isEmpty();
    }

    // 测试 @JsonProperty 注解对 JSON 序列化的影响
    @Test
    void testJsonPropertyAnnotations() throws Exception {
        // Arrange
        dynamicreviewrecordtable record = new dynamicreviewrecordtable();
        record.setReviewerID(1);
        record.setTitle("测试标题");

        // 配置 ObjectMapper：禁用方法检测，强制使用字段名 + @JsonProperty
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE));

        // 执行序列化
        String json = mapper.writeValueAsString(record);

        // Assert
        assertThat(json).contains("\"ReviewerID\":1", "\"Title\":\"测试标题\"");
        assertThat(json).doesNotContain("reviewerID", "title");
    }
    // 测试实体类的 equals 和 hashCode 方法
    @Test
    void testEqualsAndHashCode() {
        // Arrange
        dynamicreviewrecordtable record1 = new dynamicreviewrecordtable();
        record1.setReviewerID(1);
        record1.setTitle("标题");

        dynamicreviewrecordtable record2 = new dynamicreviewrecordtable();
        record2.setReviewerID(1);
        record2.setTitle("标题");

        dynamicreviewrecordtable record3 = new dynamicreviewrecordtable();
        record3.setReviewerID(2);
        record3.setTitle("不同标题");

        // Assert
        assertThat(record1).isEqualTo(record2);
        assertThat(record1).isNotEqualTo(record3);
        assertThat(record1.hashCode()).isEqualTo(record2.hashCode());
        assertThat(record1.hashCode()).isNotEqualTo(record3.hashCode());
    }

    // 测试实体类的 toString 方法
    @Test
    void testToString() {
        // Arrange
        dynamicreviewrecordtable record = new dynamicreviewrecordtable();
        record.setReviewerID(1);
        record.setTitle("测试标题");

        // Act
        String toString = record.toString();

        // Assert
        assertThat(toString).contains("ReviewerID=1", "Title=测试标题");
    }
}