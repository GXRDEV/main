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
    	#main dl{margin:0 1.5em; border-bottom:1px solid #eee;padding:1.5em 0 2em;}
    	#main dt{padding:0.5em 0 1em;line-height:1.2em;font-size:1.6em;font-family:"SimHei","Microsoft YaHei","圆体";color:#222;font-weight:600;}
    	#main dd{line-height:1.5em; text-indent:2em; font-size:1.4em;color:#333;}
    	@media (max-width: 600px) {	
			#main{width:100%;margin:0 auto;}
		}
    </style>
</head>
<body>
	<div id="main">
		<dl>
			<dt>1、后台的服务人员都是你们公司的吗？只要我下单你们就能保证服务吗？</dt>
			<dd>答：不是的，我们只是一个第三方平台，我们不和任何医院与护士存在雇佣关系，我们只是提供平台，制定规则，更好的搭建好患者与护士之间的桥梁。</dd>
		</dl>
		<dl>
			<dt>2、我在你的平台上面下单，你们自己又没有护士，质量有保证吗？</dt>
			<dd>答：我们签约的护士都是正规院校毕业，拥有护士资格证并在医院工作2年以上，所以从专业性与服务性都是可以保证的。</dd>
		</dl>
		<dl>
			<dt>3、加号服务与陪诊服务的价格是由你们平台规定的吗？</dt>
			<dd>答：我们只是提供一个平台，具体的价格是由市场来决定，也由你自己来设置，但为保证你的订单并护士迅速接单，请按市场行情出价。</dd>
		</dl>
		<dl>
			<dt>4、有我们自己定价的话， 我可以随便出价吗？</dt>
			<dd>答：为了维护市场公平，我们会总共给出3个价格，一个是指导价，一个是最低价，一个最高价，你的价格只能在最低价与最高价之间，为了维持整体公平，请您按照指导价给，如比较紧急，可以适当的高于指导价。</dd>
		</dl>
		<dl>
			<dt>5、我加号下单之后，如我临时有事，可以退款吗？</dt>
			<dd>答：在护士挂号之前您取消订单都是全额退款的，但如果护士已经在医院给你去排队，你需要跟护士商量，或者已经取到号了，这时不能取消了。</dd>
		</dl>
		<dl>
			<dt>6、我陪诊下单之后，如我临时有事，可以申请退款吗？</dt>
			<dd>答：可以的， 如您因为某些不能前来就诊，请您务必今早提前取消订单，我们将全额退款；如取消时间紧迫，护士可能已经付出行动，会影响护士以后的积极性。</dd>
		</dl>
		<dl>
			<dt>7、预约加号，我能制定专家吗，如何收费的？</dt>
			<dd>答：预约加号，我们支持制定专家，费用由您自己定，但由于不同的专家的费用是不一样的，为了保证护士正常接单，请尽量参考历史价格下单。</dd>
		</dl>
		<dl>
			<dt>8、可以用我的账户给家人朋友下单吗？</dt>
			<dd>答：可以的，下单时，您需要提供实际使用患者的姓名与联系方式以及身份证号即可，确保数据正确。</dd>
		</dl>
	</div>
</body>
</html>