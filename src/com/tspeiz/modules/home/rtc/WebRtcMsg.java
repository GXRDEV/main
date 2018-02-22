package com.tspeiz.modules.home.rtc;

public class WebRtcMsg {
	
	private String type;
	private String roomId;
	private String userId;
	private Integer userType;
	private String  remark;
	
	public WebRtcMsg(){
		
	}
	public WebRtcMsg(String type,String roomId,String userId,Integer userType){
		this.type = type;
		this.roomId = roomId;
		this.userId = userId;
		this.userType = userType;
	}
	
	public WebRtcMsg(String type,String roomId,String userId,Integer userType,String remark){
		this.type = type;
		this.roomId = roomId;
		this.userId = userId;
		this.userType = userType;
		this.remark = remark;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	
}
