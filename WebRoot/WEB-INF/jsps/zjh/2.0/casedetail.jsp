<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<!DOCTYPE html>
<html>
  <head>
    <base href="/">
    <title>我的病例</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
	<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
	<link rel="stylesheet" href="/css/view/mycase.css" />
  	<script type="text/javascript">
  		!function(n){var e=n.document,t=e.documentElement,i=720,d=i/100,o="orientationchange"in n?"orientationchange":"resize",a=function(){var n=t.clientWidth||320;n>720&&(n=720),t.style.fontSize=n/d+"px"};e.addEventListener&&(n.addEventListener(o,a,!1),e.addEventListener("DOMContentLoaded",a,!1))}(window);
  	</script>
  	<script src="http://api.html5media.info/1.1.8/html5media.min.js"></script>
  </head>  
  <body>
    <div class="index reportdetail">
    	<section>
    		<header>挂号类别：名医面诊服务</header>
    		<dl>
    			<dt>${special.hosName }&emsp;${special.depName }</dt>
    			<dd>预约医生：<span>${special.specialName }</span></dd>
    			<dd>预约日期：<span>${ocase.conDate }</span></dd>
    		</dl>
    	</section>
    	<section>
    		<header>检查报告</header>
    		<article>
    			<span>${ocase.consultationResult }</span>
    		</article>
    	</section>
    	<c:if test="${fn:length(vedios) > 0}">
	    	<section>
	    		<header class="box"><span class="cols1">视频演示</span><span class="cols0 help" onclick="document.querySelector('.helpTip').style.display='block'"><i class="iconfont">&#xe623;</i>无法播放？</span></header>
	    		<article class="clearfix">		
	    			<c:forEach items="${vedios }" var="v">
	    				<div class="videolist">
	    					<a class="videotitle whitespace" href="${v.fileUrl }" target="_blank"><i class="iconfont">&#xe61f;</i>${v.fileName }</a>	
	    					<video controls preload autobuffer></video>
	    				</div>
	    			</c:forEach>				
	    		</article>
	    	</section>
    	</c:if>
    	<section>
    		<header>检查检验</header>
    		<article id="listable" class="clearfix"></article>
    	</section>
    	<section>
    		<header>影像图片</header>
    		<article id="pacs" class="clearfix"></article>
    	</section>
    </div>
    <div class="helpTip" style="display:none;">
    	<div class="g_fixed bg"></div>    	
    	<div class="g_fixed content" onclick="document.querySelector('.helpTip').style.display='none'">
    		<h3>IOS上视频无法播放？</h3>
    		<p>步骤一：点击视频名称,打开一个新页面;</p>
    		<p>步骤二：点击右上角"..."更多，在Safari中打开;</p>
    		<p>步骤三：点击用正文中间的推送按钮;</p>
    		<p>步骤四：选择手机本地已装的播放器播放;</p>
    	</div>
    </div>
	<script src="/js/jquery-1.11.0.min.js"></script>
	<script src="/sea-modules/bootstrap/js/bootstrap.min.js"></script>
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
	<script>
		var _href = '/',_orderid = '${oid}',_oid = _orderid;
		var _noresult = '<div class="noresult"><img src="'+ window.location.origin +'/img/mobile/kbzs/icon_noresult@2x.png" style="width:58px" alt=""/></div>';
		window.parent['_sid'] = _oid;
		seajs.use('view/vedio/mainmobile');
	</script>
	<script type="text/html" id="report_model">
			<div class="report small">
				<h4>六安市立医院{record_name}</h4>
				<div class="reportbaseinfo clearfix">
					<span class="baseitem">
						<label>姓名：</label><span>{姓名}</span>
					</span>
					<span class="baseitem">
						<label>性别：</label><span>{性别}</span>
					</span>
					<span class="baseitem">
						<label>年龄：</label><span>{年龄}</span>
					</span>
					<span class="baseitem">
						<label>标识号：</label><span>{标识号}</span>
					</span>
					<span class="baseitem">
						<label>床号：</label><span>{当前床号}</span>
					</span>
					<span class="baseitem">
						<label>送检医生：</label><span>{送检医生}</span>
					</span>
					<span class="baseitem">
						<label>科室：</label><span>{送检科室}</span>
					</span>
					<span class="baseitem">
						<label>标本类型：</label><span>{标本类型}</span>
					</span>
					<span class="baseitem">
						<label>标本号：</label><span>{标本序号}</span>
					</span>
					<span class="baseitem">
						<label>检验项目：</label><span>{检验项目}</span>
					</span>
				</div>
				<div class="reporttable clearfix">
					<div class="span6">
						<table class="table">
							<thead>
								<tr>
									<th></th>
									<th>检验项目名称</th>
									<th>结果</th>
									<th>标志</th>
									<th>参考区间</th>
									<th>单位</th>
								</tr>
							</thead>
							<tbody>{tbody}</tbody>
						</table>
					</div>
				</div>
				<div class="reportfooter clearfix">
					<span class="baseitem">
						<label>接收时间：</label><span>{接收时间}</span>
					</span>
					<span class="baseitem">
						<label>报告时间：</label><span>{报告时间}</span>
					</span>
					<span class="baseitem">
						<label>检验医师：</label><span>{检验医师}</span>
					</span>
					<span class="baseitem">
						<label>审核医师：</label><span>{审核医师}</span>
					</span>
					<span class="baseitem">
						<label>采样时间：</label><span>{采样时间}</span>
					</span>
					<span class="baseitem">
						<label>报告备注：</label><span>{检验备注}</span>
					</span>
				</div>
				<div class="reportremark">
					<h6>**此结果仅对本标负责**  如对检验结果有疑义，请在报告发出三日内与检验科联系核对复查。</h6>
				</div>
			</div>
	</script>
  </body>
</html>
