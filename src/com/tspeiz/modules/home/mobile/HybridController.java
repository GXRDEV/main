package com.tspeiz.modules.home.mobile;

import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.Jedis;

import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.newrelease.AppPcLogin;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2dReferralOrder;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.ID2pService;
import com.tspeiz.modules.manage.ErweiManager;
import com.tspeiz.modules.util.qr.QRCodeUtil;
import com.tspeiz.modules.util.redis.RedisUtil;


@Controller
@RequestMapping(value = "/hybrid")
public class HybridController {
	@Autowired
	private ICommonService commonService;
	@Autowired
	private ErweiManager erweiManager;
	@Autowired
	private ID2pService d2pService;
	/**
	 * 进入扫码登陆界面---生成全局keyid用于绑定app端用户
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saoysao")
	public ModelAndView saoysao(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String keyid=erweiManager.generateErInfo();
		map.put("keyid", keyid);
		return new ModelAndView("mobile/saoyisao",map);
	}
	
	/**
	 * 显示二维码
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/showQR")
	public void getIcon(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String keyid=request.getParameter("keyid");
		JSONObject obj=new JSONObject();
		obj.put("keyid", keyid);
		BufferedImage image = QRCodeUtil.generateQRCode(obj.toString(), 184, 184);
		ImageIO.write(image, "JPG", response.getOutputStream());
	}
	/**
	 * 监听 app端是否扫码
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/listenlogin")
	@ResponseBody
	public Map<String,Object> listenlogin(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String keyid=request.getParameter("keyid");
		AppPcLogin login=commonService.queryAppPcLoginByKeyId(keyid);
		if(login.getUserId()!=null){
			//扫码登陆
			MobileSpecial userDetail = commonService
					.queryMobileSpecialByUserIdAndUserType(login.getUserId());
			DoctorRegisterInfo user=commonService.queryDoctorRegisterInfoById(login.getUserId());
			request.getSession().setAttribute("user", user);
			request.getSession().setAttribute("userDetail", userDetail);
			map.put("status", "success");
			map.put("login", login);
			map.put("stype", user.getUserType() + "");
		}else{
			//未扫描登陆
			map.put("status", "error");
		}
		return map;
	}
	/**
	 * app端扫描成功后，推送消息到前端，前端调用，执行跳转
	 * http://localhost:8080/hybrid/sucjump
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sucjump")
	@ResponseBody
	public Map<String,Object> suc(HttpServletRequest request,HttpSession session){
		Map<String,Object> map=new HashMap<String,Object>();
		String keyid=request.getParameter("keyid");
		AppPcLogin login=commonService.queryAppPcLoginByKeyId(keyid);
		request.getSession().setAttribute("user", null);
		request.getSession().setAttribute("userDetail", null);
		String redirectUrl="index.html";
		if(login.getUserId()!=null){
			//扫码登陆
			MobileSpecial userDetail = commonService
					.queryMobileSpecialByUserIdAndUserType(login.getUserId());
			if(userDetail!=null){
				DoctorRegisterInfo user=commonService.queryDoctorRegisterInfoById(login.getUserId());
				request.getSession().setAttribute("user", user);
				request.getSession().setAttribute("userDetail", userDetail);
				if(login.getScanType().equals(1)){
					if(login.getOrderType().equals(4)){
						//远程门诊
						String ouid=login.getOrderUuid();
						BusinessVedioOrder order=commonService.queryBusinessVedioOrderByUid(ouid);
						if(login.getUserType().equals(3)){
							//医生端
							redirectUrl="index.html#ajax/doc/detail.html?oid="+order.getId();
							
						}else if(login.getUserType().equals(2)){
							//专家端
							redirectUrl="index.html#ajax/exp/detail.html?oid="+order.getId();
						}
						
					}else if(login.getOrderType().equals(5)){
						//专家咨询
						SpecialAdviceOrder order=commonService.querySpecialAdviceOrderByUid(login.getOrderUuid());
						if(login.getUserType().equals(3)){
							//医生端
							redirectUrl="index.html#ajax/doc/advicedetail.html?oid="+order.getId();
						}else if(login.getUserType().equals(2)){
							//专家端
							redirectUrl="index.html#ajax/exp/advicedetail.html?oid="+order.getId();
						}
					}else if(login.getOrderType().equals(10)){
						if(login.getUserType().equals(3)){
							BusinessD2dReferralOrder order=d2pService.queryBusinessD2pReferralOrderByUuid(login.getOrderUuid());
							redirectUrl="index.html#ajax/doc/advicedetail.html?oid="+order.getId()+"&otype=10&docask=referral";
						}else{
							redirectUrl="";
						}
					}
				}else if(login.getScanType().equals(2)||login.getScanType().equals(3)){
					redirectUrl="index.html";
				}else if(login.getScanType().equals(4) && login.getOrderType().equals(0)) {
					redirectUrl = "main.html#ajax/caseInfo.html?caseuuid="+login.getOrderUuid();
				}
				map.put("stype", user.getUserType() + "");
				map.put("uid", session.getId());
				map.put("id", user.getId());
				map.put("uname",userDetail.getSpecialName());
				map.put("headimage", userDetail.getListSpecialPicture());
				map.put("status", "success");
			}else{
				map.put("status", "error");
			}
		}
		map.put("redirectUrl", redirectUrl);
		return map;
	}
}
