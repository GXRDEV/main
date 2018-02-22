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
	<script src="/sea-modules/jstpl/nodetpl.min.js"></script>
	<script src="/js/jweixin-1.0.0.js"></script>
  </head>  
  <body class="myordersDetailBody">
    <div id="orderdetail" class="hashdiv"></div>
    <div id="casedetail" class="hashdiv"></div>
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
	        <p class="weui_toast_content">数据加载中</p>
	    </div>
	</div>
	<div class="weui_dialog_confirm" id="dialog1" style="display:none;">
	    <div class="weui_mask"></div>
	    <div class="weui_dialog">
	        <div class="weui_dialog_hd"><strong class="weui_dialog_title">弹窗标题</strong></div>
	        <div class="weui_dialog_bd">自定义弹窗内容，居左对齐显示，告知需要确认的信息等</div>
	        <div class="weui_dialog_ft">
	            <a href="javascript:;" class="weui_btn_dialog default">取消</a>
	            <a href="javascript:;" class="weui_btn_dialog primary">确定</a>
	        </div>
	    </div>
	</div>
	<script type="text/javascript">
		var _href = '/',_h = _href,
			_config = {
				openid:'${openid}',oid:'${oid}',flag:'${flag}',type:'${type}',iseval:'${iseval}'
			};
	</script>
	<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
    <script src="/js/base.js"></script>
	<script>
		seajs.use('view/wx/twtelupload',function(aa){
			nodetpl.config({
			  base: _href + 'html/wx/',
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
			aa.myorderdetail();
		});
		wx.config({
	      appId: 'wxf8b4f85f3a794e77',
	      timestamp: 1473048487,
	      nonceStr: 'b6o4kKZxce1FbPN6',
	      signature: '030c3333f2de6f450021d971b163c51efe21d51e',
	      jsApiList: [
	         'previewImage' 
	      ]
	  	});
	</script>
  </body>
</html>
