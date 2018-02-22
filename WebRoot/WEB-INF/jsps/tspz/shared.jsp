<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fun"%>
<!DOCTYPE HTML>
<html>
	<head>
		<base href="/">
	   	<title>天使陪诊</title>
	   	<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=EDGE"/>
    	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    	<meta content="telephone=no" name="format-detection" />
		<link rel="stylesheet" type="text/css" href="/css/mobile.css?123" />
		<style>
			body,html{font-size:12px;}
			.main{
				/* Webkit: Safari 4-5, Chrome 1-9 */				
				background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#274F72), to(#A0BADD));				
				/* Webkit: Safari 5.1+, Chrome 10+ */				
				background: -webkit-linear-gradient(top, #274F72, #A0BADD);				
				/* Firefox 3.6+ */				
				background: -moz-linear-gradient(top, #274F72, #A0BADD);				
				/* Opera 11.10+ */				
				background: -o-linear-gradient(top, #274F72, #A0BADD);				
				/* IE 10 */				
				background: -ms-linear-gradient(top, #274F72, #A0BADD);				
				/* IE < 10 */				
				FILTER: progid:DXImageTransform.Microsoft.Gradient(startColorStr=#274F72, endColorStr=#A0BADD);
				min-height:500px;
				color:#fff;
			}
			._ctxt{padding:0.3em 1.8em 2em;line-height:1.8em;}
			._ctxt a{color:blue;}
			figure{margin:0 26% 0 26%;text-align:center;padding:20px 0;}
			.p-right{padding-right:50%;}
			.p-right p,.p-right div{text-align:right;padding-left:0.5em;}
			.p-left p,.p-left div{text-align:left;padding-right:0.5em;}
			section div:after{content:"";display:block;position:absolute;top:0;width:2px;height:100%;background-color:#fff;}
			.p-right div:after{right:-1px;}
			.p-left{padding-left:50%;}
			.p-left div:after{left:-1px;}
			section p{position:relative;height:36px;font-size:10px;font-weight:600;padding:0 14px;line-height: 36px;}
			section div{position:relative;padding:0.5em 14px 1em;line-height:1.6em;}
			section p:after,section p:before{content:"";display:block;position:absolute;border-radius:50%;}
			section p:after{width:18px;height:18px;background-color:#BAE7B2;z-index:1;}
			section p:before{width:8px;height:8px;border:2px solid #5D8174;z-index:2;}
			.p-right p:after{top:9px;right:-9px;}
			.p-right p:before{top:12px;right:-6px;}
			.p-left p:after{top:9px;left:-9px;}
			.p-left p:before{top:12px;left:-6px;}
			.bl-line{min-height:40px; background:url(/img/mobile/kbzs/share/split_bl@2x.png) repeat-x 0 bottom;background-size: 100% 35px; text-align:center; margin-bottom: -4px;}
			
			footer{font-size:13px;color:#444;line-height:1.8em;background-color:#fff;padding:0 15px 15px;}
			footer p{padding:15px 0;}
			footer div{padding:0 0 15px;}
			footer div:last-child{padding-top:15px;}
			.txtc1{color:#6EBFD7;font-weight:bold;}
			.txtc2{color:#FD8B2B;font-weight:bold;}
			.txtc3{color:#BAE7B2;font-weight:bold;}
			#getCards{width:100%;color:#fff;font-size:15px;background-color:#FD8B2B;border:0;padding:12px 0;border-radius: 3px;}
		</style>
	</head>
	<body id="index">
		<div class="main">
			<figure>
     			<img src="/img/mobile/kbzs/share/logo3@2x.png" alt="看病找谁" style="width:100%"/>
     			<img src="/img/mobile/kbzs/share/line-btm@2x.png" alt="" style="width:100%;margin-top:10px;"/>
    		</figure>
			<section class="p-right">
				<p>整个流程只需${days}天</p>
				<div>看病找谁解决了我一直觉得看病难的烦恼，专业的陪诊，全国最好的三甲医院医生，必须赞！！！</div>
			</section>
			<section class="p-left">
				<p>${fun:substring(receivePatientTime, 0, 10)}</p>
				<div>按照预约时间，我来到医院就诊，整个过程由陪诊人员细心协助，检查、取报告，完成就诊后并对我进行了健康提示，陪诊人员的亲切感简直就像自己亲人一样。</div>
			</section>
			<section class="p-right">
				<p>${fun:substring(readyTime, 0, 10)}</p>
				<div>专业陪诊人员<span class="txtc3">${nurseName }</span> 护士 给我讲解了相关的就医注意事项，以及诊前准备，对我这种有丢三落四的毛病的人确实踏实多了 。</div>
			</section>
			<section class="p-left">
				<p>${fun:substring(order.operateTime, 0, 10)}</p>
				<div>我叫<span class="txtc3">${realName }</span>，我通过‘看病找谁’公众号成功预约-<span class="txtc3">${dinfo.hospitalName}的${dinfo.realName}${dinfo.dutyName}</span></div>
			</section>
			<section class="bl-line">
				<br/><span>[目前开通服务地区——北京]</span><br/>
				<c:if test="${sex=='1'}">
					<img src="/img/mobile/kbzs/share/man@2x.png" alt="" style="width:30%;margin-top:10px;"/>
				</c:if>
				<c:if test="${sex=='0'}">
					<img src="/img/mobile/kbzs/share/woman@2x.png" alt="" style="width:30%;margin-top:10px;"/>
				</c:if>		
			</section>
		</div>
		<footer>
			<!-- 凡关注‘<span class="txtc1">看病找谁</span>’微信公众号的用户， -->
			<p class="txtc1">优惠活动一</p>
			<div>
				活动期间， 即有机会获得<span class="txtc2">代金券298元</span>（等于免费享受陪诊服务一次）。每天十个名额，抢完为止，
				没有抢到的用户不要灰心， 继续点击领取键也可获得<span class="txtc2">20 50 100元随机代金券</span>。
			</div>
			<div>
				<button type="button" id="getCards" onclick="_b.getsubscribe()">我要代金券</button>			
			</div>
		</footer>
		<script src="/js/jweixin-1.0.0.js"></script>
    	<script src="/libs/jquery-1.11.0.min.js"></script>
    	<script src="/js/base.js"></script>
		<script type="text/javascript">
			var baseUrl = '/', imgUrl = baseUrl + 'img/mobile/kbzs/3.png';
	        var lineLink = 'http://www.taozis.com/wtspz/shared/${orderid}';
	        var descContent = '我通过‘看病找谁’微信公众号非常幸运的预约了……';
	        var shareTitle = '“看病找谁”代金券，看病咨询、预约专家、就医陪诊，为您的健康买单！';
	        var _b = {
	    		href: baseUrl,
	    		oid: '${openid}' || _t._get('openid') || '',
	    		timsp:'${timestamp}',
	    		issub:'',
	    		dialog: new _dialog(),
	    		stxt:'<div class="_ctxt">恭喜您，领取了一张{1}元的{2}，快去<a href="'+ baseUrl +'wtspz/mycoupons.do?openid={3}">查看我的代金券</a>吧</div>',
	    		etxt:'<div class="_ctxt">为了方便使用代金券，请先关注我们的微信公众号吧^_^<br /><img alt="" src="'+ baseUrl +'img2/mobile/img_2.png" style="width:100%"/></div>',
	    		_post:function(url,ops,fun,err){
	    			return _$(this.href + url,ops,fun,err);
	    		},
	    		loadAuth:function(){
	    			var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx4ee3ae2857ad1e18&redirect_uri=';
					url += encodeURIComponent('http://www.taozis.com/wtspz/wxAythorization');
					url += '&response_type=code&scope=snsapi_userinfo&state=1234#wechat_redirect';
					_ts._set('oldUrl',window.location.href)
					_gr(url);
	    		},
	    		issubscribe:function(){
	    			if(!this.oid) return false;
	    			this._post('wtspz/tellsubscribe',{openid:this.oid},function(d){
	    				_b.issub = d.subscribe;
	    			});
	    		},
	    		getsubscribe:function(){
	    			if(!this.oid){
	    				this.loadAuth();
	    				return false;
	    			}
	    			if(_b.issub!='1'){
		    			this.dialog.alert.show({
		    				text : _b.etxt,
		    				modelEvent:true
		    			});
		    			return false;
	    			}
	    			this.dialog.loading.show();
	    			this._post('wtspz/receivecoup',{openid:this.oid},function(d){
	    				var t = '',tys = '';
	    				switch(d.type){
	    					case "1":
	    						tys = '首单券';
	    						break;
	    					case "2":
	    						tys = '普通券';
	    						break;
	    					default:
	    						tys = '代金券';
	    						break;
	    				}
	    				switch(d.mstatus){
	    					case "0":
	    						t = _b.stxt.replace('{1}',d.pmoney).replace('{2}',tys).replace('{3}',_b.oid);
	    						break;
	    					case "1":
	    						t = '不好意思活动已经结束，如有新活动我们会通知您。';
	    						break;
	    					case "-1":
	    					case "2":
	    						t = '活动还没开始呢，请耐心等待下';
	    						break;
	    				};
		    			_b.dialog.loading.hide();
	    				_b.dialog.alert.show({
		    				text : t,
		    				close:function(){
		    					showme.scrollIntoView();
		    				}
		    			});
		    		});
	    		}
    		};
			wx.config({
		        appId: '${appid}',
		        timestamp: _b.timsp ? parseInt(_b.timsp,10) : 0,
		        nonceStr: '${nonceStr}',
		        signature: '${signature}',
		        jsApiList: [
		            'onMenuShareAppMessage','onMenuShareTimeline','hideAllNonBaseMenuItem','showMenuItems'
		        ]
	        });
	        var ops={
	        	title: shareTitle,
			    desc: descContent,
			    link: lineLink,
			    imgUrl: imgUrl
	        };
			wx.ready(function(){
				wx.onMenuShareAppMessage(ops);
				wx.onMenuShareTimeline(ops);
				wx.hideAllNonBaseMenuItem();
				wx.showMenuItems({
				    menuList: ['menuItem:share:appMessage','menuItem:share:timeline']
				});
			});
			_b.issubscribe();
		</script>
	</body>
</html>
