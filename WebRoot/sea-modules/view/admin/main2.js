var _idsearch="true",_diyUploadSelector;
define(function(require, exports, module) {	
	//专家咨询
	exports.adviceOrder = function(){	
		require('bindEvent').adviceOrder();
	};
	exports.adviceOrderDetail = function(){	
		require('bindEvent').adviceOrderDetail();
	};
});

define('bindEvent',function(require, exports, module){
	var Controller = require('Controller');
	//专家咨询详情
	exports.adviceOrderDetail = function(){	
		$('body')
		.delegate('#pacs .imglist .thumb','click',function(){
			var lst = $(this).closest('.imglist'),rid = lst.attr('data-rid') || '',ridArr = rid.split('|'),href = '';
			if(!rid || rid.indexOf('null') != -1 || rid.indexOf('undefined') != -1) return false;
			$('#pacs .imglist.curr').removeClass('curr'),lst.addClass('curr');
			//href = 'http://yc.51zjh.com:8088/oviyam2/viewer.html?patientID='+ ridArr[0] +'&studyUID='+ ridArr[1] +'&serverName=&u=admin&p=&_=' + (+new Date);
			//window.open(href, "_blank");
			href = location.origin + '/dwv/viewer.html?patientID='+ ridArr[0] +'&studyUID='+ ridArr[1] +'&serverName=';
			Controller.loadGainPics(href);
		});
		seajs.use('view/viewer',function(view){
    		_diyUploadSelector = view.init('.userinfo .diyUpload .fileBoxUl',{ 
    			navbar:true,
    			shown:function(){
    				if(window._hasTipForImgview) return false;
    				Controller.base().showTipE('滚动鼠标滑轮放大可以看的更清楚哦');
    				window._hasTipForImgview = 1;
    			}
    		});
    	});
    	//初使化加载Pacs等数据
    	(function(sign){
        	Controller.anyscAjaxPacs(function(){
        		_pacsSign = sign;
        		$('#pacs').html(Controller.loading);
        	},function(pachtm){
    			$('#pacs').html(pachtm);
    			Controller.scrollFun();
        	},sign);
    	})(+new Date); 
  	
    	Controller.imgDo('.dialog');
    	
    	$(window).bind("scroll",function(){
        	if(window._timers) window.clearTimeout(window._timers);
        	window._timers = window.setTimeout($.proxy(Controller.scrollFun,Controller),400);
        });
    	$(window).bind("resize",function(){
        	if(window._timers0) window.clearTimeout(window._timers0);
        	window._timers0 = window.setTimeout(function(){
        		$('#listable').find('.beantimelist').each(function(){
            		Controller.updateSwiper('#'+ this.id +' .swiper-container');
        		});
        		Controller.resizeGainDia();
        		Controller.scrollFun();
        		$('#LisModelDetail .formModelHeadOptions').size() && Controller.updateSwiper('#LisModelDetail .formModelHeadOptions');
        	},1);
        });
	};
    //专家咨询
    exports.adviceOrder = function(){
    	$('body')
    	.delegate('.doslist','click',function(){
    		$(this).addClass('selected').siblings().removeClass('selected');
    		$('[name="expertId"]').val($(this).attr('data-id'));
    		$('[name="pmoney"]').val($(this).attr('data-c'));
    	})
    	.delegate('.timelist dd.open','click',function(){
    		$('.timelist dd.hover').removeClass('hover');
    		$(this).addClass('hover');
    		$('[name="stimeid"]').val($(this).attr('data-id'));
    	})
    	.delegate('button[data-moreinfo]','click',function(){
    		var bol = $(this).attr('data-moreinfo');
    		bol == 'true' ? ($('.moreinfo').show()) : ($('.moreinfo').hide());
    		$(this).addClass('selected').siblings().removeClass('selected');
    	})
    	.delegate('button[name="nextstep"]','click',function(){
    		var step = $(this).attr('data-step');
    		Controller.validateStep(step);
    	})
    	.delegate('.querywhere dt a[data-ref]','click',function(){
    		var a = $(this),href = a.attr('data-ref'),outer = $(this).closest('dl');
    		Controller.ajaxMoreInfo(href,function(d){
    			outer.html(d + '<dt><a class="abtn" href="javascript:;">收起</a></dt>');
    		});
    	})
    	.delegate('.querywhere dt .abtn','click',function(){
    		var t = this.innerHTML,outer = $(this).closest('dl');
    		t.indexOf('收起') != -1 ? (outer.addClass('only2em'),this.innerHTML = '展开') : (outer.removeClass('only2em'),this.innerHTML = '收起');
    	})
    	.delegate('.querywhere .filteDoc','click',function(){
    		var $select = $(this),data = $select.data();
    		$('[name="'+ data.type +'id"]').val(data.id || '');
    		$(this).addClass('selected').siblings().removeClass('selected');
    		(function(dd){
    			var outer = dd.closest('dl'),btn = outer.find('.abtn');
    			outer.addClass('only2em'),btn.text('展开');
        		window.setTimeout(function(){
        			dd[0].scrollIntoView(true);
        		}, 400);
    		})($select);
    		Controller.loadDocPager('1');
    	})
    	.delegate('.pagenationDIV .page','click',function(){
    		var data = $(this).data();
    		Controller.loadDocPager(data.id);
    	})
    	.delegate('#sycnCase','click',function(){
    		var ajaxurl = this.getAttribute('data-ajax'),
    			_btn = $(this),sign = +new Date;
    		_listSign = sign;
    		ajaxurl && Controller.loadBeans(ajaxurl,sign,false);
    		return false;
    	})
    	.delegate('#sycnCaseMore','click',function(){
    		var ajaxurl = this.getAttribute('data-ajax');
			ajaxurl && Controller.loadBeans(ajaxurl,'',true);
			return false;
		})
		.delegate('#sycnPacsMore','click',function(){
    		var ajaxurl = this.getAttribute('data-ajax');
			ajaxurl && Controller.loadPacsBeans(ajaxurl);
			return false;
		})
    	.delegate('.beanquerywhere .filtDetail','click',function(){
    		var ajaxurl = $('#sycnCase').attr('data-ajax'),_btn = $(this),sign = +new Date;
    		if(_btn.hasClass('disabled')) return false;
    		_listSign = sign,_btn.addClass('disabled');
    		ajaxurl && Controller.loadBeansWithWhere(ajaxurl,function(d){    			
    			_btn.removeClass('disabled');
    		},sign);
    		return false;
    	})
    	.delegate('.beanquerywhere .filtPacsDetail','click',function(){
    		var ajaxurl = $('#sycnPacsMore').attr('data-ajax'),_btn = $(this),sign = +new Date;
    		if(_btn.hasClass('disabled')) return false;
    		_listSign = sign,_btn.addClass('disabled');
    		ajaxurl && Controller.loadPacsWithWhere(ajaxurl,function(d){    			
    			_btn.removeClass('disabled');
    		},sign);
    		return false;
    	})
    	.delegate('#pacs .imglist .thumb','click',function(){
    		var lst = $(this).closest('.imglist'),rid = lst.attr('data-rid') || '',ridArr = rid.split('|'),href = '';
    		if(!rid || rid.indexOf('null') != -1 || rid.indexOf('undefined') != -1) return false;
    		$('#pacs .imglist.curr').removeClass('curr'),lst.addClass('curr');
    		//href = 'http://yc.51zjh.com:8088/oviyam2/viewer.html?patientID='+ ridArr[0] +'&studyUID='+ ridArr[1] +'&serverName=&u=admin&p=&_=' + (+new Date);
    		//window.open(href, "_blank");
    		href = location.origin + '/dwv/viewer.html?patientID='+ ridArr[0] +'&studyUID='+ ridArr[1] +'&serverName=';
    		Controller.loadGainPics(href);
		})
		.delegate('.diyUpload .media','click',function(){
			Controller.showEleDia($(this).find('source').attr('src'));
    		return false;
    	})
    	.delegate('.imgdel','click',function(){
    		var li = $(this).closest('.imglist,.reportlist');
    		var block = $(this).closest('.blocklist');
    		li.remove();
    		if(block.find('.imglist,.reportlist').size() > 0){
	    		var swiperParent = li.closest('.beantimelist'),
	    			id = swiperParent.attr('id');
	    		id && Controller.updateSwiper('#'+ id +' .swiper-container');
	    	}else{
	    		block.remove();
	    	}
    		return false;
    	})
    	.delegate('.diyCancel','click',function(event){
    		var li = $(this).closest('li');
    		li.remove();
    		return false;
    	})
    	.delegate('.beantable .tablechk','change',function(event){
    		var vls = [],dirs = [],btn = $('.beanquerywhere .sysncBtn');
    		$('.beantable .tablechk').each(function(){
    			this.checked && (function(arr){
    				arr[0] && vls.push(arr[0]),
    				arr[1] && dirs.push(arr[1])
    			})(this.value.split('@'));
    		});
    		btn.attr('id',vls.join(','));
    		btn.attr('data-dirs',dirs.join(';'));
    		vls.length || dirs.length ? btn.removeClass('disabled') : btn.addClass('disabled');
    	})
    	.delegate('.beantable tbody tr','click',function(event){
    		var chbx = $(this).find('.tablechk')[0];
    		event.target.type != 'checkbox' && (chbx.checked = !chbx.checked,$(chbx).change());
    	})
    	.delegate('.tablebtn','click',function(){
    		var id = this.id,$this = $(this),sign = + new Date();
    		id && (function(){
    			$('#listable').html(Controller.program);
    			$('#pacs').html(Controller.loading);
    			_lisSign = _pacsSign = sign;
    			_signSyncBtn = 1;
    			return true;
    		})() && (
	    		Controller.loadLis('doctor/syncLisInfoByCase',id,function(htm){
	    			$('#listable').html(htm).find('.beantimelist').each(function(){
	            		Controller.initSwiper('#'+ this.id +' .swiper-container');
	        		});
	    		},sign),
	    		Controller.loadPacs('doctor/syncPacInfoByCase',id,function(htm){
	    			htm && $('#pacs').html(htm);
	    			Controller.scrollFun();
	    		},sign)
    		);
    	})
    	.delegate('.tablebtnadd,.tablebtnover','click',function(){
    		var $this = $(this),sign = + new Date();
    		var dirs = this.getAttribute('data-dirs');
    		_signSyncBtn = $this.hasClass('tablebtnadd') ? 0 : 1;
    		dirs && (function(){
    			_pacsSign = sign;
    			!!_signSyncBtn && $('#pacs').html(Controller.loading);
    			return true;
    		})() && (
	    		Controller.loadPacsWithQuery('doctor/syncpacsadvance',{oid:_oid,imagedirs:dirs,syncType:_signSyncBtn,syncSeries:sign},function(htm){
	    			!!_signSyncBtn && htm && $('#pacs').html(htm);
	    			Controller.scrollFun();
	    		},sign)
    		);    		
    	})
    	.delegate('#listable .reportlist','click',function(){
    		var html = $(this).html(),index = $(this).index(),
    			$parent = $(this).closest('.beantimelist'),
    			pindex = $parent.index(),
    			total = $('#listable .beantimelist').size(),
    			_html = html.replace('<div class="report small">','<div class="report">');
    		var f = '',l = '',h = '';
    		pindex != 0 ? (f = '<div data-val="'+ (pindex - 1) +'" class="prevreport reportbtn"><i class="iconfont">&#xe61f;</i></div>') : (f = '');
    		pindex != (total - 1) ? (l = '<div data-val="'+ (pindex + 1) +'" class="nextreport reportbtn"><i class="iconfont">&#xe622;</i></div>') : (l = '');
    		h = Controller.creatHead(this,index);
    		Controller.showDia(h + f + _html + l);
    	})
    	.delegate('.reportbtn','click',function(){
    		var idx = $(this).attr('data-val'),  			
    			even = $('#listable .beantimelist:eq('+ idx +') .reportlist:eq(0)');
    		even.click();
    	})
    	.delegate('#LisModelDetail .fmhoption','click',function(){
    		var idx = $(this).attr('data-id'), idsArr = idx.split('-'),   			
    			$idxEle = $('#'+ idsArr[0] +' .reportlist:eq('+ idsArr[1] +')');
    		var html = $idxEle.html(),_html = html.replace('<div class="report small">','<div class="report">');
    		$('#LisModelDetail .formModelHeadOptions').siblings('.report,label,span').remove().end().after(_html);
    		$(this).addClass('selected').siblings().removeClass('selected');
    	})
    	.delegate('#pics .diyFileName','click',function(event){
    		var input = $(this).find('[name="fileName"]'),txt = $.trim($(this).text());
    		if(_utype !="1" || input.size() > 0 || event.target.type == 'button') return ;
    		$(this).addClass('editing').html('<textarea name="fileName" data-v="'+ txt +'">'+ txt +'</textarea><button type="button" class="saveFileName">保存</button>');
    	})
    	.delegate('#pics .diyFileName .saveFileName','click',function(){
    		var _this = this;
    		if(window._timerFILEName) window.clearTimeout(window._timerFILEName);
    		window._timerFILEName = window.setTimeout(function(){
    			Controller.saveDiyFileName($(_this).prev(':input'));
        		$(this).closest('.editing').removeClass('editing');
    		}, 200); 
    	})
    	.delegate('#pics .diyFileName [name="fileName"]','blur',function(){
    		var _this = this;
    		if(window._timerFILEName) window.clearTimeout(window._timerFILEName);
    		window._timerFILEName = window.setTimeout(function(){
    			Controller.saveDiyFileName($(_this));
        		$(this).closest('.editing').removeClass('editing');
    		}, 200);
    	})
    	.delegate('#pacs .imglist .thumbName','click',function(event){
    		var input = $(this).find('[name="fileName"]'),
    			txt = $.trim($(this).text()),d = txt.split('(')[0],n = $(this).find('i').text();
    		if(_utype !="1" || input.size() > 0 || event.target.type == 'button') return ;
    		$(this).addClass('editing').html('<textarea name="fileName" data-d="'+ d +'" data-v="'+ n +'">'+ n +'</textarea><button type="button" class="saveFileName">保存</button>');
    	})
    	.delegate('#pacs .imglist .thumbName .saveFileName','click',function(){
    		var _this = this;
    		if(window._timerDCIMName) window.clearTimeout(window._timerDCIMName);
    		window._timerDCIMName = window.setTimeout(function(){
        		Controller.saveDICMName($(_this).prev(':input'));
        		$(this).closest('.editing').removeClass('editing');
    		}, 200);
    	})
    	.delegate('#pacs .imglist .thumbName [name="fileName"]','blur',function(){
    		var _this = this;
    		if(window._timerDCIMName) window.clearTimeout(window._timerDCIMName);
    		window._timerDCIMName = window.setTimeout(function(){
        		Controller.saveDICMName($(_this));
        		$(this).closest('.editing').removeClass('editing');
    		}, 200);
    	})
    	.delegate('#uploadBySelf','click',function(){
    		Controller.showUploadDCIM();
    	});

    	$(window).bind("scroll",function(){
        	if(window._timers) window.clearTimeout(window._timers);
        	window._timers = window.setTimeout($.proxy(Controller.scrollFun,Controller),400);
        });
    	$(window).bind("resize",function(){
        	if(window._timers0) window.clearTimeout(window._timers0);
        	window._timers0 = window.setTimeout(function(){
        		$('#listable').find('.beantimelist').each(function(){
            		Controller.updateSwiper('#'+ this.id +' .swiper-container');
        		});
        		Controller.resizeGainDia();
        		Controller.scrollFun();
        		$('#LisModelDetail .formModelHeadOptions').size() && Controller.updateSwiper('#LisModelDetail .formModelHeadOptions');
        	},1);
        });    	
    };
});

