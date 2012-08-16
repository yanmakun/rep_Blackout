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

	public void editBukken(final int pNo, final ContentValues values) {
		final Db db = new Db(this.context);
		final SQLiteDatabase sqlite = db.getDatabase();
		try {
			sqlite.beginTransaction();
			if (pNo != 0) {
				sqlite.update
					(
						Bukken.TABLE_NAME,
						values,
						Bukken.COL_NO.getName().concat(" = ?"),
						new String[]{ Integer.toString(pNo) }
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
		} catch (Exception e) {
			// どうしよう・・・。
		} finally {
			sqlite.endTransaction();
			db.close();
		}
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
							Bukken.COL_CHARGE_NAME.getName(),
							Bukken.COL_REMARKS.getName()
					},
					Bukken.COL_NO.getName().concat(" = ?"),
					new String[]{ Integer.toString(pNo) },
					null,
					null,
					null
				);
			data = convertCursorToMap(cursor);
			cursor.close();
			sqlite.setTransactionSuccessful();
		} catch (Exception e) {
			// どうしよう・・・。
		} finally {
			sqlite.endTransaction();
			db.close();
		}
		return data;
	}

	private int getNextNo(final SQLiteDatabase sqlite) {
		final Cursor cursor = sqlite.query
			(
				Bukken.TABLE_NAME,
				new String[]{ Bukken.COL_NO.getName() },
				null,
				null,
				null,
				Bukken.COL_NO.getName().concat(" desc"),
				"1"
			);
		cursor.moveToFirst();
		return cursor.getInt(0) + 1;
	}

	private Map<String, String> convertCursorToMap(Cursor pCursor) {
		pCursor.moveToFirst();
		final Map<String, String> data = new HashMap<String, String>();
		for (int i = 0; i < pCursor.getColumnCount() - 1; i++) {
			data.put(pCursor.getColumnName(i), pCursor.getString(i));
		}
		return data;
	}
}
