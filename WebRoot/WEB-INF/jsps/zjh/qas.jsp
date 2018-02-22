<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//通过request["code"]得到code，通过code得到用户的基本信息
%>

<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>常见问题</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="apple-mobile-web-app-capable" content="yes">
	<link rel="stylesheet" type="text/css" href="/css/mobile.css" />
    <style>
    	html,body{background-color:#f1f1f1;font-family:"Microsoft YaHei","SimHei","圆体";}
    	#main{width:600px;margin:0 auto;background-color:#fff;font-size:10px;}
    	#main dl{margin:0 1.5em; border-bottom:1px solid #eee;padding:0.5em 0 0.5em;}
    	#main dt{padding:0.5em 0 0.5em;line-height:1.2em;font-size:1.6em;font-family:"SimHei","Microsoft YaHei","圆体";color: rgb(0, 82, 255);font-weight:600;}
    	#main dd{line-height:1.5em; text-indent:2em; font-size:1.4em;color:#333;}
    	.rich_media_title{margin-bottom: 10px;
		    line-height: 1.4;
		    font-weight: 400;
		    font-size: 24px;
		}
    	@media (max-width: 600px) {	
			#main{width:100%;margin:0 auto;}
		}
    </style>
</head>
<body>
	<div id="main">
		 <h2 class="rich_media_title" style="text-align:center">“佰医汇”使用中的常见问题</h2>
		<dl>
			<dt>1、平台上都是真实的专家吗？</dt>
			<dd>是的。“佰医汇”平台上的专家都是经平台方工作人员面谈后入驻，100%真实。</dd>
		</dl>
		<dl>
			<dt>2、图文问诊要收费吗？</dt>
			<dd>图文问诊一般都需要收费，不同的专家有不同的收费标准。但7天内，每位专家可提供一次免费服务（每次服务最多提供5条回复）。</dd>
		</dl>
		<dl>
			<dt>3、如何选择专家呢？</dt>
			<dd>您可以点击“全国专家”点击相应科室搜索并选择专家，也可以在首页搜索框内按照疾病、医生、医院进行分类搜索，还可以在问诊过程中，在病情提交页在专家列表页中进行选择。</dd>
		</dl>
		<dl>
			<dt>4、上传照片显示失败，该如何处理呢？</dt>
			<dd>照片过大可能会导致上传失败，您可缩小照片后重新上传（每张照片应不超过3m）。如果缩小图片后上传依然失败，请查看网络是否正常。</dd>
		</dl>
		<dl>
			<dt>5、在图文问诊中，如果专家长时间不回复该怎么办？</dt>
			<dd>图文问诊是专家利用业余时间提供的服务，专家通常会在12小时内回复。若3天未回复，问诊自动结束，如交过费则自动退回。如需获得更快回复，可选“电话问诊”、“加急电话”或“视频问诊”。如需线下就诊，请选择“加号面诊”。</dd>
		</dl>
		<dl>
			<dt>6、加号订单提交后，大概多久专家会确认呢？</dt>
			<dd>加号订单提交后，专家一般会在24小时之内确认。如24小时后专家未确认订单，则该订单因超时取消，您所缴纳的相关费用会如数退还。</dd>
		</dl>
		<dl>
			<dt>7、取消加号，是什么原因？</dt>
			<dd>因专家门诊安排临时有变，加号有可能会被取消。您可重新选择其他专家进行加号面诊。</dd>
		</dl>
		<dl>
			<dt>8、专家取消了加号，过多久费用可以退还？</dt>
			<dd>如果加号被取消，您缴纳的相关费用将在3-7个工作日如数退还到您的账号。</dd>
		</dl>
		<dl>
			<dt>9、患者提交电话问诊订单后，过多久可以和专家电话联系？</dt>
			<dd>提交电话问诊订单后，如专家确认了订单，客服会提前分别与您和专家进行沟通，约定双方都方便的通话时间，在约定时间接通您和专家的电话。</dd>
		</dl>
		<dl>
			<dt>10、全国名医会诊怎么操作？</dt>
			<dd>在本地合作医院挂号，即可通过视频与全国顶级专家面对面交流。操作流程：选择专家、本地主治医生及就诊时间，支付后即成功挂号。在就诊当天，到当地合作医院找到主治医生后，在他的帮助下向全国顶级专家视频问诊。</dd>
		</dl>
	</div>
</body>
</html>