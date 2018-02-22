package com.tspeiz.modules.common.service.weixin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.bean.weixin.resp.Article;
import com.tspeiz.modules.common.bean.weixin.resp.NewsMessage;
import com.tspeiz.modules.common.bean.weixin.resp.TextMessage;
import com.tspeiz.modules.common.entity.Users;
import com.tspeiz.modules.common.entity.newrelease.UserAccountInfo;
import com.tspeiz.modules.common.entity.newrelease.UserDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.UserWeixinRelative;
import com.tspeiz.modules.common.entity.weixin.UserExternals;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.util.EmojiFilterUtil;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.SpringUtil;
import com.tspeiz.modules.util.weixin.CommonUtil;
import com.tspeiz.modules.util.weixin.MessageUtil;


/**
 * 核心服务类
 * 
 * @author heyongb
 * 
 */
public class CoreService {
	private static Logger log = Logger.getLogger(CoreService.class);
	private static IWeixinService weixinService;
	static {
		ApplicationContext ac = SpringUtil.getApplicationContext();
		weixinService = (IWeixinService) ac.getBean("weixinService");
	}
	private static void processMap(Map<String,String> map) {
		for(String key:map.keySet()){
			System.out.println("key:"+key+"  value:"+map.get(key));
		}
	}
	public static String processRequest(HttpServletRequest request) {
        String respXml = null;
        String respContent = "";
        try {
            Map<String, String> requestMap = MessageUtil.parseXml(request);
           // processMap(requestMap);
            String fromUserName = requestMap.get("FromUserName");
            String toUserName = requestMap.get("ToUserName");
            String msgType = requestMap.get("MsgType");
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            String appid = PropertiesUtil.getString("APPID");
            String domain = PropertiesUtil.getString("DOMAINURL");
            String eventKey = requestMap.get("EventKey");
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                String content=requestMap.get("Content");
                if(content.contains("运营人员")){
                	String pre="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri=";
             		String aft="?response_type=code&scope=snsapi_base&state=123#wechat_redirect";
                    Article article=new Article();
                    article.setTitle("专家库");
                    article.setDescription("专家库");
                    article.setPicUrl("http://tupian201604.oss-cn-beijing.aliyuncs.com/2017/07/04/b96863455d6c4d908093389ddeb2d481.jpg");
                    String specials=domain+"propagate/intodoclist";
                    specials=URLEncoder.encode(specials);
                    article.setUrl(pre+specials+aft);
                    
                    Article article2=new Article();
                    article2.setTitle("远程会诊下单入口");
                    article2.setDescription("远程会诊下单入口");
                    article2.setPicUrl("http://test20171106.oss-cn-beijing.aliyuncs.com/2017/11/14/a469086483c14f8ea172ea5d6b4eea54.png");
                    String url=domain+"module/remote.html#/main";
                    url=URLEncoder.encode(url);
                   
                    article2.setUrl(pre+url+aft);
                    List<Article> articles=new ArrayList<Article>();
                    articles.add(article2);
                    articles.add(article);
                    NewsMessage newsMessage=new NewsMessage();
                    newsMessage.setArticleCount(2);
                    newsMessage.setCreateTime(new Date().getTime());
                    newsMessage.setToUserName(fromUserName);
                    newsMessage.setFromUserName(toUserName);
                    newsMessage.setArticles(articles);
                    newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
                    respXml=MessageUtil.messageToXml(newsMessage);
                }else if(content.contains("医生")){
                	//推送链接
                	textMessage.setContent("欢迎使用佰医汇医疗服务。\n<a href=\""+domain+"module/patient.html#/libs/doc/all\">点击找医生</a>\n按区域、科室来查找您想找到的医生。在医生主页，您可以选择图文、电话问诊（疾病咨询），也可以像您熟悉的医生发起报道，报道成功后，您可以与医生在线交流，诊后随访等。");
                    respXml = MessageUtil.messageToXml(textMessage);
                }else{
                    textMessage.setContent("您发送的文本消息,我们已收到,正在处理中..");
                    respXml = MessageUtil.messageToXml(textMessage);
                }
            } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
                textMessage.setContent("您发送的图片消息,我们已收到,正在处理中..");
                respXml = MessageUtil.messageToXml(textMessage);
            } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
                textMessage.setContent("您发送的语音消息,我们已收到,正在处理中..");
                respXml = MessageUtil.messageToXml(textMessage);
            } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)) {
                // 请求消息类型：视频
            } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
                // 请求消息类型：地理位置
            } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
                // 请求消息类型：链接
            } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 请求消息类型：事件推送
                // 事件类型
                String eventType = requestMap.get("Event");
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                	processUserInfo(fromUserName);
                    if(StringUtils.isNotBlank(eventKey)){
                    	String secenStr = eventKey.split("_")[1];
                    	if(secenStr.equalsIgnoreCase("-1")) {
                    		Article article=new Article();
                            article.setTitle("连接优质医资，助患者早日康复");
                            //article.setDescription("托尔斯泰");
                            article.setPicUrl("http://dcm20171021.oss-cn-beijing.aliyuncs.com/2017/10/25/b058af25fc324a6a8ee1866d68431eae.png");
                            article.setUrl(domain+"module/patient.html?openid="+fromUserName+"#/main/info");
                            List<Article> articles=new ArrayList<Article>();
                            articles.add(article);
                            NewsMessage newsMessage=new NewsMessage();
                            newsMessage.setArticleCount(1);
                            newsMessage.setCreateTime(new Date().getTime());
                            newsMessage.setToUserName(fromUserName);
                            newsMessage.setFromUserName(toUserName);
                            newsMessage.setArticles(articles);
                            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
                            respXml=MessageUtil.messageToXml(newsMessage);
                    	}else {
                    		String type = eventKey.split("_")[1];
                    		if(type.equalsIgnoreCase("docinfo")) {
                    			MobileSpecial doc=weixinService.queryMobileSpecialByUserIdAndUserType(Integer.parseInt(eventKey.split("_")[2]));
                                if(doc!=null){
                                    Article article=new Article();
                                    article.setTitle("欢迎您向“"+doc.getSpecialName()+"”医生报到");
                                    article.setDescription("报到成功后即与医生建立联系，方便健康咨询、诊后随访、用药调整等，让您与医生沟通更便捷。");
                                    if(StringUtils.isNotBlank(doc.getListSpecialPicture())){
                                        article.setPicUrl(doc.getListSpecialPicture()+"?x-oss-process=style/wx");
                                    }else {
                                        article.setPicUrl("http://test20171106.oss-cn-beijing.aliyuncs.com/2017/11/14/a469086483c14f8ea172ea5d6b4eea54.png");
                                    }
                                    article.setUrl(domain + "module/patient.html?openid="+fromUserName+"#/docinfo/"+doc.getSpecialId()+"?follow=1");
                                    List<Article> articles=new ArrayList<Article>();
                                    articles.add(article);
                                    NewsMessage newsMessage=new NewsMessage();
                                    newsMessage.setArticleCount(1);
                                    newsMessage.setCreateTime(new Date().getTime());
                                    newsMessage.setToUserName(fromUserName);
                                    newsMessage.setFromUserName(toUserName);
                                    newsMessage.setArticles(articles);
                                    newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
                                    respXml=MessageUtil.messageToXml(newsMessage);
                                }
                    		}
                    	}     
                    }else {
                    	Article article=new Article();
                        article.setTitle("连接优质医资，助患者早日康复");
                        article.setPicUrl("http://dcm20171021.oss-cn-beijing.aliyuncs.com/2017/10/25/b058af25fc324a6a8ee1866d68431eae.png");
                        article.setUrl(domain + "module/patient.html?openid="+fromUserName+"#/main/info");
                        List<Article> articles=new ArrayList<Article>();
                        articles.add(article);
                        NewsMessage newsMessage=new NewsMessage();
                        newsMessage.setArticleCount(1);
                        newsMessage.setCreateTime(new Date().getTime());
                        newsMessage.setToUserName(fromUserName);
                        newsMessage.setFromUserName(toUserName);
                        newsMessage.setArticles(articles);
                        newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
                        respXml=MessageUtil.messageToXml(newsMessage);
                    }
                } else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                    log.info("===取消关注事件===");
                } else if (eventType
                        .equalsIgnoreCase(MessageUtil.EVENT_TYPE_SCAN)) {
                	if(StringUtils.isNotBlank(eventKey)) {
                		String type = eventKey.split("_")[0];
                		if(type.equalsIgnoreCase("docinfo")){
                			MobileSpecial doc=weixinService.queryMobileSpecialByUserIdAndUserType(Integer.parseInt(eventKey.split("_")[1]));
                            if(doc!=null){
                                Article article=new Article();
                                article.setTitle("欢迎您向“"+doc.getSpecialName()+"”医生报到");
                                article.setDescription("报到成功后即与医生建立联系，方便健康咨询、诊后随访、用药调整等，让您与医生沟通更便捷。");
                                if(StringUtils.isNotBlank(doc.getListSpecialPicture())) {
                                	article.setPicUrl(doc.getListSpecialPicture()+"?x-oss-process=style/wx");
                                }else {
                                    article.setPicUrl("http://test20171106.oss-cn-beijing.aliyuncs.com/2017/11/14/a469086483c14f8ea172ea5d6b4eea54.png");
                                }
                                article.setUrl(domain + "module/patient.html?openid="+fromUserName+"#/docinfo/"+doc.getSpecialId()+"?follow=1");
                                List<Article> articles=new ArrayList<Article>();
                                articles.add(article);
                                NewsMessage newsMessage=new NewsMessage();
                                newsMessage.setArticleCount(1);
                                newsMessage.setCreateTime(new Date().getTime());
                                newsMessage.setToUserName(fromUserName);
                                newsMessage.setFromUserName(toUserName);
                                newsMessage.setArticles(articles);
                                newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
                                respXml=MessageUtil.messageToXml(newsMessage);
                            }
                		}
                	}
                    log.info("===扫描二维码事件===");

                } else if (eventType.equals(MessageUtil.EVENT_TYPE_LOCATION)) {

                } else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respXml;
    }
	
	private static void processUserInfo(String openid){
		UserWeixinRelative userWeixinRelative = weixinService.queryUserWeiRelativeByOpenId(openid);
		log.info("===openid==="+openid);
		Integer userId = null;
		if(userWeixinRelative !=null) {
		}else {
			//创建临时新账户userId，如果有老数据，绑定时需要更换成老的userId，同时更新新产生数据里的userId
			UserAccountInfo user = new UserAccountInfo();
			user.setUuid(UUID.randomUUID().toString().replace("-", ""));
			user.setRegisterTime(new Date());
			user.setOrigin("weixin");
			user.setStatus("1");
			userId = weixinService.saveUserAccountInfo(user);
			UserDetailInfo userDetailInfo = new UserDetailInfo();
			JSONObject person = CommonUtil.GetPersonInfo(weixinService
					.queryWeiAccessTokenById(PropertiesUtil.getString("APPID")).getAccessToken(), openid);
			userDetailInfo.setDisplayName(person.getString("nickname"));
			userDetailInfo.setSex(person.getInt("sex"));
			userDetailInfo.setHeadImageUrl(person.getString("headimgurl"));
			userDetailInfo.setId(userId);
			weixinService.saveUserDetailInfo(userDetailInfo);
			userWeixinRelative = new UserWeixinRelative();
			userWeixinRelative.setUserId(userId);
			userWeixinRelative.setOpenId(openid);
			userWeixinRelative.setAppId(PropertiesUtil.getString("APPID"));
			userWeixinRelative.setDisplayName(person.getString("nickname"));
			userWeixinRelative.setSex(userDetailInfo.getSex());
			userWeixinRelative.setHeadImageUrl(userDetailInfo.getHeadImageUrl());
			weixinService.saveUserWeixinRelative(userWeixinRelative);
		}
	}

	
	//图文文章
	private static List<Article> gainArticles(){
		List<Article> ars=new ArrayList<Article>();
		Article article=new Article(); 
		article.setTitle("看病找专家，就用佰医汇");
		article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/YFc2yChKf62NVn789vJiamc4CcSDYm4AUqZBqMUpbxBhDibowZbykEuF3wia2KHmH6Ria5iaMcHNkABibvnGzv1oPQZQ/0?wxfmt=png");
		article.setUrl("http://mp.weixin.qq.com/s?__biz=MzA4MDQ2Nzg4MA==&mid=500707722&idx=1&sn=c3f51b92380f5cb21d1d87f1aa5b5779&scene=0&previewkey=Z%2FUa8Nft8Bcac%2BhvE%2FI3TMNS9bJajjJKzz%2F0By7ITJA%3D#wechat_redirect");
		article.setDescription("“佰医汇”是国内唯一一家按国内医院科室主流排行榜甄选专家，并实行实名邀请制的移动医疗平台。");
		ars.add(article);
		return ars;
	}

	private static void handleUnSubscribe(String externalId) {
		UserExternals ue = weixinService
				.queryUserExternalByExternalId(externalId);
		if (ue != null) {
			ue.setStatus(0);
			weixinService.updateUserExternal(ue);
		}
	}

	private static String formatTime(String createTime) {
		long msgCreatTime = Long.parseLong(createTime) * 1000L;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(msgCreatTime));
	}

	private static String mediaNameFormatTime(String createTime) {
		long msgCreatTime = Long.parseLong(createTime) * 1000L;
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return format.format(new Date(msgCreatTime));
	}

	private static SimpleDateFormat sdf2 = new SimpleDateFormat(
			"yyyyMMddHHmmssSSS");

	private static String storeLocalPic(HttpServletRequest request,
			String netUrl) throws Exception {
		String dirPath = request.getRealPath("/") + "upload" + "/看病找谁/微信/窗口";
		if (!new File(dirPath).exists()) {
			new File(dirPath).mkdirs();
		}
		URL url = new URL(netUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		InputStream inStream = conn.getInputStream();
		byte[] data = readInputStream(inStream);
		String path = dirPath + "/" + sdf2.format(new Date()) + ".jpg";
		File imageFile = new File(path);
		FileOutputStream outStream = new FileOutputStream(imageFile);
		outStream.write(data);
		outStream.close();

		int pos = path.indexOf("\\upload");
		String relativePath = path.substring(pos + 1);
		return relativePath;
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

}