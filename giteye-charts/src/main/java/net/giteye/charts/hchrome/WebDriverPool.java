package net.giteye.charts.hchrome;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObjectInfo;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author gongjun[dt_flys@hotmail.com]
 * @since 2021-02-14 1:47
 */
public class WebDriverPool {

    private final static Logger logger = LoggerFactory.getLogger(WebDriverPool.class);

    private final int maxPoolSize;

    private GenericObjectPool<WebDriver> driverObjectPool;


    public static class WebDriverPoolFactory implements PooledObjectFactory<WebDriver> {

        private final HeadlessChromeImageGenerator generator;

        public WebDriverPoolFactory(HeadlessChromeImageGenerator generator) {
            this.generator = generator;
        }

        @Override
        public PooledObject<WebDriver> makeObject() throws Exception {
            WebDriver driver;
            // 设置ChromeDriver的路径加载驱动

            System.setProperty("webdriver.chrome.driver", generator.getDriverPath());
            ChromeOptions chromeOptions = new ChromeOptions();
            //无头模式
            chromeOptions.setHeadless(generator.isHeadless());
            //地址出现data:,
//        chromeOptions.addArguments("--user-data-dir=C:/Users/Administrator/AppData/Local/Google/Chrome/User Data/Default");
            //Chrome正在受到自动软件的控制  不显示提示语
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            chromeOptions.addArguments("--disable-gpu");
            Integer[] windowSize = generator.windowSize();
            if (windowSize[0] >= 0 && windowSize[1] >= 0) {
                chromeOptions.addArguments("--window-size=" + windowSize[0] + "," + windowSize[1]);
            }
            chromeOptions.addArguments("--ignore-certificate-errors");
            chromeOptions.addArguments("disable-infobars");
            //启动一个 chrome 实例
            driver = new ChromeDriver(chromeOptions);
            logger.info("[Giteye] 创建了一个新的 Chrome Driver");
            return new DefaultPooledObject<>(driver);
        }

        @Override
        public void destroyObject(PooledObject<WebDriver> pooledObject) throws Exception {
            logger.info("[Giteye Driver Pool Factory] 销毁 Crhome Driver");
            WebDriver driver = pooledObject.getObject();
            synchronized (driver) {
                try {
                    driver.close();
                } finally {
                    driver.quit();
                }
            }
        }

        @Override
        public boolean validateObject(PooledObject<WebDriver> pooledObject) {
            return true;
        }

        @Override
        public void activateObject(PooledObject<WebDriver> pooledObject) throws Exception {

        }

        @Override
        public void passivateObject(PooledObject<WebDriver> pooledObject) throws Exception {

        }
    }

    public WebDriverPool(HeadlessChromeImageGenerator generator, int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        WebDriverPoolFactory webDriverPoolFactory = new WebDriverPoolFactory(generator);
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(maxPoolSize);
        config.setTestOnCreate(false);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(true);
        this.driverObjectPool = new GenericObjectPool<>(webDriverPoolFactory, config);
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }


    public WebDriver getDriver() {
        try {
            return this.driverObjectPool.borrowObject();
        } catch (Exception e) {
        }
        return null;
    }

    public void closeDriver(WebDriver driver) {
        try {
            driverObjectPool.invalidateObject(driver);
        } catch (Exception e) {
        }
        logger.info("[Giteye] 关闭Driver, 剩余: " + driverObjectPool.getNumActive() + "个Driver");
    }

    public void clear() {
        driverObjectPool.clear();
    }


}
