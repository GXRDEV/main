<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
			html,body {
				background-color: #f1f1f1;
				font-family: "Microsoft YaHei", "SimHei", "圆体";
			}
			
			#main {
				width: 600px;
				margin: 0 auto;
				background-color: #fff;
				font-size: 10px;
			}
			
			#main dl {
				margin: 0 1.5em;
				border-bottom: 1px solid #eee;
				padding: 1.5em 0 2em;
			}
			
			#main dt {
				padding: 0.5em 0 1em;
				line-height: 1.2em;
				font-size: 1.6em;
				font-family: "SimHei", "Microsoft YaHei", "圆体";
				color: #222;
				font-weight: 600;
			}
			
			#main dd {
				line-height: 1.5em;
				text-indent: 2em;
				font-size: 1.4em;
				color: #333;
			}
			
			@media ( max-width : 600px) {
				#main {
					width: 100%;
					margin: 0 auto;
				}
			}
		</style>
	</head>
	<body>
		<div id="main">
			<h1 style="font-size:20px;">服务介绍：</h1>
			<dl>
				<dt>服务1：加号服务 </dt>
				<dd>加号服务，支持普通号，佰医汇，以及指定专家加号服务；</dd>
			</dl>
			<dl>
				<dt>服务2：陪诊服务 </dt>
				<dd>由我们专业的护士全程陪诊，包括取号，陪同就医，排队取药，排队取结果，全程指导；</dd>
			</dl>
			<dl>
				<dt>服务3：其它服务</dt>
				<dd>护工服务，其它服务；</dd>
			</dl>
			<h1 style="font-size:20px;">使用场景：</h1>
			<dl>
				<dt>场景1： </dt>
				<dd> 
					以前：凌晨从暖和的被窝起来，打出租车赶到医院排队，此时已经有很多人已经在排队了，放号时刚好到你的时候，佰医汇已经没有了。
				</dd>
				<dd> 
					现在：直接通过app下单，我们的护士挂号号之后，直接电话与app通知您，然后再您就诊时，在医院门口把号给到您的手里；
				</dd>
			</dl>
			<dl>
				<dt>场景2： </dt>
				<dd> 
					以前：小孩生病时，妈妈给忙着焦头难额的爸爸打电话，爸爸一方面去经理那请假，很为难，担心奖金受影响
				</dd>
				<dd> 
					现在：直接通过app下单，我们的护士在医院门口等着您，给你取号，带您就医，帮您排队做检查，去检查结果，并看好之后，送您出医院；
				</dd>
			</dl>
			<dl>
				<dt> 场景3： </dt>
				<dd> 
					以前：外地来北京的患者，由于对医院不是很熟悉，经常跑上跑下，浪费很多宝贵的时间，往往还耽误检查，运气不好可能还需要多等待一周；
				</dd>
				<dd> 
					现在：由我们专业的陪诊护士全程陪护，给您节省大量宝贵的时间；
				</dd>
			</dl>
			<h1 style="font-size:20px;">我们是谁：</h1> 
			<dl>
				<dd>
				创始人：80后，计算机专业毕业，10年工作经验，在医疗行业从事2年，有着丰富的医疗资源；互联网技术，产品，市场；熟知医疗市场；有创业经历，目前所在的医疗互联网公司就是创业型的公司；从在餐饮方向进行过创业，属于连续创业。
				成熟的团队：研发（后台、前端、app），产品设计；
				</dd>
			</dl>
			
			
			

		</div>
	</body>
</html>