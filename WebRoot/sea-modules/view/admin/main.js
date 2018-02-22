var _idsearch="true",_diyUploadSelector;		
var hosurl = '', depurl = '';
define(function(require, exports, module) {
	exports.exportinfo = function(bol){
		if(bol === true){
			hosurl = '/kangxin/gaincoohospitals';
			depurl = '/kangxin/gainhosdeparts';
		}
		require('bindEvent').exportinfo();
		seajs.use('view/webupload',function(upload){
			var Controller = require('Controller');
			$('#addheadImageUrl').size() && $('#addheadImageUrl').Uploader({
        		server: _burl + 'doctor/uploadLocalFile',
        		formData: {},
        		pick:{
		            multiple: false
		        },
		        backdata:'urlpath',
        		duplicate:true,
        		beforecreat:function(btn){
	            	$('#addheadImageUrl').closest('li').siblings().remove();
        		},
        		success:function(liobj,val){
        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
        			hide.val(Controller.reloadDiy(ul));        			
        		}
        	});
			$('#adddocImageUrl').size() && $('#adddocImageUrl').Uploader({
        		server: _burl + 'doctor/uploadLocalFile',
        		formData: {},
        		pick:{
		            multiple: false
		        },
		        backdata:'urlpath',
        		duplicate:true,
        		beforecreat:function(btn){
	            	$('#adddocImageUrl').closest('li').siblings().remove();
        		},
        		success:function(liobj,val){
        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
        			hide.val(Controller.reloadDiy(ul));        			
        		}
        	});
			
			$('#addexpImageUrl').size() && $('#addexpImageUrl').Uploader({
        		server: _burl + 'doctor/uploadLocalFile',
        		formData: {},
        		pick:{
		            multiple: false
		        },
		        backdata:'urlpath',
        		duplicate:true,
        		beforecreat:function(btn){
	            	$('#addexpImageUrl').closest('li').siblings().remove();
        		},
        		success:function(liobj,val){
        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
        			hide.val(Controller.reloadDiy(ul));        			
        		}
        	});


			$('#addrelatedPics').size() && $('#addrelatedPics').Uploader({
        		server: _burl + 'doctor/uploadLocalFile',
        		formData: {},
        		success:function(liobj,val){
        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
        			hide.val(Controller.reloadDiy(ul));        			
        		}
        	});
		});
	};
	exports.operatorinfo = function(bol){
		if(bol === true){
			hosurl = '/kangxin/gaincoohospitals';
			depurl = '/kangxin/gainhosdeparts';
		}
		require('bindEvent').operatorinfo();
		seajs.use('view/webupload',function(upload){
			var Controller = require('Controller');
			$('#addheadImageUrl').size() && $('#addheadImageUrl').Uploader({
        		server: _burl + 'doctor/uploadLocalFile',
        		formData: {},
        		pick:{
		            multiple: false
		        },
		        backdata:'urlpath',
        		duplicate:true,
        		beforecreat:function(btn){
	            	$('#addheadImageUrl').closest('li').siblings().remove();
        		},
        		success:function(liobj,val){
        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
        			hide.val(Controller.reloadDiy(ul));        			
        		}
        	});
			$('#adddocImageUrl').size() && $('#adddocImageUrl').Uploader({
        		server: _burl + 'doctor/uploadLocalFile',
        		formData: {},
        		pick:{
		            multiple: false
		        },
		        backdata:'urlpath',
        		duplicate:true,
        		beforecreat:function(btn){
	            	$('#adddocImageUrl').closest('li').siblings().remove();
        		},
        		success:function(liobj,val){
        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
        			hide.val(Controller.reloadDiy(ul));        			
        		}
        	});
			$('#addexpImageUrl').size() && $('#addexpImageUrl').Uploader({
        		server: _burl + 'doctor/uploadLocalFile',
        		formData: {},
        		pick:{
		            multiple: false
		        },
		        backdata:'urlpath',
        		duplicate:true,
        		beforecreat:function(btn){
	            	$('#addexpImageUrl').closest('li').siblings().remove();
        		},
        		success:function(liobj,val){
        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
        			hide.val(Controller.reloadDiy(ul));        			
        		}
        	});
		});
	};
	exports.operatorDocInfo = function(bol){
		if(bol === true){
			hosurl = '/kangxin/gaincoohospitals?type=2';
			depurl = '/kangxin/gainhosdeparts';
		}
		require('bindEvent').operatorDocInfo();
		seajs.use('view/webupload',function(upload){
			var Controller = require('Controller');
			$('#addheadImageUrl').size() && $('#addheadImageUrl').Uploader({
        		server: _burl + 'doctor/uploadLocalFile',
        		formData: {},
        		pick:{
		            multiple: false
		        },
		        backdata:'urlpath',
        		duplicate:true,
        		beforecreat:function(btn){
	            	$('#addheadImageUrl').closest('li').siblings().remove();
        		},
        		success:function(liobj,val){
        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
        			hide.val(Controller.reloadDiy(ul));        			
        		}
        	});
			$('#adddocImageUrl').size() && $('#adddocImageUrl').Uploader({
        		server: _burl + 'doctor/uploadLocalFile',
        		formData: {},
        		pick:{
		            multiple: false
		        },
		        backdata:'urlpath',
        		duplicate:true,
        		beforecreat:function(btn){
	            	$('#adddocImageUrl').closest('li').siblings().remove();
        		},
        		success:function(liobj,val){
        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
        			hide.val(Controller.reloadDiy(ul));        			
        		}
        	});
			$('#addexpImageUrl').size() && $('#addexpImageUrl').Uploader({
        		server: _burl + 'doctor/uploadLocalFile',
        		formData: {},
        		pick:{
		            multiple: false
		        },
		        backdata:'urlpath',
        		duplicate:true,
        		beforecreat:function(btn){
	            	$('#addexpImageUrl').closest('li').siblings().remove();
        		},
        		success:function(liobj,val){
        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
        			hide.val(Controller.reloadDiy(ul));        			
        		}
        	});
			$('#addrelatedPics').size() && $('#addrelatedPics').Uploader({
        		server: _burl + 'doctor/uploadLocalFile',
        		formData: {},
        		success:function(liobj,val){
        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
        			hide.val(Controller.reloadDiy(ul));        			
        		}
        	});
		});
	};
	exports.sysconcaseinfo = function(){
		require('bindEvent').sysconcaseinfo();
		seajs.use('view/webupload',function(upload){
			var Controller = require('Controller');
			$('#addheadImageUrl').size() && $('#addheadImageUrl').Uploader({
        		server: _burl + 'doctor/uploadLocalFile',
        		formData: {},
        		pick:{
		            multiple: false
		        },
		        backdata:'urlpath',
        		duplicate:true,
        		beforecreat:function(btn){
	            	$('#addheadImageUrl').closest('li').siblings().remove();
        		},
        		success:function(liobj,val){
        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
        			hide.val(Controller.reloadDiy(ul));        			
        		}
        	});
		});
	};
	exports.hosptialinfo = function(){
		require('bindEvent').hosptialinfo();
		seajs.use('view/webupload',function(upload){
			var Controller = require('Controller');
			$('#addbigPicture').size() && $('#addbigPicture').Uploader({
        		server: _burl + 'doctor/uploadLocalFile',
        		formData: {},
        		success:function(liobj,val){
        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
        			hide.val(Controller.reloadDiy(ul));        			
        		}
        	});
			$('#addauthorizeFile').size() && $('#addauthorizeFile').Uploader({
        		server: _burl + 'doctor/uploadLocalFile',
        		formData: {},
        		success:function(liobj,val){
        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
        			hide.val(Controller.reloadDiy(ul));        			
        		}
        	});
		});
	};
	exports.hosptialpassed = function(){
		require('bindEvent').hosptialpassed();
		seajs.use('view/webupload',function(upload){});
		seajs.use('view/viewer',function(view){
			view.init('.diyUpload');
    	});
	};
	exports.docHelpOrder = function(){	
		require('bindEvent').docHelpOrder();
	};
	exports.basicSet = function(){	
		require('bindEvent').basicSet();
		seajs.use('view/webupload',function(upload){
			var Controller = require('Controller');
			$('#addHosIcon').size() && $('#addHosIcon').Uploader({
        		server: _burl + 'doctor/uploadLocalFile',
        		formData: {},
        		backdata:'frazior',
        		single:true,
        		pick:{multiple: false},
        		thumb: {
	                width: 100,
	                height: 100
	            },
        		beforecreat:function(){
        			$('#hosform .parentFileBox').show();
        		},
        		success:function(liobj,obj){
        			 $('#logourl').attr('src',$('#hosform .parentFileBox .viewThumb img').attr('src'));   
        			 $('.imgupload [name="logourl"]').val(obj['urlpath']);
        			 $('#hosform .parentFileBox').hide();
        		}
        	});
		});
		hoseditor = UE.getEditor('introduction');
	};
	exports.serviceinfoform = function(){
		require('bindEvent').serviceinfoform();
		seajs.use('view/webupload',function(upload){
			var Controller = require('Controller');
			$('#addheadImageUrl').size() && $('#addheadImageUrl').Uploader({
				server: _burl + 'doctor/uploadLocalFile',
				formData: {},
				pick:{
					multiple: false
				},
				backdata:'urlpath',
				duplicate:true,
				beforecreat:function(btn){
					$('#addheadImageUrl').closest('li').siblings().remove();
				},
				success:function(liobj,val){
					var ul = liobj.closest('.parentFileBox'),hide = ul.next();
					hide.val(Controller.reloadDiy(ul));
				}
			});
			$('#addUnnsedImageUrl').size() && $('#addUnnsedImageUrl').Uploader({
				server: _burl + 'doctor/uploadLocalFile',
				formData: {},
				pick:{
					multiple: false
				},
				backdata:'urlpath',
				duplicate:true,
				beforecreat:function(btn){
					$('#addUnnsedImageUrl').closest('li').siblings().remove();
				},
				success:function(liobj,val){
					var ul = liobj.closest('.parentFileBox'),hide = ul.next();
					hide.val(Controller.reloadDiy(ul));
				}
			});
		});

	};
	exports.addserviceinfo = function(){
		require('bindEvent').addserviceinfo();
		seajs.use('view/webupload',function(upload){
			var Controller = require('Controller');
			$('#addheadImageUrl').size() && $('#addheadImageUrl').Uploader({
				server: _burl + 'doctor/uploadLocalFile',
				formData: {},
				pick:{
					multiple: false
				},
				backdata:'urlpath',
				duplicate:true,
				beforecreat:function(btn){
					$('#addheadImageUrl').closest('li').siblings().remove();
				},
				success:function(liobj,val){
					var ul = liobj.closest('.parentFileBox'),hide = ul.next();
					hide.val(Controller.reloadDiy(ul));
				}
			});
			$('#addUnnsedImageUrl').size() && $('#addUnnsedImageUrl').Uploader({
				server: _burl + 'doctor/uploadLocalFile',
				formData: {},
				pick:{
					multiple: false
				},
				backdata:'urlpath',
				duplicate:true,
				beforecreat:function(btn){
					$('#addUnnsedImageUrl').closest('li').siblings().remove();
				},
				success:function(liobj,val){
					var ul = liobj.closest('.parentFileBox'),hide = ul.next();
					hide.val(Controller.reloadDiy(ul));
				}
			});
		});
	}
});

