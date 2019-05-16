package com.aura.task4web.entity;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 留存分析
 */
public class FunnelAnalysis implements Serializable {

    @Id
    private Integer id;
    private Integer cateId;
    private Integer pv;
    private Integer cart;
    private Integer favour;
    private Integer buy;
    private String date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getCart() {
        return cart;
    }

    public void setCart(Integer cart) {
        this.cart = cart;
    }

    public Integer getFavour() {
        return favour;
    }

    public void setFavour(Integer favour) {
        this.favour = favour;
    }

    public Integer getBuy() {
        return buy;
    }

    public void setBuy(Integer buy) {
        this.buy = buy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
