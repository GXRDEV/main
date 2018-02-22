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
  <body>
  		<div class="toptip">
  			<div class="w1200 toptipinner clearfix">
  				<div class="fleft lefttoptip">
  					<a href="javascript:void(0)" data-action="home">首页</a>
  					<!-- <a href="javascript:void(0)">网站服务<i class="iconfont">&#xe60b;</i></a> -->
  					<a target="_blank" href="http://www.51zhuanjiahao.com/Pages/About/app.shtml">手机应用<i class="iconfont" style="font-size: 14px;vertical-align: 0;">&#xe60d;</i></a>
  					<a href="javascript:void(0)" data-js="addfavorite">加入收藏</a>
  				</div>
  				<div class="fright righttoptip">
  					<span class="usercenter">
  						<a href="javascript:void(0)" data-action="login">登录</a>
  						<a href="javascript:void(0)" data-action="regist">注册</a>
  					</span>
  					<!-- <a href="javascript:void(0)">我的提问<i class="iconfont">&#xe60b;</i></a> -->
  				</div>
  			</div>
  		</div>
  		<div class="menuicon">
  			<div class="w1200 menuiconinner">
  				<div class="iconinner clearfix">
  					<div class="sectionleft fleft clearfix">
	  					<div class="icontxt fleft">
	  						<img src="/img/website/icon.PNG"/>
	  						<h2 class="h">远程医疗领跑者</h2>
	  					</div>
	  					<div class="iconsearch fright">
	  						<div class="inputsearch">
	  							<i class="iconfont">&#xe600;</i>
	  							<input type="search" name="keywords" placeholder="请输入疾病、医生、医院..." />
	  							<button class="search">搜索</button>
	  						</div>
	  						<div class="iconsearchhistory whitespace">
	  							热门搜索：
	  							<span>前泪腺炎</span><span>月经失调</span>
	  							<span>痔疮</span><span>鼻炎</span>
	  						</div>
	  					</div>
  					</div>
  					<div class="sectionright fright">
  						<div class="iconkefu h">
  							<label>客服电话</label>
  							<span>400-631-9377</span>
  						</div>
  					</div>
  				</div>
  				<div class="menuinner h">
  					<a href="javascript:void(0)" data-action="home" class="menulist selected">首页</a>
  					<a href="javascript:void(0)" data-action="remote" class="menulist">远程门诊</a>
  					<a href="javascript:void(0)" data-action="online" class="menulist">在线问诊</a>
  					<a href="javascript:void(0)" data-action="team" class="menulist">专家团队</a>
  					<a href="javascript:void(0)" data-action="docs" class="menulist">名医对对碰<i class="iconfont">&#xe606;</i></a>
  				</div>
  			</div>
  		</div>
    	<div id="mainDIV"></div>
    	<div class="mainfooter h">
    		<ul class="clearfix">
    			<li><img src="/img/website/icon1.PNG"/></li>
    			<li>
    				<div>
    					<h2>关于我们</h2>
    					<a target="_blank" href="http://www.51zhuanjiahao.com/Pages/About/app.shtml">微信公众号</a>
    					<a target="_blank" href="http://weibo.com/zhuanjiahao1">新浪微博</a>
    					<a target="_blank" href="http://www.51zhuanjiahao.com/pages/about/aboutus.shtml">关于我们</a>
    					<a target="_blank" href="http://www.51zhuanjiahao.com/Pages/About/app.shtml">佰医汇app</a>
    				</div>
    			</li>
    			<li>
    				<div>
    					<h2>帮助</h2>
    					<a>建议与反馈</a>
    					<!-- <a>使用帮助</a>
    					<a>常见问题</a>
    					<a>用户协议</a> -->
    				</div>
    			</li>
    			<li class="lastli">
    				<div>
    					<a href="hosin/index.html" target="_blank"><i class="iconfont">&#xe608;</i>医院合作</a>
    					<p class="kefu"><i class="iconfont">&#xe605;</i>客服电话</p>
    					<a href="tel:400-631-9377">400-631-9377</a>
    				</div>
    			</li>
    		</ul>
    	</div>
    	<a class="globltask" target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=2071311612&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:2071311612:53" alt="点击这里给我发消息" title="点击这里给我发消息"/></a>
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
