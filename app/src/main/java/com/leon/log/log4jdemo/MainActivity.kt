package com.leon.log.log4jdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.leon.log.log4.Log4
import com.leon.log.log4.Logger4
import com.leon.log.log4.LoggerUtil2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()


    }

    private fun init() {
        Log4.config(this.application)
        Log4.configMoreLog4()

        Thread(Runnable {

            while (true) {
                Log4.logger.d("log", "time ${SystemClock.currentThreadTimeMillis()}")

                Log4.logger1.d("log1", "time ${SystemClock.currentThreadTimeMillis()}")
                Log4.logger2.d("log2", "time ${SystemClock.currentThreadTimeMillis()}")

                Thread.sleep(1000)

            }

        }).start()
    }
}
