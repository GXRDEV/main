var _ori = location.origin + '/';
$(document).bind("mobileinit", function () {
    $.mobile.defaultPageTransition = "slidefade";
     
});
function getParam(name) {
    name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
    var regexS = "[\\?&]" + name + "=([^&#]*)";
    var regex = new RegExp(regexS);
    var results = regex.exec(window.location.href);
    if (results == null)
        return "";
    else
        return results[1];
}
$(document).ready(function() {
    $.mobile.showPageLoadingMsg();

var seriesLoad;
    var  patientid = getParam('pid');
    var studyid = getParam('sid');
    if(studyid==undefined||studyid==null){
        window.location='home.html';
    }
    var patientArray = patientid.split(",");
    $("#btnback").click(function() {
        $(this).addClass( $.mobile.activeBtnClass );         
        window.location='patient.html';        
    });
    try{
        $.urlParam = function(name){ 
            var results = new RegExp('[\\?&]' + name + '=([^&#]*)').exec(window.location.href);
            return results[1] || 0 ;
        }
    }catch(err){
        alert(err);
    }
    $('#headingseries').html(patientArray[1]);
    localStorage.setItem("ipatname",patientArray[1]);
    
    var arraylistvale;
    $.post(_ori + "series", {
        "PatientID":patientArray[0],
        "StudyID":studyid,
        "ae":localStorage.getItem("aetitle"),
        "host":localStorage.getItem("hostname"),
        "port":localStorage.getItem("port"),
        "wado":localStorage.getItem("wado")
    },function(data) {  
        obj = $.parseJSON(data);
        localStorage.setItem("json", data);
        seriesList=new Array();
        arraylistvale=new Array();
          seriesLoad=new Array();
        $('#serieslist').html('');
        var i=0;
        $.each(obj, function(i, row) {
            var valuurl;
            seriesList.push(row);
            viewerarray=this['url'];
            var outofnull=new Array();
           // console.log("----"+row['url'][0]+'&frameNumber=1&rows=82&coloums=82'+"..........~~~~~~~~`"+row['url'].length);
            for(var i=0;i<row['url'].length;i++){
                if(row['url'][i]!=null){
                    outofnull.push(row['url'][i]);
                }
            }
            arraylistvale.push(outofnull);
          //  console.log(",,,,,,,,,,,,,,,,,"+outofnull);
            
            if(row['url'][0]==null){
                valuurl=row['url'];
            }else{
                 valuurl=row['url'][0];
            }
            
            seriesLoad.push(row['url']);
            var  iurl= _ori + outofnull[0];
            $('#serieslist').append('<li id=serieslistvalue'+i +' data-theme="a"  ibodypart='+row['bodyPart']+','+row['seriesDesc']+' >'
                +'<a   href="" data-transition="slide">'
                +'<img  id="imgstudy" style="float:left;"  src='+iurl+'&frameNumber=1&rows=82&coloums=82'+'>'
                +
                //                    '<label style="font: bold 14px courier !important;">'+ "Series no:"+this['seriesNumber']+'</label>'+'<br>'+
                '<label style="float:left;font: bold 15px helvetica;color:#AAAAAA;">'+ ""+row['seriesDesc']+'</label>'+'<br>'+
                '<label style="float:left;font:  14px helvetica;color: #AAAAAA">'+row['modality']+'</label>'+'<br>'+
                //                    '<label style="font: bold 14px courier !important;">'+ "Body Part:"+this['bodyPart']+'</label>'+'<br>'+
                '<label style="float:left;font: normal 14px helvetica ;color: #AAAAAA">'+ "("+row['totalInstances']+" "+"images)"+'</label>'+
                
                '</a>'+'</li>').listview('refresh'); 
        });
        i++;
        $('#serieslist').children('li').on('click', function () {
            $(this).addClass( $.mobile.activeBtnClass );
            var patient=$(this).index();
            localStorage.setItem("ibodypart", seriesList[patient]['bodyPart']+','+seriesList[patient]['seriesDesc']+','+seriesList[patient]['totalInstances']+','+patientArray[2]);
            seletedindex=$(this).index();
            localStorage.setItem("patid", seriesList[patient]['patientId']);
            localStorage.setItem("studyid", seriesList[patient]['studyUID']);
            localStorage.setItem("seriesid",  seriesList[patient]['seriesUID']);
            localStorage.setItem("seletedseries", seletedindex);
            localStorage.setItem("selectedmodality", seriesList[patient]['modality']);
            var arry=new Array();
            //arry=seriesList[patient]['url'];
                      arry=arraylistvale[patient];

            localStorage.setItem("serieLoadArray",arry);
          //  console.log("....."+seriesList[patient]['url'].length);
           // jQuery.isArray(seriesList[patient]['url'])
            window.location="viewer.html";
        });
        $.mobile.hidePageLoadingMsg();
    });


});