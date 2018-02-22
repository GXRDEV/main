<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
	<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
		<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
			<!DOCTYPE html>
			<html lang="en">

			<head>
				<title>医生随访列表</title>
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
					.opsel {
						display: inline-block;
						width: 100px;
						background-color: transparent;
						border-radius: 1px;
						padding: 0 10px;
						color: #FA8945;
						margin-left: 5px;
						line-height: 1.5em;
						height: 25px;
						border: 1px solid #FA8945;
					}
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
													<th>类型</th>
													<th>订单uuid</th>
													<th>医生ID</th>
													<th>患者ID</th>
													<th>医生姓名</th>
													<th>医生医院</th>
													<th>医生科室</th>
													<th>患者姓名</th>
													<th>消息数</th>
													<th>最新消息时间</th>
													<th>最新消息内容</th>
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

				<script>
					var oTable;
					$(document).ready(function () {
						oTable = $('.data-table').DataTable({
							"bJQueryUI": true, "sDom": '<".example_processing"r><".tablelist"t><"F"<"#filteropt">flp>',
							"iDisplayStart": 0, "iDisplayLength": 10,
							"lengthMenu": [10, 15, 20, 25, 50, 100],
							"bDestroy": true, "bRetrieve": true, "bStateSave": true,
							"bServerSide": true, "fnServerData": retrieveData,
							"sAjaxSource": "/system/gainDocFollowDatas",
							"bFilter": true, "bLengthChange": true, "bProcessing": true,
							"columns": [
								{ "visible": false }, { "visible": false }, { "visible": false }, { "visible": false }, null, null, null, null, null, null, null, null
							],
							"fnCreatedRow": function (nRow, aData, iDataIndex) {
								var ops = '<a href="/system/gainDocFollowDetailData?docId=' + aData[2] + '&subUserId=' + aData[3] + '&type=' + aData[0] + '&orderUuid=' + aData[1] + '" class="editbtn linebtn">查看详情</a>';
								$('td:eq(7)', nRow).html(ops);
								var str = aData[10].length > 20 ? aData[10].substr(0, 20)+'...' : aData[10]
								$('td:eq(6)', nRow).html(str);
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
							"initComplete": function (settings, json) {
								var tabs = '<a href="#" class="stalist curr" order-status="6">图文咨询</a>';
								tabs += '<a href="#" class="stalist" order-status="7">电话咨询</a>';
								tabs += '<a href="#" class="stalist" order-status="9">快速问诊</a>';
								tabs += '<a href="#" class="stalist" order-status="12">团队问诊</a>';
								tabs += '<a href="#" class="stalist" order-status="99">非订单随访</a>';
								$('#filteropt').html(tabs);
							}
						});
						$('body').delegate('#filteropt a', 'click', function () {
							var opt = $(this).attr('data-currorhis');
							$(this).addClass('curr').siblings().removeClass('curr');
							oTable.page('first').draw(false);
							return false;
						});
						function retrieveData(sSource, aoData, fnCallback) {
							aoData.push({ "name": "search", "value": $('input[type="search"]').val() });
							aoData.push({ "name": "ostatus", "value": $('#filteropt a.curr').attr('order-status') || '6' });
							$.ajax({
								"type": "post",
								"contentType": "application/json",
								"url": sSource,
								"dataType": "json",
								"data": JSON.stringify(aoData),
								"success": function (resp) {
									fnCallback(resp);
								}
							});
						}
					});
				</script>
			</body>

			</html>