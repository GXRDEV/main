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
	    
	    <title>首页</title>
	    <meta charset="UTF-8" />
	    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/bootstrap/3/css/bootstrap.css" /> 
		<link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.css" />
	    <link href="/sea-modules/select2/css/select2.min.css" rel="stylesheet" />
	    <link href="/font-awesome/css/font-awesome.css" rel="stylesheet" />
	    
	    <style>
	    	body{background:#eee;}
	    	.widget-box{border:1px solid #ddd;background-color:#fff;border-radius:3px;margin:15px 0;}
	    	.widget-box .widget-title{position:relative; border-bottom:1px solid #ddd;background-color:#f9f9f9;line-height:40px;}
	    	.widget-box .widget-title h5{text-indent:46px;margin:0;line-height:40px;font-weight:400}
	    	.widget-box .widget-title .icon{position:absolute;left:0;top:0;width:40px;height:100%;border-right:1px solid #ddd;text-align:center;}
	    	.widget-box .widget-title .groupbtn{position:absolute;top:0;right:10px;}
	    	.widget-box .widget-title .groupbtn label{display:inline-block;padding-left:10px;}
	    	.widget-content .highcharts-container{width:100%!important;}
	    	.widget-content .chart,.widget-content .pie{height: 400px;max-width: 100%;margin-top:20px;}

	    	.g_fixed{position:fixed;top:0;left:0;right:0;height:54px;background:#F7F7F7;padding:0 34px;z-index:100;border-bottom:1px solid #eee;}
	    	.inlineblock,.form-group .select2{display:inline-block;margin:10px 10px 0 0;float:left;line-height:34px;}
	    	.inlineblock.form-control{width:auto;min-width:10em;border-color:#aaa;}
	    	
	    	.select2 .select2-selection{height:34px;}
	    	.select2-container--default .select2-selection--single .select2-selection__rendered{line-height:33px;}
	    	.select2-container--default .select2-selection--single .select2-selection__arrow{height:32px;}
	    </style>
	</head>
	<body>
		<div class="queryWhere" style="height:60px;">
			<div class="g_fixed">
				<div class="form-group clearfix">
					<select class="form-control querytype inlineblock">
						<option value="1">按日统计</option>
						<option value="2">按月统计</option>
					</select>
				</div>
			</div>
		</div>
		<div class="container-fluid">
			<div class="row-fluid" style="margin-top:0;">
				<div class="col-xs-12">
					<div class="widget-box">
						<div class="widget-title">
							<span class="icon">
								<i class="icon-signal"></i>
							</span>
							<h5>患者新增</h5>
						</div>
						<div class="widget-content">
							<div class="chart" id="chart1" style="width:100%;"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="row-fluid" style="margin-top:0;">
				<div class="col-xs-12">
					<div class="widget-box">
						<div class="widget-title">
							<span class="icon">
								<i class="icon-signal"></i>
							</span>
							<h5>医生新增</h5>
						</div>
						<div class="widget-content">
							<div class="chart" id="chart2" style="width:100%;"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
    	<script type="text/javascript" src="//cdn.jsdelivr.net/jquery/1/jquery.min.js"></script>
    	<script src="/sea-modules/highchart/highcharts.sea.js"></script>
    	<script src="/sea-modules/select2/js/select2.min.js"></script>
		<script type="text/javascript" src="//cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script><!-- sea-modules/daterangepicker/ -->		 
		<script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
        <script>
        	var formData = {'queryType':'1'};
        	fillCharts();
        	$(function(){
        		$('.querytype').change(function(){
        			formData['queryType'] = this.value;
        			fillCharts();
        		});
        	});
        	function fillCharts(){
        		$.post("/system/newAddDatas", formData,function (data) {
			        var categories = [],pdata = [],docData = [];
			        var cates = data.categorys;
			       var patientDatas = data.patientDatas;
			        var doctorDatas = data.doctorDatas;
			        $.each(cates, function (i, cate) {
			            categories.push(cate);
			        });
			        $.each(patientDatas, function (i, data) {
			        	console.log(data);
			            pdata.push(data);
			        });
			         $.each(doctorDatas, function (i, data) {
			         console.log(data);
			            docData.push(data);
			        });
			        $('#chart1').highcharts({
		            chart: { type: 'line' },
		            title: {
		                text: ''
		            },
		            xAxis: {
		                categories:categories
		            },
		            yAxis: {
		                title: {
				            text: formData['queryType']=='1'?'患者每日新增量':'患者每月新增量'
				        }
		            },
		             plotOptions: {
				        line: {
				            dataLabels: {
				                enabled: true
				            },
				            enableMouseTracking: false
				        }
				    },
				    series: [{
				        name: '患者',
				        data: pdata
				    }]
		        });    
		        $('#chart2').highcharts({
		            chart: { type: 'line' },
		            title: {
		                text: ''
		            },
		            xAxis: {
		                categories:categories
		            },
		            yAxis: {
		                title: {
				            text: formData['queryType']=='1'?'医生每日新增量':'医生每月新增量'
				        }
		            },
		             plotOptions: {
				        line: {
				            dataLabels: {
				                enabled: true
				            },
				            enableMouseTracking: false
				        }
				    },
				    series: [{
				        name: '医生',
				        data: docData
				    }]
		        });     
			 });
        	}
        	
        </script>
	</body>
</html>
