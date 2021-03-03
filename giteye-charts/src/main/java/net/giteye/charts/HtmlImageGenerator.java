package net.giteye.charts;

import net.giteye.charts.hchrome.HeadlessChromeImageGenerator;
import net.giteye.charts.output.ChartOutput;
import net.giteye.charts.output.OutputResult;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class HtmlImageGenerator<T extends HtmlImageGenerator> {

    protected String htmlPath;

    protected URL url;

    protected String script;

    protected Object scriptResult;

    protected Integer[] windowSize = new Integer[] {-1, -1};

    protected List<TargetImage> targetImages = new LinkedList<>();

    private Date generateStartTime;

    private Date generateEndTime;


    protected Map<String, OutputResult> results = new ConcurrentHashMap<>();




    public static HeadlessChromeImageGenerator headlessChromeDriver(String driverPath) {
        return new HeadlessChromeImageGenerator(driverPath);
    }

    public T putResult(ChartOutput output, OutputResult result) {
        this.results.put(output.getName(), result);
        return (T) this;
    }

    public T removeResult(ChartOutput output) {
        return removeResult(output.getName());
    }


    public T removeResult(String name) {
        if (this.results.containsKey(name)) {
            this.results.remove(name);
        }
        return (T) this;
    }




    public OutputResult getResult(ChartOutput output) {
        return getResult(output.getName());
    }

    public OutputResult getResult(String name) {
        return this.results.get(name);
    }

    public T htmlPath(String htmlPath) {
        this.htmlPath = htmlPath;
        return (T) this;
    }

    public String htmlPath() {
        return htmlPath;
    }

    public T url(URL url) {
        this.url = url;
        return (T) this;
    }

    public T url(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return (T) this;
    }


    public URL url() {
        return this.url;
    }

    public T windowSize(int width, int height) {
        this.windowSize[0] = width;
        this.windowSize[1] = height;
        return (T) this;
    }

    public Integer[] windowSize() {
        return this.windowSize;
    }

    public T script(String script) {
        this.script = script;
        return (T) this;
    }

    public String script() {
        return script;
    }

    public Object scriptResult() {
        return scriptResult;
    }

    public T saveImage(TargetImage targetImage) {
        this.targetImages.add(targetImage);
        return (T) this;
    }

    public List<TargetImage> targetImages() {
        return this.targetImages;
    }

    public Date getGenerateStartTime() {
        return generateStartTime;
    }

    public void setGenerateStartTime(Date generateStartTime) {
        this.generateStartTime = generateStartTime;
    }

    public Date getGenerateEndTime() {
        return generateEndTime;
    }

    public void setGenerateEndTime(Date generateEndTime) {
        this.generateEndTime = generateEndTime;
    }

    /**
     * 将HTML生成为图片
     *
     * @return 图片地址
     */
    public abstract T generate();

}
