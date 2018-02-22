package com.tspeiz.modules.util.huaxin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class HttpSendSmsUtil {
	//使用StringBuffer的append获得xml形式的字符串
	private static StringBuffer sub=null;
	private static BufferedReader br=null;
	private static URL url=null;
	private static HttpURLConnection con;
	private static String line;
	private static String account="xd001061";
	private static String password="saosao00106101";
	
	public static void main(String[] args) throws Exception{
		//String content="已有患者预约成功。患者详细信息如下：\n姓名：张强\n性别: 男  \n手机号码：13552231705\n身份证：230882199003170221  \n预约信息如下：\n类型：专家会诊  \n预约时间：2016年6月30 9:30-9:40  \n专家：白文佩（北京世纪坛医院 妇科）\n请提前联系患者，辅助患者完成此次服务。【佰医汇】";
		//String newcontent = new String(content.getBytes("GBK"), "GBK");
		String content="您好，我想知道你大概什么时候能到达？【佰医汇】";
		String telephone = "13681473419";
		System.out.println(sendSmsInteface(telephone,content));
	}
	
	
	public static String sendSmsInteface(String telphone,String content){
		//判断短信是否还剩余
		String ret="";
		if(tellSmsLeft()){
			ret=sendSms(telphone,content);
			if(ret.equalsIgnoreCase("Success")){
				ret="100";
			}else{
				ret="101";
			}
		}
		return ret;
	}
	
	//判断是否还剩余短信
	private static boolean tellSmsLeft(){
		XmlEntity xmlentity=new XmlEntity();
		String xml=null;
		//查询调用方法
		xml=HttpSendSmsUtil.SelSum("1111", account, password).toString();
		System.out.println(xml);
		//赋值给xmlEntity实体类
		xmlentity.setReturnstatus("returnstatus");
		xmlentity.setMessage("message");
		xmlentity.setPayinfo("payinfo");
		xmlentity.setOverage("overage");
		xmlentity.setSendTotal("sendTotal");
		//调用XML字符串解析通用方法
		xmlentity=HttpSendSmsUtil.readStringXmlCommen(xmlentity, xml);
	    int zong=Integer.parseInt(xmlentity.getSendTotal());
	    int yong=Integer.parseInt(xmlentity.getOverage());
	    //int sheng=zong-yong;
	    if(yong>0){
	    	return true;
	    }
		return false;
	}
	
	private static String sendSms(String telphone,String content){
		XmlEntity xmlentity=new XmlEntity();
		String xml=null;
	    //发送调用
		xml=HttpSendSmsUtil.SendMessage("1111",account,password, telphone,content, "").toString();
        System.out.println(xml);
        xmlentity.setReturnstatus("returnstatus");
        xmlentity.setMessage("message");
        xmlentity.setRemainpoint("remainpoint");
        xmlentity.setTaskID("taskID");
        xmlentity.setSuccessCounts("successCounts");
        xmlentity=HttpSendSmsUtil.readStringXmlCommen(xmlentity, xml);
        System.out.println("状态"+xmlentity.getReturnstatus()+"返回信息"+xmlentity.getMessage()+"成功条数"+xmlentity.getSuccessCounts());
       	return xmlentity.getReturnstatus();
	}
	
	
	
	
	
	//查询余额
	private static StringBuffer SelSum(String userid,String account,String password) 
	{
		sub=new StringBuffer();
		try {
			url=new URL("http://dx.ipyy.net/sms.aspx/sms.aspx?action=overage&userid="+userid+"&account="+account+"&password="+password+"");	
			con = (HttpURLConnection)url.openConnection();
			//br=new BufferedReader(new InputStreamReader(url.openStream()));
		    br=new  BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8")); 
		    while((line=br.readLine())!=null)
		    {
		    	//追加字符串获得XML形式的字符串
		    	sub.append(line+"");
		    }
		    br.close();
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			return sub;
		}
	    
	}

	
	//发送短信
	private static StringBuffer SendMessage(String userid,String account,String password,String mobile,String content,String sendTime)
	{
		sub=new StringBuffer();
		try {
			//设置发送内容的编码方式
			String send_content=URLEncoder.encode(content.replaceAll("<br/>", " "), "UTF-8");//发送内容
			
			url=new URL("http://dx.ipyy.net/sms.aspx?action=send&userid="+userid+"&account="+account+"&password="+password+"&mobile="+mobile+"&content="+send_content+"&sendTime="+sendTime+"");	
			con = (HttpURLConnection)url.openConnection();
			
			br=new  BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
		    while((line=br.readLine())!=null)
		    {
		    	sub.append(line+"");
		    }
		    br.close();
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			return sub;
		}
	}

	//状态报告接口
	public StringBuffer StatusReport(String userid,String account,String password )
	{
		try {
			url=new URL("http://dx.ipyy.net/statusApi.aspx?action=query&userid="+userid+"&account="+account+"&password="+password+"");	
			con = (HttpURLConnection)url.openConnection();
			
			//br=new BufferedReader(new InputStreamReader(url.openStream()));
		   
			br=new  BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
		    
		    while((line=br.readLine())!=null)
		    {
		    	//追加字符串获得XML形式的字符串
		    	sub.append(line+"");
		    //	System.out.println("提取数据 :  "+line);
		    }
		    br.close();
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			return sub;
		}
	}
	
	
	//解析xml字符串
	public void readStringXml(String xml)
	{
		Document doc=null;
		
		try {
			//将字符转化为XML
			doc=DocumentHelper.parseText(xml);
			//获取根节点
			Element rootElt=doc.getRootElement();
			
			//拿到根节点的名称
			//System.out.println("根节点名称："+rootElt.getName());
			
			//获取根节点下的子节点的值
			String returnstatus=rootElt.elementText("returnstatus").trim();
			String message=rootElt.elementText("message").trim();
			String payinfo=rootElt.elementText("payinfo").trim();
			String overage=rootElt.elementText("overage").trim();
			String sendTotal=rootElt.elementText("sendTotal").trim();
			
			System.out.println("返回状态为："+returnstatus);
			System.out.println("返回信息提示："+message);
			System.out.println("返回支付方式："+payinfo);
			System.out.println("返回剩余短信条数："+overage);
			System.out.println("返回总条数："+sendTotal);
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	//XML字符串解析通用方法
	private static  XmlEntity readStringXmlCommen(XmlEntity xmlentity,String xml)
	{
		XmlEntity xe=new XmlEntity();
		Document doc=null;	
		try {
			//将字符转化为XML
			doc=DocumentHelper.parseText(xml);
			//获取根节点
			Element rootElt=doc.getRootElement();
			//拿到根节点的名称
			//System.out.println("根节点：" + rootElt.getName()); 
			
			//获取根节点下的子节点的值
			if(xmlentity.getReturnstatus()!=null)
			{
				xe.setReturnstatus(rootElt.elementText(xmlentity.getReturnstatus()).trim());
			}
			if(xmlentity.getMessage()!=null)
			{
				xe.setMessage(rootElt.elementText(xmlentity.getMessage()).trim());
			}
			if(xmlentity.getRemainpoint()!=null)
			{
				xe.setRemainpoint(rootElt.elementText(xmlentity.getRemainpoint()).trim());
			}
			if(xmlentity.getTaskID()!=null)
			{
				xe.setTaskID(rootElt.elementText(xmlentity.getTaskID()).trim());
			}
			if(xmlentity.getSuccessCounts()!=null)
			{
				xe.setSuccessCounts(rootElt.elementText(xmlentity.getSuccessCounts()).trim());
			}
			if(xmlentity.getPayinfo()!=null)
			{
				xe.setPayinfo(rootElt.elementText(xmlentity.getPayinfo()).trim());
			}
			if(xmlentity.getOverage()!=null)
			{
				xe.setOverage(rootElt.elementText(xmlentity.getOverage()).trim());
			}
			if(xmlentity.getSendTotal()!=null)
			{
				xe.setSendTotal(rootElt.elementText(xmlentity.getSendTotal()).trim());
			}
			//接收状态返回的报告
			if(rootElt.hasMixedContent()==false)
			{
				System.out.println("无返回状态！");
			}
			else
			{
				for (int i = 1; i <= rootElt.elements().size(); i++) {
					if(xmlentity.getStatusbox()!=null)
					{
						System.out.println("状态"+i+":");
						//获取根节点下的子节点statusbox
						 Iterator iter = rootElt.elementIterator(xmlentity.getStatusbox()); 
						// 遍历statusbox节点 
						 while(iter.hasNext())
						 {
							 Element recordEle = (Element) iter.next();
							 xe.setMobile(recordEle.elementText("mobile").trim());
							 xe.setTaskid(recordEle.elementText("taskid").trim());
							 xe.setStatus(recordEle.elementText("status").trim());
							 xe.setReceivetime(recordEle.elementText("receivetime").trim());
							 System.out.println("对应手机号："+xe.getMobile());
							 System.out.println("同一批任务ID："+xe.getTaskid());
							 System.out.println("状态报告----10：发送成功，20：发送失败："+xe.getStatus());
							 System.out.println("接收时间："+xe.getReceivetime());
						 }	 
					 }
					
				}

			}
			
			//错误返回的报告
			if(xmlentity.getErrorstatus()!=null)
			{
				//获取根节点下的子节点errorstatus
				 Iterator itererr = rootElt.elementIterator(xmlentity.getErrorstatus()); 
				// 遍历errorstatus节点
	            while(itererr.hasNext())
	            {
	            	Element recordElerr = (Element) itererr.next();
	            	xe.setError(recordElerr.elementText("error").trim());
	            	xe.setRemark(recordElerr.elementText("remark").trim());
	            	System.out.println("错误代码："+xe.getError());
	            	System.out.println("错误描述："+xe.getRemark());
	            }
			}
			
//			if(xmlentity.getCallbox()!=null)
//			{
//				//获取根节点下的子节点errorstatus
//				Iterator itercallbox = rootElt.elementIterator("errorstatus"); 
//				// 遍历errorstatus节点
//				while(itercallbox.hasNext())
//				{
//					Element recordcallbox = (Element) itercallbox.next();
//					String content=recordcallbox.elementText("content").trim();
//					String receivetime=recordcallbox.elementText("receivetime").trim();
//					String mobile=recordcallbox.elementText("mobile").trim();
//					String taskid=recordcallbox.elementText("taskid").trim();
//					
//				}
//			}
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return xe;
	}
	/**
	 * 		//状态报告
//		xml=t.StatusReport("1111", "qqqq", "mima").toString();
//		System.out.println(xml);
//		xmlentity.setStatusbox("statusbox");
//		xmlentity.setMobile("mobile");
//		xmlentity.setTaskid("taskid");
//		xmlentity.setStatus("status");
//		xmlentity.setReceivetime("receivetime");
//		xmlentity.setErrorstatus("errorstatus");
//		xmlentity.setError("error");
//		xmlentity.setRemark("remark");
//		xmlentity=t.readStringXmlCommen(xmlentity, xml);
//		System.out.println("对应手机号："+xmlentity.getMobile()+"对应状态"+xmlentity.getStatus()+"对应接收时间"+xmlentity.getReceivetime());
//		System.out.println("错误代码："+xmlentity.getError());
	    
	 */
	
}
