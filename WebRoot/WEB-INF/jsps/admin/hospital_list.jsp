<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>医院管理</title>
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
				<a href="/system/intohos" class="btn btn-content js-addinfo editbtn">新增</a>
			</div>
	        <div class="widget-box">
	          <div class="widget-content nopadding">
	            <table class="table table-bordered data-table">
	              <thead>
	                <tr>
	                  <th>ID</th>
	                  <th>名称</th>
	                  <th>简称</th>
	                  <th>等级</th>
	                  <th>地区</th>
	                  <th>合作医院</th>
	                  <th>状态</th>
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
                    "sAjaxSource": "/system/gaindhospitals",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,   
					"fnCreatedRow": function (nRow, aData, iDataIndex) {
						if(aData[5].indexOf('审核通过') != -1){
							$('td:eq(5)', nRow).html(aData[5].replace('审核通过','<span class="passed">审核通过</span>'));							
						}else if(aData[5].indexOf('审核不通过') != -1){
							$('td:eq(5)', nRow).html(aData[5].replace('审核不通过','<span class="nopassed">审核不通过</span>'));							
						}else if(aData[5].indexOf('待审核') != -1){
							$('td:eq(5)', nRow).html(aData[5].replace('待审核','<span class="waitpassed">待审核</span>'));							
						}
						var xops='';
						if(aData[6].indexOf('已上线') != -1){
							$('td:eq(6)', nRow).html(aData[6].replace('已上线','<span class="passed">已上线</span>'));	
							xops='<a href="javascript:void(0);" class="editbtn linebtn onandoff" hosid='+aData[0]+'>下线</a>';
						}else if(aData[6].indexOf('已下线') != -1){
							$('td:eq(6)', nRow).html(aData[6].replace('已下线','<span class="nopassed" >已下线</span>'));	
							xops='<a href="javascript:void(0);" class="editbtn linebtn onandoff" hosid='+aData[0]+'>上线</a>';
						}
                    	$('td:eq(7)', nRow).html('<a href="'+ _burl +'system/intohos?hosid='+ aData[0] +'" class="editbtn linebtn">编辑</a><a href="'+ _burl +'system/showdetail/'+ aData[0] +'" class="detailbtn linebtn">查看详情</a><a href="'+ _burl +'system/departs/'+aData[0]+'" class="detailbtn linebtn">科室设置</a>'+xops);
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
                    	$('#filteropt').html('<a href="#" class="stalist curr" data-currorhis="coohos">合作医院</a><a href="#" class="stalist" data-currorhis="history">专家医院</a>');
                    }
            	});  
            	
            	$('body').delegate('#filteropt a','click',function(){
                	var opt = $(this).attr('data-currorhis');
                	$(this).addClass('curr').siblings().removeClass('curr');
                	oTable.page('first').draw(false);
                	return false;
                }).delegate('.onandoff','click',function(){
                	var hosid=$(this).attr("hosid");
                	$.post('/system/onandoff',{hosid:hosid},function(data){
                		oTable.page('first').draw(false);
                	})
                });
            	oTable.page('first').draw(false);
            });
            function retrieveData( sSource, aoData, fnCallback ) {
			    aoData.push( { "name": "searchContent", "value":$('input[type="search"]').val()} );
			    aoData.push( { "name": "currorhis", "value":$('#filteropt a.curr').attr('data-currorhis')||'coohos'} );
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

