package com.tspeiz.modules.common.bean.dto;

import java.io.Serializable;

/**
 * 群组信息 输出类
 * @author heyongb
 *
 */
public class GroupInfoDto implements Serializable{

	private static final long serialVersionUID = 9000146575257205720L;
	
	private Integer groupId;//群组id
	
	private String groupUuid;//群组uuid
	
	private String groupName;//群组名称
	
	private String distName;//区域
	
	private String depName;//科室
	
	private String creatorName;//创建者名称
	
	private Integer creatorType;//创建者类型
	
	private String createTime;//创建时间
	
	private String hosName;//医院
	
	private Integer status;
	
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getGroupUuid() {
		return groupUuid;
	}

	public void setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDistName() {
		return distName;
	}

	public void setDistName(String distName) {
		this.distName = distName;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Integer getCreatorType() {
		return creatorType;
	}

	public void setCreatorType(Integer creatorType) {
		this.creatorType = creatorType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
