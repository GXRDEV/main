define(['view/vedio/controller'],function(require, exports, module){
	var Controller = require('view/vedio/controller');
    exports.init = function(){
    	$('body').keydown(function(e){
		    var e = e||event;
		    var currKey = e.keyCode||e.which||e.charCode;
		    currKey == 27 && Controller.doFullScreen('esc');
    	});
    	$('body')
    	.delegate('.userinfo .control-group textarea','focus',function(){
    		var sh = this.scrollHeight + 20,v = $(this).val() || '';
    		v.length > 20 && $(this).animate({height:(sh < 180 ? 180 : sh) + 'px'},300);
    	})
    	.delegate('.userinfo .control-group textarea','blur',function(){
    		$(this).animate({height:'3.5em'},300);
    	})
    	.delegate('#stateBar .fullscreen','click',function(){
    		Controller.doFullScreen('');
    	}).delegate('.btn-group ul a','click',function(){
    		var out = $(this).closest('.btn-group');
    		out.find('a:first').html(this.innerHTML);
    		out.removeClass('open');
    		return false;
    	}).delegate('#sycnCase','click',function(){
    		var ajaxurl = this.getAttribute('data-ajax'),
    			_btn = $(this),sign = +new Date;
    		_listSign = sign;
    		ajaxurl && Controller.loadBeans(ajaxurl,sign,false);
    		return false;
    	}).delegate('#sycnCaseMore','click',function(){
    		var ajaxurl = this.getAttribute('data-ajax');
			ajaxurl && Controller.loadBeans(ajaxurl,'',true);
			return false;
		}).delegate('#sycnPacsMore','click',function(){
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
    		var _this = this;
    		if(window._timerImg) window.clearTimeout(window._timerImg);
    		window._timerImg = window.setTimeout(function(){
        		var lst = $(_this).closest('.imglist'),rid = lst.attr('data-rid') || '',ridArr = rid.split('|'),href = '';
        		if(!rid || rid.indexOf('null') != -1 || rid.indexOf('undefined') != -1) return false;
        		$('#pacs .imglist.curr').removeClass('curr'),lst.addClass('curr');
        		href = location.origin + '/dwv/viewer.html?patientID='+ ridArr[0] +'&studyUID='+ ridArr[1] +'&serverName=';
    			Controller.loadGainPics(href,function(zidx){
        			$('#vedioOuter').css({'z-index':zidx + 2 });
    			});
    		},200);    		
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
    	.delegate('.btn-save','click',function(){
    		if($(this).attr('disabled')) return false;
    		if(!Controller.hasAsync()) return false;
    		if(!Controller.validaForm()) return false;    		
    		Controller.refreshHidden();
    		Controller.saveForm(function(dd){
    			var bol = !(_protag == '3' || _protag == '4' || _protag == '5');
    			dd && Controller.sendForm(bol ? '2' : _protag,function(d){
    				d && bol && intprotag && intprotag('2');
        		});
    		});
    	})
    	.delegate('.btn-send','click',function(){
    		if($(this).attr('disabled')){return false}
    		Controller.sendForm('2',function(d){
    			d && intprotag && intprotag('2');
    		});
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
    		var _this = this;
    		if(window._timerImg) window.clearTimeout(window._timerImg);
    		window._timerImg = window.setTimeout(function(){
	    		var html = $(_this).html(),index = $(_this).index(),
	    			$parent = $(_this).closest('.beantimelist'),
	    			pindex = $parent.index(),
	    			total = $('#listable .beantimelist').size(),
	    			_html = html.replace('<div class="report small">','<div class="report">');
	    		var f = '',l = '',h = '';
	    		pindex != 0 ? (f = '<div data-val="'+ (pindex - 1) +'" class="prevreport reportbtn"><i class="iconfont">&#xe61f;</i></div>') : (f = '');
	    		pindex != (total - 1) ? (l = '<div data-val="'+ (pindex + 1) +'" class="nextreport reportbtn"><i class="iconfont">&#xe622;</i></div>') : (l = '');
	    		h = Controller.creatHead(_this,index);
	    		Controller.showDia(h + f + _html + l);
			},200); 
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
    	.delegate('#resizeheight','mousedown',function(event){
    		var event = event || window.event,textarea = $('#reporttxt .fillbaogao');
    		window._drag = true;
    		window._dragStartY = event.clientY;
    		window._height = textarea.height();
    	})
    	.mousemove(function(event){
    		var _y,_h;
    		if(window._drag) {
    			_y = window._dragStartY - event.clientY,
    			_h = window._height;
    			$('#reporttxt .fillbaogao').css('height',(_h + _y) + 'px');
    		}
    	})
    	.mouseup(function(event){
    		window._drag = false;
    	})
    	.delegate('#resizeheight .closebtn','click',function(){
    		$('#reporttxt').removeClass('center').fadeOut();
    		$('#reportbtn').fadeIn();
    	})
    	.delegate('#reportbtn .btncircle','click',function(){
    		$('#reporttxt').slideDown();
    		$('#reportbtn').fadeOut();
    	})
    	.delegate('#reporttxt .savereport','click',function(){
    		var text = $.trim($('#reporttxt .fillbaogao').val());
    		Controller.saveReport(text,function(bol){
    			bol && reportmsg(text,'me');
    		});
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
    	})
    	.delegate('.viewer-canvas','click',function(event){
    		event.target.localName != 'img' && _diyUploadSelector.viewer('hide');
    	})
    	.delegate('#miniVideo','click',function(event){
    		var remot = $('#remoteVideo').attr('src'),
    			min = $(this).attr('src');
    		$('#remoteVideo').attr('src',min);
    		$(this).attr('src',remot);
    	});
    	//$('#miniVideo').draggable();
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
    	//初使化加载His/Pacs等数据
    	(function(sign){
    		Controller.anyscAjaxLis(function(){
    			_lisSign = sign;
        		$('#listable').html(Controller.loading);
        	},function(lishtm){
        		$('#listable').html(lishtm).find('.beantimelist').each(function(){
            		Controller.initSwiper('#'+ this.id +' .swiper-container');
        		});
        	},sign);
        	Controller.anyscAjaxPacs(function(){
        		_pacsSign = sign;
        		$('#pacs').html(Controller.loading);
        	},function(pachtm){
    			$('#pacs').html(pachtm);
    			Controller.scrollFun();
        	},sign);
    	})(+new Date);    	
    	
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