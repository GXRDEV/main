package com.tspeiz.modules.common.bean.dto;

import java.io.Serializable;
/**
 * 群组成员 输出类
 * @author heyongb
 *
 */
public class GroupMemberInfoDto implements Serializable{
	private static final long serialVersionUID = 9075927241270885753L;
	
	private Integer memberId;//成员id
	
	private String memberName;//成员名称
	
	private String memberDuty;//成员duty
	
	private String memberProfession;//成员profession
	
	private Integer memberType;//成员类型
	
	private String hosName;//医院
	
	private String depName;//科室
	
	private Integer role;//角色
	
	private  String createTime;//创建时间
	
	private Integer status;//状态  0被踢出群，1在群
	
	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberDuty() {
		return memberDuty;
	}

	public void setMemberDuty(String memberDuty) {
		this.memberDuty = memberDuty;
	}

	public String getMemberProfession() {
		return memberProfession;
	}

	public void setMemberProfession(String memberProfession) {
		this.memberProfession = memberProfession;
	}

	public Integer getMemberType() {
		return memberType;
	}

	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
