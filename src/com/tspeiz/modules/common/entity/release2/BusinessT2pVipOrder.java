package com.tspeiz.modules.common.entity.release2;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;
/**
 * 团队vip服务表
 * @author wr
 *
 */
@Entity
@Table(name="business_t2p_vip_order")
public class BusinessT2pVipOrder implements Serializable{
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

    @Column(name="TeamUuid")
    private String teamUuid;

    @Column(name="UserId")
    private Integer userId;

    @Column(name="Source")
    private Integer source;

    @Column(name="OpenId")
    private String openId;

    @Column(name="DelFlag")
    private Integer delFlag;

    @Column(name="PayStatus")
    private Integer payStatus;

    @Column(name="Status")
    private Integer status;

    @Column(name="Remark")
    private String remark;

    @Column(name="StartDate")
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

    public String getTeamUuid() {
        return teamUuid;
    }

    public void setTeamUuid(String teamUuid) {
        this.teamUuid = teamUuid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
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