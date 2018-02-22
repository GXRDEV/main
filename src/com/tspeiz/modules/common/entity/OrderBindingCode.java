package com.tspeiz.modules.common.entity;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by kx on 2017/5/5.
 */
@Entity
@Table(name="order_binding_code")
public class OrderBindingCode implements Serializable{
    private static final long serialVersionUID = -9095145945333132722L;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "Id", unique = true, nullable = false)
    private Integer id;

    @Column(name="OrderUuid")
    private String orderUuid;

    @Column(name="OrderType")
    private Integer orderType;

    @Column(name="BindingCode")
    private String bindingCode;

    @Column(name="CreateTime")
    private Timestamp createTime;

    @Column(name="BindingTime")
    private Timestamp bindingTime;

    @Column(name="BindingUser")
    private Integer bindingUser;

    @Column(name="Telephone")
    private String telephone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderUuid() {
        return orderUuid;
    }

    public void setOrderUuid(String orderUuid) {
        this.orderUuid = orderUuid;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getBindingCode() {
        return bindingCode;
    }

    public void setBindingCode(String bindingCode) {
        this.bindingCode = bindingCode;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getBindingTime() {
        return bindingTime;
    }

    public void setBindingTime(Timestamp bindingTime) {
        this.bindingTime = bindingTime;
    }

    public Integer getBindingUser() {
        return bindingUser;
    }

    public void setBindingUser(Integer bindingUser) {
        this.bindingUser = bindingUser;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
