define(['highchart'],function(require,exports,module) {
	require('highchart'),
	exports.init = function(selector,options){
      	$(selector || '.charts').highcharts($.extend({
      		title: {text: null,floating:true},
	        credits:{enabled:false},
	        yAxis:{title:null},
	        legend: {enabled:false},
	        scrollbar: { enabled: true}
      	},options));
    };
});