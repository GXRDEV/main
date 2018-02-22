define(['swiper3','swiper/3.3.1/css/swiper.min.css'],function(require,exports,module) {
	require('swiper3'),
	exports.init = function(selector,options){
		return new Swiper (selector || '.swiper-container', $.extend({},options)); 		
    };
});