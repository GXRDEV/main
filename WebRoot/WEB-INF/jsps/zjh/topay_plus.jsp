<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML>
<html>
  <head lang="en">
    <base href="/">
    <meta charset="utf-8">
	<meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" name="viewport">
	<meta content="yes" name="apple-mobile-web-app-capable">
	<meta content="black" name="apple-mobile-web-app-status-bar-style">
	<meta content="telephone=no" name="format-detection">
	<meta content="email=no" name="format-detection">
	<title>加号支付</title>
	<link rel="stylesheet" type="text/css" href="/css/mobile.css?v=1.0" />
	<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
	<style>	
		body{background-color:#EFEFEF;}    	
		.basecss{border:1px solid #E1E0DE;margin:10px 0;background-color:#fff;overflow:hidden;border-left:0;border-right:0;}
		.color1{color:#30AA91;}
		.righttxt{text-align:right;}
		.mianinfo .controls{position:relative;display:block;}
		.controls{margin:0 0 0 10px;line-height:4em;color:#999;font-size:14px;}
		.controls + .controls{border-top:1px solid #E8E8E8;}
		.controls span{display:block;}
		.controls .clabel{position:absolute;left:0;top:50%;margin-top:-2em; text-align:right;color:#555;}
		.controls .contorl{margin-left:5.6em;}
		.controls .contorl input,
		.controls .contorl select{width:100%;height:2em;border:0;outline:0;}
		.zfinfo .cols0{color:#555;}
		.zfinfo .iconfont{font-size:22px;margin-right:6px;vertical-align: -1px;}
		.select{position:relative;}
		.select .select_input{position:relative; margin-right:40px;z-index:3;}
		.select .select_select{position:absolute;left:0;top:0;height:100%;width:100%;z-index:2;background:transparent;}
		.select .select_select:after{content:"";position:absolute;right:13px;top:50%;margin-top:-9px;width:10px;height:10px;border:1px solid #999;
			transform:rotate(-135deg);-webkit-transform:rotate(-135deg);border-right:0;border-bottom:0;z-index:1;
		}
		.select .select_select select{opacity:0;}
		
		.g_fixed {background-color:#EFEFEF;z-index: 10;}
		.g_fixed .cols0{display:block;margin: 6px 10px;}
		.g_fixed .btn2{border:0;background:#00CC99 url();color:#fff;font-weight:600;font-size: 16px;width:100%;height:40px}
		.g_fixed .btn2 span{color:#FEFD65;}
	</style>
  </head>  
  <body>
    <div class="index">
		<form action="" method="post" id="postorder" name="postorder">
			<div class="mianinfo basecss">
				<label class="controls">
					<span class="clabel">服务医生：</span>
					<span class="contorl"><b class="color1">${special.specialName}</b><small style="font-size:0.7em;">（${special.depName}）</small></span>
				</label>
				<label class="controls">
					<span class="clabel">支付金额：</span>
					<span class="contorl"><b class="color1"><fmt:formatNumber type="number" value="${sa.amount}" maxFractionDigits="2"/></b></span>
				</label>
			</div>
			<div class="zfinfo basecss">
				<label class="controls box" id="btnbuy" onclick="javascript:void(0)">
					<span class="cols0"><i class="iconfont" style="color:#49BA5E">&#xe614;</i>微信支付</span>
					<span class="cols1 righttxt"><i class="iconfont">&#xe60a;</i></span>
				</label>
				<!--<label class="controls box" onclick="javascript:void(0)">
					<span class="cols0"><i class="iconfont" style="color:#F12F76">&#xe613;</i>支付宝支付</span>
					<span class="cols1 righttxt"><i class="iconfont">&#xe60a;</i></span>
				</label>
				<label class="controls box" onclick="javascript:void(0)">
					<span class="cols0"><i class="iconfont" style="color:#13B5FF">&#xe615;</i>银行卡支付</span>
					<span class="cols1 righttxt"><i class="iconfont">&#xe60a;</i></span>
				</label>
			--></div><!--
		    <div class="g_fixed">
		    	<span class="cols0">
		    		<button type="button" class="btn2" id="btnbuy"><span>(<fmt:formatNumber type="number" value="${sa.amount}" maxFractionDigits="2"/>元)</span>微信支付</button>
		    	</span>
		    </div>
		--></form>	
    </div>
		<input type="hidden"  id="appid"/>
		<input type="hidden"  id="timeStamp"/>
		<input type="hidden"  id="nonceStr"/>
		<input type="hidden"  id="package"/>
		<input type="hidden"  id="sign"/>
	<script src="/js/jquery-1.11.0.min.js"></script>
	<script src="/js/base.js"></script>
	 <script>
		var _b = {
			href:'/',
			_post:function(url,ops,fun,err){
				return _$(this.href + url,ops,fun,err);
			},
			dia:new _dialog(),
			valideTel: function(text) {
		        var _emp = /^\s*|\s*$/g;
		        text = text.replace(_emp, "");
		        var _d = /^1[3578][01379]\d{8}$/g;
		        var _l = /^1[34578][01256]\d{8}$/g;
		        var _y = /^(134[012345678]\d{7}|1[34578][012356789]\d{8})$/g;
		        if (_d.test(text)) {
		            return true;
		        } else if (_l.test(text)) {
		            return true;
		        } else if (_y.test(text)) {
		            return true;
		        }
		        alert('请输入有效的电话号码')
		        return false;
		    }
	    };
	    function encodeText(t){
			return encodeURI(t);
		}
		$(document).ready(function(){
			$('#btnbuy').bind('click',saveInfo);
			$('.select_options').change(function(){
				var v = this.value,t = this.selectedOptions[0].text,s = $(this).closest('.select');
				s.find('.select_input [type="text"]').val(t);
				s.find('.select_input [type="hidden"]').val(v);
			});
		});
		function saveInfo(){
			var dia = new _dialog();
			$('#btnbuy').unbind('click');
			dia.loading.show();
			_b._post('wzjh/surepayplus', {openid:'${openid}',orderid:'${orderid}'}, function(d){
				$('#appid').val(d.appid),
				$('#timeStamp').val(d.timeStamp),
				$('#nonceStr').val(d.nonceStr),
				$('#package').val(d.package),
				$('#sign').val(d.sign),
				callpay();
				$('#btnbuy').bind('click',callpay);
				dia.loading.hide();
			}, function(){
				$('#btnbuy').bind('click',submitForm);
				dia.loading.hide();
			});
		}
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
						alert("支付成功");
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
