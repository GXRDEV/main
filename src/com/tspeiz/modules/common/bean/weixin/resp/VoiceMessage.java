package com.tspeiz.modules.common.bean.weixin.resp;
/**
 * 
 * @author heyongb
 *
 */
public class VoiceMessage extends BaseMessage {
	private Voice Voice;

	public Voice getVoice() {
		return Voice;
	}

	public void setVoice(Voice voice) {
		Voice = voice;
	}
}
