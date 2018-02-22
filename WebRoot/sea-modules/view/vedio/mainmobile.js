//数组去重复
Array.prototype.uniques = function() { var res = [], hash = {};for(var i=0, elem; (elem = this[i]) != null; i++)  {if (!hash[elem]){res.push(elem);hash[elem] = true;}}return res;}
define(function(require, exports, module) {
	var Controller = require('Controller');
	require('bindEvent').init();
});

define('bindEvent',function(require, exports, module){
	var Controller = require('Controller');
    exports.init = function(){
    	$('body')  	
    	.delegate('#pacs .imglist','click',function(){
    		var rid = $(this).attr('data-rid') || '',ridArr = rid.split('|'),href = '';
    		if(rid.indexOf('null') != -1 || rid.indexOf('undefined') != -1) return false;
    		href = 'http://yc.51zjh.com:8088/oviyam2/viewer.html?patientID='+ ridArr[0] +'&studyUID='+ ridArr[1] +'&serverName=&u=admin&p=&_=' + (+new Date);
    		window.open(href, "_blank");
		})
    	.delegate('#listable .reportlist','click',function(){
    		var html = $(this).html(),index = $(this).index(),
    			total = htm = $('#listable .reportlist').size(),
    			_html = html.replace('<div class="report small">','<div class="report">');
    		var f = '',l = '';
    		index != 0 ? (f = '<div data-val="'+ (index - 1) +'" class="prevreport reportbtn"><i class="iconfont">上一个</i></div>') : (f = '');
    		index != (total - 1) ? (l = '<div data-val="'+ (index + 1) +'" class="nextreport reportbtn"><i class="iconfont">下一个</i></div>') : (l = '');
    		Controller.showDia(_html + f + l);
    	})
    	.delegate('.reportbtn','click',function(){
    		var idx = $(this).attr('data-val'),    			
    			even = $('#listable .reportlist:eq('+ idx +')');
    		even.click();
    	})
    	.delegate('#FormModelEdit .thumblist','click',function(){
    		var src = this.getAttribute('data-src');
    		var iframe = document.getElementById('dwviframe');
    		Controller.reloaddwv(iframe);    		
    		iframe.src = src;
    		$(this).addClass('curr').siblings().removeClass('curr');
    	})
    	.delegate('.videotitle','click',function(){
    		var href = $(this).attr('data-href'),list = $(this).closest('.videolist');
    		if(!Controller.versions().ios){
    			list.siblings('.open').removeClass('open').find('video').attr('src','');
    			list.hasClass('open') ? list.removeClass('open') : list.addClass('open');
    			$(this).siblings('video').attr('src',href);
    		}
    	});    	
    	if(!Controller.versions().ios){
			$('.videolist .videotitle').each(function(){
	    		var href = $(this).attr('href');
				$(this).attr('href','javascript:void(0)')
					.attr('data-href',href);
			});			
		}
    	//初使化加载His等数据
    	Controller.anyscAjaxLis(function(){
    		$('#listable').html(Controller.loading);
    	},function(lishtm){
    		$('#listable').html(lishtm);
    	});
    	Controller.anyscAjaxPacs(function(){
    		$('#pacs').html(Controller.loading);
    	},function(pachtm){
			$('#pacs').html(pachtm);
    	});
    	
    	$(window).bind("scroll",function(){
        	if(window._timers) window.clearTimeout(window._timers);
        	window._timers = window.setTimeout($.proxy(Controller.scrollFun,Controller),400);
        });
    };
});

