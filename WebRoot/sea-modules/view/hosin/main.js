define(function(require, exports, module) {
	exports.hosinform = function(){
		require('bindEvent').hosinform();
		seajs.use('view/webupload',function(upload){
			var Controller = require('Controller');
			$('#addauthorizeFile').Uploader({
        		server: _burl + 'doctor/uploadLocalFile',
        		formData: {},
        		thumb: {
	                width: 100,
	                height: 100
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
    exports.hosinform = function(){
    	$('body').delegate('.bindchange','change',function(){
    		var data = $(this).data(),sval = $(this).val(), type = data.type || '';
    		switch(type){
	    		case 'city':
	    			sval ? Controller.gainproorcitys('city',sval) : Controller.clearoptions(['city']);
	    			Controller.gainhosbyprovin('hosid',sval.substr(0,2));
	    			Controller.clearoptions(['depid']);
	    			$('input[name="distCode"]').val(sval);
	    			break;
	    		case 'hosid':
	    			$('input[name="distCode"]').val(sval);
	    			break;
	    		case 'depid':
	    			sval ? Controller.gaindepartsbyhos('depid',sval) : Controller.clearoptions(['depid']);
	    			break;
    		}
    	})
    	.delegate('form .input','focus',function(){
    		$(this).animate({'font-weight':'700','letter-spacing':'1px','font-size':'17px'},100)
    	})
    	.delegate('form .input','blur',function(){
    		$(this).animate({'font-weight':'400','letter-spacing':'0px','font-size':'16px'},100)
    	})
    	.delegate('.diyCancel','click',function(event){
    		var li = $(this).closest('li'),ul = li.closest('.parentFileBox'),
    			hide = ul.next();
    		li.remove();
			hide.val(Controller.reloadDiy(ul));   
    		return false;
    	})
    	.delegate('button[name="nextstep"]','click',function(){
    		var step = $(this).attr('data-step') || '0';
    		switch(step){
	    		case '0':
	    			window.location.href = 'index.html';
	    			return false;
	    			break;	    		
	    		case '1':
		    		break;
	    		case '2':
	    			if(!Controller.validateHosForm('section1')){
	    				return false;
	    			}
		    		break;  		
	    		case '3':
	    			if(!Controller.validateHosForm('section2')){
	    				return false;
	    			}
	    			Controller.submitHosForm(function(){
	    				Controller.tabshow(step);
	    			});
	    			return false;
		    		break;    		
    		}
    		Controller.tabshow(step);
    	});
    	Controller.initselect().initSelectOps();
    };
});

define('Controller',['view/base'],{
	base:function(){
		return seajs.require('view/base')	
	},
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
	tabshow:function(step){
		$('.section' + step).show().siblings().hide();
		$('.processtap .step' + step).addClass('passed').nextAll().removeClass('passed').end()
			.prevAll().addClass('passed');
	},
	initselect:function(){
		this.gainproorcitys('pro','');
		return this;
	},
	initSelectOps:function(){
		var base = this.base();
		$('select.js-ajax').each(function(){
			var pid = this.getAttribute('data-pid'),select = $(this);
			base.get('system/gainDictorys',{pid:pid},function(d){
				select.html(function(){
					var dics = d.dics || [],ops = [];
					$.each(dics,function(i,o){
						ops += '<option value="'+ o.id +'">'+ o.displayName +'</option>';
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
		var base = this.base(),codepro = code.substr(0,2);
		base.get('system/gainproorcitys',{stype:name == 'pro' ? 1 : 2,procost: name == 'city' ? codepro : ''},function(d){
			var selectval = $('select[name="'+ name +'"]').attr('data-val') || '',
				selectvalpro = selectval.substr(0,2);			
			selectval = name == 'pro' ? selectvalpro + '0000' : selectval;
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
		base.get('system/gainhosbyprovin',{procost: code},function(d){
			var selectval = $('select[name="'+ name +'"]').attr('data-val') || '';
			$('select[name="'+ name +'"]').html(function(){
				var ops = '<option value="">---请选择---</option>';
				$.each(d['hospitals'] || '',function(i,o){
					ops += '<option value="'+ o.id +'"'+ (selectval == o.id ? ' selected' : '') +'>'+ o.displayName +'</option>';
				});
				return ops;
			});
		},function(){
			$('select[name="'+ name +'"]').html('<option value="">---网络出错---</option>');
		});
	},
	gaindepartsbyhos:function(name,code){
		var base = this.base();
		base.get('system/gaindepartsbyhos',{hosid: code},function(d){
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
	validateHosForm:function(cls){
		var base = this.base(),i = 0;
		$('.'+ cls +' .control-label b').each(function(){
			var input = $(this).closest('.control-group').find('.controls .input');
			input.each(function(){
				!$(this).val() && (i++,console.log('未通过的表单：' + this.name));
			});
		});
		if(i) return base.showTipE('还有未填的项，请核实。'),false
		if('section1' == cls && !this.validateSpecial(cls)) return false;
		return true;
	},
	validateSpecial:function(cls){
		var base = this.base();
		var email = $('.' + cls + ' [name="contactorEmail"]').val(),
			tel = $('.' + cls + ' [name="contactorTelphone"]').val();
		if(!base.valideTel(tel)) return base.showTipE('请输入有效的联系人电话。'),false;
		if(!!email && !base.valideEmail(email)) return base.showTipE('请输入有效的联系邮箱。'),false;
		return true;
	},
	submitHosForm:function(callback){
		var base = this.base(),data = $('#myform').serializeArray();
		if($('#myform .submitform').hasClass('disabled')) return false;
		$('#myform .submitform').addClass('disabled');
		base.showTipIngA('正在保存').post('system/saveorupdatehos',data,function(){
			base.showTipS('保存成功'),callback();
		},function(){
			base.showTipE('保存失败');
			$('#myform .submitform').removeClass('disabled');
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
	}
});

