package com.leon.log.log4;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import java.io.File;
import java.io.IOException;


/**
 * 本地记录工具类,log打到本地时使用
 * 在application 里初始化  Logger4.init(application);
 */
public class LoggerUtil3 {
    static private final String TAG = "Logger4";


    private static String logPath = null;
    private static String fileName = null;

    //private static final LogConfigurator logConfigurator = new LogConfigurator();

    static private LoggerUtil3 loggerUtil;
    private static Logger log;// = Logger.getLogger(root);

    private LoggerUtil3() {

    }


    static public void init(Application application) {


        logPath = PathUtil.getAppLogPath(application);
        fileName = "log3.txt";
        addAppender();
    }


    public static void addAppender() {
        log = Logger.getLogger(PathUtil.getClassName());


        final RollingFileAppender rollingFileAppender;
        final Layout fileLayout = new PatternLayout("%m%n");

        try {
            rollingFileAppender = new RollingFileAppender(fileLayout, logPath + fileName);
        } catch (final IOException e) {
            throw new RuntimeException("Exception configuring log system", e);
        }

        rollingFileAppender.setMaxBackupIndex(3);
        rollingFileAppender.setMaximumFileSize(1024 * 60);
        rollingFileAppender.setImmediateFlush(true);

        log.addAppender(rollingFileAppender);


    }


    // 输出Level.DEBUG级别日志,一般开发调试信息用
    public static void d(String tag, String message) {
        if (null != log && log.isDebugEnabled()) {
            log.debug(tag + " > " + message);
        }
    }

    // 输出Level.INFO级别日志
    public static void i(String tag, String message) {
        if (null != log && log.isInfoEnabled()) {
            log.info(tag + " > " + message);
        }
    }

    // 输出Level.WARN级别日志
    public static void w(String tag, String message) {
        if (null != log && log.isEnabledFor(Level.WARN)) {
            log.warn(tag + " > " + message);
        }
    }

    // 输出Level.ERROR级别日志,一般catch住异常后使用,使用e.printStackTrace()打印出错误信息;
    public static void e(String tag, String message) {
        if (null != log && log.isEnabledFor(Level.ERROR)) {
            log.error(tag + " > " + message);
        }
    }

    public static void e(String tag, Throwable e) {
        if (null != log && log.isEnabledFor(Level.ERROR)) {
            log.error(tag + " > " + getStackTraceString(e));
        }
    }

    public static void e(String tag, String msg, Throwable e) {
        if (null != log && log.isEnabledFor(Level.ERROR)) {
            log.error(tag + " > " + msg + " e=" + getStackTraceString(e));
        }
    }

    // 输出Level.FATAL级别日志
    public static void f(String tag, Object message) {
        if (null != log && log.isEnabledFor(Level.FATAL)) {
            log.fatal(tag + " > " + message);
        }
    }

    // 输出Level.FATAL级别日志
    public static void f(String tag, Throwable e) {
        if (null != log && log.isEnabledFor(Level.FATAL)) {
            log.fatal(tag + "," + System.currentTimeMillis() + "," + getStackTraceString(e));
        }
    }


    public static String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }


    private static boolean isSdcardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


}

