package com.tspeiz.modules.home.rtc;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import net.sf.json.JSONObject;

import com.tspeiz.modules.home.rtc.room.WebRTCRoomManager_new;

@ServerEndpoint("/signalmaster_new")
public class SignalMaster_new {

	private static final String ROOM_JOIN = "join";
	private static final String ROOM_MESSAGE = "message";
	private static final String ROOM_LEAVE = "leave";
	private static final String ROOM_OTHER_JOIN = "otherjoin";

	//sessionId-> session映射
	private static final Map<String, Session> sessions = Collections
			.synchronizedMap(new HashMap<String, Session>());
	
	//sessionId->userClient
	private static final Map<String, UserClient> clients = Collections
			.synchronizedMap(new HashMap<String, UserClient>());
	
	@OnOpen
	public void onOpen(Session session) {
		sessions.put(session.getId(), session);
		
		System.out.print("\r\n==========websocket[" + session.getId() + "]:onOpen" );
	}


	@OnClose
	public void onClose(Session session) {
		
		leaveRoom(session);
		sessions.remove(session.getId());
		System.out.print("\r\n==========websocket[" + session.getId() + "]:onClose" );
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		
		 JSONObject obj = JSONObject.fromObject(message);
		 WebRtcMsg msg = (WebRtcMsg)JSONObject.toBean(obj, WebRtcMsg.class);
		 
		 if(ROOM_JOIN.equalsIgnoreCase(msg.getType())){
			 joinRoom(msg.getRoomId(),msg.getUserId(),msg.getUserType(),session); 
			 
		 } else if(ROOM_LEAVE.equalsIgnoreCase(msg.getType())){
			 
			 leaveRoom(session); 
			 
		 } else {
			 sendMessage(message,session);
		 }
	}

	private void joinRoom(String roomId,String userId,Integer userType,Session session){
		
		//清除无效的 session
		clearInvalidSession(roomId,userId,userType,session);
		//移除此用户其他地方登陆的房间号
		
		//新client 加入
		UserClient userclient = new UserClient();
		userclient.setRoomId(roomId);
		userclient.setUserId(userId);
		userclient.setUserType(userType);
		userclient.setSessionId(session.getId()); 
		
		clients.put(session.getId(), userclient);
		WebRTCRoomManager_new.addUser(userclient);
		
		System.out.print("\r\n==========websocket[" + session.getId() + "]:joinRoom,client:" + JSONObject.fromObject(userclient).toString());
		
		try {
			List<UserClient> otherUsers = WebRTCRoomManager_new.getOtherUsers(roomId, userId,userType); 
			if(otherUsers != null){
				for(UserClient otherUser:otherUsers){
					Session toSession = sessions.get(otherUser.getSessionId());
					if(toSession != null && toSession.isOpen()){
						
						WebRtcMsg rtcMsg = new WebRtcMsg(ROOM_JOIN,roomId,userId,userType);
						toSession.getBasicRemote().sendText(JSONObject.fromObject(rtcMsg).toString());//通知对方自己进入房间
						System.out.print("\r\n==========websocket[" + session.getId() + "]:joinRoom,perrclient:" + JSONObject.fromObject(otherUser).toString());
					} 
				} 
			}
			//记录进入房间消息 ，在单独的线程中,或从前端调接口记录
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void  leaveRoom(Session session){
		try {
			UserClient client = clients.get(session.getId());
			if(client != null){
		    	//通知此房间的其他人
				List<UserClient> otherUsers = WebRTCRoomManager_new.getOtherUsers(client.getRoomId(), client.getUserId(),client.getUserType());
				for (UserClient user:otherUsers){
					Session otherSession = sessions.get(user.getSessionId());
					if(otherSession != null && otherSession.isOpen()){
						WebRtcMsg rtcMsg = new WebRtcMsg(ROOM_LEAVE,client.getRoomId(),client.getUserId(),client.getUserType());
						otherSession.getBasicRemote().sendText(JSONObject.fromObject(rtcMsg).toString());//通知对方自己离开
					} 
				} 
				
			    //退出房间
				clients.remove(session.getId());
				WebRTCRoomManager_new.removeUser(client.getRoomId(), client.getUserId(),client.getUserType());
				//记录离开消息 ，在单独的线程中，或从前端调接口记录
				
				System.out.print("\r\n==========websocket[" + session.getId() + "]:leaveRoom,client:" + JSONObject.fromObject(client).toString());
		    }
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void  clearInvalidSession(String roomId,String userId,Integer userType,Session curSession){
		try {
			UserClient preClient = WebRTCRoomManager_new.getUserClient(roomId,userId,userType);
			if(preClient != null){
				
				//进入的是同一个session ,不需要处理
				Session preSession = sessions.get(preClient.getSessionId());
				if(preSession!= null && preSession.getId().equalsIgnoreCase(curSession.getId()) ){
					return;
				}
			
				//重新就如一个session
				List<UserClient> otherUsers = WebRTCRoomManager_new.getOtherUsers(roomId, userId,userType);
				if(otherUsers !=null){
					for(UserClient otherUser:otherUsers){
						Session otherSession = sessions.get(otherUser.getSessionId());
						if(otherSession != null && otherSession.isOpen()){
							WebRtcMsg rtcMsg = new WebRtcMsg(ROOM_LEAVE,roomId,userId,userType);
							otherSession.getBasicRemote().sendText(JSONObject.fromObject(rtcMsg).toString());//通知对方自己离开
						} 
					} 
				}
					
				 //退出原来的房间
				clients.remove(preSession.getId());
				WebRTCRoomManager_new.removeUser(roomId, userId,userType);
				System.out.print("\r\n==========websocket[" + preSession.getId() + "]:force leaveRoom,client:" + JSONObject.fromObject(preClient).toString());
				
				//发送消息
				if(preSession != null && preSession.isOpen()){
					WebRtcMsg rtcMsg = new WebRtcMsg(ROOM_OTHER_JOIN,roomId,userId,userType,"您已经在其他地方登陆进入房间，请刷新重新进入。");
					preSession.getBasicRemote().sendText(JSONObject.fromObject(rtcMsg).toString());
				}
			}
			
			//记录离开消息 ，在单独的线程中 或从前端调接口记录
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//发送 通话消息
	private void sendMessage(String message,Session fromSession){
		try {
		
			UserClient client = clients.get(fromSession.getId());
		    if(client == null){
		    	fromSession.getBasicRemote().sendText(message);//发送未加入房间
		    } else {
				List<UserClient> otherUsers = WebRTCRoomManager_new.getOtherUsers(client.getRoomId(), client.getUserId(),client.getUserType());// 获取通话的对象
				if(otherUsers != null){
					for(UserClient otherUser:otherUsers){
						Session toSession = sessions.get(otherUser.getSessionId());
						if(toSession != null && toSession.isOpen()){
							toSession.getBasicRemote().sendText(message);//发消息给对方
						}
					} 
				}
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
}