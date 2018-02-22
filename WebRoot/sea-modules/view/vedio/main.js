//数组去重复
var _idsearch="true",_diyUploadSelector;
Array.prototype.uniques = function() { var res = [], hash = {};for(var i=0, elem; (elem = this[i]) != null; i++)  {if (!hash[elem]){res.push(elem);hash[elem] = true;}}return res;}
define(function(require, exports, module) {
	var Controller = require('Controller');
	seajs.use('view/webupload',function(upload){
		$('#pics #addfiles').each(function(){
			$(this).Uploader({
        		server: _href + 'doctor/uploadLocalFile',
        		formData: { orderid: _orderid },
        		thumb: {
	                width: 60,
	                height: 60,
	                quality: 70,
	                allowMagnify: false,
	                crop: true,
	                type: "image/jpeg"
	            },
	            accept:{
	            	title: "Files",
	                extensions: "gif,jpg,jpeg,bmp,png,tif,mp4,avi,webm,mkv,mov,rm,ogg.ogv",
	                mimeTypes: "image/*,video/*"
	            },
	            beforepost:function(btn){
	            	var pbtn = $('#btngroups');
	            	pbtn.addClass('disabled');
        		},
        		afterpost:function(btn){
        			var pbtn = $('#btngroups');
        			pbtn.removeClass('disabled');
        		},
        		success:function(liobj,val){
        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
        			hide.val(Controller.reloadDiy(ul));        			
        		},
        		afterpost:function(){
        			_diyUploadSelector.viewer('destroy').viewer({
        				navbar:true,
            			url:'data-src'
        			});
        		}
        	});
		});        	
	});
	seajs.use('view/vedio/chatfile',function(upload){
		$('#selectfile').uploadsingle({
    		server: _href + 'doctor/uploadLocalFile',
    		formData: { orderid: _orderid },
    		thumb: {
                width: 80,
                height: 100,
                quality: 70,
                allowMagnify: false,
                crop: true,
                type: "image/jpeg"
            },
    		success:function(liobj,val){
    			var li = '<div class="thumbimg"><div class="viewThumb"><img src="'+ val +'"/></div></div>'
    			sendMessage({"type":"chart","txt":li});
    			seajs.use('view/viewer',function(view){
    				view.init('.dialog .bodyer');
    	    	});
    		}
    	});
	});
	require('bindEvent').init();
});

