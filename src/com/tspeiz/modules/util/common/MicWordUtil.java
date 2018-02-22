package com.tspeiz.modules.util.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import sun.misc.BASE64Encoder;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class MicWordUtil {
	private static Configuration configuration = null;
    private static Map<String,Template> allTemplate = null;

    static{
        configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8");
        configuration.setClassForTemplateLoading(MicWordUtil.class, "/");

        allTemplate = new HashMap<String,Template>();
        try{
            //allTemplate.put("template", configuration.getTemplate("ex_export.ftl"));
        	allTemplate.put("template", configuration.getTemplate("ex_reports.ftl"));
            allTemplate.put("apptemplate",configuration.getTemplate("ex_prots.ftl"));
            allTemplate.put("writetemplate",configuration.getTemplate("exs_report.ftl"));
        }catch(IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static File createDoc(Map<?,?> dataMap,String type){
        String name = "temp"+(int)(Math.random()*100000)+".doc";
        File f = new File(name);
        Template t = allTemplate.get(type);
        try{
            Writer w = new OutputStreamWriter(new FileOutputStream(f),"utf-8");
            t.process(dataMap,w);
            w.close();
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return f;
    }
    
    public static String getBase64Str(String url) throws Exception{
    	InputStream in = saveToFile(url);
        String str = getImageStrByInPut(in);
        return str;
    }
    
    private static HttpURLConnection httpUrl = null;
    public static InputStream saveToFile(String destUrl){  
        
        URL url = null;  
        InputStream in = null;   
        try{  
            url = new URL(destUrl);  
            httpUrl = (HttpURLConnection) url.openConnection();  
            httpUrl.connect();  
            httpUrl.getInputStream();  
            in = httpUrl.getInputStream();            
            return in;  
        }catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
    /** 
     * 读取输入流,转换为Base64字符串 
     * @param input 
     * @return 
     */  
    public static String getImageStrByInPut(InputStream input) throws Exception{  
    	ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        //创建一个Buffer字符串  
        byte[] buffer = new byte[1024];  
        //每次读取的字符串长度，如果为-1，代表全部读取完毕  
        int len = 0;  
        //使用一个输入流从buffer里把数据读取出来  
        while( (len=input.read(buffer)) != -1 ){  
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
            outStream.write(buffer, 0, len);  
        }  
        // 对字节数组Base64编码  
        return new BASE64Encoder().encode(outStream.toByteArray()); 
    }  
}
