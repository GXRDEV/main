package com.tspeiz.modules.util.oss;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.mts.model.v20140618.SubmitJobsRequest;
import com.aliyuncs.mts.model.v20140618.SubmitJobsResponse;
import com.aliyuncs.mts.model.v20140618.SubmitJobsResponse.JobResult;
import com.aliyuncs.profile.DefaultProfile;

public class VedioTransUtil {
	 
	public static JobResult transferMedia(String inputObject,String inputBucket,String outputObject,String outputBucket) throws Exception{
		DefaultProfile.addEndpoint("cn-beijing",
                "cn-beijing",
                "Mts",
                "mts.cn-beijing.aliyuncs.com");
		String region = "cn-beijing";
		String accessKeyId = "LTAI5cV9sG8ybuMT";
		String accessKeySecret = "qW11rXcBGktOYpjQcsv7SzEEJUwIPa";
	    String pipelineId = "18d914bdbc534d81a572cc3944f1b058";
	    String ossLocation = "oss-cn-beijing";
	    String transcodeTemplateId = "cad009bc3da1979c09494efd2a18ec63";
	    DefaultProfile profile = DefaultProfile.getProfile(region, accessKeyId, accessKeySecret);
	    DefaultAcsClient client = new DefaultAcsClient(profile);
	    SubmitJobsRequest request = new SubmitJobsRequest();
	    request.setPipelineId(pipelineId);
	    JSONObject input = new JSONObject();
	    input.put("Location", ossLocation);
	    input.put("Bucket", inputBucket);
	    input.put("Object", inputObject);
	    request.setInput(input.toJSONString());
	    JSONArray outputs = new JSONArray();
	    JSONObject outputConfigJson = new JSONObject();
	    outputConfigJson.put("TemplateId", transcodeTemplateId);
	    outputConfigJson.put("OutputObject", outputObject);
	    outputs.add(outputConfigJson);
	    request.setOutputs(outputs.toJSONString());
	    request.setOutputLocation(ossLocation);
	    request.setOutputBucket(outputBucket);
	    SubmitJobsResponse response=client.getAcsResponse(request);
	    SubmitJobsResponse.JobResult jobResult = response.getJobResultList().get(0);
	    if(jobResult.getSuccess()){
	        System.out.println(String.format("SubmitJob Success, JobId=%s", jobResult.getJob().getJobId()));
	    }else {
	        System.out.println(String.format("SubmitJob Failed, RequestId=%s;Code=%s;Message=%s",
	            response.getRequestId(), jobResult.getCode(), jobResult.getMessage()));
	    }
	    return jobResult;
	}
}
