package org.smartlink.workflow.utils.pdf;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 86158
 */
public class DateUtil {
    public static String getFormatTime(Object time) {
        if (time == null) {
            return "";
        }
        try {
            if (time instanceof Date) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
            }
            return time.toString();
        } catch (Exception e) {
            return "FORMAT_ERROR";
        }
    }
}
