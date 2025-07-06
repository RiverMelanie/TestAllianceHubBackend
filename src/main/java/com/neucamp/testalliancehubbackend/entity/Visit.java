package com.neucamp.testalliancehubbackend.entity;

import java.time.LocalDateTime;

public class Visit {
    private Integer visit_id;
    private Integer user_id;
    private Integer dynamic_id;
    private LocalDateTime visit_time;

    // 构造方法、getter和setter
    public Visit() {}

    public Visit(Integer user_id, Integer dynamic_id) {
        this.user_id = user_id;
        this.dynamic_id = dynamic_id;
        this.visit_time = LocalDateTime.now(); // 自动设置当前时间
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(Integer dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public LocalDateTime getVisit_time() {
        return visit_time;
    }

    public void setVisit_time(LocalDateTime visit_time) {
        this.visit_time = visit_time;
    }
}