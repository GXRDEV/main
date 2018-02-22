package com.tspeiz.modules.util.common;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.context.ApplicationContext;

import com.tspeiz.modules.common.entity.newrelease.UserBillRecord;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.util.SpringUtil;

public class GeneUserBillRecordUtil {
	private static IWenzhenService wenzhenService;
	static {
		ApplicationContext ac = SpringUtil.getApplicationContext();
		wenzhenService = (IWenzhenService) ac.getBean("wenzhenService");
	}
	
	public static void geneBill(Integer oid,Integer uid,Integer docid,BigDecimal money,String opAction,String content,String remark){
		UserBillRecord bill=new UserBillRecord();
		bill.setUserId(uid);
		bill.setDoctorId(docid);
		bill.setOrderId(oid);
		bill.setMoney(money);
		bill.setOpAction(opAction);
		bill.setOpTime(new Date());
		bill.setContent(content);
		bill.setRemark(remark);
		wenzhenService.saveUserBillRecord(bill);
	}
}
