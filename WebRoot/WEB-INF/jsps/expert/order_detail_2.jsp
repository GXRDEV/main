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
		<title>订单详情--专家</title>
    	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta content="telephone=no" name="format-detection" />
		<link rel="stylesheet" href="/css/bootstrap.min.css" />
		<link rel="stylesheet" href="/sea-modules/jqueryUI/drag&resize/jquery-ui.min.css" />
		<link rel="stylesheet" href="/fonticon/base/iconfont.css" />
		<link rel="stylesheet" href="/css/view/video.css" />
		<link rel="stylesheet" href="/css/view/video.exp.css" />
		<style type="text/css">
			.modal2 .form-horizontal .controls {padding-top: 5px;}
		</style>
	</head>
	<body>
		<!-- 菜单 -->
		<dl id="menulist" class="fixed">
			<dt id="mainmenu" class="btncircle" title="点击展开或收缩菜单"><i class="iconfont">&#xe62e;</i></dt>
			<dd id="videoBtn" class="btncircle" data-bottom="160px" data-right="-10px"><i class="iconfont">&#xe631;</i><span>视频问诊</span></dd>
			<dd id="formBtn" class="btncircle" data-bottom="140px" data-right="77px"><i class="iconfont">&#xe633;</i><span>病历信息</span></dd>
			<dd id="reportBtn" class="btncircle" data-bottom="77px" data-right="140px"><i class="iconfont">&#xe62f;</i><span>填写报告</span></dd>
			<dd id="dialogBtn" class="btncircle" data-bottom="-10px" data-right="160px"><i class="iconfont">&#xe634;</i><span>图文对话</span></dd>
		</dl>
		<!-- 填写报告 -->
		<div id="reporttxt" class="row-fluid fixed" style="display:none;">
			<div class="bgopacity"></div>
			<div class="baogao">
				<div class="header" id="resizeheight" style="cursor:ns-resize;"> 
					<span class="title"></span>
					<button class="closebtn"><i class="iconfont">&#xe61e;</i></button>
				</div>
				<div class="bodyer">
					<p><textarea class="fillbaogao" placeholder="请在此写结论给患者"></textarea></p>
					<button class="savereport"><span>发送报告</span></button>
				</div>
			</div>
		</div>
		<!-- 视频对话模块 -->
		<div id="vedioOuter" class="menufordiv" style="display:none;">
			<div class="vedio" id="main">
				<div class="stateBar" id="stateBar" data-state="${order.localHosName}/${order.localDepName}/${order.localDocName}">
					<span>远程会诊</span>
					<button class="minscreen" title="最小化"><i class="iconfont">&#xe629;</i></button>
					<button class="fullscreen" title="全屏"><i class="iconfont">&#xe608;</i></button>
				</div>
				<!-- controls -->
				<video id="remoteVideo" autoplay="autoplay" style="display:none;"></video>
				<audio id="localAudio" autoplay loop style="display:none;"></audio>
				<video id="localVideo" autoplay="autoplay" muted class="big"></video>
				<video id="miniVideo" autoplay="autoplay" muted class="small"></video>
				<div class="controls" style="display:none;">
					<div class="c_bg"></div>
					<div class="c_ctrs clearfix">
						<a class="videoswitch btn-switch animate1" id="btn-switch" href="javascript:videoClick()">发起视频问诊</a>
						<div class="videotiptxt" style="display:none;">
							医生已经就绪，点击接受进入视频会诊
						</div>
					</div>
				</div>
				<div id="footer" class="signtips"></div>
				<div id="firsttip" class="signtips">
					<c:if test = "${order.progressTag == '4'}">
						<label>对方医生已就绪，可以发起视频问诊</label>
					</c:if>
					<c:if test = "${order.progressTag != '4'}">
						<label>等待医生准备就绪后，再发起连接</label>
					</c:if>	
				</div>
				<div id="timers" style="display:none;"></div>
			</div>
		</div>
		<!-- 图文对话模块 -->
		<div id="pictxtdialog" class="menufordiv row-fluid windows" style="display:none">
			<div class="span12 backgroundfff">
				<div class="dialog">
					<div class="header"> 
						<span class="title">图文对话 </span>
						<button class="smallscreen" title="最小化" style="top:-8px;"><i class="iconfont">&#xe60a;</i></button>
					</div>
					<div class="bodyer">
						<div class="timer"></div>
					</div>
					<div class="footer clearfix">
						<div class="span10">
							<div class="inputandimg">
								<div class="selectimg">
									<i class="iconfont">&#xe606;</i>
									<div id="selectfile"></div>
								</div>
								<div class="selectinput">
									<input type="text" name="sendtext"  id="sendtext" placeholder="请在此输入内容" onkeypress="getKey();"/>											
								</div>
							</div>
						</div>
						<div class="span2">
							<button type="button" class="btnsent">发送</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 病历信息模块 -->
		<form id="myform" class="menufordiv row-fluid form-horizontal windows" method="post">
			<!-- 专家报告模块<c:if test="${fn:length(order.consultationResult) < 1}"> style="display:none"</c:if> -->
			<div id="expreport" class="menufordiv row-fluid"<c:if test="${fn:length(order.consultationResult) < 1}"> style="display:none"</c:if>>
				<div class="span12 backgroundfff" style="margin:0;">
					<div class="reportresult">
						<div class="header"> 
							<span class="title">专家咨询意见</span>
							<button class="smallscreen" title="最小化" type="button" style="top:-8px;"><i class="iconfont">&#xe60a;</i></button>
						</div>
						<div class="bodyer">
							<div class="reportxt">${order.consultationResult }</div>
						</div>
					</div>
				</div>
				<div style="height:10px;background:#eee;clear:both">&ensp;</div>
			</div>
			<div class="backgroundfff" style="margin:0px;">
				<div class="userinfo">
					<div class="header clearfix baseformaction">
						<h3>患者病历查看</h3>	
						<button class="smallscreen" title="最小化" type="button"><i class="iconfont">&#xe60a;</i></button>
					</div>
					<div class="bodyer">
						<div class="section">
							<div class="header clearfix">
								<span class="span12 stitle"><i class="iconfont">&#xe600;</i>患者基本信息</span>
							</div>
							<div class="bodyer clearfix">
								 <div class="control-group span6">
								    <label class="control-label">姓名：</label>
								    <div class="controls"><span class="readonly">${order.patientName }
								    	<script type="text/javascript">
								    		(function(UUserCard,_sex,_age){
								    			var idcard15 = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/,
								    				idcard18 = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
								    			if (idcard15.test(UUserCard) || idcard18.test(UUserCard)){
									    			var sex,age,sexcode,year = UUserCard.substring(6, 10),gmonth = UUserCard.substring(10, 12),gday = UUserCard.substring(12, 14);
													var myDate = new Date(),month = myDate.getMonth() + 1,day = myDate.getDate(); 
													if (parseInt(UUserCard.substr(16, 1)) % 2 == 1) { 
														sex = _sex || '男',sexcode = '1';
													} else { 
														sex = _sex || "女",sexcode = '0';
													} 
													age = _age || (myDate.getFullYear() - UUserCard.substring(6, 10) - 1); 
													if (gmonth < month || gmonth == month && gday <= day) { age++; } 
													document.write('/ '+ sex +' / ' + age + '岁');
												}else{
													_sex = _sex ? ('/' + _sex) : '';
													_age = _age ? ('/' + _age + '岁') : '';
													document.write(_sex + _age);
												}
								    		})('${caseinfo.idNumber }','${caseinfo.sex }' == '1' ? '男' : '女','${caseinfo.age }');
								    	</script>
								    </span></div>
								 </div>
								 <div class="control-group span6">
								    <label class="control-label">身份证号：</label>
								    <div class="controls"><span class="readonly">${order.idCard }</span></div>
								 </div>
								<%--  <div class="control-group span6">
								    <label class="control-label">地址：</label>
								    <div class="controls"><span class="readonly">${order.localAddress }</span></div>
								 </div> --%>
								 <div class="control-group span6">
								    <label class="control-label">联系电话：</label>
								    <div class="controls"><span class="readonly">${order.telephone }</span></div>
								 </div>
								 <div class="control-group span12">
								    <label class="control-label">主诉：</label>
								    <div class="controls"><span class="readonly">${caseinfo.mainSuit}</span></div>
								 </div>
								 <div class="control-group span12">
								    <label class="control-label">现病史：</label>
								    <div class="controls"><span class="readonly">${caseinfo.presentIll}</span></div>
								 </div>
								 <div class="control-group span12">
								    <label class="control-label">既往史：</label>
								    <div class="controls"><span class="readonly">${caseinfo.historyIll}</span></div>
								 </div>
								 <div class="control-group span12">
								    <label class="control-label">体检：</label>
								    <div class="controls"><span class="readonly">${caseinfo.examined}</span></div>
								 </div>
								 <div class="control-group span12">
								    <label class="control-label">辅检：</label>
								    <div class="controls"><span class="readonly">${caseinfo.assistantResult}</span></div>
								 </div>
								 <div class="control-group span12">
								    <label class="control-label">诊断：</label>
								    <div class="controls"><span class="readonly">${caseinfo.initialDiagnosis}</span></div>
								 </div>
								 <div class="control-group span12">
								    <label class="control-label">治疗意见：</label>
								    <div class="controls"><span class="readonly">${caseinfo.treatAdvice}</span></div>
								 </div>
							</div>
						</div>
						<div class="section blinfo">
							<div class="header clearfix">
								<span class="span6 stitle">
									<i class="iconfont">&#xe600;</i>病例信息
								</span>
								<span class="span6 sbtn"></span>
							</div>
							<div class="bodyer">
								<h3>检查检验</h3>
								<div id="listable" class="bodyer hasmargin prelative"></div>
								<div class="linesplit"></div>
								<h3>
									<span>影像图片</span>
								</h3>
								<div id="pacs" class="bodyer hasmargin prelative"></div>	
							</div>
							<div class="bodyer-list"></div>									
						</div>
						<div class="section">
							<div class="header clearfix">
								<span class="stitle">
									<i class="iconfont">&#xe600;</i>本地资源
								</span>
							</div>
							<div class="bodyer hasmargin clearfix">
								<div id="pics" class="diyUpload">
									<div class="parentFileBox">
										<ul class="fileBoxUl">
											<c:forEach items="${normals }" var="im">
												<li data-id="${im.id}" class="browser">
													<div class="viewThumb">
														<c:if test="${fn:contains('gif,jpg,jpeg,bmp,png,tif',fn:toLowerCase(im.fileType))}">
															<img src="${fn:replace(im.fileUrl,'http://','https://')}" data-src="${fn:replace(im.fileUrl,'http://','https://')}" />
														</c:if>
														<c:if test="${fn:contains('mp4,avi,webm,mkv,mov,rm,ogg,ogv',(im.fileType))}">
															<a class="media video_diy_bg" target="_blank"><video name="media" controls><source src="${fn:replace(im.fileUrl,'http://','https://')}"></source></video></a>
														</c:if>
													</div>
													<div class="diyFileName">${im.fileName}</div>
												</li>
											</c:forEach>
										</ul>
									</div>	
									<input type="hidden" id="pics_hidden" name="picsIds" value="${caseinfo.normalImages}"/>			
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>		

			<input type="hidden" name="oid" value="${oid }"/>
			<input type="hidden" name="merid" value="${mrecord.id}"/>
			<input type="hidden" id="lis_ids" name="lis_ids"/>	
			<input type="hidden" id="pacs_ids" name="pacs_ids"/>
			<input type="hidden" id="his_hidden" name="his"/>
			<input type="hidden" id="lis_hidden" name="lis"/>
			<input type="hidden" id="pacs_hidden" name="pacs"/>
		</form>
		<div id="firstloadin">
			<div class="bg"></div>
			<div class="btns">
				<c:choose> 
				  <c:when test="${order.progressTag == '4'}">   
				  	<p>对方医生已准备就绪，赶紧发起连接吧</p>
					<a class="firstloadbtnin" href="javascript:videoClick()" onclick="document.getElementById('firstloadin').style.display='none'">发起视频问诊</a>
					<div class="firstlook">
						<a href="javascript:void(0)" class="exitandlook">查看患者病例信息</a>
					</div>
				  </c:when> 
				  <c:when test="${order.progressTag == '5'}">   
				  	<p>订单已结束，<a href="javascript:void(0)" class="exitandlook">查看患者病例信息</a></p>
				  </c:when> 
				  <c:otherwise>
				  	<p>对方医生还没有准备就绪，您可以先<a href="javascript:void(0)" class="exitandlook">查看患者病例信息</a></p>
				  </c:otherwise> 
				</c:choose> 
			</div>
		</div>
		<script type="text/javascript">
    		var _utype='${vtype}',_burl='/',_protag='${order.progressTag}',_roomKey='${order.id}',_uid='${uid}',_hasuser='${_hasuser}',_consultationdur='${order.consultationDur}',_vediotime='${order.vedioDur}';
			var _href = '/',_orderid = '${oid}',_oid = _orderid,_swipers = {};
			var _noresult = '<div class="noresult"><img src="'+ window.location.origin +'/img/mobile/kbzs/icon_noresult@2x.png" style="width:30px" alt=""/></div>';
    		var _lisSign,_pacsSign,_listSign,_signSyncBtn = 1;
    		var _connect = false,_gcontroller;
			var _idsearch="true",_diyUploadSelector;
    	</script>
		<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
		<script src="/sea-modules/bootstrap/js/bootstrap.min.js"></script>
		<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
		<script src="/sea-modules/seajs/seajs.config.js"></script>
    	<script src="/sea-modules/jqueryUI/drag&resize/jquery-ui.min.js"></script>
    	<script src='/js/screenshare/socketio.js'> </script>
		<script src='/js/screenshare/firebase.js'> </script>
        <script src="/js/screenshare/getScreenId.js"> </script>
        <script src="/js/screenshare/BandwidthHandler.js"></script>
        <script src="/js/screenshare/screen.js"> </script>
    	<script src="/js/view/detail_new.js"></script>
    	<script src="/js/view/detail_video.js"></script>
		<script src="https://cdn.goeasy.io/goeasy.js"></script>
		<script>
			window.parent['_sid'] = _oid;
			seajs.use('view/vedio/exp');
		</script>
		<jsp:include page="../share/lisTemp.jsp" />
	</body>
</html>
