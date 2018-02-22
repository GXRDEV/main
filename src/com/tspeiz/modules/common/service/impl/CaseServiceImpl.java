package com.tspeiz.modules.common.service.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tspeiz.modules.common.dao.newrelease.ICaseInfoDao;
import com.tspeiz.modules.common.entity.newrelease.CaseInfo;
import com.tspeiz.modules.common.service.ICaseService;
import com.tspeiz.modules.util.IdcardUtils;

/**
 * 处理病例业务
 * 
 * @author heyongb
 * 
 */
@Service
public class CaseServiceImpl implements ICaseService {
	@Autowired
	private ICaseInfoDao caseInfoDao;

	/**
	 * 新增或修改病例信息
	 */
	public Integer saveOrUpdateCase(HttpServletRequest request, Integer userId,Integer usertype)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> datas = gainDatasByFront(request, userId);
		CaseInfo caseinfo = null;
		Class clz = CaseInfo.class;
		boolean update = true;
		if (datas.containsKey("id") && datas.get("id") != null) {
			// 更新
			caseinfo = caseInfoDao.find(Integer.parseInt(datas.get("id")
					.toString()));
		} else {
			update = false;
			// 新增
			caseinfo = new CaseInfo();
			caseinfo.setUuid(UUID.randomUUID().toString().replace("-", ""));
		}
		for (String propertyName : datas.keySet()) {
			PropertyDescriptor pd = new PropertyDescriptor(propertyName, clz);
			Method method = pd.getWriteMethod();
			method.invoke(caseinfo, datas.get(propertyName));
		}
		if (update) {
			caseInfoDao.update(caseinfo);
		} else {
			caseinfo.setUserType(usertype);
			caseInfoDao.save(caseinfo);
		}
		return caseinfo.getId();
	}

	private Map<String, Object> gainDatasByFront(HttpServletRequest request,
			Integer userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		String id = request.getParameter("caseid");
		if (StringUtils.isNotBlank(id)) {
			map.put("id", Integer.parseInt(id));
		} else {
			map.put("createTime", new Date());
		}

		if(StringUtils.isNotBlank(request.getParameter("username"))){
			map.put("contactName", request.getParameter("username"));
		}	
		if (StringUtils.isNotBlank(request.getParameter("contactName"))) {
			map.put("contactName", request.getParameter("contactName"));
		}
		if (StringUtils.isNotBlank(request.getParameter("sex"))) {
			map.put("sex", Integer.parseInt(request.getParameter("sex")));
		}
		if (StringUtils.isNotBlank(request.getParameter("age"))) {
			map.put("age", Integer.parseInt(request.getParameter("age")));
		}
		if (StringUtils.isNotBlank(request.getParameter("idcard"))) {
			map.put("idNumber", request.getParameter("idcard"));
			if(!StringUtils.isNotBlank(request.getParameter("sex"))){
				String ssex=IdcardUtils.getGenderByIdCard(request.getParameter("idcard"));
				map.put("sex",ssex.equalsIgnoreCase("M") ? 1 : 0);
			}
			if(!StringUtils.isNotBlank(request.getParameter("age"))){
				map.put("age",IdcardUtils.getAgeByIdCard(request.getParameter("idcard")));
			}
		}
		if(StringUtils.isNotBlank(request.getParameter("telphone"))){
			map.put("telephone", request.getParameter("telphone"));
		}
		if (StringUtils.isNotBlank(request.getParameter("telephone"))) {
			map.put("telephone", request.getParameter("telephone"));
		}
		if (StringUtils.isNotBlank(request.getParameter("caseName"))) {
			map.put("caseName", request.getParameter("caseName"));
		}
		if (StringUtils.isNotBlank(request.getParameter("keywords"))) {
			map.put("keywords", request.getParameter("keywords"));
		}
		if (StringUtils.isNotBlank(request.getParameter("description"))) {
			map.put("presentIll", request.getParameter("description"));
		}
		if (StringUtils.isNotBlank(request.getParameter("mainSuit"))) {
			map.put("mainSuit", request.getParameter("mainSuit"));
		}
		if (StringUtils.isNotBlank(request.getParameter("historyIll"))) {
			map.put("historyIll", request.getParameter("historyIll"));
		}
		if (StringUtils.isNotBlank(request.getParameter("presentIll"))) {
			map.put("presentIll", request.getParameter("presentIll"));
		}
		if (StringUtils.isNotBlank(request.getParameter("examined"))) {
			map.put("examined", request.getParameter("examined"));
		}
		if (StringUtils.isNotBlank(request.getParameter("assistantResult"))) {
			map.put("assistantResult", request.getParameter("assistantResult"));
		}
		if (StringUtils.isNotBlank(request.getParameter("initialDiagnosis"))) {
			map.put("initialDiagnosis",
					request.getParameter("initialDiagnosis"));
		}
		if (StringUtils.isNotBlank(request.getParameter("treatAdvice"))) {
			map.put("treatAdvice", request.getParameter("treatAdvice"));
		}
		if (StringUtils.isNotBlank(request.getParameter("familyHistory"))) {
			map.put("familyHistory", request.getParameter("familyHistory"));
		}
		if(StringUtils.isNotBlank(request.getParameter("caseImages"))){
			map.put("normalImages", request.getParameter("caseImages"));
		}
		if(StringUtils.isNotBlank(request.getParameter("normalImages"))){
			map.put("normalImages", request.getParameter("normalImages"));
		}
		/*if(StringUtils.isNotBlank(request.getParameter("openid"))||(StringUtils.isNotBlank(request.getParameter("rgimages"))&&request.getParameter("rgimages").equalsIgnoreCase("true"))){
			//微信端同时保存检查报告图片和影像图片
			//检查报告图片
			map.put("checkReportImages", request.getParameter("checkReportImages"));
			//影像报告
			map.put("radiographFilmImags", request.getParameter("radiographFilmImags"));
		}*/
		if(StringUtils.isNotBlank(request.getParameter("askProblem"))){
			map.put("askProblem", request.getParameter("askProblem"));
		}
		if(StringUtils.isNotBlank(request.getParameter("description"))){
			map.put("description", request.getParameter("description"));
		}
		return map;
	}
}
