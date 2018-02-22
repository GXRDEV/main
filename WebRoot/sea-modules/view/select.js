define(['select2','select2/css/select2.min.css'],function(require,exports,module){ 
	require('select2');
	exports.init = function(selector,options){
		$(selector || 'select').select2($.extend({
			dropdownAutoWidth:true,
			minimumResultsForSearch: -1
		},options));
	};
});