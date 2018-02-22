function showDialog(title,msg) {
	$("#myModal").find(".modal-header h3").html(title);
    $("#myModal").attr('class','modal');
    $("#myModal").find(".modal-body p").html(msg);
    setTimeout(function(){
        $("#myModal").attr('class','modal hide');
    },3000);
}
function showmessage(cls,msg) {
	$('.' + cls).show().html(msg);
    setTimeout(function(){
    	$('.' + cls).hide();
    },5000);
}
function keydown(e)
{
    var e = e||event;
    var currKey = e.keyCode||e.which||e.charCode;
    if(currKey == 13)
    {
        checkLogin();
    }
}

function validatePwd(str){   
    if(str.length!=0){    
        //reg=/^[0-9]+[A-Za-z0-9]*|[0-9]+[A-Za-z]+[A-Za-z0-9]*$/; 
        reg=/^[A-Za-z0-9]*$/;    
        if(!reg.test(str)){    
            return false;  
        } else {
            return true;
        }   
    } 
    return false;    
}
function checkLogin(){
	var username = $("input[name=username]").val();
	var password = $("input[name=password]").val();
	var stype=$('#loginform .formtitle a.selected').attr('data-type') || '';
	if ($.trim(username) == "") {
		showmessage("forusername","<span style='color:red;'>请输入账号！</span>");
	     return false;
	}
	if ($.trim(password) == "") {
		showmessage("forpassword","<span style='color:red;'>请输入密码！</span>");
	     return false;
	}
	$.post(_burl+'doctor/checkLogin',{username:username,password:password,stype:stype},function(data){
		 if(data.status=='success'){
			 var url='doctor/index';
			 faveInput(document.getElementById('remberme').checked);
			 window.parent.location.href=_burl+url;
		 }else{
			 showmessage("forpassword","<span style='color:red;'>账号或密码错误，请重新输入！</span>");
		 }
	});
}
function logoutfun(){
	$.post(_burl + 'doctor/logout',{},function(data){
		
		 window.parent.location.href = _burl + "index.jsp";
	});	
}
$(document).ready(function(){
	var login = $('#loginform');
	var recover = $('#recoverform');
	var rembme = document.getElementById('remberme');
	var speed = 400;
	
	$('#checkBtn').bind('click',checkLogin);
	$('#loginbox [name="password"]').keydown(keydown);
	$('.logout').click(logoutfun);
	$('.showinfo').click(function(){
		seajs.use('view/base',function(base){
			base.showDialog({
				id:'BaseInfo',
				title:'个人资料',
				cls:'modal2-500',
				nofooter:true
			}).get(_burl + 'doctor/showbasicinfo',{},function(d){	
				$('#BaseInfo .modal2-body').html('<p><img src="'+ (d.special['listSpecialPicture'].indexOf('://')!= -1 ? d.special['listSpecialPicture'].replace('http://','https://') : (d.special['listSpecialPicture'] ? 'http://wx.15120.cn/SysApi2/Files/' + d.special['listSpecialPicture'] : 'img/defdoc.jpg')) +'" /></p>'+
						'<p><b>'+ d.special['specialName'] +'</b>&emsp;'+ d.special['duty'] +'</p>'+
						'<p>'+ d.special['hosName'] +'&emsp;'+ d.special['depName'] +'</p>'+
						(d.special['regisStr'] ? '<p>出诊时间：'+ d.special['regisStr'] +'</p>' : '')+
						'<p>'+ d.special['specialty'] +'</p>'
						);
			},function(){
				base.showTipE('加载失败').hideDialog('BaseInfo');
			});
		});
	});
	$('form input').focus(function(){
		$(this).css({'font-weight':'700','letter-spacing':'1px'})
	}).blur(function(){
		$(this).css({'font-weight':'400','letter-spacing':'0px'})
	});
	$('.changepsd').click(function(){
		seajs.use('view/base',function(base){
			base.showDialog({
				id:'FormModelEdit',
				title:'修改密码',
				cls:'modal2-500',
				text:'<form action="" class="renewpsd" method="post"><input type="password" name="oldpass" autocomplete="off" placeholder="请输入原始密码"/><input type="password" name="newpass" autocomplete="off" placeholder="请输入新密码"/><input type="password" name="renewpass" autocomplete="off" placeholder="请再次输入新密码"/></form>',
				ok:function(){
					var oldpassword = $("#FormModelEdit input[name=oldpass]").val();
					var newpassword = $("#FormModelEdit input[name=newpass]").val();
					var confirmpassword = $("#FormModelEdit input[name=renewpass]").val();

				    if (!$.trim(oldpassword)) {
				    	base.showTipE('原始密码不能为空');
				        return false;
				    }				    
				    if (!$.trim(newpassword) || !$.trim(confirmpassword) ) {
				    	base.showTipE("新密码和确认新密码不能为空");
				        return false;
				    }
				    var flag = validatePwd(newpassword);

				    if (!flag || newpassword.length<6) {
				    	base.showTipE("密码必须为英文字符和数字组合，且不能低于6位");
				        return false;
				    }
				    if (newpassword == oldpassword) {
				    	base.showTipE("新密码不能与原始密码一样，请重新输入");
				        return false;
				    }
				    if (newpassword != confirmpassword) {
				    	base.showTipE("密码和确认密码项不一致，请检查");
				        return false;
				    }
					base.showTipIngA('正在保存').post(_burl + 'doctor/updatepass',$('#FormModelEdit .renewpsd').serializeArray(),function(d){	
						if(d.status == 'success'){
							base.showTipS('修改成功，请重新登录');
							window.setTimeout(logoutfun,600);
						}else{
							base.showTipE('原始密码输入错误，请重试');
						}
					},function(){			
						base.showTipE('修改失败,请重试');
					})
				}
			});
		});
	});
	$('.close').click(function(){
		$(this).closest('.modal').attr('class','modal hide');
	});
	
	$('#to-recover').click(function(){
		$("#loginform").slideUp();
		$("#recoverform").fadeIn();
		$('#recoverform .section1').show().siblings('.section').hide();
		$("#recoverform .step1").addClass('passed').siblings('.step').removeClass('passed');
	});

	$('#to-login').click(function(){
		$("#recoverform").hide();
		$("#loginform").fadeIn();
	});

    $('#nextstep').bind('click',nextstep);
    $('#nextend').bind('click',nextend);
    $('#nextok').bind('click',nextok);
    $('#getCode').bind('click',sentevent);

    if($.browser.msie == true && $.browser.version.slice(0,3) < 10) {
        $('input[placeholder]').each(function(){ 
	        var input = $(this);       
	       
	        $(input).val(input.attr('placeholder'));
	               
	        $(input).focus(function(){
	             if (input.val() == input.attr('placeholder')) {
	                 input.val('');
	             }
	        });
	       
	        $(input).blur(function(){
	            if (input.val() == '' || input.val() == input.attr('placeholder')) {
	                input.val(input.attr('placeholder'));
	            }
	        });
	    });    
    }
    if(rembme){
    	rembme.checked = window.BASE64.decoder(getCookieValue("cmVtYmVybWU=")) == '1';
    	fillInput(rembme.checked);
    }
    $('#remberme').change(function(){
    	setCookie("cmVtYmVybWU=",window.BASE64.encoder(this.checked ? '1' : '0'),24000,"/");
    });
    $("#modelClose").click(function(){
        $("#myModal").attr('class','modal hide');
    });
    $('body').delegate('#loginform .normal_text a','click',function(){
    	$(this).addClass('selected').siblings().removeClass('selected');
    });
});
function nextstep(){
	if(!valideStep1()) return false;	
	postcode(function(){
		$("#recoverform .section1").slideUp();
		$("#recoverform .section2").fadeIn();
		$("#recoverform .step1,#recoverform .step2").addClass('passed');		
	});
}
function valideStep1(){
	var tel=$("#telphone").val(),code=$("#code").val();
	if(!(/^1[3|5|7|8][0-9]\d{4,8}$/.test(tel)))return showDialog("重置密码信息","<span style='color:red;'>请输入有效的手机号码。</span>"),false;
	if(!code)return showDialog("重置密码信息","<span style='color:red;'>请输入验证码。</span>"),false;
	return true;
}
function postcode(callback){
	var tel=$("#telphone").val(),code=$("#code").val();
	$("#nextstep").text('正在验证，请稍候').addClass('disabled').unbind('click');
	$.ajax({    
        url: _burl + 'doctor/nextvalidate',
        data: {telphone:tel,code:code},
        type:'post',
        success:function(data) {
        	alert(data.status);
        	if(data.status=='success'){
	   			callback()
	   		}else{
	   			showDialog("重置密码信息","<span style='color:red;'>验证码错误，请重新输入！</span>");
	   		}
	   		$("#nextstep").text('下一步').removeClass('disabled').bind('click',nextstep);
        },    
        error : function() {
        	showDialog("重置密码信息","<span style='color:red;'>网络出错，请重试！</span>");
        	$("#nextstep").text('下一步').removeClass('disabled').bind('click',nextstep);
        }    
    });
}
function nextend(){
	validatestep2(function(){
		$("#recoverform .section2").slideUp();
		$("#recoverform .section3").fadeIn();
		$("#recoverform .step").addClass('passed');
		setRimer();
	});
}
function validatestep2(callback){
	var username = $("input[name=telphone]").val();
    var password = $("input[name=re_password]").val();
    var confirmpassword = $("input[name=re_confirmpassword]").val();

    if ($.trim(password) == "" || $.trim(confirmpassword) == "" ) {
        showDialog("重置密码信息","<span style='color:red;'>密码和确认密码项不能为空！</span>");
        return false;
    }

    var flag = validatePwd(password);

    if (!flag || password.length<6) {
        showDialog("重置密码信息","<span style='color:red;'>密码必须为英文字符和数字组合，且不能低于6位！</span>");
        return false;
    }

    if (password != confirmpassword) {
        showDialog("重置密码信息","<span style='color:red;'>密码和确认密码项不一致，请检查！</span>");
        return false;
    }
    $("#nextend").text('正在验证，请稍候').addClass('disabled').unbind('click');
    $.ajax({    
        url: _burl + 'doctor/newpassset',
        data: {
        	telphone : username,
        	newpass : password
        },
        type:'post',
        success:function(data) {
            if (data.status == 'success') {
                callback();
            }else if(data.status == 'error'){
                showDialog("重置密码信息","<span style='color:red;'>你好，密码重置失败，请稍后重试！</span>");
            }else{
                showDialog("重置密码信息","<span style='color:red;'>你好，参数异常，请重新填写表单！</span>");
            }
            $("#nextend").text('完成').removeClass('disabled').bind('click',nextend);
        },    
        error : function() {
            $("#nextend").text('完成').removeClass('disabled').bind('click',nextend);
        }    
    });
}
function nextok(){
	var username = $("input[name=telphone]").val();
    var password = $("input[name=re_password]").val();
    $("input[name=username]").val(username);
    $("input[name=password]").val(password);
    $('#recoverform .controls :input').val('');
    
	$("#recoverform").hide();
	$("#loginform").fadeIn();
	$("#recoverform .step1").addClass('passed').siblings('.step').removeClass('passed');
	clearRimer();
}
function setRimer(){
	var i = 10;
	window._timers0 = setInterval(function(){
		if(i<1){ 
			nextok();
			return false;
		}
		$('#recoverform .section3 h6').html('<span>'+ i +'</span>秒后自动跳转到登录界面');
		i--;
	},1000);
}

