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
		<title>订单详情</title>
    	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta content="telephone=no" name="format-detection" />
		<link rel="stylesheet" href="/css/bootstrap.min.css" />
		<link rel="stylesheet" href="/fonticon/base/iconfont.css" />
		<link rel="stylesheet" href="/css/view/video.css" />
		<style type="text/css">
			/* .row-fluid .span3 {width: 22%;}
			.row-fluid [class *="span"]:nth-child(4n + 1){margin-left: 0;} */			
			.recordlist{min-height:200px;}
			.recordlist .control-group,.recordlist .div{display:inline;}
			.modal2 .form-horizontal .controls {padding-top: 5px;}
		</style>
	</head>
	<body>
		<div class="container-fluid">
			<c:if test="${vtype == '1'}">
			<div class="topheader">
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
			</div>
			</c:if>
			<div class="row-fluid">
				<div class="span5 hasvedio">
					<div class="row-fluid">
						<div class="span12 backgroundfff" id="vedioOuter">
							<div class="vedio" id="main">
								<div class="stateBar" id="stateBar" data-state="<c:if test="${vtype == '1'}">${order.hosName}/${order.depName}/${order.expertName}</c:if><c:if test="${vtype == '2'}">${order.localHosName}/${order.localDepName}/${order.localDocName}</c:if>">
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
										<!-- <div class="c_beginorend">
											<span class="timelong" style="display:none;">
												<span class="time">00:00:00</span>
												<i class="iconline"></i>
											</span>
											<a href="#" class="voice">
												<i class="iconfont">&#xe61c;</i>
												<span class="voicecontrol"></span>
											</a>
										</div> -->
										<div class="videotiptxt" style="display:none;">
											<c:if test="${vtype == '1'}">专家</c:if><c:if test="${vtype == '2'}">医生</c:if>已经就绪，点击接受进入视频会诊
										</div>
									</div>
								</div>
								<div id="footer" class="signtips"></div>
								<div id="firsttip" class="signtips">
									<c:if test="${vtype == '1'}">
										<c:if test = "${order.progressTag == '4'}">
											<label>等待专家发起连接</label>
										</c:if>
										<c:if test = "${order.progressTag != '4'}">
											<label>标记状态为‘确认等待’后，等待专家发起连接</label>
										</c:if>										
									</c:if>
									<c:if test="${vtype == '2'}">
										<c:if test = "${order.progressTag == '4'}">
											<label>对方医生已就绪，可以发起视频问诊</label>
										</c:if>
										<c:if test = "${order.progressTag != '4'}">
											<label>等待医生准备就绪后，再发起连接</label>
										</c:if>	
									</c:if>
								</div>
							</div>
						</div>
					</div>
					<div id="videos-container"></div>
					
					<div class="row-fluid" id="expreport" <c:if test="${fn:length(order.consultationResult) < 1}"> style="display:none"</c:if>>
						<div class="span12 backgroundfff">
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
					
					<div class="row-fluid" id="pictxtdialog" style="display:none"><!-- style="display:none" -->
						<div class="span12 backgroundfff">
							<div class="dialog">
								<div class="header"> 
									<span class="title">图文对话 </span>
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
					<c:if test="${vtype == '2'}">
						<div class="row-fluid fixed" id="reporttxt" style="display:none;">
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
						<div class="fixed" id="reportbtn">
							<button class="btncircle">
								<i class="iconfont">&#xe61d;</i>
								<span>填写患者报告</span>
							</button>
						</div>
					</c:if>
				</div>
				<div class="span7 hasvedio">				
					<form class="row-fluid form-horizontal"  method="post" id="myform">
						<input type="hidden" name="oid" value="${oid }"/>
						<input type="hidden" name="his" id="his_hidden"/>
						<input type="hidden" name="lis" id="lis_hidden"/>
						<input type="hidden" name="pacs" id="pacs_hidden"/>
						<div class="backgroundfff" style="margin:10px 0;">
							<div class="userinfo">
								<div class="header clearfix baseformaction">
									<h3>患者病历查看</h3>								
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
											      <input type="text" name="patientName" <c:if test="${vtype != '1'}">readonly</c:if> value="${order.patientName }"/>
											    </div>
											 </div>
											 <div class="control-group span6">
											    <label class="control-label">身份证号：</label>
											    <div class="controls">
											      <input type="text" name="idcard" <c:if test="${vtype != '1'}">readonly</c:if> value="${order.idCard}"/>
											    </div>
											 </div>
											 <div class="control-group span6">
											    <label class="control-label">地址：</label>
											    <div class="controls">
											      <input type="text" name="address" <c:if test="${vtype != '1'}">readonly</c:if> value="${order.localAddress}"/>
											    </div>
											 </div>
											 <div class="control-group span6">
											    <label class="control-label">联系电话：</label>
											    <div class="controls">
											      <input type="text" name="telphone" <c:if test="${vtype != '1'}">readonly</c:if> value="${order.telephone}"/>
											    </div>
											 </div>
											 <div class="control-group span12">
											    <label class="control-label">主诉：</label>
											    <div class="controls">
											      <input type="hidden" name="merid" value="${mrecord.id}"/>
											      <c:if test="${vtype == '1'}"><textarea name="mainSuit">${mrecord.mainSuit}</textarea></c:if>
											      <c:if test="${vtype == '2'}"><span class="readonly">${mrecord.mainSuit}</span></c:if>
											    </div>
											 </div>
											 <div class="control-group span12">
											    <label class="control-label">现病史：</label>
											    <div class="controls">
											      <c:if test="${vtype == '1'}"><textarea name="presentIll">${mrecord.presentIll}</textarea></c:if>
											      <c:if test="${vtype == '2'}"><span class="readonly">${mrecord.presentIll}</span></c:if>
											    </div>
											 </div>
											 <div class="control-group span12">
											    <label class="control-label">既往史：</label>
											    <div class="controls">
											      <c:if test="${vtype == '1'}"><textarea name="historyIll">${mrecord.historyIll}</textarea></c:if>
											      <c:if test="${vtype == '2'}"><span class="readonly">${mrecord.historyIll}</span></c:if>
											    </div>
											 </div>
											 <div class="control-group span12">
											    <label class="control-label">体检：</label>
											    <div class="controls">
											      <c:if test="${vtype == '1'}"><textarea name="examined">${mrecord.examined}</textarea></c:if>
											      <c:if test="${vtype == '2'}"><span class="readonly">${mrecord.examined}</span></c:if>
											    </div>
											 </div>
											 <div class="control-group span12">
											    <label class="control-label">辅检：</label>
											    <div class="controls">
											      <c:if test="${vtype == '1'}"><textarea name="assistantResult">${mrecord.assistantResult}</textarea></c:if>
											      <c:if test="${vtype == '2'}"><span class="readonly">${mrecord.assistantResult}</span></c:if>
											    </div>
											 </div>
											 <div class="control-group span12">
											    <label class="control-label">诊断：</label>
											    <div class="controls">
											      <c:if test="${vtype == '1'}"><textarea name="initialDiagnosis">${mrecord.initialDiagnosis}</textarea></c:if>
											      <c:if test="${vtype == '2'}"><span class="readonly">${mrecord.initialDiagnosis}</span></c:if>
											    </div>
											 </div>
											 <div class="control-group span12">
											    <label class="control-label">治疗意见：</label>
											    <div class="controls">
											      <c:if test="${vtype == '1'}"><textarea name="treatAdvice">${mrecord.treatAdvice}</textarea></c:if>
											      <c:if test="${vtype == '2'}"><span class="readonly">${mrecord.treatAdvice}</span></c:if>
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
												<c:if test="${vtype == '1'}">
													<span class="tipEdit">（注：点击标题可以修改）</span>
												</c:if>
												<c:if test="${vtype == '1'}">
													<span class="custombtngroup">
														<button class="btnupload justtextbtn" type="button" id="uploadBySelf">手动上传</button>
														<c:if test="${order.dockingMode == '2'}">
															<button class="btnajaxmore btnajaxPacsmore syscase justtextbtn" type="button" id="sycnPacsMore" data-ajax="doctor/pacsadvance">高级同步</button>
														</c:if>
													</span>
												</c:if>
											</h3>
											<div id="pacs" class="bodyer hasmargin prelative"></div>	
										</div>
										<div class="bodyer-list"></div>									
									</div>
									<div class="section">
										<div class="header clearfix">
											<span class="stitle">
												<i class="iconfont">&#xe600;</i>本地资源
												<c:if test="${vtype == '1'}">
													<span class="tipEdit">（注：点击标题可以修改）</span>
												</c:if>
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
																<c:if test="${vtype == '1'}">
																	<div class="diyCancel"><i class="iconfont" title="删除"></i></div>
																</c:if>
															</li>
														</c:forEach>
														<c:if test="${vtype == '1'}">
														<li class="actionAdd">
															<div id="addfiles"><i class="iconfont">&#xe60e;</i><label>本地资料</label></div>
														</li>
														</c:if>
													</ul>
												</div>	
												<input type="hidden" id="pics_hidden" name="picsIds" value="${order.normalImages}"/>			
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<input type="hidden" id="lis_ids" name="lis_ids"/>	
						<input type="hidden" id="pacs_ids" name="pacs_ids"/>
					</form>
				</div>
			</div>			
		</div>
		
		<div class="github-stargazers" style="width:400px;"></div>
        <section class="experiment" style="display:none;">                
            <section>
                <input type="text" id="user-name" placeholder="Your Name">
                <button id="share-screen" class="setup">分享您的屏幕</button>
            </section>
            <!-- list of all available broadcasting rooms -->
            <table style="width: 100%;" id="rooms-list"></table>
            <!-- local/remote videos container -->
            <div id="videos-container"></div>
        </section>
		<c:if test="${vtype == '2'}">
			<div id="firstloadin">
				<div class="bg"></div>
				<div class="btns">
					<c:if test="${order.progressTag == '4' }">
						<p>对方医生已准备就绪，赶紧发起连接吧</p>
						<a class="firstloadbtnin" href="javascript:videoClick()" onclick="document.getElementById('firstloadin').style.display='none'">发起视频问诊</a>
						<div class="firstlook">
							<a href="javascript:void(0)" onclick="document.getElementById('firstloadin').style.display='none'">查看患者病例信息</a>
						</div>
					</c:if>
					<c:if test="${order.progressTag != '4' }">				
						<p>对方医生还没有准备就绪，您可以先<a href="javascript:void(0)" onclick="document.getElementById('firstloadin').style.display='none'">查看患者病例信息</a></p>
					</c:if>
				</div>
			</div>
		</c:if>
		<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
		<script src="/sea-modules/bootstrap/js/bootstrap.min.js"></script>
		<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
		<script src="/sea-modules/seajs/seajs.config.js"></script>
		<%-- <script src="/js/adapter.js"></script> --%>
		<script type="text/javascript">
    		var _utype='${vtype}',_burl='/',_protag='${order.progressTag}',_roomKey='${order.id}',_uid='${uid}',_hasuser='${_hasuser}',_consultationdur='${order.consultationDur}',_vediotime='${order.vedioTime}';
    		var _lisSign,_pacsSign,_listSign,_signSyncBtn = 1;
    		var _connect = false;
    	</script>
    	<script src='/js/screenshare/socketio.js'> </script>
		<script src='/js/screenshare/firebase.js'> </script>
        <script src="/js/screenshare/getScreenId.js"> </script>
        <script src="/js/screenshare/BandwidthHandler.js"></script>
        <script src="/js/screenshare/screen.js"> </script>

    	<script src="/js/view/detail_new.js"></script>
    	<script src="/js/view/detail_video.js"></script>
		<script>
			var _href = '/',_orderid = '${oid}',_oid = _orderid,_swipers = {};
			var _noresult = '<div class="noresult"><img src="'+ window.location.origin +'/img/mobile/kbzs/icon_noresult@2x.png" style="width:30px" alt=""/></div>';
			window.parent['_sid'] = _oid;
			seajs.use('view/vedio/main');
		</script>
		<script type="text/javascript">
			//if(_utype=="2")sharetodoc();
			//if(_utype=="1")waitForscreen();
		</script>
		<script type="text/javascript" src="https://cdn.goeasy.io/goeasy.js"></script>
		<script type="text/javascript">                        
      		var goEasy = new GoEasy({
            	appkey: '78028e7e-edcc-4524-b56b-45639785a53a'
            });
	      	goEasy.subscribe({
	            channel: '${order.id}',
	            onMessage: function(message){
	            	var content = message.content.replace(/&quot;/g,"\"");
	            	var jmessage = JSON.parse(content);  
	            	var _type    =  jmessage.type;
	            	var _from    =  jmessage.from,
	            		_result  =  jmessage.result || '加载无数据';  
	            	
        			//console.log('jmessage', JSON.stringify(jmessage, null, '\t'));
	            	//就绪通知
	            	if(_type == 'launchNotify' && _from !=_utype && _utype == 1){	            	
	            		_hasuser = jmessage.hasuser;
	            		initiallizeVideo(true);
	            	//退出通知
	            	} else if (_type =='cancelNotify' && _from !=_utype ){
	            		endwaitvideo();
	            		//进入就绪或退出就绪状态
	            	} else if(_type == 'progressNotify' && _from !=_utype){
	            		//var _progress = jmessage.progress;
	            		//alert("inside"  + content);
	            	//报告通知
	            	} else if (_type =='reportNotify' && _from !=_utype ){
	            		reportmsg(_result),reportmsgAnmiation(_result);           	 	
	            	} 
	            	else if (_type =='syncPacs' && _from ==_utype ){
	            		setPacsProgram(jmessage);
	            	}
	            	else if (_type =='syncPacsOut' && _from ==_utype ){
	            		!!_signSyncBtn ? setPacListWithType1(jmessage) : setPacListWithType0(jmessage);
	            	}
	            	else if (_type =='syncLis' && _from ==_utype ){
	            		setLisProgram(jmessage);
	            	}
	            	else if (_type =='updcm'){
	            		setCustomPacs(jmessage);
	            	}
	            }
	        });
	        
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
					<div class="span6 ansysdata">
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
							<tbody>{tbody1}</tbody>
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
		<script type="text/html" id="uploadDCIMTemp">
			<div id="uploader">
                <div class="queueList">
                    <div id="dndArea" class="placeholder">
                        <div id="filePicker"></div>
                        <p>按住Ctrl可多选照片，单次最多可选500张</p>
                    </div>
                </div>
                <div class="statusBar" style="display:none;">
                    <div class="progress">
                        <span class="text">0%</span>
                        <span class="percentage"></span>
                    </div><div class="info"></div>
                    <div class="btns">
                        <div id="filePicker2"></div><div class="uploadBtn">开始上传</div>
                    </div>
                </div>
            </div>
		</script>
	</body>
</html>
