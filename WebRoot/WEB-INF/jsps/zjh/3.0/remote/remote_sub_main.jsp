<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE HTML>
<html>
  <head lang="en">
    <base href="/">
    <meta charset="utf-8">
	<meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" name="viewport">
	<meta content="yes" name="apple-mobile-web-app-capable">
	<meta content="black" name="apple-mobile-web-app-status-bar-style">
	<meta content="telephone=no" name="format-detection">
	<meta content="email=no" name="format-detection">
	<title>远程会诊</title>
	<link rel="stylesheet" type="text/css" href="/css/wx.css" />
    <link rel="stylesheet" href="/weui/weui.min.css"/>
	<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
    <style>
    	body{background-color:#F6F6F6;}
    	.blockinfo{/* background-color:#fff; */padding:1px 0 0;margin-bottom:10px;}
    	.weui_cells{font-size:1.1rem;}
    	.weui_label i{font-size:1.2rem;color:#00CC99;margin-right:4px;}
    	.weui_label{width:10em;}    	

    	.weui_btn_primary{border-radius:0;background-color:#00CC99;}
    	.weui_cells_title{color:#666;}
    	#toast .weui_toast{width:80%;margin-left:-40%;}
    	#toast .weui_toast .weui_btn_primary{margin: 0 10px;border-radius: 3px;font-size:1.2rem;}
    </style>
  </head>  
  <body class="confirmBody">
    <div class="confirmIndex">
		<form action="/wzjh/iwantsub" method="post" id="postorder" name="postorder">
			<input type="hidden" name="openid" value="${openid}"/>
			<div class="mianinfo blockinfo">
				<div class="weui_cells_title"></div>
				<div class="weui_cells weui_cells_form">
			        <div class="weui_cell weui_cell_select weui_select_after">
			            <div class="weui_cell_hd">
			                <label for="" class="weui_label"><i class="iconfont">&#xe61a;</i>您所在的城市</label>
			            </div>
			            <div class="weui_cell_bd weui_cell_primary">
			                <select class="weui_select" name="city"></select>
			            </div>
			        </div>
			        <div class="weui_cell weui_cell_select weui_select_after">
			            <div class="weui_cell_hd">
			                <label for="" class="weui_label"><i class="iconfont">&#xe61c;</i>最近的合作医院</label>
			            </div>
			            <div class="weui_cell_bd weui_cell_primary">
			                <select class="weui_select" name="hosid"></select>
			            </div>
			        </div>
			        <div class="weui_cell weui_cell_select weui_select_after">
			            <div class="weui_cell_hd">
			                <label for="" class="weui_label"><i class="iconfont">&#xe61b;</i>选择就诊科室</label>
			            </div>
			            <div class="weui_cell_bd weui_cell_primary">
			                <select class="weui_select" name="depid"></select>
			            </div>
			        </div>
			    </div>
			</div>
			<div class="baseinfo blockinfo">
				<div class="weui_cells_title">选择您预约专家的级别</div>	
				<div class="weui_cells weui_cells_radio">
					<c:forEach items="${splevels }" var="spe">
						<label class="weui_cell weui_check_label" for="level${spe.id }">
				            <div class="weui_cell_bd weui_cell_primary">
				                <p>${spe.displayName }：${spe.displayValue }元</p>
				            </div>
				            <div class="weui_cell_ft">
				                <input type="radio" name="levelid" value="${spe.id }" class="weui_check" id="level${spe.id }">
				                <span class="weui_icon_checked"></span>
				            </div>
				        </label>
					</c:forEach>
					<label class="weui_cell weui_check_label" for="level-1">
			            <div class="weui_cell_bd weui_cell_primary">
			                <p>自定义收费级别：</p>
			            </div>
			            <div class="weui_cell_ft">
			                <input type="radio" name="levelid" value="-1" class="weui_check" id="level-1">
			                <span class="weui_icon_checked"></span>
			            </div>
			        </label>
			    </div>
			</div>			
			<div class="baseinfo blockinfo" for="levelid" style="display:none;">
				<div class="weui_cells_title">输入自定义收费详情</div>
				<div class="weui_cells weui_cells_form">
			        <div class="weui_cell">
			            <div class="weui_cell_hd">
			                <label for="" class="weui_label">运营人员</label>
			            </div>
			            <div class="weui_cell_bd weui_cell_primary">
			                <input class="weui_input" type="text" name="opname" placeholder="请输入姓名">
			            </div>
			        </div>
			        <div class="weui_cell">
			            <div class="weui_cell_hd">
			                <label for="" class="weui_label">运营人员电话</label>
			            </div>
			            <div class="weui_cell_bd weui_cell_primary">
			                <input class="weui_input" type="tel" name="optel" placeholder="请输入有效的手机号">
			            </div>
			        </div>
			        <div class="weui_cell">
			            <div class="weui_cell_hd"><label for="" class="weui_label">金额</label></div>
			            <div class="weui_cell_bd weui_cell_primary">
			                <input class="weui_input" type="number" name="opmoney" placeholder="请输入有效数字">
			            </div>
			            <div class="weui_cell_ft"><i class="weui_icon_warn"></i></div>
			        </div>
			    </div>
			    <p style="font-size:12px;margin:4px 0 0 15px;color:#888;">需与运营人员协商，下单后客服会与运营人员核实。</p>
			</div>
			
			<div class="baseinfo blockinfo">
				<div class="weui_cells_title">选择您希望的会诊时间</div>
				<div class="weui_cells weui_cells_radio">
					<c:forEach items="${seltimes }" var="spe">
						<label class="weui_cell weui_check_label" for="time${spe.id }">
				            <div class="weui_cell_bd weui_cell_primary">
				                <p>${spe.displayName }</p>
				            </div>
				            <div class="weui_cell_ft">
				                <input type="radio" name="timeid" value="${spe.id }" class="weui_check" id="time${spe.id }">
				                <span class="weui_icon_checked"></span>
				            </div>
				        </label>
					</c:forEach>
			    </div>
			    <p style="font-size:12px;margin:4px 0 0 15px;color:#888;">时间越长，预约成功率越大。请尽量放宽时间。</p>
			</div>
			<div class="footer">
				<div class="btns">
					<a href="javascript:;" id="submitForm" class="weui_btn weui_btn_primary">我要预约</a>
				</div>
			</div>
		</form>	
    </div>
	<div id="toast" style="display: none;">
	    <div class="weui_mask_transparent"></div>
	    <div class="weui_toast">
	        <i class="weui_icon_toast"></i>
	        <p class="weui_toast_content">提交成功</p>
	        <p class="weui_toast_content">
	        	<a href="/wzjh/myorders?openid=${openid}&flag=processing" class="weui_btn weui_btn_primary">查看我的订单（<span>3</span>秒）</a>
	        </p>
	    </div>
	</div>
	<!-- loading toast -->
	<div id="loadingToast" class="weui_loading_toast" style="display:none;">
	    <div class="weui_mask_transparent"></div>
	    <div class="weui_toast">
	        <div class="weui_loading">
	            <div class="weui_loading_leaf weui_loading_leaf_0"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_1"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_2"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_3"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_4"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_5"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_6"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_7"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_8"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_9"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_10"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_11"></div>
	        </div>
	        <p class="weui_toast_content">正在保存</p>
	    </div>
	</div>
	<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
	<script src="/js/base.js"></script>
	<script type="text/javascript">
		var _href = '/',_docid = '${docid }',_openid = '${openid }';
		seajs.use('view/wx/twtelupload',function(f){
			f.remoteHZ();
		});
	</script>
  </body>
</html>
