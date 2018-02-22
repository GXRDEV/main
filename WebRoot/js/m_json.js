var _timer = window.setInterval(function(){
	($ || jQuery) && ((function($){
		var menus=[
		{
			text:'工作服务',
			href:'#',
			icon:'icon-th-list',
			name:'m1',
			children:[
				{
					text:'订单管理',
					href:'admin/orders.do',
					name:'m1_1'
				}
			]        			
		}
	];
	function getM(){
   		$.get('',function(menus){
   			$('#sidebar').html(creatM(menus));		
   		});	
   	}
   	function creatM(menus){
   		var name = _hash || location.hash,m=name.split('_'),l=m.length,ul='<ul>';
   		$.each(menus,function(i,menu){
   			var li = '<li class="',n = '#'+ menu.name;
   			menu.children && (li+='submenu ',m[0] == n && (li+='open '));
   			name == n && (li+='active ');					
   			li+='">';
   			li+='<a href="'+ menu.href + (menu.children ? '' : n) +'" target="mainFrame"><i class="icon '+ menu.icon +'"></i><span>'+ menu.text +'</span></a>';
   			menu.children && (li+='<ul>') && $.each(menu.children,function(ii,cmenu){
   				var nn = '#'+cmenu.name;
   				li+='<li'+ ((name == nn)? ' class="active"' : '') +'><a href="'+ cmenu.href + nn +'" target="mainFrame">'+ cmenu.text +'</a></li>';
   			}) && (li+='</ul>');
   			li+='</li>';
   			ul+=li;
   		});
   		ul+='</ul>';
   		return ul;
   	}
   	$('#sidebar').html(creatM(menus));
   })(jQuery),
   window.clearInterval(_timer)
);
}, 10);