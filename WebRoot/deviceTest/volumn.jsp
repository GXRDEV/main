<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<!--
 *  Copyright (c) 2015 The WebRTC project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree.
-->
<html>
<head>
  <meta charset="utf-8">
  <meta name="description" content="WebRTC code samples">
  <meta name="viewport" content="width=device-width, user-scalable=yes, initial-scale=1, maximum-scale=1">
  <meta name="mobile-web-app-capable" content="yes">
  <meta id="theme-color" name="theme-color" content="#ffffff">
  <base target="_blank">
  <title>设备检测</title>
  <link rel="stylesheet" href="css/main.css" />
	<!-- 新 Bootstrap 核心 CSS 文件 -->
	<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">

	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="//cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
    <style>
	   	#text{margin-top:1.6em;list-style: disc ;}
	   	#text dt{font-size:1.2em;padding:5px 0 0.3em;}
	   	#text dd{line-height:1.8em;display: list-item;margin-left: 1.5em;}
    	.label{color:#444;font-size:1em;padding:0;text-align:left;}
    </style>
</head>

<body>

  <div id="container">
    <div id="meters">
      <div id="instant">
        <div class="label">Instant: </div>
        <meter high="0.25" max="1" value="0"></meter>
        <div class="value"></div>
      </div>
      <div id="slow">
        <div class="label">Slow: </div>
        <meter high="0.25" max="1" value="0"></meter>
        <div class="value"></div>
      </div>
      <div id="clip">
        <div class="label">Clip: </div>
        <meter max="1" value="0"></meter>
        <div class="value"></div>
      </div>
      <dl id="text" class="bg-info" style="padding:1em">   
      	<dt>备注：</dt>   	
      	<dd>请对着音频输入设备（话筒等）说话，查看上图是否有柱状变化;</dd>
      	<dd>如果没有请调试，直到有状态变化，如果如果情况未解决，请联系客服。</dd>
      </dl>
    </div>

   </div>
  <script src="/deviceTest/js/adapter.js"></script>
  <script src="/deviceTest/js/common.js"></script>
  <script src="/deviceTest/js/soundmeter.js"></script>
  <script src="/deviceTest/js/main.js"></script>
</body>
</html>
