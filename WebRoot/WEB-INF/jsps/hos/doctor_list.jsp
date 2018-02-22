<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>医生管理</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/css/matrix-style2.css" />
	<link rel="stylesheet" href="/css/matrix-media.css" />
	<link href="/font-awesome/css/font-awesome.css" rel="stylesheet" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/videolist.css" />
	<link rel="stylesheet" href="/css/view/videolistwidthbtn.css" />
	<link rel="stylesheet" href="/css/view/admin.css" />
	<style>
		.addep{color:red;}
		.table-line{border: 1px solid #ddd;}
		.table-line td{text-align:center;}
		.table-line td input[type]{margin:0!important;}
		.baseinfoIndex .imgupload .thumb{overflow:hidden;}
		#formeditor input[readonly]{border:0;background:transparent;}
	</style>
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
	                  <th>ID</th><th>科室</th><th>姓名</th><th>电话</th><th>创建时间</th><th>操作</th>
	                </tr>
	              </thead> 
	            </table>
	          </div>
	        </div>
	      </div>
	    </div>
	  </div>
	</div>
	<div id="formeditor" class="baseinfoIndex" style="display:none;">
		<h2>{title}</h2>
		<form class="formadd">
			<div class="clearfix">
				<label id="kesicons" style="margin-right:1em;float:left;">
					<span>医生头像：</span>
					<div class="imgupload">
						<div class="thumb prelative">
							<img id="logourl" src="" />
							<div id="addHosIcon"><i class="iconfont">&#xe63f;</i></div>
						</div>
						<input type="hidden" name="headImageUrl" value=""/>
					</div>
				</label>	
				<label id="kesicons" style="margin-right:1em;float:left;">
					<span>医生执照/工牌：</span>
					<div class="imgupload">
						<div class="thumb prelative">
							<img id="logourl_ZZ" src="" />
							<div id="addZZIcon"><i class="iconfont">&#xe63f;</i></div>
						</div>
						<input type="hidden" name="badgeUrl" value=""/>
					</div>
				</label>
			</div>
			<label>
				<span>医生姓名：</span>
				<input type="text" name="username" value=""/>
			</label>
			<label>
				<span>医生电话：</span>
				<input type="text" name="telphone" value=""/>
			</label>
			<!-- <label>
				<span>是否名医：</span>
				<span class="checkbox"><input type="checkbox" name="famousDoctor" value="1"/></span>
			</label> -->
			<div></div>
			<label>
				<span>身份证号：</span>
				<input type="text" name="idCardNo" value=""/>
			</label>
			<div></div>
			<label>
				<span>职&emsp;&emsp;务：</span>
				<select name="duty">
					<c:forEach items="${dutys}" var="de">
						<option value="${de.id }">${de.displayName }</option>
					</c:forEach>
				</select>
			</label>
			<label>
				<span>职&emsp;&emsp;称：</span>
				<select name="profession">
					<c:forEach items="${profs}" var="de">
						<option value="${de.displayName }">${de.displayName }</option>
					</c:forEach>
				</select>
			</label>
			<div></div>			
			<label>
				<span>科&emsp;&emsp;室：</span>
				<select name="depid">
					<c:forEach items="${departs}" var="de">
						<option value="${de.id }">${de.displayName }</option>
					</c:forEach>
				</select>
				<a href="/hospital/departments" class="addep">科室不全，点击添加科室</a>
			</label>
			<div></div>
			<label>
					<span>擅长：</span>
					<div><textarea name="speciality" style="width:400px;height:80px;"></textarea></div>
			</label>
			<div class="clearfix">
				<!-- <label id="kesicons" style="margin-right:1em;float:left;">
					<span>医生头像：</span>
					<div class="imgupload">
						<div class="thumb prelative">
							<img id="logourl" src="" />
							<div id="addHosIcon"><i class="iconfont">&#xe63f;</i></div>
						</div>
						<input type="hidden" name="headImageUrl" value=""/>
					</div>
				</label>	 -->
				<!-- <div style="display:inline-block;margin-right:1em;float:left;">
					<span style="font-size:14px;">出诊时间：</span>
					<table class="table table-line" style="margin:0;">
						<tr>
							<th></th>
							<th>星期一</th><th>星期二</th><th>星期三</th><th>星期四</th><th>星期五</th><th>星期六</th><th>星期日</th>
						</tr>
						<tr>
							<th>上午</th>
							<td><input type="checkbox" data-group="week1" name="outTime" value="1:am"/></td>
							<td><input type="checkbox" data-group="week2" name="outTime" value="2:am"/></td>
							<td><input type="checkbox" data-group="week3" name="outTime" value="3:am"/></td>
							<td><input type="checkbox" data-group="week4" name="outTime" value="4:am"/></td>
							<td><input type="checkbox" data-group="week5" name="outTime" value="5:am"/></td>
							<td><input type="checkbox" data-group="week6" name="outTime" value="6:am"/></td>
							<td><input type="checkbox" data-group="week7" name="outTime" value="7:am"/></td>
						</tr>
						<tr>
							<th>下午</th>
							<td><input type="checkbox" data-group="week1" name="outTime" value="1:pm"/></td>
							<td><input type="checkbox" data-group="week2" name="outTime" value="2:pm"/></td>
							<td><input type="checkbox" data-group="week3" name="outTime" value="3:pm"/></td>
							<td><input type="checkbox" data-group="week4" name="outTime" value="4:pm"/></td>
							<td><input type="checkbox" data-group="week5" name="outTime" value="5:pm"/></td>
							<td><input type="checkbox" data-group="week6" name="outTime" value="6:pm"/></td>
							<td><input type="checkbox" data-group="week7" name="outTime" value="7:pm"/></td>
						</tr>
						<tr>
							<th>全天</th>
							<td><input type="checkbox" data-group="week1" name="outTime" value="1:all"/></td>
							<td><input type="checkbox" data-group="week2" name="outTime" value="2:all"/></td>
							<td><input type="checkbox" data-group="week3" name="outTime" value="3:all"/></td>
							<td><input type="checkbox" data-group="week4" name="outTime" value="4:all"/></td>
							<td><input type="checkbox" data-group="week5" name="outTime" value="5:all"/></td>
							<td><input type="checkbox" data-group="week6" name="outTime" value="6:all"/></td>
							<td><input type="checkbox" data-group="week7" name="outTime" value="7:all"/></td>
						</tr>
					</table>	
				</div> -->
				<label>
					<span>个人简介：</span>
					<div><textarea name="profile" style="width:400px;height:187px;"></textarea></div>
				</label>
			</div>
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
		var _burl='/',_h = _burl;
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
                    "sAjaxSource": "/hospital/gaindoctors",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,     
					"fnCreatedRow": function (nRow, aData, iDataIndex) {
                    	$('td:eq(5)', nRow).html('<a href="javascript:;" id="'+ aData[0] +'" class="resetpsd linebtn">重置密码</a><a href="javascript:;" id="'+ aData[0] +'" style="margin-left:10px;" class="js-addinfo linebtn">编辑</a>');
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
                	var id = _id = this.getAttribute('id') || '';
                	!id ? 
                	showFillBody({}) :
					seajs.use('view/base',function(base){
	                	base.showTipIngA('正在执行').get('hospital/queryDoctorInfo/' + id,{docid:id},function(d){
	                		showFillBody(d.doc);
	                		base.hideTip();
	                	});
	                });
                	return false;
                })
                .delegate('.table-line input','change',function(){
                	var input = this.getAttribute('data-group'),value = this.value;
                	this.checked && $('.table-line input[data-group="'+ input +'"]').each(function(){
                		this.value != value && (this.checked = false);
                	});
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
                .delegate('.resetpsd','click',function(){
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
            function showFillBody(d){
				$('#formeditor h2').text(_id ? '编辑医生' : '新增医生');
				$('#formeditor [name="username"]').val(d.specialName || '');
				$('#formeditor [name="telphone"]').val(d.telphone || '');
				$('#formeditor [name="idCardNo"]').val(d.idCardNo||'');
				$('#formeditor [name="depid"]').val(d.depId + '');
				$('#formeditor [name="duty"]').val(d.dutyId + '');
				$('#formeditor [name="profession"]').val(d.profession + '');
				$('#formeditor [name="speciality"]').val(d.specialty||'');
				
				//document.getElementsByName('famousDoctor')[0].checked = !!d.famousDoctor;				
				/* d.outTime && $('#formeditor [name="outTime"]').each(function(){
					$.inArray(this.value,d.outTime.split(',')) != -1 ? (this.checked = true) : (this.checked = false);
				}); */
				$('#formeditor [name="profile"]').val(d.profile ||'');
				$('#formeditor [name="headImageUrl"]').val(d.listSpecialPicture ||'');
				$('#formeditor [name="badgeUrl"]').val(d.badgeUrl ||'');
				$('#formeditor #logourl').attr('src',d.listSpecialPicture ||'');
				$('#formeditor #logourl_ZZ').attr('src',d.badgeUrl ||'')
				//禁止更改
				_id && $('#formeditor [name="username"]').attr('readonly','readonly');
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
		        			 $('.imgupload [name="headImageUrl"]').val(obj['urlpath']);
		        			 $('#formeditor .parentFileBox').hide();
		        		}
		        	});
					$('#addZZIcon').size() && $('#addZZIcon').Uploader({
		        		server: _burl + 'doctor/uploadLocalFile',
		        		formData: {},
		        		backdata:'frazior',
		        		single:true,
		        		pick:{multiple: false},
		        		thumb: {width: 100,height: 100},
		        		beforecreat:function(){$('#formeditor .parentFileBox').show();},
		        		success:function(liobj,obj){
		        			 $('#logourl_ZZ').attr('src',$('#formeditor .parentFileBox .viewThumb img').attr('src'));   
		        			 $('.imgupload [name="badgeUrl"]').val(obj['urlpath']);
		        			 $('#formeditor .parentFileBox').hide();
		        		}
		        	});
				});
            }
            function saveForm(base,fn){
            	var tel = $.trim($('#formeditor [name="telphone"]').val()),
            		nam = $.trim($('#formeditor [name="username"]').val()),
            		dep = $('#formeditor [name="depid"]').val(),
            		//fam = document.getElementsByName('famousDoctor')[0].checked ? '1' : '0',
            		dutyId = $('#formeditor [name="duty"]').val(),
            		dutyName =$('#formeditor [name="duty"] option:selected').text(),
            		
            		profession = $('#formeditor [name="profession"]').val(),
            		/* outTime = (function(ips){
            			var arr = [];
            			ips.each(function(){arr.push(this.value)});
            			return arr.join(',');
            		})($('#formeditor [name="outTime"]:checked')), */
            		headImageUrl = $('#formeditor [name="headImageUrl"]').val(),
            		badgeUrl = $('#formeditor [name="badgeUrl"]').val(),
            		speciality=$('#formeditor [name="speciality"]').val(); 
            		profile = $('#formeditor [name="profile"]').val();     
					idCardNo = $('#formeditor [name="idCardNo"]').val() ;
               	if(!nam) return base.showTipE('请输入医生姓名'),false;
              	if(!base.valideTel(tel)) return base.showTipE('请输入正确的电话格式'),false;  
				if(idCardNo && !base.valideCard(idCardNo)) return base.showTipE('请输入有效身份证号'),false;
               	base.showTipIngA('正在保存').post(_id ? 'hospital/updateDoctorInfo' :'hospital/savedoctor',{
               		docid:_id,
               		telphone:tel,
               		username:nam,
               		depid:dep,
               		//famousDoctor: fam,
               		dutyId: dutyId,
               		dutyName:dutyName,
               		profession: profession,
               		//outTime: outTime,
               		headImageUrl: headImageUrl,
               		badgeUrl:badgeUrl,
               		speciality:speciality,
               		profile: profile,
					idCardNo: idCardNo
               	},function(d){
               		if(d.status == 'error'){
               			base.showTipE(d.msg);
               		}else{
               			fn();
               			_id ? oTable.draw(false) : oTable.page('first').draw(false);
               			_id ? base.hideTip() : base.showTipS('保存成功' + '，专家初使密码为'+ (d['defpass'] || '123456'));
               		}
               	},function(){
               		base.showTipE('网络出错');
               	});
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
</body>
</html>

