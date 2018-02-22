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
	<link rel="stylesheet" href="/css/view/videolist.css" />
	<link rel="stylesheet" href="/css/view/videolistwidthbtn.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/admin.css" />
	<style>
		.deps{
			width: 400px;
			margin: 0;
			padding: 0 10px;
			cursor: pointer;
			overflow: hidden;
			text-overflow:ellipsis;
			white-space: nowrap;
		}
	</style>
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
                  <th>ID</th>
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
	<!-- <div id="formeditor" style="display:none;"></div> -->
	<div id="formeditor" class="baseinfoIndex" style="display:none;">
		<h2>{title}</h2>
		<form class="formadd">
			<label>
				<span>科室名称：</span>
				<input type="text" name="displayName" value="{displayName}"/>
			</label>
			<label>
				<span>科室ID：</span>
				<input type="text" name="localDepId" style="width:8em;" placeholder="本地科室ID" value="{localDepId}"/>
			</label>
			<label style="display:block;" id="kesicons">
				<span>科室图标：</span>
				<div class="imgupload">
					<div class="thumb prelative">
						<img id="logourl" src="" />
						<div id="addHosIcon"><i class="iconfont">&#xe63f;</i></div>
					</div>
					<input type="hidden" name="logourl" value=""/>
				</div>
			</label>
			<label>
				<span>科室介绍：</span>
				<script id="introduction" name="introduction" type="text/plain"></script>
			</label>
		</form>
		<div class="form-action">
			<button class="btn btn-primary" id="ok">确定</button>
			<button class="btn" id="cancel">取消</button>
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
                    "sAjaxSource": "/hospital/gaindeparts",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true, 
					"columnDefs": [
						{
							"targets": 3,
							"createdCell": function(td, cellData, rowData, row, col){
								var str = "<p class='deps tooltip-test' data-toggle='tooltip' title="+ cellData +">"+ cellData +"</p>"
								$(td).html(str)
							}
						}
					],
					"fnCreatedRow": function (nRow, aData, iDataIndex) {
						var opts='';
						opts+='<a href="" id="'+ aData[0] +'" data-idata="'+ iDataIndex +'" class="editbtn linebtn">编辑</a>';
						opts+='<a style="margin-left:10px;" href="" id="'+ aData[0] +'" class="setbtn linebtn">设置标准科室</a>';
						if(aData[4]=='已上线'){
							opts+='<a style="margin-left:10px;" href="" did="'+ aData[0] +'" sval="0" class="chgestat linebtn">下线</a>';
						}else if(aData[4]=='已下线'){
							opts+='<a style="margin-left:10px;" href="" did="'+ aData[0] +'" sval="1" class="chgestat linebtn">上线</a>';
						}
                    	$('td:eq(5)', nRow).html(opts);
                    },
                    "oLanguage": {
                        "sProcessing": "正在获取数据，请稍后...",
                        "sLengthMenu": "每页显示 _MENU_ 条",
                        "sZeroRecords": "目前您医院还没有科室，赶快点击右上角的”新增”按钮添加科室吧！",
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
                    	var tabs='<a href="#" class="stalist curr" order-status="1">上线科室</a>';
                    	tabs+='<a href="#" class="stalist" order-status="0">下线科室</a>';
                    	$('#filteropt').html(tabs);
                    }
                });
            	$('body').delegate('#filteropt a','click',function(){
                	var opt = $(this).attr('data-currorhis');
                	$(this).addClass('curr').siblings().removeClass('curr');
                	oTable.page('first').draw(false);
                	return false;
                }).delegate('.editbtn','click',function(){
                	var id = _id = this.getAttribute('id') || '';
                	!id ? 
                	showFillBody({}) :
					seajs.use('view/base',function(base){
	                	base.showTipIngA('正在执行').get('hospital/gaindepartdetail',{depid:id},function(d){
	                		showFillBody(d.dep);
	                		base.hideTip();
	                	});
	                });
                	return false;
                })
                .delegate('#cancel','click',function(){                
                	$('#formeditor').hide().siblings('.mainlist').show();
                }) 
                .delegate('.chgestat','click',function(){      
                	var depId=$(this).attr("did"),sval=$(this).attr("sval");
                	$.post('/hospital/changeDepStatus',{depId:depId,sval:sval},function(data){
                		oTable.page('first').draw(false);
                	});
                })
                .delegate('#ok','click',function(){
	                seajs.use('view/base',function(base){
	                	saveForm(base,function(){
	                		$('#formeditor').hide().siblings('.mainlist').show();
	                	});
	                });
                })
                .delegate('.setbtn','click',function(){
                	var id = _id = this.getAttribute('id') || '';
                	seajs.use('view/base',function(base){
                		base.showDialog({
							id:'SetStandsModel',
							title:'设置标准科室',
							cls:'modal2-lg',
							ok: $.proxy(saveStand,base)
						}).get('system/gainstandsbydep',{depid:id},function(d){
							$('#SetStandsModel .modal2-body').html(function(){
								var labels = '',sids = d.sids || [];
								for(var i=0,l = stands.length; i < l;i++){
									labels += '<label class="checkbox standslist"><input type="checkbox" name="standslist" '
									+ ($.inArray(stands[i][0],sids) != -1 ? 'checked' : (sids.length >= 3 ? 'disabled' : '') ) 
									+' value="'+ stands[i][0] +'"><span>'+ stands[i][1] +'</span></label>';
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
                }).delegate('#SetStandsModel .standslist :input','change',function(){
                	var $checks = $('#SetStandsModel [name="standslist"]:checked');
                	$('#SetStandsModel [name="standslist"]').each(function(){
            			this.removeAttribute('disabled');
            		});
                	/* if($checks.size() >= 3){
                		$('#SetStandsModel [name="standslist"]').each(function(){
                			!this.checked && this.setAttribute('disabled',true);
                		});
                	}else{
                		$('#SetStandsModel [name="standslist"]').each(function(){
                			this.removeAttribute('disabled');
                		});
                	} */
                });
            });
            function showFillBody(d){
				$('#formeditor h2').text(d.id ? '编辑科室' : '新增科室');
				$('#formeditor [name="displayName"]').val(d.displayName || '');
				$('#formeditor [name="localDepId"]').val(d.localDepId || '');
				$('#formeditor [name="logourl"]').val(d.depIcon);
				$('#formeditor #logourl').attr('src',d.depIcon)
				$('#formeditor').show().siblings('.mainlist').hide();
				!$('#addHosIcon .webuploader-pick').size() && seajs.use('view/webupload',function(upload){
					$('#addHosIcon').size() && $('#addHosIcon').Uploader({
		        		server: _burl + 'doctor/uploadLocalFile',
		        		formData: {},
		        		backdata:'frazior',
		        		single:true,
		        		pick:{multiple: false},
		        		thumb: {width: 100,height: 100},
		        		beforecreat:function(){$('#formeditor .parentFileBox').show();},
		        		success:function(liobj,obj){
		        			 $('#logourl').attr('src',$('#formeditor .parentFileBox .viewThumb img').attr('src'));   
		        			 $('.imgupload [name="logourl"]').val(obj['urlpath']);
		        			 $('#formeditor .parentFileBox').hide();
		        		}
		        	});
				});
				!ue && (ue = UE.getEditor('introduction'));	
				ue.ready(function() {
					ue.setContent(d.introduction || '');
				});
            }
            function saveForm(base,fn){
            	var nam = $('#formeditor [name="displayName"]').val(),
            		ldp = $('#formeditor [name="localDepId"]').val(),
            		introduction = ue.getContent(),
            		introductionTxt = ue.getContentTxt(),
            		depIcon=$('#formeditor [name="logourl"]').val();
               	if(!nam) return base.showTipE('请输入科室'),false;
               	base.showTipIngA('正在保存').post('hospital/saveorupdatedepart',{
               		displayName:nam,
               		localDepId:ldp,
               		depid:_id,
               		introduction:introduction,
               		introductionTxt:introductionTxt,
               		depIcon:depIcon
               	},function(d){
               		if(d.status == 'error'){
               			base.showTipE('此科室已存在');
               		}else{
               			fn();
               			_id ? oTable.draw(false) : oTable.page('first').draw(false);
               			base.hideTip();
               		}
               	},function(){
               		base.showTipE('网络出错');
               	});
            }
            function saveStand(){
            	var $checks = $('#SetStandsModel [name="standslist"]:checked');
            	var standsv = (function(checkeds){
            		var _v = [];
            		checkeds.each(function(){
            			_v.push(this.value);
            		});
            		return _v.join(',')
            	})($checks),base = this;
               	//if(!standsv) return base.showTipE('请选择科室'),false;               	
               //	if($checks.size() > 3) return base.showTipE('选择科室超过限制'),false;
               	base.showTipIngA('正在保存').post('system/deptostand',{sdepids:standsv,depid:_id},function(d){
             		oTable.draw(false);
             		base.hideTip();
               	},function(){
               		base.showTipE('网络出错');
               	}).hideDialog('SetStandsModel');
            }
            function retrieveData( sSource, aoData, fnCallback ) {
			    aoData.push( { "name": "search", "value":$('input[type="search"]').val()} );
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
	<script>
		var stands = [];
		<c:forEach items="${stands}" var="o">
			stands.push([+'${o.id }','${o.displayName}']);
		</c:forEach>
	</script>
</body>
</html>

