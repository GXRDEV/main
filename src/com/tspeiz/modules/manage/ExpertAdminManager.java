package com.tspeiz.modules.manage;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.dto.D2DTuwenDto;
import com.tspeiz.modules.common.bean.dto.VedioOrderDto;
import com.tspeiz.modules.common.dao.newrelease.IDoctorDetailInfoDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageBean;
import com.tspeiz.modules.common.entity.newrelease.DoctorDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.util.IdcardUtils;
import com.tspeiz.modules.util.date.RelativeDateFormat;

@Service
public class ExpertAdminManager {
	@Autowired
	private IDoctorDetailInfoDao doctorDetailInfoDao;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IWenzhenService wenzhenService;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public void editExpertInfo(HttpServletRequest request) throws Exception {
		Map<String, Object> datas = gainDatasByFront(request);
		DoctorDetailInfo detail = null;
		Class clz = DoctorDetailInfo.class;
		if (datas.containsKey("id") && datas.get("id") != null) {
			// 更新
			detail = doctorDetailInfoDao.find(Integer.parseInt(datas.get("id")
					.toString()));
			for (String propertyName : datas.keySet()) {
				PropertyDescriptor pd = new PropertyDescriptor(propertyName,
						clz);
				Method method = pd.getWriteMethod();
				method.invoke(detail, datas.get(propertyName));
			}
			doctorDetailInfoDao.update(detail);
		}
	}

	/**
	 * 编辑个人信息 参数
	 * @param request
	 * @return
	 */
	private Map<String, Object> gainDatasByFront(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer id=Integer.parseInt(request.getParameter("docid"));//
		map.put("id",id);//专家id
		map.put("displayName", request.getParameter("displayName"));//专家姓名
		if(StringUtils.isNotBlank(request.getParameter("mobileTelphone"))){
			//联系电话
			DoctorRegisterInfo reg=commonService.queryDoctorRegisterInfoById(id);
			reg.setMobileNumber(request.getParameter("mobileTelphone"));
			commonService.updateDoctorRegisterInfo(reg);
		}
		if (StringUtils.isNotBlank(request.getParameter("duty"))) {//职务
			map.put("duty", request.getParameter("duty"));
		}
		if (StringUtils.isNotBlank(request.getParameter("speciality"))) {//擅长
			map.put("speciality", request.getParameter("speciality"));
		}
		if (StringUtils.isNotBlank(request.getParameter("profile"))) {//个人简介
			map.put("profile", request.getParameter("profile"));
		}
		if (StringUtils.isNotBlank(request.getParameter("profession"))) {//职称
			map.put("profession", request.getParameter("profession"));
		}
		map.put("distCode", request.getParameter("distCode"));//省市
		map.put("hospitalId", Integer.parseInt(request.getParameter("hosid")));//医院id
		map.put("depId", Integer.parseInt(request.getParameter("depid")));//科室id
		map.put("position", request.getParameter("position"));//地址
		map.put("relatedPics", request.getParameter("relatedPics"));//图片
		map.put("headImageUrl",request.getParameter("headImageUrl"));//头像
		return map;
	}

