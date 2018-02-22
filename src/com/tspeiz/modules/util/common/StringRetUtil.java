package com.tspeiz.modules.util.common;

import com.tspeiz.modules.util.PropertiesUtil;

public class StringRetUtil {
	
	public static String gainStringByStats(Integer status,Integer progressTag){
		String st="";
		
		switch(status){
		case 0:
			st="待付款";
			break;
		case 1:
		case 11:
			st="待就诊";
			break;
		case 2:
			st="待二次下单";//第一次完成，标记为需要二次就诊
			break;
		case 3:
			st="待三次下单";//第二次完成，标记为需要三次就诊
			break;
		case 4:
			st="待就诊（第二次）";//第二次下完单
			break;
		case 5:
			st="待就诊（第三次）";//第三次下完单
			break;
		case 10:
			st="就诊完成";
			break;
		case 20:
			st="用户取消";
			break;
		case 21:
			st="专家取消";
			break;
		}
		if(progressTag!=null&&progressTag.equals(4))st="准备就绪";
		return st;
	}
	public static String gainStringByStats_new(Integer status,Integer progressTag){
		String st="";
		switch(status){
		case 10:
			st="待接诊";
			break;
		case 20:
			st="进行中";
			break;
		case 30:
			st="已退诊";
			break;
		case 40:
			st="就诊完成";
			break;
		case 50:
			st="已取消";
			break;
		}
		if(progressTag!=null&&progressTag.equals(4))st="准备就绪";
		return st;
	}
	public static String gainStringByStats(Integer status,Integer progressTag,Integer userType){
		String st="";
		
		switch(status){
		case 1:
		case 11:
			st="待就诊";
			if(userType == 2){
				st = "病例已接收，待医生就位";
			} else if(userType == 3){
				st = "病例已发送，待就诊";
			}
			break;
		case 2:
			st="待二次下单";//第一次完成，标记为需要二次就诊
			break;
		case 3:
			st="待三次下单";//第二次完成，标记为需要三次就诊
			break;
		case 4:
			st="待就诊（第二次）";//第二次下完单
			if(userType == 2){
				st = "病例已接收，待医生就位（第二次）";
			} else if(userType == 3){
				st = "病例已发送，待就诊（第二次）";
			}
			break;
		case 5:
			st="待就诊（第三次）";//第三次下完单
			if(userType == 2){
				st = "病例已接收，待医生就位（第二次）";
			} else if(userType == 3){
				st = "病例已发送，待就诊（第二次）";
			}
			break;
		case 12:
			st="医生拒绝";
			break;
		case 10:
			st="就诊完成";
			break;
		case 20:
			st="用户取消";
			break;
		case 21:
			st="专家取消";
			break;
		}
		if(progressTag!=null&&progressTag.equals(4))st="准备就绪";
		return st;
	}
	
	public static String gainStringByStats_new(Integer status,Integer progressTag,Integer userType){
		String st="";
		boolean trans=false;
		switch(status){
		case 10:
			st="待接诊";
			if(userType == 2){
				st = "病例已接收，待医生就位";
			} else if(userType == 3){
				st = "病例已发送，待接诊";
			}
			trans=true;
			break;
		case 20:
			st="进行中";
			trans=true;
			break;
		case 30:
			st="已退诊";
			break;
		case 40:
			st="就诊完成";
			break;
		case 50:
			st="已取消";
			break;
		}
		if(progressTag!=null&&progressTag.equals(4)&&trans)st="准备就绪";
		return st;
	}
	
	public static String gainPacsIPAndPort(){
		return PropertiesUtil.getString("DCM_IP")+":"+PropertiesUtil.getString("DCM_WADO_PORT");
	}
	
	public static String gainstatusdesc(Integer status) {
		String desc = "";
		switch (status) {
		case 10:
			desc = "待接诊";
			break;
		case 20:
			desc = "进行中";
			break;
		case 30:
			desc = "已退诊";
			break;
		case 40:
			desc = "已完成";
			break;
		case 50:
			desc = "已取消";
			break;
		}
		return desc;
	}
}
