var isdown = false,_pageNo = 1;
var noresultImg = '<li class="noresult"><img src="'+ _burl +'img/mobile/kbzs/icon_noresult@2x.png" style="width:30px" alt=""/><div>暂无相关医生。</div></li>';
var noresultTxt = '<dd class="noresultTst"><label>没有数据了</label></dd>';
var _b = {
	post:function(url,ops,fun,err){
		return _$(_burl + url,ops,fun,err);
	},
	get:function(url,ops,fun,err){
		return _$$(_burl + url,ops,'get','json',fun,err);
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
	getKS:function(callback){
		isdown = true,_config.pageNo = _pageNo;
		this.get('wzjh/gainexpertsopenonline',_config,function(d){
			var deps = d.specials,html = '';
			if(deps.length > 0){
				$.each(deps,function(i,o){
					var imgurl=o.listSpecialPicture,pro=o.profession||'';
            		if(!imgurl){
            			imgurl = _burl + "img/defdoc.jpg";
            		}
            		if(imgurl.indexOf('://') == -1){
            			imgurl="http://wx.15120.cn/SysApi2/Files/"+imgurl;
            		}
					if(imgurl.indexOf('://') != -1){
						imgurl=imgurl.replace('http://','https://');
					}
                    html += '<div class="doclist">';
                    html += '<a class="atarget" href="'+ _burl +'wzjh/intodocdetail?docid='+ o.specialId +'&ltype='+ _config.ltype +'&openid='+ _config.openid +'"></a>';
                    html += '<div class="docinfo flex">';
                    html += '<div class="thumb shk0"><img alt="'+ o.specialName +'" src="'+ imgurl +'" alt=""/></div>';
                    html += '<div class="docdetail f1">';
                  	if(o.profession == '讲师') pro = '';
                    html += '	<p><b>'+ o.specialName +'</b><span>'+ (o.duty||'') +'&ensp;'+ pro+'</span></p>';
                    html += '	<p><span>'+ (o.hosName || '') +'</span><span>'+ (o.depName || '&ensp;') +'</span></p>';
                    html += '	<p><span>'+ (o.specialty || '&ensp;') +'</span></p>';
                    html += '</div>';
                    html += '</div>';
                    html += '</div>';
				});
			}
			deps.length < _config.pageSize ? (isdown = true) : (isdown = false,_pageNo++);
			callback(html);
		},function(){
			isdown = false;
		});
	},
	init:function(){
		isdown = false,_pageNo = 1;
		_config.pageNo = _pageNo;
        $('#loadingToast').show(),this.getKS(function(dd){
    		$('#doclists').html(dd ? dd : noresultImg);
    		$('#loadingToast').hide()
    	});
	}
};
$(document).ready(function(){
	$(document).endlessScroll({
        bottomPixels:50,
		fireDelay: 100,
		insertAfter: ".depart:last",
		loader:'<p style="text-align:center;"><img src="'+ _burl +'js/infinite.scroll/img/ajax-loader.gif" /></p>',
		stop:function(){
			return isdown;
		},
		callback: function(p) {
			_b.getKS(function(dd){
				$('#doclists').append(dd ? dd : noresultTxt);
			});
        }
	});
	$('body').delegate('.atarget','click',function(){
		$('#loadingToast').show();
	});
	_b.init();
});
