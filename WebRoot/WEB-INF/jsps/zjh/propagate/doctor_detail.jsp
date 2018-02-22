<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE HTML>
<html>
  <head lang="en">
    <base href="/">
    <meta charset="utf-8">
	<meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" name="viewport">
	<meta content="yes" name="apple-mobile-web-app-capable">
	<meta content="black" name="apple-mobile-web-app-status-bar-style">
	<meta content="telephone=no" name="format-detection">
	<meta content="email=no" name="format-detection">
	<title>佰医汇合作医生平台</title>
	<link rel="stylesheet" type="text/css" href="/css/mobile.css?v=1.0" />
	<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
	<link rel="stylesheet" type="text/css" href="/libs/swiper/css/swiper.min.css" />
	<link rel="stylesheet" type="text/css" href="/css/view/pingllist.css" />
	<script>
		var oid = '${openid}';
		var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx4ee3ae2857ad1e18&redirect_uri=';
		url += encodeURIComponent(location.href);
		url += '&response_type=code&scope=snsapi_base&state=2017#wechat_redirect';
		if(!oid) location.replace(url);
	</script>
	<style>
		body{font-family:PingFang-SC-Regular, Helvetica, sans-serif;background:#f5f5f5;}
		figure{margin:0;}
		header{margin-bottom:10px;}
		.swiper-container{height:200px;background:#B9B9B9;overflow:hidden;}
		.swiper-slide{position:relative;}
		.swiper-slide img{position:absolute;left:50%;top:50%;transform:translate(-50%,-50%);-webkit-transform:translate(-50%,-50%);height:100%;}
		.main .block{background:#fff;border-bottom:1px solid #E2E2E2;border-top:1px solid #DEDEDE;padding:0 14px;box-sizing:border-box;}
		.main .block + .block{margin-top:10px;}
		.docinfo{margin-top:2em;font-size:1rem;color:#323232;}
		.docinfo b{font-size:1.3rem;margin-right:0.5em;}
		.docinfo p:last-child{color:#8D8D8D;line-height:3em;}
		
		.sdegree{font-size:1rem;margin-top:0.5em;color:#323232;}
		.sdegree b{font-size:1.6rem;color:#D84C3D;}
		.signlabel{margin:1em 0 1.5em;font-size:0.8rem;}
		.signlabel span{display:inline-block;padding:2px;color:#8095A6;background-color:#F2F3F5;margin-right:1em;border-radius:2px;}
		
		.hosinfo .flex{padding:1em 0;font-size:1rem;color:#8C8C8C;line-height:1.5em;}
		.hosinfo .flex + .flex{border-top:1px solid #EDEDED;}
		.hosinfo .flex .f1{width:55%;}
		.hosinfo .flex .f1 + .f1{width:45%;}
		.hosinfo .flex span{color:#242424;}
		
		.textinfo h2{font-size:1.3rem;color:#2a2a2a;border-bottom:1px solid #E6E6E6;padding:0.8em 0;font-weight:700;}
		.textinfo p{padding:1em 0;line-height:1.6em;font-size:1rem;color:#2b2b2b;}
		
		.numlist b,.numlist span{display:block;text-align:center;}
		.numlist b{font-size:3rem;padding:0.8em 0 0.3em;}
		.numlist span{font-size:0.9rem; padding:0.3em 0 2em;}
		.alink a{position:relative; font-size:1rem;text-align:center;color:#0186D3;padding:1em 0;}
		.alink a img{ height:1.6em;}
		.alink a label{display:block;margin-top:0.3em}
		.alink a.disabled{cursor: not-allowed;}
		.alink a.disabled img,
		.alink a.disabled label{ 
			-webkit-filter: grayscale(100%);
		    -moz-filter: grayscale(100%);
		    -ms-filter: grayscale(100%);
		    -o-filter: grayscale(100%);		    
		    filter: grayscale(100%);			
		    filter: gray; opacity: .3;	    
		}
		.alink a + a:after{
			content:"";
			position:absolute;
			left:0;height:2em;width:1px;background:#ccc;
			top:50%;margin-top:-1em;
		}
		@media (min-width: 1025px) {
			html{font-size:16px!important;}
			.main{width:800px;margin:0 auto;}
		}
		@media (min-width: 768px) and (max-width: 970px) {
			html{font-size:16px!important;}
			.swiper-container{height:360px;}
		}
		@media (min-width: 481px) and (max-width: 767px) {
			html{font-size:16px!important;}
			.swiper-container{height:300px;}
		}
		@media (max-width: 480px) {
			html{font-size:13px!important;}
			.swiper-container{height:200px;}
		}
	</style>
  </head>  
  <body>
    <div class="main">
		<header>
			<section class="swiper-container">
				<figure class="swiper-wrapper">
					<c:forEach items="${images }" var="img">
						<p class="swiper-slide">
							<img src="${img.fileUrl }" />
						</p>
					</c:forEach>					
				</figure>
				<div class="swiper-pagination"></div>
			</section>			
			<section class="block">
				<div class="docinfo flex">
					<div class="f1">
						<p><b>${special.specialName }</b><span>${special.duty }&ensp;${special.profession }</span></p>
						<p>${special.hosName }&emsp;${special.depName }</p>
					</div>
					<!-- <div class="follow">
						<i class="iconfont"></i>关注
					</div> -->
				</div>
				<!-- <p class="sdegree">
					患者满意度：<b>100%</b>
				</p> -->
				<%-- <p class="signlabel">
					<!-- <span>专家会诊</span> -->
					<c:if test="${special.openTel == 1 }">
						<a href="/wzjh/intocasesub?docid=${special.specialId}&ltype=2&openid=${openid}"><span>电话问诊</span></a>
					</c:if>
					<c:if test="${special.openAsk == 1 }">
						<a href="/wzjh/intocasesub?docid=${special.specialId}&ltype=1&openid=${openid}"><span>图文问诊</span></a>
					</c:if>
				</p> --%>
			</section>
		</header>
		<section class="block hosinfo">
			<div class="flex">
				<div class="f1 whitespace">医院等级：<span>${special.hosLevel}</span></div>
				<div class="f1 whitespace">教学职称：<span>${special.profession }</span></div>
			</div>
			<div class="flex">
				<div class="f1 whitespace">所属医院：<span>${special.hosName }</span></div>
				<div class="f1 whitespace">职&emsp;&emsp;称：<span>${special.duty }</span></div>
			</div>
		</section>
		<section class="block alink">
			<div class="flex">				
				<a class="f1 whitespace <c:if test='${special.openAsk != 1 }'>disabled</c:if>" href=<c:choose><c:when test="${special.openAsk == '1' }">/wzjh/intocasesub?docid=${special.specialId}&ltype=1&openid=${openid}</c:when><c:otherwise>javascript:;</c:otherwise></c:choose>>
					<img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACMAAAAkCAYAAAAD3IPhAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTM4IDc5LjE1OTgyNCwgMjAxNi8wOS8xNC0wMTowOTowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTcgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6M0I1RDlFRjlFNTA0MTFFNkI5RDI4N0Y5RkIwOUIyRjciIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6M0I1RDlFRkFFNTA0MTFFNkI5RDI4N0Y5RkIwOUIyRjciPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDozQjVEOUVGN0U1MDQxMUU2QjlEMjg3RjlGQjA5QjJGNyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDozQjVEOUVGOEU1MDQxMUU2QjlEMjg3RjlGQjA5QjJGNyIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pnoq2XsAAAE+SURBVHja7Ni/S4JBGMDx13QoWhxzEhr6A3KpHMpGifoDcgnBLWgsApfAaCpwanR5CVraKgr64RI2KTQ1S0FERIiQYH0PLnh54aUIu+eCe+CD6/eFe++9M+ZVmh+eJZPQvze4EuyYxdRXzCU2BGO2VcyQZ9G4GBfjYlyMi3ExLuYfxAyjZ0tMGm0bYkaQw7kNMSUk4UvHTKKCM1xIxszriGcsm36bYhjDAg7VGsED5vAUvKr8ZFaxN6AHeEUZu+iE703fPdEWNnGM1i8D3vGCO9TRjbrERU0c+yiiijX0//pGGbUrHmAR69gxdb0Nj3rvjzCDFdRM3rWDk8IJxrGk14knETOBU4zqfeBW6qudwbVeoFnTIcGYabUdqy+nDrmXPM+o/0caejd8lD5c+cjjTfqkp7bkgt4hRedTgAEAJIc06AqT7cEAAAAASUVORK5CYII="/>
					<label>图文问诊</label>
					<c:if test="${!empty special.askAmount }"><label>${fn:replace(special.askAmount, '.00', '')}元 / 次</label></c:if>
				</a>
				<a class="f1 whitespace <c:if test='${special.openTel != 1 }'>disabled</c:if>" href=<c:choose><c:when test="${special.openTel == '1' }">/wzjh/intocasesub?docid=${special.specialId}&ltype=2&openid=${openid}</c:when><c:otherwise>javascript:;</c:otherwise></c:choose>>
					<img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAC0AAAAlCAYAAADWSWD3AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTM4IDc5LjE1OTgyNCwgMjAxNi8wOS8xNC0wMTowOTowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTcgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6M0I1RDlFRkRFNTA0MTFFNkI5RDI4N0Y5RkIwOUIyRjciIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6M0I1RDlFRkVFNTA0MTFFNkI5RDI4N0Y5RkIwOUIyRjciPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDozQjVEOUVGQkU1MDQxMUU2QjlEMjg3RjlGQjA5QjJGNyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDozQjVEOUVGQ0U1MDQxMUU2QjlEMjg3RjlGQjA5QjJGNyIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Po6py0MAAAOgSURBVHjazJhZSFRRGMfviGaLBBUlDpUWRWVYQmYLlJUVZWW054MULQRmFNFDkFgJRS8VpFJhm70F5UPbFIgVlG0YmD20WISVS7aImTpm1v/L/9AN7syc64yePvhx8c451/899zvfchzGwXJD0caBxaAPyDE0WqiP3yJAMlhAYni/DZwAH/8X0WPBQopMAuG8/wIcBY1gL9gADukS7YB7OHHdA1JMq9kMbgMXuAEqTS9ZBVrAaNChQ3QIyaA75HGVB4FF/LvSNL4dnAYjwTxdKy2C34M3oA5sAzdBq485BVzhLTpFG3SF8cCpMKeKbrNEcXy3ib7Da7LivJP07406Rbvor6mK866Dd2CT6Rk9LroeFPOTD1CY95MbcjiYoUu02AXG5VWKc0t4jdcpugg02fDTfrx+1ylast1ZkAhmKcydwOtznaLFjnBD7laYO4Vjn+gW/RZcBPPBZD9z5Ys8ZcrXKlosmyt47E9tYm1DySudcdpsIiQfTAVpXuaJ/1eAlTZie1CrPKv7A8FL4AZx4IvFmFGglFFkLrivc6UNitzF2uK4lzGV7GR+MKNO0i1a7By4ClaDdC9jHoFlIIzJJkm3aIMdSg19PM7LmFuMNh1sGNbpFl3PlZb0fg0M9jLuHpgJavmFJPL01iVa7C7YDoaBK6Cvl3EVjO0lbCYemrKmPw0JYAWYDWLZOXWpGzebdN/RzJQuNr9WSeUT2zAZtw+UsSHOYV3zb+QyjLXgABhh8aw2dvzV7KpqmczyHDbOPRzsGTPYNKT4yYbxbBYS+TI5bNVauZr5rHE+c1w5VziSRIEhjGCRdLdnsrfsiPYIzwVb+flT/Zx/hDDyZLMZrmHdnsbfJJxmgQaF//2ALxJtt+v4BTLBfhZM8qCJPsZLRCkEY1jyuvkSZfT/TEXBBvNBmOpGtDLx1/V881KFGlxqmTMUPwdM70J12OgJAoH0d4VMJrJBToFL9EFf1sa43pVDnvZgiPZkxHg2D8t5fLbDRlSyY02BuofZvjFzLgVfGeLKmd6D2am7ee0fzIde5oFPFrv0IiacdOPvQWYg5jn1Cg32mUULk4WUrYeZRc8zQeQy8wXiHmIR3XXQUsfSVlZ8J/jA8PaYhzwFdB+nzY0oFm43uQRiUm+vMTpPY2NN96sZt1+zR63iPmlgbPac7MqB52apZ3pStNlijM5zwwQihVUvxbnTdIm2SvdRfBkpzCJIGH9v5kaU6FT8W4ABANmYz5w83mAxAAAAAElFTkSuQmCC"/>
					<label>电话问诊</label>
					<c:if test="${!empty special.telAmount }"><label>${fn:replace(special.telAmount, '.00', '')}元 / 分钟</label></c:if>
				</a>
				<a class="f1 whitespace <c:if test='${special.openVedio != 1 }'>disabled</c:if>" href=<c:choose><c:when test="${special.openVedio == '1' }">module/remote.html?code=&state=123#/main/${openid}/${special.specialId}</c:when><c:otherwise>javascript:;</c:otherwise></c:choose>>
					<img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAC0AAAAjCAYAAAAAEIPqAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTM4IDc5LjE1OTgyNCwgMjAxNi8wOS8xNC0wMTowOTowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTcgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NDBBOThCNUVFNTA0MTFFNkI5RDI4N0Y5RkIwOUIyRjciIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NDBBOThCNUZFNTA0MTFFNkI5RDI4N0Y5RkIwOUIyRjciPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDozQjVEOUVGRkU1MDQxMUU2QjlEMjg3RjlGQjA5QjJGNyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDozQjVEOUYwMEU1MDQxMUU2QjlEMjg3RjlGQjA5QjJGNyIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PhQOz1EAAAEwSURBVHja7NixSkJRHMfxczTBQXFoSQcRpMEp2/IJAhdfwBdw8w10cg56AuemBnuD6C5Bd2poSigHaYgcFBT7/fAv18jpCnoO/v/wRRGFj9fjudxrTS9cGs/mRB7naOiBt0jzGv2Jyh6g3wlPGA9H0YpWtKIVrWhFK1rRilb0EaJnyHripXNB9AfKo7TjYPrO0IjoZ7kqv3YcTV8KBUT35cUuSjoKpqsjz/tEP6BHVEW3Dq5vK65LcQ6I5h2mJhqjFrpDBUfABfG0xEfn0ppeuH7DObpHFTSVXyBA3wfA5tAVqssf8BU10Nvq0EdoTga1pVMHjvQXupEm0Xr5i95c+DV0IV9k30MgYU/cl/8v8u1oPY0r2kT3p+PMC8ru8PkfOTfsFV2SrSnuxN5KfwUYACURMgKXTc6eAAAAAElFTkSuQmCC"/>
					<label>远程问诊</label>
					<c:if test="${!empty special.vedioAmount }"><label>${fn:replace(special.vedioAmount, '.00', '')}元 / 20分钟</label></c:if>
				</a>
			</div>
		</section>
		<section class="block textinfo">
			<h2>擅长领域</h2>
			<p>${special.specialty }</p>
		</section>
		<section class="block textinfo">
			<h2>简介</h2>
			<p>${special.profile }</p>
		</section>
		<section id="pinglistdiv" class="block textinfo">
			<h2>评价列表</h2>
			<p>
				<span id="signslist" class="signlists"></span>
				<span id="pingllist"></span>
			</p>
		</section>
		<!-- <section class="block textinfo">
			<h2>平台数据</h2>
			<div class="flex numlist">
				<div class="f1">
					<b>32</b>
					<span>专家会诊次数</span>
				</div>
				<div class="f1">
					<b>10</b>
					<span>电话问诊次数</span>
				</div>
				<div class="f1">
					<b>56</b>
					<span>图文问诊次数</span>
				</div>
			</div>
		</section> -->
    </div>
	<c:if test="${ fn:indexOf(special.listSpecialPicture,'://') != -1}">
		<input type="hidden" id="imgUrl" value="${fn:replace(special.listSpecialPicture,'http://','https://')}"/>
	</c:if>
	<c:if test="${ fn:indexOf(special.listSpecialPicture,'://') == -1}">
		<input type="hidden" id="imgUrl" value="http://wx.15120.cn/SysApi2/Files/${special.listSpecialPicture }"/>
	</c:if>
	<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
	<script src="/libs/swiper/js/swiper.jquery.min.js"></script>
	<script>
		new Swiper('.swiper-container', {
	         pagination: '.swiper-pagination',
        	paginationClickable: true
		});
	</script>	
    <script src="/js/base.js"></script>
	<script src="/js/infinite.scroll/jquery.endless-scroll-1.3.js"></script>
	<script type="text/javascript">
		var docid = '${special.specialId}';
	</script>
	<script src="/js/view/moredocpingl.js"></script>
	<script src="/js/jweixin-1.0.0.js"></script>
	<script>		
		var imgUrl = document.querySelector('#imgUrl').value;
		if(imgUrl.indexOf('://') != -1){
			imgUrl = imgUrl.replace('http://','https://');
		}else {
			imgUrl="http://wx.15120.cn/SysApi2/Files/"+imgUrl;
		}
        var lineLink = location.origin + location.pathname;
        var descContent = '${special.specialty }';
        var shareTitle = '${special.specialName} ${special.duty} ${special.profession} ${special.hosName}';
        
		var ops={
			title: shareTitle,
			desc: descContent,
			link: lineLink,
			imgUrl: imgUrl.replace(/\\/g, "/")
		};

		$.get('https://weixin.ebaiyihui.com/d2p/system/share/params', { 
			shareurl: location.href 
		}).then((res) => {
			var cfg = Object.assign(res, {
				jsApiList: [
					'hideAllNonBaseMenuItem',
					'showMenuItems',
					'onMenuShareAppMessage',
					'onMenuShareTimeline',
					'showAllNonBaseMenuItem'
				]
			});
			wx.config(cfg);
			wx.ready(function(){
				wx.hideAllNonBaseMenuItem();
				wx.showMenuItems({
				    menuList: ['menuItem:share:appMessage','menuItem:share:timeline']
				});
				wx.onMenuShareAppMessage(ops);
				wx.onMenuShareTimeline(ops);
				//wx.showAllNonBaseMenuItem();
			});
		})
	</script>
  </body>
</html>
