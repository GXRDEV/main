define(['imgviewer','imgviewer/viewer.min.css'],function(require,exports,module) {
	require('imgviewer'),
	exports.init = function(selector,options){
		var handler = function (e) { /*console.log(e.type);*/ };
		var ops = {
			url: 'src',
			navbar:false,
			title:false,
			build: handler,
			built: handler,
			show: handler,
			shown: handler,
			hide: handler,
			hidden: handler
		};
		var $selector = $(selector || '.diyUpload');
		$selector.viewer($.extend(ops,options));
		return $selector;
    };
});