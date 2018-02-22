var _burl = window.location.origin + '/';
var isdown = false,_pageNo = 1,_flag = 1;
var noresultImg = '<li class="noresult"><img src="'+ _burl +'img/mobile/kbzs/icon_noresult@2x.png" style="width:30px" alt=""/><div>您还没有订单信息，到别处去看看。</div></li>';
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
	getKS:function(pageNo,callback){
		var _this = this,flag = _flag;
		isdown = true;
		this.get('doctor/doceval',{docid:docid,pageNo:pageNo,pageSize:5},function(d){
			var apps = d.appriases,dd = '';
			if(apps.length > 0){
				$.each(apps,function(i,o){
					var tags = [],aa = o.userAccount || '';
					$.each(o.tags,function(ii,oo){
						tags.push(oo.tagName);
					});
					tags.length ? ('<span class="signlists">'+ tags.join('，') + '</span>') : '';
					dd += '<div class="pingllist prelative">\
					       <div class="headTop box">\
			    				<label class="cols1">'+ aa.substring(0,3) + '****' + aa.substring(aa.length,aa.length - 4) +'</label>\
			    				<div class="cols0"><span class="clearfix stars stars'+ o.grade +'"><i class="iconfont icon-xingxing"></i>\
								<i class="iconfont icon-xingxing"></i><i class="iconfont icon-xingxing"></i>\
			    				<i class="iconfont icon-xingxing"></i><i class="iconfont icon-xingxing"></i></span></div>\
			    			</div>\
			    			<div class="bodyDiv">\
			    				<dl>\
					                <dd><span>'+ o.content +'</span></dd>\
					                <dd class="signlists">'+ tags +'</dd>\
					                <dd class="box"><span class="cols1">'+ o.timeStr +'</span><span class="cols0">'+ (function(t){
				    					if(t == '1') return '图文问诊'
				    					else if(t == '2') return '电话问诊'
				    					else if(t == '4') return '远程门诊'
				    				})(o.orderType) +'</span></dd>\
			    				</dl>\
			    			</div>\
			    		</div>';
				});
			}
			apps.length < 5 ? (isdown = true) : (isdown = false,_pageNo++);
			callback(dd);
		},function(){
			isdown = false;
		});
	},
	getSigns:function(flag){
		this.get('doctor/caltags',{docid:docid},function(d){
			$('#signslist').html(function(){
				var tags = '';
				$.each(d.tags,function(i,g){
					tags += '<span class="signlist">'+ g.content +'('+ g.totalNumber +')</span>';
				});
				return tags;
			});			
		});
		return this;
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
			_b.getKS(_pageNo,function(dd){
				$('#pingllist').append(dd ? dd : noresultTxt);
			});
        }
	});
	_b.getSigns().getKS(1,function(dd){
		$('#pingllist').html(dd ? dd : $('#pinglistdiv').remove());
	});
});	

