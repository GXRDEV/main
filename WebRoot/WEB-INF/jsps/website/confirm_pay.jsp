<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
  <body class="confirmHtml">
  		<div class="toptip">
  			<div class="w1200 toptipinner clearfix">
  				<div class="fleft lefttoptip">
  					<a href="/">首页</a>
  					<!-- <a href="javascript:void(0)">网站服务<i class="iconfont">&#xe60b;</i></a> -->
  					<a target="_blank" href="#app">手机应用<i class="iconfont" style="font-size: 14px;vertical-align: 0;">&#xe60d;</i></a>
  					<a href="javascript:void(0)" data-js="addfavorite">加入收藏</a>
  				</div>
  				<div class="fright righttoptip">
  					<span class="usercenter">
  						<a href="javascript:void(0)" data-action="login">登录</a>
  						<a href="javascript:void(0)" data-action="regist">注册</a>
  					</span>
  					<a href="#user/user_myaskimg">我的提问<i class="iconfont">&#xe60b;</i></a>
  				</div>
  			</div>
  		</div>
  		<div class="menuicon">
  			<div class="w1200 menuiconinner">
  				<div class="iconinner clearfix prelative">
  					<div class="sectionleft fleft clearfix">
	  					<div class="icontxt fleft">
	  						<img src="/img/website/icon.PNG"/>
	  						<h2 class="h">远程医疗领跑者</h2>
	  					</div>
	  					<h2 class="fleft">远程门诊服务流程</h2>
  					</div>
  					<div class="sectionright fright clearfix">
  						<img src="/img/website/bb1.png" class="imgpross" style="width:100%;"/>
  					</div>
  				</div>
  			</div>
  		</div>
    	<div id="mainDIV">
    		<div class="confirmpay">    	
				<div class="w1200">
			 		<div class="blockdiv normal confirmdetail">
			 			<div class="header margin40">
			    			<h2 class="h">远程门诊预约</h2>
			    		</div>
			    		<div class="boyder clearfix">
			    			<div class="fleft hasline">
			    				<p>医生信息</p>
			    				<div>
			    					<b>${special.specialName }</b><span>${special.duty }/${special.profession }</span>
			    				</div>
			    				<div>
			    					${special.hosName }&emsp;${special.depName }
			    				</div>
			    			</div>
			    			<div class="fleft hasline">
			    				<p>服务类型</p>
			    				<div>
			    					<b>远程门诊</b>
			    				</div>
			    			</div>
			    			<div class="fleft hasline">
			    				<p>门诊时间</p>
			    				<div style="font-size:18px;">
			    					${apptime.scheduleDate }（${apptime.weekDesc}）&ensp;${apptime.startTime }
			    				</div>
			    			</div>
			    			<div class="fleft">
			    				<p>费用</p>
			    				<div>
			    					<b>￥${special.vedioAmount }</b>
			    				</div>
			    			</div>
			    		</div>
			 		</div>
			 		<div class="blockdiv normal confirmperson">
			 			<div class="header margin40">
			    			<h2 class="h">确认就诊人</h2>
			    		</div>
			    		<div class="boyder personlists clearfix">
			    			<c:forEach items="${contactors}" var="o" varStatus="status">
				    			<div class="personlist fleft">
				    				<div class="innerlist<c:if test="${status.index == 0 }"> selected</c:if>" data-id="${o.id }" data-name="${o.contactName }" data-tel="${o.telphone }">
					    				<h4>${o.contactName }<c:if test="${status.index == 0 }"><span>(默认)</span></c:if></h4>
					    				<div class="perinfo">
					    					<p>联系方式：${o.telphone }</p>
					    					<p>身份证号：${o.idCard }</p>
					    				</div>
				    				</div>
				    			</div>
			    			</c:forEach>
			    			<div class="addpersoninfo fleft">
			    				<div class="innerlist">
				    				<i class="iconfont">&#xe614;</i>
				    				<label>添加其他就诊人</label>
			    				</div>
			    			</div>
			    		</div>
			    		<div class="footer">
			    			<label class="checkbox"><input type="checkbox" checked name="readme"/><span>我已了解并同意以下规则</span></label>
			    			<button class="submittopay h" type="button">提交并付款</button>
			    		</div>
			 		</div>
			 	</div> 	
			</div>
    	</div>
    	<div class="mainfooter h">
    		<ul class="clearfix">
    			<li><img src="/img/website/whitelogo.png" style="width:188px;"/></li>
    			<li>
    				<div>
    					<h2>关于我们</h2>
    					<a target="_blank" href="#app">微信公众号</a>
    					<a target="_blank" href="http://weibo.com/zhuanjiahao1">新浪微博</a>
    					<a target="_blank" href="#home_statichtm/aboutus">关于我们</a>
    					<a target="_blank" href="#app">佰医汇app</a>
    				</div>
    			</li>
    			<li>
    				<div>
    					<h2>帮助</h2>
    					<a href="#home_statichtm/feedback">建议与反馈</a>
    					<a href="#home_statichtm/contactus">联系我们</a>
    					<a href="#home_statichtm/agreement">用户协议</a>
    					<!-- <a href="#home_statichtm/questions">常见问题</a> -->
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
    		<div class="copyrightdiv">
    			<p>
    				<span>版权所有：北京佰医汇医疗信息技术有限公司</span><span>京ICP备15015320号</span>
    			</p>
    			<p>
    				<span>服务声明：专家咨询建议仅供参考_不能作为诊断及医疗的依据</span>    				
    			</p>
    			<p>
    				<a target="_blank" href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=11010802017313"> 
    					京公网安备 11010802017313号
    				</a>
    			</p>
    		</div>
    	</div>
    	<form id="myform">
    		<input type="hidden" name="localHosId" value="${localHosId }"/>
    		<input type="hidden" name="localDepartId" value="${localDepartId }"/>
    		<input type="hidden" name="expertId" value="${special.specialId }" />
			<input type="hidden" name="stimeid" value="${apptime.id }" />
			<input type="hidden" name="contactorid" value="${contactors[0].id}" />
			<input type="hidden" name="uid" value="" />
			<input type="hidden" name="wxurl" value="" />
			<input type="hidden" name="pmoney" value="${special.vedioAmount }" />
			<input type="hidden" name="wxout_trade_no" value="" />
    	</form>
    	<form id="adduserinfo">
    		<div class="globbgs"></div>
    		<div class="globform">
	    		<div class="form-group">
	    			<label>姓名</label>
	    			<input type="text" name="username" placeholder="姓名" />
	    		</div>
	    		<div class="form-group">
	    			<label>手机号</label>
	    			<input type="text" name="tel" placeholder="11位手机号" />
	    		</div>
	    		<div class="form-group">
	    			<label>身份证号</label>
	    			<input type="text" name="idcard" placeholder="身份证号" />
	    		</div>
	    		<div class="form-action clearfix">
	    			<button type="button" class="canceluserinfo h">取消</button>
	    			<button type="button" class="submituserinfo h">保存</button>
	    		</div>
    		</div>
    	</form>
		<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
		<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
		<script src="/sea-modules/seajs/seajs.config.js"></script>
		<script>
			var _href = '/',_h = _href;
			var _$ob = {
				cost:'${special.vedioAmount}',
				mtime:'${apptime.scheduleDate }（${apptime.weekDesc}）&ensp;${apptime.startTime }',
				localHosId:'${localHosId }',
				localDepartId:'${localDepartId }',
				specialId:'${special.specialId }',
				stimeid:'${apptime.id }',
				docName:'${special.specialName }',
				hosName:'${special.hosName } - ${special.depName }',
				hosAdd:'${special.position}'
			};
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
				website.confirmPay();
			});
		</script>
  </body>
</html>
