webpackJsonp([92,93],{0:function(e,n,t){"use strict";Object.defineProperty(n,"__esModule",{value:!0}),t.d(n,"GET_DISTCODES",function(){return o}),t.d(n,"SET_STATE",function(){return a}),t.d(n,"SET_KEEPSTATE",function(){return i}),t.d(n,"SET_GPS",function(){return u}),t.d(n,"SET_SGPS",function(){return r}),t.d(n,"SET_WXINFO",function(){return c}),t.d(n,"SET_GOEASY",function(){return s});var o="GET_DISTCODES",a="SET_STATE",i="SET_KEEPSTATE",u="SET_GPS",r="SET_SGPS",c="SET_WXINFO",s="SET_GOEASY"},73:function(e,n,t){"use strict";function o(e,n){var t=location.href.split("#")[0],o=e.wechat,a=e.urls;if(-1!==t.indexOf("from=singlemessage&isappinstalled=0"))return location.replace(location.href.replace("from=singlemessage&isappinstalled=0","1=1"));e.http.get(a.GAINSHAREPARAMS,{shareurl:t}).then(function(e){var t=u()(e,{jsApiList:["hideAllNonBaseMenuItem","showMenuItems","onMenuShareAppMessage","onMenuShareTimeline","showAllNonBaseMenuItem","onMenuShareQQ","hideMenuItems","getLocation","previewImage","chooseImage","uploadImage","getLocalImgData","startRecord","stopRecord","onVoiceRecordEnd","playVoice","pauseVoice","stopVoice","onVoicePlayEnd","uploadVoice","chooseWXPay","closeWindow"]});o.config(t),o.ready(function(){o.hideAllNonBaseMenuItem(),o.getLocation({type:"wgs84",success:function(e){n.dispatch("getGPS",e)},fail:function(e){n.commit(r.SET_GPS,{code:"",name:"定位失败",lat:"",lon:""})}})})})}function a(e,n,t){n.beforeEach(function(n,t,o){if(e.commit("updateLoadingStatus",{isLoading:!0}),/\:\/\//.test(n.path)){var a=n.fullPath.split("://")[1];window.location.href="https://"+a}else e.dispatch("isLogin",{sceneStr:n.name&&n.params.id?[n.name,n.params.id].join("_"):""}).then(function(e){e.logined||n.matched.some(function(e){return e.meta.ignoreAuth})?o():e.needauth2?o({path:"/auth2",query:{from:n.fullPath}}):e.needwatch?o({path:"/watch",query:{url:e.needwatch}}):e.needbind&&n.matched.some(function(e){return e.meta.needBind})?o({path:"/bind",query:{from:n.fullPath}}):o()})});var o=t.wechat;n.afterEach(function(n){e.commit("updateLoadingStatus",{isLoading:!1}),o.ready(function(){o.hideAllNonBaseMenuItem()})})}Object.defineProperty(n,"__esModule",{value:!0}),t.d(n,"weChatConfig",function(){return o}),t.d(n,"routerEach",function(){return a});var i=t(35),u=t.n(i),r=t(0)}},[73]);