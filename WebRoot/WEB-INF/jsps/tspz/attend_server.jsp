<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>天使陪诊</title>
    <meta name="viewport" content="width=device-width,  user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mui/mui.dtpicker.css" />
	<link rel="stylesheet" type="text/css" href="/css/mui/mui.listpicker.css" />
	<link rel="stylesheet" type="text/css" href="/css/mui/mui.min.css" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
    <style>
    	body{font-size:14px;background-color:#F4F4F4;}
    	section{border:1px solid #ccc;border-radius:4px;margin:8px 6px;background-color:#FFF;}
    	section > div{margin:0 3px;padding-left:7em;}
    	section > div + div{border-top:1px solid #eee;}
    	section input,
    	section select{width:100%;height:26px;line-height:26px;box-sizing:border-box;border:0;background:transparent;}
    	section label{margin-left:-6.2em;display:inline-block;width:5.5em;box-sizing:border-box;padding:1.2em 0;color:#555;font-weight:600}
    	section div > p{padding-right:1em;text-align:right;width:100%;box-sizing:border-box;display: inline-block;}
    	.s{padding:12px 10px 12px 12.4px;font-size:0;}
    	.s label{display:inline-block;width:30%;font-size:14px;background-color:#F1F1F1;text-align:center;color:#777;padding:6px 0;margin:auto;font-weight:400}
    	.s input{display:none;}
    	.s input:checked + label{background-color:#0078D7;color:#fff;}
    	.s label ~ label{margin-left:5%;}
    	.s textarea{width:100%;height:60px;border:1px solid #cecece;margin-top:10px;border-radius:3px;font-size:14px;padding:5px;-webkit-appearance: none;}
    	.d {padding:20px 10px 0;font-size:0;}
    	.d span{display:inline-block;width:50%;font-size:12px;color:#666;}
    	.d span + span{text-align:right;}
    	#time{display:inline-block;width:98%;color:#aaa}

    </style>
    <style type="text/css">
		.controls{position:relative;display: inline-block;width: 84%;}
		.labelcolor .controls{color:#26B5ED;}
		.inputs{position:relative;width:100%;padding:5px 0;box-sizing:border-box;border-color:transparent;z-index:4;}
		.custom-name select{position:absolute;right:-40px;top:0;width:100%;height:100%;z-index:1; opacity: 0;}
		.custom-name{ margin-right: 30px;}
		.custom-name:before{content:"";display:block;width:9px;height:9px;position:absolute;right:-20px;top:5px;
			border:1px solid #999; border-left:0;border-top:0; z-index:0;
			transform:rotate(45deg);
			-webkit-transform:rotate(45deg);
		}
    </style>
</head>
<body>
	<div id="index" class="index">
		<form action="/wtspz/placeAnOrder.do" method="post" id="postorder" name="postorder">
		<input type="hidden" value="2" name="serviceID">
		<input type="hidden" value="${openid}" name="openid">
		<section>
			<div class="controls-group">
				<label for="historyName" class="controls-label">患者姓名</label>
				<div class="controls custom-name">
					<input type="text" class="inputs" maxlength="10" name="linkman" id="name" placeholder="请输入您的姓名"/>
					<select name="historyName" id="historyName">
						<c:forEach items="${contacts}" var="info">
							<option value="${info.id }|${info.userRealName }|${info.idCard }|${info.telphone }">${info.userRealName}</option>
						</c:forEach>
						<option value=""> </option>
					</select>
				</div>
			</div>
			<div>
				<label for="tel">手机</label>
				<input type="tel" name="telephone" id="tel" placeholder="请输入您的手机号" value="${tel}"/>
			</div>
		</section>
		<section>
			<div>
			   <label for="date">时间</label>
               <a id="time" data-options='{"type":"hour","customData":{"h":[{"text":"上午","value":"上午"},{"text":"下午","value":"下午"}]},"labels":["年", "月", "日", "时段", "分"]}' class="btn" style="width:100%">选择时间</a>
               <input type="hidden" id="orderhomeform-service_date" name="orderTime"/>
            </div>
			<div>
				<label for="address">服务地点</label>
				<input type="text" name="serviceAddress" id="address" placeholder="请输入医院名称、位置"/>
			</div>
			<div class="s">
				<c:forEach items="${items}" var="item" varStatus="stat">
					<c:if test="${stat.index==0}">
						<input type="radio" name="serviceItems" id="le${stat.index+1}" value="${item.id}" gprice="${item.guidePrice}" checked/>
					</c:if>
					<c:if test="${stat.index!=0}">
						<input type="radio" name="serviceItems" id="le${stat.index+1}" value="${item.id}"  gprice="${item.guidePrice}"/>
					</c:if>
					<label for="le${stat.index+1}">${item.itemName}</label>
				</c:forEach>
				<textarea id="distribe" name="serviceNeeds" placeholder="请输入要求（50字以内）"></textarea>
			</div>
		</section>
		<p class="d">
			<span>服务介绍</span>
			<span>指导价 ￥${items[0].guidePrice}元</span>
		</p>
		<section style="margin:10px 6px 0">
			<div>
				<label for="price">我的报价</label>
				<p><b style="color:#FF0702;">${items[0].guidePrice}</b>&ensp;元</p>
				<input type="hidden" value="${items[0].guidePrice}" name="fixedPrice"/>
			</div>
		</section>
		<div class="g_fixed" style="padding:5px;">
			<button class="buttonClassgreen" id="btnsubmit" style="line-height: 30px;">我要预约</button>
		</div>
		</form>
	</div>  
	<script src="/libs/jquery-1.11.0.min.js"></script>
    <script src="/js/base.js"></script>
    <script src="/js/view/plus_server.1.0.js"></script>
    
	<script src="/js/mui/mui.min.js"></script>
    <script src="/js/mui/mui.listpicker.js"></script>
    <script src="/js/mui/mui.dtpicker.js"></script>
    <script>
            (function($) {
                    $.init();
                    var btns = $('.btn');
                    var pickers = {};
                    btns.each(function(i, btn) {
                            btn.addEventListener('tap', function() {
                                    var optionsJson = this.getAttribute('data-options') || '{}';
                                    var options = JSON.parse(optionsJson);
                                    var id = this.getAttribute('id');
                                    var oHiddenDate = document.getElementById('orderhomeform-service_date'); 
                                    var oThis = this;  
                                    pickers[id] = pickers[id] || new $.DtPicker(options);
                                    pickers[id].show(function(rs) {
                                       oThis.innerText = rs.text;
                                       oHiddenDate.value=rs.text;    
                                    });
                            }, false);
                    });
            })(mui);
    </script>
    <script type="text/javascript">
    	$('#historyName').change(function(){
			var info = $(this).val() || ('|||');
			info = info.split('|');
			$('#name').val(info[1]);
			$('#card').val(info[2]);
			$('#tel').val(info[3]||'${tel}');
		}).change();
    </script>
</body>
</html>