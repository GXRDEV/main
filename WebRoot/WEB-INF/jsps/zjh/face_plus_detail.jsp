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
    <title>提交病情</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
	<link rel="stylesheet" type="text/css" href="/css/view/special_detail.css" />
		<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
	<style>
		body{background-color:#f3f3f3;}
		.col1 img{border-radius:50%; overflow:hidden;}		
		.ddlists{background-color:#fff;border-bottom:1px solid #DEDDDB;color:#2B2B2B;line-height:4em;font-size:14px;font-weight:600;}
		.ddlists .iconfont{font-size:18px;font-weight:300;}
		.ddlists .cols0{padding: 0 10px;}
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
					<div id="apm" style="min-height:30px;padding:15px 5px" class="clearfix">
						
					</div>
					<div class="loadings">
						<img alt="" style="" src="/img/loading/31.gif"/>
					</div>
				</div>
			</dd>	
			<dd class="ddlists box">
				<span class="cols0">
					<i class="iconfont" style="color:#F2BE06">&#xe60c;</i>
				</span>
				<span class="cols1"><span class="whitespace">加号类型：<span id="plustype"></span></span></span>
				<span class="cols0"><span id="plusmoney"></span></span>				
			</dd>
			<dd class="ddlists box">
				<span class="cols0">
					<i class="iconfont" style="color:#2C8CE2">&#xe60e;</i>
				</span>
				<span class="cols1"><span class="whitespace">加号时间：<label style="color:#DA7C64" id="plustime"></label></span></span>
			</dd>
			<dd class="ddlists box">
				<span class="cols0">
					<i class="iconfont" style="color:#DD4344">&#xe60d;</i>
				</span>
				<span class="cols1"><span class="whitespace">出诊地点：<label style="color:#DA7C64" id="worklocation"></label></span></span>
			</dd>
		</dl>		
    </div>
    <form action="/wzjh/basicconfirm" id="myform" method="post">
    	<input type="hidden" name="openid" value="${openid}"/>
    	<input type="hidden" name="appiontId" id="appiontId"/>
    	<input type="hidden" name="sid" id="expertId" value="${sid}"/>
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
			_get:function(url,ops,fun,err){
	    		return _$$(this.link + url,ops,'get','',fun,err);
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
				var d = (arr['sas'] && arr['sas'][0]) || {"canAfternoon":0,"amount":0,"canMorning":0,"canEvening":0};
				var am = [],_arr = [
					{
						name: '上午',
						cost: 'canMorning',
						remark: d['canMorning']
					},
					{
						name: '下午',
						cost: 'canAfternoon',
						remark: d['canAfternoon']
					},
					{
						name: '晚上',
						cost: 'canEvening',
						remark: d['canEvening']
					}					
				];
				$.each(_arr,function(i,o){
					var ex = '<div class="timelist"><div class="'+ (arr['status']=='enough' ? 'disabled' : '') +' timeblock '+ (o.remark < '1' ? 'disabled' : '') +'" data-id="'+ o['cost'] +'" data-cost="'+ d['amount'] +'" data-pid="'+d['id']+'" data-numtype="'+d['numberType']+'" data-work="'+d['workLocation']+'"><label>'+ o.name +'</label></div></div>';
					am.push(ex);
				});
				$('#apm').html(am.join(''));
				return this;
			}
		},_title = _b.title();
		_b.generateCaps($('.addList'));
	</script>
	<script type="text/javascript">
		var counter = 0;
		$(document).ready(function(){
			$('#sub').click(function(){			
				if(!$('#appiontId').val()){return alert('请选择预约的日期。'),false;}
				$('#myform').submit();
			});
			$('body').delegate('.doclist .goodat','click',function(){
				$(this).hasClass('overflowhidden') ? $(this).removeClass('overflowhidden') : $(this).addClass('overflowhidden')
			}).delegate('.timepicker_txt .timeblock','click',function(){
				!$(this).hasClass('disabled')?(
					!$(this).hasClass('selected') ? 
						($('.timepicker_txt .timeblock.selected').removeClass('selected'),$(this).addClass('selected'),setVal(this)) : 
						($(this).removeClass('selected'),clearVal())			
				):'';
			});
			$('.tabs-table').delegate('.stime','click',function(){
				var dtime = $(this).attr('data-time'),sid = '${sid}';
				_b.showloading()._get('wzjh/gainplustime',{dtime:dtime,sid:sid},function(d){					
					_b.appendToHtml(d);
					window.setTimeout(_b.hideloading,300);
				},function(){
					window.setTimeout(_b.hideloading,300);
				});
				$(this).addClass('selected').siblings().removeClass('selected');
				$('#datetime').val(dtime);
				$('#time').val('');
				clearVal();
			});
			$('.tabs-table .stime').each(function(){				
				var $th = $(this), dtime = $th.attr('data-time'), sid = '${sid}',$state = $('.state',this);
				$state.html('<img alt="" style="width:16px;" src= _b.link + "img/loading/31.gif"/>');
				_b.showloading()._get('wzjh/gainplustime',{dtime:dtime,sid:sid},function(d){					
					$state.html(function(){
						var o = (d['sas'] && d['sas'][0]) || {"canAfternoon":0,"amount":0,"canMorning":0,"canEvening":0};
						var st = d['status'] || '';
						$th.addClass((o['canMorning'] || o['canAfternoon'] || o['canEvening']) ? ('online ' + st) : 'offline');
						if(st == 'enough') return '约满';
						return (o['canMorning'] || o['canAfternoon'] || o['canEvening']) ? '出诊' : '休息';
					});
				},function(){
					$state.html('');
				});
			});
			$('.stime:first').click();
			listenState();
		});
		function setVal(timedual){
			$('#plustime').text(ptimeset($(timedual).attr('data-id'))),
			$('#appiontId').val($(timedual).attr('data-pid')),
			$('#plusmoney').text($(timedual).attr('data-cost')),
			$('#plustype').text(ptypeset($(timedual).attr('data-numtype'))),
			$('#worklocation').text($(timedual).attr('data-work'))
		}
		function clearVal(){
			$('#plustype,#plusmoney,#plustime,#worklocation').text(''),$('#appiontId').val('')
		}
		function ptypeset(numtype){
			
			switch(numtype){
			case '1':return "特需号";
			case '2':return "门诊号";
			case '0':return "国际号";
			}
		}
		function ptimeset(did){
			var d=$('.stime.selected').attr('data-time');
			switch(did){
			case 'canAfternoon':return d+" 13:00";
			case 'canMorning':return d+" 09:00";
			case 'canEvening':return d+" 19:00";
			}
		}
		function listenState(){
			if(counter < 7){
				window.setTimeout(listenState,200);
			}else{
				$('.stime.online:first').click();
			}
		}
	</script>
  </body>
</html>
