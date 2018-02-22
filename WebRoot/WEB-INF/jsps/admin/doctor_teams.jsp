<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>医生团队管理</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/css/matrix-style2.css" />
	<link rel="stylesheet" href="/css/matrix-media.css" />
	<link href="/font-awesome/css/font-awesome.css" rel="stylesheet" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/videolist.css" />
	<link rel="stylesheet" href="/css/view/videolistwidthbtn.css" />
</head>
<body>
	<div class="mainlist">
	  <div class="container-fluid">
	    <div class="row-fluid">
	      <div class="span12">
	        <div class="addarea"></div>
	        <div class="widget-box">
	          <div class="widget-content nopadding">
	            <table class="table table-bordered data-table">
	              <thead>
	                <tr>
	                  <th>ID</th>
	                  <th>团队名称</th>
	                  <th>团队负责人</th>
	                  <th>负责人医院</th>
	                  <th>负责人科室</th>
	                  <th>关键字</th>
	                  <th>是否推优</th>
	                  <th>人数</th>
	                  <th>通过时间</th>
	                  <th>是否测试</th>
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
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
	<script type="text/javascript">
		var _burl='/',_h = _burl,_id = '';
	</script>
	<script>
        	var oTable;
            $(document).ready(function () {            	
            	oTable = $('.data-table').DataTable({
                    "bJQueryUI": true,"sDom": '<".example_processing"r><".tablelist"t><"F"flp>',
                    "iDisplayStart":0,"iDisplayLength":10,
                    "lengthMenu": [ 10, 15, 20, 25, 50, 100],
                    "bDestroy":true,"bRetrieve":true,"bStateSave":true,
                    "bServerSide": true,"fnServerData": retrieveData,
                    "sAjaxSource": "/system/docteamdatas",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,   
					"fnCreatedRow": function (nRow, aData, iDataIndex) {			
						var opts='';
						if(aData[9]=='否') {
							opts+='<a href="javascript:void(0);" class="testFlag linebtn" tid='+aData[0]+' >标记测试</a>';
						}
						opts+='<a href="/system/doctorTeamMembers/'+aData[0]+'" class="linebtn" >成员信息</a>';
						if(aData[6]=='否'){
							opts+='<a href="javascript:void(0);" class="changedtopstatus linebtn" tid='+aData[0]+' sval="1">推优</a>';
						}else if(aData[6]=='是'){
							opts+='<a href="javascript:void(0);" class="changedtopstatus linebtn" tid='+aData[0]+' sval="0">取消推优</a>';
						}
                    	$('td:eq(10)', nRow).html(opts);
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
            	oTable.page('first').draw(false);
            	$('body').delegate('.changedtopstatus','click',function(){
                	var tid=$(this).attr("tid"),sval=$(this).attr("sval");
                	if(confirm("请确认该医生团队推优状态发生了改变？！")){	
                		$.post('/system/changedtopstatus',{tid:tid,sval:sval},function(data){
                    		oTable.page('first').draw(false);
                    	});
                	}
                }).delegate('.testFlag','click',function(){
                	var tid=$(this).attr("tid");
                	if(confirm("标记测试后将不会显示！")){
                		$.post('/system/teamTestFlag',{teamId:tid},function(data){
                    		oTable.page('first').draw(false);
                    	});
                	}
                })
            });
            function retrieveData( sSource, aoData, fnCallback ) {
			    aoData.push( { "name": "search", "value":$('input[type="search"]').val()} );
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

