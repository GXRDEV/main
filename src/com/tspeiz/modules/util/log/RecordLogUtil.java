package com.tspeiz.modules.util.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import com.mongodb.DBObject;
import com.tspeiz.modules.util.mongodb.MongoDBLogUtil;
import com.tspeiz.modules.util.mongodb.MongoDBManager;

public class RecordLogUtil {
	private static SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static void insert(String orderType, String orderDesc,
			String orderId, String orderPayId, String unixml, String prexml,
			String callxml, String outtradeno) throws Exception {
		MongoDBLogUtil ma = MongoDBLogUtil.getInstance();
		JSONObject obj = new JSONObject();
		obj.put("ordertype", orderType);
		obj.put("orderdesc", orderDesc);
		obj.put("orderid", orderId);
		obj.put("orderpayId", orderPayId);
		obj.put("unixml", unixml);
		obj.put("prexml", prexml);
		obj.put("callxml", callxml);
		obj.put("outtradeno", outtradeno);
		obj.put("logtime", format.format(new Date()));
		obj.put("updatetime", format.format(new Date()));
		ma.insert("wxpay_log", obj);
	}

	public static void update(String orderType, String orderId,
			String orderPayId, String callxml, String outtradeno)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ordertype", orderType);
		map.put("orderid", orderId);
		map.put("outtradeno", outtradeno);
		if (StringUtils.isNotBlank(orderPayId)) {
			map.put("orderpayId", orderPayId);
		}
		MongoDBLogUtil ma = MongoDBLogUtil.getInstance();
		DBObject dbobj = ma.findOne("wxpay_log", map);
		dbobj.put("callxml", callxml);
		dbobj.put("updatetime", format.format(new Date()));
		ma.insert("wxpay_log", JSONObject.fromObject(dbobj), dbobj.get("_id")
				.toString());
	}
	
	
}
