package com.tspeiz.modules.manage;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.PushCodeEnum;
import com.tspeiz.modules.common.bean.PushWordBean;
import com.tspeiz.modules.common.bean.dto.ReferOrderDto;
import com.tspeiz.modules.common.bean.dto.VedioOrderDto;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.SystemPushInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessTuwenOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorRegisterInfo;
import com.tspeiz.modules.common.entity.newrelease.DoctorServiceInfo;
import com.tspeiz.modules.common.entity.newrelease.HospitalDetailInfo;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.entity.release2.BusinessD2dReferralOrder;
import com.tspeiz.modules.common.entity.release2.BusinessDtuwenOrder;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAlliance;
import com.tspeiz.modules.common.entity.release2.HospitalHealthAllianceMember;
import com.tspeiz.modules.common.entity.release2.YltApplicationRequest;
import com.tspeiz.modules.common.entity.release2.YltInvitationRequest;
import com.tspeiz.modules.common.service.IApiGetuiPushService;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.ID2pService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.common.service.impl.WenzhenServiceImpl;
import com.tspeiz.modules.common.service.weixin.IWeixinService;
import com.tspeiz.modules.util.date.RelativeDateFormat;
import com.tspeiz.modules.util.huaxin.HttpSendSmsUtil;

@Service
public class HospitalAdminManager {
	
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IWeixinService weixinService;
	@Autowired
	private ID2pService d2pService;
	@Autowired
	private CommonManager commonManager;
	@Autowired
    private IApiGetuiPushService apiGetuiPushService;
	@Autowired
	private IWenzhenService wenzhenService;
	/**
	 * 三甲医院组建医联体
	 * @param request
	 * @param reg
	 */
	public void savehoshealth(HttpServletRequest request,DoctorRegisterInfo reg){
		MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(reg.getId());
		HospitalDetailInfo hos=weixinService.queryHospitalDetailInfoById(doc.getHosId());
		String allianceId=request.getParameter("allianceId");
		HospitalHealthAlliance hha=null;
		boolean issave=false;
		if(StringUtils.isNotBlank(allianceId)){
			hha=commonService.queryHospitalHealthAllianceById(Integer.parseInt(allianceId));
		}else{
			issave=true;
			hha=new HospitalHealthAlliance();
			hha.setUuid(UUID.randomUUID().toString().replace("-", ""));
			hha.setApplicationTime(new Timestamp(System.currentTimeMillis()));
			hha.setApplicantId(reg.getId());
			hha.setApplicantType(reg.getUserType());
			hha.setStatus(0);
			hha.setHospitalId(hos.getId());
			hha.setHospitalLevel(hos.getHospitalLevel());	
		}
		hha.setYltName(request.getParameter("yltName"));
		hha.setSpeciality(request.getParameter("speciality"));
		hha.setProfile(request.getParameter("profile"));
		hha.setIconUrl(request.getParameter("iconUrl"));
		if(issave){
			commonService.saveHospitalHealthAlliance(hha);
			addMember(hha.getUuid(),hos.getId(),hos.getHospitalLevel(),null,1,1);
		}else{
			commonService.updateHospitalHealthAlliance(hha);
		}
	}
	/**
	 * 添加医联体成员
	 * @param allianceUuid
	 * @param hospitalId
	 * @param hospitalLevel
	 * @param parentHosId
	 * @param level
	 * @param role
	 */
	private void addMember(String allianceUuid,Integer hospitalId,Integer hospitalLevel,Integer parentHosId,Integer level,Integer role){
		HospitalHealthAllianceMember member=commonService.queryHospitalHealthAllianceMemberByCon(hospitalId, allianceUuid);
		if(member==null){
			member=new HospitalHealthAllianceMember();
			member.setCreateTime(new Timestamp(System.currentTimeMillis()));
			member.setAllianceUuid(allianceUuid);
			member.setHospitalId(hospitalId);
			member.setHospitalLevel(hospitalLevel);
			member.setLevel(level);
			member.setRole(role);
			member.setStatus(1);
			commonService.saveHospitalHealthAllianceMember(member);
		}else{
			member.setStatus(1);
			commonService.updateHospitalHealthAllianceMember(member);
		}
		
	}
	/**
	 * 申请加入医联体
	 * @param yltId
	 * @param applicantId
	 * @param applicantType
	 * @param hos
	 * @param parentHosId
	 */
	public void appjoinylt(Integer yltId,Integer applicantId,Integer applicantType,HospitalDetailInfo hos,Integer parentHosId){
		HospitalHealthAlliance hha=commonService.queryHospitalHealthAllianceById(yltId);
		YltApplicationRequest yltreq=commonService.queryYltApplicationRequestByCondition(hha.getUuid(),applicantId,applicantType);
		if(yltreq==null){
			yltreq=new YltApplicationRequest();
			yltreq.setAllianceUuid(hha.getUuid());
			yltreq.setApplicationTime(new Timestamp(System.currentTimeMillis()));
			yltreq.setApplicantId(applicantId);
			yltreq.setApplicantType(applicantType);
			yltreq.setParentHosId(parentHosId);
			yltreq.setHospitalId(hos.getId());
			yltreq.setHospitalLevel(hos.getHospitalLevel());
			yltreq.setStatus(OrderStatusEnum.YLT_APP_AUDITTING.getKey());
			commonService.saveYltApplicationRequest(yltreq);
		}else{
			yltreq.setApplicationTime(new Timestamp(System.currentTimeMillis()));
			yltreq.setParentHosId(parentHosId);
			commonService.updateYltApplicationRequest(yltreq);
		}
		
	}
	/**
	 * 邀请加入医联体
	 * @param yltId
	 * @param inviterId
	 * @param inviterType
	 * @param hos
	 * @param parentHosId
	 */
	public void invitjoinylt(Integer yltId,Integer inviterId,Integer inviterType,HospitalDetailInfo hos,Integer parentHosId){
		HospitalHealthAlliance hha=commonService.queryHospitalHealthAllianceById(yltId);
		YltInvitationRequest yirreq=new YltInvitationRequest();
		yirreq.setAllianceUuid(hha.getUuid());
		yirreq.setInvitationTime(new Timestamp(System.currentTimeMillis()));
		yirreq.setInviterId(inviterId);
		yirreq.setInviterType(inviterType);
		yirreq.setParentHosId(parentHosId);
		yirreq.setHospitalId(hos.getId());
		yirreq.setHospitalLevel(hos.getHospitalLevel());
		yirreq.setStatus(OrderStatusEnum.YLT_APP_AUDITTING.getKey());
		commonService.saveYltInvitationRequest(yirreq);
	}
	/**
	 * 获取医院管理---参与的医联体数据
	 * @param hosId
	 * @param paramMap
	 * @return
	 */
	public String gainmyyltdatas(Integer hosId,Map<String, String> paramMap){
		String sEcho = paramMap.get("sEcho");
		String search = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Map<String,Object> retmap=commonService.querymyyltdatas_hos(hosId,search,start,length);
		Integer renum=(Integer)retmap.get("num");
		List<HospitalHealthAlliance> list=(List<HospitalHealthAlliance>)retmap.get("items");
		StringBuilder stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		HospitalHealthAlliance hha = null;
		String allianceUuid=null;
		HospitalHealthAlliance _hha = null;
		Integer count=null;
		for (int i = 0; i < list.size(); i++) {
			hha = list.get(i);
			allianceUuid=hha.getUuid();
			_hha=commonService.queryHospitalHealthAllianceByUuid(allianceUuid);
			count=commonService.queryCountBySql("select count(hm.Id) from hospital_health_alliance_member hm where hm.AllianceUuid='"+allianceUuid+"' and hm.Status=1 ");
			stringJson.append("[");
			stringJson.append("\"" + hha.getId() + "\",");
			stringJson.append("\"" +hha.getYltName()+ "\",");
			stringJson.append("\"" + _hha.getPosition()+ "\",");
			stringJson.append("\"" + _hha.getHosName()+ "\",");
			stringJson.append("\"" +count+ "\",");
			stringJson.append("\"" + sdf.format(hha.getApplicationTime())+ "\",");
			stringJson.append("\"" +hha.getStatus()+ "\",");
			stringJson.append("\"" +"\"");
			stringJson.append("],");
		}
		if (list.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	/**
	 * 根据选择的医联体 获取上级医院
	 * @param hosId
	 * @param allianceId
	 * @return
	 */
	public List<HospitalDetailInfo> gainhighlevelhos(Integer hosId,Integer allianceId){
		HospitalDetailInfo currhos=weixinService.queryHospitalDetailInfoById(hosId);
		String hoslevels=gainlevels(currhos.getHospitalLevel());
		List<HospitalDetailInfo> hospitals=commonService.queryhighlevelhos(allianceId,hoslevels);
		return hospitals;
	}
	/**
	 * 医院管理--医联体结构
	 * @param yltId
	 * @param hosId
	 * @return
	 */
	public String hoshealthstruts(Integer yltId,Integer hosId){
		HospitalHealthAlliance hha=commonService.queryHospitalHealthAllianceById(yltId);
		List<HospitalHealthAllianceMember> members=commonService.queryHospitalHealAllianceMembersByCon(hha.getUuid(),10,null);
		JSONArray root=new JSONArray();
		if(members!=null&&members.size()>0){
			for (HospitalHealthAllianceMember _member : members) {
				JSONObject one_obj=new JSONObject();
				dataSet(one_obj,_member,hosId);
				List<HospitalHealthAllianceMember> childrens=commonService.queryHospitalHealAllianceMembersByCon(hha.getUuid(), null, _member.getHospitalId());
				if(childrens!=null&&childrens.size()>0){
					JSONArray two_arry=new JSONArray();
					boolean two_exist=false;
					for (HospitalHealthAllianceMember fchild : childrens) {
						JSONObject two_obj=new JSONObject();
						if(dataSet(two_obj,fchild,hosId)){
							two_exist=true;
							two_obj.put("expanded", "true");
						}
						List<HospitalHealthAllianceMember> _childrens=commonService.queryHospitalHealAllianceMembersByCon(hha.getUuid(), null, fchild.getHospitalId());
						if(_childrens!=null&&_childrens.size()>0){
							JSONArray third_arry=new JSONArray();	 
							boolean third_exist=false;
							for (HospitalHealthAllianceMember _fchild : _childrens) {
								JSONObject third_obj=new JSONObject();
								if(dataSet(third_obj,_fchild,hosId))third_exist=true;
								third_arry.add(third_obj);
							}
							if(third_exist)two_obj.put("expanded", "true");
							two_obj.put("children",third_arry);
						}
						if(two_exist)one_obj.put("expanded", "true");
						two_arry.add(two_obj);
					}
					one_obj.put("children",two_arry);
				}
				root.add(one_obj);
			}	
		}
		return root.toString();
	}
	
	private boolean dataSet(JSONObject obj,HospitalHealthAllianceMember member,Integer hosId){
		boolean exist=true;
		obj.put("HospitalId", member.getHospitalId());
		if(member.getHospitalId().equals(hosId)){
			obj.put("HosName", member.getHosName()+"(我院)");
		}else{
			exist=false;
			obj.put("HosName", member.getHosName());
		}
		obj.put("Area", member.getArea());
		obj.put("HosLevel", member.getLevelDesc());
		obj.put("JoinTime", sdf.format(member.getCreateTime()));
		obj.put("HospitalLevel", member.getHospitalLevel());
		return exist;
	}
	/** 
	 * 退出医联体
	 * @param userid
	 * @param yltId
	 */
	public void quitylt(Integer userid,Integer yltId){
		HospitalHealthAlliance hha=commonService.queryHospitalHealthAllianceById(yltId);
		MobileSpecial doc=commonService.queryMobileSpecialByUserIdAndUserType(userid);
		HospitalDetailInfo hos=weixinService.queryHospitalDetailInfoById(doc.getHosId());
		List<HospitalHealthAllianceMember> members=commonService.queryHospitalHealAllianceMembersByCon(hha.getUuid(),null, hos.getId());
		if(members!=null&&members.size()>0){
			for (HospitalHealthAllianceMember _member : members) {
				List<HospitalHealthAllianceMember> _members=commonService.queryHospitalHealAllianceMembersByCon(hha.getUuid(),null,_member.getHospitalId());
				if(_members!=null&&_members.size()>0){
					for (HospitalHealthAllianceMember _mem : _members) {
						_mem.setStatus(0);
						commonService.updateHospitalHealthAllianceMember(_mem);
					}
				}
				_member.setStatus(0);
				commonService.updateHospitalHealthAllianceMember(_member);
			}
		}
		HospitalHealthAllianceMember member=commonService.queryHospitalHealthAllianceMemberByCon(hos.getId(), hha.getUuid());
		member.setStatus(0);
		commonService.updateHospitalHealthAllianceMember(member);
		if(hos.getHospitalLevel().equals(10)){
			hha.setStatus(0);
			commonService.updateHospitalHealthAlliance(hha);
		}
	}
	/**
	 * 删除医联体
	 * @param memberIds
	 */
	public void dellowermembers(String memberIds){
		if(StringUtils.isNotBlank(memberIds)){
			String[] _ids=memberIds.split(",");
			for (String mId : _ids) {
				if(StringUtils.isNotBlank(mId)){
					HospitalHealthAllianceMember member=commonService.queryHospitalHealthAllianceMemberById(Integer.parseInt(mId));
					List<HospitalHealthAllianceMember> _members=commonService.queryHospitalHealAllianceMembersByCon(member.getAllianceUuid(),null,member.getHospitalId());
					if(_members!=null&&_members.size()>0){
						for (HospitalHealthAllianceMember _member : _members) {
							_member.setStatus(0);
							commonService.updateHospitalHealthAllianceMember(_member);
						}
					}
					member.setStatus(0);
					commonService.updateHospitalHealthAllianceMember(member);
				}
			}
		}
	}
	/**
	 * 获取邀请加入的医院数据
	 * @param hosId
	 * @param paramMap
	 * @return
	 */
	public String gaininvitjoindatas(Integer hosId,Map<String,String> paramMap){
		String sEcho = paramMap.get("sEcho");
		String search = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer otype=Integer.parseInt(paramMap.get("ostatus"));//1--我发起的邀请申请  2--我收到的邀请请求
		Map<String,Object> retmap=commonService.queryinvitjoin_hos(hosId,search,start,length,otype);
		Integer renum=(Integer)retmap.get("num");
		List<YltInvitationRequest> list=(List<YltInvitationRequest>)retmap.get("items");
		StringBuilder stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		YltInvitationRequest yr = null;
		for (int i = 0; i < list.size(); i++) {
			yr = list.get(i);
			stringJson.append("[");
			stringJson.append("\"" + yr.getId() + "\",");
			stringJson.append("\"" + yr.getYltName() + "\",");//医联体名称
			stringJson.append("\"" + yr.getPosition() + "\",");//所属区域
			stringJson.append("\"" + yr.getCoreHosName()+"("+yr.getCoreHosLevel()+")"+ "\",");//核心医院/级别
			if(otype.equals(1)){
				stringJson.append("\"" + yr.getHosName()+"("+yr.getHosLevel()+")"+ "\",");
			}else if(otype.equals(2)){
				stringJson.append("\"" + yr.getOtherHosName()+"("+yr.getOtherHosLevel()+")"+ "\",");
			}
			stringJson.append("\"" + sdf.format(yr.getInvitationTime())+ "\",");
			stringJson.append("\"" + (yr.getAuditTime()!=null?sdf.format(yr.getAuditTime()):"")+ "\",");
			stringJson.append("\"" + yr.getStatus()+ "\"");
			stringJson.append("],");
		}
		if (list.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	
	/**
	 * 获取申请加入的医院数据
	 * @param hosId
	 * @param paramMap
	 * @return
	 */
	public String gainappjoindatas(Integer hosId,Map<String,String> paramMap){
		String sEcho = paramMap.get("sEcho");
		String search = paramMap.get("searchContent");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer otype=Integer.parseInt(paramMap.get("ostatus"));//1--我发起的加入申请，2--我收到的加入请求
		Map<String,Object> retmap=commonService.queryappjoin_hos(hosId,search,start,length,otype);
		Integer renum=(Integer)retmap.get("num");
		List<YltApplicationRequest> list=(List<YltApplicationRequest>)retmap.get("items");
		StringBuilder stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		YltApplicationRequest yr = null;
		for (int i = 0; i < list.size(); i++) {
			yr = list.get(i);
			stringJson.append("[");
			stringJson.append("\"" + yr.getId() + "\",");
			stringJson.append("\"" + yr.getYltName() + "\",");
			stringJson.append("\"" + yr.getPosition() + "\",");
			stringJson.append("\"" + yr.getCoreHosName()+"("+yr.getCoreHosLevel()+")"+ "\",");//核心医院/级别
			if(otype.equals(1)){
				stringJson.append("\"" + yr.getOtherHosName()+"("+yr.getOtherHosLevel()+")"+ "\",");
			}else{
				stringJson.append("\"" + yr.getHosName()+"("+yr.getHosLevel()+")"+ "\",");
			}
			
			stringJson.append("\"" + sdf.format(yr.getApplicationTime())+ "\",");
			stringJson.append("\"" + (yr.getAuditTime()!=null?sdf.format(yr.getAuditTime()):"")+ "\",");
			stringJson.append("\"" + yr.getStatus()+ "\"");
			stringJson.append("],");
		}
		if (list.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	/**
	 * 修改申请或邀请的状态
	 * @param uid
	 * @param utype
	 * @param aid
	 * @param sval
	 * @param stype
	 */
	public void changeappstatus(Integer uid,Integer utype,Integer aid,Integer sval,Integer stype){
		if(stype.equals(1)){
			YltApplicationRequest rq=commonService.queryYltApplicationRequestById(aid);
			rq.setStatus(sval);
			rq.setAuditorId(uid);
			rq.setAuditorType(utype);
			rq.setAuditTime(new Timestamp(System.currentTimeMillis()));
			commonService.updateYltApplicationRequest(rq);
			if(sval.equals(1)){
				//同意
				addmember(rq.getAllianceUuid(),rq.getHospitalId(),rq.getParentHosId());
			}
		}else if(stype.equals(2)){
			YltInvitationRequest yr=commonService.queryYltInvitationRequestById(aid);
			yr.setStatus(sval);
			yr.setAuditorId(uid);
			yr.setAuditorType(utype);
			yr.setAuditTime(new Timestamp(System.currentTimeMillis()));
			commonService.updateYltInvitationRequest(yr);
			if(sval.equals(1)){
				//同意
				addmember(yr.getAllianceUuid(),yr.getHospitalId(),yr.getParentHosId());
			}
		}
	}
	/**
	 * 申请或邀请通过时新增医联体成员
	 * @param allianceUuid
	 * @param hospitalId
	 * @param parentHosId
	 */
	private void addmember(String allianceUuid,Integer hospitalId,Integer parentHosId){
		HospitalHealthAllianceMember hh_mem=commonService.queryHospitalHealthAllianceMemberByCon(hospitalId, allianceUuid);
		if(hh_mem==null){
			hh_mem=new HospitalHealthAllianceMember();
			hh_mem.setCreateTime(new Timestamp(System.currentTimeMillis()));
			hh_mem.setAllianceUuid(allianceUuid);
			hh_mem.setHospitalId(hospitalId);
			hh_mem.setParentHosId(parentHosId);
			hh_mem.setStatus(1);
			hh_mem.setRole(2);
			HospitalDetailInfo hos=weixinService.queryHospitalDetailInfoById(hospitalId);
			hh_mem.setHospitalLevel(hos.getHospitalLevel());
			hh_mem.setLevel(gainlevel(hos.getHospitalLevel()));
			commonService.saveHospitalHealthAllianceMember(hh_mem);
		}else{
			hh_mem.setStatus(1);
			hh_mem.setHospitalId(hospitalId);
			hh_mem.setParentHosId(parentHosId);
			HospitalDetailInfo hos=weixinService.queryHospitalDetailInfoById(hospitalId);
			hh_mem.setHospitalLevel(hos.getHospitalLevel());
			hh_mem.setLevel(gainlevel(hos.getHospitalLevel()));
			commonService.updateHospitalHealthAllianceMember(hh_mem);	
		}
		
	}
	/**
	 * 获取转诊订单数据
	 * @param user
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String gainReferralOrderDatas(DoctorRegisterInfo user,Map<String, String> paramMap){
		String sEcho = paramMap.get("sEcho");
		String search = paramMap.get("search");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer dtype=Integer.parseInt(paramMap.get("dtype"));//1--发起的，2--收到的
		Integer ostatus=Integer.parseInt(paramMap.get("ostatus"));
		DoctorDetailInfo doc=commonService.queryDoctorDetailInfoById(user.getId());
		Map<String,Object> retmap=commonService.queryReferordersByCondition_hos(doc.getHospitalId(), search, ostatus, start, length, dtype);
		Integer renum=(Integer)retmap.get("num");
		List<ReferOrderDto> dtos=(List<ReferOrderDto>)retmap.get("items");
		StringBuilder stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		ReferOrderDto dto = null;
		for (int i = 0; i < dtos.size(); i++) {
			dto = dtos.get(i);
			stringJson.append("[");
			stringJson.append("\"" + dto.getReferId() + "\",");
			String depName="";
			String docName="";
			String targetHosAndDep="";
			String reDocName="";
			if(dtype.equals(1)){
				depName=(StringUtils.isNotBlank(dto.getDepName())?dto.getDepName():"");
				docName=(StringUtils.isNotBlank(dto.getDocName())?dto.getDocName():"待分配");
				targetHosAndDep+=(StringUtils.isNotBlank(dto.getReferHosName())?dto.getReferHosName():"待分配");
				targetHosAndDep+=(StringUtils.isNotBlank(dto.getReferDepName())?"/"+dto.getReferDepName():"待分配");
				reDocName=(StringUtils.isNotBlank(dto.getReferDocName())?dto.getReferDocName():"待分配");
			}else if(dtype.equals(2)){
				depName=(StringUtils.isNotBlank(dto.getReferDepName())?dto.getReferDepName():"");
				docName=(StringUtils.isNotBlank(dto.getReferDocName())?dto.getReferDocName():"待分配");
				targetHosAndDep+=(StringUtils.isNotBlank(dto.getHosName())?dto.getHosName():"待分配");
				targetHosAndDep+=(StringUtils.isNotBlank(dto.getDepName())?"/"+dto.getDepName():"待分配");
				reDocName=(StringUtils.isNotBlank(dto.getDocName())?dto.getDocName():"待分配");
			}
			stringJson.append("\"" + depName + "\",");
			stringJson.append("\"" + docName + "\",");
			String patientInfo="";
			patientInfo+=StringUtils.isNotBlank(dto.getUserName())?dto.getUserName():"";
			patientInfo+=(dto.getSex()!=null)?(dto.getSex().equals(1)?"/"+"男":"/"+"女"):"";
			patientInfo+=(dto.getAge()!=null?"/"+dto.getAge():"");
			stringJson.append("\"" + patientInfo + "\",");
			stringJson.append("\"" + targetHosAndDep + "\",");
			stringJson.append("\"" + reDocName + "\",");
			stringJson.append("\"" + dto.getReferDate() + "\",");
			stringJson.append("\"" + RelativeDateFormat.calculateTimeLoc(dto.getReferDate()) + "\",");
			stringJson.append("\"" + dto.getCreateTime() + "\",");
			stringJson.append("\"" + RelativeDateFormat.calculateTimeLoc(dto.getCreateTime()) + "\",");
			stringJson.append("\"" + (dto.getReferType().equals(1)?"住院":"门诊") + "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (dtos.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	/**
	 * 删除转诊订单
	 * @param user
	 * @param referId
	 */
	public void delReferral(DoctorRegisterInfo user,Integer referId){
		BusinessD2dReferralOrder order=d2pService.queryd2dreferralOrderbyId(referId);
		order.setDelFlag(1);
		order.setClosedTime(new Timestamp(System.currentTimeMillis()));
		order.setCloserId(user.getId());
		order.setCloserType(user.getUserType());
		d2pService.updateBusinessD2dReferralOrder(order);
	}
	/**
	 * 更新转诊订单状态 取消或退诊
	 * @param user
	 * @param referId
	 * @param sval
	 */
	public void changReferralStat(DoctorRegisterInfo user,Integer referId,Integer sval){
		BusinessD2dReferralOrder order=d2pService.queryd2dreferralOrderbyId(referId);
		order.setStatus(sval);
		order.setClosedTime(new Timestamp(System.currentTimeMillis()));
		order.setCloserId(user.getId());
		order.setCloserType(user.getUserType());
		d2pService.updateBusinessD2dReferralOrder(order);
	}
	/**
	 * 删除视频会诊订单
	 * @param user
	 * @param referId
	 */
	public void delVedio(DoctorRegisterInfo user,Integer vedioId){
		BusinessVedioOrder order=commonService.queryBusinessVedioOrderById(vedioId);
		order.setDelFlag(1);
		order.setClosedTime(new Timestamp(System.currentTimeMillis()));
		order.setCloserId(user.getId());
		order.setCloserType(user.getUserType());
		commonService.updateBusinessVedioOrder(order);
	}
	/**
	 * 更新转诊订单状态 取消或退诊
	 * @param user
	 * @param referId
	 * @param sval
	 */
	public void changVedioStat(DoctorRegisterInfo user,Integer vedioId,Integer sval){
		BusinessVedioOrder order=commonService.queryBusinessVedioOrderById(vedioId);
		order.setStatus(sval);
		order.setClosedTime(new Timestamp(System.currentTimeMillis()));
		order.setCloserId(user.getId());
		order.setCloserType(user.getUserType());
		commonService.updateBusinessVedioOrder(order);
	}
	
	/**
	 * 获取视频会诊订单数据
	 * @param user
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String gainVedioOrderDatas(DoctorRegisterInfo user,Map<String, String> paramMap){
		String sEcho = paramMap.get("sEcho");
		String search = paramMap.get("search");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer dtype=Integer.parseInt(paramMap.get("dtype"));//1--发起的，2--收到的
		Integer ostatus=Integer.parseInt(paramMap.get("ostatus"));
		DoctorDetailInfo doc=commonService.queryDoctorDetailInfoById(user.getId());
		Map<String,Object> retmap=commonService.queryVedioordersByCondition_hos(doc.getHospitalId(), search, ostatus, start, length, dtype);
		Integer renum=(Integer)retmap.get("num");
		List<VedioOrderDto> dtos=(List<VedioOrderDto>)retmap.get("items");
		StringBuilder stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		VedioOrderDto dto = null;
		for (int i = 0; i < dtos.size(); i++) {
			dto = dtos.get(i);
			stringJson.append("[");
			stringJson.append("\"" + dto.getVedioId() + "\",");
			String depName="";
			String docName="";
			String targetHosAndDep="";
			String reDocName="";
			if(dtype.equals(1)){
				depName=(StringUtils.isNotBlank(dto.getLocalDepName())?dto.getLocalDepName():"");
				docName=(StringUtils.isNotBlank(dto.getLocalDocName())?dto.getLocalDocName():"待分配");
				targetHosAndDep+=(StringUtils.isNotBlank(dto.getExHosName())?dto.getExHosName():"待分配");
				targetHosAndDep+=(StringUtils.isNotBlank(dto.getExDepName())?"/"+dto.getExDepName():"/"+"待分配");
				reDocName=(StringUtils.isNotBlank(dto.getExDocName())?dto.getExDocName():"待分配");
			}else if(dtype.equals(2)){
				depName=(StringUtils.isNotBlank(dto.getExDepName())?dto.getExDepName():"");
				docName=(StringUtils.isNotBlank(dto.getExDocName())?dto.getExDocName():"待分配");
				targetHosAndDep+=(StringUtils.isNotBlank(dto.getLocalHosName())?dto.getLocalHosName():"待分配");
				targetHosAndDep+=(StringUtils.isNotBlank(dto.getLocalDepName())?"/"+dto.getLocalDepName():"/"+"待分配");
				reDocName=(StringUtils.isNotBlank(dto.getLocalDocName())?dto.getLocalDocName():"待分配");
			}
			stringJson.append("\"" + depName + "\",");
			stringJson.append("\"" + docName + "\",");
			String patientInfo="";
			patientInfo+=StringUtils.isNotBlank(dto.getUserName())?dto.getUserName():"";
			patientInfo+=(dto.getSex()!=null)?(dto.getSex().equals(1)?"/"+"男":"/"+"女"):"";
			patientInfo+=(dto.getAge()!=null?"/"+dto.getAge():"");
			stringJson.append("\"" + patientInfo + "\",");
			stringJson.append("\"" + targetHosAndDep + "\",");
			stringJson.append("\"" + reDocName + "\",");
			String beginTime="";
			beginTime+=StringUtils.isNotBlank(dto.getConsultationDate())?dto.getConsultationDate():"";
			beginTime+=StringUtils.isNotBlank(dto.getConsultationTime())?dto.getConsultationTime():"";
			stringJson.append("\"" + beginTime + "\",");
			stringJson.append("\"" + RelativeDateFormat.calculateTimeLoc(beginTime) + "\",");
			stringJson.append("\"" + dto.getCreateTime() + "\",");
			stringJson.append("\"" + RelativeDateFormat.calculateTimeLoc(dto.getCreateTime()) + "\",");
			stringJson.append("\"" + (dto.getPayStatus()!=null?(dto.getPayStatus().equals(1)?"已支付":"未支付"):"未支付") + "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (dtos.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	
	public void distAndSelDoc(Integer type,Integer doctorId,Integer orderId,Integer dtype){
		DoctorDetailInfo doc=commonService.queryDoctorDetailInfoById(doctorId);
		String content="";
		String exp_content="";
		Integer expid=null;
		String businessKey="";
		Integer ex_type=null;
		String desc = "";
		CaseInfo caseinfo=null;
		Map<String, String> map = new HashMap<>();
		if(type.equals(4)){
			desc = "视频会诊";
			//视频订单
			BusinessVedioOrder vedioOrder=commonService.queryBusinessVedioOrderById(orderId);
			 caseinfo = wenzhenService.queryCaseInfoById(vedioOrder.getCaseId());
			businessKey=vedioOrder.getUuid();
			map.put("localDoctorId", String.valueOf(vedioOrder.getLocalDoctorId()));
			if(dtype.equals(1)){
				//我发起的
				MobileSpecial local_doc=commonService.queryMobileSpecialByUserIdAndUserType(doctorId);
				vedioOrder.setLocalDoctorId(doctorId);
				vedioOrder.setLocalDepartId(local_doc.getDepId());
				vedioOrder.setLocalHospitalId(local_doc.getHosId());
				expid=vedioOrder.getExpertId();
				if(expid!=null){
					MobileSpecial exp=commonService.queryMobileSpecialByUserIdAndUserType(expid);
					ex_type=exp.getUserType();
				}
			}else{
				//我接收的
				MobileSpecial exp=commonService.queryMobileSpecialByUserIdAndUserType(doctorId);
				vedioOrder.setExpertId(doctorId);
				vedioOrder.setExpertType(exp.getUserType());
				vedioOrder.setExpertDepId(doc.getDepId());
				vedioOrder.setExpertHospitalId(doc.getHospitalId());
				expid=vedioOrder.getLocalDoctorId();
				if(expid!=null){
					MobileSpecial local_doc=commonService.queryMobileSpecialByUserIdAndUserType(expid);
					ex_type=local_doc.getUserType();
				}
			}
			commonService.updateBusinessVedioOrder(vedioOrder);
			content=PushWordBean.VEDIO_ORDER_NEW_DOC;
			exp_content=PushWordBean.VEDIO_ORDER_NEW_EXP;
		}else if(type.equals(10)){
			desc = "预约转诊";
			//转诊订单
			BusinessD2dReferralOrder order=d2pService.queryd2dreferralOrderbyId(orderId);
		    caseinfo = wenzhenService.queryCaseInfoById(order.getCaseId());
			map.put("doctorId", String.valueOf(order.getDoctorId()));
			map.put("referralType", String.valueOf(order.getReferralType()));
			businessKey=order.getUuid();
			if(dtype.equals(1)){
				//我发起的
				MobileSpecial localdoc=d2pService.queryBusinessD2dReferralOrderByUserId(doctorId);
				expid=order.getReferralDocId();
				order.setDoctorId(doctorId);
				order.setLocalHospitalId(localdoc.getHosId());
				if(expid!=null){
					MobileSpecial exp=commonService.queryMobileSpecialByUserIdAndUserType(expid);
					ex_type=exp.getUserType();
				}
			}else{
				//我接收的
				expid=order.getDoctorId();
				order.setReferralDocId(doctorId);
				order.setReferralDepId(doc.getDepId());
				order.setReferralHosId(doc.getHospitalId());
				if(expid!=null){
					MobileSpecial exp=commonService.queryMobileSpecialByUserIdAndUserType(expid);
					ex_type=exp.getUserType();
				}
			}
			d2pService.updateBusinessD2dReferralOrder(order);
			content=PushWordBean.REFER_ORDER_NEW_DOC;
			exp_content=PushWordBean.REFER_ORDER_NEW_EXP;
		}else if(type.equals(5)){
			desc = "图文会诊";
			//视频订单
			SpecialAdviceOrder tuwenOrder=commonService.querySpecialAdviceOrderById(orderId);
			caseinfo = wenzhenService.queryCaseInfoById(tuwenOrder.getCaseId());
			businessKey=tuwenOrder.getUuid();
			map.put("localDoctorId", String.valueOf(tuwenOrder.getDoctorId()));
			if(dtype.equals(1)){
				//我发起的
				MobileSpecial local_doc=commonService.queryMobileSpecialByUserIdAndUserType(doctorId);
				tuwenOrder.setDoctorId(doctorId);
				tuwenOrder.setLocalHospitalId(local_doc.getHosId());
				expid=tuwenOrder.getExpertId();
				if(expid!=null){
					MobileSpecial exp=commonService.queryMobileSpecialByUserIdAndUserType(expid);
					ex_type=exp.getUserType();
				}
			}else{
				//我接收的
				MobileSpecial exp=commonService.queryMobileSpecialByUserIdAndUserType(doctorId);
				tuwenOrder.setExpertId(doctorId);
				tuwenOrder.setExpertType(exp.getUserType());
				expid=tuwenOrder.getExpertId();
				if(expid!=null){
					MobileSpecial local_doc=commonService.queryMobileSpecialByUserIdAndUserType(expid);
					ex_type=local_doc.getUserType();
				}
			}
			commonService.updateSpecialAdviceOrder(tuwenOrder);
			content=PushWordBean.TUWEN_ORDER_NEW_DOC;
			exp_content=PushWordBean.TUWEN_ORDER_NEW_EXP;
		}
		//给专家发送的消息
		if(expid!=null){
	        // 添加消息附属信息
	        map = apiGetuiPushService.setPushDoctorExtend(map, expid);
			//推送消息
			commonManager.generateSystemPushInfo(PushCodeEnum.ChangeDoctor.getCode(), businessKey, type, expid,
					ex_type,map, exp_content);
			MobileSpecial h_doc = commonService.queryMobileSpecialByUserIdAndUserType(doctorId);
			//医院 科室 已安排了 xxx 医生来接诊您的xx订单，请登陆 佰医汇APP端 或 佰医汇WEB端 查看详情。
			MobileSpecial e_doc = commonService.queryMobileSpecialByUserIdAndUserType(expid);
			if(e_doc!=null&&StringUtils.isNotBlank(e_doc.getMobileTelphone())){
				String _content = h_doc.getHosName()+" "+h_doc.getDepName()+" 已安排了 "+h_doc.getSpecialName()+" 医生来接诊您的"+desc+"订单，请登陆 佰医汇APP端 或 佰医汇WEB端 查看详情。【佰医汇】";
				HttpSendSmsUtil.sendSmsInteface(e_doc.getMobileTelphone(), _content);
			}
		}
		//给医生发送消息
		map = apiGetuiPushService.setPushPatientExtend(map, caseinfo.getSubUserUuid());
		commonManager.generateSystemPushInfo(PushCodeEnum.ChangeDoctor.getCode(), businessKey, type, doctorId,
				3,map, content);
		//您所在医院的管理员 xx 给您分配了一个xx订单，请登陆 佰医汇APP端 或 佰医汇WEB端 对订单进行处理。
		MobileSpecial _h_doc = commonService.queryMobileSpecialByUserIdAndUserType(doctorId);
		MobileSpecial _h_ad_doc = commonService.queryHosAdminMobileSpecialByHosId(_h_doc.getHosId());
		if(_h_ad_doc!=null && _h_doc!=null && StringUtils.isNotBlank(_h_doc.getMobileTelphone())){
			String _content = "您所在医院的管理员 "+_h_ad_doc.getSpecialName()+" 给您分配了一个"+desc+"订单，请登陆 佰医汇APP端 或 佰医汇WEB端 对订单进行处理。【佰医汇】";
			HttpSendSmsUtil.sendSmsInteface(_h_doc.getMobileTelphone(), _content);
		}
	}
	
	private void processSystemPushInfo(Integer pushCode,String businessKey,Integer businessType,Integer userId,Integer userType,String content){
		SystemPushInfo pinfo = new SystemPushInfo();
		UUID uuid = UUID.randomUUID();
		pinfo.setPushKey(uuid.toString().replace("-", ""));
		pinfo.setPushCode(pushCode);
		pinfo.setCreateTime(new Date());
		pinfo.setBusinessKey(businessKey);
		pinfo.setBusinessType(businessType);
		pinfo.setUserId(userId);
		pinfo.setUserType(userType);
		pinfo.setContent(content);
		commonService.saveSystemPushInfo(pinfo);
	}
	
	private String gaininvdesc(Integer status){
		if(OrderStatusEnum.YLT_INV_WAIT.getKey()==status){
			return OrderStatusEnum.YLT_INV_WAIT.getValue();
		}
		if(OrderStatusEnum.YLT_INV_TONGYI.getKey()==status){
			return OrderStatusEnum.YLT_INV_TONGYI.getValue();
		}
		if(OrderStatusEnum.YLT_INV_REFULSE.getKey()==status){
			return OrderStatusEnum.YLT_INV_REFULSE.getValue();
		}
		return null;
	}
	
	private Integer gainlevel(Integer hosLevel){
		Integer level=null;
		switch(hosLevel){
		case 10:
		case 11:
		case 12:
			level=1;
			break;
		case 13:
		case 14:
		case 15:
			level=2;
			break;
		case 16:
		case 17:
		case 18:
			level=3;
			break;
		}
		return level;
	}
	
	
	//上级医院等级
	private String gainlevels(Integer level){
		String levels="";
		switch(level){
		case 10:
			break;
		case 11:	
		case 12:
			levels="10";
			break;
		case 13:
		case 14:
		case 15:
			levels="10,11,12";
			break;
		case 16:
		case 17:
		case 18:
			levels="10,11,12,13,14,15";
			break;
		}
		return levels;
	}
	
	/**
	 * 获取图文会诊订单数据
	 * @param user
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object gaintuwenOrderDatas(DoctorRegisterInfo user, Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		String sEcho = paramMap.get("sEcho");
		String search = paramMap.get("search");
		Integer start = Integer.parseInt(paramMap.get("iDisplayStart"));
		Integer length = Integer.parseInt(paramMap.get("iDisplayLength"));
		Integer dtype=Integer.parseInt(paramMap.get("dtype"));//1--发起的，2--收到的
		Integer ostatus=Integer.parseInt(paramMap.get("ostatus"));
		DoctorDetailInfo doc=commonService.queryDoctorDetailInfoById(user.getId());
		Map<String,Object> retmap=commonService.querySpecialAdviceOrdersByCondition(doc.getHospitalId(), search, ostatus, start, length, dtype);
		Integer renum=(Integer)retmap.get("num");
		List<SpecialAdviceOrder> dtos=(List<SpecialAdviceOrder>)retmap.get("items");
		StringBuilder stringJson = new StringBuilder("{\"sEcho\":" + sEcho
				+ ",\"iTotalRecords\":" + renum + ",\"iTotalDisplayRecords\":"
				+ renum + ",\"aaData\":[");
		SpecialAdviceOrder dto = null;
		for (int i = 0; i < dtos.size(); i++) {
			dto = dtos.get(i);
			stringJson.append("[");
			stringJson.append("\"" + dto.getId() + "\",");
			String depName="";
			String docName="";
			String targetHosAndDep="";
			String reDocName="";
			if(dtype.equals(1)){
				depName=(StringUtils.isNotBlank(dto.getLocalDepName())?dto.getLocalDepName():"");
				docName=(StringUtils.isNotBlank(dto.getLocalDocName())?dto.getLocalDocName():"待分配");
				targetHosAndDep+=(StringUtils.isNotBlank(dto.getHosName())?dto.getHosName():"待分配");
				targetHosAndDep+=(StringUtils.isNotBlank(dto.getDepName())?"/"+dto.getDepName():"/"+"待分配");
				reDocName=(StringUtils.isNotBlank(dto.getExpertName())?dto.getExpertName():"待分配");
			}else if(dtype.equals(2)){
				depName=(StringUtils.isNotBlank(dto.getDepName())?dto.getDepName():"");
				docName=(StringUtils.isNotBlank(dto.getExpertName())?dto.getExpertName():"待分配");
				targetHosAndDep+=(StringUtils.isNotBlank(dto.getLocalHosName())?dto.getLocalHosName():"待分配");
				targetHosAndDep+=(StringUtils.isNotBlank(dto.getLocalDepName())?"/"+dto.getLocalDepName():"/"+"待分配");
				reDocName=(StringUtils.isNotBlank(dto.getLocalDocName())?dto.getLocalDocName():"待分配");
			}
			stringJson.append("\"" + depName + "\",");
			stringJson.append("\"" + docName + "\",");
			String patientInfo="";
			patientInfo+=StringUtils.isNotBlank(dto.getUserName())?dto.getUserName():"";
			patientInfo+=(dto.getSex()!=null)?(dto.getSex().equals(1)?"/"+"男":"/"+"女"):"";
			patientInfo+=(dto.getAge()!=null?"/"+dto.getAge():"");
			stringJson.append("\"" + patientInfo + "\",");
			stringJson.append("\"" + targetHosAndDep + "\",");
			stringJson.append("\"" + reDocName + "\",");
			stringJson.append("\"" + dto.getCreateTimes() + "\",");
			stringJson.append("\"" + RelativeDateFormat.calculateTimeLoc(dto.getCreateTimes()) + "\",");
			stringJson.append("\"" + (dto.getPayStatus()!=null?(dto.getPayStatus().equals(1)?"已支付":"未支付"):"未支付") + "\",");
			stringJson.append("\"\"");
			stringJson.append("],");
		}
		if (dtos.size() > 0)
			stringJson.deleteCharAt(stringJson.length() - 1);
		stringJson.append("]");
		stringJson.append("}");
		return stringJson.toString();
	}
	
    /**
     * 删除图文会诊订单
     * @param user
     * @param tuwenId
     */
    public void delTuwen(DoctorRegisterInfo user, Integer tuwenId) {
        // TODO Auto-generated method stub
        BusinessDtuwenOrder order=commonService.queryBussinessTuwenOrderById(tuwenId);
        order.setDelFlag(1);
        order.setClosedTime(new Timestamp(System.currentTimeMillis()));
        order.setCloserId(user.getId());
        order.setCloserType(user.getUserType());
        //commonService.updateBusinessVedioOrder(order);
        commonService.updateBusinesstuwenOrder(order);
        
    }
    /*
     * 更新图文会诊订单状态 取消或退诊
     */
    public void changTuwenStat(DoctorRegisterInfo user, Integer referId, Integer sval) {
        // TODO Auto-generated method stub
        BusinessDtuwenOrder order=commonService.queryBussinessTuwenOrderById(referId);
        order.setStatus(sval);
        order.setClosedTime(new Timestamp(System.currentTimeMillis()));
        order.setCloserId(user.getId());
        order.setCloserType(user.getUserType());
        commonService.updateBusinesstuwenOrder(order);
    }

}
