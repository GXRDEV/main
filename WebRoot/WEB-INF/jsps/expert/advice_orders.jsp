<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
<title>专家咨询列表</title>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="/css/bootstrap.min.css" />
<link rel="stylesheet" href="/css/matrix-style2.css" />
<link rel="stylesheet" href="/css/matrix-media.css" />
<link href="/font-awesome/css/font-awesome.css" rel="stylesheet" />
<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
<link rel="stylesheet" href="/css/view/videolist.css" />
<style>       
	
</style>		
</head>
<body>
<div class="mainlist">
  <div class="container-fluid">
    <div class="row-fluid">
      <div class="span12">
        <div class="widget-box">
          <div class="widget-content nopadding">
            <table class="table table-bordered data-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>最新消息</th>
                  <th>患者基本信息</th>
                  <th>咨询内容</th>
				  <th>下单时间</th>
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
<script src="/js/matrix.js"></script>

<script type="text/javascript">
	var _burl='/';
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
                    "sAjaxSource": "/expert/gainadviceorders",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,
                    "aoColumnDefs":[{"bSortable":false,"aTargets":[0]}], 
                    "aaSorting": [
                                  [ 1, "asc" ],
                                  [ 2, "desc"]
                    ],
                    "columns":[
                   		{"visible": false},null,null,null,null,null
                    ],       
					"fnCreatedRow": function (nRow, aData, iDataIndex) {
						var path= '/expert/showadvicedetail/' + aData[0];
                        $('td:eq(4)', nRow).html('<a href="'+ path +'" class="editTool">查看详情</a>');
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
					    $('#filteropt').html('<a href="#" class="stalist curr" data-currorhis="processing">进行中</a><a href="#" class="stalist" data-currorhis="history">历史订单</a>');
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
			    aoData.push( { "name": "currstatus", "value":$('#filteropt a.curr').attr('data-currorhis')||'processing'} );
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

