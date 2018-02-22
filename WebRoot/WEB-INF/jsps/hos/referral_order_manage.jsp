<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>预约转诊订单管理</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/css/matrix-style2.css" />
	<link rel="stylesheet" href="/css/matrix-media.css" />
	<link href="/font-awesome/css/font-awesome.css" rel="stylesheet" />
	
	<link rel="stylesheet" href="/css/view/videolist.css" />
	<link rel="stylesheet" href="/css/view/videolistwidthbtn.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/admin.css" />
	<style type="text/css">
		.fliters {
		    padding: 7px 0 7px 0;
		    margin:10px;
		}
		.fliters a.selected {
		    background: #0186D1;
		    color: #fff;
		    z-index: 10;
		}
		.fliters a {
		    position: relative;
		    line-height: 28px;
		    border-radius: 1.3em;
		    border: 1px solid #f1f1f1;
		    display: inline-block;
		    min-width: 8em;
		    text-align: center;
		    color: #8F9092;
		    background: #fff;
		}
		.fliters a:last-child {
		    margin-left: -16px;
		}
		.modal td label{ margin: 0;}
		.modal tbody th{ background: #fff; }
		.modal tbody th input{ margin: 0; }
		.modal-body{
			max-height: 300px !important
		}
	</style>
</head>
<body>
<div class="mainlist">
  <div class="container-fluid" style="padding-top: 16px;">
    <div class="fliters">
        <a href="javascript:;;" class="selected" data-sign="1">发起的</a><a href="javascript:;;" data-sign="2">接收的</a>
    </div>
    <div class="row-fluid">
      <div class="span12">     
        <div class="widget-box">
          <div class="widget-content nopadding">
            <table class="table table-bordered data-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>医院科室</th>
                  <th>医院医生</th>
                  <th>患者信息</th>
                  <th>目标医院</th>
				  <th>目标医生</th>
				  <th>转诊时间</th>
				  <th>距离转诊时间</th>
				  <th>下单时间</th>
				  <th>距离下单时间</th>
				  <th>转诊类型</th>
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
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="display: none">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel">选择医生</h4>
	      </div>
	      <div class="modal-body">
			<form class="row-fluid form-horizontal"  method="post" id="myform">
				<table class="table table-bordered">
					<thead style="background-color:#f1f1f1;">
						<tr>
							<th></th>
							<th>医生姓名</th>
							<th>所在科室</th>
							<th>是否开通服务</th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	        <button type="button" class="btn btn-primary" onclick="postForm(this)">保存</button>
	      </div>
	    </div>
	  </div>
	</div>
	<script src="/js/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/js/dataTables/js/jquery.dataTables.min.js" type="text/javascript"></script>
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="/libs/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="/libs/ueditor/ueditor.all.min.js"> </script>
    <script type="text/javascript" charset="utf-8" src="/libs/ueditor/ueditor.config.frazior.js"></script>
	<script type="text/javascript">
		var _burl='/',_h = _burl,_id = '';
	</script>
	<script>
        	var oTable,ue;
            $(document).ready(function () {            	
            	oTable = $('.data-table').DataTable({
            		"bJQueryUI": true,"sDom": '<".example_processing"r><".tablelist"t><"F"<"#filteropt">flp>',
                    "iDisplayStart":0,"iDisplayLength":10,
                    "lengthMenu": [ 10, 15, 20, 25, 50, 100],
                    "bDestroy":true,"bRetrieve":true,"bStateSave":true,
                    "bServerSide": true,"fnServerData": retrieveData,
                    "sAjaxSource": "/hospital/gainReferralOrderDatas",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,     
					"fnCreatedRow": function (nRow, aData, iDataIndex) {
						var opts='';
					 	opts+='<a href="/app/main.html#ajax/admin/detail.html?oid='+ aData[0] +'&otype=10" class="linebtn">查看详情</a>';
					 	if($('.fliters a.selected').attr('data-sign')=='1'){
					 		if($('#filteropt a.curr').attr('order-status')==undefined||$('#filteropt a.curr').attr('order-status')=='1'){
					 			if( aData[2] == '待分配' ){
										opts+='<a href="javascript:void(0);" onclick="showDialog('+ aData[0] +')" class="distDoc linebtn">分配医生</a>';
								 }
								 opts+='<a href="javascript:void(0);" id="'+ aData[0] +'" sval="50" class="changstat linebtn">取消订单</a>';
					 		}
					 		if($('#filteropt a.curr').attr('order-status')=='2'){
					 			opts+='<a href="javascript:void(0);" id="'+ aData[0] +'" sval="50" class="changstat linebtn">取消订单</a>';
					 		}
					 		if($('#filteropt a.curr').attr('order-status')=='5'||$('#filteropt a.curr').attr('order-status')=='6'){
					 			opts+='<a href="javascript:void(0);" id="'+ aData[0] +'" class="del linebtn">删除订单</a>';
					 		}
					 	}else if($('.fliters a.selected').attr('data-sign')=='2'){
					 		//接收的预约转诊订单 待分配
					 		if( $('#filteropt a.curr').attr('order-status') == undefined || $('#filteropt a.curr').attr('order-status')== '1' ){
					 			opts+='<a href="javascript:void(0);" onclick="showDialog('+ aData[0] +')" class="distDoc linebtn">分配医生</a>';
					 			opts+='<a href="javascript:void(0);" id="'+ aData[0] +'" sval="30" class="changstat linebtn">退诊</a>';
					 		}
					 		if($('#filteropt a.curr').attr('order-status')=='2'){
					 			opts+='<a href="javascript:void(0);" id="'+ aData[0] +'" sval="30" class="changstat linebtn">退诊</a>';
					 		}
					 	}
                    	$('td:eq(11)', nRow).html(opts);
                    },
                    "language": {
			            "url": "/Chinese.txt"
			        },
                    "initComplete": function(settings, json) {
                    	var tabs='<a href="#" class="stalist curr" order-status="1">待分配</a>';
                    	tabs+='<a href="#" class="stalist" order-status="2">待接诊</a>';
                    	tabs+='<a href="#" class="stalist" order-status="3">已接诊</a>';
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
                }).delegate('.fliters a','click',function(){
                	$(this).addClass('selected').siblings().removeClass('selected');
                	if($(this).attr('data-sign')=="1"){
                		$('table thead tr').children().eq(4).text("目标医院");
                		$('table thead tr').children().eq(5).text("目标医生");
                	}else if($(this).attr('data-sign')=="2"){
                		$('table thead tr').children().eq(4).text("来源医院");
                		$('table thead tr').children().eq(5).text("来源医生");
                	}
    				oTable.page('first').draw(false);
                }).delegate('.del','click',function(){
                	var referId=$(this).attr('id');
                	$.post('/hospital/delReferral',{referId:referId},function(d){
                		oTable.draw(false);
                	});
                }).delegate('.changstat','click',function(){
                	var referId=$(this).attr('id'),sval=$(this).attr('sval');
                	$.post('/hospital/changReferralStat',{referId:referId,sval:sval},function(d){
                		oTable.draw(false);
                	});
                });
                
                $.get('/hospital/gainDistDoctors',{otype:10}).done(function(d){
                	$('#myform tbody').html(function(){
                		var label = '';
                		$.each(d.doctors || [], function(i, n){
                			var openser='';
                			if(n.openAsk==1){
                				openser='已开通';
                			}else{
                				openser='<a style="color:red" href="/app/main.html#ajax/doc/server.html?docid='+n.specialId+'">未开通</a>';
                			}
                			label += '<tr><th><input type="radio" name="doclist" id="doc'+ n.specialId +'" value="'+ n.specialId +'"></th><td><label for="doc'+ n.specialId +'">'+
                			n.specialName +'</label></td><td><label for="doc'+ n.specialId +'">'+ n.depName +'</label></td><td><label for="doc'+ n.specialId +'">'+ openser+'</label></td></tr>';
                		});
                		return label;
                	});
                });
            });
           function showDialog(id) {
           		$('#myModal').modal('show');
           		$('#myModal').find('.btn-primary').attr('data-id', id);
           }
           function postForm(btn) {
           		var id = btn.getAttribute('data-id');
           		var docid = $('#myModal input[type="radio"]:checked').val();
           		$.post('/hospital/distAndSelDoc', {
           			dtype:$('.fliters a.selected').attr('data-sign'),
           			type: '10',
           			doctorId: docid,
           			orderId: id
           		}).done(function (){
           			oTable.draw(false);
           			$('#myModal').modal('hide');
           		})
           }
            function retrieveData( sSource, aoData, fnCallback ) {
			    aoData.push( { "name": "search", "value":$('input[type="search"]').val()} );
			    aoData.push( { "name": "dtype", "value":$('.fliters a.selected').attr('data-sign') || ''} );
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

