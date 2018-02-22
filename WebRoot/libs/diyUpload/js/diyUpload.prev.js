; (function ($) {
    $(document).ready(function(){
    	$('.form-horizontal').delegate('.diyUpload .toLeft','click',function () {
    		var o = readyOptions(this);
    		prevTo(this,o);
        });
        $('.form-horizontal').delegate('.diyUpload .toRight','click',function () {
            var o = readyOptions(this);
    		nextTo(this,o);
        });
        $('.form-horizontal').delegate('.diyUpload .viewThumb img','dblclick',function(){
            var img = $(this),id = img.attr('data-id')||$(this).closest('li').attr('data-iid')||'',md = $('#myModal-detail-iframe'),imgsrc = img.attr('data-src') || img.attr('src'),
            	title = img.parent().siblings('.diyFileName').text(),model = '',src='editimg.jsp?id='+ id; //+'&src=' + imgsrc;
            model += '<div class="modal" id="myModal-detail-iframe" tabindex="-1" role="dialog" style="width:90%;height:90%;margin:0;left:5%;top:5%;">';
            model += '	<div class="modal-dialog" style="height:100%;">';
            model += '		<div class="modal-content" style="height:100%;overflow:hidden;">';
            model += '			<div class="modal-header">';
            model += '  			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>';
            model += '				<h3 class="modal-title" id="H4">'+ title +'</h3>';
            model += '			</div>';
            model += '			<div class="modal-body" style="max-height:100%;"><iframe id="cIframe" src="'+ src +'" style="height:100%;width:100%;" frameborder="no" border="0" marginwidth="0" marginheight="0"  name="cIframe"></iframe></div>';
            //model += '			<div class="modal-body" style="max-height:84%;"><img id="cIframe" src="'+ imgsrc +'" name="cIframe" /></div>';
            model += '		</div>';
            model += '	</div>';
            model += '</div>';
            (md.size() < 1) ? (md = $(model),$('body').append(md)):(md.find('#H4').text(title),md.find('#cIframe').attr('src',src || imgsrc));             
            md.modal('show');
            img.attr('data-id',id);
            img[0] && !img[0]._hidemodal && (img[0]._hidemodal = function(){            	
            	$('#myModal-detail-iframe').modal('hide');
            });
        });
        $('#tabs').delegate('li','click',function(){
        	if(window._timers) window.clearTimeout(window._timers);
        	window._timers = window.setTimeout(scrollFun,400);
        });
    });    
    $(window).resize(function () {
    	if(window._timers) window.clearTimeout(window._timers);
    	window._timers = window.setTimeout(SetMoveState1,1000);
    });
    $(window).bind("scroll",function(){    	
    	if(window._timers) window.clearTimeout(window._timers);
    	window._timers = window.setTimeout(scrollFun,400);
    });
    
    function getWheelValue(e) {
        e = e || event;
        return (e.wheelDelta ? e.wheelDelta / 120 : -(e.detail % 3 == 0 ? e.detail / 3 : e.detail));
    }
    function stopEvent(e) {
        e = e || event;
        if (e.preventDefault) e.preventDefault();
        e.returnValue = false;
    }
    function addEvent(obj, type, fn) {
        var isFirefox = typeof document.body.style.MozUserSelect != 'undefined';
        if (obj.addEventListener)
            obj.addEventListener(isFirefox ? 'DOMMouseScroll' : type, fn, false);
        else
            obj.attachEvent('on' + type, fn);
        return fn;
    }
    function delEvent(obj, type, fn) {
        var isFirefox = typeof document.body.style.MozUserSelect != 'undefined';
        if (obj.removeEventListener)
            obj.removeEventListener(isFirefox ? 'DOMMouseScroll' : type, fn, false);
        else
            obj.detachEvent('on' + type, fn);
    } 
})(jQuery);

