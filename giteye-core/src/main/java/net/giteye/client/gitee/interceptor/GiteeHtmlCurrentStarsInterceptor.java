package net.giteye.client.gitee.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import net.giteye.exception.GeErrorCode;
import net.giteye.exception.GeException;
import net.giteye.vo.GiteeMetricsVO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

public class GiteeHtmlCurrentStarsInterceptor implements Interceptor<Object> {
    @Override
    public void onError(ForestRuntimeException e, ForestRequest forestRequest, ForestResponse forestResponse) {

    }

    @Override
    public void onSuccess(Object o, ForestRequest forestRequest, ForestResponse forestResponse) {
        String jsonData = forestResponse.getContent();
        Map<String, ?> map = JSON.parseObject(jsonData, new TypeReference<Map<String, ?>>() {
        });

        List<List<Object>> list = (List<List<Object>>) map.get("data");
        if (CollectionUtils.isNotEmpty(list)) {
            Integer currentStars = (Integer) list.get(list.size() - 1).get(1);
            forestResponse.setResult(currentStars);
        }else{
            throw new GeException(GeErrorCode.GITEE_HTML_CURRENT_STAR_ERROR);
        }
    }
}
