$(document).ready(function(){
	// === Sidebar navigation === //

	$('.menu_a').click(function(e){
		var link = $(this).attr('href');
		$("#iframe-main").attr("src", link);
		$(".menu_a").parent('li').removeClass('active');
		$(this).parent('li').addClass('active');
		$('#breadcrumb_span').html(' > ' + $(this).text()).closest('a').attr('href',link);
		if($(window).width() < 479) {
			!$(this).closest('li').hasClass('submenu') && $(this).closest('ul').hide().closest('li.submenu').removeClass('open');
		}
	});
	$('.submenu > a').click(function(e)
	{
		e.preventDefault();
		var submenu = $(this).siblings('ul');
		var li = $(this).closest('li');
		var submenus_parents = li.siblings('li.submenu');
		var submenus = submenus_parents.find('> ul');
		if(li.hasClass('open'))
		{
			if(($(window).width() > 768) || ($(window).width() < 479)) {
				submenu.slideUp();
			} else {
				submenu.fadeOut(250);
			}
			li.removeClass('open');
		} else {
			if(($(window).width() > 768) || ($(window).width() < 479)) {
				submenus.slideUp();			
				submenu.slideDown();
			} else {
				submenus.fadeOut(250);			
				submenu.fadeIn(250);
			}
			submenus_parents.removeClass('open');
			li.addClass('open');
		}
	});
	
	var ul = $('#sidebar > ul');
	
	/*$('#sidebar > a').click(function(e)
	{
		e.preventDefault();
		var sidebar = $('#sidebar');
		if(sidebar.hasClass('open'))
		{
			sidebar.removeClass('open');
			ul.slideUp(250);
		} else 
		{
			sidebar.addClass('open');
			ul.slideDown(250);
		}
	});*/
	
	// === Resize window related === //
	$(window).resize(function()
	{
		if($(window).width() > 479)
		{
			ul.css({'display':'block'});	
			$('#content-header .btn-group').css({width:'auto'});		
		}
		if($(window).width() < 479)
		{
			//ul.css({'display':'none'});
			//fix_position();
		}
		if($(window).width() > 768)
		{
			$('#user-nav > ul').css({width:'auto',margin:'0'});
            $('#content-header .btn-group').css({width:'auto'});
		}
	});
	
	if($(window).width() < 468)
	{
		//ul.css({'display':'none'});
		//fix_position();
	}
	
	if($(window).width() > 479)
	{
	   $('#content-header .btn-group').css({width:'auto'});
		ul.css({'display':'block'});
	}
	
	// === Tooltips === //
	$('.tip').tooltip();	
	$('.tip-left').tooltip({ placement: 'left' });	
	$('.tip-right').tooltip({ placement: 'right' });	
	$('.tip-top').tooltip({ placement: 'top' });	
	$('.tip-bottom').tooltip({ placement: 'bottom' });	
	
	// === Search input typeahead === //
	$('#search input[type=text]').typeahead({
		source: ['Dashboard','Form elements','Common Elements','Validation','Wizard','Buttons','Icons','Interface elements','Support','Calendar','Gallery','Reports','Charts','Graphs','Widgets'],
		items: 4
	});
	
	// === Fixes the position of buttons group in content header and top user navigation === //
	function fix_position()
	{
		var uwidth = $('#user-nav > ul').width();
		$('#user-nav > ul').css({width:uwidth,'margin-left':'-' + uwidth / 2 + 'px'});
		if($(window).width() < 479)
		{
			$('#user-nav > ul').css({width:'auto',margin:'0'}); 
		}
        var cwidth = $('#content-header .btn-group').width();
        $('#content-header .btn-group').css({width:cwidth,'margin-left':'-' + uwidth / 2 + 'px'});
	}
	
	// === Style switcher === //
	$('#style-switcher i').click(function()
	{
		if($(this).hasClass('open'))
		{
			$(this).parent().animate({marginRight:'-=190'});
			$(this).removeClass('open');
		} else 
		{
			$(this).parent().animate({marginRight:'+=190'});
			$(this).addClass('open');
		}
		$(this).toggleClass('icon-arrow-left');
		$(this).toggleClass('icon-arrow-right');
	});
	
	$('#style-switcher a').click(function()
	{
		var style = $(this).attr('href').replace('#','');
		$('.skin-color').attr('href','css/maruti.'+style+'.css');
		$(this).siblings('a').css({'border-color':'transparent'});
		$(this).css({'border-color':'#aaaaaa'});
	});
	
	$('.lightbox_trigger').click(function(e) {
		
		e.preventDefault();
		
		var image_href = $(this).attr("href");
		
		if ($('#lightbox').length > 0) {
			
			$('#imgbox').html('<img src="' + image_href + '" /><p><i class="icon-remove icon-white"></i></p>');
		   	
			$('#lightbox').slideDown(500);
		}
		
		else { 
			var lightbox = 
			'<div id="lightbox" style="display:none;">' +
				'<div id="imgbox"><img src="' + image_href +'" />' + 
					'<p><i class="icon-remove icon-white"></i></p>' +
				'</div>' +	
			'</div>';
				
			$('body').append(lightbox);
			$('#lightbox').slideDown(500);
		}
		
	});
	$('#lightbox').live('click', function() { 
		$('#lightbox').hide(200);
	});

	$('.active .menu_a').click();
	
	$('body').keydown(function(e){
	    var e = e||event;
	    var currKey = e.keyCode||e.which||e.charCode;
	    currKey == 27 && window.setTimeout(function(){ noSideBar('esc',false)},20);
	}).delegate('.fullscreenBtn','click',function(){
		$(this).hasClass('big') ? 
			noSideBar('esc',false) : 
			noSideBar('full',false);
	});
});
function triggermenu(href){
	var target = $('#sidebar .menu_a[href="'+ href +'"]');	
	target.click();
	target.closest('.submenu').find('> a').click();
}
function runPrefixMethod (element, method) {
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
};
function fullscreen(esc){
	var docElm = document.documentElement;
	if(esc != 'esc'){
		runPrefixMethod(docElm, "RequestFullScreen");
	}else{
		runPrefixMethod(document, "CancelFullScreen");
	}
}
function escFullScreen(esc,bol){
	if(esc != 'esc'){
		$('#iframe-main').hasClass('fullScreen') ? 
			$('#iframe-main').removeClass('fullScreen'):
			$('#iframe-main').addClass('fullScreen');
	}else{
		$('#iframe-main').removeClass('fullScreen');
	}
	bol && fullscreen(esc);
}
function noSideBar(esc,bol){
	var fullbtn = $('#breadcrumb .fullscreenBtn');
	if(esc != 'esc'){
		fullbtn.attr('title','还原').addClass('big').html('<i class="iconfont">&#xe605;</i>'),
		$('#content').hasClass('fullScreen') ? 
			$('#content').removeClass('fullScreen'):
			$('#content').addClass('fullScreen');
		//$(window).resize();
	}else{
		fullbtn.attr('title','最大化').removeClass('big').html('<i class="iconfont">&#xe604;</i>'),
		$('#content').removeClass('fullScreen');
	}
	init();
	//bol && fullscreen(esc);
}
function onlyHeader(esc){
	if(esc != 'esc'){
		$('#content').hasClass('fullScreenWidthHead') ? 
			$('#content').removeClass('fullScreenWidthHead'):
			$('#content').addClass('fullScreenWidthHead');		
	}else{
		$('#content').removeClass('fullScreenWidthHead');
	}
	init();
}
//初始化相关元素高度
function init(){
	var sctop = 89;
	if($('#content').hasClass('fullScreenWidthHead')){
		sctop = 50;
	}else if($('#content').hasClass('fullScreen')){
		sctop = 39;
	}    
    if($(window).width() > 479)
	{	
    	$("body").height($(window).height()-80);
    	$("#sidebar").height($(window).height()-50);
        $("#iframe-main").height($(window).height() - sctop);
	}else{
		$("#iframe-main").height($(window).height() - $('#sidebar').height() - sctop);
	}
}

function minMenu(bol){
	if(bol){
		$('#sidebar').closest('body').addClass('litterMenu');
		$('#sidebar').find('.submenu.open').addClass('mined').removeClass('open').find('>ul').hide();
	}else{
		$('#sidebar').closest('body').removeClass('litterMenu');
		$('#sidebar').find('.submenu.mined').addClass('open').removeClass('mined').find('>ul').show();
	}
}
