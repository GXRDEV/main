define({
	ajax:function(url, param, fun, err) {
		var _url = url.indexOf('://') != -1 ? url : url.indexOf('/') == 0 ? url : '/' + url;
	    $.ajax({
	        type: "post",
	        url: _url,
	        data: param,
	        cache: false,
	        dataType: "json",
	        success: function(data) {
	    		if ('function' == typeof fun) fun(data);
	        },
	        error:function(XMLHttpRequest, textStatus, errorThrown){
	        	console.log((+new Date()) + '：'+ textStatus + ';' + JSON.stringify(param));
	        	if ('function' == typeof err){ 
	        		err(XMLHttpRequest, textStatus, errorThrown);                	
	            }
	        }
	    });
	    return this;
	},
	postStr:function(url, param, fun, err) {
		var _url = url.indexOf('://') != -1 ? url : url.indexOf('/') == 0 ? url : '/' + url;
	    $.ajax({
	        type: "post",
	        url: _url,
	        data: param,
	        cache: false,
	        dataType: "json",
	        contentType: "application/json; charset=utf-8",
	        success: function(data) {
	    		if ('function' == typeof fun) fun(data);
	        },
	        error:function(XMLHttpRequest, textStatus, errorThrown){
	        	console.log((+new Date()) + '：'+ textStatus + ';' + JSON.stringify(param));
	        	if ('function' == typeof err){ 
	        		err(XMLHttpRequest, textStatus, errorThrown);                	
	            }
	        }
	    });
	    return this;
	},
	post:function(url, param, fun, err) {
		this.ajax(url, param, fun, err);
		return this;
	},
	get:function(url, param, fun, err) {
		var _url = url.indexOf('://') != -1 ? url : url.indexOf('/') == 0 ? url : '/' + url;
	    $.ajax({
	        type: "get",
	        url: _url,
	        data: param,
	        cache: false,
	        dataType: "json",
	        success: function(data) {
	    		if ('function' == typeof fun) fun(data);
	        },
	        error:function(XMLHttpRequest, textStatus, errorThrown){
	        	console.log((+new Date()) + '：'+ textStatus + ';' + JSON.stringify(param));
	        	if ('function' == typeof err){ 
	        		err(XMLHttpRequest, textStatus, errorThrown);                	
	            }
	        }
	    });
	    return this;
	},
	showDialog:function(ops){
		var loading = '<div class="noresult"><img src="'+ window.location.origin +'/img/loading/31.gif" alt=""/></div>';
		var footer = '<div class="modal2-footer"><button class="btn" data-dismiss="modal" aria-hidden="true">取消</button><button class="btn btn-primary">确定</button></div>';
		var m1 = '<div class="modal2 fade" id="'+ ops.id +'" tabindex="-1" role="dialog"><div class="modal2-dialog '+ (ops.cls || 'modal2-lg') +'"><div class="modal2-content"><div class="modal2-header"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>'+(ops.title ? ('<h3 class="dialogtitle">'+ ops.title +'</h3>') : '' )+'</div><div class="modal2-body">'+ (ops.text || loading) +'</div>'+ (ops.nofooter ? '' : footer) +'</div></div></div>';
		$('.modal2#' + ops.id).remove();
		$('body').addClass('modal2-open').append(m1);		
		$('.modal2#' + ops.id).modal('show');
		'function' == typeof ops.ok && $('.modal2#'+ ops.id +' .btn-primary').on('click',ops.ok);
	    return this;
	},
	showCustomDialog:function(ops){
		var loading = '<div class="noresult"><img src="'+ window.location.origin +'/img/loading/31.gif" alt=""/></div>';
		var footer = '<div class="modal2-footer"><button class="btn" data-dismiss="modal" aria-hidden="true">继续视频</button><button class="btn btn-primary">挂断视频</button></div>';
		var m1 = '<div class="modal2 fade" id="'+ ops.id +'" tabindex="-1" role="dialog"><div class="modal2-dialog '+ (ops.cls || 'modal2-lg') +'"><div class="modal2-content"><div class="modal2-header"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>'+(ops.title ? ('<h3 class="dialogtitle">'+ ops.title +'</h3>') : '' )+'</div><div class="modal2-body">'+ (ops.text || loading) +'</div>'+ (ops.nofooter ? '' : footer) +'</div></div></div>';
		$('.modal2#' + ops.id).remove();
		$('body').addClass('modal2-open').append(m1);		
		$('.modal2#' + ops.id).modal('show');
		'function' == typeof ops.ok && $('.modal2#'+ ops.id +' .btn-primary').on('click',ops.ok);
	    return this;
	},
	hideDialog:function(id){
		$('body').removeClass('modal2-open');
		$('.modal2#'+id).modal('hide').find('.btn-primary').off();
	    return this;
	},
	showLineLoading:function(id){
		seajs.use('view/progress',function(pro){
			pro.init().start();
		});
		return this;
	},
	hideLineLoading:function(id){
		seajs.use('view/progress',function(pro){
			pro.init().done();
		});
		return this;
	},
	showTip:function(txt,cls,timer){
		var id = 'tip'+(+new Date);
		this.hideTip();
		$('body').append('<div id="'+ id +'" class="deling"><div class="noresult '+ (cls || '') +'">'+ txt +'</div></div>');
		timer && window.setTimeout(function(){
			$('#' + id).fadeOut(800,function(){
				$('#' + id).remove()
			});			
		},timer || 3000);
	    return this;
	},
	showTipIngA:function(txt,timer){
		this.showTip(txt, '', timer);
	    return this;
	},
	showTipIng:function(txt,timer){
		this.showTip(txt, '', timer || 3000);
	    return this;
	},
	showTipS:function(txt,timer){
		this.showTip(txt, 'success', timer || 3000);
	    return this;
	},
	showTipE:function(txt,timer){
		this.showTip(txt, 'error', timer || 3000);
	    return this;
	},
	hideTip:function(){
		$('body > .deling').remove();
	    return this;
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
	valideEmail: function(text) {
        var _emp = /^\s*|\s*$/g;
        text = text.replace(_emp, "");
        var _d = /[a-zA-Z0-9]{1,10}@[a-zA-Z0-9]{1,5}\.[a-zA-Z0-9]{1,5}/;
        if (_d.test(text)) {
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
	        case 18:
	            return regIdCard.test(idCard);
	        default:
            	return false;
        }
    },
	getCardinfo:function(UUserCard){ 
		var sex,age,sexcode,year = UUserCard.substring(6, 10),gmonth = UUserCard.substring(10, 12),gday = UUserCard.substring(12, 14);
		var myDate = new Date(),month = myDate.getMonth() + 1,day = myDate.getDate(); 
		if (parseInt(UUserCard.substr(16, 1)) % 2 == 1) { 
			sex = '男',sexcode = '1';
		} else { 
			sex = "女",sexcode = '0';
		} 
		age = myDate.getFullYear() - UUserCard.substring(6, 10) - 1; 
		if (gmonth < month || gmonth == month && gday <= day) { age++; } 
		return {birth:year + "-" + gmonth + "-" + gday,sex:sex,age:age,sexcode:sexcode}
	}
});