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
		<title>提现详情</title>
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
			.dialog .bodyer{padding-top:2.8em;height:auto;min-height:100px;}
			.row-fluid .span6{width: 49.80617%;}
			.row-fluid [class *="span"]{margin-left: 0.12766%;}
			#pics{margin:0 15px;}
		</style>
	</head>
	<body>
		<div class="container-fluid">			
			<div class="row-fluid">				
				<div class="span12 hasvedio">				
					<form class="row-fluid form-horizontal"  method="post" id="myform">
						<div class="backgroundfff" style="margin:10px 0;">
							<div class="userinfo">
								<div class="bodyer">							
								
									<div class="section">
										<div class="header clearfix">
											<span class="span6 stitle"><i class="iconfont">&#xe600;</i>提现医生信息</span>
										</div>
										<div class="bodyer clearfix">
											<div class="control-group span2">
											    <label class="control-label">姓名：</label>
											    <div class="controls">
											    	<span class="readonly">${info.docName }</span>
											    </div>
											 </div>
											<div class="control-group span4">
											    <label class="control-label">所在医院：</label>
											    <div class="controls">
											    	<span class="readonly">${info.hosName }</span>
											    </div>
											 </div>
											<div class="control-group span2">
											    <label class="control-label">科室：</label>
											    <div class="controls">
											    	<span class="readonly">${info.depName }</span>
											    </div>
											 </div>
											 <div class="control-group span4">
											    <label class="control-label">身份证：</label>
											    <div class="controls">
											    	<span class="readonly">${info.idCardNo }</span>
											    </div>
											 </div>
										</div>								
									</div>
								
									<div class="section">
										<div class="header clearfix">
											<span class="span12 stitle">
												<i class="iconfont">&#xe600;</i>提现基本信息
											</span>
										</div>
										<div class="bodyer clearfix">
											 <div class="control-group span4">
											    <label class="control-label">提现时间：</label>
											    <div class="controls">
											    	<span class="readonly">											    	
											    	${info.createTime}
											    	</span>
											    </div>
											 </div>
											 <div class="control-group span4">
											    <label class="control-label">提现金额：</label>
											    <div class="controls">
											    	<span class="readonly">${info.money }</span>
											    </div>
											 </div>
											 <div class="control-group span4">
											    <label class="control-label">卡号持有者：</label>
											    <div class="controls">
											    	<span class="readonly">${info.cardholder }</span>
											    </div>
											 </div>
											 <div class="control-group span4">
											    <label class="control-label">提现卡号：</label>
											    <div class="controls">
											    	<span class="readonly">${info.cardNo }</span>
											    </div>
											 </div>
											 <div class="control-group span4">
												    <label class="control-label">提现银行：</label>
												    <div class="controls">
												      <span class="readonly">${info.issuingBank}</span>
												    </div>
											 </div>
											<div class="control-group span4">
												    <label class="control-label">联系电话：</label>
												    <div class="controls">
												      <span class="readonly">${info.telephone}</span>
												    </div>
											 </div>
											<div class="control-group span4">
												    <label class="control-label">审核人：</label>
												    <div class="controls">
												      <span class="readonly">${info.auditor}</span>
												    </div>
											 </div>
											 <div class="control-group span4">
												    <label class="control-label">审核时间：</label>
												    <div class="controls">
												      <span class="readonly">${info.auditTime}</span>
												    </div>
											 </div>
											 <div class="control-group span4">
												    <label class="control-label">交易单号：</label>
												    <div class="controls">
												      <span class="readonly">${info.outTradeNo}</span>
												    </div>
											 </div>
											 <div class="control-group span4">
												    <label class="control-label">代扣税：</label>
												    <div class="controls">
												      <span class="readonly">${info.taxationMoney}</span>
												    </div>
											 </div>
											 <div class="control-group span4">
												    <label class="control-label">实际到账：</label>
												    <div class="controls">
												      <span class="readonly">${info.actualMoney}</span>
												    </div>
											 </div>
											 <div class="control-group span4">
												    <label class="control-label">状态：</label>
												    <div class="controls">
												      <span class="readonly" style="color:red">
												      	<c:choose>
												      		<c:when test="${info.status==1}">
												      			待审核
												      		</c:when>
												      		<c:when test="${info.status==2}">
												      			审核通过
												      		</c:when>
												      		<c:when test="${info.status==3}">
												      			审核不通过
												      		</c:when>
												      		<c:otherwise>
												      			待审核
												      		</c:otherwise>
												      	</c:choose>
												      </span>
												    </div>
											 </div>
										</div>
									</div>	

									<div class="section section-btn" style="border: none">
										<button type="button" class="btn btn-primary hha-audit" data-toggle="modal" data-target="#myModal"  data-sval="2">审核通过</button>	
										<button type="button" class="btn btn-danger hha-audit" data-toggle="modal" data-target="#myModal" data-sval="3">审核不通过</button>
									</div>	
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>			
		</div>
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel" style="text-align: center">审核</h4>
				</div>
				<div class="modal-body">
					<div>
						<label>审核人</label>
						<input type="text" placeholder="请输入审核人名称" class="names" name="name">
					</div>
					<div>
						<label>交易单号</label>
						<input type="text" placeholder="请输入交易单号" class="number" name="number" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)">
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary pre change">保存</button>
				</div>
			</div>
		</div>
		<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
		<script src="/sea-modules/bootstrap/js/bootstrap.min.js"></script>
		<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
		<script src="/sea-modules/seajs/seajs.config.js"></script>
		<script>
			// if(${info.status});
			var sval , url = window.location.href, status = '${info.status}';
			var id = url.slice(url.lastIndexOf('/')+1)
			status == '2' ||status == '3'? 
				$('.section-btn').hide():
				$('.section-btn').show()
			$('body').delegate('.hha-audit','click',function(){
				sval = $(this).attr('data-sval')
				sval == 3?
					$('input[name="number"]').attr('disabled', true):
					$('input[name="number"]').attr('disabled', false)
			})
			.delegate('.change','click',function(){
				var auditor = $('input[name ="name"]').val()
				var outTradeNo = $('input[name="number"]').val()
				if(!auditor) return alert('请输入审核人名称'),false; 
				if(!outTradeNo && sval == 2) return alert('请输入交易单号'),false; 
				$.ajax({
					url: '/system/auditdraw',
					type: 'post',
					data:{
						id: id,
						sval: sval,
						auditor: auditor,
						outTradeNo: outTradeNo
					},
					success: function(data){
						location.href="/system/doctorwithdraws"
					},
					error: function(){
						alert('审核失败')
					}
				})
			})
		</script>
	</body>
</html>
