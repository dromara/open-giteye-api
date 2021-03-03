package net.giteye;

import net.giteye.charts.hchrome.WebDriverPool;
import net.giteye.charts.output.FileOutput;
import net.giteye.charts.HtmlImageGenerator;
import net.giteye.charts.TargetImage;
import net.giteye.charts.output.QCloudCosOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;

public class Test {

    private final static Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) throws MalformedURLException {

        QCloudCosOutput qCloudCosOutput = new QCloudCosOutput(
                "test",
                "AKIDNYS6r1dUnQomXOuxsqY7SDl4kBLGcviJ",
                "Zt84aeojPALfmcVnGK79YAWdpmUv5STM",
                "ap-shanghai")
                .bucket("giteye-1304357239")
                .baseUrl("http://chart.giteye.net")
                .onComplete((output, result) -> {
                    logger.info("Upload Complete: {}", result.getTargetPath());
                });


        HtmlImageGenerator
                .headlessChromeDriver("D:\\chromedriver\\87.0.4280.88\\chromedriver.exe")
                .url("https://timqian.com/chart.xkcd/example.html")
                .windowSize(3840, 2160)
                .saveImage(
                        TargetImage.fileName("dt_flys/forest/chart-line.png")
                                .byClass("line-chart")
                                .imageType(TargetImage.ImageType.PNG)
                                .output(FileOutput.dir("file_output", "D:/giteye-image"))
                                .output(qCloudCosOutput)
                )
                .headless(true)
                .generate();
    }

}
