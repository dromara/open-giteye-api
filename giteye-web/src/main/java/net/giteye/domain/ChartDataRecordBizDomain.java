package net.giteye.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.giteye.db.entity.ChartDataRecord;
import net.giteye.db.entity.ChartRecord;
import net.giteye.db.service.ChartDataRecordService;
import net.giteye.enums.ChartRecordStatus;
import net.giteye.vo.ChartDataRecordVO;
import net.giteye.vo.ChartRecordVO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class ChartDataRecordBizDomain {

    @Resource
    private ChartDataRecordService chartDataRecordService;

    @Resource
    private ChartRecordBizDomain chartRecordBizDomain;

    public ChartDataRecordVO getChartDataRecordByChartRecord(ChartRecordVO chartRecordVO){
        QueryWrapper<ChartDataRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("user_id", chartRecordVO.getUserId()))
                .and(wrapper -> wrapper.eq("git_site", chartRecordVO.getGitSite()))
                .and(wrapper -> wrapper.eq("git_username", chartRecordVO.getGitUsername()))
                .and(wrapper -> wrapper.eq("repo_name", chartRecordVO.getRepoName()))
                .and(wrapper -> wrapper.eq("metrics_type", chartRecordVO.getMetricsType()));
        ChartDataRecord chartDataRecord = chartDataRecordService.getOne(queryWrapper, false);
        if (ObjectUtil.isNull(chartDataRecord)) {
            return null;
        }
        return BeanUtil.copyProperties(chartDataRecord, ChartDataRecordVO.class);
    }

    public List<ChartDataRecordVO> getCanIncrementChartRecord() {
        QueryWrapper<ChartDataRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("support_increment", 1));
        List<ChartDataRecord> chartDataRecordList = chartDataRecordService.list(queryWrapper);
        List<ChartDataRecordVO> resultList = new ArrayList<>();
        for (ChartDataRecord item : chartDataRecordList) {
            resultList.add(BeanUtil.copyProperties(item, ChartDataRecordVO.class));
        }
        return resultList;
    }

    public List<ChartDataRecordVO> getCanRefreshChartRecord() {
        List<ChartDataRecord> chartDataRecordList = chartDataRecordService.list();
        List<ChartDataRecordVO> resultList = new ArrayList<>();
        for (ChartDataRecord chartDataRecord : chartDataRecordList) {
            resultList.add(BeanUtil.copyProperties(chartDataRecord, ChartDataRecordVO.class));
        }
        return resultList;
    }
}
