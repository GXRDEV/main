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
		<link rel="stylesheet" href="/css/view/chooseexps.css" />
		<style type="text/css">			
			body{background:#fff;}
			.container-fluid{padding:0;}
			.row-fluid .backgroundfff{border:0;}
			.recordlist{min-height:200px;}
			.recordlist .control-group,.recordlist .div{display:inline;}
			.modal2 .form-horizontal .controls {padding-top: 5px;}
			.form-horizontal .control-group.span4{width:33.3%}
			.userinfo .actioner{text-align:right;padding:10px;}
			.userinfo .section .bodyer{min-height:40px;}
			.userinfo .section .header{cursor:pointer;}
			.userinfo .actioner button{color: #FFF;background: #08c url();border: 1px solid #64BAC7;border-radius: 3px;box-shadow:none;width:10em;}
			.imglist.noafter .thumb:after{display:none;}
		</style>
	</head>
	<body>
		<div class="container-fluid">			
			<div class="row-fluid">				
				<div class="span12 hasvedio">				
					<form class="row-fluid form-horizontal"  method="post" id="myform">
						<div class="backgroundfff" style="margin:10px 0;">
							<div class="userinfo">
								<div class="header actioner clearfix">
									<c:if test="${needRefund=='true'}">
										<button type="button" class="btn btn-content del" style="background:red;border:0;padding:6px 0;">退款</button>	
									</c:if>
									
									<button type="button" class="btn btn-content del" style="background:red;border:0;padding:6px 0;" data-id="${oid}">删除订单</button>	
									<button type="button" class="btn btn-content chooseexps" style="border:0;padding:6px 0;" data-id="${oid}">分配专家</button>						
								</div>
								<div class="bodyer">
									<div class="section">
										<div class="header clearfix">
											<span class="span6 stitle"><i class="iconfont">&#xe600;</i>支付详情</span>
										</div>
										<div class="bodyer clearfix">
										<c:if test="${fn:length(outTradeNo) > 0}">
											<div class="control-group span4">
											    <label class="control-label">商品订单号：</label>
											    <div class="controls">
											    	<span class="readonly">${outTradeNo}&ensp;</span>
											    </div>
											 </div>
										</c:if>
										<c:if test="${fn:length(transactionId) > 0}">
											<div class="control-group span4">
											    <label class="control-label">支付单号：</label>
											    <div class="controls">
											    	<span class="readonly">${transactionId}&ensp;</span>
											    </div>
											 </div>
										</c:if>
											 <div class="control-group span4">
											    <label class="control-label">支付金额：</label>
											    <div class="controls">
											    	<span class="readonly">
											    		<c:choose>
												    		<c:when test="${money > 0 }">￥${money}</c:when>
												    		<c:otherwise>￥0.00</c:otherwise>											    			
											    		</c:choose>
											    	</span>
											    </div>
											 </div>
											 <div class="control-group span4">
											    <label class="control-label">支付状态：</label>
											    <div class="controls">
											    	<span class="readonly">
											    	<c:choose>
											    		<c:when test="${order.payStatus == '1' }">已支付（
											    			<c:choose>
											    				<c:when test="${order.orderMode == '1' }">支付宝</c:when>
											    				<c:when test="${order.orderMode == '2' }">微信</c:when>
											    				<c:when test="${order.orderMode == '3' }">银联</c:when>
											    				<c:otherwise>其它</c:otherwise>											    			
											    			</c:choose>）
											    		</c:when>
											    		<c:otherwise>
											    			<b style="color:red;">未支付</b>
											    		</c:otherwise>
											    	</c:choose>
											    	</span>
											    </div>
											 </div>
											 <div class="control-group span4">
											    <label class="control-label">订单来源：</label>
											    <div class="controls">
											    	<span class="readonly">
											    		<c:choose>
										    				<c:when test="${order.source == '1'}">
										    					<c:choose>
										    						<c:when test="${order.vedioSubType== 1}">微信公众号(远程会诊)</c:when>
										    						<c:otherwise>微信公众号(远程门诊)</c:otherwise>
										    					</c:choose>
										    				</c:when>
										    				<c:when test="${order.source == '2' }">佰医汇官网</c:when>
										    				<c:when test="${order.source == '3' }">窗口护士</c:when>	
										    				<c:when test="${order.source == '4' }">医生辅助下单</c:when>	
										    				<c:when test="${order.source == '5' }">安卓</c:when>	
										    				<c:when test="${order.source == '6' }">ios</c:when>	
										    				<c:otherwise>未知</c:otherwise>										    			
										    			</c:choose>
											    	</span>
											    </div>
											 </div>
											 <c:if test="${!empty order.exLevel }">
											 	 <div class="control-group span4">
											 	  	<label class="control-label">专家级别：</label>
											 	  	<div class="controls">
												 	  	<span class="readonly">
												 	  		${order.exLevel}
												 	  	</span>
											 	  	</div>
											 	 </div>
											 </c:if>
										</div>								
									</div>
									<c:if test="${refundStatus==1||refundStatus==-1}">
										<div class="reportresult section">
											<div class="header"> 
												<span class="stitle">
													<i class="iconfont">&#xe600;</i>退款信息
												</span>
											</div>
											<div class="bodyer form-horizontal">
												<div class="control-group">
													<label class="control-label">退款状态：</label>
													   <div class="controls">
													     <span class="readonly" style="color:red">
													     	<c:choose>
													     		<c:when test="${refundStatus==1 }">退款成功</c:when>
													     		<c:otherwise>退款失败</c:otherwise>
													     	</c:choose>
													     </span>
													 </div>
												</div>
												<div class="control-group">
														 <label class="control-label">退款时间：</label>
														 <div class="controls">
														    <span class="readonly">${refundTime}</span>
														 </div>
													</div>
												<c:if test="${refundStatus==-1 }">
													<div class="control-group">
														 <label class="control-label">支付金额：</label>
														 <div class="controls">
														    <span class="readonly">${refundResult}</span>
														 </div>
													</div>
												</c:if>
											</div>	
										</div>	
									</c:if>		
									<c:if test="${!empty opinfo.id}">
										<div class="section">
										<div class="header clearfix">
											<span class="span6 stitle"><i class="iconfont">&#xe600;</i>运营人员信息</span>
										</div>
										<div class="bodyer clearfix">
											<div class="control-group span4">
											    <label class="control-label">运营人员姓名：</label>
											    <div class="controls">
											    	<span class="readonly">${opinfo.opName }</span>
											    </div>
											 </div>
											<div class="control-group span4">
											    <label class="control-label">运营人电话：</label>
											    <div class="controls">
											    	<span class="readonly">${opinfo.opTel }</span>
											    </div>
											 </div>
										</div>								
									</div>
									</c:if>
									
									<c:if test="${fn:length(order.expertName) > 0 }">
									<div class="section">
										<div class="header clearfix">
											<span class="span6 stitle"><i class="iconfont">&#xe600;</i>预约专家信息</span>
										</div>
										<div class="bodyer clearfix">
											<div class="control-group span4">
											    <label class="control-label">专家姓名：</label>
											    <div class="controls">
											    	<span class="readonly">${order.expertName }</span>
											    </div>
											 </div>
											<div class="control-group span4">
											    <label class="control-label">专家所在医院：</label>
											    <div class="controls">
											    	<span class="readonly">${order.hosName }</span>
											    </div>
											 </div>
											<div class="control-group span4">
											    <label class="control-label">专家科室：</label>
											    <div class="controls">
											    	<span class="readonly">${order.depName }</span>
											    </div>
											 </div>
										</div>								
									</div>
									</c:if>
									<div class="section">
										<div class="header clearfix">
											<span class="span6 stitle"><i class="iconfont">&#xe600;</i>预约医生信息</span>
										</div>
										<div class="bodyer clearfix">
											<div class="control-group span4">
											    <label class="control-label">当地医生：</label>
											    <div class="controls">
											    	<span class="readonly">${order.localDocName }</span>
											    </div>
											 </div>
											<div class="control-group span4">
											    <label class="control-label">当地医院：</label>
											    <div class="controls">
											    	<span class="readonly">${order.localHosName }</span>
											    </div>
											 </div>
											<div class="control-group span4">
											    <label class="control-label">当地科室：</label>
											    <div class="controls">
											    	<span class="readonly">${order.localDepName }</span>
											    </div>
											 </div>
										</div>								
									</div>
									<div class="section">
										<div class="header clearfix">
											<span class="span12 stitle">
												<i class="iconfont">&#xe600;</i>患者基本信息
											</span>
										</div>
										<div class="bodyer clearfix">
											 <div class="control-group span4">
											    <label class="control-label">姓名：</label>
											    <div class="controls">
											    	<span class="readonly">
											    	${caseinfo.contactName } 											    	
											    	<c:choose>
											    		<c:when test="${caseinfo.sex == '1' }"> / 男</c:when>
											    		<c:when test="${caseinfo.sex == '2' }"> / 女</c:when>
											    		<c:otherwise></c:otherwise>
											    	</c:choose>											    	
											    	<c:if test="${!empty caseinfo.age }">
											    		/ ${caseinfo.age } 岁
											    	</c:if>
											    	</span>
											    	
											    </div>
											 </div>
											 <div class="control-group span4">
											    <label class="control-label">身份证号：</label>
											    <div class="controls">
											    	<span class="readonly">${order.idCard }</span>
											    </div>
											 </div>
											 <div class="control-group span4">
											    <label class="control-label">联系电话：</label>
											    <div class="controls">
											    	<span class="readonly">${order.telephone }</span>
											    </div>
											 </div>
											 <div class="control-group span12">
											    <label class="control-label">咨询目的：</label>
											    <div class="controls">
											      <span class="readonly">${caseinfo.askProblem}</span>
											    </div>
											 </div>
											 <c:if test="${!empty caseinfo.mainSuit }">
											 <div class="control-group span12">
											    <label class="control-label">主诉：</label>
											    <div class="controls">
											      <span class="readonly">${caseinfo.mainSuit}</span>
											    </div>
											 </div>
											 </c:if>
											 <c:if test="${!empty caseinfo.presentIll }">
											 <div class="control-group span12">
											    <label class="control-label">现病史：</label>
											    <div class="controls">
											      <span class="readonly">${caseinfo.presentIll}</span>
											    </div>
											 </div>
											 </c:if>
											 <c:if test="${!empty caseinfo.historyIll }">
											 <div class="control-group span12">
											    <label class="control-label">既往史：</label>
											    <div class="controls">
											      <span class="readonly">${caseinfo.historyIll}</span>
											    </div>
											 </div>
											 </c:if>
											 <c:if test="${!empty caseinfo.examined }">
											 <div class="control-group span12">
											    <label class="control-label">体检：</label>
											    <div class="controls">
											      <span class="readonly">${caseinfo.examined}</span>
											    </div>
											 </div>
											 </c:if>
											 <c:if test="${!empty caseinfo.assistantResult }">
											 <div class="control-group span12">
											    <label class="control-label">辅检：</label>
											    <div class="controls">
											      <span class="readonly">${caseinfo.assistantResult}</span>
											    </div>
											 </div>
											 </c:if>
											 <c:if test="${!empty caseinfo.initialDiagnosis }">
											 <div class="control-group span12">
											    <label class="control-label">诊断：</label>
											    <div class="controls">
											      <span class="readonly">${caseinfo.initialDiagnosis}</span>
											    </div>
											 </div>
											 </c:if>
											 <c:if test="${!empty caseinfo.treatAdvice }">
											 <div class="control-group span12">
											    <label class="control-label">治疗意见：</label>
											    <div class="controls">
											      <span class="readonly">${caseinfo.treatAdvice}</span>
											    </div>
											 </div>
											 </c:if>
										</div>
									</div>
									<div class="section blinfo">
										<div class="header clearfix">
											<span class="span6 stitle"><i class="iconfont">&#xe600;</i>病例信息</span>
										</div>
										<div class="bodyer">
											<h3>入院记录</h3>
											<div id="pics" class="diyUpload hasmargin"></div>
											<div class="linesplit"></div>
											<h3><span>影像图片</span></h3>
											<div id="pacs" class="bodyer hasmargin prelative"></div>
											<div id="pacsImg" class="hasmargin prelative"></div>	
										</div>
										<div class="bodyer-list"></div>									
									</div>
									<div class="section">
										<div class="header clearfix">
											<span class="stitle">
												<i class="iconfont">&#xe600;</i>病例附件
											</span>
										</div>
										<div class="bodyer hasmargin clearfix">
											
										</div>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>			
		</div>
		<div id="pics" style="display:none;">
			<input type="hidden" id="vedioOuter"/>
			<input type="hidden" id="addfiles"/>
			<input type="hidden" id="selectfile"/>
			<input type="hidden" id="#main" class="vedio"/>
		</div>
		<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
		<script src="/sea-modules/bootstrap/js/bootstrap.min.js"></script>
		<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
		<script src="/sea-modules/seajs/seajs.config.js"></script>
		<script type="text/javascript">
    		var _utype='${vtype}',_burl='/',_protag='${order.progressTag}',_roomKey='${order.id}',_hasuser='${_hasuser}';
    		var _lisSign,_pacsSign,_listSign,_signSyncBtn = 1;
    		var _docask = '';
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
				}).delegate('.refund','click',function(){
	            	$.post('/system/refund',{oid:'${order.id}',otype:'4'},function(data){
	            		location.href=location.href;
	            	});
	            });
				$(window).unbind("scroll");
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
	</body>
</html>
