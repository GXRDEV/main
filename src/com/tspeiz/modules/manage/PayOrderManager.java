package com.tspeiz.modules.manage;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.aspectj.bridge.ICommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tspeiz.modules.common.entity.newrelease.BusinessPayInfo;
import com.tspeiz.modules.common.service.ICommonService;

@Service
public class PayOrderManager {
	@Autowired
	private ICommonService commonService;
	
	/**
	 * 
	 * @param ordertype  订单类型
	 * @param orderid   订单id
	 * @param outTradeNo 交易号
	 * @param totalMoney 总金额
	 * @param payMode   支付方式
	 * @param onlinePay 在线支付
	 * @param accountPay  余额支付
	 * @param pointsPay  积分支付
	 * @param couponPay  优惠券支付
	 * @param couponId  优惠券id
	 */
	public Integer savePayInfo(Integer ordertype,Integer orderid,String outTradeNo,Float totalMoney,Integer payMode,Float onlinePay,Float accountPay,Float pointsPay,Float couponPay,Integer couponId){
		BusinessPayInfo payinfo=new BusinessPayInfo();
		payinfo.setOrderType(ordertype);
		payinfo.setOrderId(orderid);
		payinfo.setOutTradeNo(outTradeNo);
		payinfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
		payinfo.setTotalMoney(new BigDecimal(totalMoney));
		payinfo.setPayMode(payMode);
		payinfo.setOnlinePay(new BigDecimal(onlinePay));
		payinfo.setAccountPay(new BigDecimal(accountPay));
		payinfo.setPointsPay(new BigDecimal(pointsPay));
		payinfo.setCouponPay(new BigDecimal(couponPay));
		payinfo.setPayStatus(4);//待付款
		payinfo.setCouponId(couponId);
		Integer pid=commonService.saveBusinessPayInfo(payinfo);
		return pid;
	}
}
