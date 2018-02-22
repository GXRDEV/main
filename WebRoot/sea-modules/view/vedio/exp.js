define(['view/vedio/events','view/vedio/controller'],function(require, exports, module) {
	var Controller = require('view/vedio/controller');
	var Controller0 = _gcontroller = require('Controller0');
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
	.delegate('#mainmenu','click',function(){
		Controller0.toggleMenu();
	})
	.delegate('#videoBtn','click',function(){
		var form = $('#vedioOuter');
		form.hasClass('smallStyle') ? 
			Controller0.showVideo('big').showForm('hide') :
			Controller0.showVideo('small').showForm('show');
		
		Controller0.toggleMenu();
	})
	.delegate('#formBtn','click',function(){
		var form = $('#myform');
		form.is(':hidden') ? 
			Controller0.showForm('show').showVideo('small') : 
			Controller0.showForm('hide').showVideo('big');
		Controller0.toggleMenu();
	})
	.delegate('#reportBtn','click',function(){
		var form = $('#reporttxt');
		form.is(':hidden') ? form.slideDown() : form.slideUp();
		Controller0.toggleMenu();
	})
	.delegate('#dialogBtn','click',function(){
		var form = $('#pictxtdialog');
		if(!_connect) return false;
		form.is(':hidden') ? form.show() : form.hide();
		Controller0.toggleMenu();
	})
	.delegate('.windows .smallscreen','click',function(){
		$(this).closest('.windows').hide();
		Controller0.toggleMenu();
	})
	.delegate('.stateBar .minscreen','click',function(){
		var bol = this.getAttribute('title') == '最小化';
		if($(this).closest('.fullScreenWindow').size()) return;
		Controller0.showVideo(bol ? 'small' : 'big').showForm(bol ? 'show' : 'hide');
	})
	.delegate('#firstloadin .exitandlook','click',function(){
		$('#firstloadin').hide();
		Controller0.showVideo('none').showForm('show');
	})
	.delegate('#mainmenu','mouseover',function(e){
		if(Controller0.checkHover(e,this)){
			if(window._time110) window.clearTimeout(window._time110),window._time110 = null;
		 	window._time110 = window.setTimeout(function(){
				Controller0.toggleMenu('show');
			}, 200);			
		}
	})
	.delegate('#menulist','mouseout',function(e){
		if(Controller0.checkHover(e,this)){ 
			if(window._time110) window.clearTimeout(window._time110),window._time110 = null;
		 	window._time110 = window.setTimeout(function(){
				Controller0.toggleMenu('hide');
			}, 1000);
		}
	});	
	
	if(_protag == '4'){
		$('#vedioOuter').show().siblings('#myform').hide();
	}else if(_protag == '5'){
		$('#myform').css({position:'static',margin:'10px','width':'auto'}).show()
			.find('.header button').hide();		
		$('#menulist,#vedioOuter,#reporttxt,#pictxtdialog').hide();
		return;
	}else{
		$('#myform').show().siblings('#vedioOuter').hide();
	}
	
	$('#pictxtdialog').draggable();
	Controller0.toggleMenu();
	$(window).trigger("resize");
});

define('Controller0',['view/base'],{
	checkHover:function(e,target){
		var getEvent = this.getEvent,contains = this.contains;
		if (getEvent(e).type=="mouseover")  {
	        return !contains(target,getEvent(e).relatedTarget||getEvent(e).fromElement) && !((getEvent(e).relatedTarget||getEvent(e).fromElement)===target);
	    } else {
	        return !contains(target,getEvent(e).relatedTarget||getEvent(e).toElement) && !((getEvent(e).relatedTarget||getEvent(e).toElement)===target);
	    }
	},
	contains:function(parentNode, childNode) {  
	    if (parentNode.contains) {  
	        return parentNode != childNode && parentNode.contains(childNode);  
	    } else {  
	        return !!(parentNode.compareDocumentPosition(childNode) & 16);  
	    }  
	},
	getEvent:function(e){
	    return e||window.event;
	},/*
	bindOneMouseOver:function(){
		var _this = this;
		_this.toggleMenu();
		$("#mainmenu").one("mouseover", function(){
			_this.toggleMenu();
		});
	},*/
	toggleMenu:function(bol){
		var $this = $('#mainmenu');
		if(bol == 'show'){
			return showMenu(),false;
		}else if(bol == 'hide'){
			return hideMenu(),false;
		}
		$this.hasClass('opened') ? hideMenu() : showMenu();
		
		function showMenu(){
			$this.addClass('opened').siblings().each(function(){
				var data = $(this).data();
				$(this).animate({
					bottom:data.bottom,right:data.right,transformScale:1
				},{
					step:function(now,fx){
						if (fx.prop === "transformScale") {
			                $(this).css('-webkit-transform','scale('+now+')');
			                $(this).css('transform','scale('+now+')');  
			            }
					}
				},400);
			}).closest('#menulist').css({width:'240px',height:'240px'})
		}
		function hideMenu(){
			$this.removeClass('opened').siblings().animate({
				bottom:'20px',right:'20px',transformScale:0
			},{
				step:function(now,fx){
					if (fx.prop === "transformScale") {
		                $(this).css('-webkit-transform','scale('+now+')');
		                $(this).css('transform','scale('+now+')');  
		            }
				}
			},400).closest('#menulist').css({width:'64px',height:'64px'})
		}
		return this;
	},
	showVideo:function(type){
		var v = $('#vedioOuter'),zidx = v.css('z-index'),
			dw = $(window).width(),dh = $(window).height();
		zidx = zidx > 4 ? zidx : 4;
		if(type=="big"){
			v.css({display:'block','z-index':zidx}).animate({
				width: dw + 'px',height:dh + 'px',bottom:'0',right:'0',top:'0',left:'0'
			},400,function(){
				v.css({width:'auto',height:'auto'}).removeClass('smallStyle');
			})
			.find('#stateBar .minscreen').attr('title','最小化').html('<i class="iconfont">&#xe629;</i>');
			v.draggable("destroy");
		}else if(type == 'small'){
			v.css({display:'block','z-index':zidx}).animate({
				bottom:'0',right:'0',top:'0',left:'0',
				width:'250px',height:'187.5px'
			},400).addClass('smallStyle')
			.find('#stateBar .minscreen').attr('title','还原').html('<i class="iconfont">&#xe627;</i>');
			v.draggable();
		}else{
			v.hide();
		}
		return this;
	},
	showForm:function(type){
		var form = $('#myform');
		if(type=="show"){
			form.fadeIn(),$(window).trigger("resize")
		}else{
			form.fadeOut()
		}
		return this;
	}
});