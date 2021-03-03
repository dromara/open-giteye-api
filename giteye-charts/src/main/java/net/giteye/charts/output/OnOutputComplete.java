package net.giteye.charts.output;

/**
 * @author gongjun[dt_flys@hotmail.com]
 * @since 2021-02-03 22:24
 */
public interface OnOutputComplete {

    void onComplete(ChartOutput output, OutputResult result);
}
