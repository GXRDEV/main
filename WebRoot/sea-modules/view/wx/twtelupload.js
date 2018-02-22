define(function(require, exports, module) {
	var Controller = require('Controller');	
	exports.saveForm = function(){
		seajs.use('view/wxupload',function(upload){
			$('#uploadpics').Uploader({
	    		server: _href + 'doctor/uploadLocalFile',
	    		formData: { uid: _docid },
	    		icondel:'<i class="iconfont" title="删除">&#xe62a;</i>',
	    		thumb: {width: 77,height: 77},
	    		success:function(liobj,val){
	    			var ul = liobj.closest('.weui_uploader_files');
	    			$('#caseImages').val(Controller.reloadDiy(ul));        			
	    		}
	    	});
		});
		$('body')
		.delegate('#submitForm','click',$.proxy(Controller.validateForm,Controller))
		.delegate('[name="idcard"]','blur',$.proxy(Controller.fillAgeSex,Controller));
	};
	exports.remoteHZ = function(){
		Controller.initCity('[name="city"]');
		$('body')
		.delegate('[name="city"]','change',function(){
			Controller.initHos('[name="hosid"]',this.value).clearSelect('[name="depid"]');
		})
		.delegate('[name="hosid"]','change',function(){
			Controller.initDep('[name="depid"]',this.value);
		})
		.delegate('[name="levelid"]','change',function(){
			this.checked && this.value == '-1' ? $('[for="levelid"]').show() : $('[for="levelid"]').hide();
		})
		.delegate('#submitForm','click',$.proxy(Controller.validateRemote,Controller));
	};
	exports.remoteConfirm = function(){
		seajs.use('view/wxupload',function(upload){
			$('#uploadpics').Uploader({
	    		server: _href + 'doctor/uploadLocalFile',
	    		formData: {},
	    		thumb: {width: 77,height: 77},
	    		success:function(liobj,val){
	    			var ul = liobj.closest('.weui_uploader_files');
	    			$('#caseImages').val(Controller.reloadDiy(ul));        			
	    		}
	    	});
		});
		$('body')
		.delegate('#submitForm','click',$.proxy(Controller.validateForm,Controller))
		.delegate('[name="idcard"]','blur',$.proxy(Controller.fillAgeSex,Controller));
	};
	exports.myorderdetail = function(){
		Controller.initAllDate();
		window.onhashchange=function(){
			var hashStr = location.hash.replace("#","") || 'orderdetail';
			Controller.actionTo(hashStr);    		
		};
		window.onhashchange();
	};
	exports.myorder_detail_order = function(){
		seajs.use('view/vedio/chatfile',function(upload){
			$('#selectfile').size() && $('#selectfile').uploadsingle({
	    		server: _href + 'wzjh/appendmessage_image',
	    		formData: { oid: _config.oid,openid: _config.openid },
	    		thumb: { width: 80,height: 100 },
	    		success:function(liobj,val){
	    			var h = document.querySelector('#message').scrollHeight;
	    			$('#message').scrollTop(h);
	    		}
	    	});
		});
		$('body')
		.delegate('#endorder','click',function(){
			$('#dialog1').attr('data-type','closetw').show()
				.find('.weui_dialog_title').html('结束问诊').end()
				.find('.weui_dialog_bd').html('确认要结束当次问诊？');
		})
		.delegate('.weui_dialog_confirm[data-type="closetw"] .primary','click',function(){
			Controller.endTWOrder();
			$('#dialog1').hide();
		})
		.delegate('.weui_dialog_confirm .default','click',function(){
			$('#dialog1').hide();
		})
		.delegate('#tocontinuepay','click',$.proxy(Controller.continuePay,Controller))
		.delegate('#btnsendmsg','click',$.proxy(Controller.sendMsgToDoc,Controller));
		$('#message').css({
			height:($(window).height() - 115) + 'px',
			'overflow':'auto'
		});
		wx.ready(function () {
			$('body').delegate('.hhlist .text img','click',function(){
				var src = this.getAttribute('src'),srcs = [];
				$(this).closest('.hashdiv').find('.hhlist .text img').each(function(){
					srcs.push(this.getAttribute('src'));
				});
				wx.previewImage({
			      current: src,urls: srcs
			    });
			});
		});
	};
	exports.myorderpj = function(){
		Controller.imgDo('#pingjdetail .hhlist');
		$('body').delegate('#pingjme','click',$.proxy(Controller.validatePingj,Controller));
	};
});
define('Controller',['view/base'],{
	base:function(){
		return seajs.require('view/base')	
	},
	actionTo:function(hash){
		$('#' + hash).show().siblings('.hashdiv').hide();
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
	validatePingj:function(){
		var base = this.base(),_this = this,form = $('#myform');
		var sign = $('[name="tagids"]:checked').size(),
			cont = $('[name="content"]').val();
		if(!sign && !cont) return alert('请选择标签或者填写评价内容。'),false;
		$('#loadingToast').show();
		base.post(form.attr('action'),form.serializeArray(),function(){
			location.replace(_href + 'wzjh/myorderdetail?oid='+ _config.oid +'&type='+ _config.type +'&flag=complete&openid='+ _config.openid);
		});
	},
	endTWOrder:function(){
		var base = this.base(),_this = this;
		$('#loadingToast').show();
		base.post('doctor/closetw',{oid:_config.oid,utype:1,openid:_config.openid},function(data){			
			location.href = location.href;
			$('#loadingToast').hide();
		});
	},
	sendMsgToDoc:function(){
		var base = this.base(),_this = this;
		var msg = $.trim($('#sendtext').val());
		if(!msg) return;
		$('#loadingToast').show();
		base.post('wzjh/appendmessage_txt',{oid:_config.oid,msgContent:msg,openid:_config.openid},function(data){			
			var m = data.message || {msgTime:'',id: new Date()},h = 0;
			$('#message .bodyer').append(function(){
				return '<div class="timer">'+ m.msgTime +'</div>\
					<div class="hhlist clearfix me">\
						<span class="cols0"><span class="thumb"><img src="https://develop.ebaiyihui.com:443/img/defdoc.jpg"></span></span>\
						<span class="cols1"><span class="text hasimgview">'+ m.msgContent +'</span></span>\
					</div>';
			});
			h = document.querySelector('#message').scrollHeight;
			$('#sendtext').val('');
			$('#message').scrollTop(h);
			$('#loadingToast').hide();
		});
	},
	continuePay:function(){
		var base = this.base(),_this = this;
		$('#loadingToast').show();
		base.post('wzjh/continuepay',{oid:_config.oid,type:_config.type,openid:_config.openid},function(data){
			$('#loadingToast').hide();				
			_this.wxpay(data,function(){
				location.href = location.href;
			});
		});
	},
	initAllDate:function(){
		var base = this.base(),_this = this;
		$('#loadingToast').show();
		base.get('wzjh/gainorderinfo',{oid:_config.oid,type:_config.type,flag:_config.flag},function(data){
			nodetpl.get('myorder_detail_order.htm', {
				base: _h,data: data,type:_config.type,config:_config,location:window.location
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
	validateRemote:function(){
		var base = this.base(),_this = this;
		var hosid = $('[name="hosid"]').val(),
			depid = $('[name="depid"]').val(),
			levelid = $('[name="levelid"]:checked').val(),
			timeid = $('[name="timeid"]:checked').val();
		if(!hosid) return alert('请选择合作医院'),false;
		if(!depid) return alert('请选择就诊科室'),false;
		if(!levelid) return alert('请选择专家级别'),false;
		if(levelid == '-1'){
			if(!base.valideTel($('[name="optel"]').val())) return alert('请输入有效的联系电话'),false;
			if(!$('[name="opmoney"]').val()) return alert('请输入有效的金额'),false;
		}
		if(!timeid) return alert('请选择会诊时间'),false;
		$('#loadingToast').show();
		$('#postorder').submit();
	},
	initCity:function(selector){
		var base = this.base(),_this = this;
		base.get('wzjh/gainopencitys',{},function(b){
			$(selector).html(function(){
				var os = '<option value=""></option>';
				$.each(b.beans,function(i,o){
					os += '<option value="'+ o.remark +'">'+ o.name +'</option>';
				});
				return os;
			});
		});
		return this;
	},
	initHos:function(selector,cityid){
		var base = this.base(),_this = this;
		base.get('wzjh/gainhosbycity',{dictcode:cityid},function(b){
			$(selector).html(function(){
				var os = '<option value=""></option>';
				$.each(b.hospitals,function(i,o){
					os += '<option value="'+ o.id +'">'+ o.displayName +'</option>';
				});
				return os;
			});
		});
		return this;
	},
	initDep:function(selector,hosid){
		var base = this.base(),_this = this;
		base.get('wzjh/gaindepartsbyhos',{hosid:hosid},function(b){
			$(selector).html(function(){
				var os = '<option value=""></option>';
				$.each(b.departs,function(i,o){
					os += '<option value="'+ o.id +'">'+ o.displayName +'</option>';
				});
				return os;
			});
		});
		return this;
	},
	clearSelect:function(selector){
		$(selector).html('<option value=""></option>');
	},
	reloadDiy:function(ul){
		var vls = [];
		ul.find('li').each(function(){
			var id = this.getAttribute('data-id') || '';    			
			id && vls.push(id);
		});		
		return vls.join(',');
	},
	fillAgeSex:function(){
		var base = this.base();
		var card = $('[name="idcard"]').val(),cardObj;
		if(card && base.valideCard(card)){
			cardObj = base.getCardinfo(card);
			$('[name="sex"]').val(cardObj.sexcode);
			$('[name="age"]').val(cardObj.age);
		}
	},
	validateForm:function(){
		var base = this.base();
		var name = $('[name="contactName"]'),tel = $('[name="telephone"]'),
			card = $('[name="idcard"]'),des = $('[name="description"]');
		$('form .weui_cell_warn').removeClass('weui_cell_warn');
		if(!name.val()) return alert('请输入患者姓名'),name.closest('.weui_cell').addClass('weui_cell_warn'),false;
		if(!base.valideTel(tel.val())) return alert('请输入有效的电话号'),tel.closest('.weui_cell').addClass('weui_cell_warn'),false;
		if(card.val() && !base.valideCard(card.val())) return alert('请输入有效的身份证号'),card.closest('.weui_cell').addClass('weui_cell_warn'),false;
		this.saveForm();
	},
	saveForm:function(){
		var base = this.base(),_this = this;
		$('#loadingToast').show();
		base.post($('#postorder').attr('action'), $('#postorder').serializeArray(), function(d){
			$('#loadingToast').hide();				
			d.payStatus == false ? _this.paySuccess() : _this.wxpay(d);
		}, function(){
			$('#loadingToast').hide();
		});
	},
	paySuccess:function(){
		var i = 3,_time;
		$('#toast').show();
		_time = window.setInterval(function(){
			i--;
			$('#toast .weui_btn span').html(i);
			if(i == 0){
				window.clearInterval(_time);
				_gr(_href + 'wzjh/myorders?openid='+ _openid +'&flag=processing');
			}
		},1000);
	},
	wxpay:function(d,fn){
		var _this = this;
		(function callpay()
		{
		    if(!checkWX()) return false;
			if (typeof WeixinJSBridge == "undefined"){
			    if( document.addEventListener ){
			        document.addEventListener('WeixinJSBridgeReady', jsApiCall, false);
			    }else if (document.attachEvent){
			        document.attachEvent('WeixinJSBridgeReady', jsApiCall); 
			        document.attachEvent('onWeixinJSBridgeReady', jsApiCall);
			    }
			}else{
				jsApiCall();
			}
		})();
		function jsApiCall()
		{
			WeixinJSBridge.invoke(
				'getBrandWCPayRequest',
				{
					"appId":d.appid,
					"timeStamp":d.timeStamp,
					"nonceStr":d.nonceStr,
					"package":d['package'],
					"paySign":d.sign,
					"signType":"MD5"
				},
				function(res){
					if(res.err_msg.indexOf(':ok') > -1){
						typeof fn == 'function' ? fn() :_this.paySuccess();
					}
					(res.err_msg.indexOf(':cancel') > -1) && alert('用户已取消');
					(res.err_msg.indexOf(':fail') > -1) && alert('失败');
				}
			);
		}
		function checkWX(){
		    var wechatInfo = navigator.userAgent.match(/MicroMessenger\/([\d\.]+)/i) ;
			if( !wechatInfo ) {
			    alert("仅支持在微信里面打开") ;
			    return false;
			} else if ( wechatInfo[1] < "5.0" ) {
			    alert('您的微信版本（V '+ (wechatInfo[1] || '4.0') +'）比较低，不支持微信支付。请升级您的微信。') ;
			    return false;
			}
			return true;
		}
	}
});