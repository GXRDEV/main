<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>医生会诊列表</title>
		<meta charset="UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<link rel="stylesheet" href="/css/bootstrap.min.css" />
		<link rel="stylesheet" href="/css/matrix-style2.css" />
		<link rel="stylesheet" href="/css/matrix-media.css" />
		<link href="/font-awesome/css/font-awesome.css" rel="stylesheet" />
		<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
		<link rel="stylesheet" href="/css/view/videolist.css" />
		<style>       
			
		</style>		
	</head>
	<body>
		<div id="content">
		  <div class="container-fluid">
		    <div class="row-fluid">
		      <div class="span12">
		        <div class="widget-box">
		          <div class="widget-content nopadding">
		            <table class="table table-bordered data-table">
		              <thead>
		                <tr>
		                  <th>ID</th>
		                  <th>状态</th>  
		                  <th>预约时间</th>
		                  <th>距离当前</th>
		                  <th>预约医院</th>
		                  <th>预约科室</th>
		                  <th>患者姓名</th>
						  <th>年龄</th>
						  <th>性别</th>
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
		<script src="/js/matrix.js"></script>
		<script type="text/javascript">
			var _burl='/';
		</script>
		<script>
		     var oTable;
			$(document).ready(function () {
			    oTable = $('.data-table').DataTable({
			        "bJQueryUI": true,
			        "sDom": '<".example_processing"r><".tablelist"t><"F"flp>',
			        "iDisplayStart": 0,
			        "iDisplayLength": 10,
			        "lengthMenu": [10, 15, 20, 25, 50, 100],
			        "bDestroy": true,
			        "bRetrieve": true,
			        "bStateSave": true,
			        "bServerSide": true,
			        "fnServerData": retrieveData,
			        "sAjaxSource": _burl + "nuradmin/gainhistorys",
			        "bFilter": true,
			        "bLengthChange": true,
			        "bProcessing": true,
			        "aoColumnDefs": [{
			                "bSortable": false,
			                "aTargets": [0]
			        }],
			        "aaSorting": [[1, "asc"],[2, "desc"]],
			        "columns": [{"visible": false},null, null, null, null, null, null, null, null, null],
			        "fnCreatedRow": function (nRow, aData, iDataIndex) {
			            var path = 'javascript:void(0)',_val = aData[1];
			            $('td:eq(8)', nRow).html('<a href="' + path + '" class="editTool">查看详情</a>');
			            //console.log('aData', JSON.stringify(aData, null, '\t'));
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
			        }
			    });
			});
			 
			function retrieveData(sSource, aoData, fnCallback) {
			    aoData.push({
			        "name": "searchContent",
			        "value": $('input[type="search"]').val()
			    });
			    $.ajax({
			        "type": "post",
			        "contentType": "application/json",
			        "url": sSource,
			        "dataType": "json",
			        "data": JSON.stringify(aoData),
			        "success": function (resp) {
			            fnCallback(resp);
			        }
			    });
			}
		</script>
	</body>
</html>

