package com.tspeiz.modules.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.HashMap;

import com.tspeiz.modules.common.entity.release2.OperatorInvitCode;
import com.tspeiz.modules.util.CheckNumUtil;
import com.tspeiz.modules.util.PropertiesUtil;
import com.tspeiz.modules.util.word.RichHtmlHandler;
import com.tspeiz.modules.util.word.WordGeneratorWithFreemarker;

public class Test {
	public static void main(String[] args) throws Exception {
		String str = "http://www.ebaiyihui.com/propagate/doctordetail/13570?openid=o6g9twoCW2UuLmKRJVj4UNRcgI_M";
		str = str.replace("http", "https");
		System.out.println(str);
		/*HashMap<String, Object> data = new HashMap<String, Object>();

		StringBuilder sb = new StringBuilder();
		sb.append("<div>");
		sb.append("<img style='height:100px;width:200px;display:block;' src='D:\\diagram.png' />");
		sb.append("</br><span>�й��Σ��Ҹ��Σ�</span>");
		sb.append("</div>");
		RichHtmlHandler handler = new RichHtmlHandler(sb.toString());

		handler.setDocSrcLocationPrex("file:///C:/268BA2D4");
		handler.setDocSrcParent("test.files");
		handler.setNextPartId("01D2E527.312FD7F0");
		handler.setShapeidPrex("_x56fe__x7247__x0020");
		handler.setSpidPrex("_x0000_i");
		handler.setTypeid("#_x0000_t75");

		handler.handledHtml(false);

		String bodyBlock = handler.getHandledDocBodyBlock();
		System.out.println("bodyBlock:\n"+bodyBlock);

		String handledBase64Block = "";
		if (handler.getDocBase64BlockResults() != null
				&& handler.getDocBase64BlockResults().size() > 0) {
			for (String item : handler.getDocBase64BlockResults()) {
				handledBase64Block += item + "\n";
			}
		}
		data.put("imagesBase64String", handledBase64Block);

		String xmlimaHref = "";
		if (handler.getXmlImgRefs() != null
				&& handler.getXmlImgRefs().size() > 0) {
			for (String item : handler.getXmlImgRefs()) {
				xmlimaHref += item + "\n";
			}
		}
		data.put("imagesXmlHrefString", xmlimaHref);
		data.put("name", "����");
		data.put("content", bodyBlock);

		String docFilePath = "d:\\temp.doc";
		System.out.println(docFilePath);
		File f = new File(docFilePath);
		OutputStream out;
		try {
			out = new FileOutputStream(f);
			WordGeneratorWithFreemarker.createDoc(data, "export.ftl", out);

		} catch (FileNotFoundException e) {

		}catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
}