define(function(require, exports, module) {
	exports.tuwen = function(){
		require('bindBodyEvent').tuwen();
	};
});

define('bindBodyEvent',function(require, exports, module){
	var Controller = require('Controller1');
    exports.tuwen = function(){
    	seajs.use('view/viewer',function(view){
    		_diyUploadSelector = view.init('.userinfo .hasimgview');
    	});
    };
});

define('Controller1',['view/base'],{
	base:function(){
		return seajs.require('view/base')	
	},
	noresult:'<div class="noresult"><img src="'+ window.location.origin +'/img/mobile/kbzs/icon_noresult@2x.png" style="width:30px" alt=""/></div>',
	imgDo:function(selector){
		$(selector).each(function(){					
			var img = $('.thumb img',this);
			img[0].onload = function() {							
				var w = img.width(),h = img.height();
				if(w > h){
					img.css({width:'auto',height:'100%'});
				}else{
					img.css({width:'100%',height:'auto'});
				}
	        }
			if(img[0].complete){
				img[0].onload();
			}
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
	}
});

