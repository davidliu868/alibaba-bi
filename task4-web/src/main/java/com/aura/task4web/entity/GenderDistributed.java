package com.aura.task4web.entity;

public class GenderDistributed {
    private Integer id;
    private String cateId;
    private Integer gender;
    private Integer genderNums;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getGenderNums() {
        return genderNums;
    }

    public void setGenderNums(Integer genderNums) {
        this.genderNums = genderNums;
    }
}
