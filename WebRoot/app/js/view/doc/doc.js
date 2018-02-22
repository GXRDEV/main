function DOC_JS(){
    var mainForms = new Vue({
        el: '#main-forms',
        data: {
            caseid: '',
            userid: '',
            dockingMode: '2',
            room: true,
            footer: false,
            maintabs: {
                list: [
                    {
                        key:'info',
                        value:'患者病历'
                    },
                    {
                        key:'screen',
                        value:'专家屏幕'
                    },
                    {
                        key:'report',
                        value:'就诊报告'
                    }
                ],
                selected: 'info'
            },
            baseForm:{
                form: {
                    username:'',
                    sex:'1',
                    age:'',
                    idcard:'',
                    telphone:''
                },
                old:{},
                validate: false,
                edit: false,
                isload: false,
                issave: false
            },
            profileForm:{
                form: {
                    description:'',
                    mainSuit:'',
                    historyIll:'',
                    presentIll:'',
                    examined:'',
                    assistantResult:'',
                    initialDiagnosis:'',
                    askProblem:'',
                    familyHistory:''
                },
                old:{},
                edit: false,
                isload: false,
                issave: false
            },
            lisForm:{
                form:{
                    list: [],
                    lis_ids: []
                },
                old:{},
                validate: false,
                edit: false,
                isload: false,
                issave: false
            },
            pacForm:{
                form:{
                    list: [],
                    pacs_ids: []
                },
                old:{},
                validate: false,
                edit: false,
                isload: false,
                issave: false
            },
            othersForm: {
                form: {
                    localFiles:[],
                    normalImages:[]
                },
                old:{},
                edit: false,
                isload: false,
                issave: false
            }
        },
        methods: {
            tapMainTabs: function(key){
                this.maintabs.selected = key;
            },
            submitBase: function() {
                this.saveForm(this.baseForm, '基本信息');
            },
            submitProfile: function() {
                this.saveForm(this.profileForm, '诊疗信息');
            },
            submitLiss: function(){
                this.lisForm.form.lis_ids = (function(list){
                    var arr = [];
                    $.each(list,function(i,n){
                        $.each(n.beans,function(ii,nn){
                            arr.push(nn.key);
                        });
                    });
                    return arr;
                })(this.lisForm.form.list);

                this.saveForm(this.lisForm, '检查检验');
            },
            submitPacs: function(){
                this.pacForm.form.pacs_ids = (function(list){
                    var arr = [];
                    $.each(list,function(i,n){
                        $.each(n.beans,function(ii,nn){
                            arr.push(nn.key);
                        });
                    });
                    return arr;
                })(this.pacForm.form.list);
                this.saveForm(this.pacForm, '影像信息');
            },
            submitOthersForm: function(){
                this.saveForm(this.othersForm, '影像信息');
            },
            beginEdit: function(name) {
                var f = this[name];
                f.edit = !f.edit;
                f.old = $.extend(true, {}, f.form);
            },
            cancelEdit: function(name) {
                var form = this[name];
                form.edit = !form.edit;  
                form.form = $.extend(true, {}, form.old);
                form.old = null;
            },
            validateBase: function(){
                var _bform = this.$refs.base;
                return !this.baseForm.form.username || 
                    _bform.validateAge(this.baseForm.form.age) || 
                    _bform.validateCard(this.baseForm.form.idcard) || 
                    _bform.validateTel(this.baseForm.form.telphone);
            },
            saveForm: function(d, title){
                d.issave = true;
                var data = (function(){
                    if('lis_ids' in d.form){
                        return { "lis_ids" : d.form.lis_ids.join(',')} 
                    }
                    if('pacs_ids' in d.form){
                        return { "pacs_ids" : d.form.pacs_ids.join(',')}                   
                    }
                    if('normalImages' in d.form){
                        return { "normalImages" : d.form.normalImages.join(',')}                      
                    }
                    return $.extend({}, d.form);
                })();
                data.caseid = this.caseid;
                data.oid = Page.oid;
                $.post('/doctor/saveInfos', data)
                    .done(function () {
                        d.edit = false;
                        $.smallBox({
                            title: title + "保存成功",
                            color: $.color.success,
                            timeout: 3000
                        });
                    })
                    .fail(function () {
                        $.smallBox({
                            title: title + "保存失败",
                            color: $.color.error,
                            iconSmall: "fa fa-times"
                        });
                    })
                    .always(function(){
                        d.issave = false;
                    });
            }
        },
        created: function(){
            Page.mainForms = this;
            Page.Ctr.init(this);
        }
    });
}