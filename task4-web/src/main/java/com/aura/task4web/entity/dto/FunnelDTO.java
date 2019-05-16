package com.aura.task4web.entity.dto;

import java.io.Serializable;

public class FunnelDTO implements Serializable {

    private String date;//日期
    private Integer id;//类目ID

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
