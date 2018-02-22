package com.tspeiz.modules.home;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tspeiz.modules.common.bean.AdviceBean;
import com.tspeiz.modules.common.bean.D2pOrderBean;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.newrelease.AppraisementDoctorInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageBean;
import com.tspeiz.modules.common.entity.newrelease.DistCode;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.StandardDepartmentInfo;
import com.tspeiz.modules.common.entity.newrelease.UserBillRecord;
import com.tspeiz.modules.common.entity.newrelease.UserContactInfo;
import com.tspeiz.modules.common.entity.release2.UserMedicalRecord;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.manage.D2pManager;
import com.tspeiz.modules.util.LocationUtil;
/**
 * 微信患者端后台控制器
 * @author heyongb
 *
 */
@Controller
@RequestMapping("d2p")
public class D2pController {
	@Autowired
	private D2pManager d2pManager;
	private IWenzhenService iWenzhenService;
	
	/**
	 * 获取家庭成员
	 * 参数：openid
	 * 访问：http://localhost:8080/d2p/gainFamilies
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainFamilies")
	@ResponseBody
	public Map<String,Object> gainFamilies(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String openid=request.getParameter("openid");
		map.putAll(d2pManager.gainFamilies(openid));
		return map;
	}
	
	/**
	 * 新增或编辑家庭成员
	 * 访问：http://localhost:8080/d2p/saveOrUpdateFamiler
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveOrUpdateFamiler")
	@ResponseBody
	public Map<String,Object> saveOrUpdateFamiler(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		d2pManager.saveOrUpdateFamiler(request);
		return map;
	}
	/**
	 * 删除家庭成员
	 * 参数：id：家庭成员id
	 * 访问：http://localhost:8080/d2p/delfamiler
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/delfamiler")
	@ResponseBody
	public Map<String,Object> delfamiler(HttpServletRequest request){
		Integer id=Integer.parseInt(request.getParameter("id"));
		d2pManager.delfamiler(id);
		return null;
	}
	/**
	 * 获取家庭成员信息
	 * 访问：http://localhost:8080/d2p/gainfamilerinfo
	 * 参数：家庭成员id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainfamilerinfo")
	@ResponseBody
	public Map<String,Object> gainfamilerinfo(HttpServletRequest request){
		Integer familyid=Integer.parseInt(request.getParameter("familyid"));//家庭成员id
		Map<String,Object> map=new HashMap<String,Object>();
		UserContactInfo user=d2pManager.gainfamilerinfo(familyid);
		map.put("user", user);
		return map;
	}
	/**
	 * 获取家庭成员就诊记录
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainfamrecords")
	@ResponseBody
	public Map<String,Object> gainfamrecords(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer familyid=Integer.parseInt(request.getParameter("familyid"));
		List<UserMedicalRecord> records=d2pManager.gainfamrecords(familyid);
		map.put("records", records);
		return map;
	}
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/adnewrecord")
	@ResponseBody
	public Map<String,Object> adnewrecord(HttpServletRequest request) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		d2pManager.adnewrecord(request);
		return map;
	}
	
	
	
	/**
	 * 快速问诊提交
	 * 访问：http://localhost:8080/d2p/submitfast
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/submitfast")
	@ResponseBody
	public Map<String,Object> submitfast(HttpServletRequest request){
		return d2pManager.submitfast(request);
	}
	
	/**
	 * 取消 快速问诊
	 * 访问：http://localhost:8080/d2p/cancelfast
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cancelfast")
	@ResponseBody
	public Map<String,Object> cancelfast(HttpServletRequest request){
		Integer oid=Integer.parseInt(request.getParameter("oid"));
		d2pManager.cancelfast(oid);
		return null;
	}
	
	/**
	 * 确认医生，向他提问，返回支付参数
	 * 访问：http://localhost:8080/d2p/confirmdoc
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/confirmdoc")
	@ResponseBody
	public Map<String,Object> confirmdoc(HttpServletRequest request,HttpServletResponse response){
		Integer oid=Integer.parseInt(request.getParameter("oid"));//订单id
		Integer docid=Integer.parseInt(request.getParameter("docid"));
		String openid=request.getParameter("openid");
		return d2pManager.confirmdoc(request,response,oid,docid,openid);
	}
	
	/**
	 * 判断选择的家庭成员是否可以进行患者报道
	 * 访问：http://localhost:8080/d2p/tellreport 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/tellreport")
	@ResponseBody
	public Map<String,Object> tellreport(HttpServletRequest request){
		Integer familyid=Integer.parseInt(request.getParameter("familyid"));//选择的家庭成员id
		Integer docid=Integer.parseInt(request.getParameter("docid"));//报道的医生
		return d2pManager.tellreport(familyid,docid);
	}
	
	/**
	 * 提交患者报道
	 * 访问：http://localhost:8080/d2p/patientreport
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/patientreport")
	@ResponseBody
	public Map<String,Object> patientreport(HttpServletRequest request){
		d2pManager.patientreport(request);
		return null;
	}
	/**
	 * 我的医生
	 * 访问：http://localhost:8080/d2p/mydoctors
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/mydoctors")
	@ResponseBody 
	public Map<String,Object> mydoctors(HttpServletRequest request){
		String openid=request.getParameter("openid");
		return d2pManager.mydoctors(openid);
	}
	/**
	 * 获取系统暖意
	 * 访问：http://localhost:8080/d2p/gainsyswarms
	 * @return
	 */
	@RequestMapping(value="/gainsyswarms")
	@ResponseBody
	public Map<String,Object> gainsyswarms(){
		return d2pManager.gainsyswarms();
	}	
	
