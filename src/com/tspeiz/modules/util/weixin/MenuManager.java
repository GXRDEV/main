package com.tspeiz.modules.util.weixin;

import java.net.URLEncoder;

import com.tspeiz.modules.common.bean.weixin.AccessToken;
import com.tspeiz.modules.common.bean.weixin.pojo.Button;
import com.tspeiz.modules.common.bean.weixin.pojo.CommonButton;
import com.tspeiz.modules.common.bean.weixin.pojo.ComplexButton;
import com.tspeiz.modules.common.bean.weixin.pojo.Menu;
import com.tspeiz.modules.util.PropertiesUtil;



public class MenuManager {
	private static String domain="http://develop.ebaiyihui.com/";
	static {
		
	}
	public static void main(String[] args) {  
        // 第三方用户唯一凭证  
        String appId = PropertiesUtil.getString("APPID");
        // 第三方用户唯一凭证密钥  
        String appSecret = PropertiesUtil.getString("APPSECRET");
        // 调用接口获取access_token  
        AccessToken at = CommonUtil.getAccessToken(appId, appSecret);  
        if (null != at) {  
            // 调用接口创建菜单  
            int result = CommonUtil.createMenu(getMenu(), at.getToken());  
  
            // 判断菜单创建结果  
            if (0 == result)  
                System.out.println("菜单创建成功！");  
            else  
            	 System.out.println("菜单创建失败，错误码：" + result);  
        }  
    }
	/*public static void main(String[] args) {
		//String paymentAdvice="http://www.taozis.com/home/campaign/29"
		String main=domain+"wzjh/main";//进入主界面
		main=URLEncoder.encode(main);
		System.out.println("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2abcfb2b545f21c3&redirect_uri="+main+"?response_type=code&scope=snsapi_base&state=123#wechat_redirect");
	}*/
  
	@SuppressWarnings("deprecation")
	private static Menu getMenu() {  
		String main=domain+"wzjh/main";//进入主界面
		//String myorder=domain+"wzjh/myorders";//我的订单
		String center=domain+"module/user.html#/center";//个人中心
		String userfeed=domain+"wzjh/feedbacks";//用户反馈
		String mycase=domain+"wzjh/mycases";//我的病例
		String specials=domain+"propagate/intodoclist";
		//String remote_sub=domain+"wzjh/remconsulsub";//远程会诊
		String remote_sub=domain+"module/remote.html#/main";//远程会诊
		String tw=domain+"wzjh/onlineask/1";//图文问诊
		String tel=domain+"wzjh/onlineask/2";//电话问诊
		main=URLEncoder.encode(main);
		userfeed=URLEncoder.encode(userfeed);
		center=URLEncoder.encode(center);
		//myorder=URLEncoder.encode(myorder);
		mycase=URLEncoder.encode(mycase);
		specials=URLEncoder.encode(specials);
		remote_sub=URLEncoder.encode(remote_sub);
		tw=URLEncoder.encode(tw);
		tel=URLEncoder.encode(tel);
		String pre="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2abcfb2b545f21c3&redirect_uri=";
		String aft="?response_type=code&scope=snsapi_base&state=123#wechat_redirect";
		
		ComplexButton zaixian = new ComplexButton();//在线服务
		zaixian.setName("在线服务");
		
	    CommonButton _btn11 = new CommonButton();  
	    _btn11.setName("远程会诊");  
	    _btn11.setType("view");  
	    _btn11.setUrl(pre+remote_sub+aft);

	    CommonButton btn12 = new CommonButton();  
	    btn12.setName("专家库");  
	    btn12.setType("view");  
	    btn12.setUrl(pre+specials+aft);
	    
	    CommonButton btn14 = new CommonButton();  
	    btn14.setName("图文问诊");  
	    btn14.setType("view");  
	    btn14.setUrl(pre+tw+aft);
	    
	    CommonButton btn15 = new CommonButton();  
	    btn15.setName("电话问诊");  
	    btn15.setType("view");  
	    btn15.setUrl(pre+tel+aft);
	    
	    zaixian.setSub_button(new Button[]{btn14,btn15,_btn11,btn12});

	    CommonButton more2=new CommonButton(); 
        more2.setName("关于我们");
        more2.setType("view");
        more2.setUrl(domain+"wzjh/aboutus");
        
        CommonButton more3=new CommonButton();
        more3.setName("常见问题");
        more3.setType("view");
        more3.setUrl(domain+"wzjh/qas");
        
        
        CommonButton more5=new CommonButton();
        more5.setName("用户反馈");
        more5.setType("view");
        more5.setUrl(pre+userfeed+aft);
       
        ComplexButton morebtn = new ComplexButton();
        morebtn.setName("更多"); 
        morebtn.setSub_button(new Button[]{more2,more3,more5});

        CommonButton mycenter = new CommonButton();  
        mycenter.setName("个人中心");  
        mycenter.setType("view");  
        mycenter.setUrl(pre+center+aft);
	    
        Menu menu = new Menu();  
		menu.setButton(new Button[] {zaixian,mycenter,morebtn});
        return menu;  
    }
	
}
