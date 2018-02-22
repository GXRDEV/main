
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String _basePath="https://localhost:8443/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE html>
<html>
	<head lang="en">
		<meta charset="UTF-8">
		<title>
			随访详情
		</title>
    	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta content="telephone=no" name="format-detection" />
		<link rel="stylesheet" href="/css/bootstrap.min.css" />
		<link rel="stylesheet" href="/fonticon/base/iconfont.css" />
		<link rel="stylesheet" href="/css/view/video.css" />
		<link rel="stylesheet" href="/sea-modules/webuploader/webuploader.css" />
		<style type="text/css">
			body{background:#fafafa;}	
			.divblock{background:#fff;}
			.dialog .bodyer{padding: 3em 0 5px;height:auto;min-height:100px;}
			.doc{ margin-right: 1em}
			.me{ margin-left: 1em}
			.main{ width: 50%; margin: 0 auto}
			.viewer-toolbar li{ line-height: 0}
		</style>
	</head>
	<body>
		<div class="container-fluid userinfo">		
			<div class="row-fluid">					
				<div class="main">
					<div class="dialog section divblock">
						<div class="header"> 
							<span class="stitle">
								<i class="iconfont">&#xe600;</i>聊天消息
							</span>
						</div>
						<div class="bodyer">
							<c:forEach items="${datas }" var="im">													
								<div class="timer">${im.msgTime }</div>
								<div class="hhlist clearfix<c:if test="${im.sendType != '3' }"> me</c:if><c:if test="${im.sendType == '3' }"> doc</c:if>">								
									<span class="cols0"><span class="thumb"><img src="https://develop.ebaiyihui.com:443/img/defdoc.jpg"></span></span>
									<span class="cols1"><span class="text hasimgview">
										<c:choose>
											<c:when test="${im.msgType == 'RC:VcMsg'}">
												<button class="voiceInfo" messageUId="5FNN-5MIH-S1VP-FT2O" data-id="${im.msgContent}">语音播放</button>
											</c:when>
											<c:when test="${im.msgType == 'RC:ImgMsg'}">
												<img src="${im.fileUrl }" style="width:150px; cursor: pointer"/>
											</c:when>
											<c:otherwise>
												${im.msgContent }&emsp;
											</c:otherwise>   
										</c:choose>								
									</span></span>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>			
		</div>
		<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
		<script src="/sea-modules/bootstrap/js/bootstrap.min.js"></script>
		<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
		<script src="/sea-modules/seajs/seajs.config.js"></script>
		<script src="/sea-modules/rong/Libamr-2.2.5.min.js"></script>
		<script src="/sea-modules/rong/RongIMVoice-2.2.5.js"></script>
		<script>
			var _href = '/',_h = _href;
			seajs.use('view/admin/order',function(controller){
				controller.tuwen();
			});
			var RongIMVoice = RongIMLib.RongIMVoice;
			RongIMVoice.init();
			$(".voiceInfo").on("click",function(){
				var msgContent = $(this).data('id')
				if(msgContent){
					var duration = msgContent.length/1024;    // 音频持续大概时间(秒)
					if(!!window.ActiveXObject || "ActiveXObject" in window){
						//如果是 IE 浏览器
						RongIMVoice.preLoaded(msgContent);
						RongIMVoice.play(msgContent,duration);
					}else{
						RongIMVoice.preLoaded(msgContent, function(){
							RongIMVoice.play(msgContent,duration);
						});
					}
				}else{
					console.error('请传入 amr 格式的 base64 音频文件');
				}
			});
			
		</script>
	</body>
</html>
