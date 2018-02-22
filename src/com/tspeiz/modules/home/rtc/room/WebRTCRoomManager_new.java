package com.tspeiz.modules.home.rtc.room;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tspeiz.modules.home.rtc.UserClient;

public class WebRTCRoomManager_new {

	// 最大房间数量
	private static final int MAX_COUNT = 200;
	private static final Map<String, WebRTCSimpleRoom_new> provider = new HashMap<String, WebRTCSimpleRoom_new>();
	
	public static void addUser(UserClient userClient) {
		
		WebRTCSimpleRoom_new room = provider.get(userClient.getRoomId());
		if (room == null) {
			room = new WebRTCSimpleRoom_new();
			provider.put(userClient.getRoomId(), room);
		}  else {
			room.removeUser(userClient.getUserId(),userClient.getUserType());
		}
		room.addUser(userClient);
		
	}
	

	public static void removeUser(String roomKey, String userId,Integer userType) {
		WebRTCSimpleRoom_new room = provider.get(roomKey);
		if (room != null) {
			room.removeUser(userId,userType);
		}

		if (room != null && room.getCount() == 0) {
			provider.remove(roomKey);
		}
	}
	
	public static List<UserClient> getOtherUsers(String roomKey, String userId,Integer userType) {
		WebRTCSimpleRoom_new room = provider.get(roomKey);
		if (room != null) {
			return room.getOtherUsers(userId,userType);
		}
		return null;
	}

	public static UserClient getUserClient(String roomKey, String userId,Integer userType){
		
		WebRTCSimpleRoom_new room = provider.get(roomKey);
		if (room != null) {
			return room.getByUserId(userId,userType);
		}
		return null;
	}
	
	public static boolean isMaxRoomNum() {
		return provider.size() >= MAX_COUNT;
	}

	
}
