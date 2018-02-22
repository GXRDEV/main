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
	<title>订单确认</title>
	<link rel="stylesheet" type="text/css" href="/css/mobile.css?v=1.0" />
	<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
	<style>	
		body{background-color:#EFEFEF;}    	
		.basecss{background-color:#fff;overflow:hidden;}
		.split{padding:10px;font-size:12px;color:#aaa;}
		.controls{position:relative;display:block; margin:0 10px;line-height:3em;color:#999;font-size:14px;}
		.controls + .controls{border-top:1px solid #E8E8E8;}
		.controls span{display:block;}
		.controls .clabel{position:absolute;left:0;top:50%;margin-top:-1.5em; width:7em;color:#333;}
		.controls .clabel i{margin-right:5px;color:#00CC99;}
		.controls .contorl{margin-left:7.6em;}
		.controls .contorl input,
		.controls .contorl select{width:100%;height:2em;border:0;outline:0;}
		
		.mianinfo{font-size:13px;padding-bottom:6px;}
		.mianinfo .controls{line-height:2em;font-size:13px;border:0;}
		.mianinfo .controls .clabel{margin-top:-1em;width:5.4em;color:#8C8C8C;}
		.mianinfo .controls .contorl{margin-left:5.6em;color:#8C8C8C;}
		
		
		.select{position:relative;}
		.select .select_input{position:relative; margin-right:40px;z-index:3;}
		.select .select_select{position:absolute;left:0;top:0;height:100%;width:100%;z-index:2;background:transparent;}
		.select .select_select:after{content:"";position:absolute;right:13px;top:50%;margin-top:-9px;width:10px;height:10px;border:1px solid #999;
			transform:rotate(-135deg);-webkit-transform:rotate(-135deg);border-right:0;border-bottom:0;z-index:1;
		}
		.select .select_select select{opacity:0;position:relative;z-index:2;}
		
		.g_fixed {background-color:#EFEFEF;z-index: 10;}
		.g_fixed .cols0{display:block;margin:0px;}
		.g_fixed .btn2{border:0;background:#00CC99 url();color:#fff;font-weight:600;font-size: 16px;width:100%;height:40px}
		.g_fixed .btn2 span{color:#FEFD65;}
	</style>
  </head>  
  <body>
    <div class="index">
		<form action="/wzjh/surepay" method="post" id="postorder" name="postorder">
			<input type="hidden" name="openid" value="${openid}"/>
	    	<input type="hidden" name="cooHosId" value="${hospital.id }"/>
	    	<input type="hidden" name="localHosId" value="${hospital.id}"/>
	    	<input type="hidden" name="localDepartId" id="depart" value="${departId}"/>
	    	<input type="hidden" name="expertId" id="expertId" value="${sid}"/>
	    	<input type="hidden" name="stimeid" id="stimeid" value="${stimeid }" />
	    	<input type="hidden" name="pmoney" id="pmoney" value="${special.vedioAmount}" />
			<div class="mianinfo basecss">
	    		<p class="split">预约信息</p>
				<label class="controls">
					<span class="clabel">预约时间：</span>
					<span class="contorl">${timesch.scheduleDate }&emsp;${timesch.startTime}</span>
				</label>
				<label class="controls">
					<span class="clabel">就诊医院：</span>
					<span class="contorl">${hospital.displayName}&ensp;</span>
				</label>
				<div style="border-bottom:1px dotted #ccc;height:0;margin:4px 10px;"></div>
				<label class="controls">
					<span class="clabel">当地科室：</span>
					<span class="contorl">${depart.displayName}&ensp;</span>
				</label>
				<label class="controls">
					<span class="clabel">服务专家：</span>
					<span class="contorl whitespace">${special.specialName}&ensp;${special.hosName}</span>
				</label>
			</div>
			<div class="forminfo basecss" style="margin-top:10px;">
				<p class="split">补充就诊信息</p>
				<div class="controls">
					<span class="clabel"><i class="iconfont">&#xe611;</i>患者姓名：</span>
					<span class="contorl">
						<div class="select">
							<div class="select_input">
								<input type="text" placeholder="请输入患者真实姓名" name="username" id="username"/>
							</div>
							<div class="select_select">
								<select class="select_options" name="select_s" id="his_con">
									<c:forEach items="${conInfos}" var="ci">
										<option value="${ci.contactName}|${ci.idCard}|${ci.telphone}">${ci.contactName}</option>
									</c:forEach>
									<option value="">--添加新数据--</option>
								</select>
							</div>							
						</div>
					</span>
				</div>
				<label class="controls">
					<span class="clabel"><i class="iconfont">&#xe610;</i>身份证号：</span>
					<span class="contorl">
						<input type="text" placeholder="请输入患者身份证号" name="idcard" id="idcard"/>
					</span>
				</label>
				<label class="controls">
					<span class="clabel"><i class="iconfont">&#xe60f;</i>联系电话：</span>
					<span class="contorl">
						<input type="tel" placeholder="请输入联系电话，可以非本人" name="telphone" id="tel"/>
					</span>
				</label>
			</div>
			<div style="height:50px;">&nbsp;</div>
		    <div class="g_fixed">
		    	<span class="cols0">
		    		<button type="button" class="btn2" id="btnbuy">￥${special.vedioAmount}元&ensp;微信支付</button>
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
		    },
		    valideCard:function(idCard){
		        var regIdCard =/^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
		        var HMCard = /^[HhMm]\d{8}((\(\d{2}\))|\d{2})$/,
		        	TCard = /^\d{8}(\d{1,2}(\([A-Za-z]\))?)?$/;
		        switch(idCard.length){
			        case 8:
			        case 9:
			        case 10:
			        case 11:
			        case 13:
			        	if (HMCard.test(idCard)){return true;}
			        	if (TCard.test(idCard)) {return true;}
			        	return false;
			        	break;
			        case 18:
			            if (regIdCard.test(idCard)) {
			                var idCardWi = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2); 
			                var idCardY = new Array(1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2); 
			                var idCardWiSum = 0; 
			                for (var i = 0; i < 17; i++) {
			                    idCardWiSum += idCard.substring(i, i + 1) * idCardWi[i];
			                }     
			                var idCardMod = idCardWiSum % 11;
			                var idCardLast = idCard.substring(17);
			     
			                if (idCardMod == 2) {
			                    if (idCardLast == "X" || idCardLast == "x") {
			                        return true;
			                    } else {
			                        return false;
			                    }
			                } else {
			                    if (idCardLast == idCardY[idCardMod]) {
			                        return true;
			                    } else {
			                        return false;
			                    }
			                }
			            }
			        	break;
			        default:
		            	return false;
			        	break;
		        }
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
			var idcard = $('[name="idcard"]').val();
			if(!_b.valideTel($('[name="telphone"]').val())) return false;
			if(idcard && !_b.valideCard(idcard)) return alert('请输入有效的身份证号'),false;
			$('#btnbuy').unbind('click');
			dia.loading.show();
			_b._post('wzjh/surepay', $('#postorder').serialize(), function(d){
				$('#appid').val(d.appid),
				$('#timeStamp').val(d.timeStamp),
				$('#nonceStr').val(d.nonceStr),
				$('#package').val(d.package),
				$('#sign').val(d.sign),
				callpay();
				$('#btnbuy').bind('click',saveInfo);
				dia.loading.hide();
			}, function(){
				$('#btnbuy').bind('click',saveInfo);
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
