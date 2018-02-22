var _zjh = {
	config:{
		pagesize:16,
		local:window.location.origin + '/',
		login_lq:'http://101.200.230.208:8080/web/user/Login',
		logout_lq:'http://101.200.230.208:8080/web/user/Logout',
		regist_lq:'http://101.200.230.208:8080/user/Register',
		hoslist:'kangxin/gainHospitalsByAddress',
		deplist:'kangxin/gainStandDeps',
		doclist:'kangxin/gainspecialdatas',
		uploadfile:'doctor/uploadLocalFile',//uid
		existnoclosed:'kangxin/existnoclosed',//uid
		gainhiscases:'kangxin/gainhiscases',//uid
		subtworder:'kangxin/subtworder',
		subtelorder:'kangxin/subtelorder',
		gainAllianceArea: 'kangxin/gainAllianceArea',
		gainAllianceByArea: 'kangxin/gainAllianceByArea',
		gainAllianceDetailInfo: 'kangxin/gainAllianceDetailInfo',
		loadAllianceDoctors: 'kangxin/loadAllianceDoctors',
		listenpaystatus_tw:'kangxin/listenpaystatus_tw',//tradeno
		hoslist_lq:'http://101.200.230.208:8080/hospital/AllHospitals',
		deplist_lq:'http://101.200.230.208:8080/hospital/AllDepartment',
		doclist_lq:'http://101.200.230.208:8080/specialist/list/query',
		uploadfile_lq:'http://101.200.230.208:8080/common/upload',
		savecaseinfo:'http://101.200.230.208:8080/case/save',
		beginTwen:'http://101.200.230.208:8080/Wenzhen/tw/start'
	}
};
define(function(require, exports, module) {
	exports.init = function(){
		require('bindEvent').bindAction().init();
	};
	exports.home = function(){
		require('bindEvent').bindAction().home(); 	
    };
    exports.remote = function(){
    	require('bindEvent').bindAction().remote();   	
    };
    exports.remote_docdetail = function(){
    	require('bindEvent').bindAction().remote_docdetail();   	
    };
    exports.online = function(){
    	require('bindEvent').bindAction().queryAndPage().online(); 	
    };
    exports.online_docdetail = function(s){
    	require('bindEvent').bindAction().online_docdetail(s); 
    };
    exports.online_wxpay = function(){
    	require('bindEvent').bindAction().online_wxpay()
    };
    exports.team = function(){
    	require('bindEvent').bindAction().queryAndPage().team();   	
    };
    exports.team_docdetail = function(){
    	require('bindEvent').bindAction().team_docdetail();   	
    };
    exports.union = function(){
    	require('bindEvent').bindAction().queryAndPage().union();   	
    };
    exports.docs = function(){
    	require('bindEvent').bindAction().docs();    	
    };
    exports.logonREG = function(){
    	require('bindEvent').bindAction().logonREG();    	
    };
    exports.confirmPay = function(){
    	require('bindEvent').bindAction().confirmPay()
    };
    exports.confirm_wxpay = function(){
    	require('bindEvent').bindAction().confirm_wxpay()
    };
    exports.success_wxpay = function(){
    	require('bindEvent').bindAction().success_wxpay()
    };
    exports.home_statichtm = function(){
    	require('bindEvent').bindAction().home_statichtm()
    };
    exports.user = function(){
    	require('bindEvent').bindAction().user();    	
    };
    exports.scrollpic = function(){
    	require('bindEvent').bindAction().scrollpic();    	
    };
    exports.appdownload = function(){
    	require('bindEvent').bindAction({ menu: false, w1366: true }).appdownload();    	
    };
	exports.explain = function(){
    	require('bindEvent').bindAction().explain()
    };
});

