<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>医联体管理</title>
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
	<link rel="stylesheet" href="/sea-modules/select2/css/select2.min.css" type="text/css" />
	<style scoped>
		.show-btn{ background: #0184d2}
		.noInfor{ width: 130px;	height: 90px }
		.text{ color: #ccc }
		.modal_create .webuploader-pick{ padding: 15px 0 10px }
		.modal_create .actionAdd .iconfont{ font-size: 80px; display: inline-block; margin-top: 20px}
		#fileBox_WU_FILE_0{top: -5px}
		.form-group label{ display: inline-block;width: 20%}
		.form-group .controls{ width: 75%; display: inline-block }
		.fileBoxUl{ margin-left: 0}
		.fileBoxUl li{ height: 80px; width: 80px }
		.select2-dropdown--below{ z-index: 1000000;}
		.select2-container {margin-top: -10px}
		textarea{ resize: none}
	</style>
</head>
<body>
	<div class="mainlist">
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
	        		<div class="addarea">
						<button class="btn btn-content change-btn show-btn" data-ids="1" data-toggle="modal" data-target="#myModal"></button>
					</div>
					<div class="widget-box">
						<div class="widget-content nopadding">
							<table class="table table-bordered data-table">
								<thead>
									<tr>
										<th>ID</th>
										<th>医联体名称</th>
										<th>所属区域</th>
										<th>核心医院</th>
										<th>入住医院数量</th>
										<th>加入时间</th>
										<th>审核状态</th>
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
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel"></h4>
			</div>
			<div class="modal-body">
				<div class="modal_create" style="display: none" id="modal_create">
					<div class="form-group">
						<label>医联体名称</label>
						<input type="text" name="yltName" class="controls">
					</div>
					<div class="form-group">
						<label style="vertical-align: top">logo</label>
						<div class="controls">
							<div id="picsheadImageUrl" class="diyUpload">
								<div class="parentFileBox">
									<ul class="fileBoxUl clearfix">
										<li id="fileBox_WU_FILE_0" class="browser" style="display:none">
											<div class="viewThumb">
												<img >
											</div>
											<div class="diyCancel">
												<i class="iconfont" title="删除"></i>
											</div>
											<div class="diySuccess" style="display: block"></div>
										</li>
										<li class="actionAdd">
											<div id="addheadImageUrl"><i class="iconfont">&#xe60e;</i></div>
										</li>
									</ul>
								</div>	
								<input type="hidden" id="headImageUrl" name="headImageUrl"/>			
							</div>
						</div>
					</div>
					<div class="form-group" style="padding: 8px 0">
						<label for="name">核心医院</label>
						<div class="controls">
							<span class="create_core" style="font-size: 14px"></span><span style="color: lightskyblue; padding-left: 8px"></span>
						</div>
					</div>
					<div class="form-group">
						<label for="name">擅长</label>
						<textarea class="controls" rows="2" name="speciality"></textarea>
					</div>
					<div class="form-group">
						<label for="name">医联体简介</label>
						<textarea class="controls" rows="2" name="introduce"></textarea>
					</div>
				</div>
				<div class="modal_add" style="display: none">
					<div class="form-group">
						<label>医联体名称</label>
						<div class="controls">
							<select id="hospitals" name="yltId">
								<option>--选择医联体--</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">核心医院</label>
						<div class="controls doublewidth" style="padding: 15px 0">
							<span class="core" style="font-size: 14px"></span><span style="color: lightskyblue; padding-left: 8px"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">上级医院</label>
						<div class="controls doublewidth">
							<select id="superiors" name="hosId">
								<option>--选择上级医院--</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label>申请医院</label>
						<div class="controls doublewidth" style="padding: 15px 0 0">
							<span class="hosNa" style="font-size: 14px"></span><span style="color: lightskyblue; padding-left: 8px"></span>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary pre"></button>
			</div>
		</div>
	</div>
	<script src="/js/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/js/dataTables/js/jquery.dataTables.min.js" type="text/javascript"></script>
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
	<script type="text/javascript" src="/sea-modules/select2/js/select2.min.js"></script>
	<script type="text/javascript">
		var _burl='/',_h = _burl,_id = '';
		seajs.use('view/admin/main',function(controller){
			controller.exportinfo();
		});
		$(document).ready(function(){
			var oTable, tabs, appjoin, parentHosId, joinyltId,cancreatylt, status, id, ids;
			appjoin = '${appjoin}',cancreatylt='${cancreatylt}';
			appjoin == 'true'?
				$('.change-btn').html('加入医联体').attr('data-id', 2):
					(cancreatylt=='true'?$('.change-btn').html('组建医联体').attr('data-id', 1):$('.change-btn').hide());
			oTable = $('.data-table').DataTable({
				"bJQueryUI": true,
				"sDom": '<".example_processing"r><".tablelist"t><"F"<"#filteropt">flp>',
				"iDisplayStart":0,
				"iDisplayLength":10,
				"lengthMenu": [ 10, 15, 20, 25, 50, 100],
				"bDestroy":true,
				"bRetrieve":true,
				"bStateSave":true,
				"bServerSide": true,
				"fnServerData": retrieveData,
				"sAjaxSource": "/hospital/gainmyyltdatas",
				"bFilter": true,
				"bLengthChange": true,
				"bProcessing": true,     
				"fnCreatedRow": function (nRow, aData, iDataIndex) {
					var member = '<a href="/hospital/yltdetail/'+aData[0]+'" class="resetpsd linebtn member">医院成员<span class="in"></span></a>'
					var spans = '<span>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; </span>';
					var details = '<span data-id="3" data-ids="3" data-yltId='+aData[0]+' data-state='+aData[6]+' class="resetpsd linebtn change-btn" data-toggle="modal" data-target="#myModal" style="cursor:pointer">详情</span>'
					status = aData[6];
					if(aData[6]==0){
						$('td:eq(6)', nRow).html("待审核");
						$('td:eq(7)', nRow).html(spans + details)
					}else if(aData[6]==1){
						$('td:eq(6)', nRow).html("已审核");
						$('td:eq(7)', nRow).html(member + details)
					}else if(aData[6]==-1){
						$('td:eq(6)', nRow).html("审核未通过");
						$('td:eq(7)', nRow).html(spans + details)
					}
				},
				"fnDrawCallback": function(){
					status == '0'?
						$('.member').hide(): $('.member').show()
				},
				"oLanguage": {
					"sProcessing": "正在获取数据，请稍后...",
					"sZeroRecords": "<img src='../../../img/order.png' class='noInfor'/><p class='text'>暂无信息</p>",
					"sInfoEmpty": "记录数为0",
					"sInfoFiltered": "(全部记录数 _MAX_ 条)",
					"sLengthMenu": "每页显示 _MENU_ 条",
					"sSearch": "搜索",
					"oPaginate": {
						"sFirst": "第一页",
						"sPrevious": "上一页",
						"sNext": "下一页",
						"sLast": "最后一页"
					}
				}
			})
			$('body').delegate('.change-btn','click',function(){
				var titleText, state = $(this).data('state');
				state == 1 ? $(this).data('ids', 3) : $(this).data('ids', 1)
				id = $(this).data('id'); ids = $(this).data('ids')
				$('.pre').data('id', id).text(state == 1 ? '保存' : '重新提交审核')
				if( id == 1){
					$('.modal_create').show().siblings().hide() && (titleText='申请组建医联体')
					$('.create_core').html('${hosName}').next().html('${hosLevel}')
					$("input[name = 'yltName']").val(' ')
					$('#modal_create [name="speciality"]').val(' ')
					$('#modal_create [name="introduce"]').val( ' ')
					$('.browser').hide()
				} else if(id == 2){
					$('.modal_add').show().siblings().hide() && (titleText='申请加入医联体')
					$('.pre').show()
					$.ajax({
						url: '/hospital/gainallylts',
						type: 'get',
						success: function (data){
							var ylts = data.ylts;
							var allhosp = ''
							for(var i in ylts){
								allhosp += "<option title="+ylts[i].id+">"+ylts[i].yltName+"</option>"
							}
							$('#hospitals').html(allhosp);
							joinyltId = $("#hospitals option:selected").attr('title');
							$("#hospitals").select2()
							selectChange(joinyltId)
						}
					});
				} else if( id == 3 ){
					$('.modal_create').show().siblings().hide() && (titleText = "医联体详情")
					var yltId = $(this).attr('data-yltId')
					$.ajax({
						type: 'get',
						url: '/hospital/yltdetailNew/'+yltId,
						success: function(d){
							$('.browser').hide()
							if(appjoin == 'true'){
								$('input[name = "yltName"]').attr('disabled', true)
								$('#modal_create [name="speciality"]').attr('disabled', true)
								$('#modal_create [name="introduce"]').attr('disabled', true)
								$('.actionAdd').hide().siblings().find('.diyCancel').hide()
								$('.pre').hide()
							}
							$("input[name = 'yltName']").val(d.alliance.yltName).attr('data-id', d.alliance.id)
							$('#modal_create [name="speciality"]').val(d.alliance.speciality)
							$('#modal_create [name="introduce"]').val(d.alliance.profile)
							$('.create_core').html(d.mainHos.displayName).next().html(d.mainHos.hosLevel)
							if(d.alliance.iconUrl){
									$('.browser').show().attr('data-id', d.alliance.iconUrl).find('img').attr('src', d.alliance.iconUrl)
							}
						}
					})
				}	
				$('#myModalLabel').html(titleText)
			})
			.delegate('input,textarea', 'focus', function(){
				var result=$(this).attr("value").replace(/(^\s*)|(\s*$)/g, "");  
        		$(this).attr("value",result);  
			})
			//select 改变
			 $("#hospitals").change(function(){
            	joinyltId = $("#hospitals option:selected").attr("title")
				selectChange(joinyltId)
            })
			function selectChange (yltId){
				$.ajax({
					url: '/hospital/gainhighlevelhos',
					type: 'post',
					data: {
						hosId: '${hosId}',
						allianceId: yltId
					},
					success: function (data){
						var hos = data.hospitals, hosStr = '', supers = data.hos, hosN = data.local_hos;
						for(var i in hos){
							hosStr += "<option title="+hos[i].id+">"+hos[i].displayName+"</option>"
						}
						$('#superiors').html(hosStr);
						$("#superiors").select2()
						$('.core').html(supers.displayName).next().html(supers.hosLevel)
						$('.hosNa').html(hosN.displayName).next().html(hosN.hosLevel)
						parentHosId = supers.id
					}
				})
			}
			// 添加，创建提交
			$('body').delegate('.pre','click', function(){
				var username = $.trim($('#modal_create [name="yltName"]').val()),
            		introduce = $.trim($('#modal_create [name="introduce"]').val()),
					speciality = $.trim($('#modal_create [name="speciality"]').val()),
					imgUrl = $('#fileBox_WU_FILE_0').data('id');
					allianceId = $('#modal_create [name="yltName"]').attr('data-id')
					ids == 1 || id == 1?
						createylt(username, introduce, imgUrl, speciality) : 
						( id == 2? 
							addylt(joinyltId, parentHosId):
							createylt(username, introduce, imgUrl, speciality, allianceId)
						)
			})
			//创建医联体
			function createylt(username, introduce, imgUrl, speciality, allianceId){
               	if(!username) return base.showTipE('请输入医联体名称'),false;  
				   $.ajax({
					url: '/hospital/orghoshealthalliance',
					type: 'post',
					data: { yltName : username, profile : introduce, iconUrl: imgUrl, speciality: speciality, allianceId: allianceId},
					success: function(data){
						 location.reload()
					},
					error: function(error){
						alert('网络错误')
					}
				})
            }
			//加入医联体
			function addylt (joinyltId, parentHosId){
				$.ajax({
					url: '/hospital/appjoinylt',
					type: 'post',
					data: { yltId : joinyltId, parentHosId : parentHosId},
					success: function(data){
						 location.reload()
					},
					error: function(error){
						alert('网络错误')
					}
				})
			}

			function retrieveData( sSource, aoData, fnCallback ) {
				aoData.push( { "name": "searchContent", "value":$('input[type="search"]').val()} );
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
		})
	</script>
</body>
</html>

