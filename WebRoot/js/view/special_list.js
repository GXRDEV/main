var _b = {
		prosing:false,
		_post:function(url,ops,fun,err){
			return _$(_burl + url,ops,fun,err);
		},
		showFilter:function(t){	
		},
		getKS:function(_v){
			var _h = _ts._get('ks_detail') || '';
			if(_b.prosing){ 
				return false;
			}
			if($('#details li').size()){ 
				return false;
			}
			if(_h){
				$('#details').html(_h);
				return false;
			}
			_b.prosing = true;
			_b._post('wzjh/gainBigDepartments',{},function(d){  
				var h = '<div class="tableRow clearfix"><aside>{0}</aside><section><ul></ul></section></div>',ds = '';
				d.length ? 
					$.each(d,function(i,o){
						ds += '<div><span>'+ o.name +'</span>';
						ds += '<ul>'
						o.beans && o.beans.length ? 
							$.each(o.beans,function(j,p){
								ds += '<li data-id="'+ p.id +'"><span>'+ p.displayName +'</span></li>';
							}) : ( ds += '<li><span>无数据</span></li>');
						ds += '</ul>'
						ds += '</div>';
					}): ($('#details li').size() < 1 && $('#details').html('<div class="noresult">查询无相关数据</div>'));
					ds && (h = h.replace('{0}',ds),$('#details').html(h),_ts._set('ks_detail',h));
					_b.prosing = false;
			},function(){
				$('#details li').size() < 1 && $('#details').html('<div class="noresult">加载出错。</div>');
				_b.prosing = false;
			});
		},
		showIndex:function(){
			$('html').removeClass('fullScreen'),$('#details').hide(),$('#index').show()
		}
};
$(document).ready(function(){
	$('#index').delegate('#thelist li .doclistselectbtn','click',function(){
		window.location.href=_burl+'wzjh/'+ _action +'?openid='+_openid+'&sid='+$(this).attr('data-id')+'&cooHosId='+_cooHosId;
	}).delegate('.filterNav a','click',function(){
		$(this).addClass('current').siblings().removeClass('current');
	});
	$('#callme').delegate('.cols1','click',function(){
		var tar = this.getAttribute('data-tar');
		if($(this).hasClass('curr')){
			$(this).removeClass('curr');
			location.hash = '';
		}else{
			$(this).addClass('curr').siblings().removeClass('curr');
			location.hash = tar;
		}
	});
	$('#details').delegate('aside div','click',function(){
		$(this).addClass('selected').siblings('.selected').removeClass('selected');
		$('#details section ul').html($(this).find('ul').html());
	}).delegate('section li','click',function(){
		var id = $(this).attr('data-id') || '',t = $(this).text();
		$('#keywords').val('');
		id && ($(this).addClass('selected'),$('#deptid').val(id),$('#mainform').submit());
	});
	
	$('#btnsearch').click(function(){
		$('#mainform').submit();
	});
	
	$('body').delegate('.g_dialog .header1 button','click',function(){
		location.hash = '';
		this.className == 'ok' && $('#mainform').submit();
		$('#callme .cols1').removeClass('curr');
	}).delegate('.g_dialog .bodyer1 .dialist','click',function(){
		var id = $(this).hasClass('select') ? '' : this.getAttribute('data-id'),
				pdiv = $(this).closest('.g_dialog'),
				sign = pdiv.attr('id');
			$(this).hasClass('select') ? $(this).removeClass('select') : $(this).addClass('select').siblings('.select').removeClass('select');
			sign == 'citys' ? $('#cityid').val(id) : $('#zwid').val(id);
	});
	if(_cityid){
		$('#citys dd[data-id="'+ _cityid +'"]').addClass('select').siblings('.select').removeClass('select');
	}
	if(_zwid){
		$('#zhics dd[data-id="'+ _zwid +'"]').addClass('select').siblings('.select').removeClass('select');
	}
	window.onhashchange=function(){
		var hashStr = location.hash.replace("#","") || '';
		switch(hashStr){
			case 'details':
				_b.getKS(1),$('html').addClass('fullScreen'),$('#details').show(),$('#index').hide(),isdown=true;
				$('#citys,#zhics').hide();
				break;
			case 'citys':
				_b.showIndex(),isdown=true;
				$('#citys').show();
				$('#zhics').hide();
				break;
			case 'zhics':
				_b.showIndex(),isdown=true;
				$('#citys').hide();
				$('#zhics').show();
				break;
			default:
				_b.showIndex(),isdown=false;
				$('#citys,#zhics').hide();
				break;
			
		}
	};
	window.onhashchange();
});	

function getdds(func){
	var html='',picurl = _burl;
	if(totle==pageno) return false;
	isdown = true;
	$.ajax({
		url:_burl + 'wzjh/morespecials',
		dataType:'json',
		type:'post',
		data:{pageNo:pageno,depid:_depid,scontent:_scontent,scity:_scity,spro:_spro,stype:'2'},
		success:function(d){
            if(d.specials.length > 0){
            	$.each(d.specials,function(i,o){
                    html += "<li class='slist'><div class='docDesc'>";
                    html += "<div class='inner'>";
                    html += "<div class='top'>";
					if(o.listSpecialPicture.indexOf('://')!=-1){
						html += "<div class='fl'><img src='"+o.listSpecialPicture.replace('http://','https://')+"' alt=''></div>";
					}else {
						html += "<div class='fl'><img src='http://wx.15120.cn/SysApi2/Files/" + o.listSpecialPicture + "' alt=''></div>";
					}
                    html += "<div class='fr'>";
                    html += "<p class='lightgray'><span class='lightgray1 name1'>" + o.specialName + "</span><span class='p_zhic'>" + o.specialTitle + "</span></p>";
                    html += "<p class='lightgray'>" + o.hosName +"</p><p class='hasopend'>已开通<i class='iconc iconc-1'></i><i class='iconc iconc-2'></i><i class='iconc iconc-3'></i><i class='iconc iconc-4'></i></p>";
                    html += "</div>";
                    html += "</div>";
                    html += "</div>";
                    html += "<div class='listSpecialty lightgray0'>擅长:" + o.specialty + "</div>";
                    html += '<a href="javascript:void(0)" data-id="'+ o.specialId +'" class="doclistselectbtn">选定</a>';
                    html += "</div></li>";
				});
				pageno++ ;func(html);isdown = false
			}else{
				totle = pageno;isdown = true;
				 if (document.getElementById('wait') != null) {
                    document.getElementById('wait').innerHTML = "已加载所有数据。";
                    document.getElementById('wait').className = "border";
                }
			}
		}
	});
}