package com.tspeiz.modules.util;

import io.rong.RongCloud;
import io.rong.messages.TxtMessage;
import io.rong.models.CodeSuccessReslut;
import io.rong.models.HistoryMessageReslut;
import io.rong.models.TokenReslut;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.tspeiz.modules.util.imchat.RongCloudConfig;
import com.tspeiz.modules.util.pay.DESCryptUtils;



public class RongCloudApi {

	private static Logger log = Logger.getLogger(RongCloudApi.class);

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static RongCloud rongCloud = RongCloud.getInstance(RongCloudConfig.APPKEY, RongCloudConfig.APPSECRET);

	/**
	 * 按照规则生成融云端用户Id
	 * 
	 * @param userId
	 * @param userType
	 * @return
	 */
	public static String getRongCloudUserId(Integer userId, Integer userType) {
		if (null == userId || null == userType) {
			return null;
		}
		DESCryptUtils des = new DESCryptUtils();
		return des.getEncString(userType.toString() + "-" + userId.toString());
	}

	/**
	 * 解析融云用户Id
	 * 
	 * @param rongCloudUserId
	 * @return
	 */
	public static String parseRongCloudUserId(String rongCloudUserId) {
		DESCryptUtils des = new DESCryptUtils();
		return des.getDesString(rongCloudUserId);
	}

	/**
	 * 获取用户Token
	 * 
	 * @param userId
	 *            用户 Id，最大长度 64 字节。是用户在 App 中的唯一标识码，必须保证在同一个 App 内不重复，重复的用户 Id
	 *            将被当作是同一用户。（必传）
	 * @param name
	 *            用户名称，最大长度 128 字节。用来在 Push 推送时显示用户的名称。（必传）
	 * @param portraitUri
	 *            用户头像 URI，最大长度 1024 字节。用来在 Push 推送时显示用户的头像。（必传）
	 * @return
	 */
	public static String getUserToken(String userId, String name, String portraitUri) {
		TokenReslut userGetTokenResult;
		try {
			userGetTokenResult = rongCloud.user.getToken(userId, name, portraitUri);
			log.info("getToken:  " + userGetTokenResult.toString());
			return userGetTokenResult.getToken();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		}
		return "";
	}

	/**
	 * 刷新用户信息
	 * 
	 * @param userId
	 *            用户 Id，最大长度 64 字节。是用户在 App 中的唯一标识码，必须保证在同一个 App 内不重复，重复的用户 Id
	 *            将被当作是同一用户。（必传）
	 * @param name
	 *            用户名称，最大长度 128 字节。用来在 Push 推送时，显示用户的名称，刷新用户名称后 5
	 *            分钟内生效。（可选，提供即刷新，不提供忽略）
	 * @param portraitUri
	 *            用户头像 URI，最大长度 1024 字节。用来在 Push 推送时显示。（可选，提供即刷新，不提供忽略）
	 * @return
	 */
	public static String refresh(String userId, String name, String portraitUri) {
		CodeSuccessReslut userRefreshResult;
		try {
			userRefreshResult = rongCloud.user.refresh(userId, name, portraitUri);
			log.info("refresh:  " + userRefreshResult.toString());
			return userRefreshResult.getErrorMessage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		}
		return "";
	}

	/**
	 * 发送文本单聊消息
	 * 
	 * @param fromUserId
	 *            发送人用户 Id（必传）
	 * @param toUserId
	 *            接收用户 Id（必传）
	 * @param content
	 *            发送消息内容
	 * @param extra
	 *            附加信息
	 */
	public static void sendMessage(String fromUserId, String toUserId, String content, String extra) {
		String[] messageToUserId = { toUserId };
		TxtMessage txtMessage = new TxtMessage(content, extra);
		CodeSuccessReslut messagePublishPrivateResult;
		try {
			messagePublishPrivateResult = rongCloud.message.publishPrivate(fromUserId, messageToUserId, txtMessage, content, "{\"pushData\":\"" + content + "\"}", "99", 0, 0, 0);
			log.info("sendMessage:  " + messagePublishPrivateResult.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		}
	}

	/**
	 * 发送文本系统消息
	 * 
	 * @param fromUserId
	 *            发送人用户 Id（必传）
	 * @param toUserId
	 *            接收用户 Id（必传）
	 * @param content
	 *            发送消息内容
	 * @param extra
	 *            附加信息
	 */
	public static void sendSystemMessage(String fromUserId, String toUserId, String content, String extra) {
		String[] messageToUserId = { toUserId };
		TxtMessage txtMessage = new TxtMessage(content, extra);
		CodeSuccessReslut messagePublishSystemResult;
		try {
			messagePublishSystemResult = rongCloud.message.PublishSystem("userId", messageToUserId, txtMessage, content, "{\"pushData\":\"" + content + "\"}", 0, 0);
			log.info("PublishSystem:  " + messagePublishSystemResult.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		}
	}

	/**
	 * 获取消息历史记录下载地址(指定某天某小时内的所有会话消息记录.目前支持二人会话、讨论组、群组、聊天室、客服、系统通知消息历史记录下载)
	 * 
	 * @param date
	 *            指定北京时间某天某小时，格式为2014010101，表示获取 2014 年 1 月 1 日凌晨 1 点至 2
	 *            点的数据。（必传）
	 * @return
	 */
	public static String getHistory(String date) {
		HistoryMessageReslut messageGetHistoryResult;
		try {
			messageGetHistoryResult = rongCloud.message.getHistory(date);
			log.info("getHistory:  " + messageGetHistoryResult.toString());
			return messageGetHistoryResult.getUrl();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		}
		return "";
	}

	/**
	 * 加入群组
	 * 
	 * @param groupCreateUserId
	 * @param groupId
	 * @param groupName
	 * @return
	 */
	public static String joinGroup(String[] groupCreateUserId, String groupId, String groupName) {
		CodeSuccessReslut groupCreateResult;
		try {
			groupCreateResult = rongCloud.group.join(groupCreateUserId, groupId, groupName);
			return groupCreateResult.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		}
		return "";
	}

	/**
	 * 退出群组
	 * 
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public static String quitGroup(String[] userId, String groupId) {
		CodeSuccessReslut groupCreateResult;
		try {
			groupCreateResult = rongCloud.group.quit(userId, groupId);
			return groupCreateResult.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		}
		return "";
	}

	/**
	 * 解散群组
	 * 
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public static String dismissGroup(String userId, String groupId) {
		CodeSuccessReslut groupCreateResult;
		try {
			groupCreateResult = rongCloud.group.dismiss(userId, groupId);
			return groupCreateResult.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		}
		return "";
	}
}
