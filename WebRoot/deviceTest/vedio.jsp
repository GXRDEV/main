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
	<!-- 新 Bootstrap 核心 CSS 文件 -->
	<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">
  <style>
  	body{text-align:center}
  	#container{text-align:left;display:inline-block;}
    div.select {
      margin: 1em 0;
    }
    p.small {
      font-size: 0.7em;
    }
    label {
      width: 6em;
      display: inline-block;
      text-align:right;
      margin-right:1em;
      font-weight:400;
    }
    select{width:38em;height:30px;}
    video{margin:10px auto;}
   	#text{margin-top:1.6em;list-style: disc ;}
   	#text dt{font-size:1.2em;padding:5px 0 0.3em;}
   	#text dd{line-height:1.8em;display: list-item;margin-left: 1.5em;}
  </style>

</head>

<body>
  <div id="container">
    <div class="select">
      <label for="audioSource">音频输入源: </label><select id="audioSource"></select>
    </div>
    <div class="select">
      <label for="audioOutput">音频输出源: </label><select id="audioOutput"></select>
    </div>
    <div class="select">
      <label for="videoSource">视频源: </label><select id="videoSource"></select>
    </div>
    <video id="video" autoplay>
    	<p>正在初使化……</p>
    </video>
    <dl id="text" class="bg-info" style="padding:1em">   
      	<dt>备注：</dt>   	
      	<dd>下拉选择框列出当前电脑可用的输入输出设备，您可以选择相应的设备测试。</dd>
      	<dd>请对着音频输入设备（话筒等）说话，测试耳机或者音响是否有声响。</dd>
      	<dd>在页面是是否看到视频画面，如果没有看到请选择视频源，测试。</dd>
      	<dd>如果没有视频画面或者未听到音频声音，请调试设备，或者查看是否安装驱动。</dd>
      	<dd>如果没有解决，请联系客服。</dd>
    </dl>
 </div>
  <script src="/deviceTest/js/adapter.js"></script>
  <script src="/deviceTest/js/main_vedio.js"></script>
</body>
</html>

