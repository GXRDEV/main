/**本地缓存*/
var _t= {
    _set: function(key, value) {    	
      if (window.localStorage) {
          localStorage.setItem(key, value);
      }else{
    	  _cookie._set(key, value);     	  
      }    
    },
    _get: function(key) {
    	if (window.localStorage) {
    		return window.localStorage ? (localStorage[key] || "") : "";
        }else{
        	return _cookie._get(key);     	  
        }
    },
    _remove:function(key){
    	if (window.localStorage) {
            localStorage.removeItem(key);
        }else{
        	_cookie._remove(key);
        }
    },
    _clear:function(){
    	localStorage.clear();
    }
};
var _ts= {
	    _set: function(key, value) {    	
	      if (window.sessionStorage) {
	    	  sessionStorage.setItem(key, value);
	      } else{
	    	  _cookie._set(key, value);     	  
	      }        
	    },
	    _get: function(key) {
	    	if (window.localStorage) {
	    		return window.sessionStorage ? (sessionStorage[key] || "") : "";
	        }else{
	        	return _cookie._get(key);     	  
	        }	        
	    },
	    _remove:function(key){
	    	if (window.sessionStorage) {
	    		sessionStorage.removeItem(key);
	        }else{
	        	_cookie._remove(key);
	        }
	    },
	    _clear:function(){
	    	sessionStorage.clear();
	    }
	};
/**cookie操作*/
var _cookie= {
    _set: function(name, value, expires) {
        var _end = new Date();
        if (expires) {
            _end.setTime(_end.getTime() + (expires * 1000));
        }
        document.cookie=name+"="+escape(value)+(expires ? (";expires="+_end.toGMTString()) : "")+";path=/;domain=" + location.host;
    },
    _get: function(name) {
        var _cookie = document.cookie;
        var _start = _cookie.indexOf(name + "=");
        if (_start != -1) {
            _start += name.length + 1;
            var _end = _cookie.indexOf(";", _start);
            if (_end == -1) {
                _end = _cookie.length;
            }
            return unescape(_cookie.substring(_start, _end));
        }
        return "";
    },
    _remove:function(name){
	    var exp = new Date();
	    exp.setTime(exp.getTime() - 1);
	    var cval = this._get(name);
	    if(cval!=null)
	        document.cookie= name + "="+ cval +";expires="+exp.toGMTString();
    },
    _clear:function(){
    	document.cookie = "";
    }
};
/**跳转链接*/
var _g= function(url) {
    location.href = url;
},_gr=function(url){
	location.replace(url);
};
var _req = function(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}
/**公用的同步*/
var _$asyn= function(url, param, fun,err) {
    var _flag = false;
    $.ajax({
        type: "post",
        url: url,
        async: false,
        data: param,
        cache: false,
        dataType: "json",
        success: function(data) {
            _flag = true;
            fun(data)
        },
        error: function() {
            console.log("网络错误，请重试");
        	err && err();
        }
    });
    return _flag;
}
/**公用的异步*/
var _$= function(url, param, fun, err) {
    $.ajax({
        type: "post",
        url: url,
        data: param,
        cache: false,
        dataType: "json",
        success: function(data) {
            fun(data);
        },
        error: function() {
        	console.log("网络错误，请重试");
        	err && err();
        }
    });
};
var _$$= function(url, param, type, dtype, fun, err) {
    $.ajax({
        type: type || "post",
        url: url,
        data: param,
        cache: false,
        dataType: dtype || "json",
        success: function(data) {
            fun(data);
        },
        error: function() {
        	console.log("网络错误，请重试");
        	err && err();
        }
    });
};
var _dialog = function(){
	this.loading = {			
		init:function(ops){
			var o = $('<div id="_loading"></div>'),
				c = '<div class="dia_cover"></div>',
				t = '<div class="dia_fixed dia_center" style="padding:15px 0;text-align:center; background-color:#fff;z-index:1001">\
						<img alt="" src="'+ window.location.origin +'/img/loading/load_32.gif"/>\
					</div>';
			this.hide();
			o.append(c + t);
			$('body').append(o);
		},
		show:function(){
			this.init();
		},
		hide:function(){
			$('#_loading').remove();
		}
	};
	this.alert = {
		init:function(ops){
			var o = $('<div id="_alert" style="display:none;"></div>'),
				c = '<div id="_alert_c" class="dia_cover"></div>',
				t = '<div id="_alert_t" class="dia_fixed" style="background-color:#fff;z-index:1001">\
						<div class="_alert_title" style="text-align:right;height:20px;overflow:hidden;margin:6px 6px 0 0;">\
							<img title="关闭" style="width:20px;" src="'+ window.location.origin +'/img/share_qr_close3.png">\
						</div>\
						<div class="_alert_text">'+ (ops.text || "") +'</div>\
						'+ (ops.btn ? '<div class="_alert_btn">'+ ops.btn +'</div>' : '')  +'\
					</div>';
			this.hide();
			o.append(c + t);
			$('body').append(o);
		},
		show:function(ops){
			var _this = this;
			this.init(ops);
			ops.bottom ? this.bottom() : this.center();
			ops.modelEvent && $('#_alert').delegate('#_alert_c','click',function(){
				_this.hide(ops);
			});
			$('#_alert').delegate('._alert_title img','click',function(){
				_this.hide(ops);
			});
		},
		center:function(){
			var _h = $('#_alert_t').height();
			$('#_alert_t').addClass('dia_center').parent().show();			
		},
		bottom:function(){
			$('#_alert_t').removeClass('dia_center').parent().show();	
		},
		hide:function(ops){
			$('#_alert').remove();
			ops && ops.close && ops.close();
		}
	};
};
//(function(){
//    function o(){document.documentElement.style.fontSize=(document.documentElement.clientWidth>414?414:document.documentElement.clientWidth)/12+"px"}
//    var e=null;
//    window.addEventListener("resize",function(){clearTimeout(e),e=setTimeout(o,300)},!1),o()
//})(window);
var _loading = {
		w3124:'<img alt="" style="width:24px;" src="'+ window.location.origin +'/img/loading/31.gif"/>',
		w31:'<img alt="" src="'+ window.location.origin +'/img/loading/31.gif"/>',
		w9:'<img alt="" src="'+ window.location.origin +'/img/loading/9.gif"/>',
		wload48:'<img alt="" src="'+ window.location.origin +'/img/loading/ajax-loader.gif"/>',
		wload32:'<img alt="" src="'+ window.location.origin +'/img/loading/load_32.gif"/>',
		wload24:'<img alt="" src="'+ window.location.origin +'/img/loading/load_24.gif"/>'		
};
//微信隐藏菜单
// (function(){	
// 	function onBridgeReady(){
// 		WeixinJSBridge.call('hideOptionMenu');
// 	}
// 	if (typeof WeixinJSBridge == "undefined"){
// 	    if( document.addEventListener ){
// 	        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
// 	    }else if (document.attachEvent){
// 	        document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
// 	        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
// 	    }
// 	}else{
// 	    onBridgeReady();
// 	}
// })(document);