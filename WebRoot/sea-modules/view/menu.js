define(function(require) {
	$(document).ready(function(){	
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
		});
		$('#userInfo > b').click(function(){
			$('#sidebar > ul').is(':visible') ? $('#sidebar > ul').hide() : $('#sidebar > ul').show()		
		});		
		var ul = $('#sidebar > ul');		
		$(window).resize(function()
		{
			var ww = $(window).width();		
			if(ww > 479)
			{
				ul.css({'display':'block'});	
			}
			if(ww < 479)
			{
				ul.css({'display':'none'});
			}
		});		
		if($(window).width() < 468)
		{
			ul.css({'display':'none'});
		}
		if($(window).width() > 479)
		{
			ul.css({'display':'block'});
		}			
		function fix_position()
		{
			var uwidth = $('#user-nav > ul').width();
			$('#user-nav > ul').css({width:uwidth,'margin-left':'-' + uwidth / 2 + 'px'});
	        
	        var cwidth = $('#content-header .btn-group').width();
	        $('#content-header .btn-group').css({width:cwidth,'margin-left':'-' + uwidth / 2 + 'px'});
		}
	});
});