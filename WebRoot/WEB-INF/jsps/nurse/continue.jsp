<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
  <head>
    <base href="/">
    <title>信息认证</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
	<link rel="stylesheet" type="text/css" href="/libs/diyUpload/css/webuploader.css">
    <link rel="stylesheet" type="text/css" href="/css/diyUpload.css">
	<link rel="stylesheet" type="text/css" href="/css/nurse_register.css" />
	<style>	
		.icon1{background:url(/img/mobile/kbzs/login_user@2x.png) no-repeat 12px 3px;background-size: 16px 18px;}
	</style>
  	<script src="/js/browser.js" type="text/javascript"></script>
  </head>  
  <body>
  	<div id="index">
		<form method="post" action="/wtspz/nurseupdate.do" name="postorde" id="postorde">
			<input type="hidden" value="${nurseid}" name="id" id="nurseid"/>	
			<section>
				<header>
					<div class="group-title icon1">护士信息认证</div>
					<p class="group-destrib">您的信息我们会完全保密，请您放心填写，若不认证，您将无法接单，请谅解</p>
				</header>
				<dl>
					<dd class="controls-group">
						<label for="name" class="controls-label">姓名</label>
						<div class="controls">
							<input type="text" class="inputs" maxlength="10" name="realName" id="name"/>
						</div>
					</dd>
					<dd class="controls-group">
						<label for="man" class="controls-label">性别</label>
						<div class="controls">
							<label for="man"><input type="radio" name="sex" checked id="man" value="1"/><i></i>男</label>
							<label for="woman"><input type="radio" name="sex" id="woman" value="0"/><i></i>女</label>
						</div>
					</dd>
					<dd class="controls-group">
						<label for="card" class="controls-label">身份证号码</label>
						<div class="controls">
							<input type="text" class="inputs" maxlength="18" name="idCard" id="card"/>
						</div>
					</dd>
					<dd class="controls-group">
						<label for="jobcity" class="controls-label">工作城市</label>
						<div class="controls control-select">
							<select name="provinceId" id="province" class="halfselect">
								<option value="6">北京市</option>
							</select>

							<select name="cityId" id="city" onchange="_b.getHos()" class="halfselect">
								<option value="">请选择</option>
								<c:forEach items="${citys}" var="city">
									<option value="${city.id}">${city.name}</option>
								</c:forEach>
							</select>
						</div>
					</dd>					
					<dd class="controls-group">
						<label for="belongHosp" class="controls-label">所在医院</label>
						<div class="controls control-select">
							<select name="hospitalId" id="belongHosp" class="select" onchange="_b.getDep()">
								<option value="">请选择</option>
							</select>
						</div>
					</dd>					
					<dd class="controls-group">
						<label for="belongDep" class="controls-label">所在科室</label>
						<div class="controls control-select">
							<select name="departmentId" id="belongDep" class="select">
								<option value="">请选择</option>
							</select>
						</div>
					</dd>					
					<dd class="controls-group">
						<label for="level" class="controls-label">护士级别</label>
						<div class="controls control-select">
							<select name="gradeId" id="level" class="select">
								<option value="">请选择</option>
								<c:forEach items="${grades}" var="grade">
									<option value="${grade.itemKey}">${grade.itemValue}</option>
								</c:forEach>
							</select>
						</div>
					</dd>
					<dd class="controls-group">
						<label for="cardno" class="controls-label">证件号</label>
						<div class="controls">
							<input type="tel" class="inputs" maxlength="12" name="idNo" id="cardno"/>
						</div>
					</dd>
					<dd class="controls-group">
						<label for="qqno" class="controls-label">QQ号</label>
						<div class="controls">
							<input type="tel" class="inputs" maxlength="11" name="qqno" id="qqno"/>
						</div>
					</dd>
					<dd class="controls-group">
						<label for="qqno" class="controls-label">擅长技能</label>
						<div class="controls">
							<textarea  class="inputs"  name="beGoodAt" id="beGoodAt"></textarea>
						</div>
					</dd>
					<dd class="controls-group twoline">
						<label for="notneed" class="controls-label">正规证件照</label>
						<div class="controls">
							<div class="parentFileBox">
								<ul class="fileBoxUl">
									<li class="actionAdd">
										<div id="addfiles1" class="js-addfiles">
											<img src="//img/mobile/kbzs/m1_addpic.png" style="width:100%;height:100%;"/>
										</div>
									</li>
								</ul>
							</div>	
							<input type="hidden" class="inputs" name="idNoPhoto" id="pic1"/>
						</div>
					</dd>
					<dd class="controls-group twoline">
						<label for="notneed" class="controls-label">身份证正面照片</label>
						<div class="controls">
							<div class="parentFileBox">
								<ul class="fileBoxUl">
									<li class="actionAdd">
										<div id="addfiles2" class="js-addfiles">
											<img src="//img/mobile/kbzs/m1_addpic.png" style="width:100%;height:100%;"/>
										</div>
									</li>
								</ul>
							</div>	
							<input type="hidden" class="inputs" name="idCardPhoto" id="pic2"/>
						</div>
					</dd>
					<dd class="controls-group twoline">
						<label for="notneed" class="controls-label">执业证或资格证照片</label>
						<div class="controls">
							<div class="parentFileBox">
								<ul class="fileBoxUl">
									<li class="actionAdd">
										<div id="addfiles3" class="js-addfiles">
											<img src="//img/mobile/kbzs/m1_addpic.png" style="width:100%;height:100%;"/>
										</div>
									</li>
								</ul>
							</div>	
							<input type="hidden" class="inputs" name="qualificationPhoto" id="pic3"/>
						</div>
					</dd>
				</dl>
			</section>
			<div id="fixed" style="z-index:99;padding:1px;">
				<button type="button" name="btnsubmit" class="buttonClassgreen" style="border-radius:2px;height:46px;background-color:#3FC0F2;" id="btnsubmit">
					提 交
				</button>
			</div>
		</form>
	</div>	
	<div class="layout_div g_fixed" onclick="hidePop()" style="background:#000;opacity:0.6;height:10000px;z-index:10000;display:none;">&nbsp;</div>	
	<div id="popupMenu" class="popup-div" style="z-index:10000;background-color:#fff;display:none;">
		<a id="single" href="javascript:void(0)">拍照</a>
		<a id="many" href="javascript:void(0)">从手机相册选择</a>
		<a href="javascript:void(0)" onclick="hidePop()">取消</a>
	</div>
	<script src="/libs/jquery-1.11.0.min.js"></script>
    <script type="text/javascript" src="/libs/diyUpload/js/webuploader.0.1.1.min.js"></script>
    <script type="text/javascript" src="/libs/diyUpload/js/diyUpload.mobile.js"></script>
	<script src="/js/base.js"></script>
	<script src="/js/view/nurse_register.1.0.js"></script>
	<script>
        _b.href = '/';
        _b.nid = '${nurseid}';
        _b.oid = '${openid}';
	</script>
  </body>
