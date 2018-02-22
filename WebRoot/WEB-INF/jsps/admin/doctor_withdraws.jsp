<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>提现审核</title>
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
		<div class="container-fluid" style="padding-top:20px;">
		<div class="condations">
		  	<div class="row-fluid">
		  		<div class="span3">
		  			<label>角色类型：</label>
		  			<select name="busitype">
		  				<option value="">==选择角色类型==</option>
		  				<option value="3">医生</option>
		  				<option value="2">专家</option>
		  			</select>
		  		</div>
		  		<div class="span2">
		  			<label>&ensp;</label>
		  			<button onclick="setQuestion()" class="btn btn-primary">查询</button>
		  		</div>
		  	</div>
	  	</div>
	    <div class="row-fluid">
	      <div class="span12">
	        <div class="addarea"></div>
	        <div class="widget-box">
	          <div class="widget-content nopadding">
	            <table class="table table-bordered data-table">
	              <thead>
	                <tr>
	                  <th>ID</th>
	                  <th>医生信息</th>   
	                  <th>提现银行</th>          
	                  <th>提现金额</th>
	                  <th>代扣税费</th>
	                  <th>实际到帐</th> 
					  <th>状态</th>
					  <th>申请时间</th>
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
        	var contadion = [];
            $(document).ready(function () {            	
            	oTable = $('.data-table').DataTable({
            		"bJQueryUI": true,"sDom": '<".example_processing"r><".tablelist"t><"F"<"#filteropt">flp>',
                    "iDisplayStart":0,"iDisplayLength":10,
                    "lengthMenu": [ 10, 15, 20, 25, 50, 100],
                    "bDestroy":true,"bRetrieve":true,"bStateSave":true,
                    "bServerSide": true,"fnServerData": retrieveData,
                    "sAjaxSource": "/system/gaindoctorwithdrawdatas",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,   
					"fnCreatedRow": function (nRow, aData, iDataIndex) {						
                    	$('td:eq(9)', nRow).html('<a href="'+ _burl +'system/docwithdrawdetail/'+ aData[0] +'" class="editbtn linebtn">查看详情</a>');
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
                    	var tabs='<a href="#" class="stalist curr" order-status="1">待审核</a>';
                    	tabs+='<a href="#" class="stalist" order-status="2">审核通过</a>';
                    	tabs+='<a href="#" class="stalist" order-status="3">审核未通过</a>';
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
			        "data": JSON.stringify(aoData.concat(contadion)),
			        "success": function(resp) {  
			            fnCallback(resp);
			        }  
			    });  
			}
			function setQuestion(){
				contadion = [
					{ "name": "busitype", "value":$('select[name="busitype"]').val()}
				];
				oTable.page('first').draw(false);
			}
	</script>
</body>
</html>

