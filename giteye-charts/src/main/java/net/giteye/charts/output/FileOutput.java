package net.giteye.charts.output;

import cn.hutool.core.io.FileUtil;
import net.giteye.charts.HtmlImageGenerator;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class FileOutput extends ChartOutput<FileOutput> {

    private final String targetDir;

    public FileOutput(String name, String targetDir) {
        super(name);
        this.targetDir = targetDir;
    }

    public static FileOutput dir(String name, String targetDir) {
        FileOutput output = new FileOutput(name, targetDir);
        return output;
    }

    @Override
    public void writeImage(HtmlImageGenerator imageGenerator, byte[] byteArray, String path, String fileName) {
        Date startTime = new Date();
        if (filenameFactory != null) {
            fileName = filenameFactory.generateFilename(path, fileName);
        } else {
            fileName = Paths.get(path, fileName).toString();
        }
        if (byteArray != null && StringUtils.isNotEmpty(fileName)) {
            File file = new File(targetDir);
            File targetFile = file;
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            Path targetFilePath = Paths.get(targetDir, fileName);
            targetFile = targetFilePath.toFile();

            try {
                FileUtil.writeBytes(byteArray, targetFile);
            } catch (Throwable th) {
                OutputResult errorResult = new OutputResult(fileName, false);
                errorResult.setException(th);
                errorResult.setStartTime(startTime);
                errorResult.setEndTime(new Date());
                imageGenerator.putResult(this, errorResult);
                if (onComplete != null) {
                    onComplete.onComplete(this, errorResult);
                }
                return;
            }
            Date endTime = new Date();
            OutputResult successResult = new OutputResult(fileName, true);
            successResult.setTargetPath(targetFile.getPath());
            successResult.setStartTime(startTime);
            successResult.setEndTime(endTime);
            imageGenerator.putResult(this, successResult);
            if (onComplete != null) {
                onComplete.onComplete(this, successResult);
            }
        }
    }
}
