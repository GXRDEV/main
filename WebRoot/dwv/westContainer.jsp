
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ page  
    import="java.util.*,java.text.*,org.apache.commons.lang3.StringUtils"%> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@taglib prefix="stu" uri="WEB-INF/tlds/StudyInfo.tld" %>
<%@taglib prefix="ser" uri="WEB-INF/tlds/SeriesDetails.tld" %>
<%@taglib prefix="img" uri="WEB-INF/tlds/ImageInfo.tld" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%
    String patName = new String(request.getParameter("patientName").getBytes("ISO-8859-1"), "UTF-8");
	String studyDesc=request.getParameter("studyDesc"); 
    studyDesc=new String(request.getParameter("studyDesc").getBytes("ISO-8859-1"), "UTF-8");
    int iteration_Total = 0;
    String studyDate="";
    if(StringUtils.isNotBlank(request.getParameter("studyDate"))){
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	long timeStart=Long.parseLong(request.getParameter("studyDate"));
    	System.out.println(timeStart);
    	Date date = new Date(timeStart);
    	studyDate=sdf.format(date);
    }
%>

<html>
 	<head>
         <style type="text/css">
            .heading
            {
                font-family: Arial,Helvitica,Serif;
                font-size: 13px;
                font-weight: bold;
                padding-left: 3px;
            }
            
            .seriesTable {
            	table-layout: fixed;
            	width: 100%;
            	font-family: Arial,Helvitica,Serif;
            	font-size: 12px;
            	border-spacing: 0px;
            	padding-left: 2px;
            }
            
             .scale-image {
            	width: 30%;
            	height: 30%;            	
            }

        </style>

        <script type="text/javascript">

            var series = new Array();

            $(document).ready(function() {
                var WPSeriesColor = $('.sorting_1').css('background-color');
                jQuery('.seriesTable').css('background-color', WPSeriesColor);
                if(WPSeriesColor == 'rgb(211, 214, 255)')
                    jQuery('.seriesTable').find('tr:first').css('background-color', WPSeriesColor);
            
                $('.seriesTable').find('img:last').load(function() { 
                	//console.log("Series Loaded!!!!!!.....");
                	if(pat.serverURL == 'C-GET' || pat.serverURL == 'C-MOVE') {
                		var tmpImgSrc = $(this).attr('src');
    	           		$(this).parent().waitForImages(function() {
							//insertInstances('${param.patient}', getParameter(tmpImgSrc, 'study'), getParameter(tmpImgSrc, 'series'));  
							getInstances('${param.patient}', getParameter(tmpImgSrc, 'study'), getParameter(tmpImgSrc, 'series'));
               			});
               		}
                	saveJpgImages();
                });   
                //var height = jQuery(window).height()-60+"px";
    			//jQuery('#westPane').css('height',height);  
            });  //document.ready 
            
            function changeImgView(but) {
                //var table = $(but).parent().parent().parent().parent();
                //console.log(table.children().find('tr:nth-child(2)').children());

                var imgBut = $(but).attr('src');
                var imgCount = 0;

                if(imgBut.indexOf("all.png") >=0 ) {
                    $(but).attr('src', 'images/one.png');

                    $(but).parent().prev().children().each(function() {
                        if(imgCount == 0) {
                            $(this).css('background-color', '#00F');
                        } else {
                            $(this).css('background-color', '#a6a6a6');
                        }
                        imgCount++;
                    });

                    imgCount = 0;

                    $(but).parent().parent().next().children().children().each(function() {
                        if(imgCount == 0) {
                            $(this).css('display', 'inline');
                        } else {
                            $(this).css('display', 'none');
                        }
                        imgCount++;
                    });
                } else if(imgBut.indexOf("one.png") >=0 ) {
                    $(but).attr('src', 'images/three.png');
                    var serDivs = $(but).parent().prev().children();
                    var totserDivs = serDivs.length;
                    serDivs.each(function() {
                        if(imgCount == 0 || imgCount == Math.round(totserDivs/2)-1 || imgCount == totserDivs-1) {
                            $(this).css('background-color', '#00F');
                        } else {
                            $(this).css('background-color', '#a6a6a6');
                        }
                        imgCount++;
                    });

                    imgCount = 0;

                    var serImgs = $(but).parent().parent().next().children().children();
                    var serInsCnt = serImgs.length;
                    serImgs.each(function() {
                        if(imgCount == 0 || imgCount == Math.round(serInsCnt/2)-1 || imgCount == serInsCnt-1) {
                            $(this).css('display', 'inline');
                        } else {
                            $(this).css('display', 'none');
                        }
                        imgCount++;
                    });
                } else {
                    $(but).attr('src', 'images/all.png');

                    $(but).parent().prev().children().each(function() {
                        $(this).css('background-color', '#00F');
                    });

                    $(but).parent().parent().next().children().children().each(function() {
                        $(this).css('display', 'inline');
                    });
                }

                /* var tmp = $(but).attr('name');
                var tmp = tmp.split("|");
                var tagUrl = "tableContainer.jsp?patient=${param.patient}&study=${param.study}&dcmURL=${param.dcmURL}";
                tagUrl += "&wadoUrl=${param.wadoUrl}";
                tagUrl += "&series=" + tmp[0].trim() + "&numberOfImages=" + tmp[1].trim()+"&toggleImageView=0";
                table.load(encodeURI(tagUrl));*/
            }

            function changeSeries(image) {
                var imgSrc = image.src;

                if(imgSrc.indexOf('images/SR_Latest.png') > 0) {
                    imgSrc = jQuery(image).attr('imgSrc');
                }

                if(imgSrc.indexOf('images/pdf.png') > 0)  {
                    imgSrc = jQuery(image).attr('imgSrc');
                }
                
                parent.selectedFrame = null;//For IE 

                var url = location.origin + '/dwv/frameContent.html?studyUID=' + getParameter(imgSrc, 'study');
                url += '&seriesUID=' + getParameter(imgSrc, 'series');
                url += '&instanceNumber=' + parseInt(image.name-1);
                url += '&serverURL=' + getParameter(imgSrc, 'serverURL');
                var actFrame = getActiveFrame();
                actFrame.src = url;
                doMouseWheel = true;
				var divName = $('.toggleOff').attr('id');
				$('.toggleOff').removeClass('toggleOff');
				$('.imgOn').addClass('imgOff').removeClass('imgOn');
				if(divName=='ruler') {
					$("#measurementDiv input").attr('size', '10');
				    $("#measurementDiv a").css('visibility', 'hidden');
				    measureEnabled = false;		
				}
				clearAnnotations();
				parent.scrollImages = false;
           	}

            function storeInstanceInfo(totIns, serId) {
            	for(var i=0; i<series.length; i++) {
                    if(serId == series[i].seriesUID) {
                        if(series[i].totalImages == totIns) {
                            insertInstances('${param.patient}', '${param.study}', serId);  
                            delete series[i].seriesUID;
                            delete series[i].totalImages;
    			} else {
                            series[i].totalImages = series[i].totalImages + 1;
    			}
    			break;
                    }
            	}
            }
            
            function toggle(divider) {
            	var div = $(divider);
            	if(div.attr('title')==='Close') {		        	
		        	div.attr('title','Open');
		        	div.parent().css('width','1%');
		        	div.parent().next().css('width','98%');
		        	div.css('background','url("images/showleft.png")');
		        	$('#previews').hide();
		        	$('.heading').hide();
		        	$('#studyTable').hide();
	        	} else {
	        		div.attr('title','Close');
		        	div.parent().css('width','18%');
		        	div.parent().next().css('width','82%');
		        	div.css('background','url("images/hideleft.png")');
		        	$('#previews').show();
		        	$('.heading').show();
		        	$('#studyTable').show();
	        	}
            }
            </script>
    </head>
    <body>
    	<div title="Close" style="width: 17px; height: 17px; cursor: pointer; float: right; background: url('images/hideleft.png');" onclick="toggle(this);"></div>
    
        <div id="patName" class="heading" style="color: #FF8A00; padding: 2px 0 3px 3px;"><%=patName%></div>
        <div id="patID" class="heading" style="color:#FF8A00; padding: 0 0 3px 3px;">ID: ${param.patient}</div>
        <table id="studyTable" style="font-family: Arial,Helvitica,Serif; font-size:13px; width: 100%;color: #FF8A00; padding-bottom:3px; border: none;">
            <tbody>
                <tr>
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td><%=studyDate%></td>
                    <td align="right" style="padding-right: 15px;">${param.totalSeries} Series</td>
                </tr>
            </tbody>
        </table>
        <div id="previews" style="overflow: auto; height: 89%; border-top: 2px solid black;">
        <c:set var="base" value="/" />
    <ser:Series patientId="${param.patient}" study="${param.study}" dcmURL="${param.dcmURL}">
        <c:set var="middle" value="${(numberOfImages+0.5)/2}" />
        <fmt:formatNumber var="middle" maxFractionDigits="0" value="${middle}" />
        <c:if test="${not (param.wadoUrl != 'C-MOVE' && param.wadoUrl != 'C-GET')}">
            <script type="text/javascript">
                series.push({
                    "seriesUID" : '${seriesId}',
                    "totalImages" : 1
                });
            </script>
        </c:if>

        <table class="seriesTable" >
            <tbody>
                <tr onclick="jQuery(this).next().toggle()" style="cursor: pointer; font-weight:bold; color: #FF8A00">
                    <td> ${seriesDesc}</td>
                    <td align="right"> ${numberOfImages} Imgs</td>
                </tr>
                <tr>
                    <td colspan="2">
                        <table style="table-layout:fixed; width:100%;">
                            <tr>
                                <td id="${fn:replace(seriesId, '.','_')}" class="seriesImgsIndex" style="width: 94%;">
                            <c:forEach var="i" begin="1" end="${numberOfImages}">
                                <c:choose>
                                    <c:when test="${(i == middle) || (i==1) || (i==numberOfImages)}">
                                        <div style="background: #00F; width: 5px; height: 5px; float: left;margin: 0 1px 1px;"></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div style="background: #a6a6a6; width: 5px; height: 5px; float: left;margin: 0 1px 1px;"></div>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                    </td>
                    <td align="right" style="vertical-align: top; ">
            <c:choose>
                <c:when test="${numberOfImages > 3}">
                    <img class="toggleImgView" src="images/three.png" name="${seriesId} | ${numberOfImages}" onclick="changeImgView(this)" />
                </c:when>
                <c:otherwise>
                    <img class="toggleImgView" src="images/all.png" name="${seriesId} | ${numberOfImages}" />
                </c:otherwise>
            </c:choose>

            </td>
            </tr>

            <tr>
                <td colspan="2" style="width: 30%; height: 30%;">
            <img:Image patientId="${param.patient}" study="${param.study}" series="${seriesId}" dcmURL="${param.dcmURL}">
                <c:choose>
                    <c:when test="${modality == 'SR'}">
                        <img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" style="max-width: 30%; max-height: 30%;" src="images/SR_Latest.png" imgSrc="${base}dcmimage?serverURL=${param.wadoUrl}&study=${param.study}&series=${seriesId}&object=${imageId}&rows=${rows}" onclick="changeSeries(this)"/>
                    </c:when>

                    <c:when test="${sopClassUID == '1.2.840.10008.5.1.4.1.1.104.1'}">
                        <img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" style="max-width: 30%; max-height: 30%;" src="images/pdf.png" imgSrc="${base}dcmimage?serverURL=${param.wadoUrl}&study=${param.study}&series=${seriesId}&object=${imageId}&rows=${rows}" onclick="changeSeries(this)" />
                    </c:when>

                    <c:otherwise>
                        <c:choose>
                            <c:when test="${param.wadoUrl == 'C-GET'}">
                                <c:choose>
                                    <c:when test="${(instanceNumber == middle) || (instanceNumber==1) || (instanceNumber==numberOfImages)}">
                                        <img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" style="max-width: 30%; max-height: 30%;" src="Wado.do?dicomURL=${param.dcmURL}&study=${param.study}&series=${seriesId}&object=${imageId}&retrieveType=${param.wadoUrl}&sopClassUID=${sopClassUID}" onclick="changeSeries(this)" />
                                    </c:when>
                                    <c:otherwise>
                                        <img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" style="max-width: 30%; max-height: 30%; display: none;" src="Wado.do?dicomURL=${param.dcmURL}&study=${param.study}&series=${seriesId}&object=${imageId}&retrieveType=${param.wadoUrl}&sopClassUID=${sopClassUID}" onclick="changeSeries(this)" />
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${param.wadoUrl == 'C-MOVE'}">
                                <c:choose>
                                    <c:when test="${(instanceNumber == middle) || (instanceNumber==1) || (instanceNumber==numberOfImages)}">
                                        <img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" style="max-width: 30%; max-height: 30%;" src="Wado.do?dicomURL=${param.dcmURL}&study=${param.study}&series=${seriesId}&object=${imageId}&retrieveType=${param.wadoUrl}" onclick="changeSeries(this)" onload="this.style.height='auto';"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" style="max-width: 30%; max-height: 30%; display: none;" src="Wado.do?dicomURL=${param.dcmURL}&study=${param.study}&series=${seriesId}&object=${imageId}&retrieveType=${param.wadoUrl}" onclick="changeSeries(this)"  />
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${(instanceNumber == middle) || (instanceNumber==1) || (instanceNumber==numberOfImages)}">
                                        <img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" style="max-width: 30%; max-height: 30%;" src="${base}dcmimage?serverURL=${param.wadoUrl}&study=${param.study}&series=${seriesId}&object=${imageId}&rows=${rows}" onclick="changeSeries(this)" />
                                    </c:when>
                                    <c:otherwise>
                                        <img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" style="max-width: 30%; max-height: 30%; display: none;" src="${base}dcmimage?serverURL=${param.wadoUrl}&study=${param.study}&series=${seriesId}&object=${imageId}&rows=${rows}" onclick="changeSeries(this)" />
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </img:Image>
            </td>
            </tr>
            <tr>
                <td colspan="2">
            <img:Image patientId="${param.patient}" study="${param.study}" series="${seriesId}" dcmURL="${param.dcmURL}">
                <c:if test="${multiframe == 'yes'}">
                    <c:forEach var="i" begin="1" end="${numberOfFrames}">
                        <img name="${i}" id="${fn:replace(imageId, '.','_')}_${i}" style="max-width: 30%; max-height: 30%; display: none;" src="${base}dcmimage?serverURL=${param.wadoUrl}&study=${param.study}&series=${seriesId}&object=${imageId}&rows=${rows}&frameNumber=${i}"/>
                    </c:forEach>
                </c:if>
            </img:Image>
            </td>
            </tr>

        </table>
    </td>
</tr>
</tbody>
</table>
</ser:Series>
</div>

		</body>
</html>