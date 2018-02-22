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
	      	<div class="addarea">
				<a class="btn btn-content js-addinfo editbtn" href="/system/serviceinfoAdd">新增</a>
			</div>
	        <div class="widget-box">
	          <div class="widget-content nopadding">
	            <table class="table table-bordered data-table">
	              <thead>
	                <tr>
	                  <th>ID</th>
	                  <th>服务名称</th>
	                  <th>服务类型</th>
	                  <th>服务状态</th>
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
                    "sAjaxSource": "/system/servicedatas",
                    "ajax": {
			            "dataSrc": "aaData"
			        },
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,  
                    "columns": [
			            { "data": "id" },
			            { "data": "serviceName" },
			            { "data": "multiplePackage" },
			            { "data": "status" },
			            { "data": null }
			        ],
			       "columnDefs": [ 
			    	   {
			                "targets": 2,
			                "render": function (data, type, row, meta) {
			                    if(data==1){
			                    	return "多种";
			                    }else{
			                    	return "单种";
			                    }
			                }
			            },
			    	   {
			                "targets": 3,
			                "render": function (data, type, row, meta) {
			                	var obj = ['未启用', '启用'];
			                    return obj[data] || '其它';
			                }
			            },
				        {
				            "targets": -1,
				            	"render": function (data, type, row, meta) {
				                    return '<a href="/system/serviceinfoupdate?serviceid='+ data.id +'&MultiplePackage='+data.multiplePackage+'" class="editbtn linebtn">编辑</a>   <a href="/system/servicedatasInfo?id='+ data.id +'" class="membermanger linebtn">查看详情</a>';
				                }
			            }
			        ], 
                    "language": {
			            "url": "/Chinese.txt"
			        },
            	});			    
            	
            });
           
            function retrieveData( sSource, aoData, fnCallback ) {
			    aoData.push( { "name": "searchContent", "value":$('input[type="search"]').val()} );
			    aoData.push( { "name": "id", "value": '${id}' } );
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

