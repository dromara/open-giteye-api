package net.giteye.domain;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.giteye.vo.WxmpAccessTokenVO;
import net.giteye.client.wxmp.WxmpClient;
import net.giteye.db.entity.ClientLogin;
import net.giteye.db.entity.UserInfo;
import net.giteye.db.enums.ClientLoginStatusEnum;
import net.giteye.db.enums.UserStatusEnum;
import net.giteye.db.enums.UserSubscribeStatusEnum;
import net.giteye.db.service.ClientLoginService;
import net.giteye.db.service.UserInfoService;
import net.giteye.exception.GeErrorCode;
import net.giteye.exception.GeException;
import net.giteye.vo.WxUserVO;
import net.giteye.vo.WxmpStarTemplateSendVO;
import net.giteye.vo.WxmpTemplateSendResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 微信公众号业务
 *
 * @author Bryan.Zhang
 * @since 2021/1/21
 */
@Component
@Transactional
public class WxmpBizDomain {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private WxmpClient wxmpClient;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ClientLoginService clientLoginService;

    public void login(String openId, String qrscene) {
        //获取微信用户的信息
        WxmpAccessTokenVO tokenVO = wxmpClient.getAccessToken();
        WxUserVO wxUserVO = wxmpClient.getWxUser(tokenVO.getAccessToken(), openId);

        //qrscene包含2部分，clientId和scene
        String scene = qrscene.split("-")[0];
        String clientId = qrscene.split("-")[1];
        wxUserVO.setQrScene(scene);

        if (ObjectUtil.isNull(wxUserVO) || StrUtil.isBlank(wxUserVO.getOpenid())) {
            throw new GeException(GeErrorCode.WX_CANNOT_GET_USER);
        }

        //根据openid查询此用户是否存在
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("wx_open_id", wxUserVO.getOpenid()));
        UserInfo userInfo = userInfoService.getOne(queryWrapper);

        //如果存在用户，则更新用户信息，如果不存在，则新增
        UserInfo buildUserInfo = buildUserInfo(wxUserVO);
        if (ObjectUtil.isNotNull(userInfo)) {
            //如果用户状态为不可用，则抛错
            if (userInfo.getStatus().equals(UserStatusEnum.INVALID.getCode())) {
                throw new GeException(GeErrorCode.WX_USER_STATUS_INVALID);
            }

            buildUserInfo.setId(userInfo.getId());
            userInfoService.updateById(buildUserInfo);
        } else {
            userInfoService.save(buildUserInfo);
        }

        //判断在client_login表里有没有相应的待登陆的记录，如果没有，则新增一条
        QueryWrapper<ClientLogin> clWrapper = new QueryWrapper<>();
        clWrapper.and(wrapper -> wrapper.eq("client_id", clientId))
                .and(wrapper -> wrapper.eq("status", ClientLoginStatusEnum.VALID.getCode()));
        int clCnt = clientLoginService.count(clWrapper);
        if (clCnt == 0){
            ClientLogin record = new ClientLogin();
            record.setClientId(clientId);
            record.setStatus(ClientLoginStatusEnum.VALID.getCode());
            record.setUserId(buildUserInfo.getId());
            clientLoginService.save(record);
        }
    }

    public void unsubscribe(String openId) {
        //根据openid查询此用户是否存在
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("wx_open_id", openId));
        UserInfo userInfo = userInfoService.getOne(queryWrapper);

        //查到用户的时候，把is_subscribe更新成0
        if (ObjectUtil.isNotNull(userInfo)) {
            userInfo.setIsSubscribe(UserSubscribeStatusEnum.UNSUBSCRIBE.getCode());
            userInfoService.updateById(userInfo);
        }
    }

    public void sendStarWxmpTemplate(WxmpStarTemplateSendVO wxmpStarTemplateSendVO){
        WxmpAccessTokenVO tokenVO = wxmpClient.getAccessToken();
        WxmpTemplateSendResultVO resultVO = wxmpClient.sendStarTemplateMsg(tokenVO.getAccessToken(), wxmpStarTemplateSendVO);
        if (resultVO.getErrcode() != 0){
            throw new GeException(GeErrorCode.WX_TEMPLATE_MSG_SEND_ERROR);
        }
    }

    private UserInfo buildUserInfo(WxUserVO wxUserVO) {
        UserInfo userInfo = new UserInfo();
        userInfo.setWxOpenId(wxUserVO.getOpenid());
        userInfo.setWxUnionId(wxUserVO.getUnionid());
        userInfo.setWxNickName(wxUserVO.getNickname());
        userInfo.setSex(wxUserVO.getSex());
        userInfo.setLanguage(wxUserVO.getLanguage());
        userInfo.setCity(wxUserVO.getCity());
        userInfo.setCountry(wxUserVO.getCountry());
        userInfo.setHeadImgUrl(wxUserVO.getHeadimgurl());
        userInfo.setWxSubscribeTime(DateUtil.date(wxUserVO.getSubscribeTime() * 1000L));
        userInfo.setSubscribeScene(wxUserVO.getSubscribeScene());
        userInfo.setQrScene(wxUserVO.getQrScene());
        userInfo.setCreateTime(DateUtil.date());
        userInfo.setIsSubscribe(wxUserVO.getSubscribe());
        userInfo.setStatus(UserStatusEnum.VALID.getCode());
        return userInfo;
    }
}
