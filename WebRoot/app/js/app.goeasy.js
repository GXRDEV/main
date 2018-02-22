loadScript('https://cdn.goeasy.io/goeasy.js', function () {
    var GOEASY = new GoEasy({
        appkey: '78028e7e-edcc-4524-b56b-45639785a53a',
        onConnected: function(){
            debugState && console.log('---GoEasy加载完成----' + (new Date()).Format('yyyy-MM-dd hh:mm:ss'));
        }
    });
    window.GO_SUB = function(channelId,msgFuc) {
        GOEASY.subscribe({
            channel: channelId,
            onMessage: msgFuc,
            onSuccess: function(){
                debugState && console.log('监听【'+ channelId +'】成功。' + (new Date()).Format('yyyy-MM-dd hh:mm:ss'));
            },
            onFailed: function(){
                debugState && console.log('监听【'+ channelId +'】失败。' + (new Date()).Format('yyyy-MM-dd hh:mm:ss'));
            }
        });
    };
    window.GO_PUB = function(channelId, msg){
        GOEASY.publish({
            channel: channelId,
            message: msg,
            onSuccess: function(){
                debugState && console.log('发布【'+ channelId +'】成功。' + (new Date()).Format('yyyy-MM-dd hh:mm:ss'));
            }
        });
    };    
});
