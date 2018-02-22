package com.tspeiz.modules.home.xy;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.release2.ThirdOrderInfo;
import com.tspeiz.modules.common.service.ICommonService;

@Controller
@RequestMapping(value="/modify")
public class ReceiveOrderStatusController {
	private Logger logger = Logger.getLogger(ReceiveOrderStatusController.class);
	@Autowired
	private ICommonService commonService;
	@RequestMapping(value = "/order/status",produces="text/plain;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public String receiveStatus(HttpServletRequest request) throws Exception{
        ServletInputStream in = request.getInputStream();
        String result = readLine(in);
        logger.info("===获取的推流数据："+result);
        JSONObject resultObj = JSONObject.fromObject(result);
        JSONObject dataObj = resultObj.getJSONObject("DATAS");
        String status = dataObj.getString("status");
        String conId = dataObj.getString("id");
        ThirdOrderInfo orderInfo = commonService.queryThirdOrderInfoByConsultationId(conId);
        orderInfo.setStatus(status);
        commonService.updateThirdOrderInfo(orderInfo);
        BusinessVedioOrder vedio = commonService.queryBusinessVedioOrderByUid(orderInfo.getOrderUuid());
        if("30".equalsIgnoreCase(status))
        	vedio.setStatus(20);
        if("50".equalsIgnoreCase(status))
        	vedio.setStatus(40);
        if("90".equalsIgnoreCase(status))
        	vedio.setStatus(50);
        commonService.updateBusinessVedioOrder(vedio);
        JSONObject retObj = new JSONObject();
        retObj.put("code", "00");
        retObj.put("message", "请求成功");
        return retObj.toString();
    }
	
	
	

    private static String readLine(ServletInputStream in) throws IOException {
       /* byte[] buf = new byte[8 * 1024];
        StringBuffer sbuf = new StringBuffer();
        int result;
        do {
            result = in.readLine(buf, 0, buf.length); // does +=
            if (result != -1) {
                sbuf.append(new String(buf, 0, result, "UTF-8"));
            }
        } while (result == buf.length); // loop only if the buffer was filled

        if (sbuf.length() == 0) {
            return null; // nothing read, must be at the end of stream
        }
        int len = sbuf.length();
        if (sbuf.charAt(len - 2) == '\r') {
            sbuf.setLength(len - 2); // cut \r\n
        } else {
            sbuf.setLength(len - 1); // cut \n
        }*/
    	
    	StringBuilder content = new StringBuilder();  
    	byte[] b = new byte[8*1024];  
    	int lens = -1;  
    	while ((lens = in.read(b)) > 0) {  
    	        content.append(new String(b, 0, lens,"UTF-8"));  
    	}  
        return content.toString();
    }
}
