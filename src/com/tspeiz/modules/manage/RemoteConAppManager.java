package com.tspeiz.modules.manage;

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

import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.dao.IRemoteConsultationAppDao;
import com.tspeiz.modules.common.dao.IRemoteConsultationAppRecommendDao;
import com.tspeiz.modules.common.entity.RemoteConsultationApp;
import com.tspeiz.modules.common.entity.RemoteConsultationAppRecommend;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorDetailInfo;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.impl.WeixinServiceImpl;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.config.ReaderConfigUtil;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;
import com.tspeiz.modules.util.log.RecordLogUtil;
import com.tspeiz.modules.util.weixin.WeixinUtil;

/**
 * 远程会诊申请辅助
 * 
 * @author heyongb
 * 
 */
@Service
public class RemoteConAppManager {
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	@Autowired
	private IRemoteConsultationAppDao remoteConsultationAppDao;
	@Autowired
	private IRemoteConsultationAppRecommendDao remoteConsultationAppRecommendDao;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IWenzhenService wenzhenService;
	@Autowired
	private IWeixinService weixinService;
	@Autowired
	private PayOrderManager payOrderManager;

	/**
	 * 申请表基本信息填写
	 * 
	 * @param request
	 * @param app
	 * @return
	 */
	public String basicAppSave(HttpServletRequest request,
			RemoteConsultationApp app) {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		app.setUuid(uuid);
		app.setAppCreateTime(sdf.format(new Date()));
		app.setStatus(1);
		remoteConsultationAppDao.save(app);
		return uuid;
	}

	/**
	 * 根据uuid获取申请单
	 * 
	 * @param uuid
	 * @return
	 */
	public RemoteConsultationApp getRemoteConsultationAppByUuid(String uuid) {
		return remoteConsultationAppDao.getRemoteConsultationAppByUuid(uuid);
	}

	/**
	 * 更新会诊申请单
	 * 
	 * @param app
	 */
	public void updateRemoteConsultationApp(RemoteConsultationApp app) {
		remoteConsultationAppDao.update(app);
	}

	/**
	 * 完成分诊信息的填写
	 * 
	 * @param uuid
	 * @param request
	 */
	public RemoteConsultationApp compeletetriage(String uuid, HttpServletRequest request) {
		String ids = request.getParameter("expertids");// 推荐专家id集合 1,2
		String[] _ids = ids.split(",");
		String triagePerson = request.getParameter("triagePerson");// 分诊负责人
		String triagePersonTel = request.getParameter("triagePersonTel");// 分诊负责人电话
		if (_ids != null && _ids.length > 0) {
			for (String id : _ids) {
				if (StringUtils.isNotBlank(id)) {
					RemoteConsultationAppRecommend rec = new RemoteConsultationAppRecommend();
					MobileSpecial sp = commonService
							.queryMobileSpecialByUserIdAndUserType(Integer
									.parseInt(id));
					rec.setUuid(uuid);
					rec.setHospitalId(sp.getHosId());
					rec.setHosName(sp.getHosName());
					rec.setDepId(sp.getDepId());
					rec.setDepName(sp.getDepName());
					rec.setDuty(sp.getDuty());
					rec.setDoctorId(sp.getSpecialId());
					rec.setDocName(sp.getSpecialName());
					rec.setHeadImage(sp.getListSpecialPicture());
					remoteConsultationAppRecommendDao.save(rec);
				}
			}
		}
		RemoteConsultationApp app = getRemoteConsultationAppByUuid(uuid);
		app.setTriagePerson(triagePerson);
		app.setTriagePersonTel(triagePersonTel);
		app.setStatus(2);// 分诊完成，待确认会诊
		updateRemoteConsultationApp(app);
		return app;
	}

	/**
	 * 完成确认会诊信息
	 * 
	 * @param uuid
	 * @param request
	 */
	public RemoteConsultationApp completeconfirm(String uuid, HttpServletRequest request) {
		RemoteConsultationApp app = getRemoteConsultationAppByUuid(uuid);
		app.setChargePersonName(request.getParameter("chargePersonName"));// 具体负责人
		app.setChargePersonTel(request.getParameter("chargePersonTel"));// 联系方式
		app.setConExpertId(Integer.parseInt(request.getParameter("expertId")));// 确认会诊专家
		app.setConSureTime(request.getParameter("conSureTime"));// 确定会诊时间
		app.setStatus(3);// 完成确认
		updateRemoteConsultationApp(app);
		return app;
	}

