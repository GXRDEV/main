package com.tspeiz.modules.home;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.RemoteConsultationApp;
import com.tspeiz.modules.common.entity.RemoteConsultationAppRecommend;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;
import com.tspeiz.modules.common.entity.weixin.WeiAccessToken;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.common.thread.TokenThread;
import com.tspeiz.modules.manage.RemoteConAppManager;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.weixin.SignUtil;
/**
 * 远程申请控制器
 * @author heyongb
 *
 */
@Controller
@RequestMapping("rcapp")
public class RemoteConAppController {
	@Autowired
	private RemoteConAppManager remoteConAppManager;
	@Autowired
	private IWenzhenService wenzhenService;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IWeixinService weixinService;

	public void test(){
		System.out.println("==");
	}
	/**
	 * 获取分享参数
	 * 访问：http://localhost:8080/rcapp/gainshareparams
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/gainshareparams")
	public @ResponseBody Map<String,Object> gainshareparams(HttpServletRequest request) throws Exception{	
		Map<String, Object> map = signMap(request.getParameter("shareurl"),
				request.getQueryString());
		return map;
	}
	/**
	 * 加载科室的医生
	 * 访问：http://localhost:8080/rcapp/loadcoodocs
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/loadcoodocs")
	public @ResponseBody Map<String,Object> loadcoodocs(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer depid=Integer.parseInt(request.getParameter("depid"));
		List<MobileSpecial> docs=commonService.queryCooDocsByDep(depid);
		map.put("docs", docs);
		return map;
	}
	
	/**
	 * 保存基本申请信息
	 * 访问：http://localhost:8080/rcapp/basicapp
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/basicapp")
	public @ResponseBody Map<String,Object>  basicapp(HttpServletRequest request,@ModelAttribute RemoteConsultationApp app) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		String uuid=remoteConAppManager.basicAppSave(request,app);
		remoteConAppManager.sendDistributePersonSms(app,request);
		map.put("uuid", uuid);
		return map;
	}
	
	
	/**
	 * 进入申请单详情，适用所有状态下
	 * 访问：http://localhost:8080/rcapp/appdetail
	 * @param request;
	 * @param uuid
	 * @return
	 */
	@RequestMapping(value="/appdetail")
	public @ResponseBody Map<String,Object> appdetail(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String uuid=request.getParameter("uuid");
		String model=request.getParameter("model");//默认为默认模式，"expert"：为专家模式
		map.put("model", model);
		RemoteConsultationApp appinfo=remoteConAppManager.getRemoteConsultationAppByUuid(uuid);
		appinfo.setId(null);
		map.put("appinfo", appinfo);
		if(appinfo.getLocalDoctorId()!=null){
			MobileSpecial localdoc=commonService.queryMobileSpecialByUserIdAndUserType(appinfo.getLocalDoctorId());
			map.put("localdoc", localdoc);
		}
		List<RemoteConsultationAppRecommend> recs=remoteConAppManager.getRemoteConsultationAppRecommendsByUuid(uuid);
		map.put("recs",recs);//推荐专家
		//病例图片
		if(StringUtils.isNotBlank(appinfo.getNormalImages())){
			List<CustomFileStorage> caseimages = wenzhenService
					.queryCustomFilesByCaseIds(appinfo.getNormalImages());
			map.put("caseimages", caseimages);
		}
		if(appinfo.getStatus()!=null&&appinfo.getStatus().equals(4)){
			//已生成订单 需要判断是否支付，如果没有支付，继续支付
			BusinessVedioOrder order=wenzhenService.queryBusinessVedioOrderById(appinfo.getOrderId());
			map.put("payStatus", order.getPayStatus());	
			map.put("oid", order.getId());
		}
		return map;
	}
	
	/**
	 * 进入分诊,填写分诊信息，进入确认分诊，根据status判断
	 * 访问：http://localhost:8080/rcapp/triageinfo
	 * 参数：uuid
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestMapping(value="/triageinfo")
	public @ResponseBody Map<String,Object> triageinfo(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String uuid=request.getParameter("uuid");
		RemoteConsultationApp appinfo=remoteConAppManager.getRemoteConsultationAppByUuid(uuid);
		appinfo.setId(null);
		map.put("uuid", uuid);
		map.put("appinfo", appinfo);
		List<RemoteConsultationAppRecommend> recs=remoteConAppManager.getRemoteConsultationAppRecommendsByUuid(uuid);
		map.put("recs",recs);//推荐专家
		return map;
	}
	
	/**
	 * 填写信息提交 ,分诊信息，确认信息
	 * 访问：http://localhost:8080/rcapp/completetriage
	 * 参数：uuid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/completetriage")
	public @ResponseBody Map<String,Object> completetriage(HttpServletRequest request)throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		String uuid=request.getParameter("uuid");
		RemoteConsultationApp app=remoteConAppManager.getRemoteConsultationAppByUuid(uuid);
		if(app.getStatus().equals(1)){
			//分诊完成
			app=remoteConAppManager.compeletetriage(uuid,request);
			remoteConAppManager.sendsteptwosms(app,request);
		}else if(app.getStatus().equals(2)){
			//确认专家后
			app=remoteConAppManager.completeconfirm(uuid,request);
			remoteConAppManager.sendstepthreesms(app,request);
		}
		map.put("uuid", uuid);
		return map;
	}
	
	/**
	 * 根据申请单生成远程订单并返回支付信息
	 * 访问：http://localhost:8080/rcapp/generateorder
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/generateorder")
	@ResponseBody
	public Map<String,Object> generateorder(HttpServletRequest request,HttpServletResponse response){
		return remoteConAppManager.generate_order(request,response);
	}
	
	/**
	 * 获取支付二维码
	 * 访问：http://localhost:8080/rcapp/gainerweima
	 * 参数：oid 订单id
	 * @param request
	 * @param response
	 * @return 微信支付二维码  code_url
	 * @throws Exception
	 */
	@RequestMapping(value="/gainerweima")
	@ResponseBody
	public Map<String,Object> gainerweima(HttpServletRequest request,HttpServletResponse response){
		return remoteConAppManager.gainerweima(request,response);
	}
	
	@RequestMapping(value = "/listenpaystatus", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> listenpaystatus(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		BusinessVedioOrder order=wenzhenService.queryBusinessVedioOrderById(oid);
		if (order.getPayStatus() != null && order.getPayStatus().equals(1)) {
			//支付成功
			map.put("status", "success");
		} else {
			map.put("status", "error");
		}
		return map;
	}

	
	private Logger log=Logger.getLogger(RemoteConAppController.class);
	private Map<String, Object> signMap(String shareurl, String queryString)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("===shareurl=="+shareurl);
		long timestamp = System.currentTimeMillis() / 1000;
		String nonceStr = UUID.randomUUID().toString();
		//获取 分享所需的 ticket
		WeiAccessToken wat=weixinService.queryWeiAccessTokenById(PropertiesUtil.getString("APPID"));
		String signature = SignUtil.getSignature(wat.getJsapiTicket(),
				nonceStr, timestamp, shareurl);
		map.put("appId", PropertiesUtil.getString("APPID"));
		map.put("timestamp", timestamp);
		map.put("nonceStr", nonceStr);
		map.put("signature", signature);
		return map;
	}
}
