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

import java.util.Map;

public class GiteeMetricsGetInterceptor implements Interceptor<Object> {

    @Override
    public void onError(ForestRuntimeException e, ForestRequest forestRequest, ForestResponse forestResponse) {

    }

    @Override
    public void onSuccess(Object o, ForestRequest forestRequest, ForestResponse forestResponse) {
        String jsonData = forestResponse.getContent();
        Map<String, ?> map = JSON.parseObject(jsonData, new TypeReference<Map<String, ?>>() {});
        GiteeMetricsVO giteeMetricsVO = BeanUtil.mapToBean(map, GiteeMetricsVO.class, true, CopyOptions.create());

        //处理json中的子项数据
        Map<String, ?> repo = (Map<String, ?>) map.get("repo");
        giteeMetricsVO.setRepoId(new Long(repo.get("id").toString()));
        giteeMetricsVO.setFullName((String) repo.get("full_name"));
        giteeMetricsVO.setOwner((String) repo.get("path"));
        giteeMetricsVO.setDescription((String) repo.get("description"));
        giteeMetricsVO.setHtmlUrl((String) repo.get("html_url"));
        giteeMetricsVO.setSshUrl((String) repo.get("ssh_url"));
        Map<String, ?> namespace = (Map<String, ?>) repo.get("namespace");
        giteeMetricsVO.setRepoName((String) namespace.get("path"));

        forestResponse.setResult(giteeMetricsVO);
    }
}
