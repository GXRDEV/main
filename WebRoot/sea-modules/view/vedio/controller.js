﻿Array.prototype.uniques = function() { var res = [], hash = {};for(var i=0, elem; (elem = this[i]) != null; i++)  {if (!hash[elem]){res.push(elem);hash[elem] = true;}}return res;}

define(['view/base','view/webupload','view/swiper'],{
	base:function(){
		return seajs.require('view/base')	
	},
	program:'<div class="progressc"><div class="barc" style="width:0%;"></div><div class="bartxt">0%</div></div>',
	loading:'<div class="loadings"><img alt="" style="" src="'+ window.location.origin +'/img/loading/31.gif"/></div>',
	inlineloading:'<div class="noresult"><img src="'+ window.location.origin +'/img/loading/31.gif" alt=""/></div>',
	noresult: _noresult || '<div class="noresult"><img src="'+ window.location.origin +'/img/mobile/kbzs/icon_noresult@2x.png" style="width:30px" alt=""/></div>',
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
		//input['idsearch']=_idsearch;
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
		base.get(_href + 'doctor/gainPacsData',{oid:_orderid,syncSeries:sign},function(d){	
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
	loadGainPics:function(href,fn){
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
    		fn && fn($('#showDWVModel').css('z-index'));
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
				($('#videoAndScreenShare,#vedioOuter').css('z-index','4'),v.removeClass('fullScreenWindow'),btn.html('&#xe608;'),parentFullscreen('esc',true)) : 
				($('#videoAndScreenShare,#vedioOuter').css('z-index','101'),v.addClass('fullScreenWindow'),btn.html('&#xe607;'),parentFullscreen('full',true));
			$('body').hasClass('overflowhidden') ? 
				$('body').removeClass('overflowhidden'):
				$('body').addClass('overflowhidden');
		}else{
			$('#videoAndScreenShare,#vedioOuter').css('z-index','4');
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
		/*(function(){
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
		})();*/
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
	},
	listenPage:function(channelId){
		var goEasy = new GoEasy({
        	appkey: '78028e7e-edcc-4524-b56b-45639785a53a'
        });
      	goEasy.subscribe({
            channel: channelId,
            onMessage: function(message){
            	var content = message.content.replace(/&quot;/g,"\"");
            	var jmessage = JSON.parse(content);  
            	var _type    =  jmessage.type;
            	var _from    =  jmessage.from,
            		_result  =  jmessage.result || '加载无数据';  
            	
    			//console.log('jmessage', JSON.stringify(jmessage, null, '\t'));
            	
            	//就绪通知
            	if(_type == 'launchNotify' && _from !=_utype && _utype == 1){	            	
            		_hasuser = jmessage.hasuser;
            		initiallizeVideo(true);
            	//退出通知
            	} else if (_type =='cancelNotify' && _from !=_utype ){
            		endwaitvideo();
            		//进入就绪或退出就绪状态
            	} else if(_type == 'progressNotify' && _from !=_utype){
            		//var _progress = jmessage.progress;
            		//alert("inside"  + content);
            	//报告通知
            	} else if (_type =='reportNotify' && _from !=_utype ){
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
            	else if (_type =='share' && _utype == '2'){
            		msgShareScreen(jmessage);
            	}
            }
        });
	}
});

