<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//通过request["code"]得到code，通过code得到用户的基本信息
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<!DOCTYPE html>
<html>
<head lang="en">
    <title>填写病历信息</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
	<link rel="stylesheet" type="text/css" href="/libs/diyUpload/css/webuploader.css">
    <link rel="stylesheet" type="text/css" href="/css/diyUpload.css">
	<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
    <style>
		body{background-color:#F0EFEB;font-size:14px;}
		.whitespace{display:block;}
		.linkbl{border:1px solid #CFC8B5;border-radius:8px;color:#F29253;font-weight:600;padding:8px 10px;background:transparent;}
		.controls .contorl input[type="text"],
		.controls .contorl select{width:100%;height:2em;border:0;outline:0;line-height:1.2em;}
		.select{position:relative;}
		.select .select_input{position:relative; margin-right:40px;z-index:3;}
		.select .select_select{position:absolute;left:0;top:0;height:100%;width:100%;z-index:2;background:transparent;}
		.select .select_select:after{content:"";position:absolute;right:13px;top:50%;margin-top:-9px;width:10px;height:10px;border:1px solid #999;
			transform:rotate(-135deg);-webkit-transform:rotate(-135deg);border-right:0;border-bottom:0;z-index:1;
		}
		.select .select_select select{opacity:0;}
		.baseinfo,.textareas{background-color:#fff;border:1px solid #D5D4D2;margin:10px 0;border-left:0;border-right:0;}
		.baseinfo .controls{margin-left:10px;line-height:4em;}
		.baseinfo .controls + .controls{border-top:1px solid #D4D4D4;}
		.baseinfo .controls .iconfont{color:#3FA897;margin-right:6px;font-size:18px;}
		.textareas .controls > label{display:block;margin:0 10px;line-height:1.6em;padding-top:10px;color:#999;}
		.textareas .controls textarea{width:100%;height:6em; margin:6px 0;border:0;outline:0;}
		.textareas .controls .small{font-size:0.8em;}
		.textareas .topit{line-height:4em;padding:0 10px;}
		.textareas .topit input{width:100%;height:30px;border:0;}
		.textareas dd + dd{border-top:1px solid #D4D4D4;}
		
		.hisdata{overflow: auto;-webkit-overflow-scrolling : touch; z-index: 10000;background-color:#fff;top:0;padding-top:1px;}
		
		.hisdata dl{margin:10px;}
		.hisdata dt{line-height:3em;}
		.hisdata dd{padding:6px;margin:6px 0;border:1px solid #ccc;background-color:#f6f6f6;border-radius:4px;}
		.hisdata dd h3,.hisdata dd p{padding:5px 0;line-height:1.6em;font-size:13px;}
		.hisdata dd p:last-child{text-align:right;font-size:12px;color:#999;}
		.hisdata dd.selected{border-color:blue;}
		
		.noresult{padding:40px 0;text-align:center;}
		.noresult div{padding:20px 0;}
		
		#addfiles .iconfont{font-size:40px;}
    </style>
    <style>
    	.parentFileBox{padding:14px 0;}		
    	.parentFileBox > .fileBoxUl > li > .diyBar > .diyProgressText{font-size:12px;}
		#addfiles{height:100%;}
		.btnUpload{width:100%!important;height:100%!important;z-index:1;}
		.webuploader-pick{background:transparent;color:#7C7C7C; z-index:0;padding:0;display:block;width: 100%;height: 100%;}
		.parentFileBox>.fileBoxUl{overflow:visible;list-style:none;}
		.parentFileBox>.fileBoxUl .viewThumb img{width:100%;position:absolute;left:50%;top:50%;transform:translate(-50%,-50%);-webkit-transform:translate(-50%,-50%);}
		.parentFileBox>.fileBoxUl>li.actionAdd{overflow:hidden;}
		.parentFileBox>.fileBoxUl>li>.diyCancel{background-size: 16px 16px;background-position: right top;}
		#addfiles{position:relative;background-color:transparent;}
		#popupMenu{height:152px;}
		#popupMenu a{display:block;text-align:center;padding:15px 0;color:#202020;font-size:16px;letter-spacing:1px;}
		#many{border-top:1px solid #DADBDC;border-bottom:7px solid #D9DBD9;}
		
		#signLabel{padding-top:10px;}
		.slTitle{display:inline-block;padding:7px 30px 7px 15px;border-radius:4px;background-color:#fff;color:#888;position:relative;}
		.slTitle:after,.slTitle:before{content:"";display:block;width:9px;height:9px;position:absolute;right:10px;top:8px;
			border:1px solid #999; border-left:0;border-top:0; 
			transform:rotate(45deg);
			-webkit-transform:rotate(45deg);
		}
		.slTitle:before{top:4px;}
		.detailSignLabel{padding:10px;border-radius:2px;background-color:#fff;box-shadow: 0px 0px 6px #ccc; margin-top: -3px; z-index: 2; position: relative;}
    	.dl_outer{font-size:12px;padding-top:10px;}
    	.dl_outer a{display:inline-block;padding:3px 5px;border-radius:3px;color:#6EA9B1;text-align:center;margin:3px;border:1px solid #fff;}
    	.dl_outer a.selected,.dl_outer a.defined{border-color:#6EA9B1;}
    </style>
</head>
<body>
	<form class="index" id="mainform" action="/wzjh/sureplus" method="post">
		<input type="hidden" name="appointId" value="${appointId}"/>
		<input type="hidden" name="openid" value="${openid}"/>
		<input type="hidden" name="sid" value="${sid}"/>
		<div class="baseinfo">
			<div class="box controls">
				<span class="cols0"><i class="iconfont">&#xe611;</i><span>姓&emsp;名：</span></span>
				<span class="cols1 contorl">
					<div class="select">
						<div class="select_input">
							<input type="text" placeholder="请输入患者真实姓名" name="username" id="username"/>
						</div>
						<div class="select_select">
							<select class="select_options" name="select_s" id="his_con">
								<option value=""></option>
								<c:forEach items="${conInfos}" var="ci">
									<option value="${ci.userName}|${ci.idCard}|${ci.telphone}">${ci.userName}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</span>
			</div>
			<div class="box controls">
				<span class="cols0"><i class="iconfont">&#xe610;</i><span>身份证：</span></span>
				<span class="cols1 contorl">					
					<input type="text" placeholder="请输入患者身份证号" name="idcard" id="idcard"/>
				</span>
			</div>
			<div class="box controls">
				<span class="cols0"><i class="iconfont">&#xe60f;</i><span>手机号：</span></span>
				<span class="cols1 contorl">					
					<input type="text" placeholder="请输入联系电话，可以非本人" name="telphone" id="telphone"/>
				</span>
			</div>
		</div>
		
		<div class="textareas">
			<dl>
				<dd class="box topit">
					<span class="cols0">病情关键词：</span>
					<span class="cols1"><input type="text" name="keywords" /></span>
					<span class="cols0">
						<button type="button" class="linkbl">引用病历</button>						
					</span>
				</dd>
				<dd class="controls">
					<label>
						<span>症状描述：</span>
						<textarea name="desc_txt"></textarea>
						<input type="hidden" name="desc"/>
					</label>
				</dd>
				<dd class="controls">
					<label>
						<span class="whitespace">家族史、治疗经历、结论<span class="small">(请详细描述，方便卖家分析问题)</span></span>
						<textarea name="familyHistory_txt"></textarea>
						<input type="hidden" name="familyHistory"/>
					</label>
				</dd>
				<dd class="controls">
					<label>
						<span class="whitespace">想问的问题或需要的帮助<span class="small">(请详细描述，方便卖家分析问题)</span></span>
						<textarea name="askProblem_txt"></textarea>
						<input type="hidden" name="askProblem"/>
					</label>
				</dd>
				<dd class="controls">
					<div id="pics" class="pics">
						<p style="color:#999;padding:5px 10px;font-size:13px;"><span>请将病患部位、检查报告、化验单、处方药单、影像资料、病历等拍照上传。（最多可上传9张，每张照片不超过3M）</span></p>
						<div class="parentFileBox">
							<ul class="fileBoxUl">
								<li class="actionAdd">
									<div id="addfiles">
										<i class="iconfont">&#xe612;</i>
									</div>
								</li>
							</ul>
						</div>	
						<input type="hidden" name="pics" id="picsIds"/>			
					</div>
				</dd>
			</dl>
		</div>
		<p style="line-height:1.6em;padding:0.5em 0;margin:0 10px;color:#888;font-size:12px;">
			<b style="color:#999;font-size: 36px;font-weight: 400;line-height: 0px;vertical-align: -16px;margin-right:5px;">*</b>您在本页的所有输入（包括拍照资料）仅您和医生可见。
		</p>
		<div id="fixed" class="g_fixed" style="padding:0;background-color:#fff;z-index:99;">
			<button type="button" class="buttonClass26B5ED" style="width:96%;margin:6px 2%;" id="sub">提&ensp;交</button>
		</div>
	</form>	
	<div class="hisdata index hasload g_fixed" style="display:none;">
		<dl>
			<dt>HIS历史数据如下：</dt>
			<c:forEach items="${cases }" var="ca">
				<dd data-id="${ca.id}" onclick="javascript:void(0)">
					<h3>${ca.patientName} - ${ca.phone}</h3>
					<p>${ca.desc}</p>
					<p>${ca.familyHistory}</p>
					<p>创建于：${ca.createTime}</p>
				</dd>
			</c:forEach>
    		<c:if test="${fn:length(cases)<=0}">
    			<dd class="noresult">
					<img src="/img/mobile/kbzs/icon_noresult@2x.png" style="width:58px" alt=""/>
					<div>您还没有历史信息。</div>
				</dd>
    		</c:if>
		</dl>
	</div>
	<div style="height:65px;">&nbsp;</div>
    <script src="/js/jquery-1.11.0.min.js"></script>
	<script src="/js/browser.js" type="text/javascript"></script>
	<script src="/js/base.js"></script>
    <script type="text/javascript" src="/libs/diyUpload/js/webuploader.0.1.1.min.js"></script>
    <script type="text/javascript" src="/libs/diyUpload/js/diyUpload.mobile.js"></script>
    <script type="text/javascript">
		$(function(){
			$('.select_options').change(function(){
				var v = this.value,
					t = (this.selectedOptions[0] || {text:''}).text,
					s = $(this).closest('.select');
				s.find('.select_input [type="text"]').val(t);
				s.find('.select_input [type="hidden"]').val(v);
				var info = v || ('||');
				info = info.split('|');
				$('#username').val(info[0]);
				$('#idcard').val(info[1]);
				$('#telphone').val(info[2]||'${f_tel}');
			}).change();
		})
	</script>
    <script>
         var h = $('#pics').width()/5 - 12;
         var isHistoryApi = !!(window.history && history.pushState),hasHistory = 0;
         var _b = {
			link:'/',
			_dia:new _dialog(),
			_post:function(url,ops,fun,err){
	    		return _$(this.link + url,ops,fun,err);
	    	},
			_get:function(url,ops,fun,err){
	    		return _$$(this.link + url,ops,'get','',fun,err);
	    	}
	     };
         $(document).ready(function(){
			$('.pics .fileBoxUl li').css({'width':h,'height':h});
			$('#addfiles .iconfont').css({'font-size': h + 'px','line-height': h + 'px'});
			$('#addfiles').diyUpload({
            	method:"POST",
                server: '/wzjh/uploadPic',
                thumb:{
	                width:h,
	                height:h,
	                quality: 70,
	                allowMagnify: false,
	                crop: true,
	                type: "image/*"
                },
                success: function (id) {
                	setUploadValue(id);
                },
                error: function (err) {
                    console.info(err);
                }
            });
           	$('#fixed').delegate('#sub','click',saveInfo);
			
			$('.linkbl').click(function(){
				if(isHistoryApi){
					window.history.state != '列表' ?					
						history.pushState("列表", "", location.href + '#hisdata') :
						history.replaceState("列表", "", location.href + '#hisdata');
					window.onhashchange();
				}else{
					location.hash = 'hisdata';
				}
			});
			$('body').delegate('.hisdata dd','click',function(){
				var id = this.getAttribute('data-id');
				$(this).addClass('selected').siblings().removeClass('selected');
				id && ajaxload(id);	
				if(isHistoryApi){
					history.replaceState("列表", "", location.href.split('#')[0]);
					window.onhashchange();
				}else{
					location.hash = '';
				}
				_b._dia.loading.show();
			}).delegate('.fileBoxUl .diyCancel','click',function(){
				var li = $(this).closest('li'),ids = [];
				li.remove();
				$('#pics li').each(function(){
					var id = this.getAttribute('data-id')||'';
					id && ids.push(id);
				});
				$('#picsIds').val(ids.join(','));
			});
			window.onhashchange=function(){
				var hashStr = location.hash.replace("#","") || '';
				switch(hashStr){
					case 'hisdata':
						showHis();
						break;
					default:
						hideHis();
						break;					
				}
			};
			window.onhashchange();
		});
		function resetForm(){
			$('.textareas :input,.baseinfo :input').val('');
		}
		function ajaxload(id){
			_b._post('wzjh/loadsinglecase',{openid:'${openid}',caseid:id},function(d){
				doit(d);
			},function(){
				alert('网络出错');
			});
		}
		function doit(d){
			var hts = [],caseobj = d['cases'];
			$('.textareas [name="keywords"]').val(caseobj['keywords']);
			$('.textareas [name="desc_txt"]').val(decodeURI(caseobj['desc']));
			$('.textareas [name="familyHistory_txt"]').val(decodeURI(caseobj['familyHistory']));
			$('.textareas [name="askProblem_txt"]').val(decodeURI(caseobj['askProblem']));
			$('#pics .actionAdd').siblings('li').remove()
				.end().before(function(){
				var ids = [],lis = [];
				$.each(d['images'],function(i,g){
					ids.push(g.id);
					lis.push('<li id="fileBox_WU_FILE_'+ g.id +'" data-id="'+ g.id +'">\
							    <div class="viewThumb"><img src="'+ g.url +'"></div>\
							    <div class="diyCancel" style="display: block;"></div>\
							</li>');
				});
				$('.textareas [name="pics"]').val(ids.join(','));
				return lis.join('');
			});
			SetMoveState1();
			_b._dia.loading.hide();
		}
		function showHis(){
			$('.hisdata').show();
			$('#mainform').hide();
		}
		function hideHis(){
			$('#mainform').show();
			$('.hisdata').hide();
		}
		function setUploadValue(id){
			var $hinput=$("#picsIds");
			if($hinput.val()==""){
				$hinput.val(id.upid);
			}else{
				$hinput.val($hinput.val()+","+id.upid);
			}
		}
		function SetMoveState1(){
			$('.pics .fileBoxUl li').css({'width':h,'height':h});
		}
		function saveInfo() {
			if($('#sub').hasClass('disabled')) return false;
			if(!$('#username').val()) return alert('姓名不能为空'),false;
			if(!$('#idcard').val()) return alert('身份证不能为空'),false;
			if(!$('#telphone').val()) return alert('电话不能为空'),false;
			encodeTxt();
			$('#sub').addClass('disabled').html('正在保存……');
			$('#mainform').submit();
		}
		function encodeTxt(){
			$('.textareas textarea').each(function(){
				$(this).next('input').val(encodeURI($.trim($(this).val())));
			});
		}
	</script>
</body>
</html>