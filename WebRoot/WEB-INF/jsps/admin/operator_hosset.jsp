<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>运营人员医院管理</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/sea-modules/select2/css/select2.min.css" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/css/matrix-style2.css" />
	<link rel="stylesheet" href="/css/matrix-media.css" />
	<link href="/font-awesome/css/font-awesome.css" rel="stylesheet" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/videolist.css" />
	<link rel="stylesheet" href="/css/view/videolistwidthbtn.css" />
	<style>
		.select2-dropdown{ z-index: 99999; }
		.select2-dropdown [type="search"]{ height: 32px; }
		.select2-container .select2-dropdown{ margin-top: -10px; }
	</style>
</head>
<body>
	<div class="mainlist">
	  <div class="container-fluid">
	    <div class="row-fluid">
	      <div class="span12">
	      	<div class="addarea">
	        	<a href="#myModal" role="button" class="btn btn-content js-addinfo editbtn" data-toggle="modal">新增</a>
			</div>
	        <div class="widget-box">
	          <div class="widget-content nopadding">
	            <table class="table table-bordered data-table">
	              <thead>
	                <tr>
	                  <th>ID</th>
	                  <th>关联ID</th>
	                  <th>区域</th>
	                  <th>医院</th>
	                  <th>时间</th>
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
	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel">创建医院群组</h4>
	      </div>
	      <div class="modal-body">
			<form class="row-fluid form-horizontal" method="post" id="myform">
	 			 <div class="control-group">
				    <label class="control-label" style="width: 7em; ">医院：</label>
				    <div class="controls" style="margin-left: 8em;">
				    	<select name="hospitalId"></select>
	    			</div>
	 			 </div>
	 			 <input type="hidden" name="opId" value="${opId }"/>
	      	</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	        <button type="button" class="btn btn-primary" onclick="postForm()">保存</button>
	      </div>
	    </div>
	  </div>
	</div>
	<script src="/js/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/sea-modules/select2/js/select2.min.js"></script>
	<script src="/js/dataTables/js/jquery.dataTables.min.js" type="text/javascript"></script>
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
                    "sAjaxSource": "/system/operatorhosdatas",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,   
                    "columns":[
                        		null,{"visible": false},null,null,null,null
                         ],
					"fnCreatedRow": function (nRow, aData, iDataIndex) {			
						var opts='';
						opts+='<a href="javascript:void(0);" class="cancel linebtn" rid="'+aData[1]+'">取消</a>';
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
                    }
            	});  
            	oTable.page('first').draw(false);
            	
            	$.get('/system/gainHospitals').done(function(d){
            		$('[name="hospitalId"]').html(function(){
            			var options = [];
            			$.each(d.hospitals || [], function(i, item){
            				options.push('<option value="'+ item.id +'">'+ item.displayName +'</option>');
            			});
            			return options.join('');
            		});
            	});
            	
            	$('body').delegate('.cancel','click',function(){
                	var rid=$(this).attr("rid");
                	if(confirm("确定要取消该关联的医院么！！")){
                		$.post("/system/cancelRelativeHos",{rid:rid},function(){
                    		oTable.page('first').draw(false);
                        	return false;
                    	});
                	}
                });
                $('[name="hospitalId"]').select2();
            });
            function retrieveData( sSource, aoData, fnCallback ) {
			    aoData.push( { "name": "searchContent", "value":$('input[type="search"]').val()} );
			    aoData.push( { "name": "opId", "value": '${opId}' } );
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
			function postForm(){
				$.post('/system/distHospitalToOp', $('#myform').serializeArray()).done(function(){
					oTable.page('first').draw(false);
					$('#myModal').modal('hide');
				});
			}
	</script>
</body>
</html>

