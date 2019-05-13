package entry;

import java.io.Serializable;

public class AdsEntry implements Serializable{
    private int adgroup_id;//广告id
//    private int user;
//    private long time_stamp;
//    private int pid;
    private int nonclk;//0-点击
    private int clk;//1-点击

    private int customer_id;//广告主id

    public AdsEntry() {
        this.adgroup_id = adgroup_id;
        this.nonclk = nonclk;
        this.clk = clk;
        this.customer_id = customer_id;
    }

    public int getAdgroup_id() {
        return adgroup_id;
    }

    public void setAdgroup_id(int adgroup_id) {
        this.adgroup_id = adgroup_id;
    }

    public int getNonclk() {
        return nonclk;
    }

    public void setNonclk(int nonclk) {
        this.nonclk = nonclk;
    }

    public int getClk() {
        return clk;
    }

    public void setClk(int clk) {
        this.clk = clk;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }
}
