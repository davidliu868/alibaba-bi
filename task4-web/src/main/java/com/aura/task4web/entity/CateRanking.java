package com.aura.task4web.entity;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 类目排序
 */
public class CateRanking implements Serializable {

    @Id
    private Integer id;
    private String date;//日期  YYYY-MM-dd
    private Integer cateId; //类目ID
    private Integer nums; //浏览数量

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public Integer getNums() {
        return nums;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }
}
