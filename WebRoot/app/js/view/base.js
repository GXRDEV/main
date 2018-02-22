var BASE = {
	href: window.location.origin + '/',
	uid: SStorage.get('_token_id') || '',
	utype: SStorage.get('_token_utype') || '',
	loginuuid: SStorage.get('_token_loginuuid') || '',
	isLost:function(a){
		return JSON.stringify(a).indexOf('<title>404/500</title>') !=-1
	},
	checkLogin: function(isredirct){
		var uid = SStorage.get('_token_uid');
		var utype = SStorage.get('_token_utype');
		if(!uid) return this.loginOut(), 0;
		checkToken(function(){},function(){
			if(isredirct) return loginOut(), 0;
			BASE.showModel({
				remote: 'ajax/modal-content/modal-unvalidepsd.html',
				cls: 'modal-smartbox'
			});				
		});
	},
	sstorageFun: function(data){
		SStorage.set('_token_id', data.id);
		SStorage.set('_token_uid', data.uid);
		SStorage.set('_token_utype', data.stype);
		SStorage.set('_token_uname', data.username);
		SStorage.set('_token_loginuuid', data.loginUuid);
	},
	loginIn: function(formData,success,fail,error,always,noredirct){
		$.post('/doctor/checkLogin', formData)
		.done(function(data){
			if('success' == data.status){
				data.uname = formData.username;
				BASE.sstorageFun(data),success();
				!noredirct && (location.href = data.stype == 2 || data.stype == 3 ? 'index.html' : '/doctor/index');
			}else{
				fail();
			}
		})
		.fail(function(){			
			'function' == typeof error && error()
		})
		.always(function(){
			'function' == typeof always && always()
		});
	},
	loginOut: function(){
		$.post('/doctor/logout').always(loginOut);
	},
	clearSession:function(){
		SStorage.remove('_token_id');
		SStorage.remove('_token_uid');
		SStorage.remove('_token_uname');
		SStorage.remove('_token_utype');
	},
	showModel: function(opt){
		opt = opt || {};
		var id = opt.id || 'Modal';
		var m = $('#' + id);
		var cls = ['modal-dialog', opt.cls || ''].join(' ');
		var txt = opt.text;
		var loading = opt.loading ? '<div class="loading"><img src="'+ window.location.origin +'/img/loading/31.gif" alt=""/></div>' : '';
		var options = $.extend({ backdrop:'static' }, opt);
		var tep = '<div class="modal fade" id="'+ id +'" tabindex="-1" data-cus="'+ (opt.gls || '') +'" role="dialog" aria-labelledby="'+ id 
				+'Label" aria-hidden="false"><div class="'+ cls +'"><div class="modal-content">'+ (txt || loading) +'</div></div></div>';
		if(opt.single && m.size() && m.is(':visible')){
			return false;
		}
		if(m.size()){
			opt.gls ? m.attr('data-cus', opt.gls) : m.removeAttr('data-cus');
			m.find('.modal-dialog').attr('class',cls);
			(opt.loading || opt.remote) && m.find('.modal-content').html(loading);
			txt && m.find('.modal-content').html(txt);
			!opt.ishow && m.modal(options);
		}else{
			$('body').append(tep);
			!opt.ishow && $('#' + id).modal(options);
		}
	},
	hideModel: function(id){
		$('#' + (id || 'Modal')).modal('hide');
	},
	closeModel: function(id){
		$('#' + (id || 'Modal')).modal('hide');
		window.setTimeout(function(){
			$('#' + (id || 'Modal')).remove()
		},500)
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
	getCardinfo: function(UUserCard){ 
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
	},
	arrayFindObjIndex: function (val,key,arr){
        var has = false;
        for(var i =0 ,l = arr.length; i< l;i++){
            if(val == arr[i][key]){
                has = true;
                break;
            }
        }
        return has ? i : -1;
    },
	imgsrc: function(_src, defsrc){
		var src = defsrc || 'img/avatars/male.png';
		if(!_src || _src == 'null') return src;
		if(_src.indexOf('://') != -1){
			src = _src.replace('http://', 'https://') ;
		}else if(_src.indexOf('data:image') != -1){
			src = _src;
		}else{
			src = 'http://wx.15120.cn/SysApi2/Files/' + _src;
		}
		return src;
	},
	refreshNotify: function(){
        $.get('/doctor/loadsysmsgs')
        .done(function(d){
            var num = d.msgs.length || 0;
            $('#notifynum').text(num);
            num ? $('#notifynum').show().next('.notification-ring').show() : 
				$('#notifynum').hide().next('.notification-ring').hide();
        });
	},
	backList: function(a, otype) {
		var iframe = location.href.indexOf('index.html') == -1;
		otype = parseInt(otype, 10);
		if(iframe) {
			switch (otype) {
				case 4:
					window.location.replace('/hospital/vedioOrderManage');break;
				case 5:
					window.location.replace('/hospital/tuwenOrderManage');break;
				case 10:
					window.location.replace('/hospital/referralOrderManage');break;
			}
		} else {
			window.location.replace(a.getAttribute('href'));
		}
	},
	backDetail: function(a, otype) {
		var iframe = location.href.indexOf('index.html') == -1;
		var href = a.getAttribute('href') || '=', oid = getParam('oid', href);
		if(iframe && oid) {
			window.location.replace('/app/main.html#ajax/admin/detail.html?oid='+ oid +'&otype='+ otype);
		} else if (oid) {
			window.location.replace(href);
		}
	}
};

//对Date的扩展，将 Date 转化为指定格式的String
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
//例子： 
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function (fmt) {
	var o = {
		"M+": this.getMonth() + 1, //月份 
		"d+": this.getDate(), //日 
		"h+": this.getHours(), //小时 
		"m+": this.getMinutes(), //分 
		"s+": this.getSeconds(), //秒 
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度 
		"S": this.getMilliseconds() //毫秒 
	};
	if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}