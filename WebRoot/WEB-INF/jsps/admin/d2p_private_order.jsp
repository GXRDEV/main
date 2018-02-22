<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>对医生的签约</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/css/matrix-style2.css" />
	<link rel="stylesheet" href="/css/matrix-media.css" />
	<link href="/font-awesome/css/font-awesome.css" rel="stylesheet" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/videolist.css" />
	<link rel="stylesheet" href="/css/view/videolistwidthbtn.css" />
	<style>
		.groupdel{ border: 0; background: transparent; color:blue;}
		.aa{ color: blue; }
	</style>
</head>
<body>
	<div class="mainlist">
	  <div class="container-fluid" style="padding-top: 26px;">
	    <div class="row-fluid">
	      <div class="span12">
	        <div class="widget-box">
	          <div class="widget-content nopadding">
	            <table class="table table-bordered data-table">
	              <thead>
	                <tr>
	                  <th>ID</th>
	                  <th>UUID</th>
	                  <th>签约医生</th>
	                  <th>患者</th>
	                  <th>签约类型</th>
	                  <th>创建时间</th>
					  <th>开始时间</th>
					  <th>结束时间</th>
					  <th>状态</th>
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
	<script type="text/javascript">
		var _burl='/',_h = _burl;
	</script>
	<script>
        	var oTable;
            $(document).ready(function () {            	
            	oTable = $('.data-table').DataTable({
                    "bJQueryUI": true,"sDom": '<".example_processing"r><".tablelist"t><"F"<"#filteropt">flp>',
                    "iDisplayStart":0,"iDisplayLength":10,
                    "lengthMenu": [ 10, 15, 20, 25, 50, 100],
                    "bDestroy":true,"bRetrieve":true,"bStateSave":true,
                    "bServerSide": true,"fnServerData": retrieveData,
                    "sAjaxSource": "/system/docprivatedatas",
                    "ajax": {
			            "dataSrc": "aaData"
			        },
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,  
                    "columns": [
			            { "data": "id" },
			            { "data": "uuid" },
			            { "data": "docName" },
			            { "data": "userName" },
			            { "data": "packageName" },
			            { "data": "createTimes" },
			            { "data": "startDate" },
			            { "data": "endDate" },
			            { "data": "status" },
			            { "data": null }
			        ],
			       "columnDefs": [ 
			       		{
			                "targets": -2,
			                "render": function (data, type, row, meta) {
			                    if(data =='10'){
			                    	return "未生效";
			                    }else if(data =='20'){
			                    	return "生效";
			                    }else if(data =='30'){
			                    	return "已退诊";
			                    }else if(data =='40'){
			                    	return "已完成";
			                    }else if(data =='50'){
			                    	return "已解约";
			                    }
			                }
			            },
				        {
				            "targets": -1,
				            	"render": function (data, type, row, meta) {
				                    return '<a href="/system/privateorders?id='+ data.id +'" class="membermanger">查看详情</a>';
				                }
			            }
			        ], 
                    "language": {
			            "url": "/Chinese.txt"
			        },
			         "initComplete": function(settings, json) {
                    	var tabs='<a href="#" class="stalist curr" order-status="20">已生效</a>';
                    	tabs+='<a href="#" class="stalist" order-status="30">已退诊</a>';
                    	tabs+='<a href="#" class="stalist" order-status="40">已完成</a>';
                    	tabs+='<a href="#" class="stalist" order-status="50">已解约</a>';
                    	tabs+='<a href="#" class="stalist" order-status="10">未生效</a>';
                    	$('#filteropt').html(tabs);
                    }
            	});		
            	$('body').delegate('#filteropt a','click',function(){
                	var opt = $(this).attr('data-currorhis');
                	$(this).addClass('curr').siblings().removeClass('curr');
                	oTable.page('first').draw(false);
                	return false;
                });	    
			      
            });
            function retrieveData( sSource, aoData, fnCallback ) {
			    aoData.push( { "name": "searchContent", "value":$('input[type="search"]').val()} );
			    aoData.push( { "name": "ostatus", "value":$('#filteropt a.curr').attr('order-status')||'20'} );
			    $.ajax( {  
			        "type": "post",   
			        "contentType": "application/json",  
			        "url": sSource,   
			        "dataType": "json",  
			        "data": JSON.stringify(aoData),
			        "success": function(resp) {  
			            fnCallback(resp);
			        }  
			    });  
			} 
	</script>
</body>
</html>

