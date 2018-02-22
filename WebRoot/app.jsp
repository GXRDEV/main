<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="/">
    <title>佰医汇_中国高端的在线医疗咨询平台，汇集全国排名前十医院科室专家</title>
    <meta name="viewport" content="width=device-width,maximum-scale=1.0" />  
	<meta name="keywords" content="佰医汇，佰医汇在线，在线问诊，好大夫，在线医疗，挂号，挂佰医汇，名医，专家会诊，病例讨论，专家随诊，慢病管理，医疗专家，专家在线交流与讨论，医患交流平台，医患咨询平台，门诊预约系统，就医经验分享" />
    <meta name="description" content="佰医汇是汇聚国内排名前十医院科室专家提供在线精问诊的移动医疗平台。患者可以通过佰医汇与中国顶级名医直接交流、问诊。佰医汇提供高端、专业、完善的医疗咨询，其中包括：在线问诊专家，专家门诊预约，医院/专家信息查询中心，医患咨询交流平台，专家就医经验分享系统，专家康复计划等服务。" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link href="/css/reset.css" rel="stylesheet" />
    <link href="/fonticon/website/iconfont.css" rel="stylesheet" />
    <link href="/css/view/website.css" rel="stylesheet" />
	<script src="/sea-modules/jstpl/nodetpl.min.js"></script>
  </head>  
  <body style="width:auto;">
    	<div id="mainDIV"></div>
		<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
		<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
		<script src="/sea-modules/seajs/seajs.config.js"></script>
		<script>
			var _href = '/',_h = _href;
			seajs.use('view/website/main',function(website){
				nodetpl.config({
				  base: _href + 'website/',
				  openTag: '<?',
				  closeTag: '?>',
				  strict: true,
				  map: function(str) {
				    return str;
				  },
				  beforeCompile: function(html) {
				    return html;
				  },
				  afterCompile: function(html) {
				    return html;
				  }
				});
				website.init();
			});
		</script>
  </body>
</html>
