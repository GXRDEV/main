<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//通过request["code"]得到code，通过code得到用户的基本信息
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>选择专家</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=yes" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
	<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
	<link rel="stylesheet" href="/css/view/doclist.css" />
	
</head>
<body class="fix-filterOuter fix-queryOuter">
	<div class="main">
		<header class="queryOuter">
			<div class="queryInner">
				<form method="get" id="searchform" action="#keywords" target="_self">
					<input type="search" name="keywords" id="keywords" placeholder="请输入医院、科室或医生姓名"/>
					<button type="submit" name="query" id="query"><i class="iconfont">&#xe603;</i></button>
				</form>
			</div>
		</header>
		<section class="bodyer">
			<article id="doclists"></article>
			<footer class="filterOuter">
				<div class="filterInner flex">
					<div class="f10"><button data-v="1" class="hosopt">医院</button></div>
					<div class="f10"><button data-v="2" class="depopt">科室</button></div>
					<div class="f10"><button data-v="3" class="ltype">服务</button></div>
				</div>
				<div class="filterOptions"></div>
				<div class="filterbg"></div>
			</footer>
		</section>
	</div>
    <script src="/js/jquery-1.11.0.min.js"></script>
	<script src="/js/infinite.scroll/jquery.endless-scroll-1.3.js"></script>
	<script src="/js/base.js"></script>
	<script type="text/javascript">
		var isdown = false,pageno = 1,totle=0,_hosid = '${hosid}',_depid = '${depid}',_openid = '${openid}',
			_bigdepid = '',_cityid = '',_ltype = '',_keywords = '',_burl = '/';
		var _b = {
			link: _burl,
			loading:'<div class="loadings"><img alt="" style="" src="'+ window.location.origin +'/img/loading/31.gif"/></div>',
			noresult:'<div class="noresult"><img src="'+ window.location.origin +'/img/mobile/kbzs/icon_noresult@2x.png" style="width:30px" alt=""/></div>',
			_post:function(url,ops,fun,err){
				return _$(_burl + url,ops,fun,err);
			},
			_get:function(url,ops,fun,err){
				return _$$(_burl + url,ops,'get','json',fun,err);
			},
			_getDEP:'',
			getDEP:function (callback){
				if(_b._getDEP) return _b._getDEP;
				this._get('propagate/gainBigDepartments', {}, function(d){
					var dl = '<div class="hoslist flex"><div class="leftlist f10"><dl>';
					$.each(d,function(i,o){
						dl += '<dd><label class="leftops forbtn2" id="'+ o.stands[0]['bigDepId'] +'">'+ o.name +'</label>'+ 
							(function(stands){
								var _dl = '<dl>';
								$.each(stands,function(ii,oo){
									_dl += '<dd><label id="'+ oo.bigDepId +'_'+ oo.id +'">'+ oo.displayName +'</label></dd>';
								});
								_dl += '</dl>';
								return _dl;
							})(o.stands)
						+'</dd>';
					});
					dl += '</dl></div><div class="rightlist f20" style="max-height:'+ $(window).height() * 0.6 +'px"></div></div>';
					
					_b._getDEP = dl;
					callback(dl);
				});
			},
			_getHOS:{},
			getHOS:function (id,callback){
				if(id in _b._getHOS) return _b._getHOS[id];
				this._get('propagate/gainHospitalsByArea', {distcode: id == 'all' ? '' : id}, function(d){
					var h = d.hospitals;
					_b._getHOS[id] = (function(_h){
								var _dl = '<dl>';
								$.each(_h,function(i,o){
									_dl += '<dd><label id="'+ o.id +'">'+ o.displayName +'</label></dd>';
								});
								_dl += '</dl>';
								return _h.length ? _dl : _b.noresult;
							})(h || []);
					callback(_b._getHOS[id]);
				});
			},
			getHOSHtml:function(){
				var citylist = [{
					code:'all',
					name:'全国'
				},{
					code:'11',
					name:'北京'
				},{
					code:'31',
					name:'上海'
				},{
					code:'440100',
					name:'广州'
				},{
					code:'440300',
					name:'深圳'
				}];
				return '<div class="hoslist flex"><div class="leftlist f10">'+ (function(cl){
					var dl = '<dl>';
					$.each(cl,function(i,o){
						dl += '<dd><label class="leftops forbtn1" id="'+ o.code +'">'+ o.name +'</label></dd>';
					});
					dl += '</dl>';
					return dl;
				})(citylist) +'</div><div class="rightlist f20" style="max-height:'+ $(window).height() * 0.6 +'px"></div></div>'
			},
			getLtype:function(){
				var lists = [{
					code:'all',
					name:'所有'
				},{
					code:'1',
					name:'图文问诊'
				},{
					code:'2',
					name:'电话问诊'
				},{
					code:'4',
					name:'远程问诊'
				}];
				return '<div class="hoslist flex"><div class="rightlist f1">'+ (function(cl){
					var dl = '<dl>';
					$.each(cl,function(i,o){
						dl += '<dd><label class="onlyme" id="'+ o.code +'">'+ o.name +'</label></dd>';
					});
					dl += '</dl>';
					return dl;
				})(lists) +'</div></div>'
			}
		};
		$(document).ready(function(){
			$('#searchform').submit(function(){
				location.hash = 'keywords';
				return false;
			});
			$(document).endlessScroll({
		        bottomPixels:10,
				fireDelay: 100,
				insertAfter: ".doclist:last",
				loader:'<p style="text-align:center;"><img src="'+ _b.link +'img/spinner.gif" /></p>',
				stop:function(){
					return isdown;
				},
				callback: function(p) {
					getdds(function(dds){
						$('#doclists').append(dds);
					});
		        }
			});
			$('body').delegate('.filterInner button','click',function(){
				var btn = this;
				$(btn).hasClass('selected') ? (function(){
					$(btn).removeClass('selected');
					$('.filterOptions,.filterbg').hide();
					isdown = false;
				})() : (function(){
					var v = $(btn).attr('data-v');
					$('.filterInner button.selected').removeClass('selected');
					$(btn).addClass('selected');
					switch(v){
						case '1':
							$('.filterOptions').html(_b.getHOSHtml()).show().next().show();
							$('.filterOptions').find('#' + (_cityid || 'all')).click();
							break;
						case '2':
							$('.filterOptions').html(_b.getDEP(function(h){
								$('.filterOptions').html(h).find('#' + (_bigdepid.split('_')[0] || '1')).click();
							}) || _b.loading).show().next().show();
							$('.filterOptions').find('#' + (_bigdepid.split('_')[0] || '1')).click();
							break;
						case '3':
							$('.filterOptions').html(_b.getLtype()).show().next().show();
							break;
					}
					isdown = true;
				})();
			}).delegate('.filterOptions .leftlist .leftops','click',function(){
				var id = this.getAttribute('id'),is1 = $(this).hasClass('forbtn1');
				$(this).parent().addClass('selected').siblings().removeClass('selected');
				$('.filterOptions .rightlist').html(is1 ? (_b.getHOS(id,function(h){
						$('.filterOptions .rightlist').html(h)
					}) || _b.loading) : $(this).next('dl').clone());
			}).delegate('.filterOptions .rightlist label[id]','click',function(){
				var id = this.getAttribute('id'),bol = id.indexOf('_') != -1;
				isdown = false,
				pageno = 1,
				totle=0;
				if(bol){
					_depid = id.split('_').pop();
					_bigdepid = id;
					$('.depopt').text($(this).text());
				}else{
					if($(this).hasClass('onlyme')){
						_ltype = id == 'all' ? '' : id;
						$('.ltype').text($(this).text());
					}else{
						_hosid = id,
						_cityid = $('.filterOptions .leftlist .selected label').attr('id'),
						_bigdepid = _depid = '';
						$('.hosopt').text($(this).text());
						$('.depopt').text('科室');
					}
				}
				location.hash = _hosid + '/' + _depid + '/' + _ltype;
				$('.filterOptions,.filterbg').hide();
				$('.filterInner .selected').removeClass('selected');
			});
			window.onhashchange=function(){
				var hashStr = location.hash.replace("#","");
				var hashArr = hashStr.split('/');
				if('keywords' == hashStr){
					pageno = 1;
					_keywords = $.trim($('#keywords').val());
					$('.filterOuter,.queryOuter').hide();
				}else{
					$('#keywords').val(_keywords = '');
					$('.filterOuter,.queryOuter').show();
					_hosid = hashArr[0] || '';
					_depid = hashArr[1] || '';
					_ltype = hashArr[2] || '';
				}				
				$('#doclists').html(_b.loading);
				getdds(function(dds){
					$('#doclists').html(dds);
				});
			}
			window.onhashchange();
		});
		function getdds(func){
			var html='',picurl = _b.link;
			if(totle==pageno) return false;
			isdown = true;
			$.ajax({
				url:picurl + 'propagate/doclistdata',
				dataType:'json',
				type:'post',
				data:{pageNo:pageno,depid:_depid,hosid:_hosid,ltype:_ltype,searchContent:_keywords},
				success:function(d){
		            if(d.specials.length > 0){
		            	$.each(d.specials,function(i,o){
		            		var imgurl=o.listSpecialPicture,pro=o.profession||'';
		            		if(!imgurl){
		            			imgurl = _burl + "img/defdoc.jpg";
		            		}
		            		if(imgurl.indexOf('://') == -1){
		            			imgurl="http://wx.15120.cn/SysApi2/Files/"+imgurl;
		            		}
							if(imgurl.indexOf('://') != -1){
								imgurl=imgurl.replace('http://','https://');
							}
		                    html += '<div class="doclist">';
		                    html += '<a class="redirect" href="'+ picurl +'propagate/doctordetail/'+ o.specialId +'?openid='+ _openid +'"></a>';
		                    html += '<div class="docinfo flex">';
		                    html += '<div class="thumb shk0"><img alt="'+ o.specialName +'" src="'+ imgurl +'" alt=""/></div>';
		                    html += '<div class="docdetail f1">';
		                  	if(o.profession == '讲师') pro = '';
		                    html += '	<p><b>'+ o.specialName +'</b><span>'+ (o.duty||'') +'&ensp;'+ pro+'</span></p>';
		                    html += '	<p><span>'+ (o.hosName || '') +'</span><span>'+ (o.depName || '&ensp;') +'</span></p>';
		                    html += '	<p><span>'+ (o.specialty || '&ensp;') +'</span></p>';
		                    html += '	<p><span>专家会诊</span>';
		                    if(o.openAsk){
		                    	html += '<span>图文问诊</span>';
		                    }
		                    if(o.openTel){
		                    	html += '<span>电话问诊</span>';
		                    }
		                    html += '</p></div>';
		                    html += '</div>';
		                    html += '</div>';
						});
						pageno++ ;func(html);isdown = false
					}else{
						pageno == 1 && func(_b.noresult)
					}
				}
			});
		}
	</script>
</body>
</html>