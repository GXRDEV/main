<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE HTML>
<html>
  <head>
    	<base href="/">
   		<title>出诊日期</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />	
		<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=EDGE"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<link href="favicon.ico" rel="shortcut icon" />
		<link rel="stylesheet" href="css/bootstrap.min.css" />
		<link rel="stylesheet" href="css/bootstrap-responsive.min.css" />
		<link rel="stylesheet" href="css/unicorn.main.css" />
		<link rel="stylesheet" href="css/unicorn.grey.css" />
		<link rel="stylesheet" href="css/datepicker.css" />
		<link rel="stylesheet" href="calendar/css/fullcalendar.css" />	
		<link rel="stylesheet" href="calendar/css/fancybox.css">	
        <style type="text/css">
        	.container-fluid .row-fluid:first-child{margin-top:0;}
        	.widget-box{margin-top:0;}        	
        </style>
        <style type="text/css">
			.fancy{width:450px; height:auto}
			.fancy h3{height:30px; line-height:30px; border-bottom:1px solid #d3d3d3; font-size:14px}
			.fancy form{padding:10px}
			.fancy p{height:28px; line-height:28px; padding:4px; color:#999}
			.input{height:20px; line-height:20px; padding:2px; border:1px solid #d3d3d3; width:100px}
			.btn{-webkit-border-radius: 3px;-moz-border-radius:3px;padding:5px 12px; cursor:pointer}
			.btn_ok{background: #360;border: 1px solid #390;color:#fff}
			.btn_cancel{background:#f0f0f0;border: 1px solid #d3d3d3; color:#666 }
			.btn_del{background:#f90;border: 1px solid #f80; color:#fff }
			.sub_btn{height:32px; line-height:32px; padding-top:6px; border-top:1px solid #f0f0f0; text-align:right; position:relative}
			.sub_btn .del{position:absolute; left:2px}
			.fc-header-right {
			    position: absolute !important;
			    right: 0;
			    text-align: right;
			    top:-1px;
			}
			.fc-header-left{position: absolute !important; top:0;left:0;}
			.fc-button-month,.fc-button-agendaWeek,.fc-button-agendaDay{
				border-left: 1px solid #D5D5D5;
			    height: 36px;
			    line-height: 37px;
			    padding: 0 14px;
			    white-space: nowrap;
			}
			.fc-state-active{
				 background: none repeat scroll 0 0 #F9F9F9;
			     color: #797979;
			     border: none;
			}	
			.fc-event-time{
				display:none;
			}		
		</style>
        <script src="/js/jquery-1.8.3.js"></script>
        <script src="/js/bootstrap.min.js"></script>
        <script src="/js/unicorn.js"></script>
		<script src="/calendar/js/jquery-ui.js"></script>
		<script src='/calendar/js/fullcalendar.min.js'></script>
		<script type="text/javascript">
			$(function() {
				//页面加载完初始化日历 
				$('#fullcalendar').fullCalendar({
					//设置日历头部信息
					header: {
						left: 'prev,next today',
						center: 'title',
						right: 'month,agendaWeek,agendaDay'
					},
					firstDay:1,//每行第一天为周一 
			        editable: true,//启用拖动 
					events: '/expert/gainExpertTimeDatas'
				});
			});
		</script>
	</head>
	<body>
		<div id="content">
			<div id="content-header">
				<h1>我的日程</h1>
			</div>
			<div class="container-fluid">
				<div class="row-fluid">
					<div class="span12">
						<div class="widget-box widget-calendar">
							<div class="widget-title">
								<span class="icon"><i class="icon-calendar"></i></span>
								<h5></h5>								
							</div>
							<div class="widget-content nopadding">
								<div >
									<div id="fullcalendar" style="width:100%"></div>
								</div>								
							</div>
						</div>
					</div>
				</div>			
			</div>
		</div> 
	</body>
</html>
