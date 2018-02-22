var _origin = (function(){
	if (window["context"] == undefined) {
	    if (!window.location.origin) {
	        window.location.origin = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port: '');
	    }
	    window["context"] = location.origin+"/V6.0";
	}
	return window.location.origin;
})();
seajs.config({
    base: _origin + "/sea-modules/",
	alias: {
	  "jquery": "jquery/1.10.1/jquery.js",
	  'bootstrap': 'bootstrap/js/bootstrap.js',
	  'select2': 'select2/js/select2.min.js',
	  'datatable': 'datatable/js/jquery.dataTables.min.js',
	  'datetime': 'datetimepicker/bootstrap-datetimepicker.js',
	  'webuploader': 'webuploader/webuploader.sea.js',
	  'imgviewer': 'imgviewer/viewer.min.js',
	  'highchart': 'highchart/highcharts.sea.js',
	  'swiper3': 'swiper/3.3.1/js/swiper.jquery.min.js',
	  'swiper2': 'swiper/2.7.0/idangerous.swiper.min.js',
	  'nprogress': 'nprogress/nprogress.js'
	}
});
