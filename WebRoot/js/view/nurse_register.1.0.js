var _b = {
	_post:function(url,ops,fun,err){
		return _$(this.href + url,ops,fun,err);
	},
	getHos:function(){
		var pid = $('#province').val(),cid = $('#city').val();
		if(!cid){
			return $('#belongHosp,#belongDep').html('<option value="">请选择</option>'),false;
		}
		_b._post('wtspz/loadhospitals', {pid:pid,cid:cid}, function(d){
			var os = '<option value="">请选择</option>';
			d.hospitals && $.each(d.hospitals,function(i,o){
				os += '<option value="'+ o.hospitalId +'">'+ o.hospitalName +'</option>';
			});
			$('#belongHosp').html(os);
			$('#belongDep').html('<option value="">请选择</option>');
		});
	},
	getDep:function(){
		var hosid = $('#belongHosp').val();
		if(!hosid){
			return $('#belongDep').html('<option value="">请选择</option>'),false;
		}
		_b._post('wtspz/loaddeparts', {hosid:hosid}, function(d){
			var os = '<option value="">请选择</option>';
			d.departs && $.each(d.departs,function(i,o){
				os += '<option value="'+ o.officeId +'">'+ o.officeName +'</option>';
			});
			$('#belongDep').html(os);
		});
	},
	valideTel: function(text) {
        var _emp = /^\s*|\s*$/g;
        text = text.replace(_emp, "");
        var _d = /^1[3578][01379]\d{8}$/g;
        var _l = /^1[34578][01245678]\d{8}$/g;
        var _y = /^(134[012345678]\d{7}|1[34578][012356789]\d{8})$/g;
        if (_d.test(text)) {
            return true;
        } else if (_l.test(text)) {
            return true;
        } else if (_y.test(text)) {
            return true;
        }
        alert('请输入有效的电话号码')
        return false;
    },
    valideCard:function(idCard){
        var regIdCard =/^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
        if (regIdCard.test(idCard)) {
            if (idCard.length == 18) {
                var idCardWi = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2); //将前17位加权因子保存在数组里
                var idCardY = new Array(1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2); //这是除以11后，可能产生的11位余数、验证码，也保存成数组
                var idCardWiSum = 0; //用来保存前17位各自乖以加权因子后的总和
                for (var i = 0; i < 17; i++) {
                    idCardWiSum += idCard.substring(i, i + 1) * idCardWi[i];
                }     
                var idCardMod = idCardWiSum % 11; //计算出校验码所在数组的位置
                var idCardLast = idCard.substring(17); //得到最后一位身份证号码
     
                //如果等于2，则说明校验码是10，身份证号码最后一位应该是X
                if (idCardMod == 2) {
                    if (idCardLast == "X" || idCardLast == "x") {
                        return true;
                    } else {
                        alert("身份证号码错误！");
                        return false;
                    }
                } else {
                    //用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码
                    if (idCardLast == idCardY[idCardMod]) {
                        return true;
                    } else {
                        alert("身份证号码错误！");
                        return false;
                    }
                }
            }
        } else {
            alert("身份证格式不正确!");
            return false;
        }
    }
};
var h = $('#index').width()/5 - 12;
$(document).ready(function(){
	$('.fileBoxUl li').css({'width':h,'height':h});
	$('.js-addfiles').each(function(){
		var _id = this.id,hiddenid = $(this).closest('.parentFileBox').siblings('input').attr('id');
		$('#' + _id).diyUpload({
        	method:"POST",
            server: _b.href + 'wtspz/uploadFile.do',
            formData: {  uid: 'freeser',nid: _b.nid },
            fileNumLimit:1,
            fileSizeLimit: 2*2048*1024,
            fileSingleSizeLimit: 2 * 2048*1024 ,
            duplicate:true,
            thumb:{
                width:h,
                height:h,
                quality: 70,
                allowMagnify: false,
                crop: true,
                type: "image/*"
            },pick: {
                id: '#'+_id,
                innerHTML: "点击选择图片",
                multiple: false
            },
            success: function (id) {
            	setUploadValue(id.url,hiddenid);
            },
            error: function (err) {
                console.info(err);
            }
        });			
	});
   	/*$('#single').click(function(){
   		var id = $(this).attr('data-id') || 'index';
   		$('#'+ id +' input[type="file"]').removeAttr('multiple').click();
   		hidePop();
   	});
   	$('#many').click(function(){
   		var id = $(this).attr('data-id') || 'index';
   		$('#'+ id +' input[type="file"]').attr('multiple','multiple').click();
   		hidePop();
   	});  	           	
	if(browser.versions.android){
		$('#popupMenu').remove();
		$('.webuploader-pick').css('z-index','0');
	}else{
		$('#index').delegate('.js-addfiles','click',showMsg);
	}	
	*/
	
	$('#btnsubmit').click(function(){
		var card = $('#card').val(),iCount = 0;
		$('.inputs,select').each(function(){
			var v = $.trim(this.value);
			if(v.length < 1){
				$(this).closest('dd').addClass('error');
				iCount++ ;
			}else{
				$(this).closest('dd').removeClass('error');
			}
		});
		if(iCount) {
			return $('.error :input:first').focus(),false;
		}
		if(!_b.valideCard(card)){
			return false;
		}
		$('#postorde').submit();
	});
});

function setUploadValue(id,_hidden){
	var $hinput = $('#'+_hidden);
	//if(!$hinput.val()){
	$hinput.val(id);
	//}else{
	//	$hinput.val($hinput.val()+","+id);
	//}
}
function showMsg(e){
	$('#popupMenu a').attr('data-id',this.id);
	$('#popupMenu').addClass('g_fixed').fadeIn();
	$('.layout_div').show();
}
function showCustom(){
	$('#customLabel_div').addClass('g_fixed center').fadeIn();
	$('.layout_div').show();
}			
function hidePop(){
	$('.popup-div').fadeOut();
	$('.layout_div').hide();
}
function SetMoveState1(){
	$('.fileBoxUl li').css({'width':h,'height':h});
}