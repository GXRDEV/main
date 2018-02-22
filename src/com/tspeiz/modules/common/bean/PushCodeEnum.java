package com.tspeiz.modules.common.bean;

/**
 * 推送编码枚举
 * 
 * @author liqi
 * 
 */
public enum PushCodeEnum {
	Logout("退出登录", 1), CheckPass("审核通过", 2), CheckNotPass("审核未通过", 3), GotoOrderDetail("跳转订单详情", 21),
	EditAccountDetail("个人信息修改通知", 51), 
	
	NewOrder("收到新的订单", 101), 
	CancelOrder("订单取消", 103), 
	CloseOrder("订单关闭", 104), 
	AgreeOrder("接诊通知", 105), 
	RefuseOrder("退诊通知", 106),
	PaySuccess("支付成功通知", 107),
	ReviewOrder("复诊通知", 108),
	
	ChangeCase("修改病历通知", 201),
	ChangeOrderTime("修改订单时间通知", 202),
	ChangeSpecialist("修改专家通知", 203),
	ChangeDoctor("修改医生通知", 204),
	Replaced("参与者被替换通知", 205),
	BeforeOrderBegin("订单即将开始", 206),
	GrabOrder("快速问诊抢单通知", 207),
	CreateAssociatedOrder("生成关联订单信息通知", 208),
	SaveReview("保存复诊内容通知", 209),
	ESTIMATE("评价通知",210),
	LEGER_ACCOUNT("入账通知",211),
	SAVE_EDIT_REPORT("保存会诊结果",212),
	
	GroupApply("群组加入邀请（申请）",301),
	AgreeGroupApply("同意群组邀请（申请）",302),
	GroupRemoveMember("团队移除成员",303),
	APPLY_JOIN("申请加入群组",304),
	AGREE_APPLY("同意申请",305),
	OUT_GROUP("退出群组",306),
	FriendApply("加好友申请",311),
	AgreeFriendApply("同意加好友申请",312),
	AgreeDocTeamApply("团队审核通知",402),
	AgreeDocSval("医生下线",4);
	public static  String getStatusValue(int code){
        String value = "";
        for(PushCodeEnum item : PushCodeEnum.values()){
            if(item.code == code ){
            	 value = item.name; 
            }
        }
        return value;
    }

	private String name;
	private int code;

	private PushCodeEnum(String name, int code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
