<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE html>
<html>
	<head lang="en">
		<meta charset="UTF-8">
		<title>
			私人医生服务订单详情
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
			#pics{margin:0 15px;}
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
								 <label class="control-label">创建时间：</label>
								 <div class="controls">
								    <span class="readonly">${order.createTimes}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">服务时间：</label>
								 <div class="controls">
								    <span class="readonly">${order.packageName}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">金额：</label>
								 <div class="controls">
								    <span class="readonly">${order.money}/元</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">服务开始时间：</label>
								 <div class="controls">
								    <span class="readonly">${order.startDate}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">服务结束时间：</label>
								 <div class="controls">
								    <span class="readonly">${order.endDate}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">服务描述：</label>
								 <div class="controls">
								    <span class="readonly">${order.remark}</span>
								 </div>
							</div>
						</div>	
					</div>	
					<div class="reportresult section">
						<div class="header"> 
							<span class="stitle">
								<i class="iconfont">&#xe600;</i>支付信息
							</span>
						</div>
						<div class="bodyer form-horizontal">
							<c:if test="${fn:length(outTradeNo) > 0}">
								<div class="control-group">
								    <label class="control-label">商户订单号：</label>
								    <div class="controls">
								      <span class="readonly">${outTradeNo}</span>
								    </div>
								</div>
							</c:if>
							<c:if test="${fn:length(transactionId) > 0}">
								<div class="control-group">
								    <label class="control-label">支付单号：</label>
								    <div class="controls">
								      <span class="readonly">${transactionId}</span>
								    </div>
								</div>
							</c:if>
							<div class="control-group">
								 <label class="control-label">支付金额：</label>
								 <div class="controls">
								    <span class="readonly">${money}（元）</span>
								 </div>
							</div>
							<div class="control-group">
							    <label class="control-label">支付状态：</label>
							    <div class="controls">
							      <span class="readonly">
							      	<c:choose>
										<c:when test="${order.payStatus == '1' }">已支付
										</c:when>
										<c:otherwise>
											    <b style="color:red;">未支付</b>
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
										<c:otherwise>
											   其它
										</c:otherwise>
									</c:choose>
							      </span>
							    </div>
							</div>
						</div>	
					</div>	
					<c:if test="${order.status ==50 }">
						<div class="reportresult section">
							<div class="header"> 
								<span class="stitle">
									<i class="iconfont">&#xe600;</i>解约信息
								</span>
							</div>
							<div class="bodyer form-horizontal">
								<div class="control-group">
	                                 <label class="control-label">服务vip解约时间：</label>
	                                 <div class="controls">
	                                    <span class="readonly">${order.relieveTime}</span>
	                                 </div>
	                            </div>
	                            <div class="control-group">
	                                 <label class="control-label">服务时间：</label>
	                                 <div class="controls">
	                                    <span class="readonly">${order.totalServerTime}</span>
	                                 </div>
	                            </div>
	                            <div class="control-group">
	                                 <label class="control-label">服务费用：</label>
	                                 <div class="controls">
	                                    <span class="readonly">${order.serverMoney}</span>
	                                 </div>
	                            </div>
	                            <div class="control-group">
	                                 <label class="control-label">返还费用：</label>
	                                 <div class="controls">
	                                    <span class="readonly">${order.returnMoney}</span>
	                                 </div>
	                            </div>
	                         </div>
	                     </div>
					</c:if>
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
										 <label class="control-label">失败原因：</label>
										 <div class="controls">
										    <span class="readonly">${refundResult}</span>
										 </div>
									</div>
								</c:if>
							</div>	
						</div>	
					</c:if>
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
								    <span class="readonly">${order.docName}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">医院：</label>
								 <div class="controls">
								    <span class="readonly">${order.hosName}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">科室：</label>
								 <div class="controls">
								    <span class="readonly">${order.depName}</span>
								 </div>
							</div>
						</div>	
					</div>
					<div class="reportresult section">
						<div class="header"> 
							<span class="stitle">
								<i class="iconfont">&#xe600;</i>患者信息
							</span>
						</div>
						<div class="bodyer form-horizontal">
							<div class="control-group">
								 <label class="control-label">患者名称：</label>
								 <div class="controls">
								    <span class="readonly">${order.userName}</span>
								 </div>
							</div>
							<div class="control-group">
								 <label class="control-label">性别：</label>
								 <div class="controls">
								    <span class="readonly">
							      	<c:choose>
							      		<c:when test="${order.sex==1 }">男</c:when>
							      		<c:when test="${order.sex==2 }">女</c:when>
							      	</c:choose>
							      </span>
								 </div>
							</div>
						</div>	
					</div>
				</div>
			</div>
		</div>
		<c:if test="${order.status == 50 && refundStatus !=1}">
            <div style="height:4em;">&ensp;</div>
            <div class="fixed" style="text-align:center;padding:1em 0;width:100%"><a  href="javascript:void(0);" style="padding:8px 4em;" class="btn btn-danger js-addinfo refund">退款</a></div>
        </c:if>
		<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
		<script src="/sea-modules/bootstrap/js/bootstrap.min.js"></script>
		<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
		<script src="/sea-modules/seajs/seajs.config.js"></script>
		<script type="text/javascript">
            $(document).ready(function () {   
                 $('body').delegate('.refund','click',function(){
                     $.post('/system/refundVip',{oid:'${order.id}',otype:'15'},function(data){
                         location.href = location.href;
                     });
                });
            });
        </script>
		
	</body>
</html>
