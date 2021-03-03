package net.giteye.api.controller;

import net.giteye.api.resp.ApiResp;
import net.giteye.charts.hchrome.HeadlessChromeImageGenerator;
import net.giteye.domain.ChartImageBizDomain;
import net.giteye.domain.ChartRecordBizDomain;
import net.giteye.vo.ChartImageGenerateResultVO;
import net.giteye.vo.ChartRecordVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gongjun[dt_flys@hotmail.com]
 * @since 2021-02-03 23:37
 */
@RestController
@RequestMapping(path = "/chart")
public class ChartController {

    @Resource
    private ChartRecordBizDomain chartRecordBizDomain;

    @Resource
    private ChartImageBizDomain chartImageBizDomain;

    @GetMapping("/img/gen/{uuid}")
    public ChartImageGenerateResultVO generate(@PathVariable("uuid") String uuid) {
        ChartRecordVO chartRecordVO = chartRecordBizDomain.getChartRecordByUUID(uuid);
        ChartImageGenerateResultVO resultVO = chartImageBizDomain.generateChartImage(chartRecordVO);
        return resultVO;
    }

    @GetMapping("/drivers/clear")
    public ApiResp<String> clearDrivers() {
        HeadlessChromeImageGenerator.getDriverPool().clear();
        String num = "";
        try {
            List<String> strList = new ArrayList<>();
            Process process = Runtime.getRuntime().exec("ps -ef | grep chrome | grep -v grep | wc -l");
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            process.waitFor();
            while ((line = input.readLine()) != null) {
                strList.add(line);
            }
            if (strList.size() > 0) {
                num = strList.get(0);
            }
        } catch (IOException e) {
        } catch (InterruptedException e) {
        }

        return ApiResp.success("chrome process: " + num);
    }

}
