package com.tspeiz.modules.home.rtc;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang3.StringUtils;

import com.tspeiz.modules.home.rtc.room.WebRTCRoomManager;

@ServerEndpoint("/signalmaster/{roomKey}/{uid}")
public class SignalMaster {

    private static final long MAX_TIME_OUT = 60 * 60 * 1000; //1小时自动切断
	// 用户和websocket的session映射
	private static final Map<String, Session> sessions = Collections
			.synchronizedMap(new HashMap<String, Session>());

	/**
	 * 打开websocket
	 * 
	 * @param session
	 *            websocket的session
	 * @param uid
	 *            打开用户的UID
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("roomKey") String roomKey,
			@PathParam("uid") String uid) {
		System.out.print("\r\nWebSocket:" + "[room:" + roomKey + ";user:" + uid
				+ "] open");
		session.setMaxIdleTimeout(MAX_TIME_OUT);
		sessions.put(uid, session);

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
	public void onClose(Session session, @PathParam("roomKey") String roomKey,
			@PathParam("uid") String uid) {
		// remove(session,roomKey,uid);
		System.out.print("\r\nWebSocket" + "[room:" + roomKey + ";user:" + uid
				+ "] begin close");
		WebRTCRoomManager.removeUser(roomKey, uid);
		sessions.remove(uid);
		System.out.print("\r\nWebSocket" + "[room:" + roomKey + ";user:" + uid
				+ "] end close");

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
			@PathParam("roomKey") String roomKey, @PathParam("uid") String uid) {
		/*System.out.print("\r\n==============onMessage:[room:" + roomKey
				+ ";From:" + uid + "]" + message);*/
		try {

			String otherUser = null;
			if (StringUtils.isNotBlank(uid)) {
				otherUser = WebRTCRoomManager.getOtherUser(roomKey, uid);// 获取通话的对象
			}

			if (otherUser != null) {
				if (SignalMaster.sessions.get(otherUser) != null) {
					/*System.out.print("\r\n==============onMessage:[room:"
							+ roomKey + ";From:" + uid + " ;To:" + otherUser
							+ "]");*/
					Session osession = sessions.get(otherUser); // 被呼叫的session
					if (osession.isOpen())
						// osession.getBasicRemote().sendText(new
						// String(message));
						osession.getBasicRemote().sendText(message);
					else {
						osession.close();
						System.out.print("\r\n==============onMessage:"
								+ "target User not open");
						this.nowaiting(session);
					}
				} else {
					WebRTCRoomManager.removeUser(roomKey, otherUser);
					System.out.print("\r\n==============onMessage:"
							+ "target User not open");
					this.nowaiting(session);
				}

			} else {
				System.out.print("\r\n==============onMessage:"
						+ "target User not exist ");
				this.nowaiting(session);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 没有人在等待
	 * 
	 * @param session
	 *            发送消息的session
	 */
	private void nowaiting(Session session) {
		session.getAsyncRemote().sendText("{\"type\" : \"nowaiting\"}");
	}

	
	public static  void removeUid(String uid){
		
        Session session = sessions.get(uid);
		try {
			if(session != null && session.isOpen()) session.close(); // 关闭session
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}