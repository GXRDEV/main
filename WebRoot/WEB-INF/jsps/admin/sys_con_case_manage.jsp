<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>会诊案例管理</title>
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
				<a class="btn btn-content js-addinfo editbtn" href="/system/sysConCaseOp">新增</a>
			</div>
	        <div class="widget-box">
	          <div class="widget-content nopadding">
	            <table class="table table-bordered data-table">
	              <thead>
	                <tr>
	                  <th>ID</th>
	                  <th>标题</th>
	                  <th>创建时间</th>
	                  <th>会诊类型</th>
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
                    "sAjaxSource": "/system/sysconcasedatas",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,
					"fnCreatedRow": function (nRow, aData, iDataIndex) {			
						var opts='';
						opts+='<a href="/system/sysConCaseOp?sscId='+aData[0]+'" class="linebtn">编辑</a>';
						if($('#filteropt a.curr').attr('order-status')=='0'){
							opts+='<a href="javascript:void(0);" data-id="'+aData[0]+'" sval="1" class="delsyscon linebtn">恢复</a>';
						}else{
							opts+='<a href="javascript:void(0);" data-id="'+aData[0]+'" sval="0" class="delsyscon linebtn">删除</a>';
						}
                    	$('td:eq(4)', nRow).html(opts);
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
                    	var tabs='<a href="#" class="stalist curr" order-status="1">有效案例</a>';
                    	tabs+='<a href="#" class="stalist" order-status="0">无效案例</a>';
                    	$('#filteropt').html(tabs);
                    }
            	});  
            	oTable.page('first').draw(false);
            	$('body').delegate('#filteropt a','click',function(){
                	var opt = $(this).attr('data-currorhis');
                	$(this).addClass('curr').siblings().removeClass('curr');
                	oTable.page('first').draw(false);
                	return false;
                }).delegate('.delsyscon','click',function(){
                	$.post('/system/deleteSysConCaseInfo',{sscId:$(this).attr('data-id'),sval:$(this).attr('sval')},function(data){
                		oTable.draw(false);
                	});
                });
            });
            function retrieveData( sSource, aoData, fnCallback ) {
			    aoData.push( { "name": "search", "value":$('input[type="search"]').val()} );
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

