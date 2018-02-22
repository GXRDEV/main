//数组去重复
Array.prototype.uniques = function() { var res = [], hash = {};for(var i=0, elem; (elem = this[i]) != null; i++)  {if (!hash[elem]){res.push(elem);hash[elem] = true;}}return res;}
define(function(require, exports, module) {
	require('bindEvent').init();
});
define('bindEvent',function(require, exports, module){
    exports.init = function(){
    	var Tables = require('Tables'),Controller = require('Controller');
        $('#content').delegate('.js-editinfo','click',function(){
        	var tr = $(this).closest('tr'),tablename = tr.closest('.widget-content').attr('data-tablespace');
        	var table = Tables[tablename] || [];
        	$(this).closest('tr').addClass('editing').find('.text').each(function(i){
        		var obj = table[i] || {},sval = $(this).attr('data-val') || '';
        		if(!obj.type) return;
        		$(this).replaceWith(Controller.getInput(obj,sval));
        	});
        	Controller.listenTime();
        }).delegate('.js-addinfo','click',function(){
        	var parent = $(this).closest('.widget-content'),tablename = parent.attr('data-tablespace');
        	parent.find('tbody').append(Controller.getTR(tablename));
        	Controller.listenTime();
        }).delegate('.js-saveinfo','click',function(){
        	var btn = $(this),tr = btn.closest('tr');        	
        	Controller.saveForm(tr,function(did){
        		tr.removeClass('editing').attr('id',did);
        		Controller.getLabel(tr);
        	});
        }).delegate('.js-delinfo','click',function(){
        	var tr = $(this).closest('tr');
        	confirm('确定要删除？') && Controller.delTR(tr,function(did){
        		tr.remove();
        	});
        }).delegate('.js-cancelinfo','click',function(){     
        	var tr = $(this).closest('tr');
        	tr.removeClass('editing');
        	Controller.getLabel(tr,true);
        }).delegate('[haschild="true"]','change',function(){
        	var input = $(this).closest('.timerang').find('input'),pname = this.name;
        	var start = input[0].value,end = input[1].value;
        	if(!start || !end) return ;
        	$('[parent="timerang"]').val(Controller.getMin(start,end) + '分');
        })
        .delegate('.js-inittime','click',function(){
        	var btn = $(this),tr = btn.closest('.hasdaterang'); 
        	var start = tr.find('[name="start"]'),
        		end = tr.find('[name="end"]');
        	Controller.initTime(start,end);
        });
        
        Controller.listenSelect();
        
        seajs.use('view/date',function(date){
			date.init('input[ltype="dateRang"]',{
				format:'yyyy-mm-dd',
				startView:2,
    			minView:2
			});
    	});
    };
});
define('Tables',{
	"week":[{key:"1",value:"星期一"},{key:"2",value:"星期二"},{key:"3",value:"星期三"},{key:"4",value:"星期四"},{key:"5",value:"星期五"},{key:"6",value:"星期六"},{key:"0",value:"星期日"}],
	"day":[{key:"am",value:"上午"},{key:"pm",value:"下午"},{key:"em",value:"晚上"}],
	"state":[{key:"1",value:"开启"},{key:"0",value:"关闭"}],
	"RemoteTime":[{type:"select",name:"week",option:"week"},{type:"timerang",name:"timerang",haschild:"true"},{type:"checkbox",name:"state",option:"state"},{type:"text",name:"mark"}],
	"editor":'<button class="btn1 state1 js-editinfo"><i class=iconfont>&#xe606;</i></button><button class="btn1 state2 js-saveinfo"><i class=iconfont>&#xe60b;</i></button><button class="btn1 state2 js-cancelinfo"><i class=iconfont>&#xe609;</i></button><button class="btn1 state1 js-delinfo"><i class=iconfont>&#xe607;</i></button>'
});
define('Controller',['view/base','view/date'],{
	getInput:function(o,sval){
		var input = '';
		switch(o.type){
			case 'select':
				input = this.getSelect(o.option,o.name,sval);
				break;
			case 'timerang':
				sval = sval.split(',');
				input = '<label class="timerang"><span class="time1"><input type="text" name="'+ o.name +'" haschild="'+ o.haschild +'" value="'+ (sval[0]||'') +'"/></span><span class="split iconfont">&#xe608;</span><span class="time1"><input type="text" name="'+ o.name +'" haschild="'+ o.haschild +'" value="'+ (sval[1]||'') +'"/></span></label>';
				break;
			case 'checkbox':
				if(sval=='1'){
					input = '<label class="checkbox"><input type="checkbox" checked name="'+ o.name +'" value="'+ sval +'"/>开启</label>';
				}else{
					input = '<label class="checkbox"><input type="checkbox" name="'+ o.name +'" value="'+ sval +'"/>开启</label>';
				}
				break;
			case 'text':
			case 'number':
				input = '<input type="'+ o.type +'" name="'+ o.name +'" value="'+ sval +'"/>';
				break;
			case 'label':				
				input = '<input type="text" class="readonly" readonly '+ (o.parent ? ('parent="'+ o.parent +'"') : '') +' name="'+ o.name +'" value="'+ sval +'"/>';
				break;
		}
		return '<label class="inputs" data-val="'+ sval +'">'+ input +'</label>';
	},
	getSelect:function(o,name,sval){
		var ops = seajs.require('Tables')[o] || [],op = [];
		for(var i = 0;i<ops.length;i++){
			var cur = ops[i],selected = cur.key == sval ? 'selected' : '';
			op.push('<option value="'+ cur.key +'" '+ selected +'>'+ cur.value +'</option>');
		}
		return '<select name="'+ ( name || o ) +'">'+ op.join('') +'<select>';
	},
	getTR:function(o){
		var Tables = seajs.require('Tables'),ops = Tables[o] || [],op = [];
		for(var i = 0;i<ops.length;i++){
			var cur = ops[i];
			op.push('<td><span class="td">'+ this.getInput(cur,'') +'</span></td>');
		}
		op.push('<td><span class="td center">'+ Tables['editor'] +'</span></td>');
		return '<tr class="editing">'+ op.join('') +'</tr>';
	},
	getVals:function(tr){
		var input = {};
		tr.find('.inputs :input').each(function(i){
			var arr=[],value = (this.type || this.localName) == 'checkbox' ? (this.checked ? '1' : '0') : this.value;
			this.name && this.name in input ? (
					arr = input[this.name].split(','),
					arr.push(value || ''),
					arr = arr.uniques(),
					input[this.name] = arr.join(',')
			) : (input[this.name] = value || '');
			if(this.name == 'timerang'){
				input['startTime'] = input['timerang'].split(',')[0] || '';
				input['endTime'] = input['timerang'].split(',')[1] || '';
			}
		});
		tr.attr('id') && (input['id'] = tr.attr('id'));
		return input;
	},
	getByKey:function(key,o){
		var ops = o ? (seajs.require('Tables')[o] || []) : [],op = [];
		if(!o) return key.split(',').join(' 至 ');
		for(var i = 0;i < ops.length;i++){
			ops[i].key == key && op.push(ops[i].value);
		}
		return op.join(',') || key;
	},
	getLabel:function(tr,init){
		var counter = 0,parent = tr.closest('.widget-content'),id = tr.attr('id') || '',
			tablename = parent.attr('data-tablespace'),_this = this;
		var Tables = seajs.require('Tables'),ops = Tables[tablename] || [],inputs = this.getVals(tr);
		tr.find('.inputs').each(function(i){
			var defval = this.getAttribute('data-val') || '',th = ops[i],txt = '';
			if(!id && !defval) counter++;
			!init ? 
				(defval = inputs[th.name],txt = _this.getByKey(defval,th.option || '')) : 
				(txt = _this.getByKey(defval,th.option || ''));			
			$(this).replaceWith('<label class="text" data-val="'+ defval +'">'+ (txt || '&nbsp;') +'</label>')
		});
		if(counter == ops.length) tr.remove();
	},
	saveForm:function(tr,callback){
		var base = seajs.require('view/base'),_this = this,inputs = this.getVals(tr),
			parent = tr.closest('.widget-content'),action=parent.attr('data-action'),id = tr.attr('id') || '';
		inputs['id']=id;
		inputs['expertid'] = expertid;
		action && base.showTipIngA('操作执行中……').ajax(action,inputs,function(data){
			base.showTipS('操作成功');
			callback(data.tid || id);
		},function(){
			base.showTipE('操作失败');
			callback(id);
		});
	},
	delTR:function(tr,callback){
		var base = seajs.require('view/base'),_this = this,id = tr.attr('id'),
		parent = tr.closest('.widget-content'),tablespace=parent.attr('data-tablespace'),delurl='';
		if(!id) return callback(),false;
		delurl="expert/delremotetime";
		base.showTipIngA('正在删除……').ajax(delurl,{tid:id},function(data){
			base.showTipS('删除成功');
			callback();
		},function(){
			base.showTipE('删除失败');
		});
	},
	listenTime:function(){
		seajs.use('view/date',function(date){
			date.init('input[name="timerang"]',{
				format:'h:ii',
				startView:0,
    			minView:0,
    			minuteStep: 30,
    			hasscroll:true
			});
    	});
	},
	listenSelect:function(){
		var _this = this;
		$('[data-option]').each(function(){
			var v = $(this).attr('data-val'),o = $(this).attr('data-option');
			$(this).html(_this.getByKey(v,o || ''));
		});
	},
	getMin:function(s,e){
		return parseInt((e.split(':')[0] * 60 + parseInt(e.split(':')[1],10))-(s.split(':')[0] * 60 + parseInt(s.split(':')[1],10)),10)
	},
	initTime:function(s,e,callback){
		var base = seajs.require('view/base'),_this = this;
		var sv = s.val(),ev = e.val();		
		if(!sv || !ev){
			base.showTipE('开始结束时间不能为空');
			return false;
		}
		if(sv > ev){
			base.showTipE('开始不能早于结束时间');
			return false;
		}
		base.showTipIngA('正在生成……').post(_href + 'expert/generateConShedules',{start:sv,end:ev,expertid:expertid},function(data){
			base.showTipS('生成成功');
			window.setTimeout(function(){
				location.href = location.href;
			},300);
		},function(){
			base.showTipE('生成失败');
		});
	}
});