package com.bol.kahala.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
public class LoggingUtil {

    private LoggingUtil() {
        throw new IllegalStateException("Utility class");
    }
    public static void logInfo(String message, Object... param) {
        StringBuilder sb = new StringBuilder();
        log.info(getLogMessageWithTraceId(message, sb), param);
    }

    public static void logError(String message, Object... param) {
        StringBuilder sb = new StringBuilder();
        log.error(getLogMessageWithTraceId(message, sb), param);
    }

    private static String getLogMessageWithTraceId(String message, StringBuilder sb) {
        String traceId = MDC.get("requestId");
        return sb.append("[requestId] : [").append(traceId).append("] : ")
                .append(message).toString();
    }


}
