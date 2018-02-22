<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head lang="en">
		<meta charset="UTF-8">
		<title>报告详情</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta content="telephone=no" name="format-detection" />
		<link rel="stylesheet" href="/css/bootstrap.min.css" />
		<link rel="stylesheet" href="/css/view/video.css" />
		<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
		<style type="text/css">
			.row-fluid .span7 {
			    width: 80%;
			}
			.form-horizontal .control-group.span4 {width: 19%;}
			.userinfo .section .bodyer {background-color: #FEFEFE;min-height: 0px; }
			.input{width: 150px!important;}
		</style>
	</head>
	<body>
		<div class="container-fluid">
			<div class="row-fluid">
				
				<div class="span7">				
					<form class="row-fluid form-horizontal"  method="post">
						<div class="span12 backgroundfff">
							<div class="userinfo">
								<div class="header clearfix baseformaction">
									<span class="span6"><span class="title">${bean.kvs['报告名称']}</span></span>	
								</div>
								<div class="bodyer">
									<div class="section">
										<div class="bodyer clearfix">
											<div class="control-group span4">
											    <label class="control-label">姓名：</label>
											    <div class="controls">
											    	<span class="input">${bean.kvs['采样人']}</span>
											    </div> 
											</div>
											<div class="control-group span4">
											    <label class="control-label">性别：</label>
											    <div class="controls">
											    	<span class="input">${bean.kvs['性别']}</span>
											    </div>
											</div>
											<div class="control-group span4">
											    <label class="control-label">年龄：</label>
											    <div class="controls">
											    	<span class="input">${bean.kvs['年龄']}</span>
											    </div>
											</div>
											<div class="control-group span4">
											    <label class="control-label">标识号：</label>
											    <div class="controls">
											    	<span class="input">${bean.kvs['标识号']}</span>
											    </div>
											</div>
											<div class="control-group span4">
											    <label class="control-label">床号：</label>
											    <div class="controls">
											    	<span class="input">${bean.kvs['床号']}</span>
											    </div>
											</div>
										</div>
										<div  class="bodyer clearfix">
											<div class="control-group span4">
											    <label class="control-label">送检医生：</label>
											    <div class="controls">
											    	<span class="input">${bean.kvs['申请人']}</span>
											    </div>
											</div>
											<div class="control-group span4">
											    <label class="control-label">科室：</label>
											    <div class="controls">
											    	<span class="input">${bean.kvs['病人科室']}</span>
											    </div>
											</div>
											<div class="control-group span4">
											    <label class="control-label">标本类型：</label>
											    <div class="controls">
											    	<span class="input">${bean.kvs['标本类型']}</span>
											    </div>
											</div>
											<div class="control-group span4">
											    <label class="control-label">标本号：</label>
											    <div class="controls">
											    	<span class="input">${bean.kvs['标本序号']}</span>
											    </div>
											</div>
											<div class="control-group span4">
											    <label class="control-label">检验项目：</label>
											    <div class="controls">
											    	<span class="input">${bean.kvs['检验项目']}</span>
											    </div>
											</div>
										</div>
									</div>
								</div>
								<div class="bodyer">
									<div class="section">
										<div class="bodyer clearfix">
											<div class="control-group span4">
											    <label class="control-label">接收时间：</label>
											    <div class="controls">
											    	<span class="input">${bean.kvs['核收时间']}</span>
											    </div>
											</div>
											
											<div class="control-group span4">
											    <label class="control-label">检验医师：</label>
											    <div class="controls">
											    	<span class="input">${bean.kvs['检验人']}</span>
											    </div>
											</div>
											<div class="control-group span4">
											    <label class="control-label">审核时间：</label>
											    <div class="controls">
											    	<span class="input">${bean.kvs['审核时间']}</span>
											    </div>
											</div>
											<div class="control-group span4">
											    <label class="control-label">审核医师：</label>
											    <div class="controls">
											    	<span class="input">${bean.kvs['审核人']}</span>
											    </div>
											</div>
										</div>
										<div  class="bodyer clearfix">
											<div class="control-group span4">
											    <label class="control-label">采样时间：</label>
											    <div class="controls">
											    	<span class="input">${bean.kvs['采样时间']}</span>
											    </div>
											</div>
										</div>
									</div>
								</div>
								
							</div>
						</div>
					</form>
				</div>
			</div>			
		</div>
	</body>
</html>
