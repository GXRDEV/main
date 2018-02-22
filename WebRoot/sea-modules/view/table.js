define(['datatable','datatable/css/dataTables.custom.css'],function(require,exports,module) {
	require('datatable'),
	exports.init = function(selector,options){
      	$(selector || '.table').DataTable($.extend({
      		"bJQueryUI": true,"sDom": 'r<".tablelist"t><"F"flp>',
            "iDisplayStart":0,"iDisplayLength":10,
            "bDestroy":true,"bRetrieve":true,"bStateSave":true,
            "bFilter": true,"bLengthChange": true,"bProcessing": true,
            "aoColumnDefs":[{"bSortable":false,"aTargets":[0]}], 
            "oLanguage": {
                "sProcessing": "正在获取数据，请稍后...",
                "sLengthMenu": "每页显示 _MENU_ 条",
                "sZeroRecords": "没有您要搜索的内容",
                "sInfoEmpty": "记录数为0",
                "sInfoFiltered": "(全部记录数 _MAX_ 条)",
                "sSearch": "搜索 ",
                "oPaginate": {
                    "sFirst": "第一页",
                    "sPrevious": "上一页",
                    "sNext": "下一页",
                    "sLast": "最后一页"
                }
            }
      	},options));
    };
});