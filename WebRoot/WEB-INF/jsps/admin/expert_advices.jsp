<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>专家咨询订单</title>
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
		table td img{width: 40px;height:40px;}
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
                  <th>专家头像</th>
                  <th>专家姓名</th>
                  <th>专家医院</th>
                  <th>专家科室</th>
                  <th>医生姓名</th>
                  <th>医生医院</th>
                  <th>医生科室</th>
                  <th>患者基本信息</th>
                  <th>咨询目的</th>
                  <th>支付状态</th>
                  <th>提交时间</th>
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
                    "sAjaxSource": "/system/gainexadvices",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,   
                    "columns":[
                          		null,null,null,null,null,null,null,null,null,null,null,null,null
                           ],
					"fnCreatedRow": function (nRow, aData, iDataIndex) {
						var path= '/system/showadvicedetail/' + aData[0],_val=aData[9];
						var ops='';
						ops+='<a href="javascript:void(0);" data-id="'+ aData[0] +'" class="linebtn sharelink">分享链接</a>';
						$('td:eq(1)', nRow).html('<img src="'+ (aData[1] == 'null' ? '/app/img/avatars/male.png' : aData[1]) +'" alt="专家头像">');
                    	$('td:eq(9)', nRow).html((aData[9] || '').substring(0,15));
                    	$('td:eq(12)', nRow).html('<a href="'+path+'" data-id="'+ aData[0] +'" class="editbtn linebtn">查看详情</a>'+ops);
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
                    	tabs+='<a href="#" class="stalist" order-status="2">待接诊</a>';
                    	tabs+='<a href="#" class="stalist" order-status="4">已完成</a>';
                    	tabs+='<a href="#" class="stalist" order-status="5">已退诊</a>';
                    	tabs+='<a href="#" class="stalist" order-status="6">已取消</a>';
                    	$('#filteropt').html(tabs);                   
                    }
                });
            	oTable.page('first').draw(false);
            	$('body').delegate('#filteropt a','click',function(){
                	var opt = $(this).attr('data-currorhis');
                	$(this).addClass('curr').siblings().removeClass('curr');
                	oTable.page('first').draw(false);
                	return false;
                }).delegate('.closetw','click',function(){
                	var oid=$(this).attr("data-id");
                	$.post('/doctor/closetw',{oid:oid,utype:'0'},function(data){
                		oTable.page('first').draw(false);
                	});
                }).delegate('.sharelink','click',function(){
                	$.post('/system/generateUrl',{oid:$(this).attr('data-id'),ltype:'5'},function(data){
                		prompt("病例分享链接",'/'+"share/case/"+data.ucode);
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
            });
	</script>
</body>
</html>

