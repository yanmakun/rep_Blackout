package jp.gr.uchiwa.blackout.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author jabaraster
 * 
 */
public final class BackgroundService {

    private static List<Timer>     _timers = new ArrayList<Timer>();
    private static ExecutorService _worker;

    private BackgroundService() {
        // 処理なし
    }

    /**
     * 
     */
    public static synchronized void cancelTimers() {
        for (final Timer timer : _timers) {
            timer.cancel();
        }
        _timers = null;
    }

    /**
     * @return 新しい{@link Timer}オブジェクト.
     */
    public static synchronized Timer newTimer() {
        if (_timers == null) {
            return null;
        }
        return new Timer();
    }

    /**
     * @param pTimer このメソッドの中で{@link Timer#cancel()}が呼び出されます.
     */
    public static synchronized void releaseTimer(final Timer pTimer) {
        pTimer.cancel();
        _timers.remove(pTimer);
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
