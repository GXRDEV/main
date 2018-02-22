<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="/">
    
    <title>我的服务</title>
   <meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="/css/bootstrap.min.css" />
<link rel="stylesheet" href="/css/matrix-style2.css" />
<link rel="stylesheet" href="/css/matrix-media.css" />
<link href="/font-awesome/css/font-awesome.css" rel="stylesheet" />
<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
<link rel="stylesheet" href="/css/view/videolist.css" />
<style>       
	
</style>		
</head>
<body>
<div class="mainlist">
  <div class="container-fluid">
    <div class="row-fluid">
      <div class="span12">
        <div class="widget-box">
          <div class="widget-content nopadding">
            <table class="table table-bordered data-table">
              <thead>
                <tr>
                  <th>服务类型</th>
                  <th>状态</th>
                  <th>金额</th>
                  <th>备注</th>
				  <th>操作</th>
                </tr>
              </thead> 
              <tbody>
              	<c:forEach items="${servers}" var="ser">
              		<tr>
              			<td>${ser.serviceName }</td>
              			<td>
              				<c:choose>
              					<c:when test="${ser.isOpen==1 }">已开启</c:when>
              					<c:otherwise>未开启</c:otherwise>
              				</c:choose>
              			</td>
              			<td>${ser.amount }</td>
              			<td>${ser.description}</td>
              			<td><a href="/expert/serdetailset/${ser.id}">设置详情</a></td>
              		</tr>
              	</c:forEach>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<script src="/js/jquery.min.js"></script>
</body>
</html>
