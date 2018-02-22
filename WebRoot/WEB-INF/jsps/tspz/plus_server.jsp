<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>天使陪诊-加号</title>
    <meta name="viewport" content="width=device-width,  user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
   
    <link rel="stylesheet" type="text/css" href="/css/mui/mui.dtpicker.css" />
	<link rel="stylesheet" type="text/css" href="/css/mui/mui.listpicker.css" />
	<link rel="stylesheet" type="text/css" href="/css/mui/mui.min.css" />
	  
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
    <style>
    	body{font-size:14px;background-color:#F4F4F4;}
    	section{border:1px solid #ccc;border-radius:4px;margin:8px 6px;background-color:#FFF;overflow:hidden;}
    	section > div{margin:0 3px;}
    	section > div + div{border-top:1px solid #eee;}
    	section input[type="text"],section input[type="tel"],section input[type="number"],
    	section select{width:100%;height:26px;line-height:26px;box-sizing:border-box;border:0;background:transparent;-webkit-appearance: none;}
    	section div > p{padding-right:1.4em;text-align:right;width:100%;box-sizing:border-box;display: inline-block;}
    	.s{padding:12px 10px 12px 12.4px;font-size:0;}
    	.s label{display:inline-block;width:30%;font-size:14px;margin:auto;padding:0;position:relative;}
    	.s label input{position:absolute;left:0;top:0;opacity:0;z-index:5;width:100%;height:26px;}
    	.s label span{display:block; background-color:#F1F1F1;text-align:center;color:#777;padding:6px 0;font-weight:400;border-radius:2px;}
    	.s .zjName{display:none;border:1px solid #cecece;margin-top:10px;border-radius:3px;font-size:14px;padding:8px;height:auto;}
    	.s input:checked + span{background-color:#42D2D9;color:#fff;}
    	#le3:checked + span:after{content:"";display:block;width:0;height:0;border-width:14px 20px;border-style:solid;
    		border-color:#42D2D9 transparent transparent transparent;position:absolute;bottom:-22px;left:50%;margin-left:-20px;}
    	.s label ~ label{margin-left:5%;}
    	.s textarea{width:100%;height:60px;border:1px solid #cecece;margin-top:10px;border-radius:3px;font-size:14px;padding:5px;-webkit-appearance: none;}
    	
    	.d {padding:20px 10px 0;font-size:0;}
    	.d >span{display:inline-block;width:50%;font-size:12px;color:#666;}
    	.d span + span{text-align:right;}
		.linput{position:relative;}
		.linput input{text-align:right;padding-right:2em;}
		.linput span{position:absolute;right:0.8em;top:0.33em;}    	
    </style>
    <style type="text/css">
		.labelcolor .controls{color:#26B5ED;}
		.custom-name .inputs{position:relative;width:90%;padding:5px 0;box-sizing:border-box;border-color:transparent;z-index:4;}
		.custom-name select{position:absolute;right:-50%;top:0;width:100%;height:100%;z-index:1; opacity: 0;}
		.custom-name{ margin-right: 30px;}
		.custom-name:before{content:"";display:block;width:9px;height:9px;position:absolute;right:-20px;top:5px;
			border:1px solid #999; border-left:0;border-top:0; z-index:0;
			transform:rotate(45deg);
			-webkit-transform:rotate(45deg);
		}
		.jstime,.controls-group{display: -moz-box;  display: -webkit-box; display: box;padding:0;position:relative;}			
		.jstime .s{padding:0;font-size:14px;}
		.jstime a,.jstime .s label{width:auto;display:block;}
		.jstime a{padding:4px 0;color:#888;}

		.controls-group{padding:1.2em 0;}
		.controls{position:relative;display:block; -webkit-box-flex: 1; box-flex: 1;}
		.controls-label{display:inline-block; width:6em;box-sizing:border-box;color:#555;font-weight:600;line-height: 26px;padding:0 0.6em;}		
    </style>
    <style>
	    #_outer{width:100px;height:2em;line-height:2em;position:absolute;right:0;top:-2px; border-radius:1em;z-index:10;overflow:hidden;}    
	    #_texts{display:-moz-box;display:-webkit-box;display:box;width:200%;position:absolute;right:0;top:0;}
	    #_texts .ui-label{display:block;height:100%;-moz-box-flex:1;-webkit-box-flex:1;box-flex:1;font-weight:normal;box-sizing:border-box;}    
	    #_texts .ui-label1{background-color:#42D2D9;color:#fff;text-align:left;padding-left:1em;}    
	    #_texts .ui-label2{background-color:#d8d8d8;text-align:right;padding-right:1em;}    
	    #_drager{position:absolute;width:1.8em;height:4em;top:-1em;left:0.1em;}
	    #_drager .lab{position:absolute;width:1.8em;height:1.8em;left:0;right:0;bottom:0;top:0;margin:auto;cursor:move;box-shadow:0 0 2px 2px #eee inset;background-color:#fff;border-radius:50%;}
	    #_drager input{display:none;}
    </style>
</head>
<body>
	<div id="index" class="index">
		<form action="/wtspz/placeAnOrder.do" method="post" id="postorder" name="postorder">
		<input type="hidden" value="1" name="serviceID">
		<input type="hidden" value="${openid}" name="openid">
		<section>
			<div class="controls-group">
				<label for="historyName" class="controls-label">姓名</label>
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
			<div class="controls-group">
				<label class="controls-label" for="tel">手机</label>
				<div class="controls"><input type="tel" name="telephone" id="tel" placeholder="请输入您的手机号" value="${tel}"/></div>
			</div>
			<div class="controls-group">
				<label class="controls-label" for="card">身份证号</label>
				<div class="controls"><input type="text" name="idCard" id="card" placeholder="请输入您的身份证号"/></div>
			</div>
		</section>
		<section>
			<div class="controls-group">
				<label class="controls-label" for="hospital">医院</label>
				<div class="controls custom-name">
					<input type="text" name="hospital" class="inputs" id="hospital" placeholder="请输入医院"/>
					<select name="hospitalName" id="hospitalName">
						<option value=""> </option>
						<c:forEach items="${hospitals}" var="info">
							<option value="${info.hospitalId}">${info.hospitalName}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="controls-group">
				<label class="controls-label" for="department">科室</label>
				<div class="controls custom-name">
					<input type="text" name="department" class="inputs" id="department" placeholder="请输入科室"/>
					<select name="departmentName" id="departmentName">
						<option value=""> </option>
					</select>
				</div>
			</div>
			<div class="controls-group">
				<label class="controls-label" for="time">就诊时间</label>				                
                <div class="controls">
                	<div class="jstime">
	                	<div class="div1">
	                		<a id="time" data-options='{"type":"hour","customData":{"h":[{"text":"上午","value":"上午"},{"text":"下午","value":"下午"}]},"labels":["年", "月", "日", "时段", "分"]}' class="btn">选择时间</a>
	                		<input type="hidden" id="orderhomeform-service_date" name="orderTime"/>
	                	</div>
	                	<div class="s div2">
		                	<div class="outer" id="_outer">
								<div id="_texts" class="ui-slider-inneroffset">
								  <b class="ui-label ui-label1">越快越好</b>
								  <b class="ui-label ui-label2">越快越好</b>
								</div>
								<div id="_drager">
									<b class="lab"></b>
								  	<input type="checkbox" name="notime" id="notime" value="yes" />
								</div>
							</div>
						</div>
						         	
                	</div>
                </div>
			</div>
			
			<div class="s" style="clear:both">
				<c:forEach items="${items}" var="item" varStatus="stat">
					<label for="le${stat.index+1}">
						<c:if test="${stat.index==0}">
							<input type="radio" name="serviceItems" id="le${stat.index+1}" value="${item.id}" gprice="<fmt:formatNumber type="number" value="${item.guidePrice}" maxFractionDigits="0"/>" checked="checked"/>
						</c:if>
						<c:if test="${stat.index!=0}">
							<input type="radio" name="serviceItems" id="le${stat.index+1}" value="${item.id}" gprice="<fmt:formatNumber type="number" value="${item.guidePrice}" maxFractionDigits="0"/>"/>
						</c:if>
						<span>${item.itemName}</span>
					</label>
				</c:forEach>
				<input type="text" class="zjName" name="expertName" id="zjName" placeholder="请输指定专家姓名"/>
				<textarea id="distribe" name="serviceNeeds" placeholder="请输入要求（50字以内）"></textarea>
			</div>
			
		</section>
		<p class="d">
			<span>服务介绍</span>
			<span>指导价 ￥<span class="gprice">${items[0].guidePrice}</span>元</span>
		</p>
		<section style="margin:10px 6px 0">
			<div class="linput controls-group">
				<label for="fixedPrice" class="controls-label">我的报价</label>
				<div class="controls">
					<input type="number" name="fixedPrice" style="color:red;padding-right:5em;" id="fixedPrice" value="<fmt:formatNumber type="number" value="${items[0].guidePrice}" maxFractionDigits="0"/>"/>
					<span>元</span>	
				</div>		
			</div>
		</section>
		<div class="g_fixed" style="padding:5px;">
			<button type="button" class="buttonClassgreen" id="btnsubmit" style="line-height: 30px;">我要预约</button>
		</div>
		</form>
	</div> 
	<div style="height:50px;">&nbsp;</div>
	<script src="/libs/jquery-1.11.0.min.js"></script>
    <script src="/js/base.js"></script>
    <script src="/js/view/plus_server.1.0.js"></script>
    <script>
    	var off = false;
    	$(function(){
    		$('.s :radio').change(function(){
    			var p = this.getAttribute('gprice');
    			if(!this.checked) return false;
    			if(this.id=='le3'){
    				$('#zjName').show();
    			}else{
    				$('#zjName').hide();
    			}
    			$(".gprice").html(p);
    			$("#fixedPrice").val(p);
    		});
    		$('#notime').change(function(){
    			var t = $('#time');
    			this.checked ? (off = true,t.html('不指定，越早越好'),$('#orderhomeform-service_date').val('不指定，越早越好')): (off = false,t.html('选择时间'));
    		});
    	});
    </script> 
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
                                    if(off) return false; 
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
    	$('.custom-name select').change(function(){
    		var t = $(':selected',this).text();
    		$(this).siblings(':input').val(t);
    	});
    	$('#historyName').change(function(){
			var info = $(this).val() || ('|||');
			info = info.split('|');
			$('#name').val(info[1]);
			$('#card').val(info[2]);
			$('#tel').val(info[3]||'${tel}');
		}).change();
		$('#hospitalName').change(function(){
			if(!$(this).val()) return false;
			$.post(
				"/wtspz/loaddeparts.do",
				{
					hosid:$(this).val()
				},
				function(data){
					var os = '<option value=""> </option>';
					data.departs && $.each(data.departs,function(i,o){
						os += '<option value="'+ o.officeId +'">'+ o.officeName +'</option>';
					});
					$('#departmentName').html(os);
				}
			);
		});
    </script>
    <script>
	    ;(function(){
	      var d = document.querySelector('#_drager'),
	          out = document.querySelector('#_outer'),
	          inner = document.querySelector('#_texts'),
	          checkbx = document.querySelector('#_drager input'),
	          outWidth = out.clientWidth,
	          dWidth = d.offsetWidth,
	          halfWidth = outWidth / 2,
	          startX = out.offsetLeft,
	          x = 0;
	      d.onmousedown = function(ev){
	          document.onselectstart= function(){return false;}; 
	          startX = ev.pageX;
	          document.onmousemove = function(ev){
	            return _touchmove(ev);
	          };
	      };
	      d.onmouseup = document.onmouseup = function(ev){
	          document.onselectstart = document.onmousemove = null;
	          if(!x) return false;      
	          _touchend(halfWidth,x);
	          x = 0;
	      };
	      inner.onclick = function(){
	          checkbx.checked ? _touchend(1,0) : _touchend(0,1);
	      };
	      d.ontouchstart = function(ev) {
	          ev.preventDefault();
	          if (!d || !ev.touches.length) return;
	          var touch = ev.touches[0];
			  startX = touch.pageX;
	          d.ontouchmove = function(ev) {
	            ev.preventDefault();
	            if (!d || !ev.touches.length) return;
	            var touch = ev.touches[0];
	            _touchmove(touch);
	            return true;
	          };
	
	          return true;
	      };
	      d.ontouchend = function(ev) {
	          ev.preventDefault();
	          document.onselectstart = d.ontouchmove = null;
	          if(!x) return false;
	          _touchend(halfWidth,x);
	          x = 0;
	          return false;
	      };
	      function _touchmove(ev){
	          x = ev.pageX - startX;
	          var _x = Math.abs(x),cw = outWidth - dWidth;
	          if(_x >= cw || _x <= 0 ){
	              return false;
	          }
	          if(x >= 0){
	              d.style.left = x + 'px';
	              inner.style.right = '-'+ (x + parseInt(dWidth/2,10)) +'px';
	          }else{
	              d.style.left = (cw + x) + 'px';
	              inner.style.right = '-'+ (cw + x + parseInt(dWidth/2,10)) +'px';
	          }
	          return true;
	      }
	      function _touchend(halfWidth,x){
	        if(halfWidth < x){
	            d.style.left = 'auto';
	            d.style.right = '2px';
	            d.style.webkitTransition = 'right 0.8s';
	            inner.style.right = '-100%';
	            inner.style.webkitTransition = 'right 0.5s';
	            checkbx.checked = true;
	          }else{
	            d.style.left = '2px';
	            d.style.right = 'auto';
	            d.style.webkitTransition = 'left 0.8s';
	            inner.style.right = '0px';
	            inner.style.webkitTransition = 'right 0.5s';
	            checkbx.checked = false;
	          }
	      	  $('#notime').change();
	      }
	    })();
  </script>
</body>
</html>