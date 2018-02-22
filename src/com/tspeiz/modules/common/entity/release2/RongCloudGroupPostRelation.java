package com.tspeiz.modules.common.entity.release2;

import javax.persistence.Entity;
import javax.persistence.Table;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="rong_cloud_group_post_relation")
public class RongCloudGroupPostRelation implements Serializable{
	private static final long serialVersionUID = 1924204290655001271L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;

	@Column(name="CreateTime")
	private Timestamp createTime;

	@Column(name="GroupUuid")
	private String groupUuid;

	@Column(name="PostUuid")
	private String postUuid;

	@Column(name="RepasteUserId")
	private Integer repasteUserId;

	@Column(name="Status")
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getGroupUuid() {
		return groupUuid;
	}

	public void setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
	}

	public String getPostUuid() {
		return postUuid;
	}

	public void setPostUuid(String postUuid) {
		this.postUuid = postUuid;
	}

	public Integer getRepasteUserId() {
		return repasteUserId;
	}

	public void setRepasteUserId(Integer repasteUserId) {
		this.repasteUserId = repasteUserId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
