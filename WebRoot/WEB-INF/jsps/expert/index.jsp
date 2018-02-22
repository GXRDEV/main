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
</head>
<body>
  	<jsp:include page="header.jsp" />
  	
    <!--main-container-part-->
    <div id="content">
        <!--breadcrumbs-->
        <div id="content-header">
			<div id="breadcrumb"> 
          		<a href="index.html" target="iframe-main" title="Go to Home" class="tip-bottom">
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
        var outsideGroup = 'expert_' + '${sessionScope.user.id}';
      	goEasy.subscribe({
            channel: outsideGroup,
            onMessage: function(message){                           
            	//console.log(JSON.stringify(message));
            	var content = message.content.replace(/&quot;/g,"\"");
            	var jmessage = JSON.parse(content);  
            	var _type    =  jmessage.type;
            	var _from    =  jmessage.from,
            		_orderId =  jmessage.orderId,
            		_sponsor =  jmessage.sponsor,
            		_hosName =  jmessage.hosName,
            		_depName =  jmessage.depName;
            	
            	console.log('jmessage', JSON.stringify(jmessage, null, '\t'));
            	//医生进入或推出准备就绪状态后的通知  
            	if(_type == 'progressNotify'){
            	 	var  _progress =  jmessage.progress || '';            	 	
            	 	getOrderids(_orderId,_sponsor,_hosName,_depName,_progress);
            	} 
            	else if (_type =='clearNotify' ){
           	    	removeTipforV(_orderId,true);
            	}
            }
        });
        function getOrderids(oid,uname,hosName,depName,pro){
        	$.get(_bse + 'doctor/isUserhasOrder',{userId:'${sessionScope.user.id}',utype:'2',orderId:oid,progress:pro},function(d){
        		d.result && (pro == '4' ? showTipforV(oid,uname,hosName,depName) : removeTipforV(oid,false));
        	});
        }
        
        function showTipforV(id,name,hosName,depName){
        	var message = $('#menu-messages'),
        		numLab = message.find('.label-important'),
        		list = message.find('.dropdown-menu'),localAudio = document.getElementById("localAudio"),
        		content = hosName+depName+name + '大夫已准备就绪，您可以发起视频会诊了，点击前往。'
        		msg = '<li onclick="removeTipforV('+ id +',false)" id="'+ id +'"><a class="sAdd" title="" target="iframe-main" href="'+ _bse +'doctor/orderdetail/'+ id +'/2"><i class="icon-plus"></i>'+ content+'</a></li>';
			
       		if(id == window['_sid']){
       			document.getElementById('iframe-main').contentWindow.location.reload(true);
       			return false;
       		}
       		if('exportlist' == window['_sid']){
       			document.getElementById('iframe-main').contentWindow.location.reload(true);
       		}
       		localAudio.src = _bse + 'audio/global.wav';
        	list.prepend(msg);
        	notification({
        		title:'就诊通知',
        		txt:content,
        		url:_bse +'doctor/orderdetail/'+ id +'/2'
        	});
        	numLab.text(list.find('li').size()).css('display','inline-block');
        	message.addClass('open');
        	numLab.addClass('animate1');
        }
        function removeTipforV(id,bol){
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
        	if(bol) return false;     	
       		if(id == window['_sid'] || 'exportlist' == window['_sid']){
       			document.getElementById('iframe-main').contentWindow.location.reload(true);
       			return false;
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
