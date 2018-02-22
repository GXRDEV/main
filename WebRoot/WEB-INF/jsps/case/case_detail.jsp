<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<!DOCTYPE html>
<html>
  <head>
    <base href="/">
    <title>我的订单</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" type="text/css" href="/css/wx.css" />
    <link rel="stylesheet" href="/weui/weui.min.css"/>
	<link rel="stylesheet" href="/fonticon/tspzmobile/iconfont.css" type="text/css" />
    <link rel="stylesheet" href="/css/view/wxzjh.css"/>
	<script src="/js/jweixin-1.0.0.js"></script>
  </head>  
  <body class="myordersDetailBody">
    <div id="casedetail" class="hashdiv">
    	<div class="mycaseinfos">    
    		<c:if test="${fn:length(caseinfo.contactName) > 0 }">
			 	<div class="blockdiv">
			 		<header><i class="iconfont">&#xe611;</i><span>患者详情</span></header>
			 		<section>
			 			<p>患者姓名：${caseinfo.contactName }</p>
			 			<p>患者电话：${caseinfo.telephone }</p>
			 			<p>患者性别：<c:if test="${caseinfo.sex == '1' }">男</c:if><c:if test="${caseinfo.sex != '1' }">女</c:if></p>
			 			<p>患者年龄：${caseinfo.age }</p>
			 		</section>
			 	</div>
			 	<div class="blockdiv">
				 		<header><i class="iconfont">&#xe616;</i><span>咨询目的</span></header>
				 		<section>
				 			<p>${caseinfo.askProblem}</p>
				 		</section>
				</div>
			 	<div class="blockdiv">
			 		<header><i class="iconfont">&#xe616;</i><span>图片资料</span></header>
			 		<section class="hhlist">
			 			<c:forEach items="${caseimages }" var="imgs">
			 				<c:forEach items="imgs.files" var="fi">
			 					<span class="text thumb">
				 					<img src="${fi.fileUrl }" onclick="javascript:;" alt=""/>
				 				</span>
				 			</c:forEach>
			 			</c:forEach>
			 		</section>
			 	</div>    			
    		</c:if>	
    		 		
    		<c:if test="${fn:length(reminfo.localDocName) > 0 }">    			
			 	<div class="blockdiv">
			 		<header><i class="iconfont">&#xe616;</i><span>预约医生</span></header>
			 		<section>
			 			<p>当地医生：${reminfo.localDocName }</p>
			 			<p>当地医院：${reminfo.localHosName }</p>
			 			<p>当地科室：${reminfo.localDepName }</p>
			 		</section>
			 	</div>    		
    		</c:if>		
    		<c:if test="${fn:length(reminfo.expertName) > 0 }">    			
			 	<div class="blockdiv">
			 		<header><i class="iconfont">&#xe616;</i><span>预约专家</span></header>
			 		<section>
			 			<p>预约专家：${reminfo.expertName }</p>
			 			<p>专家医院：${reminfo.hosName }</p>
			 			<p>专家科室：${reminfo.depName }</p>
			 			<p>专家职称：${special.duty }</p>
			 		</section>
			 	</div>    		
    		</c:if>
    		<c:if test="${!empty caseinfo.caseName }">
    			<div class="blockdiv">
			 		<header><i class="iconfont">&#xe616;</i><span>疾病</span></header>
			 		<section>
			 			<p>${caseinfo.caseName}</p>
			 		</section>
			 	</div>    
    		</c:if>
    		<c:if test="${fn:length(caseinfo.mainSuit) > 0 }">    			
			 	<div class="blockdiv">
			 		<header><i class="iconfont">&#xe616;</i><span>主诉</span></header>
			 		<section>
			 			<p>${caseinfo.mainSuit }</p>
			 		</section>
			 	</div>    		
    		</c:if>
    		<c:if test="${fn:length(caseinfo.presentIll) > 0 }">    			
			 	<div class="blockdiv">
			 		<header><i class="iconfont">&#xe616;</i><span>现病史</span></header>
			 		<section>
			 			<p>${caseinfo.presentIll }</p>
			 		</section>
			 	</div>    		
    		</c:if>
    		<c:if test="${fn:length(caseinfo.historyIll) > 0 }">    			
			 	<div class="blockdiv">
			 		<header><i class="iconfont">&#xe616;</i><span>既往史</span></header>
			 		<section>
			 			<p>${caseinfo.historyIll }</p>
			 		</section>
			 	</div>    		
    		</c:if>
    		<c:if test="${fn:length(caseinfo.examined) > 0 }">    			
			 	<div class="blockdiv">
			 		<header><i class="iconfont">&#xe616;</i><span>体检</span></header>
			 		<section>
			 			<p>${caseinfo.examined }</p>
			 		</section>
			 	</div>    		
    		</c:if>
    		<c:if test="${fn:length(caseinfo.assistantResult) > 0 }">    			
			 	<div class="blockdiv">
			 		<header><i class="iconfont">&#xe616;</i><span>辅检</span></header>
			 		<section>
			 			<p>${caseinfo.assistantResult }</p>
			 		</section>
			 	</div>    		
    		</c:if>
    		<c:if test="${fn:length(caseinfo.initialDiagnosis) > 0 }">    			
			 	<div class="blockdiv">
			 		<header><i class="iconfont">&#xe616;</i><span>诊断</span></header>
			 		<section>
			 			<p>${caseinfo.initialDiagnosis }</p>
			 		</section>
			 	</div>    		
    		</c:if>
    		<c:if test="${fn:length(caseinfo.treatAdvice) > 0 }">    			
			 	<div class="blockdiv">
			 		<header><i class="iconfont">&#xe616;</i><span>治疗意见</span></header>
			 		<section>
			 			<p>${caseinfo.treatAdvice }</p>
			 		</section>
			 	</div>
    		</c:if>
    		<div class="blockdiv" id="reports" style="display:none;">
		 		<header><i class="iconfont">&#xe616;</i><span>检查报告单</span></header>
		 		<section class="reportlists"></section>
		 	</div>
    		<div class="blockdiv" id="pacsimg" style="display:none;">
		 		<header><i class="iconfont">&#xe616;</i><span>影像图片</span></header>
		 		<section class="pacsimgs"></section>
		 	</div>
    		<div class="blockdiv" id="localimg" style="display:none;">
		 		<header><i class="iconfont">&#xe616;</i><span>本地资源</span></header>
		 		<section class="hhlist"></section>
		 	</div>
    		<div class="blockdiv" id="localreplay">
		 		<header><i class="iconfont">&#xe616;</i><span>专家回复</span></header>
		 		<section class="hhlist" style="display:none;"></section>
		 		<c:if test="${otype==1}">
			 		<footer style="padding:0 0 5px;">
			 			<div class="weui_cells weui-cells_form" style="margin-top:0;">
				            <div class="weui_cell">
				                <div class="weui_cell__bd">
				                    <textarea class="weui_textarea" placeholder="请输入回复内容" rows="3"></textarea>
				                </div>
				            </div>
				        </div>
				        <div style="margin: 10px;">
				        	<button type="button" class="weui_btn weui_btn_plain_primary">专家回复</button>
				        </div>		 			
			 		</footer>
		 		</c:if>
		 	</div>
		</div>
    </div>
	<div id="loadingToast" class="weui_loading_toast" style="display:none;">
	    <div class="weui_mask_transparent"></div>
	    <div class="weui_toast">
	        <div class="weui_loading">
	            <div class="weui_loading_leaf weui_loading_leaf_0"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_1"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_2"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_3"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_4"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_5"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_6"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_7"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_8"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_9"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_10"></div>
	            <div class="weui_loading_leaf weui_loading_leaf_11"></div>
	        </div>
	        <p class="weui_toast_content">数据处理中</p>
	    </div>
	</div>
	<script type="text/javascript">
		var _href = '/',_h = _href,
			_config = {
				oid:'${record.orderId}' || '${oid}',
				param: '${paramval}'
			};
	</script>
	<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
    <script src="/js/base.js"></script>
	<script>
		seajs.use('view/case/caseshare',function(aa){
			aa.twtelremt();
		});
	</script>
	<script>
		var name = '${caseinfo.contactName }' || '${reminfo.patientName}';
		var sex = '<c:if test="${caseinfo.sex == '1' }">男</c:if><c:if test="${caseinfo.sex != '1' }">女</c:if>' || '<c:if test="${reminfo.sex == '1' }">男</c:if><c:if test="${reminfo.sex != '1' }">女</c:if>';
		var age = '${caseinfo.age }';
		var des = '${caseinfo.description }' || '${record.mainSuit }' || '${record.presentIll }' || '${record.examined }' || '${record.treatAdvice }';
	</script>
	<script>
        var lineLink = location.href;
        var shareTitle = name + '病例详情';
        var descContent = sex + (sex ? '，' : '') + age + (age ? '，' : '') + des;
	  	var ops={
        	title: shareTitle,
		    desc: descContent,
		    link: lineLink,
		    imgUrl: 'https://develop.ebaiyihui.com:443/img/defdoc.jpg'
        };
		wx.config({
	      appId: '${appid }' || 'wxf8b4f85f3a794e77',
	      timestamp: (+'${timestamp }') || 1473048487,
	      nonceStr: '${nonceStr }' || 'b6o4kKZxce1FbPN6',
	      signature: '${signature }' || '030c3333f2de6f450021d971b163c51efe21d51e',
	      jsApiList: [
	         'previewImage','onMenuShareAppMessage','onMenuShareTimeline','showAllNonBaseMenuItem'
	      ]
	  	});
		wx.ready(function(){
        	wx.onMenuShareAppMessage(ops);
			wx.onMenuShareTimeline(ops);
			wx.showAllNonBaseMenuItem();
		});	
	</script>
  </body>
</html>
