package net.giteye.client.wxmp.interceptor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import net.giteye.vo.WxmpAccessTokenVO;

import java.util.Date;

/**
 * 用于获取微信公众号AccessToken所用的拦截器
 * 里面缓存了上一个AccessToken，如果token没过期，则用上一个token，如果过期了，则去请求新的token
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public class WxmpAccessTokenInterceptor implements Interceptor<Object> {

    private volatile WxmpAccessTokenVO cacheAccessToken;

    @Override
    public boolean beforeExecute(ForestRequest request) {
        if (ObjectUtil.isNull(cacheAccessToken)){
            return true;
        }
        Date expiresDate = DateUtil.offsetSecond(cacheAccessToken.getCurrentDate(),cacheAccessToken.getExpiresIn());
        if (DateUtil.compare(expiresDate,new Date()) < 0){
            return true;
        }else{
            request.methodReturn(cacheAccessToken);
            return false;
        }
    }

    @Override
    public void afterExecute(ForestRequest request, ForestResponse response) {
        WxmpAccessTokenVO wxmpAccessTokenVO = (WxmpAccessTokenVO)response.getResult();
        wxmpAccessTokenVO.setCurrentDate(new Date());
        cacheAccessToken = wxmpAccessTokenVO;
    }

    @Override
    public void onError(ForestRuntimeException e, ForestRequest forestRequest, ForestResponse forestResponse) {

    }

    @Override
    public void onSuccess(Object o, ForestRequest forestRequest, ForestResponse forestResponse) {

    }
}
