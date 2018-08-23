package com.leon.log.log4

import android.app.Application
import android.os.Environment
import android.util.Log

import java.io.File

import de.mindpipe.android.logging.log4j.LogConfigurator
import org.apache.log4j.*
import java.io.IOException

/**
 * Description：不要忘记填写 哦
 * Created by leon on 2018/8/23.
 */

object Log4 {


    lateinit var logger: Logger4
    lateinit var logger1: Logger4
    lateinit var logger2: Logger4

    private lateinit var application: Application
    /**
     * 初始化log
     */
    fun config(application: Application) {
        this.application = application
        val root = application.packageName


        val logPath = PathUtil.getAppLogPath(application)
        Log.i("path", logPath)

        val fileName = "log4j.txt"

        val logConfigurator = LogConfigurator(logPath + fileName)
        logConfigurator.setLevel(root, Level.ALL)
        logConfigurator.filePattern = "%d{yyyy:MM:dd HH:mm:ss} -%p, %m%n"//格式
        logConfigurator.maxBackupSize = 3//备份数量
        logConfigurator.maxFileSize = (1024 * 1024 * 2).toLong()//2m
        logConfigurator.isImmediateFlush = true//追加
        logConfigurator.configure()
    }

    private fun appender(fileName: String): Appender {
        val rollingFileAppender: RollingFileAppender
        val fileLayout = PatternLayout("%m%n")

        try {
            rollingFileAppender = RollingFileAppender(fileLayout, PathUtil.getAppLogPath(application) + fileName)
        } catch (e: IOException) {
            throw RuntimeException("Exception configuring log system", e)
        }
        rollingFileAppender.maxBackupIndex = 3
        rollingFileAppender.maximumFileSize = (1024 * 60).toLong()
        rollingFileAppender.immediateFlush = true

        return rollingFileAppender
    }

    fun configMoreLog4() {
        logger = Logger4()
        logger1 = Logger4("logger1").addAppender(appender("logger1"))
        logger2 = Logger4("logger2").addAppender(appender("logger2"))
    }


}
