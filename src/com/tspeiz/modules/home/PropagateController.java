package com.tspeiz.modules.home;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tspeiz.modules.common.bean.weixin.DepartHelper;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.BigDepartment;
import com.tspeiz.modules.common.entity.Departments;
import com.tspeiz.modules.common.entity.SystemServiceInfo;
import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;
import com.tspeiz.modules.common.entity.newrelease.DoctorServiceInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.StandardDepartmentInfo;
import com.tspeiz.modules.common.entity.weixin.WeiAccessToken;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.common.thread.TokenThread;
import com.tspeiz.modules.manage.CommonManager;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.weixin.CommonUtil;
import com.tspeiz.modules.util.weixin.SignUtil;

@Controller
@RequestMapping("propagate")
public class PropagateController {
	@Resource
	private ICommonService commonService;
	@Resource
	private IWeixinService weixinService;
	@Resource
	private CommonManager commonManager;

	/**
	 * 进入专家列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/intodoclist", method = RequestMethod.GET)
	public ModelAndView intodoclist(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		openid = gainOpenIdByConditions(request, openid);
		map.put("openid", openid);
		Map<String, Object> ret = signMap(request.getRequestURL(),
				request.getQueryString());
		map.put("appid", ret.get("appid"));
		map.put("timestamp", ret.get("timestamp"));
		map.put("nonceStr", ret.get("nonceStr"));
		map.put("signature", ret.get("signature"));
		map.put("openid", openid);
		return new ModelAndView("zjh/propagate/doctor_list", map);
	}

	// 获取专家列表数据
	@RequestMapping(value="/doclistdata",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> doclistdata(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Map<String,Object> querymap=new HashMap<String,Object>();
		String pageNo=request.getParameter("pageNo");
		String hosid=request.getParameter("hosid");
		String bigdepid=request.getParameter("bigdepid");
		String depid=request.getParameter("depid");
		String ltype=request.getParameter("ltype");
		String searchContent=request.getParameter("searchContent");
		Integer _pageNo=1;
		if(StringUtils.isNotBlank(pageNo)){
			_pageNo=Integer.parseInt(pageNo);
		}
		if(StringUtils.isNotBlank(hosid)){
			querymap.put("hosid", hosid);
		}
		if(StringUtils.isNotBlank(depid)){
			querymap.put("depid", depid);
		}
		if(StringUtils.isNotBlank(ltype)){
			querymap.put("ltype", ltype);
		}
		if(StringUtils.isNotBlank(searchContent)){
			querymap.put("searchContent", searchContent);
		}
		List<MobileSpecial> specials=weixinService.queryMobileSpecialsByConditionsPro(querymap,_pageNo, 10);
		for (MobileSpecial special : specials) {
			DoctorServiceInfo tw=commonService.queryDoctorServiceInfoByCon(special.getSpecialId(), 1, 1);
			DoctorServiceInfo tel=commonService.queryDoctorServiceInfoByCon(special.getSpecialId(), 2, 2);
			DoctorServiceInfo rm=commonService.queryDoctorServiceInfoByCon(special.getSpecialId(), 4, 3);
			if(tw!=null&&tw.getIsOpen().equals(1)){
				special.setOpenAsk(1);
				special.setAskAmount(tw.getAmount());
			}else{
				special.setOpenAsk(0);
			}
			if(tel!=null&&tel.getIsOpen().equals(1)){
				special.setOpenTel(1);
				special.setTelAmount(tel.getAmount());
			}else{
				special.setOpenTel(0);
			}
			if(rm!=null&&rm.getIsOpen().equals(1)){
				special.setOpenVedio(1);
				special.setVedioAmount(rm.getAmount());
			}else{
				special.setOpenVedio(0);
			}
		}
		map.put("specials", specials);
		map.put("hosid", hosid);
		map.put("depid",depid);
		map.put("bigdepid",bigdepid);
		return map;
	}
	
	//根据地区获取医院
	@RequestMapping(value="/gainHospitalsByArea",method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> gainHospitalsByArea(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String distcode=request.getParameter("distcode");
		String type=request.getParameter("type");//2 医生所属 1专家所属，默认专家
		Integer seltype=1;
		if(StringUtils.isNotBlank(type))seltype=Integer.parseInt(type);
		List<HospitalDetailInfo> hospitals=weixinService.queryHospitalDetailsByDsitcode(distcode,seltype);
		map.put("hospitals", hospitals);
		return map;
	}
	
	/**
	 * 大科室-标准科室
	 * 访问：http://localhost:8080/propagate/gainBigDepartments
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gainBigDepartments", produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	String gainBigDepartments(HttpServletRequest request) {
		List<BigDepartment> bigDeps = weixinService.queryBigDepartments();
		List<DepartHelper> helps = new ArrayList<DepartHelper>();
		for (BigDepartment _big : bigDeps) {
			List<StandardDepartmentInfo> departs = weixinService
					.queryStandardDepartmentByBigDep(_big.getId());
			DepartHelper helper = new DepartHelper();
			helper.setName(_big.getDisplayName());
			helper.setStands(departs);
			helps.add(helper);
		}
		return JSONArray.fromObject(helps).toString();
	}

	//专家详情
	@RequestMapping(value = "/doctordetail/{did}", method = RequestMethod.GET)
	public ModelAndView doctordetail(@PathVariable Integer did,
			HttpServletRequest request) throws Exception {
		String openid = request.getParameter("openid");
		openid = gainOpenIdByConditions(request, openid);
		Map<String, Object> map = new HashMap<String, Object>();
		MobileSpecial special = commonService
				.queryMobileSpecialByUserIdAndUserType(did);
		String relatedPics = special.getRelatedPics();
		List<CustomFileStorage> images = null;
		if (StringUtils.isNotBlank(relatedPics)) {
			images = weixinService.queryCustomFileStorageImages(relatedPics);
		}
		DoctorServiceInfo tw=commonService.queryDoctorServiceInfoByCon(did, 1, 1);
		DoctorServiceInfo tel=commonService.queryDoctorServiceInfoByCon(did, 2, 2);
		DoctorServiceInfo rm=commonService.queryDoctorServiceInfoByCon(did, 4, 3);
		if(tw!=null&&tw.getIsOpen().equals(1)){
			special.setOpenAsk(1);
			special.setAskAmount(tw.getAmount());
		}else{
			special.setOpenAsk(0);
		}
		if(tel!=null&&tel.getIsOpen().equals(1)){
			special.setOpenTel(1);
			special.setTelAmount(tel.getAmount());
		}else{
			special.setOpenTel(0);
		}
		if(rm!=null&&rm.getIsOpen().equals(1)){
			SystemServiceInfo ssi = commonService.querySystemServiceById(rm.getServiceId());
			special.setOpenVedio(1);
			special.setVedioAmount(commonManager.processD2DVedioMoney(rm.getAmount(), BigDecimal.valueOf(Double.valueOf(ssi.getPriceParameter()))));
		}else{
			special.setOpenVedio(0);
		}
		map.put("images", images);
		map.put("special", special);
		Map<String, Object> ret = signMap(request.getRequestURL(),
				request.getQueryString());
		map.put("appid", ret.get("appid"));
		map.put("timestamp", ret.get("timestamp"));
		map.put("nonceStr", ret.get("nonceStr"));
		map.put("signature", ret.get("signature"));
		map.put("openid", openid);
		return new ModelAndView("zjh/propagate/doctor_detail", map);
	}

	private Map<String, Object> signMap(StringBuffer homeUrl, String queryString)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(queryString)) {
			homeUrl.append("?").append(queryString);
		}
		String url = homeUrl.toString();
		url = url.replace("http", "https");
		System.out.println(url);
		long timestamp = System.currentTimeMillis() / 1000;
		String nonceStr = UUID.randomUUID().toString();
		WeiAccessToken wat=weixinService.queryWeiAccessTokenById(PropertiesUtil.getString("APPID"));
		String signature = SignUtil.getSignature(wat.getJsapiTicket(),
				nonceStr, timestamp, url);
		map.put("appid", PropertiesUtil.getString("APPID"));
		map.put("timestamp", timestamp);
		map.put("nonceStr", nonceStr);
		map.put("signature", signature);
		return map;
	}
	
	private String gainOpenIdByConditions(HttpServletRequest request,
			String openid) {
		if (!StringUtils.isNotBlank(openid)) {
			String code = request.getParameter("code");
			if (StringUtils.isNotBlank(code)) {
				// 根据code获取openid
				JSONObject jsonObject = gainJsonObject(code);
				if (jsonObject != null) {
					if (jsonObject.containsKey("openid")) {
						openid = jsonObject.getString("openid");
						request.getSession().setAttribute("cuopenid", openid);
					} else {
						openid = (String) request.getSession().getAttribute(
								"cuopenid");
					}
				}
			}
		}
		return openid;
	}

	private JSONObject gainJsonObject(String code) {
		String appid = PropertiesUtil.getString("APPID");
		String appsecret = PropertiesUtil.getString("APPSECRET");
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ appid + "&secret=" + appsecret + "&code=" + code
				+ "&grant_type=authorization_code";
		JSONObject jsonObject = CommonUtil.httpRequest(url, "GET", null);
		return jsonObject;
	}

}
