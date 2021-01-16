package com.jkkg.hhtx.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import com.jkkg.hhtx.BuildConfig;
import com.jkkg.hhtx.app.MyApp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.util.Log.INFO;

public class CrashTree  extends Timber.Tree {

    /**
     * 是否记录log
     * @param tag tag
     * @param priority 级别
     * @return true 往下走log，否则不走
     */
    @Override
    protected boolean isLoggable(@Nullable String tag, int priority) {
        return priority >= INFO;
    }

    /**
     * 自己处理对应的日志信息
     * @param priority 级别
     * @param tag tag
     * @param message message
     * @param t 错误信息
     */
    @Override
    protected void log(int priority, @Nullable final String tag, @NotNull final String message, @Nullable final Throwable t) {
        /* 如果日志界别是v或d就不做任何处理 */
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return;
        }
        if (BuildConfig.TEST) {
            logcat(tag,message,t);
        }
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter){
                try {
                    saveLogcat(tag,message,t);
                    emitter.onNext(true);
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }})
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                    }
                });

    }

    /**
     * 打印日志
     */
    @SuppressLint("LogNotTimber")
    private void logcat(@Nullable String tag, @NotNull String message, @Nullable Throwable t) {
        if (tag==null) {
            if (t==null) {
                Log.e("TAG", message);
            } else {
                Log.e("TAG", message ,t );
            }
        } else {
            if (t==null) {
                Log.e(tag, message);
            } else {
                Log.e(tag, message ,t );
            }
        }
    }

    /**
     * 保存日志
     */
    @SuppressLint("LogNotTimber")
    private void saveLogcat(@Nullable String tag, @NotNull String message, @Nullable Throwable t) throws Exception {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sb.append(sdf.format(new Date())).append("  日志：");
        sb.append("[").append(tag).append("] {").append(message).append("}\n");
        if (t!=null) {
            Writer writer = new StringWriter();
            PrintWriter pw = new PrintWriter(writer);
            t.printStackTrace(pw);

            Throwable cause = t.getCause();
            while (cause != null) {
                cause.printStackTrace(pw);
                cause = cause.getCause();
            }
            pw.close();
            String result = writer.toString();
            sb.append("错误信息：").append("\n").append(result);
        }

        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + MyApp.getApp().getPackageName() + File.separator + "log");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        SimpleDateFormat fileFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        FileOutputStream fos = new FileOutputStream(new File(dir,fileFormat.format(new Date())),true);
        fos.write(sb.toString().getBytes());
        fos.flush();
        fos.close();
    }
}
