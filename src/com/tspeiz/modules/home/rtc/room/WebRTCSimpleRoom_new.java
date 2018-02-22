package com.tspeiz.modules.home.rtc.room;

import java.util.ArrayList;
import java.util.List;

import com.tspeiz.modules.home.rtc.UserClient;
public class WebRTCSimpleRoom_new {
	
	private List<UserClient> users;
	public void addUser(UserClient userClient){
		
		if(users == null){
			users = new ArrayList<UserClient>();
		} 
		users.add(userClient);
	}
	
	public void removeUser( String userId,Integer userType){
		if(users != null){
			for(UserClient user:users){
				if(user.getUserId() != null 
						&& user.getUserId().equalsIgnoreCase(userId) 
						&& user.getUserType().equals(userType)){
					users.remove(user);
					break;
				}
			}
		}
	}
	
	public UserClient getByUserType(Integer type){
		
		if(users == null){
			return null;
		}
		
		for(UserClient user: users){
			if(user.getUserType() != null && user.getUserType().equals(type)){
				return user;
			}
		}
		return null;
	}
	
	public UserClient getByUserId(String userId,Integer userType){
		
		if(users == null){
			return null;
		}
		
		for(UserClient user: users){
			if(user.getUserId() != null 
					&& user.getUserId().equalsIgnoreCase(userId) 
					&& user.getUserType().equals(userType)){
				return user;
			}
		}
		return null;
	}
	
	
	public Integer getCount() {

		if(users == null){
			return 0;
		} 
		
		return users.size();
	}
	
	public List<UserClient> getOtherUsers(String userId,Integer userType) {
		
		List<UserClient> tempList = new ArrayList<UserClient>();
		if(users != null){
			for(UserClient userClient: users){
				if(userClient.getUserId() != null ){
					if(!userClient.getUserId().equalsIgnoreCase(userId)
							&& !userClient.getUserType().equals(userType)){
						tempList.add(userClient);
					}
				}
				
			}
		}
		
		return tempList;
	}
	
	public void clear(){
		
		if(users != null){
			users.clear();
		} 
	}
}
