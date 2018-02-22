<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>群组成员管理</title>
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
	                  <th>成员</th>
	                  <th>成员类型</th>
	                  <th>医院</th>
	                  <th>科室</th>
					  <th>角色</th>
					  <th>创建时间</th>
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
                    "sAjaxSource": "/system/gainGroupMemberDatas",
                    "ajax": {
			            "dataSrc": "aaData"
			        },
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,  
                    "columns": [
			            { "data": "memberId" },
			            { "data": "memberType" },
			            { "data": "memberName" },
			            { "data": "hosName" },
			            { "data": "depName" },
			            { "data": "role" },
			            { "data": "createTime" },
			            { "data": null }
			        ],
			        "columnDefs": [ 
			        	{
			                "targets": 1,
			                "render": function (data, type, row, meta) {
			                	var obj = ['', '患者', '专家', '医生', '', '医生管理员', '企业管理员'];
			                    return obj[data] || '其它';
			                }
			            },
			        	{
			                "targets": -3,
			                "render": function (data, type, row, meta) {
			                	var obj = ['', '管理员', '普通成员'];
			                    return obj[data] || '其它';
			                }
			            },
				        {
				            "targets": -1,
			                "defaultContent": '<button class="groupdel">踢出群组</button>'
			            }
			        ],
                    "language": {
			            "url": "/Chinese.txt"
			        }
            	});  			    
			    $('.data-table tbody').on( 'click', '.groupdel', function () {
			        var data = oTable.row( $(this).parents('tr') ).data();
			        if(data['memberId'] && confirm('确认要将TA踢出当前群组？')){
			        	$.post('/system/removeDocTeamGroupMember', { memberId: data['memberId'] }).done(function(d){
				        	oTable.page('first').draw(false);
				        })
			        }
			    });
            	//oTable.page('first').draw(false);
            });
            function retrieveData( sSource, aoData, fnCallback ) {
			    aoData.push( { "name": "searchContent", "value":$('input[type="search"]').val()} );
			    aoData.push( { "name": "groupId", "value": '${groupId}' } );
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

