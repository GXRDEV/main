<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE html>
<html>
  <head>
    <base href="/">
    <title>服务评价</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
	<style>
		#index{margin-top:6px;background-color:#fff;}
		.icon_stars2x{display:inline-block;height:20px;width:110px;vertical-align:middle;
			background:url(/img/mobile/kbzs/icon_stars_j.png) no-repeat 0 0;}
		.icon_stars2x0{background-position:-110px 0;}
		.icon_stars2x1{background-position:-88px 0;}
		.icon_stars2x2{background-position:-66px 0;}
		.icon_stars2x3{background-position:-44px 0;}
		.icon_stars2x4{background-position:-22px 0;}
		.icon_stars2x5{background-position:0 0;}
		form{margin:0 15px;}
		form p{line-height:1.8em;padding:5px 0;}
		p textarea{width:100%;height:100px;padding:5px;box-sizing:border-box;border:1px solid #ccc;border-radius:3px;line-height:1.6em;}
		p label{font-weight:600;color:#10B5F0;font-size:14px;}
		.title{padding:10px 0;border-bottom:1px solid #F6F6F6;color:#666;font-size:0;}
		.title span{font-size:16px;display:inline-block;width:50%;}
		.title span:last-child{text-align:right;color:#aaa;font-size:10px;}
		.stars{position:relative;}
		.stars input{position:absolute;top:2px;left:0;opacity:0;}
		.line{height:0;border-bottom:1px solid #e6e6e6;margin:6px 0;}
		#callme{background-color:#f4f4f4;border-top:1px solid #888;padding:10px;color:#999;font-size:12px;}
		#callme button{display:block;position:absolute;right:3px; bottom: 50%; margin-bottom: -14px;color:#fff;padding:6px 16px;border-radius:4px;
			background:#0AB3EF;border:0;font-size:14px;}
	</style>
  </head>  
  <body>
    <div id="index">
    	<form action="/wtspz/serverestimate.do" method="post">
	    	<div class="title">
	    		<span><b>服务评价</b></span>
	    		<span>满意请给五星哦！</span>
	    	</div>
			<p>
				<label>对服务人员的评价</label>&ensp;
				<span class="stars stars1">
					<i class="icon_stars2x icon_stars2x0"></i>
					<input type="radio" name="nursePraLevel" value="1" style="left:3px" />
					<input type="radio" name="nursePraLevel" value="2" style="left:27px"  />
					<input type="radio" name="nursePraLevel" value="3" style="left:50px"  />
					<input type="radio" name="nursePraLevel" value="4" style="left:73px"  />
					<input type="radio" name="nursePraLevel" value="5" style="left:97px"  />
				</span>
			</p>
			<p style="color:#aaa;padding-top:0;">
				提示：对本次服务人员进行评论，您的评价将直接影响服务人员的收入哦
			</p>
			<div class="line"></div>
			<p>
				<label>对本次就诊服务的意见与反馈</label>
			</p>
			<p>
				<textarea id="contxt2" name="ordercomment_txt" placeholder="写下您对我们整个陪诊过程的评价，意见与建议，我们会努力不断完善产品。"></textarea>
				<input type="hidden" id="contxt2_hidden" name="ordercomment" />
			</p> 
			<input type="hidden" name="orderid" value="${orderid}"/>
			<div id="callme" class="fixed" style="z-index:999;">
				<p>感谢您的评价与支持，我们会断续努力！</p>
				<button type="submit">提交</button>
			</div>   	
    	</form>
    </div>
	<div style="height:40px;">&nbsp;</div>
	<script src="/libs/jquery-1.11.0.min.js"></script>
	<script>
		$(document).ready(function(){
			$('#index p .stars input').change(function(){
				var v = this.value;
				(this.checked && v) && $(this).siblings('i').attr('class','icon_stars2x icon_stars2x' + v);
			});
			$('#callme button').click(function(){
				var t1 = $('#contxt1').val(),t2 = $('#contxt2').val(),
					star1 = $('.stars1 input:checked').val(),star2 = $('.stars2 input:checked').val();
				if(!star1){ 
					alert('请选颗对服务人员的星级评论吧');
					return false;
				}
				$('#contxt1_hidden').val(encodeText(t1));
				$('#contxt2_hidden').val(encodeText(t2));
			});
		});
		
		function encodeText(t){
			return encodeURI(t);
		}
		function unencodeText(t){
			var _t = '';
			try{
				_t = decodeURI(t);
			}catch(e){
				_t = unescape(t);
			}
			return _t;
		}
	</script>
  </body>
</html>