define('bindEvent',function(require, exports, module){
	var Controller = require('Controller');
    exports.exportinfo = function(){
    	$('body').delegate('.bindchange','change',function(){
    		var data = $(this).data(),sval = $(this).val(), type = data.type || '';
    		$('select[data-val]').attr('data-val','');
    		switch(type){
	    		case 'city':
	    			sval ? Controller.gainproorcitys('city',sval) : Controller.clearoptions(['city']);
	    			Controller.gainhosbyprovin('hosid',sval.substr(0,2));
	    			Controller.clearoptions(['depid']);
	    			$('input[name="distcode"]').val(sval);
	    			break;
	    		case 'hosid':
	    			$('input[name="distcode"]').val(sval);
	    			break;
	    		case 'depid':
	    			sval ? Controller.gaindepartsbyhos('depid',sval) : Controller.clearoptions(['depid']);
	    			break;
    		}
    	})
    	.delegate('.diyCancel','click',function(event){
    		var li = $(this).closest('li'),ul = li.closest('.parentFileBox'),
    			hide = ul.next();
    		li.remove();
			hide.val(Controller.reloadDiy(ul));   
    		return false;
    	})
    	.delegate('button[name="submitform"]','click',function(){
    		if($(this).hasClass('disabled')) return false;
    		if(Controller.validateForm()){
    			Controller.submitForm()
    		}
    	})
    	.delegate('button[name="sbform"]','click',function(){
    		if($(this).hasClass('disabled')) return false;
    		if(Controller.validateSbForm()){
    			Controller.sbForm()
    		}
    	})
    	.delegate('.serverslist .checkbox input','change',function(){
    		var p = $(this).parent();
    		this.checked ? p.next().show() : p.next().hide().find(':input').val('');
    	});
    	Controller.initselect();
    };
    exports.operatorDocInfo = function(){
    	$('body').delegate('.bindchange','change',function(){
    		var data = $(this).data(),sval = $(this).val(), type = data.type || '';
    		$('select[data-val]').attr('data-val','');
    		switch(type){
	    		case 'city':
	    			sval ? Controller.gainproorcitys('city',sval) : Controller.clearoptions(['city']);
	    			Controller.gainhosbyprovin('hosid',sval.substr(0,2));
	    			Controller.clearoptions(['depid']);
	    			$('input[name="distcode"]').val(sval);
	    			break;
	    		case 'hosid':
	    			$('input[name="distcode"]').val(sval);
	    			break;
	    		case 'depid':
	    			sval ? Controller.gaindepartsbyhos('depid',sval) : Controller.clearoptions(['depid']);
	    			break;
    		}
    	})
    	.delegate('.diyCancel','click',function(event){
    		var li = $(this).closest('li'),ul = li.closest('.parentFileBox'),
    			hide = ul.next();
    		li.remove();
			hide.val(Controller.reloadDiy(ul));   
    		return false;
    	})
    	.delegate('button[name="submitform"]','click',function(){
    		if($(this).hasClass('disabled')) return false;
    		if(Controller.validateForm()){
    			Controller.submitOpDocForm()
    		}
    	})
    	.delegate('button[name="sbform"]','click',function(){
    		if($(this).hasClass('disabled')) return false;
    		if(Controller.validateSbForm()){
    			Controller.sbForm()
    		}
    	})
    	.delegate('.serverslist .checkbox input','change',function(){
    		var p = $(this).parent();
    		this.checked ? p.next().show() : p.next().hide().find(':input').val('');
    	});
    	Controller.initselect();
    };
    
    exports.operatorinfo = function(){
    	$('body').delegate('.bindchange','change',function(){
    		var data = $(this).data(),sval = $(this).val(), type = data.type || '';
    		$('select[data-val]').attr('data-val','');
    		switch(type){
	    		case 'city':
	    			sval ? Controller.gainproorcitys('city',sval) : Controller.clearoptions(['city']);
	    			Controller.gainhosbyprovin('hosid',sval.substr(0,2));
	    			$('input[name="distCode"]').val(sval);
	    			break;
	    		case 'street':
	    			$('input[name="distCode"]').val(sval);
	    			break;
	    		case 'depid':
	    			sval ? Controller.gaindepartsbyhos('depid',sval) : Controller.clearoptions(['depid']);
	    			break;
    		}
    	})
    	.delegate('.diyCancel','click',function(event){
    		var li = $(this).closest('li'),ul = li.closest('.parentFileBox'),
    			hide = ul.next();
    		li.remove();
			hide.val(Controller.reloadDiy(ul));   
    		return false;
    	})
    	.delegate('button[name="submitform"]','click',function(){
    		if($(this).hasClass('disabled')) return false;
    			Controller.submitOpForm()
    	});
    	Controller.initselect();
    };
    exports.sysconcaseinfo = function(){
    	$('body').delegate('.diyCancel','click',function(event){
    		var li = $(this).closest('li'),ul = li.closest('.parentFileBox'),
    			hide = ul.next();
    		li.remove();
			hide.val(Controller.reloadDiy(ul));   
    		return false;
    	})
    	.delegate('button[name="submitform"]','click',function(){
    		if($(this).hasClass('disabled')) return false;
    			Controller.submitSysConCaseForm()
    	});
    };
    exports.hosptialinfo = function(){
    	$('body').delegate('.bindchange','change',function(){
    		var data = $(this).data(),
    			sval = $(this).val(), 
    			stxt = $(':selected',this).text(),
    			type = data.type || '';
    		$('select[data-val]').attr('data-val','');
    		switch(type){
	    		case 'city':
	    			Controller.gainproorcitys('city',sval);
	    			Controller.clearoptions(['dist']);
	    			break;
	    		case 'dist':
	    			Controller.gainproorcitys('dist',sval);
	    			break;
	    		case 'hosid':
	    			Controller.gainproorcitys('hosid',sval);
	    			break;
    		}
    		stxt && Controller.loadMapByName(stxt);
			$('input[name="distCode"]').val(sval);
    	})
    	.delegate('input[name="location"]','blur',function(){
    		this.value && Controller.loadMapByAddr(this.value);
    	})
    	.delegate('input[name="coohos"]','change',function(){
    		this.checked ? 
    			$('.forcoohos').addClass('required') :
    			$('.forcoohos').removeClass('required')    		
    	})
    	.delegate('button[name="submitform"]','click',function(){
    		if($(this).hasClass('disabled')) return false;
    		if(Controller.validateHosForm()){
    			Controller.submitHosForm()
    		}
    	});
    	Controller.initselect().initSelectOps();
    	Controller.initMapByLatLng(parseFloat($('#lng').val()),parseFloat($('#lat').val()));
    };
    exports.hosptialpassed = function(){
    	$('body')
    	.delegate('button[name="submitpassform"]','click',function(){
    		var hosid = $('[name="hosid"]').val();
    		hosid && Controller.passHosForm(1,hosid);
    	})
    	.delegate('button[name="submitdefinedform"]','click',function(){
    		var hosid = $('[name="hosid"]').val();
    		hosid && Controller.passHosForm(2,hosid);
    	});
    	Controller.initselect().initSelectOps();
    };
    exports.docHelpOrder = function(){
    	$('body')
    	.delegate('.doslist','click',function(){
    		$(this).addClass('selected').siblings().removeClass('selected');
    		$('[name="expertId"]').val($(this).attr('data-id'));
    		$('[name="pmoney"]').val($(this).attr('data-c'));
    	})
    	.delegate('.timelist dd.open','click',function(){
    		$('.timelist dd.hover').removeClass('hover');
    		$(this).addClass('hover');
    		$('[name="stimeid"]').val($(this).attr('data-id'));
    	})
    	.delegate('button[name="nextstep"]','click',function(){
    		var step = $(this).attr('data-step');
    		Controller.validateStep(step);
    	})
    	.delegate('#sycnCase','click',function(){
    		var ajaxurl = this.getAttribute('data-ajax'),
    			_btn = $(this),sign = +new Date;
    		_listSign = sign;
    		ajaxurl && Controller.loadBeans(ajaxurl,sign,false);
    		return false;
    	})
    	.delegate('#sycnCaseMore','click',function(){
    		var ajaxurl = this.getAttribute('data-ajax');
			ajaxurl && Controller.loadBeans(ajaxurl,'',true);
			return false;
		})
		.delegate('#sycnPacsMore','click',function(){
    		var ajaxurl = this.getAttribute('data-ajax');
			ajaxurl && Controller.loadPacsBeans(ajaxurl);
			return false;
		})
    	.delegate('.beanquerywhere .filtDetail','click',function(){
    		var ajaxurl = $('#sycnCase').attr('data-ajax'),_btn = $(this),sign = +new Date;
    		if(_btn.hasClass('disabled')) return false;
    		_listSign = sign,_btn.addClass('disabled');
    		ajaxurl && Controller.loadBeansWithWhere(ajaxurl,function(d){    			
    			_btn.removeClass('disabled');
    		},sign);
    		return false;
    	})
    	.delegate('.beanquerywhere .filtPacsDetail','click',function(){
    		var ajaxurl = $('#sycnPacsMore').attr('data-ajax'),_btn = $(this),sign = +new Date;
    		if(_btn.hasClass('disabled')) return false;
    		_listSign = sign,_btn.addClass('disabled');
    		ajaxurl && Controller.loadPacsWithWhere(ajaxurl,function(d){    			
    			_btn.removeClass('disabled');
    		},sign);
    		return false;
    	})
    	.delegate('#pacs .imglist .thumb','click',function(){
    		var lst = $(this).closest('.imglist'),rid = lst.attr('data-rid') || '',ridArr = rid.split('|'),href = '';
    		if(!rid || rid.indexOf('null') != -1 || rid.indexOf('undefined') != -1) return false;
    		$('#pacs .imglist.curr').removeClass('curr'),lst.addClass('curr');
    		//href = 'http://yc.51zjh.com:8088/oviyam2/viewer.html?patientID='+ ridArr[0] +'&studyUID='+ ridArr[1] +'&serverName=&u=admin&p=&_=' + (+new Date);
    		//window.open(href, "_blank");
    		href = location.origin + '/dwv/viewer.html?patientID='+ ridArr[0] +'&studyUID='+ ridArr[1] +'&serverName=';
    		Controller.loadGainPics(href);
		})
		.delegate('.diyUpload .media','click',function(){
			Controller.showEleDia($(this).find('source').attr('src'));
    		return false;
    	})
    	.delegate('.imgdel','click',function(){
    		var li = $(this).closest('.imglist,.reportlist');
    		var block = $(this).closest('.blocklist');
    		li.remove();
    		if(block.find('.imglist,.reportlist').size() > 0){
	    		var swiperParent = li.closest('.beantimelist'),
	    			id = swiperParent.attr('id');
	    		id && Controller.updateSwiper('#'+ id +' .swiper-container');
	    	}else{
	    		block.remove();
	    	}
    		return false;
    	})
    	.delegate('.diyCancel','click',function(event){
    		var li = $(this).closest('li');
    		li.remove();
    		return false;
    	})
    	.delegate('.beantable .tablechk','change',function(event){
    		var vls = [],dirs = [],btn = $('.beanquerywhere .sysncBtn');
    		$('.beantable .tablechk').each(function(){
    			this.checked && (function(arr){
    				arr[0] && vls.push(arr[0]),
    				arr[1] && dirs.push(arr[1])
    			})(this.value.split('@'));
    		});
    		btn.attr('id',vls.join(','));
    		btn.attr('data-dirs',dirs.join(';'));
    		vls.length || dirs.length ? btn.removeClass('disabled') : btn.addClass('disabled');
    	})
    	.delegate('.beantable tbody tr','click',function(event){
    		var chbx = $(this).find('.tablechk')[0];
    		event.target.type != 'checkbox' && (chbx.checked = !chbx.checked,$(chbx).change());
    	})
    	.delegate('.tablebtn','click',function(){
    		var id = this.id,$this = $(this),sign = + new Date();
    		id && (function(){
    			$('#listable').html(Controller.program);
    			$('#pacs').html(Controller.loading);
    			_lisSign = _pacsSign = sign;
    			_signSyncBtn = 1;
    			return true;
    		})() && (
	    		Controller.loadLis('doctor/syncLisInfoByCase',id,function(htm){
	    			$('#listable').html(htm).find('.beantimelist').each(function(){
	            		Controller.initSwiper('#'+ this.id +' .swiper-container');
	        		});
	    		},sign),
	    		Controller.loadPacs('doctor/syncPacInfoByCase',id,function(htm){
	    			htm && $('#pacs').html(htm);
	    			Controller.scrollFun();
	    		},sign)
    		);
    	})
    	.delegate('.tablebtnadd,.tablebtnover','click',function(){
    		var $this = $(this),sign = + new Date();
    		var dirs = this.getAttribute('data-dirs');
    		_signSyncBtn = $this.hasClass('tablebtnadd') ? 0 : 1;
    		dirs && (function(){
    			_pacsSign = sign;
    			!!_signSyncBtn && $('#pacs').html(Controller.loading);
    			return true;
    		})() && (
	    		Controller.loadPacsWithQuery('doctor/syncpacsadvance',{oid:_oid,imagedirs:dirs,syncType:_signSyncBtn,syncSeries:sign},function(htm){
	    			!!_signSyncBtn && htm && $('#pacs').html(htm);
	    			Controller.scrollFun();
	    		},sign)
    		);    		
    	})
    	.delegate('#listable .reportlist','click',function(){
    		var html = $(this).html(),index = $(this).index(),
    			$parent = $(this).closest('.beantimelist'),
    			pindex = $parent.index(),
    			total = $('#listable .beantimelist').size(),
    			_html = html.replace('<div class="report small">','<div class="report">');
    		var f = '',l = '',h = '';
    		pindex != 0 ? (f = '<div data-val="'+ (pindex - 1) +'" class="prevreport reportbtn"><i class="iconfont">&#xe61f;</i></div>') : (f = '');
    		pindex != (total - 1) ? (l = '<div data-val="'+ (pindex + 1) +'" class="nextreport reportbtn"><i class="iconfont">&#xe622;</i></div>') : (l = '');
    		h = Controller.creatHead(this,index);
    		Controller.showDia(h + f + _html + l);
    	})
    	.delegate('.reportbtn','click',function(){
    		var idx = $(this).attr('data-val'),  			
    			even = $('#listable .beantimelist:eq('+ idx +') .reportlist:eq(0)');
    		even.click();
    	})
    	.delegate('#LisModelDetail .fmhoption','click',function(){
    		var idx = $(this).attr('data-id'), idsArr = idx.split('-'),   			
    			$idxEle = $('#'+ idsArr[0] +' .reportlist:eq('+ idsArr[1] +')');
    		var html = $idxEle.html(),_html = html.replace('<div class="report small">','<div class="report">');
    		$('#LisModelDetail .formModelHeadOptions').siblings('.report,label,span').remove().end().after(_html);
    		$(this).addClass('selected').siblings().removeClass('selected');
    	})
    	.delegate('#pics .diyFileName','click',function(event){
    		var input = $(this).find('[name="fileName"]'),txt = $.trim($(this).text());
    		if(_utype !="1" || input.size() > 0 || event.target.type == 'button') return ;
    		$(this).addClass('editing').html('<textarea name="fileName" data-v="'+ txt +'">'+ txt +'</textarea><button type="button" class="saveFileName">保存</button>');
    	})
    	.delegate('#pics .diyFileName .saveFileName','click',function(){
    		var _this = this;
    		if(window._timerFILEName) window.clearTimeout(window._timerFILEName);
    		window._timerFILEName = window.setTimeout(function(){
    			Controller.saveDiyFileName($(_this).prev(':input'));
        		$(this).closest('.editing').removeClass('editing');
    		}, 200); 
    	})
    	.delegate('#pics .diyFileName [name="fileName"]','blur',function(){
    		var _this = this;
    		if(window._timerFILEName) window.clearTimeout(window._timerFILEName);
    		window._timerFILEName = window.setTimeout(function(){
    			Controller.saveDiyFileName($(_this));
        		$(this).closest('.editing').removeClass('editing');
    		}, 200);
    	})
    	.delegate('#pacs .imglist .thumbName','click',function(event){
    		var input = $(this).find('[name="fileName"]'),
    			txt = $.trim($(this).text()),d = txt.split('(')[0],n = $(this).find('i').text();
    		if(_utype !="1" || input.size() > 0 || event.target.type == 'button') return ;
    		$(this).addClass('editing').html('<textarea name="fileName" data-d="'+ d +'" data-v="'+ n +'">'+ n +'</textarea><button type="button" class="saveFileName">保存</button>');
    	})
    	.delegate('#pacs .imglist .thumbName .saveFileName','click',function(){
    		var _this = this;
    		if(window._timerDCIMName) window.clearTimeout(window._timerDCIMName);
    		window._timerDCIMName = window.setTimeout(function(){
        		Controller.saveDICMName($(_this).prev(':input'));
        		$(this).closest('.editing').removeClass('editing');
    		}, 200);
    	})
    	.delegate('#pacs .imglist .thumbName [name="fileName"]','blur',function(){
    		var _this = this;
    		if(window._timerDCIMName) window.clearTimeout(window._timerDCIMName);
    		window._timerDCIMName = window.setTimeout(function(){
        		Controller.saveDICMName($(_this));
        		$(this).closest('.editing').removeClass('editing');
    		}, 200);
    	})
    	.delegate('#uploadBySelf','click',function(){
    		Controller.showUploadDCIM();
    	});

    	$(window).bind("scroll",function(){
        	if(window._timers) window.clearTimeout(window._timers);
        	window._timers = window.setTimeout($.proxy(Controller.scrollFun,Controller),400);
        });
    	$(window).bind("resize",function(){
        	if(window._timers0) window.clearTimeout(window._timers0);
        	window._timers0 = window.setTimeout(function(){
        		$('#listable').find('.beantimelist').each(function(){
            		Controller.updateSwiper('#'+ this.id +' .swiper-container');
        		});
        		Controller.resizeGainDia();
        		Controller.scrollFun();
        		$('#LisModelDetail .formModelHeadOptions').size() && Controller.updateSwiper('#LisModelDetail .formModelHeadOptions');
        	},1);
        });    	
    };
    exports.basicSet = function(){
    	$('body')
    	.delegate('#saveWX','click',function(){
    		Controller.saveWXBasic();
    	})
    	.delegate('#saveHOS','click',function(){
    		Controller.saveHOSBasic();
    	});
    };
    exports.serviceinfoform = function(){
		$('body').delegate('button[name="submitServiceform"]','click',function(){
			if($(this).hasClass('disabled')) return false;
			if(Controller.validateServiceForm()){
				Controller.submitServiceform()
			}
		})
		.delegate('.diyCancel','click',function(event){
			var li = $(this).closest('li'),ul = li.closest('.parentFileBox'),
				hide = ul.next();
			li.remove();
			hide.val(Controller.reloadDiy(ul));
			return false;
		})
	};
	exports.addserviceinfo = function(){
		var totalPacks = [];
		function renderHtml(){
			var html='';
			totalPacks.forEach(function(item,ind){
				html += '<form id="doublePackages'+ind+'"><div class="control-group pleft"><label class="control-label"><span style="color:red;">*</span>排序：</label>' +
					'<div class="controls"><input type="number" name="rank" placeholder ="请输入1-99的整数" value="'+item.rank+'"/></div></div>' +
					'<div class="control-group pleft"><label class="control-label"><span style="color:red;">*</span>包名：</label>'+
					'<div class="controls"><input type="text" name="packageName" value="'+item.packageName+'"/></div></div>'+
					'<div class="control-group pleft"><label class="control-label"><span style="color:red;">*</span>价格：</label>'+
					'<div class="controls"><input type="number" name="packagePrice" value="'+item.packagePrice+'"/></div></div>'+
					'<div class="control-group pleft"><label class="control-label">状态：</label><div class="controls">'+
					'<select name="status"><option value="'+item.status+'">'+(item.status==1? "生效":"不生效")+'</option></select></div></div>'+
					'<div class="control-group pleft"><label class="control-label">描述：</label><div class="controls"><textarea name="description">'+item.description+'</textarea></div></div>'+
					'</form><button class="btn btn-primary delbtn" id="'+ind+'">删除</button><hr/>';
			})
			$('#totalpacks').html(html);
		};
		$('#servicepackages').hide();
		$('#selectMultiple').change(function(){
			if($(this).val()=='0'){
				$('#siglepackform').show();
				$('#servicepackages').hide();
			}else {
				$('#siglepackform').hide();
				$('#servicepackages').show();
			}

		});

		$('body').delegate('button[name="submitServiceform"]','click',function(){
			if($(this).hasClass('disabled')) return false;
			if(Controller.validateServiceForm()){
				Controller.submitServiceform()
			}
		})
		.delegate('button#getServiceInfo','click',function(){
			var modalPackageName = $('#modal-body  input[name="packageName"]').val(),
				modalPackagePrice = $('#modal-body  input[name="packagePrice"]').val(),
				modalPackageRank = $('#modal-body  input[name="rank"]').val();
			if(modalPackageName && modalPackagePrice && modalPackageRank){
				if(modalPackageRank.indexOf('.')==-1&& +modalPackageRank>0 && +modalPackageRank<100){
					var newObj = {};
					($('#modal-body').serializeArray()).forEach(function(item){
						newObj[item.name] =item.value;
					})
					totalPacks.push(newObj);
					$('#myModal').modal('hide');
					renderHtml();
				}else {
					$('#tiptop').html('排序 请输入1-99的整数！');
				}

			}else {
				$('#tiptop').html('包名和价格不能为空,排序输入1-99的整数');
			}

		})
		.delegate('#servicepackages .delbtn','click',function(){
			totalPacks.splice(this.getAttribute('id'),1);
			renderHtml();
		})
		.delegate('#addnewpack','click',function(){
			$('#modal-body input[name="packageName"]').val('');
			$('#modal-body input[name="packagePrice"]').val('');
			$('#modal-body textarea[name="description"]').val('');
			$('#modal-body input[name="rank"]').val('');
			$(".modal-select option[value='1']").attr("selected","selected");
			$('#tiptop').html('');
			$('#myModal').modal('show');
		})
		.delegate('.diyCancel','click',function(event){
			var li = $(this).closest('li'),ul = li.closest('.parentFileBox'),
				hide = ul.next();
			li.remove();
			hide.val(Controller.reloadDiy(ul));
			return false;
		})
	};
});

