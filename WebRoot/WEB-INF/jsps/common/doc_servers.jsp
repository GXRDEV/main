<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>我的服务列表</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/css/matrix-style2.css" />
	<link rel="stylesheet" href="/css/matrix-media.css" />
	<link href="/font-awesome/css/font-awesome.css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="/libs/daterang/daterangepicker.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/videolist.css" />
	<link rel="stylesheet" href="/css/view/videolistwidthbtn.css" />
	<style>
		.condations input,
		.condations select{ width: 100%;}
	</style>	
</head>
<body>
	<div class="mainlist">
	  <div class="container-fluid" style="padding-top:20px;">
	    <div class="row-fluid">
	      <div class="span12">
	        <div class="widget-box">
	          <div class="widget-content nopadding">
	            <table class="table table-bordered data-table">
	              <thead>
	                <tr>
	                  <th>医生服务ID</th>
	                  <th>系统服务ID</th>
	                  <th>服务名称</th>
	                  <th>价格</th>
	                  <th>指导价格</th>
	                  <th>服务说明</th>
	                  <th>是否开通</th>
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
	<script type="text/javascript" src="/libs/daterang/moment.min.js"></script><!-- sea-modules/daterangepicker/ -->
	<script type="text/javascript" src="/libs/daterang/daterangepicker.js"></script>
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
	<script type="text/javascript">
		var _burl='/',_h = _burl,_id = '';
	</script>
	<script>
        	var oTable;
        	var contadion = [], formData = {};
            $(document).ready(function () {            	
            	oTable = $('.data-table').DataTable({
                    "bJQueryUI": true,"sDom": '<".example_processing"r><".tablelist"t><"F"flp>',
                    "iDisplayStart":0,"iDisplayLength":10,
                    "lengthMenu": [ 10, 15, 20, 25, 50, 100],
                    "bDestroy":true,"bRetrieve":true,"bStateSave":true,
                    "bServerSide": true,"fnServerData": retrieveData,
                    "sAjaxSource": "/doctor/gainserverdatas",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,
                    "columns":[
                         {"visible": false}, {"visible": false},null,null,null,null,null,null
                   ], 
					"fnCreatedRow": function (nRow, aData, iDataIndex) {	
						var opts='';
						if(aData[6]=='未开通'){
							opts+='<a href="javascript:void(0);" dsid="'+aData[0]+'" ssid="'+aData[1]+'" sval="1" smoney="'+aData[3]+'" class="statusup editbtn linebtn">开通</a>';
						}else if(aData[6]=='已开通'){
							opts+='<a href="javascript:void(0);" dsid="'+aData[0]+'" ssid="'+aData[1]+'" sval="0" smoney="'+aData[3]+'" class="statusup editbtn linebtn">取消开通</a>';
						}
						$('td:eq(5)', nRow).html(opts);
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
            });
            $('body').delegate('.statusup','click',function(){
            	var dsid=$(this).attr("dsid"),sval=$(this).attr("sval"),smoney=$(this).attr("smoney"),ssid=$(this).attr("ssid");
            	if(confirm("您正在变更服务状态，请确认是否继续操作！")){
            		if(sval=='1'){
            			smoney=prompt("请输入服务价格！",smoney);
            		}
            		$.post('/doctor/changeServerStatus',{dsid:dsid,ssid:ssid,sval:sval,smoney:smoney},function(data){
                		oTable.page('first').draw(false);
                	});
            	}
            });
            function retrieveData( sSource, aoData, fnCallback ) {
			    aoData.push( { "name": "searchContent", "value":$('input[type="search"]').val()} );
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
					{ "name": "docname", "value":$('input[name="docname"]').val()},
					{ "name": "busitype", "value":$('#busitype').val()},
					{ "name": "actions", "value":$('#actions').val()},
					{ "name": "startDate", "value": formData['startDate'] || '' },
					{ "name": "endDate", "value": formData['endDate'] || '' }
				];
				oTable.page('first').draw(false);
			}
	</script>
</body>
</html>

