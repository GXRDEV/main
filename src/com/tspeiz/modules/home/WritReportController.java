package com.tspeiz.modules.home;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.util.common.FileConvertUtils;
import com.tspeiz.modules.util.common.MicWordUtil;
import com.tspeiz.modules.util.weixin.WriteReportsUtil;

@Controller
public class WritReportController {
	@Autowired
	private IWenzhenService wenzhenService;
	@Autowired
	private ICommonService commonService;

	/**
	 * 会诊报告单填写
	 * 访问：http://localhost:8080/writereport/{oid}/{otype}
	 * 视频 4  专家咨询报告5
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/writereport/{oid}/{otype}")
    public String writereport(HttpServletRequest request,HttpServletResponse response,@PathVariable Integer oid,@PathVariable Integer otype)
            throws Exception{
        request.setCharacterEncoding("utf-8");
        Map<String,Object> map = new HashMap<String,Object>();
        mapdata(oid,map,otype);
        File file = null;
        InputStream fin = null;
        ServletOutputStream out = null;
        try{
        	file = MicWordUtil.createDoc(map, "writetemplate");
            fin = new FileInputStream(file);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");
            response.addHeader("Content-Disposition", "attachment;filename=baogaotianxie.doc");
            out = response.getOutputStream();
            byte[] buffer = new byte[1024];//缓冲区
            int bytesToRead = -1;
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            if(fin != null) fin.close();  
            if(out != null) out.close();  
            if(file != null) file.delete(); // 删除临时文件  
        }
        return null;
    }
	
	public static void main(String[] args) {
		System.out.println();
	}
	/**
	 * 获取报告数据
	 * 访问：http://localhost:8080/export/gainreportdata
	 * 参数：oid,otype
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/export/gainreportdata")
	public @ResponseBody
	Map<String,Object> gainreportdata(HttpServletRequest request) throws Exception{
		Integer oid=Integer.parseInt(request.getParameter("oid"));
		Integer otype=Integer.parseInt(request.getParameter("otype"));
		Map<String,Object> map=new HashMap<String,Object>();
		mapdata(oid,map, otype);
		return map;
	}
	private void mapdata(Integer oid,Map<String,Object> map,Integer otype) throws Exception{
		WriteReportsUtil.commonIf(wenzhenService, commonService, oid, map,otype);
	}
	

}
