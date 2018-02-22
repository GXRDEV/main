package com.tspeiz.modules.manage;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tspeiz.modules.common.bean.AdviceBean;
import com.tspeiz.modules.common.bean.D2pOrderBean;
import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.newrelease.AppraisementDoctorInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageBean;
import com.tspeiz.modules.common.entity.newrelease.BusinessPayInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;
import com.tspeiz.modules.common.entity.newrelease.DistCode;
import com.tspeiz.modules.common.entity.newrelease.DoctorServiceInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.StandardDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.UserBillRecord;
import com.tspeiz.modules.common.entity.newrelease.UserContactInfo;
import com.tspeiz.modules.common.entity.newrelease.UserWeixinRelative;
import com.tspeiz.modules.common.entity.release2.BusinessD2pFastaskOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2pReportOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2pTelOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2pTuwenOrder;
import com.tspeiz.modules.common.entity.release2.BusinessT2pTuwenOrder;
import com.tspeiz.modules.common.entity.release2.DoctorTeam;
import com.tspeiz.modules.common.entity.release2.SystemWarmthInfo;
import com.tspeiz.modules.common.entity.release2.UserMedicalRecord;
import com.tspeiz.modules.common.entity.release2.UserWarmthInfo;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.ID2pService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.RongCloudApi;
import com.tspeiz.modules.util.date.RelativeDateFormat;
import com.tspeiz.modules.util.imchat.RongCloudConfig;
import com.tspeiz.modules.util.log.RecordLogUtil;
import com.tspeiz.modules.util.weixin.WeixinUtil;

@Service
public class D2pManager {
	@Autowired
	private ID2pService d2pService;
	@Autowired
	private IWeixinService weixinService;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private CommonManager commonManager;
	@Autowired
	private PayOrderManager payOrderManager;
	@Autowired
	private IWenzhenService wenzhenService;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 获取患者-医生 电话订单数据
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryd2pteldatas(Map<String, String> paramMap)throws Exception{
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus=Integer.parseInt(paramMap.get("ostatus"));
		String search = paramMap.get("search");
		Map<String, Object> querymap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(search)) {
			querymap.put("search", search);
		}
		querymap.put("ostatus", ostatus);
		StringBuilder stringJson = null;
		Map<String, Object> retmap = d2pService.queryd2pteldatas(querymap,
				start, length);
		Integer renum = (Integer) retmap.get("num");
		List<D2pOrderBean> orders = (List<D2pOrderBean>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		D2pOrderBean dtel = null;
		for (int i = 0; i < orders.size(); i++) {
			dtel = orders.get(i);
			List<BusinessPayInfo> payinfos = wenzhenService
					.queryBusinesPayInfosByOId(dtel.getId(), 7);
			stringJson.append("[");
			stringJson.append("\"" + dtel.getId() + "\",");
			stringJson.append("\"" + dtel.getUserName() + "\",");
			stringJson.append("\""
					+ (dtel.getSex() == null ? "未知"
							: (dtel.getSex().equals(1) ? "男" : "女")) + "\",");
			stringJson.append("\""
					+ (dtel.getAge() == null ? "未知" : dtel.getAge()) + "\",");
			stringJson.append("\"" + dtel.getTelphone() + "\",");
			stringJson.append("\"" + dtel.getDocName() + "\",");
			stringJson.append("\"" + dtel.getHosName() + "\",");
			stringJson.append("\"" + dtel.getDepName() + "\",");
			stringJson.append("\"" + dtel.getProfession() + "\",");
			stringJson.append("\"" + sdf.format(dtel.getCreateTime()) + "\",");
			stringJson.append("\""
					+ RelativeDateFormat.calculateTimeLoc(sdf.format(dtel
							.getCreateTime())) + "\",");
			stringJson.append("\""+dtel.getOrderTime()+ "\",");
			String time="";
			try{
				time=sdf.format(dtel.getOrderTime());
			}catch(Exception e){
				
			}
			stringJson.append("\""
					+ (StringUtils.isNotBlank(time)?RelativeDateFormat.calculateTimeLoc(time):"未知") + "\",");
			stringJson.append("\""
					+ (dtel.getPayStatus().equals(4) ? "未支付" : "已支付") + "\",");
			BigDecimal money=payinfo(payinfos);
			stringJson.append("\"" +(money==null?"":money) + "\",");
			stringJson.append("\"" + gainstatusdesc(dtel.getStatus()) + "\",");
			stringJson.append("\"" + ((dtel.getPayStatus().equals(1)&&money!=null)?gainRefundDesc(dtel.getRefundStatus()):"") + "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(dtel.getRefundTime())?dtel.getRefundTime():"") + "\",");
			stringJson.append("\"" + "" + "\"");
			stringJson.append("],");
		}
		if (orders.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}

	/**
	 * 获取患者-医生 图文咨询订单数据
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryd2ptuwendatas(Map<String, String> paramMap)throws Exception {
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus=Integer.parseInt(paramMap.get("ostatus"));
		String search = paramMap.get("search");
		Map<String, Object> querymap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(search)) {
			querymap.put("search", search);
		}
		querymap.put("ostatus", ostatus);
		StringBuilder stringJson = null;
		Map<String, Object> retmap = d2pService.queryd2ptuwendatas(querymap,
				start, length);
		Integer renum = (Integer) retmap.get("num");
		List<D2pOrderBean> orders = (List<D2pOrderBean>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		D2pOrderBean dtuwen = null;
		for (int i = 0; i < orders.size(); i++) {
			dtuwen = orders.get(i);
			List<BusinessPayInfo> payinfos = wenzhenService
					.queryBusinesPayInfosByOId(dtuwen.getId(), 6);
			
			stringJson.append("[");
			stringJson.append("\"" + dtuwen.getId() + "\",");
			stringJson.append("\"" + dtuwen.getUserName() + "\",");
			stringJson.append("\""
					+ (dtuwen.getSex() == null ? "未知" : (dtuwen.getSex()
							.equals(1) ? "男" : "女")) + "\",");
			stringJson.append("\""
					+ (dtuwen.getAge() == null ? "未知" : dtuwen.getAge())
					+ "\",");
			stringJson.append("\"" + dtuwen.getTelphone() + "\",");
			stringJson.append("\"" + dtuwen.getDocName() + "\",");
			stringJson.append("\"" + dtuwen.getHosName() + "\",");
			stringJson.append("\"" + dtuwen.getDepName() + "\",");
			stringJson.append("\"" + dtuwen.getProfession() + "\",");
			stringJson
					.append("\"" + sdf.format(dtuwen.getCreateTime()) + "\",");
			stringJson.append("\""
					+ RelativeDateFormat.calculateTimeLoc(sdf.format(dtuwen
							.getCreateTime())) + "\",");
			stringJson.append("\""
					+ (dtuwen.getReceiveTime() == null ? "未接诊" : sdf
							.format(dtuwen.getReceiveTime())) + "\",");
			stringJson.append("\""
					+ (dtuwen.getReceiveTime() == null ? "未知"
							: RelativeDateFormat.calculateTimeLoc(sdf
									.format(dtuwen.getReceiveTime()))) + "\",");
			stringJson
					.append("\""
							+ (dtuwen.getPayStatus().equals(4) ? "未支付" : "已支付")
							+ "\",");
			BigDecimal money=payinfo(payinfos);
			stringJson.append("\"" +(money==null?"":money) + "\",");
			stringJson
					.append("\"" + gainstatusdesc(dtuwen.getStatus()) + "\",");
			stringJson.append("\"" + ((dtuwen.getPayStatus()!=null&&dtuwen.getPayStatus().equals(1)&&money!=null)?gainRefundDesc(dtuwen.getRefundStatus()):"") + "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(dtuwen.getRefundTime())?dtuwen.getRefundTime():"") + "\",");
			stringJson.append("\"" + "" + "\"");
			stringJson.append("],");
		}
		if (orders.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	private String gainRefundDesc(Integer status){
		String desc="";
		if(status==null||status.equals(0)){
			desc="待退款";
		}else if(status.equals(1)){
			desc="退款成功";
		}else if(status.equals(-1)){
			desc="退款失败";
		}
		return desc;
	}

	/**
	 * 获取患者-医生 快速问诊订单数据
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryd2pfastaskdatas(Map<String, String> paramMap)throws Exception{
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		String search = paramMap.get("search");
		Integer ostatus=Integer.parseInt(paramMap.get("ostatus"));
		Map<String, Object> querymap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(search)) {
			querymap.put("search", search);
		}
		querymap.put("ostatus", ostatus);
		StringBuilder stringJson = null;
		Map<String, Object> retmap = d2pService.queryd2pfastaskdatas(querymap,
				start, length);
		Integer renum = (Integer) retmap.get("num");
		List<D2pOrderBean> orders = (List<D2pOrderBean>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		D2pOrderBean dfastask = null;
		for (int i = 0; i < orders.size(); i++) {
			dfastask = orders.get(i);
			List<BusinessPayInfo> payinfos = wenzhenService
					.queryBusinesPayInfosByOId(dfastask.getId(), 9);
			stringJson.append("[");
			stringJson.append("\"" + dfastask.getId() + "\",");
			stringJson.append("\"" + dfastask.getUserName() + "\",");
			stringJson.append("\""
					+ (dfastask.getSex() == null ? "未知" : (dfastask.getSex()
							.equals(1) ? "男" : "女")) + "\",");
			stringJson.append("\""
					+ (dfastask.getAge() == null ? "未知" : dfastask.getAge())
					+ "\",");
			stringJson.append("\"" + dfastask.getTelphone() + "\",");
			stringJson.append("\"" + dfastask.getDocName() + "\",");
			stringJson.append("\"" + dfastask.getHosName() + "\",");
			stringJson.append("\"" + dfastask.getDepName() + "\",");
			stringJson.append("\"" + dfastask.getProfession() + "\",");
			stringJson.append("\"" + sdf.format(dfastask.getCreateTime())
					+ "\",");
			stringJson.append("\""
					+ RelativeDateFormat.calculateTimeLoc(sdf.format(dfastask
							.getCreateTime())) + "\",");
			stringJson.append("\""
					+ (dfastask.getReceiveTime() == null ? "未接诊" : sdf
							.format(dfastask.getReceiveTime())) + "\",");
			stringJson.append("\""
					+ (dfastask.getReceiveTime() == null ? "未知"
							: RelativeDateFormat.calculateTimeLoc(sdf
									.format(dfastask.getReceiveTime())))
					+ "\",");
			stringJson.append("\""
					+ (dfastask.getPayStatus().equals(4) ? "未支付" : "已支付")
					+ "\",");
			BigDecimal money=payinfo(payinfos);
			stringJson.append("\"" +(money==null?"":money) + "\",");
			stringJson.append("\"" + gainstatusdesc(dfastask.getStatus())
					+ "\",");
			stringJson.append("\"" + ((dfastask.getPayStatus().equals(1)&&money!=null)?gainRefundDesc(dfastask.getRefundStatus()):"") + "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(dfastask.getRefundTime())?dfastask.getRefundTime():"") + "\",");
			stringJson.append("\"" + "" + "\"");
			stringJson.append("],");
		}
		if (orders.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}

	public D2pOrderBean queryOrderDetailInfo(Integer oid, Integer otype) {
		return d2pService.queryOrderDetailInfo(oid, otype);
	}

	/**
	 * 获取家庭成员
	 * 
	 * @param openid
	 * @return
	 */
	public Map<String, Object> gainFamilies(String openid) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		List<UserContactInfo> users = weixinService
				.queryUserContactInfosByUserId(uwr.getUserId());
		String distcode = "";
		for (UserContactInfo _user : users) {
			if (!StringUtils.isNotBlank(_user.getUuid())) {
				_user.setUuid(UUID.randomUUID().toString().replace("-", ""));
				weixinService.updateUserContactInfo(_user);
			}
			if (StringUtils.isNotBlank(_user.getDistCode())) {
				// 获取地区
				distcode = _user.getDistCode();
				if (distcode.endsWith("0000")) {
					DistCode dist = commonService.queryDistCodeByCode(distcode);
					_user.setProvince(dist.getDistName());
				} else {
					DistCode prodist = commonService
							.queryDistCodeByCode(distcode.substring(0, 2)
									+ "0000");
					_user.setProvince(prodist.getDistName());
					DistCode citydist = commonService
							.queryDistCodeByCode(distcode);
					_user.setCity(citydist.getDistName());
				}
			}
		}
		map.put("users", users);
		return map;
	}

