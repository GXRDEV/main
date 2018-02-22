<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>护士注册</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=yes" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
    <style>
    	html,body{background-color:#fff;}
		.index{padding:22px 15px 0;background-color:#fff;box-sizing:border-box;}
		section dd{border:1px solid #26B5ED;overflow:hidden;position:relative;font-size:16px;}
		section dd input{padding:18px 6px 18px 46px;width:100%;box-sizing:border-box;border:0;}
		section dd button{position:absolute;top:0;right:0;height:100%;width:8em;padding:0 0.5em 0 2em;
			background:#26B5ED url(/img/mobile/kbzs/login_send@2x.png) no-repeat 8px 17px;
			color:#fff;box-sizing:border-box;border:0;font-size:14px;background-size: 20px 20px;}
		dt label{display:block;font-size:12px;padding:16px 0 20px 24px;color:#aaa;position:relative;}
		dt label input{display:none;}
		dt label i{display:inline-block;width:14px;height:14px;border:1px solid #26B5ED;border-radius:50%;position:absolute;left:3px;top:15px;}
		dt :checked + i:after{content:"";display:inline-block;width:8px;height:8px;border-radius:50%;background-color:#26B5ED;position:absolute;left:3px;top:3px;}
		section dd button.disabled{padding:0;cursor:not-allowed;color:#ddd;background-image:url();}
		.tel{background:url(/img/mobile/kbzs/login_user@2x.png) no-repeat 11px 13px;background-size: 22px 25px;}
		.code{background:url(/img/mobile/kbzs/login_psd@2x.png) no-repeat 11px 13px;background-size: 22px 25px;}

		#step2,#step3,#step4{display:none;}
		.buttonClass26B5ED{box-shadow: 3px 3px 14px #bbb;height:46px;display:inline-block;text-align:center;line-height:46px;width:100%;}
		a.buttonClass26B5ED{width:48%;}
		a.buttonClass26B5ED + a.buttonClass26B5ED{margin-left:4%}
		#step3 .fulla{width:98%;margin:0 auto;}
    </style>    
</head>
<body>
	<div id="index" class="index">
		<section>
			<dl>
				<dd style="border-radius:4px 4px 0 0;">
					<input type="tel" name="tel" placeholder="请输入手机号码" class="tel" id="tel" />
				</dd>
				<dd style="border-radius:0 0 4px 4px;border-top:0;">
					<input type="tel" name="code" placeholder="请输入验证码" id="code" />
					<button type="button" id="sendCode">发送验证码</button>
				</dd>
				<dt style="padding: 5px 0 15px;">
					<label for="xy"><input type="checkbox" value="true" checked name="xy" id="xy"/><i></i>同意用户协议与隐私条款</label>
					<button type="button" class="buttonClass26B5ED" id="btnsubmit">下一步</button>
				</dt>
			</dl>
		</section>
	</div>
	<div id="step2" class="index">
		<section>
			<dl>
				<dd style="border-radius:4px;">
					<input type="password" name="password" class="code" placeholder="请输入登录密码（至少6位）" id="password" />
				</dd>
				<dt style="padding: 20px 0 15px;">
					<button type="button" class="buttonClass26B5ED" id="btnreg">注册</button>
				</dt>
			</dl>
		</section>
	</div>	
	<div id="step3" class="index">
		<section>
			<dl>
				<dd style="border:0;text-align:center;line-height:200px;font-size:20px;font-weight:600;color:#26B5ED;">
					恭喜您，注册成功！
				</dd>
				<dt style="padding: 20px 0 15px;font-size:0;text-align:center;">
					<a href="/wtspz/nursereg/{0}" class="buttonClass26B5ED" id="btnrz">立即认证信息</a>
					<a href="javascript:alert('开发中...');" class="buttonClass26B5ED" >下载APP</a>
				</dt>
			</dl>
		</section>
	</div>
    <script src="/libs/jquery-1.11.0.min.js"></script>
    <script src="/js/base.js"></script>
    <script>    	
    	var _h = '/',
    		_b = {
    		href: _h,
    		oid: '${openid}',
    		getCode:function(){
    			var t = _b._tel();
    			if(_b.valideTel(t)){ 
	    			_b._post('wtspz/gainVeryCode.do',{telphone:t},function(d){
	    				if(d.status == 'error'){
	    					alert('网络出错，请重试');
	    					_this.clearTimer();
	    				}
	    			},function(){
	    				_this.clearTimer();
	    			}),_b.timer();
    			}
    		},
    		checkreg:function(fun,err){
    			var t = _b._tel();
    			if(_b.valideTel(t)){    						
	    			_b._post('wtspz/tellrepeattel.do',{tel:t},function(d){
	    				if(d.status == 'exist'){
	    					err(d.id,d.hasWS);
	    				}else{
	    					fun();
	    				}
	    				$('#btnsubmit').bind('click',_b.checkCode).html('下一步');
	    			},function(){
	    				$('#btnsubmit').bind('click',_b.checkCode).html('下一步');
	    			}),$('#btnsubmit').unbind('click').html(_loading.w3124);
    			}
    		},
    		checkCode:function(){
    			var t = _b._tel(),c = _b._code();
    			if(_b.valideTel(t) && _b.valideCode(c)){ 
	    			_b._post('wtspz/tellCode.do',{code:c,tel:t,openid:_b.oid},function(d){
	    				if(d.status == 'success'){   					
    						_b.checkreg(function(){
		    					location.hash = '#step2';
		    					$('.index').hide();
		    					$('#step2').show();	
    						},function(id,hasWS){
		    					location.hash = '#step3';
    							$('.index').hide();
	    						$('#step3').show().find('dd').text('您已注册过');
	    						hasWS ? 
	    							$('#btnrz').hide().siblings('a').addClass('fulla') :
	    							$('#btnrz').attr('href',$('#btnrz').attr('href').replace('{0}',d)).siblings('a').removeClass('fulla');					
    						});
	    				}else{
	    					alert('验证码错误，请重新输入');
	    				}
	    				$('#btnsubmit').bind('click',_b.checkCode).html('下一步');
	    			},function(){
	    				$('#btnsubmit').bind('click',_b.checkCode).html('下一步');
	    			}),$('#btnsubmit').unbind('click').html(_loading.w3124); 
	    		}
    		},
    		postCode:function(){
    			var t = _b._tel(),c = _b._psd();
    			if(_b.valideTel(t) && _b.validePSD(c)){ 
	    			_b._post('wtspz/nurseregister.do',{Password:c,RegisterName:t},function(d){
	    				if(d){
	    					$('.index').hide();
	    					$('#step3').show().find('dd').text('恭喜您，注册成功！');
	    					$('#btnrz').attr('href',$('#btnrz').attr('href').replace('{0}',d));
	    				}else{
	    					alert('保存失败，请重试');
	    				}
	    				$('#btnreg').bind('click',_b.postCode).html('注册');
	    			},function(){
	    				$('#btnreg').bind('click',_b.postCode).html('注册');
	    			}),$('#btnreg').unbind('click').html(_loading.w3124); 
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
    		_psd:function(){
    			return $.trim($('#password').val());
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
    		validePSD: function(text) {
		        var _emp = /^\s*|\s*$/g;
		        text = text.replace(_emp, "");
		        if (text.length > 5) {
		            return true;
		        }
		        alert('请输入6位以上密码')
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
    		$('#btnreg').bind('click',_b.postCode);
    	});
   		window.onhashchange=function(){
			var hashStr = location.hash.replace("#","");
			switch(hashStr){
				case "step2":
					$('.index').hide(),$('#step2').show();
					break;
				case "step3":
					$('.index').hide(),$('#step3').show();
					break;
				default:
					$('.index').hide(),$('#index').show();
					break;
			}
		};
    </script>    
</body>
</html>