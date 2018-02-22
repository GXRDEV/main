<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
	<head lang="en">
		<meta charset="UTF-8">
		<title>医联体详情</title>
		<meta name="viewport" content="width=device-width,  user-scalable=no">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta content="telephone=no" name="format-detection" />
		<link rel="stylesheet" href="/css/bootstrap.min.css" />
		<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
		<link rel="stylesheet" href="/css/view/admin.css" />
		<link rel="stylesheet" href="/sea-modules/jqwidgets/styles/jqx.base.css" type="text/css" />
		<link rel="stylesheet" href="/sea-modules/select2/css/select2.min.css" type="text/css" />
		<script type="text/javascript" src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
	    <script type="text/javascript" src="/sea-modules/jqwidgets/jqxcore.js"></script>
	    <script type="text/javascript" src="/sea-modules/jqwidgets/jqxdata.js"></script>
	    <script type="text/javascript" src="/sea-modules/jqwidgets/jqxbuttons.js"></script>
	    <script type="text/javascript" src="/sea-modules/jqwidgets/jqxscrollbar.js"></script>
	    <script type="text/javascript" src="/sea-modules/jqwidgets/jqxdatatable.js"></script>
		<script type="text/javascript" src="/sea-modules/jqwidgets/jqxlistbox.js"></script>
		<script type="text/javascript" src="/sea-modules/jqwidgets/jqxdropdownlist.js"></script>
	    <script type="text/javascript" src="/sea-modules/jqwidgets/jqxtreegrid.js"></script>
	    <script type="text/javascript" src="/sea-modules/select2/js/select2.min.js"></script>
	   	<script type="text/javascript" src="/sea-modules/jqwidgets/jqxmenu.js"></script>
		<script src="/js/bootstrap.min.js"></script>
		<style scoped>
			.btns button{ border: 1px solid #0085d2; background: none; margin-top: 20px; padding: 3px 8px; margin-right: 10px; font-size: 12px; color: #0085d2; outline: none; border-radius: 2px}
			.btns .colbtn{ border: 1px solid #f8b00c; color: #f8b00c}
			.form-group{ padding: 3px 0}
			.form-group label{ display: inline-block;width: 25%}
			.form-group .controls{ width: 70%; display: inline-block }
			.hosLevel{ font-size: 12px; color: #0085d2; padding-left: 10px}
		</style>
</head>
<body class='default' style="padding: 20px">
    <div id="treeGrid">
    </div>
	<div class="btns">
		<button class="add" data-id="1" data-toggle="modal" data-target="#myModal">邀请加入医联体</button>
		<button class="colbtn" data-id="2" data-toggle="modal" data-target="#myModal">删除下级医院</button>
		<button class="colbtn" data-id="3" data-toggle="modal" data-target="#myModal">退出当前医联体</button>
	</div>
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel"></h4>
				</div>
				<div class="modal-body">
					<div class="addhos" style="display: none">
						<div class="form-group">
							<label>医联体名称</label>
							<div class="controls">
								<span class="yltName"></span>
							</div>
						</div>
						<div class="form-group">
							<label>上级医院</label>
							<div class="controls">
								<span class="lasthos"></span><span class="hosLevel"></span>
							</div>
						</div>
						<div class="form-group">
							<label for="name">邀请医院名称</label>
							<div class="controls">
								<select id="hospitals">
						    		<option>--选择医院--</option>
						    	</select>
							</div>
						</div>
						<div class="form-group">
							<label for="name">邀请医院级别</label>
							<div class="controls">
								<span class="level"></span>
							</div>
						</div>
						<div class="form-group" >
							<label for="name">邀请人</label>
							<div class="controls">
								<span class="invName"></span>
							</div>
						</div>
					</div>
					<div class="delhos" style="display: none">
						 <div class="form-group">
							<label>医联体名称</label>
							<div class="controls">
								<span class="yltName"></span>
							</div>
						</div>
						<div class="form-group">
							<label>当前医院</label>
							<div class="controls">
								<span class="lasthos"></span><span class="hosLevel"></span>
							</div>
						</div>
						<div class="form-group">
							<label>删除医院名称</label>
							<div class="controls">
								<select id="delHospitals">
						    		<option>--选择医院--</option>
						    	</select>
							</div>
						</div>
						<div class="form-group">
							<label>删除人</label>
							<div class="controls">
								<span class="invName"></span>
							</div>
						</div>
					</div>
					<div class="exithos" style="display: none">
						<div class="form-group">
							<label>医联体名称</label>
							<div class="controls">
								<span class="yltName"></span>
							</div>
						</div>
						<div class="form-group">
							<label>核心医院</label>
							<div class="controls">
								<span class="corehos"></span><span class="hosLevel"></span>
							</div>
						</div>
						<div class="form-group">
							<label>本医院</label>
							<div class="controls">
								<span class="lasthos"></span><span class="hosLevel"></span>
							</div>
						</div> 
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary submits">确认</button>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var row, parentId;
	  	$(document).ready(function(){  
		    var treeGrid;  
		    //初始化TreeGrid数据  
		    intTreeGridData();  
		});  
	  	function intTreeGridData(){
	  		 $.ajax({ 
	  		        type:"post",  
	  		        url:"/hospital/gainyltdetaildatas",
	  		        data:{yltId:${alliance.id}},  
	  		        dataType:"json",  
	  		        success:function (data){
						var corehos = data[0].HosName, coreLevel = data[0].HosLevel;
	  		        	var hospitals = eval(data);//转成数组对象 
	  		        	initTreeTable(hospitals, corehos, coreLevel);
	  		        }
	  		 });
	  	}
	  	function initTreeTable(hospitals, corehos, coreLevel){
	  		var parentId, id, hospId;
	  		var source =
            {
                dataType: "json",
                dataFields: [
                    { name: 'HospitalId', type: 'number' },
                    { name: 'HosName', type: 'string' },
                    { name: 'Area', type: 'string' },
                    { name: 'HosLevel', type: 'string' },
                    { name: 'JoinTime', type: 'date' },
                    { name: 'children', type: 'array' },
                    { name: 'expanded', type: 'bool' },
                    { name: 'HospitalLevel',type:'number'}
                ],
                hierarchy:
                {
                    root: 'children'
                },
                id: 'HospitalId',
                localData: hospitals,
				beforeLoadComplete: function(records){
					
				}
            };
            var dataAdapter = new $.jqx.dataAdapter(source);
           
            $("#treeGrid").jqxTreeGrid({
                width: 800,
                source: dataAdapter,
         		sortable: true,
                theme: 'energyblue',                
                columns: [
                  { text: '医院名称', dataField: 'HosName', width: 200 },
                  { text: '区域', dataField: 'Area', width: 200 },
                  { text: '医院级别', dataField: 'HosLevel', width: 200 },
                  { text: '加入时间', dataField: 'JoinTime', cellsFormat: 'd', width: 200 }
                ]
            });
			$('.btns').delegate('button', 'click', function(){
				//console.log(corehos)
				id = $(this).data('id');
				var titleStr, multiple;
				var obj = {}
				if( id== 1){
					titleStr = '邀请下级医院';
					$('.addhos').show().siblings().hide();
					obj['hosId'] = ${hos.id};
					selectHos('/system/gainhospitalsbycon', '#hospitals', false, obj )
				}else if(id == 2){
					titleStr = '删除下级医院';
					$('.delhos').show().siblings().hide()
					obj['yltId'] = ${alliance.id};
					selectHos('/hospital/gainlowermembers', '#delHospitals', true, obj)
				}else {
					titleStr = '退出当前医联体';
					$('.exithos').show().siblings().hide()
				}
				$('#myModalLabel').html(titleStr)
				$('.yltName').html('${alliance.yltName}')
				$('.invName').html('${docName}')
				$('.lasthos').html('${hos.displayName}').next().html('${hos.hosLevel}')
				$('.corehos').html(corehos).next().html(coreLevel)
				
				function selectHos (urls, selectId, multiples, obj){
					$.ajax({
						type: 'post',
						url: urls,
						data: obj,
						success: function(data){
							var hosp = data.hospitals || data.members;
							var allhosp = "";
							if(hosp.length && id == 1){
								for(var i in hosp){
									allhosp += "<option name="+hosp[i].hosLevel+" title="+hosp[i].id+">"+hosp[i].displayName+"</option>"
								}
							} else if (hosp.length && id == 2){
								for(var i in hosp){
									allhosp += "<option title="+hosp[i].id+">"+hosp[i].hosName+"</option>"
								}
							} else{
								allhosp += "<option>没有下级医院可供选择</option>"
							}
							$(selectId).html(allhosp)
							$(selectId).select2({ 
								multiple: multiples	
							})
							if( id == 1){
								$('.level').html($("#hospitals option:selected").attr("name"))
								hospId = $("#hospitals option:selected").attr("title")
							}else if( id == 2){
								var arr = []
								$.each($("#delHospitals").select2('data'), function (i, text){
									arr.push(text.title)
								})
								hospId = arr.join()
							}
							
							hospId ? $(".submits").attr("disabled",false) : $(".submits").attr("disabled",true)
						}
					})
				}
				$("#hospitals").change(function(){	
					$('.level').html($("#hospitals option:selected").attr("name"))
					hospId = $("#hospitals option:selected").attr("title")
				})
			})
			$.fn.modal.Constructor.prototype.enforceFocus = function () {};
			$('body').delegate('.submits', 'click', function(){
				id == 1 ? add() :( id == 2? del() : exits ())
			})
			addColor()
			//展开、收起时触发
			$('#treeGrid').on('rowExpand', function(){
				addColor()
			})
			$('#treeGrid').on('rowCollapse', function(){
				addColor()
			})
			//添加
			function add(){
				$.ajax({
					type: 'post',
					url: '/hospital/invitjoinylt',
					data: {
						yltId: ${alliance.id},
						hospitalId: hospId
					},
					success: function (data){
						location.reload()
					}
				})
			}
			// 删除
			function del(){
				var arr = []
				$.each($("#delHospitals").select2('data'), function (i, text){
					arr.push(text.title)
				})
				hospId = arr.join()
				$.ajax({
					type: 'post',
					url: '/hospital/dellowermembers',
					data: {
						memberIds: hospId
					},
					success: function(){
						location.reload()
					}
				})
			}
			// 退出
			function exits () {
				$.ajax({
					type: 'post',
					url: '/hospital/quitylt',
					data: {
						yltId: ${alliance.id}
					},
					success: function(){
						location.href="/hospital/hoshealthmanage";
					}
				})
			}
			//添加颜色
			function addColor(){
				$('tr').each(function(i, val){
					if($(this).data('key') == ${hos.id}){
						$(this).css('color', '#0085d2')
					}
				})
			}
	  	}
    </script>
</body>
</html>
