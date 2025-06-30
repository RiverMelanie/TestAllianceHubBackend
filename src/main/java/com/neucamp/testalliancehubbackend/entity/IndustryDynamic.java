package com.neucamp.testalliancehubbackend.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 行业动态实体类
 * 对应数据库表：industry_dynamic
 */
@Data
public class IndustryDynamic{
    /**
     * 动态ID，自增
     */
    private Integer dynamicId;
    /**
     * 发布者ID
     */
    private Integer publisherId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 作者
     */
    private String author;

    /**
     * 图片链接
     */
    private String imageUrl;

    /**
     * 创建时间，默认当前时间
     */
    private Date createTime;

    /**
     * 审核状态，默认 0
     */
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
