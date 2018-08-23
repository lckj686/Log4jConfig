package com.leon.log.log4;

import android.app.Application;
import android.os.Environment;
import android.util.Log;


import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import java.io.File;
import java.io.IOException;

import de.mindpipe.android.logging.log4j.LogConfigurator;


/**
 * 本地记录工具类,log打到本地时使用
 * 在application 里初始化  Logger4.init(application);
 * <p>
 * <p>
 * <p>
 * <p>
 * /*
 * log4j.appender.A1.layout.ConversionPattern=%-4r %-5p %d{yyyy-MM-dd HH:mm:ssS} %c %m%n
 * 这里需要说明的就是日志信息格式中几个符号所代表的含义：
 * 　　         －X号: X信息输出时左对齐；
 * <p>
 * %p: 输出日志信息优先级，即DEBUG，INFO，WARN，ERROR，FATAL,
 * %d: 输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyy MMM dd HH:mm:ss,SSS}，输出类似：2002年10月18日 22：10：28，921
 * %r: 输出自应用启动到输出该log信息耗费的毫秒数
 * %c: 输出日志信息所属的类目，通常就是所在类的全名
 * %t: 输出产生该日志事件的线程名
 * %l: 输出日志事件的发生位置，相当于%C.%M(%F:%L)的组合,包括类目名、发生的线程，以及在代码中的行数。举例：Testlog4.main(TestLog4.java:10)
 * %x: 输出和当前线程相关联的NDC(嵌套诊断环境),尤其用到像java servlets这样的多客户多线程的应用中。
 * %%: 输出一个"%"字符
 * %F: 输出日志消息产生时所在的文件名称
 * %L: 输出代码中的行号
 * %m: 输出代码中指定的消息,产生的日志具体信息
 * %n: 输出一个回车换行符，Windows平台为"\r\n"，Unix平台为"\n"输出日志信息换行
 * 可以在%与模式字符之间加上修饰符来控制其最小宽度、最大宽度、和文本的对齐方式。如：
 * 1)%20c：指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，默认的情况下右对齐。
 * 2)%-20c:指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，"-"号指定左对齐。
 * 3)%.30c:指定输出category的名称，最大的宽度是30，如果category的名称大于30的话，就会将左边多出的字符截掉，但小于30的话也不会有空格。
 * 4)%20.30c:如果category的名称小于20就补空格，并且右对齐，如果其名称长于30字符，就从左边交远销出的字符截掉。
 */
public class Logger4 {
    static private final String TAG = "Logger4";


    public Logger log;// = Logger.getLogger(root);


    public Logger4() {
        log = Logger.getRootLogger();
    }

    public Logger4(String fileName) {
        log = Logger.getLogger(fileName);
    }


    public Logger4 addAppender(Appender newAppender) {


        log.addAppender(newAppender);

        return this;

    }

    // 输出Level.DEBUG级别日志,一般开发调试信息用
    public void d(String tag, String message) {
        if (null != log && log.isDebugEnabled()) {
            log.debug(tag + " > " + message);
        }
    }

    // 输出Level.INFO级别日志
    public void i(String tag, String message) {
        if (null != log && log.isInfoEnabled()) {
            log.info(tag + " > " + message);
        }
    }

    // 输出Level.WARN级别日志
    public void w(String tag, String message) {
        if (null != log && log.isEnabledFor(Level.WARN)) {
            log.warn(tag + " > " + message);
        }
    }

    // 输出Level.ERROR级别日志,一般catch住异常后使用,使用e.printStackTrace()打印出错误信息;
    public void e(String tag, String message) {
        if (null != log && log.isEnabledFor(Level.ERROR)) {
            log.error(tag + " > " + message);
        }
    }

    public void e(String tag, Throwable e) {
        if (null != log && log.isEnabledFor(Level.ERROR)) {
            log.error(tag + " > " + getStackTraceString(e));
        }
    }

    public void e(String tag, String msg, Throwable e) {
        if (null != log && log.isEnabledFor(Level.ERROR)) {
            log.error(tag + " > " + msg + " e=" + getStackTraceString(e));
        }
    }

    // 输出Level.FATAL级别日志
    public void f(String tag, Object message) {
        if (null != log && log.isEnabledFor(Level.FATAL)) {
            log.fatal(tag + " > " + message);
        }
    }

    // 输出Level.FATAL级别日志
    public void f(String tag, Throwable e) {
        if (null != log && log.isEnabledFor(Level.FATAL)) {
            log.fatal(tag + "," + System.currentTimeMillis() + "," + getStackTraceString(e));
        }
    }


    public String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }


    private boolean isSdcardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


}

