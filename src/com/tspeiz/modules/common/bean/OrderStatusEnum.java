package com.tspeiz.modules.common.bean;

/**
 * Created by 51zjh on 2016/8/5.
 */
public enum OrderStatusEnum {

    //图文
    TUWNE_RUNNING(1,4,"进行中"),
    //TUWNE_FINISHED(1,0,"已完成"),
    TUWNE_CLOSED(1,0,"已关闭"),
    TUWNE_CANCEL(1,5,"已取消"),
    TUWNE_OVERTIME(1,13,"超时关闭"),

    //电话
    TEL_WAIT(2,0,"等待确认"),
    TEL_AGREE(2,1,"医生确认"),
    TEL_REFUSE(2,2,"医生拒绝"),
   // TEL_FINISHED(2,0,"已完成"),
    TEL_CLOSED(2,3,"已关闭"),
    TEL_ABNORMAL(2,4,"异常关闭"),
   // TEL_CANCEL(2,0,"已取消")，

    //远程门诊
    VEDIO_WAIT_VISIT1(4,1,"待就诊"),
    VEDIO_WAIT_ORDER2(4,2,"待二次下单"),
    VEDIO_WAIT_ORDER3(4,3,"待三次下单"),
    VEDIO_WAIT_VISIT2(4,4,"待二次就诊"),
    VEDIO_WAIT_VISIT3(4,5,"待三次就诊"),
    VEDIO_FINISHED(4,10,"就诊完成"),
    VEDIO_USER_CANCEL(4,20,"用户取消"),
    VEDIO_EXPERT_CANCEL(4,21,"专家取消"),
    
    //专家咨询
    SPECIAL_RUNNING(5,4,"进行中"),
    SPECIAL_CLOSED(5,0,"已关闭"),
    SPECIAL_CANCEL(5,5,"已取消"),
    SPECIAL_OVERTIME(5,13,"超时关闭"),
    SPECIAL_DOC_AGREE(5,1,"医生同意"),
    SPECIAL_DOC_UNAREE(5,2,"医生拒绝"),

    //支付状态
    PAY_FREE(11,4,"免支付"),
    PAY_PAID(11,1,"已支付"),
    PAY_WAIT(11,4,"待支付"),
    
    D2P_ORDER_VALUE_TUWEN(20,6,"图文问诊"),
    D2P_ORDER_VALUE_TEL(20,7,"电话问诊"),
    D2P_ORDER_VALUE_REPORT(20,8,"患者报道问诊"),
    D2P_ORDER_VALUE_FAST(20,9,"快速问诊"),
    D2P_ORDER_VALUE_WARM(20,13,"送心意"),
    
    D2P_ORDER_STATUS_WAIT(21,10,"待接诊"),
    D2P_ORDER_STATUS_RUNNING(21,20,"进行中"),
    D2P_ORDER_STATUS_RERUNNING(21,25,"待复诊"),
    D2P_ORDER_STATUS_OUT(21,30,"已退诊"),
    D2P_ORDER_STATUS_COMPLETED(21,40,"已完成"),
    D2P_ORDER_STATUS_CANCELED(21,50,"已取消"),
    SYSTEM_CLOSED_TYPE(22,6,"系统类型"),
    SYSTEM_CLOSED_ID(22,13590,"系统ID"),
    
    YLT_APP_AUDITTING(23,0,"待审核"),
    YLT_APP_TONGGUO(23,1,"通过"),
    YLT_APP_FAILD(23,-1,"不通过"),
    
    YLT_INV_WAIT(24,0,"等待医院确认"),
    YLT_INV_REFULSE(24,-1,"拒绝加入"),
    YLT_INV_TONGYI(24,1,"同意加入"),
    VEDIO_DURATION(25,20,"会诊时长");
    
    

    private int type;
    private int key;
    private String value;

    private  OrderStatusEnum(int type,int key,String value){
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public static  String getStatusValue(int type, int key){
        String value = "";
        for(OrderStatusEnum item : OrderStatusEnum.values()){
            if(item.type == type ){
                if (item.key == key){
                    value = item.value;
                    break;
                }
            }
        }
        return value;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setKey(int key) {
        this.key = key;
    }
    public int getKey() {
        return key;
    }

    public String getValue( )
    {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
