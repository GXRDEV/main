<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
		<!DOCTYPE html>
		<html lang="en">

		<head>
			<title>平台医生/专家</title>
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
				.imcome-label{
					display: inline-block; min-width: 6em;
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
												<th>ID</th>
												<th>姓名</th>
												<th>医院</th>
												<th>科室</th>
												<th>电话</th>
												<th>类型</th>
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
		<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h3 id="myModalLabel">分帐管理</h3>
			</div>
			<div class="modal-body">
				<form action="" style="margin-left: 2em;">
					<div class="control-group hid"><label class="imcome-label">姓名：</label><span id="doc"></span></div>
					<div class="control-group hid"><label class="imcome-label">医院：</label><span id="hos"></span></div>
					<div class="control-group hid"><label class="imcome-label">电话：</label><span id="tel"></span></div>
					<div class="control-group"><label class="imcome-label">入账金额*：</label><input type="number" name="amount"></div>
					<div class="control-group"><label class="imcome-label">入账名目：</label><input type="text" name="amountType" placeholder="例如“奖励金”"></div>
					<div class="control-group"><label class="imcome-label">操作人*：</label><input type="text" name="operator"></div>
				</form>
			</div>
			<div class="modal-footer">
				<span style="color: red; margin-right: 2em;" id="tiptop"></span>
				<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
				<button class="btn btn-primary" onclick="sumitMoneyResult()">提交</button>
			</div>
		</div>
			<script src="/js/jquery.min.js"></script>
			<script src="/js/bootstrap.min.js"></script>
			<script src="/js/dataTables/js/jquery.dataTables.min.js" type="text/javascript"></script>
			<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
			<script src="/sea-modules/seajs/seajs.config.js"></script>
			<script type="text/javascript">
				var _burl = '/',
					_h = _burl,
					_id = '';
			</script>
			<script>
				var oTable, Selected;
				$(document).ready(function () {
					oTable = $('.data-table').DataTable({
						"bJQueryUI": true,
						"sDom": '<".example_processing"r><".tablelist"t><"F"flp>',
						"iDisplayStart": 0,
						"iDisplayLength": 10,
						"lengthMenu": [10, 15, 20, 25, 50, 100],
						"bDestroy": true,
						"bRetrieve": true,
						"bStateSave": true,
						"bServerSide": true,
						"fnServerData": retrieveData,
						"sAjaxSource": "/system/gainDoctors",
						"bFilter": true,
						"bLengthChange": true,
						"bProcessing": true,
						"fnCreatedRow": function (nRow, aData, iDataIndex) {
							$('td:eq(6)', nRow).html('<button class="editbtn linebtn" onclick="giveMoney('+ iDataIndex +')">发钱</button>');
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
						}
					});
				});
				function giveMoney(idx) {
					Selected = oTable.rows(idx).data()[0];
					$('#doc').html(Selected[1]);
					$('#hos').html(Selected[2] + ' / ' + Selected[3]);
					$('#tel').html(Selected[4]);
					$('#tiptop').html('');
					$('input[name="amount"]').val('');
					$('input[name="amountType"]').val('');
					//$('input[name="operator"]').val('');
					$('#myModal').modal('show');
				}
				function sumitMoneyResult () {
					var doctorId = Selected[0];
					var amount = $('input[name="amount"]').val()
					var amountType = $('input[name="amountType"]').val()
					var operator = $('input[name="operator"]').val()
					if(doctorId && amount > 0 && operator) {
						$('#tiptop').html('正在提交...');
						$.post('/system/subDocImcome', { 
							doctorId: doctorId, 
							amount: amount, 
							amountType: amountType, 
							operator: operator
						}).done(function (d) {
							$('#tiptop').html('支付成功。');
							oTable.draw(false);
							$('#myModal').modal('hide');
						})
					} else {
						$('#tiptop').html('金额要大于0，操作人不能为空。');
					}
				}
				function retrieveData(sSource, aoData, fnCallback) {
					aoData.push({
						"name": "search",
						"value": $('input[type="search"]').val()
					});
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
			</script>
		</body>

		</html>