	/**
	 * 根据会诊申请单生成订单并支付
	 * 
	 * @param request
	 * @return 支付信息
	 */
	public Map<String, Object> generate_order(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String uuid = request.getParameter("uuid");// 申请单id
		RemoteConsultationApp app = getRemoteConsultationAppByUuid(uuid);
		// 生成远程订单
		Integer oid = generateremoteorder(app);
		app.setOrderId(oid);
		app.setStatus(4);
		remoteConsultationAppDao.update(app);
		String product_id = UUID.randomUUID().toString().replace("-", "");
		Map<String, Object> retMap = WeixinUtil.weipay_pc(request, response,
				app.getConMoney().floatValue(),
				PropertiesUtil.getString("APPID"),
				PropertiesUtil.getString("APPSECRET"),
				PropertiesUtil.getString("PARTNER"),
				PropertiesUtil.getString("PARTNERKEY"), "远程会诊", product_id,
				PropertiesUtil.getString("PayCallBackUrl") + "d2p/paynotify",
				null, null, null);
		Integer pid = payOrderManager.savePayInfo(4, oid,
				retMap.get("out_trade_no").toString(), app.getConMoney()
						.floatValue(), 2, app.getConMoney().floatValue(), 0.0f,
				0.0f, 0.0f, null);
		map.put("code_url", retMap.get("code_url"));
		map.put("out_trade_no", retMap.get("out_trade_no"));
		try {
			RecordLogUtil.insert("4", "视频会诊", oid + "", pid + "",
					(String) retMap.get("paramxml"),
					(String) retMap.get("prepayxml"), "",
					(String) retMap.get("out_trade_no"));
		} catch (Exception e) {

		}
		return map;
	}

	public Map<String, Object> gainerweima(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String uuid = request.getParameter("uuid");// 申请单id
		Integer oid = Integer.parseInt(request.getParameter("oid"));// 远程订单id
		RemoteConsultationApp app = getRemoteConsultationAppByUuid(uuid);
		String product_id = UUID.randomUUID().toString().replace("-", "");
		Map<String, Object> retMap = WeixinUtil.weipay_pc(request, response,
				app.getConMoney().floatValue(),
				PropertiesUtil.getString("APPID"),
				PropertiesUtil.getString("APPSECRET"),
				PropertiesUtil.getString("PARTNER"),
				PropertiesUtil.getString("PARTNERKEY"), "视频会诊", product_id,
				PropertiesUtil.getString("PayCallBackUrl") + "d2p/paynotify",
				null, null, null);
		Integer pid = payOrderManager.savePayInfo(4, oid,
				retMap.get("out_trade_no").toString(), app.getConMoney()
						.floatValue(), 2, app.getConMoney().floatValue(), 0.0f,
				0.0f, 0.0f, null);
		map.put("code_url", retMap.get("code_url"));
		map.put("out_trade_no", retMap.get("out_trade_no"));
		try {
			RecordLogUtil.insert("4", "视频会诊", oid + "", pid + "",
					(String) retMap.get("paramxml"),
					(String) retMap.get("prepayxml"), "",
					(String) retMap.get("out_trade_no"));
		} catch (Exception e) {

		}
		return map;
	}

	/**
	 * 生成远程订单
	 * 
	 * @param app
	 */
	private Integer generateremoteorder(RemoteConsultationApp app) {
		// 生成病例
		CaseInfo caseinfo = new CaseInfo();
		caseinfo.setUuid(UUID.randomUUID().toString().replace("-", ""));
		caseinfo.setContactName(app.getPatientName());
		caseinfo.setAge(app.getAge());
		caseinfo.setSex(app.getSex());
		caseinfo.setPresentIll(app.getPatientDesc());
		caseinfo.setAskProblem(app.getAskProblem());
		caseinfo.setNormalImages(app.getNormalImages());
		caseinfo.setCreateTime(new Date());
		Integer caseid = wenzhenService.saveCaseInfo(caseinfo);
		BusinessVedioOrder order = new BusinessVedioOrder();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		order.setUuid(uuid);
		order.setCaseId(caseid);
		order.setCaseUuid(caseinfo.getUuid());
		order.setExpertId(app.getConExpertId());
		order.setCreateTime(new Timestamp(System.currentTimeMillis()));
		// 待定
		if (app.getLocalDoctorId() != null) {
			order.setLocalDoctorId(app.getLocalDoctorId());
			DoctorDetailInfo detail = commonService
					.queryDoctorDetailInfoById(app.getLocalDoctorId());
			order.setLocalDepartId(detail.getDepId());
			order.setLocalHospitalId(detail.getHospitalId());
		}
		order.setConsultationDate(app.getConSureTime());
		order.setSource(6);// 远程申请表
		order.setStatus(OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey());
		order.setPayStatus(4);
		order.setDelFlag(0);
		order.setConsultationDur(20);
		Integer oid = weixinService.saveBusinessVedioOrder(order);
		return oid;
	}

	public List<RemoteConsultationAppRecommend> getRemoteConsultationAppRecommendsByUuid(
			String uuid) {
		return remoteConsultationAppRecommendDao
				.getRemoteConsultationAppRecommendsByUuid(uuid);
	}

	private static String FEN_ZHEN_REN_TEL = "13681473419";// 13517952118,13970538482
	private static String YUN_WEI_FU_ZE_REN_TEL = "13681473419";// 18311267361
																// 刘英红

