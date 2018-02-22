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
        <title>回顾管理</title>
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
            th{ font-weight: normal !important}
            .stalist{ color: #626262}
        </style>
    </head>
    <body>
        <div class="mainlist" style="padding-bottom: 0">
            <div class="container-fluid">
                <div class="row-fluid">
                    <div class="span12">
                        <div class="addarea">
                            <a class="btn btn-primary" href="/system/hiscase/operate?type=5" style="width: 80px; margin: 0 20px">上传图文</a>
                            <a class="btn btn-primary" href="/system/hiscase/operate?type=4" style="width: 80px; background: #f6ab00">上传视频</a>
                        </div>
                        <div class="widget-box">
                            <div class="widget-content nopadding">
                                <table class="table table-bordered data-table">
                                    <thead>
                                        <tr>
                                            <th>案例标题</th>
                                            <th>创建时间</th>
                                            <th>会诊科室</th>
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
                    "sAjaxSource": "/system/hiscase/list",
                    "bFilter": true,"bLengthChange": true,"bProcessing": true,   
                    "columns":[
                        { "data": "title" },
                        { "data": "createTime" },
                        { "data": "depName" },
                        { "data": null }
                    ],
					"fnCreatedRow": function (nRow, aData, iDataIndex) {
                        var type = $('#filteropt a.curr').attr('order-status') || 5
                        var ops='<a href="/system/hiscase/operate?type='+type+'&id='+aData.hisCaseUuid+'" class="editbtn linebtn">编辑</a>';
                        ops += '<span class="editbtn linebtn del" data-id="'+aData.hisCaseUuid+'" style="cursor: pointer">删除</span>'
                        $('td:eq(3)', nRow).html(ops);
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
                        var tabs='<a href="#" class="stalist curr" order-status="5">图文案例</a>';
                    	tabs+='<a href="#" class="stalist" order-status="4">视频案例</a>';
                    	$('#filteropt').html(tabs);
                    }
                });
                function retrieveData( sSource, aoData, fnCallback ) {
                    aoData.push( { "name": "search", "value":$('input[type="search"]').val()} );
                    aoData.push( { "name": "type", "value":$('#filteropt a.curr').attr('order-status')||'5'} );
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

            $('body')
            .delegate('.del', 'click', function(){
                var id = $(this).attr('data-id')
                $.post('/system/hisCase/delete', { hisCaseUuid: id})
                .done(function (){
                    oTable.page('first').draw(false)
                })
            })
            .delegate('#filteropt a', 'click', function(){
                $(this).addClass('curr').siblings().removeClass('curr');
                oTable.page('first').draw(false)
            })
        </script>
    </body>
</html>