	/**
	 * 送心意 预订单有关需要传参：orderType,orderUuid
	 * 返回：支付所需参数
	 * 访问：http://localhost:8080/d2p/subuserwarm
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/subuserwarm")
	@ResponseBody
	public Map<String,Object> subuserwarm(HttpServletRequest request,HttpServletResponse response){
		return d2pManager.subuserwarm(request,response);
	}
	
	/**
	 * 我的订单
	 * 访问：http://localhost:8080/d2p/myorders?openid=oqDWctwJxcVAlOFFIFSyOtUE8Q6Q
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/myorders")
	@ResponseBody
	public Map<String,Object> myorders(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String openid=request.getParameter("openid");
		String pageNo=request.getParameter("pageNo");
		String pageSize=request.getParameter("pageSize");
		String ltype=request.getParameter("ltype");//1：待付款，2：待就诊，3：待评价，4：历史订单
		List<D2pOrderBean> orders=d2pManager.myorders(openid,ltype,pageNo,pageSize);
		map.put("orders", orders);
		return map;
	}
	
	/**
	 * 医生主页信息
	 * 参数：docid
	 * 访问：http://localhost:8080/d2p/docmain
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/docmain")
	@ResponseBody
	public Map<String,Object> docmain(HttpServletRequest request){
		Integer docid=Integer.parseInt(request.getParameter("docid"));
		return d2pManager.docmain(docid);
	}
	/**
	 * 获取医生评价
	 * 访问：http://localhost:8080/d2p/gainAppraises
	 */
	@RequestMapping(value="/gainAppraises")
	@ResponseBody
	public Map<String,Object> gainAppraises(HttpServletRequest request){
		Integer docid=Integer.parseInt(request.getParameter("docid"));
		String pageNo=request.getParameter("pageNo");
		String pageSize=request.getParameter("pageSize");
		return d2pManager.gainAppraises(docid,pageNo,pageSize);
	}
	/**
	 * 患者报道中的单聊参数获取
	 * 访问：http://localhost:8080/d2p/gainImTokenD2pSingle
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainImTokenD2pSingle")
	@ResponseBody
	public Map<String,Object> gainImTokenD2pSingle(HttpServletRequest request){
		Integer oid=Integer.parseInt(request.getParameter("oid"));
		return d2pManager.gainImTokenD2pSingle(oid);
	}
	/**
	 * 订单聊天中的群聊参数获取
	 * 访问：http://localhost:8080/d2p/gainImTokenD2pGroup
	 * @param request
	 * @return
	 */
	@RequestMapping(value="gainImTokenD2pGroup")
	@ResponseBody
	public Map<String,Object> gainImTokenD2pGrop(HttpServletRequest request){
		Integer oid=Integer.parseInt(request.getParameter("oid"));
		Integer otype=Integer.parseInt(request.getParameter("otype"));
		return d2pManager.gainImTokenD2pGroup(oid,otype);
	}
	
