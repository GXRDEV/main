<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>账单列表</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/css/matrix-style2.css" />
	<link rel="stylesheet" href="/css/matrix-media.css" />
	<link href="/font-awesome/css/font-awesome.css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="/libs/daterang/daterangepicker.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/videolist.css" />
	<link rel="stylesheet" href="/css/view/videolistwidthbtn.css" />
	<style>
		.condations input,
		.condations select{ width: 100%;}
	</style>	
</head>
<body>
	<div class="mainlist">
	  <div class="container-fluid" style="padding-top:20px;">
	  	<div class="condations">
		  	<div class="row-fluid">
		  		<div class="span2">
		  			<label>医生名称：</label>
		  			<input type="text" name="docname" />
		  		</div>
		  		<div class="span3">
		  			<label>时间范围：</label>
		  			<input id="reportrange" class="form-control inlineblock" type="text" name="daterange" value="" />
		  		</div>
		  		<div class="span3">
		  			<label>业务类型：</label>
		  			<select name="busitype">
		  				<option>==选择业务类型==</option>
		  				<option value="4">远程账单</option>
		  				<option value="1">图文咨询账单</option>
		  				<option value="2">电话咨询账单</option>
		  				<option value="5">专家咨询账单</option>
		  				<option value="-1">提现账单</option>
		  			</select>
		  		</div>
		  		<div class="span2">
		  			<label>操作类型：</label>
		  			<select name="actions">
		  				<option>==选择操作类型==</option>
		  				<option value="1">收入</option>
		  				<option value="2">支出</option>
		  			</select>
		  		</div>
		  		<div class="span2">
		  			<label>&ensp;</label>
		  			<button onclick="setQuestion()" class="btn btn-primary">查询</button>
		  		</div>
		  	</div>
	  	</div>
	    <div class="row-fluid">
	      <div class="span12">
	        <div class="widget-box">
	          <div class="widget-content nopadding">
	            <table class="table table-bordered data-table">
	              <thead>
	                <tr>
	                  <th>ID</th>
	                  <th>来源</th>
	                  <th>操作类型</th>
	                  <th>实际收入</th>
	                  <th>原始收入</th>
	                  <th>扣税金额</th>
	                  <th>平台补贴</th>
					  <th>当前余额</th>
					  <th>收支前余额</th>
					  <th>业务类型</th>
					  <th>操作</th>
	                </tr>
	              </thead> 
	            </table>
	          </div>
	        </div>
	      </div>
	    </div>
	  </div>
	</div>
	<script src="/js/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/js/dataTables/js/jquery.dataTables.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="/libs/daterang/moment.min.js"></script><!-- sea-modules/daterangepicker/ -->
	<script type="text/javascript" src="/libs/daterang/daterangepicker.js"></script>
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
	<script type="text/javascript">
		var _burl='/',_h = _burl,_id = '';
	</script>
	<script>
        	var oTable;
        	var contadion = [], formData = {};
            $(document).ready(function () {            	
            	oTable = $('.data-table').DataTable({
                    "bJQueryUI": true,"sDom": '<".example_processing"r><".tablelist"t><"F"flp>',
                    "iDisplayStart":0,"iDisplayLength":10,
                    "lengthMenu": [ 10, 15, 20, 25, 50, 100],
                    "bDestroy":true,"bRetrieve":true,"bStateSave":true,
                    "bServerSide": true,"fnServerData": retrieveData,
                    "sAjaxSource": "/doctor/gaindocbilldatas",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,   
					"fnCreatedRow": function (nRow, aData, iDataIndex) {						
                    	//$('td:eq(9)', nRow).html('<a href="'+ _burl +'system/docwithdrawdetail/'+ aData[0] +'" class="editbtn linebtn">查看详情</a>');
                    },
                    "oLanguage": {
                        "sProcessing": "正在获取数据，请稍后...",
                        "sLengthMenu": "每页显示 _MENU_ 条",
                        "sZeroRecords": "没有您要搜索的内容",
                        "sInfoEmpty": "记录数为0",
                        "sInfoFiltered": "(全部记录数 _MAX_ 条)",
                        "sSearch": "搜索 ",
                        "oPaginate": {
                            "sFirst": "第一页",
                            "sPrevious": "上一页",
                            "sNext": "下一页",
                            "sLast": "最后一页"
                        }
                    },
                    "initComplete": function(settings, json) {
                    }
            	});  
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
				    });
				
				    //$('#reportrange').on('cancel.daterangepicker', function(ev, picker) {});
				    
				    cb(start, end);
			    })();
            	//oTable.page('first').draw(false);
            });
            function retrieveData( sSource, aoData, fnCallback ) {
			    aoData.push( { "name": "searchContent", "value":$('input[type="search"]').val()} );
			    $.ajax( {  
			        "type": "post",   
			        "contentType": "application/json",  
			        "url": sSource,   
			        "dataType": "json",  
			        "data": JSON.stringify(aoData.concat(contadion)),
			        "success": function(resp) {  
			            fnCallback(resp);
			        }  
			    });
			}
			function setQuestion(){
				contadion = [
					{ "name": "docname", "value":$('input[name="docname"]').val()},
					{ "name": "busitype", "value":$('#busitype').val()},
					{ "name": "actions", "value":$('#actions').val()},
					{ "name": "startDate", "value": formData['startDate'] || '' },
					{ "name": "endDate", "value": formData['endDate'] || '' }
				];
				oTable.page('first').draw(false);
			}
	</script>
</body>
</html>

