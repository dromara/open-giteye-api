package net.giteye.charts.output;

import java.util.Date;

/**
 * @author gongjun[dt_flys@hotmail.com]
 * @since 2021-02-03 22:26
 */
public class OutputResult {

    private final String filename;

    private final boolean success;

    private String targetPath;

    private Throwable exception;

    private Date startTime;

    private Date endTime;

    public OutputResult(String filename, boolean success) {
        this.filename = filename;
        this.success = success;
    }

    public String getFilename() {
        return filename;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isError() {
        return !isSuccess();
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
