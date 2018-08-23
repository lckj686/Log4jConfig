package com.leon.log.log4;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Description：不要忘记填写 哦
 * Created by leon on 2018/8/6.
 */

public class PathUtil {

    public static String getAppSdCardPath(Application context) {
        if (isSdcardExist()) {
            //DIRECTORY_DOCUMENTS api =19
            // if (BuildConfig.DEBUG) {
            String packageName = context.getPackageName();
            String name[] = packageName.split("\\.");
            String fileName = "";
            for (int i = 0; i < name.length; i++) {
                fileName += name[i];
            }

            return Environment.getExternalStorageDirectory() + File.separator + fileName + File.separator;
            //}
            // return context.getExternalFilesDir(Environment.DIRECTORY_ALARMS) + File.separator;
        } else {
            File file = context.getDir("sdcard", Context.MODE_PRIVATE);
            return file.getPath() + File.separator + "sdcard" + File.separator;
        }
    }


    public static boolean isSdcardExist() {
        boolean reval = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

        if (!reval) {
            // Log.e(TAG, "Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) = false");
        }

        return reval;
    }

    public static String getAppLogPath(Application application) {
        String logPath = "";
        if (isSdcardExist()) {
            logPath = PathUtil.getAppSdCardPath(application);
        } else {
            logPath = application.getFilesDir().getPath() + File.separator;
        }

        return logPath;
    }

    public static String getClassName() {
        String className = null;
        try {
            throw new Exception();
        } catch (Exception e) {
            StackTraceElement[] element = e.getStackTrace();
            className = element[0].getClassName();
        }
        return className;
    }

    public static String getFileName() {
        String className = null;
        try {
            throw new Exception();
        } catch (Exception e) {
            StackTraceElement[] element = e.getStackTrace();
            className = element[0].getFileName();
        }
        return className;
    }
}
