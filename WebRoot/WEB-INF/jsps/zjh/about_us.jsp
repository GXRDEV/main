<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//通过request["code"]得到code，通过code得到用户的基本信息
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta name="format-detection" content="telephone=no">
        <link rel="dns-prefetch" href="//res.wx.qq.com">
		<link rel="dns-prefetch" href="//mmbiz.qpic.cn">
		<link rel="shortcut icon" type="image/x-icon" href="http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/images/icon/common/favicon22c41b.ico">
        <title>关于“佰医汇”</title>
		<link rel="stylesheet" type="text/css" href="http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/style/page/appmsg/page_mp_article_improve2d1390.css">
<style>
     
    </style>
<!--[if lt IE 9]>
<link rel="stylesheet" type="text/css" href="http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/style/page/appmsg/page_mp_article_improve_pc2c9cd6.css">
<![endif]-->
    </head>
    <body id="activity-detail" class="zh_CN mm_appmsg" ontouchstart="">
    <div id="js_cmt_mine" class="discuss_container editing access" style="display:none;">
        <div class="discuss_container_inner">
            <h2 class="rich_media_title">关于“佰医汇”</h2>
            <span id="log"></span>
            <div class="frm_textarea_box_wrp">
                <span class="frm_textarea_box">
                    <textarea id="js_cmt_input" class="frm_textarea" placeholder="留言将由公众号筛选后显示，对所有人可见。"></textarea>
                    <div class="emotion_tool">
                        <span class="emotion_switch" style="display:none;"></span>
                        <span id="js_emotion_switch" class="pic_emotion_switch_wrp">
                            <img class="pic_default" src="http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/images/icon/appmsg/emotion/icon_emotion_switch.2x278965.png" alt="">
                            <img class="pic_active" src="http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/images/icon/appmsg/emotion/icon_emotion_switch_active.2x278965.png" alt="">
                        </span>
                        <div class="emotion_panel" id="js_emotion_panel">
                            <span class="emotion_panel_arrow_wrp" id="js_emotion_panel_arrow_wrp">
                                <i class="emotion_panel_arrow arrow_out"></i>
                                <i class="emotion_panel_arrow arrow_in"></i>
                            </span>
                            <div class="emotion_list_wrp" id="js_slide_wrapper">
                                
                                
                            </div>
                            <ul class="emotion_navs" id="js_navbar">
                                
                            </ul>
                        </div>
                    </div>
                </span>
            </div>
            <div class="discuss_btn_wrp"><a id="js_cmt_submit" class="btn btn_primary btn_discuss btn_disabled" href="javascript:;">提交</a></div>
            <div class="discuss_list_wrp" style="display:none">
                <div class="rich_tips with_line title_tips discuss_title_line">
                    <span class="tips">我的留言</span>
                </div>
                <ul class="discuss_list" id="js_cmt_mylist"></ul>
            </div>
            <div class="rich_tips tips_global loading_tips" id="js_mycmt_loading">
                <img src="http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/images/icon/common/icon_loading_white2805ea.gif" class="rich_icon icon_loading_white" alt="">
                <span class="tips">加载中</span>
            </div>
            <div class="wx_poptips" id="js_cmt_toast" style="display:none;">
                <img alt="" class="icon_toast" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGoAAABqCAYAAABUIcSXAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA3NpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDoyMTUxMzkxZS1jYWVhLTRmZTMtYTY2NS0xNTRkNDJiOGQyMWIiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MTA3QzM2RTg3N0UwMTFFNEIzQURGMTQzNzQzMDAxQTUiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MTA3QzM2RTc3N0UwMTFFNEIzQURGMTQzNzQzMDAxQTUiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChNYWNpbnRvc2gpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6NWMyOGVjZTMtNzllZS00ODlhLWIxZTYtYzNmM2RjNzg2YjI2IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjIxNTEzOTFlLWNhZWEtNGZlMy1hNjY1LTE1NGQ0MmI4ZDIxYiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pmvxj1gAAAVrSURBVHja7J15rF1TFMbXk74q1ZKHGlMkJVIhIgg1FH+YEpEQJCKmGBpThRoSs5jVVNrSQUvEEENIhGiiNf9BiERICCFIRbUiDa2qvudbOetF3Tzv7XWGffa55/uS7593977n3vO7e5+199p7v56BgQGh0tcmvAUERREUQVEERREUQVEERREUQVEERREUQVEERREUQVEERREUQVEERVAUQVEERVAUQbVYk+HdvZVG8b5F0xj4RvhouB+eCy8KrdzDJc1RtAX8ILxvx98V1GyCSkN98Cx4z/95/Wn4fj6j6tUEeN4wkFSnw1MJqj5NhBfAuwaUHREUg4lqNMmePVsHll/HFhVfe1t3FwpJI8DXCCquDrCWNN4B6Tb4M3Z98aTPmTvh0YHl18PXw29yZiKejoPvcUD6E74yFBJbVDk6Bb7K8aP/Hb4c/tRzEYIqprPhSxzlf4Uvhb/0Xoig8qnHAJ3lqPMzfDH8XZ4LEpRf2sVdA5/sqPO9Qfop70UJyn+/boaPddT5yrq7VUUvTIVJI7q74MMddXR8NB1eXcYvhBpZm0s2w72/o86HFoKvLau/pYaXzjLMdUJ6y0LwtWV9CIIaXtvA8+G9HHV03u5q+K+yH47U0NoRngPv7KjzHDwTLj0bS1BDazfJJlcnOOostC6ysnCT+q80G/sIvFVgeW09D8FPVT0uoP7VfvAD8NjA8pqmuAN+OcYAjso0RbIZ8DGB5TVNcRO8JMaHY9SXSdfa3eeANJimWBLrA7JFiZwIXye+NMUV8CcxP2SRFjXefok7NRjSGZJlWUPvw2/wtNiQirSoXWyMsR28wR7AzzYM0oXw+Y7yK+CLJGeaoqjyrJSdZJD6Ov4+z5y6NJc0Az7NUecHydIUy+v60KNyQHoM3nKI1y7YCFiq0i7uBvgER52vDdKqWn9djhY1Dn4G3n6Ecqm2rF74dvgoR53S0hQxW9RJAZAGW5bSn58QJA27dQ7uIEedjywEX5NKVxCqsY6y+qA+LxFI4+yZ6oH0trWkNan80jygtIUsc5SflgAsDXgehfdx1KkkTRE76tN+Xue2jnTU0Ru1oIbvpt30bBtKhOp5yaaRkts0lic8V1i6dPcIRx2d/l8Y8XtNNEg7OOo8bl1kmmOKnDsO88CaYzejau0hWZqiL7C83oCH4SeTHvwV2BqqsHRVztSEYOmWF80NeXZT6Hd4KflResE9vCnBOlCyGfDNAstHTVPUDWoQ1t3iW+9WNizvlhfd4aerXd+ThqiMfNR6+9LvOOro5OY5JX2H4+F7HZD+kGzlamMgldWiirQsjcwWFbjmqZJteekJLK9pisvgL6RhKvuciZiwzrWWGapfrPy30kBVcSBIrw0aD3PU0XB6cehntq7rTMf7/2iQlktDVdXJLXlg6VjmiYBn6rWSTRCH6hvJ0hQrpcGq8oidsmHpTP8t8DGO9/vcWt9qabiqPgup1yKyQwvC2tSefZ73SSpNkUJ4PlLorlHZ+446nc8f3fIyywlJhwrTuwVSjBa1ccvSxN0hjjoK5xVrYZMd9V6XbFfgBukixTwGLg8sDam3dZR/wZ6L/dJlin1en8LS+bgpFbz3Ygvzu1J1HKxYNqxGpCmaCEo12rrBorD6LRp8UbpcdR5VWhTW35KlKd6QFqjuM2XzwlpnMxTvSkuUwuG/Xlg6NtPjbT6WFimF/VG6LEvXgn8QGDjMbBukVECFwhpoS+CQatfX2Q1q6H7wENHdrfCr0lKleEB9JyxNneus+VJpsVL9TwI6W65LovWIGl3KtVJaLv7LBwYTFEERFEVQFEERFEVQFEERFEVQFEERFEVQFEERFEVQFEERFFWq/hFgADUMN4RzT6/OAAAAAElFTkSuQmCC">
                <p class="toast_content">已留言</p>
            </div>
        </div>
    </div>

    <div id="js_article" class="rich_media">
        
        <div id="js_top_ad_area" class="top_banner">
 
        </div>
                

        <div class="rich_media_inner">
            <div id="page-content">
                <div id="img-content" class="rich_media_area_primary">
                   
                    <div class="rich_media_meta_list">
                      	<h2 class="rich_media_title" style="text-align:center;"id="activity-name">
                       		 关于“佰医汇” 
                    	</h2>
                        <div id="js_profile_qrcode" class="profile_container" style="display:none;">
                            <div class="profile_inner">
                                <strong class="profile_nickname">佰医汇</strong>
                                <img class="profile_avatar" id="js_profile_qrcode_img" src="" alt="">

                                <p class="profile_meta">
                                <label class="profile_meta_label">微信号</label>
                                <span class="profile_meta_value">zhuanjiahao1</span>
                                </p>

                                <p class="profile_meta">
                                <label class="profile_meta_label">功能介绍</label>
                                <span class="profile_meta_value">“佰医汇”平台提供全国顶级专家在线问诊服务，平台上全部是全国排名前十医院/科室副主任医师以上专家。您足不出户，就可直接与国内顶级专家本人交流，获得对病情的权威专业解答。目前可提供图文问诊、电话问诊、视频问诊、专家会诊、加号面诊、加急电话等。</span>
                                </p>
                                
                            </div>
                            <span class="profile_arrow_wrp" id="js_profile_arrow_wrp">
                                <i class="profile_arrow arrow_out"></i>
                                <i class="profile_arrow arrow_in"></i>
                            </span>
                        </div>
                    </div>
                    
                    
                    
                    
                                                            
                                                            
                    
                    <div class="rich_media_content " id="js_content">
                        
                        <p>&nbsp;<span style="font-family: 宋体;"><img data-src="http://mmbiz.qpic.cn/mmbiz/YFc2yChKf60ds3CENf925dYxfibEqHFJmLkDRxXyaDs2NwIXYVia75jeArBCYoJeAicv2q4U3ibvgViasuDqpqckibcQ/0?wxfmt=png&amp;wx_fmt=png" data-ratio="0.10971223021582734" data-s="300,640" data-w="" data-type="png"  /><br  /></span></p><p>　　“佰医汇”是国内唯一一家按国内医院科室主流排行榜甄选专家，并实行实名邀请制的移动医疗平台。</p><p>　　“佰医汇”定位恰如其名，汇聚的全部是中国顶级专家，致力于通过最新信息技术（包括但不限于移动互联技术），提供最权威、最精准、最方便、最接地气的医疗服务。</p><p>　　综合“复旦排行榜”、“北大排行榜”、“医科院排行榜”等权威榜单，“佰医汇”甄选出全国排名前十的医院科室副主任以上专家，邀请其入驻。</p><p>　　所有专家都经过平台方工作人员面谈确认或平台已有专家推荐背书，并认同“佰医汇”理念和服务要求。<br  />&nbsp;</p><p>　　<strong>平台专家分布</strong></p><p>　　平台上现已有近千名专家，覆盖全部主要疾病和科室，均来自全国知名医院：</p><p>　　综合医院，如：北京协和医院、北京301医院、北京北大医院、北京北医三院、北京北大人民医院、北京儿童医院、四川华西医院、武汉协和医院、上海复旦中山医院等；</p><p>　　专科医院，如：北京宣武医院、北京安贞医院、北京天坛医院、北京阜外医院、北京朝阳医院、北京同仁医院、北京积水潭医院、北京医科院肿瘤医院等。<br  />&nbsp;</p><p>　　<strong>产品形态</strong></p><p>　　“佰医汇”APP版、微信版、PC版现已全面推出。患者可通过多种形式登录，获得平台各项服务。<br  />&nbsp;</p><p>　　<strong>产品功能</strong></p><p>　　普通患者可获得由全国顶级专家提供的图文问诊、电话问诊、加急电话、加号面诊、视频问诊、专家会诊等多项服务。合作医院患者除获得以上服务外，还可获得全国顶级专家提供的手术、会诊等服务。此外，通过“佰医汇”（合作医院版），合作医院患者还可获得合作医院医生提供的各项服务：预约挂号、查报告单、快速问诊、患者复诊、上门诊疗、电话问诊。<br  />&nbsp;</p><p>　　<strong>全国顶级专家提供的主要服务说明</strong>：</p><p>　　——图文问诊：提交病情的文字和图片资料，向专家进行线上咨询。</p><p>　　——电话问诊：提交病情资料后，通过电话即可与专家直接沟通。</p><p>　　——加急电话：快速接通专家电话进行咨询，解决病情紧急时的燃眉之急。</p><p>　　——加号面诊：帮您预约挂到佰医汇，进行线下就诊，解除佰医汇“一号难求”之忧。</p><p>　　——视频问诊：通过视频电话，模拟真实就医场景，实现与专家远程面对面交流。</p><p>　　——专家会诊：针对较复杂病情，可同时获得两位以上专家服务，轻松实现在线会诊。</p><p><span style="font-family: 宋体;"></span></p>
                    </div>
                    
                    <link rel="stylesheet" type="text/css" href="http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/style/page/appmsg/page_mp_article_improve_combo2d1390.css">
                   
                                        <div class="rich_media_tool" id="js_toobar3">
                                                                    <div id="js_read_area3" class="media_tool_meta tips_global meta_primary" style="display:none;">阅读 <span id="readNum3"></span></div>

                        <span style="display:none;" class="media_tool_meta meta_primary tips_global meta_praise" id="like3">
                            <i class="icon_praise_gray"></i><span class="praise_num" id="likeNum3"></span>
                        </span>

                        <a id="js_report_article3" style="display:none;" class="media_tool_meta tips_global meta_extra" href="javascript:void(0);">投诉</a>

                    </div>



                                    </div>

                <div class="rich_media_area_primary sougou" id="sg_tj" style="display:none">

                </div>

                <div class="rich_media_area_extra">

                    
                                        <div class="mpda_bottom_container" id="js_bottom_ad_area">
                        
                    </div>
                                        
                    <div id="js_iframetest" style="display:none;"></div>
                                        
                                    </div>
               
            </div>
            <div id="js_pc_qr_code" class="qr_code_pc_outer" style="display:none;">
                <div class="qr_code_pc_inner">
                    <div class="qr_code_pc">
                        <img id="js_pc_qr_code_img" class="qr_code_pc_img">
                        <p>微信扫一扫<br>关注该公众号</p>
                    </div>
                </div>
            </div>

        </div>
    </div>
    
  <script type="text/javascript">
      var not_in_mm_css = "http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/style/page/appmsg/not_in_mm2c9cd6.css";
      var windowwx_css = "http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/style/page/appmsg/page_mp_article_improve_winwx2c9cd6.css";
      var tid = "";
      var aid = "";
      var clientversion = "0";
      var appuin = "MzA4MDQ2Nzg4MA=="||"";

      var source = "0";
      var scene = 75;
      
      var itemidx = "";

      var _copyright_stat = "0";
      var _ori_article_type = "";

      var nickname = "佰医汇";
      var appmsg_type = "6";
      var ct = "1462258975";
      var publish_time = "2016-05-03" || "";
      var user_name = "gh_8bba0a5a63a5";
      var user_name_new = "";
      var fakeid   = "";
      var version   = "";
      var is_limit_user   = "0";
      var round_head_img = "http://mmbiz.qpic.cn/mmbiz/YFc2yChKf60ds3CENf925dYxfibEqHFJmXF0Jib2k72bhTE3gmUqIcN1hsSlFcqXiaQNRP40LiaKzDDFl0pGqViaLwg/0";
      var msg_title = "关于“佰医汇”";
      var msg_desc = "“佰医汇”是国内唯一一家按国内医院科室主流排行榜甄选专家，并实行实名邀请制的移动医疗平台。";
      var msg_cdn_url = "http://mmbiz.qpic.cn/mmbiz/YFc2yChKf61B60kJjTEnseFKmXuDkKkChfn52qTV1dBsMr6bSnXqaLrYhQic4DCsEMaMIm2bSdBdYf5UyR5yicpA/0?wx_fmt=jpeg";
      var msg_link = "http://mp.weixin.qq.com/s?__biz=MzA4MDQ2Nzg4MA==&amp;mid=500707722&amp;idx=1&amp;sn=c3f51b92380f5cb21d1d87f1aa5b5779#rd";
      var user_uin = "0"*1;
      var msg_source_url = '';
      var img_format = 'jpeg';
      var srcid = '';
      var networkType;
      var appmsgid = '' || '500707722'|| "";
      var comment_id = "0" * 1;
      var comment_enabled = "" * 1;
      var is_need_reward = "0" * 1;
      var is_https_res = ("" * 1) && (location.protocol == "https:");

      var devicetype = "";
      var source_username = "";  
      var profile_ext_signature = "" || "";
      var reprint_ticket = "";
      var source_mid = "";
      var source_idx = "";

      var show_comment = "";
      var __appmsgCgiData = {
            can_use_page : "0"*1,
            is_wxg_stuff_uin : "0"*1,
            card_pos : "",
            copyright_stat : "0",
            source_biz : "",
            hd_head_img : "http://wx.qlogo.cn/mmhead/Q3auHgzwzM50j2nVic2ibIJxv5thoc9yWh6hYD9vcgU34zATyDa0rUcw/0"||(window.location.protocol+"//"+window.location.host + "http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/images/pic/appmsg/pic_rumor_link.2x264e76.jpg")
      };
      var _empty_v = "http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/images/pic/pages/voice/empty26f1f1.mp3";

      var copyright_stat = "0" * 1;

      var pay_fee = "" * 1;
      var pay_timestamp = "";
      var need_pay = "" * 1;

      var need_report_cost = "0" * 1;
      var use_tx_video_player = "0" * 1;

      var friend_read_source = "" || "";
      var friend_read_version = "" || "";
      var friend_read_class_id = "" || "";
      
            window.wxtoken = "";
          if(!!window.__initCatch){
        window.__initCatch({
            idkey : 27613,
            startKey : 0,
            limit : 128,
            reportOpt : {
                uin : uin,
                biz : biz,
                mid : mid,
                idx : idx,
                sn  : sn
            },
            extInfo : {
                network_rate : 0.01    
            }
        });
    }
  </script>
  <script type="text/javascript">
          seajs.use('appmsg/index.js');
  </script>

    </body>
</html>

 
