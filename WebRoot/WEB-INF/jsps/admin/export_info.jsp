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
</head>
<link rel="stylesheet" href="/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="/libs/daterang/daterangepicker.css" />
<link rel="stylesheet" href="/sea-modules/select2/css/select2.min.css" />

<style>
    .export-info .header{ overflow: auto; }
    .export-info .header ul{ margin: 0; width: 120%; display: flex}
    .export-info .header li{ flex: 1; list-style: none; text-align: center; line-height: 3em; cursor: pointer; background: #e5e5e5}
    .export-info .header li:hover{ color: lightcoral}
    .export-info .content{ clear: both; margin: 3rem}
    .export-info .content div label{ display: inline-block; width: 10%}
    .calendar{ max-width: 300px !important} 
    .daterangepicker_input{width: 250px !important}
    .export-info .footer {margin-left: 3rem}
    .export-info .widthLabel{ width: 90px !important}
    .export-info .nomargin { margin: 0 !important}
    .hosName,.depName{ display: inline-block; width: 212px; height: 28px; line-height: 28px; padding-left: 6px; border: 1px solid #ccc; border-radius: 4px; cursor: pointer; vertical-align: middle}
    .modal-body{ height: 300px; clear: both}
    .modal-body div span{ width: 50%; float: left;}
    .modal-body div span input{ float: right; margin-right: 1.5em}
    select, input{ margin: 0 !important}
</style>
<body>
    <div class="export-info">
        <div class="header">
            <ul>
                <li data-id="1" style="color: lightcoral">专家登录统计导出</li>
                <li data-id="2">专家服务导出</li>
                <li data-id="3">视频会诊数据导出</li>
                <li data-id="4">图文会诊导出</li>
                <li data-id="5">关注量/报道量导出</li>
                <li data-id="6">医生注册量导出</li>
                <li data-id="7">二维码导出</li>
                <li data-id="8">医生收入导出</li>
            </ul>
            
        </div>
        <div class="content">
            <div style="margin-bottom: 1.2em" class="docName">
                <label>专家姓名</label>
                <input type="text" name="docName" style="margin: 0">
            </div>
            <div style="margin-bottom: 1.2em" class="hos">
                <label>医院</label>
                <select name="hosid" class="bindchange" id="selecthos">
                    <option value="">---请选择---</option>
                </select>
            </div>
            <div style="margin-bottom: 1.2em">
                <label>科室</label>
                <select name="depid" class="deps" id="selectdep">
                    <option value="">---请选择---</option>
                </select>
            </div>
            <div class="timer" style="margin-bottom: 1.2em">
                <label>时间</label>
                <input id="reportrange" class="form-control" type="text" name="daterange" value="" />
            </div>
            <div class="serverType" style="display: none">
                <label>服务类型</label>
                <div style="display: inline-block">
                    <label class="widthLabel">
                        <input type="checkbox" name="isOpenvedio" class="nomargin">&ensp;远程会诊
                    </label>
                    <label class="widthLabel">
                        <input type="checkbox" name="isOpentuwen" class="nomargin">&ensp;图文会诊
                    </label>
                </div>
            </div>
            <div class="orderStatus" style="display: none; margin-top: 5px">
                <label>订单状态</label>
                <div style="display: inline-block">
                    <label class="widthLabel">
                        <input type="radio" data-id="10" name="orderStatus" class="nomargin">&ensp;待接诊
                    </label>
                    <label class="widthLabel">
                        <input type="radio" data-id="20" name="orderStatus" class="nomargin">&ensp;进行中
                    </label>
                    <label class="widthLabel">
                        <input type="radio" data-id="25" name="orderStatus" class="nomargin">&ensp;待复诊
                    </label>
                    <label class="widthLabel">
                        <input type="radio" data-id="30" name="orderStatus" class="nomargin">&ensp;已退诊
                    </label>
                    <label class="widthLabel">
                        <input type="radio" data-id="40" name="orderStatus" class="nomargin">&ensp;已完成
                    </label>
                    <label class="widthLabel">
                        <input type="radio" data-id="50" name="orderStatus" class="nomargin">&ensp;已取消
                    </label>
                </div>
            </div>
            <div class="attention" style="display: none; margin-top: 5px">
                <label>关注/报道</label>
                <div style="display: inline-block">
                    <label class="widthLabel">
                        <input type="radio" value="1" name="attention" class="nomargin" >&ensp;关注量
                    </label>
                    <label class="widthLabel">
                        <input type="radio" value="2" name="attention" class="nomargin">&ensp;报道量
                    </label>
                </div>
            </div>
        </div>
        <div class="footer">
            <a type="button" class="btn btn-primary report">导出</a>
        </div>
    </div>
</body>
	<script src="/js/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
    <script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
    <script type="text/javascript" src="/libs/daterang/moment.min.js"></script>
	<script type="text/javascript" src="/libs/daterang/daterangepicker.js"></script>
    <script src="/sea-modules/select2/js/select2.min.js"></script>
    <script type="text/javascript">
        var formData = {}, dataId=1, startDate, endDate, isOpenvedio, isOpentuwen, orderStatus, docName, type, hosid, depid;
        //获取医院
        $.get('/system/gainhosdatas')
            .done(function(d){
                var ops = '<option value="">---请选择---</option>';
                $.each(d['hospitals'] || '',function(i,o){
                    ops += '<option value='+ o.id +'>'+ o.displayName +'</option>';
                });
            $('#selecthos').html(ops);
        })
        $('body')
        .delegate('.header li', 'click', function(){
            dataId = $(this).attr('data-id');
            docName = ''
            $("[type='checkbox']").removeAttr("checked")
            $("[type='radio']").removeAttr("checked")
            isOpenvedio = isOpentuwen = 0;
            orderStatus = ''
            formData['startDate'] = start.format('YYYY-MM-DD');
            formData['endDate'] = end.format('YYYY-MM-DD');
            cb(start, end);
            type = 1;
            $(this).css('color','lightcoral').siblings().css('color', '#000');
            dataId == 1 ? $('.docName').show() : $('.docName').hide()
            dataId == 2 ? $('.serverType').show() : $('.serverType').hide();
            (dataId == 3 || dataId == 4) ? $('.orderStatus').show() : $('.orderStatus').hide();
            dataId == 5 ? $('.attention').show().find('input[value="1"]').attr('checked', true) : $('.attention').hide();
            (dataId == 8 && !hosid) ? $('.report').attr('disabled', true) : $('.report').attr('disabled', false)
        })
        .delegate('.report', 'click', function(){
            docName= '&docName='+$('[name="docName"]').val() || ''
            hosid = $("#selecthos").val();
            depid = $("#selectdep").val();
            startDate = formData['startDate']
            endDate = formData['endDate']
            isOpenvedio = $('[name="isOpenvedio"]').is(':checked') ? 1 : 0
            isOpentuwen = $('[name="isOpentuwen"]').is(':checked') ? 1 : 0
            orderStatus = $('[name="orderStatus"]:checked').attr('data-id') || ''
            type = $('input[name="attention"]:checked').val();
            var str = '', timer='', sta=''
            // 根据id调换接口
            timer = '&startDate='+startDate+'&endDate='+endDate
            dataId == 2 ? str = '&isOpenvedio='+isOpenvedio+'&isOpentuwen='+isOpentuwen : dataId == 3 ? sta = '&sta='+orderStatus : ''
            sta = '&sta='+orderStatus;
            type = '&type='+type
            var urls = 'hosid='+hosid+'&depid='+depid+timer
            switch(dataId){
                case '1': window.location.href = '/system/reportlogin?'+urls+docName; break;
                case '2': window.location.href = '/system/reportExpertService?'+urls+str; break;
                case '3': window.location.href = '/system/reportvediooder?'+urls+sta; break;
                case '4': window.location.href = '/system/reportuwen?'+urls+sta; break;
                case '5': window.location.href = '/system/docreportabout?'+urls+type; break;
                case '6': window.location.href = '/system/docregisterexport?'+urls; break;
                case '7': window.location.href = '/system/erweimaExport?'+urls; break;
                case '8': window.location.href = '/system//docincomeexport?'+urls; break;
            }
        })
        .delegate('.bindchange','change',function(){
            hosid = $("#selecthos").val();
            hosid ? $('.report').attr('disabled', false) : $('.report').attr('disabled', true)
            $.get('/system/gainhosdepdatas',{ hosid: hosid })
                .done(function(d){
                    var ops = '<option value="">---请选择---</option>';
                    $.each(d['deps'] || '',function(i,o){
                        ops += '<option value='+ o.id +'>'+ o.displayName +'</option>';
                    });
                $('#selectdep').html(ops);
            })
        })

        /*************日历控件**************/
        var start, end ;
        start = end = moment();	
        function cb(start, end) {
            if(!start._isValid || !end._isValid){
                $('#reportrange').val('==全部时段==');
            }else{
                $('#reportrange').val(start.format('YYYY-MM-DD') + ' 至 ' + end.format('YYYY-MM-DD'));
            }
        }
        formData['startDate'] = start.format('YYYY-MM-DD');
        formData['endDate'] = end.format('YYYY-MM-DD');
        $('#reportrange').daterangepicker({
            locale: {
                format: 'YYYY-MM-DD',
                separator : ' 至 ',
                applyLabel : '确定',  
                cancelLabel : '取消',  
                fromLabel : '起始时间',  
                toLabel : '结束时间',  
                customRangeLabel : '自定义',  
                daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],  
                monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],  
                firstDay : 1
            },
            startDate: start,
            endDate: end,
            ranges: {
                '全部': ['',''],
                '当天': [moment().subtract(0, 'days'), moment()],
                '最近7天': [moment().subtract(6, 'days'), moment()],
                '最近30天': [moment().subtract(29, 'days'), moment()],
                '最近1年': [moment().subtract(12, 'month'), moment()],
                '当月': [moment().startOf('month'), moment().endOf('month')],
                '上月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
            }
        }, cb);
        $('#reportrange').on('apply.daterangepicker', function(ev, picker) {						
            formData['startDate'] = picker.startDate._isValid ? picker.startDate.format('YYYY-MM-DD') : '';
            formData['endDate'] = picker.endDate._isValid ? picker.endDate.format('YYYY-MM-DD') : '';
            cb(picker.startDate,picker.endDate);
        });
        $('[name="hosid"]').select2();
	</script>
</html>