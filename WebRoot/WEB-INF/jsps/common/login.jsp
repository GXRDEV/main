<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en" class="bootstrapstyle<c:if test="${ltype=='2' || ltype=='3' || ltype=='4' || ltype=='5'}">3</c:if>">
    
<head>
    <title>"佰医汇"云SaaS平台</title>
    <meta charset="UTF-8" />
	<jsp:include page="../icon.jsp" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/css/bootstrap-responsive.min.css" />
    <link rel="stylesheet" href="/css/matrix-login.css" />
    <link href="/font-awesome/css/font-awesome.css" rel="stylesheet" />
    <link href="/fonticon/tspzlogin/iconfont.css" rel="stylesheet" />
    <style type="text/css">
        input{ font-family: "Microsoft Yahei";}
        .typesel .control-label{float: left;}
        .btn-group{float: right;padding:0 5px 30px;}
        .btn-group .btn{background-color:gray;}
        .btn-group .btn.active{background-color:#04c;color:white;}
    </style>
    <script type="text/javascript">
    	window.self != window.top && (window.parent.location.href = "/doctor/login?ltype=${ltype}");
    </script>
</head>
<body onkeydown="keydown()">
    <div id="loginbox" class="clearfix">
    	<c:if test="${ltype=='2' || ltype=='3' || ltype=='4' || ltype=='5'}">
    		<div id="picplacehold" class="wleftper">
    			<c:if test="${ltype=='3' || ltype=='4' || ltype=='5'}">			
    				<h3 class="h"><span>|</span>佰医汇医院后台管理入口</h3>
    				<img class="img1" src="/img/login/hosimg.png"/>
    			</c:if> 
    			<c:if test="${ltype=='2'}">			
    				<img class="img1" src="/img/login/expimg.png" style="width:55%"/>
    				<h2 class="h" style="color:#1DA982;font-weight:400;font-size:20px;letter-spacing:1em;margin-top:1em; text-indent: 0.8em;">远程医疗领跑者</h2>
    			</c:if>  			
    			<img class="img2" src="/img/login/hossplit.png"/>
    		</div>
    	</c:if>
        <form id="loginform" class="form-vertical newstyle w50per" action="#">
	        <div class="control-group normal_text formtitle h <c:if test="${ltype=='2'}">exphead</c:if>"> 
	        	<c:choose> 
					<c:when test="${ltype == '2'}">
						<h2>"佰医汇"医生管理系统专家入口</h2>
					</c:when> 
					<c:when test="${ltype == '3'}">
						<a href="javascript:void(0)" class="selected" data-type="3">医院医生</a>
						<a href="javascript:void(0)" data-type="4">医院护士</a>
						<a href="javascript:void(0)" data-type="5">医院管理员</a>
					</c:when> 
					<c:when test="${ltype == '4'}">
						<a href="javascript:void(0)" class="selected" data-type="4">医院护士</a>
						<a href="javascript:void(0)" data-type="3">医院医生</a>
						<a href="javascript:void(0)" data-type="5">医院管理员</a>
					</c:when>
					<c:when test="${ltype == '5'}">
						<a href="javascript:void(0)" class="selected" data-type="5">医院管理员</a>
						<a href="javascript:void(0)" data-type="3">医院医生</a>
						<a href="javascript:void(0)" data-type="4">医院护士</a>
					</c:when>  
					<c:otherwise>
						<h2>"佰医汇"云SaaS平台</h2>
					</c:otherwise> 
				</c:choose> 	            
	        </div>  
            <div class="control-group">
                <label class="control-label">登陆账号</label>
                <div class="controls">
                    <div class="main_input_box">
                        <span class="add-on bg_lg"><i class="icon-user" style="font-size:16px;"></i></span>
                        <input type="text" name="username" autocomplete="off" placeholder="手机号"/>
                        <label class="error forusername"></label>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">登陆密码</label>
                <div class="controls">
                    <div class="main_input_box">
                        <span class="add-on bg_ly"><i class="icon-lock" style="font-size:16px;"></i></span>
                        <input type="password" name="password" autocomplete="off" placeholder="密码"/>
                        <label class="error forpassword"></label>
                    </div>
                </div>
            </div>
            <div class="form-actions">
            	<span class="pull-left"><a href="javascript:void(0)" class="flip-link btn btn-info" id="to-recover">忘记密码?</a></span>
                <span class="pull-right"><input type="button" id="checkBtn" class="btn btn-success" value="登录"/></span>
                <label class="checkbox"><input type="checkbox" id="remberme" name="remberme" />记住密码</label>
            </div>
        </form>
        <form id="recoverform" action="#" class="form-vertical newstyle w50per" autocomplete="off">
	       	<div class="control-group normal_text formtitle"> 
		    	<h2>找回帐号密码</h2>
		    	<h3 class="signstep clearfix">
		    		<span class="step step1 passed">
		    			<span class="customtxt">
		    				<span class="number">
		    					<i class="iconfont">&#xe600;</i>
		    					<b>1</b>
		    				</span>
		    				<span class="text">验证手机号</span>
		    			</span>
		    		</span>
		    		<span class="step step2">
		    			<label class="points clearfix">
		    				<i class="iconfont">&#xe601;</i>
		    				<i class="iconfont">&#xe601;</i>
		    				<i class="iconfont">&#xe601;</i>
		    			</label>
		    			<span class="customtxt">
		    				<span class="number">
		    					<i class="iconfont">&#xe600;</i>
		    					<b>2</b>
		    				</span>
		    				<span class="text">设置密码</span>
		    			</span>
		    		</span>
		    		<span class="step step3">
		    			<label class="points clearfix">
		    				<i class="iconfont">&#xe601;</i>
		    				<i class="iconfont">&#xe601;</i>
		    				<i class="iconfont">&#xe601;</i>
		    			</label>
		    			<span class="customtxt">
		    				<span class="number">
		    					<i class="iconfont">&#xe600;</i>
		    					<b>3</b>
		    				</span>
		    				<span class="text">完成</span>
		    			</span>
		    		</span>
		    	</h3>
		    </div>  
		    <div class="section1 section">
	            <div class="control-group">
	                <label class="control-label">手机号</label>
	                <div class="controls">
	                   	<div class="main_input_box hascode">
	                    	<span class="add-on bg_lo"><i class="icon-phone" style="font-size:16px;"></i></span>
	                    	<input type="tel" name="telphone" id="telphone" placeholder="注册时的手机号" />
	                        <button type="button" class="btn btn-info" id="getCode">获取验证码</button>
	                	</div>
	                </div>
	            </div>            
	            <div class="control-group">
	                <label class="control-label">验证码</label>
	                <div class="controls">
	                    <div class="main_input_box">
	                        <span class="add-on bg_ly"><i class="icon-file-alt" style="font-size:16px;"></i></span>
	                        <input type="text" name="code" id="code" placeholder="注册手机收到的验证码"/>
	                    </div>
	                </div>
	            </div>          
	            <div class="form-actions">
	                <span class="pull-left"><a href="javascript:void(0)" class="flip-link btn btn-info" id="to-login">&laquo;返回登录</a></span>
	                <span class="pull-right"><a href="javascript:void(0)" class="btn btn-success" id="nextstep">下一步</a></span>
	            </div>
            </div>
		    <div class="section2 section" style="display:none">
	            <div class="control-group">
	                <label class="control-label">设置新密码</label>
	                <div class="controls">
	                   	<div class="main_input_box">
	                    	<span class="add-on bg_lo"><i class="icon-lock" style="font-size:16px;"></i></span>
	                    	<input type="password" name="re_password" placeholder="输入新密码" />
	                	</div>
	                </div>
	            </div>
	            <div class="control-group">
	                <label class="control-label">确认新密码</label>
	                <div class="controls">
	                   	<div class="main_input_box">
	                    	<span class="add-on bg_lo"><i class="icon-lock" style="font-size:16px;"></i></span>
	                    	<input type="password" name="re_confirmpassword" placeholder="再次输入新密码" />
	                	</div>
	                </div>
	            </div>                 
	            <div class="form-actions">
	                <a href="javascript:void(0)" class="btn btn-success" style="width: 50%;margin: 0 auto;" id="nextend">完成</a>
	            </div>
            </div>
		    <div class="section3 section" style="display:none">
	            <div class="control-group">
	                <h2>密码设置成功 ! </h2>
	                <h6><span>10</span>秒后自动跳转到登录界面</h6>
	            </div>                   
	            <div class="form-actions">
	                <a href="javascript:void(0)" class="btn btn-success" style="width: 50%;margin: 0 auto;" id="nextok">立即登录</a>
	            </div>
            </div>
            
            <div class="control-group normal_text">
                <!-- <div data-href="http://themedesigner.in/demo/matrix-admin/login.html" style="font-size:14px;color:gray;">推荐使用webkit内核浏览器，如chrome等</div> -->
            </div>
        </form>
        <div class="modal small hide fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
             <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 id="myModalLabel">错误警告</h3>
             </div>
             <div class="modal-body">
                <p class="error-text"><i class="icon-warning-sign modal-icon"></i><span class="confirmError"></span></p>
             </div>
		</div>
    </div>
    <div class="footer">
    	<h6>北京佰医汇医疗信息技术有限公司</h6>
    	<h6>京ICP备15015320号  京公网备11010802017313号</h6>
    </div>
    <script src="/js/jquery.min.js"></script>
    <script src="/js/matrix.login.js"></script>
    <script src="/js/jbase64.js"></script>
    <script type="text/javascript">
    	var _burl='/';
    </script>
</body>

</html>
