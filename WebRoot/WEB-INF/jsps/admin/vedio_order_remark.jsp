<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
</head>
<style>
    .remark_div{ width: 400px; height: 200px; margin: 50px auto;}
    .remark_div textarea{ width: 100%; height: 100%; resize: none}
    .remark_div div{ text-align: center}
</style>
<body>
    <div class="remark_div">
        <textarea>${vodio.remark}</textarea>
        <div>
            <button type="button" class="btn btn-danger">取消</button>&emsp;
			<button type="button" class="btn btn-primary">保存</button>
        </div>
    </div>
    <script src="/js/jquery.min.js"></script>
    <script src="/js/bootstrap.min.js"></script>
    <script>
        var id = window.location.href.substr(window.location.href.indexOf('id')+3)
        $('body')
        .delegate('.btn-danger', 'click', function(){
            window.location.href = '/system/vedioordermanage'
        })
        .delegate('.btn-primary', 'click', function(){
            $.post('/system/updateVedioorderRemrk', {
                id: id,
                remark: $('textarea').val()
            }).done(function(){
                window.location.href = '/system/vedioordermanage'
            })
        })
    </script>
</body>
</html>