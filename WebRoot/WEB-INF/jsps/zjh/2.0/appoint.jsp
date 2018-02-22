<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head lang="en">
		<meta charset="UTF-8">
		<title>佰医汇</title>
		<jsp:include page="../../icon.jsp" />
		<meta name="viewport" content="width=device-width,  user-scalable=no">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta content="telephone=no" name="format-detection" />
		<link rel="stylesheet" href="/css/mobile.css" type="text/css" />
		<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
		<link rel="stylesheet" type="text/css" href="/libs/swiper/css/swiper.min.css" />
		<style type="text/css">
			body{background-color:#F0EFED;font-size:14px;}
			.noresult{padding:30px 0;text-align:center;opacity:.6;width: 100%;}
			.loadings{position:absolute;left:0;top:0;width:100%;height:100%;z-index:100;background-color:#ccc;opacity:.6;}
			.loadings img{width:24px;position:absolute;left:0;top:0;bottom:0;right:0;margin:auto;}
			section{background-color:#fff;border-top:1px solid #E0E0E0;border-bottom:1px solid #E0E0E0;}
			.itemlist{position:relative; line-height:42px;margin-left:10px;color:#757575;}
			.itemlist + .itemlist{border-top:1px solid #ECECEC;}
			.itemlist .iconfont{color:#05CD9A;margin-right:6px;}
			.hasselect{position:relative;}
			.hasselect select{width: 100%; height: 30px; border: 0; background-color: transparent; position: relative; z-index: 2;outline:0;
			    appearance: none;-webkit-appearance: none;}
			.hasselect:after{content:"";position:absolute;right:10px;top:50%;margin-top:-6px;width:8px;height:8px;
				border:1px solid #ccc;border-left:0;border-top:0;
				transform:rotate(45deg);-webkit-transform:rotate(45deg);}
			.split1{color:#a0a0a0;padding:18px 0 6px 10px;}
			.split2{color:#a0a0a0;padding:15px 10px;font-size:13px;}
			section header{border-bottom:1px solid #08CE9C;line-height:44px;}
			section header h3{color:#08CE9C;margin-left:10px; font-size:16px;font-weight:600;}
			section header h3:before{content:"";display:inline-block;width:2px;height:1em;margin:0 6px -2px 0;background-color:#08CE9C;}
			section header a{margin-right:10px;}
			
			.doclist .thumb{position:relative; display: inline-block;width:60px;height:60px;overflow:hidden;border-radius:50%;}
			.doclist .thumb img{position:absolute;top:50%;left:50%;transform:translate(-50%,-50%);-webkit-transform:translate(-50%,-50%);}
			.doclist .docl{padding: 6px 30px;box-sizing: border-box;}
			.doclist .docl p{line-height:30px;color:#3E3E3E;margin-left:1em;}
			.doclist .docl p span:last-child{margin-left:0.6em;}
			.doclist .docl p:first-child span:last-child{color:#9B9B9B;}
			.doclist .docl .hidden{display:none;}
			
			#doclist{min-height:76px;}
			.swiper-button-next,.swiper-button-prev{background:transparent url();width: 18px;height: 36px;}
			.swiper-button-next:after,
			.swiper-button-prev:after{position:relative;content:"";display:inline-block;
				transform:rotate(45deg) translateY(-50%);-webkit-transform:rotate(45deg) translateY(-50%);top:50%;
				width:10px;height:10px;border:1px solid #aaa;}
			.swiper-button-next:after{border-left:0;border-bottom:0;}
			.swiper-button-prev:after{border-right:0;border-top:0;}
			#doctimes{min-height:180px;}
			#doctimes .doctimelists{min-height:100px; margin: 10px 0;}
			#doctimes table{width:100%;text-align:center;}
			#doctimes th{text-align:center;border:1px solid #E0E0E0;border-left-color:transparent;border-right-color:transparent;font-size:13px;padding:5px 0;}
			#doctimes th label{display:block;line-height:1.8em;text-align:center;}
			#doctimes th label:nth-child(2){display:inline-block;width:1.8em;height:1.8em;border-radius:50%;line-height:1.9em;overflow:hidden;font-size:12px;}
			#doctimes th.offline{color:#aaa;}
			#doctimes th.online{color:#676767;}
			#doctimes th.online .state{color:#F88942;}
			#doctimes th.selected{position:relative; border-color:#E0E0E0;border-bottom-color:transparent;}
			#doctimes th.selected:after{content:"";position:absolute;left:-1px;top:0;height:100%;width:1px;background-color:#E0E0E0;}
			#doctimes th.selected label:nth-child(2){color:#fff;background-color:#FD8845;}
			
			.doctimelists{position:relative;}
			.doctimelists .timedure{line-height:2em;color:#999;text-align:center;}
			.doctimelists .timelist{float:left;width:20%;}
			.doctimelists .timeblock{padding:5px 0; margin:5px;border:1px solid #FA8945; background-color:#fff;color:#FA8945;font-size:13px;}
			.doctimelists .timeblock.disabled,
			.doctimelists .timeblock.enough{border-color:#D6D2D3;background-color:#C8C8C8;color:#FDFDFD!important;}
			.doctimelists .timeblock.selected{background-color:#FA8945;color:#fff;}
			.doctimelists .timeblock label,
			.doctimelists .timeblock span{display:block;text-align:center;line-height:1.5em;}
			
			#sub{height:48px;background:#00CC99;color:#fff;border:0;width:100%;font-size:20px;}
		</style>
	</head>
	<body>		
		<div class="index">
			<section>
				<div class="itemlist box">
					<span class="cols0"><i class="iconfont">&#xe61a;</i><span>您当前所在的城市：</span></span>
					<span class="cols1"><span class="input">${hos.city}</span></span>
				</div>
				<div class="itemlist box">
					<span class="cols0"><i class="iconfont">&#xe61c;</i><span>距您最近的合作医院：</span></span>
					<span class="cols1"><span class="input">${hos.displayName}</span></span>
				</div>
				<div class="itemlist box">
					<span class="cols0"><i class="iconfont">&#xe61b;</i><span>选择您要就诊的科室：</span></span>
					<span class="cols1 hasselect">
						<select name="chooseks" id="chooseks">
							<c:forEach items="${localDeparts}" var="dep">
								<c:choose>
									<c:when test="${depid == dep.id}">
										<option value="${dep.id }" selected>${dep.displayName }</option>
									</c:when>
									<c:otherwise>
										<option value="${dep.id }">${dep.displayName }</option>
									</c:otherwise>
								</c:choose>	
							</c:forEach>
						</select>
					</span>
				</div>
			</section>
			<p class="split1">选择您要预约的专家</p>
			<section class="docandtimelist">
				<header class="box">
					<span class="cols1">
						<h3>推荐专家</h3>
					</span>
					<!-- <span class="cols0">
						<a href="">搜索专家</a>
					</span> -->
				</header>
				<div class="doclist swiper-container">
					<dl class="swiper-wrapper" id="doclist"></dl>				
			        <div class="swiper-button-next"></div>
			        <div class="swiper-button-prev"></div>	
				</div>
				<div class="doctimes" id="doctimes"></div>
			</section>
			<p class="split2">提示：左右滑动切换专家</p>
		</div>
	    <form action="/wzjh/suretoconfirm" id="myform" method="post" style="height:50px;">
	    	<input type="hidden" name="openid" value="${openid}"/>
	    	<input type="hidden" name="sid" id="sid" />
	    	<input type="hidden" name="departId" id="departId" />
	    	<input type="hidden" name="stimeid" id="stimeid"/>
	    	<input type="hidden" name=datetime id="datetime"/>
	    	<input type="hidden" name="depid" id="depid" value="${depid}" />
	    	<input type="hidden" name="expertId" id="expertId" value="${expertId}"/>
		    <div class="g_fixed" style="z-index:1000">
		    	<button type="button" id="sub">确定</button>
		    </div>
	    </form>
		<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
		<script src="/libs/swiper/js/swiper.jquery.min.js"></script>
		<script src="/js/base.js"></script>
		<script>
			var depid = '${depid}',expertId = '${expertId}',swiper;
			var _b = {
				link:'/',
				loading:'<div class="loadings"><img alt="" style="" src="'+ window.location.origin +'/img/loading/31.gif"/></div>',
				noresult: '<div class="noresult"><img src="'+ window.location.origin +'/img/mobile/kbzs/icon_noresult@2x.png" style="width:30px" alt=""/></div>',
				load:{
					show:function(selector){
						$(selector).addClass('prelative').append(_b.loading);
					},
					hide:function(selector){
						$(selector).removeClass('prelative').find('.loadings').remove();
					}
				},
				_post:function(url,ops,fun,err){
		    		return _$(this.link + url,ops,fun,err);
		    	},
				_get:function(url,ops,fun,err){
		    		return _$$(this.link + url,ops,'get','json',fun,err);
		    	},
				formatDate:function(d){
					if(!d) return +new Date();
					d = d.split('-');
					return d[0] + '-' + this.formatHH(d[1]) + '-' + this.formatHH(d[2]);
				},
				formatHH:function(h){
					h = (h || '00').split('');
					h.length < 2 && h.unshift('0');
					return h.join('');
				},
				getWeek:function(week){
				  var day;
				   switch (week){
				    case 7:day="日";
				      break;
				    case 1:day="一";
				      break;
				    case 2:day="二";
				      break;
				    case 3:day="三";
				      break;
				    case 4:day="四";
				      break;
				    case 5:day="五";
				      break;
				    case 6:day="六";
				      break;
				   }
				   return day;
				},
				getdocbydepid:function(depid,callback){
					this._post('wzjh/gainRecommondSpecials',{departId:depid},function(d){
						var sp = d.specials || [],doclist = '';
						$.each(sp,function(i,o){
							var imgUrl=o.listSpecialPicture;
							if(imgUrl.indexOf('://') != -1){
								imgUrl = imgUrl.replace('http://','https://');
							}else{
								imgUrl="http://wx.15120.cn/SysApi2/Files/"+o.listSpecialPicture;
							}
							doclist += '<dd class="docl swiper-slide" data-id="'+ o.specialId +'">';
							doclist += '<div class="box">';
							doclist += '	<span class="thumb">';
							doclist += '		<img src="'+imgUrl +'" alt="" />';
							doclist += '	</span>';
							doclist += '	<div class="cols1">';
							doclist += '		<p class="whitespace"><span>'+ o.specialName +'</span><span>'+ o.duty+' '+o.profession+'</span></p>';
							doclist += '		<p class="whitespace"><span>'+ o.hosName +'</span><span>'+ o.depName +'</span></p>';								
							doclist += '	</div>';
							doclist += '</div>';
							doclist += '<div class="detail hidden">';
							o.specialty && (doclist += '<p class="">擅长：<span>'+o.specialty+'</span></p>');
							o.profile && (doclist += '<p class="">简介：<span>'+o.profile+'</span></p>');
							doclist += '</div>';
							doclist += '</dd>';
						});
						callback(doclist);
					},function(){
						callback('');
					});
				},
				getdoctimes :function(sid,sdate,callback){
					this._post('wzjh/gainSpecialTimes',{sid:sid,sdate:sdate},function(d){
						var sp = d.times || [],obj = {cls:'',txt:'',htm:''};
						$.each(sp,function(i,o){
							obj.htm += '<div class="timelist"><div class="timeblock '+ (o.hasAppoint == '1' ? 'disabled' : '') ;
							obj.htm += '" data-cost="'+o.cost+'" data-id="'+ o.id +'"><label>'+ o.startTime +'</label></div></div>';
						});
						sp.length ? 
							(obj.cls = 'online',obj.txt = '出诊') : 
							(obj.cls = 'offline',obj.txt = '休息',obj.htm = _b.noresult);
						
						callback(obj);
					},function(){
						callback({
							cls : 'offline',
							txt : '出错',
							htm : _b.noresult
						});
					});
				},
				gettitle:function(){
					var today=new Date(),week = today.getDay() || 7,cdate,cdatestr = '',cmd = '';
					var secondtr = '<table class="tabs-table"><tr>',obj = {htm:'',dates:[]};
					for(var i=0;i<=6;i++){
						cdate = new Date();
						cdate.setDate(today.getDate()+i);
						cmd = (cdate.getMonth() + 1) +'-' + cdate.getDate();
						cdatestr = _b.formatDate(cdate.getFullYear() + '-'+ cmd);
						week=cdate.getDay()||7;
						secondtr+='<th class="stime" data-time="'+ cdatestr +'"><label>'+ this.getWeek(week);
						secondtr+='</label><label>'+ cmd.split('-')[1] +'</label><label class="state">';
						secondtr+='<img alt="" style="width:16px;" src="'+ _b.link +'img/loading/31.gif"/></label></th>';
						obj.dates.push(cdatestr);
					}
					secondtr+='</tr></table><div class="doctimelists clearfix"></div>';	
					obj.htm = secondtr;
					return obj;
				},
				initswiper:function(){
					swiper && swiper.destroy(true,true);
					swiper = new Swiper('.swiper-container', {
				        nextButton: '.swiper-button-next',
				        prevButton: '.swiper-button-prev',
				        onInit:function(){
						    expertId ? window.setTimeout(function(){
				        		var idx = $('.docl[data-id="${expertId}"]').index() || 0;
						    	swiper.slideTo(idx, 500, _b.loaddoctimes);
						    	swiper.destroy(false);
						    	$('.swiper-container > div').hide();
						    },300) : _b.loaddoctimes();
						    _b.imgDo();
				        },
				        onSlideChangeEnd: function(){
					    	_b.loaddoctimes();
					    },
				        onSlideChangeStart: function(){
					    	$('.swiper-wrapper .swiper-slide .detail').addClass('hidden');					    	
					    }
				    });
				},
				imgDo:function(){
					$('#doclist .swiper-slide').each(function(){					
						var img = $('.thumb img',this);
						img[0].onload = function() {							
							var w = img.width(),h = img.height();
							if(w > h){
								img.css({width:'auto',height:'100%'});
							}else{
								img.css({width:'100%',height:'auto'});
							}
				        }
					});
				},
				loaddoctimes:function(){					
					if(window.loaddoctimer){ 
						clearTimeout(window.loaddoctimer);
						window.loaddoctimer = null;
					}
					window.loaddoctimer = window.setTimeout(function(){
						_b.loaddoctimesDEP();
					},600);
				},
				loaddoctimesDEP:function(sid){
					var obj = this.gettitle();
					$('#doctimes').html(obj.htm);
					$.each(obj.dates,function(i,tim){
						_b.loadsingdate(tim);
					});
				},
				loadsingdate :function(tim){				
					var th = $('#doctimes [data-time="'+ tim +'"]'),state = th.find('.state');
					var curr = $('#doclist .swiper-slide-active'),sid,depid = $('#chooseks').val();
					curr.size() < 1 && (curr = $('#doclist .swiper-slide').first());
					sid = curr.attr('data-id');	
					
					th.hasClass('selected') && _b.load.show('.doctimelists');				
					this.getdoctimes(sid,tim,function(o){
						var $selected = $('#doctimes .selected'),$count = $selected.size();
						th.addClass(o.cls);
						state.html(o.txt);
						if(o.cls == 'online' && ($count < 1 || ($count > 0 && tim < $selected.attr('data-time')))){
							th.addClass('selected').siblings('.selected').removeClass('selected');
							$('#datetime').val(tim);
						}
						th.hasClass('selected') && ($('#doctimes .doctimelists').html(o.htm),_b.load.hide('.doctimelists'));
					});
				}
			};
			
		</script>
		<script>
			$(document).ready(function(){
				$('#chooseks').change(function(){
					var depid = this.value;
					_b.load.show('.docandtimelist');
					_b.getdocbydepid(depid, function(html){
						$('#doclist').html(html || _b.noresult);
						html ? (_b.initswiper(),$('.swiper-container > div').show()) : ($('#doctimes').html(''),$('.swiper-container > div').hide());
						_b.load.hide('.docandtimelist');
					});
					$('#datetime,#stimeid').val('');
				}).change();
				$('#doctimes').delegate('.stime','click',function(){
					var th = $(this),state = th.find('.state');
					var tim = th.attr('data-time');					
					$(this).addClass('selected').siblings().removeClass('selected');					
					_b.loadsingdate(tim);					
					$('#datetime').val(tim);
					$('#stimeid').val('');
				});
				$('body').delegate('.doctimelists .timeblock','click',function(){
					!$(this).hasClass('disabled')?(
					!$(this).hasClass('selected') ? 
						($('.doctimelists .timeblock.selected').removeClass('selected'),$(this).addClass('selected')) : 
						$(this).removeClass('selected'),
						$('#stimeid').val($(this).hasClass('selected') ? $(this).attr('data-id') : '')):'';
				})
				.delegate('.swiper-wrapper .swiper-slide','click',function(){
					var dtail = $(this).find('.detail');
					dtail.hasClass('hidden') ? dtail.slideUp(500,function(){
						dtail.removeClass('hidden')
					}) : dtail.slideDown(500,function(){
						dtail.addClass('hidden')
					});
				});
				$('#sub').click(function(){
					var curr = $('#doclist .swiper-slide-active'),sid,
						depid = $('#chooseks').val();
					curr.size() < 1 && (curr = $('#doclist .swiper-slide').first());
					sid = curr.attr('data-id');
					if(!$('#datetime').val()){return alert('请选择相应的日期。'),false;}
					if(!$('#stimeid').val()){return alert('请选择相应的时间段。'),false;}
					if(!depid){return alert('请选择相应的科室'),false;}
					$('#sid').val(sid);
					$('#departId').val(depid);
					$('#myform').submit();
				});
			});
		</script>
		<script src="/js/jweixin-1.0.0.js"></script>
    	<script>
	    	wx.config({
			    appId: '${appid}',
	            timestamp: +'${timestamp}',
	            nonceStr: '${nonceStr}',
	            signature: '${signature}',
	            jsApiList: [
	              'getLocation'
	            ]
			});
			wx.ready(function(){
				wx.getLocation({
				    type: 'wgs84',
				    success: function (res) {
				        var latitude = res.latitude;
				        var longitude = res.longitude;
				        gainLocation(latitude,longitude);
				    },
				    fail: function (res) {
				       //alert(JSON.stringify(res));
				    },
				    complete:function(){}
				});
			});
			wx.error(function(res){
				//alert(JSON.stringify(res));
			});
			
			function gainLocation(latitude,longitude){
				/*$.post("wzjh/gainCityByLocation",{latitude:latitude,longitude:longitude},function(data){
					$('#city_name').attr('data-id',data.chid).attr('data-value',data.city).text(data.city);
					$('#lcity').text(data.city)
						.closest('.citys').attr('data-id',data.chid).attr('data-value',data.city);
					if(data.chid){$('#cooHosId').val(data.chid);}
					setState();
				});*/
			}
	    </script>
	</body>
</html>
