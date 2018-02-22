<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//通过request["code"]得到code，通过code得到用户的基本信息
%>

<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>绑定公号</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
    <style>
    	*{padding:0;margin:0;}
    	a{text-decoration:none;}
    	html{font-size: 10px;}
    	body{background-color:#E7E7E7;font-family:"黑体";font-size:1.4rem;}
    	#step1{width:600px;margin:0 auto; }
    	form dd{border:1px solid #ADADAD;background-color:#fff;margin:8px 14px;border-radius:4px;overflow:hidden;}
    	form dd table{width:100%;}
    	form dd td{position:relative;height:40px;line-height:36px;padding:2px;font-size:1.6rem;}
    	form dd .label{text-align:right;color:#999;}
    	form dd .has2words{width:54px;}
    	form dd .has3words{width:70px;}
    	form dd .has4words{width:86px;}
    	form dd .has5words{width:102px;}
    	
    	form .select div{position:absolute;left:4px;bottom:4px;right:4px;top:4px;z-index:1;color:#A882BD;}
    	form .select div:after{content:"";display:inline-block;width:30px;border-left:1px solid #ccc;
    		height:30px;position:absolute;right:2px; top:2px;background:url(/img/mobile/regdoc/down.png) no-repeat center center;}
    	form .select select,form dd input{position:relative;width:100%;z-index:2;color:#333;height:30px;border:0;outline:0;}
    	form .select select{opacity:0;}
    	@media (max-width: 600px) {	
			#step1{width:100%;margin:0 auto;}
    		form dd td,form dd select,form dd input{font-weight:600;}
		}
		.height100{height:100%;}
		#step2{display:none;}
		#sendCode{width:100px;font-size:14px;height:34px;}
		#sendCode.disabled{color:#f3f3f3;cursor:not-allowed;font-size:10px;}   
		form .error{border-color:#FF7C7C;} 	
    	
    	dt .files{display:inline-block;width:103px;height:106px;position:relative;overflow:hidden;}
    	dt .files label{line-height:92px;color:#585753;position:relative;z-index:5;font-size:12px;text-shadow: 1px 1px 0 #fff;}
    	dt .files img{position:absolute;width:100%;top:0;left:0;z-index:1;border-radius:50%;}
    	dt .files input{font-size:80px;position:absolute;z-index:10;left:0;top:0;opacity:0;}
    	.buttonClass1 {background-color: #00CC99;}
    </style>
</head>
<body>
	<form method="post" id="myform" action="">
		<input type="hidden" name="openid" value="${openid}"/>
    	<input type="hidden" name="cooHosId" value="${hospital.id }"/>
    	<input type="hidden" name="localHosId" value="${hospital.id }"/>
    	<input type="hidden" name="departId" id="depart" value="${departId}"/>
    	<input type="hidden" name="expertId" id="expertId" value="${sid}"/>
    	<input type="hidden" name="appiontId" id="appiontId" value="${appiontId}"/>
    	<input type="hidden" name="sid" id="sid" value="${sid}"/>
    	<input type="hidden" name="stimeid" id="stimeid" value="${stimeid }" />
		<div id="step1">
			<dl>
				<dd>
					<table>
						<tr>
							<td class="has3words label nowrap"><label>手机号：</label></td>
							<td>
								<input type="tel" name="tel" id="tel"  class="validate"/>
							</td>
						</tr>
					</table>
				</dd>
				<dt style="text-align:right;padding-right:14px;">
					<button type="button" class="buttonClass1" id="sendCode">获取验证码</button>
				</dt>
				<dd>
					<table>
						<tr>
							<td class="has3words label nowrap"><label>验证码：</label></td>
							<td>
								<input type="text" id="code"  name="code"  class="validate"/>
							</td>
						</tr>
					</table>
				</dd>
			</dl>
		</div>
	</form>	
	<div class="g_fixed" style="background-color:#fff;z-index:1000"><button type="button" class="buttonClass1" id="btnsubmit">完成</button></div>
    <script src="/js/jquery-1.11.0.min.js"></script>
    <script src="/js/base.js"></script>
    <script>   
    	$(document).ready(function(){
    		$('form .validate').blur(function(){
    			if(!$.trim($(this).val())){ 
					$(this).closest('dd').addClass('error');
				}else{
					$(this).closest('dd').removeClass('error');
				}
    		});

    	});    
    </script>
    
    <script>    	
    	var _h = '/',
    		_b = {
    		href: _h,
    		getCode:function(){
    			var t = _b._tel();
    			if(_b.valideTel(t)){    						
	    			_b._post('wzjh/gainVeryCode',{telphone:t},function(d){
	    				if(d.status == 'error'){
	    					alert('网络出错，请重试');
	    					_b.clearTimer();
	    				}
	    			},function(){
	    				_b.clearTimer();
	    			}),_b.timer();
    			}
    		},
    		checkCode:function(){
    			var t = _b._tel(),c = _b._code();
    			if(_b.valideTel(t) && _b.valideCode(c)){ 
	    			_b._post('wzjh/tellCode',{code:c,tel:t,openid:'${openid}'},function(d){
	    				if(d.status == 'success'){
	    					$('#myform').attr("action","/wzjh/suretoconfirm");
	    					$('#myform').submit();
	    				}else{
	    					alert('验证码错误，请重新输入');
	    					$('#btnsubmit').bind('click',_b.checkCode).html('完成');
	    				}
	    			},function(){
	    				$('#btnsubmit').bind('click',_b.checkCode).html('完成');
	    			}),$('#btnsubmit').unbind('click').html('<img alt="" src="'+ _b.href +'img/spinner.gif"/>'); 
	    		}
    		},
    		_post:function(url,ops,fun,err){
    			return _$(this.href + url,ops,fun,err);
    		},
    		_tel:function(){
    			return $.trim($('#tel').val());
    		},
    		_code:function(){
    			return $.trim($('#code').val());
    		},
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
    		valideCode: function(text) {
		        var _emp = /^\s*|\s*$/g;
		        text = text.replace(_emp, "");
		        var _d = /^\d{4}$/g;
		        if (_d.test(text)) {
		            return true;
		        }
		        alert('请输入4位数字验证码')
		        return false;
		    },
		    timer:function(){
		    	var i = 90,_cleart = this.clearTimer;
		    	$('#sendCode').unbind('click').addClass('disabled');		    	
				window._timers = setInterval(function(){
					if(i<1){ 
						_cleart();
						return false;
					}
					$("#sendCode").text( i );
					i--;
				},1000);
		    	
		    },
		    clearTimer:function(){
		    	if(window._timers) clearInterval(window._timers);
				$("#sendCode").removeClass('disabled').text('获取验证码').bind('click',_b.getCode);
		    }
    	};
    	$(document).ready(function(){
    		$('#sendCode').bind('click',_b.getCode);  
    		$('#btnsubmit').bind('click',_b.checkCode);   		
    	});
    </script>
</body>
</html>