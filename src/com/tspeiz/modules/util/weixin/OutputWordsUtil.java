package com.tspeiz.modules.util.weixin;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.IWenzhenService;

public class OutputWordsUtil {
	public static void commonIf(IWenzhenService wenzhenService,
			ICommonService commonService, Integer oid, Map<String, Object> map,
			Integer type) throws Exception {
		BusinessVedioOrder vedioOrder = null;
		SpecialAdviceOrder adviceOrder = null;
		CaseInfo ca = null;
		MobileSpecial doc = null;
		MobileSpecial exp = null;
		Integer caseId = null;
		String descType = "";
		Integer docid = null;
		Integer expId = null;
		if (type.equals(4)) {
			vedioOrder = wenzhenService.queryBusinessVedioOrderById(oid);
			docid = vedioOrder.getLocalDoctorId();
			expId = vedioOrder.getExpertId();
			caseId = vedioOrder.getCaseId();
			descType = "远程会诊";
		} else if (type.equals(5)) {
			adviceOrder = wenzhenService.querySpecialAdviceOrderById(oid);
			docid = adviceOrder.getDoctorId();
			expId = adviceOrder.getExpertId();
			caseId = adviceOrder.getCaseId();
			descType = "图文会诊";
		}
		ca = commonService.queryCaseInfoById(caseId);
		if (ca != null) {
			map.put("mainSuit", ca.getMainSuit() != null ? ca.getMainSuit()
					: "");
			map.put("examined", ca.getExamined() != null ? ca.getExamined()
					: "");
			map.put("initialDiagnosis",
					ca.getInitialDiagnosis() != null ? ca.getInitialDiagnosis()
							: "");
			map.put("askProblem2",
					ca.getAskProblem() != null ? ca.getAskProblem() : "");
		}
		map.put("type", descType);// 会诊方式
		map.put("level", "");
		map.put("username",
				StringUtils.isNotBlank(ca.getContactName()) ? ca
						.getContactName() : "");
		map.put("sex",
				ca.getSex() != null ? (ca.getSex().equals(1) ? "男" : "女")
						: "未知");
		map.put("age", ca.getAge() != null ? ca.getAge() : "");

		doc = commonService.queryMobileSpecialByUserIdAndUserType(docid);
		if (doc != null) {
			map.put("hosName", doc.getHosName() != null ? doc.getHosName()
					: "");
			map.put("displayName",
					doc.getSpecialName() != null ? doc.getSpecialName() : "");
			map.put("depName", doc.getDepName() != null ? doc.getDepName()
					: "");
			map.put("docduty2", doc.getDuty() != null ? doc.getDuty() : "");
		} else {
			map.put("hosName", "");
			map.put("displayName", "");
			map.put("depName", "");
			map.put("docduty2", "");
		}

		if (expId != null) {
			exp = commonService.queryMobileSpecialByUserIdAndUserType(expId);
			map.put("hosNames", exp.getHosName() != null ? exp.getHosName()
					: "");
			map.put("docName",
					exp.getSpecialName() != null ? exp.getSpecialName() : "");
			map.put("depNames", exp.getDepName() != null ? exp.getDepName()
					: "");
			map.put("dutys", exp.getDuty() != null ? exp.getDuty() : "");
		} else {
			map.put("hosNames", "");
			map.put("docName", "");
			map.put("depNames", "");
			map.put("dutys", "");
		}
	}
}
