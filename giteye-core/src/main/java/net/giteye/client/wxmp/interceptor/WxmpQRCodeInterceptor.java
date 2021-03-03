package net.giteye.client.wxmp.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;

public class WxmpQRCodeInterceptor implements Interceptor<Object> {

    @Override
    public void afterExecute(ForestRequest request, ForestResponse response) {
        String returnJson = response.readAsString();
        JSONObject jsonObject = JSON.parseObject(returnJson);
        String ticket = jsonObject.getString("ticket");
        String result = StrUtil.format("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket={}",ticket);
        request.methodReturn(result);
    }

    @Override
    public void onError(ForestRuntimeException e, ForestRequest forestRequest, ForestResponse forestResponse) {

    }

    @Override
    public void onSuccess(Object o, ForestRequest forestRequest, ForestResponse forestResponse) {

    }
}
