define(['view/vedio/events','view/vedio/controller'],function(require, exports, module) {
	var Controller = require('view/vedio/controller');
	var Controller0 = require('Controller0');
	
	Controller0.switchMenu(_protag,false);
	seajs.use('view/webupload',function(upload){
		$('#pics #addfiles').each(function(){
			$(this).Uploader({
        		server: _href + 'doctor/uploadLocalFile',
        		formData: { orderid: _orderid },
        		thumb: {
	                width: 60,
	                height: 60,
	                quality: 70,
	                allowMagnify: false,
	                crop: true,
	                type: "image/jpeg"
	            },
	            accept:{
	            	title: "Files",
	                extensions: "gif,jpg,jpeg,bmp,png,mp4,avi,webm,mkv,mov,rm,ogg.ogv",/*gif,jpg,jpeg,bmp,png,mp4,avi,webm,mkv,mov,rm,ogg.ogv*/
	                mimeTypes: "image/jpg,image/jpeg,image/bmp,image/png,video/mp4,video/avi,video/webm,video/mkv"
	            },
	            beforepost:function(btn){
	            	var pbtn = $('#btngroups');
	            	pbtn.addClass('disabled');
        		},
        		afterpost:function(btn){
        			var pbtn = $('#btngroups');
        			pbtn.removeClass('disabled');
        		},
        		success:function(liobj,val){
        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
        			hide.val(Controller.reloadDiy(ul));        			
        		},
        		afterpost:function(){
        			_diyUploadSelector.viewer('destroy').viewer({
        				navbar:true,
            			url:'data-src'
        			});
        		}
        	});
		});        	
	});
	seajs.use('view/vedio/chatfile',function(upload){
		$('#selectfile').uploadsingle({
    		server: _href + 'doctor/uploadLocalFile',
    		formData: { orderid: _orderid },
    		thumb: {
                width: 80,
                height: 100,
                quality: 70,
                allowMagnify: false,
                crop: true,
                type: "image/jpeg"
            },
    		success:function(liobj,val){
    			var li = '<div class="thumbimg"><div class="viewThumb"><img src="'+ val +'"/></div></div>'
    			sendMessage({"type":"chart","txt":li});
    			seajs.use('view/viewer',function(view){
    				view.init('.dialog .bodyer');
    	    	});
    		}
    	});
	});
	
	require('view/vedio/events').init();
	_roomKey && Controller.listenPage(_roomKey);
	
	$('body')
	.delegate('#saveFormAndNext','click',function(){
		if($(this).attr('disabled')) return false;
		if(!Controller.hasAsync()) return false;
		if(!Controller.validaForm()) return false;    		
		Controller.refreshHidden();
		Controller.saveForm(function(dd){
			var bol = !(_protag == '3' || _protag == '4' || _protag == '5');
			dd && Controller.sendForm(bol ? '2' : _protag,function(d){
				d && bol && intprotag && intprotag('2');
				Controller0.switchMenu(bol ? 2 : _protag,false);
    		});
		});		
	})
	.delegate('#formBtn','click',function(){
		var form = $('#baseinform');
		if(form.is(':hidden')){
			form.slideDown().find('.userinfo .smallscreen').show(),Controller0.moveMenuToBottom().show(),$('#videoAndScreenShare').hide()
			if(_protag == '4'){
				$('#formBtn').hide(),
				$('#videoBtn').show()
			}else{
				$('#formBtn').show(),
				$('#videoBtn').hide()
			}
			$(window).trigger("resize");
		}else{
			form.hide();
			if(_protag == '4'){
				$('#videoAndScreenShare,#formBtn').show(),
				$('#videoBtn').hide(),
				Controller0.moveMenuToButtomCenter().show()
			}else{
				$('#videoAndScreenShare,#videoBtn').hide(),
				$('#formBtn').show(),
				Controller0.moveMenuToCenter().show()
			}
		}
	})
	.delegate('#videoBtn','click',function(){
		$('#videoAndScreenShare,#formBtn').show();
		$('#baseinform,#videoBtn').hide();
		Controller0.moveMenuToButtomCenter().show();
	})
	.delegate('#leavelBtn','click',function(){
		Controller0.moveMenuToCenter().show();
		Controller0.switchMenu(3,true);
	})
	.delegate('#readyBtn','click',function(){
		Controller0.moveMenuToCenter().show();
		Controller0.switchMenu(4,true);
	})
	.delegate('#endBtn','click',function(){		
		Controller0.switchMenu(5,true);
	})
	.delegate('#secondBtn','click',function(){
		Controller0.switchMenu(6,true);
	})
	.delegate('#thirdBtn','click',function(){
		Controller0.switchMenu(7,true);
	})
	.delegate('#pictxtdialog.windows .smallscreen','click',function(){
		$(this).closest('.windows').hide();
		$('#dialogBtn').show();
	})
	.delegate('#baseinform.windows .smallscreen','click',function(){
		$('#formBtn').click();
	})
	.delegate('#dialogBtn','click',function(){
		var form = $('#pictxtdialog');
		if(!_connect) return false;
		form.is(':hidden') ? form.show() : form.hide();
	})
	.delegate('#changeYuan','click',function(){
		var t = $(this).text();
		if(t.indexOf('语音') != -1){
			$(this).text('切到视频');
			switchVideoAudio(true);
		}else{
			$(this).text('切到语音');
			switchVideoAudio(false);
		}		
	})
	.delegate('#tellExps','click',function(){
		//已通知专家;
		Controller0.tellExpNoShare();		
	})
	.delegate('#reloadShare','click',function(){
		//重新加载;
		openwin();
	})	
	.delegate('#newwindow','click',function(){
		var ifm = $('#screenframe'),src = ifm.attr('data-src'),_src = ifm.attr('src');
		if(!src) return false;
		if(_src == src) return ifm.attr('src',_burl + 'html/screenshare.html?state=outed'),openNewWin(),false;//detail_new.js
		//if(_src != src) return ifm.attr('src',src),window._OpenWindow.close(),false;		
	})
	.delegate('#bigwin','click',function(){
		$('#screenShareOuter').hasClass('fullScreenShare') ?
				($(this).attr('title','最大化').html('<i class="iconfont">&#xe627;</i>'),$('#screenShareOuter').removeClass('fullScreenShare')) :
				($(this).attr('title','还原').html('<i class="iconfont">&#xe628;</i>'),$('#screenShareOuter').addClass('fullScreenShare'))
	});	
	$('#pictxtdialog').draggable();
});
define('Controller0',['view/base'],{
	base:function(){
		return seajs.require('view/base')	
	},
	moveMenuToBottom:function(){
		var $this = $('#menulist');
			$this.addClass('bottom').removeClass('center').css({left:'auto',top:'auto',bottom:'10px',right:'30px','transform':'translate(0,0)'})/*.animate({
				bottom:'10px',right:'0'
			},400)*/;
		return $this;
	},
	moveMenuToCenter:function(){
		var $this = $('#menulist');
		$this.addClass('center').removeClass('bottom').css({bottom:'auto',right:'auto',top:'50%',left:'50%','transform':'translate(-50%,-50%)'})/*.animate({
			top:'50%',left:'50%'
		},400)*/;
		return $this;
	},
	moveMenuToButtomCenter:function(){
		var $this = $('#menulist');
		$this.addClass('bottom').removeClass('center').css({left:'50%',top:'auto',bottom:'10px',right:'auto','transform':'translate(-50%,0)'})/*.animate({
			top:'50%',left:'50%'
		},400)*/;
		return $this;
	},
	switchMenu:function(sta,bol){
		switch(sta.toString()){
			case '2'://保存成功
			case '3'://标记离开
				$('#menulist,#readyBtn').show();
				$('#leavelBtn,#endBtn,#secondBtn,#thirdBtn').hide();
				$('#baseinform,#videoAndScreenShare').hide();
				this.moveMenuToCenter().show();
				break;
			case '4'://标记就绪
				$('#menulist,#leavelBtn,#videoAndScreenShare').show();
				$('#readyBtn,#endBtn,#secondBtn,#thirdBtn').hide();
				$('#baseinform').hide();
				this.moveMenuToButtomCenter().show();
				break;
			case '5'://完成
			case '6'://第二次就诊
			case '7'://第三次就诊
			default:
				$('#menulist,#videoAndScreenShare').hide();
				$('#baseinform').show();
				break;
		}
		_connect && $('#dialogBtn').show();
		bol && sta && sta != 2 ? saveState(sta) : intprotag(sta.toString());//detail_video.js
	},
	tellExpNoShare:function(){
    	var base = this.base(),_this = this;    
		if(!pcstarted) return base.showTipE('专家还没有发起连接呢'),false;
		base.post(_burl + "doctor/callforshare", {orderid : _roomKey },function(){
			base.showTipS('已经通知专家了');
		});
	}
});