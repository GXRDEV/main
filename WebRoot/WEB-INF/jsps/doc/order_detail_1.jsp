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
		<title>订单详情--医生</title>
    	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta content="telephone=no" name="format-detection" />
		<link rel="stylesheet" href="/css/bootstrap.min.css" />
		<link rel="stylesheet" href="/sea-modules/jqueryUI/drag&resize/jquery-ui.min.css" />
		<link rel="stylesheet" href="/fonticon/base/iconfont.css" />
		<link rel="stylesheet" href="/css/view/video.css" />
		<link rel="stylesheet" href="/css/view/video.doc.css" />
		<style type="text/css">		
			.recordlist{min-height:200px;}
			.recordlist .control-group,.recordlist .div{display:inline;}
			.modal2 .form-horizontal .controls {padding-top: 5px;}
		</style>
	</head>
	<body>
		<!-- 图文对话模块 -->
		<div id="pictxtdialog" class="row-fluid fixed windows" style="display:none">
			<div class="span12 backgroundfff">
				<div class="dialog">
					<div class="header"> 
						<span class="title">图文对话 </span>
						<button class="smallscreen winbtn" title="最小化" style="top:-8px;"><i class="iconfont">&#xe60a;</i></button>
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
		<!-- 菜单模块 -->
		<dl id="menulist" class="fixed" style="display:none">
			<dd id="formBtn" class="btncircle"><i class="iconfont">&#xe633;</i><span>病例信息</span><label>点击查看病例详情和专家的诊断报告</label></dd>
			<dd id="videoBtn" class="btncircle" style="display:none"><i class="iconfont">&#xe631;</i><span>返回视频</span><label>点击返回视频问诊界面</label></dd>
			<dd id="dialogBtn" class="btncircle" style="display:none"><i class="iconfont">&#xe634;</i><span>图文对话</span><label>可以与医生交流病例信息里面缺少的资料</label></dd>
			<dd id="readyBtn" class="btncircle"><i class="iconfont">绪</i><span>准备就绪</span><label>点击“准备就绪”后，专家就可以发起视频问诊了</label></dd>
			<dd id="leavelBtn" class="btncircle"><i class="iconfont">离</i><span>有事离开</span><label>点击“有事离开”后，专家暂时不能发起问诊了</label></dd>
			<dd id="endBtn" class="btncircle"><i class="iconfont">&#xe63a;</i><span>就诊结束</span><label>点击“就诊结束”后，当前订单就完成了</label></dd>
			<!-- <dd id="secondBtn" class="btncircle"><i class="iconfont">&#xe636;</i><span>二次就诊</span></dd>
			<dd id="thirdBtn" class="btncircle"><i class="iconfont">&#xe637;</i><span>三次就诊</span></dd> -->
		</dl>
		<!-- 视频和屏幕分享模块 -->
		<div id="videoAndScreenShare" class="fixed" style="display:none">
			<div class="clearfix">
				<div class="fleft">
					<!-- 视频对话模块 -->
					<div id="vedioOuter" class="backgroundfff">
						<div class="vedio" id="main">
							<div class="stateBar" id="stateBar" data-state="${order.hosName}/${order.depName}/${order.expertName}">
								<span>远程会诊</span>
								<button class="fullscreen"><i class="iconfont">&#xe608;</i></button>
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
									<span class="otherbtn">
										<button id="changeYuan" type="button">切到语音</button>
									</span>
									<div class="videotiptxt" style="display:none;">
										专家已经就绪，点击接受进入视频会诊
									</div>
								</div>
							</div>
							<div id="footer" class="signtips"></div>
							<div id="firsttip" class="signtips">
								<c:if test = "${order.progressTag == '4'}">
									<label>等待专家发起连接</label>
								</c:if>
								<c:if test = "${order.progressTag != '4'}">
									<label>标记状态为‘确认等待’后，等待专家发起连接</label>
								</c:if>
							</div>
							<div id="timers" style="display:none;"></div>
						</div>
					</div>
				</div>
				<div class="fleft">
					<!-- 视频对话模块 -->
					<div id="screenShareOuter">
						<div id="screens" class="windows">
							<div class="header" style="text-align:left;"> 
								<span class="title">专家屏幕分享 <span class="tip">（分享没有出来？<a href="javascript:void(0)" id="reloadShare"> 重新加载 </a>或者<a href="javascript:void(0)" id="tellExps"> 通知专家 </a>）</span></span>
								<button id="newwindow" class="newwindow winbtn" title="新窗口打开"><i class="iconfont">&#xe63d;</i></button>
								<button id="bigwin" class="fullscreen winbtn" title="最大化"><i class="iconfont">&#xe627;</i></button>
							</div>
							<div class="bodyer">
        						<iframe id="screenframe" src="/html/screenshare.html?state=notlink" name="screenframe" style="width:100%;height:100%" frameborder="no" border="0" allowscriptaccess="always" marginwidth="0" marginheight="0" allowfullscreen="true" wmode="opaque" allowTransparency="true" type="application/x-shockwave-flash"></iframe>
							</div>
						</div>						
					</div>
				</div>
			</div>
		</div>
		<!-- 病历信息模块 -->
		<div id="baseinform" class="container-fluid windows" style="display:none">
			<%-- <div class="topheader">
				<dl class="clearfix">
					<dd class="statedd first state0 passed animate">
						<span class="stateline"></span>
						<span class="statemain">
							<span class="stateicon">
								<i class="iconY">1</i>
							</span>
							<label class="statetext"><span class="innerText">开始</span></label>
						</span>
					</dd>
					<dd class="statedd state1">
						<span class="stateline"></span>
						<span class="statemain">
							<span class="stateicon">
								<i class="iconY">2</i>
							</span>
							<label class="statetext">
								<a href="#" class="btn btn-info btn-save" disabled>保存并发送病历至专家</a>
							</label>
						</span>
					</dd><!-- 
					<dd class="statedd state2">
						<span class="stateline"></span>
						<span class="statemain">
							<span class="stateicon">
								<i class="iconY">3</i>
							</span>
							<label class="statetext"><a href="#" class="btn btn-info btn-send" disabled >发送病例至专家</a></label>
						</span>
					</dd> -->
					<dd class="statedd state3">
						<span class="stateline"></span>
						<span class="statemain">
							<span class="stateicon">
								<i class="iconY">3</i>
							</span>
							<label class="statetext">
								<div class="btn-group">
									<a class="btn btn-info dropdown-toggle" data-toggle="dropdown" href="#" disabled>
										<c:choose> 
											<c:when test="${order.progressTag == '3'}">有事离开</c:when> 
											<c:when test="${order.progressTag == '4'}">等待专家</c:when> 
											<c:otherwise>标记医生状态</c:otherwise> 
										</c:choose> 
									</a>
									<a class="btn btn-info dropdown-toggle" data-toggle="dropdown" href="#" disabled><span class="caret"></span></a>
									<ul class="dropdown-menu">
									    <li sval="4"><a href="#">等待专家</a></li>
									    <li sval="3"><a href="#">有事离开</a></li>
									</ul>
								</div>
								<i class="iconfont state3icon">
									<c:if test = "${order.progressTag == '3'}"><span style="color:#E35850">&#xe615;</span></c:if>
									<c:if test = "${order.progressTag == '4'}"><span style="color:#5FB41B">&#xe614;</span></c:if>
								</i>
							</label>
						</span>
					</dd>
					<dd class="statedd state4">
						<span class="stateline"></span>
						<span class="statemain">
							<span class="stateicon">
								<i class="iconY">4</i>
							</span>
							<label class="statetext">
								<div class="btn-group">
									<a class="btn btn-info dropdown-toggle" data-toggle="dropdown" href="#" disabled>标记就诊状态</a>
									<a class="btn btn-info dropdown-toggle" data-toggle="dropdown" href="#" disabled><span class="caret"></span></a>
									<ul class="dropdown-menu">
									    <li sval="5"><a href="#">就诊结束</a></li>
									    <li sval="6"><a href="#">二次就诊</a></li>
									    <li sval="7"><a href="#">三次就诊</a></li>
									</ul>
								</div>
							</label>
						</span>
					</dd>
					<dd class="statedd last state5">
						<span class="stateline"></span>
						<span class="statemain">
							<span class="stateicon">
								<i class="iconY">5</i>
							</span>
							<label class="statetext"><span class="innerText">结束</span></label>
						</span>
					</dd>
				</dl>
			</div> --%>
			<div class="row-fluid">				
				<div class="span12 hasvedio">				
					<form class="row-fluid form-horizontal"  method="post" id="myform">
						<!-- 专家报告模块<c:if test="${fn:length(order.consultationResult) < 1}"> style="display:none"</c:if> -->
						<div id="expreport" class="row-fluid" <c:if test="${fn:length(order.consultationResult) < 1}"> style="display:none"</c:if>>
							
							<div style="height:10px;background:#eee;clear:both">&ensp;</div>
							<div class="span12 backgroundfff" style="margin:0;">
								<div class="reportresult">
									<div class="header"> 
										<span class="title">专家咨询意见</span>
									</div>
									<div class="bodyer">
										<div class="reportxt">${order.consultationResult }</div>
									</div>
								</div>
							</div>
						</div>
						<div class="backgroundfff" style="margin:10px 0;">
							<div class="userinfo">
								<div class="header clearfix baseformaction">
									<h3>患者病历</h3>								
									<button class="smallscreen winbtn" title="最小化" type="button" style="display:none;"><i class="iconfont">&#xe60a;</i></button>
								</div>
								<div class="bodyer">
									<div class="section">
										<div class="header clearfix">
											<span class="span12 stitle">
												<i class="iconfont">&#xe600;</i>患者基本信息
											</span>
										</div>
										<div class="bodyer clearfix">
											 <div class="control-group span6">
											    <label class="control-label">姓名：</label>
											    <div class="controls">
											      <input type="text" name="patientName" value="${order.patientName }"/>
											    </div>
											 </div>
											 <div class="control-group span6">
											    <label class="control-label">身份证号：</label>
											    <div class="controls">
											      <input type="text" name="idcard" value="${order.idCard}"/>
											    </div>
											 </div>
											<%--  <div class="control-group span6">
											    <label class="control-label">地址：</label>
											    <div class="controls">
											      <input type="text" name="address" value="${order.localAddress}"/>
											    </div>
											 </div> --%>
											 <div class="control-group span6">
											    <label class="control-label">联系电话：</label>
											    <div class="controls">
											      <input type="text" name="telphone" value="${order.telephone}"/>
											    </div>
											 </div>
											 <div class="control-group span12">
											    <label class="control-label">主诉：</label>
											    <div class="controls">
											      <textarea name="mainSuit">${caseinfo.mainSuit}</textarea>
											    </div>
											 </div>
											 <div class="control-group span12">
											    <label class="control-label">现病史：</label>
											    <div class="controls">
											      <textarea name="presentIll">${caseinfo.presentIll}</textarea>
											    </div>
											 </div>
											 <div class="control-group span12">
											    <label class="control-label">既往史：</label>
											    <div class="controls">
											      <textarea name="historyIll">${caseinfo.historyIll}</textarea>
											    </div>
											 </div>
											 <div class="control-group span12">
											    <label class="control-label">体检：</label>
											    <div class="controls">
											      <textarea name="examined">${caseinfo.examined}</textarea>
											    </div>
											 </div>
											 <div class="control-group span12">
											    <label class="control-label">辅检：</label>
											    <div class="controls">
											      <textarea name="assistantResult">${caseinfo.assistantResult}</textarea>
											    </div>
											 </div>
											 <div class="control-group span12">
											    <label class="control-label">诊断：</label>
											    <div class="controls">
											      <textarea name="initialDiagnosis">${caseinfo.initialDiagnosis}</textarea>
											    </div>
											 </div>
											 <div class="control-group span12">
											    <label class="control-label">治疗意见：</label>
											    <div class="controls">
											      <textarea name="treatAdvice">${caseinfo.treatAdvice}</textarea>
											    </div>
											 </div>
										</div>
									</div>
									<div class="section blinfo">
										<div class="header clearfix">
											<span class="span6 stitle">
												<i class="iconfont">&#xe600;</i>病例信息
											</span>
											<span class="span6 sbtn">
												<c:if test="${vtype == '1' && order.dockingMode == '2'}">
													<button class="btnajaxmore syscase justtextbtn" type="button" id="sycnCaseMore" data-ajax="doctor/gainUserPatients">高级同步</button>
													<button class="btnajax syscase" type="button" id="sycnCase" data-ajax="doctor/gainUserPatients">同步<i class="iconfont">&#xe609;</i></button>
												</c:if>
											</span>
										</div>
										<div class="bodyer">
											<h3>检查检验</h3>
											<div id="listable" class="bodyer hasmargin prelative"></div>
											<div class="linesplit"></div>
											<h3>
												<span>影像图片</span>
												<span class="tipEdit">（注：点击标题可以修改）</span>
												<span class="custombtngroup">
													<button class="btnupload justtextbtn" type="button" id="uploadBySelf">手动上传</button>
													<c:if test="${order.dockingMode == '2'}">
														<button class="btnajaxmore btnajaxPacsmore syscase justtextbtn" type="button" id="sycnPacsMore" data-ajax="doctor/pacsadvance">高级同步</button>
													</c:if>
												</span>
											</h3>
											<div id="pacs" class="bodyer hasmargin prelative"></div>	
										</div>
										<div class="bodyer-list"></div>									
									</div>
									<div class="section">
										<div class="header clearfix">
											<span class="stitle">
												<i class="iconfont">&#xe600;</i>本地资源
												<span class="tipEdit">（注：点击标题可以修改）</span>
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
																<div class="diyCancel"><i class="iconfont" title="删除"></i></div>
															</li>
														</c:forEach>
														<li class="actionAdd">
															<div id="addfiles"><i class="iconfont">&#xe60e;</i><label>本地资料</label></div>
														</li>
													</ul>
												</div>	
												<input type="hidden" id="pics_hidden" name="picsIds" value="${caseinfo.normalImages}"/>			
											</div>
										</div>
									</div>
								</div>
								<c:if test="${order.progressTag != '5' }">
									<div class="footer">
										<button type="button" id="saveFormAndNext">保存并发送病历至专家</button>
									</div>
								</c:if>
							</div>
						</div>
						<input type="hidden" name="oid" value="${oid }"/>
						<input type="hidden" name="merid" value="${mrecord.id}"/>
						<input type="hidden" name="his" id="his_hidden"/>
						<input type="hidden" name="lis" id="lis_hidden"/>
						<input type="hidden" name="pacs" id="pacs_hidden"/>
						<input type="hidden" id="lis_ids" name="lis_ids"/>	
						<input type="hidden" id="pacs_ids" name="pacs_ids"/>
					</form>
				</div>
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
    	<script src='/js/screenshare/socketio.js'></script>
		<script src='/js/screenshare/firebase.js'></script>
        <script src="/js/screenshare/getScreenId.js"></script>
        <script src="/js/screenshare/BandwidthHandler.js"></script>
        <script src="/js/screenshare/screen.js"></script>
    	<script src="/js/view/detail_new.js"></script>
    	<script src="/js/view/detail_video.js"></script>
		<script src="https://cdn.goeasy.io/goeasy.js"></script>
		<script>
			window.parent['_sid'] = _oid;
			seajs.use('view/vedio/doc');
		</script>
		<jsp:include page="../share/lisTemp.jsp" />
		<jsp:include page="../share/uploadDCIMTemp.jsp" />		
	</body>
</html>
