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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.common.service.IWenzhenService;
import com.tspeiz.modules.util.common.MicWordUtil;
import com.tspeiz.modules.util.weixin.OutputWordsUtil;

@Controller
public class DownLoadsController {
	
	@Autowired
	private IWenzhenService wenzhenService;
	@Autowired
	private ICommonService commonService;
	/**
	 * 导出会诊申请报告到word文档
	 * 访问：http://localhost:8080/exportsWord/{oid}/{type}
	 * @param oid type  4远程  5图文 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * 
	 */
	@RequestMapping("/exportsWord/{oid}/{type}")
    public String exportsword(HttpServletRequest request,HttpServletResponse response,@PathVariable Integer oid,@PathVariable Integer type)
            throws Exception{
        request.setCharacterEncoding("utf-8");
        Map<String,Object> map = new HashMap<String,Object>();
        mapdata(oid,map,type);
        File file = null;
        InputStream fin = null;
        ServletOutputStream out = null;
        try{
        	file = MicWordUtil.createDoc(map, "apptemplate");
            fin = new FileInputStream(file);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");
            response.addHeader("Content-Disposition", "attachment;filename=huizhenshenqingdan.doc");
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
	 * 获取会诊申请报告数据
	 * 访问：http://localhost:8080/exports/getreportdatas
	 * @param oid type
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/exports/getreportdatas")
	public @ResponseBody
	Map<String,Object> getreportdatas(HttpServletRequest request) throws Exception{
		Integer oid=Integer.parseInt(request.getParameter("oid"));
		Integer type=Integer.parseInt(request.getParameter("type"));
		Map<String,Object> map=new HashMap<String,Object>();
		mapdata(oid,map,type);
		return map;
	}
	private void mapdata(Integer oid,Map<String,Object> map,Integer type) throws Exception{
		OutputWordsUtil.commonIf(wenzhenService, commonService, oid, map,type);
	}
}
