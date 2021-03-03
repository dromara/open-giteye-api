package net.giteye.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.giteye.vo.UserInfoVO;
import net.giteye.vo.WxmpAccessTokenVO;
import net.giteye.client.wxmp.WxmpClient;
import net.giteye.db.entity.ClientLogin;
import net.giteye.db.entity.UserInfo;
import net.giteye.db.enums.ClientLoginStatusEnum;
import net.giteye.db.service.ClientLoginService;
import net.giteye.db.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 授权类业务
 *
 * @author Bryan.Zhang
 * @since 2021/1/26
 */
@Component
@Transactional
public class AuthBizDomain {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private final static String SCENE = "giteye";

    @Resource
    private WxmpClient wxmpClient;

    @Resource
    private ClientLoginService clientLoginService;

    @Resource
    private UserInfoService userInfoService;

    public String generateQRCode(String clientId) {
        WxmpAccessTokenVO token = wxmpClient.getAccessToken();
        return wxmpClient.generateQRCode(token.getAccessToken(), StrUtil.format("{}-{}", SCENE, clientId));
    }

    public UserInfoVO pollClientLogin(String clientId) {
        QueryWrapper<ClientLogin> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("client_id", clientId))
                .and(wrapper -> wrapper.eq("status", ClientLoginStatusEnum.VALID.getCode())).orderByDesc("id");
        ClientLogin clientLogin = clientLoginService.getOne(queryWrapper);

        //如果不存在这个clientLogin记录，则返回null
        if(ObjectUtil.isNull(clientLogin)){
            return null;
        }

        UserInfo userInfo = userInfoService.getById(clientLogin.getUserId());
        UserInfoVO userInfoVO = BeanUtil.copyProperties(userInfo, UserInfoVO.class);

        //更新clientLogin的状态，这里为什么更新多个，是为了防止有多条有效记录的出现
        ClientLogin setEntity = new ClientLogin();
        setEntity.setStatus(ClientLoginStatusEnum.INVALID.getCode());
        clientLoginService.update(setEntity, queryWrapper);

        return userInfoVO;
    }
}
