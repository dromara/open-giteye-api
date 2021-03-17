package net.giteye.client.gitee.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.google.common.collect.Lists;
import net.giteye.vo.GiteeRepoVO;

import java.util.HashMap;
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
        CopyOptions copyOptions = CopyOptions.create();
        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put("public", "open");
        fieldMap.put("private", "hide");
        copyOptions.setFieldMapping(fieldMap);
        for (Map<String, ?> map : list){
            item = BeanUtil.mapToBean(map, GiteeRepoVO.class, true, copyOptions);

            //处理namespace.path
            namespace = (Map<String, Object>) map.get("namespace");
            path = (String) namespace.get("path");
            item.setOwner(path);

            resultList.add(item);
        }
        forestResponse.setResult(resultList);
    }
}
