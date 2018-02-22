package com.tspeiz.modules.util.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tspeiz.modules.common.bean.BusinessTypeBean;

public class BusinessTypeGenerate {
	public static Map<String,Object> gainBusinessTypeByUserType(Integer utype){
		Map<String,Object> map=new HashMap<String,Object>();
		List<BusinessTypeBean> beans=new ArrayList<BusinessTypeBean>();
		if(utype.equals(2)){
			beans.add(new BusinessTypeBean("图文会诊",5));
			beans.add(new BusinessTypeBean("视频会诊",4));
			beans.add(new BusinessTypeBean("送心意",13));
			beans.add(new BusinessTypeBean("提现",-1));
		}else if(utype.equals(3)){
			beans.add(new BusinessTypeBean("图文咨询",6));
			beans.add(new BusinessTypeBean("电话咨询",7));
			beans.add(new BusinessTypeBean("快速问诊",9));
			beans.add(new BusinessTypeBean("团队咨询",12));
			beans.add(new BusinessTypeBean("视频会诊",4));
			beans.add(new BusinessTypeBean("图文会诊",5));
			beans.add(new BusinessTypeBean("送心意",13));
			beans.add(new BusinessTypeBean("提现",-1));
		}
		map.put("beans", beans);
		return map;
	}
}
