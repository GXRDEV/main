<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>远程会诊订单列表</title>
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
		.opsel{
			display: inline-block;
			width:100px;
		    background-color: transparent;
		    border-radius: 1px;
		    padding: 0 10px;
		    color: #FA8945;
		    margin-left:5px;
		    line-height: 1.5em;
		    height:25px;
		    border: 1px solid #FA8945;
		}
	</style>
</head>
<body>
<div class="mainlist">
  <div class="container-fluid">
    <div class="row-fluid">
      <div class="span12">
        <div class="widget-box" style="margin-top:30px;">
          <div class="widget-content nopadding">
            <table class="table table-bordered data-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>医院科室</th>
                  <th>分配医生</th>
                  <th>状态</th>  
                  <th>预约时间</th>
                  <th>距离当前</th>
                  <th>患者姓名</th>
                  <th>患者年龄</th>
                  <th>患者性别</th>
				  <th>电话</th>
				  <th>所选专家</th>
				  <th>专家医院</th>
				  <th>专家科室</th>
				  <th>支付状态</th>
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
                    "bJQueryUI": true,"sDom": '<".example_processing"r><".tablelist"t><"F"<"#filteropt">flp>',
                    "iDisplayStart":0,"iDisplayLength":10,
                    "lengthMenu": [ 10, 15, 20, 25, 50, 100],
                    "bDestroy":true,"bRetrieve":true,"bStateSave":true,
                    "bServerSide": true,"fnServerData": retrieveData,
                    "sAjaxSource": "/hospital/gainorders",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,
                    "aoColumnDefs":[{"bSortable":false,"aTargets":[0]}], 
                    "aaSorting": [
                                  [ 1, "asc" ],
                                  [ 2, "desc"]
                    ],
                    "columns":[
						{"visible": false},null,null,null,null,null,null,null,null,null,null,null,null,null,null
                    ],       
					"fnCreatedRow": function (nRow, aData, iDataIndex) {
						var path= '/doctor/orderdetail/' + aData[0]+"/1",_val=aData[3],_className = '',_iconfont = '';
						var ops='<a id="'+ aData[0] +'" href="javascript:void(0)" class="editTool linebtn">分配医生</a>';
						var sel='<select class="opsel" oid="'+aData[0]+'"><option value="">标记状态</option><option value="1">就诊结束</option><option value="2">需二次就诊</option><option value="3">需三次就诊</option></select>';
						if(_val.indexOf('准备就绪') != -1){						
							_className = 'zbjxcls',
							_iconfont = '&#xe611;';
							ops+=sel;
						}
						if(_val.indexOf('待就诊') != -1){						
							_className = 'djzcls',
							_iconfont = '&#xe612;';
						}
						if(_val.indexOf('正在进行') != -1){						
							_className = 'zzjxcls',
							_iconfont = '&#xe613;';
							ops+=sel;
						}
						if(_val.indexOf('就诊完成') != -1){						
							_className = 'ywccls',
							_iconfont = '&#xe610;';
						}
						ops=ops+'<a id="'+ aData[0] +'" href="javascript:void(0)" class="del linebtn">删除订单</a>';
						$(nRow).addClass(_className);
						$('td:eq(2)', nRow).html('<span class="hasiconfont"><i class="iconfont">'+ _iconfont +'</i>'+ _val +'</span>');
                        $('td:eq(13)', nRow).html(ops);
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
					    $('#filteropt').html('<a href="#" class="stalist curr" data-currorhis="current">当前订单</a><a href="#" class="stalist" data-currorhis="history">历史订单</a>');
					}
                });
                $('body').delegate('#filteropt a','click',function(){
                	var opt = $(this).attr('data-currorhis');
                	$(this).addClass('curr').siblings().removeClass('curr');
                	oTable.page('first').draw(false);
                	return false;
                }).delegate('.del','click',function(){
                	if(confirm("确定要删除该订单么！！?")){
                		$.post("/hospital/delorder",{oid:$(this).attr('id')},function(){
                    		oTable.page('first').draw(false);
                        	return false;
                    	});
                	}
                }).delegate('.editTool','click',function(){
                	var id = this.getAttribute('id') || '';
                	seajs.use('view/base',function(base){
                		base.showDialog({
							id:'SelectDocModel',
							title: '选择医生',
							cls:'modal2-600',
							text: $('#formtemp').html(),
							ok: $.proxy(saveForm,base)
						}).post('hospital/gainrelatevedocs',{oid:id},function(d){	
							var docs = d.docs || [];							
							$('#SelectDocModel .modal2-body tbody').html(function(){
								var hts = ''
								$.each(docs,function(i,o){
									hts += '<tr><td>'+ o.specialId +'</td><td>'+ o.specialName +'</td><td>'+ o.depName +'</td><td>'+ o.telphone +'</td><td><input type="radio" name="radiolist" class="tablechk" data-oid="'+ id +'" value="'+ o.specialId +'"/></td></tr>';
								});
								return hts || '<div class="noresult"><img src="'+ window.location.origin +'/img/mobile/kbzs/icon_noresult@2x.png" style="width:30px" alt=""/></div>';
							});
						},function(){
							if(_listSign != sign) return false;
							base.showTipE('加载失败').hideDialog('FormModelEdit');
						});
                	});
                })
		    	.delegate('#SelectDocModel tbody tr','click',function(event){
		    		var chbx = $(this).find('.tablechk')[0];
		    		event.target.type != 'radio' && (chbx.checked = !chbx.checked);
		    	});
            });
            function saveForm(){
            	var base = this;
            	var radio = $('#SelectDocModel tbody tr').find('.tablechk:checked'),oid = radio.attr('data-oid') || '',docid = radio.val() || '';
            	docid && oid ? base.showTipIngA('正在处理分配').post('hospital/distribute',{oid:oid,docid:docid},function(d){
               		if(d.status == 'error'){
               			base.showTipE('已分配给此医生');
               		}else{
               			oTable.draw(false);
               			base.hideTip();
               		}
               	},function(){
               		base.showTipE('网络出错');
               	}).hideDialog('SelectDocModel') : base.showTipE('请选择要分配给哪位医生');
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
            
            $('body').delegate('.opsel','change',function(){
            	var oid=$(this).attr("oid"),val=$(this).val(),base = this;
            	val&&$.post('/hospital/updateorderstatus',{oid:oid,val:val},function(d){
            		oTable.draw(false);
            	});
            })
	</script>
	<script type="text/html" id="formtemp">
		<table class="table">
			<thead>
				<tr><td>ID</td><td>姓名</td><td>科室</td><td>电话</td><td>操作</td></tr>
			</thead>
			<tbody><tr><td colspan="5"><div class="loadings"><img alt="" style="" src="/img/loading/31.gif"/></div></td></tr></tbody>
		</table>
	</script>
</body>
</html>

