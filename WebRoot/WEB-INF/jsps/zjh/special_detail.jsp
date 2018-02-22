<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE html>
<html>
  <head>
    <base href="/">
    <title>医生详情</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
	<link rel="stylesheet" type="text/css" href="/css/view/special_detail.css" />
	<link rel="stylesheet" type="text/css" href="/libs/swiper/css/swiper.min.css" />
	<style>
		body{background-color:#f3f3f3;}
		.col1 img{border-radius:50%; overflow:hidden;}		
	</style>
	<script type="text/html" id="dateTemplte">	
		<div class="addTable">
			<table class="tabs-table">
				<thead>
					{0}
				</thead>
			</table>
		</div>					
	</script>
  </head>  
  <body>
    <div id="index" class="index">
    	<header class="doclist">
			<div class="box">
				<div class="col1 thumb">
					<c:choose>
						<c:when test="${fn:contains(special.detailsProfilePicture,'://')}">
							<img alt="暂无头像" src="${fn:replace(special.detailsProfilePicture,'http://','https://')}"/>
						</c:when>
						<c:otherwise>
							<img alt="暂无头像" src="http://wx.15120.cn/SysApi2/Files/${special.detailsProfilePicture}"/>
						</c:otherwise>
					</c:choose>
				</div>
				<div class="col2">
					<div class="baseinfo whitespace">${special.specialName}<span class="level">${special.specialTitle}</span></div>
					<div class="jobinfo whitespace"><span>${special.hosName}</span></div>					
				</div>
			</div>
			<div class="overflowhidden goodat">
				擅长：${special.specialty}
			</div>
    	</header>
    	<dl>
			<dd class="tabs-dd" id="tabs-dd" >
				<div class="addList" data-id="1">
					<div class="childdiv"></div>
				</div>
				<div class="timepicker_txt">
					<p class="timedure">
						上午：8:00 - 12:00
					</p>
					<div id="am" class="clearfix"></div>
					<p class="timedure">
						下午：13:00 - 17:00
					</p>
					<div id="pm" class="clearfix">
						<!--<div class="cols1 timelist">
							<div class="timeblock disabled" data-id="8">
								<label>13:00</label>
								<span class="leavenum">(1/20)</span>
							</div>
						</div>
					--></div>
					<div class="loadings">
						<img alt="" style="" src="/img/loading/31.gif"/>
					</div>
				</div>	
			</dd>			
			<c:if test="${fn:length(localSpecials)>0}">
				<dt class="bottomTitle">会诊本地资源对接：</dt>
				<dd class="dockslist swiper-container">
					<dl class="swiper-wrapper">
						<c:forEach items="${hosDeps}" var="sp">
							<dd class="docksl swiper-slide"  data-depId="${sp.id}">
								<%-- <div class="doclist">医院：${sp.hosName}</div> --%>
								<div class="doclist">就诊医院：${sp.hosName}</div>
								<div class="doclist">对应科室：${sp.displayName}</div>
								<div class="doclist">医院地址：${sp.location}</div>
								<div class="doclist">咨询联系电话：${sp.contactTelephone}</div>
								<%-- <div class="doclist">科室介绍：${sp.specialty}</div> --%>
							</dd>
						</c:forEach>
					</dl>				
			        <div class="swiper-button-next swiper-button-white"></div>
			        <div class="swiper-button-prev swiper-button-white"></div>	
				</dd>
			</c:if>
		</dl>		
    </div>
    <form action="/wzjh/sureorder" id="myform" method="post">
    	<input type="hidden" name="orderId" value="${orderId}"/>
    	<input type="hidden" name="openid" value="${openid}"/>
    	<input type="hidden" name="cooHosId" value="${cooHosId }"/>
    	<input type="hidden" name="localHosId" value="${cooHosId}"/>
    	<input type="hidden" name="condate" id="datetime" />
    	<input type="hidden" name="contime" id="time" />
    	<input type="hidden" name="cmoney" id="cmoney"/>
    	<input type="hidden" name="localDepartId" id="depart" />
    	<input type="hidden" name="expertId" id="expertId" value="${sid}"/>
	    <div class="g_fixed" style="background-color:#fff;z-index:1000">
	    	<button type="button" id="sub">提交</button>
	    </div>
    </form>
    <div style="height:70px;"></div>
    <script src="/js/jquery-1.11.0.min.js"></script>
    <script src="/js/base.js"></script>
	<script type="text/javascript">
		var _b = {
			link:'/',
			_post:function(url,ops,fun,err){
	    		return _$(this.link + url,ops,fun,err);
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
			getWeek:function getWeek(week){
			  var day;
			   switch (week){
			    case 7:day="星期日";
			      break;
			    case 1:day="星期一";
			      break;
			    case 2:day="星期二";
			      break;
			    case 3:day="星期三";
			      break;
			    case 4:day="星期四";
			      break;
			    case 5:day="星期五";
			      break;
			    case 6:day="星期六";
			      break;
			   }
			   return day;
			 },
			generateCaps:function(selector){
				var tb = $('#dateTemplte').html();
				selector.find('.childdiv').append(tb.replace('{0}',_title));
			},
			title:function(){
				var today=new Date(),week = today.getDay() || 7,cdate,cdatestr = '',cmd = '';
				var secondtr='';
				secondtr+='<tr>';
				for(var i=1;i<=7;i++){
					cdate = new Date();
					cdate.setDate(today.getDate()+i);
					cmd = (cdate.getMonth() + 1) +'-' + cdate.getDate();
					cdatestr = _b.formatDate(cdate.getFullYear() + '-'+ cmd);
					week=cdate.getDay()||7;
					secondtr+='<th class="stime" data-time="'+ cdatestr +'"><label>'+this.getWeek(week)+'</label><label>'+ cmd +'</label><label class="state"></label></th>';
				}
				secondtr+='</tr>';	
				return secondtr;
			},
			showloading:function(){
				$('.timepicker_txt .loadings').show();
				return this;
			},
			hideloading:function(){
				$('.timepicker_txt .loadings').hide();
				return this;
			},
			appendToHtml:function(arr){
				var am = [],pm = [];
				$.each(arr,function(i,o){
					var ex = '<div class="timelist"><div class="timeblock '+ (o.remark < '1' ? 'disabled' : '') +'" data-cost="'+o.cost+'" data-id="'+ o.name +'"><label>'+ o.name +'</label></div></div>';
					o.name < '13:00' ? am.push(ex) : pm.push(ex);
				});
				$('#am').html(am.join(''));
				$('#pm').html(pm.join(''));
				return this;
			}
		},_title = _b.title();
		_b.generateCaps($('.addList'));
	</script>
	<script type="text/javascript">
		var counter = 0;
		$(document).ready(function(){
			$('#sub').click(function(){
				var $localDoc = $('.docksl.swiper-slide-active').size() ? $('.docksl.swiper-slide-active') : $('.docksl.swiper-slide:first'),
					dpid = $localDoc.attr('data-depId') || '',
					localDocid = $localDoc.attr('data-id') || '';				
				if(!$('#datetime').val()){return alert('请选择相应的日期。'),false;}
				if(!$('#time').val()){return alert('请选择相应的时间段。'),false;}
				if(!dpid){return alert('请选择相应的科室或医生。'),false;}
				$('#depart').val(dpid);
				$('#myform').submit();
			});
			$('body').delegate('.doclist .goodat','click',function(){
				$(this).hasClass('overflowhidden') ? $(this).removeClass('overflowhidden') : $(this).addClass('overflowhidden')
			}).delegate('.timepicker_txt .timeblock','click',function(){
				!$(this).hasClass('disabled')?(
				!$(this).hasClass('selected') ? 
					($('.timepicker_txt .timeblock.selected').removeClass('selected'),$(this).addClass('selected')) : 
					$(this).removeClass('selected'),$('#cmoney').val($(this).hasClass('selected')?$(this).attr('data-cost'):''),			
				$('#time').val($(this).hasClass('selected') ? $(this).attr('data-id') : '')):'';
			});
			$('.tabs-table').delegate('.stime','click',function(){
				var dtime = $(this).attr('data-time'),sid = '${sid}';
				_b.showloading()._post('wzjh/gaintimes',{timedate:dtime,sid:sid},function(d){					
					_b.appendToHtml(d.times || []);
					window.setTimeout(_b.hideloading,300);
				},function(){
					window.setTimeout(_b.hideloading,300);
				});
				$(this).addClass('selected').siblings().removeClass('selected');
				$('#datetime').val(dtime);
				$('#time').val('');
			});
			$('.tabs-table .stime').each(function(){				
				var $th = $(this), dtime = $th.attr('data-time'), sid = '${sid}',$state = $('.state',this);
				$state.html('<img alt="" style="width:16px;" src="'+ _b.link +'img/loading/31.gif"/>');
				_b.showloading()._post('wzjh/gaintimes',{timedate:dtime,sid:sid},function(d){
					counter++;					
					$state.html(function(){
						var o = d.times || [];
						$th.addClass(o.length ? 'online' : 'offline');
						return o.length ? '出诊' : '休息';
					});
				},function(){
					$state.html('');
				});
			});
			$('.stime:first').click();
			listenState();
		});
		function listenState(){
			if(counter < 7){
				window.setTimeout(listenState,200);
			}else{
				$('.stime.online:first').click();
			}
		}
	</script>
		<script src="/libs/swiper/js/swiper.jquery.min.js"></script>
		<script>
			var swiper = new Swiper('.swiper-container', {
		        nextButton: '.swiper-button-next',
		        prevButton: '.swiper-button-prev'		   
		    });			
		</script>
  </body>
</html>