define('Controller',['view/base','view/webupload'],{
	base:function(){
		return seajs.require('view/base')	
	},
	program:'<div class="progressc"><div class="barc" style="width:0%;"></div><div class="bartxt">0%</div></div>',
	loading:'<div class="loadings"><img alt="" style="" src="'+ window.location.origin +'/img/loading/31.gif"/></div>',
	noresult: _noresult,	
	anyscAjaxLis:function(beforecallback,callback){
		var _this = this,base = _this.base();
		beforecallback();
		base.get(_href + 'doctor/gainLisData',{oid:_orderid},function(d){	
			var lishtm;
			lishtm = d.records ? _this.ansysLisBeansWithTime(d.records) : _this.noresult;
			callback(lishtm);
		},function(){
			callback(_this.noresult);
			base.showTipE('LIS拉取数据失败');
		});
	},
	anyscAjaxPacs:function(beforecallback,callback){
		var _this = this,base = _this.base();
		beforecallback();
		base.get(_href + 'doctor/gainPacsData',{oid:_orderid},function(d){	
			var pashtm;
			pashtm = d.pac_records ? _this.ansysPacsBeansWithSign(d.pac_records) : _this.noresult;
			callback(pashtm);
		},function(){
			callback(_this.noresult);
			base.showTipE('PACS拉取数据失败');
		});
	},
	ansysLisBeansWithTime : function(d){
		var timlist = [],_this = this;
		$.each(d,function(i,o){
			var div = '<div class="blocklist beantimelist clearfix" id="key'+ o.key.replace(/\//g,'') +'">';
			div += '<div class="beantime"><span class="time">'+ o.key +'</span></div>';
			div += '<div class="swiper-container"><div class="swiper-wrapper">' + _this.ansysLisBeans(o.beans) + '</div><div class="swiper-button-next"></div><div class="swiper-button-prev"></div></div>';
			div += '</div>';
			timlist.push(div);
		});
		return timlist.join('') || this.noresult;
	},
	ansysLisBeans:function(d){
		var rls = [];
		$.each(d,function(i,o){
			var reportlist = '<div class="reportlist"></div>',
				reportModel = $('#report_model').html(),trs = '';
			reportModel = reportModel.replace('{record_name}',o['kvs']['record_name']);
			$.each(o.beans,function(ii,b){
				var bkvs = b['kvs'],sx = bkvs['缩写'] || '',sxarr = sx.split('(');
				trs += '<tr><td>'+ (sxarr[1] || '').split(')')[0] +'</td>';
				trs += '<td>'+ sxarr[0] +'</td>';
				trs += '<td>'+ bkvs['检验结果'] +'</td>';
				trs += '<td style="text-align:center;">'+ (bkvs['标志'] == 'None' ? '' : bkvs['标志']) +'</td>';
				trs += '<td>'+ bkvs['参考区间'] +'</td>';
				trs += '<td>'+ bkvs['单位'] +'</td></tr>';
				if(!ii){
					$.each(bkvs,function(x,y){
						reportModel = reportModel.replace('{'+ x +'}',bkvs[x]);
					});
				}
			});
			reportModel = reportModel.replace('{tbody}',trs);
			rls.push('<div class="reportlist" data-id="'+ o['key'] +'">'+ reportModel +'<label class="re_title">'+ o['kvs']['record_name'] +'</label></div>');
		});
		return rls.join('') || this.noresult;
	},
	ansysPacsBeansWithSign:function(d){
		var signlist = [],_this = this;
		$.each(d,function(i,o){
			var div = '<div class="blocklist pacsSignlist clearfix">';
			div += '<div class="pacsSign"><span class="sing">'+ o.key +'</span></div>';
			div += _this.ansysPacsBeans(o.beans);
			div += '</div>';
			signlist.push(div);
		});
		return signlist.join('') || this.noresult;
	},
	ansysPacsBeans:function(d){
		var pacs = '',_this  = this;
		$.each(d,function(i,o){
			pacs += '<div class="imglist" data-rid="'+ o.kvs['patient_id'] + '|'+ o.studyId + '">';
			pacs += '<div class="thumb">';
			pacs += '<img src="'+ _this.ansysUrl(o) + '"/>';
			pacs += '</div>';
			pacs += '<label class="thumbName">'+ o['remark'] +'('+ o.kvs['Check_Item_E'] + ')</label>';
			pacs += '</div>';
		});
		return pacs;
	},
	showDia:function(html){
		var _this = this,base = _this.base();		
		base.hideDialog('FormModelEdit').showDialog({
			id:'FormModelEdit',
			text:html,
			nofooter:true
		});
    	$('#FormModelEdit').on('hide.bs.modal', function (e) {
    		$('body').removeClass('modal2-open')
    	});
	},
	loadGainPics:function(rid,callback){
		var _this = this,base = _this.base();
		base.showDialog({
			id:'FormModelEdit',
			cls:'modal2-auto',
			nofooter:true
		}).get(_href + 'doctor/gainImagePics',{oid:_oid,rid:rid},function(d){	
			$('#FormModelEdit .modal2-body').html(_this.ansysSeries(d.ph || {}));
			callback();			
		},function(){			
			base.showTipE('加载失败');
			window.setTimeout(function(){
				base.hideDialog('FormModelEdit');
			},1000);
		});
		
    	$('#FormModelEdit').on('hide.bs.modal', function (e) {
    		$('body').removeClass('modal2-open')
    	});
    	
    	$('.modal2-open .modal2').bind("scroll",function(){
        	if(window._timers) window.clearTimeout(window._timers);
        	window._timers = window.setTimeout($.proxy(_this.scrollFun,_this),400);
        }).scroll();
	},
	ansysSeries:function(href){
		return '<div class="row-fluid dwvOuter"><div class="dwvContainer prelative span12"><iframe name="dwviframe" src="'+ href +'" id="dwviframe" frameborder="no" border="0" marginwidth="0" marginheight="0"></iframe></div></div>';
	},
	ansysUrl:function(o){
		return _href + 'dcmimage?study='+ o.studyId +'&series='+ o.seriesId +'&object='+ o.instanceId;
	},
	reloaddwv:function(iframe){
		$('#FormModelEdit .loadings').fadeIn();
		if (iframe.attachEvent){ 
			iframe.attachEvent("onload", function(){ 
				//alert("Local iframe is now loaded."); 
    			$('#FormModelEdit .loadings').fadeOut();
			}); 
		} else { 
			iframe.onload = function(){ 
				//alert("Local iframe is now loaded."); 
    			$('#FormModelEdit .loadings').fadeOut();
			}; 
		}
	},
	scrollFun:function(){
		var _this = this;
		var st = $(window).scrollTop(), sth = st + $(window).height();
		$('.imglist[data-val],.thumblist[data-val]').each(function(){
			var o = $(this), post = o.offset().top, posb = post + o.height();
	        if ((post > st && post < sth) || (posb > st && posb < sth)) {
	           o.attr('data-val') && (function(item){
	        	   var src = item.attr('data-val');
	        	   var iframe = '<img src="'+ src +'" />';
	        	   item.removeAttr('data-val').find('.thumb').html(iframe);
	           })(o);
	        }
		});		
	},
	versions:function(){
		var u = navigator.userAgent, ua = u.toLowerCase(), app = navigator.appVersion;
		return {
			trident: u.indexOf('Trident') > -1, //IE内核
			presto: u.indexOf('Presto') > -1, //opera内核
			webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
			gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
			mobile: !!u.match(/AppleWebKit.*Mobile.*/)||!!u.match(/AppleWebKit/), //是否为移动终端
			ios: !!u.match(/(i[^;]+\;(U;)? CPU.+Mac OS X)/), //ios终端
			android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
			iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器
			iPad: u.indexOf('iPad') > -1, //是否iPad
			webApp: u.indexOf('Safari') == -1, //是否web应该程序，没有头部与底部
			weixin: ua.match(/MicroMessenger/i) == 'micromessenger' //判断是否为微信浏览器
		};
	}
});

