<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
  <head>
    <base href="/">
    <title>我的代金券</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
	<style>
		dl{margin:10px;font-size:16px;}
		dd{margin:10px 0;background:#fff url(/img/mobile/kbzs/icon_kbzs_bg@2x.png) no-repeat right 20px center;background-size:100px 108px;
			min-height:100px;color:#A4A5A7;}
		dd div{padding:0 0 20px 20px;}
		dd p{text-align:right;padding:5px 10px;}
		dd div b{font-size:48px;padding-right:20px;}
		dd div b span{font-size:26px;}
		dd div label{font-weight:600;height:45px;display:inline-block;vertical-align: middle;}
		.topbg{height:13px;background:#10B5F0 url(/img/mobile/kbzs/icon_bg.png) repeat-x 0 bottom;background-size:8px 16px;padding:0}
		.bottombg{height:9px;background:url(/img/mobile/kbzs/icon_point@2x.png) repeat-x center center;background-size:10px 3px;}
		.useable p,.useable b{color:#10B5F0;}
		.unuseable{color:#DFDFDF;}
		.unuseable .topbg{background-color:#BDC2C6;}
		._ctxt{padding:0.3em 1.8em 2em;line-height:1.8em;text-align:center;}
		._ctxt a{color:blue;}
		.noresult{padding:40px 0;text-align:center;background:url();}
		.noresult div{padding:20px 0;}
		#moreLi{margin:0 10px}
		#moreA{display:block;text-align:center;padding:12px 0;color:#26B5F1;background-color:#fff;}
	</style>
  </head>  
  <body>
    <div id="index">
    	<dl id="orderList">
    				<dd class="useable">
    				<p class="topbg"></p>
					<p>天使陪诊代金券[首单券]</p>
					<div>
						<b><span>￥&ensp;</span>
							<fmt:formatNumber pattern="###">
								300
							</fmt:formatNumber>	
					</b>
						<label>
									可用
									有效截止日期 - 2015-10-07
						</label>
					</div>
					<p class="bottombg"></p>
				</dd>	
				
    				<dd class="useable">
    				<p class="topbg"></p>
					<p>天使陪诊代金券[普通券]</p>
					<div>
						<b><span>￥&ensp;</span>
							<fmt:formatNumber pattern="###">
								100
							</fmt:formatNumber>	
					</b>
						<label>
									可用

									有效截止日期 - 2015-10-29
							
						</label>
					</div>
					<p class="bottombg"></p>
				</dd>

    				<dd class="unuseable">
					<p class="topbg"></p>
					<p>天使陪诊代金券[普通券]</p>
					<div>
						<b><span>￥&ensp;</span>
							<fmt:formatNumber pattern="###">
								50
							</fmt:formatNumber>	
					</b>
						<label>
								已过期
						</label>
					</div>
					<p class="bottombg"></p>
				</dd>
		</dl>	
    </div>
	<div class="nbsp fixed" style="height:66px;"></div>
	<div class="btndiv fixed" style="background-color:#D9D9D9;">
		<p style="margin:10px;border-radius:3px;overflow:hidden;position: relative;font-size:15px;">
			<input type="text" name="codeT" id="codeT" placeholder="输入代金券码" style="width:100%;padding:8px;box-sizing:border-box;border:0;"/>
			<button type="button" id="codeB" onclick="javascript:alert('开发中..敬请期待!');" style="background-color:#ACACAC;border:0;padding:0 1.5em;height:100%;position:absolute;right:-2px;top:0;color:#fff;">兑换</button>		
		</p>
	</div>   
	<script src="/libs/jquery-1.11.0.min.js"></script>
	<script src="/js/base.js"></script>
	<script>
    	var _b = {
    		href: '/',
    		oid:'${openid}',
	    	dialog: new _dialog(),
    		loadimg:function(){
    			return '<a class="loading" style="display:block;font-size:12px;line-height:26px;text-align:center;padding:10px 0;background-color:#fff;"><img src="'+ this.href +'img/mobile/loading2.gif" alt="" style="width:24px;vertical-align: top;"/>正在加载</a>';
    		},
    		_post:function(url,ops,fun,err){
    			return _$(this.href + url,ops,fun,err);
    		},
    		gainCode:function(v){
    			if(!v) return false;
    			this.dialog.loading.show();
    			this._post('wtspz/gainCouponByCode',{code:v.toUpperCase(),openid:'${openid}'},function(d){    				
    				_b.dialog.alert.show({
	    				text : '<div class="_ctxt">'+ (d.status == 'success' ? '兑换成功' : '无效的兑换码') +'</div>',
	    				close:function(){
	    					d.status == 'success' && location.reload();
	    				}
	    			});
	    			_b.dialog.loading.hide();
    			},function(){
    				_b.dialog.loading.hide();
    			});
    		}
    	};	
    	$(document).ready(function(){
    		addChangeEvent(document.getElementById('codeT'),function(){
    			if(this.value.length){
    				$('#codeB').css('background-color','#35BBEE');
    			}else{
    				$('#codeB').css('background-color','#ACACAC');
    			}
    		});
    	});
    	function addChangeEvent(obj, fn) {
		    var input = document.createElement('input'), isPro = 'onpropertychange' in input,
		          isInput = 'oninput' in input;
		    if (obj.attachEvent)
		        obj.attachEvent(isPro ? 'onpropertychange' :'oninput', fn);/*IE6-10*/
		    else
		        obj.addEventListener(isInput ? 'input' : 'change', fn, false);/*IE11,Firefox,chrome,safari*/
		    return fn;
		}
		function moreInfo(id,more,p){
			var out_d = $('#' + id),more_d = $('#' + more),
				pageno = out_d[0]['_pageNo'] || 1,nextP = parseInt(pageno) + 1;
			more_d.find('.moreBtn').hide().after(_b.loadimg());
			p && p == 1 && (nextP = p);
			_b._post('wtspz/morecoups.do',{openid:'${openid}',pageNo: nextP},function(d){
				var ops = '';
				d.coupons.length ? ($.each(d.coupons,function(i,o){
					var cls = o.timeLimit == '1' ? 'unuseable' : 'useable',
						t = o.timeLimit == '1' ? '已过期' : (o.endTime ? '有效截止日期 - '+ o.endTime : '可用');
					ops += '<dd class="'+ cls +'">\
								<p class="topbg"></p>\
								<p>看病代金券</p>\
								<div>\
									<b><span>￥&ensp;</span>'+ parseInt(o.money,10) +'</b>\
									<label>'+ t +'</label>\
								</div>\
								<p class="bottombg"></p>\
							</dd>';
				}),out_d[0]['_pageNo'] = nextP,d.coupons.length > 9 && more_d.find('.moreBtn').show()) : (more_d.hide());
				more_d.find('.loading').remove();
				ops && (nextP == 1 ? out_d.html(ops) : out_d.append(ops));
			});
		}	
	</script>
  </body>
</html>
