package net.giteye.charts.hchrome;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.resource.ClassPathResource;
import com.alibaba.fastjson.JSONObject;
import net.giteye.charts.output.ChartOutput;
import net.giteye.charts.HtmlImageGenerator;
import net.giteye.charts.TargetImage;
import net.giteye.charts.output.OutputResult;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class HeadlessChromeImageGenerator extends HtmlImageGenerator<HeadlessChromeImageGenerator> {

    private final static Logger logger = LoggerFactory.getLogger(HeadlessChromeImageGenerator.class);

    private static WebDriverPool webDriverPool;

    private final String driverPath;

    private Integer waitTimeout = 3;

    private boolean headless = true;

    private static Map<String, WebDriver> driverCache = new ConcurrentHashMap<>();

    private List<String> arguments = new LinkedList<>();

    public HeadlessChromeImageGenerator(String driverPath) {
        this.driverPath = driverPath;
    }

    public boolean headless() {
        return headless;
    }

    public HeadlessChromeImageGenerator headless(boolean headless) {
        this.headless = headless;
        return this;
    }

    public HeadlessChromeImageGenerator addArgument(String argument) {
        if (StringUtils.isNotBlank(argument)) {
            this.arguments.add(argument.trim());
        }
        return this;
    }

    public Integer waitTimeout() {
        return this.waitTimeout;
    }

    public HeadlessChromeImageGenerator waitTimeout(Integer waitTimeout) {
        this.waitTimeout = waitTimeout;
        return this;
    }


    private String getDriverKey() {
        StringBuilder builder = new StringBuilder();
        for (String arg : this.arguments) {
            builder.append(arg).append(",");
        }
        builder.append("size=" + windowSize[0] + "x" +windowSize[1]);
        return builder.toString();
    }

    /**
     * ???????????????????????????Driver
     * @return
     */
    public static Map<String, WebDriver> getCachedDrivers() {
        return driverCache;
    }

    /**
     * ????????????Driver
     */
    public static void closeAllDrivers() {
        logger.info("[Giteye] ????????????Chrome Driver");
        for (String key : driverCache.keySet()) {
            WebDriver driver = driverCache.get(key);
            if (driver != null) {
                try {
                    driver.close();
                    driver.quit();
                } catch (Throwable th) {}
                finally {
                    driverCache.remove(key);
                }
            }
        }
    }


    public WebDriverPool getWebDriverPool() {
        if (webDriverPool == null) {
            synchronized (HeadlessChromeImageGenerator.class) {
                if (webDriverPool == null) {
                    webDriverPool = new WebDriverPool(this, 10);
                }
            }
        }
        return webDriverPool;
    }

    public static WebDriverPool getDriverPool() {
        return webDriverPool;
    }


    /**
     * ?????????WebDriver
     *
     * @return
     */
    public WebDriver getWebDriver() {
        WebDriverPool driverPool = getWebDriverPool();
        WebDriver driver = driverPool.getDriver();
        return driver;
    }


    @Override
    public HeadlessChromeImageGenerator generate() {
        int retryCount = 0;
        do {
            StopWatch watch = new StopWatch();
            watch.start("ChromeDriverTime");
            WebDriver driver = getWebDriver();
            watch.stop();
            logger.info("[Giteye] ??????ChromeDriver?????????" + watch.getLastTaskTimeMillis() + "ms");
            String path = null;
            synchronized (driver) {
                try {
                    watch.start("GenerateTime");
                    setGenerateStartTime(new Date());

                    URL url = url();
                    if (url != null) {
                        //????????????
                        path = url.toString();
                    } else {
                        ClassPathResource resource = new ClassPathResource(htmlPath);
                        path = resource.getFile().getPath();
                    }
                    //????????????
                    logger.info("[Giteye] ?????????????????????" + path);
                    driver.get(path);
                    //???????????????????????????3?????????
                    Integer timeout = waitTimeout;
                    if (timeout == null) {
                        timeout = 3;
                    }
                    driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
                    if (timeout > 5) {
                        Thread.sleep(Math.min(timeout, 5) * 1000);
                    }
                    // currentHandler = driver.getWindowHandle();
                    setGenerateEndTime(new Date());
                    watch.stop();
                    logger.info("[Giteye] ?????????????????????" + watch.getLastTaskTimeMillis() + "ms");

                    if (StringUtils.isNotBlank(script)) {
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        scriptResult = js.executeScript(script);
                    } else {
                        for (TargetImage targetImage : targetImages) {
                            WebElement element = null;
                            try {
                                if (targetImage.element() == null) {
                                    element = driver.findElement(By.tagName("body"));
                                } else {
                                    element = driver.findElement(targetImage.element());
                                }
                            } catch (Throwable th) {
                            }
                            if (element != null) {
                                byte[] byteArray = element.getScreenshotAs(OutputType.BYTES);
                                for (ChartOutput output : targetImage.outputs()) {
                                    logger.info("output: {}, targetImg: {}", output, JSONObject.toJSONString(targetImage));
                                    output.writeImage(this, byteArray, targetImage.filePath(), targetImage.fileName());
                                }
                            } else {
                                logger.warn("[Giteye] ???????????????????????? " +
                                        targetImage.element().toString() + ", url: " + path);
                            }
                        }
                    }
                } catch (Throwable th) {
                    getWebDriverPool().closeDriver(driver);
                    if (retryCount < 5) {
                        retryCount++;
                        logger.warn("\n\t[??????]: " + retryCount +  "\n\t[Giteye] ?????????????????????[" + th.getMessage() + "], url: " + path);
                        continue;
                    } else {
                        throw new RuntimeException(th);
                    }
                } finally {
                    getWebDriverPool().closeDriver(driver);
                }
            }
            return this;
        } while (true);
    }

    public String getDriverPath() {
        return driverPath;
    }

    public boolean isHeadless() {
        return headless;
    }

    public static Map<String, WebDriver> getDriverCache() {
        return driverCache;
    }

    public List<String> getArguments() {
        return arguments;
    }

}
