webpackJsonp([68],{1127:function(t,e,i){e=t.exports=i(471)(),e.push([t.i,".docdetail[data-v-dea28d64]{background-color:#fff}header[data-v-dea28d64]{height:5.85rem;line-height:1.5rem;background-color:#fff;padding:1.3rem 1rem 0 0;margin-left:1rem;border-bottom:2px solid #eee}header h3[data-v-dea28d64]{font-size:1.615rem;color:#222;font-weight:400;letter-spacing:.2rem;margin-bottom:.6rem}header .colorSize[data-v-dea28d64]{color:#878d99;font-size:1rem}header .thumb[data-v-dea28d64]{width:3.07rem;height:3.07rem;top:1.3rem;right:2rem}section[data-v-dea28d64]{padding:0 1rem 1rem}section .graycolor[data-v-dea28d64]{color:#878d99}section p[data-v-dea28d64]{padding-bottom:.6rem;line-height:1.5em;margin:0 1rem}section .title[data-v-dea28d64]{display:inline-block;width:20%;color:#0086d1;vertical-align:top}section .info[data-v-dea28d64]{width:77%;min-height:5rem;display:inline-block;border-left:1px solid #0086d1;color:#222}section .info span[data-v-dea28d64]{width:.5rem;height:.5rem;border-radius:50%;margin-top:.25rem;margin-left:-.25rem;background-color:#0086d1;z-index:3}section .info i[data-v-dea28d64]{width:1rem;height:1rem;border-radius:50%;margin-top:0;margin-left:-.5rem;background-color:#81c1e7}",""])},1240:function(t,e,i){var a=i(1127);"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);i(472)("5b9a0805",a,!0)},1461:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("article",{staticClass:"docdetail"},[i("header",{staticClass:"relative bbox"},[i("h3",[t._v(t._s(t.doc.specialName))]),t._v(" "),i("p",{staticClass:"colorSize"},[t._v(t._s(t.doc.hosName)+" | "+t._s(t.doc.depName))]),t._v(" "),i("div",{staticClass:"thumb bgcove radius50 absolute",style:"background-image:url("+t.imgsrc(t.doc.listSpecialPicture)+")"})]),t._v(" "),i("section",[t._m(0,!1,!1),t._v(" "),i("div",{staticClass:"infomain"},[i("div",{staticClass:"title base12"},[t._v("\n          执业点\n        ")]),t._v(" "),i("div",{staticClass:"info relative base12 bbox"},[i("span",{staticClass:"absolute"}),t._v(" "),i("i",{staticClass:"absolute"}),t._v(" "),i("p",[t._v(t._s(t.doc.hosName))]),t._v(" "),i("p",[t._v(t._s(t.doc.depName)+" ("+t._s("3"==t.doc.userType?"医生":"专家")+")")])])]),t._v(" "),i("div",{staticClass:"infomain"},[i("div",{staticClass:"title base12"},[t._v("\n          团队\n        ")]),t._v(" "),t.teams.length?i("div",{staticClass:"info relative base12 bbox"},[i("span",{staticClass:"absolute"}),t._v(" "),i("i",{staticClass:"absolute"}),t._v(" "),t._l(t.teams,function(e,a){return i("p",{key:a},[t._v(t._s(e.teamName))])})],2):i("div",{staticClass:"info relative base12 bbox"},[i("span",{staticClass:"absolute"}),t._v(" "),i("i",{staticClass:"absolute"}),t._v(" "),i("p",{staticClass:"graycolor"},[t._v("暂无所属团队")])])]),t._v(" "),i("div",{staticClass:"infomain"},[i("div",{staticClass:"title base12"},[t._v("\n          擅长领域\n        ")]),t._v(" "),i("div",{staticClass:"info relative base12 bbox"},[i("span",{staticClass:"absolute"}),t._v(" "),i("i",{staticClass:"absolute"}),t._v(" "),t.doc.specialty?i("p",[t._v(t._s(t.doc.specialty))]):i("p",{staticClass:"graycolor"},[t._v(" 暂无")])])]),t._v(" "),i("div",{staticClass:"infomain"},[i("div",{staticClass:"title base12"},[t._v("\n          个人简介\n        ")]),t._v(" "),i("div",{staticClass:"info relative base12 bbox"},[i("span",{staticClass:"absolute"}),t._v(" "),i("i",{staticClass:"absolute"}),t._v(" "),t.doc.profile?i("p",[t._v(t._s(t.doc.profile))]):i("p",{staticClass:"graycolor"},[t._v("毕业院校、进修经历、获奖履历、执业经历等")])])])])])},staticRenderFns:[function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"infomain",staticStyle:{height:"2.27rem"}},[i("div",{staticClass:"title"}),t._v(" "),i("div",{staticClass:"info"})])}]}},396:function(t,e,i){i(1240);var a=i(9)(i(944),i(1461),"data-v-dea28d64",null);t.exports=a.exports},488:function(t,e,i){"use strict";var a=i(7),s=a.build.assetsPublicPath+a.build.assetsSubDirectory;e.a=function(t){var e=arguments.length>1&&void 0!==arguments[1]?arguments[1]:"doc",i=arguments.length>2&&void 0!==arguments[2]?arguments[2]:60,a=arguments.length>3&&void 0!==arguments[3]?arguments[3]:"m_fill",o=s+"/"+e+(-1!=e.indexOf(".")?"":".png");return t&&"null"!=t?(-1!=t.indexOf("http://")?o=-1!=location.href.indexOf("https://")?t.replace("http://","https://"):t:-1!=t.indexOf("https://")||-1!=t.indexOf("blob:http")?o=t:-1==t.indexOf("data:image/")&&-1==t.indexOf("wxLocalResource://")&&-1==t.indexOf("weixin://")||(o=t),-1==o.indexOf("aliyuncs.com")&&(i="auto"),o+("auto"==i?"":"?x-oss-process=image/resize,"+a+",h_"+i+",w_"+i)):o}},526:function(t,e,i){i(529);var a=i(9)(i(527),i(530),"data-v-696e1578",null);t.exports=a.exports},527:function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=i(20),s=i.n(a),o=i(488);e.default={name:"itemfleximg",props:{link:[String,Object],isLink:{type:Boolean,default:!1},img:String,size:String,logo:String,radius:String,thumb:{type:String,default:"left"},borderBottom:{type:Boolean,default:!1}},computed:{style:function(){var t=this.size||"3.83rem",e=this.logo||"doc",a=-1!=t.indexOf("rem")?30*parseInt(t):parseInt(t);return"background-image:url("+i.i(o.a)(this.img,e,a)+");"+(this.radius?"border-radius: "+this.radius+";":"")},thumbstyle:function(){var t=this.size||"3.83rem";return t?"width:"+t+";-webkit-box-flex: 0 0 "+t+"; -webkit-flex:0 0 "+t+";flex:0 0 "+t+";":""},islink:function(){return"object"==s()(this.link)?!!this.link.path||!!this.link.name:!!this.link},linkUrl:function(){return this.islink?this.link:location.hash.replace("#","")}}}},528:function(t,e,i){e=t.exports=i(471)(),e.push([t.i,".itemlist[data-v-696e1578]{padding:1.6rem 1.16rem 1.3rem 0;color:#333;background:#fff}.itemlist .mainbox[data-v-696e1578]{overflow:hidden;margin-left:1rem}.itemlist.borderbottom[data-v-696e1578]{border-bottom:.5px solid #eee}.outerthumb[data-v-696e1578]{-webkit-box-flex:0 0 3.83rem;-ms-flex:0 0 3.83rem;-webkit-flex:0 0 3.83rem;flex:0 0 3.83rem;width:3.83rem}.thumb[data-v-696e1578]{width:100%;height:0;padding-top:100%}.thumbRight .outerthumb[data-v-696e1578]{-webkit-box-ordinal-group:2;-webkit-order:1;order:1}.thumbRight .mainbox[data-v-696e1578]{-webkit-box-ordinal-group:1;-webkit-order:0;order:0;margin-left:0}",""])},529:function(t,e,i){var a=i(528);"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);i(472)("7a4fa524",a,!0)},530:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("router-link",{staticClass:"itemlist flex relative",class:{thumbRight:"right"==t.thumb,borderbottom:t.borderBottom},attrs:{to:t.linkUrl}},[i("div",{staticClass:"outerthumb",style:t.thumbstyle},[i("i",{staticClass:"thumb bgcove",style:t.style}),t._v(" "),t._t("thumb")],2),t._v(" "),i("div",{staticClass:"flex_1 mainbox"},[t._t("default")],2),t._v(" "),t.isLink?i("i",{staticClass:"iconfont icon-arrright absolute top right h100 flex acenter gray",staticStyle:{padding:"0 .6rem"}}):t._e()])},staticRenderFns:[]}},944:function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=i(526),s=i.n(a),o=i(488);e.default={name:"docinfo",components:{ItemFlexImg:s.a},data:function(){return{id:this.$route.params.id,doc:{},teams:{}}},created:function(){var t=this;this.$http.get(this.$urls.DOCMAIN,{docid:this.id}).then(function(e){t.doc=e.doc||{}}),this.$http.get(this.$urls.GAINDOCBELONGTEAMS,{docid:this.id}).then(function(e){t.teams=e.teams||{}})},methods:{imgsrc:o.a}}}});