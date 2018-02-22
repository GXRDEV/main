<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>充值码管理</title>
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
                  <th>ID</th>
                  <th>充值码</th>
                  <th>金额</th>
                  <th>截止日期</th>
                  <th>使用状态</th>
                  <th>状态</th>
				  <th>创建时间</th>
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
	<div id="formeditor" style="display:none;">
		<h2>创建充值码</h2>
		<form class="formadd">
			<label>
				<span>金额：</span>
				<input type="text" name="money"style="width:8em;" />
			</label>
			<label>
				<span>数量：</span>
				<input type="text" name="num" style="width:8em;" placeholder="创建的充值码数量" />
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
	
	<script type="text/javascript">
		var _burl='/',_h = _burl,_id = '';
	</script>
	<script>
        	var oTable,ue;
            $(document).ready(function () {            	
            	oTable = $('.data-table').DataTable({
                    "bJQueryUI": true,"sDom": '<".example_processing"r><".tablelist"t><"F"flp>',
                    "iDisplayStart":0,"iDisplayLength":10,
                    "lengthMenu": [ 10, 15, 20, 25, 50, 100],
                    "bDestroy":true,"bRetrieve":true,"bStateSave":true,
                    "bServerSide": true,"fnServerData": retrieveData,
                    "sAjaxSource": "/system/gainchargecodes",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,     
					"fnCreatedRow": function (nRow, aData, iDataIndex) {
                    },
                    "oLanguage": {
                        "sProcessing": "正在获取数据，请稍后...",
                        "sLengthMenu": "每页显示 _MENU_ 条",
                        "sZeroRecords": "目前还没有生成充值码！",
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
                    "initComplete": function(settings, json) {}
                });
                $('body').delegate('.editbtn','click',function(){
                	var id = _id = this.getAttribute('id') || '';
                	showFillBody({});
                	return false;
                })
                .delegate('#cancel','click',function(){                
                	$('#formeditor').hide().siblings('.mainlist').show();
                })
                .delegate('#ok','click',function(){
	                seajs.use('view/base',function(base){
	                	saveForm(base,function(){
	                		$('#formeditor').hide().siblings('.mainlist').show();
	                	});
	                });
                })
            });
            function showFillBody(d){
				$('#formeditor').show().siblings('.mainlist').hide();
            }
            function saveForm(base,fn){
            	var money = $('#formeditor [name="money"]').val(),
            		num = $('#formeditor [name="num"]').val();
               	base.showTipIngA('正在创建.....').post('system/generatecodes',{
               		money:money,
               		num:num
               	},function(d){
               		if(d.status == 'error'){
               			base.showTipE('创建失败');
               		}else{
               			fn();
               			oTable.page('first').draw(false);
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
               	if($checks.size() > 3) return base.showTipE('选择科室超过限制'),false;
               	base.showTipIngA('正在保存').post('system/deptostand',{sdepids:standsv,depid:_id},function(d){
             		oTable.draw(false);
             		base.hideTip();
               	},function(){
               		base.showTipE('网络出错');
               	}).hideDialog('SetStandsModel');
            }
            function retrieveData( sSource, aoData, fnCallback ) {
			    aoData.push( { "name": "searchContent", "value":$('input[type="search"]').val()} );
			    aoData.push( { "name": "currorhis", "value":$('#filteropt a.curr').attr('data-currorhis')||''} );
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

