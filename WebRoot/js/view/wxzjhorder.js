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
		this.post('wzjh/gainorders',{openid:oid,pageNo:pageNo,flag:flag,pageSize:5},function(d){
			var deps = d.orders,dd = '';
			var btn = '',href,sign = '';
			if(deps.length > 0){
				$.each(deps,function(i,o){
					if(flag == 'processing'){
						btn = '<span style="color:#FF8B8B">'+ (o.desc || '进行中') +'</span>';
					}else if(flag == 'complete'){
						btn = '<span style="color:#00CC99">已完成</span>';
					}else{
						btn = '<span class="endbtn">已取消</span>';
					}
					href = _burl +'wzjh/myorderdetail?oid='+ o.id +'&type='+ o.type +'&flag=' + flag +'&openid=' + oid;					
					if(o.type == '1'){
						sign = '图文问诊';
					}else if(o.type == '2'){
						sign = '电话问诊';
					}else{
						sign = '远程门诊';
					}
					dd += '<li class="orderList prelative">\
						   <a class="href" href="'+ href +'"><i class="iconfont">&#xe60a;</i></a>\
					       <div class="headTop box">\
			    				<label class="cols1">订单号：'+ o.id +'</label>\
			    				<div class="cols0">'+ btn +'</div>\
			    			</div>\
			    			<div class="bodyDiv">\
			    				<dl>\
			    					<dd><label>服务医生：</label><span>'+ (o.docName || '未选择') +'&ensp;（'+ (o.hospital || '未选择') +'）</span></dd>\
					                <dd><label>创建时间：</label><span>'+ o.timeStr +'</span></dd>\
					                <dd><label>服务类型：</label><span>'+ sign +'</span></dd>\
			    				</dl>\
			    			</div>\
			    		</li>';
				});
			}
			deps.length < 5 ? (isdown = true) : (isdown = false,_pageNo++);
			callback(dd);
		},function(){
			isdown = false;
		});
	},
	actionTo:function(flag){
		isdown = false,_pageNo = 1,_flag = flag;
        $('.weui_navbar [data-flag="'+ flag +'"]').addClass('weui_bar_item_on').siblings('.weui_bar_item_on').removeClass('weui_bar_item_on');
        $('#loadingToast').show(),this.getKS(1,function(dd){
    		$('#orderList').html(dd ? dd : noresultImg);
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
			_b.getKS(_pageNo,function(dd){
				$('#orderList').append(dd ? dd : noresultTxt);
			});
        }
	});
	$('#navbar').on('click', '.weui_navbar_item', function () {
        location.hash = $(this).attr('data-flag');
    });
	window.onhashchange=function(){
		var hashStr = location.hash.replace("#","") || 'processing';
		_b.actionTo(hashStr);    		
	};
	window.onhashchange();
});	
