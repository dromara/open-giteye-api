package net.giteye.charts.utils;

import org.apache.commons.lang.StringUtils;

public class PathUtil {

    public static String urlPath(String basePath, String path) {
        StringBuilder builder = new StringBuilder();
        if (basePath.endsWith("/")) {
            builder.append(basePath.substring(0, basePath.length() - 1));
        } else {
            builder.append(basePath);
        }
        if (StringUtils.isNotBlank(path)) {
            builder.append("/");
            if (path.startsWith("/")) {
                builder.append(path.substring(1));
            } else {
                builder.append(path);
            }
        }
        return builder.toString();
    }

}
