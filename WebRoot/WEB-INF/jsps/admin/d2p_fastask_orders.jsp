<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>医生-患者 快速问诊订单</title>
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
                  <th>联系电话(患者)</th>
                  <th>医生信息</th>
                  <th>医生医院</th>
                  <th>医生科室</th>
                  <th>医生职称</th>
                  <th>下单时间</th>
                  <th>距离下单</th>
                  <th>接诊时间</th>
                  <th>距离接诊</th>
                  <th>支付状态</th>
                  <th>费用</th>
                  <th>订单状态</th>
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
                    "sAjaxSource": "/system/d2pfastaskdatas",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,
                    "columnDefs": [
                    	{
                    		"targets": 1,
                   			"render": function ( data, type, row, meta ) {	                  
						    	return row[1] + '<i>/</i>' + row[2] + '<i>/</i>' + row[3] + '<i>/</i>' + row[4];
						    }
                   		},
                   		{
                   			"targets": [2, 3, 4, 6, 7,8], "visible": false
                   		},
                   		{
                   			"targets": 5,
                   			"render": function ( data, type, row, meta ) {	
                   				if(row[5]==''||row[5]=='null')return "未选择";
						    	return row[5] + '<i>/</i>' + row[6] + '<i>/</i>' + row[7]+ '<i>/</i>' + row[8];
						    }
                   		},
                   		{
                   			"targets": 16,
                   			"createdCell": function (td, cellData, rowData, row, col){
                   				var status = $('#filteropt a.curr').attr('order-status'); 
                   				(status == 5 || status == 6) ? $(td).show() : $(td).hide();
								(rowData[16] == '待退款' || rowData[16] == '退款失败') ? $(td).addClass('wait') : ''
                   			}
                   		},
                   		{
                   			"targets": 17,
                   			"createdCell": function (td, cellData, rowData, row, col){
                   				var status = $('#filteropt a.curr').attr('order-status'); 
                   				(status == 5 || status == 6) ? $(td).show() : $(td).hide();
                   			}
                   		}
					],
					"fnCreatedRow": function (nRow, aData, iDataIndex) {
						var opts='<a href="/app/main.html#ajax/admin/admin_detail.html?orderId='+aData[0]+'&&orderType=9" class="editbtn linebtn">查看详情</a>';
						if(aData[15]=='待接诊'&&aData[13]=='未支付'){
							opts+='<a href="javascript:void(0);" class="statusup linebtn" val="delete" oid='+aData[0]+'>订单删除</a>';
						}else if(aData[15]=='进行中'){
							opts+='<a href="javascript:void(0);" class="statusup linebtn" val="complete" oid='+aData[0]+'>订单完成</a>';
						}else if(aData[15]=='已退诊'||aData[14]=='已取消'){
							opts+='<a href="javascript:void(0);" class="statusup linebtn" val="delete" oid='+aData[0]+'>订单删除</a>';
						}
                    	$('td:eq(12)', nRow).html(opts);
                    },
                    "fnDrawCallback":function(nRow){
                    	if($('#filteropt a.curr').attr('order-status')=='5'||
                    			$('#filteropt a.curr').attr('order-status')=='6'){
							$('th:eq(10)').show().next().show();
						}else{
							$('th:eq(10)').hide().next().hide();
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
                    	var tabs='<a href="#" class="stalist curr" order-status="3">问诊中</a>';
						tabs+='<a href="#" class="stalist" order-status="1">待支付</a>';
                    	tabs+='<a href="#" class="stalist" order-status="4">已完成</a>';
                    	tabs+='<a href="#" class="stalist" order-status="5">已退诊</a>';
                    	tabs+='<a href="#" class="stalist" order-status="6">已取消</a>';
                    	$('#filteropt').html(tabs);
                    }
                });
            	$('body').delegate('#filteropt a','click',function(){
                	var opt = $(this).attr('data-currorhis');
                	$(this).addClass('curr').siblings().removeClass('curr');
                	oTable.page('first').draw(false);
                	return false;
                }).delegate('.statusup','click',function(){
                	var oid=$(this).attr("oid"),sval=$(this).attr("val"),refuls='';
                	if(confirm("您正在变更订单状态，请确认是否继续操作！")){
                		$.post('/system/changeOrderStatus',{oid:oid,sval:sval,otype:"9",refuls:refuls},function(data){
                    		oTable.draw(false);
                    	});
                	}
                }).delegate('.refund','click',function(){
                	alert("暂未开放。。。。");
                }).delegate('.sharelink','click',function(){
                	$.post('/system/generateUrl',{oid:$(this).attr('data-id'),ltype:'2'},function(data){
                		prompt("病例分享链接","http://develop.ebaiyihui.com/share/case/"+data.ucode);
                	});
                });
            function retrieveData( sSource, aoData, fnCallback ) {
			    aoData.push( { "name": "search", "value":$('input[type="search"]').val()} );
			    aoData.push( { "name": "ostatus", "value":$('#filteropt a.curr').attr('order-status')||'3'} );
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

