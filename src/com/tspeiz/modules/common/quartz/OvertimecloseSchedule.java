package com.tspeiz.modules.common.quartz;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.tspeiz.modules.common.bean.WenzhenBean;
import com.tspeiz.modules.common.entity.newrelease.BusinessWenZhenInfo;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.util.SpringUtil;

public class OvertimecloseSchedule {
	private static IWenzhenService wenzhenService;
	static {
		ApplicationContext ac = SpringUtil.getApplicationContext();
		wenzhenService = (IWenzhenService) ac.getBean("wenzhenService");
	}
	public void close(){
		//定时获取正在进行超过了48小时的图文订单
		List<WenzhenBean> list=wenzhenService.queryOverTimeTwOrders(3);
		if(list!=null&&list.size()>0){
			System.out.println("有："+list.size()+"条数据超时了，需要自动关闭");
			for (WenzhenBean wenzhenBean : list) {
				BusinessWenZhenInfo info=wenzhenService.queryBusinessWenZhenInfoById(wenzhenBean.getId());
				info.setAskStatus(13);
				wenzhenService.updateBusinessWenzhenInfo(info);
			}
		}
	}
}
