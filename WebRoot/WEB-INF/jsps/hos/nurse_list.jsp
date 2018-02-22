<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>护士管理</title>
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
			<button class="btn btn-content js-addinfo">新增</button>
		</div>
        <div class="widget-box">
          <div class="widget-content nopadding">
            <table class="table table-bordered data-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>姓名</th>
                  <th>电话</th>
                  <th>创建时间</th>
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
		var _burl='/',_h = _burl;
	</script>
	<script>
        	var oTable;
            $(document).ready(function () {            	
            	oTable = $('.data-table').DataTable({	
                    "bJQueryUI": true,"sDom": '<".example_processing"r><".tablelist"t><"F"flp>',
                    "iDisplayStart":0,"iDisplayLength":10,
                    "lengthMenu": [ 10, 15, 20, 25, 50, 100],
                    "bDestroy":true,"bRetrieve":true,"bStateSave":true,
                    "bServerSide": true,"fnServerData": retrieveData,
                    "sAjaxSource": "/hospital/gainnurses",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,     
								"fnCreatedRow": function (nRow, aData, iDataIndex) {
                    	$('td:eq(4)', nRow).html('<a href="" id="'+ aData[0] +'" class="resetpsd linebtn">重置密码</a>');
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
								}
              });
				$('body').delegate('.js-addinfo','click',function(){
                	var id = this.getAttribute('id') || '';
                	seajs.use('view/base',function(base){
                		base.showDialog({
												id:'FormModels',
												title: id ? '编辑护士' : '新增护士',
												cls:'modal2-600',
												text: $('#formtemp').html(),
												ok: $.proxy(saveForm,base)
											});
                	});
                }).delegate('.resetpsd','click',function(){
                	var id = this.getAttribute('id') || '';
                	if(confirm('确定要重置此医生的密码的初使密码？')){
                		seajs.use('view/base',function(base){
                			base.showTipIngA('正在初使化').post('hospital/passreset',{uid:id},function(d){
			               		base.showTipS('初使化成功');
			               	},function(){
			               		base.showTipE('网络出错');
			               	}).hideDialog('FormModels');
                		});
                	}
                	return false;
                });
            });
            function saveForm(){
            	var tel = $('#FormModels [name="telphone"]').val(),
            		nam = $('#FormModels [name="username"]').val(),
            		base = this;
               	if(!base.valideTel(tel)) return base.showTipE('请输入正确的电话格式'),false;                	
               	if(!nam) return base.showTipE('请输入护士姓名'),false;
               	base.showTipIngA('正在保存').post('hospital/addnurse',{telphone:tel,username:nam},function(d){
               		if(d.status == 'error'){
               			base.showTipE('此护士已注册');
               		}else{
               			oTable.page('first').draw(false);
               			base.hideTip();
               		}
               	},function(){
               		base.showTipE('网络出错');
               	}).hideDialog('FormModels');
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
	</script>
	<script type="text/html" id="formtemp">
		<form class="formadd">
			<label>
				<span>电话：</span>
				<input type="text" name="telphone" placeholder="用作登录用户名"/>
			</label>
			<label>
				<span>姓名：</span>
				<input type="text" name="username"/>
			</label>
		</form>
	</script>
</body>
</html>

