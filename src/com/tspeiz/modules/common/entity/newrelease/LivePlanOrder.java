package com.tspeiz.modules.common.entity.newrelease;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by kangxin on 2018/2/2.
 */
@Entity
@Table(name="live_plan_order")
public class LivePlanOrder implements Serializable {
    private static final long serialVersionUID = -1637742887773617961L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "UUID")
    private String uuid;

    @Column(name="Title")
    private String title;

    @Column(name = "UserId")
    private Integer userId;

    @Column(name = "CreateTime")
    private Timestamp createTime;

    @Column(name = "EndTime")
    private Timestamp endTime;
    
    @Column(name = "BeginTime")
    private Timestamp beginTime;

    @Column(name = "ChatRoomStartTime")
    private Timestamp chatRoomStartTime;

    @Column(name = "ChatRoomCloseTime")
    private Timestamp chatRoomCloseTime;

    @Column(name = "VedioUrl")
    private String VedioUrl;
    
    @Column(name = "Duration")
    private Double duration;

    @Column(name = "ConsultationDetails")
    private String consultationDetails;

    @Transient
    private String beginTimes;
    @Transient
    private String chatRoomStartTimes;
    @Transient
    private String chatRoomCloseTimes;
    @Transient
    private String docName;
    
    @Transient
    private String durationStr;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public Timestamp getChatRoomStartTime() {
        return chatRoomStartTime;
    }

    public void setChatRoomStartTime(Timestamp chatRoomStartTime) {
        this.chatRoomStartTime = chatRoomStartTime;
    }

    public Timestamp getChatRoomCloseTime() {
        return chatRoomCloseTime;
    }

    public void setChatRoomCloseTime(Timestamp chatRoomCloseTime) {
        this.chatRoomCloseTime = chatRoomCloseTime;
    }

    public String getVedioUrl() {
        return VedioUrl;
    }

    public void setVedioUrl(String vedioUrl) {
        VedioUrl = vedioUrl;
    }

    public String getBeginTimes() {
        return beginTimes;
    }

    public void setBeginTimes(String beginTimes) {
        this.beginTimes = beginTimes;
    }

    public String getChatRoomStartTimes() {
        return chatRoomStartTimes;
    }

    public void setChatRoomStartTimes(String chatRoomStartTimes) {
        this.chatRoomStartTimes = chatRoomStartTimes;
    }

    public String getChatRoomCloseTimes() {
        return chatRoomCloseTimes;
    }

    public void setChatRoomCloseTimes(String chatRoomCloseTimes) {
        this.chatRoomCloseTimes = chatRoomCloseTimes;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

    public String getConsultationDetails() {
        return consultationDetails;
    }

    public void setConsultationDetails(String consultationDetails) {
        this.consultationDetails = consultationDetails;
    }

	public String getDurationStr() {
		return durationStr;
	}

	public void setDurationStr(String durationStr) {
		this.durationStr = durationStr;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	
}