define('bindEvent',function(require, exports, module){
	var Controller = require('Controller');
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
    	.delegate('.fullscreen','click',function(){
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
    			Controller.loadGainPics(href);
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
				console.log('open4' + new Date);
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

define('Controller',['view/base','view/webupload','view/swiper'],{
	base:function(){
		return seajs.require('view/base')	
	},
	program:'<div class="progressc"><div class="barc" style="width:0%;"></div><div class="bartxt">0%</div></div>',
	loading:'<div class="loadings"><img alt="" style="" src="'+ window.location.origin +'/img/loading/31.gif"/></div>',
	inlineloading:'<div class="noresult"><img src="'+ window.location.origin +'/img/loading/31.gif" alt=""/></div>',
	noresult: _noresult,
	reloadDiy:function(ul){
		var vls = [];
		ul.find('li').each(function(){
			var id = this.getAttribute('data-id') || '';
			id && vls.push(id);
		});		
		return vls.join(',');
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
		base.get(_href + 'doctor/gainLisData',{oid:_orderid,asktype:_docask,syncSeries:sign},function(d){	
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
		base.get(_href + 'doctor/gainPacsData',{oid:_orderid,asktype:_docask,syncSeries:sign},function(d){	
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
	validaForm:function(){
		var _this = this,base = _this.base();
		var tel = $('#myform [name="telphone"]').val(),
			card = $('#myform [name="idcard"]').val();
		if(card && !this.valideCard(card)) return base.showTipE('请输入有效的身份证号'),false;
		if(!this.valideTel(tel)) return base.showTipE('请输入有效的联系电话'),false;
		return true;
	},
	saveForm:function(callback){
		var _this = this,base = _this.base();
		base.showTipIngA('正在保存病历').post(_href + 'doctor/saveInfos',$('#myform').serialize(),function(d){			
			base.showTipS('保存成功');
			callback(true);
		},function(){
			callback();
			base.showTipE('保存失败');
		});
	},
	sendForm:function(sval,callback){
		var _this = this,base = _this.base();
		base.showTipIngA('正在发送').post(_href + 'doctor/postInfoToExpert',{oid:_oid,sval:sval},function(d){			
			base.showTipS('发送成功');
			callback(true);
		},function(){
			callback();
			base.showTipE('发送失败');
		});
	},
	saveReport:function(txt,callback){
		var _this = this,base = _this.base();
		base.showTipIngA('正在发送报告')
			.post(_href + 'doctor/insertConResult',{conResult:txt,oid:_oid},function(d){			
				base.showTipS('报告发送成功');
				callback(true);
			},function(){
				callback();
				base.showTipE('报告发送失败');
			});
	},
	runPrefixMethod:function(element, method){
		var usablePrefixMethod;
	    ["webkit", "moz", "ms", "o", ""].forEach(function(prefix) {
	        if (usablePrefixMethod) return;
	        if (prefix === "") {
	            method = method.slice(0,1).toLowerCase() + method.slice(1);
	        }
	        var typePrefixMethod = typeof element[prefix + method];
	        if (typePrefixMethod + "" !== "undefined") {
	            if (typePrefixMethod === "function") {
	                usablePrefixMethod = element[prefix + method]();
	            } else {
	                usablePrefixMethod = element[prefix + method];
	            }
	        }
	    });    
	    return usablePrefixMethod;
	},
	fullscreen:function(esc){
		var docElm = document.documentElement;
		if(esc != 'esc'){
			this.runPrefixMethod(docElm, "RequestFullScreen");
		}else{
			this.runPrefixMethod(document, "CancelFullScreen");
		}
	},
	doFullScreen:function(esc){
		var v = $('#main'),btn = v.find('.fullscreen i');
		var parentFullscreen = window.parent.escFullScreen || $.proxy(this.fullscreen,this);
		if(esc != 'esc'){
			v.hasClass('fullScreenWindow') ? 
				(v.removeClass('fullScreenWindow'),btn.html('&#xe608;'),parentFullscreen('esc',true)) : 
				(v.addClass('fullScreenWindow'),btn.html('&#xe607;'),parentFullscreen('full',true));
			$('body').hasClass('overflowhidden') ? 
				$('body').removeClass('overflowhidden'):
				$('body').addClass('overflowhidden');
		}else{
			v.removeClass('fullScreenWindow'),parentFullscreen('esc');
			$('body').removeClass('overflowhidden');
		}
	},
	reloaddwv:function(iframe){
		$('#showDWVModel .loadings').fadeIn();
		if (iframe.attachEvent){ 
			iframe.attachEvent("onload", function(){ 
    			$('#showDWVModel .loadings').fadeOut();
			}); 
		} else { 
			iframe.onload = function(){ 
    			$('#showDWVModel .loadings').fadeOut();
			}; 
		}
	},
	hasAsync:function(){
		//用于同步时，没有同步完不能保存
		return true;
	},
	valideTel: function(text) {
        var _emp = /^\s*|\s*$/g;
        text = text.replace(_emp, "");
        var _d = /^1[3578][01379]\d{8}$/g;
        var _l = /^1[34578][01245678]\d{8}$/g;
        var _y = /^(134[012345678]\d{7}|1[34578][012356789]\d{8})$/g;
        if (_d.test(text)) {
            return true;
        } else if (_l.test(text)) {
            return true;
        } else if (_y.test(text)) {
            return true;
        }
        return false;
    },
    valideCard:function(idCard){
        var regIdCard =/^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
        var HMCard = /^[HhMm]\d{8}((\(\d{2}\))|\d{2})$/,
        	TCard = /^\d{8}(\d{1,2}(\([A-Za-z]\))?)?$/;
        switch(idCard.length){
	        case 8:
	        case 9:
	        case 10:
	        case 11:
	        case 13:
	        	if (HMCard.test(idCard)){return true;}
	        	if (TCard.test(idCard)) {return true;}
	        	return false;
	        	break;
	        case 18:
	            if (regIdCard.test(idCard)) {
	                var idCardWi = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2); //将前17位加权因子保存在数组里
	                var idCardY = new Array(1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2); //这是除以11后，可能产生的11位余数、验证码，也保存成数组
	                var idCardWiSum = 0; //用来保存前17位各自乖以加权因子后的总和
	                for (var i = 0; i < 17; i++) {
	                    idCardWiSum += idCard.substring(i, i + 1) * idCardWi[i];
	                }     
	                var idCardMod = idCardWiSum % 11; //计算出校验码所在数组的位置
	                var idCardLast = idCard.substring(17); //得到最后一位身份证号码
	     
	                if (idCardMod == 2) {
	                    if (idCardLast == "X" || idCardLast == "x") {
	                        return true;
	                    } else {
	                        return false;
	                    }
	                } else {
	                    if (idCardLast == idCardY[idCardMod]) {
	                        return true;
	                    } else {
	                        return false;
	                    }
	                }
	            }
	        	break;
	        default:
            	return false;
	        	break;
        }
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
			.post(_href + 'doctor/compleupcheckitem',{pid:fid,itemname:v,oid:_oid},function(d){			
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
		/*$('.fileBoxUl img[data-src]').each(function(){
			var o = $(this), post = o.offset().top, posb = post + o.height();
	        if ((post > st && post < sth) || (posb > st && posb < sth)) {
	        	o.attr('data-src') && (function(item){
	        	   var src = item.attr('data-src');
	        	   item.removeAttr('data-src').attr('src',src).addClass('loaded');
	           })(o);
	        }
		});*/
		/****视频浮动处理*****/
		(function(){
			var ovedio = $('#main.vedio'),o = $('#vedioOuter'), post = o.offset().top,
				ow = o[0].offsetWidth, oh = ow * 0.75,top = ovedio.css('top'),
				posb = post + oh;
			
			if ((post > st && post < sth) || (posb > st && posb < sth)) {
				ovedio.removeAttr('style');
				o.removeAttr('style');
			}else{
				ovedio.css({
					position:'fixed',
					padding:'0',
					zIndex:'10',
					height:oh + 'px',
					width: ow + 'px',
					top: top == '0px' ? '-300px' : top,
					left:'20px'
				}).animate({
					top:'10px'
				},300);
				o.css({
					height:oh + 'px',
					background:'#000'
				});
			}
		})();
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
        		formData: { oid: _orderid },
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
	}
});

