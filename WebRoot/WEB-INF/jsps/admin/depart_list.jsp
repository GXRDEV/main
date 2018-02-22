<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>科室管理</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/css/matrix-style2.css" />
	<link rel="stylesheet" href="/css/matrix-media.css" />
	<link href="/font-awesome/css/font-awesome.css" rel="stylesheet" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/videolist.css" />
	<link rel="stylesheet" href="/css/view/videolistwidthbtn.css" />
</head>
<body>
	<div class="mainlist">
	  <div class="container-fluid">
	    <div class="row-fluid">
	      <div class="span12">
	        <div class="addarea">
				<button class="btn btn-content js-addinfo editbtn">新增</button>
			</div>
	        <div class="widget-box">
	          <div class="widget-content nopadding">
	            <table class="table table-bordered data-table">
	              <thead>
	                <tr>
	                  <th>医院ID</th>
	                  <th>ID</th>
	                  <th>医院</th>
	                  <th>科室名称</th>
	                  <th>本地科室ID</th>
	                  <th>标准科室</th>
	                  <th>状态</th>
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
            	oTable = $('.data-table').DataTable({
                    "bJQueryUI": true,"sDom": '<".example_processing"r><".tablelist"t><"F"<"#filteropt">flp>',
                    "iDisplayStart":0,"iDisplayLength":10,
                    "lengthMenu": [ 10, 15, 20, 25, 50, 100],
                    "bDestroy":true,"bRetrieve":true,"bStateSave":true,
                    "bServerSide": true,"fnServerData": retrieveData,
                    "sAjaxSource": "/system/gaindeparts",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,   
                    "columns":[
                          		{"visible": false},null,null,null,null,null,null,null
                    ], 
					"fnCreatedRow": function (nRow, aData, iDataIndex) {
						var xops='';
						if(aData[6].indexOf('已上线') != -1){
							$('td:eq(5)', nRow).html(aData[6].replace('已上线','<span class="passed">已上线</span>'));
							xops='<a href="javascript:void(0);" class="linebtn onandoff" depid='+aData[1]+'>下线</a>';
						}else if(aData[6].indexOf('已下线') != -1){	
							$('td:eq(5)', nRow).html(aData[6].replace('已下线','<span class="nopassed" >已下线</span>'));	
							xops='<a href="javascript:void(0);" class="linebtn onandoff" depid='+aData[1]+'>上线</a>';
						}
                    	$('td:eq(6)', nRow).html('<a href="" id="'+ aData[1] +'" data-idata="'+ iDataIndex +'" class="editbtn linebtn">编辑</a><a style="margin-left:10px;" href="" id="'+ aData[1] +'" class="setbtn linebtn">设置标准科室</a>'+xops);
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
                    	$('#filteropt').html('<a href="#" class="stalist curr" data-currorhis="ondep">上线科室</a><a href="#" class="stalist" data-currorhis="offdep">下线科室</a>');
                    }
                });
                $('body').delegate('.editbtn','click',function(){
                	var id = _id = this.getAttribute('id') || '',idx = this.getAttribute('data-idata') || '';
                	var o = idx ? oTable.data()[idx] : ['','','','','','',''];
                	seajs.use('view/base',function(base){
                		base.showDialog({
							id:'FormModels',
							title: id ? '编辑科室' : '新增科室',
							cls:'modal2-600',
							text: (function(h){
								h = h.replace('{displayName}',o[3]);
								h = h.replace('{localDepId}',o[4]);
								h = h.replace('data-hos="'+ o[0] +'"','selected');
								return h;
							})($('#formtemp').html()),
							ok: $.proxy(saveForm,base)
						});
						$('#FormModels').on('hide.bs.modal', function (e) {
				    		$('body').removeClass('modal2-open')
				    	}); 
                	});
                	return false;
                }).delegate('.setbtn','click',function(){
                	var id = _id = this.getAttribute('id') || '';
                	seajs.use('view/base',function(base){
                		base.showDialog({
							id:'SetStandsModel',
							title:'设置标准科室',
							cls:'modal2-600',
							ok: $.proxy(saveStand,base)
						}).get('system/gainstandsbydep',{depid:id},function(d){
							$('#SetStandsModel .modal2-body').html(function(){
								var labels = '';
								for(var i=0,l = stands.length; i < l;i++){
									labels += '<label class="checkbox standslist"><input type="checkbox" name="standslist" '+ ($.inArray(stands[i][0],d.sids || []) != -1 ? 'checked' : '' ) +' value="'+ stands[i][0] +'"><span>'+ stands[i][1] +'</span></label>';
								}
								return labels;
							});
						},function(){
							$('#SetStandsModel .modal2-body').html('');
						});
						$('#SetStandsModel').on('hide.bs.modal', function (e) {
				    		$('body').removeClass('modal2-open')
				    	}); 
                	});
                	return false;
                }).delegate('#filteropt a','click',function(){
                	var opt = $(this).attr('data-currorhis');
                	$(this).addClass('curr').siblings().removeClass('curr');
                	oTable.page('first').draw(false);
                	return false;
                }).delegate('.onandoff','click',function(){
                	var depid=$(this).attr("depid");
                	$.post('/system/onandoffdep',{depid:depid},function(data){
                		oTable.page('first').draw(false);
                	})
                });
            	oTable.page('first').draw(false);
            });
            function saveForm(){
            	var nam = $('#FormModels [name="displayName"]').val(),
            		ldp = $('#FormModels [name="localDepId"]').val(),
            		base = this;
               	if(!nam) return base.showTipE('请输入科室'),false;
               	base.showTipIngA('正在保存').post('system/saveorupdatedepart',{displayName:nam,localDepartId:ldp,hosid:'${hosid}',depid:_id},function(d){
               		if(d.status == 'error'){
               			base.showTipE('此科室已存在');
               		}else{
               			_id ? oTable.draw(false) : oTable.page('first').draw(false);
               			base.showTipS('科室'+ (_id ? '修改' : '添加') +'成功');
               		}
               	},function(){
               		base.showTipE('网络出错');
               	}).hideDialog('FormModels');
            }
            function saveStand(){
            	var standsv = (function(checkeds){
            		var _v = [];
            		checkeds.each(function(){
            			_v.push(this.value);
            		});
            		return _v.join(',')
            	})($('#SetStandsModel [name="standslist"]:checked')),base = this;
               	//if(!standsv) return base.showTipE('请选择科室'),false;
               	base.showTipIngA('正在保存').post('system/deptostand',{sdepids:standsv,depid:_id},function(d){
             		oTable.draw(false);
             		base.hideTip();
               	},function(){
               		base.showTipE('网络出错');
               	}).hideDialog('SetStandsModel');
            }
            function retrieveData( sSource, aoData, fnCallback ) {
			    aoData.push( { "name": "searchContent", "value":$('input[type="search"]').val()} );
			    aoData.push( { "name": "hosid", "value":'${hosid}'});
			    aoData.push( { "name": "currorhis", "value":$('#filteropt a.curr').attr('data-currorhis')||'ondep'} );
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
	<script type="text/html" id="formtemp">
		<form class="formadd">
			<label>
				<span>科室名称:</span>
				<input type="text" name="displayName" value="{displayName}"/>
			</label>
			<label>
				<span>本地科室ID：</span>
				<input type="text" name="localDepId" value="{localDepId}"/>
			</label>
		</form>
	</script>
	<script>
		var departs = [],stands = [];
		<c:forEach items="${stands}" var="o">
			stands.push([+'${o.id }','${o.displayName}']);
		</c:forEach>
	</script>
</body>
</html>

