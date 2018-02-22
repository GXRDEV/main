<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>案例详情</title>
        <link rel="stylesheet" href="/css/matrix-style2.css" />
        <link rel="stylesheet" href="/css/bootstrap.min.css" />
        <link rel="stylesheet" href="/font-awesome/css/font-awesome.css" />
        <link rel="stylesheet" href="/fonticon/base/iconfont.css"></link>
        <link rel="stylesheet" href="/css/flex.css"></link>
        <link rel="stylesheet" href="/sea-modules/select2/css/select2.min.css" type="text/css" />
        <link rel="stylesheet" href="/sea-modules/webuploader/webuploader.css">
        <style>
            button{ border-radius: 0}
            textarea{ resize: none}
            .f10{ margin-left: 15px}
            .flex{ margin-bottom: 20px}
            .flex:first-child{ margin-top: 50px}
            input,textarea{ width: 350px; margin: 0!important}
            .fileBoxUl{ margin: 20px 0 0; min-width: 390px}
            .fileBoxUl li { margin: 0 15px 35px 0 !important}
            .select2 { width: 365px!important}
            .select2-container--open .select2-dropdown{ left: 80px!important}
            .hide{ display: none !important}
            .diySuccess{ display: block !important}
        </style>
    </head>
    <body style="margin: 0 80px; overflow: hidden; ">
        <div class="flex acenter">
            <span>案例标题</span>
            <div class="f10">
                <input type="text" name="titles">
            </div>
        </div>
        <div class="flex acenter">
            <span>会诊科室</span>
            <div class="f10">
                <select id="dep" name="userId">
                    <option>--选择会诊科室--</option>
                </select>
            </div>
        </div>
        <div class="flex acenter">
            <span>案例简介</span>
            <div class="f10">
                <textarea name="info" id="" cols="30" rows="3"></textarea>
            </div>
        </div>
        <div class="flex pic hide" style="margin: 0;">
            <span>图文病例</span>
            <div style="margin: 0 15px; position: relative; width: 350px" >
                <input type="text" placeholder="输入图像相关描述信息" style="width: 240px;" name="caseDesc">
                <div id="picsrelatedPics" class="diyUpload">
                    <div class="parentFileBox">
                        <ul class="fileBoxUl clearfix">
                            <li class="browser" style="display:none">
                                <div class="viewThumb">
                                    <img >
                                </div>
                                <div class="diyCancel">
                                    <i class="iconfont" title="删除"></i>
                                </div>
                                <div class="diySuccess"></div>
                            </li>
                            <li class="actionAdd" style="position: absolute; top:0; right: 72px">
                                <div id="addrelatedPics">上传图片</div>
                            </li>
                        </ul>
                    </div>	
                    <input type="hidden" id="headImageUrl" name="headImageUrl"/>			
                </div>
            </div>
        </div>
        <div class="flex acenter video hide">
            <span>视频地址：</span>
            <div>
                <input type="text" id="vedioUrl" name="vedioUrl" style="float: left;width: 240px;" placeholder="选择视频地址" disabled>
                <div id="filePicker" style="position:relative;float:left;margin-left: 10px;">选择视频</div>
                <input type="hidden" name="duration" id="duration">
            </div>
        </div>
        <div class="flex acenter">
            <span>背景图片：</span>
            <div>
                <input type="text" id="bgimgUrl" name="bgimgUrl" style="float: left;width: 240px;" placeholder="选择背景图片地址" disabled>
                <div id="imgUpload" style="position:relative;float:left;margin-left: 10px;">选择图片</div>
                <input type="hidden" id="durations">
            </div>
        </div>
        <div class="flex acenter">
            <span>诊断结果</span>
            <div class="f10">
                <textarea name="result" id="" cols="30" rows="3"></textarea>
            </div>
        </div>
        <div>
            <button class="btn btn-primary save" style="width: 100px; margin: 0 20px">保存</button>
            <button class="btn btn-primary cancel" style="width: 100px;background: #ff6666">取消</button>
        </div>
        <div style="position: absolute; left: 0; top: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.3); display: none; padding: 215px 0 0 45%" class="tcenter loading">
            <img src="../../../img/loading/ajax-loader.gif" width="50px">
            <p class="process" style="font-size: 18px">0%</p>
            <p class="errors" style="display: none; color: #f53131">上传失败</p>
        </div>
        <script src="/js/jquery.min.js"></script>
        <script src="/js/bootstrap.min.js"></script>
        <script src="/sea-modules/select2/js/select2.min.js"></script>
        <script src="/sea-modules/webuploader/webuploader.js"></script>
        <script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	    <script src="/sea-modules/seajs/seajs.config.js"></script>
        <script>
            var types, id, select;
            var _burl='/',_h = _burl,_id = '';
            seajs.use('view/admin/main',function(controller){
                controller.exportinfo();
            });
            function getUrl(name){
                var reg = new RegExp(name + "=([^&]*)(&|$)", "i");   
                var r = window.location.search.substr(1).match(reg);
                if (r != null) return r[1]; return null;	
            }
            types = getUrl('type')
            id = getUrl('id')
            types == '5' ? $('.pic').removeClass('hide') : $('.video').removeClass('hide')
            /*************会诊科室**************/
            function depName (dep){
                $.get('/system/standsdeparts').done( function (data){
                    var deps = '', alldep = data.stands;
                    for(var i in alldep){
                        deps+='<option value='+alldep[i].id+' title='+alldep[i].id+'>'+alldep[i].displayName+'</option>'
                    }
                    select = $('#dep').html(deps).select2({ multiple: true })
                    id && select.val(dep.map( function (i){ return i.standardDepId })).trigger('change')
                })
            }
            !id && depName()
            id && $.get('/system/hiscase/get',{hisCaseUuid: id}).done(function (data){
                var attachment = ''
                $('input[name=titles]').val(data.hisCaseInfo.title)
                $('textarea[name=info]').val(data.hisCaseInfo.mainSuit)
                $('textarea[name=result]').val(data.hisCaseInfo.treatAdvice)
                $('#bgimgUrl').val(data.hisCaseInfo.backImageUrl)
                $('input[name=caseDesc]').val(data.historyCaseAttachment.title)
                data.attachments && (attachment = data.attachments.map(function (val){
                    return '<li class="browser" data-id='+val.id+'>'+
                            '<div class="viewThumb">'+
                                '<img data-src='+val.fileUrl+' src='+val.fileUrl+'>'+
                            '</div>'+
                            '<div class="diyCancel">'+
                                '<i class="iconfont" title="删除"></i>'+
                            '</div>'+
                            '<div class="diySuccess"></div>'+
                            '<div class="diyFileName">'+val.fileName+'</div>'+
                        '</li>'
                }))
                types == 5 && $('.fileBoxUl').append(attachment.join(''))
                types == 4 && $('#vedioUrl').val(data.attachments[0].fileUrl) && $('#duration').val(data.attachments[0].id)
                depName(data.deps)
            })
            function init(id, url, duration,accept){
                var uploader = WebUploader.create({
                    auto: true,
                    swf: '../../../sea-modules/webuploader/Uploader.swf',
                    server: '/doctor/uploadFileNew',
                    pick: {
                        id: id,
                        multiple: false
                    },
                    accept: accept
                });
                // 文件上传过程中实时显示进度
                uploader.on( 'uploadProgress', function( file, percentage ) {
                    $('.loading').show().find('.process').html( (percentage*100).toFixed(0) +'%')
                });
                // 上传成功
                uploader.on( 'uploadSuccess', function( file, response ) {
                    $(url).val(response.urlpath)
                    $(duration).val(response.upid)
                    $('.loading').hide()
                });
                // 上传失败，显示上传出错。
                uploader.on('uploadError', function( file ) {
                    alert('上传失败，请重试')
                    $('.loading').hide()
                });
            }
            init('#filePicker', '#vedioUrl', '#duration',  { title: 'File', extensions: 'mp4,mov,avi,wmv,rmvb,mkv', mimeTypes: 'video/*' })
            init('#imgUpload', '#bgimgUrl', '#durations', {title: 'Images', extensions: 'gif,jpg,jpeg,bmp,png', mimeTypes: 'image/*'})
            $('body') 
            .delegate('.save', 'click', function(){
                var depid = [], attach = [];
                $.each($("#dep").select2('data'), function (i, val){
                    depid.push(val.title)
                })
                types == 5 && $('.fileBoxUl').find('.browser').each(function() {
                    if($(this).attr('data-id')){
                        attach.push($(this).attr('data-id'))
                    }                    
                })
                types == 4 && attach.push($('#duration').val())
                var obj = !($('input[name=titles]').val() && depid.length && $('textarea[name=info]').val() && $('textarea[name=result]').val() && $('#bgimgUrl').val())
                if(types == 5){
                    if(!attach.length || obj || !$('input[name=caseDesc]').val()){
                        alert('请填写完整图文案例信息')
                        return
                    }
                } else {
                    if(!$('#vedioUrl').val() || obj){
                        alert('请填写完整视频案例信息')
                        return
                    }
                }
                $.post('/system/hiscase/save', {
                    hisCaseUuid: id ||'',
                    type: types,
                    title: $('input[name=titles]').val(),
                    depIds: depid.join(),
                    mainSuit: $('textarea[name=info]').val(),
                    caseDesc: $('input[name=caseDesc]').val(),
                    attachmentIds: attach.join(),
                    treatAdvice: $('textarea[name=result]').val(),
                    backImageUrl: $('#bgimgUrl').val()
                }).done( function (){
                    window.location.href = '/system/hiscase/manage'
                })
            })
            .delegate('.cancel', 'click', function (){
                if(confirm("您确定要取消吗？")){
                    window.location.href = '/system/hiscase/manage'
                }
            })
        </script>
    </body>
</html>