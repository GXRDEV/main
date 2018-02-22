//数组去重复
Array.prototype.uniques = function() { var res = [], hash = {};for(var i=0, elem; (elem = this[i]) != null; i++)  {if (!hash[elem]){res.push(elem);hash[elem] = true;}}return res;}
define(function(require, exports, module) {
	exports.init = function(){
		require('bindEvent').init();
	}
	exports.detail = function(){
		require('bindEvent').detail();
	}
});

define('bindEvent',function(require, exports, module){
	var Controller = require('Controller');
    exports.init = function(){
    	$('body').delegate('.option','click',function(){
    		if($(this).hasClass('disabled')) return false;
    		$(this).addClass('selected').siblings().removeClass('selected');
    		Controller.pageNo = 1;
    		Controller.pageEnd = false;
    		Controller.fillDoclist(function(list){
    			$('.sectionresult .thumbnails').html(list || _noresult);
    		});
    	});
    	Controller.fillDoclist(function(list){
			$('.sectionresult .thumbnails').html(list || _noresult);
		});
    	$(window).bind("scroll",function(){
        	if(window._timers) window.clearTimeout(window._timers);
        	window._timers = window.setTimeout($.proxy(Controller.scrollFun,Controller),400);
        });
    	if(document.addEventListener){
    	    document.addEventListener('DOMMouseScroll',$.proxy(Controller.scrollFun,Controller),false);
    	}else{
    		window.onmousewheel = document.onmousewheel = $.proxy(Controller.scrollFun,Controller);
    	}
    };
    exports.detail = function(){
    	$('body').delegate('.showmore','click',function(){
    		var personinfo = $('.sectiondetailinfo .detailinfo'),
    			$this = $(this);
    		personinfo.hasClass('overflowhidden3em') ? 
    				(personinfo.removeClass('overflowhidden3em'),$this.html('收起更多<i class="iconfont">&#xe620;</i>')) : 
    				(personinfo.addClass('overflowhidden3em'),$this.html('展开更多<i class="iconfont">&#xe600;</i>'));
    	}).delegate('.option','click',function(){
    		if($(this).hasClass('disabled')) return false;
    		$(this).addClass('selected').siblings().removeClass('selected');
    		$('#times').val(this.getAttribute('data-id'));
    	}).delegate('.postFrom','click',function(){
    		if($(this).hasClass('disabled')) return false;
    		if(!Controller.valideFrom()) return false;
    		Controller.submitFrom();
    	});
    	
    	Controller.getDetailTimes();
    };
});

