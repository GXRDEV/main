<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE HTML>
<html>
	<head>
		<base href="/">
	   	<title>佰医汇- APP展示</title>
	   	<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=EDGE"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
	<body>	
		<script type="text/javascript">
			var browser={
				versions:function(){
					var u = navigator.userAgent, app = navigator.appVersion;
					return {
						trident: u.indexOf('Trident') > -1, //IE内核
						presto: u.indexOf('Presto') > -1, //opera内核
						webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
						gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
						mobile: !!u.match(/AppleWebKit.*Mobile.*/)||!!u.match(/AppleWebKit/), //是否为移动终端
						ios: !!u.match(/(i[^;]+\;(U;)? CPU.+Mac OS X)/), //ios终端
						android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
						iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器
						iPad: u.indexOf('iPad') > -1, //是否iPad
						webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
					};
				}(),
				language:(navigator.browserLanguage || navigator.language).toLowerCase()
			};
			var ua = navigator.userAgent.toLowerCase();
			if(browser.versions.ios || browser.versions.iPhone){
				window.location = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.kangxin.patient';
			}
			else if(browser.versions.android){
				window.location = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.kangxin.patient';
			}
			else{
				document.writeln("<BR/>对不起，我们暂时不支持您的手机操作系统，多谢您的关注！")
			}
		</script>
	</body>
</html>
