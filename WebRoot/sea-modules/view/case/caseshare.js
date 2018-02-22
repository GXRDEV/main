define(function(require, exports, module) {
	var Controller = require('Controller');	
	exports.twtelremt = function(){
		$('body').delegate('.hhlist .text img','click',function(){
			var src = this.getAttribute('src'),srcs = [];
			$(this).closest('.blockdiv').find('.hhlist .text img').each(function(){
				srcs.push(this.getAttribute('src'));
			});
			wx.previewImage({
		      current: src,urls: srcs
		    });
		}).delegate('#localreplay .weui_btn','click',$.proxy(Controller.savereplay,Controller));
		
		Controller.loadreplay().imgDo('.blockdiv');
	};
});
define('Controller',['view/base'],{
	base:function(){
		return seajs.require('view/base')	
	},
	imgDo:function(selector){
		$(selector).each(function(){
			$('.thumb img',this).each(function(){
				var img = $(this);
				img[0].onload = function() {							
					var w = img.width(),h = img.height();
					if(w > h){
						img.css({width:'auto',height:'100%','max-width':'inherit'});
					}else{
						img.css({width:'100%',height:'auto'});
					}
		        }
				if(img[0].complete){
					img[0].onload();
				}				
			});
		});
	},
	initAllDate:function(){
		var base = this.base(),_this = this;
		$('#loadingToast').show();
		base.get('wzjh/gainorderinfo',{oid:_config.oid,type:_config.type,flag:_config.flag},function(data){
			nodetpl.get('myorder_detail_order.htm', {
				base: _h,data: data,type:_config.type,location:window.location
			}, function(d) {
				$('#orderdetail').html(d);
				_this.imgDo('#twdiv .hhlist');
				$('#loadingToast').hide();
			});
			nodetpl.get('myorder_detail_case.htm', {
				base: _h,data: data,type:_config.type
			}, function(d) {
				$('#casedetail').html(d);
				_this.imgDo('.mycaseinfos .hhlist');
				$('#loadingToast').hide();
			});
		});
	},
	initlocalImg:function(){
		var base = this.base(),_this = this;
		if(!_config.oid) return this;
		$('#loadingToast').show();
		base.get('share/remotenormals',{oid:_config.oid},function(b){
			$('#localimg').show().find('.section').html(function(){
				var os = '';
				$.each(b.normals,function(i,o){
					var bol = location.href.indexOf('http://') != -1;
					var href = bol ? o.fileUrl.replace('http://','https://') : o.fileUrl;
					var type = o.fileType.toLocaleLowerCase();
					if('gif,jpg,jpeg,bmp,png,tif'.indexOf(type) != -1){
						os += '	<span class="text thumb"><img src="'+ href +'" onclick="javascript:;" alt=""/></span>';
					}else if('mp4,avi,webm,mkv,mov,rm,ogg,ogv'.indexOf(type) != -1){
						os += '<a class="media video_diy_bg" target="_blank"><video name="media" controls><source src="'+ href +'"></source></video></a>';
					}
				});
				return os;
			});
			$('#loadingToast').hide();
		});
		return this;
	},
	reloadDiy:function(ul){
		var vls = [];
		ul.find('li').each(function(){
			var id = this.getAttribute('data-id') || '';    			
			id && vls.push(id);
		});		
		return vls.join(',');
	},
	loadreplay: function(){
		var base = this.base(),_this = this;
		base.get('share/gainmessages',{oid:_config.oid},function(d){
			d.messages && d.messages.length && $('#localreplay .hhlist').show().html(function(){
				var h = '';
				$.each(d.messages,function(i,n){
					h += '<p><span style="color:blue">'+ (n.sendType == 2 ? '专家回复：' : '追问：') +'</span>'+ n.msgContent +'<small style="color:#aaa"> -- '+ n.msgTime +'</small></p>';
				});
				return h;
			});
		});
		return this;
	},
	savereplay: function(){
		var base = this.base(),_this = this;
		var text = $.trim($('#localreplay textarea').val());
		if(!text) return alert('请输入要回复的内容'), $('#localreplay textarea').focus(), 0;
		$('#loadingToast').show();
		base.post('share/replytoUser',{
			paramval: _config.param,
			content: text
		},function(d){
			location.href = location.href;
		})
	}
});