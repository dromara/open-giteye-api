package net.giteye.domain;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import net.giteye.charts.HtmlImageGenerator;
import net.giteye.charts.TargetImage;
import net.giteye.charts.output.OutputResult;
import net.giteye.charts.output.QCloudCosOutput;
import net.giteye.charts.utils.PathUtil;
import net.giteye.enums.ChartMetrics;
import net.giteye.property.GeProperty;
import net.giteye.property.QCloudProperty;
import net.giteye.vo.ChartImageGenerateResultVO;
import net.giteye.vo.ChartRecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;

/**
 * @author gongjun[dt_flys@hotmail.com]
 * @since 2021-02-03 23:37
 */
@Component
public class ChartImageBizDomain {

    private final static Logger logger = LoggerFactory.getLogger(ChartImageBizDomain.class);

    @Resource
    private GeProperty geProperty;

    @Resource
    private QCloudProperty qCloudProperty;

    private QCloudCosOutput imageCosOutput;

    private QCloudCosOutput thumbnailCosOutput;

//    private QCloudCosOutput historyImageCosOutput;


    @PostConstruct
    public void initialize() {
        imageCosOutput = new QCloudCosOutput(
                "cos_output",
                qCloudProperty.getSecretId(),
                qCloudProperty.getSecretKey(),
                qCloudProperty.getCosRegionId())
                .bucket(qCloudProperty.getCosGiteeBucket())
                .baseUrl(qCloudProperty.getCosBaseUrl())
                .onComplete((output, result) -> {
                    logger.info("[Giteye] QCloud COS 成功上传图表: " + result.getTargetPath());
                });

        thumbnailCosOutput = new QCloudCosOutput(
                "thumb_cos_output",
                qCloudProperty.getSecretId(),
                qCloudProperty.getSecretKey(),
                qCloudProperty.getCosRegionId())
                .bucket(qCloudProperty.getCosGiteeBucket())
                .baseUrl(qCloudProperty.getCosBaseUrl())
                .onComplete((output, result) -> {
                    logger.info("[Giteye] QCloud COS 成功上传缩略图: " + result.getTargetPath());
                });

    }


    /**
     * 生成图表图片
     *
     * @param chartRecord 图表记录
     */
    public ChartImageGenerateResultVO generateChartImage(ChartRecordVO chartRecord) {
        ChartImageGenerateResultVO imageGenerateResultVO = new ChartImageGenerateResultVO();
        HtmlImageGenerator generator = null;
        try {
            String gitSite = chartRecord.getGitSite();
            String gitUsername = chartRecord.getGitUsername();
            String repo = chartRecord.getRepoName();
            String imgUuid = chartRecord.getImgUuid();
//        ChartMetrics metrics = ChartMetrics.getEnumByCode(chartRecord.getMetricsType());
            // 开始生成图片
            String metrics = chartRecord.getMetricsType();
            String url = StrUtil.format("{}/{}", qCloudProperty.getChartUrl(), imgUuid);
            String targetImagePath = StrUtil.format("{}/{}/{}",
                    gitSite, gitUsername, repo);
            TargetImage.ImageType imageType = TargetImage.ImageType.PNG;
            String targetImageName = StrUtil.format("{}.{}", imgUuid, imageType.getExtName());
            String thumbnailName = StrUtil.format("{}-thumb.{}", imgUuid, imageType.getExtName());
            generator = HtmlImageGenerator
                    .headlessChromeDriver(geProperty.getHeadlessChromeDriver())
                    .url(url)
                    .windowSize(geProperty.getChartWebWidth(), geProperty.getChartWebHeight())
                    .saveImage(TargetImage.fileName(targetImageName)
                            .filePath(targetImagePath)
                            .byId("private-wrap")
                            .imageType(imageType)
                            .output(imageCosOutput))
                    .saveImage(TargetImage.fileName(thumbnailName)
                            .filePath(targetImagePath)
                            .byId("private-thumbnail")
                            .imageType(imageType)
                            .output(thumbnailCosOutput))
                    .headless(true)
                    .waitTimeout(metrics.equals(ChartMetrics.CONTRIBUTORS.getCode()) ? 10 : 3)
                    .generate();

            OutputResult result = generator.getResult(imageCosOutput);
            OutputResult thumbResult = generator.getResult(thumbnailCosOutput);
            // 清空result
            imageGenerateResultVO.setSuccess(result.isSuccess());
            imageGenerateResultVO.setImgUUID(imgUuid);
            imageGenerateResultVO.setImgType(imageType.getExtName());
            imageGenerateResultVO.setImgUrl(result.getTargetPath());
            imageGenerateResultVO.setImgFileName(targetImageName);
            imageGenerateResultVO.setThumbnailUrl(thumbResult.getTargetPath());
            imageGenerateResultVO.setTheme(chartRecord.getTheme());
            imageGenerateResultVO.setGenerateStartTime(generator.getGenerateStartTime());
            imageGenerateResultVO.setGenerateEndTime(generator.getGenerateEndTime());
            imageGenerateResultVO.setUploadStartTime(result.getStartTime());
            imageGenerateResultVO.setUploadEndTime(result.getEndTime());
            Throwable exception = result.getException();
            if (exception != null) {
                imageGenerateResultVO.setMsg(exception.getMessage());
            } else {
                imageGenerateResultVO.setMsg("success");
            }
            imageGenerateResultVO.setSuccess(true);
        } catch (Throwable th) {
            imageGenerateResultVO.setSuccess(false);
            imageGenerateResultVO.setMsg(th + "");
            logger.error("[Giteye] 图片生成或上传发生错误: ", th);
        }

        return imageGenerateResultVO;
    }



}
