package jp.gr.uchiwa.blackout.service;

import java.util.HashMap;
import java.util.Map;

import jp.gr.uchiwa.blackout.service.Db.Bukken;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BukkenEditService {

	private final Context context;

	public BukkenEditService(final Context pContext) {
		this.context = pContext;
	}

	public void editBukken(final Map<String, String> pData) {
		final Db db = new Db(this.context);
		final SQLiteDatabase sqlite = db.getDatabase();
		final int no = Integer.parseInt(pData.get(Bukken.COL_NO.getName()));
		final ContentValues values = convertMapToContentValues(pData);
		try {
			sqlite.beginTransaction();
			if (no != 0) {
				sqlite.update
					(
						Bukken.TABLE_NAME,
						values,
						Bukken.COL_NO.getName().concat(" = ?"),
						new String[]{ Integer.toString(no) }
					);
			} else {
				values.put(Bukken.COL_NO.getName(), getNextNo(sqlite));
				sqlite.insert
					(
						Bukken.TABLE_NAME,
						null,
						values
					);
			}
			sqlite.setTransactionSuccessful();
		} finally {
			sqlite.endTransaction();
			db.close();
		}
	}

	private ContentValues convertMapToContentValues(Map<String, String> pData) {
		final ContentValues values = new ContentValues();
		values.put(Bukken.COL_NO.getName(),             pData.get(Bukken.COL_NO.getName()));
		values.put(Bukken.COL_BUKKEN_NAME.getName(),    pData.get(Bukken.COL_BUKKEN_NAME.getName()));
		values.put(Bukken.COL_SUB_GROUP_NAME.getName(), pData.get(Bukken.COL_SUB_GROUP_NAME.getName()));
		values.put(Bukken.COL_URGENT_CONTACT.getName(), pData.get(Bukken.COL_URGENT_CONTACT.getName()));
		values.put(Bukken.COL_CHARGE_NAME.getName(),    pData.get(Bukken.COL_CHARGE_NAME.getName()));
		values.put(Bukken.COL_REMARKS.getName(),        pData.get(Bukken.COL_REMARKS.getName()));
		return values;
	}

	public Map<String, String> getBukken(final int pNo) {
		final Db db = new Db(this.context);
		final SQLiteDatabase sqlite = db.getDatabase();
		Map<String, String> data = null;
		try {
			sqlite.beginTransaction();
			final Cursor cursor = sqlite.query
				(
					Bukken.TABLE_NAME,
					new String[]{
							Bukken.COL_NO.getName(),
							Bukken.COL_BUKKEN_NAME.getName(),
							Bukken.COL_SUB_GROUP_NAME.getName(),
							Bukken.COL_URGENT_CONTACT.getName(),
							Bukken.COL_CHARGE_NAME.getName()
					},
					Bukken.COL_NO.getName().concat(" = ?"),
					new String[]{ Integer.toString(pNo) },
					null,
					null,
					null
				);
			data = convertCursorToMap(pNo, cursor);
			cursor.close();
			sqlite.setTransactionSuccessful();
		} finally {
			sqlite.endTransaction();
			db.close();
		}
		return data;
	}

	private int getNextNo(final SQLiteDatabase sqlite) {
		int nextNo = 1;
		final Cursor cursor = sqlite.query
			(
				Bukken.TABLE_NAME,
				new String[]{ Bukken.COL_NO.getName() },
				null,
				null,
				null,
				null,
				Bukken.COL_NO.getName().concat(" desc"),
				"1"
			);
		cursor.moveToFirst();
		if (cursor.getCount() != 0) {
			nextNo = cursor.getInt(0) + 1;
		}
		return nextNo;
	}

	private Map<String, String> convertCursorToMap(int pNo, Cursor pCursor) {
		pCursor.moveToFirst();
		final Map<String, String> data = new HashMap<String, String>();
		final int cnt = pCursor.getCount();
		data.put(Bukken.COL_NO.getName(), Integer.toString(pNo));
		if (cnt > 0) {
			for (int i = 0; i < pCursor.getColumnCount(); i++) {
				data.put(pCursor.getColumnName(i), pCursor.getString(i));
			}
		}
		return data;
	}
}