</html>
<!--input type="text" class="inputs search" maxlength="18" name="belongHosp" id="belongHosp"/>
<label class="datalist" for="belongHosp"> 
    <div class="list" data-index="重庆市chongqing">重庆市</div>
    <div class="list" data-index="哈尔滨市haerbing">哈尔滨市</div>
    <div class="list" data-index="长春市changchun">长春市</div>
    <div class="list" data-index="重庆市chongqing">重庆市</div>
    <div class="list" data-index="哈尔滨市haerbing">哈尔滨市</div>
    <div class="list" data-index="长春市changchun">长春市</div>
    <div class="list" data-index="重庆市chongqing">重庆市</div>
    <div class="list" data-index="哈尔滨市haerbing">哈尔滨市</div>
    <div class="list" data-index="长春市changchun">长春市</div>
    <div class="list" data-index="重庆市chongqing">重庆市</div>
    <div class="list" data-index="哈尔滨市haerbing">哈尔滨市</div>
    <div class="list" data-index="长春市changchun">长春市</div>
</label-->
<!-- 
	var eleStyle = document.createElement("style");
	document.querySelector("head").appendChild(eleStyle);
	$('.search').each(function(){
		if (!document.addEventListener){ return alert('浏览器版本过低'),false;}
		var eleInput = this,
			eleDatalist = $(this).siblings(".datalist")[0];
			
		eleInput.addEventListener("input", function() {
			var val = this.value.trim().toLowerCase(),id = this.id;
			if (val !== '') {
				eleStyle.innerHTML = '#'+ id +' + .datalist .list:not([data-index*="'+ this.value +'"]) { display: none; }';
			} else {
				eleStyle.innerHTML = '';
			}
		});
		eleDatalist.addEventListener("mousedown", function(event) {
			eleInput.value = event.target.innerHTML;
			eleInput.blur();
		});
	});	
	
	.search {
	    padding: 5px;
	    -webkit-box-sizing: content-box;
	}
	.datalist {
		box-sizing:border-box;
		position:absolute!important;
		left:0;top:100%;right:0;   
	    width: 100%;padding:0.6em 1em!important;
	    max-height:160px;
	    background-color: #fff;
	    box-shadow: 0 1px #ccc, 1px 0 #ccc, -1px 0 #ccc, 0 -1px #ccc;
	    overflow: auto;line-height:30px;
	    display:none!important; z-index: 100;
	}
	.datalist .list{white-space:nowrap; text-overflow:ellipsis; overflow: hidden;}
	.search:focus + .datalist {
	    display: block!important;
	}
 -->
