package com.mi.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import miot.service.common.utils.SdCardUtils;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = CrashHandler.class.getSimpleName();

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;

    public CrashHandler(Context context) {
        mContext = context.getApplicationContext();
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        handleException(throwable);

        if (mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, throwable);
        }

//        if (!handleException(throwable) && mDefaultHandler != null) {
//            mDefaultHandler.uncaughtException(thread, throwable);
//
//        } else {
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
//        }
    }

    private boolean handleException(Throwable exception) {
        if (exception == null) {
            return false;
        }

        String crashLog = getCrashLog(exception);
        try {
            saveCrashLog(crashLog);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "saveCrashLog exception");
        }

        //TODO: send crash log to cloud
        return true;
    }

    private void collectDeviceInfo(Context context) {
        //TODO: get device info, see the class: PhoneDevice
    }

    private String getCrashLog(Throwable exception) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        exception.printStackTrace(printWriter);

        Throwable cause = exception.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        return writer.toString();
    }

    private void saveCrashLog(String log) throws IOException {
        if (SdCardUtils.isSDCardUseful()) {
            String path = Environment.getExternalStorageDirectory().getPath() + "/auxgroup";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            DateTime now = new DateTime();
            String time = now.toString("yyyy-MM-dd HH:mm");
            String fileName = "crash_" + time + ".log";

            FileOutputStream fos = new FileOutputStream(path + "/" + fileName);
            fos.write(log.getBytes());
            fos.close();
        }
    }
}

