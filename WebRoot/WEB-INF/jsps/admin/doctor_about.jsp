<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>医生的关注量/统计量</title>
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
	                  <th>医生id</th>
	                  <th>医生姓名</th>
	                  <th>医生医院</th>
	                  <th>所在科室</th>
	                  <th>今日报到/关注</th>
	                  <th>总报到/关注</th>
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
                    "sAjaxSource": "/system/docreportaboutdatass",
                    "ajax": {
			            "dataSrc": "aaData"
			        },
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,  
                    "columns": [
                    	{ "data": "id" },
			            { "data": "docName" },
			            { "data": "hosName" },
			            { "data": "depName" },
			            { "data": "todayCounts"},
			            { "data": "counts" }
			        ],
                    "language": {
			            "url": "/Chinese.txt"
			        },
			         "initComplete": function(settings, json) {
                    	var tabs='<a href="#" class="stalist curr" order-status="1">患者报道量</a>';
                    	tabs+='<a href="#" class="stalist" order-status="2">患者关注量</a>';
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
			    aoData.push( { "name": "ostatus", "value":$('#filteropt a.curr').attr('order-status')||'1'} );
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

