package com.tspeiz.modules.util.oss;

import com.tspeiz.modules.util.PropertiesUtil;

public class OSSConfigure {
	private String endpoint;//节点
	private String accessKeyId;
	private String accessKeySecret;
	private String bucketName;//创建目录
	private String accessUrl;
	private String accessUrls;
	private String vedioBucketName;
	private String outPutvedioBucketName;
	private String accessUrloutput;
	public OSSConfigure() {
		endpoint = PropertiesUtil.getString("oss_endpoint").trim();
		accessKeyId = PropertiesUtil.getString("oss_accessKeyId").trim();
		accessKeySecret = PropertiesUtil.getString("oss_accessKeySecret").trim();
		bucketName = PropertiesUtil.getString("oss_bucketName").trim();
		accessUrl = PropertiesUtil.getString("oss_accessUrl").trim();
		accessUrls = PropertiesUtil.getString("oss_accessUrls").trim();
		accessUrloutput = PropertiesUtil.getString("oss_accessUrl_output").trim();
		vedioBucketName =PropertiesUtil.getString("oss_vedioBucketName").trim();
		outPutvedioBucketName =PropertiesUtil.getString("oss_vediooutputBucketName").trim();
	}
	public OSSConfigure(String endpoint, String accessKeyId, String accessKeySecret, String bucketName,
			String accessUrl, String accessUrls, String vedioBucketName, String outPutvedioBucketName,
			String accessUrloutput) {
		super();
		this.endpoint = endpoint;
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
		this.bucketName = bucketName;
		this.accessUrl = accessUrl;
		this.accessUrls = accessUrls;
		this.vedioBucketName = vedioBucketName;
		this.outPutvedioBucketName = outPutvedioBucketName;
		this.accessUrloutput = accessUrloutput;
	}
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public String getAccessKeyId() {
		return accessKeyId;
	}
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	public String getAccessKeySecret() {
		return accessKeySecret;
	}
	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public String getAccessUrl() {
		return accessUrl;
	}
	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}
	public String getAccessUrls() {
		return accessUrls;
	}
	public void setAccessUrls(String accessUrls) {
		this.accessUrls = accessUrls;
	}
	public String getVedioBucketName() {
		return vedioBucketName;
	}
	public void setVedioBucketName(String vedioBucketName) {
		this.vedioBucketName = vedioBucketName;
	}
	public String getOutPutvedioBucketName() {
		return outPutvedioBucketName;
	}
	public void setOutPutvedioBucketName(String outPutvedioBucketName) {
		this.outPutvedioBucketName = outPutvedioBucketName;
	}
	public String getAccessUrloutput() {
		return accessUrloutput;
	}
	public void setAccessUrloutput(String accessUrloutput) {
		this.accessUrloutput = accessUrloutput;
	}
}
