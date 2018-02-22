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
	<title>问诊服务</title>
	<!-- <script type="text/javascript">
  		!function(n){var e=n.document,t=e.documentElement,i=414,d=i/100,o="orientationchange"in n?"orientationchange":"resize",a=function(){var n=t.clientWidth||320;n>414&&(n=414),t.style.fontSize=n/d+"px"};e.addEventListener&&(n.addEventListener(o,a,!1),e.addEventListener("DOMContentLoaded",a,!1))}(window);
  	</script> -->
	<link rel="stylesheet" type="text/css" href="/css/wx.css" />
    <link rel="stylesheet" href="/weui/weui.min.css"/>
	<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
    <style>
    	body{background-color:#F6F6F6;}
    	.blockinfo{padding:1px 0 0;}
    	.selectchange,.weui_cells{font-size:1.1rem;}
    	.selectchange{color:#00CC99!important;}
    	.selectchange i{font-size:1.2rem;}
    	.weui_label{width:6em;}
    	.readmexy label{display:block;line-height:2em;padding:0 1em 10px;color:#888;}
    	.readmexy label i{color:#00CC99;vertical-align:-1px;}
    	.readmexy label input{display:none;}
    	.readmexy label input ~ i.icon-fangxingxuanzhong:before{content:"\e628";}
    	.readmexy label input:checked ~ i.icon-fangxingxuanzhong:before{content:"\e629";}
    	.weui_btn_primary{border-radius:0;background-color:#00CC99;}
    	.weui_cells_title{color:#666;}
    	#toast .weui_toast{width:80%;margin-left:-40%;}
    	#toast .weui_toast .weui_btn_primary{margin: 0 10px;border-radius: 3px;font-size:1.2rem;}
    </style>
  </head>  
  <body class="confirmBody">
    <div class="confirmIndex">
		<form action="/wzjh/subremote" method="post" id="postorder" name="postorder">
			<input type="hidden" name="openid" value="${openid}"/>
	    	<input type="hidden" name="hosid" value="${hosid}"/>
	    	<input type="hidden" name="depid" value="${depid}"/>
	    	<input type="hidden" name="levelid" value="${splevel}"/>
	    	<input type="hidden" name="timeid" value="${timeid}"/>
	    	<input type="hidden" name="opname" value="${opname}"/>
	    	<input type="hidden" name="opmoney" value="${money}"/>
	    	<input type="hidden" name="optel" value="${optel}"/>
	    	<input type="hidden" name="caseid" value=""/>
	    	<div class="mianinfo blockinfo">
				<div class="weui_cells_title">预约信息</div>
				<div class="weui_cells weui_cells_form">
			        <div class="weui_cell">
			            <div class="weui_cell_hd"><label for="" class="weui_label">就诊医院</label></div>
			            <div class="weui_cell_bd weui_cell_primary">
			                <span class="readonly">${hospital.displayName }</span>
			            </div>
			        </div>
			        <div class="weui_cell">
			            <div class="weui_cell_hd"><label for="" class="weui_label">预约科室</label></div>
			            <div class="weui_cell_bd weui_cell_primary">
			                <span class="readonly">${depart.displayName }</span>
			            </div>
			        </div>
			        <div class="weui_cell">
			            <div class="weui_cell_hd"><label for="" class="weui_label">级别</label></div>
			            <div class="weui_cell_bd weui_cell_primary">
			                <span class="readonly">${leveldesc}</span>
			            </div>
			        </div>
			        <div class="weui_cell">
			            <div class="weui_cell_hd"><label for="" class="weui_label">时间</label></div>
			            <div class="weui_cell_bd weui_cell_primary">
			                <span class="readonly">${time.displayName }</span>
			            </div>
			        </div>
			    </div>
			</div>
			<div class="mianinfo blockinfo">
				<div class="weui_cells_title">补充个人信息</div>
				<div class="weui_cells weui_cells_form">
			        <div class="weui_cell"> <!-- weui_cell_warn -->
			            <div class="weui_cell_hd"><label for="" class="weui_label">姓名</label></div>
			            <div class="weui_cell_bd weui_cell_primary">
			                <input class="weui_input" type="text" name="contactName" placeholder="请输入姓名">
			            </div>
			            <div class="weui_cell_ft"><i class="weui_icon_warn"></i></div>
			            <!-- <div class="weui_cell_hd selectchange"><i class="iconfont">&#xe627;</i>更换</div> -->
			        </div>
			        <div class="weui_cell">
			            <div class="weui_cell_hd"><label for="" class="weui_label">手机号</label></div>
			            <div class="weui_cell_bd weui_cell_primary">
			                <input class="weui_input" type="tel" name="telephone" placeholder="请输入有效的手机号">
			            </div>
			            <div class="weui_cell_ft"><i class="weui_icon_warn"></i></div>
			        </div>
			        <div class="weui_cell">
			            <div class="weui_cell_hd"><label for="" class="weui_label">身份证</label></div>
			            <div class="weui_cell_bd weui_cell_primary">
			                <input class="weui_input" type="text" name="idcard" placeholder="请输入有效的身份证号">
			            </div>
			            <div class="weui_cell_ft"><i class="weui_icon_warn"></i></div>
			        </div>
			        <div class="weui_cell weui_cell_select weui_select_after">
			            <div class="weui_cell_hd">
			                <label for="" class="weui_label">性别</label>
			            </div>
			            <div class="weui_cell_bd weui_cell_primary">
			                <select class="weui_select" name="sex">
			                    <option value="1">男</option>
			                    <option value="0">女</option>
			                </select>
			            </div>
			        </div>
			        <div class="weui_cell">
			            <div class="weui_cell_hd"><label for="" class="weui_label">年龄</label></div>
			            <div class="weui_cell_bd weui_cell_primary">
			                <input class="weui_input" type="number" name="age" placeholder="请输入年龄">
			            </div>
			            <div class="weui_cell_ft"><i class="weui_icon_warn"></i></div>
			        </div>
			    </div>
			</div>
			<div class="baseinfo blockinfo">
				<div class="weui_cells_title">疾病信息</div>	
				<div class="weui_cells weui_cells_form">
			        <div class="weui_cell">
			            <div class="weui_cell_bd weui_cell_primary">
			                <input class="weui_input" type="text" name="caseName" placeholder="请输入疾病名称">
			            </div>
			            <div class="weui_cell_ft"><i class="weui_icon_warn"></i></div>
			        </div>
					<div class="weui_cell">
			            <div class="weui_cell_bd weui_cell_primary">
			                <textarea class="weui_textarea" name="description" placeholder="请详细描述您的病情，症状，治疗经过以及需要获得的帮助" rows="10"></textarea>
			                <!-- <div class="weui_textarea_counter"><span>0</span>/200</div> -->
			            </div>
			        </div>
				</div>
			</div>
			
			<div class="baseinfo blockinfo">
				<div class="weui_cells_title">上传影像资料</div>
				<div class="weui_cells weui_cells_form">
			        <div class="weui_cell">
			            <div class="weui_cell_bd weui_cell_primary">
			                <div class="weui_uploader">
			                    <!-- <div class="weui_uploader_hd weui_cell">
			                        <div class="weui_cell_bd weui_cell_primary">上传影像资料</div>
			                        <div class="weui_cell_ft">0/2</div>
			                    </div> -->
			                    <div class="weui_uploader_bd">
			                        <ul class="weui_uploader_files"></ul>
			                        <div id="uploadpics" class="weui_uploader_input_wrp"></div>
			               	 		<input type="hidden" name="caseImages" id="caseImages" />
			                    </div>
			                    <label style="color:#aaa;font-size:12px;display: block;">症状部位，检查报告或者其他病情资料</label>			                    
			                </div>
			            </div>
			        </div>
				</div>
			</div>
			<div class="footer" style="margin-top:10px;">
				<div class="readmexy">
					<!-- <label>
						<input type="checkbox" name="readme" value="1" checked /><i class="iconfont icon-fangxingxuanzhong"></i>
						<span>我已了解并同意服务规则</span>
					</label> -->
				</div>
				<div class="btns">
					<a href="javascript:;" id="submitForm" class="weui_btn weui_btn_primary">
						<c:choose>
							<c:when test="${money > 0 }">
								微信支付(￥${money})
							</c:when>
							<c:otherwise>提交</c:otherwise>
						</c:choose>
					</a>
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
			f.remoteConfirm();
		});
	</script>
  </body>
</html>
