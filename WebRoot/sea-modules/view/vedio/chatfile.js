define(['webuploader','webuploader/webuploadsingle.css'],function(require,exports,module) {
	var WebUploader = require('webuploader');
	(function ($) {
	    $.fn.extend({
	        uploadsingle: function (opt, serverCallBack) {
	            if (typeof opt != "object") {
	                alert('参数错误!');
	                return;
	            }
	            var $fileInput = $(this);
	            var $fileInputId = $fileInput.attr('id'),procs;
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
	            opt = $.extend(getOption('#' + $fileInputId,document.getElementById($fileInputId).innerHTML),opt);
	            procs = opt.process;	            
	            if (!WebUploader.Uploader.support()) {
	                alert( '上传组件不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
	                throw new Error( 'WebUploader does not support the browser you are using.' );
	                return false;
	            }
	            var webUploader = getUploader(opt);
	            //绑定文件加入队列事件;
	            webUploader.on('fileQueued', function (file) {
	                createBox($fileInput, file, webUploader);
	                opt.beforepost($fileInput);
	            });
	            //进度条事件
	            webUploader.on('uploadProgress', function (file, percentage) {
	                var $fileBox = $('#fileBox_' + file.id);
	                var $diyBar = $fileBox.find('.progressc');
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
	            	opt.afterpost($fileInput);
	            	//opt.change($fileInput)
	            });

	            //绑定发送至服务端返回后触发事件;
	            webUploader.on('uploadAccept', function (object, data) {
	                if (serverCallBack) serverCallBack(data);
	            });

	            //上传成功后触发事件;
	            webUploader.on('uploadSuccess', function (file, response) {
	            	var dataid = response['urlpath'];
	                var $fileBox = $('#fileBox_' + file.id);
	                $fileBox.attr('data-id',dataid).removeClass('imgprocing');
	                	//.children('.diyState').html('<i class="iconfont error">&#xe614;</i>').show();
	                $fileBox.find('.progressc').hide();
	                if (successCallBack) {
	                    successCallBack($fileBox,dataid);
	                }
	            });

	            //上传失败后触发事件;
	            webUploader.on('uploadError', function (file, reason) {
	                var $fileBox = $('#fileBox_' + file.id);
	                $fileBox.find('.progressc').hide();
	                $fileBox.children('.diyState').html('<i class="iconfont error">&#xe624;</i>').show();	                
	                //showDiyProgress(0, $diyBar, '上传失败!');
	                var err = '上传失败! 文件:' + file.name + ' 错误码:' + reason;
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

	    function getOption(objId,name) {
	    	var acceptObj = {
	                title: "Images",
	                extensions: "gif,jpg,jpeg,bmp,png",
	                mimeTypes: "image/jpg,image/jpeg,image/gif,image/bmp,image/png"
	            },pickObj = {
	    			id: objId,
		            innerHTML: name || "选择图片",
		            multiple: false
		        };
			(objId.indexOf('-luyin') != -1 || objId.indexOf('-mp3') != -1) && (acceptObj = {
	                title: "Audios",
	                extensions: "mp3,wav",
	                mimeTypes: "audio/*"
	        },pickObj = {
	    			id: objId,
		            innerHTML: name || "选择录音",
		            multiple: false
		    });
	        return {
	            pick: pickObj,
	            accept: acceptObj,
	            thumb: {
	                width: 80,
	                height: 80,
	                quality: 70,
	                allowMagnify: false,
	                crop: true,
	                type: "image/jpeg"
	            },
	            auto:true,
	            sendAsBinary:true, //是否已二进制的流的方式发送文件
	            process:true,
	            prepareNextFile:true,
	            chunked:false,
	            threads:3,
	            method:"post",
	            swf: window.location.origin +'/lib/diyUpload/Uploader.swf',
	            chunkSize: 2048 * 1024, 			//第片大小，2M
	            fileNumLimit: 100,               	//验证文件总数量, 超出则不允许加入队列。
	            fileSizeLimit: 100*50*2048*1024,    //验证文件总大小是否超出限制, 超出则不允许加入队列。
	            fileSingleSizeLimit: 50*2048*1024, 	//验证单个文件大小是否超出限制, 超出则不允许加入队列。
	            beforepost:function(){},
	            afterpost:function(){},
	    	    change:function(){}
	        };
	    }
	    function getUploader(opt) {
	        return new WebUploader.Uploader(opt);
	    }
	    //操作进度条;
	    function showDiyProgress(progress, $diyBar) {
	        if (progress >= 100) {
	            progress = progress + '%';
	        } else {
	            progress = progress + '%';
	        }
	        var $diyProgress = $diyBar.find('.barc');
	        var $diyProgressText = $diyBar.find('.bartxt');
	        $diyProgress.width(progress);
	        $diyProgressText.text(progress);
	    }
	    //取消事件;	
	    function removeLi($li, file_id, webUploader) {
	        webUploader.removeFile(file_id);
	        $li.remove();
	    }
	    //创建文件操作div;	
	    function createBox($fileInput, file, webUploader) {
	        var file_id = file.id;
	    	
	        var li = '<div id="fileBox_' + file_id + '" class="thumbimg imgprocing">\
						<div class="diyState"></div><div class="viewThumb"></div>\
	        			<div class="progressc" style="display:none;"><div class="barc" style="width:0%;"></div><div class="bartxt">0%</div></div>\
					</div>';

	        var tmp = '<div class="hhlist clearfix me">';
	    	tmp += '<span class="cols0"><span class="thumb"><img src="'+ window.location.origin +'/img/defdoc.jpg"></span></span>';
	    	tmp += '<span class="cols1"><span class="text">'+ li +'</span></span></div>';
	    	
	        $(".dialog .bodyer").append(tmp);
	        var $fileBox = $('#fileBox_' + file_id);
	        
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
	        fileType['mp3'] = 'mp3';
	        fileType['wav'] = 'mp3';
	        fileType['ram'] = 'mp3';
	        fileType['ra'] = 'mp3';
	        fileType['mid'] = 'mp3';
	        fileType = fileType[type] || 'txt';
	        return fileType + suffix;
	    }
	})(jQuery);
	
	exports.init = function(id){
		
	};
});