define('Controller',['view/base','view/webupload'],{
	base:function(){
		return seajs.require('view/base')	
	},
	program:'<div class="progressc"><div class="barc" style="width:0%;"></div><div class="bartxt">0%</div></div>',
	loading:'<div class="loadings"><img alt="" style="" src="'+ window.location.origin +'/img/loading/31.gif"/></div>',
	noresult: _noresult,
	timer:0,
	pageNo:1,
	pageEnd:false,
	showloading:function(){
		$('.sectionresult').find('.loadings').remove();
		$('.sectionresult').append(this.loading);
	},
	hideloading:function(){
		$('.sectionresult').find('.loadings').remove();
	},
	fillDoclist:function(callback){
		var _this = this,base = _this.base();
		var data = this.getPostData(),timer = +new Date;
		this.timer = timer;
		_this.showloading();
		base.get(_href + 'nuradmin/showexperts',data,function(d){	
			if(_this.timer != timer) return false;
			callback(_this.doPageHtml(d['specials'],data));	
			_this.hideloading();
		},function(){
			if(_this.timer != timer) return false;
			_this.hideloading();			
		});
	},
	getPostData:function(){
		var data = {pageNo : this.pageNo};
		$('.sectioncondation .option.selected').each(function(){
			var str = this.getAttribute('data-id'),strArr = str.split(':');
			data[strArr[0]] = strArr[1] || 0;
		});
		return data;
	},
	doPageHtml:function(arr,obj){
		var href = _href + 'nuradmin/writeinfo?sdate='+ obj.sdate +'&timetype='+ obj.timetype +'&depid=' + obj.depid;
		var li = '';
		arr.length > 0 && (this.pageNo = obj.pageNo + 1,this.pageEnd = false);
		arr.length < 10 && (this.pageEnd = true);
		$.each(arr,function(i,o){
			var pic=o.listSpecialPicture;
			if(pic.indexOf('://')!=-1){
				pic = pic.replace('http://','https://');
			}else{
				pic = 'http://wx.15120.cn/SysApi2/Files/'+pic;
			}
			li += '<li class="span2">';
			li += '	<div class="thumbnail">';
			li += '		<div class="thumb img-circle"><img src="'+ pic +'" alt=""/></div>';
			li += '		<label>' + o.hosName +'&emsp;' + o.specialName +'</label>';
			li += '		<label>' + o.depName +'&emsp;' + o.duty +'&emsp;' + o.profession +'</label>';
			li += '		<span class="timelist">'+ o.regisStr +'</span>';
			li += '		<div class="btnarea">';
			li += '			<a href="'+ href + '&sid=' + o.specialId +'" class="btn btn-border">马上预约</a>';
			li += '		</div>';
			li += '	</div>';
			li += '</li>';
		});
		return li;
	},
	scrollFun:function(){
		var st, sth, scrollH,_this = this;
		if(this.pageEnd) return false;
		st = $(window).scrollTop(), 
		sth = st + $(window).height(),
		scrollH = document.body.scrollHeight;
		//console.log('scrollHeight:'+ scrollH);
		//console.log('scrollTop + height:'+ sth);
		scrollH == sth && _this.fillDoclist(function(list){
			$('.sectionresult .thumbnails').append(list);			
		});
	},
	getDetailTimes:function(callback){
		var _this = this,base = _this.base();
		$('.sectionright .timelists').html(_this.loading);
		base.get(_href + 'nuradmin/gainSpecialTimes',{sdate:_sdate,timetype:_timetype,sid:_sid},function(d){
			var timehtml = '';
			d.times && $.each(d.times,function(i,o){
				timehtml += '<span data-id="'+ o.id +'" class="option'+ (o.hasAppoint == '1' ? ' disabled' : '') +'">'+ o.startTime +'</span>';
			});
			$('.sectionright .timelists').html(timehtml || _this.noresult);
		},function(){
			$('.sectionright .timelists').html(_this.noresult);
		});
	},
	submitFrom:function(){
		var _this = this,base = _this.base();
		$('.postFrom').addClass('disabled');
		base.showTipIngA('正在提交……').post(_href + 'nuradmin/registwinfo',$('.formpost').serializeArray(),function(d){
			$('.postFrom').removeClass('disabled');
			base.showTipS('提交成功');
			location.href = _href + 'nuradmin/registeration';
		},function(){
			$('.postFrom').removeClass('disabled');
			base.showTipE('提交失败');
		});
	},
	valideFrom:function(){
		var _this = this,base = _this.base();
		var time = $('[name="timeid"]'),
			name = $('[name="patientName"]'),
			//card = $('[name="idcard"]'),
			tel = $('[name="telphone"]');

		if(!time.val()) return base.showTipE('请选择出诊时间'),false;
		if(!name.val()) return base.showTipE('请输入姓名'),false;
		//if(!this.valideCard(card.val())) return base.showTipE('请有效的身份证号'),false;
		if(!this.valideTel(tel.val())) return base.showTipE('请有效的联系电话'),false;
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
    valideCard:function(code){
    	var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
        var tip = "";
        var pass= true;        
        if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
            tip = "身份证号格式错误";
            pass = false;
        }        
        else if(!city[code.substr(0,2)]){
            tip = "地址编码错误";
            pass = false;
        }
        else{
            if(code.length == 18){
                code = code.split('');
                var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
                var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
                var sum = 0;
                var ai = 0;
                var wi = 0;
                for (var i = 0; i < 17; i++)
                {
                    ai = code[i];
                    wi = factor[i];
                    sum += ai * wi;
                }
                var last = parity[sum % 11];
                if(parity[sum % 11] != code[17]){
                    tip = "校验位错误";
                    pass =false;
                }
            }
        }
        if(!pass) console.log(tip);
        return pass;
    }
	
});

