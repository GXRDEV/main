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
	<link rel="stylesheet" type="text/css" href="/css/mobile.css?v=1.0" />
	<link href="/weixin/css/normal.css" rel="stylesheet">
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
    <link rel="stylesheet" type="text/css" href="/weixin/css/main.css">
    <link rel="stylesheet" type="text/css" href="/weixin/css/doctor.css">
    <link rel="stylesheet" type="text/css" href="/weixin/css/index.css">
    <link rel="stylesheet" type="text/css" href="/weixin/css/Specialists.css?v=1.0">
	<link rel="stylesheet" type="text/css" href="/css/view/specialList.css" charset="GB2312"/>
    <style>
    	*{-webkit-tap-highlight-color:rgba(0,0,0,0);}
    	body{min-width:auto;}
    	.hidden{display:none;}
    	/*.docList li{position:relative;}
		.docList li.slist:after{content:"";position:absolute;width:10px;height:10px;border:1px solid #ccc;border-left:0;border-bottom:0;
			-webkit-transform:rotate(45deg);
			transform:rotate(45deg);
			right:8px;top:50%;margin-top:-8px;}*/
    	.doctor {
			margin-top: 0px;
		}
		.g_fixed{
			position:fixed!important
		}
		.docDesc .fl img{border:0;}
		#endless_scroll_loader{z-index:999;}
		
		
    </style>
