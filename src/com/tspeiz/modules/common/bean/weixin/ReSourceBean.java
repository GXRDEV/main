package com.tspeiz.modules.common.bean.weixin;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ReSourceBean implements Serializable,Comparable{
	private static final long serialVersionUID = -929995428412452080L;
	private Integer id;
	private BigInteger count;
	private String name;
	private String remark;
	private Float cost;
	private Object key;
	private Object value;
	private String studyId;
	private String seriesId;
	private String instanceId;
	private Map<Object,Object> kvs;
	
	private List<ReSourceBean> beans;
	
	private String checkNo;
	private String checkSn;
	
	public ReSourceBean(){}
	
	public ReSourceBean(String key,Object value){
		this.key=key;
		this.value=value;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Float getCost() {
		return cost;
	}
	public void setCost(Float cost) {
		this.cost = cost;
	}
	public Object getKey() {
		return key;
	}
	public void setKey(Object key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Map<Object, Object> getKvs() {
		return kvs;
	}
	public void setKvs(Map<Object, Object> kvs) {
		this.kvs = kvs;
	}

	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public String getCheckSn() {
		return checkSn;
	}

	public void setCheckSn(String checkSn) {
		this.checkSn = checkSn;
	}

	public List<ReSourceBean> getBeans() {
		return beans;
	}

	public void setBeans(List<ReSourceBean> beans) {
		this.beans = beans;
	}

	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getStudyId() {
		return studyId;
	}

	public void setStudyId(String studyId) {
		this.studyId = studyId;
	}

	public String getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public BigInteger getCount() {
		return count;
	}

	public void setCount(BigInteger count) {
		this.count = count;
	}
}
