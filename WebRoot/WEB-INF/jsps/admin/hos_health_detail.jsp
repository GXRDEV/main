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
	    <script type="text/javascript" src="/sea-modules/jqwidgets/jqxtreegrid.js"></script>
	    <script type="text/javascript" src="/sea-modules/select2/js/select2.min.js"></script>
	   	<script type="text/javascript" src="/sea-modules/jqwidgets/jqxmenu.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){  
				var treeGrid;  
				//初始化TreeGrid数据  
				intTreeGridData();  
			});  
			function intTreeGridData(){
				$.ajax({ 
						type:"post",  
						url:"/system/hoshealthstruts_new",
						data:{yltId:${hha.id}},  
						dataType:"json",  
						success:function(data){
							var hospitals = eval(data);//转成数组对象 
							initTreeTable(hospitals);
						}
				});
			}
			function initTreeTable(hospitals){
				var parentId,hospId;
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
					localData: hospitals
				};
				var dataAdapter = new $.jqx.dataAdapter(source);
				//console.log(hospitals.length)
				if (hospitals.length == 0) {
					$.ajax({ 
						type:"post",  
						url:"/system/gainhospitalsbycon",
						success:function(data){
							var hosp = data.hospitals;
							var allhosp = "";
							if(hosp!=[]){
								for(var i in hosp){
									allhosp += "<option value="+hosp[i].displayName+" title="+hosp[i].id+">"+hosp[i].displayName+"</option>"
								}
							}
							$("#hospitals").html(allhosp)
							hospId = $("#hospitals option:selected").attr("title")
							hospId ? $(".btn").attr("disabled",false) : $(".btn").attr("disabled",true)
							$("select").select2()
						}
					});
				}
				$("#treeGrid").jqxTreeGrid(
				{
					width: 720,
					source: dataAdapter,
					sortable: true,
					editable: true,
					theme: 'energyblue',                
					columns: [
					{ text: '医院名称', dataField: 'HosName', width: 200 },
					{ text: '区域', dataField: 'Area', width: 200 },
					{ text: '医院级别', dataField: 'HosLevel', width: 200 },
					{ text: '加入时间', dataField: 'JoinTime', cellsFormat: 'd', width: 120 }
					]
				});
				// create context menu
				var contextMenu = $("#Menu").jqxMenu({ width: 100, height: 28, autoOpenPopup: false, mode: 'popup' });
				$("#treeGrid").on('contextmenu', function () {
					return false;
				});
				
				$("#treeGrid").on('rowClick', function (event) {
					var args = event.args;
					var row = args.row;
					parentId = row.HospitalId;
					$.ajax({ 
						type:"post",  
						url:"/system/gainhospitalsbycon",
						data:{ hosId: row.HospitalId },
						success:function(data){
							var hosp = data.hospitals;
							var allhosp = "";
							if(hosp!=[]){
								for(var i in hosp){
									allhosp += "<option value="+hosp[i].displayName+" title="+hosp[i].id+">"+hosp[i].displayName+"</option>"
								}
							}
							$("#hospitals").html(allhosp)
							hospId = $("#hospitals option:selected").attr("title")
							$("#hospitals").select2()
							parentId && hospId ? $(".btn").attr("disabled",false) : $(".btn").attr("disabled",true)
						}
					});
					if(row.HospitalLevel=='16'||row.HospitalLevel=='17'||row.HospitalLevel=='18'){
						contextMenu.jqxMenu('close', parseInt(event.args.originalEvent.clientX) + 5 + scrollLeft, parseInt(event.args.originalEvent.clientY) + 5 + scrollTop);
					}else{
						if (args.originalEvent.button == 2) {
							var scrollTop = $(window).scrollTop();
							var scrollLeft = $(window).scrollLeft();
							contextMenu.jqxMenu('open', parseInt(event.args.originalEvent.clientX) + 5 + scrollLeft, parseInt(event.args.originalEvent.clientY) + 5 + scrollTop);
							return false;
						}else{
							contextMenu.jqxMenu('close', parseInt(event.args.originalEvent.clientX) + 5 + scrollLeft, parseInt(event.args.originalEvent.clientY) + 5 + scrollTop);
						}
					}
					
					
					
				});
				//select变值
				$("#hospitals").change(function(){
					hospId = $("#hospitals option:selected").attr("title")
					
				})
				//点击提交
				$(".btn").on("click",function () {
					$.ajax({ 
						type:"post",  
						url:"/system/saveHosHealthMember",
						data:{
							allianceId:${hha.id},
							parentHosId:parentId,
							hospitalId:hospId
						},  
						success:function(data){
							location.reload()
						}
					});
				})
				$("#Menu").on('click', function (event) {
					var args = event.args;
					var selection = $("#treeGrid").jqxTreeGrid('getSelection');
					var rowid = selection[0].uid
					if ($.trim($(args).text()) == "新增下级医院") {
						$("#treeGrid").jqxTreeGrid('beginRowEdit', rowid);
					} else {
						$("#treeGrid").jqxTreeGrid('deleteRow', rowid);
					}
					$.ajax({ 
						type:"post",  
						url:"/system/delHosHealthMember",
						data:{
							hosId:rowid,
							allianceId:${hha.id}
						},  
						success:function(data){}
					});
				});
			}
		</script>
		<style>
			.default{ padding: 20px}
			.tab-title{ font-size: 1.2em}
		</style>
</head>
<body class='default'>
	<p class="tab-title">
		医联体医院结构
	</p>
    <div id="treeGrid">
    </div>
    <div id='Menu' style="display:none">
        <ul>
            <li>删除该医院</li>
        </ul>
    </div>
    <form class="row-fluid form-horizontal"  method="post" id="myform">
    	<input type="hidden" id="parentHosId" name="parentHosId" value=""/>
    	<input type="hidden" id="allianceId" name="allianceId" value="${hha.id}" />
		<div class="userinfo">
			<div class="header clearfix baseformaction" style="padding: 20px 0 10px; font-size: 1.2em">
				添加医院
			</div>
			<div class="bodyer">
				<div class="section">
					<div class="bodyer clearfix">
						 <div class="control-group">
						    <label style="padding: 0 10px 0 20px; display: inline-block">选择医院：</label>
						    <div style="display: inline-block">
						    	<select id="hospitals" name="hospitalId">
						    		<option>--选择医院--</option>
						    	</select>
								<button type="button" class="btn btn-primary" name="sbform" disabled="true" style="margin-left: 10px">保存</button>
			    			</div>
			 			 </div>
					</div>					
				</div>
			</div>
		</div>
	</form>
</body>
</html>
