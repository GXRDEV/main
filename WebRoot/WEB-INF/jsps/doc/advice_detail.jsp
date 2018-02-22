<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE html>
<html lang="en">
<head>
	<title>查看回复</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/sea-modules/webuploader/webuploader.css" />
	<link rel="stylesheet" href="/css/view/video.css" />
	<link rel="stylesheet" href="/css/view/admin.css" />
	<style>
		.dialog .header{z-index:10;position:relative;padding-left:100px;box-sizing: border-box;background:#fbfbfb!important;}
		.dialog .header .thumb{position:absolute;left:0;top:0;width: 80px;height: 80px;border-radius:0;margin: 0;}
		.dialog .bodyer{padding:1.2em 0;height:auto!important;min-height:300px!important;}
		.dialog .footer{position:relative;z-index:10;background:#EFEFEF;}
		.dialog .footer .controls{margin:0 10em 0 10px!important;position:relaitve!important;}
		.dialog .footer textarea{width:100%!important;height:5em!important;margin:0;}
		.dialog .footer button{position:absolute;right:8px;bottom:7px;height:4em;width:8em;}
		
	</style>
</head>
<body class="dochelporder">
	<form class="row-fluid form-horizontal"  method="post" id="myform">
		<input type="hidden" name="expertid" value="${order.doctorId }"/>
		<input type="hidden" name="orderid" value="${order.id }"/>
		<input type="hidden" name="uuid" value="${order.uuid }"/>
		<input type="hidden" name="caseid" value="${caseinfo.id }"/>
		<div class="userinfo">
			<div class="bodyer">				
				<div class="section section1 step">
					<div class="header clearfix">
						<span class="span12 stitle">
							<i class="iconfont">&#xe600;</i>患者基本信息
						</span>
					</div>
					<div class="bodyer clearfix" style="min-height: 20px;">
						 <div class="control-group span6">
						    <label class="control-label">姓名：</label>
						    <div class="controls" style="white-space:nowrap;">
						    	<span class="input">${caseinfo.contactName }</span>
						    	（<span class="input"><c:choose>
						    		<c:when test="${caseinfo.sex == '1' }">男</c:when>
						    		<c:otherwise>女</c:otherwise>
						    	</c:choose></span>
						    	<span class="input"> / ${caseinfo.age }岁</span> / ${caseinfo.telephone }）
			    			</div>
			 			 </div>
			 			 <c:if test="${!empty(caseinfo.idNumber) }">
			 			 <div class="control-group span6">
						    <label class="control-label" style="width:100px;">身份证：</label>
						    <div class="controls" style="margin-left:110px;">
						    	<span class="input">${caseinfo.idNumber }</span>
			    			</div>
			 			 </div>
			 			 </c:if>
					</div>					
				</div>				
				<div class="section baseinfo">					
					<div class="header clearfix">
						<span class="span12 stitle">
							<i class="iconfont">&#xe600;</i>患者病情描述
						</span>
					</div>
					<div class="bodyer clearfix fromgroups" style="min-height:auto;">
						<div class="control-group" style="margin:10px;">
						    <span class="input">${caseinfo.description }</span>
						</div>
					</div>
				</div>				
				<div class="section fromgroups moreinfo">					
					<div class="header clearfix">
						<span class="span12 stitle">
							<i class="iconfont">&#xe600;</i>患者病历信息
						</span>
					</div>
					<div class="bodyer clearfix fromgroups">
						<div class="control-group">
						    <label class="control-label">主诉：</label>
						    <div class="controls">
						    	<span class="input">${caseinfo.mainSuit }</span>
						    </div>
						 </div>
						 <div class="control-group">
						    <label class="control-label">现病史：</label>
						    <div class="controls">
						    	<span class="input">${caseinfo.presentIll }</span>
						    </div>
						 </div>
						 <div class="control-group">
						    <label class="control-label">既往史：</label>
						    <div class="controls">
						    	<span class="input">${caseinfo.historyIll }</span>
						    </div>
						 </div>
						 <div class="control-group">
						    <label class="control-label">体检：</label>
						    <div class="controls">
						    	<span class="input">${caseinfo.examined }</span>
						    </div>
						 </div>
						 <div class="control-group">
						    <label class="control-label">辅检：</label>
						    <div class="controls">
						    	<span class="input">${caseinfo.assistantResult }</span>
						    </div>
						 </div>
						 <div class="control-group">
						    <label class="control-label">诊断：</label>
						    <div class="controls">
						    	<span class="input">${caseinfo.initialDiagnosis }</span>
						    </div>
						 </div>
						 <div class="control-group">
						    <label class="control-label">治疗意见：</label>
						    <div class="controls">
						    	<span class="input">${caseinfo.treatAdvice }</span>
						    </div>
						 </div>
					</div>
				</div>
				<div class="section blinfo moreinfo">
					<div class="header clearfix">
						<span class="span6 stitle">
							<i class="iconfont">&#xe600;</i>病例信息
						</span>
					</div>
					<div class="bodyer">
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
						<div id="pics" class="diyUpload"><!-- id="pics"  -->
							<div class="parentFileBox">
								<ul class="fileBoxUl">
									<c:forEach items="${caseimages }" var="im">
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
							<input type="hidden" id="pics_hidden" name="picsIds" value=""/>			
						</div>
					</div>
				</div>
				<div class="section baseinfo">					
					<div class="header clearfix">
						<span class="span12 stitle">
							<i class="iconfont">&#xe600;</i>专家回复
						</span>
					</div>
					<div class="bodyer clearfix fromgroups" style="min-height:auto;">
						<div class="dialog section divblock">
							<div class="header">
								<div class="thumb">
									<c:if test="${ fn:length(special.listSpecialPicture) > 0 }">
										<c:if test="${ fn:indexOf(special.listSpecialPicture,'://') != -1}">
											<img src="${fn:replace(special.listSpecialPicture,'http://','https://')}"/>
										</c:if>
										<c:if test="${ fn:indexOf(special.listSpecialPicture,'://') == -1}">
											<img src="http://wx.15120.cn/SysApi2/Files/${special.listSpecialPicture}"/>
										</c:if>
									</c:if>
									<c:if test="${ fn:length(special.listSpecialPicture) <= 0 }">
										<img src="https://develop.ebaiyihui.com:443/img/defdoc.jpg">
									</c:if>
								</div>
								<div class="details">
									<div>${special.specialName}（${special.duty}/${special.profession}）</div>
									<div>${special.hosName}&ensp;${special.depName}</div>
								</div>
							</div>
							<div class="bodyer" id="messageReply">
								<c:forEach items="${msgs }" var="im">													
									<div class="timer">${im.msgTime }</div>
									<div class="hhlist clearfix<c:if test="${im.sendType != '2' }"> me</c:if><c:if test="${im.sendType == '2' }"> doc</c:if>">								
										<span class="cols0"><span class="thumb">
											<c:if test="${im.sendType != '2' }">
												<img src="https://develop.ebaiyihui.com:443/img/defdoc.jpg">
											</c:if>
											<c:if test="${im.sendType == '2' }">
												<c:if test="${ fn:length(special.listSpecialPicture) > 0 }">
													<c:if test="${ fn:indexOf(special.listSpecialPicture,'://') != -1}">
														<img src="${fn:replace(special.listSpecialPicture,'http://','https://')}"/>
													</c:if>
													<c:if test="${ fn:indexOf(special.listSpecialPicture,'://') == -1}">
														<img src="http://wx.15120.cn/SysApi2/Files/${special.listSpecialPicture}"/>
													</c:if>
												</c:if>
												<c:if test="${ fn:length(special.listSpecialPicture) <= 0 }">
													<img src="https://develop.ebaiyihui.com:443/img/defdoc.jpg">
												</c:if>
											</c:if>
										</span></span>
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
							<div class="footer">
								<div class="controls">
								    <textarea name="replycontent" id="replycontent"></textarea>
					 			 	<button type="button" class="btn btn-primary" id="reply">回复</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>	
	<script src="/js/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
    <script src="/js/view/detail_video.js"></script>
	<script type="text/javascript">
		var _burl='/',_href = _burl,_utype = '2',_protag = '1',_h = _burl,_id = '',
		_uuid = '${order.uuid}',
		_orderid = '${order.id}',_oid = _orderid,_swipers = {};
		var _lisSign,_pacsSign,_listSign,_signSyncBtn = 1,goEasy;
		seajs.use('view/admin/main2',function(controller){
			controller.adviceOrderDetail();
		});
		function initialfirstipxt(){}
	</script>
	<script type="text/javascript">
		$(function(){
			$("#reply").click(function(){
				var t = $('#replycontent').val();
				$.post("/docadmin/replytoexpert",{oid:_oid,msgContent:t},function(data){
					var m = data.message || {msgTime:'',id: new Date()},h = 0;
					$('#messageReply').append(function(){
						return '<div class="timer">'+ m.msgTime +'</div>\
							<div id="s'+ m.id +'" class="hhlist clearfix me">\
								<span class="cols0"><span class="thumb"><img src="https://develop.ebaiyihui.com:443/img/defdoc.jpg"></span></span>\
								<span class="cols1"><span class="text hasimgview">'+ t +'</span></span>\
							</div>';
					});
					//h = document.querySelector('#messageReply').scrollHeight;
					$('#replycontent').val('');
					//$('#messageReply').scrollTop(h);
				});
			});
		});
	</script>
	<script type="text/javascript" src="https://cdn.goeasy.io/goeasy.js"></script>
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

