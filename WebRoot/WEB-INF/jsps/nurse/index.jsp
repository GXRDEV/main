<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>"佰医汇"云SaaS平台</title>
    <meta charset="UTF-8" />
	<jsp:include page="../icon.jsp" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link href="/fonticon/tspzlogin/iconfont.css" rel="stylesheet" />
</head>
<body>
  	<jsp:include page="header.jsp" />
  	
    <!--main-container-part-->
    <div id="content">
        <!--breadcrumbs-->
        <div id="content-header">
			<div id="breadcrumb"> 
          		<a href="index.html" target="iframe-main" title="Go to Home" class="tip-bottom">
          			<i class="icon-home"></i> 首页 <span id="breadcrumb_span"></span>          	
          		</a>
          		<button class="fullscreenBtn" title="最大化"><i class="iconfont">&#xe604;</i></button>
          	</div>        
        </div>
        <!--End-breadcrumbs-->
        <iframe id="iframe-main" name="iframe-main" style="width:100%;" frameborder="no" border="0" allowscriptaccess="always" marginwidth="0" marginheight="0" allowfullscreen="true" wmode="opaque" allowTransparency="true" type="application/x-shockwave-flash"></iframe>
    </div>
</body>
</html>
