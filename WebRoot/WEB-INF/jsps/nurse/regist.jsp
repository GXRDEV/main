<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 

<!DOCTYPE html>
<html>
  <head>
  	<base href="/">
	<meta charset="UTF-8">
	<title>护士挂号</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" />
	<link rel="stylesheet" href="/css/view/nurse.css" />
  </head>  
  <body>
  	<div class="mainsection">
  		<div class="sectioncondation thumbnail">
  			<div class="control-group">
  				<label class="control-label">选择科室：</label>
  				<div class="controls">
  					<c:forEach items="${departs }" var="dep" varStatus="depindex">
  						<span data-id="depid:${dep.id }" class="option <c:if test="${depindex.getIndex() == 0}">selected</c:if>">${dep.displayName }</span>
  					</c:forEach>
  				</div>
  			</div>
  			<div class="control-group">
  				<label class="control-label">选择日期：</label>
  				<div class="controls">
  					<script>
  						(function(){
  							var _b = {
	  							formatDate:function(d){
									if(!d) return +new Date();
									d = d.split('-');
									return d[0] + '-' + this.formatHH(d[1]) + '-' + this.formatHH(d[2]);
								},
								formatHH:function(h){
									h = (h || '00').split('');
									h.length < 2 && h.unshift('0');
									return h.join('');
								},
								getWeek:function (week){
									var day;
								    switch (week){
									    case 0:day="星期日";break;
									    case 1:day="星期一";break;
									    case 2:day="星期二";break;
									    case 3:day="星期三";break;
									    case 4:day="星期四";break;
									    case 5:day="星期五";break;
									    case 6:day="星期六";break;
								    }
								   return day;
								},
								creatHtm:function(){
									var today=new Date(),cdate,spans='',cmd;
									for(var i=0;i<=6;i++){
										cdate = new Date();
										cdate.setDate(today.getDate()+i);
										cmd = (cdate.getMonth() + 1) + '-' + cdate.getDate();
										cmd = _b.formatDate(cdate.getFullYear() + '-'+ cmd);
										spans += '<span data-id="sdate:'+ cmd +'" title="'+ cmd +'" class="option'+ (!i ? ' selected' : '') +'">'+ _b.getWeek(cdate.getDay()) + '</span>';								
									}
									return spans;
								} 
  							};
  							document.write(_b.creatHtm());
  						})();
  					</script>
  				</div>
  			</div>
  			<div class="control-group">
  				<label class="control-label">选择时间：</label>
  				<div class="controls">
  					<span data-id="timetype:0" class="option selected">全天</span>
  					<span data-id="timetype:1" class="option">上午</span>
  					<span data-id="timetype:2" class="option">下午</span>
  				</div>
  			</div>
  		</div>
  		<div class="sectionresult">
  			<ul class="thumbnails"></ul>
  		</div>
  	</div>
 	<script src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
	<script src="/sea-modules/bootstrap/js/bootstrap.min.js"></script>
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
	<script>
		var _href = '/';
		var _noresult = '<div class="noresult"><img src="'+ window.location.origin +'/img/mobile/kbzs/icon_noresult@2x.png" style="width:58px" alt=""/></div>';
		seajs.use('view/nurse/main',function(controller){
			controller.init();
		});
	</script>
  </body>
</html>
