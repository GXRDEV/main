webpackJsonp([72],{1075:function(t,e,a){e=t.exports=a(471)(),e.push([t.i,"header[data-v-43700314]{height:2.9rem;width:100%;background:#eee;position:relative}header i[data-v-43700314]{display:inline-block;height:1.67rem;width:5.16rem;background:url("+a(762)+");background-size:contain;position:absolute;top:50%;margin-top:-.83rem;left:1.2rem}header span[data-v-43700314]{padding:.2rem 1rem;background:#0186d1;border-radius:.8rem;position:absolute;right:1.5rem;top:50%;-webkit-transform:translateY(-50%);transform:translateY(-50%)}.content[data-v-43700314]{padding-top:1.45rem;text-align:center}.content img[data-v-43700314]{width:81%}.pleft[data-v-43700314]{padding-left:10%;color:#9196a1;margin-top:1.25rem}footer[data-v-43700314]{margin-top:2.08rem;background:#f4f4f4;padding:1.58rem 1.83rem 3.9rem;color:#5d5d5d}footer p[data-v-43700314]{line-height:1.8rem}",""])},1188:function(t,e,a){var r=a(1075);"string"==typeof r&&(r=[[t.i,r,""]]),r.locals&&(t.exports=r.locals);a(472)("59b8dd9e",r,!0)},1345:function(t,e,a){t.exports=a.p+"static/v2/img/appsharebg.67f66d5.png"},1413:function(t,e,a){t.exports={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("article",{staticClass:"bgfff h100"},[a("header",[a("i"),t._v(" "),a("span",{staticClass:"small9 fff tcenter",on:{click:t.openApp}},[t._v("打开应用")])]),t._v(" "),t._m(0,!1,!1),t._v(" "),a("p",{staticClass:"big14 pleft"},[t._v("您的好友 "),a("span",{staticClass:"maincolor"},[t._v(t._s(t.docName))]),t._v(" 邀请您加入佰医汇！")]),t._v(" "),a("p",{staticClass:"big14 pleft"},[t._v("邀请码 ： "),a("span",{staticClass:"blacklower"},[t._v(t._s(t.code))])]),t._v(" "),a("p",{staticClass:"tcenter small10 maincolor",staticStyle:{"margin-top":"2.9rem"}},[t._v("填写邀请码并完成注册，您将获得10元现金红包")]),t._v(" "),t._m(1,!1,!1)])},staticRenderFns:[function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"content"},[r("img",{attrs:{src:a(1345)}})])},function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("footer",{staticClass:"base12"},[a("p",{staticClass:"big20 blacklower"},[t._v("详细规则")]),t._v(" "),a("p",{staticStyle:{"margin-top":"2rem","margin-bottom":"2.46rem"}},[t._v("为了保障您的合法权益，请在参与邀请注册活动之前，仔细阅读本协议。")]),t._v(" "),a("p",[t._v("一 参与方式")]),t._v(" "),a("p",[t._v("1. 通过好友邀请，在本页面打开或下载“佰医汇”APP,在注册页面填写邀请人提供的邀请码并完成注册，邀请者和受邀者将各获得10元现金红包。")]),t._v(" "),a("p",[t._v("2. 完成注册后需要及时进行执业认证，认证通过后可在账户内完成金额提现。")])])}]}},447:function(t,e,a){a(1188);var r=a(9)(a(992),a(1413),"data-v-43700314",null);t.exports=r.exports},762:function(t,e,a){t.exports=a.p+"static/v2/img/zjh.d017682.png"},992:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={data:function(){return{id:this.$route.params.id,docName:"",code:""}},created:function(){var t=this;this.$http.get(this.$urls.GETCODE,{doctorId:this.id}).then(function(e){t.code=e.code,t.docName=e.doc.specialName})},methods:{openApp:function(){window.location.href="http://a.app.qq.com/o/simple.jsp?pkgname=com.kangxin.doctor"}}}}});