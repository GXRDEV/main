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
    <title>Insert title here</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css" />
    <link rel="stylesheet" href="/sea-modules/select2/css/select2.min.css" type="text/css" />
    <link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css"></link>
	<link rel="stylesheet" href="/css/view/admin.css" />
    <link rel="stylesheet" href="/sea-modules/webuploader/webuploader.css">
</head>
<style>
    input{ margin-bottom: 0 !important}
    .size{ font-size: 14px; color: #666; }
	.main-box{ width: 100%; }
    .inline{ display: inline-block; }
	.del-color{ color: #ff6666 }
	.done-color{ color: #0086d1 }
	h3 { color: #000; font-size: 18px; font-weight: normal; }
	.marginBottom{ margin-bottom: 20px }
	label{ display: inline-block; text-align: right; font-weight: normal;}
	.user input,.select { border: none; outline: none; background: #f5f5f5; height: 35px; width: 130px; margin: 0 0 0 0.5em; padding: 0 15px; border-radius: 0; box-shadow: none; vertical-align: middle}
	.select {position: relative;}
	.select-box{ position: absolute; width: 100%; left: 0; top: 35px; background: #f5f5f5; display: none;}
	.selected{ position: absolute; width: 100%; height: 100%; margin: 0; line-height: 35px;}
	.select span{ position: absolute; right: 15px; top: 12px; width: 0; height: 0; border-left: 5px solid transparent; border-right: 5px solid transparent; border-top: 10px solid #c9c9c9;}
	.user-line .inline:first-child{ min-width: 35% }
	.user-line .inline .first-label{ width: 105px; display: inline-block; text-align: right}
	.user-line .inline:first-child input{ width: 200px;} 
	.choice{ padding: 5px 22px; color: #666; background: #f5f5f5; border: 1px solid #ddd; border-radius: 3em; cursor: pointer; margin-right: 30px}
	.choiced{ color: #fff; background: #0086d1; border: 1px solid #0086d1; border-radius: 3em ;cursor: pointer}
	.btn-box{ text-align: center; }
	.btn-box button{ margin: 35px 0 0; padding: 7px 24px; }
    .is-urgent input[type="radio"]{ display: none;}
    .is-urgent input + i{ display: inline-block; width: 15px; height: 15px; border-radius: 50%; background: #eee; vertical-align: bottom; margin: 0 8px 2px 0;}
    .is-urgent input + i:after{ content: ""; display: inline-block; width: 9px;  height: 9px; background: #ccc; border-radius: 50%; margin: 3px }
    .is-urgent input:checked + i{ background: #cce7f6}
    .is-urgent input:checked + i:after{ background: #0086d1}
    .select2{ width: 220px!important}
    .select2-selection__rendered{ text-align: left}
    .report{  position: absolute; top: 60px; background: #f8f8f8; width: 185px; height: 90px; vertical-align: top; padding: 25px 30px;}
    .right-link{ width: 90px; line-height: 28px; text-align: center; border-radius: 3em; background: #e9e9e9; color: #999; margin: 5px 0 20px}
    .right-link:hover {text-decoration: none; }
    .right-link:last-child{ color: #fff; background: #0086d1}
    .atta-head span{ border: 1px solid #0086d1; border-radius: 3px; padding: 2px 15px; cursor: pointer; color: #0086d1; float: right; margin-top: 17px}
    .atta-main div{ width: 150px; display: inline-block; background: #f8f8f8; position: relative; padding: 0 15px 5px; margin: 0 15px 15px 0 }
    .atta-main div .del{ position: absolute; right: -5px; top: -5px; width: 15px; text-align: center; line-height: 13px; height: 15px; color: #fff; background: red; border-radius: 50%; cursor: pointer }
    .atta-main div p:nth-child(2){ padding-top: 15px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis; margin: 0}
    .atta-main div p:last-child{ text-align: center; margin-top: 3px}
    .atta-main div p:last-child a{  border-radius: 2px; padding: 1px 5px; border: 1px solid #0086d1; color: #0086d1; }
</style>
<body>
    <div class="mark" style="position: fixed; width: 100%; height: 100%; z-index: 10; background: rgba(0, 0, 0, 0.7); padding-top: 150px; text-align: center">
        <img src="../../../img/loading/ajax-loader.gif" alt="" style="width: 70px">
        <p style="color: #fff; font-size: 20px; padding-top: 30px">加载中</p>
    </div>
    <div style="display: none; padding: 15px 30px; border-bottom: 1px solid #bdbdbd; color: #666 " class="head-title">
        会诊状态：<span class="done-color head-state"></span>&emsp;&emsp;
    </div>
    <div style="padding: 5px 30px">
        <div class="main-box size inline">
            <h3>患者信息匹配</h3>
            <div class="user">
                <div class="user-line">
                    <div class="inline marginBottom">
                        <label class="first-label">患者身份证号码<b class="del-color">*</b><span style="display: none">：</span></label>
                        <input type="text" name="idCard">
                    </div>
                    <div class="inline marginBottom">
                        <label class="first-label">患者年龄<b class="del-color">*</b><span style="display: none">：</span></label>
                        <input type="text" name="patientAge">
                    </div>
                </div>
                <div class="user-line">
                    <div class="inline marginBottom">
                        <label class="first-label">患者姓名<b class="del-color">*</b><span style="display: none">：</span></label>
                        <input type="text" name="patientName">
                    </div>
                    <div class="inline marginBottom">
                        <label class="first-label">患者性别<b class="del-color">*</b><span style="display: none">：</span></label>
                        <div class="select inline">
                            <p class="selected"></p><span></span>
                            <div class="select-box">
                                <p style="padding-left: 15px">男</p>
                                <p style="padding-left: 15px">女</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="marginBottom type-head">
                <h3 class="inline" style="width: 120px; margin-right: .5rem">选择会诊方式</h3>
                <div class="inline">
                    <p class="inline choice choice-type" data-toggle="modal" data-target="#myModal" data-id="1">指定专家</p>
                    <p class="inline choice choice-type" data-toggle="modal" data-target="#myModal" data-id="2">交互式</p>
                </div>
            </div>
            <div style="display: none" class="type-main">
                <div class="user-line">
                    <div class="inline marginBottom">
                        <span class="first-label">选择会诊方式：</span>
                        <span class="done-color choice-types"></span>
                    </div>
                    <div class="inline choice-type marginBottom" data-toggle="modal" data-target="#myModal">
                        <span class="first-label">是否紧急</span>：
                        <span class="del-color choice-emergency"></span>
                    </div>
                </div>
                <div class="user-line">
                    <div class="inline choice-type marginBottom" data-toggle="modal" data-target="#myModal">
                        <span class="first-label">会诊医院：</span>
                        <span class="choice-hos"></span>
                    </div>
                    <div class="inline choice-type marginBottom" data-toggle="modal" data-target="#myModal">
                        <span class="first-label">会诊科室</span>：
                        <span class="choice-dep"></span>
                    </div>
                </div>
                <div class="user-line choice-type marginBottom" data-toggle="modal" data-target="#myModal">
                    <div class="inline">
                        <span class="first-label">会诊专家：</span>
                        <span class="choice-exp"></span>
                    </div>
                </div>
            </div>            
        </div>
        <div class="report inline" style="display: none">
            <div class="inline">
                <img src="../../../img/report.png" alt="" style="width: 50px">
                <p style="padding-top: 15px; margin: 0; color: #555">会诊报告</p>
            </div>
            <div style="float: right">
                <a href="javascript:;;" class="right-link" style="display: block">在线查看</a>
                <a href="javascript:;;" class="right-link" style="display: block">下载</a>
            </div>
        </div>
        <div style="overflow: hidden">
            <div class="atta-head" style="clear: both">
                <h3 class="inline" style="float: left">会诊附件信息</h3>
                <span class="up-atta" data-toggle="modal" data-target="#myModal">上传</span>
            </div>
            <div class="atta-main" style="clear: both"></div>
        </div>
        <div class="btn-box size foot">
            <button class="btn btn-primary choiced" style="padding: 5px 40px">提交</button>
            <p style="margin-top: 1em">请注意，提交信息后将无法修改</p>
        </div>
    </div>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none; overflow:hidden">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header" style="border: none">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                </div>
                <div class="modal-body" style="text-align: center; padding: 40px 15px 0">
                    <div class="type">
                        <div style="margin-bottom: 1em">
                            <label>选择医院&ensp;</label>
                            <select name="hos" id="hos">
                                <option value="">--选择医院--</option>
                            </select>
                        </div>
                        <div style="margin-bottom: 1em">
                            <label>选择科室&ensp;</label>
                            <select name="dep" id="dep">
                                <option value="">--选择科室--</option>
                            </select>
                        </div>
                        <div style="margin-bottom: 1em" class="choice-doc">
                            <label>选择专家&ensp;</label>
                            <select name="doc" id="doc">
                                <option value="">--选择专家--</option>
                            </select>
                        </div>
                        <div style="margin-bottom: 1em">
                            <label>是否紧急&ensp;</label>
                            <div class="is-urgent inline" style="width: 220px; text-align: left">
                                <label style="margin-right: 12px">
                                    <input type="radio" name="radio" value="不紧急" data-id="0" checked><i></i>不紧急
                                </label>
                                <label>
                                    <input type="radio" name="radio" value="紧急" data-id="1"><i></i>紧急
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="atta" style="display: none;">
                        <p style="line-height: 280px; font-size: 25px; color: #ababab" class="placeholder">请选择zip或rar文件</p>
                        <div id="uploader" class="wu-example">
                            <div id="thelist" class="uploader-list"></div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer btn-box" style="border: none;background: none; padding-bottom: 20px">
                    <button class="btn btn-primary choiced type-btn" style="padding: 5px 40px; margin: 0" data-dismiss="modal" aria-hidden="true">确定</button>
                    <div style="position: relative">
                        <button class="btn btn-primary choiced atta-btn" style="padding: 5px 40px; margin: 0 0 0 110px" data-dismiss="modal" aria-hidden="true" disabled>上传</button>
                        <div id="picker" style="left: 70px; border-radius: 3em">选择文件</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="/js/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/sea-modules/select2/js/select2.min.js"></script>
    <script src="/sea-modules/webuploader/webuploader.js"></script>
    <script>
    </script>
    <script>
        var ischange, orderId, consultationTypeId, thirdDoctorId, thirdDepartmentId, thirdHospitalId, emergency, attechmentlist=[], attechmentlists=[], attachmentIds=[], typeId=1;
        var id = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, age = /^([0-9]|[0-9]{2}|100)$/;
        var obj = {
            '00': '退回申请',
            '10': '待前质控',
            '20': '待分配',
            '30': '待报告',
            '40': '待审核',
            '50': '完成',
            '90': '取消会诊',
            '15': '待二级前质控'
        }
        var uploader = WebUploader.create({
            swf: '../../../sea-modules/webuploader/Uploader.swf',
            server: '/doctor/uploadFileNew',
            pick: '#picker',
            resize: false,
        	formData: {},
            accept: {
                title: 'File',
                extensions: 'zip,rar',
                mimeTypes: '.zip,.rar'
            }
        });
        uploader.on('fileQueued', function( file ) {
            $('.placeholder').hide()
            $('.uploader-list').html( 
                '<div id="' + file.id + '" class="item">' +
                    '<img src="../../../img/zip.jpg" />'+
                    '<input type="text" value="'+file.name+'" name="fileName" />'+
                '</div>' 
            )
            $('.atta-btn').attr('disabled', false)
        });
        uploader.on( 'uploadProgress', function( file, percentage ) {
            var $li = $( '#'+file.id ),
                $percent = $li.find('.progress .progress-bar');
            // 避免重复创建
            if ( !$percent.length ) {
                $percent = $('<div class="progress progress-striped active">' +
                '<div class="progress-bar" role="progressbar" style="width: 0%">' +
                '</div>' +
                '</div>').appendTo( $li ).find('.progress-bar');
            }
            showTipE('上传中')
            $percent.css( 'width', percentage * 100 + '%' );
        })
        uploader.on( 'uploadSuccess', function( file, response) {
            setTimeout( function (){
                showTipE('上传成功')
            }, 500)
            attechmentlists.push({
                fileName: $('input[name="fileName"]').val(),
                url: response.urlpath,
                id: response.upid
            })
            $('.atta-main').append(
                '<div>'+
                    '<p class="del">&times;</p>'+
                    '<p>名称：<span>'+$('input[name="fileName"]').val()+'</span></p>'+
                    '<img src="../../../img/zip.jpg" alt="">'+
                    '<p class="operation">'+
                        '<a href="'+response.urlpath+'">下载</a>'+
                    '</p>'+
                '</div>'
            );
            clears()
        });
        uploader.on( 'uploadError', function( file ) {
            setTimeout( function (){
                showTipE('上传出错')
            }, 500)
            clears()
        });
        uploader.on( 'uploadComplete', function( file ) {
            $( '#'+file.id ).find('.progress').fadeOut();
        })
        $('.atta-btn').click(function (){
            uploader.upload()
        })
        function clears(){
            $('.atta-btn').attr('disabled', true);
            $('.item').remove()
            $('.placeholder').show()
        }        
        orderId = window.location.href.slice(window.location.href.indexOf('=')+1)
        $.get('/xinyi/order/info', {orderId: orderId}).done(function(data){
            $('input[name=patientName]').val(data.caseInfo.contactName)
            $('input[name=patientAge]').val(data.caseInfo.age);
            (patientSex = data.caseInfo.sex) == 1 ? $('.selected').text('男') : $('.selected').text('女')
            $('input[name=idCard]').val(data.caseInfo.idNumber || '')
            data.files && data.files.forEach( function (item, ind){
                $('.atta-main').append(
                    '<div>'+
                        '<p class="del">&times;</p>'+
                        '<p>名称：<span>'+item.fileName+'</span></p>'+
                        '<img src="../../../img/zip.jpg" alt="">'+
                        '<p class="operation">'+
                            '<a href="'+item.fileUrl+'">下载</a>'+
                        '</p>'+
                    '</div>'
                );
                attechmentlists[ind] = {}
                attechmentlists[ind].fileName = item.fileName
                attechmentlists[ind].url = item.fileUrl
                attechmentlists[ind].id = item.id
            });
            (ischange = data.doneApp) == 'false' && gethos()
            $('.mark').hide()
            if(ischange != 'false'){
                $('.report,.type-main').show();
                $('input[type="text"]').css({'background': '#fff', 'padding': 0, 'height': '20px', 'width': 'auto'}).attr('disabled', true);
                $('.select').css({'background': '#fff', 'padding': 0, 'height': '20px'}).children('.selected').show().siblings().hide();
                $('.selected').css('line-height', '20px')
                $('.choice-type').attr('data-toggle', '')
                $('.main-box').css('width', '75%')
                $('.type-head,.up-atta,.del,.foot').hide();
                $('input[name=patientName]').val(data.info.patientName)
                $('input[name=patientAge]').val(data.info.patientAge);
                data.info.sex == 1 ? $('.selected').text('男') : $('.selected').text('女')
                $('input[name=idCard]').val(data.info.idCard || '')
                $('.choice-types').text(data.info.consultationTypeId == 1 ? '指定专家' : '交互式')
                $('.choice-emergency').text(data.info.emergency == 1 ? '紧急' : '不紧急')
                $('.choice-hos').text(data.info.hospitalName)
                $('.choice-dep').text(data.info.departmentName)
                $('.choice-exp').text(data.info.doctorName)
                $('.head-title').show().children('.head-state').text(obj[data.info.status])
            }
        })
        function showTipE (txt,cls){
            var id = 'tip'+(+new Date);
            $('body').append('<div id="'+ id +'" class="deling"><div class="noresult '+cls+'">'+ txt +'</div></div>');
            window.setTimeout(function(){
                $('#' + id).fadeOut(800,function(){
                    $('#' + id).remove()
                });			
            }, 1000);
        }
        function gethos (){
            $.get('/xinyi/hospital/get').done(function(data){
                var hos = data.hospitals, hoslist=''
                for(var i in hos){
                    hoslist+='<option value="'+hos[i].hospitalName+'" data-id="'+hos[i].hospitalId+'">'+hos[i].hospitalName+'</option>'
                }
                $('#hos').html(hoslist).select2()
                getdep($('#hos option:selected').attr('data-id'))
            })
        }
        
        function getdep(hospitalId){
            $.get('/xinyi/department/get',{ hospitalId: hospitalId }).done( function(data){
                var dep = data.departments, deplist;
                for(var i in dep){
                    deplist += '<option value="'+dep[i].departmentName+'" data-id="'+dep[i].departmentId+'">'+dep[i].departmentName+'</option>' 
                }
                $('#dep').html(deplist).select2()
                typeId==1 && getdoc($('#dep option:selected').attr('data-id'))
            })
        }
        function getdoc(departmentId){
            $.get('/xinyi/doctor/get', { departmentId: departmentId }).done(function(data){
                var doc = data.doctors, doclist
                for(var i in doc){
                    doclist += '<option value="'+doc[i].doctorName+'" data-id="'+doc[i].doctorId+'">'+doc[i].doctorName+'</option>' 
                }
                $('#doc').html(doclist).select2()
            })
        }
        // 匹配身份证
        $('html')
         .delegate('.selected','click', function(e){
            if(ischange != 'false') return;
            $('.select-box').show()
            e.stopPropagation();
        }) 
        .delegate('.select-box, body', 'click', function(){
            $('.select-box').hide()
        })
        .delegate('.select-box p', 'click', function(){
            $('.selected').text($(this).text())
        })
        .delegate('.choice-type', 'click', function (){
            $('.type').show().siblings().hide().parent().next().children('.type-btn').show().siblings().hide()
        })
        .delegate('.choice', 'click', function(){
            typeId = $(this).data('id')
            if(typeId == 1){
                 $('.choice-doc').show()
            } else {
                $('.choice-doc').hide()
            }
        })
        .delegate('#hos', 'change', function(){
            getdep($('#hos option:selected').attr('data-id'))
        })
        .delegate('#dep', 'change', function(){
            typeId==1 && getdoc($('#dep option:selected').attr('data-id'))
        })
        .delegate('.type-btn', 'click', function(){
            consultationTypeId = typeId
            $('.type-main').show()
            $('.choice-emergency').text($('input[name="radio"]:checked').val())
            $('.choice-hos').text($('#hos').val())
            $('.choice-dep').text($('#dep').val())
            if(consultationTypeId == 1){
                $('.choice-exp').text($('#doc').val())
                $('.type-head div').children("p:first-child").addClass('choiced').siblings().removeClass('choiced')
                $('.choice-exp').parent().parent().show()
                $('.choice-types').text('指定专家')
            } else {
                $('.choice-exp').text('')
                $('.type-head div').children("p:last-child").addClass('choiced').siblings().removeClass('choiced')
                $('.choice-exp').parent().parent().hide()
                $('.choice-types').text('交互式')
            }
        })
        .delegate('.up-atta', 'click', function(){
            $('.atta').show().siblings().hide().parent().next().children('.type-btn').hide().siblings().show()
        })
        .delegate('.del', 'click', function (){
            var ind = $(this).parent().index()
            attechmentlists.splice(ind, 1)
            $(this).parent().remove()
        })
        .delegate('.foot button', 'click', function (){
            var thirdDoctorName = $('.choice-exp').text(), thirdDepartmentName = $('.choice-dep').text(), thirdHospitalName = $('.choice-hos').text();
            idCard = $('input[name="idCard"]').val()
            patientName = $('input[name="patientName"]').val()
            patientAge = $('input[name="patientAge"]').val()
            patientSex = $('.selected').text() == '男' ? 1 : 0;
            thirdDoctorId = $('#doc option:selected').attr('data-id')
            thirdDepartmentId = $('#hos option:selected').attr('data-id')
            thirdHospitalId = $('#dep option:selected').attr('data-id')
            emergency = $('input[name="radio"]:checked').attr('data-id')
            if(id.test(idCard) == false) return showTipE('请输入正确的身份证号码', 'error'), false;
            if(!patientName) return showTipE('请输入患者姓名', 'error'), false;
            if(!(patientSex == 1 || patientSex == 0)) return showTipE('请选择患者性别', 'error'), false;
            if(age.test(patientAge) == false) return showTipE('请输入正确的年龄', 'error'), false;
            if( !consultationTypeId) return showTipE('请选择会诊方式', 'error'), false;
            consultationTypeId == 0 && ( thirdDoctorId = thirdDoctorName = '')
            attechmentlists.forEach( function (item, ind){
                attechmentlist[ind] = {}
                attechmentlist[ind].filename = item.fileName
                attechmentlist[ind].url = item.url
                attachmentIds.push(item.id)
            })
            $.post('/xinyi/order/submit', {
                orderId: orderId,
                idCard: idCard,
                patientName: patientName,
                patientAge: patientAge,
                patientSex: patientSex,
                thirdDoctorId: thirdDoctorId,
                consultationTypeId: consultationTypeId,
                thirdDepartmentId: thirdDepartmentId,
                thirdHospitalId: thirdHospitalId,
                thirdDoctorName: thirdDoctorName,
                thirdDepartmentName: thirdDepartmentName,
                thirdHospitalName: thirdHospitalName,
                emergency: emergency,
                attachments: JSON.stringify({attechmentlist}),
                attachmentIds: attachmentIds.join()
            }).done( function (data){
                showTipE('提交成功')
                setTimeout(function(){
                    window.location.reload()
                }, 500)
            })
        })
    </script>
</body>
</html>