define('Controller',['view/base'],{
	base:function(){
		return seajs.require('view/base')	
	},
	program:'<div class="progressc"><div class="barc" style="width:0%;"></div><div class="bartxt">0%</div></div>',
	loading:'<div class="loadings"><img alt="" style="" src="'+ window.location.origin +'/img/loading/31.gif"/></div>',
	inlineloading:'<div class="noresult"><img src="'+ window.location.origin +'/img/loading/31.gif" alt=""/></div>',
	noresult:'<div class="noresult"><img src="'+ window.location.origin +'/img/mobile/kbzs/icon_noresult@2x.png" style="width:30px" alt=""/></div>',
	reloadDiy:function(ul){
		var vls = [];
		ul.find('li').each(function(){
			var id = this.getAttribute('data-id') || '';    			
			id && vls.push(id);
		});		
		return vls.join(',');
	},
	serialArrToObj:function(arr){
		var obj = {};
		$.each(arr,function(i,o){
			obj[o.name] = (function(){
				var v = [];
				if(o.name in obj) v = obj[o.name].split(',');
				v.push(o.value);
				return v.join(',')
			})();
		});
		return obj;
	},
	imgDo:function(selector){
		$(selector).each(function(){					
			var imgs = $('.thumb img',this);
			imgs.each(function(){
				var img = $(this);
				img[0].onload = function() {							
					var w = img.width(),h = img.height();
					if(w > h){
						img.css({width:'auto',height:'100%','max-width':'inherit'});
					}else{
						img.css({width:'100%',height:'auto'});
					}
		        }
				if(img[0].complete){
					img[0].onload();
				}
			});
		});
	},
	formatDate:function(d){
		if(!d) return +new Date();
		d = d.split('/');
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
		    case 0:day="周日";
		      break;
		    case 1:day="周一";
		      break;
		    case 2:day="周二";
		      break;
		    case 3:day="周三";
		      break;
		    case 4:day="周四";
		      break;
		    case 5:day="周五";
		      break;
		    case 6:day="周六";
		      break;
		}
		return day;
	},
	loadDocPager:function(pageno,hid,did){
		var base = this.base(),_this = this;
		var hosid = hid || $('[name="hosid"]').val(),depid = did || $('[name="depid"]').val();
		base.showLineLoading();
		base.get('docadmin/gainspecials',{hosid:hosid,sdepid:depid,pageNumber: pageno || 1,pageSize:15},function(d){
			var pager = d.pager;
			pager['hosid'] = hosid;
			pager['depid'] = depid;
			_this.fillSpecList(pager);
		});		
	},
	fillSpecList:function(pager){
		var base = this.base(),_this = this;
		nodetpl.get('pc_special_advice_list.htm', {
			base: _h,
			pager:pager
		}, function(d) {
			$('.section3 .exportslist').html(d);
			_this.imgDo('.section3 .exportslist .doslist');
			base.hideLineLoading();
		});
	},
	initDocTimeLists:function(selector,sid){
		var base = this.base(),_this = this,totalday = 7,$selector = $(selector);
		base.showLineLoading();
		$selector.html(function(){
			var today = new Date(),day = today.getDate(),week,cdate,ymd,md,dls = '';
			for(var i = 0;i < totalday;i++){
				cdate = new Date(),cdate.setDate(day + i);
				md = (cdate.getMonth() + 1) +'/' + cdate.getDate();
				ymd = _this.formatDate(cdate.getFullYear() + '/'+ md);
				week = _this.getWeek(cdate.getDay());
				dls += '<dl data-ymd="'+ ymd +'" data-week="'+ week +'" class="swiper-slide timelist"><dt>'+ md +'（'+ week +'）</dt></dl>';
			}
			return dls;
		}).find('dl[data-ymd]').each(function(){
			var ymd = $(this).attr('data-ymd');
			_this.initDocTimeList(sid,ymd,function(dd){
				totalday--;
				!totalday && base.hideLineLoading();
				$selector.find('dl[data-ymd="'+ ymd +'"]').append(dd || '<dd>09:00</dd><dd>10:00</dd><dd>14:00</dd><dd>16:00</dd><dd>17:00</dd>');
			});
		});
	},
	initDocTimeList:function(sid,sdate,callback){
		var base = this.base(),_this = this;
		base.post('wzjh/gainSpecialTimes',{sid:sid,sdate:sdate},function(d){
			var times = d.times || [];			
			callback((function(){
				var dds = '';
				$.each(times,function(i,o){
					dds += '<dd data-id="'+ o.id +'" data-a="'+ (o.startTime < '12:00' ? '上午' : '下午') +'" data-t="'+ o.startTime 
						+'" data-c="'+o.cost+'" class="'+ (o.hasAppoint == '1' ? 'disabled' : 'open') +'">'+ o.startTime +'</dd>';
				});
				return dds;
			})());
		},function(){
			callback();
		});
	},
	ajaxMoreInfo:function(href,callback){
		var base = this.base(),_this = this;
		base.showLineLoading();
		base.get(href,{},function(d){
			var data = d.hospitals || d.sdeps || {};			
			callback((function(){
				var dds = '';
				$.each(data,function(i,o){
					dds += '<dd class="filteDoc" title="'+ o.displayName +'" data-type="'+ ('bigDepId' in o ? 'dep' : 'hos') +'" data-id="'+ o.id +'">'+ o.displayName +'</dd>';
				});
				return dds;
			})());
			base.hideLineLoading();
		},function(){
			callback('');
			base.hideLineLoading();
		});
	},
	validateStep:function(step){
		var base = this.base(),_this = this;
		var expid = $('[name="expertId"]').val(),
			timid = $('[name="stimeid"]').val(),
			username = $('[name="username"]').val(),
			telphone = $('[name="telphone"]').val(),
			age = $('[name="age"]').val(),
			idcard = $('[name="idcard"]').val(),
			oid = $('[name="oid"]').val();
		var arr = step.split('|');
		switch(step){
			case '-1':
				var href = $('.form-action [data-step="-1"]').attr('data-href');
				href && window.parent.triggermenu && window.parent.triggermenu(href);
				return false;
			case '2':
				if(!username) return base.showTipE('请输入姓名'),false;
				if(!base.valideTel(telphone)) return base.showTipE('请输入有效的电话'),false;
				if(!age) return base.showTipE('请输入有效年龄'),false;
				if(idcard && !base.valideCard(idcard)) return base.showTipE('请输入有效身份证号'),false;
				return this.subtimeHelpOrderbase(),false;
			case '2-1':
				step = 2;
				break;
			case '3':
				$('[name="expertId"],[name="pmoney"]').val(''),$('.section3 .doslist.selected').removeClass('selected');
				this.loadDocPager('1','','');
				break; 
			case '3-1':
				step = 3;
				break;
			/*case '4|5':
				!!expid ? 
					(step = 4,this.initDocTimeLists('.doctimes .swiper-wrapper',expid)) : (step = 5);
				if(step == 5) return this.creatHelpOrderWXurlCodeWithoutExport(),false;
				break;*/
			case '5':
				if(!expid) return base.showTipE('请选择专家'),false;
				return this.creatHelpOrderWXurlCode(),false;
			case '5-1':
				step = 5;
				break;
			case '6':
				return this.listenWxPay(),false;
				break;
			case '6-1':
				step = 6;
				break;
		}
		$('.section' + step).show().siblings().hide();
		for(var i = 1;i < step;i++){
			$('.topheader .state' + i).addClass('passed').removeClass('animate');
		}
		$('.topheader .state' + step).addClass('passed animate').nextAll().removeClass('passed animate');
		step == '3' && this.imgDo('.dochelporder .doslist');
	},
    listenWxPay:function(){
    	var base = this.base(),_this = this;
    	var tradeno = $('#myform [name="out_trade_no"]').val();
    	tradeno && base.post('kangxin/listenpaystatus_tw',{tradeno:tradeno},function(d){
    		if(d.status == 'success'){
    			_this.validateStep('6-1');
    		}else{
    			window.setTimeout(function(){
    				_this.listenWxPay();
    	    	},3000);
    		}
    	});
    },
	creatHelpOrderWXurlCode:function(){
		//this.refreshHidden();
		this.creatHOWXurl('docadmin/continuetopay');
	},
	creatHelpOrderWXurlCodeWithoutExport:function(){
		//this.refreshHidden();
		//this.creatHOWXurl('docadmin/subnoexpertorder');
	},
	creatHOWXurl:function(url){
		var base = this.base(),data = $('#myform').serializeArray(),_this = this;		
		base.showTipIngA('正在保存').post(url,data,function(d){
			var cost = $('[name="pmoney"]').val() || '',src;
			if(d.pay == 'true'){
				src = _burl + 'kangxin/showqr?code_url=' + d.code_url;
				base.hideTip();
				d.code_url && $('.section5 .imgerm img').attr('src',src);
				d.code_url && $('.section5 .pay h2').html(cost.indexOf('.') != -1 ? cost : (cost + '.00'));
				d.code_url && $('[name="out_trade_no"]').val(d.out_trade_no);
				_this.validateStep(d.code_url ? '5-1' :'6-1');
			}
			else{
				_this.validateStep('6-1');
			}
		},function(){
			base.showTipE('保存失败');
		});
	},
	subtimeHelpOrderbase:function(){
		var base = this.base(),data = $('#myform').serializeArray(),_this = this;
		//var d = {oid:'20203',uuid:'5161026000010',caseid:'5349'};
		base.showTipIngA('正在保存').post('docadmin/nextadvice',data,function(d){
			base.hideTip(),
			!_orderid && (_this.initUploader(d.oid),_this.goEasy(d.uuid)),
			_orderid = _oid = d.oid,
			_uuid = d.uuid,
			$('[name="oid"]').val(d.oid),
			$('[name="caseid"]').val(d.caseid),
			$('[name="uuid"]').val(d.uuid);
			_this.validateStep('2-1');
		},function(){
			base.showTipE('保存失败');
		});
	},
	initUploader:function(orderid){
		var _this = this;
		seajs.use('view/webupload',function(upload){
			$('#pics #addfiles').each(function(){
				$(this).Uploader({
	        		server: _href + 'doctor/uploadLocalFile',
	        		formData: { orderid: orderid },
	        		thumb: {
		                width: 60,
		                height: 60
		            },
		            accept:{
		            	title: "Files",
		                extensions: "gif,jpg,jpeg,bmp,png,tif,mp4,avi,webm,mkv,mov,rm,ogg.ogv",
		                mimeTypes: "image/*,video/*"
		            },
	        		success:function(liobj,val){
	        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
	        			hide.val(_this.reloadDiy(ul));        			
	        		},
	        		afterpost:function(){
	        			_diyUploadSelector.viewer('destroy').viewer({
	        				url: 'data-src'
	        			});
	        		}
	        	});
			});        	
		});
		seajs.use('view/viewer',function(view){
    		_diyUploadSelector = view.init('.userinfo .diyUpload .fileBoxUl',{ 
    			navbar:true
    		});
    	});
	},
	loadBeans:function(url,sign,bol){
		var _this = this,base = _this.base();
		base.showDialog({
			id:'FormModelEdit',
			title:'病历列表',
			text:bol ? _this.queryWhere() : _this.queryWhereonly(),
			nofooter:true
		});
		!bol && base.post(_href + url,{oid:_orderid,idsearch:'true',syncSeries:sign},function(d){	
			if(_listSign != sign) return false;
			var beans = d.beans || [];
			if(beans.length <=0 ) _idsearch = "false";
			$('#FormModelEdit .modal2-body').find('.noresult').remove().end().append(_this.ansysBeans(d));
		},function(){
			if(_listSign != sign) return false;
			base.showTipE('加载失败').hideDialog('FormModelEdit');
		});

    	$('#FormModelEdit').on('hide.bs.modal', function (e) {
    		$('body').removeClass('modal2-open')
    	});
	},
	loadPacsBeans:function(url){
		var _this = this,base = _this.base();
		base.showDialog({
			id:'FormModelEdit',
			title:'影像列表',
			text:_this.queryWherePacs(),
			nofooter:true
		});
    	$('#FormModelEdit').on('hide.bs.modal', function (e) {
    		$('body').removeClass('modal2-open')
    	});
	},
	loadPacsWithWhere:function(url,callback,sign){
		var _this = this,base = _this.base(),
			data = this.getQueryWhere();
		$('#FormModelEdit .beanquerywhere').siblings().remove().end().after(_this.inlineloading);
		data['orderid'] = _oid;
		base.post(_href + url,data,function(d){	
			if(_listSign != sign) return false;
			$('#FormModelEdit .beanquerywhere').siblings().remove().end().after(_this.ansysPacsTable(d));
			callback();			
		},function(){
			if(_listSign != sign) return false;
			base.showTipE('加载失败');
			callback();	
		});
	},
	loadBeansWithWhere:function(url,callback,sign){
		var _this = this,base = _this.base(),
			data = this.getQueryWhere();
		$('#FormModelEdit .beanquerywhere').siblings().remove().end().after(_this.inlineloading);
		base.post(_href + url,data,function(d){	
			if(_listSign != sign) return false;
			$('#FormModelEdit .beanquerywhere').siblings().remove().end().after(_this.ansysBeans(d));
			callback();			
		},function(){
			if(_listSign != sign) return false;
			base.showTipE('加载失败');
			callback();		
		});
	},
	getQueryWhere:function(){
		var input = {};
		$('.beanquerywhere :input').each(function(){
			input[this.name] = this.value;
		});
		input['idsearch']=_idsearch;
		return input;
	},
	queryWhere:function(){
		return '<div class="beanquerywhere row-fluid">'
			+ '<div class="form-group span2"><label class="form-label">姓名：</label><div class="form-inputs"><input type="text" name="searchName"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">病人ID：</label><div class="form-inputs"><input type="text" name="searchPatientId"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">住院号：</label><div class="form-inputs"><input type="text" name="adminsionNum"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">门诊号：</label><div class="form-inputs"><input type="text" name="outpatientNum"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">科室名：</label><div class="form-inputs"><input type="text" name="department"/></div></div>'
			+ '<div class="form-group span2"><div class="form-action"><button type="button" class="filtBtn filtDetail">检索</button><button type="button" class="sysncBtn tablebtn disabled">同步</button></div></div>'
			+'</div>'
	},
	queryWherePacs:function(){
		return '<div class="beanquerywhere has3button row-fluid">'
			+ '<div class="form-group span2"><label class="form-label">患者姓名：</label><div class="form-inputs"><input type="text" name="patientName"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">检查编号：</label><div class="form-inputs"><input type="text" name="checkNo"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">影像类型：</label><div class="form-inputs"><input type="text" name="checkType"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">缴费单号：</label><div class="form-inputs"><input type="text" name="mzNumber"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">住院号：</label><div class="form-inputs"><input type="text" name="patientId"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">日期：</label><div class="form-inputs"><input type="text" ltype="date" name="regtime"/></div></div>'
			+ '<div class="form-group span2"><div class="form-action"><button type="button" class="filtBtn filtPacsDetail">检索</button>'
			+ '<button type="button" title="追加到已同步数据之后（去重）" class="sysncBtn tablebtnadd disabled">追加同步</button><button type="button" title="覆盖之前已同步的数据" class="sysncBtn tablebtnover disabled">覆盖同步</button></div></div>'
			+'</div>'
	},
	queryWhereonly:function(){
		return '<div class="beanquerywhere row-fluid">'
			+ '<div class="form-group spanonly"><div class="form-action"><button type="button" class="sysncBtn tablebtn disabled">同步</button></div></div>'
			+'</div>'
			+ this.inlineloading;
	},
	ansysBeans:function(d){
		var tables = '<table class="table beantable"><thead><tr><th>病人ID</th><th>姓名</th><th>性别</th><th>年龄</th><th>科室</th><th>门诊日期</th><th style="width:6.5em;">选择</th></tr></thead><tbody>{0}</tbody></table>';
		var trs = '',tr,kvs,beans = d.beans || [];
		for(var i = 0,l = beans.length;i<l;i++){
			tr = d.beans[i],kvs=tr['kvs'];
			trs += '<tr>';
			trs += '<td>'+ kvs['病人ID'] +'</td>';
			trs += '<td>'+ kvs['姓名'] +'</td>';
			trs += '<td>'+ kvs['性别'] +'</td>';
			trs += '<td>'+ kvs['年龄'] +'</td>';
			trs += '<td>'+ kvs['病人科室'] +'</td>';
			trs += '<td>'+ kvs['门诊日期'] +'</td>';
			trs += '<td><input type="checkbox" class="tablechk" value="'+ kvs['病人ID'] +'"/></td>';
			trs += '</tr>';
		}
		tables = tables.replace('{0}',trs);
		
		return tables;
	},
	ansysPacsTable:function(d){
		var tables = '<table class="table beantable"><thead><tr><th>姓名</th><th>病人ID</th><th>交费单号</th><th>检查编号</th><th>日期</th><th style="width:20%">检查项</th><th>影像类型</th><th>选择</th></tr></thead><tbody>{0}</tbody></table>';
		var trs = '',tr,kvs,beans = d.pacbeans || [];
		for(var i = 0,l = beans.length;i<l;i++){
			tr = d.pacbeans[i],kvs=tr['kvs'];
			trs += '<tr>';
			trs += '<td>'+ kvs['Patient_Name'] +'</td>';
			trs += '<td>'+ kvs['Patient_ID'] +'</td>';
			trs += '<td>'+ kvs['MZ_Number'] +'</td>';
			trs += '<td>'+ kvs['Check_No'] +'</td>';
			trs += '<td title="'+ kvs['REGISTER_DATE'] +'">'+ kvs['REGISTER_DATE'] +'</td>';
			trs += '<td title="'+ kvs['Check_Item_E'] +'">'+ kvs['Check_Item_E'] +'</td>';
			trs += '<td>'+ kvs['Modality'] +'</td>';
			trs += '<td><input type="checkbox" class="tablechk" value="@'+ kvs['Modality'] +':'+ kvs['Image_Directory'] +'"/></td>';
			trs += '</tr>';
		}
		tables = tables.replace('{0}',trs);
		
		return tables;
	},
	anyscAjaxLis:function(beforecallback,callback,sign){
		var _this = this,base = _this.base();
		beforecallback();
		base.get(_href + 'doctor/gainLisData',{oid:_orderid,syncSeries:sign},function(d){	
			var lishtm;
			if(sign != _lisSign) return false;
			lishtm = d.records ? _this.ansysLisBeansWithTime(d.records) : _this.noresult;
			callback(lishtm);
		},function(){
			if(sign != _lisSign) return false;
			callback(_this.noresult);
			base.showTipE('LIS拉取数据失败');
		});
	},
	anyscAjaxPacs:function(beforecallback,callback,sign){
		var _this = this,base = _this.base();
		beforecallback();
		base.get(_href + 'doctor/gainPacsData',{uuid:_uuid,asktype:'true',syncSeries:sign},function(d){	
			var pashtm;
			if(sign != _pacsSign) return false;
			pashtm = d.pac_records ? _this.ansysPacsBeansWithSign(d.pac_records) : _this.noresult;
			callback(pashtm);
		},function(){
			if(sign != _pacsSign) return false;
			callback(_this.noresult);
			base.showTipE('PACS拉取数据失败');
		});
	},
	loadLis:function(url,pid,callback,sign){
		var _this = this,base = _this.base();
		base.showTipIngA('正在同步').hideDialog('FormModelEdit').post(_href + url,{oid:_orderid,patientid:pid,syncSeries:sign},function(d){	
			if(sign != _lisSign) return false;
			base.showTipS('LIS同步成功');
			callback(d.lisbeans ? _this.ansysLisBeansWithTime(d.lisbeans) : _this.noresult);
		},function(){
			if(sign != _lisSign) return false;
			callback(_this.noresult);
			base.showTipE('LIS同步失败');
		});
	},
	loadPacs:function(url,pid,callback,sign){
		var _this = this,base = _this.base();
		base.showTipIngA('正在同步').hideDialog('FormModelEdit').post(_href + url,{oid:_orderid,patientid:pid,syncSeries:sign},function(d){	
			if(sign != _pacsSign) return false;
			base.showTipS('PACS同步成功');
			callback('');
		},function(){
			if(sign != _pacsSign) return false;
			callback(_this.noresult);
			base.showTipE('PACS同步失败');
		});
	},
	loadPacsWithQuery:function(url,data,callback,sign){
		var _this = this,base = _this.base();
		base.showTipIngA('正在同步').hideDialog('FormModelEdit').post(_href + url,data,function(d){	
			if(sign != _pacsSign) return false;
			base.showTipS('PACS同步成功');
			callback('');
		},function(){
			if(sign != _pacsSign) return false;
			callback(_this.noresult);
			base.showTipE('PACS同步失败');
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
		//console.log('ansysLisBeans', JSON.stringify(d, null, '\t'));
		$.each(d,function(i,o){
			var reportlist = '<div class="reportlist"></div>',
				reportModel = $('#report_model').html(),trsO = [],trsE = [],half = o.beans.length / 2;
			var delimg = _utype == '1' ? '<span class="imgdel iconfont">&#xe60b;</span>' : '';
			reportModel = reportModel.replace('{record_name}',o['kvs']['record_name']);
			
			$.each(o.beans,function(ii,b){
				var bkvs = b['kvs'],sx = bkvs['缩写'] || '',sxarr = sx.split('('),trs = '';
				trs += '<tr><td>'+ sx +'</td>';
				trs += '<td>'+ bkvs['中文名'] +'</td>';
				trs += '<td>'+ bkvs['检验结果'] +'</td>';
				trs += '<td style="text-align:center;">'+ (bkvs['标志'] == 'None' ? '' : bkvs['标志']) +'</td>';
				trs += '<td>'+ bkvs['参考区间'] +'</td>';
				trs += '<td>'+ bkvs['单位'] +'</td></tr>';
				if(!ii){
					$.each(bkvs,function(x,y){
						reportModel = reportModel.replace('{'+ x +'}',bkvs[x]);
					});
				}
				ii < half ? trsO.push(trs) : trsE.push(trs);
			});
			reportModel = reportModel.replace('{tbody}',trsO.join(''));
			reportModel = reportModel.replace('{tbody1}',trsE.join(''));
			rls.unshift('<div class="reportlist swiper-slide" data-id="'+ o['key'] +'">'+ reportModel +'<label class="re_title">'+ o['kvs']['record_name'].replace('报告单',' ' + o['remark'])  +'</label>'+ delimg +'</div>');
		});
		return rls.join('');
	},
	creatHead:function(ele,idx){
		var $parent = $(ele).closest('.beantimelist'),$rplist = $parent.find('.reportlist'),
			pid = $parent.attr('id'),head = '<div class="formModelHeadOptions"><div class="swiper-wrapper">{o}</div></div>',ops = [];
		if($rplist.size() == 1) return '';
		$rplist.each(function(i){
			var head = $('.re_title',this).text();
			ops.push('<div class="swiper-slide fmhoption'+ (idx == i ? ' selected' : '') +'" data-id="'+ pid +'-'+ i +'">'+ head.split(' ')[0] +'</div>');
		});
		return head.replace('{o}',ops.join(''));
	},
	ansysPacsBeansWithSign:function(d){
		var signlist = [],_this = this;
		$.each(d,function(i,o){
			var div = '<div class="blocklist pacsSignlist clearfix '+ o.key +'" data-val="0">';
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
			pacs += '<div class="imglist" data-id="'+ o['key'] + '" data-rid="'+ o.kvs['patient_id'] + '|'+ o.studyId + '" data-val="'+ _this.ansysUrl(o) + '">';
			pacs += '<div class="thumb">';
			//pacs += '<img src="'+ _this.ansysUrl(o) + '"/>';
			pacs += '</div>';
			_utype == '1' && (pacs += '<span class="imgdel iconfont">&#xe60b;</span>');
			pacs += '<label class="thumbName">'+ o['remark'] +'(<i>'+ o.kvs['Check_Item_E'] + '</i>)</label>';
			pacs += '</div>';
		});
		return pacs;
	},
	initSwiper:function(selector){
		seajs.use('view/swiper',function(swiper){
			_swipers[selector] = swiper.init(selector,{
    			slidesPerView: 'auto',
		        spaceBetween: 0,
		        freeMode: true,
		        nextButton: '.swiper-button-next',
		        prevButton: '.swiper-button-prev'
    		});
    	});		
	},
	updateSwiper:function(selector){
		_swipers[selector] && _swipers[selector].update();
	},
	showDia:function(html){
		var _this = this,base = _this.base();		
		base.hideDialog('LisModelDetail').showDialog({
			id:'LisModelDetail',
			text:html,
			nofooter:true
		});
    	$('#LisModelDetail').on('hide.bs.modal', function (e) {
    		$('body').removeClass('modal2-open')
    	});
    	$('#LisModelDetail .formModelHeadOptions').size() && seajs.use('view/swiper',function(swiper){
    		var newswp = _swipers['#LisModelDetail .formModelHeadOptions'] = swiper.init('#LisModelDetail .formModelHeadOptions',{
    			slidesPerView: 'auto',
		        spaceBetween: 0,
		        freeMode: true
    		});
    		window.setTimeout(function(){
    			var indexofa = $('.formModelHeadOptions .selected').index();
        		newswp.update();
    			indexofa && newswp.slideTo(indexofa, 1000, false);
    		},400);
    	});
	},
	showEleDia:function(txt){
		var _this = this,base = _this.base();		
		base.showDialog({
			id:'showEleDia',
			text:'<iframe name="videojsiframe" src="'+ _href +'sea-modules/videojs/examples/index.html?src='+ txt +'" frameborder="no" border="0" allowscriptaccess="always" marginwidth="0" marginheight="0" allowfullscreen="true" wmode="opaque" allowTransparency="true" type="application/x-shockwave-flash"></iframe>',
			cls:'modal2-600',
			nofooter:true
		});
    	$('#showEleDia').on('hide.bs.modal', function (e) {
    		$('body').removeClass('modal2-open');
    		window.setTimeout(function(){
    			$('#showEleDia').remove();
    		},400);
    	});
	},
	resizeGainDia:function(){
		var height = $(window).height();
		$('#showDWVModel .dwvContainer').css({height: height + 'px',overflow:'hidden'}); 
	},
	loadGainPics:function(href){
		var _this = this,base = _this.base();
		base.showDialog({
			id:'showDWVModel',
			cls:'modal2-auto',
			text:_this.ansysSeries(href),
			nofooter:true
		});  
    	$('#showDWVModel').on('hide.bs.modal', function (e) {
    		var onlyHeader = window.parent.onlyHeader || function(){};
    		$('body').removeClass('modal2-open');
    		onlyHeader('esc');
    	}); 
    	$('#showDWVModel').on('shown.bs.modal', function (e) {
    		var onlyHeader = window.parent.onlyHeader || function(){};
    		onlyHeader();
    	}); 
	},
	ansysSeries:function(href){
		var height = $(window).height();
		return '<div class="row-fluid dwvOuter"><div class="dwvContainer prelative span12" style="overflow:hidden;height:'+ height +'px"><iframe name="dwviframe" src="'+ href +'" id="dwviframe" frameborder="no" border="0" allowscriptaccess="always" marginwidth="0" marginheight="0" allowfullscreen="true" wmode="opaque" allowTransparency="true" type="application/x-shockwave-flash"></iframe></div></div>';
	},
	ansysUrl:function(o){
		return _href + 'dcmimage?study='+ o.studyId +'&series='+ o.seriesId +'&object='+ o.instanceId;
	},
	refreshHidden:function(){
		var pics = [],his = [],lis = [],pacs = [];
		$('#pics li').each(function(){
			var id = this.getAttribute('data-id');
			id && pics.push(id);
		});
		$('#pics_hidden').val(pics.join(','));
		
		$('#listable .reportlist').each(function(){
			var id = this.getAttribute('data-id');
			id && lis.push(id);
		});
		$('#lis_ids').val(lis.join(','));
		
		$('#pacs .imglist').each(function(){
			var id = this.getAttribute('data-id');
			id && pacs.push(id);
		});
		$('#pacs_ids').val(pacs.join(','));
	},
	hasAsync:function(){
		//用于同步时，没有同步完不能保存
		return true;
	},
    saveDiyFileName:function(input){
    	var dev = input.attr('data-v'),v = input.val(),
    		p = input.parent(),fid = input.closest('li').attr('data-id') || '';
    	var _this = this,base = _this.base();		
    	if(dev == v)
    		p.html(v);
    	else{
    		fid &&　base.showTipIngA('正在保存更改')
			.post(_href + 'doctor/updatefname',{fid:fid,filename:v},function(d){			
				base.showTipS('文件名更改成功');
				p.html(v);
			},function(){
				base.showTipE('文件名更改失败');
			});
    	}
    },
    saveDICMName:function(input){
    	var dev = input.attr('data-v'),dtim = input.attr('data-d'),v = input.val(),
			p = input.parent(),fid = input.closest('.imglist').attr('data-id') || '';
		var _this = this,base = _this.base();		
		if(dev == v)
			p.html(dtim + '(<i>'+ (dev || '未知') +'</i>)');
		else{
			fid &&　base.showTipIngA('正在保存更改')
			.post(_href + 'doctor/compleupcheckitem',{pid:fid,itemname:v,oid:_uuid},function(d){			
				base.showTipS('文件名更改成功');
				p.html(dtim + '(<i>'+ (v || '未知') +'</i>)');
			},function(){
				base.showTipE('文件名更改失败');
			});
		}
	},
	scrollFun:function(){
		var _this = this;
		var st = $(window).scrollTop(), sth = st + $(window).height();
		/***图片滚动加载****/
		$('.imglist[data-val]').each(function(){
			var o = $(this), post = o.offset().top, posb = post + o.height();
	        if ((post > st && post < sth) || (posb > st && posb < sth)) {
	           o.attr('data-val') && (function(item){
	        	   var src = item.attr('data-val');
	        	   var iframe = '<img alt="影像图片" src="'+ src +'" />';
	        	   if(src.indexOf('_=none') != -1){
	        		   iframe += '<label class="nopic">影像图片不存在</label><div class="bg"></div>'
	        	   }else if(src.indexOf('_=stop') != -1){
	        		   iframe += '<label class="nopic">同步操作已终止</label><div class="bg"></div>'
	        	   }
	        	   item.removeAttr('data-val').find('.thumb').html(iframe);
	           })(o);
	        }
		});			
	},
	showUploadDCIM:function(){
		var _this = this,base = _this.base();		
		base.showDialog({
			id:'showUploadDCIM',
			text:$('#uploadDCIMTemp').html(),
			cls:'modal2-lg',
			nofooter:true
		});
		seajs.use('view/vedio/uploadDCIM',function(upload){
			upload.init({
				server: _href + 'doctor/updcmfile',
				threads:1,
        		formData: { oid: _orderid, asktype : 'true'},
        		success:function(){
        			base.showTipS('DCIM文件上传成功').hideDialog('showUploadDCIM');
        		}
			});
		});
    	$('#showUploadDCIM').on('hide.bs.modal', function (e) {
    		$('body').removeClass('modal2-open');
    		window.setTimeout(function(){
    			$('#showUploadDCIM').remove();
    		},400);
    	});
	},
	goEasy:function(channelid){
		goEasy = new GoEasy({
           	appkey: '78028e7e-edcc-4524-b56b-45639785a53a'
        });
      	goEasy.subscribe({
            channel: channelid,
            onMessage: function(message){
            	var content = message.content.replace(/&quot;/g,"\"");
            	var jmessage = JSON.parse(content);  
            	var _type    =  jmessage.type;
            	var _from    =  jmessage.from,
            		_result  =  jmessage.result || '加载无数据'; 
       			//console.log('jmessage', JSON.stringify(jmessage, null, '\t'));
            	
            	if (_type =='reportNotify' && _from !=_utype ){
            		reportmsg(_result),reportmsgAnmiation(_result);           	 	
            	} 
            	else if (_type =='syncPacs' && _from ==_utype ){
            		setPacsProgram(jmessage);
            	}
            	else if (_type =='syncPacsOut' && _from ==_utype ){
            		!!_signSyncBtn ? setPacListWithType1(jmessage) : setPacListWithType0(jmessage);
            	}
            	else if (_type =='syncLis' && _from ==_utype ){
            		setLisProgram(jmessage);
            	}
            	else if (_type =='updcm'){
            		setCustomPacs(jmessage);
            	}
            }
        });
	}
});

