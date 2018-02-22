<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//通过request["code"]得到code，通过code得到用户的基本信息
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>支付订单</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
    
</head>
<body>
	<div id="index">		
		<p style="color:#ED4046;font-size:11px;padding:13px;line-height:1.8em;border:1px solid #ccc;background-color:#f5f5f5;border-radius:4px;margin:13px;">
			说明：费用包含预约专家、挂号费，以及护士陪诊费用。在医院发生的检查、治疗费用等均需要顾客自己额外承担。
		</p>
		<div style="margin:1px 12px;">
			<button type="button" name="btnok" onclick="callpay()" class="buttonClassgreen" style="border-radius:2px;height:46px;" id="btnsubmit">
					微信支付(<fmt:formatNumber pattern="###.##">${order.fixedPrice}</fmt:formatNumber>元)
			</button>
		</div>
	</div>
	<form>
		<input type="hidden" value="${appid}" id="appid"/>
		<input type="hidden" value="${timeStamp}" id="timeStamp"/>
		<input type="hidden" value="${nonceStr}" id="nonceStr"/>
		<input type="hidden" value="${package}" id="package"/>
		<input type="hidden" value="${sign}" id="sign"/>
	</form>
   <script src="/libs/jquery-1.11.0.min.js"></script>
   <script type="text/javascript">
		function jsApiCall()
		{
			WeixinJSBridge.invoke(
				'getBrandWCPayRequest',
				{
					"appId":$('#appid').val(),
					"timeStamp":$('#timeStamp').val(),
					"nonceStr":$('#nonceStr').val(),
					"package":$('#package').val(),
					"signType":"MD5",
					"paySign":$('#sign').val(),
				},
				function(res){
					if(res.err_msg.indexOf(':ok') > -1){
						location.replace("/wtspz/paysuccess.do");
					}
					(res.err_msg.indexOf(':cancel') > -1) && alert('用户已取消');
					(res.err_msg.indexOf(':fail') > -1) && alert('失败');
				}
			);
		}
		function callpay()
		{
            if(!checkWX()) return false;
            
			if (typeof WeixinJSBridge == "undefined"){
			    if( document.addEventListener ){
			        document.addEventListener('WeixinJSBridgeReady', jsApiCall, false);
			    }else if (document.attachEvent){
			        document.attachEvent('WeixinJSBridgeReady', jsApiCall); 
			        document.attachEvent('onWeixinJSBridgeReady', jsApiCall);
			    }
			}else{
				jsApiCall();
			}
		}
        function checkWX(){
	        var wechatInfo = navigator.userAgent.match(/MicroMessenger\/([\d\.]+)/i) ;
			if( !wechatInfo ) {
			    alert("仅支持在微信里面打开") ;
			    return false;
			} else if ( wechatInfo[1] < "5.0" ) {
			    alert('您的微信版本（V '+ (wechatInfo[1] || '4.0') +'）比较低，不支持微信支付。请升级您的微信。') ;
			    return false;
			}
			return true;
        }
   </script>
    
</body>
</html>