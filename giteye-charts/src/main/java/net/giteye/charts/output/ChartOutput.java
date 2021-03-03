package net.giteye.charts.output;

import net.giteye.charts.HtmlImageGenerator;

public abstract class ChartOutput<T extends ChartOutput> {

    private final String name;

    protected OutputFilenameFactory filenameFactory;

    protected OnOutputComplete onComplete;

    protected ChartOutput(String name) {
        this.name = name;
    }

    public OnOutputComplete onComplete() {
        return onComplete;
    }

    public T onComplete(OnOutputComplete onComplete) {
        this.onComplete = onComplete;
        return (T) this;
    }

    public OutputFilenameFactory filenameFactory() {
        return this.filenameFactory;
    }

    public T filenameFactory(OutputFilenameFactory filenameFactory) {
        this.filenameFactory = filenameFactory;
        return (T) this;
    }

    public String getName() {
        return name;
    }

    public abstract void writeImage(
            HtmlImageGenerator imageGenerator,
            byte[] byteArray, String path, String fileName);


}
