Vue.component('my-baseform', function (resolve, reject) {
    $.get('ajax/form/base.html')
        .done(function (d) {
            resolve({
                props: {
                    fdata: {
                        type: Object
                    }
                },
                template: d,
                methods: {
                    validateAge: function (v) {
                        return !(v && v > 0 && v < 120);
                    },
                    validateTel: function (v) {
                        return !BASE.valideTel(v);
                    },
                    validateCard: function (v) {
                        return !!v && !BASE.valideCard(v);
                    }
                }
            })
        });
});
Vue.component('my-profileform', function (resolve, reject) {
    $.get('ajax/form/profile.html')
        .done(function (d) {
            resolve({
                props: {
                    fdata: {
                        type: Object
                    },
                    edit: {
                        type: Boolean,
                        default: true
                    }
                },
                template: d
            })
        });
});
Vue.component('my-question', function (resolve, reject) {
    $.get('ajax/form/question.html')
        .done(function (d) {
            resolve({
                props: {
                    fdata: {
                        type: Object
                    },
                    edit: {
                        type: Boolean,
                        default: true
                    }
                },
                template: d
            })
        });
});
Vue.component('my-tab', function (resolve, reject) {
    $.get('ajax/form/tab.html')
        .done(function (d) {
            resolve({
                props: {
                    fdata: {
                        type: Object
                    }
                },
                template: d
            })
        });
});
Vue.component('my-liss', function (resolve, reject) {
    $.get('ajax/form/lis.html')
        .done(function (d) {
            resolve({
                props: {
                    fdata: {
                        type: Object
                    }
                },
                template: d,
                methods: {
                    synchLis: function () {
                        
                    },
                    synchLisWithQuery: function () {
                        BASE.showModel({
                            remote: 'ajax/modal-content/modal-lis-sync-query.html',
                            cls: 'modal-lg'
                        });
                    },
                    synchLisWithUpload: function () {
                        $('#lisfiles_addfiles input').trigger('click');
                    },
                    pacfileok: function(){
                        Page.mainForms.lisForm.form.img_list = this.$refs.lisfiles.imglist,
                        Page.mainForms.lisForm.form.img_list_ids = this.$refs.lisfiles.ids
                    },
                    halfData: function (data, bol) {
                        var half = Math.ceil(data.length / 2);
                        return bol ? data.slice(0, half) : data.slice(half, data.length);
                    },
                    isDeepModal: function () {
                        return Page.mainForms.dockingMode == '2';
                    },
                    removeItem: function (pkey, key) {
                        var lisForm = Page.mainForms.lisForm.form;

                        var pIdx = BASE.arrayFindObjIndex(pkey, 'key', lisForm.list);
                        var idx = BASE.arrayFindObjIndex(key, 'key', lisForm.list[pIdx].beans);
                        idx != -1 && lisForm.list[pIdx].beans.splice(idx, 1);
                    },
                    showReportDetail: function (e) {
                        var o = $(e.target).closest('.reportlist'),
                            op = o.closest('.beantimelist');
                        var Idx = o.index(),
                            pIdx = op.index();

                        function creatAndshow(oIdx, opIdx) {
                            var $op = $('#lis-list .beantimelist:eq(' + opIdx + ')'),
                                $o = $op.find('.reportlist:eq(' + oIdx + ')');
                            var oSize = $op.find('.reportlist').size(),
                                opSize = $('#lis-list .beantimelist').size();
                            var prev = opIdx != 0 ? '<button class="btn btn-prev" onclick="Page.creatAndshow(0,' + (opIdx - 1) + ')"><i class="fa fa-angle-left"></i></button>' : '';
                            var next = opIdx != (opSize - 1) ? '<button class="btn btn-next" onclick="Page.creatAndshow(0,' + (opIdx + 1) + ')"><i class="fa fa-angle-right"></i></button>' : '';
                            var head = oSize > 1 ? (function (ele, idx, pidx) {
                                var header = [];
                                var _header = '<div class="navbar-header"><button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-2"><span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button><a class="navbar-brand" href="javascript:;">报告单</a></div>';
                                ele.each(function (i) {
                                    header.push('<li class="toplist' + (idx == i ? ' active' : '')
                                        + '" onclick="Page.creatAndshow(' + i + ',' + pidx + ')"><a href="javascript:;">' + $('.re_title', this).attr('title')
                                        + '</a></li>');
                                });
                                return '<div class="navbar navbar-default report-head">' + _header + '<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-2"><ul class="nav navbar-nav">' + header.join('') + '</ul></div></div>';
                            })($op.find('.reportlist'), oIdx, opIdx) : '';
                            var close = '<button type="button" class="close fixed-close" data-dismiss="modal" aria-hidden="true">×</button>';
                            var rpt = '<div class="report-body">' + prev + '<div class="report">' + $o.find('.report').html() + '</div>' + next + '</div>';
                            BASE.showModel({
                                text: close + head + rpt,
                                cls: 'modal-1024'
                            });
                        }
                        Page.creatAndshow = creatAndshow;
                        creatAndshow(Idx, pIdx);
                    }
                }
            });
        });
});
Vue.component('my-pacs', function (resolve, reject) {
    $.get('ajax/form/pac.html')
        .done(function (d) {
            resolve({
                props: {
                    fdata: {
                        type: Object
                    },
                    edit: {
                        type: Boolean,
                        default: true
                    }
                },
                template: d,
                mounted: function(){
                    // $(window).bind("scroll",function(){
                    //     var st = $(window).scrollTop(), sth = st + $(window).height();
                    //     $('.vue img[data-src]').each(function(){
                    //         var o = $(this), post = o.offset().top, posb = post + o.height();
                    //         if ((post > st && post < sth) || (posb > st && posb < sth)) {
                    //             this.getAttribute('src') != this.getAttribute('data-src') && o.attr('src', o.attr('data-src'));
                    //         }
                    //     });
                    // }).trigger('scroll');
                },
                methods: {
                    synchPacWithQuery: function () {
                        BASE.showModel({
                            remote: 'ajax/modal-content/modal-pac-sync-query.html',
                            cls: 'modal-lg'
                        });
                    },
                    synchPacWithUpload: function () {
                        BASE.showModel({
                            remote: 'ajax/modal-content/modal-DCIM.html',
                            cls: 'modal-lg'
                        });
                    },
                    synchImgPacWithUpload: function(){
                        $('#pacfiles_addfiles input').trigger('click');
                    },
                    pacfileok: function(){
                        Page.mainForms.pacForm.form.img_list = this.$refs.pacfiles.imglist,
                        Page.mainForms.pacForm.form.img_list_ids = this.$refs.pacfiles.ids
                    },
                    isDeepModal: function () {
                        return Page.mainForms.dockingMode == '2';
                    },
                    removeItem: function (pkey, key) {
                        if(confirm("确定要要删除吗？")){
                            var pacForm = Page.mainForms.pacForm.form;
                            var pIdx = BASE.arrayFindObjIndex(pkey, 'key', pacForm.list);
                            var idx = BASE.arrayFindObjIndex(key, 'key', pacForm.list[pIdx].beans);
                            idx != -1 && pacForm.list[pIdx].beans.splice(idx, 1);
                            var url = this.edit ? '/doctor/editPacsInfo' : '/doctor/editPacsInfo_case'
                            var pacs_ids = (function(list){
                                var arr = [];
                                $.each(list,function(i,n){
                                    $.each(n.beans,function(ii,nn){
                                        arr.push(nn.key);
                                    });
                                });
                                return arr.join(',')})
                            (pacForm.list)
                            var obj = this.edit ? { oid: Page.otype == 4 ? Page.oid : Page.ouid, otype: Page.otype, pacs_ids: pacs_ids} : { caseUuid: Page.ouid, pacs_ids: pacs_ids}
                            $.post(url, obj)
                                .done(function () {
                                    $.smallBox({
                                        title: "删除提示",
                                        content: "影像资料删除成功",
                                        color: $.color.success,
                                        timeout: 3000
                                    });
                                })
                                .fail(function () {
                                    $.smallBox({
                                        title: "删除提示",
                                        content: "影像资料删除失败",
                                        color: $.color.error,
                                        iconSmall: "fa fa-times"
                                    });
                                })
                        }
                    },
                    saveFileName: function (pid, id, val) {
                        if(!val) return false;
                        var oldForm = Page.mainForms.pacForm.old.list;
                        var pidx = BASE.arrayFindObjIndex(pid, 'key', oldForm || []);
                        var oidx = pidx != -1 ? BASE.arrayFindObjIndex(id, 'key', oldForm[pidx].beans || []) : -1;
                        var oName = oidx != -1 ? oldForm[pidx].beans[oidx].kvs.Check_Item_E : '';
                        if(val == oName) return false;
                        $.post('/doctor/compleupcheckitem', { pid: id, itemname: val, oid:  Page.oid })
                            .done(function () {
                                pidx != -1 && oidx != -1 && (oldForm[pidx].beans[oidx].kvs.Check_Item_E = val);
                                $.smallBox({
                                    title: "”修改为“" + val + "”，成功",
                                    color: $.color.success,
                                    timeout: 3000
                                });
                            })
                            .fail(function () {
                                $.smallBox({
                                    title: "修改为“" + val + "”，失败！",
                                    color: $.color.error,
                                    iconSmall: "fa fa-times"
                                });
                            });
                    },
                    showPacDetail: function (key, pid, sid) {
                        var href = location.origin + '/dwv/viewer.html?patientID=' + pid + '&studyUID=' + sid + '&serverName=';
                        var close = '<button type="button" class="close fixed-close full-dialog" data-dismiss="modal" aria-hidden="true">×</button>';
                        var iframe = '<iframe name="dwviframe" src="' + href
                            + '" id="dwviframe" frameborder="no" border="0" allowscriptaccess="always" marginwidth="0" marginheight="0" allowfullscreen="true" wmode="opaque" allowTransparency="true" type="application/x-shockwave-flash" style="width:100vw;height:100vh;"></iframe>';
                        BASE.showModel({
                            text: close + iframe,
                            cls: 'modal-full',
                            gls: 'pacmodel'
                        });
                    }
                }
            });
        });
});
Vue.component('my-selectexp',function(resolve, reject){
    $.get('ajax/form/selectexp.html')
        .done(function (d) {
            resolve({
                props: {
                    oid: { type: String },
                    eid: { type: String },
                    docask: { type: String ,default: ''}
                },
                data: function(){
                    return {
                        tab: 'hos',
                        keywords: '',
                        hosid: '',
                        cityid: '',
                        depid: '',
                        bigdepid: '',
                        dutyid: '',
                        hos: [],
                        dep: [],
                        duty: [
                            ['-1','所有职务'],
                            ['主任医师','主任医师'],
                            ['副主任医师','副主任医师']
                        ],
                        pager: {
                            list: []
                        },
                        expid: '',
                        info: {},
                        filter:{
                            hosid:'',
                            depid:'',
                            bigdepid:'',
                            dutyid:''
                        },
                        isload: false
                    };
                },
                template: d,
                created: function(){
                    var that = this;
                    this.expid = this.eid;
                    that.hos.length < 1 && $.get('/propagate/gainHospitalsByArea')
                    .done(function(d){
                        var h = {};
                        $.each(d.hospitals || [],function(i,n){
                            var dis = n.distCode.substring(0,2) || '99';
                            var dis4 = n.distCode.substring(0,4);
                            if(dis4 == '4401' || dis4 == '4403') dis = dis4;
                            if(dis in h){
                                h[dis]['child'].push({
                                    id: n.id,
                                    name: n.displayName
                                });
                            }else{
                                h[dis] = {
                                    name: getcity(dis),
                                    child: [{
                                        id: n.id,
                                        name: n.displayName
                                    }]
                                };
                            }
                        });
                        that.hos = h;
                    });

                    that.dep.length < 1 && $.get('/propagate/gainBigDepartments')
                    .done(function(d){
                        that.dep = [].concat(JSON.parse(d));
                    });

                    that.loadExp(1);

                    function getcity(id){
                        var discodes = {
                            '11' : '北京',
                            '31' : '上海',
                            '36' : '江西',
                            '42' : '湖北',
                            '43' : '湖南',
                            '44' : '广东',
                            '51' : '华西',
                            '4401' : '广州',
                            '4403' : '深圳',
                            '99' : '未知'
                        };
                        return id in discodes ? discodes[id] : (id || '未知');
                    }
                },
                methods: {
                    tapdd: function(key, value, txt){
                        if(key == 'cityid' && value == '-1'){
                            this.hosid = '-1';
                        }else if(key == 'bigdepid' && value == '-1'){
                            this.depid = '-1';
                        } 
                        if(value){
                            this[key] != value ? 
                                (this[key] = value,this.filter[key] = txt) : 
                                (this[key] = '',this.filter[key] = '');
                        }
                        if(key == 'cityid' || key == 'bigdepid'){
                            value == '-1' &&  this.loadExp(1);
                        }else{
                            this.loadExp(1);
                        }
                    },
                    select: function(info){
                        this.expid = info.specialId;
                        this.info = info;
                        this.$emit('select');
                    },
                    imgSrc: function(src){
                        return BASE.imgsrc(src);
                    },
                    loadExp: function(no){
                        var that = this;
                        that.isload = true;
                        no && $.get('/docadmin/loadExperts',{
                            hosid: that.hosid,
                            docask: that.docask,
                            //bigDepId: that.bigdepid,
                            depId: that.depid,
                            duty: that.dutyid,
                            keywords: that.keywords,
                            pageNumber: no || 1,
                            pageSize: 12
                        })
                        .done(function(d){
                            if(BASE.isLost(d)) return BASE.checkLogin(),0;
                            that.pager = d.pager;
                        })
                        .always(function(){
                            that.isload = false;
                        })
                    }
                }
            })
        });
});
Vue.component('my-selecttime', function (resolve, reject) {
    $.get('ajax/form/selectexptime.html')
        .done(function (d) {
            resolve({
                props: {
                    oid: { type: String },
                    expid: { type: String }
                },
                data: function(){
                    return {
                        timeid: '',
                        timename: '',
                        sdate: '',
                        sdates: [],
                        times: [],
                        info: {},
                        isload: false
                    };
                },
                template: d,
                methods: {
                    imgSrc: function(src){
                        return BASE.imgsrc(src);
                    },
                    getTimes: function(){
                        var that = this;
                        that.isload = true;
                        $.post('/wzjh/gainSpecialTimes',{sid: this.expid, sdate: new Date(this.sdate).Format('yyyy-MM-dd') })
                        .done(function(d){
                            that.times = d.times || [];
                        })
                        .always(function(){
                            that.isload = false;
                        });
                    },
                    fillTimes: function(s){
                        this.sdate = s;
                        this.getTimes();
                    },
                    select: function(tid, tname){
                        this.timeid = tid;
                        this.timename = tname;
                        this.$emit('select');
                    }
                },
                created: function(){
                    var that = this;
                    createHead();
                    getExpinfo();
                    function createHead(){
                        var total = 7,arr = [];
                        var today = new Date(),day = today.getDate(),week,cdate,ymd,md;
                        for(var i = 0;i < total; i++){
                            cdate = new Date(),cdate.setDate(day + i);
                            md = [cdate.getMonth() + 1, cdate.getDate()];
                            ymd = [cdate.getFullYear()].concat(md);
                            week = getWeek(cdate.getDay());
                            arr.push({
                                w: week,
                                ymd: ymd.join('-'),
                                md: md.join('/')
                            });
                        }
                        that.sdates = arr;
                        that.fillTimes(arr[0].ymd);
                    }
                    function getWeek(week){
                        var day;
                        switch (week){
                            case 0:day="周日";break;
                            case 1:day="周一";break;
                            case 2:day="周二";break;
                            case 3:day="周三";break;
                            case 4:day="周四";break;
                            case 5:day="周五";break;
                            case 6:day="周六";break;
                        }
                        return day;
                    }
                    function getExpinfo(){
                        $.post('/kangxin/gainspedetail',{docid: that.expid})
                        .done(function(d){
                            that.info = d.special || {};
                        });
                    }
                }
            })
        });
});
Vue.component('my-pay', function (resolve, reject) {
    $.get('ajax/form/pay.html')
        .done(function (d) {
            resolve({
                props: {
                    oid: { type: String },
                    haslisten: { type: Boolean, default: true },
                    pay: { type: Object },
                    money: { type: Number }
                },
                data: function(){
                    return {
                        islisten: true,
                        timer: 400
                    };
                },
                template: d,
                watch:{
                    pay: {
                        handler: function (val, oldVal) { 
                            val.code_url && val.code_url != oldVal.code_url && this.init()
                        },
                        deep: true
                    }
                },
                mounted: function(){
                    this.pay.code_url && this.init()
                },
                methods: {
                    init: function(){
                        var that = this;
                        this.haslisten && this.listen();                    
                        window.setTimeout(function(){that.islisten = false},this.timer * 1000),
                        this.timeleave();
                    },
                    listen: function(){
                        var that = this;
                        this.paystate(that.payok,function(){
                            window.setTimeout(that.listen,3000);
                        });
                    },
                    paystate: function(ok,fail){
                        var that = this;
                        this.islisten && this.pay.out_trade_no && 
                            $.post('/kangxin/listenpaystatus',{tradeno:this.pay.out_trade_no})
                            .done(function(d){
                                d.status == 'success' ? ok() : fail();
                                debugState && console.log('正在监听支付状态');
                            })
                            .fail(function(){
                                debugState && console.log('支付监听失败');
                            });
                    },
                    timeleave: function(){
                        (this.timer -= 1) && window.setTimeout(this.timeleave,1000);
                    },
                    payok: function(){
                        this.islisten = false;
                        this.$emit('payok');
                    }
                },
                destroyed: function(){
                    this.islisten = false;
                    debugState && console.log('已销毁支付监听');
                }
            })
        });
});
Vue.component('my-webupload', function (resolve, reject) {
    $.get('ajax/form/webupload.html')
        .done(function (d) {
            resolve({
                template: d,
                props: {
                    id: { type: String, default: 'file-list' },
                    oid: { type: String, default: '' },
                    onlyimg: { type: Boolean, default: true },
                    btn: { type: Boolean, default: true },
                    list: { type: Array, default: function () { return [] } },
                    edit: { type: Boolean, default: true },
                    editname: { type: Boolean, default: false },
                    single: { type: Boolean, default: false }
                },
                data: function () {
                    return {
                        imglist: [],
                        old: [],
                        isload: false,
                        issave: false,
                        OBJ: {}
                    }
                },
                computed: {
                    ids: function () {
                        return (function (a) {
                            var arr = [];
                            $.each(a, function (i, n) {
                                arr.push(n.id);
                            });
                            return arr;
                        })(this.imglist);
                    },
                    urls: function(){
                        return (function (a) {
                            var arr = [];
                            $.each(a, function (i, n) {
                                arr.push(n.fileUrl);
                            });
                            return arr;
                        })(this.imglist);
                    }
                },
                created: function () {
                    this.imglist = [].concat(this.list || []);
                },
                watch: {
                    list: function (n, o) {
                        this.imglist = [].concat(this.list || []);
                    },
                    imglist: function (n, o) {
                        JSON.stringify(n) != JSON.stringify(o) && window.setTimeout(this.initViewer,1000); 
                    },
                    edit: function(n, o){
                        if(n == true) this.old = this.imglist;
                    }
                },
                methods: {
                    imgsrc: function(src){
                        return BASE.imgsrc(src, 'img/avatars/64.png');
                    },
                    initViewer: function(){
                        var that = this;
                        loadCss('js/plugin/imgviewer/viewer.css', function () {
                            loadScript('js/plugin/imgviewer/viewer.min.js', function () {
                                that.OBJ.imgViewer && that.OBJ.imgViewer.viewer('destroy');
                                debugState && console.log('-------初始化图片查看-------');
                                that.OBJ.imgViewer = $('#' + that.id + '.diyUpload .fileBoxUl').viewer({
                                    url: 'data-src',
                                    navbar: true,
                                    title: false,
                                    transition: false,
                                    built: function () {
                                        $('#' + that.id + ' .viewer-canvas').bind('click', function (event) {
                                            event.target.localName != 'img' && that.OBJ.imgViewer.viewer('hide');
                                        });
                                    }
                                });
                                //$(window).trigger('scroll');
                            });
                        });
                    },
                    success: function () {
                        this.$emit('success');
                    },
                    isImg: function (val) {
                        var filetype = (val||'').split('.').pop();
                        return jQuery.inArray(filetype.toLowerCase(), ['gif', 'jpg', 'jpeg', 'bmp', 'png', 'tif']) != -1;
                    },
                    removeImage: function (val) {
                        var list = [].concat(this.imglist);
                        var arridx = BASE.arrayFindObjIndex(val, 'id', list);
                        arridx != -1 && list.splice(arridx, 1);
                        this.imglist = list;
                        this.success();
                    },
                    saveFileName: function (id, val) {
                        if (!val) return false;
                        var oldForm = this.old.list;
                        var oidx = BASE.arrayFindObjIndex(id, 'id', oldForm || []);
                        var oName = oidx != -1 ? oldForm[oidx].fileName : '';
                        if (val == oName) return false;
                        var that = this;
                        $.post('/doctor/updatefname', { fid: id, filename: val })
                            .done(function () {
                                oidx != -1 && (oldForm[oidx].fileName = val);
                                $.smallBox({
                                    title: "修改为“" + val + "”，成功",
                                    color: $.color.success,
                                    timeout: 3000
                                });
                                that.success();
                            })
                            .fail(function () {
                                $.smallBox({
                                    title: "修改“" + val + "”，失败！",
                                    color: $.color.error,
                                    iconSmall: "fa fa-times"
                                });
                            });
                    },
                    showVideo: function (e) {
                        var tet = $(e.target).find('source').attr('src');
                        BASE.showModel({
                            cls: 'modalvideo',
                            text: '<button type="button" onclick="BASE.closeModel()" class="close fixed-close">×</button>' + '<iframe name="videojsiframe" src="' + BASE.href + 'sea-modules/videojs/examples/index.html?src=' + tet + '" frameborder="no" border="0" allowscriptaccess="always" marginwidth="0" marginheight="0" allowfullscreen="true" wmode="opaque" allowTransparency="true" type="application/x-shockwave-flash"></iframe>'
                        });
                    }
                },
                mounted: function () {
                    $('#' + this.id + '_addfiles').size() && loadWebUploader(this);                    
                    function loadWebUploader(that) {
                        loadScript("js/plugin/webuploader/webuploader.min.js", function () {
                            loadScript("js/plugin/webuploader/upload.base.js", function () {
                                var _type = that.onlyimg;
                                debugState && console.log('-------初始化图片上传控件-------');
                                that.OBJ.webUploader = $('#' + that.id + '_addfiles').Uploader({
                                    server: BASE.href + 'doctor/uploadLocalFile',
                                    formData: { orderid: that.oid },
                                    pick: {
                                        multiple: !that.single
                                    },
                                    duplicate: true,
                                    thumb: {
                                        width: 100,
                                        height: 100,
                                        quality: 70,
                                        allowMagnify: false,
                                        crop: true,
                                        type: "image/jpeg"
                                    },
                                    accept: {
                                        title: "Files",
                                        extensions: (function () {
                                            return _type ? 'gif,jpg,jpeg,bmp,png' : 'gif,jpg,jpeg,bmp,png,mp4,avi,webm,mkv,mov,rm,ogg.ogv';
                                        })(),
                                        mimeTypes: (function () {
                                            return _type ? 'image/jpg,image/jpeg,image/bmp,image/png,' : "image/jpg,image/jpeg,image/bmp,image/png,video/mp4,video/avi,video/webm,video/mkv"
                                        })()
                                    },
                                    success: function (liobj, val) {
                                        var fileid = liobj.attr('id').replace('fileBox_', '');
                                        var img = liobj.find('img');
                                        img.size() < 1 && (img = liobj.find('source'));
                                        var obj = {
                                            id: val,
                                            thumb: img.attr('src') || '',
                                            fileType: liobj.find('img').size() ? 'jpg' : 'mp4',
                                            fileUrl: img.attr('data-src') || img.attr('src'),
                                            fileName: liobj.find('.diyFileName').text()
                                        };
                                        if(val){
                                            if (that.single) {
                                                that.imglist = [].concat([obj]);
                                            } else {
                                                that.imglist = that.imglist.concat([obj]);
                                            }
                                        }else{
                                        	$.smallBox({title: "文件上传",content:"文件上传失败，请重试",color: $.color.error,iconSmall: "fa fa-times",timeout: 3000});
                                        }
                                        that.OBJ.webUploader.removeFile(fileid);
                                        $('#' + that.id + '.diyUpload .browser[data-id="' + val + '"]').remove();
                                        that.success();
                                    },
                                    afterpost: function () {
                                        //重置图片查看器
                                        //that.OBJ.imgViewer.viewer('destroy').viewer();
                                    }
                                });
                                // $(window).bind("scroll",function(){
		                        //     var st = $(window).scrollTop(), sth = st + $(window).height();
                                //     $('.vue img[data-src]').each(function(){
                                //         var o = $(this), post = o.offset().top, posb = post + o.height();
                                //         if ((post > st && post < sth) || (posb > st && posb < sth)) {
                                //             this.getAttribute('src') != this.getAttribute('data-src') && o.attr('src', o.attr('data-src'));
                                //         }
                                //     });
                                // }).trigger('scroll');
                            });
                        });
                    }
                },
                destroyed: function() {
                    try{
			            this.OBJ.imgViewer && this.OBJ.imgViewer.viewer('destroy');
                    }catch(e){
                        //debugState && console.log('imgViewer.destroy', e);
                    }
                    try{
			            this.OBJ.webUploader && this.OBJ.webUploader.destroy();
                    }catch(e){
                        //debugState && console.log('webUploader.destroy', e);
                    }
                }
            })
        })
});
Vue.component('my-report', function (resolve, reject) {
    $.get('ajax/form/report.html')
        .done(function (d) {
            resolve({
                props: {
                    oid: { type: String, default: '' },
                    list: { type: Object, default: function () { return {} } },
                    edit: { type: Boolean, default: true },
                    otype: { type: String, default: '' },
                    status:{ type: Number }
                },
                data: function () {
                    return {
                        tab: '1'
                    }
                },
                template: d,
                methods: {
                    sendreport: function () {
                        Page.tab = this.tab
                        BASE.showModel({
                            remote: 'ajax/modal-content/modal-report.html',
                            cls: 'modal-lg'
                        });
                    },
                    expreport: function () {
                        BASE.showModel({
                            remote: 'ajax/modal-content/modal-report-word.html',
                            cls: 'modal-lg'
                        });
                    },
                    maxImgShow: function(){
                        Page.maximg = this.list.photoReport
                        BASE.showModel({
                            remote: 'ajax/modal-content/modal-max-image.html',
                            backdrop: true,
                            cls: 'modal-lg'
                        });
                    }
                    
                },
                computed: {
                    usertype: function(){
                        return Page.mainForms.usertype == 'receive'
                    }
                }
            })
        })
});
Vue.component('my-room', function (resolve, reject) {
    $.get('ajax/form/room.html')
        .done(function (d) {
            resolve({
                template: d,
                props: {
                    oid: { type: String, default: '' },
                    docask: { type: String, default: '' },
                    room: { type: Boolean, default: false },
                    video: { type: Boolean, default: false },
                    footer: { type: Boolean, default: false },
                    status: { type: Number }
                },
                data: function () {
                    return {
                        roomid: '',
                        expInfo: {
                            info: {
                                specialName: '未分配'
                            },
                            state: false
                        },
                        otype: Page.otype,
                        utype: SStorage.get('_token_utype'),
                        sysMsg: [],
                        userMsg: [],
                        sendtext: '',
                        isInit: true,
                        initMsg: '准备初使化',
                        initImg: '../img/loading/31.gif',
                        playId: '',
                        playLength: null,
                        frame: '_blank.html',
                        openStatus: false
                    }
                },
                computed: {
                    framesrc: function() {
                        return 'video.html?oid='+ this.oid +'&utype='+ this.utype +'&otype=' + this.otype + '&status=' + +this.openStatus
                    }
                },
                methods: {
                    showPic: function(idx){
                        var item = this.userMsg[idx];
                        Page.media = item;
                        BASE.showModel({
                            remote: 'ajax/modal-content/modal-media.html',
                            backdrop: true
                        });
                    },
                    playMedia: function(idx){
                        var that = this;
                        var item = this.userMsg[idx];
                        if(this.playId == item.msgContent){
                            this.playId = ''
                            return false;
                        }
                        this.playId = item.msgContent;
                        if(!this.playId){
                            return alert('播放异常，找不到文件。'), false;
                        }
                        this.playLength = Math.floor(this.playId.length / 1024) + 1;
                        if (that.playId && typeof RongIMLib != 'undefined' && ('RongIMVoice' in RongIMLib)) {
                            RongIMLib.RongIMVoice.init();
                            RongIMLib.RongIMVoice.preLoaded(that.playId, function(){
                                RongIMLib.RongIMVoice.play(that.playId, that.playLength)
                                RongIMLib.RongIMVoice.onprogress = function(){
                                    that.playLength--
                                    !that.playLength && (that.playId = '')
                                }
                            })
                        }
                    },
                    roomsuccess: function () {
                        this.$emit('roomsuccess');
                    },
                    sendMsg: function () {
                        var t = this.sendtext;
                        if(!t) return this.sendtext = '', 0;
                        sendmessage(t, this);
                        this.userMsg.push({
                            "sendType": Page.utype,
                            "msgContent": t,
                            "msgType": "RC:TxtMsg",
                            "fileUrl": null
                        });
                        this.sendtext = '';
                        function sendmessage(text, that) {
                            var msg = new RongIMLib.TextMessage({ 
                                content: text, 
                                extra: Page.ouid + ","+ Page.otype +","+ Page.order.docName +",3" });
                            var conversationtype = RongIMLib.ConversationType.GROUP; // 私聊                            
                            RongIMClient.getInstance().sendMessage(conversationtype, that.roomid, msg, {
                                onSuccess: function (message) {
                                    // savemessage(text);
                                },
                                onError: errorIM
                            });
                        }
                    },
                    loadroom: function(){
                        var that = this;
                        //初始化聊天对象详情
                        loadUID(function () {
                            checkRoomState(that);
                        });
                        //初始化聊天功能
                        this.status == 20 && initRongIM(that);
                        //加载聊天历史消息
                        loadMessage(that);

                        this.roomsuccess();
                        //初始化room可缩放功能
                        initResizable();
                    },
                    toemjc (text) {
                        if (typeof RongIMLib != 'undefined' && 'RongIMEmoji' in RongIMLib) {
                            return RongIMLib.RongIMEmoji.emojiToHTML(text)
                        } else {
                            return text
                        }
                    },
                    newWindow: function() {
                        this.openStatus = 'footer' in Page ? Page.footer.isBegin : false;
                        window.frames['videoiframe'].location.replace(this.frame);
                        document.getElementById('videoiframe').style.display = 'none';
                        Page.popup = window.open(this.framesrc, "newwin", "height=480,width=960,toolbar=no,menubar=no,alwaysRaised=yes,depended=yes,location=no");
                    },
                    backIframe: function() {
                        this.openStatus = 'footer' in Page ? Page.footer.isBegin : false;
                        window.frames['videoiframe'].location.replace(this.framesrc);
                        document.getElementById('videoiframe').style.display = 'block';
                        Page.popup = window.frames['videoiframe'];
                    }
                },
                watch:{
                    sendtext: function(n, o){
                        this.sendtext = n.replace('\n','')
                    },
                    room: function(n,o){
                        if(!n) return 0;
                        this.loadroom();
                    },
                    video: function(n,o){
                        if(!n) return 0;
                        window.setTimeout(this.backIframe);
                    },
                    userMsg: {
                        deep: true,
                        handler: function(){
                            this.$nextTick(scrollMsgToView);
                            function scrollMsgToView() {
                                $('.side-column .messagelist dd:last').size() && $('.side-column .messagelist dd:last')[0].scrollIntoView(true);
                            }
                        }
                    }
                },
                mounted: function(){
                    var that = this;
                    this.room && this.loadroom();
                    this.video && this.backIframe();
                },
                destroyed: function(){
                    try{
                        RongIMClient && RongIMClient.getInstance &&RongIMClient.getInstance().disconnect();
                    }catch(e){
                        //debugState && console.log('RongIMClient.getInstance().disconnect', e);
                    }
                    try {
                        Page.popup && Page.popup.close();
                    } catch (error) {}
                }
            })
        });

        function initRongIM(that){
            var data = { type: Page.usertype == 'receive' ? 2 : 3, oid: Page.oid, otype: Page.otype };
            $.get('/doctor/gainIMToken', data)
                .done(function (d) {
                    debugState && console.log('-----gainIMToken------', d);
                    if (d.appkey && d.token) {
                        initIMLib(that, d.appkey, d.token), that.roomid = d.userId;
                    } else {
                        that.initMsg = '初使化失败';
                        that.initImg = '../img/noresult.png';
                    }
                });
        }
        function checkRoomState(vue) {
            $.get('/doctor/isPeerPresence', {
                userId: Page._uid,
                userType: Page.utype,
                orderId: Page.oid
            })
            .done(function (d) {
                sessionStorage.setItem('_remote_info',JSON.stringify(d.peer))
                d.result && (vue.expInfo.state = d.result == 'true');
                d.peer && (vue.expInfo.info = d.peer);
                Page.footer.online = vue.expInfo.state;
            });
        }
        function loadUID(callback) {
            if (Page._uid) return callback(), 0;
            $.get('/doctor/showbasicinfo')
            .done(function (d) {
                d.special.specialId && (
                    Page._uid = d.special.specialId, callback()
                );
            })
        }
        function loadMessage(a) {
            $.get('/doctor/loadmessages',{
                oid: Page.oid, 
                docask: a.docask, 
                vtype: Page.utype == '2' ? '3' : '2'
            }).done(function(d){
                a.userMsg = (a.userMsg).concat(d.messages || []);
            }).always(function(){
                RongIMLib.RongIMEmoji.init();
            });
        }
        function initResizable(){
            window.setTimeout(function(){
                $( ".side-column-fixed" ).resizable({
                    handles: "w",
                    minWidth: 240,
                    resize: function( event, ui ) {
                        var out = ui.element.parent();
                        var vdo = ui.element.find('header.videos .video-eles section');
                        ui.element.css({'left': 'auto'});
                        out.css({'width': ui.size.width});
                        vdo.css('height', ui.size.width * 0.75);
                        $('#vue-footer').css('right', ui.size.width);
                    }
                });
            },1600);
        }
        function initIMLib(room, key, token){
            RongIMClient.init(key);
            RongIMClient.setConnectionStatusListener({
                onChanged: function (status) {
                    room.isInit = true;
                    switch (status) {
                        case RongIMLib.ConnectionStatus.CONNECTED:
                            room.initMsg = '链接成功';                            
                            room.isInit = false;
                            break;
                        case RongIMLib.ConnectionStatus.CONNECTING:
                            room.initMsg = '正在链接';
                            break;
                        case RongIMLib.ConnectionStatus.DISCONNECTED:
                            room.initMsg = '断开连接';
                            room.initImg = '../img/noresult.png';
                            break;
                        case RongIMLib.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT:
                            room.initMsg = '其他设备登录';
                            room.initImg = '../img/noresult.png';
                            break;
                        case RongIMLib.ConnectionStatus.NETWORK_UNAVAILABLE:
                            room.initMsg = '网络不可用';
                            room.initImg = '../img/noresult.png';
                            break;
                    }
                }
            });
            // 消息监听器
            RongIMClient.setOnReceiveMessageListener({
                // 接收到的消息
                onReceived: function (message) {
                    if (message.targetId != room.roomid){
                        debugState && console.log('targetId不一置的消息');
                        return 0;
                    }
                    // 判断消息类型
                    if(message.offLineMessage){
                        debugState && console.log('我接收到的离线消息为：', message);
                        return 0;
                    }
                    var item = {
                        "sendType": Page.utype == 3 ? 2 : 3,
                        "msgContent": ''
                    }
                    switch (message.messageType) {
                        case RongIMClient.MessageType.TextMessage:
                            item.msgContent = message.content.content;
                            item.msgType = 'RC:TxtMsg';
                            break;
                        case RongIMClient.MessageType.ImageMessage:
                            var msgcontent = 'data:image/'+ (message.content.content.slice(0, 1) == '/' ? 'jpeg' : 'png') +';base64,' + message.content.content
                            item.msgContent = msgcontent;
                            item.fileUrl = message.content.imageUri;
                            item.msgType = 'RC:ImgMsg';
                            break;
                        case RongIMClient.MessageType.VoiceMessage:
                            item.msgContent = message.content.content;
                            item.msgType = 'RC:VcMsg';
                            break;
                        default:
                            item = null;
                        // 自定义消息
                        // do something...
                    }
                    item && room.userMsg.push(item);
                    debugState && console.log('我接收到的消息为：', message);
                }
            });
            // 连接融云服务器。
            RongIMClient.connect(token, {
                onSuccess: function (userId) {
                    debugState && console.log("Login successfully." + userId);
                },
                onTokenIncorrect: function () {
                    debugState && console.log('token无效');
                },
                onError: errorIM
            });            
        }

        function errorIM(errorCode, message) {
            var info = '';
            switch (errorCode) {
                case RongIMLib.ErrorCode.TIMEOUT:
                    info = '超时';
                    break;
                case RongIMLib.ErrorCode.UNKNOWN_ERROR:
                    info = '未知错误';
                    break;
                case RongIMLib.ErrorCode.REJECTED_BY_BLACKLIST:
                    info = '在黑名单中，无法向对方发送消息';
                    break;
                case RongIMLib.ErrorCode.NOT_IN_DISCUSSION:
                    info = '不在讨论组中';
                    break;
                case RongIMLib.ErrorCode.NOT_IN_GROUP:
                    info = '不在群组中';
                    break;
                case RongIMLib.ErrorCode.NOT_IN_CHATROOM:
                    info = '不在聊天室中';
                    break;
                case RongIMLib.ErrorCode.UNACCEPTABLE_PaROTOCOL_VERSION:
                    info = '不可接受的协议版本';
                    break;
                case RongIMLib.ErrorCode.IDENTIFIER_REJECTED:
                    info = 'appkey不正确';
                    break;
                case RongIMLib.ErrorCode.SERVER_UNAVAILABLE:
                    info = '服务器不可用';
                    break;
                default:
                    info = 'XX';
                    break;
            }
            debugState && console.log('发送失败:', info);
        }
});

