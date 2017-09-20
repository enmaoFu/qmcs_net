package com.and.yzy.frame.application;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.and.yzy.frame.config.SavePath;
import com.and.yzy.frame.util.OkHttpUtils;
import com.and.yzy.frame.util.RetrofitUtils;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class BaseApplication extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        RetrofitUtils.init();
        initPresco();
        Logger.init("result")               // default tag : PRETTYLOGGER or use just init()
                .setMethodCount(3)            // default 2
                .hideThreadInfo()             // default it is shown
                .setLogLevel(LogLevel.FULL);  // default : LogLevel.FULL


        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        // 初始化Bugly
        Beta.autoCheckUpgrade = false;
        Bugly.init(context, "5a70183e27", true, strategy);

    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    private void initPresco() {

        final File file = new File(SavePath.SAVE_PATH + "cache/");
        if (!file.exists()) {
            file.mkdirs();
        }
        DiskCacheConfig.Builder diskCacheConfig = DiskCacheConfig.newBuilder(mContext)
                .setBaseDirectoryPathSupplier(new Supplier<File>() {
                    @Override
                    public File get() {
                        return file;
                    }
                })
                .setBaseDirectoryName("image_cache")
                .setMaxCacheSize(40 * ByteConstants.MB)
                .setMaxCacheSizeOnLowDiskSpace(10 * ByteConstants.MB)
                .setMaxCacheSizeOnVeryLowDiskSpace(2 * ByteConstants.MB);
        ImagePipelineConfig.Builder config = OkHttpImagePipelineConfigFactory.newBuilder(this, OkHttpUtils.getInstance());
        config.setMainDiskCacheConfig(diskCacheConfig.build());
        Fresco.initialize(this, config.build());
    }


    public static Context getApplicationCotext() {

        return mContext.getApplicationContext();

    }
}
