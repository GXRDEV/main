<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>医联体管理</title>
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
	      	 <div class="addarea">
				<a class="btn btn-content js-addinfo editbtn" href="/system/addhoshealth">新增</a>
			</div>
	        <div class="widget-box">
	          <div class="widget-content nopadding">
	            <table class="table table-bordered data-table">
	              <thead>
	                <tr>
	                  <th>ID</th>
	                  <th>医联体名称</th>
	                  <th>区域</th>
	                  <th>核心医院</th>
	                  <th>核心医院级别</th>
	                  <th>入驻医院数</th>
	                  <th>审核时间</th>
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
                    "bServerSide": true,"fnServerData": retrieveData,
                    "sAjaxSource": "/system/hoshealthdatas",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,   
					"fnCreatedRow": function (nRow, aData, iDataIndex) {			
						var opts='';
						if($('#filteropt a.curr').attr('order-status')=='1'||$('#filteropt a.curr').attr('order-status')==undefined){
							opts+='<a href="javascript:void(0);" class="statusup linebtn" sval="-1" hid='+aData[0]+'>下线</a>';
						}else{
							opts+='<a href="javascript:void(0);" class="statusup linebtn" sval="1" hid='+aData[0]+'>上线</a>';
						}
						opts+='<a href="/system/addhoshealth?yltId='+aData[0]+'" class="linebtn">查看详情</a>';
						opts+='<a href="/system/hoshealthdetail/'+aData[0]+'" class="linebtn">医院成员</a>'
                    	$('td:eq(7)', nRow).html(opts);
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
                    	var tabs='<a href="#" class="stalist curr" order-status="1">已上线</a>';
                    	tabs+='<a href="#" class="stalist" order-status="-1">已下线</a>';
                    	$('#filteropt').html(tabs);
                    }
            	});  
            	oTable.page('first').draw(false);
            	$('body').delegate('#filteropt a','click',function(){
                	var opt = $(this).attr('data-currorhis');
                	$(this).addClass('curr').siblings().removeClass('curr');
                	oTable.page('first').draw(false);
                	return false;
                }).delegate('.statusup','click',function(){
                	var hid=$(this).attr("hid"),sval=$(this).attr("sval");
                	if(confirm("您正在变更医联体的上下线状态，请确认是否继续操作！")){
	                	$.post('/system/changeHosHealthStatus',{hid:hid,sval:sval},function(data){
	                		oTable.page('first').draw(false);
	                	});
                	}
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