	/**
	 * 新增或修改家庭成员
	 * 
	 * @param request
	 */
	public void saveOrUpdateFamiler(HttpServletRequest request) {
		String openid = request.getParameter("openid");
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		String id = request.getParameter("id");// 家庭成员id
		UserContactInfo user = null;
		boolean issave = false;
		if (StringUtils.isNotBlank(id)) {
			// 编辑
			user = weixinService.queryUserContactorInfoById(Integer
					.parseInt(id));
			if (!StringUtils.isNotBlank(user.getUuid())) {
				user.setUuid(UUID.randomUUID().toString().replace("-", ""));
			}
		} else {
			// 新增
			user = new UserContactInfo();
			user.setUuid(UUID.randomUUID().toString().replace("-", ""));
			user.setUserId(uwr.getUserId());
			user.setCreateTime(new Timestamp(System.currentTimeMillis()));
			issave = true;
		}
		user.setContactName(request.getParameter("contactName"));
		user.setTelphone(request.getParameter("telphone"));
		if (StringUtils.isNotBlank(request.getParameter("sex"))) {
			user.setSex(Integer.parseInt(request.getParameter("sex")));
		}
		if (StringUtils.isNotBlank(request.getParameter("idCard"))) {
			user.setIdCard(request.getParameter("idCard"));
		}
		if (StringUtils.isNotBlank(request.getParameter("distCode"))) {
			user.setDistCode(request.getParameter("distCode"));
		}
		if (StringUtils.isNotBlank(request.getParameter("age"))) {
			user.setAge(Integer.parseInt(request.getParameter("age")));
		}
		if (StringUtils.isNotBlank(request.getParameter("isDefault"))
				&& request.getParameter("isDefault").equalsIgnoreCase("1")) {
			// 设置其他联系人为非默认
			processDefaultUser(openid);
			user.setIsDefault(1);
		} else {
			user.setIsDefault(0);
		}
		if (issave) {
			weixinService.saveUserContactInfo(user);
		} else {
			weixinService.updateUserContactInfo(user);
		}
	}

	/**
	 * 设置其他联系人为非默认联系人
	 * 
	 * @param openid
	 */
	private void processDefaultUser(String openid) {
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		List<UserContactInfo> users = weixinService
				.queryUserContactInfosByUserId(uwr.getUserId());
		for (UserContactInfo _user : users) {
			_user.setIsDefault(0);
			weixinService.updateUserContactInfo(_user);
		}
	}

	/**
	 * 删除家庭成员
	 * 
	 * @param id
	 */
	public void delfamiler(Integer id) {
		UserContactInfo user = weixinService.queryUserContactorInfoById(id);
		user.setStatus(0);
		weixinService.updateUserContactInfo(user);
	}

	/**
	 * 快速问诊提交
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> submitfast(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		String depid = request.getParameter("depId");// 标准科室 id，可为空
		String distCode = request.getParameter("distCode");// 定位获取的distcode
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		Integer userid = uwr.getUserId();
		Integer familyid = Integer.parseInt(request.getParameter("familyId"));// 家庭成员id
		UserContactInfo familer = weixinService
				.queryUserContactorInfoById(familyid);
		Integer caseid = processCase(request.getParameter("caseName"),
				request.getParameter("presentIll"),
				request.getParameter("normalImages"),
				request.getParameter("askProblem"), familer);// 创建病例
		if (StringUtils.isNotBlank(familer.getDistCode())) {
			distCode = familer.getDistCode();// 如果家庭成员有设置区域，使用家庭成员的区域位置，否则使用定位区域
		}
		// 创建订单
		Integer oid = processFastOrder(openid, caseid, userid,
				familer.getUuid(), depid, distCode);
		map.put("oid", oid);
		return map;
	}

	/**
	 * 创建病例
	 * 
	 * @param request
	 * @param userid
	 * @param fam
	 * @return
	 */
	private Integer processCase(String caseName, String presendIll,
			String normalImages, String askproblem, UserContactInfo fam) {
		CaseInfo ca = new CaseInfo();
		ca.setUuid(UUID.randomUUID().toString().replace("-", ""));
		ca.setUserId(fam.getUserId());
		ca.setSubUserUuid(fam.getUuid());
		ca.setCreateTime(new Date());
		ca.setContactName(fam.getContactName());
		ca.setIdNumber(fam.getIdCard());
		ca.setSex(fam.getSex());
		ca.setAge(fam.getAge());
		ca.setTelephone(fam.getTelphone());
		ca.setCaseName(caseName);
		ca.setPresentIll(presendIll);
		ca.setNormalImages(normalImages);
		ca.setUserType(1);
		ca.setAskProblem(askproblem);
		return wenzhenService.saveCaseInfo(ca);
	}

