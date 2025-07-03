package com.neucamp.testalliancehubbackend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class dynamicreviewrecordtable {

    @JsonProperty("ReviewerID")
    @NotNull(message = "审核人 ID 不能为空")
    private Integer ReviewerID;
    @JsonProperty("Title")
    private String Title;
    @JsonProperty("NewsImage")
    private String NewsImage;
    @JsonProperty("Content")
    private String Content;
    @JsonProperty("NewsSummary")
    private String NewsSummary;
    @JsonProperty("Author")
    private String Author;
    @JsonProperty("ReviewResult")
    private String ReviewResult;

    public Integer getReviewerID() {
        return ReviewerID;
    }

    public void setReviewerID(Integer reviewerID) {
        ReviewerID = reviewerID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getNewsImage() {
        return NewsImage;
    }

    public void setNewsImage(String newsImage) {
        NewsImage = newsImage;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getNewsSummary() {
        return NewsSummary;
    }

    public void setNewsSummary(String newsSummary) {
        NewsSummary = newsSummary;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getReviewResult() {
        return ReviewResult;
    }

    public void setReviewResult(String reviewResult) {
        ReviewResult = reviewResult;
    }


}