	/**
	 * 1.提交申请 ，发给分诊人员 xx提交了一个xx医院xx科室的远程会诊申请单，请您及时进行分诊。查看详情：URL;
	 * 
	 * @提交了一个@@的远程会诊申请单，请您及时进行分诊。查看详情：@@@@。
	 * @param app
	 */
	public void sendDistributePersonSms(RemoteConsultationApp app,HttpServletRequest request) throws Exception{
		if (app.getLocalDoctorId() != null) {
			MobileSpecial doc = commonService
					.queryMobileSpecialByUserIdAndUserType(app
							.getLocalDoctorId());
			String content=app.getAppConName() + "提交了一个" + doc.getHosName()
					+ doc.getDepName() + "的远程会诊申请单，请您及时进行分诊。查看详情："
					+ gaindetailurl(app);
			FEN_ZHEN_REN_TEL=ReaderConfigUtil.gainConfigVal(request, "basic.xml",
					"root/fen_zhen_ren_tel");
			HttpSendSmsUtil.sendSmsInteface(FEN_ZHEN_REN_TEL,content);
		}
	}

	/**
	 * 2.进行了分诊， 
	 * a.发给申请人员 
	 * 分诊人xx已对您提交的xx医院xx科室xx患者的会诊单进行了分诊。查看详情：URL
	 * 分诊人@已对您提交的@@患者的会诊单进行了分诊。查看详情：@@@@。
	 * 
	 * b.发给专家运维团队负责人
	 * 
	 * 分诊人xx已对xx提交的 xx医院xx科室的会诊单进行了分诊，请及时确认会诊专家。查看详情：URL
	 * 分诊人@已对@提交的@@会诊单进行了分诊，请及时确认会诊专家。查看详情：@@@@。
	 * @param app
	 */
	public void sendsteptwosms(RemoteConsultationApp app,HttpServletRequest request)throws Exception {
		MobileSpecial doc = commonService
				.queryMobileSpecialByUserIdAndUserType(app.getLocalDoctorId());
		if (StringUtils.isNotBlank(app.getAppConTel())) {
			// 给申请人发短信
			String app_content="分诊人" + app.getTriagePerson() + "已对您提交的" + doc.getHosName() + doc.getDepName()
					+ app.getPatientName()+"患者的会诊单进行了分诊。查看详情：" + gaindetailurl(app);
			HttpSendSmsUtil.sendSmsInteface(app.getAppConTel(),app_content);
		}
		// 专家运维团队负责人
		String yun_content="分诊人" + app.getTriagePerson() + "已对" + app.getAppConName()
				+ "提交的" + doc.getHosName() + doc.getDepName()
				+ "的会诊单进行了分诊，请及时确认会诊专家。查看详情：" + gaindetailurl(app);
		YUN_WEI_FU_ZE_REN_TEL=ReaderConfigUtil.gainConfigVal(request, "basic.xml",
				"root/yun_wei_ze_ren_tel");
		HttpSendSmsUtil.sendSmsInteface(YUN_WEI_FU_ZE_REN_TEL,yun_content);
	}

	/**
	 * a.发给申请人 
	 * 专家运维人员xx已对您提交的xx医院xx科室xx患者的会诊单进行了会诊专家确认，您可以帮患者下单啦。查看性情：URL
	 * 专家运维人员@已对您提交的@@会诊单进行了会诊专家确认。查看详情：@@@@。
	 * b.发给专家维护团队负责人 
	 * 专家运维人员xx已对xx提交的xx医院xx科室的会诊单进行了会诊专家确认。查看详情：URL
	 * 专家运维人员@已对@提交的@@会诊但进行了会诊专家确认。查看详情：@@@@。
	 * @param app
	 */
	public void sendstepthreesms(RemoteConsultationApp app,HttpServletRequest request)throws Exception{
		MobileSpecial doc = commonService
				.queryMobileSpecialByUserIdAndUserType(app.getLocalDoctorId());
		String app_content="专家运维人员" + app.getChargePersonName() + "已对您提交的"
				+ doc.getHosName() + doc.getDepName()+app.getPatientName()
				+ "患者的会诊单进行了会诊专家确认。查看详情：" + gaindetailurl(app);
		// 发给申请人
		HttpSendSmsUtil.sendSmsInteface(app.getAppConTel(),app_content);
		String yun_content="专家运维人员" + app.getChargePersonName() + "已对"
				+ app.getAppConName() + "提交的" + doc.getHosName()
				+ doc.getDepName() + "的会诊单进行了会诊专家确认。查看详情："
				+ gaindetailurl(app);
		YUN_WEI_FU_ZE_REN_TEL=ReaderConfigUtil.gainConfigVal(request, "basic.xml",
				"root/yun_wei_ze_ren_tel");
		HttpSendSmsUtil.sendSmsInteface(YUN_WEI_FU_ZE_REN_TEL,yun_content);
	}

	private String gaindetailurl(RemoteConsultationApp app) {
		return PropertiesUtil.getString("DOMAINURL") + "module/helptool.html#/detail/"
				+ app.getUuid()+"。【佰医汇】";
	}
}
