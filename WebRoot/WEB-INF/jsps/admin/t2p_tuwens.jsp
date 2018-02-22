<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>团队-患者 团队问诊</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/css/matrix-style2.css" />
	<link rel="stylesheet" href="/css/matrix-media.css" />
	<link href="/font-awesome/css/font-awesome.css" rel="stylesheet" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/videolist.css" />
	<link rel="stylesheet" href="/css/view/videolistwidthbtn.css" />
	<link rel="stylesheet" href="/css/view/chooseexps.css" />
	<style>
		.opsel{
			display: inline-block;
			width:100px;
		    background-color: transparent;
		    border-radius: 1px;
		    padding: 0 10px;
		    color: #FA8945;
		    margin-left:5px;
		    line-height: 1.5em;
		    height:25px;
		    border: 1px solid #FA8945;
		}
		td i{ color:#ccc; font-style:normal;margin:0 3px;}
		.wait{ color: red !important; font-size: 15px!important; font-weight: 600 !important}
	</style>
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
                  <th>患者信息</th>
                  <th>患者性别</th>
                  <th>患者年龄</th>
                  <th>团队信息</th>
                  <th>团队责任人</th>
                  <th>责任人医院</th>
                  <th>责任人科室</th>
                  <th>医生信息</th>
                  <th>医生医院</th>
                  <th>医生科室</th>
                  <th>提交时间</th>
                  <th>接诊时间</th>
                  <th>需要指派</th>
                  <th>支付状态</th>
                  <th>费用</th>
                  <th>退款状态</th>
                  <th>退款时间</th>
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
		seajs.use('view/admin/main1',function(controller){
			controller.chooseEXPS();
		});
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
                    "sAjaxSource": "/system/t2ptuwendatas",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,
                    "columnDefs": [
                    	{
                    		"targets": 1,
                   			"render": function ( data, type, row, meta ) {	                  
						    	return row[1] + '<i>/</i>' + row[2] + '<i>/</i>' + row[3];
						    }
                   		},
                   		{
                   			"targets": [2, 3,5,6,7,9,10], "visible": false
                   		},
                   		{
                   			"targets": 4,
                   			"render": function ( data, type, row, meta ) {	                  
						    	return row[4] + '<i>/</i>' + row[5] + '<i>/</i>' + row[6]+'<i>/</i>' + row[7];
						    }
                   		},
                   		{
                   			"targets": 8,
                   			"render": function ( data, type, row, meta ) {	 
                   				if(row[8]=='null') return "待分配";
						    	return row[8] + '<i>/</i>' + row[9] + '<i>/</i>' + row[10];
						    }
                   		},
                   		{
                   			"targets": 16,
                   			"createdCell": function (td, cellData, rowData, row, col){
                   				var status = $('#filteropt a.curr').attr('order-status'); 
                   				(status == 6 || status == 7) ? $(td).show() : $(td).hide();
								(rowData[16] == '待退款' || rowData[16] == '退款失败') ? $(td).addClass('wait') : ''
                   			}
                   		},
                   		{
                   			"targets": 17,
                   			"createdCell": function (td, cellData, rowData, row, col){
                   				var status = $('#filteropt a.curr').attr('order-status'); 
                   				(status == 6 || status == 7) ? $(td).show() : $(td).hide();
                   			}
                   		}
					],
					"fnCreatedRow": function (nRow, aData, iDataIndex) {
						var os=$('#filteropt a.curr').attr('order-status')||"2";
						var opts='<a href="/app/main.html#ajax/admin/admin_detail.html?orderId='+aData[0]+'&&orderType=12" class="editbtn linebtn">查看详情</a>';
						if(os=='2'){
							opts+='<a href="javascript:void(0);" class="tstatusup linebtn" val="out" oid='+aData[0]+'>订单退诊</a>';
							opts+='<a href="javascript:void(0);" class="tstatusup linebtn" val="delete" oid='+aData[0]+'>订单删除</a>';
						}else if(os=='3'){
							opts+='<a href="javascript:void(0);" class="tstatusup linebtn" val="complete" oid='+aData[0]+'>订单完成</a>';
						}else if(os=='6'||os=='7'){
							opts+='<a href="javascript:void(0);" class="tstatusup linebtn" val="delete" oid='+aData[0]+'>订单删除</a>';
						}
                    	$('td:eq(11)', nRow).html(opts);
                    },
                    "fnDrawCallback":function(nRow){
                    	if($('#filteropt a.curr').attr('order-status')=='6'||
                    			$('#filteropt a.curr').attr('order-status')=='7'){
							$('th:eq(9)').show().next().show();
						}else{
							$('th:eq(9)').hide().next().hide();
						}
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
                    	var tabs='<a href="#" class="stalist curr" order-status="2">待接诊</a>';
                    	tabs+='<a href="#" class="stalist" order-status="3">已接诊</a>';
                    	tabs+='<a href="#" class="stalist" order-status="4">待复诊</a>';
                    	tabs+='<a href="#" class="stalist" order-status="5">已完成</a>';
                    	tabs+='<a href="#" class="stalist" order-status="6">已退诊</a>';
                    	tabs+='<a href="#" class="stalist" order-status="7">已取消</a>';
                    	$('#filteropt').html(tabs);
                    }
                });
            	$('body').delegate('#filteropt a','click',function(){
                	var opt = $(this).attr('data-currorhis');
                	$(this).addClass('curr').siblings().removeClass('curr');
                	oTable.page('first').draw(false);
                	return false;
                }).delegate('.tstatusup','click',function(){
                	var oid=$(this).attr("oid"),sval=$(this).attr("val"),refuls='';
                	if(confirm("您正在变更订单状态，请确认是否继续操作！")){
                		if(sval=='out'){
                			refuls=prompt("请输入退诊理由！","");
                		}
                		$.post('/system/changeOrderStatus',{oid:oid,sval:sval,otype:"12",refuls:refuls},function(data){
                    		oTable.draw(false);
                    	});
                	}
                }).delegate('.sharelink','click',function(){
                	$.post('/system/generateUrl',{oid:$(this).attr('data-id'),ltype:'2'},function(data){
                		prompt("病例分享链接","http://develop.ebaiyihui.com/share/case/"+data.ucode);
                	});
                });
            function retrieveData( sSource, aoData, fnCallback ) {
			    aoData.push( { "name": "search", "value":$('input[type="search"]').val()} );
			    aoData.push( { "name": "ostatus", "value":$('#filteropt a.curr').attr('order-status')||'2'} );
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
            });
	</script>
</body>
</html>

