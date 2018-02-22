﻿Array.prototype.uniques = function() { var res = [], hash = {};for(var i=0, elem; (elem = this[i]) != null; i++)  {if (!hash[elem]){res.push(elem);hash[elem] = true;}}return res;}
define(['view/base','view/cookies'],{
	base:function(){
		return seajs.require('view/base')	
	},
	getParameter:function(queryString, parameterName) {
        var parameterName = parameterName + "=";
        if(queryString.length > 0) {
            var begin = queryString.indexOf(parameterName);
            if(begin != -1) {
                begin += parameterName.length;
                var end = queryString.indexOf("&", begin);
                if(end == -1) {
                    end = queryString.length;
                }
                return decodeURIComponent(queryString.substring(begin, end));
            }
            return '';
        }
    },
    getQueryStringByName:function(queryString, parameterName) {
    	var reg = new RegExp('(^|&)' + parameterName + '=([^&]*)(&|$)', 'i');
        var r = queryString.match(reg);
        if (r != null) {
            return decodeURIComponent(r[2] ? r[2] : '');
        }
        return '';
    },
    getQueryString:function(queryString, parameterName) {
    	var url = queryString,str,theRequest = new Object();
        if (url.indexOf("?") == -1) {url = '?' + url}
        str = url.substr(1),strs = str.split("&");
        for(var i = 0; i < strs.length; i ++) {
            theRequest[strs[i].split("=")[0]] = decodeURIComponent(strs[i].split("=")[1]);
        }        
        return theRequest;
    },
	getVal:function(queryString, parameterName) {
	    var reg = new RegExp("(^|;)" + parameterName + ":([^;]*)(;|$)", "i");  
	    var r = queryString.match(reg);
	    var context = "";  
	    if (r != null)  context = r[2];  
	    reg = null,r = null;  
	    return context == null || context == "" || context == "undefined" ? "" : context;  
    },
    setParameter:function(queryString, parameterName,value){
    	var v = this.getParameter(queryString, parameterName);
    	return queryString.replace(parameterName + '=' + v,parameterName + '=' + value);
    },
	cook:function(key,value,exp){
		var session = this.cookies().cookies;
		if(value){
			session.set(key,value,exp * 24,'/');
		}else{
			return session.get(key);
		}
	},
	uncook:function(name, path){
		var session = this.cookies().cookies;
		if(name instanceof Array){
			for(var i=0,l=name.length;i<l;i++){ 
				session.remove(name[i],path || '/');
			}
		}else if(typeof name == 'string'){
			session.remove(name,path || '/');
		}
		
	},
	cookies:function(){
		return seajs.require('view/cookies');
	},
	sortDesc:function(x,y){return y[0].localeCompare(x[0]);},
	sortAsc:function(x,y){return x[0].localeCompare(y[0]);},
	discodes:{"110000":"北京市","120000":"天津市","130000":"河北省","140000":"山西省","150000":"内蒙古自治区","210000":"辽宁省","220000":"吉林省","230000":"黑龙江省","310000":"上海市","320000":"江苏省","330000":"浙江省","340000":"安徽省","350000":"福建省","360000":"江西省","370000":"山东省","410000":"河南省","420000":"湖北省","430000":"湖南省","440000":"广东省","450000":"广西壮族自治区","460000":"海南省","500000":"重庆市","510000":"四川省","520000":"贵州省","530000":"云南省","540000":"西藏自治区","610000":"陕西省","620000":"甘肃省","630000":"青海省","640000":"宁夏回族自治区","650000":"新疆维吾尔自治区"},
	_data:{
		citys:[],
		hos:{},
		departs:{},
		teamHos:{},
		unionHos: {},
		unionCity: [],
		onlineDuty:[{id:'主任医师',displayName:'主任医师'},{id:'副主任医师',displayName:'副主任医师'}],
		customDist:[{id:'11',displayName:'北京'},{id:'31',displayName:'上海'},{id:'440100',displayName:'广州'},{id:'420100',displayName:'武汉'}]
	},
	objadd:function(obj,k,v){
		var newv = [v];
		k in obj ? (
				obj[k] = obj[k].concat(newv)
			) : (obj[k] = newv);
		return obj;
	},
	objtoarr:function(obj){
		var arr = [];
		$.each(obj,function(key,val){
			arr.push({key:key,val:val});
		});
		return arr;
	},
	actionTo:function(hashstr){
		var _this = this,hashstrArr = hashstr.split('/'),page = hashstrArr[0], pid = hashstrArr[1] || '';
		$('.menuicon .selected').removeClass('selected');
		if(hashstr.indexOf('_docdetail') != -1){
			this.getSpecilsDetail(pid,function(d){
				d['hosname'] = _this.cook('_remoteDiv_openhos').split(':')[0];
				d['depname'] = _this.cook('_remoteDiv_opendeps').split(':')[0];
				_this.fillBODY(page, d);
			});
		}else if(hashstr.indexOf('online_question') != -1){
			this.getSpecilsDetail(pid,function(d){
				_this.fillBODY(page, d);
			});
		}else if(hashstr.indexOf('confirmpay') != -1){
			return ;
		}else if(hashstr.indexOf('wxpay') != -1){
			_this.fillBODY(page, {img: $('#myform [name="wxurl"]').val(),cost:_$ob['cost']});
			return ;
		}else if(hashstr.indexOf('wxpaysuccess') != -1){
			_this.fillBODY(page, _$ob);
			return ;
		}else if(hashstr.indexOf('1=1') != -1){
			return ;
		}else{
			this.fillBODY(page, {}, pid)
		}
	},
	fillBODY:function(hashstr,data,pid){
		nodetpl.get(hashstr + '.htm', {
			base: _h,
			phref:hashstr,
			pid:pid,
			data: data
		}, function(d) {
			$('#mainDIV').html(d);
			window.scrollTo(0,0);
		}), 
		$('.menuicon [data-action="'+ hashstr.split('_')[0] +'"]').addClass('selected').siblings().removeClass('selected');
	},
	addFavorite:function(){
		var url = 'https://develop.ebaiyihui.com/';
		var title = document.title;
		var ua = navigator.userAgent.toLowerCase();
		if (ua.indexOf("360se") > -1) {
		    alert("由于360浏览器功能限制，请按 Ctrl+D 手动收藏！");
		} else if (ua.indexOf("msie 8") > -1) {
		    window.external.AddToFavoritesBar(url, title); //IE8
		} else if (document.all) {
		    try {
		        window.external.addFavorite(url, title);
		    } catch (e) {
		        alert('您的浏览器不支持,请按 Ctrl+D 手动收藏!');
		    }
		} else if (window.sidebar) {
		    window.sidebar.addPanel(title, url, "");
		} else {
		    alert('您的浏览器不支持,请按 Ctrl+D 手动收藏!');
		}
	},
	initQueryWhere:function(){
		var base = this.base(),_this = this,discodes = this.discodes;
		var queryString = document.location.hash.split('?').pop();
		var parm = this.getParameter(queryString, "remote");
		base.showLineLoading().post('kangxin/gaincoohospitals',{},function(d){
			var hospitals = d.hospitals,citys = {},hos = {};
			$.each(hospitals,function(i,o){
				var distCode = o.distCode || '',prol = distCode.substr(0,2),pro = prol + '0000',
					cityName = discodes[pro] + ' ' +o.city,
					hosName = o.displayName;
				if(!distCode) return true;
				citys[pro] = discodes[pro];
				hos = _this.objadd(hos,pro,{key : o.id,val : hosName});
			});
			_this._data.citys = _this.objtoarr(citys);
			_this._data.hos = hos;
			_this.fillRemoteCity(_this._data.citys,parm || '');
		});
	},
	fillRemoteCity:function(citys,defv){
		var _this = this;
		$('.remoteDiv .opencitys').html(function(){
			var as = '',fid,bol;
			$.each(citys,function(i,o){
				!i ? (fid = o.key,bol = true) : (bol = false);
				!!defv && (bol = false);
				(defv || '').indexOf('|' + o.key + '|') != -1 && (bol = true,fid = o.key);
				as += '<a href="javascript:void(0)"'+ (bol ? ' class="selected"' : '') +' data-id="'+ o.key +'">'+ o.val +'</a>';
			});
			fid && _this.fillRemoteHos(fid,defv);
			return as;
		}).next('.query_more').css('display',citys.length > 8 ? 'block' : 'none');
	},
	fillRemoteHos:function(discode,defv){
		var _this = this,hos = _this._data.hos[discode];
		$('.remoteDiv .openhos').html(function(){
			var as = '',fid,bol;
			$.each(hos,function(i,o){
				!i ? (fid = o.key,bol = true) : (bol = false);
				!!defv && (bol = false);
				(defv || '').indexOf('|' + o.key + '|') != -1 && (bol = true,fid = o.key);
				as += '<a href="javascript:void(0)"'+ (bol ? ' class="selected"' : '') +' data-id="'+ o.key +'">'+ o.val +'</a>';
			});
			fid && _this.getDepbyHos(fid,defv);
			return as;
		}).next('.query_more').css('display',hos.length > 8 ? 'block' : 'none');
	},
	getDepbyHos:function(hosid,defv){
		var base = this.base(),_this = this,discodes = this.discodes;
		if(hosid in _this._data.departs) return this.fillRemoteDepart(hosid,_this._data.departs[hosid],defv), false;
		base.showLineLoading().post('kangxin/gainhosdeparts',{hosid:hosid},function(d){
			var departs = d.departs;
			_this._data.departs[hosid] = departs;
			_this.fillRemoteDepart(hosid,departs,defv);		
		});
	},
	fillRemoteDepart:function(hosid,departs,defv){
		var _this = this;
		$('.remoteDiv .opendeps').html(function(){
			var as = '',fid,bol;
			$.each(departs,function(i,o){
				!i ? (fid = o.id,bol = true) : (bol = false);
				!!defv && (bol = false);
				(defv || '').indexOf('|' + o.id + '-') != -1 && (bol = true,fid = o.id);
				as += '<a href="javascript:void(0)" data-hosid="'+ hosid +'" data-id="'+ o.id +'"'+ (bol ? ' class="selected"' : '') +'>'+ o.displayName +'</a>';
			});
			_this.getSpecialsPage(hosid,fid);
			return as;
		}).next('.query_more').css('display',departs.length > 8 ? 'block' : 'none');	
		//!i && (fid = o.id);//<a href="javascript:void(0)" class="selected" data-hosid="'+ hosid +'" data-id="">全部</a>
	},
	getSpecialsPage:function(hosid,depid,pageno){
		var base = this.base(),_this = this;
		base.get('kangxin/gainspecialspage',{hosid:hosid,depid:depid || '',pageNumber: pageno || 1,pageSize:_zjh.config.pagesize},function(d){
			var pager = d.pager;
			pager['hosid'] = hosid;
			pager['depid'] = depid;
			_this.fillRemoteSpecList(pager);
		});
	},
	getTeamsPage:function(pageno,pagesize){
		var base = this.base(),_this = this;
		base.get(_zjh.config.doclist,{pageNumber: pageno || 1,pageSize:pagesize || _zjh.config.pagesize},function(d){
			var pager = d.pager;
			_this.fillRemoteSpecList(pager);
		});
	},
	fillRemoteSpecList:function(pager){
		var base = this.base(),_this = this;
		nodetpl.get('remote_speclist.htm', {
			base: _h,
			pager:pager
		}, function(d) {
			$('.remoteDiv .hasdoclists').html(d);
			_this.imgDo('.remoteDiv .hasdoclists .doclist');
			base.hideLineLoading();
		});
	},
	getDocVideoPage:function(pageno,pagesize){
		var base = this.base(),_this = this;
		base.post('kangxin/gaindoctrforumspage',{pageNumber: pageno || 1,pageSize:pagesize || _zjh.config.pagesize,sorttype:'desc'},function(d){
			var pager = d.pager;
			_this.fillDocVideoList(pager);
		});
	},
	fillDocVideoList:function(pager){
		var base = this.base(),_this = this;
		nodetpl.get('docs_video.htm', {
			base: _h,
			pager:pager
		}, function(d) {
			$('.remoteDiv .hasvideolists').html(d);
			base.hideLineLoading();
		});
	},
	initOnlineQueryWhere:function(){
		var base = this.base(),_this = this;
		var queryString = document.location.hash.split('?').pop();
		var hos = this.getParameter(queryString, "hos") || '';
		var dep = this.getParameter(queryString, "dep") || '';
		var duty = this.getParameter(queryString, "duty") || '';
		var no = this.getParameter(queryString, "no") || 1;	
		
		base.showLineLoading();		
		!this._data.onlineHos ? base.get(_zjh.config.hoslist,'',function(d){
			_this.fillOptions(_this._data.onlineHos = d.hospitals,hos,'hos');				
		}) : this.fillOptions(this._data.onlineHos,hos,'hos');
		
		!this._data.onlineDep ? base.get(_zjh.config.deplist,'',function(d){
			_this.fillOptions(_this._data.onlineDep = d.sdeps,dep,'dep');				
		}) : this.fillOptions(this._data.onlineDep,dep,'dep');
		
		this.fillOptions(this._data.onlineDuty,duty,'duty');
		this.fillOnlineDocList(hos,dep,duty,no);
	},
	fillOnlineDocList:function(hos,dep,duty,no){
		var base = this.base(),_this = this;
		base.get(_zjh.config.doclist,{"hosid": hos ,"sdepid": dep ,"duty": duty ,"pageSize": _zjh.config.pagesize,"pageNumber": no,"stype":"online"},function(d){
			_this.fillQueryList(d.pager,'','online_speclist');		
		});
	},
	fillOptions:function(arrays,sid,type){
		$('.remoteDiv .open'+ type +'s').html(this.getAlist(arrays,sid,type)).next('.query_more').css('display',arrays.length > 8 ? 'block' : 'none');
		this.fillQueryWhereSelected({key:type,val:sid});
	},
	getAlist:function(arrays,sid,type){
		var as = '';
		$.each(arrays,function(i,o){
			as += '<a href="javascript:void(0)" data-type="'+ type +'" data-id="'+ o.id +'"'+ (o.id == sid ? ' class="selected"' : '') +' title="'+ o.displayName +'">'+ o.displayName +'</a>';
		})
		return as || '<a href="javascript:void(0)"></a>';
	},
	fillQueryWhereSelected:function(obj){
		var txt = '';
		if(obj.val){
			txt = $('.remoteDiv .open'+ obj.key +'s [data-id="'+ obj.val +'"]').text() || (obj.key == 'keyword'? obj.val : '');
			txt && $('.remoteDiv .queryselects [data-'+ obj.key +']')
				.replaceWith('<a class="selecteds" data-'+ obj.key +'="'+ obj.val +'">'+ txt + '<i class="iconfont">&#xe60e;</i></a>');
		}
	},
	initTeamQueryWhere:function(){
		var base = this.base(),_this = this;
		var queryString = document.location.hash.split('?').pop();
		var hos = this.getParameter(queryString, "hos") || '';
		var dep = this.getParameter(queryString, "dep") || '';
		var city = this.getParameter(queryString, "city") || '';
		var no = this.getParameter(queryString, "no") || 1;	
		var keyword = this.getParameter(queryString, "keyword") || '';
		var distcode = city || 'all';
		base.showLineLoading();	
		
		keyword && this.fillQueryWhereSelected({key:'keyword',val:keyword});
		this.fillOptions(this._data.customDist,city,'city');
		
		!(this._data.teamHos && (distcode in this._data.teamHos)) ? base.get(_zjh.config.hoslist,{distcode:city},function(d){
			_this.fillOptions(_this._data.teamHos[distcode] = d.hospitals,hos,'hos');				
		}) : this.fillOptions(this._data.teamHos[distcode],hos,'hos');
		
		!this._data.onlineDep ? base.get(_zjh.config.deplist,{},function(d){
			_this.fillOptions(_this._data.onlineDep = d.sdeps,dep,'dep');				
		}) : this.fillOptions(this._data.onlineDep,dep,'dep');
		
		if($('.remoteDiv .openhoss [data-id="'+ hos +'"]').size() < 1){
			hos = '';
		}
		this.fillTeamDocList(city,hos,dep,no,keyword);
	},
	fillTeamDocList:function(distcode,hos,dep,no,keyword){
		var base = this.base(),_this = this;
		base.get(_zjh.config.doclist,{"keyword":keyword,"distcode":distcode,"hosid": hos ,"sdepid": dep ,"pageSize": _zjh.config.pagesize,"pageNumber": no},function(d){
			_this.fillQueryList(d.pager,'team_docdetail');		
		});
	},
	fillQueryList:function(pager,action,list){
		var base = this.base(),_this = this;
		nodetpl.get((list || 'querylist') + '.htm', {
			base: _h,
			action: action,
			pager: pager
		}, function(d) {
			$('.remoteDiv .hasdoclists').html(d);
			_this.imgDo('.remoteDiv .hasdoclists .doclist');
			base.hideLineLoading();
		});
	},
	getQueryResult:function(key,val){
		var hash = document.location.hash.replace("#","");			
		if(key instanceof Array){
			for(var i=0,l=key.length;i<l;i++){ 
				hash = this.replaceHashKey(hash,key[i].key,key[i].val);
			}
		}else if(typeof key == 'string'){
			hash = this.replaceHashKey(hash,key,val);
		}
		document.location.hash = hash;
	},
	initUnionQueryWhere: function () {
		var base = this.base(),_this = this;
		var queryString = document.location.hash.split('?').pop();
		var hos = this.getParameter(queryString, "hos") || '';
		var city = this.getParameter(queryString, "city") || '';
		var no = this.getParameter(queryString, "no") || 1;	
		var distcode = city || 'all';
		base.showLineLoading();	
		!this._data.unionCity.length ? base.get(_zjh.config.gainAllianceArea,{},function(d){
			d.areas.forEach(function(item){
				_this._data.unionCity.push({
					id: item.distCode,
					displayName: item.distName
				});
			})
			_this.fillOptions(_this._data.unionCity, city, 'city');				
		}) : this.fillOptions(this._data.unionCity, city, 'city');
		
		!(distcode in this._data.unionHos) ? base.get(_zjh.config.gainAllianceByArea,{distCode: city},function(d){
			_this._data.unionHos[distcode] = [];
			d.alliances.forEach(function(item){
				_this._data.unionHos[distcode].push({
					id: item.id,
					displayName: item.yltName
				});
			})
			_this.fillOptions(_this._data.unionHos[distcode], hos, 'hos');				
		}) : this.fillOptions(this._data.unionHos[distcode], hos, 'hos');
		
		hos &&　base.get(_zjh.config.gainAllianceDetailInfo,{allianceId: hos},function(d){
			var dtl = d.alliance || {};
			var imgsrc = dtl.iconUrl || '//develop.ebaiyihui.com/img/icons/80.png';
			$('.unionDiv .uniondetail').show().html(function(){
				return '<div class="clearfix">\
			    			<div class="fleft thumb radius50 ohidden">\
							<img src="'+ imgsrc +'?x-oss-process=image/resize,m_fill,h_100,w_100" alt="">\
						</div>\
						<div class="detailinfo">\
							<h2>'+ dtl.yltName +'</h2>\
							<div class="signs">\
								<span>核心医院</span>\
								<span>'+ dtl.hosName +'</span>\
								<span>'+ dtl.hosLevel +'</span>\
							</div>\
							<p class="clearfix">\
								<label class="fleft">擅长：</label>\
								<span>'+ dtl.speciality +'</span>\
							</p>\
							<p class="clearfix">\
								<label class="fleft">简介：</label>\
								<span>'+ dtl.profile +'</span>\
							</p>\
						</div>\
					</div>'
			})
		});

		this.fillUnionDocList(hos, no);
	},
	fillUnionDocList:function(hos, no){
		var base = this.base(),_this = this;
		base.get(_zjh.config.loadAllianceDoctors, { 
			"allianceId": hos, 
			"pageSize": _zjh.config.pagesize, 
			"pageNumber": no 
		},function(d){
			_this.fillQueryList(d.pager, 'team_docdetail', 'unionquerylist');		
		});
	},
	replaceHashKey:function(hash,key,val){
		var _hash = decodeURIComponent(hash);
		var queryString = _hash.split('?').pop();
		var oval = this.getParameter(queryString,key);
		
		if(_hash.indexOf('?') == -1){
			_hash += '/?query=frazior';
		}
		if(_hash.indexOf(key + '=' + oval) != -1){
			_hash = _hash.replace(key + '=' + oval,key + '=' + val);
		}else{
			_hash += ('&' + key + '=' + val);
		}
		return _hash;
	},
	imgDo:function(selector){
		$(selector).each(function(){					
			var img = $('.thumb img',this);
			img[0].onload = function() {							
				var w = img.width(),h = img.height();
				if(w > h){
					img.css({width:'auto',height:'100%','max-width':'inherit'});
				}else{
					img.css({width:'100%',height:'auto'});
				}
	        }
			if(img[0].complete){
				img[0].onload();
			}
		});
	},
	getSpecilsDetail:function(docid,callback){
		var base = this.base(),_this = this;
		docid && base.showLineLoading().post('kangxin/gainspedetail',{docid:docid},function(d){
			callback(d.special || {});
			base.hideLineLoading();
		});
	},
	initSwiper:function(selector){
		seajs.use('view/swiper',function(swiper){
			swiper.init(selector,{
    			slidesPerView: 'auto',
		        spaceBetween: 0,
		        freeMode: true,
		        nextButton: '.swiper-button-next',
		        prevButton: '.swiper-button-prev'
    		});
    	});		
	},
	formatDate:function(d){
		if(!d) return +new Date();
		d = d.split('/');
		return d[0] + '-' + this.formatHH(d[1]) + '-' + this.formatHH(d[2]);
	},
	formatHH:function(h){
		h = (h || '00').split('');
		h.length < 2 && h.unshift('0');
		return h.join('');
	},
	getWeek:function(week){
		var day;
		switch (week){
		    case 0:day="周日";
		      break;
		    case 1:day="周一";
		      break;
		    case 2:day="周二";
		      break;
		    case 3:day="周三";
		      break;
		    case 4:day="周四";
		      break;
		    case 5:day="周五";
		      break;
		    case 6:day="周六";
		      break;
		}
		return day;
	},
	initDocTimeLists:function(selector){
		var base = this.base(),_this = this,totalday = 14,$selector = $(selector),sid = $selector.attr('data-sid');
		base.showLineLoading();
		$selector.html(function(){
			var today = new Date(),day = today.getDate(),week,cdate,ymd,md,dls = '';
			for(var i = 0;i < totalday;i++){
				cdate = new Date(),cdate.setDate(day + i);
				md = (cdate.getMonth() + 1) +'/' + cdate.getDate();
				ymd = _this.formatDate(cdate.getFullYear() + '/'+ md);
				week = _this.getWeek(cdate.getDay());
				dls += '<dl data-ymd="'+ ymd +'" data-week="'+ week +'" class="swiper-slide timelist"><dt>'+ md +'（'+ week +'）</dt></dl>';
			}
			return dls;
		}).find('dl[data-ymd]').each(function(){
			var ymd = $(this).attr('data-ymd');
			_this.initDocTimeList(sid,ymd,function(dd){
				totalday--;
				dd ? $selector.find('dl[data-ymd="'+ ymd +'"]').append(dd).show() : $selector.find('dl[data-ymd="'+ ymd +'"]').remove();
				!totalday && (base.hideLineLoading(),$selector.find('dl').size() < 1 && $selector.html('<div class="noresult">专家近期没有出诊安排。</div>'));
			});
		});
	},
	initDocTimeList:function(sid,sdate,callback){
		var base = this.base(),_this = this;
		base.post('wzjh/gainSpecialTimes',{sid:sid,sdate:sdate},function(d){
			var times = d.times || [];			
			callback((function(){
				var dds = '';
				$.each(times,function(i,o){
					dds += '<dd data-id="'+ o.id +'" data-t="'+ o.startTime +'" data-a="'+ (o.startTime < '12:00' ? '上午' : '下午') +'" data-c="'+o.cost+'" class="'+ (o.hasAppoint == '1' ? 'disabled' : 'open') +'">'+ o.startTime +'</dd>';
				});
				return dds;
			})());
		},function(){
			callback();
		});
	},
	getuser:function(s){
		var myacc = this.cook('_key_myaccount_') || '';
		var token = this.cook('_key_myaccount_lq_') || '';
		if(!myacc || !token){
			this.cook('_key_history_',location.href,1);
			location.hash = 'login';
			return false;
		}
		return s =='token' ? token : myacc;
	},
	checkToken:function(selector,s){
		var myacc = this.cook('_key_myaccount_') || '';
		var token = this.cook('_key_myaccount_lq_') || '';
		if(!myacc || !token){
			$(selector).append(function(){
				return '<div class="disableform"><div class="bg"></div><div class="txt">请先<a href="#login">登录</a></div></div>';
			});
			this.cook('_key_history_',location.href,1);
			return false;
		}
		return s =='token' ? token : myacc;
	},
	innerTips:function(selector,inner){
		$(selector).append(function(){
			return '<div class="disableform"><div class="bg"></div><div class="txt">'+ inner +'</div></div>';
		});
	},
	resreshUserCenter:function(state){
		var myacc = this.cook('_key_myaccount_') || '';
		var token = this.cook('_key_myaccount_lq_') || '';
		if(state == 'logout'){
			myacc = '';
			this.uncook(['_key_myaccount_','_key_myaccount_lq_']);
			//this.exitLogon(token);
			location.hash = 'login';
		}
		if(myacc && token){
			$('.toptip .usercenter').html('<a href="#user/user_myaccout">'+ myacc.split('_')[0] +'</a><a href="javascript:void(0)" data-action="logout">退出</a>');
		}else{			
			this.uncook(['_key_myaccount_','_key_myaccount_lq_']);
			$('.toptip .usercenter').html('<a href="javascript:void(0)" data-action="login">登录</a><a href="javascript:void(0)" data-action="regist">注册</a>');
		}
		return this;
	},
	exitLogon:function(token){
		var base = this.base(),_this = this;
		token && base.postStr(_zjh.config.logout_lq,'{"header":{"user":{"token":"'+ token +'"}}}',function(){});
	},
	goback:function(acname){
		var historys = this.cook('_key_history_') || '';
		historys ? this.uncook('_key_history_') : (historys = acname || '');
		if(historys.indexOf('://') != -1){
			window.location.href = historys;
		}else{
			location.hash = historys || 'home';
		}
	},
	valdateLogin:function(){
		var base = this.base(),_this = this;
		var tel = $('.loginregistDiv [name="tel"]').val(),
			psd = $('.loginregistDiv [name="password"]').val(),
			remberme = document.getElementsByName('remberme')[0].checked;
		tel && psd && base.post('kangxin/userlogin',{tel:tel,password:psd},function(d){
			if(d.status == 'error'){
				base.showTipE(d.message || '登录失败');
			}else if(d.status == 'nopass'){
				base.showTipE(d.message);
				location.hash = 'lostpsd';
			}else if(d.status == 'success'){
				_this.cook('_key_myaccount_', tel + '_' + d.user.id,remberme ? 7 : 1);
				//_this.valdateLoginLQ(tel,psd,remberme);
				_this.cook('_key_myaccount_lq_', 'temptoken', remberme ? 7 : 1);
				_this.resreshUserCenter().goback();
			}
		},function(){
			base.showTipE('登录失败');
		})
	},
	valdateLoginLQ:function(tel,psd,remberme){
		var base = this.base(),_this = this;
		tel && psd && base.postStr(_zjh.config.login_lq,'{"body":{"mobileNumber":"'+ tel +'","password":"'+ psd +'"}}',function(d){
			if(d.resultCode == '200'){
				_this.cook('_key_myaccount_lq_', d.message, remberme ? 7 : 1);
				_this.resreshUserCenter().goback();	
			}else{	
				base.showTipE(d.message || '登录失败');		
			}
		},function(){
			base.showTipE('登录失败');
		})
	},
	valdateResigst:function(){
		this.valdateRegLost('1','注册')
	},
	valdateLostpsd:function(){
		this.valdateRegLost('2','找回密码')
	},
	valdateTelRepeat:function($this){
		var base = this.base(),_this = this,tel = $this.val(),type = $this.attr('data-type'),
			size = type == 'reg' ? $('.register').size() : $('.loster').size();
		if(!size) return false;
		if(!base.valideTel(tel)) return base.showTipE('请输入正确的手机号'),$this.focus(),false;
		base.post('kangxin/telluser',{tel:tel},function(d){
			if(d.status == 'error'){
				type == 'reg' && (base.showTipE('此手机号已注册过，请登录'),$this.focus());
			}else{
				type == 'lost' && (base.showTipE('此手机号未注册过，请注册'),$this.focus());
			}
		});
	},
	valdateRegLost:function(type,msg){
		var base = this.base(),_this = this;
		var tel = $('.loginregistDiv [name="tel"]').val(),
			code = $('.loginregistDiv [name="code"]').val(),
			psd = $('.loginregistDiv [name="password"]').val();
		if(!base.valideTel(tel)) return base.showTipE('请输入正确的手机号'),false;
		if(!code) return base.showTipE('请输入正确的手机验证码'),false;		
		if(psd.length < 6 || psd.length > 20) return base.showTipE('请输入至少6位，最多20位的密码'),false;
		if(type == '1' && !document.getElementsByName('readme')[0].checked) return base.showTipE('请阅读协议'),false;
		
		base.showLineLoading().post('kangxin/userregister',{tel:tel,code:code,password:psd,ctype:type},function(d){
			if(d.status == 'error'){
				base.showTipE(d.message || (msg + '失败'));
			}else if(d.status == 'success'){
				type == '1' ? (
					_this.cook('_key_myaccount_', tel + '_' + d.uid,1),
					_this.valdateLoginLQ(tel,psd,false)
				) : (location.hash = 'login');
			}
			base.hideLineLoading();
		},function(){
			base.showTipE(msg + '失败').hideLineLoading();
		});
	},
	valdateRegLostLQ:function(tel,code,psd,type,msg){
		var base = this.base(),_this = this;
		base.postStr(_zjh.config.regist_lq,'{"body":{"mobileNumber":"'+ tel +'","password":"'+ psd +'","verifyCode":"'+ code +'"}}',function(d){
			if(d.resultCode == '200'){
				type == '1' ? (
					_this.cook('_key_myaccount_lq_', d.message, 1),
					_this.resreshUserCenter().goback()
				): (location.hash = 'login');
			}else{	
				base.showTipE(d.message || (msg + '失败'));		
			}
		},function(){
			base.showTipE(msg + '失败');
		})
	},
	getRegCode:function(){
		var base = this.base(),_this = this;
		var tel = $('.loginregistDiv [name="tel"]').val();
		var btn = $('.js-getcode'),data = btn.data();
		if(btn.hasClass('disabled')) return false;
		if(!base.valideTel(tel)) return base.showTipE('请输入正确的手机号'),false;
		base.post('kangxin/gainVeryCode',{tel:tel,ctype:data.type || 1},function(d){
			if(d.status == 'error'){
				base.showTipE('发送失败，请重试');
				_this.clearTimer(btn);
			}else{
				base.showTipS('发送成功，5分钟内有效');
			}
		},function(){
			base.showTipE('发送失败，请重试');
			_this.clearTimer(btn);
		});
		this.setTimer(btn);
	},
	setTimer:function(btn){
		var _this = this;
    	var i = 90;
    	btn.addClass('disabled');		    	
		window._timers = setInterval(function(){
			if(i<1){ 
				_this.clearTimer(btn);
				return false;
			}
			btn.text( i );
			i--;
		},1000);    	
    },
    clearTimer:function(btn){
    	if(window._timers) clearInterval(window._timers);
		btn.removeClass('disabled').text('获取验证码');
    },
    validatePerson:function(fn){
    	var base = this.base(),_this = this;
		var username = $('#adduserinfo [name="username"]').val(),
			tel = $('#adduserinfo [name="tel"]').val(),
			idcard = $('#adduserinfo [name="idcard"]').val();
		var uid = this.getuser();
		var btn = $('.confirmHtml #adduserinfo .submituserinfo');
		if(!uid) return false;
		if(!username) return base.showTipE('请输姓名'),false;
		if(!base.valideTel(tel)) return base.showTipE('请输入正确的手机号'),false;
		if(idcard && !base.valideCard(idcard)) return base.showTipE('请输入正确的身份证号'),false;		
		btn.addClass('disabled');
		base.post('kangxin/savenewcontactor',{tel:tel,username:username,idcard:idcard,uid:uid.split('_')[1]},function(d){
			if(d.status == 'error'){
				base.showTipE(d.message || '添加联系人失败');
			}else if(d.status == 'success'){
				fn();
				$('#adduserinfo')[0].reset();
				$('.confirmHtml .addpersoninfo').before(function(){
					var size = $('.confirmHtml .addpersoninfo').siblings().size();
					return '<div class="personlist fleft">\
			    				<div class="innerlist'+ (!size ? ' selected' : '') + '" data-id="'+ d.contactorid +'">\
				    				<h4>'+ username + (!size ? '<span>(默认)</span>' : '') + '</h4>\
				    				<div class="perinfo">\
				    					<p>联系方式：'+ tel +'</p>\
				    					<p>身份证号：'+ idcard +'</p>\
				    				</div>\
								</div>\
							</div>';
				});
			}
			btn.removeClass('disabled');
		},function(){
			base.showTipE('添加联系人失败');
			btn.removeClass('disabled');
		})
    },
    validateSumbitWX:function(){
    	var base = this.base(),_this = this;
		var contactorid = $('#myform [name="contactorid"]').val(),
			read = document.getElementsByName('readme')[0].checked;
		var btn = $('.confirmHtml .submittopay');
		if(!contactorid) return base.showTipE('请选择就诊人'),false;
		if(!read) return base.showTipE('请阅读协议'),false;
		btn.addClass('disabled');
		base.showLineLoading().post('wzjh/surepay_pc',$('#myform').serializeArray(),function(d){
			$('#myform [name="wxurl"]').val(d.code_url);
			$('#myform [name="wxout_trade_no"]').val(d.out_trade_no);
			btn.removeClass('disabled');
			base.hideLineLoading();
			if(!d.code_url){ return base.showTipE('保存失败'),false;}
			(function(d){
				_$ob['personName'] = d.name,
				_$ob['personTel'] = d.tel
			})($('.personlist [data-id="'+ contactorid +'"]').data())
			location.hash = 'wxpay';
		},function(){
			base.showTipE('提交失败');
			btn.removeClass('disabled');
		})
    },
    listenWxPay:function(){
    	var base = this.base(),_this = this;
    	var tradeno = $('#myform [name="wxout_trade_no"]').val();
    	tradeno && base.post('kangxin/listenpaystatus',{tradeno:tradeno},function(d){
    		if(d.status == 'success'){
    			_this.fillBODY("wxpaysuccess",d);
    			//location.hash = 'wxpaysuccess';
    		}else{
    			window.setTimeout(function(){
    				_this.listenWxPay();
    	    	},3000);    			
    		}
    	});
    },
    getHosOps:function(fn){
    	var base = this.base(),_this = this;
    	base.post('kangxin/gaincoohospitals',{},function(d){
    		var hoss = d.hospitals;
    		fn(_this.getOptions(hoss));
    	},function(){
    		fn('<option vlaue="">---请选择---</option>');
    	});
    },
    getDepByhos:function(hosid,fn){
    	var base = this.base(),_this = this;
    	hosid && base.post('kangxin/gainhosdeparts',{hosid:hosid},function(d){
    		var hoss = d.departs;
    		fn(_this.getOptions(hoss));
    	},function(){
    		fn('<option vlaue="">---请选择---</option>');
    	});
    },
    getOptions:function(d){
    	var ops = '<option vlaue="">---请选择---</option>';
    	$.each(d,function(i,o){
    		ops += '<option value="'+ o.id +'"'+ (o.distCode ? 'data-dist="'+ o.distCode +'"' : '') +'>'+ o.displayName +'</option>';
    	});
    	return ops;
    },
    homeOnlineDeparts:function(){
    	var base = this.base(),_this = this;
    	!this._data.onlineDep ? base.get(_zjh.config.deplist,'',function(d){
			_this.homefillDeps(_this._data.onlineDep = d.sdeps);			
		}) : this.homefillDeps(this._data.onlineDep);
    },
    homefillDeps:function(arrays){
    	var _this = this;
    	$('.hasdoclists .deplist ul').html(function(){
    		var as = '',fid;
    		$.each(arrays,function(i,o){
    			!i && (fid = o.id);
    			as += '<li'+ (!i ? ' class="selected"' : '') +' data-id="'+ o.id +'"><span>'+ o.displayName +'</span></li>';
    		});
    		fid && _this.homeOnlineDoclist(fid);
    		return as || '<li><span></span></li>';
    	});
    },
    homeOnlineDoclist:function(dep){
    	var base = this.base(),_this = this;
    	base.showLineLoading().get(_zjh.config.doclist,{"sdepid": dep ,"pageSize": 8,"pageNumber": 1,"stype":"online"},function(d){
			nodetpl.get('online_speclist.htm', {
				base: _h,
				action:'',
				target:'_blank',
				nopager:true,
				pager:d.pager
			}, function(d) {
				$('.hasdoclists .doclists').html(d);
				_this.imgDo('.hasdoclists .doclists .doclist');
				base.hideLineLoading()
			});
		});
    },
    getExportlist:function(fn,size,aHtml){
    	var base = this.base(),_this = this;    	
    	base.showLineLoading().get(_zjh.config.doclist,{pageNo:1,pageSize:size || 4},function(d){
    		fn((function(pager){
    			var list = '';
    			$.each(pager.list,function(i,o){
    				list += '<div class="fleft hoslist haslink clearfix">\
    							<a class="href" target="_blank" href="#team_docdetail/'+ o.specialId +'">'+ (aHtml || '') +'</a>\
								<div class="fleft thumb">\
									<img src="'+ (o.listSpecialPicture.indexOf('://')!= -1 ? o.listSpecialPicture.replace('http://','https://') : (o.listSpecialPicture ? 'http://wx.15120.cn/SysApi2/Files/' + o.listSpecialPicture : 'img/defdoc.jpg')) +'" />\
								</div>\
								<div class="fleft detail">\
									<h3 class="h whitespace">'+ o.specialName +'</h3>\
									<p class="whitespace">'+ o.duty +'/'+ o.profession +'</p>\
									<p class="whitespace">'+ o.hosName +'/'+ o.depName +'</p>\
								</div>\
							</div>';
    			});    			
    			return list;
    		})(d.pager));
    		base.hideLineLoading();
    	},function(){
    		fn('');
    	});
    },
    getVideos:function(fn){
    	var base = this.base(),_this = this;
    	base.showLineLoading().post('kangxin/gaindoctrforums',{pageNo:1,pageSize:3,sorttype:'desc'},function(d){
    		fn((function(pager){
    			var list = '';
    			$.each(pager,function(i,o){
    				list += '<a class="videolist" target="_blank" href="'+ o.accessUrl +'">\
								<div class="thumb"><img src="'+ o.backImag +'"/></div>\
								<h3 class="whitespace">'+ o.title +'</h3>\
								<p class="whitespace">'+ o.duty + o.docName +'</p>\
							</a>';
    			});
    			return list;
    		})(d.docforums));
    		base.hideLineLoading()
    	},function(){
    		fn('');
    	});
    },
	reloadDiy:function(ul){
		var vls = [];
		ul.find('li').each(function(){
			var id = this.getAttribute('data-id') || '';    			
			id && vls.push(id);
		});		
		return vls.join(',');
	},
	saveFeedback:function(text,callback){
    	var base = this.base(),_this = this;
    	var myacc = this.cook('_key_myaccount_') || '_',myarr = myacc.split('_');
    	if(!text) return base.showTipE('请输入对我们的建议与反馈'),false;
    	base.post('kangxin/savefeedback',{content:text,userName:myarr[0] || '',userId:myarr[1]||''},function(){
    		base.showTipS('感谢您的建议与反馈');
    		callback();
    	},function(){
    		base.showTipE('提交失败');
    	});
	},
	doNoclosed:function(sign,fun){
    	var base = this.base(),_this = this;
		var myacc = this.cook('_key_myaccount_') || '';
		//if(sign == 'tel') return fun(),false;
		return fun(),false;
		base.post(_zjh.config.existnoclosed,{uid:myacc.split('_')[1]},function(d){
			if(d.existornot == true){
				_this.innerTips('.postaskquestion','还有未结束订单不能下单');
			}else{
				fun();
			}
		});
	},
	loadPersonlist:function(){
    	var base = this.base(),_this = this;
		var myacc = this.cook('_key_myaccount_') || '';
		base.post(_zjh.config.gainhiscases,{uid:myacc.split('_')[1]},function(d){
			var cases = {},fid;
			$('.contactName ul').html(function(){
				var lis = '';
				$.each(d.cases,function(i,o){
					cases[o.id] = o;
					!i && (fid = o.id);
					lis += '<li data-id="'+ o.id +'"><span>'+ o.contactName +'</span></li>';
				});
				return lis;
			});
			_this._data.cases = cases;
			_this.fillFormById(fid);
		});
	},
	fillFormById:function(id){
    	var base = this.base(),_this = this;
		var o = this._data.cases[id] || {};
		$.each(o,function(name,value){
			var lis = [],casesids = [];
			if(name == 'sex'){
				$('[name="'+ name +'"]').removeAttr('checked');
				$('[name="'+ name +'"][value="'+ value +'"]')[0].checked = true;
				return;
			}
			if(name == 'caseImages'){
				$.each(value,function(i,p){
					lis.push('<li id="fileBox_WU_FILE_'+ p.id +'" data-id="'+ p.id +'" class="browser"><div class="viewThumb"><img src="'+ p.fileUrl +'"></div><div class="diyCancel"><i class="iconfont" title="删除"></i></div></li>');
					casesids.push(p.id)
				});
				$('#picslist .fileBoxUl').html(lis.join(''));
				$('[name="'+ name +'"]').val(casesids.join(','));
				return;
			}
			$('[name="'+ name +'"]').val(value || '');
		});
		$('form [name="caseid"]').attr('data-oval',o.id).val(o.id);
		$('form [name="contactName"]').attr('data-oval',o.contactName);
		$('form [name="idcard"]').val(o.idNumber);
	},
	doIDNumbers:function(idcard){
    	var base = this.base(), _this = this, obj;
		if(idcard && base.valideCard(idcard)){
			obj = this.getidCardinfo(idcard);
			$('[name="sex"]').removeAttr('checked');
			$('[name="sex"][value="'+ obj.sexcode +'"]')[0].checked = true;
			$('[name="age"]').val(obj.age);
		}
	},
	validateAskTelQue:function(sign){
    	var base = this.base(),_this = this;
		var uid = this.cook('_key_myaccount_') || '';
		var data = $('#myform').serializeArray();
    	var uname = $('form [name="contactName"]').val(),
    		tel = $('form [name="telephone"]').val(),
    		idcard = $('form [name="idcard"]').val(),
    		sickdis = $('form [name="description"]').val(),
    		caseName = $('form [name="caseName"]').val(),
    		imgs = $('form [name="caseImages"]').val();
		if(!uname) return base.showTipE('请输入问诊人姓名'),false;
		if(!base.valideTel(tel)) return base.showTipE('请输入正确的联系电话'),false;
		if(idcard && !base.valideCard(idcard)) return base.showTipE('请输入正确的身份证号'),false;	
		if(!caseName) return base.showTipE('请填写要问诊的疾病'),false;
		if(sickdis.length < 10) return base.showTipE('请输入病症描述，不少于10字'),false;	
		if(imgs.split(',').length > 9) return base.showTipE('影像资料不能超过9张'),false;
		base.showLineLoading().post(_zjh.config[sign],data,function(d){
			var url = d.code_url,no = d.out_trade_no,cost = $('[name="pmoney"]').val();
			var his = sign == 'subtworder' ? 'user/user_myaskimg' : 'user/user_myasktel';
			base.hideLineLoading();
			if(d.payStatus == true){
				_this.cook('_key_history_',location.href.split('#')[0] + '#' + his,1);
				location.hash = '1=1';
				_this.fillBODY('online_wxpay',{img:url,wxout_trade_no:no,cost:(cost.indexOf('.') != -1 ? cost : (cost + '.00'))});
			}else{
				base.showTipS('提交成功');
				location.hash = his;
			}
		});
	},
	payNotCloedOrder:function(oid,otype){
		var base = this.base(),_this = this;
		base.showLineLoading().post('kangxin/continuepay',{oid:oid,otype:otype},function(d){
			var url = d.code_url,no = d.out_trade_no,cost = d.money.toString() || '0.01';
			base.hideLineLoading();
			!_this.cook('_key_history_') && _this.cook('_key_history_',location.href,1);
			location.hash = '1=1';
			_this.fillBODY('online_wxpay',{img:url,wxout_trade_no:no,cost:(cost.indexOf('.') != -1 ? cost : (cost + '.00'))});			
		});
	},
	getidCardinfo:function(UUserCard){ 
		var sex,age,sexcode,year = UUserCard.substring(6, 10),gmonth = UUserCard.substring(10, 12),gday = UUserCard.substring(12, 14);
		var myDate = new Date(),month = myDate.getMonth() + 1,day = myDate.getDate(); 
		if (parseInt(UUserCard.substr(16, 1)) % 2 == 1) { 
			sex = '男',sexcode = '1';
		} else { 
			sex = "女",sexcode = '0';
		} 
		age = myDate.getFullYear() - UUserCard.substring(6, 10) - 1; 
		if (gmonth < month || gmonth == month && gday <= day) { age++; } 
		return {birth:year + "-" + gmonth + "-" + gday,sex:sex,age:age,sexcode:sexcode}
	},
    listenOnlineWxPay:function(){
    	var base = this.base(),_this = this;
    	var tradeno = $('[name="wxout_trade_no"]').val();
    	tradeno && base.post('kangxin/listenpaystatus_tw',{tradeno:tradeno},function(d){
    		if(d.status == 'success'){
				var href = _this.cook('_key_history_') || location.href.split('#')[0];
				_this.uncook('_key_history_');
				window.location.href = href;
    		}else{
    			window.setTimeout(function(){
    				_this.listenOnlineWxPay();
    	    	},3000);
    		}
    	});
    }/*,
	beginSendTWen:function(casesId){
    	var base = this.base(),_this = this,
			doctorId = $('form [name="doctorId"]').val();
    	casesId ? base.postStr(_zjh.config.beginTwen,'{"body":{"casesId":"'+ casesId +'","doctorId":"'+ doctorId +'","askStatus":4,"askType":0,"isCooHospital":0,"isSeen":0}}',function(d){
			if(d.resultCode == '200'){
				base.showTipS('提交成功');
				location.hash = 'online';
			}else{	
				base.showTipE(d.message || '提交失败');		
			}
		}) : base.showTipE('无效的操作');
	}*/,
	fillTuWenTableList:function(uid,pid,url,no,state){
		var base = this.base(),_this = this;
		base.showLineLoading().get(url,{"uid":uid,"pageSize": _zjh.config.pagesize,"pageNo": no},function(d){
			nodetpl.get(pid + '.htm', {
				base: _h,
				pid:pid,
				pageSize:_zjh.config.pagesize,
				pageNumber:no,
				pager: {list:d.orders}
			}, function(htm) {
				if(state == 'append'){
					$('#mycenterDIV .table tbody').append($(htm).find('tbody').html());
					$('#mycenterDIV .moretable').replaceWith($(htm).find('.moretable') || '');
				}else{
					$('#mycenterDIV').html(htm);
					window.scrollTo(0,0);
				}
	    		base.hideLineLoading()
			}) 
		});
	},
	fillTuWenDetail:function(id,sta){
		var base = this.base(),_this = this;
		base.showLineLoading().get('kangxin/gaingraphicmessages',{"oid":id},function(d){
			//判断是否已付款
			if(d.twinfo.payStatus == '4' && d.twinfo.askStatus == '4'){
				_this.payNotCloedOrder(id,1);
				return false;
			}
			nodetpl.get('user_myaskimg_detail.htm', {
				base: _h,
				sta:sta,
				messages: d.messages
			}, function(htm) {				
				$('#mycenterDIV').html(htm);
				window.scrollTo(0,0);
	    		base.hideLineLoading()
			}) 
		});
	},
    saveAskAgain:function(text,oid,uid,fn){
    	var base = this.base(),_this = this;
    	text && base.showLineLoading().post('kangxin/appendmessage',{msgContent:text,oid:oid,uid:uid},function(d){
    		base.hideLineLoading(),fn();
    	});
    },
    endAskOrder:function(oid,fn){
    	var base = this.base(),_this = this;
    	oid && base.showLineLoading().post('doctor/closetw',{oid:oid,utype:'1'},function(d){
    		base.hideLineLoading(),fn();
    	});
    },
    endTelOrder:function(oid,uid,fn){
    	var base = this.base(),_this = this;
    	oid && base.showLineLoading().post('doctor/closetel',{oid:oid,uid:uid,ctype:'3',utype:'1'},function(d){
    		base.hideLineLoading(),fn();
    	});
    },
    initVideoKanDian:function(){
    	var base = this.base(),_this = this;
    	base.showLineLoading().get('kangxin/gainnewforum',{},function(data){
    		var d = data.forum;
    		base.hideLineLoading(),
    		$('#videoKanDian').html(function(){
    			return '<div class="thumb">\
					<img src="'+ d.backImag +'"/>\
    			</div>\
    			<div class="context">\
    				<div class="title2"><span>本期节目嘉宾</span></div>\
    				<div class="text">\
    					<p>'+ (d.guestIntro || '').substr(0,180) +'...</p>\
    				</div>\
    				<div class="title2"><span>本期节目看点</span></div>\
    				<div class="text rows clearfix">\
    					'+ (function(at){
    						var p = '';
    						$.each(at,function(i,o){
    							p += '<p><span></span><label>'+ o +'</label></p>';
    						});
    						return p;
    					})((d.currWatch||'').split('@')) +'\
    				</div>\
    			</div>\
    			<div class="linka">\
    				<a href="'+ d.accessUrl +'" target="_blank">节目视频链接：'+ d.accessUrl +'</a>\
    			</div>';
    		}),
    		d.weiBoUrl ? $('#weiboreyi iframe').attr('src',d.weiBoUrl.replace(new RegExp('960','g'),'837')) : $('#weiboreyi').remove();
    	});
    }
});

