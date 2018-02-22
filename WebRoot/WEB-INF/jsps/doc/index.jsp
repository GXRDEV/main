<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>"佰医汇"云SaaS平台</title>
    <meta charset="UTF-8" />
	<jsp:include page="../icon.jsp" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link href="/fonticon/tspzlogin/iconfont.css" rel="stylesheet" />
    <script type="text/javascript">
    	window['_sid'] = '';
    </script>
</head>
<body>
  	<jsp:include page="header.jsp" />
  	
    <!--main-container-part-->
    <div id="content">
        <!--breadcrumbs-->
        <div id="content-header">
          <div id="breadcrumb"> 
          	<a href="index.html" target="iframe-main" class="tip-bottom">
          		<i class="icon-home"></i> 首页 <span id="breadcrumb_span"></span>          	
          	</a>
          	<button class="fullscreenBtn" title="最大化"><i class="iconfont">&#xe604;</i></button>
          </div>
        </div>
        <!--End-breadcrumbs-->
        <iframe id="iframe-main" name="iframe-main" style="width:100%;" frameborder="no" border="0" allowscriptaccess="always" marginwidth="0" marginheight="0" allowfullscreen="true" wmode="opaque" allowTransparency="true" type="application/x-shockwave-flash"></iframe>
    </div>
    <audio id="localAudio" autoplay style="display:none;"></audio>
    <!--end-main-container-part-->
	<script type="text/javascript" src="https://cdn.goeasy.io/goeasy.js"></script>
	<script type="text/javascript">    
        var _bse = '/';
     	var goEasy = new GoEasy({
           	appkey: '78028e7e-edcc-4524-b56b-45639785a53a'
        });
        var outsideGroup = 'doctor_' + '${sessionScope.user.id}';
        console.log('outsideGroup:' + outsideGroup);
      	goEasy.subscribe({
            channel: outsideGroup,
            onMessage: function(message){                           
            	//console.log(JSON.stringify(message));
            	var content = message.content.replace(/&quot;/g,"\"");
            	var jmessage = JSON.parse(content);  
            	var _type    =  jmessage.type;
	            var _from    =  jmessage.from,
	            	_result  =  jmessage.result || '';
            		_orderId =  jmessage.orderId,
            		_sponsor =  jmessage.sponsor,
            		_hosName =  jmessage.hosName,
            		_depName =  jmessage.depName;
            	  
            	 console.log('jmessage', JSON.stringify(jmessage, null, '\t'));
            	 //就绪通知
            	 if(_type == 'launchNotify'){
         	    		
           	    	var content = _hosName + _depName + _sponsor + '专家正发起视频，点击进入。'
           	    	getOrderids(_orderId,_sponsor,content,_orderId,4);
           	    	           	 	
            	 //退出通知
            	 } else if (_type =='cancelNotify' ){
            	 	var  _hasuser =  jmessage.hasuser;
           	    	if(_hasuser == 0){
           	    		removeTipforV(_orderId);
           	    	}
            	 //清除通知
            	 } else if (_type =='clearNotify' ){
           	    	removeTipforV(_orderId);
            	 //报告通知
            	 } else if (_type =='reportNotify' ){
            	 	var content = '收到来自' + _hosName + _depName + _sponsor + '专家的就诊报告，点击查看。'
            	 	getOrderids(_orderId,_sponsor,content,_orderId,4);            	 	
            	 }
            }
        });
        function getOrderids(oid,uname,tpe,oid,progress){
        	$.get(_bse + 'doctor/isUserhasOrder',{userId:'${sessionScope.user.id}',utype:'1',orderId:oid,progress:progress},function(d){
        		//$.inArray(oid.toString(),d.split(',')) != -1 && showTipforV(oid,uname,tpe);
        		d.result && (progress == '4' ? showTipforV(oid,uname,tpe) : removeTipforV(oid));
        	});
        }
        function showTipforV(id,name,tpe){
        	var message = $('#menu-messages'),
        		numLab = message.find('.label-important'),
        		list = message.find('.dropdown-menu'),localAudio = document.getElementById("localAudio"),
        		msg = '<li onclick="removeTipforV('+ id +')" id="'+ id +'"><a class="sAdd" title="" target="iframe-main" href="'+ _bse +'doctor/orderdetail/'+ id +'/1"><i class="icon-plus"></i>'+ tpe+'</a></li>';

       		if(id == window['_sid']){
       			return false;
       		}
       		localAudio.src = _bse + 'audio/global.wav';
        	list.prepend(msg);
        	notification({
        		title:'新通知',
        		txt:tpe,
        		url:_bse +'doctor/orderdetail/'+ id +'/1'
        	});
        	numLab.text(list.find('li').size()).css('display','inline-block');
        	message.addClass('open');
        	numLab.addClass('animate1');
        }
        function removeTipforV(id){
        	var message = $('#menu-messages'),
        		numLab = message.find('.label-important'),
        		list = message.find('.dropdown-menu');

        	list.find('#' + id).remove();
        	numLab.text(list.find('li').size());
        	if(list.find('li').size() < 1){
        		numLab.css('display','none');
        		message.removeClass('open');
        		numLab.removeClass('animate1');
        	}
        }
        function notification(opt){
	        if (window.Notification) {
			    var popNotice = function() {
			        if (Notification.permission == "granted") {
			            var notification = new Notification(opt.title, {
			                body: opt.txt,
			                icon: _bse + 'img/defdoc.jpg'
			            });			            
			            notification.onclick = function() {
			            	opt.url && $('#iframe-main').attr('src',opt.url);
			                notification.close();
			            };
			        }    
			    };
		        if (Notification.permission == "granted") {
		            popNotice();
		        } else if (Notification.permission != "denied") {
		            Notification.requestPermission(function (permission) {
		              popNotice();
		            });
		        }
			} else {
			    console.log('浏览器不支持Notification');    
			}
        }
	</script>
</body>
</html>
