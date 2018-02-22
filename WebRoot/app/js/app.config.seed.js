/*         ______________________________________
  ________|                                      |_______
  \       |           SmartAdmin WebApp          |      /
   \      |      Copyright © 2015 MyOrange       |     /
   /      |______________________________________|     \
  /__________)                                (_________\

 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * =======================================================================
 * SmartAdmin is FULLY owned and LICENSED by MYORANGE INC.
 * =======================================================================
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * =======================================================================
 * original filename: app.config.js
 * filesize: 12kb
 * author: Sunny (@bootstraphunt)
 * email: info@myorange.ca
 * =======================================================================
 * 
 * GLOBAL ROOT (DO NOT CHANGE)
 */
if (window.jQuery) { 
	$.root_ = $('body');
	/*
	 * APP CONFIGURATION (HTML/AJAX/PHP Versions ONLY)
	 * Description: Enable / disable certain theme features here
	 * GLOBAL: Your left nav in your app will no longer fire ajax calls, set 
	 * it to false for HTML version
	 */
	$.navAsAjax = true;
	
	/*
	 * GLOBAL: Sound Config (define sound path, enable or disable all sounds)
	 */
	$.sound_path = "sound/";
	$.sound_on = true;
	$.color = {
		error: '#C46A69', //c26565
		noraml: '#296191', //5384AF
		waring: '#C79121',
		success: '#739E73'
	};
}
if (location.host === 'www.51zjh.com'){
	window.IceServers = {
		"iceServers": [{
			"url": "stun:123.56.251.54:3478"
		}, {
			"url": "turn:123.56.251.54:3478",
			"username": "ling",
			"credential": "ling1234"
		}]
	};
	window.SocketIOSrc = 'https://socketio.51zjh.com/';
}else if (location.host === 'develop.ebaiyihui.com') {
	window.IceServers = {
		"iceServers": [{
			"url": "stun:123.56.251.54:3478"
		}, {
			"url": "turn:123.56.251.54:3478",
			"username": "ling",
			"credential": "ling1234"
		}]
	};
	window.SocketIOSrc = 'https://socketio.ebaiyihui.com/';
}else if(location.host === 'www.ebaiyihui.com'){
	window.IceServers = {
			"iceServers": [{
				"url": "stun:123.56.251.54:3478"
			}, {
				"url": "turn:123.56.251.54:3478",
				"username": "ling",
				"credential": "ling1234"
			}]
		};
	window.SocketIOSrc = 'https://socketio.ebaiyihui.com/';
}else {
	window.IceServers = {
		"iceServers": [{
			"url": "stun:47.95.212.72:3478"
		}, {
			"url": "turn:47.95.212.72:3478",
			"username": "turnserver",
			"credential": "0x9c4fb4f444c9a786df2ac2e83dc0d611"
		}]
	};
	window.SocketIOSrc = 'https://socketio.ebaiyihui.com/';
}
window.WebSocketSrc = "wss://" + location.host + "/signalmaster_new";
/*
 * SAVE INSTANCE REFERENCE (DO NOT CHANGE)
 * Save a reference to the global object (window in the browser)
 */
var root = this,
	/*
	 * DEBUGGING MODE
	 * debugState = true; will spit all debuging message inside browser console.
	 * The colors are best displayed in chrome browser.
	 */
	debugState = false,
	debugStyle = 'font-weight: bold; color: #00f;',
	debugStyle_green = 'font-weight: bold; font-style:italic; color: #46C246;',
	debugStyle_red = 'font-weight: bold; color: #ed1c24;',
	debugStyle_warning = 'background-color:yellow',
	debugStyle_success = 'background-color:green; font-weight:bold; color:#fff;',
	debugStyle_error = 'background-color:#ed1c24; font-weight:bold; color:#fff;',
	/*
	 * Impacts the responce rate of some of the responsive elements (lower 
	 * value affects CPU but improves speed)
	 */
	throttle_delay = 350,
	/*
	 * The rate at which the menu expands revealing child elements on click
	 */
	menu_speed = 235,
	/*
	 * Collapse current menu item as other menu items are expanded
	 * Careful when using this option, if you have a long menu it will
	 * keep expanding and may distrupt the user experience This is best 
	 * used with fixed-menu class
	 */
	menu_accordion = true,
	/*
	 * Turn on JarvisWidget functionality
	 * Global JarvisWidget Settings
	 * For a greater control of the widgets, please check app.js file
	 * found within COMMON_ASSETS/UNMINIFIED_JS folder and see from line 1355
	 * dependency: js/jarviswidget/jarvis.widget.min.js
	 */
	enableJarvisWidgets = true,
	/*
	 * Use localstorage to save widget settings
	 * turn this off if you prefer to use the onSave hook to save
	 * these settings to your datatabse instead
	 */
	localStorageJarvisWidgets = true,
	/*
	 * Turn on/off sortable feature for JarvisWidgets 
	 */
	sortableJarvisWidgets = true,
	/*
	 * Warning: Enabling mobile widgets could potentially crash your webApp 
	 * if you have too many widgets running at once 
	 * (must have enableJarvisWidgets = true)
	 */
	enableMobileWidgets = false,
	/*
	 * Turn on fast click for mobile devices
	 * Enable this to activate fastclick plugin
	 * dependency: js/plugin/fastclick/fastclick.js 
	 */
	fastClick = false,
	/*
	 * SMARTCHAT PLUGIN ARRAYS & CONFIG
	 * Dependency: js/plugin/moment/moment.min.js 
	 *             js/plugin/cssemotions/jquery.cssemoticons.min.js 
	 *             js/smart-chat-ui/smart.chat.ui.js
	 * (DO NOT CHANGE) 
	 */
	boxList = [],
	showList = [],
	nameList = [],
	idList = [],
	/*
	 * Width of the chat boxes, and the gap inbetween in pixel (minus padding)
	 */
	chatbox_config = {
		width: 200,
		gap: 35
	},
	/*
	* JS ARRAY SCRIPT STORAGE
	* Description: used with loadScript to store script path and file name
	* so it will not load twice
	*/	
	jsArray = {},
	/*
	 * These elements are ignored during DOM object deletion for ajax version 
	 * It will delete all objects during page load with these exceptions:
	 */
	ignore_key_elms = ["#header, #left-panel, #right-panel, #main, div.page-footer, #shortcut, #divSmallBoxes, #divMiniIcons, #divbigBoxes, #voiceModal, script, .ui-chatbox"];

