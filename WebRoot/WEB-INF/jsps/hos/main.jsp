<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
	  <head>
	    <base href="/">
	    
	    <title>首页</title>
	    <meta charset="UTF-8" />
	    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
	    <link rel="stylesheet" href="/css/bootstrap.min.css" />
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
	    	.quickenter a{position:relative; display:inline-block;line-height:90px;width:10em;text-align:center;color:#fff;font-family:Microsoft YaHei,SimHei;font-size:20px;margin-right:10px;border-radius:3px;}
	    	.quickenter a span{position:absolute;right:-6px;top:-6px;color:#fff;background-color:red;width:1.8em;height:1.8em;line-height: 1.7em;font-size:12px;border-radius:50%;}
	    	
	    </style>
	</head>
	<body>
		<div class="container-fluid">
				<div class="row-fluid" style="margin-top:20px">
					<div class="quickenter">
						<a href="javascript:void(0)" data-href="/hospital/departments" style="background-color:#009966">科室设置</a>
						<a href="javascript:void(0)" data-href="/hospital/doctors" style="background-color:#CD3299">医生列表</a>
						<a href="javascript:void(0)" data-href="/hospital/vedioOrderManage" style="background-color:#0066CD">订单查看<span class="icon">${uncompnum }</span></a>
					</div>
				</div>
				<div class="row-fluid" style="margin-top:0;">
					<div class="span12">
						<div class="widget-box">
							<div class="widget-title">
								<span class="icon">
									<i class="icon-signal"></i>
								</span>
								<h5>每周订单总数</h5>
							</div>
							<div class="widget-content">
								<div class="chart" id="chart1" style="width:100%;"></div>
							</div>
						</div>
					</div>
				</div>
				<div class="row-fluid" style="margin-top:0;">
					<div class="span6">
						<div class="widget-box">
							<div class="widget-title">
								<span class="icon">
									<i class="icon-signal"></i>
								</span>
								<h5>科室订单数</h5>
								<span class="groupbtn">
									<label class="radio"><input type="radio" name="depordernums" value="1" checked/><span>周订单数</span></label>
									<label class="radio"><input type="radio" name="depordernums" value="2" /><span>月订单数</span></label>
									<label class="radio"><input type="radio" name="depordernums" value="3" /><span>总订单数</span></label>
								</span>
							</div>
							<div class="widget-content">
								<div class="pie" id="chart2" style="width:100%;"></div>
							</div>
						</div>
					</div>
					<div class="span6">
						<div class="widget-box">
							<div class="widget-title">
								<span class="icon">
									<i class="icon-signal"></i>
								</span>
								<h5>远程门诊订单来源图</h5>
							</div>
							<div class="widget-content">
								<div class="pie" id="chart3" style="width:100%;"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
    	<script src="/js/jquery.min.js"></script>
    	<script src="/js/jquery.ui.custom.js"></script>
    	<script src="/js/bootstrap.min.js"></script>
    	<script src="/sea-modules/highchart/highcharts.sea.js"></script>
        <script>
        	var _href = '/';
         	$(function () {
			    chart1();
			    chart2(1,'周订单数');
			    chart3();
			    $('.groupbtn input').change(function(){
			    	this.checked && chart2(this.value,this.nextSibling.innerText);
			    });
			    $('.quickenter a').click(function(){
			    	var href = $(this).attr('data-href');
			    	window.parent.triggermenu && window.parent.triggermenu(href);
			    });
			});
			function chart1(){
				$.post(_href + "hospital/ordersbyweek", function (data) {
			        var categories = [];
			        var udata = [];
			        var cates = data.categorys;
			        var unums = data.ordernums;
			        $.each(cates, function (i, cate) {
			            categories.push(cate);
			        });
			        $.each(unums, function (i, unum) {
			            udata.push(unum);
			        });
			        getChart('chart1',[{
		                    name: "订单总数",
		                    type: "column",
		                    data: udata
		             	},
		                {
		                    name: "订单数",
		                    type: "spline",
		                    data: udata
		             	}],categories,'周订单数');			        
			    });
			}
			function chart2(stype,title){
				$.post(_href + "hospital/ordersbydep", {stype:stype}, function (data) {
			        var categories = [];
			        var udata = [];
			        var cates = data.categorys;
			        var unums = data.ordernums;
			        $.each(cates, function (i, cate) {
			            categories.push(cate);
			        });
			        $.each(unums, function (i, unum) {
			            udata.push(unum);
			        });
			        getChart('chart2',[{
		                    name: "订单总数",
		                    type: "column",
		                    data: udata
		             }],categories,title);
			    });
			}
			function getChart(id,udata,categories,title){
				$('#' + id).highcharts({
		            chart: {},
		            title: {
		                text: ''
		            },
		            xAxis: {
		                categories: categories
		            },
		            yAxis: {
		                labels: {
		                    format: '{value}',
		                    style: {
		                        color: Highcharts.getOptions().colors[1]
		                    }
		                },
		                title: {
		                    text: title,
		                    style: {
		                        color: Highcharts.getOptions().colors[1]
		                    }
		                }
		            },
		            tooltip: {
		                shared: true
		            },
		            legend: {
		                enabled: false
		            },
		            series: udata
		        });
			}
			function chart3(){
                $.ajax({
	                url: _href + "hospital/gainOrderSources",
	                type: "post",
	                dataType: "json",
	                success: function (data) {
						var showData = [];
	                    if(data.status != 'error'){
	                         $.each(data.areaData, function (key, value) {
	                             showData.push([key, value]);
	                         });
	                    }
	                    fillchart3(showData);
                    }
                });				
			}
			function fillchart3(showData){
				$('#chart3').highcharts({
			        chart: {
			            plotBackgroundColor: null,
			            plotBorderWidth: null,
			            plotShadow: false,
			        },
			        title: {
			            text: ''
			        },
			        tooltip: {
			            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
			        },
			        plotOptions: {
			            pie: {
			                allowPointSelect: true,
			                cursor: 'pointer',
			                dataLabels: {
			                    enabled: true,
			                    color: '#000000',
			                    connectorColor: '#000000',
			                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
			                }
			            }
			        },
			        series: [{
		                type: 'pie',
		                name: '订单来源分布',
		                data: showData
		             }]
			    });
			}
        </script>
	</body>
</html>
