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
    <title>我的设置</title>
		<meta charset="UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=EDGE"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<link href="favicon.ico" rel="shortcut icon" />
		<link rel="stylesheet" href="/css/bootstrap.min.css" />
		<link rel="stylesheet" href="/css/bootstrap-responsive.min.css" />
		<link rel="stylesheet" href="/css/uniform.css" />
		<link rel="stylesheet" href="/css/matrix-style2.css" />
		<link rel="stylesheet" href="/css/matrix-media.css" />
		<link rel="stylesheet" href="/css/unicorn.grey.css" />
		<link rel="stylesheet" href="/fonticon/myset/iconfont.css" />
		<link rel="stylesheet" href="/sea-modules/calendar/css/fullcalendar.css" />
        <style type="text/css">
        	.widget-content,.widget-box{border:0;}
			.state2,.editing .state1{display:none;}
			.editing .state2{display:inline-block;}
			.addarea{text-align:right; padding:10px 0;}
			.addarea .btn{width:8em;margin-right:10px;}
			.table1{width:100%;table-layout:fixed;border:1px solid #5DB2C6;background-color:#fff;}
			.table1 thead{background-color:#28b779;border-bottom:1px solid #5DB2C6;color:#fff;}
			.table1 tbody td{border-bottom:1px solid #5DB2C6;}
			.table1 .td{border-right:1px solid #5DB2C6;line-height:26px;margin:3px 0;display:block;white-space: nowrap;overflow:hidden;box-sizing:border-box;}
			
			.table1 tr th:last-child .td,.table1 tr td:last-child .td{border:0;}			
			.table1 .td .text,.table1 .td .inputs{margin:0;line-height:1.8em;}
			.table1 .td .text{padding:0 4px;overflow:hidden;}
			.table1 tr.editing{background-color:#FFE2E2;}
			table tr:hover{background:inherit;}
        </style>    
		<style>
			.widget-box{margin-bottom:20px;padding-bottom: 10px;}
			.widget-title,.widget-title:hover{background:transparent url();color:#1DCBFD;border-bottom-color:#E3EAEC;}
			.widget-title h5{color:#1DCBFD;font-size:15px;padding-left:15px;}
			.widget-title span{text-align:right;margin-top:4px;}
			.widget-content{background:transparent url();margin:10px;}
			.table1 .td .btn1{border:0;padding:4px;background:transparent;}
			.table1 .td .btn1 .iconfont{font-size:20px;}
			.table1 .td .js-delinfo .iconfont{color:#D9534F;}
			.table1 .td .js-editinfo .iconfont{color:#28b779;}
			.table1 .td .js-saveinfo .iconfont{color:#337AB7;}
			.table1 .td .js-cancelinfo .iconfont{color:#D9534F;}
			.table1 .td [type="text"],
			.table1 .td [type="number"],
			.table1 .td select{border:0;background:transparent url();width:100%;box-shadow:none;padding:0;margin:0;}
			.table1 .td .timerang{display:table-row;}
			.table1 .td .timerang span{display:table-cell;}
			.table1 .td .timerang input{text-align:center;}
			.modal2 .controllabel{font-size:15px;margin-right:10px;}
			.modal2 .controllabel b{color:Red;font-size:18px;}
			.modal2 .controllabel input{width:100%;margin-top:4px;}
			
			.datetimepicker .switch{position:relative;}
			.datetimepicker .switch:after{content:"";position:absolute;left:0;top:0;width:100%;height:100%;}
			
			.deling .noresult{position:fixed;top:26px;left:50%;margin-left:-120px;width:240px;min-height:30px;z-index:10000;padding:8px 0;
				background-color:#51A6CB;color:#fff;text-align:center;line-height:30px;border-radius:3px;opacity: .8;}
			.deling .noresult.success{background-color:#69BC3A;z-index:10001;}
			.deling .noresult.error{background-color:#DF494A;z-index:10001;}
			.btn-content {color: #FFF;background: #08c url();border: 1px solid #64BAC7;border-radius: 3px;text-shadow: 0px 1px 1px #666;}
			input[type="checkbox"]{
				overflow: hidden;
  				*zoom: 1;
			}
			.hasdaterang > *{float:left;margin:10px 5px 0 0;}
			.hasdaterang span{line-height:30px;}
			
			.fc-event-time{display:none;}
			#fullcalendar{margin-top:20px;border-top:1px dashed #ccc;padding-top:20px}
		</style>  
		<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
		<script src="/sea-modules/bootstrap/js/bootstrap.min.js"></script>
		<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
		<script src="/sea-modules/seajs/seajs.config.js"></script>
	</head>
	<body>
		<div id="content">	
			<div class="container-fluid">
				<div class="row-fluid">
					<div class="span12">						
						<div class="widget-box" id="models_show">
							<div class="widget-title noscript">
								<h5>远程会诊时间排班</h5>
							</div>
							<div class="widget-content nopadding" data-tablespace="RemoteTime" data-action="/expert/saveorupdatert">
								<table class="table1">
									<thead>
										<tr>
											<th><span class="td">时间（周）</span></th>
											<th><span class="td">时段</span></th>
											<th><span class="td">状态</span></th>
											<th><span class="td">备注</span></th>
											<th><span class="td">操作</span></th>											
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${times}" var="time">
											<tr id="${time.id}">
												<td><span class="td"><label class="text" data-val="${time.weekDay}" data-option="week">${time.weekDay}</label></span></td>
												<td><span class="td"><label class="text" data-val="${time.startTime},${time.endTime}">${time.startTime} 至 ${time.endTime}</label></span></td>
												<td><span class="td"><label class="text" data-val="${time.status}" data-option="state">${time.status}&nbsp;</label></span></td>
												<td><span class="td"><label class="text" data-val="${time.remark}">${time.remark}&nbsp;</label></span></td>
												<td><span class="td center">												
													<button class="btn1 state1 js-editinfo"><i class="iconfont">&#xe606;</i></button>
													<button class="btn1 state2 js-saveinfo"><i class="iconfont">&#xe60b;</i></button>
													<button class="btn1 state2 js-cancelinfo"><i class="iconfont">&#xe609;</i></button>
													<button class="btn1 state1 js-delinfo"><i class="iconfont">&#xe607;</i></button>
												</span></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>							
								<div class="addarea">
									<button class="btn btn-content js-addinfo">新增</button>
								</div>
							</div>
						</div>
						<div class="widget-box" id="models_show">
							<div class="widget-title noscript">
								<h5>生成专家出诊时间</h5>
							</div>
							<div class="widget-content nopadding">
								<div class="clearfix hasdaterang">
									<input type="text" name="start" placeholder="开始时间" ltype="dateRang"/><span>至</span>
									<input type="text" name="end" placeholder="结束时间" ltype="dateRang"/>
									<button class="btn btn-content js-inittime">生成</button>
								</div>
								<div id="fullcalendar"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
        <script>
        	var _href = '/',expertid = '${expertid}';
			seajs.use('view/myset/myset');
		</script> 	
		<script src='/sea-modules/calendar/js/fullcalendar.min.js'></script>
		<script>
			$('#fullcalendar').fullCalendar({
				header: {
					left: 'prev,next today',
					center: 'title',
					right: 'month,agendaWeek,agendaDay'
				},
				firstDay:1,
				events: '/expert/gainExpertTimeDatas?expertid=${expertid}'
			});
		</script>	
	</body>
</html>


