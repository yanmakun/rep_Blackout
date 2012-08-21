/**
 * 
 */
package jp.gr.uchiwa.blackout.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author jabaraster
 */
public class Db {

    private static final String FORMAT_DATETIME  = "yyyyMMddHHmmss"; //$NON-NLS-1$

    private static final String DATABASE_NAME    = "blackout.db";   //$NON-NLS-1$
    private static final int    DATABASE_VERSION = 1;

    private final Support       support;

    /**
     * @param pContext
     */
    public Db(final Context pContext) {
        this.support = new Support(pContext);
    }

    /**
     * 
     */
    public void close() {
        this.support.close();
    }

    /**
     * @return -
     */
    public SQLiteDatabase getDatabase() {
        return this.support.getWritableDatabase();
    }

    /**
     * @param pCursor
     * @param pColumn
     * @return int値.
     */
    public static int getInteger(final Cursor pCursor, final ColumnDefinition pColumn) {
        return pCursor.getInt(pCursor.getColumnIndex(pColumn.getName()));
    }

    /**
     * @param pCursor
     * @param pColumn
     * @return 文字列値.
     */
    public static String getString(final Cursor pCursor, final ColumnDefinition pColumn) {
        return pCursor.getString(pCursor.getColumnIndex(pColumn.getName()));
    }

    /**
     * @param pDatetimeString
     * @return -
     */
    public static Date parseDatetimeString(final String pDatetimeString) {
        try {
            return new SimpleDateFormat(FORMAT_DATETIME).parse(pDatetimeString);
        } catch (final ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @param pCursor
     * @return 拡張for文でカーソルを扱えるようにするためのオブジェクト.
     */
    public static Iterable<Cursor> toIterable(final Cursor pCursor) {
        if (pCursor.getCount() == 0) {
            return Collections.emptyList();
        }

        return new Iterable<Cursor>() {

            public Iterator<Cursor> iterator() {

                return new Iterator<Cursor>() {

                    public boolean hasNext() {
                        return pCursor.moveToNext();
                    }

                    public Cursor next() {
                        return pCursor;
                    }

                    public void remove() {
                        throw new UnsupportedOperationException("このメソッドはサポートしていないため、呼び出さないで下さい."); //$NON-NLS-1$
                    }
                };
            }
        };
    }

    /**
     * @author jabaraster
     */
    public final static class Bukken extends TableDefinition {
        private static final long            serialVersionUID   = 3475137677495687752L;

        /**
         * 
         */
        public static final String           TABLE_NAME         = "m_bukken";                             //$NON-NLS-1$

        /**
         * 
         */
        public static final ColumnDefinition COL_NO             = ColumnDefinition.primaryKey("no");      //$NON-NLS-1$
        /**
         * 
         */
        public static final ColumnDefinition COL_BUKKEN_NAME    = ColumnDefinition.text("bukken_name");   //$NON-NLS-1$
        /**
         * 
         */
        public static final ColumnDefinition COL_SUB_GROUP_NAME = ColumnDefinition.text("sub_group_name"); //$NON-NLS-1$
        /**
         * 
         */
        public static final ColumnDefinition COL_URGENT_CONTACT = ColumnDefinition.text("urgent_contact"); //$NON-NLS-1$
        /**
         * 
         */
        public static final ColumnDefinition COL_CHARGE_NAME    = ColumnDefinition.text("charge_text");   //$NON-NLS-1$
        /**
         * 
         */
        public static final ColumnDefinition COL_REMARKS        = ColumnDefinition.text("remarks");       //$NON-NLS-1$

        /**
         * この変数は必ず他のstatic変数より後で宣言すること！
         */
        public static final Bukken           INSTANCE           = new Bukken();

        private Bukken() {
            super(TABLE_NAME, COL_NO, COL_BUKKEN_NAME, COL_SUB_GROUP_NAME, COL_URGENT_CONTACT, COL_CHARGE_NAME, COL_REMARKS);
        }
    }

    /**
     * @author jabaraster
     */
    public final static class Schedule extends TableDefinition {
        private static final long                serialVersionUID = 3929585428899297217L;

        /**
         * 
         */
        public static final String               TABLE_NAME       = "t_schedule";                        //$NON-NLS-1$
        /**
         * 
         */
        public static final DateColumnDefinition COL_DO_DATE      = ColumnDefinition.date("do_date");    //$NON-NLS-1$ 
        /**
         * 
         */
        public static final DateColumnDefinition COL_START_TIME   = ColumnDefinition.time("start_time"); //$NON-NLS-1$
        /**
         * 
         */
        public static final DateColumnDefinition COL_END_TIME     = ColumnDefinition.time("end_time");   //$NON-NLS-1$
        /**
         * 
         */
        public static final ColumnDefinition     COL_SUB_GROUP    = ColumnDefinition.text("sub_group");  //$NON-NLS-1$
        /**
         * 
         */
        public static final ColumnDefinition     COL_PRIORITY     = ColumnDefinition.integer("priority"); //$NON-NLS-1$

        /**
         * この変数は必ず他のstatic変数より後で宣言すること！
         */
        public static final Schedule             INSTANCE         = new Schedule();

        private Schedule() {
            super(TABLE_NAME, COL_DO_DATE, COL_START_TIME, COL_END_TIME, COL_SUB_GROUP, COL_PRIORITY);
        }
    }

    /**
     * @author jabaraster
     * 
     */
    public final static class ScheduleLastModified extends TableDefinition {
        private static final long                serialVersionUID  = 5361907332618948512L;

        /**
         * 
         */
        public static final String               TABLE_NAME        = "t_schedule_last_modified";                //$NON-NLS-1$

        /**
         * 
         */
        public static final DateColumnDefinition COL_LAST_MODIFIED = ColumnDefinition.datetime("last_modified"); //$NON-NLS-1$ // yyyyMMddHHmmss

        /**
         * この変数は必ず他のstatic変数より後で宣言すること！
         */
        public static final ScheduleLastModified INSTANCE          = new ScheduleLastModified();

        private ScheduleLastModified() {
            super(TABLE_NAME, COL_LAST_MODIFIED);
        }
    }

    private static class Support extends SQLiteOpenHelper {

        /**
         * @param pContext
         */
        public Support(final Context pContext) {
            super(pContext, DATABASE_NAME, null, Db.DATABASE_VERSION);
        }

        /**
         * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
         */
        @Override
        public void onCreate(final SQLiteDatabase pDb) {
            pDb.execSQL(Bukken.INSTANCE.getTableDefinitionSql());
            pDb.execSQL(ScheduleLastModified.INSTANCE.getTableDefinitionSql());
            pDb.execSQL(Schedule.INSTANCE.getTableDefinitionSql());
        }

        /**
         * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
         */
        @Override
        public void onUpgrade( //
                @SuppressWarnings("unused") final SQLiteDatabase pDb //
                , @SuppressWarnings("unused") final int pOldVersion //
                , @SuppressWarnings("unused") final int pNewVersion) {
            // TODO Auto-generated method stub

        }
    }
}