function clearRimer(){
	if(window._timers0)clearInterval(window._timers0);
	$('#recoverform .section3 h6').html('<span>10</span>秒后自动跳转到登录界面');
}

function disabledBtn(){
	$("#getCode").text('已发送').addClass('disabled').unbind('click');
	var i = 90;
	window._timers = setInterval(function(){
		if(i<1){ 
			clearTimer();
			return false;
		}
		$("#getCode").text(i);
		i--;
	},1000);
}
function clearTimer(){
	if(window._timers)clearInterval(window._timers);
	$("#getCode").removeClass('disabled').text('获取验证码').bind('click',sentevent);
}
function sentevent(){
	var telphone=$("#telphone").val();
	if(!(/^1[3|5|7|8][0-9]\d{4,8}$/.test(telphone))){ 
        showDialog("重置密码信息","<span style='color:red;'>请输入有效的手机号码。</span>");
        $("#telphone").focus();
        return false; 
    }
    disabledBtn();
    $.ajax({    
        url: _burl + 'doctor/getsmscode',
        data: {
        	telphone:telphone
        },
        type:'post',
        success:function(data) {
        	if(data.status=="success"){
				showDialog("重置密码信息","<span style='color:red;'>"+ (data.messages || '验证码发送成功。') +"</span>");
			}else if(data.status=="error"){
				showDialog("重置密码信息","<span style='color:red;'>"+ (data.messages || '查无此手机号，请确认手机号是否正确。') +"</span>");
				clearTimer();
			}
        },    
        error : function() {
        	clearTimer();
        }    
    });
}

