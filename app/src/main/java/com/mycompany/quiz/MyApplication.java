package com.mycompany.quiz;

import android.app.Application;
import java.io.File;
import java.io.IOException;
import android.os.Environment;

import com.mycompany.quiz.R;

public class MyApplication extends Application {

    Process process;

    /**
     * Called when the application is starting, before any activity, service, or receiver objects (excluding content providers) have been created.
     */
    public void onCreate() {
        super.onCreate();

        File appDirectory = new File( Environment.getExternalStorageDirectory() + "/AppLogs" );
        File logFile = new File( appDirectory, getString(R.string.app_name) + "_logcat_.txt" );

        // create app folder
        if ( !appDirectory.exists() ) {
            appDirectory.mkdir();
        }

        // clear the previous logcat and then write the new one to the file
        try {
            process = Runtime.getRuntime().exec("logcat -c");
            process = Runtime.getRuntime().exec("logcat -f " + logFile);
        } catch ( IOException e ) {
            e.printStackTrace();
        }

    }
}
