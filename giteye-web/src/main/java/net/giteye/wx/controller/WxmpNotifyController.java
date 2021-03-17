package net.giteye.wx.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.google.common.collect.Lists;
import net.giteye.wx.crypt.WXBizMsgCrypt;
import net.giteye.wx.enums.WxmpMsgEnum;
import net.giteye.wx.notify.processor.msg.WxmpMsgProcessorManager;
import net.giteye.property.GeProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/notify")
public class WxmpNotifyController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private GeProperty geProperty;

    @Resource
    private Environment environment;

    //以下是用于开通微信公众号验证的代码
//    @ResponseBody
//    @RequestMapping(value = "/wxmp", method = RequestMethod.GET)
    public String wxNotify(@RequestParam("signature") String signature,
                           @RequestParam("timestamp") String timestamp,
                           @RequestParam("nonce") String nonce,
                           @RequestParam("echostr") String echostr) {

        List<String> list = Lists.newArrayList(geProperty.getWxmpToken(), timestamp, nonce);
        list.sort(String::compareTo);
        String str = String.join("", list);
        String mySignature = SecureUtil.sha1(str);
        log.info("mySignature:{}", mySignature);
        if (signature.equals(mySignature)) {
            log.info("signature验证成功！");
            return echostr;
        } else {
            log.info("signature验证失败！");
            return "";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/wxmp", method = RequestMethod.POST)
    public String wxNotify(@RequestParam("signature") String signature,
                           @RequestParam("timestamp") String timestamp,
                           @RequestParam("nonce") String nonce,
                           @RequestParam("openid") String openid,
                           @RequestParam(name = "msg_signature", required = false) String msgSignature,
                           @RequestBody String body) {
        log.info("微信公众号接受到的通知Body为:{}", body);
        String decryptMsg;
        try {
            String env = environment.getActiveProfiles()[0];
            if (env.equals("prod")){
                WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(geProperty.getWxmpToken(),
                        geProperty.getWxmpEncodingAesKey(),
                        geProperty.getWxmpAppId());
                decryptMsg = wxBizMsgCrypt.decryptMsg(msgSignature, timestamp, nonce, body);
                log.info("微信公众号解密结果为:{}", decryptMsg);
            }else{
                decryptMsg = body;
            }
        } catch (Exception e) {
            return "error";
        }

        Map<String, Object> bodyMap = XmlUtil.xmlToMap(decryptMsg);
        WxmpMsgEnum wxmpMsgEnum = WxmpMsgEnum.getEnumByCode(bodyMap.get("MsgType").toString().toLowerCase());
        if (ObjectUtil.isNull(wxmpMsgEnum)) {
            wxmpMsgEnum = WxmpMsgEnum.OTHER;
        }
        return WxmpMsgProcessorManager.loadInstance().getProcessor(wxmpMsgEnum).process(bodyMap);
    }
}