Vue.component('my-selectdep', function (resolve, reject) {
    $.get('ajax/form/selectdep.html')
        .done(function (d) {
            resolve({
                props: {
                    fdata: {
                        type: Object
                    }
                },
                data: function(){
                    return {
                        keywords: '',
                        ylt: '',
                        yltInd: 0,
                        yltId: '',
                        yltName: '',
                        hos: '',
                        hosInd: 0,
                        hosId: '',
                        hosName: '',
                        dep: '',
                        depInd: -1,
                        depId: '',
                        depName: '',
                        isload: true,
                        isdep: false,
                        ishos: true,
                        loadHosName: [],
                        loadHosNames: [],
                        search: ''
                    }
                }, 
                created: function(){
                    this.ylt = Page.ylt
                    this.gethos(0)
                }, 
                methods: {
                    addyltClass: function (ind){
                        this.yltInd = ind 
                        this.isload = true;
                        this.loadHosName = []
                        this.gethos(ind)
                    },
                    addhosClass: function (ind, item){
                        this.hosInd = ind 
                        this.isload = true
                        this.getdep(ind, item)
                        this.$emit('addhos-class', this.hosId, this.depId)
                    },
                    adddepClass: function (ind){
                        this.depInd = ind
                        this.depId = this.dep[ind].id
                        this.depName = this.dep[ind].displayName
                        this.$emit('adddep-class', this.hosId, this.depId)
                    },
                    gethos: function (ind) {
                        var that = this
                        that.hosInd = 0 
                        that.yltId = that.ylt[ind].id
                        that.yltName = that.ylt[ind].yltName
                        that.dep = ''
                        that.ishos = true
                        $.get('/docadmin/gainhoshealthmember', { hhaId: that.yltId })
                        .done(function(data){
                            that.hos = data.members;
                            that.ishos = false
                            that.getdep(0)
                            that.loadHosName=[];
                            that.loadHosNames=[];
                            $.each(that.hos, function(ind, val){
                                var obj = {}
                                obj.displayName = val.displayName
                                obj.hosLevel = val.hosLevel
                                that.loadHosName.push(obj)
                                that.loadHosNames.push(obj)
                            })
                        })
                    },
                    getdep: function (ind, item){
                        var that = this;
                        if(item){
                            function findObj (obj){
                                if(obj.displayName == item.displayName){
                                    that.hosId = obj.id
                                    that.hosName = obj.displayName
                                }
                            }
                            var array = that.hos.find ( findObj )
                        }
                        else{
                            that.hosId = that.hos[ind].id
                            that.hosName = that.hos[ind].displayName
                        } 
                         this.depInd = -1
                         that.depId = ''
                         that.depName = ''
                         $.get('/docadmin/gaindepsbyhos', { hosId: that.hosId })
                         .done(function(data){
                            that.isload = false;
                            that.dep = data.departs
                            that.dep.length? 
                                that.isdep = false:
                                that.isdep = true
                         })
                    },
                    loadHos: function(){
                        var that = this
                        if( !that.search ){
                            that.hosInd = -1;
                            that.loadHosName = that.loadHosNames 
                        }else {
                            that.loadHosName = []
                            $.each(this.loadHosNames, function(ind, val){
                                if(val.displayName.indexOf(that.search) != -1){    
                                    that.loadHosName = that.loadHosName.concat(val)
                                }
                            })
                        }
                    }
                    
                },
                template: d
            })
        });
});
Vue.component('my-selectdoc',function(resolve, reject){
    $.get('ajax/form/selectdoc.html')
        .done(function (d) {
            resolve({
                props: {
                    oid: { type: String },
                    eid: { type: Number },
                    dtype:{ type: Number}
                },
                data: function(){
                    return {
                        selected: 0,
                        tab: 'area',
                        areaid: '',
                        list_areaid: '',
                        keywords: '',
                        discodes: '',
                        hosid: -1,
                        cityid: '',
                        depid: -1,
                        area: [],
                        hos: [],
                        dep: [],
                        pager: {
                            list: []
                        },
                        expid: '',
                        info: {},
                        filter:{
                            areaid: '',
                            hosid:'',
                            depid:'',
                            bigdepid:'',
                        },
                        isload: false,
                        loader: true
                    };
                },
                template: d,
                created: function(){
                    var that = this;
                    this.expid = this.eid;
                    that.loader = true
                    //城市列表
                    $.get('/docadmin/gainopencitys',{type: 3})
                    .done(function(data){
                        that.loader = false
                        that.area = data.pros
                    })
                    //医院列表
                    that.getHos('')
                    
                    //获取科室
                    $.get('/kangxin/gainStandDeps')
                    .done(function(d){
                        that.dep = d.sdeps
                    })
                    that.loadDoc(-1, 1)
                },
                methods: {
                    tapdd: function(key, value, txt){
                        switch(key){
                            case 'cityid':
                                    this[key] = value; 
                                    this.selected = 1;
                                    this.areaid='';
                                    this.discodes= value
                                    this.list_areaid ='';
                                    this.hosid= -1;
                                    this.filter.hosid= '';
                                    this.filter.areaid = txt; break;
                            case 'areaid':
                                    this[key] = value;
                                    this.discodes= value 
                                    this.selected = 2;
                                    this.list_areaid ='';
                                    this.hosid= -1;
                                    this.filter.hosid= '';
                                    this.filter.areaid = txt; break;
                            case 'list_areaid':
                                    this.discodes= value
                                    this[key] = value; 
                                    this.selected = 3;
                                    this.hosid= -1;
                                    this.filter.hosid= '';
                                    this.filter.areaid = txt; break;
                            case 'hosid':
                            case 'depid':
                                    this[key] = value;
                                    this.filter[key] = txt; break;
                        }
                        this.loadDoc(-1, 1)
                        this.discodes? 
                            this.getHos(this.discodes): 
                            this.getHos('')
                    },
                    hosClick: function (){

                    },
                    getHos: function(discode){
                        var that = this
                        $.get('/propagate/gainHospitalsByArea', {distcode: discode, type: -1})
                        .done(function(data){
                            that.hos = data.hospitals
                        })
                    },
                    select: function(info){
                        this.expid = info.specialId;
                        this.info = info;
                        this.$emit('select');
                    },
                    imgSrc: function(src){
                        return BASE.imgsrc(src);
                    },

                    loadDoc: function(page){
                        var that = this;
                        that.isload = true;
                        var str = '';
                        this.eid == 1? str +='/docadmin/loadExpertOrDoctors': str+= '/docadmin/loadDoctors'
                        page && $.get( str ,{
                            distCode: that.discodes,
                            hosid: that.hosid,
                            depId: that.depid,
                            keywords: that.keywords,
                            pageNumber: page || 1,
                            pageSize: 12,
                            dtype: that.dtype
                        })
                        .done(function(d){
                            if(BASE.isLost(d)) return BASE.checkLogin(),0;
                            that.pager = d.pager;
                        })
                        .always(function(){
                            that.isload = false;
                        })
                    }
                }
            })
        });
});
Vue.component('my-requirement', function (resolve, reject) {
    $.get('ajax/form/requirement.html')
        .done(function (d) {
            resolve({
                props: {
                    requir: {
                        type: Object
                    }
                },
                template: d
            })
        });
});
Vue.component('my-record', function (resolve, reject) {
    $.get('ajax/form/record.html')
        .done(function (d) {
            resolve({
                props: {
                    fdata: {
                        type: Object
                    }
                },
                template: d,
                methods: {
                    synchrecordWithUpload: function () {
                        $('#recordfiles_addfiles input').trigger('click');
                    },
                    recordfileok: function () {
                        Page.mainForms.othersForm.form.img_list = this.$refs.recordfiles.imglist,
                        Page.mainForms.othersForm.form.img_list_ids = this.$refs.recordfiles.ids
                    }
                }
            })
        });
});
Vue.component('my-enclosure', function (resolve, reject) {
    $.get('ajax/form/enclosure.html')
        .done(function (d) {
            resolve({
                props: {
                    fdata: {
                        type: Array
                    },
                    // readonly: {
                    //     type: Boolean,
                    //     deafult: false
                    // },
                    caseid: [String, Number]
                },
                template: d,
                data: function () {
                    return {
                        timer: '',
                        ind: 0,
                        attachments: [],
                        allType: {'1': 'CT', '2': 'DX', '3': 'MR', '4': 'DCM', '5': 'IMG', '99': '其他','6':'检查报告','7':'影像图片'},
                        OBJ: {},
                        isbtn: false,
                        arrN:[],
                        isShow: false
                    }
                },
                created: function (){
                    // 获取类型
                    $.get('/docadmin/gainSysDicList/3')
                    .done(function(d){
                        Page.encloType = d.dictionaries
                    })
                    this.attachments = this.fdata || []
                    this.isShow = sessionStorage.getItem('_token_utype') == 3 ? true: false
                },
                mounted: function (){
                    var that = this
                    $(document).bind( 'changeenclo', function (a){
                        that.attachments = JSON.parse(sessionStorage.getItem('_catch_attachments_'))
                    })
                },
                watch: {
                    fdata: function (o, n){
                        this.attachments = o
                    },
                    attachments: function (o, n){
                        o != n && window.setTimeout(this.initViewer, 1000)
                        var arr = []
                        o.forEach(function (item) {
                            arr.push({
                                id: item.id || '',
                                type: item.type,
                                reportTime: item.reportTime,
                                remark: item.remark,
                                attachmentIds: (item.attachmentIds instanceof Array) ? item.attachmentIds.join(',') : item.attachmentIds
                            })
                        })
                        this.$emit('attach', arr)
                    }
                },
                methods: {
                    initViewer: function (){
                        var that = this
                        loadCss('js/plugin/imgviewer/viewer.css', function () {
                            loadScript('js/plugin/imgviewer/viewer.min.js', function () {
                                that.OBJ.imgViewer && that.OBJ.imgViewer.viewer('destroy');
                                that.OBJ.imgViewer = $('#enclosure .attachments').viewer({
                                    url: 'data-src',
                                    navbar: true,
                                    title: false,
                                    transition: false,
                                    built: function () {
                                        $('#enclosure .viewer-canvas').bind('click', function (event) {
                                            event.target.localName != 'img' && that.OBJ.imgViewer.viewer('hide');
                                        });
                                    }
                                });
                            });
                        });
                    },
                    // 新增或编辑
                    enclosureupload: function (ids){
                        this.timer = ids || new Date().getTime()
                        sessionStorage.setItem('_catch_time_name_', (this.timer || this.attachments.createTime))
                        sessionStorage.setItem('_catch_attachments_', JSON.stringify(this.attachments))
                        
                        BASE.showModel({
                            remote: 'ajax/modal-content/modal-enclo.html',
                            cls: 'modal-lg'
                        });
                    },
                    imgsrc: function(src){
                        return BASE.imgsrc(src, 'img/avatars/64.png');
                    },
                    // 删除
                    del: function (ind){
                        if(confirm("确定要删除吗？")){
                            this.attachments.splice(ind, 1)
                            sessionStorage.setItem('_catch_attachments_', JSON.stringify(this.attachments))
                            $(document).trigger('changeenclo')
                            let arr=[]
                            this.attachments.forEach(function (item) {
                                arr.push({
                                    id: item.id || '',
                                    type: item.type,
                                    reportTime: item.reportTime,
                                    remark: item.remark,
                                    attachmentIds: (item.attachmentIds instanceof Array) ? item.attachmentIds.join(',') : item.attachmentIds
                                })
                            })
                            this.arrN = arr;
                            this.submitEnclo();
                        }
                    },
                    submitEnclo: function(){
                        this.saveForm('editCaseInfo', {
                            'attachments': JSON.stringify({'attachments': this.arrN}),
                        }, this.encloForm, '病例附件')
                    },
                    saveForm: function(fun, data, d, title){
                        data.caseid = this.caseid;
                        data.oid = Page.oid;
                        data.otype = Page.otype;
                        $.post('/doctor/' + fun, data)
                            .done(function () {
                                $.smallBox({
                                    title: "保存提示",
                                    content: title + "修改成功",
                                    color: $.color.success,
                                    timeout: 3000
                                });
                            })
                            .fail(function () {
                                $.smallBox({
                                    title: "保存提示",
                                    content: title + "修改失败",
                                    color: $.color.error,
                                    iconSmall: "fa fa-times"
                                });
                            })
                    }
                }
            })
        });
});