	/**
	 * 获取专家咨询订单
	 * @param unread
	 * @param userId
	 * @param sEcho
	 * @param start
	 * @param length
	 * @param searchContent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String gainadviceorders(String currstatus,Integer userId,String sEcho,Integer start,Integer length,String searchContent){
		StringBuilder stringJson = null;
		List<SpecialAdviceOrder> _rcs = null;
		if (StringUtils.isNotBlank(currstatus)
				&& currstatus.equalsIgnoreCase("processing")) {
			Map<String, Object> retmap = commonService.querySpecialAdviceOrdersByCondition(1,userId,searchContent,start,length,2);
			Integer renum = (Integer) retmap.get("num");
			List<SpecialAdviceOrder> rcs = (List<SpecialAdviceOrder>) retmap
					.get("items");
			if (_rcs == null)
				_rcs = new ArrayList<SpecialAdviceOrder>();
			_rcs.addAll(rcs);
			stringJson = new StringBuilder("{\"sEcho\":" + sEcho
					+ ",\"iTotalRecords\":" + (renum)
					+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":[");
		} else {
			Map<String, Object> retmap = commonService.querySpecialAdviceOrdersByCondition(2,userId,searchContent,start,length,2);
			Integer renum = (Integer) retmap.get("num");
			List<SpecialAdviceOrder> rcs = (List<SpecialAdviceOrder>) retmap
					.get("items");
			if (_rcs == null)
				_rcs = new ArrayList<SpecialAdviceOrder>();
			_rcs.addAll(rcs);
			stringJson = new StringBuilder("{\"sEcho\":" + sEcho
					+ ",\"iTotalRecords\":" + renum
					+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":[");
		}
		SpecialAdviceOrder order = null;
		for (int i = 0; i < _rcs.size(); i++) {
			String time = "";
			order = _rcs.get(i);
			stringJson.append("[");
			stringJson.append("\"" + order.getId() + "\",");
			stringJson.append("\"" + order.getHeadImageUrl()+ "\",");
			stringJson.append("\"" + order.getExpertName() + "\",");
			stringJson.append("\"" + order.getHosName() + "\",");
			Integer unreadNum = order.getExpertUnreadMsgNum() != null ? order.getExpertUnreadMsgNum():0;
			stringJson.append("\"" + "您有"+unreadNum+"条新消息" + "\",");
			String age="";
			String sex="";
			if(order.getAge()==null){
				if(StringUtils.isNotBlank(order.getIdCard())){
					age=String.valueOf(IdcardUtils.getAgeByIdCard(order.getIdCard()));
				}else{
					age="未知";
				}
			}else{
				age=order.getAge().toString();
			}
			if(order.getSex()==null){
				if(StringUtils.isNotBlank(order.getIdCard())){
					String _sex=IdcardUtils.getGenderByIdCard(order.getIdCard());
					sex=_sex.equalsIgnoreCase("M")?"男":"女";
				}else{
					sex="未知";
				}
			}else{
				sex=order.getSex().equals(1)?"男":"女";
			}
			stringJson.append("\"" + order.getUserName()+" "+sex+" "+age + "\",");
			stringJson.append("\"" +(StringUtils.isNotBlank(order.getDiseaseDes())?order.getDiseaseDes():"")+ "\",");
			stringJson.append("\"" + sdf.format(order.getCreateTime()) + "\",");
			stringJson.append("\"" + order.getPayStatus() + "\",");
			stringJson.append("\"" + OrderStatusEnum.getStatusValue(5,order.getStatus()) + "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (_rcs.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	public Map<String,Object> processReply(Integer oid,String msgContent,Integer userId){
		Map<String,Object> map=new HashMap<String,Object>();
		SpecialAdviceOrder order=commonService.querySpecialAdviceOrderById(oid);
		BusinessMessageBean message = new BusinessMessageBean();
		Timestamp date=new Timestamp(System.currentTimeMillis());
		message.setOrderId(oid);
		message.setOrderType(5);
		message.setMsgType("text");
		message.setMsgContent(msgContent);
		message.setSendId(userId);
		message.setSendType(2);
		message.setSendTime(date);
		message.setRecvId(order.getDoctorId());
		message.setRecvType(3);
		message.setStatus(1);
		wenzhenService.saveBusinessMessageBean(message);
		message.setMsgTime(sdf.format(message.getSendTime()));
		map.put("message",message);
		order.setExpertLastAnswerTime(new Timestamp(System.currentTimeMillis()));
		order.setExpertSendMsgCount(order.getExpertSendMsgCount()==null?1:(order.getExpertSendMsgCount()+1));
		//医生未读数
		order.setDocUnreadMsgNum(order.getDocUnreadMsgNum()==null?1:(order.getDocUnreadMsgNum()+1));
		commonService.updateSpecialAdviceOrder(order);
		/*UserDevicesRecord u=wenzhenService.querySendMessageOrNot(order.getUserId());
		MobileSpecial special=commonService.queryMobileSpecialByUserIdAndUserType(user.getId());
		if(u!=null){
			String _time=sdf.format(order.getCreateTime());
			String _ret="";
			try {
				_ret = ReaderConfigUtil.gainConfigVal(request, "basic.xml",
						"root/getui");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			IPushResult ret = GetuiPushUtils.PushMessage(_ret,
					u.getPushId(),"1;"+order.getId().toString(),"您"+_time+"提交给"+special.getHosName()+""+special.getSpecialName()+special.getDuty()+"的图文问诊，专家已亲自回复，点击查看。", 1);
		}
		//短信发送
		sendpatientsms(order,special);*/
		return map;
	}
	
	/**
	 * 获取该专家视频会诊数据
	 * @param ostatus
	 * @param docid
	 * @param sEcho
	 * @param start
	 * @param length
	 * @param searchContent
	 * @param dtype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String gainVedioOrderDatas(Integer ostatus,Integer docid,
				String sEcho,Integer start,Integer length,String searchContent){
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.queryVedioOrderDatas_expert(docid,searchContent,ostatus,start,length);
		Integer renum = (Integer) retmap.get("num");
		List<VedioOrderDto> vedios = (List<VedioOrderDto>) retmap.get("items");
		for (VedioOrderDto dto : vedios) {
			dto.setDisCreateTime(RelativeDateFormat.calculateTimeLoc(dto.getCreateTime()));
			String ctime="";
			ctime+=StringUtils.isNotBlank(dto.getConsultationDate())?dto.getConsultationDate():"";
			ctime+=StringUtils.isNotBlank(dto.getConsultationTime())?" "+dto.getConsultationTime():"";
			if(StringUtils.isNotBlank(ctime)){
				dto.setDisBeginTime(RelativeDateFormat.calculateTimeLoc(ctime));
			}else{
				dto.setDisBeginTime(ctime);
			}
		}
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":");
		stringJson.append(net.sf.json.JSONArray.fromObject(vedios).toString());
		stringJson.append("}");
		return stringJson.toString();
	}
	
	/**
	 * 获取该医生专家咨询数据
	 * @param ostatus
	 * @param docid
	 * @param sEcho
	 * @param start
	 * @param length
	 * @param searchContent
	 * @param dtype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String gainD2DTuwenDatas(Integer ostatus,Integer docid,
				String sEcho,Integer start,Integer  length,String searchContent){
		StringBuilder stringJson = null;
		Map<String, Object> retmap = commonService.queryD2DTuwenDatas_expert(docid, searchContent, ostatus, start, length);
		Integer renum = (Integer) retmap.get("num");
		List<D2DTuwenDto> tuwens = (List<D2DTuwenDto>) retmap.get("items");
		for (D2DTuwenDto tuwen : tuwens) {
			if(ostatus.equals(3)){
				//已接诊
				Integer count=commonService.queryUnReadMsg(tuwen.getUuid(),5,docid);
				tuwen.setUnReadMsgCount(count);
			}
			tuwen.setDisCreateTime(RelativeDateFormat.calculateTimeLoc(tuwen.getCreateTime()));
			if(StringUtils.isNotBlank(tuwen.getReceiveTime())){
				tuwen.setDisReceiveTime(RelativeDateFormat.calculateTimeLoc(tuwen.getReceiveTime()));
			}
		}
		stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + (renum)
				+ ",\"iTotalDisplayRecords\":" + renum + ",\"aaData\":");
		stringJson.append(net.sf.json.JSONArray.fromObject(tuwens).toString());
		stringJson.append("}");
		return stringJson.toString();
	}
}
