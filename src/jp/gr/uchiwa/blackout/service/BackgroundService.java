package jp.gr.uchiwa.blackout.service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author jabaraster
 * 
 */
public final class BackgroundService {

    private static ExecutorService _worker;

    private BackgroundService() {
        // 処理なし
    }

    /**
     * @param pWorker
     */
    public static void setWorker(final ExecutorService pWorker) {
        _worker = pWorker;
    }

    /**
     * @param <V>
     * @param pTask
     * @return -
     */
    public static <V> Future<V> submit(final Callable<V> pTask) {
        return _worker.submit(pTask);
    }
}
