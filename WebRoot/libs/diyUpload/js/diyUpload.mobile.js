/* 
*	jQuery文件上传插件,封装UI,上传处理操作采用Baidu WebUploader;
*	@Author 黑爪爪;
*/
(function ($) {

    $.fn.extend({
        /*
        *	上传方法 opt为参数配置;
        *	serverCallBack回调函数 每个文件上传至服务端后,服务端返回参数,无论成功失败都会调用 参数为服务器返回信息;
        */
        diyUpload: function (opt, serverCallBack) {
            if (typeof opt != "object") {
                alert('参数错误!');
                return;
            }
            var $fileInput = $(this), strType = $fileInput.attr('data-type');
            var $fileInputId = $fileInput.attr('id'),procs;

            //组装参数;
            if (opt.url) {
                opt.server = opt.url;
                delete opt.url;
            }

            if (opt.success) {
                var successCallBack = opt.success;
                delete opt.success;
            }

            if (opt.error) {
                var errorCallBack = opt.error;
                delete opt.error;
            }
            //迭代出默认配置
            opt = $.extend(getOption('#' + $fileInputId),opt);
            procs = opt.process;
            
            if (strType == 'file') {
                opt['pick']['innerHTML'] = '选择文件';
                opt['accept'] = null;
            }

            if (!WebUploader.Uploader.support()) {
                alert( '上传组件不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
                throw new Error( 'WebUploader does not support the browser you are using.' );
                return false;
            }
            var webUploader = getUploader(opt);

            //绑定文件加入队列事件;
            webUploader.on('fileQueued', function (file) {
                createBox($fileInput, file, webUploader, opt);
                listLocation($fileInput);
                disabledBtn($fileInput);
                if (SetMoveState1) SetMoveState1($fileInput);
            });

            //进度条事件
            webUploader.on('uploadProgress', function (file, percentage) {
                var $fileBox = $('#fileBox_' + file.id);
                var $diyBar = $fileBox.find('.diyBar');
                $diyBar.show();
                percentage = percentage * 100;
                showDiyProgress(percentage.toFixed(2), $diyBar);
            });
            if(opt.changeformData){
            	webUploader.on('uploadStart', function (file) {
            		webUploader.option('formData',opt.changeformData());
                });            	
            }
            //全部上传结束后触发;
            webUploader.on('uploadFinished', function () {
            	enabledBtn($fileInput);
            	if (SetMoveState1) {
            		SetMoveState1($fileInput);
            	}else {
            		//procs && $fileInput.closest('.fileBoxUl').find('li').fadeIn()
            	}
            });

            //绑定发送至服务端返回后触发事件;
            webUploader.on('uploadAccept', function (object, data) {
                if (serverCallBack) serverCallBack(data);
            });

            //上传成功后触发事件;
            webUploader.on('uploadSuccess', function (file, response) {
                var $fileBox = $('#fileBox_' + file.id);
                var $diyBar = $fileBox.find('.diyBar');
                $fileBox.removeClass('diyUploadHover').children('.diyCancel').show();
                $fileBox.attr('data-id',response['upid'] || response);
                $diyBar.hide();
                
                //procs && $fileBox.fadeOut();
                if (successCallBack) {
                    successCallBack(response);
                }
            });

            //上传失败后触发事件;
            webUploader.on('uploadError', function (file, reason) {
                var $fileBox = $('#fileBox_' + file.id);
                var $diyBar = $fileBox.find('.diyBar');
                showDiyProgress(0, $diyBar, '上传失败!');
                var err = '上传失败! 文件:' + file.name + ' 错误码:' + reason;
                //procs && $fileBox.fadeOut();
                if (errorCallBack) {
                    errorCallBack(err);
                }
            });

            //选择文件错误触发事件;
            webUploader.on('error', function (code) {
                var text = '';
                switch (code) {
                    case 'F_DUPLICATE': text = '该文件已经被选择了!';
                        break;
                    case 'Q_EXCEED_NUM_LIMIT': text = '上传文件数量超过限制!';
                        break;
                    case 'F_EXCEED_SIZE': text = '文件大小超过限制!';
                        break;
                    case 'Q_EXCEED_SIZE_LIMIT': text = '所有文件总大小超过限制!';
                        break;
                    case 'Q_TYPE_DENIED': text = '文件类型不正确或者是空文件!';
                        break;
                    default: text = '未知错误!';
                        break;
                }
                alert(text);
            });
        }
    });

    function getOption(objId) {
        return {
            pick: {
                id: objId,
                innerHTML: "点击选择图片",
                multiple: true
            },
            accept: {
                title: "Images",
                extensions: "gif,jpg,jpeg,bmp,png",
                mimeTypes: "image/jpg,image/jpeg,image/gif,image/bmp,image/png"
            },
            thumb: {
                width: 170,
                height: 170,
                quality: 70,
                allowMagnify: false,
                crop: true,
                type: "image/jpeg"
            },
           // compress:true, //配置压缩的图片的选项
            auto:true,
            sendAsBinary:true, //是否已二进制的流的方式发送文件
            prepareNextFile:true,
            chunked:false,//是否分片上传
            //threads:3,
            swf: window.location.origin +'/lib/diyUpload/Uploader.swf',
            //chunkSize: 2048 * 1024, //第片大小，2M
            fileNumLimit: 100,               //验证文件总数量, 超出则不允许加入队列。
            fileSizeLimit: 100*50*2048*1024,     //验证文件总大小是否超出限制, 超出则不允许加入队列。
            fileSingleSizeLimit: 20 * 2048*1024 //验证单个文件大小是否超出限制, 超出则不允许加入队列。
        };
    }

    function getUploader(opt) {
        return new WebUploader.Uploader(opt);
    }

    //操作进度条;
    function showDiyProgress(progress, $diyBar, text) {
        if (progress >= 100) {
            progress = progress + '%';
            text = text || '上传完成';
        } else {
            progress = progress + '%';
            text = text || progress;
        }
        var $diyProgress = $diyBar.find('.diyProgress');
        var $diyProgressText = $diyBar.find('.diyProgressText');
        $diyProgress.width(progress);
        $diyProgressText.text(text);
    }

    //取消事件;	
    function removeLi($li, file_id, webUploader) {
        webUploader.removeFile(file_id);
        if ($li.siblings('li').length <= 0) {
            $li.parents('.parentFileBox').remove();
        } else {
            $li.remove();
        }

    }

    //创建文件操作div;	
    function createBox($fileInput, file, webUploader, opt) {
        var file_id = file.id;
        var $parentFileBox = $fileInput.prev('.parentFileBox');
        //添加父系容器;
        if ($parentFileBox.length <= 0) {
        	$parentFileBox = $fileInput.closest('.parentFileBox');
        	if ($parentFileBox.length <= 0) {
	            var div = '<div class="parentFileBox"> \
							<ul class="fileBoxUl"></ul>\
						</div>';
	            $fileInput.before(div);
	            $parentFileBox = $fileInput.prev('.parentFileBox');
	
	        }
    	}

        //添加子容器;
        var li = '<li id="fileBox_' + file_id + '" class="diyUploadHover"> \
					<div class="viewThumb"></div> \
					<div class="diyCancel"></div> \
					<div class="diySuccess"></div> \
					<div class="diyFileName">' + file.name + '</div>\
					<div class="diyBar"> \
							<div class="diyProgress"></div> \
							<div class="diyProgressText">0%</div> \
					</div> \
				</li>';
		var btnUp = $parentFileBox.find('.webuploader-pick'),btnLi = btnUp.closest('li');
		if(opt.fileNumLimit == 1){
			$.each(webUploader.getFiles(),function(i,file){
				file_id != file.id && webUploader.removeFile( file ,true);
			});
			btnLi.size() ? (btnLi.siblings('li').remove(),btnLi.before(li) ) : $parentFileBox.children('.fileBoxUl').html(li);
		}else{
			btnLi.size() ? btnLi.before(li) : $parentFileBox.children('.fileBoxUl').prepend(li);
		}
        //父容器宽度;
        //var $width = $('.fileBoxUl>li').length * 180;
        //var $maxWidth = $fileInput.parent().width();
        //$width = $maxWidth > $width ? $width : $maxWidth;
        //$parentFileBox.width($width);

        var $fileBox = $parentFileBox.find('#fileBox_' + file_id + '.diyUploadHover');

        /*//绑定取消事件;
        var $diyCancel = $fileBox.children('.diyCancel').one('click', function () {
        	var ul = $(this).closest('.parentFileBox'),hidden = ul.next('input[type="hidden"]'),hs = [];
        	removeLi($(this).parent('li'), file_id, webUploader);
        	ul.find('.viewThumb img').each(function(){
        		var id = $(this).attr('data-id');
        		id && hs.push(id);
        	});
        	hidden.val(hs.join(','));
        });*/

        if (file.type.split("/")[0] != 'image') {
            var liClassName = getFileTypeClassName(file.name.split(".").pop());
            $fileBox.addClass(liClassName);
            return;
        }
        //生成预览缩略图;
        webUploader.makeThumb(file, function (error, dataSrc) {
            if (!error) {
                $fileBox.find('.viewThumb').append('<img src="' + dataSrc + '" >');
            }
        });
        webUploader.makeThumb(file, function (error, dataSrc) {
            if (!error) {
                $fileBox.find('.viewThumb img').attr('data-src',dataSrc);
            }
        },1,1);
    }
    function listLocation(fileInput){
    	var lis = fileInput.closest('.parentFileBox').find('.fileBoxUl li[id^="fileBox_"]'),
    		t = lis.size();
    	$.each(lis,function(i,li){
    		var $iL = $(li).find('.imglocation');
    		$iL.remove();
    		$iL = $('<div class="imglocation">'+ (t-i) +' / '+ t +'</div>');
    		$(li).append($iL);
    	});
    }
    function disabledBtn(fileInput){
    	var btn = fileInput.closest('form').find('.btn'),labelBtn = $('<button disabled="disabled" class="labelBtn btn btn-primary">图片上传中，请稍等……</button>');
    	!btn.hasClass('labelBtn') && !$('.labelBtn').size() && btn.addClass('readyHide').hide().after(labelBtn);
    }
    function enabledBtn(fileInput){
    	var btn = fileInput.closest('form').find('.readyHide'),labelBtn = $('.labelBtn');
    	btn.removeClass('readyHide').show();
    	labelBtn.remove();
    }
    //获取文件类型;
    function getFileTypeClassName(type) {
        var fileType = {};
        var suffix = '_diy_bg';
        fileType['pdf'] = 'pdf';
        fileType['zip'] = 'zip';
        fileType['rar'] = 'rar';
        fileType['csv'] = 'csv';
        fileType['doc'] = 'doc';
        fileType['xls'] = 'xls';
        fileType['xlsx'] = 'xls';
        fileType['txt'] = 'txt';
        fileType = fileType[type] || 'txt';
        return fileType + suffix;
    }

})(jQuery);