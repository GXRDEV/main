package com.tspeiz.modules.manage;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.entity.newrelease.BusinessOrderInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessTelOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessTuwenOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessWenZhenInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessWenzhenTel;
import com.tspeiz.modules.common.entity.newrelease.DoctorDetailInfo;
import com.tspeiz.modules.common.service.ICaseService;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.common.TwAndTelCommonPayUtil;
import com.tspeiz.modules.util.config.ReaderConfigUtil;
import com.tspeiz.modules.util.log.RecordLogUtil;
import com.tspeiz.modules.util.weixin.WeixinUtil;

/**
 * 图文或电话的订单处理业务
 * 
 * @author heyongb
 * 
 */
@Service
public class WenzhenManager {
	@Autowired
	private ICaseService caseService;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IWenzhenService wenzhenService;
	@Autowired
	private PayOrderManager payOrderManager;
	@Autowired
	private CommonManager commonManager;

	public Map<String, Object> processTwAndTelOrder(HttpServletRequest request,
			HttpServletResponse response, Integer userId, Integer ltype)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 1.处理病例
		Integer caseid = caseService.saveOrUpdateCase(request, userId,1);
		// 2.生成订单
		Map<String, Object> omap = generateTwAndTelOrder(request, userId,
				caseid, ltype);
		// 3.生成支付单
		Map<String,Object> pmap=processPay(userId,Boolean.valueOf(omap.get("payStatus").toString()), request,
				response, Integer.parseInt(omap.get("oid").toString()), ltype,
				omap.get("desc").toString(), request.getParameter("openid"),Float.valueOf(omap.get("money").toString()));
		map.putAll(omap);
		map.putAll(pmap);
		return map;
	}

	private Map<String, Object> generateTwAndTelOrder(
			HttpServletRequest request, Integer userId, Integer caseid,
			Integer ltype) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer docid = Integer.parseInt(request.getParameter("docid"));// 专家id
		DoctorDetailInfo doc = commonService.queryDoctorDetailInfoById(docid);
		boolean pay = false;
		Integer oid = null;
		String desc = "";
		BigDecimal money = null;
		if (ltype.equals(1)) {
			doc.setAskAmount(commonManager.gainMoneyByOrder(1,doc.getId()));
			// 图文问诊
			if (doc.getAskAmount() != null
					&& doc.getAskAmount().compareTo(BigDecimal.ZERO) > 0) {
				pay = true;
			}
			// 生成订单
			oid = generateTwOrder(request, userId, caseid, docid, pay);
			desc = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/twbody");
			money = doc.getAskAmount();
		} else {
			doc.setTelAmount(commonManager.gainMoneyByOrder(2,doc.getId()));
			// 电话问诊
			if (doc.getTelAmount() != null
					&& doc.getTelAmount().compareTo(BigDecimal.ZERO) > 0) {
				pay = true;
			}
			// 生成订单
			oid = generateTelOrder(request, userId, docid, caseid, pay);
			desc = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/telbody");
			money = doc.getTelAmount();
		}
		map.put("desc", desc);
		map.put("oid", oid);
		map.put("money", money);
		map.put("payStatus", pay);
		return map;
	}

	private Map<String,Object> processPay(Integer userId,boolean pay, HttpServletRequest request,
			HttpServletResponse response, Integer orderId, Integer ltype,
			String desc, String openid,Float money) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		if (pay) {
			// 支付
			String ret = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/orderPrefix_zaixian");
			Map<String, Object> retMap = WeixinUtil.weipay(request, response,
					money,
					PropertiesUtil.getString("APPID"),
					PropertiesUtil.getString("APPSECRET"),
					PropertiesUtil.getString("PARTNER"),
					PropertiesUtil.getString("PARTNERKEY"), desc, openid,
					PropertiesUtil.getString("DOMAINURL")
							+ "kangxin/wenzhennotify", null, null, ret);
			Integer pid=payOrderManager.savePayInfo(ltype, orderId, retMap.get("out_trade_no").toString(), money, 2, money, 0.0f, 0.0f, 0.0f, null);
			map.putAll(retMap);
			//记录日志
			try {
				RecordLogUtil.insert(ltype + "", desc, orderId + "", pid + "",
						(String) retMap.get("paramxml"),
						(String) retMap.get("prepayxml"), "",
						retMap.get("out_trade_no").toString());
			} catch (Exception e) {

			}
		}
		return map;
	}

	private Integer generateTwOrder(HttpServletRequest request, Integer uid,
			Integer caseid, Integer docid, Boolean pay) {
		BusinessTuwenOrder info=new BusinessTuwenOrder();
		info.setOpenId(request.getParameter("openid"));
		TwAndTelCommonPayUtil.twOrderInfo(info, caseid, docid, uid, OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey(), 3, pay);
		info.setUuid(commonManager.generateUUID(1));
		return wenzhenService.saveBusinessTuwenOrder(info);
	}

	private Integer generateTelOrder(HttpServletRequest request, Integer uid,
			Integer docid, Integer caseid, Boolean pay) {
		BusinessTelOrder telinfo=new BusinessTelOrder();
		telinfo.setOpenId(request.getParameter("openid"));
		TwAndTelCommonPayUtil.telOrderInfo(telinfo, uid, docid, caseid, pay,3, 0);
		telinfo.setUuid(commonManager.generateUUID(2));
		return wenzhenService.saveBusinessTelOrder(telinfo);
	}
}
