<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
	<head lang="en">
		<meta charset="UTF-8">
		<title>天使陪诊•辅助就医第一平台</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta content="telephone=no" name="format-detection" />
		<link rel="stylesheet" href="/css/bootstrap.min.css" />
		<link rel="stylesheet" href="/css/view/video.css" />
		<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
		<style type="text/css">
			
		</style>
	</head>
	<body>
		<div class="container-fluid">
			<div class="topheader">
				<dl class="clearfix">
					<dd class="statedd passed first">
						<span class="stateline"></span>
						<span class="statemain">
							<span class="stateicon">
								<i class="iconY">1</i>
							</span>
							<label class="statetext">编辑病例</label>
						</span>
					</dd>
					<dd class="statedd passed">
						<span class="stateline"></span>
						<span class="statemain">
							<span class="stateicon">
								<i class="iconY">2</i>
							</span>
							<label class="statetext"><a href="#" class="btn btn-info btn-sendToDoc">发送病例至专家</a></label>
						</span>
					</dd>
					<dd class="statedd">
						<span class="stateline"></span>
						<span class="statemain">
							<span class="stateicon">
								<i class="iconY">3</i>
							</span>
							<label class="statetext">
								<div class="btn-group btn-signState">
									<a class="btn btn-info" href="#">标记就诊状态</a>
									<a class="btn btn-info dropdown-toggle" data-toggle="dropdown" href="#"><span class="caret"></span></a>
									<ul class="dropdown-menu">
									    <li><a href="#">标记就诊开始</a></li>
									    <li><a href="#">标记就诊结束</a></li>
									</ul>
								</div>
							</label>
						</span>
						<!--<span class="statebtns">
							<div class="btn-group">
							  <button class="btn btn-info">确认等待</button>
							  <button class="btn">有事离开</button>
							</div>
						</span>
					--></dd>
					<dd class="statedd">
						<span class="stateline"></span>
						<span class="statemain">
							<span class="stateicon">
								<i class="iconY">4</i>
							</span>
							<label class="statetext">
								<div class="btn-group btn-signOver">
									<a class="btn btn-info" href="#">标记就诊结束</a>
									<a class="btn btn-info dropdown-toggle" data-toggle="dropdown" href="#"><span class="caret"></span></a>
									<ul class="dropdown-menu">
									    <li><a href="#">标记就诊进行中</a></li>
									    <li><a href="#">标记就诊结束</a></li>
									</ul>
								</div>
							</label>
						</span>
						<span class="statebtns">
							
						</span>
					</dd>
					<dd class="statedd last">
						<span class="stateline"></span>
						<span class="statemain">
							<span class="stateicon">
								<i class="iconY">5</i>
							</span>
							<label class="statetext">结束</label>
						</span>
					</dd>
				</dl>
			</div>
			<div class="row-fluid">
				<div class="span5">
					<div class="row-fluid">
						<div class="span12 backgroundfff">
							<div class="vedio"></div>
						</div>
					</div>
					<div class="row-fluid">
						<div class="span12 backgroundfff">
							<div class="dialog">
								<div class="header"> 
									<span class="title">图文对话 </span>
								</div>
								<div class="bodyer">
									<div class="timer">15:44</div>
									<div class="hhlist clearfix doc">
										<span class="cols0">
											<span class="thumb">
												<img src="//placehold.it/60" />
											</span>
										</span>
										<span class="cols1">
											<span class="text">卖家您好，我是患者的主治医生高飞，患者的……</span>
										</span>
									</div>
									<div class="hhlist clearfix me">
										<span class="cols0">											
											<span class="thumb">
												<img src="//placehold.it/60" />
											</span>
										</span>
										<span class="cols1">
											<span class="text">你好，我看了患者的病历，患者在严重的……</span>
										</span>
									</div>
								</div>
								<div class="footer clearfix">
									<div class="span10">
										<div class="inputandimg">
											<div class="selectimg">
												<i class="iconfont">&#xe606;</i>
												<input type="file" name="selectfile" id="selectfile" accept=".png,.jpg,.gif,.bmp,.dcm"/>
											</div>
											<div class="selectinput">
												<input type="text" name="sendtext" placeholder="请在此输入内容"/>											
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
				</div>
				<div class="span7">				
					<form class="row-fluid form-horizontal">
						<div class="span12 backgroundfff">
							<div class="userinfo">
								<div class="header clearfix baseformaction">
									<span class="span6"><span class="title">整理患者病历</span></span>
									<span class="span6 sbtn">
										<button type="button" class="btn-save">保存</button>
										<button type="button" class="btn-send">发送</button>
									</span>
								</div>
								<div class="bodyer">
									<div class="section">
										<div class="header clearfix">
											<span class="span12 stitle">
												<i class="iconfont">&#xe600;</i>患者病例信息
											</span>
										</div>
										<div class="bodyer clearfix">
											 <div class="control-group span6">
											    <label class="control-label">姓名：</label>
											    <div class="controls">
											      <input type="text" name="username" />
											    </div>
											 </div>
											 <div class="control-group span6">
											    <label class="control-label">身份证号：</label>
											    <div class="controls">
											      <input type="text" name="cardno" />
											    </div>
											 </div>
											 <div class="control-group span6">
											    <label class="control-label">地址：</label>
											    <div class="controls">
											      <input type="text" name="cardno" />
											    </div>
											 </div>
											 <div class="control-group span6">
											    <label class="control-label">联系电话：</label>
											    <div class="controls">
											      <input type="text" name="cardno" />
											    </div>
											 </div>
											 <div class="control-group span12">
											    <label class="control-label">主诉补充：</label>
											    <div class="controls">
											      <textarea name="buchong"></textarea>
											    </div>
											 </div>
										</div>
									</div>
									<div class="section">
										<div class="header clearfix">
											<span class="span6 stitle">
												<i class="iconfont">&#xe600;</i>病例信息
											</span>
											<span class="span6 sbtn">
												<button class="btnajax" data-ajax="">从His同步<i class="iconfont">&#xe609;</i></button>
											</span>
										</div>
										<div class="bodyer clearfix">
											<div class="control-group span4">
											    <label class="control-label">BMI：</label>
											    <div class="controls">
											    	<span class="input">12.25</span>
											    </div>
											</div>
											<div class="control-group span4">
											    <label class="control-label">血压：</label>
											    <div class="controls">
											    	<span class="input">140/90</span>
											    </div>
											</div>
											<div class="control-group span4">
											    <label class="control-label">心率：</label>
											    <div class="controls">
											    	<span class="input">78</span>
											    </div>
											</div>
											<div class="control-group span4">
											    <label class="control-label">身高/体重：</label>
											    <div class="controls">
											    	<span class="input">176/90</span>
											    </div>
											</div>
											<div class="control-group span12">
											    <label class="control-label">个人史：</label>
											    <div class="controls">
											    	<span class="input">12.25</span>
											    </div>
											</div>
											<div class="control-group span12">
											    <label class="control-label">过敏史：</label>
											    <div class="controls">
											    	<span class="input">12.25</span>
											    </div>
											</div>
										</div>
									</div>
									<div class="section">
										<div class="header clearfix">
											<span class="span6 stitle">
												<i class="iconfont">&#xe600;</i>血常规/尿常规
											</span>
											<span class="span6 sbtn">
												<button class="btnajax" data-ajax="">从Lis同步<i class="iconfont">&#xe609;</i></button>
											</span>
										</div>
										<div class="bodyer hasmargin clearfix">
											<div class="imglist span2">
												<div class="thumb">
													<img src="//placehold.it/1"/>
												</div>
												<span class="imgdel iconfont">&#xe60b;</span>
												<label class="thumbName">
													尿常规  4月28日
												</label>
											</div>
											<div class="imglist span2">
												<div class="thumb">
													<img src="//placehold.it/1"/>
												</div>
												<span class="imgdel iconfont">&#xe60b;</span>
												<label class="thumbName">
													尿常规  4月28日
												</label>
											</div>
										</div>
									</div>
									<div class="section">
										<div class="header clearfix">
											<span class="span6 stitle">
												<i class="iconfont">&#xe600;</i>CT/MR/X光
											</span>
											<span class="span6 sbtn">
												<button class="btnajax" data-ajax="">从Pacs同步<i class="iconfont">&#xe609;</i></button>
											</span>
										</div>
										<div class="bodyer hasmargin clearfix">
											<div class="imglist span3">
												<div class="thumb">
													<img src="//placehold.it/1"/>
												</div>
												<span class="imgdel iconfont">&#xe60b;</span>
												<label class="thumbName">
													尿常规  4月28日
												</label>
											</div>
										</div>
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
														<li class="actionAdd">
															<div id="addfiles"><i class="iconfont">&#xe60e;</i><label>本地资料</label></div>
														</li>
													</ul>
												</div>	
												<input type="hidden" name="picsIds" />			
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>			
		</div>
		<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
		<script src="/sea-modules/bootstrap/js/bootstrap.min.js"></script>
		<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
		<script src="/sea-modules/seajs/seajs.config.js"></script>
		<script>
			var _href = '/',_orderid = '${oid}';
			seajs.use('view/vedio/main');
		</script>
	</body>
</html>