	/**
	 * 获取家庭成员病例信息
	 * 访问：http://localhost:8080/d2p/gainfamilycaseinfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainfamilycaseinfo")
	@ResponseBody
	public Map<String,Object> gainfamilycaseinfo(HttpServletRequest request){
		Integer oid=Integer.parseInt(request.getParameter("oid"));
		Integer otype=Integer.parseInt(request.getParameter("otype"));
		return d2pManager.gainfamilycaseinfo(oid,otype);  
	}
	/**
	 * 获取订单中的聊天记录
	 * http://localhost:8080/d2p/gainchatdatas
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainchatdatas")
	@ResponseBody
	public Map<String,Object> gainchatdatas(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Integer oid=Integer.parseInt(request.getParameter("oid"));
		Integer otype=Integer.parseInt(request.getParameter("otype"));
		String pageNo=request.getParameter("pageNo");
		String pageSize=request.getParameter("pageSize");
		List<BusinessMessageBean> messages=d2pManager.gainchatdatas(oid,otype,pageNo,pageSize);
		map.put("messages", messages);
		return map;
	}
	
	/**
	 * 保存评价
	 * 访问：http://localhost:8080/d2p/saveestimate
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveestimate", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveevalua(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer oid = Integer.parseInt(request.getParameter("oid"));
		Integer otype = Integer.parseInt(request.getParameter("otype"));
		String content = request.getParameter("content");// 评价内容
		Integer grade = Integer.parseInt(request.getParameter("grade"));// 星级
		d2pManager.saveevalua(oid,otype,content,grade);
		return map;
	}
	
	
	/**
	 * 获取医生数据
	 * 访问：http://localhost:8080/d2p/gaindoctors
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gaindoctors")
	@ResponseBody
	public Map<String,Object> gaindoctors(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String pageNo=request.getParameter("pageNo");
		String pageSize=request.getParameter("pageSize");
		String search=request.getParameter("search");//检索
		String serviceid=request.getParameter("serviceId");//服务 6图文，7电话
		String depid=request.getParameter("depid");//标准科室
		String distcode=request.getParameter("distCode");//区域
		List<MobileSpecial> doctors=d2pManager.gaindoctors(search,serviceid,depid,distcode,pageNo,pageSize);
		map.put("docs", doctors);
		return map;
	}
	
	/**
	 * 根据经纬度获取地理位置信息
	 * 访问：http://localhost:8080/d2p/gainlocationinfo
	 * 返回："province"：省
			"city"：市
			"district"：区
			"distcode"：distcode
	 * @param request 
	 * 
	 * @return
	 */
	@RequestMapping(value="/gainlocationinfo")
	@ResponseBody
	public Map<String,Object> gainlocationinfo(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String latitude = request.getParameter("latitude");
		String longitude = request.getParameter("longitude");
		map.putAll(LocationUtil.gainDataFromLngAndlat(Double.parseDouble(latitude),
				Double.parseDouble(longitude)));
		return map;
	}
	/**
	 * 最近咨询
	 * 访问：http://localhost:8080/d2p/newlyadvices
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/newlyadvices")
	@ResponseBody
	public Map<String,Object> newlyadvices(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String openid=request.getParameter("openid");
		List<AdviceBean> beans=d2pManager.newlyadvices(openid);
		map.put("beans", beans);
		return map;
	}
	
	/**
	 * 继续支付 如果选择了医生的话
	 * 访问：http://localhost:8080/d2p/continuepay
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/continuepay")
	@ResponseBody
	public Map<String,Object> continuepay(HttpServletRequest request,HttpServletResponse response){
		Integer oid=Integer.parseInt(request.getParameter("oid"));
		Integer otype=Integer.parseInt(request.getParameter("otype"));
		String openid=request.getParameter("openid");
		return d2pManager.continuepay(openid,oid,otype,request,response);
	}
	
	/**
	 * 提交电话问诊 返回支付参数
	 * 访问：http://localhost:8080/d2p/subtelorder
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/subtelorder")
	@ResponseBody
	public Map<String,Object> subtelorder(HttpServletRequest request,HttpServletResponse response){
		return d2pManager.subtelorder(request,response);
	}
	
	/**
	 * 提交图文问诊 返回支付参数
	 * 访问：http://localhost:8080/d2p/subtuwenorder
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/subtuwenorder")
	@ResponseBody
	public Map<String,Object> subtuwenorder(HttpServletRequest request,HttpServletResponse response){
		return d2pManager.subtuwenorder(request,response);
	}
	
	/**
	 * 获取标准科室 （热门科室，全部标准科室，分页/不分页）
	 * 访问：http://localhost:8080/d2p/gainstanddeps
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainstanddeps")
	@ResponseBody
	public Map<String,Object> gainstanddeps(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String ispage=request.getParameter("ispage");//是否分页   1:分页获取，0：不分页，全部获取
		String ishot=request.getParameter("ishot");//是否热门  1：热门科室，0：不是，正常获取
		String pageNo=request.getParameter("pageNo");
		String pageSize=request.getParameter("pageSize");
		List<StandardDepartmentInfo> deps=d2pManager.gainstanddeps(ispage,ishot,pageNo,pageSize);
		map.put("deps", deps);
		return map;
	}
	
	/**
	 * 获取附近医院 是否分页，省市区级别医院 
	 * 访问：http://localhost:8080/d2p/gainnearhoses
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainnearhoses")
	@ResponseBody
	public Map<String,Object> gainnearhoses(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String ispage=request.getParameter("ispage");
		String htype=request.getParameter("htype");//类型：1:省级，2：市级，3：区县
		String distcode=request.getParameter("distcode");
		String pageNo=request.getParameter("pageNo");
		String pageSize=request.getParameter("pageSize");
		List<HospitalDetailInfo> hospitals=d2pManager.gainnearhoses(ispage,htype,distcode,pageNo,pageSize);
		map.put("hospitals", hospitals);
		return map;
	}
	
	/**
	 * 心意墙
	 * 访问：http://localhost:8080/d2p/warmwall
	 * @param request
	 * @return  
	 */
	@RequestMapping(value="/warmwall")
	@ResponseBody
	public Map<String,Object> warmwall(HttpServletRequest request){
		Integer docid=Integer.parseInt(request.getParameter("docid"));
		String pageNo=request.getParameter("pageNo");
		String pageSize=request.getParameter("pageSize");
		return d2pManager.warmwall(docid,pageNo,pageSize);
	}
	/**
	 * 设置usercontactinfo 的uuid
	 */
	@RequestMapping(value="/updatecontact")
	public void updatecontact(){
		d2pManager.updatecontact();
	}
	
