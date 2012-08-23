/**
 * 
 */
package jp.gr.uchiwa.blackout.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Application;

/**
 * @author jabaraster
 * 
 */
public class BlackoutApplication extends Application {

    ExecutorService worker;

    /**
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();
        setupWorker();
        setupExceptionOperation();
    }

    /**
     * @see android.app.Application#onTerminate()
     */
    @Override
    public void onTerminate() {
        if (this.worker != null) {
            this.worker.shutdownNow();
        }
        BackgroundService.cancelTimers();
        super.onTerminate();
    }

    private File getErrorLogFile() {
        return getFileStreamPath("error.txt"); //$NON-NLS-1$
    }

    @SuppressWarnings("nls")
    private synchronized void saveException(final Thread pThread, final Throwable pException) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(getErrorLogFile());
            final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

            writer.write("Exception Info in [");
            writer.write(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(Calendar.getInstance().getTime()));
            writer.write("]");
            writer.newLine();

            writer.write("ThreadName: ");
            writer.write(pThread.getName());
            writer.newLine();

            pException.printStackTrace(new PrintWriter(writer));
            writer.newLine();

            writer.flush();

        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    //
                }
            }
        }
    }

    private void setupExceptionOperation() {
        final UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @SuppressWarnings("synthetic-access")
            public void uncaughtException(final Thread pThread, final Throwable pEx) {
                saveException(pThread, pEx);
                defaultHandler.uncaughtException(pThread, pEx);
            }
        });
    }

    private void setupWorker() {
        this.worker = Executors.newFixedThreadPool(3);
        BackgroundService.setWorker(this.worker);
    }
}