	/**
	 * 创建快速订单
	 * 
	 * @param caseid
	 * @param userid
	 * @param subUserUuid
	 * @param depid
	 * @return
	 */
	private Integer processFastOrder(String openid, Integer caseid,
			Integer userid, String subUserUuid, String depid, String distCode) {
		BusinessD2pFastaskOrder forder = new BusinessD2pFastaskOrder();
		forder.setUuid(UUID.randomUUID().toString().replace("-", ""));
		forder.setCaseId(caseid);
		forder.setUserId(userid);
		forder.setSubUserUuid(subUserUuid);
		if (StringUtils.isNotBlank(depid)) {
			forder.setDepId(Integer.parseInt(depid));
		}
		forder.setCreateTime(new Timestamp(System.currentTimeMillis()));
		forder.setSource(3);
		forder.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());
		forder.setPayStatus(4);
		forder.setOpenId(openid);
		forder.setDistCode(distCode);
		d2pService.saveBusinessD2pFastAskOrder(forder);
		return null;
	}

	/**
	 * 取消快速问诊
	 * 
	 * @param oid
	 */
	public void cancelfast(Integer oid) {
		BusinessD2pFastaskOrder forder = d2pService
				.queryd2pfastaskorderbyid(oid);
		forder.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey());// 取消
		d2pService.updated2pfastaskorder(forder);
	}

	/**
	 * 提交快速问诊
	 * 
	 * @param request
	 * @param response
	 * @param oid
	 * @param docid
	 * @return
	 */
	public Map<String, Object> confirmdoc(HttpServletRequest request,
			HttpServletResponse response, Integer oid, Integer docid,
			String openid) {
		Map<String, Object> map = new HashMap<String, Object>();
		Float money = commonManager.gainMoneyByOrder(
				OrderStatusEnum.D2P_ORDER_VALUE_FAST.getKey(), docid)
				.floatValue();
		Map<String, Object> retMap = WeixinUtil
				.weipay(request, response, money,
						PropertiesUtil.getString("APPID"),
						PropertiesUtil.getString("APPSECRET"),
						PropertiesUtil.getString("PARTNER"),
						PropertiesUtil.getString("PARTNERKEY"), "快速问诊", openid,
						PropertiesUtil.getString("DOMAINURL")
								+ "kangxin/wenzhennotify", null, null, null);
		Integer pid = payOrderManager.savePayInfo(
				OrderStatusEnum.D2P_ORDER_VALUE_FAST.getKey(), oid,
				retMap.get("out_trade_no").toString(), money, 2, money, 0.0f,
				0.0f, 0.0f, null);
		BusinessD2pFastaskOrder order = d2pService
				.queryd2pfastaskorderbyid(oid);
		order.setDoctorId(docid);
		d2pService.updated2pfastaskorder(order);
		map.putAll(retMap);
		try {
			RecordLogUtil.insert(OrderStatusEnum.D2P_ORDER_VALUE_FAST.getKey()
					+ "", "快速问诊", oid + "", pid + "",
					(String) retMap.get("paramxml"),
					(String) retMap.get("prepayxml"), "",
					(String) retMap.get("out_trade_no"));
		} catch (Exception e) {

		}
		return map;
	}

	public Map<String, Object> tellreport(Integer familyid, Integer docid) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserContactInfo user = weixinService
				.queryUserContactorInfoById(familyid);
		if (!StringUtils.isNotBlank(user.getUuid())) {
			map.put("report", "true");// 可以进行患者报道
		} else {
			BusinessD2pReportOrder order = d2pService
					.queryd2preportorderbyconditions(user.getUuid(), docid);
			if (order != null) {
				map.put("report", "false");// 已报道过该医生，不可以继续
			} else {
				map.put("report", "true");
			}
		}
		return map;
	}

	/**
	 * 患者报道
	 * 
	 * @param request
	 */
	public void patientreport(HttpServletRequest request) {
		String openid = request.getParameter("openid");
		Integer docid = Integer.parseInt(request.getParameter("docid"));// 报道的医生id
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		Integer userid = uwr.getUserId();
		Integer familyid = Integer.parseInt(request.getParameter("familyId"));// 家庭成员id
		String checkall = request.getParameter("checkall");
		UserContactInfo familer = weixinService
				.queryUserContactorInfoById(familyid);
		Integer caseid = processCase(request.getParameter("caseName"),
				request.getParameter("presentIll"),
				request.getParameter("normalImages"),
				request.getParameter("askProblem"), familer);
		// 创建患者报道记录--等待医生确认
		processPatientReportOrder(caseid, docid, userid, familer.getUuid(),
				openid, checkall);
	}

	/**
	 * 创建患者报道订单
	 * 
	 * @param caseid
	 * @param docid
	 * @param userid
	 * @param subUserUuid
	 * @param openid
	 */
	private void processPatientReportOrder(Integer caseid, Integer docid,
			Integer userid, String subUserUuid, String openid, String checkall) {
		BusinessD2pReportOrder order = new BusinessD2pReportOrder();
		order.setUuid(UUID.randomUUID().toString().replace("-", ""));
		order.setCaseId(caseid);
		order.setDoctorId(docid);
		order.setUserId(userid);
		order.setSubUserUuid(subUserUuid);
		order.setCreateTime(new Timestamp(System.currentTimeMillis()));
		order.setSource(3);
		order.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());
		order.setOpenId(openid);
		if (StringUtils.isNotBlank(checkall) && checkall.equalsIgnoreCase("1")) {
			order.setIsCheckAll(1);
		}
		d2pService.saveBusinessPatientReportOrder(order);
	}

	/**
	 * 我的医生
	 * 
	 * @param openid
	 * @return
	 */
	public Map<String, Object> mydoctors(String openid) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		Integer userid = uwr.getUserId();
		List<BusinessD2pReportOrder> list = d2pService
				.queryd2preportordersbyuserid(userid);
		map.put("orders", list);
		return map;
	}

	/**
	 * 获取系统暖意
	 * 
	 * @return
	 */
	public Map<String, Object> gainsyswarms() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SystemWarmthInfo> warms = d2pService.querysystemwarms();
		map.put("warms", warms);
		return map;
	}

	/**
	 * 提交送心意，进行支付，
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Map<String, Object> subuserwarm(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		Integer docid = Integer.parseInt(request.getParameter("docid"));
		String orderUuid = request.getParameter("orderUuid");
		String orderType = request.getParameter("orderType");
		String content = request.getParameter("content");
		String warmthMoney = request.getParameter("warmthMoney");
		String warmthName = request.getParameter("warmthName");
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		Integer warmid = generateUserWarm(uwr.getUserId(), docid, orderUuid,
				StringUtils.isNotBlank(orderType) ? Integer.parseInt(orderType)
						: null, content, warmthMoney, warmthName, "");
		Float _money = Float.valueOf(warmthMoney);
		// 支付信息
		Map<String, Object> retMap = WeixinUtil
				.weipay(request, response, _money,
						PropertiesUtil.getString("APPID"),
						PropertiesUtil.getString("APPSECRET"),
						PropertiesUtil.getString("PARTNER"),
						PropertiesUtil.getString("PARTNERKEY"), "送心意", openid,
						PropertiesUtil.getString("DOMAINURL")
								+ "kangxin/wenzhennotify", null, null, null);
		Integer pid = payOrderManager.savePayInfo(
				OrderStatusEnum.D2P_ORDER_VALUE_WARM.getKey(), warmid, retMap
						.get("out_trade_no").toString(), _money, 2, _money,
				0.0f, 0.0f, 0.0f, null);
		map.putAll(retMap);
		try {
			RecordLogUtil.insert(OrderStatusEnum.D2P_ORDER_VALUE_WARM.getKey()
					+ "", "送心意", warmid + "", pid + "",
					(String) retMap.get("paramxml"),
					(String) retMap.get("prepayxml"), "",
					(String) retMap.get("out_trade_no"));
		} catch (Exception e) {

		}
		return map;
	}

	/**
	 * 生成用户暖意记录
	 * 
	 * @param userid
	 * @param docid
	 * @param orderUuid
	 * @param orderType
	 * @param content
	 * @param warmthMoney
	 * @param warmthName
	 * @param remark
	 * @return
	 */
	private Integer generateUserWarm(Integer userid, Integer docid,
			String orderUuid, Integer orderType, String content,
			String warmthMoney, String warmthName, String remark) {
		UserWarmthInfo uw = new UserWarmthInfo();
		uw.setCreateTime(new Timestamp(System.currentTimeMillis()));
		uw.setUserId(userid);
		uw.setDoctorId(docid);
		uw.setContent(content);
		uw.setOrderType(orderType);
		uw.setOrderUuid(orderUuid);
		if (StringUtils.isNotBlank(warmthMoney)) {
			uw.setWarmthMoney(new BigDecimal(warmthMoney));
		}
		uw.setWarmthName(warmthName);
		uw.setRemark(remark);
		uw.setStatus(4);
		return d2pService.saveUserWarmthInfo(uw);
	}

	/**
	 * 各状态订单 （待就诊，待付款，历史订单，带评价）
	 * 
	 * @param openid
	 * @param ltype
	 * @return
	 */
	public List<D2pOrderBean> myorders(String openid, String ltype,
			String pageNo, String pageSize) {
		Integer _pageNo = 1;
		Integer _pageSize = 5;
		if (StringUtils.isNotBlank(pageNo)) {
			_pageNo = Integer.parseInt(pageNo);
		}
		if (StringUtils.isNotBlank(pageSize)) {
			_pageSize = Integer.parseInt(pageSize);
		}
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		List<D2pOrderBean> orders = d2pService.querymyorders(uwr.getUserId(),
				ltype, _pageNo, _pageSize);
		return orders;
	}

	/**
	 * 获取家庭成员信息
	 * 
	 * @param uid
	 * @return
	 */
	public UserContactInfo gainfamilerinfo(Integer uid) {
		UserContactInfo user = weixinService.queryUserContactorInfoById(uid);
		return user;
	}

	/**
	 * 获取家庭成员就诊记录
	 * 
	 * @param uid
	 *            家庭成员id
	 * @return
	 */
	public List<UserMedicalRecord> gainfamrecords(Integer uid) {
		UserContactInfo user = gainfamilerinfo(uid);
		List<UserMedicalRecord> list = d2pService.queryusermedicalrecords(user
				.getUuid());
		return list;
	}

	/**
	 * 新增就诊记录
	 * 
	 * @param request
	 */
	public void adnewrecord(HttpServletRequest request) throws Exception {
		Integer familyid = Integer.parseInt(request.getParameter("familyid"));
		UserContactInfo user = gainfamilerinfo(familyid);
		// 创建病例
		CaseInfo ca = new CaseInfo();
		ca.setUuid(UUID.randomUUID().toString().replace("-", ""));
		ca.setUserId(user.getUserId());
		ca.setSubUserUuid(user.getUuid());
		ca.setCreateTime(new Date());
		ca.setContactName(user.getContactName());
		ca.setIdNumber(user.getIdCard());
		ca.setSex(user.getSex());
		ca.setAge(user.getAge());
		ca.setTelephone(user.getTelphone());
		ca.setCaseName(request.getParameter("diseaseName"));
		ca.setPresentIll(request.getParameter("presentIll"));
		ca.setNormalImages(request.getParameter("normalImages"));
		Integer caseid = processCase(request.getParameter("caseName"),
				request.getParameter("presentIll"),
				request.getParameter("normalImages"), "", user);
		UserMedicalRecord record = new UserMedicalRecord();
		record.setCaseId(caseid);
		record.setUuid(UUID.randomUUID().toString().replace("-", ""));
		record.setCreateTime(new Timestamp(System.currentTimeMillis()));
		record.setUserId(user.getUserId());
		record.setSubUserUuid(user.getUuid());
		if (StringUtils.isNotBlank(request.getParameter("visitDate"))) {
			record.setVisitDate(_sdf.parse(request.getParameter("visitDate")));
		}
		String hosId = request.getParameter("hosId");// 如果是选择的医院，需要传医院id
		String hosName = request.getParameter("hosName");
		String depId = request.getParameter("depId");// 如果是选择的科室，需要传科室id
		String depName = request.getParameter("depName");
		String doctorId = request.getParameter("doctorId");// 如果是选择的医生，需要传医生id
		String doctorName = request.getParameter("doctorName");
		record.setHosName(hosName);
		record.setDepName(depName);
		record.setDoctorName(doctorName);
		if (StringUtils.isNotBlank(hosId)) {
			record.setHosId(Integer.parseInt(hosId));
		}
		if (StringUtils.isNotBlank(depId)) {
			record.setDepId(Integer.parseInt(depId));
		}
		if (StringUtils.isNotBlank(doctorId)) {
			record.setDoctorId(Integer.parseInt(doctorId));
		}
		String firstVisit = request.getParameter("firstVisit");// 0:初诊 1:复诊
		record.setFirstVisit(Integer.parseInt(firstVisit));

		record.setDelFlag(0);
		d2pService.saveUserMedicalRecord(record);
	}

	/**
	 * 医生主页信息
	 * 
	 * @param docid
	 * @return
	 */
	public Map<String, Object> docmain(Integer docid) {
		Map<String, Object> map = new HashMap<String, Object>();
		MobileSpecial doc = commonService
				.queryMobileSpecialByUserIdAndUserType(docid);
		map.put("doc", doc);
		// 开通的服务
		List<DoctorServiceInfo> services = commonService
				.queryOpenDoctorServiceInfo(docid, "6,7");
		map.put("services", services);
		// 收到的心意
		Integer warmcount = d2pService.querydoctorwarms(docid);
		map.put("warmcount", warmcount);
		return map;
	}

	/**
	 * 获取医生评价
	 * 
	 * @param docid
	 * @return
	 */
	public Map<String, Object> gainAppraises(Integer docid, String pageNo,
			String pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer _pageNo = 1;
		Integer _pageSize = 5;
		if (StringUtils.isNotBlank(pageNo))
			_pageNo = Integer.parseInt(pageNo);
		if (StringUtils.isNotBlank(pageSize))
			_pageSize = Integer.parseInt(pageSize);
		List<AppraisementDoctorInfo> list = wenzhenService
				.queryAppraisementDoctorInfosByDoc(docid, _pageNo, _pageSize);
		Integer count = wenzhenService.queryAppraisementDoctorInfosCount(docid);
		map.put("count", count);
		map.put("appraises", list);
		return map;
	}

	/**
	 * 患者报道中的单聊参数获取
	 * 
	 * @param oid
	 * @return
	 */
	public Map<String, Object> gainImTokenD2pSingle(Integer oid) {
		Map<String, Object> map = new HashMap<String, Object>();
		BusinessD2pReportOrder order = d2pService.queryd2preportorderbyid(oid);
		UserContactInfo user = weixinService.queryUserContactInfoByUuid(order
				.getSubUserUuid());
		// 家庭成员-医生 单聊
		String userId_p = RongCloudApi.getRongCloudUserId(user.getId(), 1);
		String userId_d = RongCloudApi.getRongCloudUserId(order.getDoctorId(),
				3);
		map.put("userId", userId_d);
		map.put("appkey", RongCloudConfig.APPKEY);
		if (StringUtils.isNotBlank(user.getRongCloudToken())) {
			map.put("token", user.getRongCloudToken());
		} else {
			String token = RongCloudApi.getUserToken(userId_p,
					user.getContactName(),
					PropertiesUtil.getString("DOMAINURL") + "img/defdoc.jpg");
			user.setRongCloudToken(token);
			weixinService.updateUserContactInfo(user);
			map.put("token", token);
		}
		return map;
	}

	/**
	 * 订单聊天中的群聊参数获取
	 * 
	 * @param oid
	 * @param otype
	 * @return
	 */
	public Map<String, Object> gainImTokenD2pGroup(Integer oid, Integer otype) {
		Map<String, Object> map = new HashMap<String, Object>();
		String orderuuid = "";
		String subUserUuid = "";
		Integer docid = null;
		String groupname = "";
		switch (otype) {
		case 6:
			// 图文问诊
			BusinessD2pTuwenOrder tworder = d2pService
					.queryd2ptuwenorderbyid(oid);
			orderuuid = tworder.getUuid();
			subUserUuid = tworder.getSubUserUuid();
			docid = tworder.getDoctorId();
			groupname = "图文问诊";
			break;
		case 7:
			// 电话问诊
			BusinessD2pTelOrder telorder = d2pService.queryd2ptelorderbyid(oid);
			orderuuid = telorder.getUuid();
			subUserUuid = telorder.getSubUserUuid();
			docid = telorder.getDoctorId();
			groupname = "电话问诊";
			break;
		case 9:
			// 快速问诊
			BusinessD2pFastaskOrder fastorder = d2pService
					.queryd2pfastaskorderbyid(oid);
			orderuuid = fastorder.getUuid();
			subUserUuid = fastorder.getSubUserUuid();
			docid = fastorder.getDoctorId();
			groupname = "快速问诊";
			break;
		}
		map.put("userId", orderuuid);
		map.put("appkey", RongCloudConfig.APPKEY);
		UserContactInfo user = weixinService
				.queryUserContactInfoByUuid(subUserUuid);
		String userId_p = RongCloudApi.getRongCloudUserId(user.getId(), 1);
		if (StringUtils.isNotBlank(user.getRongCloudToken())) {
			map.put("token", user.getRongCloudToken());
		} else {
			String token = RongCloudApi.getUserToken(userId_p,
					user.getContactName(),
					PropertiesUtil.getString("DOMAINURL") + "img/defdoc.jpg");
			user.setRongCloudToken(token);
			weixinService.updateUserContactInfo(user);
			map.put("token", token);
		}
		String userId_d = RongCloudApi.getRongCloudUserId(docid, 3);
		RongCloudApi.joinGroup(new String[] { userId_d, userId_p }, orderuuid,
				groupname);
		return map;
	}

	/**
	 * 获取家庭成员病例信息
	 * 
	 * @param oid
	 * @param otype
	 * @return
	 */
	public Map<String, Object> gainfamilycaseinfo(Integer oid, Integer otype) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer caseid = null;
		switch (otype) {
		case 6:
			// 图文问诊
			BusinessD2pTuwenOrder tworder = d2pService
					.queryd2ptuwenorderbyid(oid);
			caseid = tworder.getCaseId();
			break;
		case 7:
			// 电话问诊
			BusinessD2pTelOrder telorder = d2pService.queryd2ptelorderbyid(oid);
			caseid = telorder.getCaseId();
			break;
		case 9:
			// 快速问诊
			BusinessD2pFastaskOrder fastorder = d2pService
					.queryd2pfastaskorderbyid(oid);
			caseid = fastorder.getCaseId();
			break;
		}
		CaseInfo cinfo = commonService.queryCaseInfoById_new(caseid);
		if (StringUtils.isNotBlank(cinfo.getNormalImages())) {
			List<CustomFileStorage> caseimages = wenzhenService
					.queryCustomFilesByCaseIds(cinfo.getNormalImages());
			map.put("caseimages", caseimages);
		}
		return map;
	}

	/**
	 * 获取聊天记录
	 * 
	 * @param oid
	 * @param otype
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<BusinessMessageBean> gainchatdatas(Integer oid, Integer otype,
			String pageNo, String pageSize) {
		Integer _pageNo = 1, _pageSize = 5;
		if (StringUtils.isNotBlank(pageNo))
			_pageNo = Integer.parseInt(pageNo);
		if (StringUtils.isNotBlank(pageSize))
			_pageSize = Integer.parseInt(pageSize);
		List<BusinessMessageBean> messages = wenzhenService
				.queryBusinessMessageBeansByCon(oid, otype, _pageNo, _pageSize);
		return messages;
	}

	/**
	 * 保存评价
	 * 
	 * @param oid
	 * @param otype
	 * @param content
	 * @param grade
	 */
	public void saveevalua(Integer oid, Integer otype, String content,
			Integer grade) {
		Integer docid = null;
		Integer userid = null;
		String subUserUuid = "";
		String orderUuid="";
		switch (otype) {
		case 6:
			BusinessD2pTuwenOrder tworder = d2pService
					.queryd2ptuwenorderbyid(oid);
			docid = tworder.getDoctorId();
			userid = tworder.getUserId();
			subUserUuid = tworder.getSubUserUuid();
			orderUuid=tworder.getUuid();
			break;
		case 7:
			BusinessD2pTelOrder telorder = d2pService.queryd2ptelorderbyid(oid);
			docid = telorder.getDoctorId();
			userid = telorder.getUserId();
			subUserUuid = telorder.getSubUserUuid();
			orderUuid=telorder.getUuid();
			break;
		case 9:
			BusinessD2pFastaskOrder fastorder = d2pService
					.queryd2pfastaskorderbyid(oid);
			docid = fastorder.getDoctorId();
			userid = fastorder.getUserId();
			subUserUuid = fastorder.getSubUserUuid();
			orderUuid=fastorder.getUuid();
			break;
		}
		processInfoSave(userid, subUserUuid, orderUuid, otype, docid, grade, content);
	}

	// 保存评价数据
	private void processInfoSave(Integer uid, String subUserUuid, String orderUuid,
			Integer ltype, Integer docid, Integer grade, String content) {
		AppraisementDoctorInfo appraisementDoctorInfo = new AppraisementDoctorInfo();
		appraisementDoctorInfo.setContent(content);
		appraisementDoctorInfo.setCreateTime(new Date());
		appraisementDoctorInfo.setDoctorId(docid);
		appraisementDoctorInfo.setGrade(grade);
		appraisementDoctorInfo.setPatientId(uid);

		appraisementDoctorInfo.setOrderUuid(orderUuid);;
		appraisementDoctorInfo.setOrderType(ltype);
		appraisementDoctorInfo.setIsPassed(grade.equals(5) ? 1 : 0);
		wenzhenService.saveAppraisementDoctorInfo(appraisementDoctorInfo);
	}

	/**
	 * 获取医生数据
	 * 
	 * @param search
	 * @param serviceid
	 * @param depid
	 * @param distcode
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<MobileSpecial> gaindoctors(String search, String serviceid,
			String depid, String distcode, String pageNo, String pageSize) {
		Integer _pageNo = 1;
		Integer _pageSize = 5;
		if (StringUtils.isNotBlank(pageNo))
			_pageNo = Integer.parseInt(pageNo);
		if (StringUtils.isNotBlank(pageSize))
			_pageSize = Integer.parseInt(pageSize);
		List<MobileSpecial> docs = d2pService.querydoctors(search, serviceid,
				depid, distcode, _pageNo, _pageSize);
		for (MobileSpecial _doc : docs) {
			processserverdata(_doc);
		}
		return docs;
	}

	/**
	 * 设置医生服务开通数据
	 * 
	 * @param _doc
	 */
	private void processserverdata(MobileSpecial _doc) {
		DoctorServiceInfo ds = commonService.queryDoctorServiceInfoByCon(
				_doc.getSpecialId(), 6, 6);
		if (ds != null) {
			if (ds.getIsOpen() != null && ds.getIsOpen().equals(1)) {
				_doc.setOpenAsk(1);// 开通图文
				_doc.setAskAmount(ds.getAmount());
			}
		} else {
			_doc.setOpenAsk(0);// 未开通
		}
		DoctorServiceInfo _ds = commonService.queryDoctorServiceInfoByCon(
				_doc.getSpecialId(), 7, 7);
		if (_ds != null) {
			if (_ds.getIsOpen() != null && _ds.getIsOpen().equals(1)) {
				_doc.setOpenTel(1);// 开通电话
				_doc.setTelAmount(_ds.getAmount());
			}
		} else {
			_doc.setOpenTel(0);// 未开通
		}
	}

	private SimpleDateFormat hour_sdf = new SimpleDateFormat("HH:mm");
	private SimpleDateFormat day_sdf = new SimpleDateFormat("MM-dd");

	public List<AdviceBean> newlyadvices(String openid) {
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		List<AdviceBean> list = d2pService.newlyadvices(uwr.getUserId());
		for (AdviceBean bean : list) {
			if (bean.getMsgTime() != null) {
				if (isToday(bean.getMsgTime())) {
					// ====今天===
					bean.setTimeStr(hour_sdf.format(bean.getMsgTime()));
				} else if (isYestody(bean.getMsgTime())) {
					// ===昨天===
					bean.setTimeStr("昨天");
				} else {
					// ===之前日期===
					bean.setTimeStr(day_sdf.format(bean.getMsgTime()));
				}
			}
		}
		return list;
	}

	private static boolean isYestody(Date date) {
		long delta = new Date().getTime() - date.getTime();
		if (delta < 48L * 3600000L) {
			return true;
		}
		return false;
	}

	private static boolean isToday(Date date) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		if (fmt.format(date).toString()
				.equals(fmt.format(new Date()).toString())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 继续支付
	 * 
	 * @param openid
	 * @param oid
	 * @param otype
	 * @param request
	 * @param response
	 * @return
	 */
	public Map<String, Object> continuepay(String openid, Integer oid,
			Integer otype, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer docid = null;
		String desc = "";
		switch (otype) {
		case 4:
			BusinessVedioOrder vorder=wenzhenService.queryBusinessVedioOrderById(oid);
			docid=vorder.getExpertId();
			desc="远程会诊";
			break;
		case 6:
			BusinessD2pTuwenOrder tworder = d2pService
					.queryd2ptuwenorderbyid(oid);
			docid = tworder.getDoctorId();
			desc = "图文问诊";
			break;
		case 7:
			BusinessD2pTelOrder telorder = d2pService.queryd2ptelorderbyid(oid);
			docid = telorder.getDoctorId();
			desc = "电话问诊";
			break;
		case 9:
			BusinessD2pFastaskOrder fastorder = d2pService
					.queryd2pfastaskorderbyid(oid);
			docid = fastorder.getDoctorId();
			desc = "快速问诊";
			fastorder.setDoctorId(docid);
			d2pService.updated2pfastaskorder(fastorder);
			break;
		}
		if (docid == null) {
			map.put("status", "error");// 未选择医生
		} else {
			map.put("status", "success");// 可以进行支付
			Float money = commonManager.gainMoneyByOrder(otype, docid)
					.floatValue();
			Map<String, Object> retMap = WeixinUtil.weipay(request, response,
					money, PropertiesUtil.getString("APPID"),
					PropertiesUtil.getString("APPSECRET"),
					PropertiesUtil.getString("PARTNER"),
					PropertiesUtil.getString("PARTNERKEY"), desc, openid,
					PropertiesUtil.getString("DOMAINURL")
							+ "kangxin/wenzhennotify", null, null, null);
			Integer pid = payOrderManager.savePayInfo(otype, oid,
					retMap.get("out_trade_no").toString(), money, 2, money,
					0.0f, 0.0f, 0.0f, null);
			map.putAll(retMap);
			try {
				RecordLogUtil.insert(otype.toString(), desc, oid + "",
						pid + "", (String) retMap.get("paramxml"),
						(String) retMap.get("prepayxml"), "",
						(String) retMap.get("out_trade_no"));
			} catch (Exception e) {

			}
		}
		return map;
	}

	/**
	 * 电话问诊提交
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> subtelorder(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		Integer docid = Integer.parseInt(request.getParameter("docid"));
		String answerTelephone = request.getParameter("answerTelephone");// 接听电话
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		Integer userid = uwr.getUserId();
		Integer familyid = Integer.parseInt(request.getParameter("familyId"));// 家庭成员id
		UserContactInfo familer = weixinService
				.queryUserContactorInfoById(familyid);
		Integer caseid = processCase(request.getParameter("caseName"),
				request.getParameter("presentIll"),
				request.getParameter("normalImages"),
				request.getParameter("askProblem"), familer);// 创建病例
		// 创建订单
		Integer oid = processTelOrder(openid, caseid, userid, docid,
				familer.getUuid(), answerTelephone);
		map.put("oid", oid);
		// 支付参数
		map.putAll(continuepay(openid, oid, 7, request, response));
		return map;
	}

	/**
	 * 图文问诊提交
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> subtuwenorder(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		Integer docid = Integer.parseInt(request.getParameter("docid"));
		UserWeixinRelative uwr = weixinService
				.queryUserWeiRelativeByOpenId(openid);
		Integer userid = uwr.getUserId();
		Integer familyid = Integer.parseInt(request.getParameter("familyId"));// 家庭成员id
		UserContactInfo familer = weixinService
				.queryUserContactorInfoById(familyid);
		Integer caseid = processCase(request.getParameter("caseName"),
				request.getParameter("presentIll"),
				request.getParameter("normalImages"),
				request.getParameter("askProblem"), familer);// 创建病例
		// 创建订单
		Integer oid = processTuwenOrder(openid, caseid, userid, docid,
				familer.getUuid());
		map.put("oid", oid);
		// 支付参数
		map.putAll(continuepay(openid, oid, 6, request, response));
		return map;
	}

	/**
	 * 创建电话订单
	 * 
	 * @param openid
	 * @param caseid
	 * @param userid
	 * @param docid
	 * @param subUserUuid
	 * @return
	 */
	private Integer processTelOrder(String openid, Integer caseid,
			Integer userid, Integer docid, String subUserUuid,
			String answerTelephone) {
		BusinessD2pTelOrder telorder = new BusinessD2pTelOrder();
		telorder.setUuid(UUID.randomUUID().toString().replace("-", ""));
		telorder.setCreateTime(new Timestamp(System.currentTimeMillis()));
		telorder.setCaseId(caseid);
		telorder.setDoctorId(docid);
		telorder.setUserId(userid);
		telorder.setSubUserUuid(subUserUuid);
		telorder.setSource(3);
		telorder.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());
		telorder.setPayStatus(4);
		telorder.setDelFlag(0);
		telorder.setOpenId(openid);
		return d2pService.saveBusinessD2pTelOrder(telorder);
	}

	/**
	 * 创建图文问诊订单
	 * 
	 * @param openid
	 * @param caseid
	 * @param userid
	 * @param docid
	 * @param subUserUuid
	 * @return
	 */
	private Integer processTuwenOrder(String openid, Integer caseid,
			Integer userid, Integer docid, String subUserUuid) {
		BusinessD2pTuwenOrder tuwenorder = new BusinessD2pTuwenOrder();
		tuwenorder.setUuid(UUID.randomUUID().toString().replace("-", ""));
		tuwenorder.setCreateTime(new Timestamp(System.currentTimeMillis()));
		tuwenorder.setCaseId(caseid);
		tuwenorder.setDoctorId(docid);
		tuwenorder.setUserId(userid);
		tuwenorder.setSubUserUuid(subUserUuid);
		tuwenorder.setSource(3);
		tuwenorder.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());
		tuwenorder.setPayStatus(4);
		tuwenorder.setDelFlag(0);
		tuwenorder.setOpenId(openid);
		return d2pService.saveBusinessD2pTuwenOrder(tuwenorder);
	}

	/**
	 * 获取标准科室 （热门科室，全部标准科室，分页/不分页）
	 * 
	 * @param ispage
	 * @param ishot
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<StandardDepartmentInfo> gainstanddeps(String ispage,
			String ishot, String pageNo, String pageSize) {
		Integer _pageNo = 1, _pageSize = 5;
		if (StringUtils.isNotBlank(pageNo))
			_pageNo = Integer.parseInt(pageNo);
		if (StringUtils.isNotBlank(pageSize))
			_pageSize = Integer.parseInt(pageSize);
		return d2pService.querystanddeps(ispage, ishot, _pageNo, _pageSize);
	}

	/**
	 * 获取附近医院 是否分页，省市区级别医院
	 * 
	 * @param ispage
	 * @param htype
	 * @param distcode
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<HospitalDetailInfo> gainnearhoses(String ispage, String htype,
			String distcode, String pageNo, String pageSize) {
		Integer _pageNo = 1, _pageSize = 5;
		if (StringUtils.isNotBlank(pageNo))
			_pageNo = Integer.parseInt(pageNo);
		if (StringUtils.isNotBlank(pageSize))
			_pageSize = Integer.parseInt(pageSize);
		return d2pService.querynearhoses(ispage, htype, distcode, _pageNo,
				_pageSize);
	}

	/**
	 * 心意墙
	 * 
	 * @param docid
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Map<String, Object> warmwall(Integer docid, String pageNo,
			String pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer warmcount = d2pService.querydoctorwarms(docid);
		map.put("warmcount", warmcount);
		Integer _pageNo = 1, _pageSize = 5;
		if (StringUtils.isNotBlank(pageNo))
			_pageNo = Integer.parseInt(pageNo);
		if (StringUtils.isNotBlank(pageSize))
			_pageSize = Integer.parseInt(pageSize);
		List<UserWarmthInfo> list = d2pService.querydoctorwarms_list(docid,
				_pageNo, _pageSize);
		map.put("warms", list);
		return map;
	}

	public void updatecontact() {
		List<UserContactInfo> list = weixinService
				.queryUserContactInfosByOpenId("");
		Integer i = 0;
		for (UserContactInfo user : list) {
			user.setUuid(UUID.randomUUID().toString().replace("-", ""));
			weixinService.updateUserContactInfo(user);
			System.out.println("====完成" + i++ + "条数据修改");
		}
	}

	/**
	 * 获取区域数据
	 * 
	 * @param type
	 * @param parentCode
	 * @return
	 */
	public List<DistCode> gainArea(String type, String parentCode) {
		return d2pService.gainArea(type, parentCode);
	}

	/**
	 * 保存消息
	 * 
	 * @param otype
	 * @param oid
	 * @param msgContent
	 * @param fileUrl
	 * @param msgType
	 * @return
	 */
	public BusinessMessageBean saveMessage(Integer otype, Integer oid,
			String msgContent, String fileUrl, String msgType) {
		if (msgType.equalsIgnoreCase("text")
				|| msgType.equalsIgnoreCase("image")) {
			return saveTextOrImageMessage(otype, oid, msgContent, fileUrl,msgType);
		}
		return null;
	}

	/**
	 * 新增文本或图片消息
	 * 
	 * @param otype
	 * @param oid
	 * @param msgContent
	 * @param fileUrl
	 * @return
	 */
	public BusinessMessageBean saveTextOrImageMessage(Integer otype,
			Integer oid, String msgContent, String fileUrl,String msgType) {
		BusinessMessageBean msg = new BusinessMessageBean();
		Map<String, Object> retmap = gainorderuuid(otype, oid);
		msg.setOrderUuid(retmap.get("uuid").toString());
		msg.setOrderId(oid);
		msg.setOrderType(otype);
		if(msgType.equalsIgnoreCase("text")){
			msg.setMsgType("text");
		}else if(msgType.equalsIgnoreCase("image")){
			msg.setMsgType("image/jpg");
		}
		
		msg.setMsgContent(msgContent);
		msg.setRecvId(Integer.valueOf(retmap.get("docid").toString()));
		msg.setRecvType(3);
		msg.setSendId(Integer.valueOf(retmap.get("userid").toString()));
		msg.setSendType(1);
		msg.setFileUrl(fileUrl);
		msg.setSendTime(new Timestamp(System.currentTimeMillis()));
		switch (otype) {
		case 6:
		case 7:
		case 9:
			msg.setChannelType("GROUP");
			break;
		case 8:
			msg.setChannelType("PERSON");
			break;
		}
		wenzhenService.saveBusinessMessageBean(msg);
		msg.setMsgTime(sdf.format(msg.getSendTime()));
		return msg;
	}

	private Map<String, Object> gainorderuuid(Integer otype, Integer oid) {
		Map<String, Object> map = new HashMap<String, Object>();
		String uuid = "";
		Integer userid = null;
		Integer docid = null;
		switch (otype) {
		case 6:
			// 图文问诊
			BusinessD2pTuwenOrder tworder = d2pService
					.queryd2ptuwenorderbyid(oid);
			uuid = tworder.getUuid();
			userid = tworder.getUserId();
			docid = tworder.getDoctorId();
			break;
		case 7:
			// 电话问诊
			BusinessD2pTelOrder telorder = d2pService.queryd2ptelorderbyid(oid);
			uuid = telorder.getUuid();
			userid = telorder.getUserId();
			docid = telorder.getDoctorId();
			break;
		case 8:
			BusinessD2pReportOrder reorder = d2pService
					.queryd2preportorderbyid(oid);
			reorder.getUuid();
			userid = reorder.getUserId();
			docid = reorder.getDoctorId();
			break;
		case 9:
			// 快速问诊
			BusinessD2pFastaskOrder fastorder = d2pService
					.queryd2pfastaskorderbyid(oid);
			uuid = fastorder.getUuid();
			userid = fastorder.getUserId();
			docid = fastorder.getDoctorId();
			break;
		}
		map.put("uuid", uuid);
		map.put("userid", userid);
		map.put("docid", docid);
		return map;
	}
	
	public List<UserBillRecord> gainuserbills(String openid,String ispage,String pageNo,String pageSize){
		Integer _pageNo=1,_pageSize=5;
		UserWeixinRelative uwr = weixinService.queryUserWeiRelativeByOpenId(openid);
		Integer userid = uwr.getUserId();
		if(StringUtils.isNotBlank(pageNo))_pageNo=Integer.parseInt(pageNo);
		if(StringUtils.isNotBlank(pageSize))_pageSize=Integer.parseInt(pageSize);
		return wenzhenService.gainuserbills(userid,ispage,_pageNo,_pageSize);
	}
	
	
	/**
	 * 获取患者-医生 患者报道订单数据
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryd2preportdatas(Map<String, String> paramMap) {
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		String search = paramMap.get("search");
		Integer ostatus=Integer.parseInt(paramMap.get("ostatus"));
		Map<String, Object> querymap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(search)) {
			querymap.put("search", search);
		}
		querymap.put("ostatus", ostatus);
		StringBuilder stringJson = null;
		Map<String, Object> retmap = d2pService.queryd2preportdatas(querymap,
				start, length);
		Integer renum = (Integer) retmap.get("num");
		List<D2pOrderBean> orders = (List<D2pOrderBean>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		D2pOrderBean dfastask = null;
		for (int i = 0; i < orders.size(); i++) {
			dfastask = orders.get(i);
			stringJson.append("[");
			stringJson.append("\"" + dfastask.getId() + "\",");
			stringJson.append("\"" + dfastask.getUserName() + "\",");
			stringJson.append("\""
					+ (dfastask.getSex() == null ? "未知" : (dfastask.getSex()
							.equals(1) ? "男" : "女")) + "\",");
			stringJson.append("\""
					+ (dfastask.getAge() == null ? "未知" : dfastask.getAge())
					+ "\",");
			stringJson.append("\"" + dfastask.getTelphone() + "\",");
			stringJson.append("\"" + dfastask.getDocName() + "\",");
			stringJson.append("\"" + dfastask.getHosName() + "\",");
			stringJson.append("\"" + dfastask.getDepName() + "\",");
			stringJson.append("\"" + dfastask.getProfession() + "\",");
			stringJson.append("\"" + sdf.format(dfastask.getCreateTime())
					+ "\",");	
			stringJson.append("\"" + (dfastask.getReceiveTime()!=null?sdf.format(dfastask.getReceiveTime()):"") + "\",");
			stringJson.append("\"" + gainstatusdesc_report(dfastask.getStatus())
					+ "\",");
			stringJson.append("\"" + (dfastask.getChatStatus()==null?"关闭":(dfastask.getChatStatus().equals(1)?"开启":"关闭"))
					+ "\",");
			stringJson.append("\"" + "" + "\"");
			stringJson.append("],");
		}
		if (orders.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	
	/**
	 * 获取患者-医生 患者会诊需求申请单数据
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryd2pconreqdatas(Map<String, String> paramMap) {
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		String search = paramMap.get("search");
		Integer ostatus=Integer.parseInt(paramMap.get("ostatus"));
		Map<String, Object> querymap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(search)) {
			querymap.put("search", search);
		}
		querymap.put("ostatus", ostatus);
		StringBuilder stringJson = null;
		Map<String, Object> retmap = d2pService.queryd2pconreqdatas(querymap,
				start, length);
		Integer renum = (Integer) retmap.get("num");
		List<D2pOrderBean> orders = (List<D2pOrderBean>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		D2pOrderBean dfastask = null;
		for (int i = 0; i < orders.size(); i++) {
			dfastask = orders.get(i);
			stringJson.append("[");
			stringJson.append("\"" + dfastask.getId() + "\",");
			stringJson.append("\"" + dfastask.getUserName() + "\",");
			stringJson.append("\""
					+ (dfastask.getSex() == null ? "未知" : (dfastask.getSex()
							.equals(1) ? "男" : "女")) + "\",");
			stringJson.append("\""
					+ (dfastask.getAge() == null ? "未知" : dfastask.getAge())
					+ "\",");
			stringJson.append("\"" + dfastask.getTelphone() + "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(dfastask.getDocName())?dfastask.getDocName():"")+ "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(dfastask.getHosName())?dfastask.getHosName():"")+ "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(dfastask.getDepName())?dfastask.getDepName():"")+ "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(dfastask.getProfession())?dfastask.getProfession():"")+ "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(dfastask.getTeamName())?dfastask.getTeamName():"") + "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(dfastask.getTdocName())?dfastask.getTdocName():"") + "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(dfastask.getThosName())?dfastask.getThosName():"") + "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(dfastask.getTdepName())?dfastask.getTdepName():"") + "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(dfastask.getTprofession())?dfastask.getTprofession():"") + "\",");
			stringJson.append("\"" + sdf.format(dfastask.getCreateTime())
					+ "\",");	
			stringJson.append("\"" + (dfastask.getReceiveTime()!=null?sdf.format(dfastask.getReceiveTime()):"") + "\",");
			stringJson.append("\"" + gainstatusdesc_con_req(dfastask.getStatus())
					+ "\",");
			stringJson.append("\"" + (dfastask.getConsultationType().equals(4)?"视频会诊":"图文会诊")
					+ "\",");
			stringJson.append("\"" + "" + "\"");
			stringJson.append("],");
		}
		if (orders.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String queryd2preferdatas(Map<String, String> paramMap) {
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		String search = paramMap.get("search");
		Integer ostatus=Integer.parseInt(paramMap.get("ostatus"));
		Map<String, Object> querymap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(search)) {
			querymap.put("search", search);
		}
		querymap.put("ostatus", ostatus);
		StringBuilder stringJson = null;
		Map<String, Object> retmap = d2pService.queryd2preferdatas(querymap,
				start, length);
		Integer renum = (Integer) retmap.get("num");
		List<D2pOrderBean> orders = (List<D2pOrderBean>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		D2pOrderBean obean = null;
		for (int i = 0; i < orders.size(); i++) {
			obean = orders.get(i);
			stringJson.append("[");
			stringJson.append("\"" + obean.getId() + "\",");
			stringJson.append("\"" + obean.getUserName() + "\",");
			stringJson.append("\""
					+ (obean.getSex() == null ? "未知" : (obean.getSex()
							.equals(1) ? "男" : "女")) + "\",");
			stringJson.append("\""
					+ (obean.getAge() == null ? "未知" : obean.getAge())
					+ "\",");
			stringJson.append("\"" + obean.getTelphone() + "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(obean.getDocName())?obean.getDocName():"")+ "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(obean.getHosName())?obean.getHosName():"")+ "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(obean.getDepName())?obean.getDepName():"")+ "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(obean.getProfession())?obean.getProfession():"")+ "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(obean.getTdocName())?obean.getTdocName():"") + "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(obean.getThosName())?obean.getThosName():"") + "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(obean.getTdepName())?obean.getTdepName():"") + "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(obean.getTprofession())?obean.getTprofession():"") + "\",");
			stringJson.append("\"" + sdf.format(obean.getCreateTime())
					+ "\",");	
			stringJson.append("\"" + (StringUtils.isNotBlank(obean.getOrderDate())?obean.getOrderDate():"") + "\",");
			stringJson.append("\"" + (obean.getConsultationType().equals(1)?"住院":"门诊")
					+ "\",");
			stringJson.append("\"" + (obean.getReceiveTime()!=null?sdf.format(obean.getReceiveTime()):"") + "\",");
			stringJson.append("\"" + gainstatusdesc_ref(obean.getStatus())
					+ "\",");
			stringJson.append("\"" + "" + "\"");
			stringJson.append("],");
		}
		if (orders.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	/**
	 * 获取待审核的医生团队数据
	 * @param paramMap
	 * @return
	 */
	public String queryauditdocteamdatas(Map<String, String> paramMap){
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		String search = paramMap.get("search");
		Integer ostatus=Integer.parseInt(paramMap.get("ostatus"));
		Map<String, Object> querymap = new HashMap<String, Object>();
		querymap.put("ostatus", ostatus);
		if(StringUtils.isNotBlank(search)){
			querymap.put("search", search);
		}
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.queryauditdocteamdatas(querymap,
				start, length);
		Integer renum = (Integer) retmap.get("num");
		List<DoctorTeam> orders = (List<DoctorTeam>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		DoctorTeam obean = null;
		for (int i = 0; i < orders.size(); i++) {
			obean = orders.get(i);
			stringJson.append("[");
			stringJson.append("\"" + obean.getId() + "\",");
			stringJson.append("\"" + obean.getApplicant() + "\",");
			stringJson.append("\"" + (obean.getApplicantType().equals(2)?"专家":"医生") + "\",");
			stringJson.append("\"" + obean.getTeamName() + "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(obean.getKeywords())?obean.getKeywords():"") + "\",");
			stringJson.append("\"" + sdf.format(obean.getApplicationTime()) + "\",");
			stringJson.append("\"" + "" + "\"");
			stringJson.append("],");
		}
		if (orders.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	/**
	 * 获取医生团队数据
	 * @param paramMap
	 * @return
	 */
	public String querydocteamdatas(Map<String, String> paramMap){
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		String search = paramMap.get("search");
		Map<String, Object> querymap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(search)){
			querymap.put("search", search);
		}
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.querydocteamdatas(querymap,
				start, length);
		Integer renum = (Integer) retmap.get("num");
		List<DoctorTeam> orders = (List<DoctorTeam>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		DoctorTeam obean = null;
		for (int i = 0; i < orders.size(); i++) {
			obean = orders.get(i);
			stringJson.append("[");
			stringJson.append("\"" + obean.getId() + "\",");
			stringJson.append("\"" + obean.getTeamName() + "\",");
			stringJson.append("\"" +obean.getDocName()+(StringUtils.isNotBlank(obean.getDocDuty())?"/"+obean.getDocDuty():"")
					+(StringUtils.isNotBlank(obean.getDocProfession())?"/"+obean.getDocProfession():"")+ "\",");
			stringJson.append("\"" +obean.getHosName() + "\",");
			stringJson.append("\"" +obean.getDepName() + "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(obean.getKeywords())?obean.getKeywords():"") + "\",");
			stringJson.append("\"" + (obean.getAreaOptimal()!=null?(obean.getAreaOptimal().equals(1)?"是":"否"):"否")+ "\",");
			stringJson.append("\"" +obean.getMemNum() + "\",");
			stringJson.append("\"" + (obean.getAuditTime()!=null ? sdf.format(obean.getAuditTime()):"") + "\",");
			stringJson.append("\"" + ((obean.getIsTest()!=null && obean.getIsTest().equals(1))?"是":"否")+ "\",");
			stringJson.append("\"" + "" + "\"");
			stringJson.append("],");
		}
		if (orders.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	
	
	
	
	/**
	 * 获取团队问诊数据
	 * @param paramMap
	 * @return
	 */
	public String queryt2ptuwendatas(Map<String, String> paramMap) throws Exception{
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer ostatus=Integer.parseInt(paramMap.get("ostatus"));
		String search = paramMap.get("search");
		Map<String, Object> querymap = new HashMap<String, Object>();
		querymap.put("ostatus", ostatus);
		if(StringUtils.isNotBlank(search)){
			querymap.put("search", search);
		}
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.queryt2ptuwendatas(querymap,
				start, length);
		
		Integer renum = (Integer) retmap.get("num");
		List<BusinessT2pTuwenOrder> orders = (List<BusinessT2pTuwenOrder>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		BusinessT2pTuwenOrder obean = null;
		for (int i = 0; i < orders.size(); i++) {
			obean = orders.get(i);
			List<BusinessPayInfo> payinfos = wenzhenService
					.queryBusinesPayInfosByOId(obean.getId(), 12);
			stringJson.append("[");
			stringJson.append("\"" + obean.getId() + "\",");
			stringJson.append("\"" + obean.getUserName() + "\",");
			stringJson.append("\"" +(obean.getSex()!=null?(obean.getSex().equals(1)?"男":"女"):"未知") + "\",");
			stringJson.append("\"" +(obean.getAge()!=null?obean.getAge():"未知")+(StringUtils.isNotBlank(obean.getTelephone())?"/"+obean.getTelephone():"") + "\",");
			stringJson.append("\"" +obean.getTeamName() + "\",");
			stringJson.append("\"" +obean.getTdocName() + "\",");
			stringJson.append("\"" +obean.getTdocHos() + "\",");
			stringJson.append("\"" +obean.getTdocDep() + "\",");
			stringJson.append("\"" +obean.getDocName() + "\",");
			stringJson.append("\"" +obean.getDocHos() + "\",");
			stringJson.append("\"" +obean.getDocDep() + "\",");
			stringJson.append("\"" +obean.getTimeStr() + "\",");
			stringJson.append("\"" +(obean.getReceiveTime()!=null?sdf.format(obean.getReceiveTime()):"") + "\",");
			stringJson.append("\"" +(obean.getNeedDispatch()!=null?(obean.getNeedDispatch().equals(1)?"是":"否"):"否") + "\",");
			stringJson.append("\"" +(obean.getPayStatus()!=null?(obean.getPayStatus().equals(4)?"未支付":"已支付"):"未支付") + "\",");
			BigDecimal money=payinfo(payinfos);
			stringJson.append("\"" +(money==null?"":money) + "\",");
			stringJson.append("\"" + ((obean.getPayStatus()!=null&&obean.getPayStatus().equals(1)&&money!=null)?gainRefundDesc(obean.getRefundStatus()):"") + "\",");
			stringJson.append("\"" + (StringUtils.isNotBlank(obean.getRefundTime())?obean.getRefundTime():"") + "\",");
			stringJson.append("\"" + "" + "\"");
			stringJson.append("],");
		}
		if (orders.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	
	public String queryDoctor(Map<String, String> paramMap){
		String sEcho = paramMap.get("sEcho");
		// 搜索内容
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		String search = paramMap.get("search");
		Map<String, Object> querymap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(search)){
			querymap.put("search", search);
		}
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.queryDoctor(querymap,
				start, length);
		Integer renum = (Integer) retmap.get("num");
		List<MobileSpecial> doctors = (List<MobileSpecial>) retmap.get("items");
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		MobileSpecial doc = null;
		for (int i = 0; i < doctors.size(); i++) {
			doc = doctors.get(i);
			stringJson.append("[");
			stringJson.append("\"" + doc.getSpecialId() + "\",");
			stringJson.append("\"" + doc.getSpecialName() + "\",");
			stringJson.append("\"" + doc.getHosName()+ "\",");
			stringJson.append("\"" + doc.getDepName() + "\",");
			stringJson.append("\"" + doc.getTelphone() + "\",");
			stringJson.append("\"" + (doc.getUserType().equals(3)?"医生":"专家") + "\",");
			stringJson.append("\"" + "" + "\"");
			stringJson.append("],");
		}
		if (doctors.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	private String gainstatusdesc(Integer status) {
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
	private String gainstatusdesc_report(Integer status) {
		String desc = "";
		switch (status) {
		case 10:
			desc = "待审核";
			break;
		case 20:
			desc = "已同意";
			break;
		case 30:
			desc = "已拒绝";
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
	private String gainstatusdesc_con_req(Integer status) {
		String desc = "";
		switch (status) {
		case 10:
			desc = "待审核";
			break;
		case 20:
			desc = "已接收";
			break;
		case 30:
			desc = "已拒绝";
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
	private String gainstatusdesc_ref(Integer status) {
		String desc = "";
		switch (status) {
		case 10:
			desc = "待接诊";
			break;
		case 20:
			desc = "已接诊";
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
	private BigDecimal payinfo(List<BusinessPayInfo> payinfos)
			throws Exception {
		BigDecimal money = null;
		if (payinfos != null && payinfos.size() > 0) {
			money = payinfos.get(payinfos.size() - 1).getTotalMoney();
		}
		return money;
	}
}
