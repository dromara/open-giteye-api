package net.giteye.client.gitee.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.google.common.collect.Lists;
import net.giteye.vo.GiteeRepoVO;

import java.util.List;
import java.util.Map;

public class GiteeReposGetInterceptor implements Interceptor<Object> {

    @Override
    public void onError(ForestRuntimeException e, ForestRequest forestRequest, ForestResponse forestResponse) {

    }

    @Override
    public void onSuccess(Object o, ForestRequest forestRequest, ForestResponse forestResponse) {
        String jsonData = forestResponse.getContent();
        List<Map<String, ?>> list = JSON.parseObject(jsonData, new TypeReference<List<Map<String, ?>>>(){});

        List<GiteeRepoVO> resultList = Lists.newArrayList();
        GiteeRepoVO item;
        Map<String, Object> namespace;
        String path;
        for (Map<String, ?> map : list){
            item = BeanUtil.mapToBean(map, GiteeRepoVO.class, true, CopyOptions.create());

            //处理namespace.path
            namespace = (Map<String, Object>) map.get("namespace");
            path = (String) namespace.get("path");
            item.setOwner(path);

            resultList.add(item);
        }
        forestResponse.setResult(resultList);
    }
}
