<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="/">
    <title>轮播图配置</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">    
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" href="/css/jquery.gridly.css" />
    <link rel="stylesheet" href="/css/bootstrap.min.css" />
    <link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
    <link rel="stylesheet" href="/css/view/admin.css" />
  </head>
  <style scoped>
      .gridly {
        position: relative;
      }
      .delete{
      	position: absolute;
        right: 5px;
        top: 0;
        font-size: 20px;
        cursor: pointer;
      }
      .small {
        width: 200px;
        height: 120px;
        border: 1px solid #ccc;
        background: #fff;
        position: relative;
      }
      img{
        width: 200px;
        height: 100px
      }
      .webuploader-pick{
        padding: 0
      }
  </style>
  <body class="exportdetail" style="padding: 20px">
      <div class="main_wx" style="margin-bottom: 20px">
          <p>微信公众号</p>
          <div class="gridly wx" z-index="100"></div>
          <button type="button" class="btn btn-primary Add" data-toggle="modal" data-target="#myModal" data-id="1">添加</button>
      </div>
      <div class="main_doc" style="margin-bottom: 20px">
          <p>专家App</p>
          <div class="gridly doc"></div>
          <button type="button" class="btn btn-primary Add" data-toggle="modal" data-target="#myModal" data-id="2">添加</button>
      </div>
      <div class="main_exp">
          <p>医生App</p>
          <div class="gridly exp"></div>
          <button type="button" class="btn btn-primary Add" data-toggle="modal" data-target="#myModal" data-id="3">添加</button>
      </div>
      <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none">
              <div class="modal-content">
                  <div class="modal-header">
                      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                      <h4 class="modal-title" id="myModalLabel">添加图片</h4>
                  </div>
                  <div class="modal-body">
                        <div>
                            <label>图片</label>
                            <div class='actions'>
                                <div class="controls">
                                    <div class="diyUpload">
                                      <div class="parentFileBox">
                                          <ul class="fileBoxUl clearfix">
                                            <li class="actionAdd">
                                              <div id="addheadImageUrl"><i class="iconfont">&#xe60e;</i></div>
                                            </li>
                                          </ul>
                                      </div>	
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="control-group" style="margin-top: 20px">
                            <label class="control-label">提示文字</label>
                            <div class="controls doublewidth">
                                <input type="text" placeholder="请输入提示文字" class="title"/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">链接地址</label>
                            <div class="controls doublewidth">
                                <input type="text" placeholder="请输入链接地址" class="urls"/>
                            </div>
                        </div>
                  </div>
                  <div class="modal-footer">
                      <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                      <button type="button" class="btn btn-primary wxPre">添加</button>
                  </div>
              </div>
      </div>
      <script type="text/javascript" src="/sea-modules/jquery/1.10.1/jquery.min.js"></script>
      <script src="/js/bootstrap.min.js"></script>
      <script src="/js/jquery.gridly.js"></script>
      <script src="/js/sample.js"></script>
      <script src="/js/rainbow.js"></script>
      <script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	  <script src="/sea-modules/seajs/seajs.config.js"></script>
      <script>
        var _burl='/',_h = _burl,_id = '';
        seajs.use('view/admin/main',function(controller){
          controller.exportinfo();
        });
          $(document).ready(function(){
                obtain(1, '.wx')
                obtain(2, '.doc')
                obtain(3, '.exp')
                //添加
                var ids = 1
                $('.Add').on('click',function() {
                    ids = $(this).data('id')
                })
                $('.wxPre').on('click', function(){
                    var imgUrl = $(this).parent().siblings('.modal-body').find('img').data('src')
                    var titleVal = $(this).parent().siblings('.modal-body').find('.title').val()
                    var urls = $(this).parent().siblings('.modal-body').find('.urls').val()
                    add(ids, imgUrl, titleVal, urls)
                })
                //删除
                $('.gridly').on('click', '.delete', function(){
                    var id, imgUrl, title;
                    id = $(this).siblings('img').data('id');
                    imgUrl = $(this).siblings('img')[0].currentSrc;
                    titleVal = $(this).siblings('p').html()
                    $.ajax({
                        type: 'post',
                        url: '/system/delfigconfig',
                        data:{
                            figId: id,
                        },
                        success: function(){}
                    })
                })
                //移动
                $('.gridly').on('mouseup', '.small',function(){
                    var arr = [], arr1 = [], str = '';
                    $(this).parent().children().each(function( ind, attr, a){
                        arr.push({ id: $(this).attr("data-id"), lefts: parseInt(attr.style.left)})
                    })
                    arr.sort(function( num1, num2){
                        return num1.lefts > num2.lefts
                    })
                    $.each(arr, function (ind, attr){
                        arr1.push(attr.id)
                    })
                    str = arr1.join(';')
                    $.ajax({
                        type: 'post',
                        url: '/system/sortfigconfig',
                        data: {
                            ids: str
                        },
                        success: function(){}
                    })
                })
          })
          // 获取数据
          function obtain (type, className){
              $.ajax({ 
                    type:'post',  
                    url:'/system/gainfigconfigs',
                    data:{
                      type: type
                    },
                    success: function(data){
                        var str= ''
                        var arr = data.cons
                        for (var i in arr) {
                            str += '<div class="brick small" data-id='+ arr[i].id +'>'
                              +'<img src='+arr[i].imageUrl+' alt="图片" data-id='+ arr[i].id +'><p>'+arr[i].title+'</p>'
                              +'<a class="delete" href="#">&times;</a></div>'
                        }
                        $(className).html(str)
                        $('.gridly').gridly();
                    }
                })
          }
          // 添加
          function add (type, imgUrl, titleVal, urls){
              $.ajax({
                type: 'post',
                url: '/system/saveorupdatefigcons',
                data:{
                    appType: type,
                    imageUrl: imgUrl,
                    title: titleVal,
                    linkUrl: urls
                },
                success: function(data){
                    location.reload()
                }
            })
          }
      </script>
  </body>
</html>
