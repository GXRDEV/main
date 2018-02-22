package com.tspeiz.modules.util;

import java.util.Properties;

import com.tspeiz.modules.util.config.ReaderConfigUtil;

public class PropertiesUtil {
	private static Properties prop=new Properties();
	private static Properties _prop = new Properties();
	static{
		try{
            prop.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("resconfig/resource.properties"));
            _prop.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("resconfig/resource_test.properties"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static String getString(String key){
		String resource = ReaderConfigUtil.gainConfigVal(gainXmlPath(), "basic.xml", "root/resource");
		if(resource.equalsIgnoreCase("server")){
			return prop.getProperty(key);
		}
		return _prop.getProperty(key);
	}
	
	public static Integer getInteger(String key){
		return Integer.parseInt(getString(key));
	}
	
	private static String gainXmlPath(){
		String path = PropertiesUtil.class.getClassLoader().getResource("resconfig/resource.properties").getPath(); 
		return path.substring(0, path.lastIndexOf("WEB-INF"));
	}
}
