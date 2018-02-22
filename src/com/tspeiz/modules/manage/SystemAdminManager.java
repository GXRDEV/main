package com.tspeiz.modules.manage;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.tspeiz.modules.common.entity.newrelease.*;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.uitls.RandomUtil;
import com.tspeiz.modules.common.bean.CommonOrderInfo;
import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.PayInfo;
import com.tspeiz.modules.common.bean.dto.BusinessD2pPrivateOrderDto;
import com.tspeiz.modules.common.bean.dto.BusinessT2pVipOrderDto;
import com.tspeiz.modules.common.bean.dto.DocAboutDatas;
import com.tspeiz.modules.common.bean.dto.DocFollowDto;
import com.tspeiz.modules.common.bean.dto.DoctorsAboutsDto;
import com.tspeiz.modules.common.bean.dto.GroupInfoDto;
import com.tspeiz.modules.common.bean.dto.GroupMemberInfoDto;
import com.tspeiz.modules.common.bean.dto.HistoryCaseDto;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.entity.SystemServiceInfo;
import com.tspeiz.modules.common.entity.SystemServicePackage;
import com.tspeiz.modules.common.entity.release2.BusinessD2dReferralOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2pConsultationRequest;
import com.tspeiz.modules.common.entity.release2.BusinessD2pFastaskOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2pReportOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2pTelOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2pTuwenOrder;
import com.tspeiz.modules.common.entity.release2.BusinessT2pTuwenOrder;
import com.tspeiz.modules.common.entity.release2.DoctorTeam;
import com.tspeiz.modules.common.entity.release2.DoctorTeamMember;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAlliance;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAllianceMember;
import com.tspeiz.modules.common.entity.release2.HospitalMaintainerRelation;
import com.tspeiz.modules.common.entity.release2.OperatorInvitCode;
import com.tspeiz.modules.common.entity.release2.PlatformHealthConsultation;
import com.tspeiz.modules.common.entity.release2.RongCloudGroup;
import com.tspeiz.modules.common.entity.release2.RongCloudGroupMember;
import com.tspeiz.modules.common.entity.release2.RongCloudGroupPostRelation;
import com.tspeiz.modules.common.entity.release2.ShufflingFigureConfig;
import com.tspeiz.modules.common.entity.release2.SystemSmsRecord;
import com.tspeiz.modules.common.entity.release2.UserCaseAttachment;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.ID2pService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.util.CheckNumUtil;
import com.tspeiz.modules.util.ChineseToPinyinUtil;
import com.tspeiz.modules.util.DateUtil;
import com.tspeiz.modules.util.IdcardUtils;
import com.tspeiz.modules.util.PasswordUtil;
import com.tspeiz.modules.util.RongCloudApi;
import com.tspeiz.modules.util.UUIDUtil;
import com.tspeiz.modules.util.date.RelativeDateFormat;
import com.tspeiz.modules.util.date.TimecsUtil;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;
import com.tspeiz.modules.util.weixin.WeixinUtil;

@Service
public class SystemAdminManager {
	@Autowired
	private ICommonService commonService;
	@Autowired
	private ID2pService d2pService;
	@Autowired
	private IWenzhenService wenzhenService;
	@Autowired
	private IWeixinService weixinService;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat refund_sdf = new SimpleDateFormat(
			"yyyyMMddHHmmssSSS");

	private static Map<String, Object> weekdatas = null;
	static {
		try {
			weekdatas = TimecsUtil.getYearMonth(12);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 新入驻医院月增量
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> newhoscal() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> ordernums = new ArrayList<Integer>();
		// List<Integer> mtotalnums = new ArrayList<Integer>();
		List<String> categorys = (List<String>) weekdatas.get("categorys");
		List<String> dates = (List<String>) weekdatas.get("dates");
		// 查询总数
		Map<String, Object> _m = commonService.queryNewAddHospital(dates);
		List<ReSourceBean> _ordernums = (List<ReSourceBean>) _m.get("modata");
		// List<ReSourceBean> _mtotalnums=(List<ReSourceBean>)_m.get("todata");
		for (ReSourceBean bean : _ordernums) {
			ordernums.add(bean.getCount().intValue());
		}
		/*
		 * for(ReSourceBean bean:_mtotalnums){
		 * mtotalnums.add(bean.getCount().intValue()); }
		 */
		map.put("categorys", categorys);
		map.put("ordernums", ordernums);
		// map.put("mtotalnums", mtotalnums);
		return map;
	}

	/**
	 * 新入驻专家月增量
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> newexpertcal() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> ordernums = new ArrayList<Integer>();
		// List<Integer> mtotalnums = new ArrayList<Integer>();
		List<String> categorys = (List<String>) weekdatas.get("categorys");
		List<String> dates = (List<String>) weekdatas.get("dates");
		Map<String, Object> ret = commonService.queryNewExpertsAdd(dates);
		List<ReSourceBean> _ordernums = (List<ReSourceBean>) ret.get("modata");
		// List<ReSourceBean> _mtotalnums=(List<ReSourceBean>)ret.get("todata");
		for (ReSourceBean bean : _ordernums) {
			ordernums.add(bean.getCount().intValue());
		}
		/*
		 * for(ReSourceBean bean:_mtotalnums){
		 * mtotalnums.add(bean.getCount().intValue()); }
		 */
		map.put("categorys", categorys);
		map.put("ordernums", ordernums);
		// map.put("mtotalnums", mtotalnums);
		return map;
	}

