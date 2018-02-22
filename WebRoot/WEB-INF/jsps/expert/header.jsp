<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>专家管理系统</title>
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
      <h1><a href="dashboard.html">专家管理系统</a></h1>
    </div>
    <!--close-Header-part--> 

    <!--top-Header-menu-->
    <div id="user-nav" class="navbar navbar-inverse">
        <ul class="nav">
            <li  class="dropdown" id="profile-messages" >
                <a title="" href="#" data-toggle="dropdown" data-target="#profile-messages" class="dropdown-toggle">
                    <i class="icon icon-user"></i>&nbsp;
                    <span class="text">欢迎你，${userDetail.specialName}</span>&nbsp;
                    <b class="caret"></b>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="javascript:void(0)" class="showinfo"><i class="icon-user"></i> 个人资料</a></li>
                    <li class="divider"></li>
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
                    <!-- <li><a class="sAdd" title="" href="#"><i class="icon-plus"></i> 新消息</a></li>
                    <li class="divider"></li>
                    <li><a class="sInbox" title="" href="#"><i class="icon-envelope"></i> 收件箱</a></li>
                    <li class="divider"></li>
                    <li><a class="sOutbox" title="" href="#"><i class="icon-arrow-up"></i> 发件箱</a></li>
                    <li class="divider"></li>
                    <li><a class="sTrash" title="" href="#"><i class="icon-trash"></i> 回收站</a></li> -->
                </ul>
            </li>
            <li class=""><a title="" href="#"><i class="icon icon-cog"></i> <span class="text">&nbsp;设置</span></a></li>
            <li class=""><a title="" href="javascript:void(0);" class="logout"><i class="icon icon-share-alt"></i> <span class="text">&nbsp;退出系统</span></a></li>
        </ul>
    </div>
    <!--close-top-Header-menu-->

    <!--start-top-serch
    <div id="search">
        <input type="text" placeholder="搜索..."/>
        <button type="submit" class="tip-bottom" title="Search"><i class="icon-search icon-white"></i></button>
    </div>-->
    <!--close-top-serch-->

    <!--sidebar-menu-->
    <div id="sidebar">
        <ul>
            <%-- <li class="submenu">
                <a class="menu_a" link="/index2.html"><i class="icon icon-home"></i> <span>控制面板</span></a>
            </li> --%>
			<li class="submenu open">
				<a href="#">
                    <i class="icon icon-th"></i> 
                    <span>我的订单</span> 
                    <span class="label label-important">6</span>
                </a>
				<ul>
                    <li class="active"><a class="menu_a" href="/expert/experhz" target="iframe-main"><i class="icon icon-caret-right"></i>远程门诊</a></li>
                   <li><a class="menu_a" href="/expert/graphics" target="iframe-main"><i class="icon icon-caret-right"></i>图文问诊</a></li>
                   <li><a class="menu_a" href="/expert/telorders" target="iframe-main"><i class="icon icon-caret-right"></i>电话问诊</a></li>
                   <li><a class="menu_a" href="/expert/showadviceorders" target="iframe-main"><i class="icon icon-caret-right"></i>专家咨询</a></li>
                   
                     <%-- <li><a class="menu_a" href="/doctor/graphics" target="iframe-main"><i class="icon icon-caret-right"></i>加急电话</a></li>
                    <li><a class="menu_a" href="/doctor/graphics" target="iframe-main"><i class="icon icon-caret-right"></i>电话问诊</a></li>
					<li><a class="menu_a" href="/doctor/graphics" target="iframe-main"><i class="icon icon-caret-right"></i>邀请手术</a></li>
					<li><a class="menu_a" href="/doctor/graphics" target="iframe-main"><i class="icon icon-caret-right"></i>专家会诊</a></li> --%>
                </ul>
            </li>
            <li class="submenu">
                <a class="menu_a" href="//expert/serverlist"><i class="icon icon-home"></i> <span>我的服务</span></a>
            </li>
          <%-- 
            <li class="submenu"> 
                <a href="#">
                    <i class="icon icon-stop"></i> 
                    <span>我的下级医生</span> 
                    <span class="label label-important">5</span>
                </a>
                <ul>
                    <li><a class="menu_a" link="gallery.html"><i class="icon icon-caret-right"></i>医生管理</a></li>
                    <li><a class="menu_a" link="calendar.html"><i class="icon icon-caret-right"></i>日历</a></li>
                    <li><a class="menu_a" link="invoice.html"><i class="icon icon-caret-right"></i>清单示例</a></li>
                    <li><a class="menu_a" link="chat.html"><i class="icon icon-caret-right"></i>聊天</a></li>
                </ul>
            </li>--%>
            <li class="submenu"> 
                <a href="#">
                    <i class="icon icon-info-sign"></i> 
                    <span>我的设置</span> 
                    <span class="label label-important">1</span>
                </a>
                <ul>
                    <li><a class="menu_a" href="/expert/remotetimeset" target= "iframe-main"><i class="icon icon-caret-right"></i>远程会诊排班</a></li>
                </ul>
            </li> 
            <li class="submenu"> 
                <a href="#">
                    <i class="icon icon-table"></i> 
                    <span>辅助工具</span> 
                    <span class="label label-important">1</span>
                </a>
                <ul>
                    <li><a class="menu_a" href="/deviceTest/index.jsp" target="iframe-main"><i class="icon icon-caret-right"></i>设备测试</a></li>
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
