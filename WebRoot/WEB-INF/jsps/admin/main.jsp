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
					<label class="form-label inlineblock">辅助条件：</label>
					<input id="reportrange" class="form-control inlineblock" type="text" name="daterange" value="" />
					<select class="form-control hosptials inlineblock">
						<option value="">==所有医院==</option>
						<c:forEach items="${hospitals }" var="hos">
							<option value="${hos.id}">${hos.displayName}</option>
						</c:forEach>
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
							<h5>会诊订单统计(<span id="ocount">${orcount}</span>)</h5>
						</div>
						<div class="widget-content">
							<div class="chart" id="chart1" style="width:100%;"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="row-fluid" style="margin-top:0;" id="hoscal">
				<div class="col-xs-12">
					<div class="widget-box">
						<div class="widget-title">
							<span class="icon">
								<i class="icon-signal"></i>
							</span>
							<h5>入驻医院统计(${hoscount})</h5>
						</div>
						<div class="widget-content">
							<div class="chart" id="chart2" style="width:100%;"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="row-fluid" style="margin-top:0;" id="excal">
				<div class="col-xs-12">
					<div class="widget-box">
						<div class="widget-title">
							<span class="icon">
								<i class="icon-signal"></i>
							</span>
							<h5>入驻专家统计(${excount})</h5>
						</div>
						<div class="widget-content">
							<div class="chart" id="chart3" style="width:100%;"></div>
						</div>
					</div>
				</div>
			</div>
			<!-- 饼图 -->
			<div class="row-fluid" style="margin-top:0;">
				<div class="col-xs-6"  id="hosdis">
					<div class="widget-box">
						<div class="widget-title">
							<span class="icon">
								<i class="icon-signal"></i>
							</span>
							<h5>会诊订单医院分布(<span id="ahcount"></span>)</h5>
						</div>
						<div class="widget-content">
							<div class="pie" id="chart4" style="width:100%;"></div>
						</div>
					</div>
				</div>
				<div class="col-xs-6">
					<div class="widget-box">
						<div class="widget-title">
							<span class="icon">
								<i class="icon-signal"></i>
							</span>
							<h5>会诊订单专家分布(<span id="aecount"></span>)</h5>
						</div>
						<div class="widget-content">
							<div class="pie" id="chart5" style="width:100%;"></div>
						</div>
					</div>
				</div>
			</div>
			<!-- 饼图 -->
			<div class="row-fluid" style="margin-top:0;">
				<div class="col-xs-6">
					<div class="widget-box">
						<div class="widget-title">
							<span class="icon">
								<i class="icon-signal"></i>
							</span>
							<h5>会诊订单科室分布(<span id="adcount"></span>)</h5>
						</div>
						<div class="widget-content">
							<div class="pie" id="chart6" style="width:100%;"></div>
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
        	var _href = '/',formData = {};
         	$(function () {
			    (function(){
			    	/*************下拉select**************/
			    	$('.hosptials').select2().on('change',function(){
			    		formData['hosid'] = this.value;
			    		if(this.value!=''){
			    			$("#hoscal").hide();	
			    			$("#excal").hide();	
			    			$("#hosdis").hide();	
			    			
			    		}else{
			    			$("#hoscal").show();
			    			$("#excal").show();
			    			$("#hosdis").show();
			    		}
			    		fillCharts();
			    	});
			    })();
			    (function(){
			    	/*************日历控件**************/
			    	var start = moment().subtract(12, 'month');
				    var end = moment();				
				   	function cb(start, end) {
				    	if(!start._isValid || !end._isValid){
				    		$('#reportrange').val('==全部时段==');
				    	}else{
				        	$('#reportrange').val(start.format('YYYY-MM-DD') + ' 至 ' + end.format('YYYY-MM-DD'));
				        }
				    }
				    formData['startDate'] = start.format('YYYY-MM-DD');
					formData['endDate'] = end.format('YYYY-MM-DD');
				    $('#reportrange').daterangepicker({
						locale: {
					      	format: 'YYYY-MM-DD',
                        	separator : ' 至 ',
                            applyLabel : '确定',  
                            cancelLabel : '取消',  
                            fromLabel : '起始时间',  
                            toLabel : '结束时间',  
                            customRangeLabel : '自定义',  
                            daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],  
                            monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],  
                            firstDay : 1  
                        },
				        startDate: start,
				        endDate: end,
				        ranges: {
				           '全部': ['',''],
				           '最近7天': [moment().subtract(6, 'days'), moment()],
				           '最近30天': [moment().subtract(29, 'days'), moment()],
				           '最近1年': [moment().subtract(12, 'month'), moment()],
				           '当月': [moment().startOf('month'), moment().endOf('month')],
				           '上月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
				        }
				    }, cb);
					$('#reportrange').on('apply.daterangepicker', function(ev, picker) {						
						formData['startDate'] = picker.startDate._isValid ? picker.startDate.format('YYYY-MM-DD') : '';
						formData['endDate'] = picker.endDate._isValid ? picker.endDate.format('YYYY-MM-DD') : '';
						cb(picker.startDate,picker.endDate);
				        fillCharts();
				    });
				
				    //$('#reportrange').on('cancel.daterangepicker', function(ev, picker) {});
				    
				    cb(start, end);
			    })();
			    
			    fillCharts();
			});
			function fillCharts(){
		    	console.log('formData',formData);
			    chart1(formData);
			    chart2(formData);
			    chart3(formData);
			    chart4(formData);
			    chart5(formData);
			    chart6(formData);
		    }
         	function chart1(data){
				$.post(_href + "system/ordercal",data, function (data) {
					$('#ocount').text(data.orcount);
			        var categories = [];
			        var udata = [],udata_t=[];
			        var cates = data.categorys;
			        var unums = data.ordernums;
			       // var unums_t = data.mtotalnums;
			        $.each(cates, function (i, cate) {
			            categories.push(cate);
			        });
			        $.each(unums, function (i, unum) {
			            udata.push(unum);
			        });
			        //$.each(unums_t, function (i, unum) {
			        	//udata_t.push(unum);
			        //});
			        fillColumnChart('chart1',[{
		                    name: "会诊订单总数",
		                    type: "column",
		                    data: udata
		             	},
		                {
		                    name: "新增会诊订单数",
		                    type: "spline",
		                    data: udata
		             	}],categories,'月增会诊订单数');			        
			    });
			}
			function chart2(data){
				$.post(_href + "system/newhoscal",data, function (data) {
			        var categories = [];
			        var udata = [],udata_t=[];
			        var cates = data.categorys;
			        var unums = data.ordernums;
			       // var unums_t = data.mtotalnums;
			        $.each(cates, function (i, cate) {
			            categories.push(cate);
			        });
			        $.each(unums, function (i, unum) {
			            udata.push(unum);
			        });
			       // $.each(unums_t, function (i, unum) {
			        	//udata_t.push(unum);
			        //});
			        fillColumnChart('chart2',[{
		                    name: "入驻医院总数",
		                    type: "column",
		                    data: udata
		             	},
		                {
		                    name: "新入驻医院数",
		                    type: "spline",
		                    data: udata
		             	}],categories,'月增合作医院数');			        
			    });
			}
			function chart3(data){
				$.post(_href + "system/newexpertcal",data, function (data) {
			        var categories = [];
			        var udata = [],udata_t=[];
			        var cates = data.categorys;
			        var unums = data.ordernums;
			       // var unums_t = data.mtotalnums;
			        $.each(cates, function (i, cate) {
			            categories.push(cate);
			        });
			        $.each(unums, function (i, unum) {
			            udata.push(unum);
			        });
			        //$.each(unums_t, function (i, unum) {
			        	//udata_t.push(unum);
			        //});
			        fillColumnChart('chart3',[
		                {
		                    name: "新入驻专家总数",
		                    type: "column",
		                    data: udata
		             	},{
		                    name: "新入驻专家数",
		                    type: "spline",
		                    data: udata
		             	}],categories,'月增专家数');			        
			    });
			}
			
			function chart4(data){
                $.ajax({
	                url: _href + "system/orderhoscal",
	                type: "post",
	                dataType: "json",
	                data:data,
	                success: function (data) {
	                	$('#ahcount').text(data.tcount);
						var showData = [];
	                    if(data.status != 'error'){
	                         $.each(data.areaData, function (key, value) {
	                             showData.push([key, value]);
	                         });
	                    }
	                    fillPieChart("chart4","会诊订单医院分布",showData);
                    }
                });				
			}
			function chart5(data){
                $.ajax({
	                url: _href + "system/orderexcal",
	                type: "post",
	                dataType: "json",
	                data:data,
	                success: function (data) {
	                	$('#aecount').text(data.tcount);
						var showData = [];
	                    if(data.status != 'error'){
	                         $.each(data.areaData, function (key, value) {
	                             showData.push([key, value]);
	                         });
	                    }
	                    fillPieChart("chart5","会诊订单专家分布",showData);
                    }
                });				
			}
			function chart6(data){
                $.ajax({
	                url: _href + "system/orderdepcal",
	                type: "post",
	                dataType: "json",
	                data:data,
	                success: function (data) {
	                	$('#adcount').text(data.tcount);
						var showData = [];
	                    if(data.status != 'error'){
	                         $.each(data.areaData, function (key, value) {
	                             showData.push([key, value]);
	                         });
	                    }
	                    fillPieChart("chart6","会诊订单科室分布",showData);
                    }
                });				
			}
			function fillColumnChart(id,udata,categories,title){
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
			function fillPieChart(id,title,showData){
				$('#'+id).highcharts({
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
		                name: title,
		                data: showData
		             }]
			    });
			}
        </script>
	</body>
</html>
