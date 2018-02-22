var _b = {
	_post:function(url,ops,fun,err){
		return _$(this.href + url,ops,fun,err);
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
$(document).ready(function(){
	$('#btnsubmit').click(function(){
		var card = $('#card').val(),tel = $('#tel').val(),
		name = $('#name').val(),ads = $('#address').val(),
		time=$("#orderhomeform-service_date").val(),
		hospital=$('#hospital').val(),depart=$('#department').val();
		if(!name.length){
			alert('请填写下您的姓名');
			return false;
		}
		if(typeof(card)!="undefined"&&!_b.valideCard(card)){
			return false;
		}
		if(typeof(hospital)!="undefined"&&hospital==""){
			alert("请输入医院");
			return false;
		}
		if(typeof(depart)!="undefined"&&depart==""){
			alert("请输入科室");
			return false;
		}
		if(!$('#notime').is(':checked') && time==""){
			alert("请选择就诊时间");
			return false;
		}
		if(typeof(ads)!= "undefined"&&!ads.length){
			alert('请填写下您的服务地址');
			return false;
		}
		if(!_b.valideTel(tel)){
			return false;
		}
		$('#postorder').submit();
	});
});