	/**
	 * 获取区域数据  省市区
	 * 访问：http://localhost:8080/d2p/gainArea
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainArea")
	@ResponseBody
	public Map<String,Object> gainArea(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String type=request.getParameter("type");//0全部，1：省 ，2：市，3：区
		String parentCode=request.getParameter("parentCode");//上级code
		List<DistCode> codes=d2pManager.gainArea(type,parentCode);
		map.put("codes", codes);
		return map;
	}
	
	/**
	 * 保存消息（文本,图片消息）
	 * 访问：http://localhost:8080/d2p/saveMessage
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveMessage")
	@ResponseBody
	public Map<String,Object> saveTxtMessage(HttpServletRequest request){
		Integer otype=Integer.parseInt(request.getParameter("otype"));
		Integer oid=Integer.parseInt(request.getParameter("oid"));
		String msgType=request.getParameter("msgType");//text,image,voice
		String msgContent=request.getParameter("msgContent");//文本消息
		String fileUrl=request.getParameter("fileUrl");//图片消息
		d2pManager.saveMessage(otype,oid,msgContent,fileUrl,msgType);
		return null;
	}
	
	/**
	 * 获取患者交易记录
	 * 访问：http://localhost:8080/d2p/gainuserbills
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gainuserbills")
	@ResponseBody
	public Map<String,Object> gainuserbills(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String openid=request.getParameter("openid");
		String ispage=request.getParameter("ispage");//是否分页
		String pageNo=request.getParameter("pageNo");
		String pageSize=request.getParameter("pageSize");
		List<UserBillRecord> records=d2pManager.gainuserbills(openid,ispage,pageNo,pageSize);
		map.put("records", records);
		return map;
	}
}
