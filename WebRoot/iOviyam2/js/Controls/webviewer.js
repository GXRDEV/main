/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var _ori = location.origin + '/';
var stageheight,overlay,canvas,imagearray,textOverlay,intervel,intervelback,intervelnxt, imgindex=0,imageChanger,stagewidth,leftview,canvasImgdata,nativerow,nativecoloum,diffrentHeight, addEventInArray,loadPixel;
var timer;
var bottomval=400;
var currentzoom=1;
var dragtool=false;
var zoomflag=false;
var wlwwflage=false;
var nxttouch=true;
var fliphoriv=true;
var rotateleftclr=true;
var wlwcenable=true;
var tagData;
var loopval="";
var looparrayval;
var  objects;
var toolEvent = [];

$(document).ready(function() {
    $("#imgSlider").val(0).slider("refresh");
    if(localStorage.getItem("studyid")==undefined||localStorage.getItem("studyid")==null){
        window.location='home.html';
    }
    var canRow="500";
    var canColumn="500"; 
    var objects=new Array();
    canvas=document.getElementById('imgcanvas');
    overlay=document.getElementById('overlayc');
    var imageObj = new Image();
    var bodypart=localStorage.getItem("ibodypart");
    var bodypartcombine = bodypart.split(","); 
    var sex= "F";
    var sercombine = sex.split(",");
    var DELAY = 500,
    clicks = 0,
    timer = null;
    var touchval=false;
    var orien=false;
    $.post(_ori + "instancequery", {
        "patientId": "19369",
        "studyUID": "1.3.12.2.1107.5.1.4.36907.30000013052406000760900000001",
        "seriesUID":"1.3.12.2.1107.5.1.4.36907.30000013052405113534300000024"
    },function(data) {  
        tagData=new Array();     
        tagData = $.parseJSON(data);
        loadtmpimages();
        init();
        alterpage();
    });
    $("#imgcanvas").rasterDrag();
    zoombind();
    $("#nxtbtn").click(function(){
        clearTimeout(intervel);
        nextImage();
    });
    $('#backbtns').click(function(){
        clearTimeout(intervel);
        if(0 == imgindex){
            imgindex=imagearray.length-1;
        }else{ 
            imgindex--;
        }
        imageChange(imgindex);
    });
    $("#nxtbtn").live("click", function(e){
        clicks++;  //count clicks
        if(clicks === 1) {
            // clearTimeout(intervel);
            timer = setTimeout(function() {
                //nextImage(); //perform single-click action    
                clicks = 0;             //after action performed, reset counter
            }, DELAY);
        } else {
            clearTimeout(timer);    //prevent single-click action
            loopchange();
            //perform double-click action
            clicks = 0;             //after action performed, reset counter
        }
    }).live("dblclick", function(e){
        e.preventDefault();  //cancel system double-click event
    });
    $("#nxtbtn").bind("taphold", longTouch);
    $("#backbtns").bind("taphold", backLclick);
    $('#nxtbtn').bind('touchend',function(){
        clearTimeout(intervelnxt);
        touchval=false;
    });
    $('#backbtns').bind('touchend',function(){
        clearTimeout(intervelback);
        touchval=false;
    });
    $("#Tbtnclose").bind("touchstart",closets);
    $("#Tbtnclose").bind("touchend",closete);
    $("#Tbtnflipver").bind("touchstart",flipverts);
    $("#Tbtnflipver").bind("touchend",flipverte);
    $("#Tbtnfliphori").bind("touchstart",fliphorits);
    $("#Tbtnfliphori").bind("touchend",fliphorite);
    $("#Tbtnreset").bind("touchstart",resetts);
    $("#Tbtnreset").bind("touchend",resette);
    $("#Tbtnrotater").bind("touchstart",rotaterts);
    $("#Tbtnrotater").bind("touchend",rotaterte);
    $("#Tbtntextovly").bind("touchstart",textovlyts);
    $("#Tbtntextovly").bind("touchend",textovlyte);
    $("#Tbtnrotatel").bind("touchstart",rotatelts);
    $("#Tbtnrotatel").bind("touchend",rotatelte);
    //    $("#Tbtnfliphori").bind("touchstart",flipHtouchs);
    //    $("#Tbtnfliphori").bind("touchend",flipHtouche);
    $("#Tbtninvert").bind("touchstart",touchstinvrt);
    $("#Tbtninvert").bind("touchend",touchedinvrt);
    $("#btndefault").bind("touchstart",defaultbtn);
    $("#btnabdomen").bind("touchstart",abdomenbtn);
    $("#btnlung").bind("touchstart",lungbtn);
    $("#btnbrain").bind("touchstart",brainbtn);
    $("#btnbone").bind("touchstart",bonebtn);
    $("#btnheadneck").bind("touchstart",headneckbtn);
    function loopchange(){
        touchval=true;
        if(imgindex==imagearray.length-1){
            imgindex=0;
        }else{
            imgindex++;
        }
        imageChange(imgindex);
        if(tagData[0]['frametime']!=undefined){
            intervel=setTimeout(loopchange, loopval);
        }else if(tagData[0]['frametimevector']!=undefined){
            intervel=setTimeout(loopchange, looparrayval[imgindex]);
        }else{
            intervel=setTimeout(loopchange, 200);
        }
    }
    function nextImage(){
        if(imgindex==imagearray.length-1){
            imgindex=0;
        }else{
            imgindex++;
        }
        imageChange(imgindex);
    }
    $(window).bind('orientationchange', function() {
        orien=true;
      //  alert("...................."+dragtool);
       // $("#wrap").rasterDrag();
       if(!dragtool){
        $("#imgcanvas").rasterDrag();
       }
        alterpage();
        clearCanvasData();
    });
    function alterpage()
    {   
        resizewindow();
        overlaychange();
        if(orien){
            orien=false;
            loadImage(imagearray[imgindex]);
        }
        resizeTools();
    }
    
    function loadtmpimages(){
        objects=new Array();
        var stringData=localStorage.getItem("serieLoadArray");
        objects = stringData.split(",");
        //  console.log("..............frames...."+objects);
        if( !tagData[0]['numberofframes'] || tagData[0]['numberofframes'] == "Empty"){
            if(objects.length==1){
                $("#foottable").hide();
            }else{
                $("#foottable").show();
            }
            $("#imgSlider").attr('min',0);
            $("#imgSlider").attr('max',objects.length-1);
            $("#imgSlider").attr('step',1);
            loadfirstImage(objects[0]+"&framedata=Empty"+"&rows="+nativerow+"&coloumns="+nativecoloum);
        }else{ 
            if(tagData[0]['numberofframes']==1){
                $("#foottable").hide();
            }else{
                $("#foottable").show();
            }
            $("#imgSlider").attr('min',0);
            $("#imgSlider").attr('max',tagData[0]['numberofframes']);
            $("#imgSlider").attr('step',1);
            loadfirstImage(objects[0]+"&framedata="+1);
            // console.log("......"+tagData[0]['frametime']+"..frame time vector.."+tagData[0]['frametimevector']);
            if(tagData[0]['frametime']!=undefined){
                loopval=(1000/parseInt(tagData[0]['frametime']));
            }else if(tagData[0]['frametimevector']!=undefined){
                looparrayval=new Array();
                looparrayval=(tagData[0]['frametimevector']);
            }
        }
        $("#tmpimage").html("");
        if(!tagData[0]['numberofframes'] || tagData[0]['numberofframes'] == 'Empty'){
            for (var i=0; i < objects.length; i++) {
                $("#tmpimage").append("<img src='"+ _ori + objects[i]+"&framedata=Empty"+"'/>");
            } 
        }else{
            for (var i=0; i < parseInt(tagData[0]['numberofframes']); i++) {
                $("#tmpimage").append("<img src='"+ _ori + objects[0]+"&framedata="+i+"'/>");
            } 
        }
    }
    function init() {
        imagearray=new Array();
        $('#tmpimage').children('img').each(function () {
            imagearray.push($(this).get(0));
        });
    }
    function clearDrawCanvas(){
        var jcanvas = document.getElementById('overlayc');
        var cxt=jcanvas.getContext('2d');
        cxt.clearRect(0, 0, jcanvas.width, jcanvas.height);
    
    }
    function loadImage(imageObj){
        try{         
            var jcanvas = document.getElementById('imgcanvas');
            var cxt=jcanvas.getContext('2d');
            cxt.drawImage(imageObj, 0, 0,nativerow,nativecoloum);
             //alert("...load  image..."+jcanvas.width+".."+jcanvas.height);
        }catch(err){
            console.log("...."+err);		
        }
    }
    function loadfirstImage(src){
        $("#pixelspacingoly").html(tagData[imgindex]['pixelSpacing']);
        var wc=tagData[imgindex]['windowCenter'].split("|");
        var ww=tagData[imgindex]['windowWidth'].split("|");
        $("#wlwcoly").html("WC:"+wc[0]+":"+"WW:"+ww[0]);
        nativerow=tagData[imgindex]['nativeRows'];
        nativecoloum=tagData[imgindex]['nativeColumns'];
        $("#resolution").html("Matrix:"+""+tagData[imgindex]['nativeRows']+"x"+tagData[imgindex]['nativeColumns']);   
        var jcanvas = document.getElementById('imgcanvas');
        jcanvas.width  = nativerow;
        jcanvas.height = nativecoloum;
        var cxt=jcanvas.getContext('2d');
        var imageObj = new Image();
        // alert("native row:///"+nativerow+"..."+nativecoloum+"canrow:.."+canRow+"canColoums:."+canColumn);
        imageObj.onload = function() {
            // imageObj.height(500);
            cxt.drawImage(imageObj, 0, 0,nativerow,nativecoloum); 
        };
       
        imageObj.src =  _ori + src;
    }
    $("#imgSlider").change(function(event) {
        resetWl();
        imgindex = $(this).val() || 0;
        var img=(parseInt(imgindex)+1);
        if(!tagData[0]['numberofframes'] || tagData[0]['numberofframes']=="Empty" ){
            //imgindex=imgindex+1;
            $("#imageoly").html("Image:"+(img)+"/"+parseInt((bodypartcombine[2])));
            $("#thicknewwoly").html("T:"+" "+tagData[imgindex]['sliceThickness']+":mm"+" "+"L:"+Math.round(tagData[imgindex]['sliceLocation']));
            $("#pixelspacingoly").html(tagData[imgindex]['pixelSpacing']);
            var wc=tagData[imgindex]['windowCenter'].split("|");
            var ww=tagData[imgindex]['windowWidth'].split("|");
            $("#wlwcoly").html("WC:"+wc[0]+":"+"WW:"+ww[0]);
            nativerow=tagData[imgindex]['nativeRows'];
            nativecoloum=tagData[imgindex]['nativeColumns'];
            $("#resolution").html("Matrix:"+""+tagData[imgindex]['nativeRows']+"x"+tagData[imgindex]['nativeColumns']);       
            loadImage(imagearray[imgindex]);
        }
        else{
            $("#imageoly").html("Image:"+(img)+"/"+tagData[0]['numberofframes']);
            loadImage(imagearray[imgindex]);
        }
    });
    $("#popupclick1").click(function() {
        $("#slidecont").show();
    });
    //loadDicom(objects[imgindex]['wadourl']);
    
    $("#Tbtnflipver").click(flipV);
    $("#Tbtnfliphori").click(flipH);
    $("#Tbtnrotater").click(rotateR);
    $("#Tbtnrotatel").click(rotateL);
    // $("#Tbtntextovly").click(textOverlay);
    $("#Tbtninvert").click(inverthandler);
    $("#Tbtnline").click(lineTool);
    $("#Tbtncircle").click(circleTool);
    $("#Tbtnzoomdrag").click(zoomTool);
    $("#Tbtnreset").click(resettool);
    $("#Tbtnwindowlevel").click(wlwcControl);
    addEventInArray=function addEventInArray(eventName){
        // alert("event name :"+eventName);
        //        if($.inArray(eventName, toolEvent)!= -1){   
        //            removeArr(toolEvent, eventName);
        //            toolEvent.push(eventName);
        //        }else{
        //            toolEvent.push(eventName);
        //        }   
        toolEvent = [];
        toolEvent.push(eventName);
             
    }
    loadPixel= function  loadDicomImage(flag){
        if(flag){
            loadDicom(objects[imgindex],nativerow,nativecoloum);
        }
    }
    function resizeTools(){
        //toolEvent = [];
        var i=0;
        // for(i=0;i<toolEvent.length;i++)
        // {
        
        if(toolEvent[0]=='invert'){
            //alert("......");
            addEventInArray("invert");
            invertcanvasdata();
            closeToolbar();
        }
        if(toolEvent[0]=='flipV'){
            flipvertical();
        }
        if(toolEvent[0]=='flipH'){
            fliphori();
        }
        if(toolEvent[0]=='rotateR'){
            rotateright();
        }
        if(toolEvent[0]=='rotateL'){
            rotateleft();
        }
        if(toolEvent[0]=='line'){
            linetool();
        }
        if(toolEvent[0]=='circle'){
            circle();
        }
        if(toolEvent[0]=='zoom'){
            zoomdrag();
        }
        if(toolEvent[0]=='wlwc'){
            bindwlwc();
        }
    // console.log("......"+toolEvent[i]);
    // }
    }
    function removeAllTools(){
        toolEvent = [];
    }
    $("#footerSlider").children("div").css("background","rgba(255, 255, 255, 0)");
    $("#footerSlider").children("div").css("width","100%");
    function overlaychange(){
        OrientationFlag(true);
        if(localStorage.getItem("ipatname")!='undefined'){
            $("#patNameoly").html(localStorage.getItem("ipatname"));
        }
        if(sercombine[0]!='undefined'){
            $("#sexoly").html(""+" "+sercombine[0]);
        }
        if(localStorage.getItem("selectedmodality")!='undefined'){
            $("#modalityoly").html(""+" "+localStorage.getItem("selectedmodality"));
        }
        if(sercombine[1]!='undefined'){
            $("#studydateoly").html(sercombine[1]);
        }
        if(localStorage.getItem("istudydesc")!='undefined'){
            $("#studydesceoly").html(localStorage.getItem("istudydesc"));
        }
        if(bodypartcombine[1]!='undefined'){
            $("#seriesdesceoly").html(bodypartcombine[1]);
        }
        if(bodypartcombine[0]!='undefined'){
            $("#bodypartoly").html(bodypartcombine[0]);
        }
        if(bodypartcombine[2]!='undefined'){
            //$("#thicknewwoly").html(bodypartcombine[2]+":mm");
            $("#thicknewwoly").html("0:mm");
        }
        if(imgindex!='undefined' && parseInt((bodypartcombine[2]))!='undefined'){
            //$("#imageoly").html("Image:"+((imgindex+1))+"/"+parseInt((bodypartcombine[2])));
            //imgindex=$(this).val ();
            $("#imageoly").html("Image:"+(imgindex+1)+"/"+parseInt((bodypartcombine[2])));
           // $("#thicknewwoly").html("T:"+" "+tagData[imgindex]['sliceThickness']+":mm"+" "+"L:"+Math.round(tagData[imgindex]['sliceLocation']));
            $("#pixelspacingoly").html(tagData[imgindex]['pixelSpacing']);
            var wc=tagData[imgindex]['windowCenter'].split("|");
            var ww=tagData[imgindex]['windowWidth'].split("|");
            $("#wlwcoly").html("WC:"+wc[0]+":"+"WW:"+ww[0]);            
            $("#resolution").html("Matrix:"+""+tagData[imgindex]['nativeRows']+"x"+tagData[imgindex]['nativeColumns']);       
        }
    }
    function imageChange(imgindex){
        //  $("#imgSlider").val(imgindex).slider("refresh");
        resetWl();
        var img=(parseInt(imgindex)+1);
        $("#imgSlider").val(imgindex).slider("refresh");
        if(!tagData[0]['numberofframes'] || tagData[0]['numberofframes']=="Empty" ){
            //imgindex=imgindex+1;
            $("#imageoly").html("Image:"+(img)+"/"+parseInt((bodypartcombine[2])));
            $("#thicknewwoly").html("T:"+" "+tagData[imgindex]['sliceThickness']+":mm"+" "+"L:"+Math.round(tagData[imgindex]['sliceLocation']));
            $("#pixelspacingoly").html(tagData[imgindex]['pixelSpacing']);
            var wc=tagData[imgindex]['windowCenter'].split("|");
            var ww=tagData[imgindex]['windowWidth'].split("|");
            $("#wlwcoly").html("WC:"+wc[0]+":"+"WW:"+ww[0]);
            nativerow=tagData[imgindex]['nativeRows'];
            nativecoloum=tagData[imgindex]['nativeColumns'];
            $("#resolution").html("Matrix:"+""+tagData[imgindex]['nativeRows']+"x"+tagData[imgindex]['nativeColumns']);       
            loadImage(imagearray[imgindex]);
        }
        else{
            $("#imageoly").html("Image:"+(img)+"/"+tagData[0]['numberofframes']);
            loadImage(imagearray[imgindex]);
        }
    }
    imageChanger = imageChange;
});