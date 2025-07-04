package com.neucamp.testalliancehubbackend.entity;

import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class IndustryDynamicTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    // 测试实体类的基本属性设置与获取
    @Test
    void testEntityAttributes() {
        // Arrange
        IndustryDynamic dynamic = new IndustryDynamic();
        Date now = new Date();

        // Act
        dynamic.setDynamicId(1);
        dynamic.setPublisherId(1001);
        dynamic.setTitle("测试标题");
        dynamic.setContent("测试内容");
        dynamic.setSummary("摘要");
        dynamic.setAuthor("作者");
        dynamic.setImageUrl("http://example.com/image.jpg");
        dynamic.setCreateTime(now);
        dynamic.setAuditStatus(1);

        // Assert
        assertThat(dynamic.getDynamicId()).isEqualTo(1);
        assertThat(dynamic.getPublisherId()).isEqualTo(1001);
        assertThat(dynamic.getTitle()).isEqualTo("测试标题");
        assertThat(dynamic.getContent()).isEqualTo("测试内容");
        assertThat(dynamic.getSummary()).isEqualTo("摘要");
        assertThat(dynamic.getAuthor()).isEqualTo("作者");
        assertThat(dynamic.getImageUrl()).isEqualTo("http://example.com/image.jpg");
        assertThat(dynamic.getCreateTime()).isEqualTo(now);
        assertThat(dynamic.getAuditStatus()).isEqualTo(1);
    }

    // 测试 @NotBlank 注解对 title 字段的约束
    @Test
    void testTitleNotNull() {
        // Arrange
        IndustryDynamic dynamic = new IndustryDynamic();
        dynamic.setTitle(null); // 违反 @NotBlank 约束

        // Act
        Set<ConstraintViolation<IndustryDynamic>> violations = validator.validate(dynamic);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .containsExactly("标题不能为空");
    }

    // 测试 title 字段为空字符串的情况
    @Test
    void testTitleNotEmpty() {
        // Arrange
        IndustryDynamic dynamic = new IndustryDynamic();
        dynamic.setTitle(""); // 违反 @NotBlank 约束

        // Act
        Set<ConstraintViolation<IndustryDynamic>> violations = validator.validate(dynamic);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .containsExactly("标题不能为空");
    }

    // 测试 title 字段为空白字符串的情况
    @Test
    void testTitleNotBlank() {
        // Arrange
        IndustryDynamic dynamic = new IndustryDynamic();
        dynamic.setTitle("   "); // 违反 @NotBlank 约束

        // Act
        Set<ConstraintViolation<IndustryDynamic>> violations = validator.validate(dynamic);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .containsExactly("标题不能为空");
    }

    // 测试有效 title 的情况
    @Test
    void testValidTitle() {
        // Arrange
        IndustryDynamic dynamic = new IndustryDynamic();
        dynamic.setTitle("有效标题"); // 符合约束

        // Act
        Set<ConstraintViolation<IndustryDynamic>> violations = validator.validate(dynamic);

        // Assert
        assertThat(violations).isEmpty();
    }

    // 测试实体类的 equals 和 hashCode 方法
    @Test
    void testEqualsAndHashCode() {
        // Arrange
        IndustryDynamic dynamic1 = new IndustryDynamic();
        dynamic1.setDynamicId(1);
        dynamic1.setTitle("标题");

        IndustryDynamic dynamic2 = new IndustryDynamic();
        dynamic2.setDynamicId(1);
        dynamic2.setTitle("标题");

        IndustryDynamic dynamic3 = new IndustryDynamic();
        dynamic3.setDynamicId(2);
        dynamic3.setTitle("不同标题");

        // Assert
        assertThat(dynamic1).isEqualTo(dynamic2);
        assertThat(dynamic1).isNotEqualTo(dynamic3);
        assertThat(dynamic1.hashCode()).isEqualTo(dynamic2.hashCode());
        assertThat(dynamic1.hashCode()).isNotEqualTo(dynamic3.hashCode());
    }

    // 测试实体类的 toString 方法
    @Test
    void testToString() {
        // Arrange
        IndustryDynamic dynamic = new IndustryDynamic();
        dynamic.setDynamicId(1);
        dynamic.setTitle("测试标题");

        // Act
        String toString = dynamic.toString();

        // Assert
        assertThat(toString).contains("dynamicId=1", "title=测试标题");
    }
}