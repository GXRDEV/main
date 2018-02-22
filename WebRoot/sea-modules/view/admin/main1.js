define(function(require, exports, module) {
	exports.chooseEXPS = function(){
		require('bindBodyEvent').chooseEXPS();
	};
});
var _diyUploadSelector1;
define('bindBodyEvent',function(require, exports, module){
	var Controller = require('Controller1');
    exports.chooseEXPS = function(){
		$('body').delegate('.chooseexps','click',function(){
			var oid = $(this).attr('data-id') || '';
			oid && Controller.chooseEXP(oid);
		})
		.delegate('.del','click',function(){
			var oid = $(this).attr('data-id') || '';
			oid && Controller.delOrder(oid);
		})
		.delegate('.end','click',function(){
			var oid = $(this).attr('data-id') || '';
			oid && Controller.endOrder(oid);
		})
    	.delegate('#chooseEXP tbody tr.explist','click',function(event){
    		var chbx = $(this).find('.tablechk')[0];
    		if($(this).hasClass('trtimelists')) return false;
    		event.target.type != 'radio' && (chbx.checked = !chbx.checked,$(chbx).change());
    	})
    	.delegate('#chooseEXP tbody .tablechk','change',function(){
    		$(this).closest('tbody').find('.trtimelists').remove();
    		this.checked && $(this).closest('tr').after('<tr class="trtimelists"><td colspan="5"><div class="clearfix doctimelist"></div></td></tr>');
    		this.checked && Controller.initDocTimeLists('#chooseEXP .doctimelist',this.value);
    	})
    	.delegate('.timelist dd.open','click',function(){
    		$('.timelist dd.hover').removeClass('hover');
    		$(this).addClass('hover');
    	})
    	.delegate('.viewer-canvas','click',function(event){
    		event.target.localName != 'img' && _diyUploadSelector1.viewer('hide');
    	});
		'undefined' != typeof _oid && Controller.initPacLisImg();
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
		base.post('/wzjh/gainSpecialTimes',{sid:sid,sdate:sdate},function(d){
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
	delOrder:function(oid){
		var base = this.base(),_this = this;
		if(_docask == 'true'){
			base.post('system/delzjzx',{oid:oid},function(d){
				window.location.href = _burl + 'system/exadvices';
			});	
		}else{
			base.post('system/delorder',{oid:oid},function(d){
				window.location.href = _burl + 'system/vedioordermanage';
			});			
		}
	},
	endOrder:function(oid){
		var base = this.base(),_this = this;
		if(_docask == 'true'){
			base.post('doctor/endadviceorder',{oid:oid},function(d){
				window.location.href = _burl + 'system/exadvices';
			});
		}else{
			
		}
	},
	chooseEXP:function(oid){
		var base = this.base(),_this = this;
		base.showDialog({
			id:'chooseEXP',
			title: '选择专家',
			cls:'modal2-lg',
			ok: function(){
				var radio = $('#chooseEXP tbody tr').find('.tablechk:checked'),
					oid = radio.attr('data-oid') || '',
					docid = radio.val() || '',
					stimeid = $('#chooseEXP tbody').find('dd.hover').attr('data-id');
            	docid && oid && stimeid ? base.showTipIngA('正在分配').post('/system/distexpert',{oid:oid,expertId:docid,stimeid:stimeid},function(d){
               		if(d.status == 'error'){
               			base.showTipE('已分配专家了');
               		}else{
               			window.setTimeout(function(){
               				window.history.go(-1)
               			},500);
               		}
               	},function(){
               		base.showTipE('网络出错');
               	}).hideDialog('chooseEXP') : base.showTipE('请选择专家出诊时间段');
			}
		}).post('/system/loadexperts',{oid:oid},function(d){
			var table = '<table class="table"><thead><tr><th>姓名</th><th>医院</th><th>科室</th><th>职称</th><th>操作</th></tr></thead><tbody>{0}</tbody></table>';
			var trs = '<tr><td colspan="5"><input type="text" placeholder="从已有结果中过滤" style="width:45%" onkeyup="expersfilter(this.value)" />';
			trs += '<input type="text" placeholder="从全专家库中搜索" style="width:45%;margin-left:5%" onkeyup="expersfilterDB(this.value,'+ oid +')" /></td></tr>';
			$.each(d.specials,function(i,o){
				trs += '<tr class="explist"><td>'+ o.specialName +'</td><td>'+ o.hosName +'</td><td>'+ o.depName +'</td><td>'+ o.duty +'/'+ o.profession +'</td><td><input type="radio" name="radiolist" class="tablechk" data-oid="'+ oid +'" value="'+ o.specialId +'"/></td></tr>';
			});
			$('#chooseEXP .modal2-body').html(table.replace('{0}',trs));
		},function(){
			$('#chooseEXP .modal2-body').html(base.noresult);
		});
		
    	$('#chooseEXP').on('hide.bs.modal', function (e) {
    		$('body').removeClass('modal2-open')
    	});
	},
	initPacLisImg: function(){
		var base = this.base(),_this = this;
		base.get('doctor/loadcaseinfo',{ oid: _oid, docask: _docask },function(data){
            _this.fillLisDiv(data.checkReportImages || []);
            _this.fillPacDiv(data.radiographFilmImags || []);
            _this.fillFileDiv(data.normals || []);
            seajs.use('view/viewer',function(view){
            	_diyUploadSelector1 = view.init('.pacsSignlist.IMG');
        	});
        });
	},
	fillLisDiv: function(list){
		var h = '';
		if(list.length < 1) return 0;
		h += '<div class="blocklist pacsSignlist clearfix IMG">';
		h += '<div class="pacsSign"><span class="sing">IMG</span></div>';
		for(var i = 0, l = list.length; i < l; i++){
			h += '<div class="imglist noafter">';
			h += '	<div class="thumb"><img alt="检查图片" src="'+ list[i]['fileUrl'] +'"></div>';
			h += '	<label class="thumbName">'+ list[i]['fileName'] +'</label>';
			h += '</div>';
		}
		h += '</div>';
		$('#listableImg').html(h);		
	},
	fillPacDiv: function(list){
		var h = '';
		if(list.length < 1) return 0;
		h += '<div class="blocklist pacsSignlist clearfix IMG">';
		h += '<div class="pacsSign"><span class="sing">IMG</span></div>';
		for(var i = 0, l = list.length; i < l; i++){
			h += '<div class="imglist noafter">';
			h += '	<div class="thumb"><img alt="影像图片" src="'+ list[i]['fileUrl'] +'"></div>';
			h += '	<label class="thumbName">'+ list[i]['fileName'] +'</label>';
			h += '</div>';
		}
		h += '</div>';
		$('#pacsImg').html(h);
	},
	fillFileDiv: function(list){
		var h = '';
		if(list.length < 1) {
			h = '<div class="noresult"><img src="'+ window.location.origin +'/img/mobile/kbzs/icon_noresult@2x.png" style="width:30px" alt=""/></div>'
		} else {
			h += '<div class="pacsSignlist clearfix IMG" style="margin:10px;">';
			for(var i = 0, l = list.length; i < l; i++){
				h += '<div class="imglist noafter">';
				h += '	<div class="thumb"><img alt="本地图片" src="'+ list[i]['fileUrl'] +'"></div>';
				h += '	<label class="thumbName">'+ list[i]['fileName'] +'</label>';
				h += '</div>';
			}
			h += '</div>';
		}
		$('#pics').html(h);
	}
});
function expersfilter(value){
	$('#chooseEXP tr.explist').each(function(){
		$(this).text().indexOf(value) != -1 ? $(this).show() : $(this).hide();
	});
}
function expersfilterDB(value,oid){
	if(window._modalfiltime) window.clearTimeout(window._modalfiltime)
	window._modalfiltime = window.setTimeout(getdbdata, 1000);
	function getdbdata(){
		$('#chooseEXP .modal2-body tbody .explist').remove();
		$('#chooseEXP .modal2-body tbody').append('<tr class="explist"><td colspan="5" style="padding:10px 0;">正在搜索，请稍等…</td></tr>');
		$.post('/system/loadexperts',{oid:oid,keywords:value},function(d){
			var trs = '',r = $('#chooseEXP .modal2-body tbody .explist');
			$.each(d.specials,function(i,o){
				trs += '<tr class="explist"><td>'+ o.specialName +'</td><td>'+ o.hosName +'</td><td>'+ o.depName +'</td><td>'+ o.duty +'/'+ o.profession +'</td><td><input type="radio" name="radiolist" class="tablechk" data-oid="'+ oid +'" value="'+ o.specialId +'"/></td></tr>';
			});
			r.replaceWith(trs);
		});
	}
}
