package com.tspeiz.modules.common.entity.release2;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by kx on 2017/11/16.
 */
@Entity
@Table(name="system_sms_record")
public class SystemSmsRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", unique = true, nullable = false)
    private Integer id;

    @Column(name="Telphone")
    private String telphone;

    @Column(name="Code")
    private String code;

    @Column(name="Source")
    private Integer source;

    @Column(name="TaskId")
    private String taskId;

    @Column(name="SendTime")
    private Timestamp sendTime;

    @Column(name="UpdateTime")
    private Timestamp updateTime;


    @Column(name="Status")
    private Integer status;  //1--发送成功  -1 发送失败

    @Column(name="Remark")
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