define('Controller',['view/base'],{
	base:function(){
		return seajs.require('view/base')	
	},
	program:'<div class="progressc"><div class="barc" style="width:0%;"></div><div class="bartxt">0%</div></div>',
	loading:'<div class="loadings"><img alt="" style="" src="'+ window.location.origin +'/img/loading/31.gif"/></div>',
	inlineloading:'<div class="noresult"><img src="'+ window.location.origin +'/img/loading/31.gif" alt=""/></div>',
	noresult:'<div class="noresult"><img src="'+ window.location.origin +'/img/mobile/kbzs/icon_noresult@2x.png" style="width:30px" alt=""/></div>',
	reloadDiy:function(ul){
		var vls = [];
		ul.find('li').each(function(){
			var id = this.getAttribute('data-id') || '';    			
			id && vls.push(id);
		});		
		return vls.join(',');
	},
	clearoptions:function(arr){
		for(var i = 0,l = arr.length; i < l;i++){
			$('select[name="'+ arr[i] +'"]').html('<option value="">---请选择---</option>');
		}
	},
	initselect:function(){
		var defvpro = $('select[name="pro"]').attr('data-val') || '',
		defvcity = $('select[name="city"]').attr('data-val') || '',
		defvdist = $('select[name="dist"]').attr('data-val') || '',
		defvhos = $('select[name="hosid"]').attr('data-val') || '';
		this.gainproorcitys('pro',defvpro);
		defvpro && this.gainproorcitys('city',defvpro);
		defvpro && this.gainproorcitys('dist',defvcity);
		defvpro && this.gainhosbyprovin('hosid',defvpro.substr(0,2));
		defvhos && this.gaindepartsbyhos('depid',defvhos);
		return this;
	},
	initSelectOps:function(){
		var base = this.base();
		$('select.js-ajax').each(function(){
			var pid = this.getAttribute('data-pid'),
				svl = this.getAttribute('data-val') || '',
				select = $(this);
			base.get('system/gainDictorys',{pid:pid},function(d){
				select.html(function(){
					var dics = d.dics || [],ops = [];
					$.each(dics,function(i,o){
						ops += '<option value="'+ o.id +'"'+ (svl == o.id ? 'selected' : '') +'>'+ o.displayName +'</option>';
					});
					return ops;
				});
			},function(){
				select.html('');
			});			
		});
		return this;
	},
	gainproorcitys:function(name,code){
		var base = this.base(),codepro = code.substr(0,2),_codepro=code.substr(0,4);
		base.get('system/gainproorcitys',{stype:name == 'pro' ? 1 : (name == 'city'?2:3),procost: name == 'city' ? codepro :(name == 'dist'?_codepro:'')},function(d){
			var selectval = $('select[name="'+ name +'"]').attr('data-val') || '',
				selectvalpro = selectval.substr(0,2),
				_selectvalpro = selectval.substr(0,4);	
			selectval = name == 'pro' ? selectvalpro + '0000' : (name == 'city' ? _selectvalpro + '00' : selectval);
			// name=name == 'hosid'?"dist":name;
			$('select[name="'+ name +'"]').html(function(){
				var ops = '<option value="">---请选择---</option>';
				$.each(d['codes'] || '',function(i,o){
					ops += '<option value="'+ o.distCode +'"'+ (selectval == o.distCode ? ' selected' : '') +'>'+ o.distName +'</option>';
				});
				return ops;
			});
		},function(){
			$('select[name="'+ name +'"]').html('<option value="">---网络出错---</option>');
		});
	},
	gainhosbyprovin:function(name,code){
		var base = this.base();
		base.get(hosurl || 'system/gainhosbyprovin',{procost: code},function(d){
			var selectval = $('select[name="'+ name +'"]').attr('data-val') || '';
			$('select[name="'+ name +'"]').html(function(){
				var ops = '<option value="">---请选择---</option>';
				$.each(d['hospitals'] || '',function(i,o){
					ops += '<option value="'+ o.id +'"'+ (selectval == o.id ? ' selected' : '') +'>'+ o.displayName +'</option>';
				});
				return ops;
			});
			$('.select2-selection__rendered').text($('[name="hosid"]').children('option:selected').text());
		},function(){
			$('select[name="'+ name +'"]').html('<option value="">---网络出错---</option>');
		});
	},
	gaindepartsbyhos:function(name,code){
		var base = this.base();
		base.get(depurl || 'system/gaindepartsbyhos',{hosid: code},function(d){
			var selectval = $('select[name="'+ name +'"]').attr('data-val') || '';
			$('select[name="'+ name +'"]').html(function(){
				var ops = '<option value="">---请选择---</option>';
				$.each(d['departs'] || '',function(i,o){
					ops += '<option value="'+ o.id +'"'+ (selectval == o.id ? ' selected' : '') +'>'+ o.displayName +'</option>';
				});
				return ops;
			});
		},function(){
			$('select[name="'+ name +'"]').html('<option value="">---网络出错---</option>');
		});
	},
	validateForm:function(){
		var base = this.base();
		var name = $('[name="displayName"]').val(),tel = $('[name="telphone"]').val(),
			hos = $('[name="hosid"]').val(),dep = $('[name="depid"]').val(),
			idcard = $('[name="idCardNo"]').val(),proval = $('[name="pro"]').val(), cityval = $('[name="city"]').val();
		var i = 0;
		$('.serverslist .checkbox input').each(function(){
			if(this.checked && $(this).parent().next('.required').find('input').val().length < 1){
				return i++,false;
			}
		});
		if(i) return base.showTipE('开通服务的价格为必填项'),false;
		if(!name) return base.showTipE('请输入专家姓名'),false;
		if(!base.valideTel(tel)) return base.showTipE('请输入正确的电话格式'),false;
		if(idcard && !base.valideCard(idcard)) return base.showTipE('请输入有效身份证号'),false;
		if(!proval) return base.showTipE('请选择所在省份'),false;
		if(!cityval) return base.showTipE('请选择所在市'),false;
		if(!hos) return base.showTipE('请选择专家所在医院'),false;
		if(!dep) return base.showTipE('请选择专家所在科室'),false;
		return true;
	},
	validateSbForm:function(){
		var base = this.base();
		var yltName = $('[name="yltName"]').val(),profile = $('[name="profile"]').val();
		var i = 0;
		if(!yltName) return base.showTipE('请输入医联体名称'),false;
		if(!profile) return base.showTipE('请输入医联体简介信息'),false;
		return true;
	},
	validateServiceForm:function(){
		function isRepeat(arr){
			var result = [];
			for(var i in arr) { if(result[arr[i]]) return true; result[arr[i]] = true;}
			return false;
		}
		var base = this.base();
		var servicename = $('#myserviceform input[name="serviceName"]').val(),
		multiplePackage = $('[name="multiplePackage"]').val();
		if(!servicename) return base.showTipE('请输入服务名称'),false;
		if($('#selectMultiple').val()==0 ||multiplePackage == 0){
			var packageName = $('#siglepackform input[name="packageName"]').val(),
			packagePrice = $('#siglepackform input[name="packagePrice"]').val();
			if(!packageName) return base.showTipE('请输入服务包名称'),false;
			if(!packagePrice) return base.showTipE('请输入服务包价格'),false;
		}
		if($('#selectMultiple').val()==1 || multiplePackage == 1){
			var packagesName = $('#servicepackages input[name="packageName"]'),
			packagesPrice = $('#servicepackages input[name="packagePrice"]'),
			packagesRanks = $('#servicepackages input[name="rank"]'),obj = {},arr=[];
			obj.packagesName = packagesName;
			obj.packagesPrice = packagesPrice;
			packagesRanks.each(function(item,val){
				arr.push($(val).val());
			});
			arr.forEach(function(item){
				if(!item) return base.showTipE('排序号不能为空！'),false;
				if(!((item).indexOf('.')==-1 && +item>0 && +item<100 )) return base.showTipE('排序号 请输入1-99的整数'),false;
			})
			if(isRepeat(arr)) return base.showTipE('排序号不能重复！'),false;
			for(var k in obj){
				obj[k].each(function(item,val){
					if(!$(val).val()) return base.showTipE('请输入服务包名称或价格'),false;
				})
			}
		}
		return true;
	},
	submitForm:function(){
		var base = this.base(),data = $('#myform').serializeArray();
		$('#myform button[name="submitform"]').addClass('disabled').html('正在保存，请稍后');
		base.showTipIngA('正在保存').post('system/saveorupdateexpert',data,function(d){
			base.showTipS('保存成功' + (!$('#myform [name="expertid"]').val() ? '，专家初使密码为'+ (d['defpass'] || '123456') : '')),
			window.setTimeout(function(){
				window.location.href = _burl + 'system/experts';
			},1000);
		},function(){
			base.showTipE('保存失败');
			$('#myform button[name="submitform"]').removeClass('disabled').html('保存');
		});
	},
	submitOpForm:function(){
		var base = this.base(),data = $('#myform').serializeArray();
		$('#myform button[name="submitform"]').addClass('disabled').html('正在保存，请稍后');
		base.showTipIngA('正在保存').post('system/saveorupdateOp',data,function(d){
			base.showTipS('保存成功'),
			window.setTimeout(function(){
				window.location.href = _burl + 'system/operatormanage';
			},1000);
		},function(){
			base.showTipE('保存失败');
			$('#myform button[name="submitform"]').removeClass('disabled').html('保存');
		});
	},
	submitOpDocForm:function(){
		var base = this.base(),data = $('#myform').serializeArray();
		$('#myform button[name="submitform"]').addClass('disabled').html('正在保存，请稍后');
		base.showTipIngA('正在保存').post('system/saveOrUpdateDoc',data,function(d){
			base.showTipS('保存成功'),
			window.setTimeout(function(){
				window.location.href = _burl + 'system/localdocs';
			},1000);
		},function(){
			base.showTipE('保存失败');
			$('#myform button[name="submitform"]').removeClass('disabled').html('保存');
		});
	},
	submitSysConCaseForm:function(){
		var base = this.base(),data = $('#myform').serializeArray();
		$('#myform button[name="submitform"]').addClass('disabled').html('正在保存，请稍后');
		base.showTipIngA('正在保存').post('system/saveorupdateSysConCaseInfo',data,function(d){
			base.showTipS('保存成功'),
			window.setTimeout(function(){
				window.location.href = _burl + 'system/sysConCaseManage';
			},1000);
		},function(){
			base.showTipE('保存失败');
			$('#myform button[name="submitform"]').removeClass('disabled').html('保存');
		});
	},

	submitServiceform:function(){
		var base = this.base(),totalObj = {},packageObj={},arr = [],newObj = {},
		serviceObj = $('#myserviceform').serializeArray();
		serviceObj.forEach(function(item){
			newObj[item.name] = item.value;
		});
		totalObj["serviceObj"] = newObj;
		if(serviceObj[serviceObj.length-1].value== '0'){
			var newObj2 = {};
			packageObj = $('#siglepackform').serializeArray();
			packageObj.forEach(function(item){
				newObj2[item.name] = item.value;
			})
			arr.push(newObj2);
			totalObj["packageObj"]=arr;
		}else {
			$('#servicepackages form').each(function(item,val){
				var packpages={};
				$(val).serializeArray().forEach(function(item){
					packpages[item.name]=item.value;
				})
				arr.push(packpages);
			})
			totalObj["packageObj"]=arr;
		}
		$('#serviceInfo button[name="submitServiceform"]').addClass('disabled').html('正在保存，请稍后');
		base.showTipIngA('正在保存').post('system/saveorupdateServiceInfo',{ obj:JSON.stringify(totalObj) },function(d){
			base.showTipS('保存成功');
			window.setTimeout(function(){
				window.location.href = _burl + 'system/serviceinfo';
			},1000);
		},function(){
			base.showTipE('保存失败');
			$('#serviceInfo button[name="submitServiceform"]').removeClass('disabled').html('保存');
		});
	},
	sbForm:function(){
		var base = this.base(),data = $('#myform').serializeArray();
		$('#myform button[name="sbform"]').addClass('disabled').html('正在保存，请稍后');
		base.showTipIngA('正在保存').post('system/savehoshealth',data,function(d){
			base.showTipS('保存成功'),
			window.setTimeout(function(){
				window.location.href = _burl + 'system/hoshealthmanage';
			},1000);
		},function(){
			base.showTipE('保存失败');
			$('#myform button[name="sbform"]').removeClass('disabled').html('保存');
		});
	},
	validateHosForm:function(){
		var base = this.base();
		var name = $('[name="displayName"]').val();
		var email = $('[name="contactorEmail"]').val(),
			$tel = $('[name="contactorTelphone"]'),
			tel = $tel.val(),
			$coname = $('[name="contactorName"]'),
			coname = $coname.val(),
			$coo = $('[name="coohos"]'),
			doc = $('[name="dockingMode"]').val(),
			proval = $('[name="pro"]').val(),
			cityval = $('[name="city"]').val(),
			distval = $('[name="dist"]').val();
		if(!name) return base.showTipE('请输入医院名称'),false;
		if($coo[0].checked){
			if(!doc) return base.showTipE('请选择对接模式。'),false;
			if($coname.size() && !coname) return base.showTipE('请输入联系人姓名。'),false;
			if($tel.size() && !base.valideTel(tel)) return base.showTipE('请输入有效的联系人电话。'),false;
		}

		if(!proval) return base.showTipE('请选择所在省份'),false;
		if(proval=="110000" ||proval=="310000" ||proval=="120000" ||proval=="500000" ){
			if(!cityval) return base.showTipE('请选择所在市'),false;
			if(!distval) return base.showTipE('请选择所在区'),false;
		}else {
			if(!cityval) return base.showTipE('请选择所在市'),false;
		}
		return true;
	},
	submitHosForm:function(){
		var base = this.base(),_this = this,data = $('#myform').serializeArray();
		$('#myform button[name="submitform"]').addClass('disabled').html('正在保存，请稍后');
		base.showTipIngA('正在保存').post('system/saveorupdatehos',data,function(d){
			if(!$('#myform [name="hosid"]').val() && $('#myform [name="coohos"]')[0].checked){
				base.hideTip(),
				_this.confirmLoginName(d.accountid,d.loginName,d.hosid);
			}else{
				base.showTipS('保存成功'),
				window.setTimeout(function(){
					window.location.href = _burl + 'system/hospitals';
				},400);
			}
		},function(){
			base.showTipE('保存失败');
			$('#myform button[name="submitform"]').removeClass('disabled').html('保存');
		});
	},
	passHosForm:function(auditSta,hosid){
		var base = this.base(),_this = this;
		if($('#myform .submitform').hasClass('disabled')) return false;
		$('#myform button.submitform').addClass('disabled');
		base.showTipIngA('正在操作').post('system/auditthrough',{auditSta:auditSta,hosid:hosid},function(d){			
			if(auditSta == 1){
				base.hideTip(),
				_this.confirmLoginName(d.accountid,d.loginName,hosid);
			}else{
				base.showTipS('操作成功'),
				window.setTimeout(function(){
					window.location.href = _burl + 'system/hospitals';
				},400);
			}
		},function(){
			base.showTipE('操作失败');
			$('#myform button.submitform').removeClass('disabled');
		});
	},
	confirmLoginName:function(accountid,loginName,hosid){
		var base = this.base(),_this = this;
		base.showDialog({
			id:'confirmLoginName',
			title: '确认帐号名',
			cls:'modal2-500',
			text: '<label><span>帐号名：</span><input type="text" name="loginname" value="'+ loginName +'" style="width: 90%;margin-top: 6px;"/></label>',
			ok: function(){
				var lname = $('#confirmLoginName [name="loginname"]').val();
				lname && base.showTipIngA('正在保存').post('system/sureconfirm',{accountid:accountid,hosid:hosid,loginName:lname},function(d){			
					if(d.status == 'error'){
						base.showTipE('账号已存在，重新输入')
					}else{
						base.showTipS('操作成功'),
						window.setTimeout(function(){
							window.location.href = _burl + 'system/hospitals';
						},400);
					}
				},function(){
					base.showTipE('操作失败');
					$('#myform button.submitform').removeClass('disabled');
				});
			}
		});
	},
	serialArrToObj:function(arr){
		var obj = {};
		$.each(arr,function(i,o){
			obj[o.name] = (function(){
				var v = [];
				if(o.name in obj) v = obj[o.name].split(',');
				v.push(o.value);
				return v.join(',')
			})();
		});
		return obj;
	},
	initbaiduMap:function(lng,lat){
		var map,_this = this;
		if(window._map) return window._map;
		map = new BMap.Map("picklatlng");
		map.enableScrollWheelZoom(true);
		map.centerAndZoom(new BMap.Point(lng || 116.331398,lat || 39.897445),12);
		map.addEventListener("click", showInfo);		
		function showInfo(e){
			var marker = new BMap.Marker(new BMap.Point(e.point.lng, e.point.lat));
			deleteAllPoint();
			map.addOverlay(marker);
			_this.setLonLat(e.point.lng, e.point.lat);
		}
		function deleteAllPoint(){
			var allOverlay = map.getOverlays();
			for (var i = 0; i < allOverlay.length -1; i++){
				map.removeOverlay(allOverlay[i]);
			}
		}
		window._map = map;
		return map;
	},
	initMapByLatLng:function(lng,lat){
		var map = this.initbaiduMap(lng,lat),geolocation = new BMap.Geolocation();
		if(lng && lat){
			var point = new BMap.Point(lng, lat);
			map.centerAndZoom(point, 16);
			map.addOverlay(new BMap.Marker(point));
			map.panTo(point);
		}else{
			geolocation = new BMap.Geolocation();
			geolocation.getCurrentPosition(function(r){
				if(this.getStatus() == BMAP_STATUS_SUCCESS){
					map.addOverlay(new BMap.Marker(r.point));
					map.panTo(r.point);
				}
				else {
					console.log('failed'+this.getStatus());
				}        
			});
		}
	},
	loadMapByName:function(city){
		var map = this.initbaiduMap();
		map.centerAndZoom(city,10);
	},
	loadMapByAddr:function(add){
		var base = this.base(),_this = this;
		var map = this.initbaiduMap();
		var myGeo = new BMap.Geocoder();
		myGeo.getPoint(add, function(point){
			if (point) {
				map.centerAndZoom(point, 16);
				map.addOverlay(new BMap.Marker(point));
				_this.setLonLat(point.lng, point.lat);
			}else{
				base.showTipE('您输入的地址没有解析到经纬度，可以通过点击地图获取经纬度');
			}
		});
	},
	setLonLat:function(lng,lat){
		$('#lng').val(lng);
		$('#lat').val(lat);
	},
	imgDo:function(selector){
		$(selector).each(function(){					
			var img = $('.thumb img',this);
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
	},
	initDocTimeLists:function(selector,sid){
		var base = this.base(),_this = this,totalday = 7,$selector = $(selector);
		base.showLineLoading();
		$selector.html(function(){
			var today = new Date(),day = today.getDate(),week,cdate,ymd,md,dls = '';
			for(var i = 0;i < totalday;i++){
				cdate = new Date(),cdate.setDate(day + i);
				md = (cdate.getMonth() + 1) +'/' + cdate.getDate();
				ymd = _this.formatDate(cdate.getFullYear() + '/'+ md);
				week = _this.getWeek(cdate.getDay());
				dls += '<dl data-ymd="'+ ymd +'" data-week="'+ week +'" class="swiper-slide timelist"><dt>'+ md +'（'+ week +'）</dt></dl>';
			}
			return dls;
		}).find('dl[data-ymd]').each(function(){
			var ymd = $(this).attr('data-ymd');
			_this.initDocTimeList(sid,ymd,function(dd){
				totalday--;
				!totalday && base.hideLineLoading();
				$selector.find('dl[data-ymd="'+ ymd +'"]').append(dd || '<dd>09:00</dd><dd>10:00</dd><dd>14:00</dd><dd>16:00</dd><dd>17:00</dd>');
			});
		});
	},
	initDocTimeList:function(sid,sdate,callback){
		var base = this.base(),_this = this;
		base.post('wzjh/gainSpecialTimes',{sid:sid,sdate:sdate},function(d){
			var times = d.times || [];			
			callback((function(){
				var dds = '';
				$.each(times,function(i,o){
					dds += '<dd data-id="'+ o.id +'" data-a="'+ (o.startTime < '12:00' ? '上午' : '下午') +'" data-t="'+ o.startTime 
						+'" data-c="'+o.cost+'" class="'+ (o.hasAppoint == '1' ? 'disabled' : 'open') +'">'+ o.startTime +'</dd>';
				});
				return dds;
			})());
		},function(){
			callback();
		});
	},
	validateStep:function(step){
		var base = this.base(),_this = this;
		var expid = $('[name="expertId"]').val(),
			timid = $('[name="stimeid"]').val(),
			username = $('[name="username"]').val(),
			telphone = $('[name="telphone"]').val(),
			age = $('[name="age"]').val(),
			idcard = $('[name="idcard"]').val(),
			oid = $('[name="oid"]').val();
		var arr = step.split('|');
		switch(step){
			case '-1':
				var href = $('.form-action [data-step="-1"]').attr('data-href');
				href && window.parent.triggermenu && window.parent.triggermenu(href);
				return false;
			case '2':
				if(!username) return base.showTipE('请输入姓名'),false;
				if(!base.valideTel(telphone)) return base.showTipE('请输入有效的电话'),false;
				if(!age) return base.showTipE('请输入有效年龄'),false;
				if(idcard && !base.valideCard(idcard)) return base.showTipE('请输入有效身份证号'),false;
				return this.subtimeHelpOrderbase(),false;
			case '2-1':
				step = 2;
				break;
			case '2|3|4':
				!!expid ? 
					(step = 4) : 
					(step = 2);				
				break;
			case '3|6':
				$('.section3').size() ? 
					(step = 3) : 
					(step = 6,$('[name="expertId"],[name="stimeid"],[name="pmoney"]').val(''));
				$('[name="expertId"],[name="pmoney"]').val(''),$('.section3 .doslist.selected').removeClass('selected');
				if(step == 6) return this.creatHelpOrderWXurlCodeWithoutExport(),false;
				break;
			case '4|5':
				!!expid ? 
					(step = 4,this.initDocTimeLists('.doctimes .swiper-wrapper',expid)) : (step = 5);
				if(step == 5) return this.creatHelpOrderWXurlCodeWithoutExport(),false;
				break;
			case '5':
				if(!expid) return base.showTipE('请选择专家'),false;
				if(!timid) return base.showTipE('请专家出诊时间段'),false;
				return this.creatHelpOrderWXurlCode(),false;
			case '5-1':
				step = 5;
				break;
			case '6':
				return this.listenWxPay(),false;
				break;
			case '6-1':
				step = 6;
				break;
		}
		$('.section' + step).show().siblings().hide();
		for(var i = 1;i < step;i++){
			$('.topheader .state' + i).addClass('passed').removeClass('animate');
		}
		$('.topheader .state' + step).addClass('passed animate').nextAll().removeClass('passed animate');
		step == '3' && this.imgDo('.dochelporder .doslist');
	},
    listenWxPay:function(){
    	var base = this.base(),_this = this;
    	var tradeno = $('#myform [name="out_trade_no"]').val();
    	tradeno && base.post('kangxin/listenpaystatus',{tradeno:tradeno},function(d){
    		if(d.status == 'success'){
    			_this.validateStep('6-1');
    		}else{
    			window.setTimeout(function(){
    				_this.listenWxPay();
    	    	},3000);
    		}
    	});
    },
	creatHelpOrderWXurlCode:function(){
		this.refreshHidden();
		this.creatHOWXurl('wzjh/surepay_pc');
	},
	creatHelpOrderWXurlCodeWithoutExport:function(){
		this.refreshHidden();
		this.creatHOWXurl('docadmin/subnoexpertorder');
	},
	creatHOWXurl:function(url){
		var base = this.base(),data = $('#myform').serializeArray(),_this = this;		
		base.showTipIngA('正在保存').post(url,data,function(d){
			var cost = $('[name="pmoney"]').val() || '',src = _burl + 'kangxin/showqr?code_url=' + d.code_url;
			base.hideTip();
			d.code_url && $('.section5 .imgerm img').attr('src',src);
			d.code_url && $('.section5 .pay h2').html(cost.indexOf('.') != -1 ? cost : (cost + '.00'));
			d.code_url && $('[name="out_trade_no"]').val(d.out_trade_no);
			_this.validateStep(d.code_url ? '5-1' :'6-1');
		},function(){
			base.showTipE('保存失败');
		});
	},
	subtimeHelpOrderbase:function(){
		var base = this.base(),data = $('#myform').serializeArray(),_this = this;
		base.showTipIngA('正在保存').post('docadmin/createorder',data,function(d){
			base.hideTip(),
			!_orderid && (_this.initUploader(d.oid),_this.goEasy(d.oid)),
			_orderid = _oid = d.oid,
			$('[name="oid"]').val(d.oid);
			_this.validateStep('2-1');
		},function(){
			base.showTipE('保存失败');
		});
	},
	initUploader:function(orderid){
		var _this = this;
		seajs.use('view/webupload',function(upload){
			$('#pics #addfiles').each(function(){
				$(this).Uploader({
	        		server: _href + 'doctor/uploadLocalFile',
	        		formData: { orderid: orderid },
	        		thumb: {
		                width: 60,
		                height: 60
		            },
		            accept:{
		            	title: "Files",
		                extensions: "gif,jpg,jpeg,bmp,png,tif,mp4,avi,webm,mkv,mov,rm,ogg.ogv",
		                mimeTypes: "image/*,video/*"
		            },
	        		success:function(liobj,val){
	        			var ul = liobj.closest('.parentFileBox'),hide = ul.next();
	        			hide.val(_this.reloadDiy(ul));        			
	        		},
	        		afterpost:function(){
	        			_diyUploadSelector.viewer('destroy').viewer({
	        				url: 'data-src'
	        			});
	        		}
	        	});
			});        	
		});
		seajs.use('view/viewer',function(view){
    		_diyUploadSelector = view.init('.userinfo .diyUpload .fileBoxUl',{ 
    			navbar:true
    		});
    	});
	},
	loadBeans:function(url,sign,bol){
		var _this = this,base = _this.base();
		base.showDialog({
			id:'FormModelEdit',
			title:'病历列表',
			text:bol ? _this.queryWhere() : _this.queryWhereonly(),
			nofooter:true
		});
		!bol && base.post(_href + url,{oid:_orderid,idsearch:'true',syncSeries:sign},function(d){	
			if(_listSign != sign) return false;
			var beans = d.beans || [];
			if(beans.length <=0 ) _idsearch = "false";
			$('#FormModelEdit .modal2-body').find('.noresult').remove().end().append(_this.ansysBeans(d));
		},function(){
			if(_listSign != sign) return false;
			base.showTipE('加载失败').hideDialog('FormModelEdit');
		});

    	$('#FormModelEdit').on('hide.bs.modal', function (e) {
    		$('body').removeClass('modal2-open')
    	});
	},
	loadPacsBeans:function(url){
		var _this = this,base = _this.base();
		base.showDialog({
			id:'FormModelEdit',
			title:'影像列表',
			text:_this.queryWherePacs(),
			nofooter:true
		});
    	$('#FormModelEdit').on('hide.bs.modal', function (e) {
    		$('body').removeClass('modal2-open')
    	});
	},
	loadPacsWithWhere:function(url,callback,sign){
		var _this = this,base = _this.base(),
			data = this.getQueryWhere();
		$('#FormModelEdit .beanquerywhere').siblings().remove().end().after(_this.inlineloading);
		data['orderid'] = _oid;
		base.post(_href + url,data,function(d){	
			if(_listSign != sign) return false;
			$('#FormModelEdit .beanquerywhere').siblings().remove().end().after(_this.ansysPacsTable(d));
			callback();			
		},function(){
			if(_listSign != sign) return false;
			base.showTipE('加载失败');
			callback();	
		});
	},
	loadBeansWithWhere:function(url,callback,sign){
		var _this = this,base = _this.base(),
			data = this.getQueryWhere();
		$('#FormModelEdit .beanquerywhere').siblings().remove().end().after(_this.inlineloading);
		base.post(_href + url,data,function(d){	
			if(_listSign != sign) return false;
			$('#FormModelEdit .beanquerywhere').siblings().remove().end().after(_this.ansysBeans(d));
			callback();			
		},function(){
			if(_listSign != sign) return false;
			base.showTipE('加载失败');
			callback();		
		});
	},
	getQueryWhere:function(){
		var input = {};
		$('.beanquerywhere :input').each(function(){
			input[this.name] = this.value;
		});
		input['idsearch']=_idsearch;
		return input;
	},
	queryWhere:function(){
		return '<div class="beanquerywhere row-fluid">'
			+ '<div class="form-group span2"><label class="form-label">姓名：</label><div class="form-inputs"><input type="text" name="searchName"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">病人ID：</label><div class="form-inputs"><input type="text" name="searchPatientId"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">住院号：</label><div class="form-inputs"><input type="text" name="adminsionNum"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">门诊号：</label><div class="form-inputs"><input type="text" name="outpatientNum"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">科室名：</label><div class="form-inputs"><input type="text" name="department"/></div></div>'
			+ '<div class="form-group span2"><div class="form-action"><button type="button" class="filtBtn filtDetail">检索</button><button type="button" class="sysncBtn tablebtn disabled">同步</button></div></div>'
			+'</div>'
	},
	queryWherePacs:function(){
		return '<div class="beanquerywhere has3button row-fluid">'
			+ '<div class="form-group span2"><label class="form-label">患者姓名：</label><div class="form-inputs"><input type="text" name="patientName"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">检查编号：</label><div class="form-inputs"><input type="text" name="checkNo"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">影像类型：</label><div class="form-inputs"><input type="text" name="checkType"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">缴费单号：</label><div class="form-inputs"><input type="text" name="mzNumber"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">住院号：</label><div class="form-inputs"><input type="text" name="patientId"/></div></div>'
			+ '<div class="form-group span2"><label class="form-label">日期：</label><div class="form-inputs"><input type="text" ltype="date" name="regtime"/></div></div>'
			+ '<div class="form-group span2"><div class="form-action"><button type="button" class="filtBtn filtPacsDetail">检索</button>'
			+ '<button type="button" title="追加到已同步数据之后（去重）" class="sysncBtn tablebtnadd disabled">追加同步</button><button type="button" title="覆盖之前已同步的数据" class="sysncBtn tablebtnover disabled">覆盖同步</button></div></div>'
			+'</div>'
	},
	queryWhereonly:function(){
		return '<div class="beanquerywhere row-fluid">'
			+ '<div class="form-group spanonly"><div class="form-action"><button type="button" class="sysncBtn tablebtn disabled">同步</button></div></div>'
			+'</div>'
			+ this.inlineloading;
	},
	ansysBeans:function(d){
		var tables = '<table class="table beantable"><thead><tr><th>病人ID</th><th>姓名</th><th>性别</th><th>年龄</th><th>科室</th><th>门诊日期</th><th style="width:6.5em;">选择</th></tr></thead><tbody>{0}</tbody></table>';
		var trs = '',tr,kvs,beans = d.beans || [];
		for(var i = 0,l = beans.length;i<l;i++){
			tr = d.beans[i],kvs=tr['kvs'];
			trs += '<tr>';
			trs += '<td>'+ kvs['病人ID'] +'</td>';
			trs += '<td>'+ kvs['姓名'] +'</td>';
			trs += '<td>'+ kvs['性别'] +'</td>';
			trs += '<td>'+ kvs['年龄'] +'</td>';
			trs += '<td>'+ kvs['病人科室'] +'</td>';
			trs += '<td>'+ kvs['门诊日期'] +'</td>';
			trs += '<td><input type="checkbox" class="tablechk" value="'+ kvs['病人ID'] +'"/></td>';
			trs += '</tr>';
		}
		tables = tables.replace('{0}',trs);
		
		return tables;
	},
	ansysPacsTable:function(d){
		var tables = '<table class="table beantable"><thead><tr><th>姓名</th><th>病人ID</th><th>交费单号</th><th>检查编号</th><th>日期</th><th style="width:20%">检查项</th><th>影像类型</th><th>选择</th></tr></thead><tbody>{0}</tbody></table>';
		var trs = '',tr,kvs,beans = d.pacbeans || [];
		for(var i = 0,l = beans.length;i<l;i++){
			tr = d.pacbeans[i],kvs=tr['kvs'];
			trs += '<tr>';
			trs += '<td>'+ kvs['Patient_Name'] +'</td>';
			trs += '<td>'+ kvs['Patient_ID'] +'</td>';
			trs += '<td>'+ kvs['MZ_Number'] +'</td>';
			trs += '<td>'+ kvs['Check_No'] +'</td>';
			trs += '<td title="'+ kvs['REGISTER_DATE'] +'">'+ kvs['REGISTER_DATE'] +'</td>';
			trs += '<td title="'+ kvs['Check_Item_E'] +'">'+ kvs['Check_Item_E'] +'</td>';
			trs += '<td>'+ kvs['Modality'] +'</td>';
			trs += '<td><input type="checkbox" class="tablechk" value="@'+ kvs['Modality'] +':'+ kvs['Image_Directory'] +'"/></td>';
			trs += '</tr>';
		}
		tables = tables.replace('{0}',trs);
		
		return tables;
	},
	anyscAjaxLis:function(beforecallback,callback,sign){
		var _this = this,base = _this.base();
		beforecallback();
		base.get(_href + 'doctor/gainLisData',{oid:_orderid,syncSeries:sign},function(d){	
			var lishtm;
			if(sign != _lisSign) return false;
			lishtm = d.records ? _this.ansysLisBeansWithTime(d.records) : _this.noresult;
			callback(lishtm);
		},function(){
			if(sign != _lisSign) return false;
			callback(_this.noresult);
			base.showTipE('LIS拉取数据失败');
		});
	},
	anyscAjaxPacs:function(beforecallback,callback,sign){
		var _this = this,base = _this.base();
		beforecallback();
		base.get(_href + 'doctor/gainPacsData',{oid:_orderid,syncSeries:sign},function(d){	
			var pashtm;
			if(sign != _pacsSign) return false;
			pashtm = d.pac_records ? _this.ansysPacsBeansWithSign(d.pac_records) : _this.noresult;
			callback(pashtm);
		},function(){
			if(sign != _pacsSign) return false;
			callback(_this.noresult);
			base.showTipE('PACS拉取数据失败');
		});
	},
	loadLis:function(url,pid,callback,sign){
		var _this = this,base = _this.base();
		base.showTipIngA('正在同步').hideDialog('FormModelEdit').post(_href + url,{oid:_orderid,patientid:pid,syncSeries:sign},function(d){	
			if(sign != _lisSign) return false;
			base.showTipS('LIS同步成功');
			callback(d.lisbeans ? _this.ansysLisBeansWithTime(d.lisbeans) : _this.noresult);
		},function(){
			if(sign != _lisSign) return false;
			callback(_this.noresult);
			base.showTipE('LIS同步失败');
		});
	},
	loadPacs:function(url,pid,callback,sign){
		var _this = this,base = _this.base();
		base.showTipIngA('正在同步').hideDialog('FormModelEdit').post(_href + url,{oid:_orderid,patientid:pid,syncSeries:sign},function(d){	
			if(sign != _pacsSign) return false;
			base.showTipS('PACS同步成功');
			callback('');
		},function(){
			if(sign != _pacsSign) return false;
			callback(_this.noresult);
			base.showTipE('PACS同步失败');
		});
	},
	loadPacsWithQuery:function(url,data,callback,sign){
		var _this = this,base = _this.base();
		base.showTipIngA('正在同步').hideDialog('FormModelEdit').post(_href + url,data,function(d){	
			if(sign != _pacsSign) return false;
			base.showTipS('PACS同步成功');
			callback('');
		},function(){
			if(sign != _pacsSign) return false;
			callback(_this.noresult);
			base.showTipE('PACS同步失败');
		});
	},
	ansysLisBeansWithTime : function(d){
		var timlist = [],_this = this;
		$.each(d,function(i,o){
			var div = '<div class="blocklist beantimelist clearfix" id="key'+ o.key.replace(/\//g,'') +'">';
			div += '<div class="beantime"><span class="time">'+ o.key +'</span></div>';
			div += '<div class="swiper-container"><div class="swiper-wrapper">' + _this.ansysLisBeans(o.beans) + '</div><div class="swiper-button-next"></div><div class="swiper-button-prev"></div></div>';
			div += '</div>';
			timlist.push(div);
		});
		return timlist.join('') || this.noresult;
	},
	ansysLisBeans:function(d){
		var rls = [];
		//console.log('ansysLisBeans', JSON.stringify(d, null, '\t'));
		$.each(d,function(i,o){
			var reportlist = '<div class="reportlist"></div>',
				reportModel = $('#report_model').html(),trsO = [],trsE = [],half = o.beans.length / 2;
			var delimg = _utype == '1' ? '<span class="imgdel iconfont">&#xe60b;</span>' : '';
			reportModel = reportModel.replace('{record_name}',o['kvs']['record_name']);
			
			$.each(o.beans,function(ii,b){
				var bkvs = b['kvs'],sx = bkvs['缩写'] || '',sxarr = sx.split('('),trs = '';
				trs += '<tr><td>'+ sx +'</td>';
				trs += '<td>'+ bkvs['中文名'] +'</td>';
				trs += '<td>'+ bkvs['检验结果'] +'</td>';
				trs += '<td style="text-align:center;">'+ (bkvs['标志'] == 'None' ? '' : bkvs['标志']) +'</td>';
				trs += '<td>'+ bkvs['参考区间'] +'</td>';
				trs += '<td>'+ bkvs['单位'] +'</td></tr>';
				if(!ii){
					$.each(bkvs,function(x,y){
						reportModel = reportModel.replace('{'+ x +'}',bkvs[x]);
					});
				}
				ii < half ? trsO.push(trs) : trsE.push(trs);
			});
			reportModel = reportModel.replace('{tbody}',trsO.join(''));
			reportModel = reportModel.replace('{tbody1}',trsE.join(''));
			rls.unshift('<div class="reportlist swiper-slide" data-id="'+ o['key'] +'">'+ reportModel +'<label class="re_title">'+ o['kvs']['record_name'].replace('报告单',' ' + o['remark'])  +'</label>'+ delimg +'</div>');
		});
		return rls.join('');
	},
	creatHead:function(ele,idx){
		var $parent = $(ele).closest('.beantimelist'),$rplist = $parent.find('.reportlist'),
			pid = $parent.attr('id'),head = '<div class="formModelHeadOptions"><div class="swiper-wrapper">{o}</div></div>',ops = [];
		if($rplist.size() == 1) return '';
		$rplist.each(function(i){
			var head = $('.re_title',this).text();
			ops.push('<div class="swiper-slide fmhoption'+ (idx == i ? ' selected' : '') +'" data-id="'+ pid +'-'+ i +'">'+ head.split(' ')[0] +'</div>');
		});
		return head.replace('{o}',ops.join(''));
	},
	ansysPacsBeansWithSign:function(d){
		var signlist = [],_this = this;
		$.each(d,function(i,o){
			var div = '<div class="blocklist pacsSignlist clearfix '+ o.key +'" data-val="0">';
			div += '<div class="pacsSign"><span class="sing">'+ o.key +'</span></div>';
			div += _this.ansysPacsBeans(o.beans);
			div += '</div>';
			signlist.push(div);
		});
		return signlist.join('') || this.noresult;
	},
	ansysPacsBeans:function(d){
		var pacs = '',_this  = this;
		$.each(d,function(i,o){
			pacs += '<div class="imglist" data-id="'+ o['key'] + '" data-rid="'+ o.kvs['patient_id'] + '|'+ o.studyId + '" data-val="'+ _this.ansysUrl(o) + '">';
			pacs += '<div class="thumb">';
			//pacs += '<img src="'+ _this.ansysUrl(o) + '"/>';
			pacs += '</div>';
			_utype == '1' && (pacs += '<span class="imgdel iconfont">&#xe60b;</span>');
			pacs += '<label class="thumbName">'+ o['remark'] +'(<i>'+ o.kvs['Check_Item_E'] + '</i>)</label>';
			pacs += '</div>';
		});
		return pacs;
	},
	initSwiper:function(selector){
		seajs.use('view/swiper',function(swiper){
			_swipers[selector] = swiper.init(selector,{
    			slidesPerView: 'auto',
		        spaceBetween: 0,
		        freeMode: true,
		        nextButton: '.swiper-button-next',
		        prevButton: '.swiper-button-prev'
    		});
    	});		
	},
	updateSwiper:function(selector){
		_swipers[selector] && _swipers[selector].update();
	},
	showDia:function(html){
		var _this = this,base = _this.base();		
		base.hideDialog('LisModelDetail').showDialog({
			id:'LisModelDetail',
			text:html,
			nofooter:true
		});
    	$('#LisModelDetail').on('hide.bs.modal', function (e) {
    		$('body').removeClass('modal2-open')
    	});
    	$('#LisModelDetail .formModelHeadOptions').size() && seajs.use('view/swiper',function(swiper){
    		var newswp = _swipers['#LisModelDetail .formModelHeadOptions'] = swiper.init('#LisModelDetail .formModelHeadOptions',{
    			slidesPerView: 'auto',
		        spaceBetween: 0,
		        freeMode: true
    		});
    		window.setTimeout(function(){
    			var indexofa = $('.formModelHeadOptions .selected').index();
        		newswp.update();
    			indexofa && newswp.slideTo(indexofa, 1000, false);
    		},400);
    	});
	},
	showEleDia:function(txt){
		var _this = this,base = _this.base();		
		base.showDialog({
			id:'showEleDia',
			text:'<iframe name="videojsiframe" src="'+ _href +'sea-modules/videojs/examples/index.html?src='+ txt +'" frameborder="no" border="0" allowscriptaccess="always" marginwidth="0" marginheight="0" allowfullscreen="true" wmode="opaque" allowTransparency="true" type="application/x-shockwave-flash"></iframe>',
			cls:'modal2-600',
			nofooter:true
		});
    	$('#showEleDia').on('hide.bs.modal', function (e) {
    		$('body').removeClass('modal2-open');
    		window.setTimeout(function(){
    			$('#showEleDia').remove();
    		},400);
    	});
	},
	resizeGainDia:function(){
		var height = $(window).height();
		$('#showDWVModel .dwvContainer').css({height: height + 'px',overflow:'hidden'}); 
	},
	loadGainPics:function(href){
		var _this = this,base = _this.base();
		base.showDialog({
			id:'showDWVModel',
			cls:'modal2-auto',
			text:_this.ansysSeries(href),
			nofooter:true
		});  
    	$('#showDWVModel').on('hide.bs.modal', function (e) {
    		var onlyHeader = window.parent.onlyHeader || function(){};
    		$('body').removeClass('modal2-open');
    		onlyHeader('esc');
    	}); 
    	$('#showDWVModel').on('shown.bs.modal', function (e) {
    		var onlyHeader = window.parent.onlyHeader || function(){};
    		onlyHeader();
    	}); 
	},
	ansysSeries:function(href){
		var height = $(window).height();
		return '<div class="row-fluid dwvOuter"><div class="dwvContainer prelative span12" style="overflow:hidden;height:'+ height +'px"><iframe name="dwviframe" src="'+ href +'" id="dwviframe" frameborder="no" border="0" allowscriptaccess="always" marginwidth="0" marginheight="0" allowfullscreen="true" wmode="opaque" allowTransparency="true" type="application/x-shockwave-flash"></iframe></div></div>';
	},
	ansysUrl:function(o){
		return _href + 'dcmimage?study='+ o.studyId +'&series='+ o.seriesId +'&object='+ o.instanceId;
	},
	refreshHidden:function(){
		var pics = [],his = [],lis = [],pacs = [];
		$('#pics li').each(function(){
			var id = this.getAttribute('data-id');
			id && pics.push(id);
		});
		$('#pics_hidden').val(pics.join(','));
		
		$('#listable .reportlist').each(function(){
			var id = this.getAttribute('data-id');
			id && lis.push(id);
		});
		$('#lis_ids').val(lis.join(','));
		
		$('#pacs .imglist').each(function(){
			var id = this.getAttribute('data-id');
			id && pacs.push(id);
		});
		$('#pacs_ids').val(pacs.join(','));
	},
	validaForm:function(){
		var _this = this,base = _this.base();
		var tel = $('#myform [name="telphone"]').val(),
			card = $('#myform [name="idcard"]').val();
		if(card && !this.valideCard(card)) return base.showTipE('请输入有效的身份证号'),false;
		if(!this.valideTel(tel)) return base.showTipE('请输入有效的联系电话'),false;
		return true;
	},
	saveForm:function(callback){
		var _this = this,base = _this.base();
		base.showTipIngA('正在保存病历').post(_href + 'doctor/saveInfos',$('#myform').serialize(),function(d){			
			base.showTipS('保存成功');
			callback(true);
		},function(){
			callback();
			base.showTipE('保存失败');
		});
	},
	sendForm:function(sval,callback){
		var _this = this,base = _this.base();
		base.showTipIngA('正在发送').post(_href + 'doctor/postInfoToExpert',{oid:_oid,sval:sval},function(d){			
			base.showTipS('发送成功');
			callback(true);
		},function(){
			callback();
			base.showTipE('发送失败');
		});
	},
	saveReport:function(txt,callback){
		var _this = this,base = _this.base();
		base.showTipIngA('正在发送报告')
			.post(_href + 'doctor/insertConResult',{conResult:txt,oid:_oid},function(d){			
				base.showTipS('报告发送成功');
				callback(true);
			},function(){
				callback();
				base.showTipE('报告发送失败');
			});
	},
	runPrefixMethod:function(element, method){
		var usablePrefixMethod;
	    ["webkit", "moz", "ms", "o", ""].forEach(function(prefix) {
	        if (usablePrefixMethod) return;
	        if (prefix === "") {
	            method = method.slice(0,1).toLowerCase() + method.slice(1);
	        }
	        var typePrefixMethod = typeof element[prefix + method];
	        if (typePrefixMethod + "" !== "undefined") {
	            if (typePrefixMethod === "function") {
	                usablePrefixMethod = element[prefix + method]();
	            } else {
	                usablePrefixMethod = element[prefix + method];
	            }
	        }
	    });    
	    return usablePrefixMethod;
	},
	fullscreen:function(esc){
		var docElm = document.documentElement;
		if(esc != 'esc'){
			this.runPrefixMethod(docElm, "RequestFullScreen");
		}else{
			this.runPrefixMethod(document, "CancelFullScreen");
		}
	},
	doFullScreen:function(esc){
		var v = $('#main'),btn = v.find('.fullscreen i');
		var parentFullscreen = window.parent.escFullScreen || $.proxy(this.fullscreen,this);
		if(esc != 'esc'){
			v.hasClass('fullScreenWindow') ? 
				(v.removeClass('fullScreenWindow'),btn.html('&#xe608;'),parentFullscreen('esc',true)) : 
				(v.addClass('fullScreenWindow'),btn.html('&#xe607;'),parentFullscreen('full',true));
			$('body').hasClass('overflowhidden') ? 
				$('body').removeClass('overflowhidden'):
				$('body').addClass('overflowhidden');
		}else{
			v.removeClass('fullScreenWindow'),parentFullscreen('esc');
			$('body').removeClass('overflowhidden');
		}
	},
	reloaddwv:function(iframe){
		$('#showDWVModel .loadings').fadeIn();
		if (iframe.attachEvent){ 
			iframe.attachEvent("onload", function(){ 
    			$('#showDWVModel .loadings').fadeOut();
			}); 
		} else { 
			iframe.onload = function(){ 
    			$('#showDWVModel .loadings').fadeOut();
			}; 
		}
	},
	hasAsync:function(){
		//用于同步时，没有同步完不能保存
		return true;
	},
	valideTel: function(text) {
        var _emp = /^\s*|\s*$/g;
        text = text.replace(_emp, "");
        var _d = /^1[3578][01379]\d{8}$/g;
        var _l = /^1[34578][01245678]\d{8}$/g;
        var _y = /^(134[012345678]\d{7}|1[34578][012356789]\d{8})$/g;
        if (_d.test(text)) {
            return true;
        } else if (_l.test(text)) {
            return true;
        } else if (_y.test(text)) {
            return true;
        }
        return false;
    },
    valideCard:function(idCard){
        var regIdCard =/^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
        var HMCard = /^[HhMm]\d{8}((\(\d{2}\))|\d{2})$/,
        	TCard = /^\d{8}(\d{1,2}(\([A-Za-z]\))?)?$/;
        switch(idCard.length){
	        case 8:
	        case 9:
	        case 10:
	        case 11:
	        case 13:
	        	if (HMCard.test(idCard)){return true;}
	        	if (TCard.test(idCard)) {return true;}
	        	return false;
	        	break;
	        case 18:
	            if (regIdCard.test(idCard)) {
	                var idCardWi = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2); //将前17位加权因子保存在数组里
	                var idCardY = new Array(1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2); //这是除以11后，可能产生的11位余数、验证码，也保存成数组
	                var idCardWiSum = 0; //用来保存前17位各自乖以加权因子后的总和
	                for (var i = 0; i < 17; i++) {
	                    idCardWiSum += idCard.substring(i, i + 1) * idCardWi[i];
	                }     
	                var idCardMod = idCardWiSum % 11; //计算出校验码所在数组的位置
	                var idCardLast = idCard.substring(17); //得到最后一位身份证号码
	     
	                if (idCardMod == 2) {
	                    if (idCardLast == "X" || idCardLast == "x") {
	                        return true;
	                    } else {
	                        return false;
	                    }
	                } else {
	                    if (idCardLast == idCardY[idCardMod]) {
	                        return true;
	                    } else {
	                        return false;
	                    }
	                }
	            }
	        	break;
	        default:
            	return false;
	        	break;
        }
    },
    saveDiyFileName:function(input){
    	var dev = input.attr('data-v'),v = input.val(),
    		p = input.parent(),fid = input.closest('li').attr('data-id') || '';
    	var _this = this,base = _this.base();		
    	if(dev == v)
    		p.html(v);
    	else{
    		fid &&base.showTipIngA('正在保存更改')
			.post(_href + 'doctor/updatefname',{fid:fid,filename:v},function(d){			
				base.showTipS('文件名更改成功');
				p.html(v);
			},function(){
				base.showTipE('文件名更改失败');
			});
    	}
    },
    saveDICMName:function(input){
    	var dev = input.attr('data-v'),dtim = input.attr('data-d'),v = input.val(),
			p = input.parent(),fid = input.closest('.imglist').attr('data-id') || '';
		var _this = this,base = _this.base();		
		if(dev == v)
			p.html(dtim + '(<i>'+ (dev || '未知') +'</i>)');
		else{
			fid &&base.showTipIngA('正在保存更改')
			.post(_href + 'doctor/compleupcheckitem',{pid:fid,itemname:v,oid:_oid},function(d){			
				base.showTipS('文件名更改成功');
				p.html(dtim + '(<i>'+ (v || '未知') +'</i>)');
			},function(){
				base.showTipE('文件名更改失败');
			});
		}
	},
	scrollFun:function(){
		var _this = this;
		var st = $(window).scrollTop(), sth = st + $(window).height();
		/***图片滚动加载****/
		$('.imglist[data-val]').each(function(){
			var o = $(this), post = o.offset().top, posb = post + o.height();
	        if ((post > st && post < sth) || (posb > st && posb < sth)) {
	           o.attr('data-val') && (function(item){
	        	   var src = item.attr('data-val');
	        	   var iframe = '<img alt="影像图片" src="'+ src +'" />';
	        	   if(src.indexOf('_=none') != -1){
	        		   iframe += '<label class="nopic">影像图片不存在</label><div class="bg"></div>'
	        	   }else if(src.indexOf('_=stop') != -1){
	        		   iframe += '<label class="nopic">同步操作已终止</label><div class="bg"></div>'
	        	   }
	        	   item.removeAttr('data-val').find('.thumb').html(iframe);
	           })(o);
	        }
		});			
	},
	showUploadDCIM:function(){
		var _this = this,base = _this.base();		
		base.showDialog({
			id:'showUploadDCIM',
			text:$('#uploadDCIMTemp').html(),
			cls:'modal2-lg',
			nofooter:true
		});
		seajs.use('view/vedio/uploadDCIM',function(upload){
			upload.init({
				server: _href + 'doctor/updcmfile',
				threads:1,
        		formData: { oid: _orderid },
        		success:function(){
        			base.showTipS('DCIM文件上传成功').hideDialog('showUploadDCIM');
        		}
			});
		});
    	$('#showUploadDCIM').on('hide.bs.modal', function (e) {
    		$('body').removeClass('modal2-open');
    		window.setTimeout(function(){
    			$('#showUploadDCIM').remove();
    		},400);
    	});
	},
	goEasy:function(channelid){
		goEasy = new GoEasy({
           	appkey: '78028e7e-edcc-4524-b56b-45639785a53a'
        });
      	goEasy.subscribe({
            channel: channelid,
            onMessage: function(message){
            	var content = message.content.replace(/&quot;/g,"\"");
            	var jmessage = JSON.parse(content);  
            	var _type    =  jmessage.type;
            	var _from    =  jmessage.from,
            		_result  =  jmessage.result || '加载无数据'; 
       			//console.log('jmessage', JSON.stringify(jmessage, null, '\t'));
            	
            	if (_type =='reportNotify' && _from !=_utype ){
            		reportmsg(_result),reportmsgAnmiation(_result);           	 	
            	} 
            	else if (_type =='syncPacs' && _from ==_utype ){
            		setPacsProgram(jmessage);
            	}
            	else if (_type =='syncPacsOut' && _from ==_utype ){
            		!!_signSyncBtn ? setPacListWithType1(jmessage) : setPacListWithType0(jmessage);
            	}
            	else if (_type =='syncLis' && _from ==_utype ){
            		setLisProgram(jmessage);
            	}
            	else if (_type =='updcm'){
            		setCustomPacs(jmessage);
            	}
            }
        });
	},
	saveHOSBasic:function(){
		var _this = this,base = _this.base(),i = 0;	
		var logourlVal = $('#hosform [name="logourl"]').val(),
			introduction = hoseditor.getContent(),
			introductionTxt = hoseditor.getContentTxt(),
			c1 = $('#hosform .required:eq(0)'),
			c2 = $('#hosform .required:eq(1)');			
/*		!logourlVal ? (i++,c1.addClass('error')) : c1.removeClass('error');
		!introduction ? (i++,c2.addClass('error')) : c2.removeClass('error');
		if(!!i) return false;*/
		base.showTipIngA('正在保存医院基本信息').post(hosform.action,{logourl:logourlVal,introduction:introduction},function(){
			base.showTipS('医院基本信息保存成功');
		})
	},
	saveWXBasic:function(){
		var _this = this,base = _this.base();	
		var i = 0;
/*		$('#wxform .required').each(function(){
			var input = $(this).find(':input').val();
			!input ? (i++,$(this).addClass('error')) : $(this).removeClass('error');
		});
		if(!!i) return false;*/
		base.showTipIngA('正在保存微信公号配置').post(wxform.action,$('#wxform').serializeArray(),function(){
			base.showTipS('微信公号配置保存成功');
		})
	}
});

