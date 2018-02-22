package com.tspeiz.modules.home.rtc.room;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.tspeiz.modules.home.rtc.SignalMaster;

import net.sf.json.JSONArray;

public class WebRTCRoomManager {

	// 最大房间数量
	private static final int MAX_COUNT = 200;
	private static final Map<String, WebRTCSimpleRoom> provider = new HashMap<String, WebRTCSimpleRoom>();

	public static void addUser(String roomKey, String user, Integer type) {
		WebRTCSimpleRoom room = provider.get(roomKey);

		System.out.println("\r\n===============addUser: room: " + roomKey
				+ ";user:" + user);
		if (room == null) {
			room = new WebRTCSimpleRoom(roomKey, user, type);
			provider.put(roomKey, room);
			System.out.println("\r\n===============cuRoomNum: "
					+ provider.size());
		} else {
			room.addUser(user);
		}
	}

	public static void removeUser(String roomKey, String user) {
		WebRTCSimpleRoom room = provider.get(roomKey);
		if (room != null) {
			System.out.println("\r\n===============begin removeUser curuser:"
					+ user + ";romUser1:" + room.getUser1() + ";romUser2:"
					+ room.getUser2());
			room.removeUser(user);
			System.out.println("\r\n===============after removeUser users:"
					+ JSONArray.fromObject(room).toString());
		}

		if (room != null && !room.haveUser()) {
			provider.remove(roomKey);
			System.out.println("\r\n==============remove room:" + roomKey);
			System.out
					.println("\r\n==============cuRoomNum:" + provider.size());
		}
	}

	public static boolean haveUser(String key) {

		WebRTCSimpleRoom room = provider.get(key);
		if (room != null) {
			return room.haveUser();
		} else {
			return false;
		}
	}

	public static String getOtherUser(String roomKey, String user) {
		WebRTCSimpleRoom room = provider.get(roomKey);
		if (room != null) {
			return room.getOtherUser(user);
		}
		return null;
	}

	public static Integer getUserNum(String key) {
		Integer userNum = 0;
		WebRTCSimpleRoom room = provider.get(key);
		if (room != null) {
			userNum = room.getUserNum();
		}
		return userNum;
	}

	public static boolean isTypeUserExist(String roomKey, Integer type) {

		WebRTCSimpleRoom room = provider.get(roomKey);
		if (room != null) {
			return room.isTypeUserExist(type);
		}
		return false;
	}

	public static boolean isUserExist(String roomKey, String user) {

		WebRTCSimpleRoom room = provider.get(roomKey);
		if (room != null) {
			return room.isUserExist(user);
		}
		return false;
	}

	public static WebRTCSimpleRoom getRoom(String roomKey) {
		
		return  provider.get(roomKey);
	}

	public static void removeRoom(String roomKey) {

		WebRTCSimpleRoom room = provider.get(roomKey);
		if (room != null) {

			String user1 = room.getUser1();
			String user2 = room.getUser2();

			if (StringUtils.isNotBlank(user1)) {
				SignalMaster.removeUid(user1);
			}
			if (StringUtils.isNotBlank(user2)) {
				SignalMaster.removeUid(user2);
			}
		}

		provider.remove(roomKey);

	}

	/**
	 * 是否可以继续创建通话房间
	 * 
	 * @return 可以：true；不可以false；
	 */
	public static boolean canCreateRoom() {
		return provider.size() <= MAX_COUNT;
	}

	/**
	 * 测试使用
	 */
	public static void clear() {

		provider.clear();
	}

}
