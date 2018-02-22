<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="/">
    <title>医生详情</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" href="/css/bootstrap.min.css" />
		<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
		<link rel="stylesheet" href="/css/view/admin.css" />
		<style>
				label{float: left; text-align: right; width: 160px;}
				.section .box{ margin-bottom: 10px; min-height: 35px}
				.section div span{ margin-left: 10px}
				p{ display: inline-block}
				img{ width: 80px; height: 80px}
				.flex{ display: -webkit-box; display: -webkit-flex!important; display: flex; }
				.flex_1{ -webkit-box-flex: 1; -webkit-flex:1; flex:1;}
		</style>
  </head>
  
  <body>
		<div class="userinfo" style="padding-left: 20px">
            <div class="exportdetail">
                <div class="header clearfix baseformaction">
                    <h3>医生详情</h3>								
                </div>
                <div class="bodyer" style="margin-bottom: 20px">
                    <div class="section">
                        <div class="box flex"><label>医生姓名：</label><span class="flex_1">${doctors.specialName}</span></div>
                        <div class="box flex"><label>电话：</label><span class="flex_1">${doctors.mobileTelphone}</span></div>
                        <div class="box flex"><label>身份证号：</label><span class="flex_1">${doctors.idCardNo}</span></div>
                        <div class="box flex"><label>职务：</label><span class="flex_1">${doctors.duty}</div>
                        <div class="box flex"><label>职称：</label><span class="flex_1">${doctors.profession}</span></div>
                        <div class="box"><label>所在省市：</label><span>${doctors.shengName }</span><span>${doctors.shiName }</span></div>
                        <div class="box"><label>所在医院/科室：</label><span>${doctors.hosName }</span><span>${doctors.depName }</span></div>
                        <div class="box flex"><label>地址：</label><span class="flex_1">${doctors.position }</span></div>
                        <div class="box flex"><label>擅长领域：</label><span class="flex_1">${doctors.specialty }</span></div>
                        <div class="box flex"><label>简介：</label><span class="flex_1">${doctors.profile }</span></div>
                        <div class="flex">
                            <label>开通服务：</label>
                            <div class="flex_1" style="margin-left: 10px">
                                <div>
                                    <p><input type="checkbox" name="openVedio" disabled <c:if test="${doctors.openVedio == '1' || empty(doctors.specialId) }">checked</c:if> value="1"/><span>视频会诊</span></p>
                                    <span style="<c:if test="${doctors.openVedio != '1' && !empty(doctors.specialId) }">display:none</c:if>">￥ ${doctors.vedioAmount} 元</span>
            
                                </div>
                                <div>
                                    <p ><input type="checkbox" name="openAsk" disabled <c:if test="${doctors.openAsk == '1' || empty(doctors.specialId) }">checked</c:if> value="1"/><span>图文咨询</span></p>
                                    <span style="<c:if test="${doctors.openAsk != '1' && !empty(doctors.specialId) }">display:none</c:if>">￥ ${doctors.askAmount} 元</span>
                                </div>
                                <div>
                                    <p><input type="checkbox" name="openTel" disabled <c:if test="${doctors.openTel == '1' || empty(doctors.specialId) }">checked</c:if> value="1"/><span>电话问诊</span></p>
                                    <span style="<c:if test="${doctors.openTel != '1' && !empty(doctors.specialId) }">display:none</c:if>">￥ ${doctors.telAmount} 元</span>
                                </div>
                                <div>
                                    <p><input type="checkbox" name="openAdvice" disabled <c:if test="${doctors.openAdvice == '1' || empty(doctors.specialId) }">checked</c:if> value="1"/><span>专家咨询</span></p>
                                    <span style="<c:if test="${doctors.openAdvice != '1' && !empty(doctors.specialId) }">display:none</c:if>">￥ ${doctors.adviceAmount} 元</span>
                                </div>
                            </div>
                        </div>
                        <div style="height: 80px;margin-bottom: 10px;">
                            <label>医生头像：</label>
                            <c:if test="${fn:length(doctors.listSpecialPicture) > 1}">
                                <img src="${doctors.listSpecialPicture}" alt="">
                            </c:if>
                        </div>
                        <div style="margin-bottom: 10px; height: 80px">
                            <label>医生执照/工牌：</label>
                            <c:forEach items="${images}" var="im">
                                <img src="${im.badgeUrl}" />
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
		</div>
  </body>
</html>
