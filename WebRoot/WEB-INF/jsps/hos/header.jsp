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
      <h1 style="width:240px;"><a href="dashboard.html" style="width:240px;">医院管理系统<span style="font-size:0.8em;opacity:.8">（ID:${userDetail.specialId}）</span></a></h1>
    </div>
    <!--close-Header-part--> 

    <!--top-Header-menu-->
    <div id="user-nav" class="navbar navbar-inverse">
        <ul class="nav">
            <li  class="dropdown" id="profile-messages" >
                <a title="" href="#" data-toggle="dropdown" data-target="#profile-messages" class="dropdown-toggle">
                    <i class="icon icon-user"></i>&nbsp;
                    <span class="text">欢迎你，${userDetail.specialName} </span>&nbsp;
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
                    <!-- <span class="label label-important">0</span>&nbsp;  -->
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
    <div id="sidebar">
        <ul>
             <li class="submenu active">
                <a class="menu_a" href="/hospital/main"><i class="icon icon-home"></i> <span>统计</span></a>
            </li>
            <li class="submenu open">
				<a href="#">
                    <i class="icon icon-th"></i> 
                    <span>订单管理</span> 
                </a>
				<ul>
                    <li><a class="menu_a" href="/hospital/vedioOrderManage" target="iframe-main"><i class="icon icon-caret-right"></i>视频会诊</a></li>
                    <li><a class="menu_a" href="/hospital/tuwenOrderManage" target="iframe-main"><i class="icon icon-caret-right"></i>图文会诊</a></li>
                    <li><a class="menu_a" href="/hospital/referralOrderManage" target="iframe-main"><i class="icon icon-caret-right"></i>预约转诊</a></li>
                </ul>
            </li>
            <li><a class="menu_a" href="/hospital/doctors" target="iframe-main"><i class="icon icon-caret-right"></i>医生管理</a></li>
            <li><a class="menu_a" href="/hospital/departments" target="iframe-main"><i class="icon icon-caret-right"></i>科室管理</a></li>
			<li class="submenu">
                <a href="#">
                    <i class="icon icon-table"></i> 
                    <span class="menu-item-parent">辅助下单</span>
                </a>
                <ul>
                   <li>
                        <a class="menu_a" target="iframe-main" href="app/main.html#ajax/doc/helporder.html"><i class="icon icon-caret-right"></i>视频会诊</a>
                   </li>
                   <li>
                        <a class="menu_a" target="iframe-main" href="app/main.html#ajax/doc/advice.html"><i class="icon icon-caret-right"></i>图文会诊</a>
                    </li>     
                    <li>
                        <a class="menu_a" target="iframe-main" href="app/main.html#ajax/doc/referral.html"><i class="icon icon-caret-right"></i>预约转诊</a>
                    </li>
                </ul>
            </li>
             <li class="submenu"> 
                <a href="#">
                    <i class="icon icon-table"></i> 
                    <span>医联体管理</span> 
                </a>
                <ul>
                	<li><a class="menu_a" href="/hospital/hoshealthmanage" target="iframe-main"><i class="icon icon-caret-right"></i>医联体管理</a></li>
                    <li><a class="menu_a" href="/hospital/invitjoinmanage" target="iframe-main"><i class="icon icon-caret-right"></i>邀请加入列表</a></li>
                    <li><a class="menu_a" href="/hospital/appjoinmanage" target="iframe-main"><i class="icon icon-caret-right"></i>申请加入列表</a></li>
                </ul>
            </li> 
             <%-- <li class="submenu"> 
                <a href="#">
                    <i class="icon icon-table"></i> 
                    <span>本院公号</span> 
                </a>
                <ul>
                	<li><a class="menu_a" href="/hospital/basicconfig" target="iframe-main"><i class="icon icon-caret-right"></i>基本配置</a></li>
                    <li><a class="menu_a" href="/hospital/wxplusorders" target="iframe-main"><i class="icon icon-caret-right"></i>预约挂号订单</a></li>
                    <li><a class="menu_a" href="/hospital/userfeedbacks" target="iframe-main"><i class="icon icon-caret-right"></i>意见反馈</a></li>
                </ul>
            </li>  --%>
          <!-- 
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
            </li> -->
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
