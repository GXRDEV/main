<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@page errorPage="ErrorPage.jsp" %>
<%@taglib prefix="img" uri="WEB-INF/tlds/ImageInfo.tld" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="middle" value="${param.numberOfImages/2}" />
<fmt:formatNumber var="middle" maxFractionDigits="0" value="${middle}" />

<html>
  <head>
        <script type="text/javascript">
        	function toggleImgView(but) {
        		var table = $(but).parent().parent().parent().parent();
    		    var tmp = $(but).attr('name');

    		    var tmp = tmp.split("|");
        		var tagUrl = "tableContainer.jsp?patient=${param.patient}&study=${param.study}&dcmURL=${param.dcmURL}";
                        tagUrl += "&wadoUrl=${param.wadoUrl}";
        		tagUrl += "&series=" + tmp[0].trim() + "&numberOfImages=" + tmp[1].trim();

	       		var imgBut = $(but).attr('src');
	       		if(imgBut.indexOf("all.png") >=0 ) {
	       		   $(but).attr('src', 'one.png');
	       		   tagUrl += "&toggleImageView=1";
	       		} else if(imgBut.indexOf("one.png") >=0 ) {
	       		   $(but).attr('src', 'three.png');
	       		   tagUrl += "&toggleImageView=3";
	       		} else {
	       		   $(but).attr('src', 'all.png');
	       		   tagUrl += "&toggleImageView=0";
	       		}
        	    table.load(encodeURI(tagUrl));
        	}

            function changeSeries(image) {
            	var imgSrc = image.src;
                var url = location.origin + '/dwv/frameContent.html?studyUID=' + getParameter(imgSrc, 'study');
				url += '&seriesUID=' + getParameter(imgSrc, 'series');
				url += '&serverURL=' + getParameter(imgSrc, 'serverURL');
    			var actFrame = getActiveFrame();
    			actFrame.src = url;
            }

        </script>
  </head>
  <body>
<table style="font-size:10px; width:100%" cellspacing="0">
  <tr>
  	<td>
	  <c:forEach var="i" begin="1" end="${param.numberOfImages}">
	    <c:choose>
	       <c:when test="${param.toggleImageView == 0}">
	       		<div style="background: #00F; width: 5px; height: 5px; float: left;margin: 0 1px 1px;"></div>
	       </c:when>

	       <c:when test="${param.toggleImageView == 3}">
			 <c:choose>
      		<c:when test="${(i == middle) || (i==1) || (i==param.numberOfImages)}">
            	<div style="background: #00F; width: 5px; height: 5px; float: left;margin: 0 1px 1px;"></div>
            </c:when>
		 	<c:otherwise>
               <div style="background: #a6a6a6; width: 5px; height: 5px; float: left;margin: 0 1px 1px;"></div>
			</c:otherwise>
		  </c:choose>
		  </c:when>
		  <c:when test="${param.toggleImageView == 1}">
			<c:choose>
      		<c:when test="${i==1}">
            	<div style="background: #00F; width: 5px; height: 5px; float: left;margin: 0 1px 1px;"></div>
            </c:when>
		 	<c:otherwise>
               <div style="background: #a6a6a6; width: 5px; height: 5px; float: left;margin: 0 1px 1px;"></div>
			</c:otherwise>
		  </c:choose>

		  </c:when>
		  </c:choose>
         </c:forEach>
	   </td>
	   <td align="right" style="vertical-align: top; ">
	      <c:choose>
	         <c:when test="${param.toggleImageView == 0}">
		  	   <img class="toggleImgView" src="images/all.png" name="${param.series} | ${param.numberOfImages}" onclick="toggleImgView(this)" />
		     </c:when>

		      <c:when test="${param.toggleImageView == 1}">
		        <img class="toggleImgView" src="images/one.png" name="${param.series} | ${param.numberOfImages}" onclick="toggleImgView(this)" />
		      </c:when>

		       <c:when test="${param.toggleImageView == 3}">
		          <img class="toggleImgView" src="images/three.png" name="${param.series} | ${param.numberOfImages}" onclick="toggleImgView(this)" />
		       </c:when>
		  </c:choose>
	   </td>
     </tr>
     <tr>
        <td colspan="2">
          <c:choose>
              <c:when test="${param.toggleImageView == 0}">
                <img:Image patientId="${param.patient}" study="${param.study}" series="${param.series}" dcmURL="${param.dcmURL}">
                  <img src="http://localhost:8080/dcmimage?serverURL=${param.wadoUrl}&study=${param.study}&series=${param.series}&object=${imageId}&rows=48" height="48px" width="48px" onclick="changeSeries(this)" />
               </img:Image>
              </c:when>

              <c:when test="${param.toggleImageView == 1}">
                <img:Image patientId="${param.patient}" study="${param.study}" series="${param.series}" dcmURL="${param.dcmURL}">
                <c:if test="${instanceNumber==1}">
                  <img src="http://localhost:8080/dcmimage?serverURL=${param.wadoUrl}&study=${param.study}&series=${param.series}&object=${imageId}&rows=48" height="48px" width="48px" onclick="changeSeries(this)" />
                </c:if>
               </img:Image>
              </c:when>

             <c:when test="${param.toggleImageView == 3}">
               <img:Image patientId="${param.patient}" study="${param.study}" series="${param.series}" dcmURL="${param.dcmURL}">
                <c:if test="${(instanceNumber == middle) || (instanceNumber==1) || (instanceNumber==param.numberOfImages)}">
                  <img src="http://localhost:8080/dcmimage?serverURL=${param.wadoUrl}&study=${param.study}&series=${param.series}&object=${imageId}&rows=48" height="48px" width="48px" onclick="changeSeries(this)" />
                </c:if>
               </img:Image>
              </c:when>

         </c:choose>
      </td>
   </tr>
</table>
</body>
</html>