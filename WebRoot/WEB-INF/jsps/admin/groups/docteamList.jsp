<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>医生团队群组管理</title>
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
		.groupdel{ border: 0; background: transparent; color:blue; margin-left: 1em;}
		.membermanger{ color: blue; }
		.clearfix select{ float: left; margin: 0 10px 10px 0;}
	</style>
</head>
<body>
	<div class="mainlist">
	  <div class="container-fluid">
	    <div class="row-fluid">
	      <div class="span12">
	        <!-- <div class="addarea">
	        	<a href="#myModal" role="button" class="btn btn-content js-addinfo editbtn" data-toggle="modal">创建</a>
			</div> -->
	        <div class="widget-box">
	          <div class="widget-content nopadding">
	            <table class="table table-bordered data-table">
	              <thead>
	                <tr>
	                  <th>ID</th>
	                  <th>群组UUID</th>
	                  <th>群组名称</th>
	                  <th>创建者</th>
	                  <th>创建者类型</th>
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
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
	<script type="text/javascript">
		var _burl='/',_h = _burl,_id = '';
	</script>
	<script>
        	var oTable;
            $(document).ready(function () {            	
            	oTable = $('.data-table').DataTable({
                    "bJQueryUI": true,"sDom": '<".example_processing"r><".tablelist"t><"F"<"#filteropt">flp>',
                    "iDisplayStart":0,"iDisplayLength":10,
                    "lengthMenu": [ 10, 15, 20, 25, 50, 100],
                    "bDestroy":true,"bRetrieve":true,"bStateSave":true,
                    "bServerSide": true,
                    "fnServerData": retrieveData,
                    "sAjaxSource": "/system/gainGroupDatas",
                    "ajax": {
			            "dataSrc": "aaData"
			        },
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,  
                    "columns": [
			            { "data": "groupId" },
			            { "data": "groupUuid" },
			            { "data": "groupName" },
			            { "data": "creatorName" },
			            { "data": "creatorType" },
			            { "data": "createTime" },
			            { "data": null }
			        ],
			        "columnDefs": [ 
			        	{
			                "targets": 4,
			                "render": function (data, type, row, meta) {
			                	var obj = ['', '患者', '专家', '医生', '', '医生管理员', '企业管理员'];
			                    return obj[data] || '其它';
			                }
			            },
			            {
				            "targets": -1,
			                "render": function (data, type, row, meta) {
			                	var str = data.status == 1 ? '<a href="/system/docteamgroupmembers/'+ data.groupId +'" class="membermanger">成员管理</a>' : ''
			                    return str + '<button class="groupdel">'+ (data.status == 1 ? '解散' : '已解散') +'</button>';
			                }				        } 
			        ],
                    "language": {
			            "url": "/Chinese.txt"
			        },
			         "initComplete": function(settings, json) {
	                    	var tabs='<a href="#" class="stalist curr" order-status="1">生效的群组</a>';
	                    	tabs+='<a href="#" class="stalist" order-status="2">解散的群组</a>';
	                    	$('#filteropt').html(tabs);
	                    }
            	});  
            	
            	$('.data-table tbody').on( 'click', '.groupdel', function () {
			        var data = oTable.row( $(this).parents('tr') ).data();
			        if(data['groupId'] && confirm('确认要'+ (data.status == 1 ? '解散' : '恢复') +'当前群组？')){
			        	$.post('/system/disSolveDocTeamGroup', { groupId: data['groupId'], otype: data['status'] == '1' ? '1' : '2' }).done(function(d){
				        	oTable.page('first').draw(false);
				        })
			        }
			    });
            	$('body').delegate('.stalist','click',function(){
                	var opt = $(this).attr('order-status');
                	$(this).addClass('curr').siblings().removeClass('curr');
                	oTable.page('first').draw(false);
                	return false;
                });	 
            	// oTable.page('first').draw(false);
            	
            	// gainCitys(1);
            	
            	$.get('/kangxin/gainStandDeps').done(function(d){
            		$('[name="depId"]').html(function(){
            			var options = [];
            			$.each(d.sdeps || [], function(i, item){
            				options.push('<option value="'+ item.id +'">'+ item.displayName +'</option>');
            			});
            			return options.join('');
            		});
            	});
            });
            function retrieveData( sSource, aoData, fnCallback ) {
			    aoData.push( { "name": "search", "value":$('input[type="search"]').val()} );
			    aoData.push( { "name": "groupType", "value": 2 } );
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

