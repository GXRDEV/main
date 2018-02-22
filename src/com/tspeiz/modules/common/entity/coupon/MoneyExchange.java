package com.tspeiz.modules.common.entity.coupon;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 兑换码表
 * @author heyongb
 *
 */
@Entity
@Table(name="money_exchange")
public class MoneyExchange implements Serializable{
	private static final long serialVersionUID = -2134659075360099983L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="CharCode",length=10)
	private String charCode;//兑换码
	
	@Column(name="Money")
	private BigDecimal money;//金额
	
	@Column(name="UseOrNot",length=1)
	private Integer useOrNot=0;//0--未使用，1已使用
	
	@Column(name="DeadLine",length=20)
	private String deadLine;//截止日期
	
	@Column(name="MoneyType",length=1)
	private Integer moneyType;//1--兑换码，2-代金券
	
	@Column(name="CreateTime",length=20)
	private String createTime;//创建时间
	
	@Column(name="Status",length=1)
	private Integer status;//状态  1，有效，0-失效

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCharCode() {
		return charCode;
	}

	public void setCharCode(String charCode) {
		this.charCode = charCode;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getUseOrNot() {
		return useOrNot;
	}

	public void setUseOrNot(Integer useOrNot) {
		this.useOrNot = useOrNot;
	}

	public String getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
	}

	public Integer getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(Integer moneyType) {
		this.moneyType = moneyType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
