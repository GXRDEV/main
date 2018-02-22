package com.tspeiz.modules.common.bean;

import java.util.List;

/**
 * 辅助类
 * @author heyongb
 *
 */
public class HelpBean {
	private String distCode;
	
	private String distName;
	
	private List<HelpBean> citys;

	public String getDistCode() {
		return distCode;
	}

	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	public String getDistName() {
		return distName;
	}

	public void setDistName(String distName) {
		this.distName = distName;
	}

	public List<HelpBean> getCitys() {
		return citys;
	}

	public void setCitys(List<HelpBean> citys) {
		this.citys = citys;
	}
}
