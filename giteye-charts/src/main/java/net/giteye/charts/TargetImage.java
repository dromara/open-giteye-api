package net.giteye.charts;

import net.giteye.charts.output.ChartOutput;
import org.openqa.selenium.By;

import java.util.LinkedList;
import java.util.List;

public class TargetImage {

    private By element;

    private String targetFilePath;

    private final String targetFileName;

    public enum ImageType {
        PNG("png", "image/png"),
        JPEG("jpg", "image/jpeg");

        private final String extName;

        private final String contentType;

        ImageType(String extName, String contentType) {
            this.extName = extName;
            this.contentType = contentType;
        }

        public String getExtName() {
            return extName;
        }

        public String getContentType() {
            return contentType;
        }
    }


    private TargetImage.ImageType imageType = TargetImage.ImageType.PNG;

    private List<ChartOutput> outputs = new LinkedList<>();

    public TargetImage(String targetFileName) {
        this.targetFileName = targetFileName;
    }


    public static TargetImage fileName(String targetFileName) {
        TargetImage targetImage = new TargetImage(targetFileName);
        return targetImage;
    }

    public TargetImage filePath(String targetFilePath) {
        this.targetFilePath = targetFilePath;
        return this;
    }

    public String filePath() {
        return this.targetFilePath;
    }

    public By element() {
        return this.element;
    }

    public TargetImage element(By by) {
        this.element = by;
        return this;
    }

    public TargetImage byId(String id) {
        this.element = By.id(id);
        return this;
    }

    public TargetImage byClass(String className) {
        this.element = By.className(className);
        return this;
    }

    public String fileName() {
        return this.targetFileName;
    }

    public TargetImage imageType(TargetImage.ImageType imageType) {
        this.imageType = imageType;
        return this;
    }


    public TargetImage.ImageType imageType() {
        return imageType;
    }

    public TargetImage output(ChartOutput output) {
        this.outputs.add(output);
        return this;
    }

    public List<ChartOutput> outputs() {
        return this.outputs;
    }


}
