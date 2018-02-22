<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>        
        <link rel="icon" type="image/png" href="images/favicon.png"/>
        <link rel="stylesheet" href="css/login.css" type="text/css" />
        <link rel="stylesheet" type="text/css" href="css/jquery.ui.all.css" />
        <script type="text/javascript" src="js/lib/jquery-latest.js"></script>
        <script type="text/javascript" src="js/lib/jquery-ui-latest.js"></script>
        <script type="text/javascript" src="js/lib/themeswitchertool.js"></script>
        <script type="text/javascript" src="js/lib/jquery.cookies.min.js"></script>
        <script type="text/javascript">
            if( /Android|webOS|iPhone|iPad|iPod|BlackBerry/i.test(navigator.userAgent) ) {
                $.get("do/IOviyamContext", function(data) {
                    var loc = window.location.toString();
                    loc = loc.substring(0, loc.indexOf('/', loc.lastIndexOf(':')));
                    window.location = loc + data;
                });
            } 
        </script>
        <script>    
            var lang = $.cookies.get( 'language' );
            var bundleName = '';
            if (lang == null || lang.trim() == 'en_GB') {
                bundleName = 'js/i18n/Bundle.js'; 
            } else {
                bundleName = 'js/i18n/' + "Bundle_" + lang + ".js";
            }           
            document.write('<script type="text/javascript" src="' + bundleName + '"><\/script>');
        </script>
        <script type="text/javascript">
            $(document).ready(function() {
                if(!!getStudyDetails()){
                    loadUser();
                    $('#loginButton').button();
                    $("#loginButton").val(languages['Login'] || 'login');
                    $(document).attr('title', languages['PageTitle']);                    
                }
            });  

            function setCookie(name, value, expires, path, domain, secure) {
		  		var curCookie = name + "=" + escape(value) +
			  	((expires) ? "; expires=" + expires.toUTCString() : "") +
			  	((path) ? "; path=" + path : "") +
			  	((domain) ? "; domain=" + domain : "") +
			  	((secure) ? "; secure" : "");
		  		document.cookie = curCookie;
			}
            
            function validateForm(form) {
            	if(form.remember_me.checked) {
        	  		var credentials = {
        	  			'username' : form.j_username.value,
        	  			'password' : form.j_password.value,
        	  			'remember': form.remember_me.checked
        	  		};
        	  		setCookie("credentials",JSON.stringify(credentials));
        	  } else {
        		  	setCookie("credentials","");
        	  }
        	  return true;	
            }
            
            function loadUser() {
        		var credentials = $.cookies.get("credentials");
        		if(credentials) {
        			document.login.j_username.value = credentials['username'];
        			document.login.j_password.value = credentials['password'];
        			document.login.remember_me.checked = credentials['remember'];
        			//document.login.login_button.focus();
        		} else {
        			document.login.j_username.focus();
        		}
        	}            
        </script>
        <script type="text/javascript">
            function getParameter(queryString, parameterName) {
                var parameterName = parameterName + "=";
                if(queryString.length > 0) {
                    var begin = queryString.indexOf(parameterName);
                    if(begin != -1) {
                        begin += parameterName.length;
                        var end = queryString.indexOf("&", begin);
                        if(end == -1) {
                            end = queryString.length;
                        }
                        return unescape(queryString.substring(begin, end));
                    }
                    return '';
                }
            }
            function getStudyDetails() {
                var queryString = document.location.search.substring(1);
                var patId = getParameter(queryString, "patientID");
                var u = getParameter(queryString, "u");
                var p = getParameter(queryString, "p");
                var formpost = document.getElementById("login");
                if (!!patId && !!u) {
                    document.login.j_username.value = u;
                    document.login.j_password.value = p || u;
                    formpost.submit ? formpost.submit() : $(formpost).submit();
                }else{
                    formpost.style.opacity = '1';
                    return true;
                }
            }
        </script>
    </head>
    <body class="ui-widget-content" style="border:none;">
    <section>
        <form name="login" id="login" action="j_security_check" method="post" style="opacity:0"> <!--onsubmit="return validateForm(this);"-->
            <fieldset>
                <legend><font>Login</font></legend>
                <h1><script>document.write(languages['PageTitle']) </script></h1> <h4>DICOM Web Workstation - version 2.2</h4>
                
                <label><font><script>document.write(languages['UserName'])</script> </font><span class="mandatory"><font>*</font></span><font> :</font></label>
                <input type="text" name="j_username" class="textInput" />

                <label><font><script>document.write(languages['Password'])</script> </font><span class="mandatory"><font>*</font></span><font> :</font></label>
                <input type="password" name="j_password" class="textInput" />

				<table> 
                    <tr> 
                        <td><input type="checkbox" name="remember_me" value="Remember Me"/></td> 
                        <td style="font-size: 10pt;"> Remember Me </td> 
                    </tr> 
                </table>

                <input type="submit" name="loginButton" id="loginButton" value="login" class="button">
            </fieldset>
        </form>
    </section>
</body>
</html>