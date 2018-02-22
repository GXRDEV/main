$(function(){
	$("input#allCheck").click(function(){
		if($(this).attr("checked")=="checked"){
			$("input[name='ids']").each(function(){
				$(this).attr("checked",true);
				$(this).closest("tr").css("background-color","#dde");
			});
			
		}else{
			$("input[name='ids']").each(function(){
				$(this).closest("tr").css("background-color","#F6F6F6");
				$(this).attr("checked",false);
			});			
		}
	});
	$("input[name='ids']").live("click",function(){
		if($(this).attr("checked")=="checked"){
			$(this).closest("tr").css("background-color","#dde");
		}else{
			$(this).closest("tr").css("background-color","#F6F6F6");
		}
		allCheckOrNot();
	});
	$(".table_list td[class!=ckbox]").live("click",function(){
		var $checkbox=$(this).closest("tr").find("td:first").find("input[name='ids']");
		if($checkbox.attr("checked")=="checked"){
			$(this).closest("tr").css("background-color","#F6F6F6");
			$checkbox.attr("checked",false);
		}else{
			$(this).closest("tr").css("background-color","#dde");
			$checkbox.attr("checked",true);
		}
		allCheckOrNot();
	});
	$(".table_list tbody tr").live("mouseover",function(){
		$(this).css("background-color","#ade");
	}).live("mouseout",function(){
		var $checkbox=$(this).find("td:first").find("input[name='ids']");
		if($checkbox.attr("checked")=="checked"){
			$(this).css("background-color","#dde");
		}else{
			$(this).css("background-color","#FAFAFA");
		}
	});
	$(".table_list td a").live('click',function(event){
        event.stopPropagation();
    });
	function allCheckOrNot(){
		var length=$("input[name='ids']").length;
		var checkedLength=$("input[name='ids']:checked").length;
		if(checkedLength<length){
			$("#allCheck").attr("checked",false);
		}else{
			$("#allCheck").attr("checked",true);
		}
	}
});