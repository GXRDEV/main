package com.tspeiz.modules.home.rtc.room;

import org.apache.commons.lang.StringUtils;

public class WebRTCSimpleRoom {

	private String key; // orderId

	private String user1; // session1

	private String user2; // session2
	
	private Integer type1;
	
	private Integer type2;
	

	public WebRTCSimpleRoom() {

	}

	public WebRTCSimpleRoom(String key, String user1,Integer type1) {
		this.key = key;
		this.user1 = user1;
		this.type1 = type1;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	public String getUser1() {
		return user1;
	}

	public void setUser1(String user1) {
		this.user1 = user1;
	}

	public String getUser2() {
		return user2;
	}

	public void setUser2(String user2) {
		this.user2 = user2;
	}

	public void addUser(String user) {
		if (StringUtils.isEmpty(user1)) {
			this.setUser1(user);
		} else if (StringUtils.isEmpty(user2)) {
			this.setUser2(user);
		}
	}

	public void removeUser(String user) {

		if (StringUtils.isNotEmpty(user)) {

			if (user.equals(user1)) {
				this.setUser1(null);
				this.setType1(null);
			}
			if (user.equals(user2)) {
				this.setUser2(null);
				this.setType2(null);
			}
		}
	}

	public boolean isUserExist(String user) {

		if (StringUtils.isNotEmpty(user)) {

			if (user.equals(user1)) {
				return true;
			}
			if (user.equals(user2)) {
				return true;
			}
		}

		return false;
	}
	
	public boolean isTypeUserExist(Integer type) {

		if (type != null) {

			if (StringUtils.isNotBlank(user1) && type.equals(type1)) {
				return true;
			}
			if (StringUtils.isNotBlank(user2) && type.equals(type2)) {
				return true;
			}
		}

		return false;
	}

	public boolean haveUser() {
		return !(this.getUser1() == null && this.getUser2() == null);
	}

	public String getOtherUser(String user) {
		if (user.equals(user1)) {
			return user2;
		} else {
			return user1;
		}
	}

	public Integer getUserNum() {

		Integer userNum = 0;
		if (StringUtils.isNotEmpty(user1)) {
			userNum = userNum + 1;
		}

		if (StringUtils.isNotEmpty(user2)) {
			userNum = userNum + 1;
		}

		return userNum;
	}

	public Integer getType1() {
		return type1;
	}

	public void setType1(Integer type1) {
		this.type1 = type1;
	}

	public Integer getType2() {
		return type2;
	}

	public void setType2(Integer type2) {
		this.type2 = type2;
	}
}
