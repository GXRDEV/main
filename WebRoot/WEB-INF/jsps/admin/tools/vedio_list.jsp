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
        <title>视频列表</title>
        <link rel="stylesheet" href="/css/matrix-style2.css" />
        <link rel="stylesheet" href="/css/view/videolist.css" />
        <link rel="stylesheet" href="/css/view/videolistwidthbtn.css" />
        <link rel="stylesheet" href="/css/bootstrap.min.css" />
        <link href="/font-awesome/css/font-awesome.css" rel="stylesheet" />
        <link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
        <link rel="stylesheet" href="/sea-modules/webuploader/webuploader.css">
        <link rel="stylesheet" type="text/css" href="/sea-modules/datetimepicker/bootstrap-datetimepicker.css" />
        <link rel="stylesheet" href="/sea-modules/select2/css/select2.min.css" type="text/css" />
        <style>
            .tcenter{ text-align: center}
            .img{ width: 100px; height: 100px}
            .progress{ position: absolute; top: 0; right: 0;}
            input{ margin: 0 !important}
            .time{ margin: 10px 0}
            th{ font-weight: 400!important}
            .select2{ width: 220px!important}
            .select2-dropdown{ z-index: 99999}
            .form-horizontal .controls{
                padding: 0; text-align: left;
                margin-left: 120px;
            }
            .form-horizontal .control-label{
                width: 110px;
            }
            .modal{
                width: 666px;
                margin-left: -333px;
            }
        </style>
    </head>
    <body>
        <div class="mainlist" style="padding-bottom: 0">
            <div class="container-fluid">
                <div class="row-fluid">
                    <div class="span12">
                        <div class="addarea"><button class="btn btn-primary" data-toggle="modal" data-target="#myModal" style="width: 100px; margin: 0 20px">上传</button></div>
                        <div class="widget-box">
                            <div class="widget-content nopadding">
                                <table class="table table-bordered data-table">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>会诊嘉宾</th>
                                            <th>标题</th>
                                            <th>视频开始播放时间</th>
                                            <th>视频时长</th>
                                            <th>聊天室生效时间</th>
                                            <th>聊天室关闭时间</th>
                                            <th>操作</th>
                                        </tr>
                                    </thead> 
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none">
            <div class="modal-dialog">
                <div class="modal-content" style="position: relative">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title tcenter" id="myModalLabel">视频上传</h4>
                    </div>
                    <div class="modal-body tcenter form-horizontal" style="position: static;">
                        <div class="control-group">
                            <label class="control-label" for="titles">&emsp;标&emsp;&emsp;题：</label>
                            <div class="controls clearfix">
                                <input type="text" id="titles" name="titles">
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="chatStart">聊天室开始：</label>
                            <div class="controls clearfix">
                                <input id="chatStart" class="form-control" type="text" name="singledatePicker" value=""/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="chatEnd">聊天室结束：</label>
                            <div class="controls clearfix">
                                <input id="chatEnd" class="form-control" type="text" name="singledatePicker" value="" />
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="startTime">视频开始：</label>
                            <div class="controls clearfix">
                                <input id="startTime" class="form-control" type="text" name="singledatePicker" value=""/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="info">会诊详情：</label>
                            <div class="controls clearfix">
                                <textarea id="info" name="info" rows="2" style="resize: none"></textarea>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="user">会诊嘉宾：</label>
                            <div class="controls clearfix">
                                <select id="user" name="userId">
                                    <option>--选择会诊嘉宾--</option>
                                </select>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="vedioUrl">视频地址：</label>
                            <div class="controls clearfix">
                                <input type="text" id="vedioUrl" name="vedioUrl" style="float:left;">
                                <div id="filePicker" style="position:relative;float:left;margin-left: 10px;">选择视频</div>
                                <input type="hidden" name="duration" id="duration">
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" onclick="saveToDB()">保存</button>
                    </div>
                    <div style="position: absolute; left: 0; top: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.3); display: none" class="tcenter loading">
                        <img src="../../../img/loading/ajax-loader.gif" width="50px" style="margin: 120px 0 10px">
                        <p class="process" style="font-size: 18px">0%</p>
                        <p class="errors" style="display: none; color: #f53131">上传失败</p>
                    </div>
                </div>
            </div>
        </div>
        <script src="/js/jquery.min.js"></script>
        <script src="/js/bootstrap.min.js"></script>
        <script src="/sea-modules/webuploader/webuploader.js"></script>
        <script src="/js/datetimepicker/bootstrap-datetimepicker.js"></script>
        <script src="/js/datetimepicker/bootstrap-datetimepicker.zh-CN.js"></script>
        <script src="/js/dataTables/js/jquery.dataTables.min.js" type="text/javascript"></script>
        <script src="/sea-modules/select2/js/select2.min.js"></script>
        <script>
            var oTable;
            $(document).ready(function () {            	
            	oTable = $('.data-table').DataTable({
                    "bJQueryUI": true,"sDom": '<".example_processing"r><".tablelist"t><"F"<"#filteropt">flp>',
                    "iDisplayStart":0,"iDisplayLength":10,
                    "lengthMenu": [ 10, 15, 20, 25, 50, 100],
                    "bDestroy":true,"bRetrieve":true,"bStateSave":true,
                    "bServerSide": true,"fnServerData": retrieveData,
                    "sAjaxSource": "/system/liveplandatass",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,   
                    "columns":[
                        { "data": "id" },
                        { "data": "docName" },
                        { "data": "title" },
                        { "data": "beginTimes" },
                        { "data": "durationStr" },
                        { "data": "chatRoomStartTimes" },
                        { "data": 'chatRoomCloseTimes' },
                        { "data": null }
                    ],
					"fnCreatedRow": function (nRow, aData, iDataIndex) {
						var ops='<a href="/system/planliveInfo?liveId='+aData['id']+'" class="editbtn linebtn">查看详情</a>';
                        $('td:eq(7)', nRow).html(ops);
                    },
                    "oLanguage": {
                        "sProcessing": "正在获取数据，请稍后...",
                        "sLengthMenu": "每页显示 _MENU_ 条",
                        "sZeroRecords": "没有您要搜索的内容",
                        "sInfoEmpty": "记录数为0",
                        "sInfoFiltered": "(全部记录数 _MAX_ 条)",
                        "sSearch": "搜索 ",
                        "oPaginate": {
                            "sFirst": "第一页",
                            "sPrevious": "上一页",
                            "sNext": "下一页",
                            "sLast": "最后一页"
                        }
                    },
                    "initComplete": function(settings, json) {
                    }
                });
                function retrieveData( sSource, aoData, fnCallback ) {
                    aoData.push( { "name": "search", "value":$('input[type="search"]').val()} );
                    $.ajax( {  
                        "type": "post",   
                        "contentType": "application/json",  
                        "url": sSource,   
                        "dataType": "json",  
                        "data": JSON.stringify(aoData),
                        "success": function(resp) {  
                            fnCallback(resp);
                        }  
                    });  
                }  
            })
            /*************日历控件**************/
            var opt = {
                language: 'zh-CN',
                autoclose: true,
                startDate: new Date(),
                pickerPosition: 'bottom-left',
                format: 'yyyy-mm-dd hh:ii:ss',
            };
            $("#chatStart").datetimepicker(opt).on('changeDate', function (params) {
                var start = $("#chatStart").val()
                var end = $("#chatEnd").val()
                var cur = $("#startTime").val()
                if (start && end && (start >= end || cur <= start)) {
                    $("#chatStart").val('')
                    alert('聊天室开始时间必须在聊天室结束时间和视频开始时间之前')
                }
            })
            $("#chatEnd").datetimepicker(opt).on('changeDate', function (params) {
                var start = $("#chatStart").val()
                var end = $("#chatEnd").val()
                var cur = $("#startTime").val()
                if (start && end && (end <= start || cur >= end)) {
                    $("#chatEnd").val('')
                    alert('聊天室结束时间必须在聊天室开始时间和视频开始时间之后')
                }
            })
            $("#startTime").datetimepicker(opt).on('changeDate', function (params) {
                var start = $("#chatStart").val()
                var end = $("#chatEnd").val()
                var cur = $("#startTime").val()
                if (start && end && (cur <= start || cur >= end)) {
                    $("#startTime").val('')
                    alert('视频开始时间必须在聊天室开始时间和聊天室结束时间之间')
                }
            })
            /*************会诊嘉宾**************/
            $('#user').select2({
                ajax: {
                    url: '/docadmin/loadExpertOrDoctors',
                    data: function (params) {
                        var query = {
                            keywords: params.term,
                            pageNumber: params.page || 1,
                            pageSize: 10
                        }
                        return query;
                    },
                    cache: true,  
                    processResults: function (res, params) {  
                        params.page = params.page || 1
                        if (res.pager.list.length) {  
                            var allexp = res.pager.list, options = [];
                            for (var i = 0, len = allexp.length; i < len; i++) {  
                                var option = {  
                                    "id": allexp[i]["specialId"],  
                                    "text": allexp[i]["specialName"],
                                    "title": allexp[i]["specialId"]
                                };
                                options.push(option)
                            }
                            return {  
                                results: options,  
                                pagination: {  
                                    more: (params.page * 10) < res.pager.pageCount 
                                }  
                            };  
                        }  
                    },  
                    escapeMarkup: function (markup) {  
                        return markup;  
                    }                
                }
            })
            /*************视频上传**************/
            var uploader = WebUploader.create({
                swf: '../../../sea-modules/webuploader/Uploader.swf',
                server: '/doctor/uploadFileNew',
                pick: {
                    id: '#filePicker',
                    multiple: false,
                },
                resize: false,
                auto: true,
                formData: {},
                accept: {
                    title: 'File',
                    extensions: 'mp4,mov,avi,wmv,rmvb,mkv',
                    mimeTypes: 'video/*'
                }
            })
            uploader.on('fileQueued', function( file ) {
                // var $li = $('<div id="' + file.id + '" class="file-item thumbnail" style="display: inline-block; width: 128px; height: 128px;margin: 7px;">' +
                //             '<img src="../../../img/vedio.jpg" class="img">' +
                //             '<p class="info" style="overflow: hidden; margin: 5px 0">' + file.name + '</p>' +
                //         '</div>');
                // $('.uploader-list').html( $li )
            });
            // 文件上传过程中实时显示进度
            uploader.on( 'uploadProgress', function( file, percentage ) {
                $('.loading').show().find('.process').html( (percentage*100).toFixed(0) +'%')
            });
            // 上传成功
            uploader.on( 'uploadSuccess', function( file, response ) {
                $('#vedioUrl').val(response.urlpath)
                $('#duration').val(response.duration)
                $('.loading').hide()
            });
            // 上传失败，显示上传出错。
            uploader.on('uploadError', function( file ) {
                alert('上传失败，请重试')
                $('.loading').hide()
            });

            function saveToDB() {
                var form = {
                    title: $('input[name=titles]').val(),
                    userId: $('#user option:selected').attr('title'),
                    beginTime: $('#startTime').val(),
                    consultationDetails: $('textarea[name=info]').val(),
                    chatRoomStartTime: $('#chatStart').val(),
                    chatRoomCloseTime: $('#chatEnd').val(),
                    vedioUrl: $('#vedioUrl').val(),
                    duration: $('#duration').val()
                };
                if(!(form.title && form.userId && form.beginTime && form.consultationDetails && form.chatRoomStartTime && form.chatRoomCloseTime && form.vedioUrl)) {
                    return alert('所有选项都为必填项。');
                }
                $.post('/system/saveorupdateplanlive', form).done( function(){
                    setTimeout(location.reload(), 1000)
                })
            }
        </script>
    </body>
</html>