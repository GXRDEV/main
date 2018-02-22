package com.tspeiz.modules.util.weixin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.entity.release2.DoctorConsultationOpinion;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.util.common.MicWordUtil;

public class OutputWordUtil {
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
	private static SimpleDateFormat year_sdf=new SimpleDateFormat("yyyy");
	private static SimpleDateFormat month_sdf=new SimpleDateFormat("MM");
	private static SimpleDateFormat day_sdf=new SimpleDateFormat("dd");
	private static String sig="http://tupian201604.oss-cn-beijing.aliyuncs.com/2017/06/15/20170615140712483.png";
	
public static void commonIf(IWenzhenService wenzhenService,ICommonService commonService,Integer oid,Map<String,Object> map,Integer otype) throws Exception{
		BusinessVedioOrder vedioOrder = null;
		SpecialAdviceOrder adviceOrder = null;
		DoctorConsultationOpinion opinion = null;
		CaseInfo ca = null;
		MobileSpecial doc=null;
		MobileSpecial exp =null;
		String descType = "";
		Integer docid = null;
		Integer expId = null;
		Integer caseId = null;
		String optionId = "";
		if(otype.equals(4)){
			vedioOrder=wenzhenService.queryBusinessVedioOrderById(oid);
			docid = vedioOrder.getLocalDoctorId();
			expId = vedioOrder.getExpertId();
			caseId = vedioOrder.getCaseId();
			optionId = vedioOrder.getConsultationOpinionUuid();
			descType = "远程会诊";
	        map.put("appdate",sdf.format(vedioOrder.getCreateTime()));
	        map.put("visitdate", vedioOrder.getConsultationDate()!=null?vedioOrder.getConsultationDate():"");//问诊日期
		}else if(otype.equals(5)){
			adviceOrder=wenzhenService.querySpecialAdviceOrderById(oid);
			docid = adviceOrder.getDoctorId();
			expId = adviceOrder.getExpertId();
			caseId = adviceOrder.getCaseId();
			optionId = adviceOrder.getConsultationOpinionUuid();
			descType = "图文会诊";
	        map.put("appdate",sdf.format(adviceOrder.getCreateTime()));
	        map.put("visitdate", adviceOrder.getReceiveTime()!=null?adviceOrder.getReceiveTime():"");
		}
		map.put("type", descType);
		ca=commonService.queryCaseInfoById(caseId);
		map.put("username",StringUtils.isNotBlank(ca.getContactName())?ca.getContactName():"");
		map.put("sex",ca.getSex()!=null?(ca.getSex().equals(1)?"男":"女"):"");
		map.put("age",ca.getAge()!=null?ca.getAge():"");

		doc=commonService.queryMobileSpecialByUserIdAndUserType(docid);
		if(doc!=null){
			map.put("appdoc",doc.getSpecialName()!=null?doc.getSpecialName():"");
			map.put("appdep",doc.getDepName()!=null?doc.getDepName():"");
			map.put("apphos",doc.getHosName()!=null?doc.getHosName():"");
		}else{
			map.put("appdoc","");
			map.put("appdep","");
			map.put("apphos","");
		};
		
		exp=commonService.queryMobileSpecialByUserIdAndUserType(expId);
		if(exp!=null){
			map.put("expname",exp.getSpecialName()!=null?exp.getSpecialName():"");
			map.put("visitdep",exp.getDepName()!=null?exp.getDepName():"");
			map.put("visithos",exp.getHosName()!=null?exp.getHosName():"");
			map.put("profession",exp.getProfession()!=null?exp.getProfession():"");
		}else{
			map.put("expname","");
			map.put("visitdep","");
			map.put("visithos","");
			map.put("profession","");
		}

		opinion=commonService.queryDoctorConsultationOpinionByUuid(optionId);
        if(opinion!=null){
        	map.put("diagnosis",StringUtils.isNotBlank(opinion.getDiagnosis())?opinion.getDiagnosis():"");
            map.put("treatplan",StringUtils.isNotBlank(opinion.getTreatPlan())?opinion.getTreatPlan():"");
            map.put("attentions",StringUtils.isNotBlank(opinion.getAttentions())?opinion.getAttentions():"");
            if(StringUtils.isNotBlank(opinion.getSignature())){
            	sig=opinion.getSignature();
            }else{
				map.put("diagnosis","");
				map.put("treatplan","");
				map.put("attentions","");
			}
        }else{
        	map.put("diagnosis","");
			map.put("treatplan","");
			map.put("attentions","");
        }
        map.put("year", year_sdf.format(new Date()));
        map.put("month", month_sdf.format(new Date()));
        map.put("day", day_sdf.format(new Date()));
        map.put("image", MicWordUtil.getBase64Str(sig));
	};
	
}
