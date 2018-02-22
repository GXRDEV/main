package com.tspeiz.modules.home;

/**
 * Created by kx on 2017/11/29.
 */
public class RequestBean {
    private String LOGICNAME;
    private String TOKEN;
    private String MESSAGEID;
    private Object DATAS;

    public String getLOGICNAME() {
        return LOGICNAME;
    }

    public void setLOGICNAME(String LOGICNAME) {
        this.LOGICNAME = LOGICNAME;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public String getMESSAGEID() {
        return MESSAGEID;
    }

    public void setMESSAGEID(String MESSAGEID) {
        this.MESSAGEID = MESSAGEID;
    }

    public Object getDATAS() {
        return DATAS;
    }

    public void setDATAS(Object DATAS) {
        this.DATAS = DATAS;
    }
}