	/**
	 * 会诊订单统计（包括 整体订单数，月增数）
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> ordercal(String hosid) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> ordernums = new ArrayList<Integer>();
		// List<Integer> mtotalnums = new ArrayList<Integer>();
		List<String> categorys = (List<String>) weekdatas.get("categorys");
		List<String> dates = (List<String>) weekdatas.get("dates");
		List<ReSourceBean> ordersAddCon = (List<ReSourceBean>) commonService
				.queryOrdersAddCon(null, hosid).get("modata");
		map.put("orcount", ordersAddCon.get(0).getCount());
		Map<String, Object> _m = commonService.queryOrdersAddCon(dates, hosid);
		List<ReSourceBean> _ordernums = (List<ReSourceBean>) _m.get("modata");
		// List<ReSourceBean> _mtotalnums=(List<ReSourceBean>)_m.get("todata");
		for (ReSourceBean bean : _ordernums) {
			ordernums.add(bean.getCount().intValue());
		}
		/*
		 * for(ReSourceBean bean:_mtotalnums){
		 * mtotalnums.add(bean.getCount().intValue()); }
		 */
		map.put("categorys", categorys);
		map.put("ordernums", ordernums);
		// map.put("mtotalnums", mtotalnums);
		return map;
	}

	/**
	 * 会诊订单医院分布
	 * 
	 * @return
	 */
	public Map<String, Object> orderhoscal(String startDate, String endDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer totalCount = commonService.queryOrdersAddCon(startDate,
				endDate, 0, null).get("modata");
		List<ReSourceBean> beans = commonService.queryOrderHosCal(startDate,
				endDate);
		Map<String, Double> retMap = new HashMap<String, Double>();
		for (ReSourceBean bean : beans) {
			retMap.put(bean.getName() + "(" + bean.getCount() + ")",
					Double.valueOf(getPercent(bean.getCount().intValue(),
							totalCount)));
		}
		map.put("tcount", totalCount);
		map.put("areaData", retMap);
		map.put("status", "success");
		return map;
	}

	/**
	 * 会诊订单专家分布
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> orderexcal(String hosid, String startDate,
			String endDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer totalCount = commonService.queryOrdersAddCon(startDate,
				endDate, 0, hosid).get("modata");
		List<ReSourceBean> beans = commonService.orderexcal(hosid, startDate,
				endDate);
		Map<String, Double> retMap = new HashMap<String, Double>();
		Integer tempTotal = 0;
		for (ReSourceBean bean : beans) {
			retMap.put(bean.getName() + "(" + bean.getCount() + ")",
					Double.valueOf(getPercent(bean.getCount().intValue(),
							totalCount)));
			tempTotal += bean.getCount().intValue();
		}
		if (totalCount - tempTotal > 0) {
			retMap.put("其他(" + (totalCount - tempTotal) + ")", Double
					.valueOf(getPercent(totalCount - tempTotal, totalCount)));
		}
		map.put("tcount", totalCount);
		map.put("areaData", retMap);
		map.put("status", "success");
		return map;
	}

	/**
	 * 会诊订单科室分布
	 * 
	 * @param hosid
	 * @param date
	 * @return
	 */
	public Map<String, Object> orderdepcal(String hosid, String startDate,
			String endDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer totalCount = commonService.queryOrdersAddCon(startDate,
				endDate, 0, hosid).get("modata");
		List<ReSourceBean> beans = commonService.orderdepcal(hosid, startDate,
				endDate);
		Map<String, Double> retMap = new HashMap<String, Double>();
		Integer tempTotal = 0;
		for (ReSourceBean bean : beans) {
			retMap.put(bean.getName() + "(" + bean.getCount() + ")",
					Double.valueOf(getPercent(bean.getCount().intValue(),
							totalCount)));
			tempTotal += bean.getCount().intValue();
		}
		if (totalCount - tempTotal > 0) {
			retMap.put("其他(" + (totalCount - tempTotal) + ")", Double
					.valueOf(getPercent(totalCount - tempTotal, totalCount)));
		}
		map.put("tcount", totalCount);
		map.put("areaData", retMap);
		map.put("status", "success");
		return map;
	}

	@SuppressWarnings("unchecked")
	public String queryNeedAuditDocs(String sEcho, Integer status,
			String search, Integer start, Integer length) {
		Map<String, Object> retmap = commonService.queryNeedAuditDocs(status,
				search, start, length);
		StringBuilder stringJson = null;
		Integer renum = (Integer) retmap.get("num");
		List<DoctorRegisterInfo> docs = (List<DoctorRegisterInfo>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		DoctorRegisterInfo doc = null;
		for (int i = 0; i < docs.size(); i++) {
			doc = docs.get(i);
			stringJson.append("[");
			stringJson.append("\"" + doc.getId() + "\",");
			stringJson.append("\"" + doc.getDisplayName() + "\",");
			stringJson.append("\"" + doc.getMobileNumber() + "\",");
			stringJson.append("\"" + doc.getHosName() + "\",");
			stringJson.append("\"" + doc.getDepName() + "\",");
			stringJson.append("\"" + desc(doc.getStatus()) + "\",");
			stringJson.append("\"" + sdf.format(doc.getRegisterTime()) + "\",");
			stringJson.append("\"" + "" + "\"");
			stringJson.append("],");
		}
		if (docs.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		System.out.println(stringJson.toString());
		return stringJson.toString();
	}

	private String desc(Integer status) {
		String desc = "";
		switch (status) {
		case -1:
			desc = "待认证";
			break;
		case -2:
			desc = "待审核";
			break;
		case -3:
			desc = "认证未通过";
			break;
		}
		return desc;
	}

	@SuppressWarnings("unchecked")
	public String gainexadvices(Map<String, String> paramMap) {
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		StringBuilder stringJson = null;
		List<SpecialAdviceOrder> _rcs = null;
		Integer type = Integer.parseInt(paramMap.get("ostatus"));
		Map<String, Object> retmap = commonService
				.querySpecialAdviceOrdersByCondition_sys(type, searchContent,
						start, length);
		Integer renum = (Integer) retmap.get("num");
		List<SpecialAdviceOrder> rcs = (List<SpecialAdviceOrder>) retmap
				.get("items");
		if (_rcs == null)
			_rcs = new ArrayList<SpecialAdviceOrder>();
		_rcs.addAll(rcs);
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":[");
		SpecialAdviceOrder order = null;
		for (int i = 0; i < _rcs.size(); i++) {
			order = _rcs.get(i);
			stringJson.append("[");
			stringJson.append("\"" + order.getId() + "\",");
			stringJson.append("\"" + order.getHeadImageUrl() + "\",");
			stringJson.append("\"" + order.getExpertName() + "\",");
			stringJson.append("\"" + order.getHosName() + "\",");
			stringJson.append("\"" + order.getDepName() + "\",");
			stringJson.append("\"" + order.getLocalDocName() + "\",");
			stringJson.append("\"" + order.getLocalHosName() + "\",");
			stringJson.append("\"" + order.getLocalDepName() + "\",");
			String age = "";
			String sex = "";
			if (order.getAge() == null) {
				if (StringUtils.isNotBlank(order.getIdCard())) {
					age = String.valueOf(IdcardUtils.getAgeByIdCard(order
							.getIdCard()));
				} else {
					age = "未知";
				}
			} else {
				age = order.getAge().toString();
			}
			if (order.getSex() == null) {
				if (StringUtils.isNotBlank(order.getIdCard())) {
					String _sex = IdcardUtils.getGenderByIdCard(order
							.getIdCard());
					sex = _sex.equalsIgnoreCase("M") ? "男" : "女";
				} else {
					sex = "未知";
				}
			} else {
				sex = order.getSex().equals(1) ? "男" : "女";
			}
			stringJson.append("\"" + order.getUserName() + " " + sex + " "
					+ age + "\",");
			stringJson.append("\""
					+ (StringUtils.isNotBlank(order.getDiseaseDes()) ? order
							.getDiseaseDes() : "") + "\",");
			stringJson.append("\""
					+ (order.getPayStatus() != null ? (order.getPayStatus()
							.equals(1) ? "已支付" : "未支付") : "未支付") + "\",");
			stringJson.append("\"" + sdf.format(order.getCreateTime()) + "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (_rcs.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();

	}

	public void changeOrderStatus(Integer oid, Integer otype, String sval,
			String refuls) {
		switch (otype) {
		case 6:
			BusinessD2pTuwenOrder tworder = d2pService
					.queryd2ptuwenorderbyid(oid);
			if (sval.equalsIgnoreCase("delete")) {
				tworder.setDelFlag(1);
			} else if (sval.equalsIgnoreCase("out")) {
				tworder.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey());
				tworder.setClosedTime(new Timestamp(System.currentTimeMillis()));
				tworder.setCloserId(OrderStatusEnum.SYSTEM_CLOSED_ID.getKey());
				tworder.setCloserType(OrderStatusEnum.SYSTEM_CLOSED_TYPE
						.getKey());
			} else if (sval.equalsIgnoreCase("complete")) {
				tworder.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED
						.getKey());
				tworder.setClosedTime(new Timestamp(System.currentTimeMillis()));
				tworder.setCloserId(OrderStatusEnum.SYSTEM_CLOSED_ID.getKey());
				tworder.setCloserType(OrderStatusEnum.SYSTEM_CLOSED_TYPE
						.getKey());
			}
			tworder.setRefusalReason(refuls);
			d2pService.updated2ptuwenorder(tworder);
			break;
		case 7:// 电话问诊
			BusinessD2pTelOrder telorder = d2pService.queryd2ptelorderbyid(oid);
			if (sval.equalsIgnoreCase("delete")) {
				telorder.setDelFlag(1);
			} else if (sval.equalsIgnoreCase("out")) {
				telorder.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_OUT
						.getKey());
				telorder.setClosedTime(new Timestamp(System.currentTimeMillis()));
				telorder.setCloserId(OrderStatusEnum.SYSTEM_CLOSED_ID.getKey());
				telorder.setCloserType(OrderStatusEnum.SYSTEM_CLOSED_TYPE
						.getKey());
			} else if (sval.equalsIgnoreCase("complete")) {
				telorder.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED
						.getKey());
				telorder.setClosedTime(new Timestamp(System.currentTimeMillis()));
				telorder.setCloserId(OrderStatusEnum.SYSTEM_CLOSED_ID.getKey());
				telorder.setCloserType(OrderStatusEnum.SYSTEM_CLOSED_TYPE
						.getKey());
			}
			telorder.setRefusalReason(refuls);
			d2pService.updated2ptelorder(telorder);
			break;
		case 9:// 快速问诊
			BusinessD2pFastaskOrder fastorder = d2pService
					.queryd2pfastaskorderbyid(oid);
			if (sval.equalsIgnoreCase("delete")) {
				fastorder.setDelFlag(1);
			} else if (sval.equalsIgnoreCase("complete")) {
				fastorder.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED
						.getKey());
				fastorder.setClosedTime(new Timestamp(System
						.currentTimeMillis()));
				fastorder
						.setCloserId(OrderStatusEnum.SYSTEM_CLOSED_ID.getKey());
				fastorder.setCloserType(OrderStatusEnum.SYSTEM_CLOSED_TYPE
						.getKey());
			}
			fastorder.setRefusalReason(refuls);
			d2pService.updated2pfastaskorder(fastorder);
			break;
		case 10:// 转诊
			BusinessD2dReferralOrder ddo = d2pService
					.queryd2dreferralOrderbyId(oid);
			if (sval.equalsIgnoreCase("delete")) {
				ddo.setDelFlag(1);
			} else if (sval.equalsIgnoreCase("out")) {
				ddo.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey());
				ddo.setClosedTime(new Timestamp(System.currentTimeMillis()));
				ddo.setCloserId(OrderStatusEnum.SYSTEM_CLOSED_ID.getKey());
				ddo.setCloserType(OrderStatusEnum.SYSTEM_CLOSED_TYPE.getKey());
			} else if (sval.equalsIgnoreCase("complete")) {
				ddo.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED
						.getKey());
				ddo.setClosedTime(new Timestamp(System.currentTimeMillis()));
				ddo.setCloserId(OrderStatusEnum.SYSTEM_CLOSED_ID.getKey());
				ddo.setCloserType(OrderStatusEnum.SYSTEM_CLOSED_TYPE.getKey());
			}
			ddo.setRefusalReason(refuls);
			d2pService.updated2dreferralOrder(ddo);
			break;
		case 11:// 会诊申请
			BusinessD2pConsultationRequest reqorder = d2pService
					.queryd2pconreqorderbyid(oid);
			if (sval.equalsIgnoreCase("delete")) {
				reqorder.setDelFlag(1);
			} else if (sval.equalsIgnoreCase("complete")) {
				reqorder.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED
						.getKey());
			}
			reqorder.setRefusalReason(refuls);
			d2pService.updated2pconreqorder(reqorder);
			break;
		case 12:// 团队咨询
			BusinessT2pTuwenOrder tto = d2pService
					.querybusinesst2ptuwenById(oid);
			if (sval.equalsIgnoreCase("delete")) {
				tto.setDelFlag(1);
			} else if (sval.equalsIgnoreCase("out")) {
				tto.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey());
				tto.setCloseTime(new Timestamp(System.currentTimeMillis()));
				tto.setCloserId(OrderStatusEnum.SYSTEM_CLOSED_ID.getKey());
				tto.setCloserType(OrderStatusEnum.SYSTEM_CLOSED_TYPE.getKey());
			} else if (sval.equalsIgnoreCase("complete")) {
				tto.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED
						.getKey());
				tto.setCloseTime(new Timestamp(System.currentTimeMillis()));
				tto.setCloserId(OrderStatusEnum.SYSTEM_CLOSED_ID.getKey());
				tto.setCloserType(OrderStatusEnum.SYSTEM_CLOSED_TYPE.getKey());
			}
			tto.setRefusalReason(refuls);
			d2pService.updatet2ptuwen(tto);
		}
	}

	@SuppressWarnings("unchecked")
	public String queryhoshealthauditdatas(Map<String, String> paramMap) {
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String search = paramMap.get("search");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus = Integer.parseInt(paramMap.get("ostatus"));
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.queryhoshealthdatas(search,
				start, length, ostatus.toString());
		Integer renum = (Integer) retmap.get("num");
		List<HospitalHealthAlliance> healths = (List<HospitalHealthAlliance>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":[");
		HospitalHealthAlliance hoshealth = null;
		for (int i = 0; i < healths.size(); i++) {
			hoshealth = healths.get(i);
			stringJson.append("[");
			stringJson.append("\"" + hoshealth.getId() + "\",");
			stringJson.append("\"" + hoshealth.getYltName() + "\",");
			stringJson.append("\"" + hoshealth.getApplicant() + "\",");
			stringJson.append("\""
					+ (hoshealth.getApplicantType().equals(6) ? "企业管理员"
							: "医院管理员") + "\",");
			stringJson.append("\"" + hoshealth.getHosName() + "\",");
			stringJson.append("\"" + hoshealth.getHosLevel() + "\",");
			stringJson.append("\""
					+ (hoshealth.getStatus().equals(0) ? "待审核" : "审核未通过")
					+ "\",");
			stringJson.append("\"" + sdf.format(hoshealth.getApplicationTime())
					+ "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (healths.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}

	/**
	 * 修改医联体状态
	 * 
	 * @param hospitalHealthId
	 * @param sval
	 * @param userId
	 * @param userType
	 */
	public void changeHosHealthStatus(Integer hospitalHealthId, Integer sval,
			Integer userId, Integer userType) {
		HospitalHealthAlliance hha = commonService
				.queryHospitalHealthAllianceById(hospitalHealthId);
		hha.setStatus(sval);
		hha.setAuditorType(userType);
		hha.setAuditTime(new Timestamp(System.currentTimeMillis()));
		hha.setAuditorId(userId);
		commonService.updateHospitalHealthAlliance(hha);
	}

	public String queryhoshealthdatas(Map<String, String> paramMap) {
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus = Integer.parseInt(paramMap.get("ostatus"));
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.queryhoshealthdatas(
				searchContent, start, length, ostatus + "");
		Integer renum = (Integer) retmap.get("num");
		List<HospitalHealthAlliance> healths = (List<HospitalHealthAlliance>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":[");
		HospitalHealthAlliance hoshealth = null;
		List<HospitalDetailInfo> hoses = null;
		for (int i = 0; i < healths.size(); i++) {
			hoshealth = healths.get(i);
			stringJson.append("[");
			stringJson.append("\"" + hoshealth.getId() + "\",");
			stringJson.append("\"" + hoshealth.getYltName() + "\",");
			stringJson.append("\"" + hoshealth.getDistName() + "\",");
			stringJson.append("\"" + hoshealth.getHosName() + "\",");
			stringJson.append("\"" + hoshealth.getHosLevel() + "\",");
			hoses = commonService
					.queryHospitalHealthAllianceMemberByHhaId(hoshealth
							.getUuid());
			stringJson.append("\""
					+ ((hoses != null && hoses.size() > 0) ? hoses.size() : 0)
					+ "\",");
			stringJson.append("\"" + sdf.format(hoshealth.getAuditTime())
					+ "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (healths.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}

	public void savehoshealth(HttpServletRequest request, DoctorRegisterInfo reg) {
		String yltId = request.getParameter("yltId");// 如果是编辑 需要传医联体id
		Integer hosId = Integer.parseInt(request.getParameter("hosId"));
		HospitalDetailInfo hos = weixinService
				.queryHospitalDetailInfoById(hosId);
		HospitalHealthAlliance hha = null;
		boolean issave = false;
		if (StringUtils.isNotBlank(yltId)) {
			// 编辑
			hha = commonService.queryHospitalHealthAllianceById(Integer
					.parseInt(yltId));
		} else {
			// 新增
			issave = true;
			hha = new HospitalHealthAlliance();
			hha.setUuid(UUID.randomUUID().toString().replace("-", ""));
			hha.setApplicationTime(new Timestamp(System.currentTimeMillis()));
			hha.setApplicantId(reg.getId());
			hha.setApplicantType(reg.getUserType());
			hha.setAuditorId(reg.getId());
			hha.setAuditorType(reg.getUserType());
			hha.setAuditTime(new Timestamp(System.currentTimeMillis()));
		}
		hha.setHospitalId(hosId);
		hha.setHospitalLevel(hos.getHospitalLevel());
		hha.setYltName(request.getParameter("yltName"));
		hha.setSpeciality(request.getParameter("speciality"));
		hha.setProfile(request.getParameter("profile"));
		hha.setStatus(1);
		hha.setIconUrl(request.getParameter("iconUrl"));
		if (issave) {
			commonService.saveHospitalHealthAlliance(hha);
			// 添加成员
			saveHhMember(hha.getUuid(), hosId, hos.getHospitalLevel(), "");
		} else {
			commonService.updateHospitalHealthAlliance(hha);
			// 成员编辑
			List<HospitalHealthAllianceMember> members = commonService
					.queryHospitalHealthAllianceMemberByCon_main(hha.getUuid());
			for (HospitalHealthAllianceMember member : members) {
				if (member.getRole().equals(1)) {
					member.setHospitalId(hosId);
					member.setHospitalLevel(hos.getHospitalLevel());
				} else {
					member.setParentHosId(hosId);
				}
				commonService.updateHospitalHealthAllianceMember(member);
			}
		}
	}

	private void dataSet(JSONObject obj, HospitalHealthAllianceMember member) {
		obj.put("HospitalId", member.getHospitalId());
		obj.put("HosName", member.getHosName());
		obj.put("Area", member.getArea());
		obj.put("HosLevel", member.getLevelDesc());
		obj.put("JoinTime", sdf.format(member.getCreateTime()));
		obj.put("HospitalLevel", member.getHospitalLevel());
	}

	/**
	 * 获取医联体结构数据信息
	 * 
	 * @param yltId
	 * @return
	 */
	public String hoshealthstruts(Integer yltId) {
		HospitalHealthAlliance hha = commonService
				.queryHospitalHealthAllianceById(yltId);
		List<HospitalHealthAllianceMember> members = commonService
				.queryHospitalHealAllianceMembersByCon(hha.getUuid(), 10, null);
		JSONArray root = new JSONArray();
		if (members != null && members.size() > 0) {
			for (HospitalHealthAllianceMember _member : members) {
				JSONObject one_obj = new JSONObject();
				dataSet(one_obj, _member);
				List<HospitalHealthAllianceMember> childrens = commonService
						.queryHospitalHealAllianceMembersByCon(hha.getUuid(),
								null, _member.getHospitalId());
				if (childrens != null && childrens.size() > 0) {
					one_obj.put("expanded", "true");
					JSONArray two_arry = new JSONArray();
					for (HospitalHealthAllianceMember fchild : childrens) {
						JSONObject two_obj = new JSONObject();
						dataSet(two_obj, fchild);
						List<HospitalHealthAllianceMember> _childrens = commonService
								.queryHospitalHealAllianceMembersByCon(
										hha.getUuid(), null,
										fchild.getHospitalId());
						if (_childrens != null && _childrens.size() > 0) {
							two_obj.put("expanded", "true");
							JSONArray third_arry = new JSONArray();
							for (HospitalHealthAllianceMember _fchild : _childrens) {
								JSONObject third_obj = new JSONObject();
								dataSet(third_obj, _fchild);
								third_arry.add(third_obj);
							}
							two_obj.put("children", third_arry);
						}
						two_arry.add(two_obj);
					}
					one_obj.put("children", two_arry);
				}
				root.add(one_obj);
			}
		}
		return root.toString();
	}

	/**
	 * 医联体新增医院保存
	 * 
	 * @param allianceId
	 * @param parentHosId
	 * @param hospitalId
	 * @return
	 */
	public Map<String, Object> saveHosHealthMember(Integer allianceId,
			String parentHosId, Integer hospitalId) {
		Map<String, Object> map = new HashMap<String, Object>();
		HospitalHealthAlliance hha = commonService
				.queryHospitalHealthAllianceById(allianceId);
		HospitalDetailInfo hos = commonService
				.getHospitalDetailInfoById(hospitalId);
		saveHhMember(hha.getUuid(), hos.getId(), hos.getHospitalLevel(),
				parentHosId);
		map.put("status", "success");
		return map;
	}

	private void saveHhMember(String hhaUuid, Integer hosId, Integer hosLevel,
			String parentHosId) {
		HospitalHealthAllianceMember member = commonService
				.queryHospitalHealthAllianceMemberByCon(hosId, hhaUuid);
		if (member == null) {
			member = new HospitalHealthAllianceMember();
			member.setCreateTime(new Timestamp(System.currentTimeMillis()));
			member.setAllianceUuid(hhaUuid);
			member.setHospitalId(hosId);
			member.setHospitalLevel(hosLevel);
			if (StringUtils.isNotBlank(parentHosId)) {
				member.setParentHosId(Integer.parseInt(parentHosId));
				member.setRole(2);// 合作医院
			} else {
				member.setRole(1);// 核心医院
			}
			member.setStatus(1);
			member.setLevel(gainlevel(hosLevel));
			commonService.saveHospitalHealthAllianceMember(member);
		} else {
			member.setStatus(1);
			commonService.updateHospitalHealthAllianceMember(member);
		}
	}

	/**
	 * 删除医联体医院
	 * 
	 * @param hosId
	 * @param allianceId
	 * @return
	 */
	public Map<String, Object> delHosHealthMember(Integer hosId,
			Integer allianceId) {
		Map<String, Object> map = new HashMap<String, Object>();
		HospitalHealthAlliance hha = commonService
				.queryHospitalHealthAllianceById(allianceId);
		HospitalHealthAllianceMember member = commonService
				.queryHospitalHealthAllianceMemberByCon(hosId, hha.getUuid());
		processCaDel(member);
		member.setStatus(0);
		commonService.updateHospitalHealthAllianceMember(member);
		map.put("status", "success");
		return map;
	}

	/**
	 * 级联删除医联体医院
	 * 
	 * @param member
	 */
	private void processCaDel(HospitalHealthAllianceMember member) {
		List<HospitalHealthAllianceMember> members = commonService
				.queryHospitalHealAllianceMembersByCon(
						member.getAllianceUuid(), null, member.getHospitalId());
		if (members != null && members.size() > 0) {
			for (HospitalHealthAllianceMember _member : members) {
				processCaDel(_member);
				_member.setStatus(0);
				commonService.updateHospitalHealthAllianceMember(_member);
			}
		}
	}

	/**
	 * 获取运营人员数据
	 * 
	 * @param paramMap
	 * @return
	 */
	public String queryoperatordatas(Map<String, String> paramMap) {
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String search = paramMap.get("search");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus = Integer.parseInt(paramMap.get("ostatus"));
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.queryoperatordatas(search,
				ostatus, start, length);
		Integer renum = (Integer) retmap.get("num");
		List<MobileSpecial> uinfos = (List<MobileSpecial>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":[");
		MobileSpecial uinfo = null;
		for (int i = 0; i < uinfos.size(); i++) {
			uinfo = uinfos.get(i);
			stringJson.append("[");
			stringJson.append("\"" + uinfo.getSpecialId() + "\",");
			stringJson.append("\"" + uinfo.getSpecialName() + "\",");
			stringJson.append("\"" + uinfo.getMobileTelphone() + "\",");
			stringJson.append("\""
					+ (StringUtils.isNotBlank(uinfo.getPosition()) ? uinfo
							.getPosition() : "") + "\",");
			stringJson.append("\""
					+ (StringUtils.isNotBlank(uinfo.getInvitCode()) ? uinfo
							.getInvitCode() : "") + "\",");
			stringJson.append("\"" + sdf.format(uinfo.getRegisterTime())
					+ "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (uinfos.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}

	public Map<String, Object> generateOperatorCode(Integer docid) {
		Map<String, Object> map = new HashMap<String, Object>();
		DoctorDetailInfo doc = commonService.queryDoctorDetailInfoById(docid);
		map.put("status", "error");// 已生成邀请码
		if (!StringUtils.isNotBlank(doc.getInvitCode())) {
			DoctorRegisterInfo reg = commonService
					.queryDoctorRegisterInfoById(docid);
			String spells = ChineseToPinyinUtil.getFirstSpell(doc
					.getDisplayName());
			String phoneSpell = reg.getMobileNumber().substring(
					reg.getMobileNumber().length() - 4);
			String finalSpell = spells + phoneSpell;
			OperatorInvitCode invitCode = commonService.queryOperatorInvitCode(
					reg.getId(), finalSpell);
			if (invitCode == null) {
				invitCode = new OperatorInvitCode();
				invitCode.setCode(finalSpell);
				invitCode.setDoctorId(reg.getId());
				commonService.saveOperatorInvitCode(invitCode);
			} else {
				finalSpell = processMulCode(reg.getId(), finalSpell);
			}
			doc.setInvitCode(finalSpell);
			commonService.updateDoctorDetailInfo(doc);
			map.put("status", "success");
		}
		return map;
	}

	/**
	 * 处理邀请码相同的数据
	 * 
	 * @param docid
	 * @param finalSpell
	 * @return
	 */
	private String processMulCode(Integer docid, String finalSpell) {
		Boolean loop = true;
		String retSpell = "";
		while (loop) {
			retSpell = finalSpell + CheckNumUtil.randomChars(1);
			OperatorInvitCode invitCode = commonService.queryOperatorInvitCode(
					docid, retSpell);
			if (invitCode == null) {
				invitCode = new OperatorInvitCode();
				invitCode.setCode(retSpell);
				invitCode.setDoctorId(docid);
				commonService.saveOperatorInvitCode(invitCode);
				loop = false;
			}
		}
		return retSpell;
	}

	/**
	 * 新增或编辑运营人员
	 * 
	 * @param request
	 */
	public void saveorupdateOp(HttpServletRequest request) {
		String specialId = request.getParameter("specialId");
		String specialName = request.getParameter("specialName");
		String sex = request.getParameter("sex");
		String mobileTelphone = request.getParameter("mobileTelphone");
		String distCode = request.getParameter("distCode");
		String listSpecialPicture = request.getParameter("listSpecialPicture");// 头像
		String usertype = request.getParameter("usertype");// 类型
		if (StringUtils.isNotBlank(specialId)) {
			// 编辑
			DoctorRegisterInfo reg = commonService
					.queryDoctorRegisterInfoById(Integer.parseInt(specialId));
			reg.setMobileNumber(mobileTelphone);
			reg.setUserType(Integer.parseInt(usertype));
			DoctorDetailInfo doc = commonService
					.queryDoctorDetailInfoById(Integer.parseInt(specialId));
			doc.setDisplayName(specialName);
			doc.setDistCode(distCode);
			doc.setHeadImageUrl(listSpecialPicture);
			commonService.updateDoctorDetailInfo(doc);
			commonService.updateDoctorRegisterInfo(reg);
		} else {
			// 新增
			DoctorRegisterInfo reg = new DoctorRegisterInfo();
			reg.setLoginName(mobileTelphone);
			reg.setSalt("PdjwDr");
			reg.setPassword(PasswordUtil.MD5Salt("123456"));
			reg.setMobileNumber(mobileTelphone);
			reg.setRegisterTime(new Timestamp(System.currentTimeMillis()));
			reg.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
			reg.setStatus(1);
			reg.setUserType(Integer.parseInt(usertype));
			Integer did = commonService.saveDoctorRegisterInfo(reg);
			DoctorDetailInfo doc = new DoctorDetailInfo();
			doc.setId(did);
			doc.setDisplayName(specialName);
			doc.setHeadImageUrl(listSpecialPicture);
			doc.setDistCode(distCode);
			doc.setStatus(1);
			commonService.saveDoctorDetailInfo(doc);
		}
	}

	public Integer saveorupdatefigcons(HttpServletRequest request) {
		String figId = request.getParameter("figId");
		Integer _figId = null;
		Integer appType = Integer.parseInt(request.getParameter("appType"));
		String title = request.getParameter("title");
		String imageUrl = request.getParameter("imageUrl");
		String linkUrl = request.getParameter("linkUrl");
		ShufflingFigureConfig fig = null;
		boolean issave = true;
		if (StringUtils.isNotBlank(figId)) {
			// 编辑
			fig = commonService.queryShufflingFigureConfigById(Integer
					.parseInt(figId));
			issave = false;
		} else {
			// 新增
			fig = new ShufflingFigureConfig();
		}
		fig.setAppType(appType);
		fig.setTitle(title);
		fig.setImageUrl(imageUrl);
		fig.setLinkUrl(linkUrl);
		fig.setStatus(1);
		if (issave) {
			_figId = commonService.saveShufflingFigureConfig(fig);
		} else {
			commonService.updateShufflingFigureConfig(fig);
			_figId = fig.getId();
		}
		return _figId;
	}

	/**
	 * 删除轮播图
	 * 
	 * @param id
	 */
	public void delfigconfig(Integer id) {
		commonService.delShufflingFigureConfig(id);
	}

	/**
	 * 轮播图排序
	 * 
	 * @param ids
	 */
	public void sortfigconfig(String ids) {
		ShufflingFigureConfig fig = null;
		if (StringUtils.isNotBlank(ids)) {
			String[] _ids = ids.split(";");
			for (int i = 0; i < _ids.length; i++) {
				String id = _ids[i];
				if (StringUtils.isNotBlank(id)) {
					fig = commonService.queryShufflingFigureConfigById(Integer
							.parseInt(id));
					fig.setRank(i + 1);
					commonService.updateShufflingFigureConfig(fig);
				}
			}
		}
	}

	/**
	 * 患者报道详情
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> reportdetail(Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		BusinessD2pReportOrder order = d2pService.queryd2preportorderbyid(id);
		CaseInfo ca = commonService.queryCaseInfoById(order.getCaseId());
		/*
		 * if(StringUtils.isNotBlank(ca.getNormalImages())){
		 * List<CustomFileStorage> caseimages = wenzhenService
		 * .queryCustomFilesByCaseIds(ca.getNormalImages());
		 * map.put("caseimages", caseimages); }
		 */
		// 病例图片
		List<UserCaseAttachment> images = wenzhenService
				.queryUserAttachmentByCaseUuid(ca.getUuid());
		for (UserCaseAttachment userCaseAttachment : images) {
			if (StringUtils.isNotBlank(userCaseAttachment.getAttachmentIds())) {
				List<CustomFileStorage> files = wenzhenService
						.queryCustomFilesByCaseIds(userCaseAttachment
								.getAttachmentIds());
				userCaseAttachment.setFiles(files);
				Integer filescount = files != null ? files.size() : 0;
				userCaseAttachment.setFilescount(filescount);
			}
		}
		ca.setAttachments(images);
		map.put("caseimages", images);
		List<BusinessMessageBean> msgs = wenzhenService
				.queryBusinessMessageBeansByCon(id, order.getUuid(), 8);
		for (BusinessMessageBean _msg : msgs) {
			_msg.setMsgTime(sdf.format(_msg.getSendTime()));
		}
		Integer docid = order.getDoctorId();
		if (docid != null) {
			MobileSpecial doc = commonService
					.queryMobileSpecialByUserIdAndUserType(docid);
			map.put("doc", doc);
		}
		map.put("msgs", msgs);
		map.put("caseinfo", ca);
		map.put("order", order);
		return map;
	}

	/**
	 * 会诊申请详情
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> conreqdetail(Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		BusinessD2pConsultationRequest order = d2pService
				.queryd2pconreqorderbyid(id);
		// 团队
		String teamUuid = order.getTeamUuid();
		DoctorTeam dt = commonService.queryDoctorTeamByUuid(teamUuid);
		map.put("dteam", dt);
		CaseInfo ca = commonService.queryCaseInfoById(order.getCaseId());
		/*
		 * if(StringUtils.isNotBlank(ca.getNormalImages())){
		 * List<CustomFileStorage> caseimages = wenzhenService
		 * .queryCustomFilesByCaseIds(ca.getNormalImages());
		 * map.put("caseimages", caseimages); }
		 */
		// 病例图片
		List<UserCaseAttachment> images = wenzhenService
				.queryUserAttachmentByCaseUuid(ca.getUuid());
		for (UserCaseAttachment userCaseAttachment : images) {
			if (StringUtils.isNotBlank(userCaseAttachment.getAttachmentIds())) {
				List<CustomFileStorage> files = wenzhenService
						.queryCustomFilesByCaseIds(userCaseAttachment
								.getAttachmentIds());
				userCaseAttachment.setFiles(files);
				Integer filescount = files != null ? files.size() : 0;
				userCaseAttachment.setFilescount(filescount);
			}
		}
		ca.setAttachments(images);
		map.put("caseimages", images);
		Integer docid = order.getDoctorId();
		if (docid != null) {
			MobileSpecial doc = commonService
					.queryMobileSpecialByUserIdAndUserType(docid);
			map.put("doc", doc);
		}
		map.put("caseinfo", ca);
		map.put("order", order);
		return map;
	}

	/**
	 * 医联体申请 -详情
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> audithhadetail(Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		HospitalHealthAlliance hha = commonService
				.queryHospitalHealthAllianceById_new(id);
		map.put("hha", hha);
		return map;
	}

	/**
	 * 团队问诊 -详情
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> t2ptuwendetail(Integer id) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		BusinessT2pTuwenOrder order = d2pService.querybusinesst2ptuwenById(id);
		// 团队
		String teamUuid = order.getTeamUuid();
		DoctorTeam dt = commonService.queryDoctorTeamByUuid(teamUuid);
		map.put("dteam", dt);
		CaseInfo ca = commonService.queryCaseInfoById(order.getCaseId());
		/*
		 * if(StringUtils.isNotBlank(ca.getNormalImages())){
		 * List<CustomFileStorage> caseimages = wenzhenService
		 * .queryCustomFilesByCaseIds(ca.getNormalImages());
		 * map.put("caseimages", caseimages); }
		 */
		// 病例图片
		List<UserCaseAttachment> images = wenzhenService
				.queryUserAttachmentByCaseUuid(ca.getUuid());
		for (UserCaseAttachment userCaseAttachment : images) {
			if (StringUtils.isNotBlank(userCaseAttachment.getAttachmentIds())) {
				List<CustomFileStorage> files = wenzhenService
						.queryCustomFilesByCaseIds(userCaseAttachment
								.getAttachmentIds());
				userCaseAttachment.setFiles(files);
				Integer filescount = files != null ? files.size() : 0;
				userCaseAttachment.setFilescount(filescount);
			}
		}
		ca.setAttachments(images);
		map.put("caseimages", images);
		List<BusinessPayInfo> payinfos = wenzhenService
				.queryBusinesPayInfosByOId(id, 12);
		map.putAll(payinfo(payinfos));
		List<BusinessMessageBean> msgs = wenzhenService
				.queryBusinessMessageBeansByCon(id, order.getUuid(), 12);
		for (BusinessMessageBean _msg : msgs) {
			_msg.setMsgTime(sdf.format(_msg.getSendTime()));
		}
		Integer docid = order.getDoctorId();
		if (docid != null) {
			MobileSpecial doc = commonService
					.queryMobileSpecialByUserIdAndUserType(docid);
			System.out.println(doc);
			map.put("doc", doc);
		}
		// 判断是否需要退款
		if ((order.getStatus().equals(30) || order.getStatus().equals(50))
				&& order.getPayStatus() != null
				&& order.getPayStatus().equals(1)) {
			BigDecimal money = (BigDecimal) map.get("money");
			if (money != null && money.compareTo(BigDecimal.ZERO) > 0) {
				Object obj = map.get("refundStatus");
				if (obj != null && Integer.valueOf(obj.toString()).equals(1)) {
					// 退款成功，不需要显示退款
				} else {
					// 待退款或退款失败
					map.put("needRefund", "true");
				}
			}
		}
		map.put("msgs", msgs);
		map.put("caseinfo", ca);
		map.put("order", order);
		return map;
	}

	/**
	 * 转诊订
	 * 
	 * @param oid
	 * @return
	 */
	public Map<String, Object> d2dreferdetail(Integer oid) {
		Map<String, Object> map = new HashMap<String, Object>();
		BusinessD2dReferralOrder order = d2pService
				.queryd2dreferralOrderbyId(oid);
		map.put("order", order);
		CaseInfo ca = commonService.queryCaseInfoById(order.getCaseId());
		/*
		 * if(StringUtils.isNotBlank(ca.getNormalImages())){
		 * List<CustomFileStorage> caseimages = wenzhenService
		 * .queryCustomFilesByCaseIds(ca.getNormalImages());
		 * map.put("caseimages", caseimages); }
		 */
		// 病例图片
		List<UserCaseAttachment> images = wenzhenService
				.queryUserAttachmentByCaseUuid(ca.getUuid());
		for (UserCaseAttachment userCaseAttachment : images) {
			if (StringUtils.isNotBlank(userCaseAttachment.getAttachmentIds())) {
				List<CustomFileStorage> files = wenzhenService
						.queryCustomFilesByCaseIds(userCaseAttachment
								.getAttachmentIds());
				userCaseAttachment.setFiles(files);
				Integer filescount = files != null ? files.size() : 0;
				userCaseAttachment.setFilescount(filescount);
			}
		}
		ca.setAttachments(images);
		map.put("caseimages", images);
		Integer docid = order.getDoctorId();
		Integer refdocid = order.getReferralDocId();
		if (docid != null) {
			MobileSpecial doc = commonService
					.queryMobileSpecialByUserIdAndUserType(docid);
			map.put("doc", doc);
		}
		// 转诊医生
		if (refdocid != null) {
			MobileSpecial refdoc = commonService
					.queryMobileSpecialByUserIdAndUserType(refdocid);
			map.put("refdoc", refdoc);
		}
		map.put("caseinfo", ca);
		return map;
	}

	private SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private Map<String, Object> payinfo(List<BusinessPayInfo> payinfos)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String outTradeNo = "";
		BigDecimal money = null;
		String transactionId = "";
		Integer refundStatus = null;
		String refundTime = "";
		String refundResult = "";
		String payStatus = "";
		if (payinfos != null && payinfos.size() > 0) {
			BusinessPayInfo payInfo = payinfos.get(payinfos.size() - 1);
			outTradeNo = payInfo.getOutTradeNo();
			money = payInfo.getTotalMoney();
			if (isXML(payInfo.getOutTradeResult())) {
				transactionId = gainTransactionId(payInfo.getOutTradeResult());
			} else {
				transactionId = payInfo.getOutTradeResult();
			}
			refundStatus = payInfo.getRefundStatus();
			refundTime = payInfo.getRefundTime() != null ? _sdf.format(payInfo
					.getRefundTime()) : "";
			refundResult = payInfo.getOutTradeResult();
			if (StringUtils.isNotBlank(refundResult)) {
				refundResult = gainResultFromMsg(refundResult);
			}
			payStatus = payInfo.getPayStatus().toString();
		}
		map.put("transactionId", transactionId);
		map.put("outTradeNo", outTradeNo);
		map.put("money", money);
		map.put("refundStatus", refundStatus);
		map.put("refundTime", refundTime);
		map.put("refundResult", refundResult);
		map.put("payStatus", payStatus);
		return map;
	}

	private String gainResultFromMsg(String result) throws Exception {
		String return_msg = "";
		if (StringUtils.isNotBlank(result)) {
			if (isXML(result)) {
				Document doc = DocumentHelper.parseText(result);
				Element rootElt = doc.getRootElement(); // 获取根节点
				return_msg = rootElt.elementText("return_msg");
			} else {
				return_msg = result;
			}
		}
		return return_msg;
	}

	@Autowired
	private CommonManager commonManager;

	@SuppressWarnings("unchecked")
	public String gainvedioorderdatas(Map<String, String> paramMap)
			throws Exception {
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String search = paramMap.get("search");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer type = Integer.parseInt(paramMap.get("ostatus"));
		Map<String, Object> retmap = weixinService.gainvedioorderdatas(search,
				start, length, type);
		Integer renum = (Integer) retmap.get("num");
		List<BusinessVedioOrder> orders = (List<BusinessVedioOrder>) retmap
				.get("items");
		StringBuilder stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		BusinessVedioOrder order = null;
		for (int i = 0; i < orders.size(); i++) {
			order = orders.get(i);
			List<BusinessPayInfo> payinfos = wenzhenService
					.queryBusinesPayInfosByOId(order.getId(), 4);
			BigDecimal money = (BigDecimal) payinfo(payinfos).get("money");
			String _money = "";
			if (money == null) {
				if (order.getExpertId() != null) {
					money = commonManager.gainMoneyByOrder(4,
							order.getExpertId());
					if (money != null)
						_money = money.toString();
				}
			} else {
				_money = money.toString();
			}
			String _time = gaintime(order);
			long cd = 0;
			if (StringUtils.isNotBlank(_time) && _time.length() > 10) {
				cd = DateUtil.calculateSecond(_time + ":00",
						sdf.format(new Date()));
			}
			stringJson.append("[");
			stringJson.append("\"" + order.getId() + "\",");
			stringJson.append("\"" + order.getExpertId() + "\",");
			stringJson.append("\"" + type + "\",");
			if (cd > 0 && cd >= 1800) {
				stringJson.append("\"" + "1" + "\",");// 超时了
			} else {
				stringJson.append("\"" + "2" + "\",");// 没超时
			}
			stringJson.append("\""
					+ order.getPatientName()
					+ "/"
					+ (order.getSex() != null ? (order.getSex().equals(1) ? "男"
							: "女") : "")
					+ "/"
					+ order.getAge()
					+ "/"
					+ (StringUtils.isNotBlank(order.getTelephone()) ? order
							.getTelephone() : "") + "\",");// 患者信息
			String exjson = "";
			exjson += (order.getExpertId() != null ? (StringUtils
					.isNotBlank(order.getExpertName()) ? order.getExpertName()
					: "") : "待分配");
			exjson += (StringUtils.isNotBlank(order.getHosName()) ? "/"
					+ order.getHosName() : "");
			exjson += (StringUtils.isNotBlank(order.getDepName()) ? "/"
					+ order.getDepName() : "");
			exjson += (StringUtils.isNotBlank(order.getExProfession()) ? "/"
					+ order.getExProfession() : "");
			stringJson.append("\"" + exjson + "\",");// 专家信息
			String docjson = "";
			docjson += (StringUtils.isNotBlank(order.getLocalDocName()) ? order
					.getLocalDocName() : "");
			docjson += (StringUtils.isNotBlank(order.getLocalHosName()) ? "/"
					+ order.getLocalHosName() : "");
			docjson += (StringUtils.isNotBlank(order.getLocalDepName()) ? "/"
					+ order.getLocalDepName() : "");
			stringJson.append("\"" + docjson + "\",");// 医生信息
			String ctime = sdf.format(order.getCreateTime());
			stringJson.append("\"" + ctime + "\",");// 下单时间
			stringJson.append("\"" + RelativeDateFormat.calculateTimeLoc(ctime)
					+ "\",");// 距离下单时间
			String time = (StringUtils.isNotBlank(order.getConsultationDate()) ? order
					.getConsultationDate() : "")
					+ " "
					+ (StringUtils.isNotBlank(order.getConsultationTime()) ? order
							.getConsultationTime() : "");
			stringJson.append("\"" + time + "\",");// 视频开始时间
			stringJson
					.append("\""
							+ (StringUtils.isNotBlank(time) ? (time
									.contains(":") ? (RelativeDateFormat
									.calculateTimeLoc(time)) : "") : "")
							+ "\",");// 距离开始时间
			stringJson.append("\"" + _money + "\",");// 费用
			stringJson.append("\""
					+ (order.getPayStatus() != null ? (order.getPayStatus()
							.equals(1) ? "已支付" : "未支付") : "未支付") + "\",");// 支付状态
			stringJson.append("\""
					+ ((money != null && order.getPayStatus() != null && order
							.getPayStatus().equals(1)) ? gainRefundDesc(order
							.getRefundStatus()) : "") + "\",");
			stringJson.append("\""
					+ (StringUtils.isNotBlank(order.getRefundTime()) ? order
							.getRefundTime() : "") + "\",");
			stringJson.append("\"\"");// 操作
			stringJson.append("],");
		}
		if (orders.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}

	private String gaintime(BusinessVedioOrder rc) {
		String time = "";
		if (StringUtils.isNotBlank(rc.getConsultationDate())) {
			time += rc.getConsultationDate();
		}
		if (StringUtils.isNotBlank(rc.getConsultationTime())) {
			time += " " + rc.getConsultationTime();
		}
		return time;
	}

	@SuppressWarnings("unchecked")
	public String gaindtdtuwenorderdatas(Map<String, String> paramMap)
			throws Exception {
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String search = paramMap.get("search");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		StringBuilder stringJson = null;
		Integer type = Integer.parseInt(paramMap.get("ostatus"));
		Map<String, Object> retmap = commonService
				.querySpecialAdviceOrdersByCondition_new(type, search, start,
						length);
		Integer renum = (Integer) retmap.get("num");
		List<SpecialAdviceOrder> rcs = (List<SpecialAdviceOrder>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":[");
		SpecialAdviceOrder order = null;
		for (int i = 0; i < rcs.size(); i++) {
			order = rcs.get(i);
			List<BusinessPayInfo> payinfos = wenzhenService
					.queryBusinesPayInfosByOId(order.getId(), 5);
			BigDecimal money = (BigDecimal) payinfo(payinfos).get("money");
			String _money = "";
			if (money == null) {
				if (order.getExpertId() != null) {
					money = commonManager.gainMoneyByOrder(5,
							order.getExpertId());
					if (money != null)
						_money = money.toString();
				}
			} else {
				_money = money.toString();
			}
			stringJson.append("[");
			stringJson.append("\"" + order.getId() + "\",");
			stringJson.append("\""
					+ order.getUserName()
					+ "/"
					+ (order.getSex() != null ? (order.getSex().equals(1) ? "男"
							: "女") : "")
					+ "/"
					+ order.getAge()
					+ (StringUtils.isNotBlank(order.getTelephone()) ? order
							.getTelephone() : "") + "\",");// 患者信息
			String exjson = "";
			exjson += (StringUtils.isNotBlank(order.getExpertName()) ? order
					.getExpertName() : "待分配");
			exjson += (StringUtils.isNotBlank(order.getHosName()) ? "/"
					+ order.getHosName() : "");
			exjson += (StringUtils.isNotBlank(order.getDepName()) ? "/"
					+ order.getDepName() : "");
			exjson += (StringUtils.isNotBlank(order.getExProfession()) ? "/"
					+ order.getExProfession() : "");
			stringJson.append("\"" + exjson + "\",");
			String docjson = "";
			docjson += (StringUtils.isNotBlank(order.getLocalDocName()) ? order
					.getLocalDocName() : "待分配");
			docjson += (StringUtils.isNotBlank(order.getLocalHosName()) ? "/"
					+ order.getLocalHosName() : "");
			docjson += (StringUtils.isNotBlank(order.getLocalDepName()) ? "/"
					+ order.getLocalDepName() : "");
			docjson += (StringUtils.isNotBlank(order.getLocalProfession()) ? "/"
					+ order.getLocalProfession()
					: "");
			stringJson.append("\"" + docjson + "\",");
			stringJson.append("\"" + sdf.format(order.getCreateTime()) + "\",");
			stringJson.append("\""
					+ RelativeDateFormat.calculateTimeLoc(sdf.format(order
							.getCreateTime())) + "\",");
			stringJson.append("\""
					+ (order.getReceiveTime() != null ? sdf.format(order
							.getReceiveTime()) : "") + "\",");
			stringJson
					.append("\""
							+ (order.getReceiveTime() != null ? RelativeDateFormat
									.calculateTimeLoc(sdf.format(order
											.getReceiveTime())) : "") + "\",");
			stringJson.append("\"" + _money + "\",");
			stringJson.append("\""
					+ (order.getPayStatus() != null ? (order.getPayStatus()
							.equals(1) ? "已支付" : "未支付") : "") + "\",");
			stringJson.append("\""
					+ ((money != null && (money.compareTo(BigDecimal.ZERO) > 0)
							&& order.getPayStatus() != null && order
							.getPayStatus().equals(1)) ? gainRefundDesc(order
							.getRefundStatus()) : "") + "\",");
			stringJson.append("\""
					+ (StringUtils.isNotBlank(order.getRefundTime()) ? order
							.getRefundTime() : "") + "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (rcs.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}

	private String gainRefundDesc(Integer status) {
		String desc = "";
		if (status == null || status.equals(0)) {
			desc = "待退款";
		} else if (status.equals(1)) {
			desc = "退款成功";
		} else if (status.equals(-1)) {
			desc = "退款失败";
		}
		return desc;
	}

	/**
	 * 获取医生团队成员信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public String gainDoctorTeamMemberDatas(Map<String, String> paramMap) {
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		StringBuilder stringJson = null;
		Integer teamId = Integer.parseInt(paramMap.get("teamId"));
		Map<String, Object> retmap = commonService.queryDoctorTeamMemberDatas(
				teamId, searchContent, start, length);
		Integer renum = (Integer) retmap.get("num");
		List<DoctorTeamMember> members = (List<DoctorTeamMember>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":[");
		DoctorTeamMember member = null;
		for (int i = 0; i < members.size(); i++) {
			member = members.get(i);
			stringJson.append("[");
			stringJson.append("\"" + member.getId() + "\",");
			stringJson.append("\""
					+ member.getDocName()
					+ (StringUtils.isNotBlank(member.getDocDuty()) ? "/"
							+ member.getDocDuty() : "")
					+ (StringUtils.isNotBlank(member.getDocProfession()) ? "/"
							+ member.getDocProfession() : "") + "\",");
			stringJson.append("\""
					+ (StringUtils.isNotBlank(member.getHosName()) ? member
							.getHosName() : "") + "\",");
			stringJson.append("\""
					+ (StringUtils.isNotBlank(member.getDepName()) ? member
							.getDepName() : "") + "\",");
			stringJson.append("\"" + gainRoleStr(member.getRole()) + "\",");
			stringJson.append("\"" + sdf.format(member.getCreateTime()) + "\"");
			stringJson.append("],");
		}
		if (members.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}

	private String gainRoleStr(Integer role) {
		String roleStr = "";
		switch (role) {
		case 1:
			roleStr = "团队负责人";
			break;
		case 2:
			roleStr = "特约专家";
			break;
		case 3:
			roleStr = "普通成员";
			break;
		case 4:
			roleStr = "团队助理";
			break;
		default:
			break;
		}
		return roleStr;
	}

	/**
	 * 获取群组数据
	 * 
	 * @param paramMap
	 */
	@SuppressWarnings("unchecked")
	public String gainGroupDatas(Map<String, String> paramMap) {
		String sEcho = paramMap.get("sEcho");
		String search = paramMap.get("search");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus = Integer.parseInt(paramMap.get("ostatus"));
		StringBuilder stringJson = null;
		Integer groupType = Integer.parseInt(paramMap.get("groupType"));// 群组类型
																		// 1:订单;2:Team;3:医院;4:区域科室;5自主创建
		Map<String, Object> retmap = commonService.queryGroups(ostatus,
				groupType, search, start, length);
		Integer renum = (Integer) retmap.get("num");
		List<GroupInfoDto> groups = (List<GroupInfoDto>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":");
		stringJson.append(net.sf.json.JSONArray.fromObject(groups).toString());
		stringJson.append("}");
		return stringJson.toString();
	}

	/**
	 * 获取群组成员数据
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String gainGroupMemberDatas(Map<String, String> paramMap) {
		String sEcho = paramMap.get("sEcho");
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		StringBuilder stringJson = null;
		Integer groupId = Integer.parseInt(paramMap.get("groupId"));// 群组Id
		Map<String, Object> retmap = commonService.gainGroupMemberDatas(
				groupId, searchContent, start, length);
		Integer renum = (Integer) retmap.get("num");
		List<GroupMemberInfoDto> groups = (List<GroupMemberInfoDto>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":");
		stringJson.append(net.sf.json.JSONArray.fromObject(groups).toString());
		stringJson.append("}");
		return stringJson.toString();
	}

	/**
	 * 运营人员医院数据
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String operatorhosdatas(Map<String, String> paramMap) {
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer opId = Integer.parseInt(paramMap.get("opId"));
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.operatorhosdatas(
				searchContent, opId, start, length);
		Integer renum = (Integer) retmap.get("num");
		List<HospitalDetailInfo> hospitals = (List<HospitalDetailInfo>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":[");
		HospitalDetailInfo hos = null;
		for (int i = 0; i < hospitals.size(); i++) {
			hos = hospitals.get(i);
			stringJson.append("[");
			stringJson.append("\"" + hos.getId() + "\",");
			stringJson.append("\"" + hos.getRelativeId() + "\",");
			stringJson.append("\"" + hos.getDistName() + "\",");
			stringJson.append("\"" + hos.getDisplayName() + "\",");
			stringJson.append("\"" + sdf.format(hos.getCreateTime()) + "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (hospitals.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}

	/**
	 * 创建群组
	 * 
	 * @param request
	 */
	public void createGroup(Integer uid, Integer utype,
			HttpServletRequest request) {
		String groupName = request.getParameter("groupName");// 群组名称
		String distCode = request.getParameter("distCode");// 区域
		String depId = request.getParameter("depId");// 标准科室id
		String hospitalId = request.getParameter("hospitalId");// 医院id
		Integer groupType = Integer.parseInt(request.getParameter("groupType"));
		RongCloudGroup group = new RongCloudGroup();
		String groupUuid = UUID.randomUUID().toString().replace("-", "");
		group.setGroupUuid(groupUuid);
		group.setGroupName(groupName);
		group.setGroupType(groupType);
		group.setCreatorId(uid);
		group.setCreatorType(utype);
		group.setStatus(1);
		group.setCreateTime(new Timestamp(System.currentTimeMillis()));
		if (StringUtils.isNotBlank(distCode)) {
			group.setDistCode(distCode);
		}
		if (StringUtils.isNotBlank(depId)) {
			group.setStandardDepId(Integer.parseInt(depId));
		}
		if (StringUtils.isNotBlank(hospitalId)) {
			group.setHospitalId(Integer.parseInt(hospitalId));
		}
		commonService.saveRongCloudGroup(group);
		// 创建群组成员 系统创建 角色管理员
		createGroupMember(groupName, groupUuid, groupType, uid, utype, 1);
	}

	/**
	 * 创建群组成员，并加入融云群组
	 * 
	 * @param groupName
	 * @param groupUuid
	 * @param groupType
	 * @param userId
	 * @param userType
	 * @param role
	 */
	private void createGroupMember(String groupName, String groupUuid,
			Integer groupType, Integer userId, Integer userType, Integer role) {
		if (groupType.equals(4)) {
			RongCloudGroupMember member = new RongCloudGroupMember();
			member.setGroupUuid(groupUuid);
			member.setGroupType(groupType);
			member.setCreateTime(new Timestamp(System.currentTimeMillis()));
			member.setUserId(userId);
			member.setUserType(userType);
			member.setStatus(1);
			member.setRole(role);
			commonService.saveRongCloudGroupMember(member);
			String member_uid = RongCloudApi.getRongCloudUserId(userId,
					userType);
			RongCloudApi.joinGroup(new String[] { member_uid }, groupUuid,
					groupName);
		}
	}

	/**
	 * 解散或生效群组
	 * 
	 * @param groupId
	 */
	public void disSolveGroup(Integer groupId, Integer otype, Integer userId,
			Integer userType) {
		RongCloudGroup group = commonService.queryRongCloudGroupById(groupId);
		if (otype.equals(1)) {
			group.setStatus(0);// 解散
		} else if (otype.equals(2)) {
			group.setStatus(1);// 生效
		}
		commonService.updateRongCloudGroup(group);
		List<RongCloudGroupMember> groupMembers = commonService
				.queryRongCloudGroupMembersByGroupUuid(group.getGroupUuid());
		if (otype.equals(1)) {
			String rong_uid = RongCloudApi.getRongCloudUserId(userId, userType);
			RongCloudApi.dismissGroup(rong_uid, group.getGroupUuid());// 解散群组
			for (RongCloudGroupMember member : groupMembers) {
				member.setStatus(0);
				commonService.updateRongCloudGroupMember(member);
			}
		} else if (otype.equals(2)) {
			// 使群组生效 将之前的群组成员加入
			List<String> users = new ArrayList<String>();
			for (RongCloudGroupMember member : groupMembers) {
				member.setStatus(1);
				commonService.updateRongCloudGroupMember(member);
				String rong_uid = RongCloudApi.getRongCloudUserId(
						member.getUserId(), member.getUserType());
				users.add(rong_uid);
			}
			String[] userids = users.toArray(new String[] {});
			RongCloudApi.joinGroup(userids, group.getGroupUuid(),
					group.getGroupName());
		}
	}

	/**
	 * 踢出群组
	 * 
	 * @param memberId
	 */
	public void removeGroupMember(Integer memberId) {
		RongCloudGroupMember member = commonService
				.queryRongCloudGroupMembersById(memberId);
		RongCloudGroup group = commonService
				.queryRongCloudGroupByGroupUuid(member.getGroupUuid());
		commonService.delRongCloudGroupMember(memberId);
		RongCloudApi.quitGroup(
				new String[] { RongCloudApi.getRongCloudUserId(
						member.getUserId(), member.getUserType()) },
				group.getGroupUuid());
	}

	public void distHospitalToOp(Integer opId, Integer hospitalId) {
		MobileSpecial doc = commonService
				.queryMobileSpecialByUserIdAndUserType(opId);
		HospitalMaintainerRelation hmr = new HospitalMaintainerRelation();
		hmr.setCreateTime(new Timestamp(System.currentTimeMillis()));
		hmr.setHospitalId(hospitalId);
		hmr.setUserId(doc.getSpecialId());
		hmr.setUserType(doc.getUserType());
		commonService.saveHospitalMaintainerRelation(hmr);
	}

	/**
	 * 获取会诊案例数据
	 * 
	 * @param paramMap
	 * @return
	 */
	public String sysconcasedatas(Map<String, String> paramMap) {
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String search = paramMap.get("search");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus = Integer.parseInt(paramMap.get("ostatus"));
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService
				.querySystemConsultationCaseDatas(search, ostatus, start,
						length);
		Integer renum = (Integer) retmap.get("num");
		List<PlatformHealthConsultation> cases = (List<PlatformHealthConsultation>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":[");
		PlatformHealthConsultation cinfo = null;
		for (int i = 0; i < cases.size(); i++) {
			cinfo = cases.get(i);
			stringJson.append("[");
			stringJson.append("\"" + cinfo.getId() + "\",");
			stringJson.append("\"" + cinfo.getTitle() + "\",");
			stringJson.append("\"" + sdf.format(cinfo.getCreateTime()) + "\",");
			stringJson.append("\"" + cinfo.getTypeName() + "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (cases.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}

	/**
	 * 医生团队审核通过后 创建群组
	 * 
	 * @param dt
	 */
	public void processDoctorTeamAudit(DoctorTeam dt) {
		if (dt.getStatus().equals(1)) {
			// 审核通过 创建群组 及成员
			RongCloudGroup group = new RongCloudGroup();
			group.setCreateTime(new Timestamp(System.currentTimeMillis()));
			group.setGroupUuid(dt.getUuid());
			group.setGroupType(2);
			group.setGroupName(dt.getTeamName());
			group.setCreatorId(OrderStatusEnum.SYSTEM_CLOSED_ID.getKey());
			group.setCreatorType(OrderStatusEnum.SYSTEM_CLOSED_TYPE.getKey());
			group.setStatus(1);
			group.setLogoUrl(dt.getLogoUrl());
			commonService.saveRongCloudGroup(group);
			List<DoctorTeamMember> members = commonService
					.queryDoctorTeamMembersByTeamId(dt.getId());
			List<String> users = new ArrayList<String>();
			for (DoctorTeamMember member : members) {
				RongCloudGroupMember groupMember = new RongCloudGroupMember();
				groupMember.setGroupUuid(group.getGroupUuid());
				groupMember.setGroupType(group.getGroupType());
				groupMember.setCreateTime(new Timestamp(System
						.currentTimeMillis()));
				groupMember.setUserId(member.getDoctorId());
				groupMember.setUserType(member.getDoctorType());
				groupMember.setStatus(1);
				if (member.getRole().equals(1)) {
					groupMember.setRole(1);
				} else {
					groupMember.setRole(2);
				}
				commonService.saveRongCloudGroupMember(groupMember);
				String rong_uid = RongCloudApi.getRongCloudUserId(
						groupMember.getUserId(), groupMember.getUserType());
				users.add(rong_uid);
			}
			if (users != null && users.size() > 0) {
				String[] userids = users.toArray(new String[] {});
				RongCloudApi.joinGroup(userids, group.getGroupUuid(),
						group.getGroupName());
			}
		}
	}

	public void saveorupdateSysConCaseInfo(String sscId, String title,
			String summary, String backImage, String textLink, Integer tagType) {
		PlatformHealthConsultation ssc = null;
		if (!StringUtils.isNotBlank(sscId)) {
			// 新增
			ssc = new PlatformHealthConsultation();
			ssc.setCreateTime(new Timestamp(System.currentTimeMillis()));
			ssc.setTagType(tagType);
			ssc.setBackImage(backImage);
			ssc.setTitle(title);
			ssc.setSummary(summary);
			ssc.setDetailType(null);
			ssc.setTextLink(textLink);
			ssc.setHtmlContent(null);
			ssc.setIssuerId(null);
			ssc.setIssuerType(null);
			ssc.setReadNum(null);
			ssc.setLikeNum(null);
			ssc.setStatus(1);
			commonService.saveSystemConsultationCase(ssc);
		} else {
			// 修改
			ssc = commonService.querySystemConsultationCaseById(Integer
					.parseInt(sscId));
			ssc.setCreateTime(new Timestamp(System.currentTimeMillis()));
			ssc.setTagType(tagType);
			ssc.setBackImage(backImage);
			ssc.setTitle(title);
			ssc.setSummary(summary);
			ssc.setTextLink(textLink);
			ssc.setStatus(1);
			commonService.updateSystemConsultationCase(ssc);
		}
	}

	/**
	 * 手动退款
	 * 
	 * @param oid
	 * @param otype
	 * @return
	 */
	public Map<String, Object> refund(Integer oid, Integer otype)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		BusinessPayInfo pay = commonService.queryBusinessPayInfoByCondition(
				oid, otype);
		if (pay != null) {
			if (pay.getRefundStatus() != null
					&& pay.getRefundStatus().equals(1)) {
				map.put("status", "error");
				map.put("message", "已退款，不需再次退款");
			} else {
				String finalmoney = pay.getTotalMoney().toString();
				finalmoney = WeixinUtil.changeY2F(finalmoney);
				String out_refund_no = "YW" + refund_sdf.format(new Date());
				String refundResult = WeixinUtil.toRefund(pay.getOutTradeNo(),
						out_refund_no, finalmoney, finalmoney,
						pay.getTradeType());
				if (StringUtils.isNotBlank(refundResult)) {
					BusinessPayInfo _pinfo = commonService
							.queryBusinessPayInfoByTradeNo(pay.getOutTradeNo());
					_pinfo.setRefundTime(new Timestamp(System
							.currentTimeMillis()));
					_pinfo.setOutRefundResult(refundResult);
					_pinfo.setRefundStatus(gainRefundStatus(refundResult));
					commonService.updateBusinessPayInfo(_pinfo);
					try {
						processRefundBill(_pinfo);
						map.put("status", "success");
						map.put("message", "退款成功");
					} catch (Exception e) {
						TransactionAspectSupport.currentTransactionStatus()
								.setRollbackOnly();
						map.put("status", "error");
						map.put("message", "退款失败");
					}
				} else {
					map.put("status", "error");
					map.put("message", "退款失败");
				}
			}
		} else {
			map.put("status", "error");// 未找到已支付记录，不能退款
			map.put("message", "未找到已支付记录，不能退款");
		}
		return map;
	}

	private void processRefundBill(BusinessPayInfo pinfo) {
		if (pinfo.getRefundStatus() != null
				&& pinfo.getRefundStatus().equals(1)) {
			Map<String, Object> _ret = gainOrderInfo(pinfo);
			if (pinfo.getOrderType().equals(4)
					|| pinfo.getOrderType().equals(5)) {
				if (pinfo.getFromTye() != null && pinfo.getFromTye().equals(1)) {
					// 患者支付
					savePatientBill(_ret.get("orderUuid").toString(),
							pinfo.getOrderType(), Integer.parseInt(_ret.get(
									"userId").toString()),
							Integer.parseInt(_ret.get("doctorId").toString()),
							pinfo.getOnlinePay(), _ret.get("content")
									.toString(),
							_ret.get("content").toString(), "", 1);
				}
			} else {
				savePatientBill(_ret.get("orderUuid").toString(),
						pinfo.getOrderType(),
						Integer.parseInt(_ret.get("userId").toString()),
						Integer.parseInt(_ret.get("doctorId").toString()),
						pinfo.getOnlinePay(), _ret.get("content").toString(),
						_ret.get("content").toString(), "", 1);
			}
		}
	}

	public void savePatientBill(String orderUuid, Integer otype, Integer uid,
			Integer docid, BigDecimal money, String opAction, String content,
			String remark, Integer type) {
		UserBillRecord bill = new UserBillRecord();
		bill.setUserId(uid);
		bill.setDoctorId(docid);
		bill.setBusinessId(orderUuid);
		bill.setBusinessType(otype);
		bill.setMoney(money);
		bill.setOpAction(opAction);
		bill.setOpTime(new Date());
		bill.setContent(content);
		bill.setRemark(remark);
		bill.setType(type);
		wenzhenService.saveUserBillRecord(bill);
	}

	private Map<String, Object> gainOrderInfo(BusinessPayInfo pinfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer userId = null;
		Integer doctorId = null;
		String content = "";
		String orderUuid = "";
		switch (pinfo.getOrderType()) {
		case 4:
			BusinessVedioOrder vedioOrder = wenzhenService
					.queryBusinessVedioOrderById(pinfo.getOrderId());
			userId = vedioOrder.getUserId();
			doctorId = vedioOrder.getExpertId();
			orderUuid = vedioOrder.getUuid();
			content = "视频会诊";
			break;
		case 5:
			SpecialAdviceOrder d2dTuwenOrder = wenzhenService
					.querySpecialAdviceOrderById(pinfo.getOrderId());
			userId = d2dTuwenOrder.getUserId();
			doctorId = d2dTuwenOrder.getExpertId();
			orderUuid = d2dTuwenOrder.getUuid();
			content = "图文会诊";
			break;
		case 6:
			BusinessD2pTuwenOrder d2pTuwenOrder = d2pService
					.queryd2ptuwenorderbyid(pinfo.getOrderId());
			userId = d2pTuwenOrder.getUserId();
			doctorId = d2pTuwenOrder.getDoctorId();
			orderUuid = d2pTuwenOrder.getUuid();
			content = "图文问诊";
			break;
		case 7:
			BusinessD2pTelOrder d2pTelOrder = d2pService
					.queryd2ptelorderbyid(pinfo.getOrderId());
			userId = d2pTelOrder.getUserId();
			doctorId = d2pTelOrder.getDoctorId();
			orderUuid = d2pTelOrder.getUuid();
			content = "电话问诊";
			break;
		case 9:
			BusinessD2pFastaskOrder fastaskOrder = d2pService
					.queryd2pfastaskorderbyid(pinfo.getOrderId());
			userId = fastaskOrder.getUserId();
			doctorId = fastaskOrder.getDoctorId();
			orderUuid = fastaskOrder.getUuid();
			content = "快速问诊";
			break;
		case 12:
			BusinessT2pTuwenOrder t2pTuwenOrder = d2pService
					.querybusinesst2ptuwenById(pinfo.getOrderId());
			userId = t2pTuwenOrder.getUserId();
			doctorId = t2pTuwenOrder.getDoctorId();
			orderUuid = t2pTuwenOrder.getUuid();
			content = "团队问诊";
			break;
		}
		map.put("userId", userId);
		map.put("doctorId", doctorId);
		map.put("content", content);
		map.put("orderUuid", orderUuid);
		return map;
	}

	private Integer gainRefundStatus(String result) throws Exception {
		Document doc = DocumentHelper.parseText(result);
		Element rootElt = doc.getRootElement(); // 获取根节点
		String return_code = rootElt.elementText("result_code");
		if (return_code.equalsIgnoreCase("SUCCESS")) {
			return 1;
		}
		return -1;
	}

	private String gainTransactionId(String result) throws Exception {
		String transaction_id = "";
		if (StringUtils.isNotBlank(result)) {
			Document doc = DocumentHelper.parseText(result);
			Element rootElt = doc.getRootElement(); // 获取根节点
			transaction_id = rootElt.elementText("transaction_id");
		}
		return transaction_id;
	}

	private boolean isXML(String value) {
		try {
			if (StringUtils.isNotBlank(value)) {
				DocumentHelper.parseText(value);
			} else {
				return false;
			}
		} catch (DocumentException e) {
			return false;
		}
		return true;
	}

	private Integer gainlevel(Integer hosLevel) {
		Integer level = null;
		switch (hosLevel) {
		case 10:
		case 11:
		case 12:
			level = 1;
			break;
		case 13:
		case 14:
		case 15:
			level = 2;
			break;
		case 16:
		case 17:
		case 18:
			level = 3;
			break;
		}
		return level;
	}

	private String getPercent(int x, int total) {
		Format format = new DecimalFormat("0.0");
		return format.format(((double) x / total) * 100);
	}

	public void updateVedioorderRemrk(Integer id, String remark) {
		// TODO Auto-generated method stub
		BusinessVedioOrder vodio = null;
		// vodio=weixinService.vedioorderRemrk(sta,caseId);
		vodio = commonService.queryBusinessVedioById(id);
		vodio.setRemark(remark);
		commonService.updateVedioorderRemrk(vodio);
	}

	public void saveDoctorBillInfo(DoctorRegisterInfo reg,
			BigDecimal actualMoney, BigDecimal originalMoney,
			BigDecimal curAccount, BigDecimal preAccount) {
		DoctorBillInfo docbill = new DoctorBillInfo();
		docbill.setUuid(UUIDUtil.getUUID());
		docbill.setDoctorId(reg.getId());
		docbill.setCreateTime(new Timestamp(System.currentTimeMillis()));
		docbill.setSource("奖励金");
		docbill.setType(1);
		docbill.setActualMoney(actualMoney);
		docbill.setOriginalMoney(originalMoney);
		docbill.setTaxationMoney(null);
		docbill.setSubsidyMoney(null);
		docbill.setCurAccount(curAccount);
		docbill.setPreAccount(preAccount);
		docbill.setRemark(null);
		docbill.setBusinessId(null);
		docbill.setBusinessType(null);
		docbill.setTeamUuid(null);
		commonService.saveDoctorBillInfo(docbill);

	}

	/**
	 * 踢出团队群组
	 * 
	 * @param memberId
	 * @param userId
	 */
	public void removeDocTeamGroupMember(Integer memberId) {
		RongCloudGroupMember member = commonService
				.queryRongCloudGroupMembersById(memberId);
		RongCloudGroup group = commonService
				.queryRongCloudGroupByGroupUuid(member.getGroupUuid());
		commonService.delRongCloudGroupMember(memberId);
		RongCloudApi.quitGroup(
				new String[] { RongCloudApi.getRongCloudUserId(
						member.getUserId(), member.getUserType()) },
				group.getGroupUuid());
		DoctorTeamMember docmembers = commonService
				.queryDoctorTeamMembersByDocId(member.getUserId());
		commonService.delDoctorTeamMembers(docmembers.getId());
	}

	/**
	 * 获取医生团队群组成员数据
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String gainDocTeamGroupMemberDatas(Map<String, String> paramMap) {
		String sEcho = paramMap.get("sEcho");
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		StringBuilder stringJson = null;
		Integer groupId = Integer.parseInt(paramMap.get("groupId"));// 群组Id
		Map<String, Object> retmap = commonService.gainDocTeamGroupMemberDatas(
				groupId, searchContent, start, length);
		Integer renum = (Integer) retmap.get("num");
		List<GroupMemberInfoDto> groups = (List<GroupMemberInfoDto>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":");
		stringJson.append(net.sf.json.JSONArray.fromObject(groups).toString());
		stringJson.append("}");
		return stringJson.toString();
	}

	public void disSolveDocTeamGroup(Integer groupId, Integer otype,
			Integer userId, Integer userType) {
		// TODO Auto-generated method stub
		RongCloudGroup group = commonService.queryRongCloudGroupById(groupId);
		if (otype.equals(1)) {
			group.setStatus(0);// 解散
		}
		commonService.updateRongCloudGroup(group);
		RongCloudGroupPostRelation grouppost = commonService
				.queryRongCloudGroupPostRelation(group.getGroupUuid());
		if (null != grouppost) {
			if (otype.equals(1)) {
				grouppost.setStatus(0);// 解散
			}
			commonService.updateRongCloudGroupPostRelation(grouppost);
		}
		List<RongCloudGroupMember> groupMembers = commonService
				.queryRongCloudGroupMembersByGroupUuid(group.getGroupUuid());
		if (otype.equals(1)) {
			String rong_uid = RongCloudApi.getRongCloudUserId(userId, userType);
			RongCloudApi.dismissGroup(rong_uid, group.getGroupUuid());// 解散群组
			for (RongCloudGroupMember member : groupMembers) {
				member.setStatus(0);
				commonService.updateRongCloudGroupMember(member);
			}
		}
		// 删除团队成员
		List<DoctorTeamMember> members = commonService
				.queryDoctorTeamMembers(group.getGroupUuid());
		if (members != null && members.size() > 0) {
			for (DoctorTeamMember doctorTeamMember : members) {
				commonService.delDoctorTeamMembers(doctorTeamMember.getId());
			}
		}
		// 删除团队deflag==1
		DoctorTeam docteam = commonService.queryDoctorTeamByuuid(group
				.getGroupUuid());
		docteam.setDeFlag(1);
		commonService.updateDoctorTeam(docteam);
		// 删除团队请求数据
		commonService.delDocTeamRequest(group.getGroupUuid());
	}

	/**
	 * 提交医生线下入账
	 * 
	 * @param doctorId
	 * @param amount
	 * @param amountType
	 * @param operator
	 * @return
	 */
	public Map<String, Object> subDocImcome(Integer doctorId, String amount,
			String amountType, String operator) {
		Map<String, Object> map = new HashMap<>();
		DoctorRegisterInfo reg = commonService
				.queryDoctorRegisterInfoById(doctorId);
		BigDecimal originalMoney = BigDecimal.valueOf(Float.valueOf(amount));
		BigDecimal preAccount = (BigDecimal) (reg.getBalance() == null ? new BigDecimal(
				0.00) : reg.getBalance());// 收支前余额
		BigDecimal curAccount;// 当前账户余额
		curAccount = preAccount.add(originalMoney).setScale(2,
				BigDecimal.ROUND_HALF_UP);
		saveDoctorBillInfo(reg, originalMoney, originalMoney, curAccount,
				preAccount, amountType, operator);
		reg.setBalance(preAccount.add(originalMoney));
		commonService.updateDoctorRegisterInfo(reg);
		// 短信发送
		if (StringUtils.isNotBlank(reg.getMobileNumber())) {
			String content = "您好！您的账户已收入" + originalMoney
					+ "元，请登陆佰医汇医生版点击(我的)，在(我的收入)中查看。【佰医汇】";
			HttpSendSmsUtil.sendSmsInteface(reg.getMobileNumber(), content);
		}
		// 个推推送
		String content = "您好！您的账户已收入" + originalMoney + "元，请点击【我的】，在【我的收入】中查看。";
		commonManager.generateSystemPushInfo(211, null, null, doctorId,
				reg.getUserType(), null, content);
		return map;
	}

	public void saveDoctorBillInfo(DoctorRegisterInfo reg,
			BigDecimal actualMoney, BigDecimal originalMoney,
			BigDecimal curAccount, BigDecimal preAccount, String source,
			String extend) {
		// TODO Auto-generated method stub
		DoctorBillInfo docbill = new DoctorBillInfo();
		docbill.setUuid(UUIDUtil.getUUID());
		docbill.setDoctorId(reg.getId());
		docbill.setCreateTime(new Timestamp(System.currentTimeMillis()));
		docbill.setSource(source);
		docbill.setType(1);
		docbill.setActualMoney(actualMoney);
		docbill.setOriginalMoney(originalMoney);
		docbill.setTaxationMoney(null);
		docbill.setSubsidyMoney(null);
		docbill.setCurAccount(curAccount);
		docbill.setPreAccount(preAccount);
		docbill.setRemark(null);
		docbill.setBusinessId(null);
		docbill.setBusinessType(null);
		docbill.setExtend(extend);
		commonService.saveDoctorBillInfo(docbill);

	}

	/* *
	 * 获取签约医生的数据
	 * 
	 * @param paramMap
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String docprivatedatas(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		String sEcho = paramMap.get("sEcho");
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus = Integer.parseInt(paramMap.get("ostatus"));
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.docprivatedatas(ostatus,
				searchContent, start, length);
		Integer renum = (Integer) retmap.get("num");
		List<BusinessD2pPrivateOrderDto> order = (List<BusinessD2pPrivateOrderDto>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":");
		stringJson.append(net.sf.json.JSONArray.fromObject(order).toString());
		stringJson.append("}");
		return stringJson.toString();
	}

	/**
	 * 
	 * 获取签约医生团队的数据
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String docteamvipdatas(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		String sEcho = paramMap.get("sEcho");
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus = Integer.parseInt(paramMap.get("ostatus"));
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.docteamvipdatas(ostatus,
				searchContent, start, length);
		Integer renum = (Integer) retmap.get("num");
		List<BusinessT2pVipOrderDto> order = (List<BusinessT2pVipOrderDto>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":");
		stringJson.append(net.sf.json.JSONArray.fromObject(order).toString());
		stringJson.append("}");
		return stringJson.toString();
	}

	@SuppressWarnings("unchecked")
	public String servicedatas(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		String sEcho = paramMap.get("sEcho");
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.servicedatas(searchContent,
				start, length);
		Integer renum = (Integer) retmap.get("num");
		List<SystemServiceInfo> order = (List<SystemServiceInfo>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":");
		stringJson.append(net.sf.json.JSONArray.fromObject(order).toString());
		stringJson.append("}");
		return stringJson.toString();
	}

	public Map<String, Object> saveorupdateServiceInfo(String obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		SystemServiceInfo systemServiceInfo = new SystemServiceInfo();
		JSONObject parseObject = JSONObject.parseObject(obj);
		JSONObject serviceObj = (JSONObject) parseObject.get("serviceObj");
		systemServiceInfo.setServiceName(StringUtils.isBlank(serviceObj
				.getString("serviceName")) ? null : serviceObj
				.getString("serviceName"));
		systemServiceInfo.setUserType(StringUtils.isBlank(serviceObj
				.getString("userType")) ? null : Integer.parseInt(serviceObj
				.getString("userType")));
		systemServiceInfo.setStatus(StringUtils.isBlank(serviceObj
				.getString("status")) ? null : Integer.parseInt(serviceObj
				.getString("status")));
		systemServiceInfo.setServicePrice(StringUtils.isBlank(serviceObj
				.getString("servicePrice")) ? null : new BigDecimal(serviceObj
				.getString("servicePrice")));
		systemServiceInfo.setDescription(StringUtils.isBlank(serviceObj
				.getString("description")) ? null : serviceObj
				.getString("description"));
		systemServiceInfo.setRank(StringUtils.isBlank(serviceObj
				.getString("rank")) ? null : Integer.parseInt(serviceObj
				.getString("rank")));
		systemServiceInfo.setMultiplePackage(StringUtils.isBlank(serviceObj
				.getString("multiplePackage")) ? null : Integer
				.parseInt(serviceObj.getString("multiplePackage")));
		systemServiceInfo.setDoctorDivided(StringUtils.isBlank(serviceObj
				.getString("doctorDivided")) ? null : new BigDecimal(serviceObj
				.getString("doctorDivided")));
		systemServiceInfo.setExpertDivided(StringUtils.isBlank(serviceObj
				.getString("expertDivided")) ? null : new BigDecimal(serviceObj
				.getString("expertDivided")));
		systemServiceInfo.setPlatformDivided(StringUtils.isBlank(serviceObj
				.getString("platformDivided")) ? null : new BigDecimal(
				serviceObj.getString("platformDivided")));
		systemServiceInfo.setRemark(StringUtils.isBlank(serviceObj
				.getString("remark")) ? null : serviceObj.getString("remark"));
		systemServiceInfo.setGroupType(StringUtils.isBlank(serviceObj
				.getString("groupType")) ? null : Integer.parseInt(serviceObj
				.getString("groupType")));
		systemServiceInfo.setImageUrl(StringUtils.isBlank(serviceObj
				.getString("imageUrl")) ? null : serviceObj
				.getString("imageUrl"));
		systemServiceInfo.setImageUrlUnused(StringUtils.isBlank(serviceObj
				.getString("imageUrlUnused")) ? null : serviceObj
				.getString("imageUrlUnused"));
		systemServiceInfo.setServiceNote(StringUtils.isBlank(serviceObj
				.getString("serviceNote")) ? null : serviceObj
				.getString("serviceNote"));
		systemServiceInfo.setServiceDescUrl(StringUtils.isBlank(serviceObj
				.getString("serviceDescUrl")) ? null : serviceObj
				.getString("serviceDescUrl"));
		systemServiceInfo.setPricingParameter(StringUtils.isBlank(serviceObj
				.getString("priceParameter")) ? null : serviceObj
				.getString("priceParameter"));

		JSONArray packageArray = (JSONArray) parseObject.get("packageObj");

		if (StringUtils.isNotBlank(serviceObj.getString("id"))) {
			// 编辑
			systemServiceInfo
					.setId(Integer.parseInt(serviceObj.getString("id")));
			commonService.updateServiceInfo(systemServiceInfo);
			List<SystemServicePackage> systemServicePackages = commonService
					.queryServicePackagesByServiceId(systemServiceInfo.getId());
			commonService.updateServicePackage(systemServiceInfo.getId(),
					packageArray, systemServicePackages);
		} else {
			// 新增
			Integer serviceId = commonService
					.saveServiceInfo(systemServiceInfo);
			SystemServicePackage systemServicePackage = commonService
					.queryServicePackageByServiceId(systemServiceInfo.getId());
			if (null != systemServicePackage) {
				commonService.deleteServicePackage(systemServicePackage
						.getServiceId());
			}
			commonService.saveServicePackage(serviceId, packageArray);
		}
		map.put("status", "success");

		return map;
	}

	@SuppressWarnings("unchecked")
	public String gaininvitDocdatas(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		String sEcho = paramMap.get("sEcho");
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		String invitCode = paramMap.get("invitCode");
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.gaininvitDocdatas(
				searchContent, start, length, invitCode);
		Integer renum = (Integer) retmap.get("num");
		List<MobileSpecial> doclist = (List<MobileSpecial>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":");
		stringJson.append(net.sf.json.JSONArray.fromObject(doclist).toString());
		stringJson.append("}");
		return stringJson.toString();
	}

	@SuppressWarnings("unchecked")
	public String aboutdocsdatas(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		String sEcho = paramMap.get("sEcho");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		String search = paramMap.get("search");
		Map<String, Object> querymap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(search)) {
			querymap.put("search", search);
		}
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.aboutdocsdatas(querymap,
				start, length);
		Integer renum = (Integer) retmap.get("num");
		List<DocAboutDatas> doclist = (List<DocAboutDatas>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":");
		stringJson.append(net.sf.json.JSONArray.fromObject(doclist).toString());
		stringJson.append("}");
		return stringJson.toString();
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> newAddDatas(String queryType) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> categorys = null;
		List<Integer> doctorDatas = new ArrayList<Integer>();
		List<Integer> patientDatas = new ArrayList<Integer>();
		if ("1".equalsIgnoreCase(queryType)) {
			categorys = (List<String>) DateUtil.daysOfCount(12);
			map.put("categorys", categorys);
		} else if ("2".equalsIgnoreCase(queryType)) {
			map.put("categorys", weekdatas.get("categorys"));
			categorys = (List<String>) weekdatas.get("dates");
		}
		// 新增医生
		List<ReSourceBean> docBeans = (List<ReSourceBean>) commonService
				.queryNewAddDoctors(categorys, queryType).get("modata");
		for (ReSourceBean reSourceBean : docBeans) {
			doctorDatas.add(reSourceBean.getCount().intValue());
		}
		// 新增患者
		List<ReSourceBean> patientBeans = (List<ReSourceBean>) commonService
				.queryNewAddPatients(categorys, queryType).get("modata");
		for (ReSourceBean reSourceBean : patientBeans) {
			patientDatas.add(reSourceBean.getCount().intValue());
		}
		map.put("doctorDatas", doctorDatas);
		map.put("patientDatas", patientDatas);
		return map;
	}

	/**
	 * 手动退款
	 * 
	 * @param oid
	 * @param otype
	 * @return
	 */
	public Map<String, Object> refundVip(Integer oid, Integer otype)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		BusinessPayInfo pay = commonService.queryBusinessPayInfoByCondition(
				oid, otype);
		if (pay != null) {
			if (pay.getRefundStatus() != null
					&& pay.getRefundStatus().equals(1)) {
				map.put("status", "error");
				map.put("message", "已退款，不需再次退款");
			} else {
				String finalmoney = "";
				String totalmoney = "";
				if (otype.equals(14)) {
					// 团队vip
					BusinessT2pVipOrderDto t2pVipOrder = commonService
							.queryteamevipByid(oid);
					finalmoney = t2pVipOrder.getReturnMoney().toString();
				} else if (otype.equals(15)) {
					// 医生签约
					BusinessD2pPrivateOrderDto privateOrder = commonService
							.queryprivateOrdersByid(oid);
					finalmoney = privateOrder.getReturnMoney().toString();
				}
				finalmoney = WeixinUtil.changeY2F(finalmoney);
				totalmoney = WeixinUtil.changeY2F(pay.getTotalMoney()
						.toString());
				String out_refund_no = "YW" + refund_sdf.format(new Date());
				String refundResult = WeixinUtil.toRefund_child(
						gainTransactionId(pay.getOutTradeResult()),
						out_refund_no, totalmoney, finalmoney,
						pay.getTradeType());
				if (StringUtils.isNotBlank(refundResult)) {
					BusinessPayInfo _pinfo = commonService
							.queryBusinessPayInfoByTradeNo(pay.getOutTradeNo());
					_pinfo.setRefundTime(new Timestamp(System
							.currentTimeMillis()));
					_pinfo.setOutRefundResult(refundResult);
					_pinfo.setRefundStatus(gainRefundStatus(refundResult));
					commonService.updateBusinessPayInfo(_pinfo);
					map.put("status", "success");
					map.put("message", "退款成功");
				} else {
					map.put("status", "error");
					map.put("message", "退款失败");
				}
			}
		} else {
			map.put("status", "error");// 未找到已支付记录，不能退款
			map.put("message", "未找到已支付记录，不能退款");
		}
		return map;
	}

	/**
	 * 获取订单详情 --通用方法
	 * 
	 * @param orderId
	 * @param orderType
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> orderInfoCommon(Integer orderId,
			Integer orderType) throws Exception {
		Map<String, Object> map = new HashMap<>();
		CommonOrderInfo info = gainCommonOrderInfo(orderId, orderType);
		map.put("orderInfo", info);
		// 1.病例信息
		CaseInfo caseInfo = commonService.queryCaseInfoById(info.getCaseId());
		getAttahments(caseInfo);
		getNormalImages(caseInfo);
		map.put("caseInfo", caseInfo);
		// 2.聊天信息
		List<BusinessMessageBean> msgs = wenzhenService
				.queryBusinessMessageBeansByCon(orderId, info.getOrderUuid(),
						orderType);
		for (BusinessMessageBean _msg : msgs) {
			_msg.setMsgTime(_sdf.format(_msg.getSendTime()));
		}
		map.put("messages", msgs);
		// 3.支付信息
		List<BusinessPayInfo> payinfos = wenzhenService
				.queryBusinesPayInfosByOId(orderId, orderType);
		PayInfo payInfo = processPay(payinfo(payinfos));

		/*
		 * List<BusinessPayInfo> payinfos = wenzhenService
		 * .queryBusinesPayInfosByOId(id, 12); map.putAll(payinfo(payinfos));
		 */

		BigDecimal money = (BigDecimal) payinfo(payinfos).get("money");
		// BigDecimal money = (BigDecimal) map.get("money");
		String _money = null;
		if (money == null) {
			if (info.getExpertId() != null) {
				money = commonManager.gainMoneyByOrder(orderType,
						info.getExpertId());
				if (money != null)
					_money = money.toString();
			}
		} else {
			_money = money.toString();
		}
		payInfo.setMoney(_money);
		map.put("payInfo", payInfo);
		// 4.医生信息
		if (info.getDoctorId() != null) {
			MobileSpecial doc = commonService
					.queryMobileSpecialByUserIdAndUserType(info.getDoctorId());
			map.put("doctorInfo", doc);
		}
		// 5.团队信息
		String teamUuid = info.getTeamUuid();
		DoctorTeam dt = commonService.queryDoctorTeamByUuid(teamUuid);
		map.put("dteam", dt);
		// 判断是否需要退款
		BigDecimal moneys;
		if ((info.getStatus().equals(30) || info.getStatus().equals(50))
				&& info.getPayStatus() != null && info.getPayStatus().equals(1)) {
			if (null != payInfo.getMoney()) {
				moneys = new BigDecimal(payInfo.getMoney().toString());
			} else {
				moneys = new BigDecimal(0);
			}
			if (moneys != null && moneys.compareTo(BigDecimal.ZERO) > 0) {
				Object obj = payInfo.getRefundStatus();
				if (obj != null && Integer.valueOf(obj.toString()).equals(1)) {
					// 退款成功，不需要显示退款
				} else {
					// 待退款或退款失败
					map.put("needRefund", "true");
				}
			}
		}
		// 处理差异
		processDistinct(info, orderType, map);
		return map;
	}

	/**
	 * 处理差异
	 * 
	 * @param info
	 * @param orderType
	 * @param map
	 */
	private void processDistinct(CommonOrderInfo info, Integer orderType,
			Map<String, Object> map) {
		if (orderType.equals(7)) {
			// 电话问诊
			map.put("answerTime", info.getAnswerTime());
			map.put("answerTelephone", info.getAnswerTelephone());
		}
		if (orderType.equals(11) || orderType.equals(12)) {
			DoctorTeam dt = commonService.queryDoctorTeamByUuid(info
					.getTeamUuid());
			map.put("team", dt);
		}
		if (orderType.equals(4) || orderType.equals(5) || orderType.equals(10)) {
			Integer expertId = info.getExpertId();
			if (expertId != null) {
				MobileSpecial expertInfo = commonService
						.queryMobileSpecialByUserIdAndUserType(expertId);
				map.put("expertInfo", expertInfo);
			}
		}
	}

	/**
	 * 获取订单信息
	 * 
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	private CommonOrderInfo gainCommonOrderInfo(Integer orderId,
			Integer orderType) {
		CommonOrderInfo info = new CommonOrderInfo();
		Integer caseId = null;// 病例id
		String orderUuid = "";// 订单uuid
		Integer doctorId = null;// 医生id
		Integer payStatus = null;// 支付状态
		Integer source = null;// 订单来源
		Integer status = null;// 订单状态
		switch (orderType) {
		case 4:
			BusinessVedioOrder vedioOrder = commonService
					.queryBusinessVedioById(orderId);
			caseId = vedioOrder.getCaseId();
			orderUuid = vedioOrder.getUuid();
			doctorId = vedioOrder.getLocalDoctorId();
			payStatus = vedioOrder.getPayStatus();
			source = vedioOrder.getSource();
			status = vedioOrder.getStatus();
			info.setExpertId(vedioOrder.getExpertId());
			break;
		case 5:
			// d2d 图文会诊
			SpecialAdviceOrder d2dTuwen = commonService
					.querySpecialAdviceOrderById(orderId);
			caseId = d2dTuwen.getCaseId();
			orderUuid = d2dTuwen.getUuid();
			doctorId = d2dTuwen.getDoctorId();
			payStatus = d2dTuwen.getPayStatus();
			source = d2dTuwen.getSource();
			status = d2dTuwen.getStatus();
			info.setExpertId(d2dTuwen.getExpertId());
			break;
		case 6:
			// d2p 图文问诊
			BusinessD2pTuwenOrder d2pTuwenOrder = d2pService
					.queryd2ptuwenorderbyid(orderId);
			caseId = d2pTuwenOrder.getCaseId();
			orderUuid = d2pTuwenOrder.getUuid();
			doctorId = d2pTuwenOrder.getDoctorId();
			payStatus = d2pTuwenOrder.getPayStatus();
			source = d2pTuwenOrder.getSource();
			status = d2pTuwenOrder.getStatus();
			break;
		case 7:
			// d2p 电话问诊
			BusinessD2pTelOrder d2pTelOrder = d2pService
					.queryd2ptelorderbyid(orderId);
			caseId = d2pTelOrder.getCaseId();
			orderUuid = d2pTelOrder.getUuid();
			doctorId = d2pTelOrder.getDoctorId();
			payStatus = d2pTelOrder.getPayStatus();
			source = d2pTelOrder.getSource();
			status = d2pTelOrder.getStatus();
			info.setAnswerTelephone(d2pTelOrder.getAnswerTelephone());
			info.setAnswerTime(d2pTelOrder.getOrderTime());
			break;
		case 8:
			// d2p 患者报道
			BusinessD2pReportOrder d2pReportOrder = d2pService
					.queryd2preportorderbyid(orderId);
			caseId = d2pReportOrder.getCaseId();
			orderUuid = d2pReportOrder.getUuid();
			doctorId = d2pReportOrder.getDoctorId();
			source = d2pReportOrder.getSource();
			status = d2pReportOrder.getStatus();
			break;
		case 9:
			// d2p 快速问诊
			BusinessD2pFastaskOrder d2pFastaskOrder = d2pService
					.queryd2pfastaskorderbyid(orderId);
			caseId = d2pFastaskOrder.getCaseId();
			orderUuid = d2pFastaskOrder.getUuid();
			doctorId = d2pFastaskOrder.getDoctorId();
			payStatus = d2pFastaskOrder.getPayStatus();
			source = d2pFastaskOrder.getSource();
			status = d2pFastaskOrder.getStatus();
			break;
		case 10:
			// d2d 转诊
			BusinessD2dReferralOrder d2dReferralOrder = d2pService
					.queryd2dreferralOrderbyId(orderId);
			caseId = d2dReferralOrder.getCaseId();
			orderUuid = d2dReferralOrder.getUuid();
			doctorId = d2dReferralOrder.getDoctorId();
			source = d2dReferralOrder.getSource();
			status = d2dReferralOrder.getStatus();
			info.setExpertId(d2dReferralOrder.getReferralDocId());
			info.setReferralType(d2dReferralOrder.getReferralType());
			info.setReferralDate(d2dReferralOrder.getReferralDate());
			break;
		case 11:
			// t2p 会诊申请
			BusinessD2pConsultationRequest d2pConsultationRequest = d2pService
					.queryd2pconreqorderbyid(orderId);
			caseId = d2pConsultationRequest.getCaseId();
			orderUuid = d2pConsultationRequest.getUuid();
			doctorId = d2pConsultationRequest.getDoctorId();
			source = d2pConsultationRequest.getSource();
			status = d2pConsultationRequest.getStatus();
			info.setTeamUuid(d2pConsultationRequest.getTeamUuid());
			break;
		case 12:
			BusinessT2pTuwenOrder t2pTuwenOrder = d2pService
					.querybusinesst2ptuwenById(orderId);
			caseId = t2pTuwenOrder.getCaseId();
			orderUuid = t2pTuwenOrder.getUuid();
			doctorId = t2pTuwenOrder.getDoctorId();
			payStatus = t2pTuwenOrder.getPayStatus();
			source = t2pTuwenOrder.getSource();
			status = t2pTuwenOrder.getStatus();
			info.setConsultationResult(t2pTuwenOrder.getConsultationResult());
			info.setTeamUuid(t2pTuwenOrder.getTeamUuid());
			break;
		}
		info.setCaseId(caseId);
		info.setOrderUuid(orderUuid);
		info.setDoctorId(doctorId);
		info.setPayStatus(payStatus);
		info.setSource(source);
		info.setStatus(status);
		return info;
	}

	/**
	 * 病例附件
	 * 
	 * @param ca
	 * @return
	 */
	public List<UserCaseAttachment> getAttahments(CaseInfo ca) {
		List<UserCaseAttachment> images = wenzhenService
				.queryUserAttachmentByCaseUuid(ca.getUuid());
		for (UserCaseAttachment userCaseAttachment : images) {
			if (StringUtils.isNotBlank(userCaseAttachment.getAttachmentIds())) {
				List<CustomFileStorage> files = wenzhenService
						.queryCustomFilesByCaseIds(userCaseAttachment
								.getAttachmentIds());
				userCaseAttachment.setFiles(files);
				Integer filescount = files != null ? files.size() : 0;
				userCaseAttachment.setFilescount(filescount);
			}
		}
		ca.setAttachments(images);
		return images;
	}

	/**
	 * 入院记录
	 * 
	 * @param ca
	 */
	public void getNormalImages(CaseInfo ca) {
		if (StringUtils.isNotBlank(ca.getNormalImages())) {
			List<CustomFileStorage> files = wenzhenService
					.queryCustomFilesByCaseIds(ca.getNormalImages());
			ca.setCaseImages(files);
		}
	}

	/**
	 * 支付信息
	 * 
	 * @param map
	 * @return
	 */
	private PayInfo processPay(Map<String, Object> map) {
		PayInfo payInfo = new PayInfo();
		payInfo.setMoney(map.get("money"));
		payInfo.setOutTradeNo(map.get("outTradeNo"));
		payInfo.setRefundResult(map.get("refundResult"));
		payInfo.setRefundStatus(map.get("refundStatus"));
		payInfo.setRefundTime(map.get("refundTime"));
		payInfo.setTransactionId(map.get("transactionId"));
		payInfo.setPayStatus(map.get("payStatus"));
		return payInfo;
	}

	public void saveDocorServiceInfo(DoctorDetailInfo detail) {
		// TODO Auto-generated method stub
		DoctorServiceInfo docser = new DoctorServiceInfo();
		BigDecimal archiaterMoney = new BigDecimal(30);
		BigDecimal otherMoney = new BigDecimal(20);
		docser.setDoctorId(detail.getId());
		docser.setTeamUuid(null);
		docser.setServiceId(6);
		docser.setPackageId(6);
		docser.setIsOpen(1);
		if (detail.getDutyId().equals(4)) {
			docser.setAmount(archiaterMoney);
			docser.setShowPrice(archiaterMoney);
		} else {
			docser.setAmount(otherMoney);
			docser.setShowPrice(otherMoney);
		}
		docser.setDescription(null);
		docser.setIsMultiPackage(null);
		docser.setServicePhone(null);
		commonService.saveDoctorServiceInfo(docser);
	}

	/* *
	 * 
	 * 
	 * @param paramMap
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String docreportaboutdatass(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		String sEcho = paramMap.get("sEcho");
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus = Integer.parseInt(paramMap.get("ostatus"));
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.docreportaboutdatass(
				ostatus, searchContent, start, length);
		Integer renum = (Integer) retmap.get("num");
		List<DoctorsAboutsDto> datas = (List<DoctorsAboutsDto>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":");
		stringJson.append(net.sf.json.JSONArray.fromObject(datas).toString());
		stringJson.append("}");
		return stringJson.toString();
	}

	/**
	 * 获取短信验证码数据
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String smsDatas(Map<String, String> paramMap) {
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String search = paramMap.get("search");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus = Integer.parseInt(paramMap.get("ostatus"));
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.querySystemSmsRecordDatas(
				search, ostatus, start, length);
		Integer renum = (Integer) retmap.get("num");
		List<SystemSmsRecord> records = (List<SystemSmsRecord>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":[");
		SystemSmsRecord record = null;
		for (int i = 0; i < records.size(); i++) {
			record = records.get(i);
			stringJson.append("[");
			stringJson.append("\"" + record.getId() + "\",");
			stringJson.append("\"" + record.getTelphone() + "\",");
			stringJson.append("\"" + record.getCode() + "\",");
			stringJson.append("\"" + sdf.format(record.getSendTime()) + "\",");
			stringJson
					.append("\"" + sdf.format(record.getUpdateTime()) + "\",");
			stringJson.append("\""
					+ (record.getStatus().equals(1) ? "发送成功" : (record
							.getStatus().equals(-1) ? "发送失败" : "未发送")) + "\",");
			stringJson.append("\""
					+ (StringUtils.isNotBlank(record.getRemark()) ? record
							.getRemark() : "") + "\"");
			stringJson.append("],");
		}
		if (records.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}

	@SuppressWarnings("unchecked")
	public String gainreceiveorderdatas(Map<String, String> paramMap)
			throws Exception {
		// TODO Auto-generated method stub
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		String search = paramMap.get("search");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer type = Integer.parseInt(paramMap.get("ostatus"));
		Map<String, Object> retmap = weixinService.gainrecriveorderdatas(
				search, start, length, type);
		Integer renum = (Integer) retmap.get("num");
		List<BusinessVedioOrder> orders = (List<BusinessVedioOrder>) retmap
				.get("items");
		StringBuilder stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		BusinessVedioOrder order = null;
		for (int i = 0; i < orders.size(); i++) {
			order = orders.get(i);
			List<BusinessPayInfo> payinfos = wenzhenService
					.queryBusinesPayInfosByOId(order.getId(), 4);
			BigDecimal money = (BigDecimal) payinfo(payinfos).get("money");
			String _money = "";
			if (money == null) {
				if (order.getExpertId() != null) {
					money = commonManager.gainMoneyByOrder(4,
							order.getExpertId());
					if (money != null)
						_money = money.toString();
				}
			} else {
				_money = money.toString();
			}
			String _time = gaintime(order);
			long cd = 0;
			if (StringUtils.isNotBlank(_time) && _time.length() > 10) {
				cd = DateUtil.calculateSecond(_time + ":00",
						sdf.format(new Date()));
			}
			stringJson.append("[");
			stringJson.append("\"" + order.getId() + "\",");
			stringJson.append("\""
					+ order.getPatientName()
					+ "/"
					+ (order.getSex() != null ? (order.getSex().equals(1) ? "男"
							: "女") : "")
					+ "/"
					+ order.getAge()
					+ "/"
					+ (StringUtils.isNotBlank(order.getTelephone()) ? order
							.getTelephone() : "") + "\",");// 患者信息
			String exjson = "";
			exjson += (order.getExpertId() != null ? (StringUtils
					.isNotBlank(order.getExpertName()) ? order.getExpertName()
					: "") : "待分配");
			exjson += (StringUtils.isNotBlank(order.getHosName()) ? "/"
					+ order.getHosName() : "");
			exjson += (StringUtils.isNotBlank(order.getDepName()) ? "/"
					+ order.getDepName() : "");
			exjson += (StringUtils.isNotBlank(order.getExProfession()) ? "/"
					+ order.getExProfession() : "");
			stringJson.append("\"" + exjson + "\",");// 专家信息
			String docjson = "";
			docjson += (StringUtils.isNotBlank(order.getLocalDocName()) ? order
					.getLocalDocName() : "");
			docjson += (StringUtils.isNotBlank(order.getLocalHosName()) ? "/"
					+ order.getLocalHosName() : "");
			docjson += (StringUtils.isNotBlank(order.getLocalDepName()) ? "/"
					+ order.getLocalDepName() : "");
			stringJson.append("\"" + docjson + "\",");// 医生信息
			String ctime = sdf.format(order.getCreateTime());
			stringJson.append("\"" + ctime + "\",");// 下单时间
			stringJson.append("\"" + order.getAppTime() + "\",");// 申请时间
			stringJson.append("\"" + "会诊时间" + "\",");// 会诊时间
			stringJson.append("\""
					+ (order.getPayStatus() != null ? (order.getPayStatus()
							.equals(1) ? "已支付" : "未支付") : "未支付") + "\",");// 支付状态
			
			stringJson.append("\"\"");// 操作
			stringJson.append("],");
		}
		if (orders.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String gainDocFollowDatas(Map<String, String> paramMap)
			throws Exception {
		// TODO Auto-generated method stub
		String sEcho = paramMap.get("sEcho");
		String search = paramMap.get("search");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer type = Integer.parseInt(paramMap.get("ostatus"));
		Map<String, Object> retmap = commonService.queryDocFollowDatas(search, start, length, type);
		Integer renum = (Integer) retmap.get("num");
		List<DocFollowDto> follows = (List<DocFollowDto>) retmap.get("items");
		StringBuilder stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		if(follows !=null &&!follows.isEmpty()) {
			for(DocFollowDto df : follows) {
				stringJson.append("[");
				stringJson.append("\"" + df.getOrderType() + "\",");
				stringJson.append("\"" + df.getOrderUuid() + "\",");
				stringJson.append("\"" + df.getDocId() + "\",");
				stringJson.append("\"" + df.getSubUserId() + "\",");
				stringJson.append("\"" + df.getDocName() + "\",");
				stringJson.append("\"" + df.getDocHos() + "\",");
				stringJson.append("\"" + df.getDocDep() + "\",");
				stringJson.append("\"" + df.getUserName() + "\",");
				stringJson.append("\"" + df.getMsgCount() + "\",");
				stringJson.append("\"" + df.getMsgTime() + "\",");
				stringJson.append("\"" + df.getMsgContent() + "\",");
				stringJson.append("\"\"");// 操作
				stringJson.append("],");
			}
			if (follows.size() > 0)
				stringJson.deleteCharAt(stringJson.length() - 1);
		}
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
    
    @SuppressWarnings("unchecked")
    public String userWarmDocDatas(Map<String, String> paramMap) {
        // TODO Auto-generated method stub
        String sEcho = paramMap.get("sEcho");
        String searchContent = paramMap.get("searchContent");
        Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
        Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus = Integer.parseInt(paramMap.get("ostatus"));
        StringBuilder stringJson = null;
        Map<String, Object> retmap = commonService.userWarmDocDatas(ostatus,searchContent, start, length);
        Integer renum = (Integer) retmap.get("num");
        List<MobileSpecial> datas = (List<MobileSpecial>) retmap
                .get("items");
        stringJson = new StringBuilder("{\"sEcho\":" + sEcho
                + ",\"iTotalRecords\":" + (renum)
                + ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":");
        stringJson.append(net.sf.json.JSONArray.fromObject(datas).toString());
        stringJson.append("}");
        return stringJson.toString();
        
    }


	public String liveplandatass(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		String sEcho = paramMap.get("sEcho");
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		//Integer ostatus = Integer.parseInt(paramMap.get("ostatus"));
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.liveplandatass(searchContent, start, length);
		Integer renum = (Integer) retmap.get("num");
		List<LivePlanOrder> datas = (List<LivePlanOrder>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":");
		stringJson.append(net.sf.json.JSONArray.fromObject(datas).toString());
		stringJson.append("}");
		return stringJson.toString();
	}
	/**
	 * 获取往期案例数据
	 * @param paramMap
	 * @return
	 */
	public String hisCaseList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		String sEcho = paramMap.get("sEcho");
		String searchContent = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer type = Integer.parseInt(paramMap.get("type"));//4--往期视频案例，5--往期图文案例
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.queryHisCaseList(searchContent, start, length,type);
		Integer renum = (Integer) retmap.get("num");
		List<HistoryCaseDto> datas = (List<HistoryCaseDto>) retmap
				.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":");
		stringJson.append(net.sf.json.JSONArray.fromObject(datas).toString());
		stringJson.append("}");
		return stringJson.toString();
		
		
	}
	/**
	 * 往期案例新增修改
	 * @param hisCaseUuid
	 * @param title
	 * @param depIds
	 * @param mainSuit
	 * @param caseDesc
	 * @param backImageUrl
	 * @param attachmentIds
	 * @param treatAdvice
	 * @return
	 */
	public Map<String,Object> hisCaseSave(Integer type,String hisCaseUuid,String title,String depIds,
			String mainSuit,String caseDesc,String backImageUrl,String attachmentIds,String treatAdvice){
		if(StringUtils.isNotBlank(hisCaseUuid)) {
			//编辑
			processEditHisCase(hisCaseUuid,type,title,depIds,mainSuit,caseDesc,backImageUrl,attachmentIds,treatAdvice);
		}else {
			//新增 history case info 
			processAddHisCase(type,title,depIds,mainSuit,caseDesc,backImageUrl,attachmentIds,treatAdvice);
		}
		return null;
	}
	/**
	 * 获取会诊详情
	 * @param hisCaseUuid
	 * @return
	 */
	public Map<String,Object> hisCaseGet(String hisCaseUuid) {
		Map<String,Object> map = new HashMap<String,Object>();
		HistoryCaseInfo hisCaseInfo = commonService.queryHistoryCaseInfo(hisCaseUuid);
		map.put("hisCaseInfo", hisCaseInfo);
		List<HistoryCaseDepRelative> deps = commonService.queryHistoryCaseDepRelatives(hisCaseUuid);
		map.put("deps", deps);
		HistoryCaseAttachment historyCaseAttachment = commonService.queryHistoryCaseAttachment(hisCaseUuid);
		map.put("historyCaseAttachment", historyCaseAttachment);
		if(StringUtils.isNotBlank(historyCaseAttachment.getAttentmentsId())) {
			List<CustomFileStorage> files = weixinService.queryCustomFileStorageImages(historyCaseAttachment.getAttentmentsId());
			map.put("attachments", files);
		}
		return map;
	}
	
	/**
	 * 删除案例
	 * @param hisCaseUuid
	 */
	public void hisCaseDelete(String hisCaseUuid) {
		HistoryCaseInfo hisCaseInfo = commonService.queryHistoryCaseInfo(hisCaseUuid);
		hisCaseInfo.setStatus(0);
		commonService.updateHistoryCaseInfo(hisCaseInfo);
	}
	
	private void processEditHisCase(String hisCaseUuid,Integer type,String title,String depIds,
			String mainSuit,String caseDesc,String backImageUrl,String attachmentIds,String treatAdvice) {
		HistoryCaseInfo historyCaseInfo = commonService.queryHistoryCaseInfo(hisCaseUuid);
		if(StringUtils.isNotBlank(treatAdvice)){
			historyCaseInfo.setTreatAdvice(treatAdvice);
		}
		if(StringUtils.isNotBlank(mainSuit)){
			historyCaseInfo.setMainSuit(mainSuit);
		}
		if(StringUtils.isNotBlank(backImageUrl)){
			historyCaseInfo.setBackImageUrl(backImageUrl);
		}
		if(StringUtils.isNotBlank(title)){
			historyCaseInfo.setTitle(title);
		}
		commonService.updateHistoryCaseInfo(historyCaseInfo);
		HistoryCaseAttachment historyCaseAttachment = commonService.queryHistoryCaseAttachment(hisCaseUuid);
		if(historyCaseAttachment != null){
			historyCaseAttachment.setAttentmentsId(attachmentIds);
			historyCaseAttachment.setTitle(title);
			commonService.updateHistoryCaseAttachment(historyCaseAttachment);
		}else{
			HistoryCaseAttachment historyCaseAttachments=new HistoryCaseAttachment();
			historyCaseAttachments.setCreateTime(new Timestamp(System.currentTimeMillis()));
			historyCaseAttachments.setHistoryCaseUuid(historyCaseInfo.getUuid());
			historyCaseAttachments.setAttentmentsId(attachmentIds);
			historyCaseAttachments.setTitle(title);
			historyCaseAttachments.setUuid(UUIDUtil.getUUID());
			commonService.saveHistoryCaseAttachment(historyCaseAttachments);
		}
		List<HistoryCaseDepRelative> depRelatives = commonService.queryHistoryCaseDepRelatives(hisCaseUuid);
		if(depRelatives == null ) {
			if(StringUtils.isNotBlank(depIds)) {
				String[] _ids = depIds.split(",");
				if(_ids != null && _ids.length>0) {
					for(String depId : _ids) {
						if(StringUtils.isNotBlank(depId) && StringUtils.isNumeric(depId)) {
							historyCaseDepRelativeSave(historyCaseInfo.getUuid(),Integer.parseInt(depId));
						}
					}
				}
			}
		}else {
			
				/*List<Integer> remainIds = new ArrayList<Integer>();//保留科室
				String[] _ids = depIds.split(",");
				if(_ids != null && _ids.length>0) {
					for(String depId : _ids) {
						if(!existDep(depId,depRelatives)) {
							//新增
							historyCaseDepRelativeSave(hisCaseUuid,Integer.parseInt(depId));
						}else {
							remainIds.add(Integer.parseInt(depId));
						}
					}
				}
				if(remainIds.size()>0) {
					for(HistoryCaseDepRelative _depRelative : depRelatives) {
						if(needDel(_depRelative.getStandardDepId(),remainIds))
							commonService.delHistoryCaseDepRelative(_depRelative.getId());
					}
				}*/
				//先全部删除以前的
				for(HistoryCaseDepRelative _depRelative : depRelatives) {
					commonService.delHistoryCaseDepRelative(_depRelative.getId());
				}
				//在进行新增
				if(StringUtils.isNotBlank(depIds)){
					String[] _ids = depIds.split(",");
					if(_ids != null && _ids.length>0) {
						for(String depId : _ids) {
								//新增
								historyCaseDepRelativeSave(hisCaseUuid,Integer.parseInt(depId));
						}
					}	
				}
			}
		}
	
	private boolean needDel(Integer depId,List<Integer> ids) {
		boolean needDel = false;
		for(Integer id:ids) {
			if(!depId.equals(id)) 
				needDel = true;
		}
		return needDel;
	}
	
	private boolean existDep(String depId,List<HistoryCaseDepRelative> depRelatives) {
		boolean exist = false;
		for(HistoryCaseDepRelative depRelative : depRelatives) {
			if(depRelative.getStandardDepId().equals(Integer.parseInt(depId))) 
				exist = true;
		}
		return exist;
	}
	
	/**
	 * 处理新增数据
	 * @param type
	 * @param title
	 * @param depIds
	 * @param mainSuit
	 * @param caseDesc
	 * @param backImageUrl
	 * @param attachmentIds
	 * @param treatAdvice
	 */
	private void processAddHisCase(Integer type,String title,String depIds,
			String mainSuit,String caseDesc,String backImageUrl,String attachmentIds,String treatAdvice) {
		HistoryCaseInfo historyCaseInfo = new HistoryCaseInfo();
		historyCaseInfoDataSet(historyCaseInfo,type,title,
				mainSuit,backImageUrl,treatAdvice);
		Integer id = commonService.saveHistoryCaseInfo(historyCaseInfo);
		HistoryCaseAttachment historyCaseAttachment = new HistoryCaseAttachment();
		historyCaseAttachment.setAttentmentsId(attachmentIds);
		historyCaseAttachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
		historyCaseAttachment.setHistoryCaseUuid(historyCaseInfo.getUuid());
		historyCaseAttachment.setTitle(caseDesc);
		historyCaseAttachment.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		commonService.saveHistoryCaseAttachment(historyCaseAttachment);
		if(StringUtils.isNotBlank(depIds)) {
			//标准科室
			String[] _ids = depIds.split(",");
			if(_ids != null && _ids.length>0) {
				for(String depId : _ids) {
					if(StringUtils.isNotBlank(depId) && StringUtils.isNumeric(depId)) {
						historyCaseDepRelativeSave(historyCaseInfo.getUuid(),Integer.parseInt(depId));
					}
				}
			}
		}
	}
	
	private void historyCaseInfoDataSet(HistoryCaseInfo historyCaseInfo,Integer type,String title,
			String mainSuit,String backImageUrl,String treatAdvice) {
		historyCaseInfo.setBackImageUrl(backImageUrl);
		historyCaseInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
		historyCaseInfo.setMainSuit(mainSuit);
		historyCaseInfo.setStatus(1);
		historyCaseInfo.setTitle(title);
		historyCaseInfo.setTreatAdvice(treatAdvice);
		historyCaseInfo.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		historyCaseInfo.setType(type);
	}
	
	private void historyCaseDepRelativeSave(String historyCaseUuid,Integer depId) {
		HistoryCaseDepRelative historyCaseDepRelative = new HistoryCaseDepRelative();
		historyCaseDepRelative.setCreateTime(new Timestamp(System.currentTimeMillis()));
		historyCaseDepRelative.setHistoryCaseUuid(historyCaseUuid);
		historyCaseDepRelative.setStandardDepId(depId);
		commonService.saveHistoryCaseDepRelative(historyCaseDepRelative);
	}
	
}