define('bindEvent',function(require, exports, module){
	var Controller = require('view/website/controller');
    exports.init = function(){
    	/*$('body').delegate('.menulist','click',function(){
    		var data = $(this).data();
    		data.action && $(this).addClass('selected').siblings().removeClass('selected');
    	});*/
    	$('div.menuicon').hide();
    	window.onhashchange=function(){
    		var hashStr = location.hash.replace("#","") || 'home';
    		Controller.actionTo(hashStr);    		
    	};
    	window.onhashchange();
    	Controller.resreshUserCenter();
    };
    exports.bindAction = function(options){
    	if (options && options.menu == false) {
    		$('div.menuicon').hide()
    	} else {
    		$('div.menuicon').show()
    	}
    	if (options && options.w1366 == true) {
    		$('body > .mainfooter ,body > .toptip').addClass('ww1366')
    	} else {
    		$('body > .mainfooter ,body > .toptip').removeClass('ww1366')
    	}
    	$('body').undelegate().delegate('a[data-action]','click',function(){
    		var data = $(this).data();
    		if(data.action != 'logout'){
    			location.hash = data.action || 'home';
    		}else{
    			Controller.resreshUserCenter(data.action);
    		}
    	})
    	.delegate('[data-js="addfavorite"]','click',Controller.addFavorite)
    	.delegate('.menuicon .search','click',function(){
    		var $key = $('.menuicon [name="keywords"]');
    		document.location.hash = 'team/?keyword=' + $.trim($key.val());
    		$key.val('');
    	})
    	.delegate('.menuicon [name="keywords"]','keydown',function(e){
    		var e = e||event,currKey = e.keyCode||e.which||e.charCode,$key = $(this);
    	    currKey == 13 && (document.location.hash = 'team/?keyword=' + $.trim($key.val()),$key.val(''));
    	});
    	return this;
    };
    exports.logonREG = function(){
    	$('body')
    	.delegate('.js-formlogin','click',$.proxy(Controller.valdateLogin,Controller))
    	.delegate('.js-formregist','click',$.proxy(Controller.valdateResigst,Controller))
    	.delegate('.js-getcode','click',$.proxy(Controller.getRegCode,Controller))
    	.delegate('.js-formlostme','click',$.proxy(Controller.valdateLostpsd,Controller))
    	.delegate('.form-input input','focus',function(){
    		$(this).animate({'font-weight':'700','letter-spacing':'1px','font-size':'18px'},100)
    	}).delegate('.form-input input','blur',function(){
    		$(this).animate({'font-weight':'400','letter-spacing':'0px','font-size':'16px'},100)
    	}).delegate('.forms','keydown',function(){
    		var e = e||event;
    	    var currKey = e.keyCode||e.which||e.charCode;
    	    if(currKey == 13)
    	    {
    	        $('.form-actions > button:eq(0)').trigger('click');
    	    }
    	}).delegate('.registDiv [name="tel"]','blur',function(){
    		var _this = this;
    		window.setTimeout(function(){
        		Controller.valdateTelRepeat($(_this));
    		},300);
    	});
    };
    exports.home = function(){
    	$('body')
    	.delegate('.bindchange','change',function(){
    		var datafor = $(this).attr('data-for');
    		Controller.getDepByhos(this.value,function(ops){
    			$('select[name="'+ datafor +'"]').html(ops)
    				.next('.select2').find('.select2-selection__rendered').text('---请选择---').attr('title','---请选择---');
    			
    		});
    	})
    	.delegate('.yuyuebtn','click',function(){
    		var hosid = $('select[name="hosptial"]').val(),
    			depid = $('select[name="department"]').val(),
    			discode = $('select[name="hosptial"]').find(':selected').attr('data-dist');
    		location.hash = 'remote/?remote=|'+ discode +'|'+ hosid +'|'+ depid + '-';
    	})
    	.delegate('.hasdoclists .deplist li','click',function(){
    		var data = $(this).data();
    		data.id && Controller.homeOnlineDoclist(data.id);
    		$(this).addClass('selected').siblings().removeClass('selected');
    	}); 
    	Controller.getHosOps(function(ops){
			$('select[name="hosptial"]').html(ops);
		});
    	Controller.getExportlist(function(ops){
			$('.exportlists').html(ops);
		});
    	Controller.getVideos(function(ops){
			$('.dochasname .bodyer').html(ops);
		});
    	Controller.homeOnlineDeparts();
    	require('view/select').init('.mainyuyue select');
    	require('view/swiperPC').init('.swiper-container',{
    		autoplay:10000,
    		loop : true
    	});
    	
    };
    exports.remote = function(){
    	$('body')
    	.delegate('.querywhere .opencitys a[data-id]','click',function(){
    		var data = $(this).data();
    		$(this).addClass('selected').siblings().removeClass('selected')
    		data.id && Controller.fillRemoteHos(data.id);
    	})
    	.delegate('.querywhere .openhos a[data-id]','click',function(){
    		var data = $(this).data();
    		$(this).addClass('selected').siblings().removeClass('selected')
    		data.id && Controller.getDepbyHos(data.id);
    	})
    	.delegate('.querywhere .opendeps a[data-id]','click',function(){
    		var data = $(this).data();
    		$(this).addClass('selected').siblings().removeClass('selected')
    		Controller.getSpecialsPage(data.hosid,data.id);
    	})
    	.delegate('.querywhere .query_more','click',function(){
    		var main = $(this).prev('.query_ops');
    		main.hasClass('noheight') ? 
    				(main.removeClass(function(){
    					window.setTimeout(function(){main.addClass('height')},10);
    					return 'noheight';
    				}),$(this).html('更多<i class="iconfont">&#xe60b;</i>')) :
    				(main.addClass('noheight').removeClass('height'),$(this).html('收起<i class="iconfont">&#xe60a;</i>'))
    	})
    	.delegate('.pagenationDIV_pages .page','click',function(){
    		var data = $(this).data(),outer = $(this).closest('.pagenationDIV').data();
    		Controller.getSpecialsPage(outer.hosid,outer.depid,data.id);
    	})
    	.delegate('.hasdoclists .doclist','click',function(){
    		var data = $(this).data(),
    			hos = $('.remoteDiv .openhos .selected'),
    			dep = $('.remoteDiv .opendeps .selected');
    		Controller.cook('_remoteDiv_openhos',hos.text() + ':' + hos.attr('data-id'),2);
    		Controller.cook('_remoteDiv_opendeps',dep.text() + ':' + dep.attr('data-id'),2);
    		location.hash = data.action || 'remote';
    	}); 
    	Controller.initQueryWhere();
    };
    exports.remote_docdetail = function(){
    	$('body')
    	.delegate('.timelist dd.open','click',function(){
    		var hosid = Controller.cook('_remoteDiv_openhos').split(':')[1],
				depid = Controller.cook('_remoteDiv_opendeps').split(':')[1],
				uid = Controller.getuser(),
				timeid = $(this).attr('data-id');
    		if(uid){    		
        		$('#confirmpay [name="localHosId"]').val(hosid);
        		$('#confirmpay [name="localDepartId"]').val(depid);
        		$('#confirmpay [name="stimeid"]').val(timeid);
        		$('#confirmpay [name="uid"]').val(uid.split('_')[1]);
        		$('#confirmpay [name="pmoney"]').val($(this).attr('data-c'));
        		$('#confirmpay').submit();
    		}
    	})
    	.delegate('.timelist dd.open','mouseover',function(){   
    		var _this = this;
    		var $div = $('.timedetailtip'), data0 = $(_this).closest('dl').data(),
				data1 = $('.docdetail .docinfos').data(),w = $(_this).width(),st = $(document).scrollTop(),
				data = $(_this).data(),offset = $(_this).offset(),position = $(_this).position();

    		//$('.doctimes .choosedetail .costmoney').html(data.c + '元');
    		if(window._timedetailtip) clearTimeout(window._timedetailtip),window._timedetailtip = null;
    		window._timedetailtip = setTimeout(function(){
    			$div.html(function(){
        			return '<h2 style="color:#CA5840">点击即可预约</h2>' + 
        				   '<p class="whitespace">医院：'+ data1.hos +'</p>' +
        				   '<p class="whitespace">科室：'+ data1.dep +'</p>' +
        				   '<p class="whitespace">医生：'+ data1.name +'</p>' +
        				   '<p class="whitespace">时间：<span>'+ data0.ymd +'（'+ data0.week +'）<br/>'+ data.a +' '+ data.t +'</span></p>'/* +
        				   '<p class="whitespace">费用：<span style="color:#CA5840">￥'+ data.c +'</span></p>'*/;
        		}).css({
        			'position':'fixed',
        			'display':'block',
        			'top':$div.css('top') + 'px',
        			'left':$div.css('left') + 'px'
        		}).animate({
        			'top':offset.top - st + 1 + 'px',
        			'left':offset.left + 17 + w + 'px'
        		},200)
    		},300);
    	})
    	.delegate('.timelist dd.open','mouseout',function(){
    		if(window._timedetailtip) clearTimeout(window._timedetailtip),window._timedetailtip = null;
    		$('.timedetailtip').css({'display':'none'});
    	});
    	$(window).bind("scroll",function(){
    		$('.timedetailtip').css({'display':'none'});
        });
    	Controller.initDocTimeLists('.docdetailDiv .swiper-wrapper');
    	//Controller.initSwiper('.docdetailDiv .swiper-container');
    	Controller.imgDo('.docdetailDiv .docdetail');
    };
    exports.confirmPay = function(){
    	var uid = Controller.getuser();
    	if(!uid) return false;
    	$('#myform [name="uid"]').val(uid.split('_')[1]);
    	$('body').delegate('.addpersoninfo','click',function(){
    		$('.confirmHtml #adduserinfo').show();
    	})
    	.delegate('.confirmpay .personlist','click',function(){
    		$('.confirmpay .personlist .selected').removeClass('selected');
    		$('#myform [name="contactorid"]').val($('.innerlist',this).addClass('selected').attr('data-id'));
    	})
    	.delegate('#adduserinfo .canceluserinfo','click',function(){
    		$('.confirmHtml #adduserinfo').hide();
    	})
    	.delegate('#adduserinfo .submituserinfo','click',function(){
    		if($(this).hasClass('disabled')) return false;
    		Controller.validatePerson(function(){
    			$('.confirmHtml #adduserinfo').hide();
    		});
    	})
    	.delegate('.submittopay','click',function(){
    		if($(this).hasClass('disabled')) return false;
    		Controller.validateSumbitWX(function(){
    			//$('.confirmHtml #adduserinfo').hide();
    		});
    	});    	
    };
    exports.confirm_wxpay = function(){
    	window.setTimeout(function(){
    		Controller.listenWxPay();
    	},10000);
    };
    exports.success_wxpay = function(){
    	$('.menuicon .imgpross').attr('src',$('.menuicon .imgpross').attr('src').replace('bb1.png','bb2.png'));
    };
    exports.queryAndPage = function(){
    	$('body')
    	.delegate('.querywhere .queryselects .selecteds','click',function(){
    		var data = $(this).data(),_this = this;
    		$.each(data,function(i,o){
        		$(_this).replaceWith('<a data-'+ i +'=""></a>');
        		Controller.getQueryResult(i,'');
    		});
    	})
    	.delegate('.querywhere dd .query_ops a[data-id]','click',function(){
    		var data = $(this).data();
    		$(this).addClass('selected').siblings().removeClass('selected')
    		Controller.getQueryResult([{key:data.type,val:data.id},{key:'no',val:'1'}]);
    	})
    	.delegate('.querywhere .query_more','click',function(){
    		var main = $(this).prev('.query_ops');
    		main.hasClass('noheight') ? 
    				(main.removeClass(function(){
    					window.setTimeout(function(){main.addClass('height')},10);
    					return 'noheight';
    				}),$(this).html('更多<i class="iconfont">&#xe60b;</i>')) :
    				(main.addClass('noheight').removeClass('height'),$(this).html('收起<i class="iconfont">&#xe60a;</i>'))
    	})
    	.delegate('.pagenationDIV_pages .page','click',function(){
    		var data = $(this).data(),outer = $(this).closest('.pagenationDIV').data();
    		Controller.getQueryResult('no',data.id);
    	});
    	return this;
    };
    exports.online = function(){
    	/*$('body')
    	.delegate('.hasdoclists .doclist .href','click',function(){
    		var token = Controller.getuser('token');
    		var data = $(this).data();
    		if(!token) return false;  		
    	});*/
    	Controller.initOnlineQueryWhere();   	
    };
    exports.online_docdetail = function(sign){
		var myacc = Controller.checkToken('.postaskquestion'),uid;
    	Controller.imgDo('.docdetailDiv .docdetail');
		if(!myacc) return;
		uid = myacc.split('_')[1];
		$('form [name="uid"]').val(uid);
		Controller.doNoclosed(sign,function(){
	    	Controller.loadPersonlist();
	    	seajs.use('view/webupload',function(upload){
				$('#addPics').Uploader({
	        		server: _zjh.config.uploadfile,
	        		formData: {uid:uid},
	        		duplicate:true,
			        backdata:'frazior',
			        icondel:'<i class="iconfont" title="删除">&#xe60e;</i>',
	        		thumb: {
		                width: 100,height: 100
		            },
	        		success:function(liobj,obj){
	        			liobj.attr('data-id',obj.upid)
	        			$('#caseImages').val(Controller.reloadDiy($('#picslist')));   
	        		}
	        	});
			});
	    	$('body')
	    	.delegate('form .select input','focus',function(){
	    		 $(this).closest('.select').addClass('select-focus').find('.options').css('width',$(this).width() + 10 + 'px');
	    	})
	    	.delegate('form .select input','blur',function(){
	    		 var select = $(this);
	    		 window._selecttimer && clearTimeout(window._selecttimer);
	    		 window._selecttimer = setTimeout(function(){
	    			 select.closest('.select').removeClass('select-focus');
	    		 },200);
	    		 window.setTimeout(function(){
	    			 select.val() != select.attr('data-oval') ? 
	    				$('form [name="caseid"]').val('') : 
	    				$('form [name="caseid"]').val($('form [name="caseid"]').attr('data-oval'));
	     		},300);
	    	})
	    	.delegate('form [name="idcard"]','blur',function(){
	    		this.value && Controller.doIDNumbers(this.value);
		   	})
	    	.delegate('form .select .options li','click',function(){
	    		var o = $(this).data();
	    		$(this).addClass('selected').siblings().removeClass('selected');
	    		Controller.fillFormById(o.id);
	    	})
	    	.delegate('#picslist .fileBoxUl .diyCancel','click',function(){
	    		$(this).closest('.browser').remove();
	    		$('#caseImages').val(Controller.reloadDiy($('#picslist'))); 
	    	})
	    	.delegate('#uploadask','click',function(){
	    		Controller.validateAskTelQue('subtworder');
	    	})
	    	.delegate('#uploadtel','click',function(){
	    		Controller.validateAskTelQue('subtelorder');
	    	});
		});
    };
    exports.online_wxpay = function(){
    	window.setTimeout(function(){
    		Controller.listenOnlineWxPay();
    	},10000);
    };
    exports.team = function(){
    	Controller.initTeamQueryWhere(); 
    };
    exports.union = function(){
    	Controller.initUnionQueryWhere(); 
    };
    exports.team_docdetail = function(){
    	Controller.imgDo('.docdetailDiv .docdetail');
    };
    exports.docs = function(){
    	$('body')
    	.delegate('.pagenationDIV_pages .page','click',function(){
    		var data = $(this).data();
    		Controller.getDocVideoPage(data.id);
    	});
    	Controller.getDocVideoPage(1);  	
    };
    exports.home_statichtm = function(){
    	$('body')
    	.delegate('.uploadfeedback','click',function(){
    		var textarea = $('textarea.feedbackContent').val();
    		Controller.saveFeedback(textarea,function(){
    			$('textarea.feedbackContent').val('')
    		});
    	});
    };
    exports.user = function(){
    	var uid = Controller.getuser();
    	var hashStr = location.hash.replace("#","").split('/');
    	var pid = hashStr[1] || '';
    	if(!uid) return false;
    	uid = uid.split('_')[1];    	
    	switch(pid){
    		case 'user_myaskimg':
    			Controller.fillTuWenTableList(uid,pid,'kangxin/gaingraphics',1);
    			break;
    		case 'user_myasktel':
    			Controller.fillTuWenTableList(uid,pid,'kangxin/gaintelorders',1);
    			break;
    		case 'user_myaccout':
    			break;
    		case 'user_myaskimg_detail':
    			hashStr[2] && Controller.fillTuWenDetail(hashStr[2],hashStr[3]);
    			break;
    		default:
    			break;
    	}
    	$('body')
    	.delegate('a.moretable','click',function(){
    		var no = $(this).attr('data-no') || 1;
    		switch(pid){
	    		case 'user_myaskimg':
	    			Controller.fillTuWenTableList(uid,pid,'kangxin/gaingraphics',no,'append');
	    			break;
	    		case 'user_myasktel':
	    			Controller.fillTuWenTableList(uid,pid,'kangxin/gaintelorders',no,'append');
	    			break;
	    		case 'user_myaccout':
	    			break;
	    		default:
	    			break;
	    	}
    	})
    	.delegate('.myaskimg .haspay[data-id]','click',function(event){
    		var id = $(this).attr('data-id');
    		event.target.type != 'button' && id && (location.hash = 'user/user_myaskimg_detail/' + id);
    	})
    	.delegate('.notpay[data-id]','click',function(e){
    		var id = $(this).attr('data-id'),otype = $(this).closest('.myaskimg').size() ? 1 : 2;
    		event.target.type != 'button' && (
    				Controller.cook('_key_history_',location.href.split('#')[0] + '#user/user_myaskimg_detail/' + id,1),
    				Controller.payNotCloedOrder(id.split('/')[0],otype)
    			);
    	})
    	.delegate('.askagainbtn','click',function(){
    		$('.askaigan .askform').is(':hidden') ? $('.askaigan .askform').slideDown() : $('.askaigan .askform').slideUp();
    	})
    	.delegate('.saveaskagainbtn','click',function(){
    		var text = $('.askform [name="msgContent"]').val();
    		Controller.saveAskAgain(text,hashStr[2],uid,function(){
    			hashStr[2] && Controller.fillTuWenDetail(hashStr[2],hashStr[3]);
    		});
    	})
    	.delegate('.closeaskbtn','click',function(){
    		Controller.endAskOrder(hashStr[2],function(){
    			location.hash = 'user/user_myaskimg';
    		});
    	})
    	.delegate('.colseTelOrder','click',function(){
    		var oid = $(this).closest('tr').attr('data-id') || '';
    		oid = oid.split('/')[0];
    		Controller.endTelOrder(oid,uid,function(){
    			Controller.fillTuWenTableList(uid,pid,'kangxin/gaintelorders',1);
    		});
    	});	
    };
    exports.scrollpic = function(){
    	$('#videoKanDian').size() && Controller.initVideoKanDian();
    	$('#tuijian').size() && Controller.getExportlist(function(ops){
			$('#tuijian').html(ops);
		},3,'<span class="inner">立即预约</span>');
    	$('body').delegate('.title2 .btncoexp','click',function(){
    		var txt = $(this).text().indexOf('收缩') != -1;
    		if(txt){
    			$(this).html('展开<i class="iconfont">&#xe619;</i>'),
    			$(this).closest('.title2').nextAll('.maincoexp').slideUp();
    		}else{
    			$(this).html('收缩<i class="iconfont">&#xe61a;</i>'),
    			$(this).closest('.title2').nextAll('.maincoexp').slideDown();
    		}
    	});
    };
    exports.appdownload = function() {};
	exports.explain = function(){
		
    };
	
});

