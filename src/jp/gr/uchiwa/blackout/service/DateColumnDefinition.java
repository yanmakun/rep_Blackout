package jp.gr.uchiwa.blackout.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.gr.uchiwa.blackout.util.ExceptionUtil;
import android.database.Cursor;

/**
 * @author jabaraster
 * 
 */
public class DateColumnDefinition extends ColumnDefinition {
    private static final long serialVersionUID = -6252321064033332198L;

    private final String      format;

    /**
     * @param pName
     * @param pNullable
     * @param pFormat
     */
    public DateColumnDefinition(final String pName, final boolean pNullable, final String pFormat) {
        super(pName, ColumnType.TEXT, false, pNullable);
        this.format = pFormat;
    }

    /**
     * @param pCursor
     * @return -
     */
    public Date getDate(final Cursor pCursor) {
        final String s = pCursor.getString(pCursor.getColumnIndex(getName()));
        if (s == null) {
            return null;
        }
        return toDateCore(s);
    }

    /**
     * @param pStringValue
     * @return 日付型の値.
     */
    public Date toDate(final String pStringValue) {
        return toDateCore(pStringValue);
    }

    /**
     * @param pDateValue
     * @return 文字列値.
     */
    public String toString(final Date pDateValue) {
        return new SimpleDateFormat(this.format).format(pDateValue);
    }

    private Date toDateCore(final String pStringValue) {
        try {
            return new SimpleDateFormat(this.format).parse(pStringValue);
        } catch (final ParseException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }
}