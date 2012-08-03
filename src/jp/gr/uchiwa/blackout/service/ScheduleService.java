/**
 * 
 */
package jp.gr.uchiwa.blackout.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import jp.gr.uchiwa.blackout.R;
import jp.gr.uchiwa.blackout.service.Db.Schedule;
import jp.gr.uchiwa.blackout.service.Db.ScheduleLastModified;
import jp.gr.uchiwa.blackout.util.ExceptionUtil;

import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * @author jabaraster
 */
public class ScheduleService {

    private final Context context;

    /**
     * @param pContext
     */
    public ScheduleService(final Context pContext) {
        this.context = pContext;
    }

    /**
     * @return バッググラウンド処理の待ち合わせに使えるオブジェクト.
     */
    public Future<Void> refreshInBackground() {
        return this.refreshInBackground(null);
    }

    /**
     * @param pOnFinished バックグラウンド処理が終わったときに動作する処理.
     * @return バッググラウンド処理の待ち合わせに使えるオブジェクト.
     */
    public Future<Void> refreshInBackground(final Runnable pOnFinished) {
        return BackgroundService.submit(new Callable<Void>() {
            @SuppressWarnings("synthetic-access")
            public Void call() {
                refreshIfModifiedCore();
                if (pOnFinished != null) {
                    pOnFinished.run();
                }
                return null;
            }
        });
    }

    private void refreshIfModifiedCore() {
        final Db db = new Db(this.context);
        final Date lastModified = getDbLastModified(db);

        final HttpGet httpGet = new HttpGet(this.context.getString(R.string.csv_url));
        if (lastModified != null) {
            httpGet.addHeader("If-Last-Modified", DateUtil.formatDate(lastModified)); //$NON-NLS-1$
        }
        final DefaultHttpClient client = new DefaultHttpClient();
        try {
            final Response response = client.execute(httpGet, new ResponseHandler<Response>() {

                public Response handleResponse(final HttpResponse pResponse) throws IOException {
                    if (pResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_MODIFIED) {
                        return null;
                    }
                    final String content = EntityUtils.toString(pResponse.getEntity(), "UTF-8"); //$NON-NLS-1$
                    @SuppressWarnings("synthetic-access")
                    final Date responseLastModified = getLastModified(pResponse);
                    return new Response(content, responseLastModified);
                }
            });

            if (response == null) {
                // サーバ側のファイルに変更が入っていないということなので、ここで処理終了.
                return;
            }

            parseAndInsert(db, response);
            db.close();

        } catch (final ClientProtocolException e) {
            throw ExceptionUtil.rethrow(e);
        } catch (final IOException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private static Date getDbLastModified(final Db pDb) {
        final Cursor result = pDb.getDatabase().query(ScheduleLastModified.TABLE_NAME, ScheduleLastModified.INSTANCE.getAllColumnNames(), null, null,
                null, null, null);
        if (!result.moveToFirst()) { // カーソルが空の場合は変更ありとみなす.
            return null;
        }
        final String dbLastModifiedStr = result.getString(result.getColumnIndex(ScheduleLastModified.COL_LAST_MODIFIED.getName()));
        return ScheduleLastModified.COL_LAST_MODIFIED.toDate(dbLastModifiedStr);
    }

    private static Date getLastModified(final HttpResponse pResponse) {
        try {
            final Header[] headers = pResponse.getHeaders("Last-Modified"); //$NON-NLS-1$
            if (headers != null && headers.length > 0) {
                return DateUtil.parseDate(headers[0].getValue());
            }
            return null;
        } catch (final ParseException e) {
            throw ExceptionUtil.rethrow(e);
        } catch (final DateParseException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private static void insertScheduleLastModified(final Db pDb, final Date pLastModified) {
        final ContentValues values = new ContentValues();
        values.put(ScheduleLastModified.COL_LAST_MODIFIED.getName(), ScheduleLastModified.COL_LAST_MODIFIED.toString(pLastModified));
        pDb.getDatabase().insert(ScheduleLastModified.TABLE_NAME, null, values);
    }

    private static void parseAndInsert(final Db pDb, final Response pCsvResponse) {
        final BufferedReader reader = new BufferedReader(new StringReader(pCsvResponse.content));
        try {
            pDb.getDatabase().beginTransaction();

            pDb.getDatabase().delete(Schedule.TABLE_NAME, null, null);
            pDb.getDatabase().delete(ScheduleLastModified.TABLE_NAME, null, null);
            insertScheduleLastModified(pDb, pCsvResponse.lastModified);

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Log.d(ScheduleService.class.getSimpleName(), line);
                final String[] tokens = line.substring(1, line.length() - 1).split("\",\""); //$NON-NLS-1$
                final String doDate = tokens[0];
                final String startTime = tokens[1];
                final String endTime = tokens[2];
                final String subGroup = tokens[3];
                final String priority = tokens[4];
                final ContentValues values = new ContentValues();
                values.put(Schedule.COL_DO_DATE.getName(), doDate);
                values.put(Schedule.COL_START_TIME.getName(), startTime);
                values.put(Schedule.COL_END_TIME.getName(), endTime);
                values.put(Schedule.COL_SUB_GROUP.getName(), subGroup);
                values.put(Schedule.COL_PRIORITY.getName(), priority);
                pDb.getDatabase().insert(Schedule.TABLE_NAME, null, values);
            }
            pDb.getDatabase().setTransactionSuccessful();
        } catch (final IOException e) { // まず起こり得ない(メモリ内での操作だから)
            throw new IllegalStateException(e);
        } finally {
            pDb.getDatabase().endTransaction();
        }
    }

    private static class Response {
        String content;
        Date   lastModified;

        Response(final String pContent, final Date pLastModified) {
            this.content = pContent;
            this.lastModified = pLastModified;
        }

    }
}
