function intprotag(_protag){
	switch(_protag){
		case '1':	
			otherset(1),nextable(2);
			break;
		case '2':
			otherset(2),nextable(3);
			break;
		case '3':
			otherset(3),nextable(3);
			break;
		case '4':
			initiallizeVideo(true);
			//otherset(4);//y
			nextable(3);//y
			//nextable(4);//y
			break;
		case '5':
			otherset(5);						
			break;
		default:
			removesty(5);
			$('.sbtn').show();
			$('.topheader .btn-save,.topheader .btn-send').removeAttr('disabled');
			$('body').addClass('globelstate' + _utype);
			$('.diyUpload .actionAdd').css('display','inline-block');
			break;
	}
	initialfirstipxt(_protag==4 ? true : false);
	initialIconstate(_protag=='2' ? '3' : _protag);
}
function foreverSave(bol){
	if(bol){
		$('.sbtn').show();
		$('.state1 .btn-save').removeAttr('disabled');
		$('.diyUpload .actionAdd').css('display','inline-block');
		$('body').addClass('globelstate' + _utype);	
	}else{
		$('.sbtn').hide();
		$('.state1 .btn-save').attr('disabled',true);
		$('.diyUpload .actionAdd').css('display','none');
		$('body').removeClass('globelstate' + _utype);	
	}
}
function initialIconstate(val){
	$('.topheader .state3').find('.state3icon').html(function(){
		if(val == '3'){
			return '<span style="color:#E35850">&#xe615;</span>';
		}else if(val == '4'){
			return '<span style="color:#5FB41B">&#xe614;</span>';
		}else{
			return '';
		}
	});
}
function otherset(val){
	for(var i=1;i<=val;i++){
		$('.topheader .state'+i).addClass('passed').find('.statetext a.btn').attr('disabled',true);
	}
	$('.topheader .state' + val).nextAll('.passed').removeClass('passed').find('.statetext a.btn').attr('disabled',true);
	$('.topheader .passed:last').addClass('animate').siblings('.animate').removeClass('animate');
	$('.sbtn').hide();
	$('.diyUpload .actionAdd').css('display','none');
	$('body').removeClass('globelstate' + _utype);
	foreverSave(_protag != '5');
	//底部按钮控制
	if(val == 4){
		$('#endBtn,#secondBtn,#thirdBtn').show();
	}else{
		$('#endBtn,#secondBtn,#thirdBtn').hide();
	}
}
function nextable(val){
	$('.topheader .state'+val).find('.statetext a.btn').removeAttr('disabled');
}
function removesty(val){
	for(var i=1;i<=val;i++){
		$('.topheader .state'+i).removeClass('passed').find('.statetext a.btn').attr('disabled',true);
	}
	$('.topheader .first').addClass('animate').siblings('.animate').removeClass('animate');
	$('.topheader .state3 .dropdown-toggle:first').html('标记医生状态');
	$('.topheader .state4 .dropdown-toggle:first').html('标记就诊状态');
}
function getKey()  
{  
    if(event.keyCode==13){  
    	var txt=$("#sendtext").val();
    	if(txt==''){
			alert("发送内容不能为空");
			return ;
		}
		chartmsg(txt,'me');
    }
}
function chartmsg(txt,cls){
	var tmp = '<div class="hhlist clearfix '+ cls +'">';
	tmp += '<span class="cols0"><span class="thumb"><img src="'+ _href +'img/defdoc.jpg"></span></span>';
	tmp += '<span class="cols1"><span class="text">'+ txt +'</span></span></div>';
	$('#pictxtdialog').show().find(".dialog .bodyer").append(tmp);
	if('me'==cls){					
		$("#sendtext").val('');
		sendMessage({"type":"chart","txt":txt});
	}
	seajs.use('view/viewer',function(view){
		view.init('.dialog .bodyer');
	});
}
function reportmsg(txt,cls){
	var tmp = '<div class="reportxt">'+ txt +'</div>';
	$("#expreport").fadeIn().find('.bodyer').html(tmp);
	if('me'==cls){
		$("#reporttxt .fillbaogao").val('');
		$('#reporttxt').removeClass('center').fadeOut();
		$('#reportbtn').fadeIn();
		//sendMessage({"type":"reportsult","txt":txt});
	}
}
function reportmsgAnmiation(txt){
	seajs.use('view/base',function(bas){
		bas.showDialog({
			id:'ReportModel',
			title:'专家咨询意见',
			text:txt,
			nofooter:true
		});	
		$('#ReportModel').on('hide.bs.modal', function (e) {
			$('body').removeClass('modal2-open')
		});		
	});
}
function msgShareScreen(jmessage){
	seajs.use('view/base',function(bas){
		bas.showDialog({
			id:'screenShareAgain',
			cls:'modal2-400',
			title:'屏幕分享请求',
			text:'<p style="padding:20px 0 0;font-size: 16px;line-height: 1.5em;">医生没有看到您的屏幕分享，点击“确定”分享屏幕给医生</p>',
			ok:function(){				
				sharetodoc();
				bas.hideDialog('screenShareAgain');
			}
		});	
		$('#screenShareAgain').on('hide.bs.modal', function (e) {
			$('body').removeClass('modal2-open')
		});
	});	
}
function setLisProgram(jmsg){
	var total = jmsg.total,curr = jmsg.curr + 1,
		percent = parseInt(curr / total * 100,10);
		program = $('#listable .progressc');	   
	if(window._processtime) window.clearTimeout(window._processtime);
	window._processtime = window.setTimeout(function(){
		program.find('.barc').animate({width : percent + '%'},400);
		program.find('.bartxt').text(percent + '%');
	}, 400);
}
function setPacListWithType1(jmsg){
	var total = jmsg.outTotal;
	var signlist = [];
	$.each(total,function(key,val){
		var div = '<div class="blocklist pacsSignlist clearfix '+ key +'" data-val="0">';
		div += '<div class="pacsSign"><span class="sing">'+ key +'</span></div>';
		div += setPacList(val,jmsg.syncSeries);
		div += '</div>';
		signlist.push(div);
	});
	$('#pacs').html(signlist.join('') || _noresult);
}
function setPacListWithType0(jmsg){
	var total = jmsg.outTotal;
	$.each(total,function(key,val){
		var outdiv = $('#pacs').find('.pacsSignlist.' + key);
		outdiv.size() > 0 ? 
			outdiv.attr('data-val','0').append(setPacList(val,jmsg.syncSeries)) :
			$('#pacs').append(function(){
				var div = '<div class="blocklist pacsSignlist clearfix '+ key +'" data-val="0">';
				div += '<div class="pacsSign"><span class="sing">'+ key +'</span></div>';
				div += setPacList(val,jmsg.syncSeries);
				div += '</div>';
				return div;
			});
	});
}
function setPacList(total,sign){
	var pacs = '';
	for(var i = 0;i < total;i++){
    	pacs += '<div class="imglist pacjmsg a'+ sign +'">';
		pacs += '<div class="thumb">';
		//pacs += '	<img src="//placehold.it/1"/>';
		pacs += '</div>';
		pacs += '<label class="thumbName"><div class="progressc"><div class="barc" style="width:0%;"></div><div class="bartxt">0/0</div></div></label>';
		pacs += '</div>';
	}
	return pacs;
}
function setCustomPacs(jmsg){
	var state = jmsg.status;
	if(state == 'old') return ;
	var outdiv = $('#pacs').find('.pacsSignlist.' + jmsg.Modality);
	outdiv.size() > 0 ? 
		outdiv.attr('data-val','0').append(creatSinglePic(jmsg)) :
		$('#pacs').append(function(){
			var div = '<div class="blocklist pacsSignlist clearfix '+ jmsg.Modality +'" data-val="0">';
			div += '<div class="pacsSign"><span class="sing">'+ jmsg.Modality +'</span></div>';
			div += creatSinglePic(jmsg);
			div += '</div>';
			return div;
		});
	$(window).scroll();
}
function creatSinglePic(jmsg){
	var pacs = '';
	pacs += '<div class="imglist pacjmsg" data-id="'+ jmsg.pid +'" data-rid="'+ jmsg.dcmurl +'" data-val="'+ getDICMSrc(jmsg.dcmurl) +'">';
	pacs += '<div class="thumb"></div>';
	pacs += '<label class="thumbName">'+ jmsg.REPORT_DATE +'(<i></i>)</label>';
	pacs += '</div>';
	return pacs;
}
function setPacsProgram(jmsg){
	var outitem = $('#pacs .'+ jmsg.modality),idx = (+outitem.attr('data-val'));
	var item = $('.imglist.a'+ jmsg.syncSeries +':eq('+ idx +')',outitem),
		program = item.find('.progressc');	        		
	var total = jmsg.inTotal,curr = jmsg.inCurr + 1,
		percent = parseInt(curr / total * 100,10);
	
	console.log('正在同步'+ jmsg.modality +'.imglist.a'+ _pacsSign +':eq('+ idx +')['+ percent +'%]');
	
	program.find('.barc').animate({width : percent + '%'},400);
	program.find('.bartxt').text(curr + '/'+ total);
	
	jmsg.pid && (
			item.attr('data-rid',jmsg.httpurl).attr('data-id',jmsg.pid),
			item.find('imgdel').size() < 1 && item.append('<span class="imgdel iconfont">&#xe60b;</span>')
	);
	jmsg.httpurl && (
			outitem.attr('data-val',idx + 1),
			item.attr('data-val',getDICMSrc(jmsg.httpurl)),
			$(window).scroll()
		);
	jmsg.checkItem && (item.removeClass('pacjmsg').find('.thumbName').html(jmsg.reportdate + '(<i>'+ jmsg.checkItem +'</i>)'));
	if(jmsg.httpurl == 'stop'){
		$('#pacs .imglist.a'+ jmsg.syncSeries).each(function(){
			var dval = $(this).attr('data-val');
			if(!dval || dval.indexOf('_=stop') != -1){
				$(this).addClass('nouse').attr('data-val',_href + 'img/nopic@2x.png?_=stop');
			}
		});
		window.setTimeout(function(){
			$('#pacs .imglist.nouse').fadeOut();
		}, 4000);
	}
}
function getDICMSrc(str){
	var strA = str.split('|');
	return (str == 'none' || str == 'stop') ?
			(_href + 'img/nopic@2x.png?_=' + str ):
			(_href + 'dcmimage?study='+ strA[1] +'&series='+ strA[2] +'&object='+ strA[3]);
}
function saveState(sv){
	$.post(_href + 'doctor/postInfoToExpert',{oid:_oid,sval:sv},function(){	
		_protag = sv;
		initiallizeVideo(sv==4 ? true : false);					
		intprotag((sv==6 || sv==7) ? '' : sv.toString());
	});
}
$(function(){
	intprotag(_protag);
	$('#myform').delegate('a.deltr','click',function(){
		$(this).closest("tr").remove();
	});
	$(".dropdown-menu li").click(function(){
		var sv=$(this).attr("sval");
		saveState(sv);
	});
	$(".btnsent").click(function(){
		var txt=$("#sendtext").val();
		if(txt==''){
			alert("发送内容不能为空");
			return ;
		}
		chartmsg(txt,'me');
	});
	$('.close').click(function(){
		$('body').removeClass('modal2-open');
	});
});



