define(['nprogress','nprogress/nprogress.css'],function(require,exports,module) {
	require('nprogress'),
	exports.init = function(){
		/*NProgress.start();
	    NProgress.set(0.4);
	    NProgress.inc();
	    NProgress.done();*/
		return NProgress;
    };
});