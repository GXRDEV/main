/**
 * Unicorn Admin Template
 * Diablo9983 -> diablo9983@gmail.com
**/
var baner = 80;
$(document).ready(function(){
	// === Sidebar navigation === //

	$('#sidebar').delegate('.submenu > a','click',function(e)
	{
		e.preventDefault();
		var submenu = $(this).siblings('ul');
		var li = $(this).parents('li');
		var submenus = $('#sidebar li.submenu ul');
		var submenus_parents = $('#sidebar li.submenu');
		if(li.hasClass('open'))
		{
			if(($(window).width() > 768) || ($(window).width() < 479)) {
				submenu.slideUp();
			} else {
				submenu.fadeOut(250);
			}
			li.removeClass('open');
		} else 
		{
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
	}).delegate('.submenu ul a','click',function(){
		var _h = $(this).attr('href');		
		_h && _h != '#' && (_t._set('_a_d_href', _h),_t._set('_a_d_hash',_h.split('#')[1]));
		$('.submenu .active').removeClass('active');
		$(this).closest('li').addClass('active');
	});
	
	var ul = $('#sidebar > ul');
	
	// === Resize window related === //
	$(window).resize(function()
	{
		var w = $(window).width(),h = $(window).height();
		if(w > 479)
		{
			ul.css({'display':'block'});	
			$('#content-header .btn-group').css({width:'auto'});		
		}
		if(w < 479)
		{
			ul.css({'display':'none'});
			fix_position();
		}
		if(w > 768)
		{
			$('#user-nav > ul').css({width:'auto',margin:'0'});
            $('#content-header .btn-group').css({width:'auto'});
		}
		$('#content').css('height',baner ? (h - baner) : '100%' );
	});
	
	if($(window).width() < 468)
	{
		ul.css({'display':'none'});
		fix_position();
	}
	if($(window).width() > 479)
	{
	   $('#content-header .btn-group').css({width:'auto'});
	   ul.css({'display':'block'});
	}
	$('#content').css('height',baner ? ($(window).height() - baner) : '100%');
	//$('#mainFrame').attr('src',$('.submenu > a:first').attr('href'))
	function fix_position()
	{
		var uwidth = $('#user-nav > ul').width();
		$('#user-nav > ul').css({width:uwidth,'margin-left':'-' + uwidth / 2 + 'px'});
        
        var cwidth = $('#content-header .btn-group').width();
        $('#content-header .btn-group').css({width:cwidth,'margin-left':'-' + uwidth / 2 + 'px'});
	}
});
//(
function screepAll(bol){
	if(bol=='esc'){
		baner = 80;
		$('#content').removeClass('marginl0').css({'height':($(window).height() - baner)}).siblings('div').show()
	}else{
		baner = 0;
		$('#content').addClass('marginl0').css({'height':'100%'}).siblings('div').hide();
	}
}
//)('esc');
