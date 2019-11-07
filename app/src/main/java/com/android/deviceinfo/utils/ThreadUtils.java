package com.android.deviceinfo.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

/**
 */

public class ThreadUtils {

    /**
     * 数据源数据加载、网络请求等操作使用此线程池
     */
    private static ExecutorService THREAD_POOL;

    /**
     * 主线程handler，用于线程切换
     */
    private static final Handler MAIN_LOOPER_HANDLER = new Handler(Looper.getMainLooper());

    static {
        //cpu核心数量
        int cpuCount = Runtime.getRuntime().availableProcessors();
        //线程池核心线程数量，最小5，最大cpu核心数量
        int corePoolSize = Math.max(8, cpuCount);
        //最大线程池数量
        int maximumPoolSize = Integer.MAX_VALUE;
        //非核心线程缓存时间，秒
        int keepAliveSeconds = 60;

        ThreadFactory sThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r, "VGEDITOR #" + mCount.getAndIncrement());
            }
        };

        final BlockingQueue<Runnable> sPoolWorkQueue =
                new SynchronousQueue<>();

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize, keepAliveSeconds, TimeUnit.SECONDS,
                sPoolWorkQueue, sThreadFactory);
        //不允许核心线程被回收
        threadPool.allowCoreThreadTimeOut(false);
        THREAD_POOL = threadPool;
    }

    private ThreadUtils() {
        throw new AssertionError("不要实例化我！");
    }

    /**
     * 使用线程池execute
     * @param runnable 需要执行的runnable
     */
    @MainThread
    public static void exec(@NonNull Runnable runnable) {
        THREAD_POOL.execute(runnable);
    }

    /**
     * 使用线程池submit
     * @param runnable 需要执行的runnable
     */
    @MainThread
    public static void submit(@NonNull Runnable runnable) {
        THREAD_POOL.submit(runnable);
    }

    /**
     * 在主线程执行
     * @param runnable 需要执行的runnable
     */
    @WorkerThread
    public static void post(@NonNull Runnable runnable) {
        MAIN_LOOPER_HANDLER.postAtFrontOfQueue(runnable);
    }

    /**
     * 当前是否在主线程
     * @return boolean
     */
    public static boolean isMainThread() {
        Looper mainLooper = Looper.getMainLooper();
        Looper myLooper = Looper.myLooper();
        return mainLooper == myLooper;
    }
}
