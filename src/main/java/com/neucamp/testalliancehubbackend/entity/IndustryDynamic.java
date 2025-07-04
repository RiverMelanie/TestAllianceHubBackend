package com.neucamp.testalliancehubbackend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 行业动态实体类
 * 对应数据库表：industry_dynamic
 */
@Data
@TableName("industry_dynamic")
public class IndustryDynamic{
    /**
     * 动态ID，自增
     */
    @TableId("dynamicId")
    private Integer dynamicId;

    public IndustryDynamic() {
    }

    /**
     * 发布者ID
     */
    @TableId("publisherId")
    private Integer publisherId;

    /**
     * 标题
     */
    @TableId("title")
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 内容
     */
    @TableId("content")
    private String content;

    public IndustryDynamic(Integer dynamicId, Integer publisherId, String title, String content, String summary, String author, String imageUrl, Date createTime, Integer auditStatus) {
        this.dynamicId = dynamicId;
        this.publisherId = publisherId;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.author = author;
        this.imageUrl = imageUrl;
        this.createTime = createTime;
        this.auditStatus = auditStatus;
    }

    /**
     * 摘要
     */
    @TableId("summary")
    private String summary;

    /**
     * 作者
     */
    @TableId("author")
    private String author;


    @TableId("imageUrl")
    private String imageUrl;

    /**
     * 创建时间，默认当前时间
     */
    @TableId("createTime")
    private Date createTime;

    /**
     * 审核状态，
     */
    @Column(name = "auditStatus")
    private Integer auditStatus;
    public Integer getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(Integer dynamicId) {
        this.dynamicId = dynamicId;
    }

    public Integer getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

}
