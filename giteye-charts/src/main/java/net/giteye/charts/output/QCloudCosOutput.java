package net.giteye.charts.output;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.URLUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import net.giteye.charts.HtmlImageGenerator;
import net.giteye.charts.utils.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;

public class QCloudCosOutput extends ChartOutput<QCloudCosOutput> {

    private final static Logger logger = LoggerFactory.getLogger(QCloudCosOutput.class);

    private final String secretId;

    private final String secretKey;

    private String regionId;

    private String bucket;

    private COSCredentials cred;

    private Region region;

    private COSClient cosClient;

    private String baseUrl;


    public QCloudCosOutput(String name, String secretId, String secretKey, String regionId) {
        super(name);
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.regionId = regionId;
        this.cred = new BasicCOSCredentials(this.secretId, this.secretKey);
        this.region = new Region(this.regionId);
        ClientConfig config = new ClientConfig(region);
        config.setMaxConnectionsCount(800);
        config.setConnectionTimeout(30000);
        config.setConnectionRequestTimeout(30000);
        config.setSocketTimeout(30000);
        config.setMaxErrorRetry(3);
        this.cosClient = new COSClient(cred, config);
    }

    public QCloudCosOutput bucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String bucket() {
        return this.bucket;
    }

    public QCloudCosOutput baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public String baseUrl() {
        return this.baseUrl;
    }

    @Override
    public void writeImage(HtmlImageGenerator imageGenerator, byte[] byteArray, String path, String fileName) {
        StopWatch watch = new StopWatch();
        watch.start();
        Date startTime = new Date();
        PutObjectResult result = null;
        if (filenameFactory != null) {
            fileName = filenameFactory.generateFilename(path, fileName);
        } else {
            fileName = PathUtil.urlPath(path, fileName);
        }
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(byteArray.length);
            metadata.setContentType("image/png");
            PutObjectRequest request = new PutObjectRequest(bucket, fileName, inputStream, metadata);
            result = cosClient.putObject(request);
        } catch (IOException e) {
            logger.info("[Giteye] QCloud COS 上传失败 -> Request Id: {}, Version Id: {}",
                    result.getRequestId(), result.getVersionId());
            OutputResult errorResult = new OutputResult(fileName, false);
            errorResult.setException(e);
            errorResult.setStartTime(startTime);
            errorResult.setEndTime(new Date());
//            if (imageGenerator != null) {
                imageGenerator.putResult(this, errorResult);
                if (onComplete != null) {
                    onComplete.onComplete(this, errorResult);
                }
//            }
            return;
        }
        watch.stop();
        Date endTime = new Date();
//        if (result != null) {
            logger.info("[Giteye] QCloud COS 上传成功 -> Request Id: {}, Version Id: {}, eTag: {}",
                result.getRequestId(), result.getVersionId(), result.getETag());
            logger.info("[Giteye] QCloud COS 上传图片成功, 耗时: {}ms", watch.getLastTaskTimeMillis());
            OutputResult successResult = new OutputResult(fileName, true);
            successResult.setStartTime(startTime);
            successResult.setEndTime(endTime);
            String targetUrl = URLUtil.completeUrl(baseUrl, fileName);
            successResult.setTargetPath(targetUrl);
            imageGenerator.putResult(this, successResult);
            if (onComplete != null) {
                onComplete.onComplete(this, successResult);
            }
//        }
    }
}
