<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>申请加入医院列表</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/css/matrix-style2.css" />
	<link rel="stylesheet" href="/css/matrix-media.css" />
	<link href="/font-awesome/css/font-awesome.css" rel="stylesheet" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/videolist.css" />
	<link rel="stylesheet" href="/css/view/videolistwidthbtn.css" />
	<style>
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
	min-width: 12em;
	text-align: center;
	color: #8F9092;
	background: #fff;
	margin:0 -10px!important;
	}
	.fliters a:last-child {
	margin-left: -16px;
	}

	</style>
</head>
<body>
	<div class="mainlist">
	  <div class="container-fluid">
	    <div class="row-fluid">
	      <div class="span12">
			<div class="fliters" id="filteropt"></div>
	        <div class="widget-box">
	          <div class="widget-content nopadding">
	            <table class="table table-bordered data-table">
	              <thead>
	                <tr>
	                  <th>ID</th>
	                  <th>医联体名称</th>
	                  <th>所属区域</th>
	                  <th>核心医院</th>
	                  <th class="hosTitle">上级医院</th>
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
            $(document).ready(function () {     
							var opt = 1;     
            	oTable = $('.data-table').DataTable({
            		"bJQueryUI": true,"sDom": '<".example_processing"r><".tablelist"t><"F"flp>',
                    "iDisplayStart":0,"iDisplayLength":10,
                    "lengthMenu": [ 10, 15, 20, 25, 50, 100],
                    "bDestroy":true,"bRetrieve":true,"bStateSave":true,
                    "bServerSide": true,"fnServerData": retrieveData,
                    "sAjaxSource": "/hospital/gainappjoindatas",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,
										"fnCreatedRow": function (nRow, aData, iDataIndex) {		
											var opts='';
											 if( opt == 1){
														if( aData[7] == 0 ){
															opts += '等待上级医院审核'
														}else if(aData[7]=='1'){
															opts+='审核通过';
														}else if(aData[7]=='-1'){
															opts+='审核未通过';
														}
												}else{
													if(aData[7] == 0){
															opts+='<a href="javascript:void(0);" aid="'+aData[0]+'" sval="1" class="changestatus linebtn">同意加入</a>';
															opts+='<a href="javascript:void(0);" aid="'+aData[0]+'" sval="-1" class="changestatus linebtn">拒绝加入</a>'
													} else if(aData[7] == 1){
															opts+='本院同意加入';
													} else{
															opts+='本院拒绝加入';
													}
												}
												$('td:eq(7)', nRow).html(opts);
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
												var tabs;
												tabs='<a href="#" class="selected" data-ostatus="1">我发起的加入申请</a>';
												tabs+='<a href="#" class="" data-ostatus="2">我收到的加入请求</a>';
												$('#filteropt').html(tabs)
                    }
            	});  
            	 $('body').delegate('#filteropt a','click',function(){
									opt = $(this).attr('data-ostatus');
									opt == 1? $('.hosTitle').html('上级医院'): $('.hosTitle').html('请求医院')
									$(this).addClass('selected').siblings().removeClass('selected');
									oTable.page('first').draw(false);
									return false;
							}).delegate('.changestatus','click',function(){
                 	var aid=$(this).attr('aid'),sval=$(this).attr('sval');
                 	if(confirm("请确认是否更改医院的申请加入医联体的状态？！")){	
                		$.post('/hospital/changeappstatus',{aid:aid,sval:sval,stype:1},function(data){
                    		oTable.page('first').draw(false);
                    	});
                	}
                 });
            });
            function retrieveData( sSource, aoData, fnCallback ) {
							aoData.push( { "name": "searchContent", "value":$('input[type="search"]').val()} );
							aoData.push( { "name": "ostatus", "value":$('#filteropt a.selected').data('ostatus')||'1'} );
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

