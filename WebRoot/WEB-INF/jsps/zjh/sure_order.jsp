<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
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
	<title>顶级医生</title>
	<link rel="stylesheet" type="text/css" href="/css/mobile.css?v=1.0" />
	<style>	
		body{background-color:#EFEFEF;}    	
		.basecss{border:1px solid #E1E1E1;border-radius:5px;margin:10px;background-color:#fff;overflow:hidden;}
		
		.controls{position:relative;display:block; margin:0 6px;border-bottom:1px solid #E8E8E8;line-height:4em;color:#999;font-size:14px;}
		.controls span{display:block;}
		.controls .clabel{position:absolute;left:0;top:50%;margin-top:-2em; width:4.4em;text-align:right;color:#333;}
		.controls .contorl{margin-left:5.6em;}
		.controls .contorl input,
		.controls .contorl select{width:100%;height:2em;border:0;outline:0;}
		.select{position:relative;}
		.select .select_input{position:relative; margin-right:40px;z-index:3;}
		.select .select_select{position:absolute;left:0;top:0;height:100%;width:100%;z-index:2;background:transparent;}
		.select .select_select:after{content:"";position:absolute;right:13px;top:50%;margin-top:-9px;width:10px;height:10px;border:1px solid #999;
			transform:rotate(-135deg);-webkit-transform:rotate(-135deg);border-right:0;border-bottom:0;z-index:1;
		}
		.select .select_select select{opacity:0;position:relative;z-index:2;}
		
		.g_fixed {background-color:#EFEFEF;z-index: 10;}
		.g_fixed .cols0{display:block;margin: 6px 10px;}
		.g_fixed .btn2{border:0;background:#00CC99 url();color:#fff;font-weight:600;font-size: 16px;width:100%;height:40px}
		.g_fixed .btn2 span{color:#FEFD65;}
	</style>
  </head>  
  <body>
    <div class="index">
		<form action="/sapper/surepay" method="post" id="postorder" name="postorder">
			<input type="hidden" name="openid" value="${openid}"/>
	    	<input type="hidden" name="cooHosId" value="${cooHosId }"/>
	    	<input type="hidden" name="localHosId" value="${cooHosId}"/>
	    	<input type="hidden" name="condate" id="datetime" value="${condate}"/>
	    	<input type="hidden" name="contime" id="time" value="${contime}"/>
	    	<input type="hidden" name="cmoney" id="cmoney" value="${cmoney}"/>
	    	<input type="hidden" name="localDepartId" id="depart" value="${localDepartId}"/>
	    	<input type="hidden" name="expertId" id="expertId" value="${expertId}"/>
			<div class="mianinfo basecss">
				<label class="controls">
					<span class="clabel">预约专家</span>
					<span class="contorl whitespace">${expert.specialName}（${expert.hosName}&ensp;${expert.specialTitle}）</span>
				</label>
				<label class="controls">
					<span class="clabel">预约时间</span>
					<span class="contorl">${condate }&emsp;${contime}</span>
				</label>
				<label class="controls">
					<span class="clabel">会诊地址</span>
					<span class="contorl">${cooHos.displayName}</span>
				</label>
				<label class="controls">
					<span class="clabel">当地科室</span>
					<span class="contorl">${coodep.displayName}</span>
				</label>
			</div>
			<div class="forminfo basecss">
				<div class="controls">
					<span class="clabel">姓名</span>
					<span class="contorl">
						<div class="select">
							<div class="select_input">
								<input type="text" placeholder="请输入患者真实姓名" name="username" id="username"/>
							</div>
							<div class="select_select">
								<select class="select_options" name="select_s" id="his_con">
									<c:forEach items="${conInfos}" var="ci">
										<option value="${ci.userName}|${ci.idCard}|${ci.telphone}">${ci.userName}</option>
									</c:forEach>
									<option value="">--添加新数据--</option>
								</select>
							</div>							
						</div>
					</span>
				</div>
				<label class="controls">
					<span class="clabel">身份证</span>
					<span class="contorl">
						<input type="text" placeholder="请输入患者身份证号" name="idcard" id="idcard"/>
					</span>
				</label>
				<label class="controls">
					<span class="clabel">联系电话</span>
					<span class="contorl">
						<input type="tel" placeholder="请输入联系电话，可以非本人" name="telphone" id="tel"/>
					</span>
				</label>
			</div>
			<div style="height:50px;">&nbsp;</div>
		    <div class="g_fixed">
		    	<span class="cols0">
		    		<button type="button" class="btn2" id="btnbuy"><span>(${cmoney}元)</span>微信支付</button>
		    	</span>
		    </div>
		</form>	
    </div>
		<input type="hidden"  id="appid"/>
		<input type="hidden"  id="timeStamp"/>
		<input type="hidden"  id="nonceStr"/>
		<input type="hidden"  id="package"/>
		<input type="hidden"  id="sign"/>
	<script src="/js/jquery-1.11.0.min.js"></script>
	<script src="/js/base.js"></script>
	<script type="text/javascript">
		$(function(){
			$('#his_con').change(function(){
				var info = $(this).val() || ('||');
				info = info.split('|');
				$('#username').val(info[0]);
				$('#idcard').val(info[1]);
				$('#tel').val(info[2]||'${f_tel}');
			}).change();
		})
	</script>
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
			if(!_b.valideTel($('[name="telphone"]').val())) return false;
			$('#btnbuy').unbind('click');
			dia.loading.show();
			_b._post('wzjh/surepay', $('#postorder').serialize(), function(d){
				$('#appid').val(d.appid),
				$('#timeStamp').val(d.timeStamp),
				$('#nonceStr').val(d.nonceStr),
				$('#package').val(d.package),
				$('#sign').val(d.sign),
				callpay();
				$('#btnbuy').bind('click',submitForm);
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
						window.location.href='/wzjh/myorders?openid=${openid}';
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
