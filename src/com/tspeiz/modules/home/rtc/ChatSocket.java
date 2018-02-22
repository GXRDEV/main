package com.tspeiz.modules.home.rtc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import net.sf.json.JSONObject;

@ServerEndpoint("/chatsocket/{roomKey}")
public class ChatSocket {

	// channel 和 websocket的session映射
	private static final Map<String, List<Session>> channels = Collections
			.synchronizedMap(new HashMap<String, List<Session>>());

	// 进入房间

	/**
	 * 打开websocket
	 * 
	 * @param session
	 *            websocket的session
	 * @param uid
	 *            打开用户的UID
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("roomKey") String roomKey) {
		System.out.print("\r\nWebSocket:" + "[room:" + roomKey + ",session:"
				+ session.getId() + "]" + "open");
		addSession(roomKey, session);
	}

	/**
	 * websocket关闭
	 * 
	 * @param session
	 *            关闭的session
	 * @param uid
	 *            关闭的用户标识
	 */
	@OnClose
	public void onClose(Session session, @PathParam("roomKey") String roomKey) {
		System.out.print("\r\nWebSocket" + "[room:" + roomKey + ",session:"
				+ session.getId() + "]" + "close");
		removeSession(roomKey, session);
	}

	/**
	 * 收到消息
	 * 
	 * @param message
	 *            消息内容
	 * @param session
	 *            发送消息的session
	 * @param uid
	 */
	@OnMessage
	public void onMessage(String message, Session session,
			@PathParam("roomKey") String roomKey) {

		System.out.print("\r\n=========onMessage" + "[room:" + roomKey
				+ ",session:" + session.getId() + "]" + message);
		
		broadcast(session,roomKey,message);
		

	}

	/**
	 * 表示有人等待
	 * 
	 * @param session
	 * @param roomKey
	 * @param uid
	 */
	public static void broadcast(Session session, String roomKey,
			String message) {

		List<Session> sessions = channels.get(roomKey);
		if (sessions != null) {
			for (Session ses : sessions) {
				// ses.getAsyncRemote().sendText(message);
				if (ses != session && ses.isOpen()) {
					try {
						ses.getBasicRemote().sendText(message);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void addSession(String channel, Session session) {
		System.out.print("\r\n=======addSession1" + "[room:" + channel
				+ ",session:" + session.getId() + "]" + "sessionNum:"
				+ channels.size());

		List<Session> sessions = channels.get(channel);
		if (sessions != null) {
			sessions.add(session);
		} else {
			sessions = new ArrayList<Session>();
			sessions.add(session);
			channels.put(channel, sessions);
		}

		System.out.print("\r\n=======addSession2" + "[room:" + channel
				+ ",session:" + session.getId() + "]" + "sessionNum:"
				+ channels.size());
	}

	private void removeSession(String channel, Session session) {

		System.out.print("\r\n=======removeSession1" + "[room:" + channel
				+ ",session:" + session.getId() + "]" + "sessionNum:"
				+ channels.size());

		List<Session> sessions = channels.get(channel);
		if (sessions != null) {
			sessions.remove(session);
		}
		if (sessions.size() == 0) {
			channels.remove(channel);
		}
		System.out.print("\r\n=======removeSession2" + "[room:" + channel
				+ ",session:" + session.getId() + "]" + "sessionNum:"
				+ channels.size());

	}
}