</head>
<body>
	<div id="index">
		<div id="titleTap" class="g_fixed">
			<div class="nav-bar-block">
				<div class="bar-header box">
					<div class="cols1">
						<form action="/wzjh/faceplus" id="mainform" method="get" class="queryform">
							<input type="hidden" name="depid" id="deptid" value="${depid}"/>
							<input type="hidden" name="scity" id="cityid" value="${scity}"/>
							<input type="hidden" name="spro" id="zwid" value="${spro}"/>
							<input type="hidden" value="${openid}" id="uopenid" name="openid" />
							<input type="search" name="scontent" class="keywords" id="keywords" placeholder="根据专家姓名、擅长、医院、科室搜索" value="${scontent}"/>
							<button type="submit" id="btnsearch" class="btnsearch"><i class="iconfont icon-query">&#xe60f;</i> </button>
						</form>
					</div>
				</div>
			</div>
			<div class="filterNav flex ">
				<a href="#" class="filterOption f1 current"><span>好评度</span></a>
				<a href="#" class="filterOption f1"><span>地区</span></a>
				<a href="#" class="filterOption f1"><span>医院</span></a>
				<a href="#" class="filterOption f1"><span>科室</span></a>
				<a href="#" class="filterOption f1"><span>职称</span></a>
				<a href="#" class="filterOption f1"><span>价格</span></a>
			</div>
		</div>
		<div id="history" style="display:none;">
			<div class="dt clearfix">
				<div style="float:left;">历史搜索</div>
				<div style="float:right;" class="clearhistory remove"><i class="icon_clear"></i>清空</div>
			</div>
			<dl class="historyList twocolums"></dl>
		</div>		
		<div id="hotList" style="display:none;">
			<dl class="hotlist twocolums">
				<dt class="dt"><div>热门关键词</div></dt>
				<c:forEach items="${hots}" var="hot">
					<dd><div>${hot.searchKey }</div></dd>
				</c:forEach>
			</dl>
		</div>
		<div class="doctor center">
	        <div class="docList" style="margin-top:87px;">
	            <ul id="thelist">
	            	<c:forEach items="${specials}" var="sp">
	            		 <li class="slist">
	            		 	<div class="docDesc">
	            		 		<div class="inner">
	            		 			<div class="top">
	                                    <div class="fl">
											<c:if test="${ fn:indexOf(sp.listSpecialPicture,'://') != -1}">
												<img src="${fn:replace(sp.listSpecialPicture,'http://','https://')}" alt="${sp.specialName}"/>
											</c:if>
											<c:if test="${ fn:indexOf(sp.listSpecialPicture,'://') == -1}">
												<img src="http://wx.15120.cn/SysApi2/Files/${sp.listSpecialPicture}" alt="${sp.specialName}"/>
											</c:if>
										</div>
	                                    <div class="fr">
	                                        <p class="lightgray">
	                                        	<span class="lightgray1 name1">${sp.specialName}</span>
	                                        	<span class="p_zhic">${sp.specialTitle}</span>
	                                        </p>
	                                        <p class="lightgray">${sp.hosName}&nbsp;&nbsp;&nbsp;</p> 
	                                        <p class="hasopend">已开通<i class="iconc iconc-1"></i><i class="iconc iconc-2"></i><i class="iconc iconc-3"></i><i class="iconc iconc-4"></i></p>
	                                     </div>
	                                </div>
	            		 		</div>
	                        	<div class="listSpecialty lightgray0">擅长:${sp.specialty} </div>
	                        	<a href="javascript:void(0)" data-id="${sp.specialId}" class="doclistselectbtn">选定</a>
	            		 	</div>
	            		 </li>
	            	</c:forEach>     
	            </ul>
	        </div>
	        <div class="placeholder"></div>
	        <div class="wait" id="wait"></div>
	    </div>
	</div>	
	</div>
	<div id="loading" style="display:none;">
		<div style="position:fixed;left:0;top:0;right:0;bottom:0;opacity:0.5;background-color:#eee;z-index:10000;">&nbsp;</div>
		<img style="position:fixed;left:50%;top:50%;z-index:10001;" src="/img/mobile/ajax-loader.gif" />
	</div>
	<div id="callme" class="g_fixed">
		<div class="filter box">
			<div class="cols1" data-tar="citys">
				<span class="ks city">北上广<i class="iconfont">&#xe600;</i></span>				
			</div>
			<div class="cols1" data-tar="details">
				<span class="ks keshi">科室<i class="iconfont">&#xe600;</i></span>				
			</div>
			<div class="cols1" data-tar="zhics">
				<span class="ks zhicheng">职称<i class="iconfont">&#xe600;</i></span>				
			</div>
		</div>
	</div>
	<div id="details" class="index">
		<img style="position:absolute;left:50%;top:50%;" src="/img/mobile/ajax-loader.gif" />
	</div>	 
	<div id="citys" class="g_fixed g_dialog">
		<div class="g_layout"></div>
		<div class="g_content g_fixed">
			<div class="header1 box">
				<span class="cols1">
					<button class="quit">取消</button>
				</span>
				<span class="cols1">
					<button class="ok">确定</button>
				</span>
			</div>
			<dl class="bodyer1">
				<dd data-id="北京" class="dialist" onclick="javascript:void(0)"><span>北京</span></dd>
				<dd data-id="上海" class="dialist" onclick="javascript:void(0)"><span>上海</span></dd>
				<dd data-id="广州" class="dialist" onclick="javascript:void(0)"><span>广州</span></dd>
			</dl>
		</div>
	</div>
	<div id="zhics" class="g_fixed g_dialog">
		<div class="g_layout"></div>
		<div class="g_content g_fixed">
			<div class="header1 box">
				<span class="cols1">
					<button class="quit">取消</button>
				</span>
				<span class="cols1">
					<button class="ok">确定</button>
				</span>
			</div>
			<dl class="bodyer1">
				<dd data-id="主任医师" class="dialist" onclick="javascript:void(0)"><span>主任医师</span></dd>
				<dd data-id="副主任医师" class="dialist" onclick="javascript:void(0)"><span>副主任医师</span></dd>
			</dl>
		</div>
	</div>	
    <script>
		var pageno = 2,totle=0,keywords='${keywords}',isdown = false,_burl = '/',_action = 'plusexpertdetail';
    </script>
    <script src="/js/jquery-1.11.0.min.js"></script>
	<script src="/js/infinite.scroll/jquery.endless-scroll-1.3.js"></script>
	<script src="/js/base.js"></script>
	<script src="/js/view/special_list.js"></script>
	<script type="text/javascript">
		var isdown = false,pageno = 2,totle=0,_openid='${openid}',_cityid='${scity}',_zwid='${spro}',_cooHosId = '${cooHosId}';
		var _depid = '${depid}',_scontent = '${scontent}',
			_scity = '${scity}',_spro = '${spro}';
		$(document).ready(function(){
			$(document).endlessScroll({
		        bottomPixels:10,
				fireDelay: 100,
				insertAfter: ".slist:last",
				loader:'<p style="text-align:center;"><img src="'+ _burl +'lib/infinite.scroll/img/ajax-loader.gif" /></p>',
				stop:function(){
					return isdown;
				},
				callback: function(p) {
					getdds(function(dds){
						$('#thelist').append(dds);
					});
		        }
			});
		});
	</script>
</body>
</html>