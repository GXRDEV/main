package com.tspeiz.modules.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class ReaderConfigUtil {
	public static String gainConfigVal(HttpServletRequest request,
			String filename, String nodename) throws Exception {
		String xmlpath = request.getSession().getServletContext()
				.getRealPath("/config")
				+ File.separator + filename;
		SAXReader reader = new SAXReader();
		Document doc;
		InputStream in = new FileInputStream(xmlpath);
		String retval = "";
		try {
			doc = reader.read(in);
			Node node = doc.selectSingleNode("/" + nodename);
			retval = node.getText();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return retval;
	}
	public static String gainConfigVal(String path,
			String filename, String nodename){
		String xmlpath = path+"config"+File.separator
				+ File.separator + filename;
		SAXReader reader = new SAXReader();
		Document doc;
		String retval = "";
		try {
			InputStream in = new FileInputStream(xmlpath);
			doc = reader.read(in);
			Node node = doc.selectSingleNode("/" + nodename);
			retval = node.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retval;
	}

	public static Node gainConfigNode(String filename, String nodename)
			throws Exception {
		SAXReader reader = new SAXReader();
		Document doc;
		InputStream in = new FileInputStream(filename);
		Node node = null;
		try {
			doc = reader.read(in);
			node = doc.selectSingleNode("/" + nodename);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return node;
	}

	public static String gainConfigVal(String filename, String nodename)
			throws Exception {
		SAXReader reader = new SAXReader();
		Document doc;
		InputStream in = new FileInputStream(filename);
		String retval = "";
		try {
			doc = reader.read(in);
			Node node = doc.selectSingleNode("/" + nodename);
			retval = node.getText();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return retval;
	}

	@SuppressWarnings("unchecked")
	public static List<Element> gainNodeParamElements(Node root,
			String childNode) throws Exception {
		Node params = root.selectSingleNode(childNode);
		Element ele = (Element) params;
		return ele.elements();
	}
}
