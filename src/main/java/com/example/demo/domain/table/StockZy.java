package com.example.demo.domain.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 */
@Entity(name="stock_zy")
public class StockZy implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,columnDefinition="varchar(50) COMMENT '姓名'")
    private String name;
    @Column(nullable = false,columnDefinition="varchar(100) COMMENT '专业'")
    private String zy;
    @Column(nullable = true,columnDefinition="varchar(100) COMMENT '专业'")
    private String infoCity;
    @Column(nullable = false,columnDefinition="varchar(100) COMMENT '电话'")
    private String phone;
    @Column(nullable = true,columnDefinition="varchar(10) COMMENT '级别'")
    private String infoLevel;
    @Column(nullable = true,columnDefinition="varchar(1000) COMMENT '描述'")
    private String infoDesc;
    @Column(nullable = true,columnDefinition="varchar(20) COMMENT '意向客户'")
    private String customerYx;
    @Column(nullable = true,columnDefinition="varchar(20) COMMENT '微信客户'")
    private String customerWx;

    public String getCustomerYx() {
        return customerYx;
    }

    public void setCustomerYx(String customerYx) {
        this.customerYx = customerYx;
    }

    public String getCustomerWx() {
        return customerWx;
    }

    public void setCustomerWx(String customerWx) {
        this.customerWx = customerWx;
    }

    public StockZy() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInfoLevel() {
        return infoLevel;
    }

    public void setInfoLevel(String infoLevel) {
        this.infoLevel = infoLevel;
    }

    public String getInfoDesc() {
        return infoDesc;
    }

    public void setInfoDesc(String infoDesc) {
        this.infoDesc = infoDesc;
    }

    public String getInfoCity() {
        return infoCity;
    }

    public void setInfoCity(String infoCity) {
        this.infoCity = infoCity;
    }

    @Override
    public String toString() {
        return "StockZy{" +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", zy='" + zy + '\'' +
                ", infoCity='" + infoCity + '\'' +
                ", infoLevel='" + infoLevel + '\'' +
                ", infoDesc='" + infoDesc + '\'' +
                '}';
    }
}
