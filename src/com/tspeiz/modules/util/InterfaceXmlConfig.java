package com.tspeiz.modules.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class InterfaceXmlConfig {
	public static String filename="C:\\apache-tomcat-7.0.63\\webapps\\tspeiz\\hospitalXML\\六安医院.xml";
	private static Document document=null;
	static{
		SAXReader saxReader = new SAXReader();
		try {
			document = saxReader.read(new File(filename));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//获取基本配置
	private static Map<String,Object> gainBasicMap(Element root){
		Map<String,Object> map=new HashMap<String,Object>();
		Element basic=root.element("basic");
		map.put("visit_ip", basic.elementTextTrim("visit"));//访问ip
		map.put("appid", basic.elementTextTrim("appid"));//appid
		map.put("version", basic.elementTextTrim("version"));//版本
		map.put("mongon_lis", basic.elementTextTrim("mongon_lis"));//lis表
		map.put("mongon_pacs", basic.elementTextTrim("mongon_pacs"));//pacs表
		return map;
	}
	//获取注册挂号配置---只支持二层循环
	@SuppressWarnings("unchecked")
	private static String gainDataString(String nodeName){
		Element root = document.getRootElement();
		Element node=root.element(nodeName);
		JSONObject jo=new JSONObject();
		List<Element> childrens=node.elements();
		if(childrens!=null&&childrens.size()>0){
			for (Element ele : childrens) {
				Attribute haschild_att=ele.attribute("haschild");
				if(haschild_att!=null&&haschild_att.getText().equalsIgnoreCase("true")){
					List<Element> _childrens=ele.elements();
					if(_childrens!=null&&_childrens.size()>0){
						JSONObject _jo=new JSONObject();
						for (Element _ele : _childrens) {
							_jo.put(_ele.getName(), _ele.getTextTrim());
						}
						jo.put(ele.getName(), _jo.toString());
					}
				}else{
					jo.put(ele.getName(), ele.getTextTrim());
				}
			}
		}
		return jo.toString();
	}

	public static void main(String[] args) throws Exception {
		String register_data=gainDataString("register");
		/*register_data=register_data.replace("${dept_id}", "11231");
		register_data=register_data.replace("${name}", "heyonbg");
		register_data=register_data.replace("${id_card}", "362502198902051414");
		register_data=register_data.replace("${telephone}", "13681473419");
		register_data=register_data.replace("${type_name}", "普通挂号");*/
		System.out.println(register_data);
		System.out.println("=============================");
		System.out.println(gainDataString("records"));
	}
}
