<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>绑定信息</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=yes" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
    <style>
    	html,body{background-color:#ddd;}
		#index{padding:0 15px;background-color:#fff;box-sizing:border-box;}
		header{color:#BFBFBF;padding:26px 0 44px;text-align:center;}
		header figure{margin:0;}
		header figure img{margin-bottom: -20px; margin-right:15px;}
		header figcaption{display:inline-block;/*border-left:1px solid #26B5ED;*/padding-left:10px;font-size:16px;text-align:center;}
		header figcaption label{display:block;color:#26B5ED;font-size:26px;padding-bottom:6px;font-weight:600;}
		
		section dd{border:1px solid #26B5ED;overflow:hidden;position:relative;font-size:16px;}
		section dd input{padding:18px 6px 18px 46px;width:100%;box-sizing:border-box;border:0;}
		section dd button{position:absolute;top:0;right:0;height:100%;width:8em;padding:0 0.5em 0 2em;
			background:#26B5ED url(/img/mobile/kbzs/login_send@2x.png) no-repeat 8px 17px;
			color:#fff;box-sizing:border-box;border:0;font-size:14px;background-size: 20px 20px;}
		section dd button.disabled{padding:0;cursor:not-allowed;color:#ddd;background-image:url();}
		.tel{background:url(/img/mobile/kbzs/login_user@2x.png) no-repeat 11px 13px;background-size: 22px 25px;}
		.code{background:url(/img/mobile/kbzs/login_psd@2x.png) no-repeat 11px 13px;background-size: 22px 25px;}
		
		footer{color:#DEDEDE;padding-top:20px;font-size:12px;}
		footer div{border-top:1px solid #DEDEDE;padding:10px;}
		footer div a{font-size:14px;color:#26B5ED;}
		
		.buttonClass26B5ED{box-shadow: 3px 3px 14px #bbb;height:46px;}
		@media (max-width: 600px) {	
			html,body{background-color:#fff;}
		}
    </style>    
</head>
<body>
	<div id="index">
		<header>
			<figure>
				<!--<img src="/img/mobile/kbzs/kbzs_logo@2x.png" style="width:74px" alt="天使陪诊"/>-->
				<figcaption>
					<label>天使陪诊</label>
					<span>一款便捷的医疗应用</span>
				</figcaption>
			</figure>
		</header>
		<section>
			<dl>
				<dd style="border-radius:4px 4px 0 0;">
					<input type="tel" name="tel" placeholder="请输入手机号码" class="tel" id="tel" />
				</dd>
				<dd style="border-radius:0 0 4px 4px;border-top:0;">
					<input type="tel" name="code" placeholder="请输入验证码" class="code" id="code" />
					<button type="button" id="sendCode">发送验证码</button>
				</dd>
				<dt style="padding: 20px 0 15px;">
					<button type="button" class="buttonClass26B5ED" id="btnsubmit">立即登录</button>
				</dt>
			</dl>
		</section>
		<footer>
			<div>
				如有任何疑问请拨打客服电话：<a href="tel:13810949001">13810949001</a>
			</div>
		</footer>
	</div>
    <script src="/libs/jquery-1.11.0.min.js"></script>
    <script src="/js/base.js"></script>
    <script>    	
    	var _h = '/',
    		_b = {
    		href: _h,
    		oid: '${openid}',
    		history: function(){
    			var u = '';
    			switch('${flag}'){
    				case "1":
    				case "2":
    					u = _h + 'wtspz/intoservice/${flag}/${openid}';
    					break;
    				default:
    					u = _h + 'wtspz/my${flag}s.do?openid=${openid}';
    					break;
    			}
    			return u;
    		},
    		getCode:function(){
    			var t = _b._tel();
    			if(_b.valideTel(t)){    						
	    			_b._post('wtspz/gainVeryCode.do',{telphone:t},function(d){
	    				if(d.status == 'error'){
	    					alert('网络出错，请重试');
	    					_this.clearTimer();
	    				}
	    			}),_b.timer();
    			}
    		},
    		checkCode:function(){
    			var t = _b._tel(),c = _b._code();
    			if(_b.valideTel(t) && _b.valideCode(c)){ 
	    			_b._post('wtspz/tellCode.do',{code:c,tel:t,openid:_b.oid},function(d){
	    				if(d.status == 'success'){
	    					location.replace(_b.history());
	    				}else{
	    					alert('验证码错误，请重新输入');
	    					$('#btnsubmit').bind('click',_b.checkCode).html('立即登录');
	    				}
	    			}),$('#btnsubmit').unbind('click').html(_loading.w3124); 
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
		    	var i = 60,_cleart = this.clearTimer;
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
				$("#sendCode").removeClass('disabled').text('发送验证码').bind('click',_b.getCode);
		    }
    	};
    	$(document).ready(function(){
    		$('#sendCode').bind('click',_b.getCode);    
    		$('#btnsubmit').bind('click',_b.checkCode);  		
    	});
    </script>    
</body>
</html>