function nextTo(btn,o){
	var ul = $(btn).siblings('.fileBoxUl'),lis = ul.find('li'),li0 = ul.find('li[data-top="t0"]:last'),idx = parseInt(li0.attr('data-index'),10);  
	if(o.c === o.tp){ $(btn).hide();return false;}
	lis.removeAttr('data-top').removeClass('animated flipInY').hide().filter(function(){
		dataIdx = parseInt($(this).attr('data-index'),10);
		return dataIdx > idx && dataIdx <= (idx + parseInt(o.p,10)) 
	}).attr('data-top','t0').each(function(){
		var img = $(this).find('.viewThumb img')
		img.attr('data-srcing') && img.attr('src',img.attr('data-srcing')).removeAttr('data-srcing');		
	}).addClass('animated flipInY').show();
	ul.attr('data-cur',o.c + 1)
	if((o.c + 1) == o.tp){ $(btn).hide();}
	if((o.c + 1) > 1){ $(btn).siblings('.scroll').show();}
}
function prevTo(btn,o){
	var ul = $(btn).siblings('.fileBoxUl'),lis = ul.find('li'),li0 = ul.find('li[data-top="t0"]:first'),idx = parseInt(li0.attr('data-index'),10);  
	if(o.c === 1){ $(btn).hide();return false;}
	lis.removeAttr('data-top').removeClass('animated flipInY').hide().filter(function(){
		dataIdx = parseInt($(this).attr('data-index'),10);
		return dataIdx >= (idx - parseInt(o.p,10)) && dataIdx < idx
	}).attr('data-top','t0').each(function(){
		var img = $(this).find('.viewThumb img')
		img.attr('data-srcing') && img.attr('src',img.attr('data-srcing')).removeAttr('data-srcing');			
	}).addClass('animated flipInY').show();
	ul.attr('data-cur',o.c - 1)
	if((o.c - 1) == 1){ $(btn).hide();}
	if((o.c - 1) < o.tp){ $(btn).siblings('.scroll').show();}
}

function readyOptions(btn){
	var ul = $(btn).siblings('.fileBoxUl'),lis = ul.find('li'),li0 = ul.find('li:eq(0)');
	if(ul.attr('data-total')) return {
		t:parseInt(ul.attr('data-total'),10),
    	c:parseInt(ul.attr('data-cur')||1,10),
    	tp:Math.ceil(ul.attr('data-total')/ul.attr('data-pages')),
    	p:parseInt(ul.attr('data-pages'),10)
	};    		
	$.each(lis, function (i, obj) {
        var position = $(obj).position(), top = position.top;
        top == 0 ? $(obj).attr('data-top','t0').show() : $(obj).removeAttr('data-top').hide();
        $(obj).attr('data-index',i);
    });    
	var rt = lis.size(),rp = $('li[data-top="t0"]',ul).size();
    ul.attr('data-total',rt).attr('data-cur',1).attr('data-pages',rp);
    return {
    	t:rt,
    	c:1,
    	tp:Math.ceil(rt/rp),
    	p:rp
    }
}
function SetMoveState1(_this,isReset) {
    var diyU = _this ? $(_this).closest('.parentFileBox') : $('.parentFileBox');
    diyU.each(function(){
    	var lis = $("li",this),ul = $('.fileBoxUl',this),t=0,p=0;
    	if(!$(this).is(':visible')) return false;
    	!isReset ? lis.show() : $("li[data-top]:last",this).nextAll().show();
        $.each(lis, function (i, obj) {
            var position = $(obj).position(), top = position.top;
            top == 0 ? ($(obj).attr('data-top','t0').show()) : $(obj).removeAttr('data-top').hide();
            $(obj).attr('data-index',i);
        });
        t = lis.size(),p=$('li[data-top="t0"]',this).size();
        ul.attr('data-total',t).attr('data-pages',p).attr('data-cur',1);
        $('.scroll',this).hide();
        t > p ? $('.toRight',this).show() : $('.toRight',this).hide();        
        scrollFun('parentFileBox',this);
    });
}

function scrollFun(s,_this){
	var st = $(window).scrollTop(), sth = st + $(window).height(),_obj = (s == 'parentFileBox' ? _this : document);
	$('.fileBoxUl:visible',_obj).each(function(){
		var o = $(this), post = o.offset().top, posb = post + o.height();		
        if ((post > st && post < sth) || (posb > st && posb < sth)) {
        	!$(this).attr('data-total') && readyOptions($(this).siblings('.toRight')[0]);
        }
	});
	$('li[data-top] img[data-srcing]',_obj).each(function(){
		var o = $(this), post = o.offset().top, posb = post + o.height();
        if ((post > st && post < sth) || (posb > st && posb < sth)) {
           o.attr('data-srcing') && o.attr('src',o.attr('data-srcing')).removeAttr('data-srcing');
        }
	});
}
var _ajaxImgUrl = window.location.origin + '/';
