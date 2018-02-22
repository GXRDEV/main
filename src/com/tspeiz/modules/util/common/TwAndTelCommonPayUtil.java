package com.tspeiz.modules.util.common;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.tspeiz.modules.common.entity.newrelease.BusinessOrderInfo;
import com.tspeiz.modules.common.entity.newrelease.BusinessTelOrder;
import com.tspeiz.modules.common.entity.newrelease.BusinessTuwenOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseImages;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;

public class TwAndTelCommonPayUtil {
	public static boolean tellcaseimage(List<CaseImages> images,
			HttpServletRequest request, String caseid) {
		String imgstr = request.getParameter("caseImages");
		if (images != null && images.size() > 0) {
			String[] _strs = imgstr.split(",");
			if (_strs != null && _strs.length > 0) {
				if (Integer.valueOf(_strs.length).equals(images.size())) {
					Integer num = 0;
					for (CaseImages _case : images) {
						boolean b = false;
						for (String _str : _strs) {
							if (_case.getId().equals(Integer.parseInt(_str))) {
								b = true;
								break;
							}
						}
						if (b) {
							num += 1;
						}
					}
					if (!num.equals(images.size()))
						return false;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			if (StringUtils.isNotBlank(imgstr))
				return false;
		}
		return true;
	}

	public static void caseInfoData(CaseInfo info, HttpServletRequest request,
			Integer uid, String caseName, String cname, String idcard,
			String desc, String tel) {
		String age = request.getParameter("age");
		if (StringUtils.isNotBlank(age)) {
			info.setAge(Integer.parseInt(age));
		}
		String sex = request.getParameter("sex");
		if (StringUtils.isNotBlank(sex)) {
			info.setSex(Integer.parseInt(sex));
		}
		info.setUserId(uid);
		info.setCaseName(caseName);
		info.setContactName(cname);
		info.setIdNumber(idcard);
		/*info.setDescription(desc);*/
		info.setPresentIll(desc);
		info.setTelephone(tel);
		info.setCreateTime(new Date());
		info.setAskProblem(request.getParameter("askProblem"));
	}

	public static void payOrderInfo(BusinessOrderInfo payinfo, Integer oid,
			BigDecimal money, Integer ltype, Integer payStatus, Integer uid,
			Integer docid, String outTradeNo) {
		payinfo.setWenZhen_Id(oid);
		payinfo.setAmount(money);
		payinfo.setOrderType(ltype);
		payinfo.setPayStatus(payStatus);
		payinfo.setFromId(uid);
		payinfo.setToId(docid);
		payinfo.setCreateTime(new Date());
		payinfo.setPayMode(2);
		payinfo.setFlowNumber(outTradeNo);
	}
	
	/*public static void twOrderInfo(BusinessWenZhenInfo info,Integer caseid,Integer docid,Integer uid,Integer askStatus,Integer askType,String orgin,Boolean pay){
		info.setCases_Id(caseid);
		info.setDoctorId(docid);
		info.setUserId(uid);
		info.setAskStatus(askStatus);
		info.setAskType(askType);
		info.setStartTime(new Date());
		info.setOrigin(orgin);
		if (pay) {
			info.setPayStatus(4);
		} else {
			info.setPayStatus(0);
		}
	}
	*/
	public static void twOrderInfo(BusinessTuwenOrder info,Integer caseid,Integer docid,Integer uid,Integer status,Integer source,Boolean pay){
		info.setCaseId(caseid);
		info.setDoctorId(docid);
		info.setUserId(uid);
		info.setStatus(status);
		info.setCreateTime(new Timestamp(System.currentTimeMillis()));
		info.setSource(source);
		info.setDelFlag(0);
		if (pay) {
			info.setPayStatus(4);//待付款
		} else {
			info.setPayStatus(0);//免费
		}
	}
	public static void telOrderInfo(BusinessTelOrder telinfo,Integer uid,
			Integer docid, Integer caseid, Boolean pay,Integer source,Integer status) {
		telinfo.setCaseId(caseid);
		telinfo.setDoctorId(docid);
		telinfo.setUserId(uid);
		telinfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
		telinfo.setSource(source);
		telinfo.setStatus(status);
		telinfo.setDelFlag(0);
		if (pay) {
			telinfo.setPayStatus(4);//待付款
		} else {
			telinfo.setPayStatus(0);
		}
	}
	/*public static void telOrderInfo(BusinessWenzhenTel telinfo,Integer uid,
			Integer docid, Integer caseid, Boolean pay,String orgin,Integer status) {
		telinfo.setCaseId(caseid);
		telinfo.setCreateTime(new Date());
		telinfo.setDoctorId(docid);
		telinfo.setOrigin(orgin);
		telinfo.setStatus(0);// 等待确认
		telinfo.setUserId(uid);
		if (pay) {
			telinfo.setPayStatus(4);
		} else {
			telinfo.setPayStatus(0);
		}
	}*/
}
