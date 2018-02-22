<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Doctor Admin</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="/css/bootstrap.min.css" />
    <link rel="stylesheet" href="/css/bootstrap-responsive.min.css" />
    <link rel="stylesheet" href="/css/matrix-style.css" />
    <link rel="stylesheet" href="/css/matrix-media.css" />
    <link href="/font-awesome/css/font-awesome.css" rel="stylesheet" />
</head>
<body>
    <!--Header-part-->
    <div id="header">
      <h1><a href="dashboard.html">管理员操作系统</a></h1>
    </div>
    <!--close-Header-part--> 

    <!--top-Header-menu-->
    <div id="user-nav" class="navbar navbar-inverse">
        <ul class="nav">
            <li  class="dropdown" id="profile-messages" >
                <a title="" href="#" data-toggle="dropdown" data-target="#profile-messages" class="dropdown-toggle">
                    <i class="icon icon-user"></i>&nbsp;
                    <span class="text">欢迎你，${user.displayName} </span>&nbsp;
                    <b class="caret"></b>
                </a>
                <ul class="dropdown-menu">
                    <!-- <li><a href="javascript:void(0)" class="showinfo"><i class="icon-user"></i> 个人资料</a></li>
                    <li class="divider"></li> -->
                    <li><a href="javascript:void(0)" class="changepsd"><i class="icon-check"></i> 修改密码</a></li>
                    <li class="divider"></li>
                    <li><a href="javascript:void(0);" class="logout"><i class="icon-key"></i> 退出系统</a></li>
                </ul>
            </li>
            <li class="dropdown" id="menu-messages">
                <a href="#" data-toggle="dropdown" data-target="#menu-messages" class="dropdown-toggle">
                    <i class="icon icon-envelope"></i>&nbsp;
                    <span class="text">我的消息</span>&nbsp;
                    <span class="label label-important">0</span>&nbsp; 
                    <b class="caret"></b>
                </a>
                <ul class="dropdown-menu">  
                </ul>
            </li>
            <li class=""><a title="" href="#"><i class="icon icon-cog"></i> <span class="text">&nbsp;设置</span></a></li>
            <li class=""><a title="" href="javascript:void(0);" class="logout"><i class="icon icon-share-alt"></i> <span class="text">&nbsp;退出系统</span></a></li>
        </ul>
    </div>
    <!--close-top-Header-menu-->

    <!--sidebar-menu-->
    <div id="sidebar" style="overflow: auto">
        <ul>
            <li class="submenu active">
                <a class="menu_a" class="active" href="/system/main"><i class="icon icon-home"></i> <span>首页</span></a>
            </li>
            <li class="submenu">
            	<a href="#">
                    <i class="icon icon-th"></i> 
                    <span>订单管理</span> 
                </a>
                <ul>
                    <li class="submenu">
		            	<a href="#">
		                    <i class="icon icon-cog"></i> 
		                    <span>医生TO患者订单管理</span> 
		                </a>
		                <ul>
		                	<li ><a class="menu_a" href="/system/d2ptuwens" target="iframe-main"><i class="icon icon-caret-right"></i>图文咨询订单管理</a></li>
		                    <li ><a class="menu_a" href="/system/d2ptels" target="iframe-main"><i class="icon icon-caret-right"></i>电话咨询订单管理</a></li>
                   	 		<li ><a class="menu_a" href="/system/d2pfastasks" target="iframe-main"><i class="icon icon-caret-right"></i>快速问诊订单管理</a></li>
                   	 		<li ><a class="menu_a" href="/system/d2preports" target="iframe-main"><i class="icon icon-caret-right"></i>患者报道管理</a></li>
		                </ul>
		            </li>
		            <li class="submenu">
		            	<a href="#">
		                    <i class="icon icon-cog"></i> 
		                    <span>医生团队TO患者订单管理</span> 
		                </a>
		                <ul>
                   	 		<li ><a class="menu_a" href="/system/t2ptuwens" target="iframe-main"><i class="icon icon-caret-right"></i>团队咨询订单管理</a></li>
                   	 		<li ><a class="menu_a" href="/system/d2pconreqs" target="iframe-main"><i class="icon icon-caret-right"></i>患者会诊需求申请单管理</a></li>
		                </ul>
		            </li>
		            <li class="submenu">
		            	<a href="#">
		                    <i class="icon icon-cog"></i> 
		                    <span>医生TO医生订单管理</span> 
		                </a>
		                <ul>
                   	 		<li ><a class="menu_a" href="/system/dtdtuwenordermanage" target="iframe-main"><i class="icon icon-caret-right"></i>图文会诊订单</a></li>
                   	 		<li ><a class="menu_a" href="/system/vedioordermanage" target="iframe-main"><i class="icon icon-caret-right"></i>视频会诊订单</a></li>
                   	 		<li ><a class="menu_a" href="/system/d2dreferrals" target="iframe-main"><i class="icon icon-caret-right"></i>预约转诊订单</a></li>
		                </ul>
		            </li>
		           <%--  <li class="submenu">
		            	<a href="#">
		                    <i class="icon icon-cog"></i> 
		                    <span>专家TO医生订单管理</span> 
		                </a>
		                <ul>
                   	 		<li ><a class="menu_a" href="/system/exadvices" target="iframe-main"><i class="icon icon-caret-right"></i>专家咨询订单</a></li>
                   	 		<li ><a class="menu_a" href="/system/helporders" target="iframe-main"><i class="icon icon-caret-right"></i>视频会诊订单</a></li>
		                </ul>
		            </li> --%>
		            <li class="submenu">
		            	<a href="#">
		                    <i class="icon icon-cog"></i> 
		                    <span>专家TO患者订单管理</span> 
		                </a>
		                <ul>
                   	 		<li ><a class="menu_a" href="/system/tuwenmanage" target="iframe-main"><i class="icon icon-caret-right"></i>图文会诊订单</a></li>
                   	 		<li ><a class="menu_a" href="/system/telorders" target="iframe-main"><i class="icon icon-caret-right"></i>电话咨询订单</a></li>
		                </ul>
		            </li>
                </ul>
            </li>
       		<li class="submenu">
				 <a class="menu_a" class="active" href="/system/hospitals"><i class="icon icon-caret-right"></i> <span>医院管理</span></a>
            </li>
            <li class="submenu">
				 <a class="menu_a" class="active" href="/system/experts"><i class="icon icon-caret-right"></i> <span>专家管理</span></a>
            </li>
            <li class="submenu">
				<a href="#">
                    <i class="icon icon-th"></i> 
                    <span>医生管理</span> 
                </a>
				<ul>
                    <li ><a class="menu_a" href="/system/docaudit" target="iframe-main"><i class="icon icon-caret-right"></i>医生认证审核</a></li>
                    <li ><a class="menu_a" href="/system/localdocs" target="iframe-main"><i class="icon icon-caret-right"></i>医生管理</a></li>
                    <li ><a class="menu_a" href="/system/docsorderlist" target="iframe-main"><i class="icon icon-caret-right"></i>医生订单统计</a></li>
                </ul>            
             </li>
             <li class="submenu">
				<a href="#">
                    <i class="icon icon-th"></i> 
                    <span>医生团队管理</span> 
                </a>
				<ul>
                    <li ><a class="menu_a" href="/system/doctorteamaudit" target="iframe-main"><i class="icon icon-caret-right"></i>医生团队审核</a></li>
                    <li ><a class="menu_a" href="/system/doctorteams" target="iframe-main"><i class="icon icon-caret-right"></i>医生团队管理</a></li>
                </ul>            
             </li>
             <li class="submenu">
				<a href="#">
                    <i class="icon icon-th"></i> 
                    <span>账户管理</span> 
                </a>
				<ul>
                    <li ><a class="menu_a" href="/system/doctorwithdraws" target="iframe-main"><i class="icon icon-caret-right"></i>医生提现审核</a></li>
                    <li ><a class="menu_a" href="/system/doctorbills" target="iframe-main"><i class="icon icon-caret-right"></i>医生账单查询</a></li>
                	<li ><a class="menu_a" href="/system/docImcome" target="iframe-main"><i class="icon icon-caret-right"></i>医生线下入账</a></li>
                	
                </ul>            
             </li>
             <li class="submenu">
				<a href="#">
                    <i class="icon icon-th"></i> 
                    <span>轮播图配置管理</span> 
                </a>
				<ul>
                    <li ><a class="menu_a" href="/system/figureconfigs" target="iframe-main"><i class="icon icon-caret-right"></i>首页轮播图配置管理</a></li>
                </ul>            
             </li>
             <li class="submenu">
				<a href="#">
                    <i class="icon icon-th"></i> 
                    <span>统计功能</span> 
                </a>
				<ul>
                    <li ><a class="menu_a" href="/system/exlogincal" target="iframe-main"><i class="icon icon-caret-right"></i>专家登陆统计</a></li>
                    <li ><a class="menu_a" href="/system/doctorlogincal" target="iframe-main"><i class="icon icon-caret-right"></i>医生登陆统计</a></li>
                    <li ><a class="menu_a" href="/system/doctorAbout" target="iframe-main"><i class="icon icon-caret-right"></i>医生关注量等统计</a></li>
                    <li ><a class="menu_a" href="/system/dayNewAdd" target="iframe-main"><i class="icon icon-caret-right"></i>新增患者/医生统计</a></li>
                    <li ><a class="menu_a" href="/system/exportinfo" target="iframe-main"><i class="icon icon-caret-right"></i>统计导出</a></li>
                    <li><a class="menu_a" href="/system/docFollows" target="iframe-main"><i class="icon icon-caret-right"></i>医生随访管理</a></li>
                    <li ><a class="menu_a" href="/system/warmsinfo" target="iframe-main"><i class="icon icon-caret-right"></i>送心意统计</a></li>
                </ul>                    
             </li>
             <li class="submenu">
				<a href="#">
                    <i class="icon icon-th"></i> 
                    <span>群组管理</span> 
                </a>
				<ul>
                    <li><a class="menu_a" href="/system/hosgroups" target="iframe-main"><i class="icon icon-caret-right"></i>医院群组管理</a></li>
                    <li><a class="menu_a" href="/system/citygroups" target="iframe-main"><i class="icon icon-caret-right"></i>同市区同科室群组管理</a></li>
                    <li><a class="menu_a" href="/system/docteams" target="iframe-main"><i class="icon icon-caret-right"></i>医生团队群组管理</a></li>
                </ul>            
             </li>
             <li class="submenu">
				<a href="#">
                    <i class="icon icon-th"></i> 
                    <span>医联体管理</span> 
                </a>
				<ul>
                    <li ><a class="menu_a" href="/system/hoshealthaudit" target="iframe-main"><i class="icon icon-caret-right"></i>医联体审核</a></li>
                    <li ><a class="menu_a" href="/system/hoshealthmanage" target="iframe-main"><i class="icon icon-caret-right"></i>医联体管理</a></li>
                </ul>            
             </li>
             <li class="submenu">
				<a href="#">
                    <i class="icon icon-th"></i> 
                    <span>运营人员管理</span> 
                </a>
				<ul>
                    <li ><a class="menu_a" href="/system/operatormanage" target="iframe-main"><i class="icon icon-caret-right"></i>运营人员管理</a></li>
                </ul>            
             </li>
             <!-- <li class="submenu">
				<a href="#">
                    <i class="icon icon-th"></i> 
                    <span>会诊案例管理</span> 
                </a>
				<ul>
                    <li ><a class="menu_a" href="/system/sysConCaseManage" target="iframe-main"><i class="icon icon-caret-right"></i>会诊案例管理</a></li>
                </ul>            
             </li> -->
             <li class="submenu">
				<a href="#">
                    <i class="icon icon-th"></i> 
                    <span>患者签约管理</span> 
                </a>
				<ul>
                    <li ><a class="menu_a" href="/system/privateDoctor" target="iframe-main"><i class="icon icon-caret-right"></i>对医生的签约</a></li>
                    <li ><a class="menu_a" href="/system/privateDoctorTeam" target="iframe-main"><i class="icon icon-caret-right"></i>对团队的签约</a></li>
                </ul>            
             </li>
             <li class="submenu">
				<a href="#">
                    <i class="icon icon-th"></i> 
                    <span>服务管理</span> 
                </a>
				<ul>
                    <li ><a class="menu_a" href="/system/serviceinfo" target="iframe-main"><i class="icon icon-caret-right"></i>服务信息</a></li>
                </ul>            
             </li>
             <li class="submenu">
				<a href="#">
                    <i class="icon icon-th"></i> 
                    <span>短信验证码</span> 
                </a>
				<ul>
                    <li ><a class="menu_a" href="/system/smsinfo" target="iframe-main"><i class="icon icon-caret-right"></i>短信验证码</a></li>
                </ul>            
             </li>
             <li class="submenu">
				 <a class="menu_a" class="active" href="/xinyi/receiveorderlist"><i class="icon icon-caret-right"></i> <span>中日友好医院</span></a>				    
             </li>
             <li class="submenu">
				<a href="#">
                    <i class="icon icon-th"></i> 
                    <span>会诊案例管理</span> 
                </a>
				<ul>
                    <li ><a class="menu_a" href="/system/vediolist" target="iframe-main"><i class="icon icon-caret-right"></i>直播管理</a></li>
                    <li ><a class="menu_a" href="/system/hiscase/manage" target="iframe-main"><i class="icon icon-caret-right"></i>回顾管理</a></li>
                </ul>            
             </li>
        </ul>
    </div>
    
    <script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
    <script src="/js/excanvas.min.js"></script>
    <script src="/js/jquery.min.js"></script>
    <script src="/js/jquery.ui.custom.js"></script>
    <script src="/js/bootstrap.min.js"></script>
    <script src="/js/nicescroll/jquery.nicescroll.min.js"></script>
    <script src="/js/matrix.js"></script>
    <script type="text/javascript">
    	var _burl='/';
    </script>
     <script src="/js/matrix.login.js"></script>
    <script type="text/javascript">
    $(function(){
        init();
        $(window).resize(function(){
            init();
        });
    });

    // This function is called from the pop-up menus to transfer to
    // a different page. Ignore if the value returned is a null string:
    function goPage (newURL) {
        // if url is empty, skip the menu dividers and reset the menu selection to default
        if (newURL != "") {
            // if url is "-", it is this page -- reset the menu:
            if (newURL == "-" ) {
                resetMenu();            
            } 
            // else, send page to designated URL            
            else {  
                document.location.href = newURL;
            }
        }
    }

    // resets the menu selection upon entry to this page:
    function resetMenu() {
        document.gomenu.selector.selectedIndex = 2;
    }

    // uniform使用示例：
    // $.uniform.update($(this).attr("checked", true));
    </script>
</body>
</html>
