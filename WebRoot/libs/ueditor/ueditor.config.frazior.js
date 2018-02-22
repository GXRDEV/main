UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
UE.Editor.prototype.getActionUrl = function(action) {
    if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadimage') {
        return _burl + 'imageupload.do';
    } else if (action == 'uploadvideo') {
        return _burl + 'imageupload.do';
    } else {
        return this._bkGetActionUrl.call(this, action);
    }
}