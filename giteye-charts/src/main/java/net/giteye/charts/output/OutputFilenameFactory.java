package net.giteye.charts.output;

/**
 * @author gongjun[dt_flys@hotmail.com]
 * @since 2021-02-04 1:23
 */
public interface OutputFilenameFactory {

    String generateFilename(String path, String srcFilename);
}
