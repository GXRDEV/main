define(['swiper2','swiper/2.7.0/idangerous.swiper.css'],function(require,exports,module) {
	require('swiper2'),
	exports.init = function(selector,options){
		return new Swiper (selector || '.swiper-container', $.extend({},options)); 		
    };
});