/*
 * END APP.CONFIG
 */

/**本地缓存*/
var LStorage = {
	set: function (key, value) {
		if (window.localStorage) {
			localStorage.setItem(key, value);
		} else {
			COOKIE._set(key, value);
		}
	},
	get: function (key) {
		if (window.localStorage) {
			return window.localStorage ? (localStorage[key] || "") : "";
		} else {
			return COOKIE._get(key);
		}
	},
	remove: function (key) {
		if (window.localStorage) {
			localStorage.removeItem(key);
		} else {
			COOKIE._remove(key);
		}
	},
	clear: function () {
		localStorage.clear();
	}
};
var SStorage = {
	set: function (key, value) {
		if (window.sessionStorage) {
			sessionStorage.setItem(key, value);
		} else {
			COOKIE._set(key, value);
		}
	},
	get: function (key) {
		if (window.localStorage) {
			return window.sessionStorage ? (sessionStorage[key] || "") : "";
		} else {
			return COOKIE._get(key);
		}
	},
	remove: function (key) {
		if (window.sessionStorage) {
			sessionStorage.removeItem(key);
		} else {
			COOKIE._remove(key);
		}
	},
	clear: function () {
		sessionStorage.clear();
	}
};
var COOKIE = {
	_set: function (name, value, expires) {
		var _end = new Date();
		if (expires) {
			_end.setTime(_end.getTime() + (expires * 1000));
		}
		document.cookie = name + "=" + escape(value) + (expires ? (";expires=" + _end.toGMTString()) : "") + ";path=/;domain=" + location.host;
	},
	_get: function (name) {
		var _cookie = document.cookie;
		var _start = _cookie.indexOf(name + "=");
		if (_start != -1) {
			_start += name.length + 1;
			var _end = _cookie.indexOf(";", _start);
			if (_end == -1) {
				_end = _cookie.length;
			}
			return unescape(_cookie.substring(_start, _end));
		}
		return "";
	},
	_remove: function (name) {
		var exp = new Date();
		exp.setTime(exp.getTime() - 1);
		var cval = this._get(name);
		if (cval != null)
			document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
	},
	_clear: function () {
		document.cookie = "";
	}
};


/*
 * LOAD SCRIPTS
 * Usage:
 * Define function = myPrettyCode ()...
 * loadScript("js/my_lovely_script.js", myPrettyCode);
 */
function loadScript(scriptName, callback) {

	if (!jsArray[scriptName]) {
		// adding the script tag to the head as suggested before
		var body = document.getElementsByTagName('body')[0],
			script = document.createElement('script');
		script.type = 'text/javascript';
		script.src = scriptName;

		// then bind the event to the callback function
		// there are several events for cross browser compatibility
		script.onload = function () {
			jsArray[scriptName] = true;
			callback();
		};

		// fire the loading
		body.appendChild(script);

		// clear DOM reference
		//body = null;
		//script = null;

	} else if (callback) {
		// changed else to else if(callback)
		if (debugState) {
			root.root.console.log("This script was already loaded %c: " + scriptName, debugStyle_warning);
		}
		//execute function
		callback();
	}
}

function loadCss(linkName, callback, rel) {
	if (!jsArray[linkName]) {
		jsArray[linkName] = true;

		// adding the script tag to the head as suggested before
		var head = document.getElementsByTagName('head')[0],
			link = document.createElement('link');
		link.type = 'text/css';
		link.rel = 'stylesheet' + (rel ? ('/' + rel) : '');
		link.href = linkName;

		link.onload = callback || function () {};

		// fire the loading
		head.appendChild(link);

	} else if (callback) {
		// changed else to else if(callback)
		if (debugState) {
			root.root.console.log("This link was already loaded %c: " + linkName, debugStyle_warning);
		}
		//execute function
		callback();
	}
}

/* ~ END: LOAD SCRIPTS */

/*
 * HELPFUL FUNCTIONS
 * We have included some functions below that can be resued on various occasions
 * 
 * Get param value
 * example: alert( getParam( 'param' ) );
 */
function getParam(name, href) {
	name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
	var regexS = "[\\?&]" + name + "=([^&#]*)";
	var regex = new RegExp(regexS);
	var results = regex.exec(href || window.location.href);
	if (results == null)
		return "";
	else
		return results[1];
}