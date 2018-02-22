package com.tspeiz.modules.common.entity.release2;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;
/**
 * 私人医生表
 * @author wr
 *
 */
@Entity
@Table(name="business_d2p_private_order")
public class BusinessD2pPrivateOrder implements Serializable{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "Id", unique = true, nullable = false)
    private Integer id;

    @Column(name="UUID")
    private String uuid;

    @Column(name="CreateTime")
    private Timestamp createTime;

    @Column(name="DocServicePackageId")
    private Integer docServicePackageId;

    @Column(name="DoctorId")
    private Integer doctorId;

    @Column(name="UserId")
    private Integer userId;

    @Column(name="SubUserUuid")
    private String subUserUuid;

    @Column(name="Source")
    private Integer source;

    @Column(name="ReceiveTime")
    private Timestamp receiveTime;

    @Column(name="OpenId")
    private String openId;

    @Column(name="PayStatus")
    private Integer payStatus;

    @Column(name="Status")
    private Integer status;

    @Column(name="DelFlag")
    private Integer delFlag;

    @Column(name="Remark")
    private String remark;

    @Column(name= "StartDate")
    private Date startDate;

    @Column(name="EndDate")
    private Date endDate;
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getDocServicePackageId() {
        return docServicePackageId;
    }

    public void setDocServicePackageId(Integer docServicePackageId) {
        this.docServicePackageId = docServicePackageId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSubUserUuid() {
        return subUserUuid;
    }

    public void setSubUserUuid(String subUserUuid) {
        this.subUserUuid = subUserUuid;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Timestamp getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Timestamp receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
