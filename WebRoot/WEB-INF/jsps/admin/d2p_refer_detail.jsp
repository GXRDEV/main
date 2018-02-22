
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
			转诊订单详情
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
			.divblock .bodyer{padding:5px 0;}
			.dialog .bodyer{padding-top:2.8em;height:auto;min-height:100px;}
			.userinfo .row-fluid .span6{width: 49.80617%;}
			.userinfo .row-fluid [class *="span"]{margin-left: 0.12766%;}
			.imglist.noafter .thumb:after{display:none;}
			#pics{margin:0 15px;}
			#pics .fileBoxUl li{list-style: none; float: left; margin: .5em}
			#pics .browser{ background: #f4f4f4; padding: 15px; width: 120px; }
			#pics .browser .type { color: #0186d1; font-size: 1.2em}
			#pics .browser .type .timer { color: #666; font-size: .8rem}
			#pics .browser .remark { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; margin-bottom: .3em}
			#pics .browser img { height: 120px; width: 120px; display: none}
			#pics .browser img:first-child{ display: block}
			#pics .browser .len{position: absolute; background: rgba(0, 0, 0, 0.5); color: #fff; width: 100%; height: 30px; line-height: 30px;  bottom: 0; left: 0; text-align: center}
		</style>
	</head>
	<body>
		<div class="container-fluid userinfo">
			<div class="row-fluid">				
			<div class="span12">	
				<div class="reportresult section">
						<div class="header"> 
							<span class="stitle">
								<i class="iconfont">&#xe600;</i>订单信息
							</span>
						</div>
						<div class="bodyer form-horizontal">
							<div class="control-group">
								   <label class="control-label">转诊类型：</label>
								   <div class="controls">
								      <span class="readonly">
								      	<c:choose>
										<c:when test="${order.referralType == '1' }">住院
										</c:when>
										<c:when test="${order.referralType == '0' }">门诊
										</c:when>
										<c:otherwise>
											   其它
										</c:otherwise>
									</c:choose>
								      </span>
								    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">订单来源：</label>
							    <div class="controls">
							      <span class="readonly">
							      	<c:choose>
										<c:when test="${order.source == '1' }">IOS
										</c:when>
										<c:when test="${order.source == '2' }">Android
										</c:when>
										<c:when test="${order.source == '3' }">微信公号
										</c:when>
										<c:when test="${order.source == '4' }">网站后台
										</c:when>
										<c:otherwise>
											   其它
										</c:otherwise>
									</c:choose>
							      </span>
							    </div>
							</div>
							<div class="control-group">
								   <label class="control-label">转诊日期：</label>
								   <div class="controls">
								      <span class="readonly">${order.referralDate }</span>
								    </div>
							</div>
							<div class="control-group">
								   <label class="control-label">订单状态：</label>
								   <div class="controls">
								      <span class="readonly">
								      	<c:choose>
										<c:when test="${order.status == '10' }">待接诊
										</c:when>
										<c:when test="${order.status == '20' }">已接诊
										</c:when>
										<c:when test="${order.status == '30' }">已退诊
										</c:when>
										<c:when test="${order.status == '40' }">已完成
										</c:when>
										<c:when test="${order.status == '50' }">已取消
										</c:when>
										<c:otherwise>
											   其它
										</c:otherwise>
									</c:choose>
								      </span>
								    </div>
							</div>
							<c:if test="${!empty order.receiveTime }">
								<div class="control-group">
								   <label class="control-label">接诊时间：</label>
								   <div class="controls">
								      <span class="readonly">${order.receiveTime }</span>
								    </div>
								</div>
							</c:if>
						</div>	
					</div>		
					<div class="reportresult section">
						<div class="header"> 
							<span class="stitle">
								<i class="iconfont">&#xe600;</i>医生信息
							</span>
						</div>
						<div class="bodyer form-horizontal">
							<div class="control-group">
								 <label class="control-label">医生：</label>
								 <div class="controls">
								    <span class="readonly">${doc.specialName}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">医院：</label>
								 <div class="controls">
								    <span class="readonly">${doc.hosName}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">科室：</label>
								 <div class="controls">
								    <span class="readonly">${doc.depName}</span>
								 </div>
							</div>
						</div>	
					</div>
					<div class="reportresult section">
						<div class="header"> 
							<span class="stitle">
								<i class="iconfont">&#xe600;</i>转诊医生信息
							</span>
						</div>
						<div class="bodyer form-horizontal">
							<div class="control-group">
								 <label class="control-label">医生：</label>
								 <div class="controls">
								    <span class="readonly">${refdoc.specialName}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">医院：</label>
								 <div class="controls">
								    <span class="readonly">${refdoc.hosName}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">科室：</label>
								 <div class="controls">
								    <span class="readonly">${refdoc.depName}</span>
								 </div>
							</div>
						</div>	
					</div>
					<div class="reportresult section divblock">
						<div class="header"> 
							<span class="stitle">
								<i class="iconfont">&#xe600;</i>基本信息
							</span>
						</div>
						<div class="bodyer form-horizontal">
							<div class="control-group">
							    <label class="control-label">姓名：</label>
							    <div class="controls">
							      <span class="readonly">${caseinfo.contactName } &nbsp;&nbsp;<c:if test="${caseinfo.sex != '1' }">女</c:if><c:if test="${caseinfo.sex == '1' }">男</c:if>  &nbsp;&nbsp;${caseinfo.age }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">电话：</label>
							    <div class="controls">
							      <span class="readonly">${caseinfo.telephone }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">身份证：</label>
							    <div class="controls">
							      <span class="readonly">${caseinfo.idNumber }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">疾病名称：</label>
							    <div class="controls">
							      <span class="readonly">${caseinfo.caseName }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">病情描述：</label>
							    <div class="controls">
							      <span class="readonly">${caseinfo.presentIll }</span>
							    </div>
							</div>
							<div class="control-group">
							    <label class="control-label">咨询目的：</label>
							    <div class="controls">
							      <span class="readonly">${caseinfo.askProblem }</span>
							    </div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="section blinfo">
					<div class="header clearfix">
						<span class="stitle">
							<i class="iconfont">&#xe600;</i>病例信息
						</span>
					</div>
					<div class="bodyer">
						<h3>入院记录</h3>
						<div id="pics" class="diyUpload"></div>
						<div class="linesplit"></div>
						<h3><span>影像图片</span></h3>
						<div id="pacs" class="bodyer hasmargin prelative"></div>
						<div id="pacsImg" class="hasmargin prelative"></div>	
						</div>
					<div class="bodyer-list"></div>									
			</div>			
			<div class="row-fluid">				
				<div class="span6">
					<div class="someimg section divblock">
						<div class="header"> 
							<span class="stitle">
								<i class="iconfont">&#xe600;</i>病例附件
							</span>
						</div>
						<div class="bodyer" style="overflow: hidden">
							<div id="pics" class="diyUpload">
								<ul class="fileBoxUl">	
									<c:forEach items="${caseimages }" var="im">													
										<li data-id="${im.id}" class="browser">
											<div class="type">
												<c:if test="${im.type == '1' }">CT</c:if>
												<c:if test="${im.type == '2' }">DX</c:if>
												<c:if test="${im.type == '3' }">MR</c:if>
												<c:if test="${im.type == '4' }">DCM</c:if>
												<c:if test="${im.type == '5' }">IMG</c:if>
												<span class="timer">&ensp;${im.reportTimes }</span>
											</div>
											<div class="remark">描述：${im.remark}</div>	
											<div class="viewThumb hasimgview" style="position: relative">	
												<c:forEach items="${im.files}" var="fi">
													<c:choose>
														<c:when test="${fn:contains(fi.fileUrl,'://')}">
															<img src="${fn:replace(fi.fileUrl,'http://','https://')}"/>
														</c:when>
														<c:otherwise>
															<img src="http://wx.15120.cn/SysApi2/Files/${fi.fileUrl}" />
														</c:otherwise>
													</c:choose>			
												</c:forEach>													
											</div>														
										</li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</div>						
				</div>		
				<div class="span6">
					<div class="dialog section divblock">
						<div class="header"> 
							<span class="stitle">
								<i class="iconfont">&#xe600;</i>聊天消息
							</span>
						</div>
						<div class="bodyer">
							<c:forEach items="${msgs }" var="im">													
								<div class="timer">${im.sendTime }</div>
								<div class="hhlist clearfix<c:if test="${im.sendType != '2' }"> me</c:if><c:if test="${im.sendType == '2' }"> doc</c:if>">								
									<span class="cols0"><span class="thumb"><img src="https://develop.ebaiyihui.com:443/img/defdoc.jpg"></span></span>
									<span class="cols1"><span class="text hasimgview">
										<c:choose>
											<c:when test="${im.msgType == 'audio/amr'}">
												<audio src="${im.path }" controls="controls">当前浏览器不支持.</audio>
											</c:when>
											<c:when test="${im.msgType == 'image/jpg'}">
												<a href="javascript:;"  style="display:inline-block;width:150px;"><img src="${im.fileUrl }" style="width:100%;"/></a>
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
		<c:if test="${twinfo.status==4}">
			<div style="height:4em;">&ensp;</div>
			<div class="fixed" style="text-align:center;padding:1em 0;width:100%"><a  href="/system/intohos" style="padding:8px 4em;" class="btn btn-danger js-addinfo closetw">结束订单</a></div>
		</c:if>	
		<div id="pics" style="display:none;">
			<input type="hidden" id="vedioOuter"/>
			<input type="hidden" id="selectfile"/>
		</div>
		<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
		<script src="/sea-modules/bootstrap/js/bootstrap.min.js"></script>
		<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
		<script src="/sea-modules/seajs/seajs.config.js"></script>
		<script type="text/javascript">
    		var _utype='',_burl='/';
    		var _lisSign,_pacsSign,_listSign,_signSyncBtn = 1;
    		var _docask = 'referral';
    	</script>
    	<script>
			var _href = '/',_h = _href,_orderid = '${order.id}',_oid = _orderid,_swipers = {};
			var _noresult = '<div class="noresult"><img src="'+ window.location.origin +'/img/mobile/kbzs/icon_noresult@2x.png" style="width:30px" alt=""/></div>';
			window.parent['_sid'] = _oid;
			seajs.use('view/vedio/main');
			seajs.use('view/admin/main1',function(controller){
				controller.chooseEXPS();
			});
			$(function(){
				$('.close').click(function(){
					$('body').removeClass('modal2-open');
				});
				$('body').delegate('.section .header','click',function(){
					var body = $(this).next('.bodyer');
					body.is(':hidden') ? body.slideDown() : body.slideUp();
				})
				$(window).unbind("scroll");
			});
		</script>
		<script>
			var _href = '/',_h = _href;
			// seajs.use('view/admin/order',function(controller){
			// 	controller.tuwen();
			// });
			 $(document).ready(function () { 
				 $('body').delegate('.closetw','click',function(){
		            	$.post('/doctor/closetw',{oid:'${twinfo.id}',utype:'0'},function(data){
		            		location.href='/system/tuwenmanage';
		            	});
		            });
			 });
		</script>
	</body>
</html>
