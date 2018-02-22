<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 

<!DOCTYPE html>
<html class="html100">
  <head>
  	<base href="/">
	<meta charset="UTF-8">
	<title>护士挂号</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" />
	<link rel="stylesheet" href="/css/view/nurse.css" />
  </head>  
  <body class="html100">
  	<div class="detailsection clearfix html100">
  		<div class="sectionleft">
  			<div class="header">
  				<span>专家详情：</span>
  			</div>
  			<div class="sectionuserinfo prelative">
  				<div class="thumb">
  					<c:choose>
  						<c:when test="${fn:contains(special.listSpecialPicture, '://')}">
							<img src="${fn:replace(special.listSpecialPicture,'http://','https://')}"/>
  						</c:when>
  						<c:otherwise>
  							<img src="http://wx.15120.cn/SysApi2/Files/${special.listSpecialPicture }"/>
  						</c:otherwise>
  					</c:choose>
  					
  				</div>
  				<div class="text">
  					<div class="ubaseinfo">
  						<span>${special.specialName }&ensp;${special.specialTitle }</span>
  						<span>${special.hosName }&ensp;${special.depName }</span>
  					</div>
  					<div class="star">
  						星级评价：<i class="icon icon5"><i class="iconfont">&#xe623;</i><i class="iconfont">&#xe623;</i><i class="iconfont">&#xe623;</i><i class="iconfont">&#xe623;</i><i class="iconfont">&#xe623;</i></i>
  					</div>
  					<div class="goodat "><!-- overflowhidden2em -->
  						擅长：${special.specialty }
  					</div>
  				</div>
  			</div>
  			<div class="sectiondetailinfo">
  				<div class="detailinfo"><!-- overflowhidden3em -->
  					个人介绍：${special.specialty }
  				</div>
  				<!-- <p><a href="javascript:void(0)" class="showmore">展开更多<i class="iconfont">&#xe600;</i></a></p> -->
  			</div>
  			<div class="split1"></div>
  			<!-- <div class="header">
  				<span>医生评价：</span>
  			</div> -->
  		</div>
  		<div class="sectionright prelative">
  			<div class="header">
  				<span>出诊时间：</span>
  			</div>
  			<div class="timelists"></div>
  			<div class="split1"></div>
  			<div class="header">
  				<span>患者信息：</span>
  			</div>
  			<form action="/nuradmin/registwinfo" method="post" class="row-fluid formpost">
  				<input type="hidden" name="sid" value="${sid }"/> 
  				<input type="hidden" name="depid" value="${depid }"/> 
  				<input type="hidden" id="times" name="timeid"/> 
				<div class="control-groups">
					<label class="control-label">姓名：</label>
					<div class="controls">
						<input type="text" name="patientName"/>
					</div>
				</div>
  				<div class="split2"></div>
				<div class="control-groups">
					<label class="control-label">身份证号：</label>
					<div class="controls">
						<input type="text" name="idcard"/>
					</div>
				</div>
  				<div class="split2"></div>
				<div class="control-groups">
					<label class="control-label">联系电话：</label>
					<div class="controls">
						<input type="text" name="telphone"/>
					</div>
				</div>
  				<div class="form-action">
  					<button type="button" class="postFrom">收费${special.vedioAmount}元挂号</button>
  				</div>
  			</form>
  		</div>
  	</div>
 	<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
	<script src="/sea-modules/bootstrap/js/bootstrap.min.js"></script>
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
	<script>
		var _href = '/';
		var _sdate = '${sdate}',_timetype = '${timetype}',_sid = '${sid}',_depid = '${depid}';
		var _noresult = '<div class="noresult"><img src="'+ window.location.origin +'/img/mobile/kbzs/icon_noresult@2x.png" style="width:58px" alt=""/></div>';
		seajs.use('view/nurse/main',function(controller){
			controller.detail();
		});
	</script>
  </body>
</html>
