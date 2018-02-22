define(['datetime','datetimepicker/bootstrap-datetimepicker.min.css','datetimepicker/bootstrap-datetimepicker.zh-CN.js'],function(require,exports,module) {
	require('datetime'),
    require('datetimepicker/bootstrap-datetimepicker.zh-CN.js');
	exports.init = function(selector,options){
      	$(selector || 'input.date' || 'input[ltype="date"]').datetimepicker($.extend({
      		format:'yyyy-mm-dd',
      		minView:2,
      		language:'zh-CN',
      		autoclose:true
      	},options));
    };
});