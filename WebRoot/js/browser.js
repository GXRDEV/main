var browser={
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
	}(),
	language:(navigator.browserLanguage || navigator.language).toLowerCase()
};


/*;
(function ($, window, document, undefined) {    
    if (!window.browser) {                 
        var userAgent = navigator.userAgent.toLowerCase(),
            uaMatch;        
        window.browser = {}                 
        *//**
         * 判断是否为ie
         *//*        
        function isIE() {            
            return ("ActiveXObject" in window);        
        }        
        *//**
         * 判断是否为谷歌浏览器
         *//*        
        if (!uaMatch) {            
            uaMatch = userAgent.match(/chrome\/([\d.]+)/);            
            if (uaMatch != null) {                
                window.browser['name'] = 'chrome';                
                window.browser['version'] = uaMatch[1];            
            }        
        }        
        *//**
         * 判断是否为火狐浏览器
         *//*        
        if (!uaMatch) {            
            uaMatch = userAgent.match(/firefox\/([\d.]+)/);            
            if (uaMatch != null) {                
                window.browser['name'] = 'firefox';                
                window.browser['version'] = uaMatch[1];            
            }        
        }        
        *//**
         * 判断是否为opera浏览器
         *//*        
        if (!uaMatch) {            
            uaMatch = userAgent.match(/opera.([\d.]+)/);            
            if (uaMatch != null) {                
                window.browser['name'] = 'opera';                
                window.browser['version'] = uaMatch[1];            
            }        
        }        
        *//**
         * 判断是否为Safari浏览器
         *//*        
        if (!uaMatch) {            
            uaMatch = userAgent.match(/safari\/([\d.]+)/);            
            if (uaMatch != null) {                
                window.browser['name'] = 'safari';                
                window.browser['version'] = uaMatch[1];            
            }        
        }        
        *//**
         * 最后判断是否为IE
         *//*        
        if (!uaMatch) {            
            if (userAgent.match(/msie ([\d.]+)/) != null) {                
                uaMatch = userAgent.match(/msie ([\d.]+)/);                
                window.browser['name'] = 'ie';                
                window.browser['version'] = uaMatch[1];            
            } else {                
                *//**
                 * IE10
                 *//*                 if (isIE() && !! document.attachEvent && (function () {
                    "use strict";
                    return !this;
                }())) {                    
                    window.browser['name'] = 'ie';                    
                    window.browser['version'] = '10';                
                }                
                *//**
                 * IE11
                 *//*                
                if (isIE() && !document.attachEvent) {                    
                    window.browser['name'] = 'ie';                    
                    window.browser['version'] = '11';                
                }            
            }        
        }        
        *//**
         * 注册判断方法
         *//*        
        if (!$.isIE) {            
            $.extend({                
                isIE: function () {                    
                    return (window.browser.name == 'ie');                
                }            
            });        
        }        
        if (!$.isChrome) {            
            $.extend({                
                isChrome: function () {                    
                    return (window.browser.name == 'chrome');                
                }            
            });        
        }        
        if (!$.isFirefox) {            
            $.extend({                
                isFirefox: function () {                    
                    return (window.browser.name == 'firefox');                
                }            
            });        
        }        
        if (!$.isOpera) {            
            $.extend({                
                isOpera: function () {                    
                    return (window.browser.name == 'opera');                
                }            
            });        
        }        
        if (!$.isSafari) {            
            $.extend({                
                isSafari: function () {                    
                    return (window.browser.name == 'safari');                
                }            
            });        
        }    
    }
})(jQuery, window, document);*/