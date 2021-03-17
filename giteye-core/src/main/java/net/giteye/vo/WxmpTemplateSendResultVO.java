package net.giteye.vo;

/**
 * 微信公众号模板消息发送返回VO
 *
 * @author Bryan.Zhang
 * @since 2021/3/11
 */
public class WxmpTemplateSendResultVO extends BaseVO{

    private Integer errcode;

    private String errmsg;

    private Long msgid;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Long getMsgid() {
        return msgid;
    }

    public void setMsgid(Long msgid) {
        this.msgid = msgid;
    }
}