function fillInput(bol){
	var userNameValue = getCookieValue("dXNlck5hbWU=");
	var passwordValue = getCookieValue("cGFzc3dvcmQ=");
	
	userNameValue.length && $('[name="username"]').val(window.BASE64.decoder(userNameValue));
	bol && passwordValue.length && $('[name="password"]').val(window.BASE64.decoder(passwordValue)); 
}
function faveInput(bol){
	var userNameValue = $('[name="username"]').val();
	var passwordValue = $('[name="password"]').val();
	var userNameValuec = window.BASE64.decoder(getCookieValue("dXNlck5hbWU="));
	var passwordValuec = window.BASE64.decoder(getCookieValue("cGFzc3dvcmQ="));
	
	userNameValue != userNameValuec && setCookie("dXNlck5hbWU=",window.BASE64.encoder(userNameValue),2400,"/");
	bol ? (passwordValue != passwordValuec && setCookie("cGFzc3dvcmQ=",window.BASE64.encoder(passwordValue),2400,"/")):
		deleteCookie("cGFzc3dvcmQ=","/");
}

function setCookie(name, value, hours, path) {
  var name = escape(name);
  var value = escape(value);
  var expires = new Date();
  expires.setTime(expires.getTime() + hours * 3600000);
  path = path == "" ? "" : ";path=" + path;
  _expires = (typeof hours) == "string" ? "" : ";expires=" + expires.toUTCString();
  document.cookie = name + "=" + value + _expires + path;
}

function getCookieValue(name) {
  var name = escape(name);
  var allcookies = document.cookie;
  name += "=";
  var pos = allcookies.indexOf(name);
  if (pos != -1) {
      var start = pos + name.length;
      var end = allcookies.indexOf(";", start);
      if (end == -1) end = allcookies.length;
      var value = allcookies.substring(start, end); 
      return (value); 
  } else return ""; 
}

function deleteCookie(name, path) {
  var name = escape(name);
  var expires = new Date(0);
  path = path == "" ? "" : ";path=" + path;
  document.cookie = name + "=" + ";expires=" + expires.toUTCString() + path;
}