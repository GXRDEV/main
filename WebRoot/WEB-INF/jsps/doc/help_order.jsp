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
	<title>辅助下单</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/video.css" />
	<link rel="stylesheet" href="/css/view/admin.css" />
</head>
<body class="dochelporder">
	<form class="row-fluid form-horizontal"  method="post" id="myform">
		<input type="hidden" name="expertid" value="${special.specialId }"/>
		<div class="userinfo">
			<div class="header clearfix baseformaction">
				<!-- <h3>辅助下单</h3>-->
				<div class="topheader hasexp<c:if test="${fn:length(specials)>0}">6</c:if>">
					<dl class="clearfix">
						<dd class="statedd first state1 passed animate">
							<span class="stateline"></span>
							<span class="statemain">
								<span class="stateicon">
									<i class="iconY">1</i>
								</span>
								<label class="statetext"><span class="innerText">基本信息</span></label>
							</span>
						</dd>
						<dd class="statedd state2">
							<span class="stateline"></span>
							<span class="statemain">
								<span class="stateicon">
									<i class="iconY">2</i>
								</span>
								<label class="statetext">
									<span class="innerText">病例信息</span>
								</label>
							</span>
						</dd>
						<c:if test="${fn:length(specials)>0}">
							<dd class="statedd state3">
								<span class="stateline"></span>
								<span class="statemain">
									<span class="stateicon">
										<i class="iconY">3</i>
									</span>
									<label class="statetext">
										<span class="innerText">选择专家</span>
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
										<span class="innerText">选择时间</span>
									</label>
								</span>
							</dd>
							<dd class="statedd state5">
								<span class="stateline"></span>
								<span class="statemain">
									<span class="stateicon">
										<i class="iconY">5</i>
									</span>
									<label class="statetext">
										<span class="innerText">支付</span>
									</label>
								</span>
							</dd>
						</c:if>
						<dd class="statedd last state6">
							<span class="stateline"></span>
							<span class="statemain">
								<span class="stateicon">
									<i class="iconY">
										<c:if test="${fn:length(specials)>0}">6</c:if>
										<c:if test="${fn:length(specials)<1}">3</c:if>
									</i>
								</span>
								<label class="statetext"><span class="innerText">完成</span></label>
							</span>
						</dd>
					</dl>
				</div>
			</div>
			<div class="bodyer">
				<div class="section section1 step">
					<div class="header clearfix">
						<span class="span12 stitle">
							<i class="iconfont">&#xe600;</i>患者基本信息
						</span>
					</div>
					<div class="bodyer clearfix">
						 <div class="control-group">
						    <label class="control-label">姓名：</label>
						    <div class="controls">
						    	<input type="text" name="username" />
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">电话：</label>
						    <div class="controls">
						    	<input type="text" name="telphone" />
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">身份证：</label>
						    <div class="controls">
						    	<input type="text" name="idcard" />
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">年龄：</label>
						    <div class="controls">
						    	<input type="number" name="age" />
			    			</div>
			 			 </div>
			 			 <div class="control-group">
						    <label class="control-label">性别：</label>
						    <div class="controls">
						    	<label class="radio"><input type="radio" name="sex" value="1" checked/>男</label>
						    	<label class="radio"><input type="radio" name="sex" value="2"/>女</label>
			    			</div>
			 			 </div>
			 			 <div class="form-action">
			 			 	<button type="button" class="btn btn-primary" name="nextstep" data-step="2">下一步</button>
			 			 </div>
					</div>					
				</div>
				<div class="section2 step">
					<div class="section fromgroups">					
						<div class="header clearfix">
							<span class="span12 stitle">
								<i class="iconfont">&#xe600;</i>患者病历信息
							</span>
						</div>
						<div class="bodyer clearfix fromgroups">
							<div class="control-group">
							    <label class="control-label">主诉：</label>
							    <div class="controls">
							      <input type="hidden" name="merid" value="${mrecord.id}"/>
							      <textarea name="mainSuit"></textarea>
							    </div>
							 </div>
							 <div class="control-group">
							    <label class="control-label">现病史：</label>
							    <div class="controls">
							      <textarea name="presentIll"></textarea>
							    </div>
							 </div>
							 <div class="control-group">
							    <label class="control-label">既往史：</label>
							    <div class="controls">
							      <textarea name="historyIll"></textarea>
							    </div>
							 </div>
							 <div class="control-group">
							    <label class="control-label">体检：</label>
							    <div class="controls">
							      <textarea name="examined"></textarea>
							    </div>
							 </div>
							 <div class="control-group">
							    <label class="control-label">辅检：</label>
							    <div class="controls">
							      <textarea name="assistantResult"></textarea>
							    </div>
							 </div>
							 <div class="control-group">
							    <label class="control-label">诊断：</label>
							    <div class="controls">
							    	<textarea name="initialDiagnosis"></textarea>
							    </div>
							 </div>
							 <div class="control-group">
							    <label class="control-label">治疗意见：</label>
							    <div class="controls">
							      <textarea name="treatAdvice"></textarea>
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
								<c:if test="${dockingMode == '2'}">
									<button class="btnajaxmore syscase justtextbtn" type="button" id="sycnCaseMore" data-ajax="doctor/gainUserPatients">高级同步</button>
									<button class="btnajax syscase" type="button" id="sycnCase" data-ajax="doctor/gainUserPatients">同步<i class="iconfont">&#xe609;</i></button>
								</c:if>
							</span>
						</div>
						<div class="bodyer">
							<c:if test="${dockingMode == '2'}">
								<h3>检查检验</h3>
								<div id="listable" class="bodyer hasmargin prelative"></div>
								<div class="linesplit"></div>
							</c:if>
							<h3>
								<span>影像图片</span>
								<span class="tipEdit">（注：点击标题可以修改）</span>
								<span class="custombtngroup">
									<button class="btnupload justtextbtn" type="button" id="uploadBySelf">手动上传</button>
									<c:if test="${dockingMode == '2'}">
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
								<c:if test="${vtype == '1'}">
									<span class="tipEdit">（注：点击标题可以修改）</span>
								</c:if>
							</span>
						</div>
						<div class="bodyer hasmargin clearfix">
							<div id="pics" class="diyUpload">
								<div class="parentFileBox">
									<ul class="fileBoxUl">
										<li class="actionAdd">
											<div id="addfiles" class="webuploader-container"><i class="iconfont">&#xe60e;</i><label>添加资料</label></div>
										</li>
									</ul>
								</div>	
								<input type="hidden" id="pics_hidden" name="picsIds" value=""/>			
							</div>
						</div>
					</div>
					<div class="section">
			 			<div class="bodyer" style="min-height:auto;">
				 			<div class="form-action" style="margin-top:0;border-top:0;">
				 			 	<button type="button" class="btn" name="nextstep" data-step="1">上一步</button>
				 			 	<button type="button" class="btn btn-primary" name="nextstep" data-step="3|6">下一步</button>
				 			</div>
			 			</div>				
					</div>
				</div>
				<c:if test="${fn:length(specials)>0}">			
				<div class="section section3 step">
					 <div class="header clearfix">
						<span class="span12 stitle">
							<i class="iconfont">&#xe600;</i>选择专家
						</span>
					 </div>
				     <div class="bodyer clearfix ">
			 			 <div class="exportslist">
						 	<c:forEach items="${specials}" var="o">
								<div class="doslist" data-id="${o.specialId }" data-c="${o.vedioAmount }">
									<div class="thumb">
										<c:if test="${ fn:length(o.listSpecialPicture) > 0 }">
											<c:if test="${ fn:indexOf(o.listSpecialPicture,'://') != -1}">
												<img src="${fn:replace(o.listSpecialPicture,'http://','https://')}"/>
											</c:if>
											<c:if test="${ fn:indexOf(o.listSpecialPicture,'://') == -1}">
												<img src="http://wx.15120.cn/SysApi2/Files/${o.listSpecialPicture}"/>
											</c:if>
										</c:if>
										<c:if test="${ fn:length(o.listSpecialPicture) <= 0 }">
											<img src="/img/defdoc.jpg"/>
										</c:if>
									</div>
									<div class="dosinfo">
										<p class="fp">${o.specialName}</p>
										<p>${o.duty}/${o.profession}</p>
										<p>${o.hosName}</p>
										<p>${o.depName}</p>
										<p class="lp">擅长：<span>${o.specialty}</span></p>
									</div>
								</div>
							</c:forEach>
			 			 </div>
			 			 <div class="form-action">
			 			 	<button type="button" class="btn" name="nextstep" data-step="2-1">上一步</button>
			 			 	<button type="button" class="btn btn-primary" name="nextstep" data-step="4|5">下一步<span style="font-size:12px;opacity: .7;">（可以不选择专家）</span></button>
			 			 </div>
					</div>
				</div>			
				<div class="section section4 step">
					 <div class="header clearfix">
						<span class="span12 stitle">
							<i class="iconfont">&#xe600;</i>选择时间
						</span>
					 </div>
				     <div class="bodyer clearfix ">						
			 			 <div class="doctimes">
						 	<div class="swiper-container">
						    	<div class="swiper-wrapper clearfix"></div>
					    	</div>
			 			 </div>
			 			 <div class="form-action">
			 			 	<button type="button" class="btn" name="nextstep" data-step="3">上一步</button>
			 			 	<button type="button" class="btn btn-primary" name="nextstep" data-step="5">下一步</button>
			 			 </div>
					</div>
				</div>
				<div class="section section5 step wxconfirmpay">
					 <div class="header clearfix">
						<span class="span12 stitle">
							<i class="iconfont">&#xe626;</i>支付
						</span>
					 </div>
				     <div class="bodyer clearfix ">						
			 			 <div class="pay">
						 	<p>扫一扫付款（元）</p>
			    			<h2>0.01</h2>
			    			<div class="imgerm">
			    				<img src="/img/qrcode/wx.png" />
			    				<div class="clearfix">
			    					<i class="iconfont fleft">&#xe625;</i>
			    					<span class="fright">打开微信扫一扫</span>
			    				</div>
			    			</div>
			 			 </div>
			 			 <div class="form-action">
			 			 	<button type="button" class="btn" name="nextstep" data-step="2|3|4">上一步</button>
			 			 	<button type="button" class="btn btn-primary" name="nextstep" data-step="6">支付成功</button>
			 			 </div>
					</div>
				</div>
				</c:if>
				<div class="section section6 step wxsuccesspay">
					 <div class="header clearfix">
						<span class="span12 stitle">
							<i class="iconfont">&#xe600;</i>完成
						</span>
					 </div>
				     <div class="bodyer clearfix ">						
			 			 <div class="pay">
						 	<h2><b>下单成功</b></h2>
			    			<div class="yuyuedetail">
			    				<dl class="something">
			    					<dt>注意事项：</dt>
			    					<dd>1、您可以在‘远程门诊’里查看订单详情</dd>
			    					<dd>2、如果您预约成功，由于个人原因不能就诊，请及时取消订单</dd>
			    					<dd>3、如遇专家停诊，您的订单会被取消，我们会以短信的方式通知您，请保持手机畅通。</dd>
			    					<dd>4、每个帐号每月下单和取消订单数量有上限，请按需预约，若超出上限，当月将无法再预约。</dd>
			    				</dl>
			    			</div>
			 			 </div>
			 			 <div class="form-action">
			 			 	<button type="button" class="btn" name="nextstep" data-step="-1" data-href="/docadmin/experhz">返回订单列表</button>
			 			 </div>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" id="lis_ids" name="lis_ids"/>	
		<input type="hidden" id="pacs_ids" name="pacs_ids"/>
		<input type="hidden" name="oid" />
		<input type="hidden" name="out_trade_no"/>
		<input type="hidden" name="dockingMode" value="${dockingMode }"/>
		<input type="hidden" name="expertId"/>
		<input type="hidden" name="stimeid"/>
		<input type="hidden" name="pmoney"/>
		<input type="hidden" name="stype" value="helper"/>
	</form>	
	<script src="/js/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
    <script src="/js/view/detail_video.js"></script>
	<script type="text/javascript">
		var _burl='/',_href = _burl,_utype = '1',_protag = '1',_h = _burl,_id = '',_orderid,_oid = _orderid,_swipers = {};
		var _lisSign,_pacsSign,_listSign,_signSyncBtn = 1,goEasy;
		seajs.use('view/admin/main',function(controller){
			controller.docHelpOrder();
		});
		function initialfirstipxt(){}
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

